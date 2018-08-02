package pt.evolute.dbtransfer.transfer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.Properties;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import pt.evolute.dbtransfer.db.DBConnection;
import pt.evolute.dbtransfer.db.DBConnector;
import pt.evolute.dbtransfer.db.beans.ConnectionDefinitionBean;
import pt.evolute.dbtransfer.db.beans.Name;

public class MoverTest {

	private static ConnectionDefinitionBean SRC;
	private static ConnectionDefinitionBean DEST;
	private static DBConnection CON_DEST;
	private static DBConnection CON_SRC;

	private Properties props;
	private Mover mover;

	
	
	@BeforeClass
	public static void beforeClass() throws Exception {

		SRC = new ConnectionDefinitionBean("jdbc:h2:mem:test1", "test1", "test2", "");

		DEST = new ConnectionDefinitionBean("jdbc:h2:mem:test2", "test1", "test2", "");

		CON_DEST = DBConnector.getConnection(DEST, true);

		CON_SRC = DBConnector.getConnection(SRC, true);

		intDB(CON_SRC);
		intDB(CON_DEST);
	}

	private static void intDB(DBConnection con) throws Exception {

		con.executeUpdate("CREATE TABLE TAB1 ( ID NUMERIC(10,0) PRIMARY KEY, VALUE VARCHAR(30))");
		con.executeUpdate("CREATE TABLE TAB2 ( ID NUMERIC(10,0) PRIMARY KEY, VALUE VARCHAR(30))");
		con.executeUpdate("CREATE TABLE TAB3 ( ID NUMERIC(10,0) PRIMARY KEY, VALUE VARCHAR(30))");

	}

	@Before
	public void setup() throws SQLException {
		props = new Properties();
		intDeleteAll(CON_SRC);
		intDeleteAll(CON_DEST);
		
	}

	private void intDeleteAll(DBConnection con) throws SQLException {
		
		con.executeUpdate("DELETE FROM TAB1");
		con.executeUpdate("DELETE FROM TAB2");
		con.executeUpdate("DELETE FROM TAB3");
		
	}

	@Test
	public void simple() throws Exception {

		CON_SRC.executeUpdate("INSERT INTO TAB1 VALUES (1, 'XXX')");
		CON_SRC.executeUpdate("INSERT INTO TAB1 VALUES (2, 'XXX')");
		CON_SRC.executeUpdate("INSERT INTO TAB1 VALUES (3, 'XXX')");
		CON_SRC.executeUpdate("INSERT INTO TAB2 VALUES (4, 'XXX')");

		mover = new Mover(props, SRC, DEST);

		mover.moveDB();

		assertThat(CON_DEST.getRowCount(new Name("TAB1")), equalTo(3));
		assertThat(CON_DEST.getRowCount(new Name("TAB2")), equalTo(1));
		assertThat(CON_DEST.getRowCount(new Name("TAB3")), equalTo(0));

	}
	
	@Test
	public void ignorePkErrorLessRows() throws Exception {

		CON_SRC.executeUpdate("INSERT INTO TAB1 VALUES (1, 'XXX')");
		CON_SRC.executeUpdate("INSERT INTO TAB1 VALUES (2, 'XXX')");
		CON_SRC.executeUpdate("INSERT INTO TAB2 VALUES (4, 'XXX')");
	
		CON_DEST.executeUpdate("INSERT INTO TAB1 VALUES (2, 'XXX')");

		mover = new Mover(props, SRC, DEST);

		mover.moveDB();

		assertThat(CON_DEST.getRowCount(new Name("TAB1")), equalTo(2));
		assertThat(CON_DEST.getRowCount(new Name("TAB2")), equalTo(1));

	}
	
	@Test
	public void ignorePkErrorMoreRows() throws Exception {

		CON_SRC.executeUpdate("INSERT INTO TAB1 VALUES (1, 'XXX')");
		CON_SRC.executeUpdate("INSERT INTO TAB1 VALUES (2, 'XXX')");
		CON_SRC.executeUpdate("INSERT INTO TAB2 VALUES (4, 'XXX')");
	
		CON_DEST.executeUpdate("INSERT INTO TAB1 VALUES (2, 'XXX')");
		CON_DEST.executeUpdate("INSERT INTO TAB1 VALUES (10, 'XXX')");

		mover = new Mover(props, SRC, DEST);

		mover.moveDB();

		assertThat(CON_DEST.getRowCount(new Name("TAB1")), equalTo(3));
		assertThat(CON_DEST.getRowCount(new Name("TAB2")), equalTo(1));

	}

}

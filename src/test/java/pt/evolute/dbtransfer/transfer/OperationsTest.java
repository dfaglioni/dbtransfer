package pt.evolute.dbtransfer.transfer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import br.loop.db.domain.Table;
import br.loop.db.domain.TableJoin;
import br.loop.db.domain.TableKeyMover;
import pt.evolute.dbtransfer.db.DBConnection;
import pt.evolute.dbtransfer.db.DBConnector;
import pt.evolute.dbtransfer.db.beans.ConnectionDefinitionBean;
import pt.evolute.dbtransfer.db.beans.Name;
import pt.evolute.dbtransfer.operations.TableJoinExecutor;
import pt.evolute.dbtransfer.operations.TableKeyMoverExecutor;

public class OperationsTest {

	private static ConnectionDefinitionBean SRC;
	private static ConnectionDefinitionBean DEST;
	private static DBConnection CON_DEST;
	private static DBConnection CON_SRC;

	private Properties props;
	private Mover mover;

	private TableKeyMoverExecutor tableKeyMoverExecutor = new TableKeyMoverExecutor();
	private TableJoinExecutor tableJoinExecutor = new TableJoinExecutor();

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

		con.executeUpdate("CREATE TABLE TAB_A ( ID NUMERIC(10,0) PRIMARY KEY, VALUE VARCHAR(30))");
		con.executeUpdate("CREATE TABLE TAB_B ( ID NUMERIC(10,0) PRIMARY KEY, ID_A NUMERIC(10,0), VALUE VARCHAR(30))");
		con.executeUpdate("CREATE TABLE TAB_C ( ID NUMERIC(10,0) PRIMARY KEY, ID_A NUMERIC(10,0), VALUE VARCHAR(30))");
		con.executeUpdate("CREATE TABLE TAB_D ( ID NUMERIC(10,0) PRIMARY KEY, ID_C NUMERIC(10,0), VALUE VARCHAR(30))");

		con.executeUpdate(
				"CREATE TABLE TAB_JOIN ( ID NUMERIC(10,0) PRIMARY KEY, VALUE VARCHAR(30), VALUE2 VARCHAR(30))");
		con.executeUpdate("CREATE TABLE TAB_JOIN_DEP ( ID NUMERIC(10,0) PRIMARY KEY, ID_TAB_JOIN NUMERIC(10,0))");

	}

	@Before
	public void setup() throws SQLException, IOException {
		
		FileUtils.writeStringToFile(new File(Mover.DONE_FILE), "emp", Charset.defaultCharset());
		
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
	@Ignore
	public void simpleChangeKey() throws Exception {

		CON_SRC.executeUpdate("INSERT INTO TAB1 VALUES (1, 'XXX')");
		
		mover = new Mover(props, SRC, DEST);

		mover.moveDB();
		
		assertThat("TAB1", CON_DEST.getRowCountWhere(new Name("TAB1"), " WHERE ID = 10"),
				equalTo(1));


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

	@Test
	public void moveKeySimple() throws Exception {

		CON_DEST.executeUpdate("INSERT INTO TAB_A VALUES (1, 'XXX')");
		CON_DEST.executeUpdate("INSERT INTO TAB_B VALUES (1, 1, 'XXX')");
		CON_DEST.executeUpdate("INSERT INTO TAB_C VALUES (2, 1, 'XXX')");
		CON_DEST.executeUpdate("INSERT INTO TAB_D VALUES (1, 2, 'XXX')");

		TableKeyMover tableAMover = new TableKeyMover();
		tableAMover.setSpace(10);
		tableAMover.getTable().setColumn("id");
		tableAMover.getTable().setTable("tab_a");

		TableKeyMover tableCMover = new TableKeyMover();
		tableCMover.setSpace(10);
		tableCMover.getTable().setColumn("id");
		tableCMover.getTable().setTable("tab_c");

	
		TableKeyMover tableNotExists = new TableKeyMover();
		tableNotExists.setSpace(10);
		tableNotExists.getTable().setColumn("id");
		tableNotExists.getTable().setTable("tab_x");

		Table table_b = new Table();
		table_b.setColumn("id_a");
		table_b.setTable("tab_b");

		Table table_c = new Table();
		table_c.setColumn("id_a");
		table_c.setTable("tab_c");

		Table table_notExists = new Table();
		table_notExists.setColumn("id_x");
		table_notExists.setTable("tab_x");

		
		tableAMover.getTable().getDependencies().addAll(Arrays.asList(table_b, table_c, table_notExists));

		tableKeyMoverExecutor.execute(CON_DEST, Arrays.asList(tableAMover, tableCMover, tableNotExists));

		assertThat("tab_a", CON_DEST.getRowCountWhere(new Name("TAB_A"), " WHERE ID = 1"), equalTo(0));
		assertThat("tab_b", CON_DEST.getRowCountWhere(new Name("TAB_B"), " WHERE ID_A = 1"), equalTo(0));
		assertThat("tab_c", CON_DEST.getRowCountWhere(new Name("TAB_C"), " WHERE ID_A = 1"), equalTo(0));

		assertThat("tab_c id", CON_DEST.getRowCountWhere(new Name("TAB_C"), " WHERE ID = 2"), equalTo(0));

	}

	@Test
	public void moveKeySimpleZeroSpace() throws Exception {

		TableKeyMover tableAMover = new TableKeyMover();
		tableAMover.setSpace(0);
		tableAMover.getTable().setColumn("id");
		tableAMover.getTable().setTable("tab_xxx");

		DBConnection dbConnection = Mockito.mock(DBConnection.class);
		Mockito.when(dbConnection.getTableList()).thenReturn(Arrays.asList(new Name(tableAMover.getTable().getTable())));

		tableKeyMoverExecutor.execute(dbConnection, Arrays.asList(tableAMover));
		
		
		Mockito.verify(dbConnection, Mockito.times(0)).executeUpdate(Mockito.anyString());

	}	
	@Test
	public void moveLessThenMax() throws Exception {

		CON_DEST.executeUpdate("INSERT INTO TAB_A VALUES (100, 'XXX')");
		CON_DEST.executeUpdate("INSERT INTO TAB_A VALUES (101, 'XXX')");
		CON_DEST.executeUpdate("INSERT INTO TAB_A VALUES (102, 'XXX')");

		TableKeyMover tableAMover = new TableKeyMover();
		tableAMover.setSpace(1);
		tableAMover.getTable().setColumn("id");
		tableAMover.getTable().setTable("tab_a");

		tableKeyMoverExecutor.execute(CON_DEST, Arrays.asList(tableAMover));
		
		assertThat("TAB_MOV", CON_DEST.getRowCountWhere(new Name("TAB_A"), " WHERE ID =  100"),
				equalTo(1));


	}

	@Test
	public void moveLessThenMaxIgnore() throws Exception {

		CON_DEST.executeUpdate("INSERT INTO TAB_A VALUES (1000000, 'XXX')");
		CON_DEST.executeUpdate("INSERT INTO TAB_A VALUES (1010000, 'XXX')");
		CON_DEST.executeUpdate("INSERT INTO TAB_A VALUES (1020000, 'XXX')");

		TableKeyMover tableAMover = new TableKeyMover();
		tableAMover.setSpace(1);
		tableAMover.setIgnoreMax(true);
		tableAMover.getTable().setColumn("id");
		tableAMover.getTable().setTable("tab_a");

		tableKeyMoverExecutor.execute(CON_DEST, Arrays.asList(tableAMover));
		
		assertThat("TAB_MOV", CON_DEST.getRowCountWhere(new Name("TAB_A"), " WHERE ID =  1000001"),
				equalTo(1));


	}
	
	@Test
	public void joinSimple() throws Exception {

		CON_DEST.executeUpdate("INSERT INTO TAB_JOIN VALUES (100, 'XXX','1')");
		CON_DEST.executeUpdate("INSERT INTO TAB_JOIN VALUES (101, 'XXX','1')");
		CON_DEST.executeUpdate("INSERT INTO TAB_JOIN VALUES (102, 'XXX','1')");
		CON_DEST.executeUpdate("INSERT INTO TAB_JOIN VALUES (103, 'XXX','2')");
		CON_DEST.executeUpdate("INSERT INTO TAB_JOIN VALUES (104, 'yyy','10')");
		CON_DEST.executeUpdate("INSERT INTO TAB_JOIN VALUES (105, 'yyy','10')");
		CON_DEST.executeUpdate("INSERT INTO TAB_JOIN_DEP VALUES (1000, 100)");
		CON_DEST.executeUpdate("INSERT INTO TAB_JOIN_DEP VALUES (1001, 101)");
		CON_DEST.executeUpdate("INSERT INTO TAB_JOIN_DEP VALUES (1002, 101)");
		CON_DEST.executeUpdate("INSERT INTO TAB_JOIN_DEP VALUES (1003, 102)");
				
		CON_DEST.executeUpdate("INSERT INTO TAB_JOIN_DEP VALUES (1004, 105)");
		

		TableJoin tableJoin = new TableJoin();

		tableJoin.getColumns().addAll(Arrays.asList("value", "value2"));

		tableJoin.getTable().setColumn("id");

		tableJoin.getTable().setTable("tab_join");
		
	    Table tableDep = new Table();
	    tableDep.setTable("TAB_JOIN_DEP");
	    tableDep.setColumn("ID_TAB_JOIN");
	    
		tableJoin.getTable().getDependencies().add(tableDep );

		tableJoinExecutor.execute(CON_DEST, Arrays.asList(tableJoin));

		assertThat("tab_join", CON_DEST.getRowCountWhere(new Name("TAB_JOIN"), " WHERE VALUE = 'XXX' AND VALUE2 = 1"),
				equalTo(1));

		assertThat("TAB_JOIN_DEP", CON_DEST.getRowCountWhere(new Name("TAB_JOIN_DEP"), " WHERE ID_TAB_JOIN = 100"),
				equalTo(4));

	}
}

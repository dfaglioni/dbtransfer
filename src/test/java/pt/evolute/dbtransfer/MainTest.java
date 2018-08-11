package pt.evolute.dbtransfer;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import pt.evolute.dbtransfer.operations.TableJoinExecutor;
import pt.evolute.dbtransfer.operations.TableKeyMoverExecutor;

import static org.mockito.Mockito.*;

public class MainTest {

	private Properties properties;

	private Main main;

	private TableKeyMoverExecutor tableKeyMoverExecutor;
	
	private TableJoinExecutor tableJoinExecutor;

	@Before
	public void setup() throws Exception {

		properties = new Properties();
	
		tableKeyMoverExecutor = mock(TableKeyMoverExecutor.class);

		tableJoinExecutor = mock(TableJoinExecutor.class);
		
		main = new Main();

		main.setTableKeyMoverExecutor(tableKeyMoverExecutor);
		
		main.setTableJoinExecutor(tableJoinExecutor);

	}

	@Test
	public void executeMover() throws Exception {

		configureDest();
		

		properties.put(Constants.MOVE_KEY, "true");
		properties.put(Constants.MOVE_KEY_FILE, "./examples/tablekeymover-1.json");
			
		
		main.execute(properties);
		
		verify(tableKeyMoverExecutor, times(1)).execute(any(), any());

	}
	
	@Test
	public void executeJoin() throws Exception {

		configureDest();
		

		properties.put(Constants.JOIN, "true");
		properties.put(Constants.JOIN_FILE, "./examples/tablejoin-1.json");
			
		
		main.execute(properties);
		
		verify(tableJoinExecutor, times(1)).execute(any(), any());

	}


	private void configureDest() {
		properties.put(Constants.URL_DB_DESTINATION, "jdbc:h2:mem:testxx");
		properties.put(Constants.USER_DB_DESTINATION, "");
		properties.put(Constants.PASSWORD_DB_DESTINATION, "");
	}

}

package pt.evolute.dbtransfer;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import pt.evolute.dbtransfer.keymover.TableKeyMoverExecutor;

public class MainTest {

	private Properties properties;

	private Main main;

	private TableKeyMoverExecutor tableKeyMoverExecutor;

	@Before
	public void setup() throws Exception {

		properties = new Properties();
	
		tableKeyMoverExecutor = mock(TableKeyMoverExecutor.class);

		main = new Main();

		main.setTableKeyMoverExecutor(tableKeyMoverExecutor);

	}

	@Test
	public void executeMover() throws Exception {

		properties.put(Constants.URL_DB_DESTINATION, "jdbc:h2:mem:testxx");
		properties.put(Constants.USER_DB_DESTINATION, "");
		properties.put(Constants.PASSWORD_DB_DESTINATION, "");
		

		properties.put(Constants.MOVE_KEY, "true");
		properties.put(Constants.MOVE_KEY_FILE, "./examples/tablekeymover-1.json");
			
		
		main.execute(properties);
		
		verify(tableKeyMoverExecutor, times(1)).execute(any(), any());

	}

}

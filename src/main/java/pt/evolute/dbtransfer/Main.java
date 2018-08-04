package pt.evolute.dbtransfer;

import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import br.loop.db.domain.TableKeyMover;
import br.loop.db.repository.TableRepository;
import pt.evolute.dbtransfer.db.DBConnector;
import pt.evolute.dbtransfer.db.beans.ConnectionDefinitionBean;
import pt.evolute.dbtransfer.db.helper.HelperManager;
import pt.evolute.dbtransfer.db.jdbc.JDBCConnection;
import pt.evolute.dbtransfer.keymover.TableKeyMoverExecutor;
import pt.evolute.dbtransfer.transfer.AsyncStatement;
import pt.evolute.dbtransfer.transfer.Mover;

/**
 *
 * @author lflores
 */
public class Main {

	private TableKeyMoverExecutor tableKeyMoverExecutor = new TableKeyMoverExecutor();

	private TableRepository tableRepository = new TableRepository();

	public void setTableKeyMoverExecutor(TableKeyMoverExecutor tableKeyMoverExecutor) {
		this.tableKeyMoverExecutor = tableKeyMoverExecutor;
	}

	public void execute(Properties props) throws Exception, SQLException {
		
		HelperManager.setProperties(props);
		System.out.println("BEGIN: " + new Date());
		long start = System.currentTimeMillis();

		ConnectionDefinitionBean srcBean = ConnectionDefinitionBean.loadBean(props, Constants.SOURCE_PROPS);
		ConnectionDefinitionBean dstBean = ConnectionDefinitionBean.loadBean(props, Constants.DESTINATION_PROPS);

		JDBCConnection.debug = "true".equalsIgnoreCase(props.getProperty(Constants.DEBUG));

		if ("true".equalsIgnoreCase(props.getProperty(Constants.MOVE_KEY))) {

			List<TableKeyMover> readListTableKeyMoverFromPathname = tableRepository.readListTableKeyMoverFromPathname(props.getProperty(Constants.MOVE_KEY_FILE, ""));

			System.out.println("Moving key using " + dstBean + " " + readListTableKeyMoverFromPathname );
			
			tableKeyMoverExecutor.execute(DBConnector.getConnection(dstBean, false),
					readListTableKeyMoverFromPathname);

		}

		if ("true".equalsIgnoreCase(props.getProperty(Constants.TRANSFER))) {
			String s = props.getProperty(Constants.TRANSFER_THREADS);
			try {
				int i = Integer.parseInt(s);
				AsyncStatement.PARALLEL_THREADS = i;
			} catch (Exception ex) {
			}
			System.out.println("Transfering");
			Mover m = new Mover(props, srcBean, dstBean);
			try {
				m.moveDB();
			} catch (SQLException ex) {
				ex.printStackTrace(System.out);
				ex.printStackTrace();
				// ErrorLogger.logException( ex );
				if (ex.getNextException() != null) {
					throw ex.getNextException();
				}
				throw ex;

			}
		}

		if ("true".equalsIgnoreCase(props.getProperty(Constants.JOIN))) {

			// TODO DISABLE POSTGRES CONSTRAINTS

			// PEGAR O ARQUIVO DEFINIDO COM A TABELAS QUE SERIAM JUNTADAS
			// DEFINIR A COLUNAS DE DISTINCT
			/*
			 * { table : bairro, columns : ['cidade','descricao'], pk_column : 'bairro' ,
			 * dependencies : [ {table : cep, column : bairro}, { table : enderecopessoa,
			 * column : bairro} ] }
			 * 
			 */
			// LIGAR CONSTRAINTS
		}

		System.out.println("END: " + new Date());
		System.out.println("Process took: " + (System.currentTimeMillis() - start) / 1000 + " seconds");
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Usage: " + Main.class.getName() + " <props.file>");
			System.exit(1);
		} else {
			try {
				System.out.println("Loading props: " + args[0]);
				Properties p = new Properties();
				p.load(new FileInputStream(args[0]));
				p.list(System.out);
				new Main().execute(p);
			} catch (Throwable th) {
				th.printStackTrace();
				th.printStackTrace(System.out);
				try {
					Thread.sleep(1500);
				} catch (InterruptedException ex) {
				}
				System.exit(2);
			}
			System.exit(0);
		}
	}

}

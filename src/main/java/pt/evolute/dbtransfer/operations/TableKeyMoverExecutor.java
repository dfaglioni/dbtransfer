package pt.evolute.dbtransfer.operations;

import java.sql.SQLException;
import java.util.List;

import br.loop.db.domain.Table;
import br.loop.db.domain.TableKeyMover;
import pt.evolute.dbtransfer.db.DBConnection;
import pt.evolute.dbtransfer.db.beans.Name;

public class TableKeyMoverExecutor {

	public void execute(DBConnection con_dest, List<TableKeyMover> listTableKeyMover) throws Exception {

		List<Name> listTables = con_dest.getTableList();

		for (TableKeyMover tableKeyMover : listTableKeyMover) {

			Name tableName = new Name(tableKeyMover.getTable().getTable());

			if (listTables.contains(tableName)) {

				int maxValue = con_dest.maxValue(tableName, tableKeyMover.getTable().getColumn());

				if (maxValue > tableKeyMover.getSpace()) {

					String message = "Space less than max " + tableKeyMover.getTable().getTable() + " " + maxValue;
					System.out.println(message);

				} else {

					updateToMove(con_dest, tableKeyMover.getTable(), tableKeyMover.getSpace());

					List<Table> dependencies = tableKeyMover.getTable().getDependencies();

					for (Table table : dependencies) {

						Name tableNameDep = new Name(table.getTable());

						if (listTables.contains(tableNameDep)) {

							updateToMove(con_dest, table, tableKeyMover.getSpace());

						}
					}
				}

			}
		}

	}

	private void updateToMove(DBConnection con_dest, Table table, int space) throws SQLException {
		System.out.println("Move " + table.getTable() + " " + space);
	
		try {
			con_dest.executeUpdate(String.format("update %s set %s = %s + %d", table.getTable(), table.getColumn(),
					table.getColumn(), space));
			
		} catch (Exception e) {

			System.out.println("Error Moving " + table + " " + e.getMessage());
		
		}
	}

}

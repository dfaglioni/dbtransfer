package pt.evolute.dbtransfer.operations;

import java.io.File;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.io.FileUtils;

import br.loop.db.domain.Table;
import br.loop.db.domain.TableKeyMover;
import pt.evolute.dbtransfer.db.DBConnection;
import pt.evolute.dbtransfer.db.beans.Name;
import pt.evolute.dbtransfer.transfer.Mover;

public class TableKeyMoverExecutor {

	public void execute(DBConnection con_dest, List<TableKeyMover> listTableKeyMover) throws Exception {

		listTableKeyMover.forEach(t -> t.validate());

		List<Name> listTables = con_dest.getTableList();

		for (TableKeyMover tableKeyMover : listTableKeyMover) {

			Name tableName = new Name(tableKeyMover.getTable().getTable());

			if (listTables.contains(tableName) && tableKeyMover.getSpace() != 0) {

				int maxValue = con_dest.maxValue(tableName, tableKeyMover.getTable().getColumn());

				if (maxValue > tableKeyMover.getSpace() && !tableKeyMover.getIgnoreMax()) {

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
					FileUtils.writeStringToFile(new File(Mover.DONE_FILE),
							tableKeyMover.getTable().getTable().toUpperCase() + '\n', Charset.defaultCharset(), true);

				}

			}
		}

	}

	private void updateToMove(DBConnection con_dest, Table table, int space) throws SQLException {
		System.out.println("Move " + table.getTable() + " " + space);

		try {
			con_dest.executeUpdate(String.format("update %s set %s = cast( %s as integer) + %d", table.getTable(),
					table.getColumn(), table.getColumn(), space));

		} catch (Exception e) {

			System.out.println("Error Moving " + table + " " + e.getMessage());

		}
	}

}

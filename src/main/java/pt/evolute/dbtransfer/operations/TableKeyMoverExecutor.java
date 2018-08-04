package pt.evolute.dbtransfer.operations;

import java.sql.SQLException;
import java.util.List;

import br.loop.db.domain.Table;
import br.loop.db.domain.TableKeyMover;
import pt.evolute.dbtransfer.db.DBConnection;
import pt.evolute.dbtransfer.db.beans.Name;

public class TableKeyMoverExecutor {

	public void execute(DBConnection con_dest, List<TableKeyMover> listTableKeyMover) throws Exception {

		for (TableKeyMover tableKeyMover : listTableKeyMover) {

			int maxValue = con_dest.maxValue(new Name(tableKeyMover.getTable().getTable()),
					tableKeyMover.getTable().getColumn());

			if (maxValue > tableKeyMover.getSpace()) {

				String message = "Space less than max " + tableKeyMover.getTable() + " " + maxValue;
				System.out.println(message);
				throw new IllegalArgumentException(message);
			}

			updateToMove(con_dest, tableKeyMover.getTable(), tableKeyMover.getSpace());

			List<Table> dependencies = tableKeyMover.getTable().getDependencies();

			for (Table table : dependencies) {

				updateToMove(con_dest, table, tableKeyMover.getSpace());

			}

		}

	}

	private void updateToMove(DBConnection con_dest, Table table, int space) throws SQLException {
		System.out.println("Move " + table + " " + space);
		con_dest.executeUpdate(String.format("update %s set %s = %s + %d", table.getTable(), table.getColumn(),
				table.getColumn(), space));
	}

}

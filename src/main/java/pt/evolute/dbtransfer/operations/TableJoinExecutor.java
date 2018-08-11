package pt.evolute.dbtransfer.operations;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import br.loop.db.domain.Table;
import br.loop.db.domain.TableJoin;
import pt.evolute.dbtransfer.db.DBConnection;
import pt.evolute.utils.arrays.Virtual2DArray;

public class TableJoinExecutor {

	public void execute(DBConnection con_dest, List<TableJoin> listTableJoin) throws Exception {

		for (TableJoin tableJoin : listTableJoin) {

			String columns = tableJoin.getColumns().stream().reduce((acc, value) -> value + "," + acc).get();

			String selectRepeated = String.format("select %s from %s group by %s having count(*) > 1", columns,
					tableJoin.getTable().getTable(), columns);

			Virtual2DArray repeatedValues = con_dest.executeQuery(selectRepeated);

			if (repeatedValues.hasValues()) {

				int row = 0;

				do {

					List<Object> columnsValues = new ArrayList<>();

					for (int i = 0; i < tableJoin.getColumns().size(); i++) {

						columnsValues.add(repeatedValues.get(row, i));

					}
					removeDuplicationFor(tableJoin, columnsValues, con_dest);
					row++;

				} while (repeatedValues.hasValues());

			}

		}

	}

	private void removeDuplicationFor(TableJoin tableJoin, List<Object> columnsValues, DBConnection con_dest)
			throws Exception {

		System.out.println("removing duplication for " + tableJoin.getTable().getTable() + "  " + tableJoin.getColumns()
				+ " " + columnsValues);

		String where = tableJoin.getColumns().stream().map(column -> column + "  = ? ")
				.reduce((acc, value) -> value + " and " + acc).get();

		String selectKey = String.format("select %s from %s where %s order by %s", tableJoin.getTable().getColumn(),
				tableJoin.getTable().getTable(), where, tableJoin.getTable().getColumn());

		try (PreparedStatement pstmt = con_dest.getConnection().prepareStatement(selectKey);) {

			for (int i = 0; i < columnsValues.size(); i++) {

				pstmt.setObject(i + 1, columnsValues.get(i));

			}
			ResultSet rs = pstmt.executeQuery();

			Object key = null;

			while (rs.next()) {

				if (rs.isFirst()) {

					key = rs.getObject(tableJoin.getTable().getColumn());

				} else {

					Object keyWhere = rs.getObject(tableJoin.getTable().getColumn());

					for (Table tableDependency : tableJoin.getTable().getDependencies()) {

						updateDependency(tableDependency, key, keyWhere, con_dest);
					}

					deleteRow(tableJoin, keyWhere, con_dest);
				}

			}
		}

	}

	private void updateDependency(Table tableDependency, Object key, Object keyWhere, DBConnection con_dest)
			throws Exception {

		System.out.println("updade row from " + tableDependency.getTable() + " using " + key + " where " + keyWhere);

		String UpdateStatement = String.format("update %s set  %s  = ? where %s = ?", tableDependency.getTable(),
				tableDependency.getColumn(), tableDependency.getColumn());

		try (PreparedStatement pstmt = con_dest.getConnection().prepareStatement(UpdateStatement);) {

			pstmt.setObject(1, key);

			pstmt.setObject(2, keyWhere);

			pstmt.executeUpdate();

		}

	}

	private void deleteRow(TableJoin tableJoin, Object key, DBConnection con_dest) throws Exception {

		System.out.println("deleting row from " + tableJoin.getTable().getTable() + " using " + key);

		String deleteStatement = String.format("delete from %s where %s  = ?", tableJoin.getTable().getTable(),
				tableJoin.getTable().getColumn());

		try (PreparedStatement pstmt = con_dest.getConnection().prepareStatement(deleteStatement);) {

			pstmt.setObject(1, key);

			pstmt.executeUpdate();

		}

	}
}

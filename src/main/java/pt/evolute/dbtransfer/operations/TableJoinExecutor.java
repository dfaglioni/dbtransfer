package pt.evolute.dbtransfer.operations;

import java.util.List;

import br.loop.db.domain.TableJoin;
import pt.evolute.dbtransfer.db.DBConnection;
import pt.evolute.utils.arrays.Virtual2DArray;

public class TableJoinExecutor {

	public void execute(DBConnection con_dest, List<TableJoin> listTableJoin) throws Exception {

		for (TableJoin tableJoin : listTableJoin) {

			String columns = tableJoin.getColumns().stream().reduce((acc, value) -> value+ "," + acc).get();

			String selectRepeated = String.format("select %s from %s group by %s having count(*) > 1", columns,
					tableJoin.getTable().getTable(), columns);

			System.out.println("executed repeated " + selectRepeated);

			Virtual2DArray repeatedValues = con_dest.executeQuery(selectRepeated);

			if (repeatedValues.hasValues()) {

			    int row = 0;
				
				do {
					
					for (int i = 0; i < tableJoin.getColumns().size(); i++) {
						
						Object value = repeatedValues.get(row, i);
						System.out.println(value);
						
					}
					row++;
					

				} while (repeatedValues.hasValues());

			}

		}

	}
}

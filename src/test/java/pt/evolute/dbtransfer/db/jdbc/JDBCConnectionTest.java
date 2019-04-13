package pt.evolute.dbtransfer.db.jdbc;

import static org.junit.Assert.assertEquals;

import java.sql.Types;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import pt.evolute.dbtransfer.db.beans.ColumnDefinition;

public class JDBCConnectionTest {

	@Test
	public void sort() {

		ColumnDefinition col1 = new ColumnDefinition();
		col1.sqlType = 1;

		ColumnDefinition col2 = new ColumnDefinition();
		col2.sqlType = -8;

		ColumnDefinition col3 = new ColumnDefinition();
		col3.sqlType = Types.LONGVARBINARY;

		ColumnDefinition col4 = new ColumnDefinition();
		col4.sqlType = -5;

		
	
		List<ColumnDefinition> list = JDBCConnection.listSort(Arrays.asList(col1, col2, col3, col4));

		
		System.out.println(list);
		
		assertEquals(list.get(0).sqlType, Types.LONGVARBINARY);
		
		
	
	}

}

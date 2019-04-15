package pt.evolute.dbtransfer.db.jdbc;

import static org.junit.Assert.*;

import java.sql.Types;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import pt.evolute.dbtransfer.db.beans.ColumnDefinition;
import pt.evolute.dbtransfer.db.beans.Name;

public class JDBCConnectionTest {

	@Test
	public void sort() {

        LinkedList<ColumnDefinition> listOrigin = new LinkedList<ColumnDefinition>();

		ColumnDefinition col1 = new ColumnDefinition();
        col1.sqlType = 3;
        col1.name = new Name("col1");

		ColumnDefinition col2 = new ColumnDefinition();
        col2.sqlType = 3;
        col2.name = new Name("col2");

		ColumnDefinition col3 = new ColumnDefinition();
		col3.sqlType = Types.LONGVARBINARY;
        col3.name = new Name("col3");

		ColumnDefinition col4 = new ColumnDefinition();
        col4.sqlType = 18;
        col4.name = new Name("col4");

        ColumnDefinition col5 = new ColumnDefinition();
        col5.sqlType = Types.LONGVARBINARY;
        col5.name = new Name("col5");

        listOrigin.add(col1);
        listOrigin.add(col2);
        listOrigin.add(col3);
        listOrigin.add(col4);
        listOrigin.add(col5);

        List<ColumnDefinition> list = Collections.unmodifiableList(JDBCConnection.listSort(listOrigin));


		System.out.println(list);
		
		assertEquals(list.get(0).sqlType, Types.LONGVARBINARY);
        assertEquals(list.get(1).sqlType, Types.LONGVARBINARY);
		
	
	}

}

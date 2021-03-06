package pt.evolute.dbtransfer.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import pt.evolute.dbtransfer.db.beans.ColumnDefinition;
import pt.evolute.dbtransfer.db.beans.Name;
import pt.evolute.dbtransfer.db.helper.Helper;
import pt.evolute.utils.arrays.Virtual2DArray;

/**
 *
 * @author lflores
 */
public interface DBConnection
{
	public List<Name> getTableList()
			throws Exception;

	public List<Name> getTableList(boolean ignoreDone)
			throws Exception;

	public List<ColumnDefinition> getColumnList(Name table)
			throws Exception;
	
	public Virtual2DArray getFullTable(Name table)
			throws Exception;

	public Virtual2DArray executeQuery(String sql)
			throws Exception;

	public PreparedStatement prepareStatement(String sql)
			throws Exception;
	
	
	public int getRowCount(Name table)
			throws Exception;

	public int maxValue(Name table, String column)
			throws Exception;

	
	public int getRowCountWhere(Name table, String where)
			throws Exception;
        
    public Helper getHelper();
    
    public int executeUpdate(String sql) throws SQLException;
    
    public Connection getConnection();
    
    public DBConnection cloneConnection();

	public void close();
	
	public void resetAllTables();
}

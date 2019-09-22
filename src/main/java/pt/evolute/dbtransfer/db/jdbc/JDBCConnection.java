package pt.evolute.dbtransfer.db.jdbc;

import java.io.File;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import pt.evolute.dbtransfer.db.DBConnection;
import pt.evolute.dbtransfer.db.beans.ColumnDefinition;
import pt.evolute.dbtransfer.db.beans.Name;
import pt.evolute.dbtransfer.db.helper.Helper;
import pt.evolute.dbtransfer.db.helper.HelperManager;
import pt.evolute.dbtransfer.transfer.Mover;
import pt.evolute.utils.arrays.CursorResultSet2DArray;
import pt.evolute.utils.arrays.Virtual2DArray;
import pt.evolute.utils.db.Connector;

/**
 *
 * @author lflores
 */
public class JDBCConnection implements DBConnection {
    public static boolean debug = false;
    public static boolean debugSQL = false;

    private final String dbUrl;
    private final String dbUser;
    private final String dbPasswd;
    private final String dbSchema;

    private Connection connection;

    private final String catalog;
    private final boolean ignoreEmpty;

    private final Map<String, List<ColumnDefinition>> MAP_TABLE_COLUMNS = new HashMap<String, List<ColumnDefinition>>();

    private final List<Name> LIST_TABLES = new ArrayList<Name>();

    private final Helper helper;

    public JDBCConnection(String url, String user, String pass, boolean onlyNotEmpty, String schema) throws Exception {
        dbUrl = url;
        dbUser = user;
        dbPasswd = pass;
        testInitConnection();
        catalog = connection.getCatalog();
        if (schema == null) {
            dbSchema = Connector.getSchema(url);
        } else {
            dbSchema = schema;
        }

        ignoreEmpty = onlyNotEmpty;
        helper = HelperManager.getTranslator(url);
        helper.initConnection(this);
        System.out.println("JDBC: " + url + " catalog: " + catalog + " schema: " + dbSchema);
    }

    private void testInitConnection() throws Exception {
        boolean init;
        if (connection == null) {
            init = true;
        } else {
            try {
                init = connection.isClosed() || !connection.isValid(4);
            } catch (SQLException ex) {
                if (debug) {
                    ex.printStackTrace();
                }
                init = true;
                try {
                    connection.close();
                } catch (SQLException ex1) {
                    if (debug) {
                        ex1.printStackTrace();
                    }
                }
            }
        }
        if (init) {
            if (debug) {
                System.out.println("getConnection: " + dbUrl + " " + dbUser);
            }
            connection = Connector.getConnection(dbUrl, dbUser, dbPasswd);
            connection.setAutoCommit(false);
        }
    }

    public List<Name> getTableList() throws Exception {
        return getTableList(false);
    }

    public List<ColumnDefinition> getColumnList(Name table) throws Exception {
        List<ColumnDefinition> list = MAP_TABLE_COLUMNS.get(table.originalName);
        if (list == null) {
            testInitConnection();
            DatabaseMetaData rsmd = connection.getMetaData();
            ResultSet rs = rsmd.getColumns(catalog, dbSchema, table.originalName, null);
            list = new LinkedList<ColumnDefinition>();
            Map<String, ColumnDefinition> cols = new HashMap<String, ColumnDefinition>();
            if (debug) {
                System.out.println("COL FOR TABLE: <" + table.originalName + ">");
            }
            while (rs.next()) {
                String n;
                if (table.originalName.equals(table.originalName.toUpperCase())) {
                    n = rs.getString(4);
                } else {
                    n = "\"" + rs.getString(4) + "\"";
                }
                Name name = new Name(n);
                if (debug) {
                    System.out.println("COL: <" + name.originalName + ">");
                }
                // System.out.println( "COL: " + name.originalName );
                if (!cols.containsKey(name.saneName)) {
                    ColumnDefinition col = new ColumnDefinition();
                    col.name = name;
                    col.sqlTypeName = rs.getString(6);
                    col.sqlType = rs.getInt(5);
                    if (rs.getInt(5) == Types.CHAR || rs.getInt(5) == Types.LONGVARCHAR || rs.getInt(5) == Types.VARCHAR
                            || rs.getInt(5) == Types.NCHAR || rs.getInt(5) == Types.LONGNVARCHAR
                            || rs.getInt(5) == Types.NVARCHAR) {
                        col.sqlSize = rs.getInt(7);
                    }
                    col.defaultValue = helper.normalizeDefault(rs.getString(13));
                    col.isNotNull = "NO".equals(rs.getString(18).trim());
                    cols.put(col.name.saneName, col);
                    if (col.sqlType != Types.LONGVARBINARY) {
                        list.add(col);
                    }
                } else {
                    System.out.println("Ignoring duplicate: " + table.originalName + " - " + name);
                    new Exception("Ignoring duplicate: " + table.originalName + " - " + name).printStackTrace();
                    ;
                }
            }
            rs.close();
            list = Collections.unmodifiableList(listSort(list));
            list.forEach(c -> System.out.println(c));
            MAP_TABLE_COLUMNS.put(table.originalName, list);
        }
        // System.out.println( "COLSS size: " + list.size() );
        return list;
    }

    public static List<ColumnDefinition> listSort(List<ColumnDefinition> list) {

        list.sort((e1, e2) -> Integer.compare(e1.sqlType, e2.sqlType));

        return list;
    }

    public Virtual2DArray executeQuery(String sql) throws Exception {
        if (debug && debugSQL) {
            System.out.println("SQL: " + sql);
        }
        testInitConnection();
        Statement stm = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        helper.setupStatement(stm);
        Virtual2DArray ret = null;
        try {
            boolean hasResult = stm.execute(sql);
            if (hasResult) {
                ResultSet rs = stm.getResultSet();
                ret = new CursorResultSet2DArray(rs);
            }
        } catch (Exception ex) {
            System.out.println("<" + sql + ">");
            throw ex;
        }
        return ret;
    }

    public Virtual2DArray getFullTable(Name table) throws Exception {
        List<ColumnDefinition> cols = getColumnList(table);
        if (cols.isEmpty()) {
            System.out.println("Table without columns: " + table);
            return null;
        }
        StringBuilder buffer = new StringBuilder(cols.get(0).name.originalName);
        for (int i = 1; i < cols.size(); ++i) {
            ColumnDefinition col = cols.get(i);
            buffer.append(", ");
            buffer.append(col.name.originalName);
        }
        return executeQuery("SELECT " + buffer + " FROM " + helper.outputName(table.originalName));
    }

    public PreparedStatement prepareStatement(String sql) throws Exception {
        return connection.prepareStatement(sql);
    }

    @Override
    public int getRowCount(Name table) throws Exception {
        return getRowCountWhere(table, null);
    }

    public Helper getHelper() {
        return helper;
    }

    public String getSchema() {
        return dbSchema;
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {

        connection.setAutoCommit(true);

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            return stmt.executeUpdate();

        }

    }

    @Override
    public int getRowCountWhere(Name table, String where) throws Exception {

        return oneResultSqlFunction(table, where, "COUNT(*)");
    }

    private int oneResultSqlFunction(Name table, String where, String functionSql) throws Exception {

        String sql = "SELECT " + functionSql + " FROM " + helper.outputName(table.originalName);

        if (where != null) {
            sql = sql + " " + where;
        }

        int count = ((Number) executeQuery(sql).get(0, 0)).intValue();
        if (debug) {
            System.out.println("COUNT: " + table.originalName + " " + count + " rows.");
        }
        return count;
    }

    @Override
    public int maxValue(Name table, String column) throws Exception {

    	
        System.out.println("max: " + table.originalName + " " + column);
        
        return oneResultSqlFunction(table, null, String.format("coalesce(max(%s),0)", column));
    }

    @Override
    public Connection getConnection() {

        return connection;
    }

    @Override
    public DBConnection cloneConnection() {

        try {
            return new JDBCConnection(dbUrl, dbUser, dbPasswd, ignoreEmpty, dbSchema);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("EX in cloneConnection");

            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("EX in close");
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Name> getTableList(boolean ignoreDone) throws Exception {

        if (LIST_TABLES.isEmpty()) {

             	
            List<String> tables = FileUtils.readLines(new File(Mover.DONE_FILE), Charset.defaultCharset());

            DatabaseMetaData rsmd = connection.getMetaData();
            ResultSet rs = rsmd.getTables(catalog, dbSchema, null, new String[] {"TABLE"});
            while (rs.next()) {
                String table = rs.getString(3);
                Name n = new Name(table);
                if (helper.isTableValid(n)) {
                    if ("elo_log".equalsIgnoreCase(table) || "iri_sessao".equalsIgnoreCase(table)
                            || "iri_ses_output".equalsIgnoreCase(table) || "iri_transaccao".equalsIgnoreCase(table)
                            || (tables.contains(table.toUpperCase()) && !ignoreDone)) {
                        continue;
                    }
                    if (ignoreEmpty && getRowCount(n) == 0) {
                        continue;
                    }
                    LIST_TABLES.add(n);
                }
            }
            rs.close();
        }
        return LIST_TABLES;
    }

    @Override
    public void resetAllTables() {
        LIST_TABLES.clear();
    }

}

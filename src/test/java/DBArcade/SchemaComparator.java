package DBArcade;

import java.sql.*;
import java.util.*;

/**
 * This class compares the schema of two databases.
 */
public class SchemaComparator {
    private static final String DB_URL = "jdbc:your_database_url";
    private static final String USER = "your_username";
    private static final String PASS = "your_password";

    /**
     * Main method to compare schemas of two databases.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        try (Connection connA = DriverManager.getConnection(DB_URL + ";schema=SchemaA", USER, PASS);
             Connection connB = DriverManager.getConnection(DB_URL + ";schema=SchemaB", USER, PASS)) {

            DatabaseMetaData metaA = connA.getMetaData();
            DatabaseMetaData metaB = connB.getMetaData();

            // Get the list of tables from SchemaA
            List<String> tables = getTables(metaA);

            // Compare each table's columns between SchemaA and SchemaB
            for (String tableName : tables) {
                System.out.println("Comparing table: " + tableName);
                Map<String, ColumnInfo> columnsA = getColumnMetadata(metaA, tableName);
                Map<String, ColumnInfo> columnsB = getColumnMetadata(metaB, tableName);

                compareColumns(tableName, columnsA, columnsB);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the list of tables from the database metadata.
     *
     * @param meta the database metadata
     * @return a list of table names
     * @throws SQLException if a database access error occurs
     */
    private static List<String> getTables(DatabaseMetaData meta) throws SQLException {
        List<String> tables = new ArrayList<>();
        ResultSet rs = meta.getTables(null, null, "%", new String[]{"TABLE"});
        while (rs.next()) {
            tables.add(rs.getString("TABLE_NAME"));
        }
        return tables;
    }

    /**
     * Retrieves the column metadata for a given table.
     *
     * @param meta the database metadata
     * @param tableName the name of the table
     * @return a map of column names to ColumnInfo objects
     * @throws SQLException if a database access error occurs
     */
    private static Map<String, ColumnInfo> getColumnMetadata(DatabaseMetaData meta, String tableName) throws SQLException {
        Map<String, ColumnInfo> columns = new HashMap<>();
        ResultSet rs = meta.getColumns(null, null, tableName, "%");

        while (rs.next()) {
            String columnName = rs.getString("COLUMN_NAME");
            String dataType = rs.getString("TYPE_NAME");
            int columnSize = rs.getInt("COLUMN_SIZE");
            int nullable = rs.getInt("NULLABLE");

            columns.put(columnName, new ColumnInfo(columnName, dataType, columnSize, nullable));
        }
        return columns;
    }

    /**
     * Compares the columns of a table between two schemas.
     *
     * @param tableName the name of the table
     * @param columnsA the columns from SchemaA
     * @param columnsB the columns from SchemaB
     */
    private static void compareColumns(String tableName, Map<String, ColumnInfo> columnsA, Map<String, ColumnInfo> columnsB) {
        // Check for columns in SchemaA that are missing in SchemaB
        for (String columnName : columnsA.keySet()) {
            ColumnInfo colA = columnsA.get(columnName);
            ColumnInfo colB = columnsB.get(columnName);

            if (colB == null) {
                System.out.println("Column " + columnName + " missing in Schema B");
            } else if (!colA.equals(colB)) {
                System.out.println("Column mismatch in table " + tableName + " for column " + columnName);
                System.out.println("Schema A: " + colA);
                System.out.println("Schema B: " + colB);
            }
        }

        // Check for columns in SchemaB that are missing in SchemaA
        for (String columnName : columnsB.keySet()) {
            if (!columnsA.containsKey(columnName)) {
                System.out.println("Column " + columnName + " missing in Schema A");
            }
        }
    }

    /**
     * Class to store column information.
     */
    static class ColumnInfo {
        String name;
        String dataType;
        int size;
        int nullable;

        /**
         * Constructor for ColumnInfo.
         *
         * @param name the name of the column
         * @param dataType the data type of the column
         * @param size the size of the column
         * @param nullable whether the column is nullable
         */
        ColumnInfo(String name, String dataType, int size, int nullable) {
            this.name = name;
            this.dataType = dataType;
            this.size = size;
            this.nullable = nullable;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ColumnInfo that = (ColumnInfo) o;
            return size == that.size &&
                    nullable == that.nullable &&
                    Objects.equals(name, that.name) &&
                    Objects.equals(dataType, that.dataType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, dataType, size, nullable);
        }

        @Override
        public String toString() {
            return "ColumnInfo{" +
                    "name='" + name + '\'' +
                    ", dataType='" + dataType + '\'' +
                    ", size=" + size +
                    ", nullable=" + nullable +
                    '}';
        }
    }
}
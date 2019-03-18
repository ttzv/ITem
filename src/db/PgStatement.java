package db;

import com.sun.istack.internal.Nullable;

import java.util.ArrayList;

public class PgStatement {


    /**
     * Convenience method used for building PostgreSQL INSERT statement
     * @param table - name of table where data will be inserted
     * @param columns - list of column names where data will be inserted
     * @param values - list of values for insertion, must be in order with columns
     * @return build PostgreSQL INSERT statement ready for query
     */
    public static String insert (String table, String[] columns, String [] values ){
        String insertStatement = "INSERT INTO " + table + " ";
        StringBuilder sb = new StringBuilder(insertStatement);
        //building column data
        sb.append("(");
        for (String s : columns){
            sb.append(s);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append(") ");
        sb.append("VALUES ");

        sb.append("(");
        for (String s : values){
            sb.append(s);
            sb.append(",");
        }
        sb.deleteCharAt(sb.length()-1);
        sb.append(")");

        return sb.toString();
    }

    public static void main(String[] args) {
        String [] col = {"col1","col2","col3"};
        String [] data = {"data1","data2","data3"};
        data = PgStatement.apostrophied(data);

        System.out.println(PgStatement.insert("table1", col, data));
        System.out.println(PgStatement.update("test", "sn", "testid3", "ID=3"));

    }

    /**
     * Convenience method used for wrapping values between apostrophes
     * @param data list of data in any Serializable format
     * @return list of Strings wrapped in apostrophies
     */
    public static String[] apostrophied(String... data ){
        ArrayList<String> wrapped = new ArrayList<>();
        for (Object d : data){
            wrapped.add("'" + d.toString() + "'");
        }
        return wrapped.toArray(new String[0]);
    }

    /**
     * Convenience method used for wrapping values between apostrophes
     * @param data data to put inbetween apostrophes
     * @return data wrapped in apostrophes
     */
    public static String apostrophied(String data){
        return "'" + data + "'";
    }

    public static String select(String table, String content, @Nullable String criterium){
        String select = "SELECT " + content + " FROM " + table + " ";
        if(criterium == null) {
            return select;
        } else {
            return select + "WHERE " + criterium + " ";
        }
    }

    public static String update(String tableToUpdate, String columnToSet, String value, String criterium){ return "UPDATE " + tableToUpdate + " SET " + columnToSet + " = " + value + " WHERE " + criterium + ";"; }

    /**
     * Builds a PostgreSQL statement that checks if given value exists in column
     * @param table - table to look into
     * @param column - column where to search
     * @param value - searched value
     * @return complete Statement
     */
    public static String exists(String table, String column, String value){
        return "select exists(select 1 from " + table + " where " + column + "='" + value +"')";
    }

    /**
     * Builds a PostgreSQL statement that performs SELECT with ascending or descending sorting based on chosen column
     * @param table table to select from
     * @param content list of columns to show
     * @param column column to sort by
     * @param ascending true for ascending sorting, false for descending sorting
     * @return Complete statement
     */
    public static String selectAscending(String table, String content, @Nullable String criterium, String column, boolean ascending){
        if(ascending) {
            return select(table, content, criterium) + "order by " + column + " asc ";
        } else {
            return select(table, content, criterium) + "order by " + column + " desc ";
        }
    }
}

package db;

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
        String[] temp;
        return wrapped.toArray(new String[0]);
    }

    public static String select(String table, String content){
        return "SELECT " + content + " FROM " + table + " ";
    }

    public static String update(String tableToUpdate, String columnToSet, String value, String criterium){ return "UPDATE " + tableToUpdate + " SET " + columnToSet + " = " + value + " WHERE " + criterium + ";"; }
}

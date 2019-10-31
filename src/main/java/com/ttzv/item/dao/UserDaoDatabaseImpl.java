package com.ttzv.item.dao;

import com.ttzv.item.entity.*;
import com.ttzv.item.properties.Cfg;
import com.ttzv.item.pwSafe.Crypt;
import com.ttzv.item.utility.Utility;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserDaoDatabaseImpl extends DatabaseHandler implements EntityDAO<User> {
    //User object is constructed from two tables, first represents data retrieved from LDAP, second is for addidional data about User updated in application or from different source
    private final static String TABLE_USERS = "users";
    private final static String TABLE_USER_DETAIL = "user_details";

    public UserDaoDatabaseImpl() throws SQLException {
        super();
    }

    @Override
    public List<User> getAllEntities() {
        String query = "SELECT * FROM " + TABLE_USERS
                + " INNER JOIN " + TABLE_USER_DETAIL
                + " ON " + TABLE_USERS + ".guid=" + TABLE_USER_DETAIL + ".guid";
        List<User> userList = new ArrayList<>();
        for (List<String> list :
                executeQuery(query)) {
            //create User objects with replaced keys to provide out-of-the-box compatibility with other objects
            //1.Create user using DynamicEntity
            //2.Replace all Keys in entityMap to familiar User object keys
            //3.Add as new object to list
            userList.add(new User(DynamicEntity.newDynamicEntity()
                    .process(list)
                    .replaceKeys(
                            new KeyMapper<User>(Paths.get(KeyMapper.KEY_MAP_JSON_PATH)), KeyMapper.OBJECTKEY))
            );
        }
        return userList;
    }

    @Override
    public User getEntity(String id) {
        String query = "SELECT * FROM " + TABLE_USERS
                + " INNER JOIN " + TABLE_USER_DETAIL
                + " ON " + TABLE_USERS + ".guid=" + TABLE_USER_DETAIL + ".guid"
                + " WHERE " + TABLE_USERS + ".guid='" + id + "'";
        List<String> result = Utility.unNestList(executeQuery(query));
        assert result != null;
        return new User(DynamicEntity.newDynamicEntity()
                .process(result)
                .replaceKeys(
                new KeyMapper<User>(Paths.get(KeyMapper.KEY_MAP_JSON_PATH)), KeyMapper.OBJECTKEY)
        );
    }

    @Override
    public boolean updateEntity(User entity) {

        KeyMapper<User> keyMapper = new KeyMapper<>(KeyMapper.KEY_MAP_JSON_PATH);
        DynamicEntity uEntity = entity.getUserEntity()
                .replaceKeys(
                        new KeyMapper<User>(Paths.get(KeyMapper.KEY_MAP_JSON_PATH)), KeyMapper.DBKEY
                ).setSeparator("=");
        String criteriumOfUpdating = keyMapper.getMapping(UserData.objectGUID.toString()).get(KeyMapper.DBKEY) + "='" + entity.getGUID() + "';";
        //prepare entity
        //retrieve all DB keys that are mapped to LDAP keys to construct UPDATE for USERS table
        //USER table is only updated with data from LDAP
        List<String> ldapUniqueKeys = keyMapper.getAllMappingsOf(KeyMapper.LDAPKEY)
                .stream()
                .map(k -> keyMapper.getCorrespondingMapping(k, KeyMapper.DBKEY))
                .collect(Collectors.toList());
        List<String> ldapUniquePairs = null;
        for (String luk : ldapUniqueKeys) {
            ldapUniquePairs = uEntity.getList("'").stream().filter(s -> s.contains(luk)).collect(Collectors.toList());
        }
        String sql0 = this.updateSql(TABLE_USERS, ldapUniquePairs, criteriumOfUpdating);

        executeUpdate(sql0);

        //retrieve all DB keys that don't have corresponding LDAP mapping to construct UPDATE for USER_DETAIL table
        List<String> dbUniquekeys = keyMapper.getAllMappingsOf(KeyMapper.DBKEY);
        dbUniquekeys.removeAll(ldapUniqueKeys);
        List<String> dbUniquePairs = null;
        for (String duk : dbUniquekeys) {
            dbUniquePairs = uEntity.getList("'").stream().filter(s -> s.contains(duk)).collect(Collectors.toList());
        }
        String sql1 = this.updateSql(TABLE_USER_DETAIL, dbUniquePairs, criteriumOfUpdating);

        executeUpdate(sql1);


        /*String sql = "UPDATE " + TABLE_USERS;
        KeyMapper keyMapper = new KeyMapper(Paths.get(KeyMapper.KEY_MAP_JSON_PATH));
        List<String> userLdapColumns = getTableColumns(TABLE_USERS);
        if (userLdapColumns.size() == 0) userLdapColumns = keyMapper.getAllMappingsOf(entity.getClass().getSimpleName(), KeyMapper.DBKEY);
        List<String> userDetailColumns = getTableColumns(TABLE_USER_DETAIL);
        Map<String, String> entityMap = entity.getUserEntity().getMap();

        Map<String, String> = new HashMap<>()
*/      return true; //todo
    }

    @Override
    public boolean deleteEntity(User entity) {
        return false;
    }

    /*@Override
    public boolean executeUpdate(String sql){ //todo: move to abstract
        if (connection != null) {
            Statement statement = null;
            try {
                statement = connection.createStatement();
                statement.executeUpdate(sql);
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    @Override
    public List<List<String>> executeQuery(String query){ //todo: move to abstract
        ResultSet resultSet = null;
        Statement statement = null;
        List<List<String>> resultList = new ArrayList<>();
        List<String> innerlist = null;
        if (connection != null) {
            try {
                statement = connection.createStatement();
                resultSet = statement.executeQuery(query);
                ResultSetMetaData rsmd = resultSet.getMetaData();
                while (resultSet.next()){
                    innerlist = new ArrayList<>();
                    for (int i = 1; i <= rsmd.getColumnCount() ; i++) {
                        innerlist.add(rsmd.getColumnLabel(i) + Utility.DEFAULT_ENTITY_SEPARATOR + resultSet.getString(i));
                    }
                    resultList.add(innerlist);
                }
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return resultList;
    }


*/















    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


   /* private String dbUrl;
    private String dbUser;
    private char[] dbPass;

    public void loadCfgCredentials(){
        this.setDbUrl(Cfg.getInstance().retrieveProp(Cfg.DB_URL));
        this.setDbUser(Cfg.getInstance().retrieveProp(Cfg.DB_LOGIN));
        this.setDbPass(PHolder.db);
    }

    public boolean initConnection() throws SQLException{
        String dbUrl = "jdbc:postgresql://" + this.dbUrl;

        Properties props = new Properties();
        props.setProperty("user", dbUser);
        props.setProperty("password", new String(dbPass));

        this.conn = DriverManager.getConnection(dbUrl, props);

        return true;
    }

    *//**
     * Executes custom statements, mltiple supported
     * @param statements one or more statements
     * @throws SQLException
     *//*
    public void customStatement(String... statements) throws SQLException {
        Statement st = conn.createStatement();
        for (String statement : statements) {
            if(!statement.isEmpty()) {
                st.executeUpdate(statement);
            }
        }
        st.close();
    }

    public int delete(String table, String criterium) throws SQLException {
        Statement st = conn.createStatement();
        String statement = PgStatement.delete(table, criterium);
        int cnt = st.executeUpdate(statement);

        st.close();

        return cnt;
    }



    public void addGUID() throws SQLException {
        List<User> ldapusers = ldapParser.getUsersDataList();
        for (User u :
                ldapusers) {
            update("users", "userguid="+PgStatement.apostrophied(u.getGUID()), "userGUID="+PgStatement.apostrophied(u.getGUID()));
            System.out.println("Updated GUID " + u);
        }
    }

    public void addWhenChanged() throws SQLException {
        List<User> ldapusers = ldapParser.getUsersDataList();
        for (User u :
                ldapusers) {
            update("users", "userGUID=" + PgStatement.apostrophied(u.getGUID()), "whenChanged=" + PgStatement.apostrophied(u.getWhenChanged()));
            System.out.println("Updated whenchanged " + u);
        }
    }

    public void ldapToDb() throws SQLException {
        Statement st = conn.createStatement();

        List<User> ldapusers = ldapParser.getUsersDataList();
        String[] columns = User.columns;
        for (User u : ldapusers) {
            //String qpart ="(" +"'"+u.getSamAccountName()+"'" +","+ "'"+u.getGivenName()+"'" +","+ "'"+u.getSn()+"'" +","+ "'"+u.getDisplayName()+"'" +","+ "'"+u.getUserAccountControl()+"'"+")";
            String statement = PgStatement.insert("users", columns, PgStatement.apostrophied(u.getComplete())); //hopefully this works in the same way
            st.executeUpdate(statement);
        }

        st.close();
    }

    *//**
     * Checks if given value exists in selected column
     * @param table table where search will be performed
     * @param column column to search into
     * @param value value to search for
     * @return true if value exists, otherwise false
     * @throws SQLException when connection with database could not be established or query was invalid
     *//*
    public boolean exists(String table, String column, String value) throws SQLException {
        Statement st = conn.createStatement();

        String exists = PgStatement.exists(table,column,value);

        ResultSet resultSet = st.executeQuery(exists);
        resultSet.next();

        boolean result = resultSet.getBoolean(1);
        resultSet.close();
        return result;
    }


    *//*public ResultSet select(String identifier) throws SQLException {
        Statement st = conn.createStatement();

        ResultSet resultSet = st.executeQuery(PgStatement.select("users", "*", "userguid=" + identifier));

     }*//*

    *//**
     * Updates table USERS with new values from LDAP
     * @return List of users that were added to a database during update execution
     *//*
    public List<User> updateUsersTable() throws SQLException {
        String table = "users";
        String column = "userguid";
        Statement st = conn.createStatement();

        List<User> ldapusers = ldapParser.getUsersDataList();
        List<User> newUsers = new ArrayList<>();

        //addGUID();
        //addWhenChanged();

        for(User user : ldapusers){
            boolean exists = exists(table, column, user.getGUID());
            if(!exists) {

                String query = PgStatement.insert(table, User.insertColumns, PgStatement.apostrophied(user.getComplete()));
                System.out.println(query);
                st.executeUpdate(query);
                newUsers.add(user);
            }
        }
        st.close();
        return newUsers;
    }

    *//**
     * @param count number of users to retrieve from database
     * @return true if method performed correctly
     * @throws SQLException database connection not estabilished
     *//*
    public boolean getNewUsers(int count) throws SQLException {
        UserHolder.clear();
        Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = st.executeQuery(PgStatement.selectAscending("users,city", "*","users.city=city.id", "whencreated", false) + "limit " + count);
        while(resultSet.next()){
            User user = buildUserFromDB(resultSet);
            UserHolder.addUser(user);
        }
        //System.out.println(resultSet.getRow());
        resultSet.close();
        return true;
    }

    public User buildUserFromDB(ResultSet resultSet) throws SQLException {
        String[] resultUserData = new String[User.columns.length];
        for (int i = 0; i <= User.columns.length - 1 ; i++) {
            resultUserData[i] = resultSet.getString(User.columns[i]);
        }
        return new User(resultUserData);
    }

    *//**
     * Performs search in Users table for inputted value
     * @param value value to search for in database
     * @param limiter maximum amount of rows returned by query, no limit of set to 0
     * @return list of Users with fitting value
     *//*
    //todo: make search case insensitive and support substring searches
    public List<User> globalSearch(String value, int limiter) throws SQLException {
        if(Utility.restrictedSymbols.contains(value)){
            value="";
        }

        ArrayList<User> foundUsers = new ArrayList<>();

        if(!value.isEmpty()) {
            StringBuilder allSearchCriterium = new StringBuilder("(");
            for (int i = 0; i < User.columns.length; i++) {
                if (i < User.columns.length - 1) {
                    allSearchCriterium.append(" ").append(User.columns[i]).append(" ILIKE ").append(PgStatement.apostrophied("%" + value + "%")).append(" OR");
                } else {
                    allSearchCriterium.append(" ").append(User.columns[i]).append(" ILIKE ").append(PgStatement.apostrophied("%" + value + "%")).append(")");
                }
            }

            allSearchCriterium.append(" AND (users.city=city.id)");

            String query = PgStatement.select("users,city", "*", allSearchCriterium.toString()) + " limit " + limiter;
            if (limiter == 0) {
                query = PgStatement.select("users,city", "*", allSearchCriterium.toString());
            }

            //System.out.println(query);
            Statement st = conn.createStatement();
            ResultSet resultSet = st.executeQuery(query);


            while (resultSet.next()) {
                String[] resultUserData = new String[User.columns.length];
                for (int i = 0; i <= User.columns.length - 1; i++) {
                    resultUserData[i] = resultSet.getString(User.columns[i]);
                }
                User user = new User(resultUserData);
                foundUsers.add(user);
            }

            st.close();
        }

        return foundUsers;
    }

    public User reloadUser (User user) throws SQLException {
        return globalSearch(user.getGUID(), 1).get(0);
    }



    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public char[] getDbPass() {
        return dbPass;
    }

    public void setDbPass(char[] dbPass) {
        this.dbPass = dbPass;
    }*/

}
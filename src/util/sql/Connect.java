package util.sql;

import org.mindrot.jbcrypt.BCrypt;
import util.sql.response.RowResponse;
import util.sql.response.SelectResponse;
import util.sql.response.row.Manager;
import util.ErrorResponse;

import java.sql.*;
import java.util.ArrayList;

@SuppressWarnings("SqlSourceToSinkFlow")
public class Connect {
    private static final String URL = "jdbc:mysql://localhost:3306/sakila";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "My$ql_$erveR@2024";

    public static SelectResponse select(String query, Object... params) {
        SelectResponse response = new SelectResponse();

        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            PreparedStatement pstmt = connection.prepareStatement(query);

            for(int i = 0; i < params.length; i++) {
                Object o = params[i];
                if(o instanceof String) {
                    pstmt.setString(i + 1, (String) o);
                } else if(o instanceof Integer) {
                    pstmt.setInt(i + 1, (Integer) o);
                }
            }

            ResultSet rs = pstmt.executeQuery();

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                response.getColumns().add(metaData.getColumnName(i));
            }

            while (rs.next()) {
                ArrayList<String> row = new ArrayList<>();
                for (int i = 0; i < columnCount; i++) {
                    row.add(rs.getString(i + 1));
                }
                response.getRows().add(row);
            }
        } catch (SQLException e) {
            response.addErrorMessage("SQL Error: " + e.getMessage());
        }

        return response;
    }

    public static ErrorResponse insert(String query) {
        ErrorResponse response = new ErrorResponse();

        Connection conn = null;

        try{
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement(query);

            int rowsAffected = pstmt.executeUpdate();

            if(rowsAffected > 0) {
                System.out.println("Inserted " + rowsAffected + " row(s)");
                conn.commit();
            } else{
                conn.rollback();
                response.addErrorMessage("Error adding contact");
            }
        } catch (SQLException e){
            try {
                if (conn != null) {
                    conn.rollback();
                    response.addErrorMessage("SQL Error: " + e.getMessage());
                } else{
                    response.addErrorMessage("SQL Error: " + e.getMessage());
                }
            } catch (SQLException e1) {
                response.addErrorMessage("Connection Rollback Error: " + e.getMessage());
            }
        }

        return response;
    }

    public static ErrorResponse registerManager(String username, String email, String password){
        String query = "insert into sakila.manager (username, email, password) values(?,?,?)";

        ErrorResponse response = new ErrorResponse();

        SelectResponse checkExist = select("select * from sakila.manager where username=?", username.toLowerCase());

        if(!checkExist.isValid()){
            response.addErrorMessage(checkExist.getErrorMessagesAsString());
            return response;
        } else{
            if(!checkExist.getRows().isEmpty()){
                response.addErrorMessage("Username already exists!");
                return response;
            }
        }

        Connection conn = null;
        PreparedStatement ps = null;

        try{
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(query);

            ps.setString(1, username.toLowerCase());
            ps.setString(2, email);
            ps.setString(3, password);

            int rows = ps.executeUpdate();

            if(rows > 0){
                conn.commit();
            } else{
                conn.rollback();
                response.addErrorMessage("Error with adding a manager.");
            }
        } catch (SQLException e){
            try{
                if(conn != null){
                    conn.rollback();
                    response.addErrorMessage("SQL Exception: " + e.getMessage());
                }
            } catch (SQLException ex){
                response.addErrorMessage("SQL Exception: " + ex.getMessage());
            }
        }

        return response;
    }

    public static RowResponse<Manager> getManager(String username, String password){
        RowResponse<Manager> response = new RowResponse();
        String query = "select * from sakila.manager where username=?";

        try{
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, username.toLowerCase());

            try(ResultSet resultSet = ps.executeQuery()){
                if(resultSet.next()){
                    String hashedPassword = resultSet.getString("password");
                    if(BCrypt.checkpw(password, hashedPassword)){
                        Manager manager = new Manager(
                                Integer.parseInt(resultSet.getString("id")),
                                username,
                                resultSet.getString("email"),
                                resultSet.getBinaryStream("image")
                        );

                        response.setData(manager);
                        return response;
                    } else{
                        response.addErrorMessage("Invalid password!");
                    }
                }
                response.addErrorMessage("No account information found!");
            }
        } catch (SQLException e){
            response.addErrorMessage("SQL Exception: " + e.getMessage());
        }


        return response;
    }

    public static ErrorResponse updateManagerInfo(Manager manager){
        ErrorResponse response = new ErrorResponse();

        String sql = "update sakila.manager set username=?, email=?, image=? where id=?";

        SelectResponse checkExist = select("select * from sakila.manager where username=?", manager.getUsername().toLowerCase());

        if(!checkExist.isValid()){
            response.addErrorMessage(checkExist.getErrorMessagesAsString());
            return response;
        } else{
            ArrayList<ArrayList<String>> rows = checkExist.getRows();
            int idIndex = checkExist.getColumns().indexOf("id");
            if(!rows.isEmpty()){
                //This checks if the username exists, but not for this user
                if(Integer.parseInt(rows.get(0).get(idIndex)) != manager.getId()){
                    response.addErrorMessage("Username already exists!");
                    return response;
                }
            }
        }

        Connection conn = null;
        PreparedStatement ps;

        try{
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sql);

            ps.setString(1, manager.getUsername().toLowerCase());
            ps.setString(2, manager.getEmail());
            ps.setBinaryStream(3, manager.getImgInputStream());
            ps.setInt(4, manager.getId());

            int rowsAffected = ps.executeUpdate();

            if(rowsAffected > 0){
                conn.commit();
            } else{
                conn.rollback();
                response.addErrorMessage("Error with updating information.");
            }
        } catch (SQLException e){
            try {
                if (conn != null) {
                    conn.rollback();
                }
                response.addErrorMessage("SQL Exception: " + e.getMessage());
            } catch (SQLException rollbackEx) {
                response.addErrorMessage("SQL Rollback Exception: " + rollbackEx.getMessage());
            }
        }

        return response;
    }

    public static ErrorResponse updateManagerPassword(Manager manager, String newPassword){
        ErrorResponse response = new ErrorResponse();

        String sql = "update sakila.manager set password=? where id=?";

        Connection conn = null;
        PreparedStatement ps;

        try{
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sql);

            ps.setString(1, newPassword);
            ps.setInt(2, manager.getId());

            int rowsAffected = ps.executeUpdate();

            if(rowsAffected > 0){
                conn.commit();
            } else{
                conn.rollback();
                response.addErrorMessage("Error with updating information.");
            }
        } catch (SQLException e){
            try {
                if (conn != null) {
                    conn.rollback();
                }
                response.addErrorMessage("SQL Exception: " + e.getMessage());
            } catch (SQLException rollbackEx) {
                response.addErrorMessage("SQL Rollback Exception: " + rollbackEx.getMessage());
            }
        }

        return response;
    }

    public static ErrorResponse deleteRow(String table, String pKeyName,  int primaryKey) {
        ErrorResponse response = new ErrorResponse();

        String query = "DELETE FROM sakila." + table + " WHERE " + pKeyName + " = ?";

        try(Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD)){
            connection.setAutoCommit(false);
            PreparedStatement pstmt = connection.prepareStatement(query);

            pstmt.setInt(1, primaryKey);

            int rowsAffected = pstmt.executeUpdate();

            if(rowsAffected > 0){
                connection.commit();
            } else{
                connection.rollback();
                response.addErrorMessage("Error deleting information");
            }
        } catch (SQLException ex){
            response.addErrorMessage("SQL Exception: " + ex.getMessage());
        }

        return response;
    }

    public static ErrorResponse updateDatabase(String table, String pKeyName, int pKey, String column, Object value) {
        ErrorResponse response = new ErrorResponse();


        String query = "update sakila." + table + " set " + column + " = ? where " + pKeyName + " = " + pKey;

        Connection conn = null;

        try{
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement(query);

            if(value instanceof String){
                ps.setString(1, (String) value);
            } else if(value instanceof Integer){
                ps.setInt(1, (Integer) value);
            }

            int rowsAffected = ps.executeUpdate();

            if(rowsAffected > 0) {
                conn.commit();
            } else{
                conn.rollback();
                response.addErrorMessage("Error with updating information.");
            }
        } catch (SQLException e){
            try{
                if(conn != null){
                    conn.rollback();
                }
                response.addErrorMessage("SQL Exception: " + e.getMessage());
            } catch (SQLException rollbackEx) {
                response.addErrorMessage("SQL Rollback Exception: " + rollbackEx.getMessage());
            }
        }

        return response;
    }
}

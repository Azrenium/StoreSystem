package sql;

import sql.table.Manager;
import util.ErrorResponse;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Connect {
    private static final String URL = "jdbc:mysql://localhost:3306/sakila";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "My$ql_$erveR@2024";

    public static SelectResponse select(String sql) {
        SelectResponse response = new SelectResponse();

        
    }

    public static ErrorResponse registerManager(String username, String email, String password){
        String query = "insert into sakila.manager (username, email, password) values(?,?,?)";

        ErrorResponse response = new ErrorResponse();

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

    public static Manager getManager(String username, String password){
        Manager manager = null;
        String query = "select * from sakila.manager where username=? and password=?";
    }
}

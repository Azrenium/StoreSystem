package util.sql;

import org.mindrot.jbcrypt.BCrypt;
import util.sql.response.RowResponse;
import util.sql.response.SelectResponse;
import util.sql.response.table.Manager;
import util.ErrorResponse;

import java.sql.*;
import java.util.ArrayList;

public class Connect {
    private static final String URL = "jdbc:mysql://localhost:3306/sakila";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "My$ql_$erveR@2024";

    private static SelectResponse select(String query, Object... params) {
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
                    }
                }
            }
        } catch (SQLException e){
            response.addErrorMessage("SQL Exception: " + e.getMessage());
        }


        return response;
    }
}

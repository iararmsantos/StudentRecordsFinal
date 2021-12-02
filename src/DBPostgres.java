import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Objective: Class will connect to database using postgresql
 * @author iarar
 * Date: 11/30/2021
 */
public class DBPostgres {
    //to call in another class
    
    //using class DBPostgres or DBMaria
    //DBPostgres.getConnection();
    //DBMaria.getConnection();

    private static Connection c = null;

    //get the connection to the database
    public static Connection getConnection() {
        if (c == null) {
            try {                
                c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/studentgui",
            "postgres", "183258");
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName()+": "+e.getMessage());
            }
            System.out.println("Opened database successfully");
        }
        return c;
    }

    //close connection
    public static void closeConnection() {
        if (c != null) {
            try {
                c.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }    

    //close statement
    public static void closeStatement(Statement st) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //close resultset
    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

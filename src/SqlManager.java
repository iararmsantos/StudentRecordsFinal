
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Purpose: Class will manage queries between database and StudentFrame
 * @author iarar
 * date: 11/30/2021
 */
public class SqlManager {
    private Connection conn;    

    //to call: SqlManager manager = new SqlManager(DBPosgres.getConnection());
    public SqlManager(Connection conn) {
        this.conn = conn;
    }
    
    //will bring all data from database and insert it into a list of students
    public List<Student> findAll() {
        
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT student.studentId, student.firstName, student.lastName, student.program, "
                    + "student.mark1, student.mark2, student.mark3, student.mark4, student.mark5, student.mark6 "
                    + "FROM "
                    + "student ORDER BY studentid");
            rs = st.executeQuery();

            List<Student> list = new ArrayList<>();
            //while have data in the database update the student list
            while (rs.next()) {
                Student obj = new Student();
                obj.setStudentID(rs.getString("studentId"));
                obj.setFname(rs.getString("firstName"));
                obj.setLname(rs.getString("lastName"));
                obj.setProgram(rs.getString("program")); 
                obj.setMark(0, rs.getDouble("mark1"));
                obj.setMark(1, rs.getDouble("mark2"));
                obj.setMark(2, rs.getDouble("mark3"));
                obj.setMark(3, rs.getDouble("mark4"));
                obj.setMark(4, rs.getDouble("mark5"));
                obj.setMark(5, rs.getDouble("mark6"));                
                                
                list.add(obj);
            }      
            
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            Logger.getLogger(SqlManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBPostgres.closeStatement(st);
            DBPostgres.closeResultSet(rs);
        }
        return null;
    }    
    
    //it will insert data into student table
    public void insert(Student obj) {       
        String id = "";
        PreparedStatement st = null;
        String SQL = "INSERT INTO student(studentId, firstName, lastName, program, mark1, mark2, mark3, mark4, mark5, mark6) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";      
        try {     
           
            st = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);               
            
            //set the String || Int || Double value, to the given parameter index
            st.setString(1, obj.getStudentID());  
            st.setString(2, obj.getFname());
            st.setString(3, obj.getLname());
            st.setString(4, obj.getProgram());  
            st.setDouble(5, obj.getMark(0));
            st.setDouble(6, obj.getMark(1));
            st.setDouble(7, obj.getMark(2));
            st.setDouble(8, obj.getMark(3));
            st.setDouble(9, obj.getMark(4));
            st.setDouble(10, obj.getMark(5));  

            int rowsAffected = st.executeUpdate();              
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (Exception ex) {
            Logger.getLogger(SqlManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DBPostgres.closeStatement(st);
        }            
    }   
    
    public void update(Student obj) {
        PreparedStatement st = null;
        String SQL = "UPDATE student SET firstName = ?, lastName = ?, program = ?," +
                     "mark1 = ?, mark2 = ?, mark3 = ?, mark4 = ?, mark5 = ?, mark6 = ? "
                +     "WHERE studentId = ?";
        
        try {           
            st = conn.prepareStatement(SQL);           
            
            st.setString(1, obj.getFname());
            st.setString(2, obj.getLname());
            st.setString(3, obj.getProgram());            
            st.setDouble(4, obj.getMark(0));
            st.setDouble(5, obj.getMark(1));
            st.setDouble(6, obj.getMark(2));
            st.setDouble(7, obj.getMark(3));
            st.setDouble(8, obj.getMark(4));
            st.setDouble(9, obj.getMark(5));
            st.setString(10, obj.getStudentID());

            st.executeUpdate();           
           
        } catch (SQLException e) {
            System.out.println("Student Update");
            e.printStackTrace();
        } finally {
            DBPostgres.closeStatement(st);
        }        
    }    

    
    public void deleteById(String id) {
        PreparedStatement st = null;
        try {           
            st = conn.prepareStatement(
                    "DELETE FROM student WHERE studentid = ?");

            st.setString(1, id);
            st.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBPostgres.closeStatement(st);
        }
    } 
}

//obs: method ResultSet.getRow = get the row number for the current row in the ResultSet
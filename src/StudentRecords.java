import javax.swing.JFrame;

/**
 * The main frame composed by StudentFrame and MarksPanel will show students id, name,
program, and 6 grades
 * @author iarar
 * Date: 09/19/2021
 */
public class StudentRecords {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        StudentFrame app = new StudentFrame("Student Records");
        app.setSize(675, 350);
        app.setLocationRelativeTo(null);
        app.setVisible(true);
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //double testMarks[] = {75.4, 85, 82.3, 91.7, 65, 12};
        //Student jim = new Student("Jim","Ronholm","CPA", testMarks);
        //app.setStudent(jim);
    }

}

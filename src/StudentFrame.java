
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.InvalidMarkException;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Objective: Class JFrame will contain the GUI of Students Records
 * @author iarar
 * Date: 11/30/2021
 */
public final class StudentFrame extends JFrame {

    //panels will be inside the frame
    private JPanel mainPanel = new JPanel(new BorderLayout());
    private JPanel stdFieldsPanel = new JPanel(new GridLayout(2, 4));
    private JPanel actionBtnPanel = new JPanel(new GridLayout(1, 4));
    MarksPanel marksFrame = new MarksPanel();

    //buttons
    private JButton btnPrev = new JButton("Prev");
    private JButton btnNext = new JButton("Next");
    private JButton btnDelete = new JButton("Delete");
    private JButton btnEdit = new JButton("Edit");
    private JButton btnAdd = new JButton("Add");
    private JButton btnSave = new JButton("Save");

    //labels
    private JLabel lblID = new JLabel("ID:");
    private JLabel lblFName = new JLabel("First Name:");
    private JLabel lblProgram = new JLabel("Program:");
    private JLabel lblLName = new JLabel("Last Name:");

    //text fields
    private JTextField txtID = new JTextField();
    private JTextField txtFName = new JTextField();
    private JTextField txtProgram = new JTextField();
    private JTextField txtLName = new JTextField();

    //to handle files
    private String fileChosed;    
    String filePath = "";

    //to work with list of students
    List<Student> students = new ArrayList<>();
    private static int countStudents;
    private static int countNP;
    Student temp;
    private boolean isEditing = false;

    //container object to add elements
    Container content = getContentPane();

    //constructor
    StudentFrame(String name) {
        super(name);

        //setting grid layout to the frame as the default is Flow Layout
        setLayout(new BorderLayout());

        add(btnPrev, BorderLayout.WEST);
        add(btnNext, BorderLayout.EAST);

        actionBtnPanel.add(btnDelete);
        actionBtnPanel.add(btnEdit);
        actionBtnPanel.add(btnAdd);
        actionBtnPanel.add(btnSave);

        //add one lbl and one txt to organize layout as requirements
        stdFieldsPanel.add(lblID);
        stdFieldsPanel.add(txtID);
        stdFieldsPanel.add(lblFName);
        stdFieldsPanel.add(txtFName);
        stdFieldsPanel.add(lblProgram);
        stdFieldsPanel.add(txtProgram);
        stdFieldsPanel.add(lblLName);
        stdFieldsPanel.add(txtLName);

        //adding to the container
        add(mainPanel, BorderLayout.CENTER);
        mainPanel.add(stdFieldsPanel, BorderLayout.CENTER);
        mainPanel.add(actionBtnPanel, BorderLayout.SOUTH);

        content.add(mainPanel, BorderLayout.CENTER);
        content.add(marksFrame, BorderLayout.SOUTH);

        //disable buttons to start
        disableButtons();
        btnDelete.setEnabled(true);
        btnAdd.setEnabled(true);
        btnEdit.setEnabled(true);
        btnSave.setEnabled(true);

        txtFName.setText("no student");
        txtLName.setText("no student");
        txtProgram.setText("no student");
        txtID.setText("no student");
        disableTextFields();

        setDisabledTextColors();

        //get data from database
        findAllStudents();

        //listener to the add button
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                
                //add new student to the list                
                temp = new Student();
                students.add(temp);
                countNP = students.size() - 1;
                btnEdit.setEnabled(true);
                btnEdit.setText("Done");
                btnDelete.setEnabled(false);
                btnAdd.setEnabled(false);
                btnNext.setEnabled(false);

                //clear all text fields
                cleanTextFields();
                enableTextFields();
                marksFrame.clearMarks();

                //id of the actual student
                txtID.setText(temp.getStudentID());
                btnSave.setEnabled(false);
            }
        });

        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnAdd.setEnabled(false); 
                btnSave.setEnabled(false);

                if ("Done".equals(btnEdit.getText())) {
                    //disable or enable buttons and textfields                    
                    btnEdit.setText("Edit");
                    btnDelete.setEnabled(true);
                    btnAdd.setEnabled(true);

                    disableTextFields();

                    //set the students data to the students list                
                    students.get(countNP).setFname(txtFName.getText());
                    students.get(countNP).setLname(txtLName.getText());
                    students.get(countNP).setProgram(txtProgram.getText());
                    students.get(countNP).setStudentID(txtID.getText());
                    students.get(countNP).setMarks(marksFrame.getUserMarks());

                    //clear all text fields
                    //cleanTextFields();             
                    if(isEditing){
                        updateStudents(students.get(countNP));
                        btnPrev.setEnabled(false);
                    }else{
                        insertStudents(students.get(countNP));
                    }
                    isEditing = false;
                    if (countNP > 0) {
                        btnPrev.setEnabled(true);   
                    }
                    if(countNP < students.size() - 1){
                        btnNext.setEnabled(true);
                    }
                } else {
                    isEditing = true;
                    enableTextFields();
                    txtID.setEnabled(false);
                    btnEdit.setEnabled(true);
                    btnEdit.setText("Done");
                    btnPrev.setEnabled(false);
                    btnNext.setEnabled(false);
                }
            }
        });

        //listener to the prev button
        btnPrev.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //countNP -> variable to count when previous or next is clicked. Has to be global, will recive students size  
                if (countNP > 1) {
                    countNP--;

                    fillStudents(countNP);

                    btnNext.setEnabled(true);

                } else {
                    countNP--;
                    fillStudents(countNP);

                    btnPrev.setEnabled(false);
                    btnNext.setEnabled(true);
                }
            }
        });
        //listener to the next button
        btnNext.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                countNP++;
                if (countNP < students.size() - 1) {

                    fillStudents(countNP);

                    btnPrev.setEnabled(true);

                } else {
                    fillStudents(countNP);
                    btnPrev.setEnabled(true);
                    btnNext.setEnabled(false);
                }
            }
        });

        btnSave.addActionListener((ActionEvent e) -> {
            fileChosed = chooserFile(true);
            writeFile(fileChosed);
        });

        btnDelete.addActionListener((ActionEvent e) -> {
            delete(students.get(countNP));
            countNP--;
            findAllStudents();
            fillStudents(countNP);
        });
    }
    //delete students from the table
    private void delete(Student std){
        SqlManager manager = new SqlManager(DBPostgres.getConnection());
        manager.deleteById(std.getStudentID());
        
        JOptionPane.showMessageDialog(null, "Student Deleted!");
        
        
    }

    //it will bring all students from the database
    private void findAllStudents() {
        //connect to database
        SqlManager manager = new SqlManager(DBPostgres.getConnection());
        //insert all results into students list
        students = manager.findAll();

        if (students.size() > 0) {
            //fill students into the gui
            fillStudents(0);
        } else {
            cleanTextFields();
        }
    }
    
    //to save or update students into database
    public void updateStudents(Student std) {
        //connect to database
        SqlManager manager = new SqlManager(DBPostgres.getConnection());
        manager.update(std);
        JOptionPane.showMessageDialog(null, "Student Updated!");
    }

    //to save or update students into database
    public void insertStudents(Student std) {
        //connect to database
        SqlManager manager = new SqlManager(DBPostgres.getConnection());
        manager.insert(std);
        JOptionPane.showMessageDialog(null, "Student Inserted!");
    }

    //to read object from file
    public void readFile(String filePath) {
        //casting to array to read objects
        try ( FileInputStream fileStream = new FileInputStream(filePath);  ObjectInputStream objStream = new ObjectInputStream(fileStream)) {
            //casting to array to read objects
            students = (List<Student>) objStream.readObject();

            fillStudents(0);
            countStudents = students.size();
            if (students.size() > 1) {
                btnNext.setEnabled(true);
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(StudentFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(StudentFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidMarkException | ClassNotFoundException ex) {
            Logger.getLogger(StudentFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(StudentFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //to write object to a file
    public void writeFile(String filePath) {
        try ( FileOutputStream file = new FileOutputStream(filePath);  ObjectOutputStream output = new ObjectOutputStream(file)) {

            //writing all array at the same time
            output.writeObject(students);

            //output.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StudentFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(StudentFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String chooserFile(boolean isSave) {
        //Let user choose the filename and where to save it
        JFileChooser chooser = new JFileChooser("D:\\Iara\\Documents\\5Semester\\Java");
        chooser.setDialogTitle("Specify a file to save");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = 0;
        if (isSave) {
            result = chooser.showSaveDialog(null);
        } else {
            result = chooser.showOpenDialog(null);
        }

        //verify if user cancel the operation instead to chose a file
        if (result == JFileChooser.CANCEL_OPTION) {
            System.out.println("You cancelled");
        } else if (result == JFileChooser.APPROVE_OPTION) {
            //will get the name of the file user chose
            filePath = chooser.getSelectedFile().getAbsolutePath();
            filePath = filePath.replace("\\", "\\\\");
            System.out.printf("You chose %s %n",
                    filePath);
        }

        return filePath;
    }

    //fill textbox with selected students data
    private void fillStudents(int num) {
        txtFName.setText(students.get(num).getFname());
        txtLName.setText(students.get(num).getLname());
        txtID.setText(students.get(num).getStudentID());
        txtProgram.setText(students.get(num).getProgram());

        //to get all marks from one student
        for (int i = 0; i < 6; i++) {
            double newMark = 0;

            try {
                newMark = students.get(num).getMark(i);
            } catch (InvalidMarkException ex) {
                newMark = 0.0;
            } catch (Exception ex) {
                Logger.getLogger(StudentFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            marksFrame.getTxtMarks()[i].setText(String.valueOf(newMark));
        }
        if (students.size() > 1) {
            btnNext.setEnabled(true);
        }

        if (countNP == students.size() - 1) {
            btnNext.setEnabled(false);
        }
    }

    //clean text fields - have to work with exceptions to this rule
    private void cleanTextFields() {
        txtID.setText("");
        txtFName.setText("");
        txtLName.setText("");
        txtProgram.setText("");
        for (int i = 0; i < 6; i++) {
            marksFrame.getTxtMarks()[i].setText("");
        }
    }

    //enable user type in text fields
    private void enableTextFields() {
        txtFName.setEnabled(true);
        txtLName.setEnabled(true);
        txtProgram.setEnabled(true);
        txtID.setEnabled(true);
        for (int i = 0; i < 6; i++) {
            marksFrame.getTxtMarks()[i].setEnabled(true);
        }
    }

    //disable user type in text fields
    public void disableTextFields() {
        txtFName.setEnabled(false);
        txtLName.setEnabled(false);
        txtProgram.setEnabled(false);
        txtID.setEnabled(false);
        for (int i = 0; i < 6; i++) {
            marksFrame.getTxtMarks()[i].setEnabled(false);
        }
    }

    //set text colors to disabled TextFields
    private void setDisabledTextColors() {
        txtFName.setDisabledTextColor(Color.BLUE);
        txtLName.setDisabledTextColor(Color.BLUE);
        txtProgram.setDisabledTextColor(Color.BLUE);
        txtID.setDisabledTextColor(Color.BLUE);
        for (int i = 0; i < 6; i++) {
            marksFrame.getTxtMarks()[i].setDisabledTextColor(Color.BLUE);
        }
    }

    //disable all buttons
    private void disableButtons() {
        btnDelete.setEnabled(false);
        btnEdit.setEnabled(false);
        btnAdd.setEnabled(false);
        btnSave.setEnabled(false);
        btnPrev.setEnabled(false);
        btnNext.setEnabled(false);
    }

}

//how to change color in disabled text field: https://stackoverflow.com/questions/3698859/java-set-font-color-in-a-disabled-text-field


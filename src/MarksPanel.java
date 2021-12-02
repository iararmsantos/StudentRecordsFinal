import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Objective: Class JPanel will will be part of StudentFrame. It contain the SOUTH 
part with Students marks 
 * @author iarar
 * date: 11/30/2021
 */
public class MarksPanel extends JPanel {

    //atributes
    private JLabel lblMarks = new JLabel("Marks", SwingConstants.CENTER);
    private JTextField txtMarks[] = new JTextField[6];
    private JPanel marksPanel = new JPanel(new GridLayout(2, 3));

    //constructor
    public MarksPanel() {
        //to change Layout
        setLayout(new BorderLayout());
        
        //adding labels 
        
        //changing some features
        lblMarks.setFont(new Font("Verdana", Font.PLAIN, 18));        
        
        for (int i = 0; i < 6; i++) {
            //txtMarks[i] = new JTextField(String.valueOf(std.getMarks()));
            
            txtMarks[i] = new JTextField("Mark " + i);
            txtMarks[i].setFont(new Font("Verdana", Font.PLAIN, 15));
            marksPanel.add(txtMarks[i]);
        }
        
        //adding elements to the container
        setPreferredSize(new Dimension(775,90));
        setBackground(Color.red);
        add(lblMarks, BorderLayout.NORTH);
        add(marksPanel, BorderLayout.SOUTH);
        
        setVisible(true);
    }

    //get text marks to set colors and disable it
    public JTextField[] getTxtMarks() {
        return txtMarks;
    }
 
    public void clearMarks() {
        for (int i = 0; i < 6; i++) {
            txtMarks[i].setText(" ");
            marksPanel.add(txtMarks[i]);
        }
    }  
    
    public double[] getUserMarks(){
        double[] marks = new double[6];
        for(int i = 0; i < txtMarks.length; i++){
            marks[i] = Double.parseDouble(txtMarks[i].getText());
        }
        return marks;
    }

    public void setTxtMarks(JTextField[] txtMarks) {
        this.txtMarks = txtMarks;
    }
    
    
}

//how to center jlabel: https://stackoverflow.com/questions/19506769/how-to-center-jlabel-in-jframe-swing

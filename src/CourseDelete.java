import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.sun.org.apache.xerces.internal.util.DOMUtil.setVisible;

public class CourseDelete extends CourseDatabaseManager {
    private JTextField DeleteID;
    private JButton confirmDeleteButton;

    public CourseDelete() {


        confirmDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(findRecord(String.valueOf(DeleteID)))
                {
                    deleteCourse(String.valueOf(DeleteID));
                }
                else
                {
                    JOptionPane.showMessageDialog(frame,"Invalid ID");
                }
            }
        });
    }
}

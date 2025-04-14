 
package javaux;
 
import java.awt.Color;
import java.awt.Cursor; 
import java.awt.Font; 
import javax.swing.BorderFactory;
import javax.swing.JButton;

class Buttons{
    
    public void setuploginButton(JButton button) {
        button.setFocusable(false); 
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBackground(Color.decode("#876F4D"));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.PLAIN, 20));
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); // Set border
        button.setBounds(50, 300, 330, 50); //x, y, width, height
        //button.addMouseListener();
        button.setFocusPainted(false);
     }
}

 
package javaux;

// Importing AWT classes for UI customization and event handling
import java.awt.Color; // Represents colors to customize UI components 
import java.awt.Cursor; // Allows changing the cursor appearance
import java.awt.Font; // Manages font styles and sizes for text rendering

// Import event handling classes
import java.awt.event.ActionEvent; // Represents an action event (e.g., button clicks)
import java.awt.event.ActionListener; //Listens for and hanldes action events
import java.awt.event.MouseAdapter; // Provides a default implementation for handling mouse events 
import java.awt.event.MouseEvent; // Represents mouse events (e.g., clicks, movements)

// Importing Swing components for creating a graphical user interface (GUI)
import javax.swing.BorderFactory; // Creates a border styles for UI elements
import javax.swing.ImageIcon; // Handles icons and images in the UI
import javax.swing.JButton; // Represents a clickable button in the UI 
import javax.swing.JFrame; // Represents the main window of the application
import javax.swing.JLabel; // Displays text or images in the UI
import javax.swing.SwingConstants; // Provides constants for UI alignment

// Definition of the ProfilePage class, which represents the user profile page
public class ProfilePage {

    // Declare instance variables for GUI components
    private JFrame frame; // Main window (frame) for the profile page
    private JLabel usernameLabel, emailLabel, genderLabel; // Labels to display information
    private JButton editProfileButton, cancelButton, logoutButton, profilePictureButton; // Buttons for various actions

    // Constructor for the ProfilePage class, initializing the profile UI for the given user
    public ProfilePage(User user) {
        
        String username = user.getUsername(); // Retrieve the username of the user by calling the getUsername() method
        String userEmail = user.getEmail(); // Retrieve the email of the user by calling the getEmail() method
        String userGender = user.getGender(); // Retrieve the gender of the user by calling the getGender() method
                      
        frame = new JFrame("Profile Page");
        frame.setSize(410, 500);
        frame.setResizable(false);
        frame.getContentPane().setBackground(Color.decode("#222222"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        
        ImageIcon icon = new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\adamson-logo.png");
        frame.setIconImage(icon.getImage()); 
        
        profilePictureButton = new JButton();
        profilePictureButton.setContentAreaFilled(false);
        profilePictureButton.setFocusable(false);
        profilePictureButton.setOpaque(true);  // Make sure the background is opaque to see the color
        profilePictureButton.setFocusPainted(false);
        profilePictureButton.setBackground(Color.decode("#876F4D"));
        profilePictureButton.setBounds(130, 40, 140, 160);
        //profilePictureButton.setIcon(profilePictureIcon1);
        profilePictureButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        WhiteProfileIcon(userGender, profilePictureButton);
        
        usernameLabel = new JLabel(username);
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setFont(new Font(null, Font.TYPE1_FONT, 18));
        usernameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        usernameLabel.setBounds(0, 205, 400, 30);
        
        emailLabel = new JLabel(userEmail);
        emailLabel.setForeground(Color.decode("#876F4D"));
        emailLabel.setFont(new Font(null, Font.TYPE1_FONT, 14));
        emailLabel.setHorizontalAlignment(SwingConstants.CENTER);
        emailLabel.setBounds(0, 230, 400, 20);
        
        editProfileButton = new JButton("Edit Profile");
        editProfileButton.setContentAreaFilled(false); // Disable default background behavior
        editProfileButton.setOpaque(true);  // Make sure the background is opaque to see the color
        editProfileButton.setFocusable(false);
        editProfileButton.setFocusPainted(false);
        editProfileButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        editProfileButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        editProfileButton.setForeground(Color.WHITE);
        editProfileButton.setBounds(100, 280, 200, 30);
        editProfileButton.setBackground(Color.decode("#876F4D"));
        editProfileButton.addActionListener((ActionEvent e) -> {
            new EditProfilePage(user);  
            frame.dispose();
        });
        
        cancelButton = new JButton("Cancel");
        cancelButton.setContentAreaFilled(false); // Disable default background behavior
        cancelButton.setOpaque(true);  // Make sure the background is opaque to see the color
        cancelButton.setFocusable(false);
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cancelButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBounds(100, 350, 200, 30);
        cancelButton.setBackground(Color.decode("#876F4D"));
        cancelButton.addActionListener((ActionEvent e) -> {
            new LoggedInPage(user);
            frame.dispose();            
        });
        
        // Create a MouseAdapter object to handle mouse events for multiple buttons
        MouseAdapter listener = new MouseAdapter() {
            
            // Methods triggered when the mouse is clicked on a button
            @Override
            public void mouseClicked(MouseEvent e) {
                JButton source = (JButton) e.getSource(); // Gets the button that was clicked 
                //System.out.println("You Clicked the mouse");
                //profilePictureButton.setIcon(profilePictureIcon2);
                GoldProfileIcon(userGender, profilePictureButton); // Calls the method GoldProfileIcon that updates the profile icon based on userGender and profilePictureButton
                source.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Changes the cursor icon to a hand
                source.setBackground(Color.WHITE); // Sets the background color of the button to white
                source.setForeground(Color.BLACK); // Sets the text color of the button to black
                //signUpButton.setFont(new Font("Arial", Font.PLAIN, 20));
                source.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); // Sets the button border to white
            }

            // Method triggered when a mouse button is pressed in a button
            @Override
            public void mousePressed(MouseEvent e) {
                JButton source = (JButton) e.getSource(); // Gets the button that was pressed 
                //System.out.println("You Pressed the mouse");
                //profilePictureButton.setIcon(profilePictureIcon2);
                GoldProfileIcon(userGender, profilePictureButton); // Calls the method GoldProfileIcon that updates the profile icon based on userGender and profilePictureButton
                source.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Changes the cursor icon to a hand
                source.setBackground(Color.WHITE); // Sets the background color of the button to white
                source.setForeground(Color.decode("#8A6E4B")); // Sets the text color of the button to a Taupe color (Brown gold)
                //signUpButton.setFont(new Font("Arial", Font.PLAIN, 20));
                //source.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); // Sets the button border to a white
            }

            // Method triggered when a mouse button is released after being pressed 
            @Override
            public void mouseReleased(MouseEvent e) {
                JButton source = (JButton) e.getSource(); // Gets the button that was released after being pressed. It updates the button appearance back to its normal state.
                WhiteProfileIcon(userGender, profilePictureButton); // Calls the method WhiteProfileIcon that updates the profile icon based on userGender and profilePictureButton
                //profilePictureButton.setIcon(profilePictureIcon1);
                source.setBackground(Color.decode("#876F4D")); // Sets background color of the button to khaki
                source.setForeground(Color.WHITE); // Sets the text color of the button to white
                source.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); // Sets the button border to white
            }

            // Method triggered when the mouse enters the button area and it provides visual feedback to indicate interactivity 
            @Override
            public void mouseEntered(MouseEvent e) {
                JButton source = (JButton) e.getSource(); // Gets the button that the mouse entered
                //System.out.println("You Entered the mouse");
                //profilePictureButton.setIcon(profilePictureIcon1);
                WhiteProfileIcon(userGender, profilePictureButton); // Calls the method WhiteProfileButton that updates the profile icon based on userGender and profilePictureButton
                source.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Changes the cursor icon to a hand
                source.setBackground(Color.decode("#514937")); // Sets the background color of a button to a dark brownish-green
                source.setForeground(Color.WHITE); // Sets the text color of a button to white
                //signUpButton.setFont(new Font("Arial", Font.PLAIN, 20));
                source.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); // Sets the button border to white
            }

            // Method triggered when the mouse exits the button area. it restores the button's default appearance
            @Override
            public void mouseExited(MouseEvent e) {
                JButton source = (JButton) e.getSource(); // Gets the button that the mouse exited 
                //profilePictureButton.setIcon(profilePictureIcon1);
                WhiteProfileIcon(userGender, profilePictureButton); // Calls the method WhiteProfileIcon that updates the profile icon based on userGender and profilePicureButton
                source.setBackground(Color.decode("#876F4D")); // Sets the background color of the button to khaki
                source.setForeground(Color.WHITE); // Sets the text color of the button to white
                source.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); // Sets the button border to white
            }

            // Method to set the profile picture based on the user's gender specifically for gold
            private void GoldProfileIcon(String userGender, JButton profilePictureButton) {
                
                // Check if the user's gender is "Male"
                if (userGender.equals("Male")) {
                    // If the user is male, set the profilePictureButton's icon to a gold male profile picture
                    profilePictureButton.setIcon(new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\profilePictureMaleGold.png"));
                }
                
                // Check if the user's gender is "Female"
                else if (userGender.equals("Female")) {
                    // If the user is a female, set the profilePictureButton's icon to a gold female profile picture
                    profilePictureButton.setIcon(new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\profilePictureFemaleGold.png"));
                }
                
                // If the user's gender is neither "Male" nor "Female"
                else {
                    // Set the profilePictureButton's icon to null (no icon)
                    profilePictureButton.setIcon(null);
                }
            }
        };
        
        // Add same MouseAdapter (listener) to multiple buttons to apply the behavior 
        profilePictureButton.addMouseListener(listener);
        editProfileButton.addMouseListener(listener);
        cancelButton.addMouseListener(listener);
                
        // Add all components to the frame to make them visible
        frame.add(usernameLabel);
        frame.add(emailLabel);
        frame.add(profilePictureButton);
        frame.add(cancelButton);
        frame.add(editProfileButton);
        frame.setVisible(true);
      }

    // Method to set the profile picture based on the user's gender specifically for white
    private void WhiteProfileIcon(String userGender, JButton profilePictureButton) {
        
        // Check if the user's gender is "Male"
        if (userGender.equals("Male")) {
            // If the user is male, set the profilePictureButton's icon to a white background male profile picture
            profilePictureButton.setIcon(new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\profilePictureMaleWhite.png"));
        } 
        
        // Check if the user's gender is "Female"
        else if (userGender.equals("Female")) {
            // If the user is female, set the profilePictureButton's icon to a white background female profile picture
            profilePictureButton.setIcon(new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\profilePictureFemaleWhite.png"));
        } 
            
        // if the user's gender is neither "Male" or "Female"
        else {
            // Set the profilePictureButton's icon to null (no icon)
            profilePictureButton.setIcon(null);
        }
    }    
}

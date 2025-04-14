 
package javaux;
 
// Importing AWT classes for UI costomiAtion and event handling
//import java.awt.Color; // Represents colors to customize UI components
//import java.awt.Cursor; // Changes the cursor apparance 
//import java.awt.Font; // Manages font stle and size for text redering
import java.awt.*;

// Importing event handling classes
//import java.awt.event.ActionEvent; // Represents an action event (e.g., button clicks)
//import java.awt.event.ActionListener; // Listens for and handles action events
//import java.awt.event.KeyAdapter; // Providesa default implementation for handling key events
//import java.awt.event.KeyEvent; // Represents key events (e.g., key presses)
//import java.awt.event.MouseAdapter; // Provides a default implementation for handling mouse events
//import java.awt.event.MouseEvent; // Represents mouse events (e.g., clicks, movement)
import java.awt.event.*;

// Importing file handling and I/O operators
//import java.io.BufferedReader; // Reads text from a file efficiently
//import java.io.BufferedWriter; // Writes text to a file efficiently
//import java.io.File; // Represents a file or directory path
//import java.io.FileReader; // Reads data to a file
//import java.io.FileWriter; // Writes data to a file
//import java.io.IOException; // Handles exceptions rekated to input/output operations
import java.io.*;

// Importing date and time utilities
import java.time.LocalDate; // Represents a date without a time-zone
import java.time.format.DateTimeFormatter; // Formats date objects into strings
import java.util.Calendar; // Provides methods for working with dates and times

// Importing Base64 encoding and decoding utility
import java.util.Base64; // Provides methods for encoding and decoding Base64 data

// Importing collections
import java.util.HashMap; // Implements a data structure for storing key-value pairs

// Importing cryptographic classes for encryption and decryption
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

// Importing Swing components for creating a graphical user interface (GUI)
//import javax.swing.BorderFactory; // Creates border styles for UI elements
//import javax.swing.ButtonGroup; // Groups readio buttons to allow only one selection
//import javax.swing.ImageIcon; // Handles icons and images in the UI
//import javax.swing.JButton; // Represents a clickable button in the UI 
//import javax.swing.JComboBox; // Creates a dropdown selection list
//import javax.swing.JFrame; // Represents the main window of the application
//import javax.swing.JLabel; // Display text or images in the UI
//import javax.swing.JOptionPane; // Display pop-up dialogs (alerts, messages, confirmations)
//import javax.swing.JRadioButton; // Represents a selectable radio button
//import javax.swing.JTextField; // Represents a single-line text input field
//import javax.swing.SwingConstants; // Provides constants for UI alignment
import javax.swing.*;

import javax.crypto.spec.IvParameterSpec; 
import java.security.SecureRandom; 
import java.util.Date;

// Definition of the EditProfilePage class 
public class EditProfilePage {
    
    // Declare instance variables for GUI components 
    private JFrame frame; 
    private JLabel titleLabel, currentInfoLabel, newInfoLabel, usernameLabel, currentUsernameLabel, emailLabel, currentEmailLabel, 
                   phoneNumberLabel, birthdayLabel, currentBirthdayLabel, birthdayDateLabel, genderLabel, currentGenderLabel, line1, genderIcon;
    private JRadioButton maleButton, femaleButton;
    private JTextField newUsernameTxt, newEmailTxt, phoneNumberTxt;
    private JButton deleteAccountButton, changePasswordButton, saveChangesButton, cancelButton;
    private JComboBox<Integer> yearDropdown, dayDropdown;
    private JComboBox<String> monthDropdown;
    
    //private User user;
    
    // AES key for encryption and decryption (same as before)
    private static final String SECRET_KEY = "mysecretkey12345"; // 16-byte key (128 bits)
    
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    
    // Constructor for the EditProfilePage class
    public EditProfilePage(User user) {
        
        /* Retrieve user details from the user object */
        String userUsername = user.getUsername(); // Retrieve the username from the user object
        String userEmail = user.getEmail(); // Retrieve the email from the user object 
        String userBirthday = user.getBirthday(); // Retrieve the birthday from the user object
        String userGender = user.getGender(); // Retrieve the gender from thge user object
        
        // Parse the userBirthday string to a LocalDate object
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
        LocalDate birthdayDate = LocalDate.parse(userBirthday, formatter);

        // Extract the day, month, and year from the parsed LocalDate
        int day = birthdayDate.getDayOfMonth();
        int month = birthdayDate.getMonthValue();
        int year = birthdayDate.getYear();
        
        frame = new JFrame("Edit Profile");
        frame.setSize(540, 560); 
        frame.setResizable(false);
        frame.getContentPane().setBackground(Color.decode("#222222"));
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        ImageIcon image = new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\adamson-logo.png");
        frame.setIconImage(image.getImage());
        
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        titleLabel = new JLabel("Edit Profile Information");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font(null, Font.TYPE1_FONT, 20));
        titleLabel.setBounds(50, 20, 250, 50);
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        currentInfoLabel = new JLabel("Current Information");
        currentInfoLabel.setForeground(Color.decode("#876F4D"));
        currentInfoLabel.setFont(new Font(null, Font.TYPE1_FONT, 15));
        currentInfoLabel.setBounds(50, 60, 182, 50);
        
        newInfoLabel = new JLabel("Enter New Information");
        newInfoLabel.setForeground(Color.decode("#876F4D")); 
        newInfoLabel.setFont(new Font(null, Font.TYPE1_FONT, 15));
        newInfoLabel.setBounds(265, 60, 210, 50);
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        usernameLabel = new JLabel("Username");
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setBounds(50, 120, 62, 20);
        
        currentUsernameLabel = new JLabel(userUsername);
        currentUsernameLabel.setForeground(Color.decode("#876F4D"));
        currentUsernameLabel.setBounds(120, 120, 140, 20); //x, y, width, height
        
        newUsernameTxt = new JTextField();
        newUsernameTxt.setToolTipText("Only use alphanumeric values and/or '_', no spaces allowed"); 
        newUsernameTxt.setBounds(265, 120, 210, 20);
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        emailLabel = new JLabel("Email");
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setBounds(50, 160, 110, 20);
        
        currentEmailLabel = new JLabel(userEmail);
        currentEmailLabel.setForeground(Color.decode("#876F4D"));
        currentEmailLabel.setBounds(120, 160, 140, 20); //x, y, width, height
        
        newEmailTxt = new JTextField();
        newEmailTxt.setBounds(265, 160, 210, 20);
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        birthdayLabel = new JLabel("Birthday");
        birthdayLabel.setForeground(Color.WHITE);
        birthdayLabel.setBounds(50, 200, 110, 20);
        
        /* Current date label */
        currentBirthdayLabel = new JLabel(userBirthday); //JLabel to display current birthday of the user
        currentBirthdayLabel.setForeground(Color.decode("#876F4D"));
        currentBirthdayLabel.setBounds(120, 200, 140, 20); 
        
        /* Day Dropdown */  
        dayDropdown = new JComboBox<>(); //JComboBox for day selection
        for (int i = 1; i <= 31; i++) { // Loop to add days 1-31
            dayDropdown.addItem(i); // Add each day as an item in the dropdown
        }
        dayDropdown.setSelectedItem(day); // Set the selection to current day data of the user
        dayDropdown.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); 
        dayDropdown.setBounds(265, 200, 50, 20);

        /* Month Dropdown */
        monthDropdown = new JComboBox<>(); //  JComboBox for month selection
        for (int i = 1; i <= 12; i++) { // Loop to add months 1-12
            monthDropdown.addItem(getMonthName(i)); // You need a method like getMonthName for the month names
        }
        monthDropdown.setSelectedItem(getMonthName(month)); // Set the selection to current month data of the user
        monthDropdown.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); 
        monthDropdown.setBounds(315, 200, 95, 20);

        /* Year Dropdown */
        yearDropdown = new JComboBox<>(); // JComboBox for year selection
        int currentYear = Calendar.getInstance().get(Calendar.YEAR); // Set selection to current year data of the user
        for (int i = currentYear; i >= 1900; i--) { //add each year as an item in the dropdown
            yearDropdown.addItem(i);
        }
        yearDropdown.setSelectedItem(year); // Set default selection to current user year data
        yearDropdown.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        yearDropdown.setBounds(410, 200, 65, 20);
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        genderLabel = new JLabel("Gender"); 
        genderLabel.setForeground(Color.WHITE);
        genderLabel.setBounds(50, 240, 110, 20);
        
        currentGenderLabel = new JLabel(userGender);
        currentGenderLabel.setForeground(Color.decode("#876F4D"));
        currentGenderLabel.setBounds(120, 240, 160, 20);

        maleButton = new JRadioButton("Male");
        maleButton.setBackground(Color.decode("#222222"));
        maleButton.setForeground(Color.WHITE);
        maleButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        maleButton.setBounds(265, 240, 80, 20);

        femaleButton = new JRadioButton("Female");
        femaleButton.setBackground(Color.decode("#222222"));
        femaleButton.setForeground(Color.WHITE);
        femaleButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        femaleButton.setBounds(355, 240, 80, 20);

        // Creating a button group to ensure the only one gender is selected at a time
        ButtonGroup genderGroup = new ButtonGroup(); // ButtonGroup is a group of male and female radio button
        genderGroup.add(maleButton); // Add male button to the group
        genderGroup.add(femaleButton); // Add female button to the group
                
        // Set current radio button selected to the one from the user data
        if("Male".equals(userGender)) {
            maleButton.setSelected(true); 
        } else {
            femaleButton.setSelected(true);
        }
        
        genderIcon = new JLabel();
        genderIcon.setBounds(455, 240, 20, 20);
        genderIconMethod(userGender, genderIcon);
        //genderIcon.addActionListener(new GenderIconAction());
        maleButton.addActionListener(new GenderIconAction());
        femaleButton.addActionListener(new GenderIconAction());
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////     
       
        /* These key listeners will automatically trigger the signUp button if the enter key is pressed in the keyboard */
        
        newUsernameTxt.addKeyListener(new KeyAdapter() { // Add key listener to the newUsernameTxt textField 
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Check if Enter key is pressed
                    saveChangesButton.doClick(); // Simulate a click on the signup button
                }
            }
         });
        
        newEmailTxt.addKeyListener(new KeyAdapter() { // Add key listener to the newEmailTxt textField
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Check if the Enter key is pressed
                    saveChangesButton.doClick(); // Simulate a click on the signup button
                }
            }
        });
        
        maleButton.addKeyListener(new KeyAdapter() { // Add key listener to the maleButton textField
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Check if the Enter key is pressed
                    saveChangesButton.doClick(); // Simulate a click on the signup button
                }
            }
        });
        
        femaleButton.addKeyListener(new KeyAdapter() { // Add key listener to the femaleButton textField
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Check if the Enter key is pressed
                    saveChangesButton.doClick(); // Simulate a click on the signup button
                }
            }
        });
        
        yearDropdown.addKeyListener(new KeyAdapter() { // Add key listener to the yearDropdown JComboBox 
           @Override
           public void keyPressed(KeyEvent e) {
               if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Check if the Enter key is pressed
                   saveChangesButton.doClick(); // Simulate a click on the signup button
               }
           }
        });
        
        dayDropdown.addKeyListener(new KeyAdapter() { // Add key listener to the dayDropdown JComboBox
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Check if the Enter key is pressed
                    saveChangesButton.doClick(); // Simulate a click on the signup button
                }
            }
        });
        monthDropdown.addKeyListener(new KeyAdapter() { // Add key listener to the monthDropdown JComboBox
            @Override
            public void keyPressed(KeyEvent e) { // Check if the Enter key is pressed
                if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Simulate a click on the signup button
                    saveChangesButton.doClick();
                }
            }
        });
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 
        changePasswordButton = new JButton("Change Password"); 
        changePasswordButton.setContentAreaFilled(false);
        changePasswordButton.setOpaque(true);
        changePasswordButton.setFocusable(false);
        changePasswordButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        changePasswordButton.setBackground(Color.decode("#876F4D"));
        changePasswordButton.setForeground(Color.WHITE);
        changePasswordButton.setBorder(BorderFactory.createLineBorder(Color.WHITE)); 
        changePasswordButton.setBounds(50, 290, 210, 30);
       
        // Adding ActionListener to the changepasswoc
        changePasswordButton.addActionListener(e -> {
            new ResetPasswordFromProfilePage(user); // When the button is clicked, creat  newiinstance
            frame.dispose(); // Dispose of the current frame to remove 
        });
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////   
        deleteAccountButton = new JButton("Delete Account"); 
        deleteAccountButton.setContentAreaFilled(false);
        deleteAccountButton.setOpaque(true);
        deleteAccountButton.setFocusable(false);
        deleteAccountButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        deleteAccountButton.setBackground(Color.decode("#876F4D"));
        deleteAccountButton.setForeground(Color.WHITE);
        deleteAccountButton.setBorder(BorderFactory.createLineBorder(Color.WHITE)); 
        deleteAccountButton.setBounds(265, 290, 210, 30);
        
        // Adding ActionListener to the deletedActionButton to handle click events
        deleteAccountButton.addActionListener(e -> {
            new securityQuestionDeleteProfile(user); // When the button is clicked create a new instance of securityQuestionOrodke
            frame.dispose(); // Dispose of the current frrom the dode
        });

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////   
        line1 = new JLabel("----------------------------------------------------------------------------------------------------------");
        line1.setForeground(Color.decode("#8A6E4B"));
        line1.setBounds(50, 330, 425, 20);
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////         
        saveChangesButton = new JButton("Save Changes");
        saveChangesButton.setContentAreaFilled(false); // Disable default background behavior
        saveChangesButton.setOpaque(true);  // Make sure the background is opaque to see the color
        saveChangesButton.setFocusable(false);
        saveChangesButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        saveChangesButton.setBackground(Color.decode("#876F4D"));
        saveChangesButton.setForeground(Color.WHITE);
        saveChangesButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        saveChangesButton.setBounds(50, 370, 425, 50);
        saveChangesButton.addActionListener(new ResetInfoAction());
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////                  
        cancelButton = new JButton("Cancel");
        cancelButton.setContentAreaFilled(false); // Disable default background behavior
        cancelButton.setOpaque(true);  // Make sure the background is opaque to see the color
        cancelButton.setFocusable(false);
        cancelButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cancelButton.setBackground(Color.decode("#876F4D"));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        cancelButton.setBounds(50, 430, 425, 50);
        
        // Adding an action listener to the cancelButton to handle user interaction
        cancelButton.addActionListener(e -> {
            
            String newUsername = newUsernameTxt.getText().trim(); // Retrieving and triming inout from the newUsername text field
            String newEmail = newEmailTxt.getText().trim(); // Retrieving and triming input from the newEmail text field
            
            int daySelectedDropdown = (int) dayDropdown.getSelectedItem(); // Retrieving option selected in the dayDropdown JComboBox
            String monthSelectedDropdownString = (String) monthDropdown.getSelectedItem(); // Retrieving option selected in the monthDropdown JComboBox
            int yearSelectedDropdown = (int) yearDropdown.getSelectedItem(); // Retrieving option selected in the yearDwropdown JComboBox
            int currentUserDay = birthdayDate.getDayOfMonth(); // Extracts the dayOfMonth from the user's stored birthdate
            int currentUserMonth = birthdayDate.getMonthValue(); // Extracts the monthValue from the user's stored birthdate
            int currentUserYear = birthdayDate.getYear(); // Extracts the Year from the user's stored birthdate
            
            // Get the name of the currrn user's selected month
            String currentMonthName = getMonthName(currentUserMonth);
              
            // Check if any profile information has changed
            if((!newUsername.isEmpty() || !newEmail.isEmpty() || daySelectedDropdown != currentUserDay || !monthSelectedDropdownString.equalsIgnoreCase(currentMonthName) || yearSelectedDropdown != currentUserYear) 
                // Check if gender sleection has changed
                || (maleButton.isSelected() && !userGender.equals("Male")) || (femaleButton.isSelected() && !userGender.equals("Female"))) {
                
                // Show a confirmation dialog asking the user if they want to exit without saving changes
                int response = JOptionPane.showConfirmDialog(frame, 
                            "Are you sure you want exit without saving the changes?",
                            "Exit without saving", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                // If te user confirms (clicks "Yes")
                if (response == JOptionPane.YES_OPTION) {
                    frame.dispose(); // Close curent frame
                    new ProfilePage(user); // Open the ProfilePage again, llikely discarding unsaved changes
                } else {
                    return; // If the user selects "No", simply return and keep the current window open an=
                }
            } else {
                // If no changes were made, just close the current frame                                 
                frame.dispose();
                new ProfilePage(user); 
            }
        });       
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
               
        // Create a MouseAdapter object to handle mouse events for multiple buttons
        MouseAdapter listener = new MouseAdapter() {
            
            // Method triggered when the mouse is clicked on a button and it changes the cursor to a hand icon and modifies the button's appearance
            @Override
            public void mouseClicked(MouseEvent e) {
                JButton source = (JButton) e.getSource(); // Gets the button that was clicked
                //System.out.println("You Clicked the mouse");
                source.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Changes the cursor icon to a hand
                source.setBackground(Color.WHITE); // Sets the background color of the button to white 
                source.setForeground(Color.BLACK); // Sets the text color of the button to black
                //signUpButton.setFont(new Font("Arial", Font.PLAIN, 20));
                source.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); // Sets button border to a white outline 
            }

            // Method triggered when a mouse button is pressed in a button. It modifies the button's appearance to provide visual feedback
            @Override
            public void mousePressed(MouseEvent e) {
                JButton source = (JButton) e.getSource(); // Get the button that was pressed 
                //System.out.println("You Pressed the mouse");
                source.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Keep the cursor as a hand icon
                source.setBackground(Color.WHITE); // Set the background color of the button to white
                source.setForeground(Color.decode("#8A6E4B")); // Set the text color of the button to a Taupe color (Brown gold)
                //signUpButton.setFont(new Font("Arial", Font.PLAIN, 20));
                //source.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); // Set button border to white
            }

            // Method triggered when the mouse button is released after being pressed. It updates the button's appearance back to its normal state
            @Override
            public void mouseReleased(MouseEvent e) {
                JButton source = (JButton) e.getSource(); // Get the button that was released
                source.setBackground(Color.decode("#876F4D")); // Set the background color of the button to khaki
                source.setForeground(Color.WHITE); // Set the text color of the button to white
                source.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); // Set the button border to white
            }

            // Method triggered when the mouse enters the button area and it provides visual feedback to indicate interactivity
            @Override
            public void mouseEntered(MouseEvent e) {
                JButton source = (JButton) e.getSource(); // Get the button that the mouse entered
                //System.out.println("You Entered the mouse");
                source.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Change the cursor icon to a hand
                source.setBackground(Color.decode("#514937")); // Set the background color of the button to a dark brownish-green
                source.setForeground(Color.WHITE); // Restore the button text color to white
                //signUpButton.setFont(new Font("Arial", Font.PLAIN, 20));
                source.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); // Maintain the button border of tee button as white
            }

            // Mehtod triggered when the mouse exits the button area. It restores the button's default appearance 
            @Override
            public void mouseExited(MouseEvent e) {
                JButton source = (JButton) e.getSource(); // Get the button that the mouse exited 
                source.setBackground(Color.decode("#876F4D")); // Set the background color of the button to khaki
                source.setForeground(Color.WHITE); // Set the text color of the button to white
                source.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); // Set the button border to white
            }
        };
        
        // Add the same MouseAdapter (listener) to multiple buttons to apply the behavior 
        changePasswordButton.addMouseListener(listener);
        deleteAccountButton.addMouseListener(listener);
        saveChangesButton.addMouseListener(listener);
        cancelButton.addMouseListener(listener);
        
        // Add all components to the frame to make them visible
        frame.setVisible(true);
        frame.add(titleLabel);
        frame.add(currentInfoLabel);
        frame.add(newInfoLabel);
        frame.add(usernameLabel);
        frame.add(currentUsernameLabel);
        frame.add(newUsernameTxt);
        frame.add(emailLabel);
        frame.add(currentEmailLabel);
        frame.add(newEmailTxt);
        frame.add(birthdayLabel);
        frame.add(currentBirthdayLabel);
        frame.add(genderLabel);
        frame.add(currentGenderLabel);
        frame.add(maleButton);
        frame.add(femaleButton);
        frame.add(genderIcon);
        frame.add(line1);
        frame.add(yearDropdown);
        frame.add(dayDropdown);
        frame.add(monthDropdown);
        frame.add(deleteAccountButton);
        frame.add(changePasswordButton);
        frame.add(saveChangesButton);
        frame.add(cancelButton);
    }

    // Method to set the gender icon based on the user's gender
    private void genderIconMethod(String userGender, JLabel genderIcon) {
        // Check if the user is male
        if (userGender.equals("Male")) {
            // Set icon for male users
            genderIcon.setIcon(new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\IconMaleGold16px.png"));
        } 
        // Check if user is female
        else if (userGender.equals("Female")) {
            // Set icon for female users
            genderIcon.setIcon(new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\IconFemaleGold16px.png"));
        }
    }
    
    // Class that defines an action listener that updates gender icon when a button (male or female) is selected
    private class GenderIconAction implements ActionListener {
        @Override // Override the actionPerformed method to handle button clicks 
        public void actionPerformed(ActionEvent e) {
            // Check if the male button is selected
            if(maleButton.isSelected()) {
                // Set male icon
                genderIcon.setIcon(new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\IconMaleGold16px.png"));
            }
            // Check if the female button is selected
            else if(femaleButton.isSelected()) {
                // Set female icon
                genderIcon.setIcon(new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\IconFemaleGold16px.png"));
            }
        }
    }
            
    // Action listener for updating the username and email
    private class ResetInfoAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String currentUsername = currentUsernameLabel.getText().trim();  // Get current username from label
            String currentEmail = currentEmailLabel.getText().trim();  // Get current email from label
            String newUsername = newUsernameTxt.getText().trim();  // New username from text field
            String newEmail = newEmailTxt.getText().trim();  // New email from text field
            String newGender = maleButton.isSelected() ? "Male" : "Female";
            
            if (newUsername.isEmpty()) {
                newUsername = currentUsername;
            }
            
            if (!newUsername.matches("[a-zA-Z0-9_]+")) {
                JOptionPane.showMessageDialog(null, "'Username' not valid [No Spaces, and use only alphanumeric valaues and/or '_'", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (newEmail.isEmpty()) {
                newEmail = currentEmail;
            }
            
            // Check if email format is valid
            if (!newEmail.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.(com|ca|org|net|info)$")) {
                JOptionPane.showMessageDialog(null, "Please enter a valid 'Email'", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Check if the username already exists
            if (!currentUsername.equals(newUsername)) {
                if (isUsernameTaken(newUsername)){
                    JOptionPane.showMessageDialog(null, "This username is already taken. Please try another one.", "Error", JOptionPane.ERROR_MESSAGE);
                    return; // Stop further processing if username exists
                }
            }

            // Check if the email already exists
            if (!currentEmail.equals(newEmail) ) {
                if (isEmailTaken(newEmail)) {
                    JOptionPane.showMessageDialog(null, "This email is already in use.", "Error", JOptionPane.ERROR_MESSAGE);
                    return; // Stop further processing if email exists
                }
            }

            String newBirthday = "";
            
            int day = (int) dayDropdown.getSelectedItem();
            String monthName = (String) monthDropdown.getSelectedItem();
            int year = (int) yearDropdown.getSelectedItem();
            //String newBirthday = String.format("%d %s %d", day, month, year);
            
            int month = getMonthNumber(monthName) -1;
            
            Calendar selectedCal = Calendar.getInstance();
            selectedCal.setLenient(false);
            selectedCal.set(year, month, day);
            selectedCal.set(Calendar.HOUR_OF_DAY, 0);
            selectedCal.set(Calendar.MINUTE, 0);
            selectedCal.set(Calendar.SECOND, 0);
            selectedCal.set(Calendar.MILLISECOND, 0);
            
            // Load existing users data
            HashMap<String, String> userData = loadUserData();

            // Combine current email and username to form the key
            String key = currentEmail + ":" + currentUsername;

            // Check if the combined key exists in the user data map
            if (userData.containsKey(key)) {
                
                try {
                    Date selectedDate = selectedCal.getTime();
                    
                    // Today's date wit time stripped
                    Calendar todayCal = Calendar.getInstance();
                    todayCal.set(Calendar.HOUR_OF_DAY, 0);
                    todayCal.set(Calendar.MINUTE, 0);
                    todayCal.set(Calendar.SECOND, 0);
                    todayCal.set(Calendar.MILLISECOND, 0);
                    Date todayDate = todayCal.getTime();
                    
                    if (selectedDate.after(todayDate)) {
                        JOptionPane.showMessageDialog(null, "Invalid date! Please select today or past date only", "Date Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        newBirthday = String.format("%d %s %d", monthName, year); 
                        String encryptedNewBirthday = encryptData(newBirthday);
                        
                        // User found, update username and email
                        String encryptedCurrentEmail = encryptData(currentEmail);
                        String encryptedNewEmail = encryptData(newEmail);  // Encrypt new email
                        String encryptedNewUsername = encryptData(newUsername);  // Encrypt new username
                        String encryptedCurrentUsername = encryptData(currentUsername);
                        //String encryptedNewBirthday = encryptData(newBirthday);
                        String encryptedNewGender = encryptData(newGender);

                        // Save the updated username and email to the file
                        saveUpdatedDataToFile(encryptedCurrentEmail, encryptedNewEmail, encryptedCurrentUsername, encryptedNewUsername, encryptedNewBirthday, encryptedNewGender);

                        JOptionPane.showMessageDialog(frame, "Information updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        frame.dispose();
                        new MainPage(userData);  // Refresh the main page
                            }
                } catch (IllegalArgumentException a) {
                    JOptionPane.showMessageDialog(null, "Invalid date selected!", "Date Error", JOptionPane.ERROR_MESSAGE);
                }
                
                
            } else {
                JOptionPane.showMessageDialog(frame, "Current username or email is incorrect!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
 
    // Check if username already exists in the user data file
        private boolean isUsernameTaken(String newUsername) {
            try {
                // Open the file "user_data.txt" for reading
                BufferedReader reader = new BufferedReader(new FileReader("user_data.txt"));
                String line;
                while ((line = reader.readLine()) != null) { // Read every line of the file
                    String[] userData = line.split(","); // Split every line by commas
                    String decryptedUsername = decryptData(userData[0]); // Decrypt the username(first field)
                    if (decryptedUsername.equalsIgnoreCase(newUsername)) { // Compare with input username
                        reader.close();
                        return true; // Username already exists
                    }
                }
                reader.close(); // Close the reader after processing
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false; // Username not found
        }
        
        // Check if the email already exists in the user data file
        private boolean isEmailTaken(String newEmail) {
            try {
                // Open the file "user_data.txt" for reading 
                BufferedReader reader = new BufferedReader(new FileReader("user_data.txt"));
                String line;
                while ((line = reader.readLine()) != null) { // Read every line of the file
                    String[] userData = line.split(","); // Split every line by commas
                    String decryptedEmail = decryptData(userData[1]); // decrypt the email (second field)
                    if (decryptedEmail.equalsIgnoreCase(newEmail)) { // Compare with input email
                        reader.close();
                        return true; // Username already exists
                    }
                }
                reader.close(); // Close the reader after processing 
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false; // Username not found
        }
        
        private HashMap<String, String> loadUserData() {
            HashMap<String, String> userData = new HashMap<>();
            try (BufferedReader reader = new BufferedReader(new FileReader("user_data.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 3) {
                        String decryptedUsername = decryptData(parts[0]);
                        String decryptedEmail = decryptData(parts[1]);
                        userData.put(decryptedEmail + ":" + decryptedUsername, line);  
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return userData;
        }
        
        // Save the updated username and email to the file
        private void saveUpdatedDataToFile(String encryptedCurrentEmail, String encryptedNewEmail, 
                                       String encryptedCurrentUsername, String encryptedNewUsername, 
                                       String encryptedNewBirthday, String encryptedNewGender) {
            try {
                File file = new File("user_data.txt");
                File tempFile = new File("user_data_temp.txt");

                BufferedReader reader = new BufferedReader(new FileReader(file));
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

                String currentUsername = currentUsernameLabel.getText().trim();  // Get current username from label
                String currentEmail = currentEmailLabel.getText().trim();  // Get current email from label
                
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 3) {
                        // Decrypt stored username and email to compare them in plaintext
                        String decryptedStoredUsername = decryptData(parts[0]);
                        String decryptedStoredEmail = decryptData(parts[1]);

                        if (decryptedStoredUsername.equals(currentUsername) && decryptedStoredEmail.equals(currentEmail)) {
                            // Replace with new encrypted values
                            writer.write(encryptedNewUsername + "," + encryptedNewEmail + "," + parts[2] + "," + parts[3] + "," + parts[4] + "," + encryptedNewBirthday + "," + encryptedNewGender);
                         
                        } else {
                            writer.write(line); 
                            }
                    } else {
                        writer.write(line);
                    }
                    writer.newLine();
                }

                reader.close();
                writer.close();

                if (file.delete()) {
                    tempFile.renameTo(file);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    
    
     /* Encryption and decryption methods */

    public static String encryptData(String data) {
        try {
            // Generate a random IV
            byte[] iv = new byte[16];
            new SecureRandom().nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            
            // AES Key Spec
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            
            byte[] encryptedBytes = cipher.doFinal(data.getBytes());
            
            // Combine IV and encrypted data (IV must be known for decryption)
            byte[] combined = new byte[iv.length + encryptedBytes.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encryptedBytes, 0, combined, iv.length, encryptedBytes.length);
            
            return Base64.getEncoder().encodeToString(combined);            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Decrypt method with IV
    public static String decryptData(String encryptedData) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);

            // Extract IV (first 16 bytes)
            byte[] iv = new byte[16];
            System.arraycopy(decodedBytes, 0, iv, 0, iv.length);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            // Extract encrypted content 
            byte[] encryptedBytes = new byte[decodedBytes.length - iv.length];
            System.arraycopy(decodedBytes, iv.length, encryptedBytes, 0, encryptedBytes.length);

            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

            return new String (cipher.doFinal(encryptedBytes));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
     
    // Helper method to convert month number (1-12) to month afer 
    private static String getMonthName(int month) {
            switch (month) {
                case 1: return "January";
                case 2: return "February";
                case 3: return "March";
                case 4: return "April";
                case 5: return "May";
                case 6: return "June";
                case 7: return "July";
                case 8: return "August";
                case 9: return "September";
                case 10: return "October";
                case 11: return "November";
                case 12: return "December";
                default: return ""; // Should not reach here, but it is there just in case
            }
        }
    
    private static int getMonthNumber(String monthName) {
        switch (monthName) {
            case "January": return 1;
            case "February": return 2;
            case "March": return 3;
            case "April": return 4;
            case "May": return 5;
            case "June": return 6;
            case "July": return 7;
            case "August": return 8;
            case "September": return 9;
            case "October": return 10;
            case "November": return 11;
            case "December": return 12;
            default: return -1;
        }
    }
    
}

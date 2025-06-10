
package javaux;

/* Importing all Swing components for creating a graphical user interface (GUI) */
import javax.swing.*;// Includes all swing components such as JFrame, JButton, JLabel, etc.
 
/* Importing AWT(Abstract Window Toolkit) classes for UI customization and event handling */
import java.awt.*; // Includes core graphical components like Color, Font, and Cursor.

/* Importing even handling classes */
//import java.awt.event.ActionEvent; // Represents an action event (ex. button clicks)
//import java.awt.event.ActionListener; // Listens for action events and executes code in response
//import java.awt.event.KeyAdapter; // Provides a default implementation for handling keyboards events
//import java.awt.event.KeyEvent; // Represents a key event (ex. key presses)
//import java.awt.event.MouseAdapter; // Provides a default implementation for handling mouse events 
//import java.awt.event.MouseEvent; // Represents a mouse event (ex. clicks, movement)
import java.awt.event.*; 

/* Importing file handling and I/O operations */
//import java.io.BufferedReader; // Reads text from a file efficiently
//import java.io.BufferedWriter; // Writes text to a file efficiently
//import java.io.FileReader; // Reads data from a file line by line
//import java.io.FileWriter; // Writes data to a file
//import java.io.IOException; // Handle exeptions related to input/output operations
import java.io.*;

/* Importing cryptographic classes for encryption and decryption */
import javax.crypto.*; // Includes classes for encryption, decryption, and key generation
import javax.crypto.spec.SecretKeySpec; // Represents a secret key for symetric encryption

// Importing Base64 encoding and decoding utility
import java.util.Base64; // Provides methods for encoding and decoding Base64 data

// Importing time-related utility
import java.util.Calendar; // provides methods for working with dates and times

// Importing collecitons
import java.util.HashMap; // Implements a data structure for storing key-valued pairs

/* Importing document change event listener */
//import javax.swing.event.DocumentEvent; // Represents changes in a document (ex. text input)
//import javax.swing.event.DocumentListener; // Listens for document changes and reacts accordingly
import javax.swing.event.*;

import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;
import java.util.Date;

// Definition of the SignUp class
public class SignUp {

    // Declare instance variables for GUI components
    private JFrame frame;
    private JButton signUpButton, cancelButton;
    private JLabel signUpLabel, usernameLabel, emailLabel, passwordLabel, birthdayLabel, genderLabel, genderIcon, line1, confirmPasswordLabel, 
                   securityQuestionLabel, passwordMatchIndicator, passwordStrengthIndicator, usernameIcon, emailIcon, securityAnswerIcon;
    private JTextField usernameTxt, emailTxt, securityAnswerTxt;
    private JPasswordField signUpPasswordField, confirmPasswordField;
    private JCheckBox passwordVisibleCB1, passwordVisibleCB2;
    private ImageIcon visible, notVisible, icon;
    private JRadioButton maleButton, femaleButton; 
    private JComboBox<Integer> yearDropdown, dayDropdown;
    private JComboBox<String> monthDropdown, securityQuestionDropdown;
    private MainPage mainPage; // Reference to MainPage
    
    // AES key for encryption and decryption (in real apps, don't hardcode keys)
    private static final String SECRET_KEY = "mysecretkey12345"; // 16-byte key (128 bits)
    
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
            
        //  Constructor for SignUp class
        public SignUp(MainPage mainPage) {
            this.mainPage = mainPage; // Initialize reference
            frame = new JFrame();
            frame.setTitle("Sign Up to ADAMSON AI");
            ImageIcon image = new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\adamson-logo.png"); // Load icon for app
            visible = new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\visible1.png"); // Load Icon for password visibility toggle
            notVisible = new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\notVisible1.png"); // Load Icon for password visibility toggle
            frame.setIconImage(image.getImage());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setSize(490, 555);
            frame.getContentPane().setBackground(Color.decode("#222222"));
            frame.setLocationRelativeTo(null);
            frame.setLayout(null);
 
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            signUpLabel = new JLabel("Sign Up Now");
            signUpLabel.setForeground(Color.WHITE);
            signUpLabel.setFont(new Font(null, Font.TYPE1_FONT, 20));
            signUpLabel.setBounds(50, 20, 130, 50);
            signUpLabel.setHorizontalAlignment(SwingConstants.LEFT);
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            usernameLabel = new JLabel("Username");
            usernameLabel.setForeground(Color.WHITE);
            usernameLabel.setBounds(50, 100, 110, 20);

            usernameTxt = new JTextField();
            usernameTxt.setBounds(200, 100, 200, 20);
            usernameTxt.setToolTipText("Only use alphanumeric values and/or '_', no spaces allowed"); // ToolTip that will give users a hint of instructions
            
            // Creating a Document listener to update tooltips as icons that will give hints to users of their errors and successes in real time                                                                                       
            usernameTxt.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) { // Validate username on text insertion
                    updateUsernameIconAndToolTip();
                }

                @Override
                public void removeUpdate(DocumentEvent e) { // Validate username on text removal
                    updateUsernameIconAndToolTip();
                }

                @Override
                public void changedUpdate(DocumentEvent e) { // Validate username on text style change
                    updateUsernameIconAndToolTip();
                }

                // Method to update the username field's icon and tooltip based on its validity
                private void updateUsernameIconAndToolTip() {
                    String usernameIcn = usernameTxt.getText().trim(); // Get trimmed username text
                    String usernameStatus = getUsernameStatus(); // Validate username
        
                    usernameIcon.setToolTipText(usernameStatus); // Set tooltip with validation message
                    
                    // Set appropiate icon based on username validation
                    if (usernameIcn.isEmpty()) {
                        usernameIcon.setIcon(null); // No icon if empty
                    } else if(isUsernameTaken(usernameIcn) || !usernameIcn.matches("[a-zA-z0-9_]+")) { // Set the "Incorrect Gold Icon" if the username is taken or if the format of the username is incorrect
                        usernameIcon.setIcon(new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\IncorrectGold.png"));
                    } else if (!isUsernameTaken(usernameIcn) && usernameIcn.matches("[a-zA-z0-9_]+")) {  // Set the correct icon if the username is not taken and the format of the username is correct
                        usernameIcon.setIcon(new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\CorrectGold.png"));
                    }
                }
                
                // Method to get validation status of the entered username
                private String getUsernameStatus() {
                    String usernameStatus = usernameTxt.getText().trim();

                    // Check if the username format is correct
                    if (!usernameStatus.matches("[a-zA-Z0-9_]+")) {
                        return "Invalid username entered!";
                    }

                    // Check if the username is already regitered
                    if (isUsernameTaken(usernameStatus)) {
                        return "Username already in use!";
                    }
                    
                    return null; // No issues found with the username
                }
            });
            
            // Label to display validation icon for the username field
            usernameIcon = new JLabel(new ImageIcon());
            usernameIcon.setBounds(414, 100, 20, 20);
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            emailLabel = new JLabel("Email");
            emailLabel.setForeground(Color.WHITE);
            emailLabel.setBounds(50, 140, 110, 20);

            emailTxt = new JTextField();
            emailTxt.setBounds(200, 140, 200, 20);
            emailTxt.getDocument().addDocumentListener(new DocumentListener() { // Creating a Document listener to update tooltip as icons that will give hints to the user of their errors and successes in real-time
                @Override
                public void insertUpdate(DocumentEvent e) { // Validate email on text insert
                    updateEmailIconAndToolTip();
                }

                @Override
                public void removeUpdate(DocumentEvent e) { // Validate email on text removal
                    updateEmailIconAndToolTip();
                }

                @Override
                public void changedUpdate(DocumentEvent e) { // Validate email on text style change
                    updateEmailIconAndToolTip();
                }

                // Method to update the email field's icon and tooltip based on its validity
                private void updateEmailIconAndToolTip() {
                    String emailIcn = emailTxt.getText().trim(); // Get trimmed email text
                    String emailStatus = getEmailStatus(); // Validate email status
                    
                    emailIcon.setToolTipText(emailStatus); // Set tooltip with validation message
                    
                    if (emailIcn.isEmpty()) {
                        emailIcon.setIcon(null); // No icon if empty
                    } else if (isEmailTaken(emailIcn) || !emailIcn.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.(com|ca|org|net|info)$")) { // Set the incorrect icon if the email is taken or if the format of the email is incorrect
                        emailIcon.setIcon(new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\IncorrectGold.png"));
                    } else if (!isEmailTaken(emailIcn) && emailIcn.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.(com|ca|org|net|info)$")) { // Set the correct icon if the email is not taken and he formate of the email is correct
                        emailIcon.setIcon(new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\CorrectGold.png"));
                    }
                }
                
                // Method to get validation status of the entered email
                private String getEmailStatus() {
                    String emailStatus = emailTxt.getText().trim();

                    // Check if the email format is correct
                    if (!emailStatus.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.(com|ca|org|net|info)$")) {
                        return "Invalid email entered!";
                    }

                    // Check if the email is already registered 
                    if (isEmailTaken(emailStatus)) {
                        return "Email already in use";
                    }
                    
                    return null; // No issues found with the email
                }
            });
                       
            // Label to display validation icons for the email field
            emailIcon = new JLabel();
            emailIcon.setBounds(414, 140, 20, 20);
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            passwordLabel = new JLabel("Password");
            passwordLabel.setForeground(Color.WHITE);
            passwordLabel.setBounds(50, 180, 110, 20);

            signUpPasswordField = new JPasswordField();
            signUpPasswordField.setToolTipText("8 to 16 characters long");
            signUpPasswordField.setBounds(200, 180, 200, 20);        
            
            // This checkbox toggles the password visibility
            passwordVisibleCB1 = new JCheckBox();
            passwordVisibleCB1.setBackground(Color.decode("#222222"));
            passwordVisibleCB1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            passwordVisibleCB1.setBounds(410, 180, 20, 20);
            passwordVisibleCB1.setIcon(notVisible); // Default icon (password hidden)
            passwordVisibleCB1.setSelectedIcon(visible); // Icon when checkbox is checked (password visible)
            passwordVisibleCB1.addActionListener(new PasswordVisible1()); // Action listener to toggle visibility
            
            // Label to indicate if passwords strength
            passwordStrengthIndicator = new JLabel("Password must be 8-16 char long"); 
            passwordStrengthIndicator.setBounds(200, 200, 200, 20);
            passwordStrengthIndicator.setForeground(Color.decode("#8A6E4B"));
            
            // Adding a document listener to monitor password field changes
            signUpPasswordField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) { // When text is inserted 
                    updatePasswordStrengthIndicator(); // Check password strength
                    updatePasswordMatchIndicator(); // Check if password match
                }
                
                @Override
                public void removeUpdate(DocumentEvent e) { // When text is removed
                    updatePasswordStrengthIndicator(); // Check password strenght 
                    updatePasswordMatchIndicator(); // Check if passwords match
                }
                 
                @Override
                public void changedUpdate(DocumentEvent e) { // When formatting changes
                    updatePasswordStrengthIndicator(); // Check password strenght
                    updatePasswordMatchIndicator(); // Check if passwords match
                }
                
                // Method to evaluate password strength based on length
                private void updatePasswordStrengthIndicator() {
                    String passwordStrength = new String(signUpPasswordField.getPassword()); // Get entered password
                    
                    // Check password length and update strength indicator
                    if (passwordStrength.isEmpty()) {
                        passwordStrengthIndicator.setText("Password must be 8-16 char long");
                        passwordStrengthIndicator.setForeground(Color.decode("#8A6E4B"));
                    } else if (passwordStrength.length() < 8) {
                        passwordStrengthIndicator.setText("Weak"); // Password is too short
                        passwordStrengthIndicator.setForeground(Color.decode("#A15C4B"));
                    } else if (passwordStrength.length() <= 12) {
                        passwordStrengthIndicator.setText("Moderate"); // Medium length
                        passwordStrengthIndicator.setForeground(Color.decode("#B29A58"));
                    } else {
                        passwordStrengthIndicator.setText("Strong"); // Good length
                        passwordStrengthIndicator.setForeground(Color.decode("#4E7D3A"));
                    }
                }
                
                // Shared method for checking password match
                private void updatePasswordMatchIndicator() {
                    String password = new String(signUpPasswordField.getPassword()); // Get main password
                    String confirmPassword = new String(confirmPasswordField.getPassword()); // Get confirmation password

                    // Compare passwords and update match indicator
                    if (confirmPassword.isEmpty()) {
                        passwordMatchIndicator.setText("Passwords must macth"); // Default message
                        passwordMatchIndicator.setForeground(Color.decode("#8A6E4B")); 
                    } else if (!confirmPassword.equals(password)) {
                        passwordMatchIndicator.setText("Passwords do not match"); // If the passwords do not match
                        passwordMatchIndicator.setForeground(Color.decode("#A15C4B"));
                    } else if (!password.isEmpty()) {
                        passwordMatchIndicator.setText("Passwords Match"); // If passwords match
                        passwordMatchIndicator.setForeground(Color.decode("#4E7D3A"));
                    } else {
                        passwordMatchIndicator.setText(""); // No message when empty
                    }
                }
            });
            
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            confirmPasswordLabel = new JLabel("Confirm Password"); 
            confirmPasswordLabel.setForeground(Color.WHITE);
            confirmPasswordLabel.setBounds(50, 230, 110, 20);

            confirmPasswordField = new JPasswordField();
            confirmPasswordField.setToolTipText("8 to 16 characters long"); // Tolltip with length requirement
            confirmPasswordField.setBounds(200, 230, 200, 20);
            
            // Checkbox for toggling confirm password visibility 
            passwordVisibleCB2 = new JCheckBox();
            passwordVisibleCB2.setBackground(Color.decode("#222222"));
            passwordVisibleCB2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            passwordVisibleCB2.setBounds(410, 230, 20, 20); //+30 diff from above
            passwordVisibleCB2.setIcon(notVisible); // Default hidden icon (password hidden)
            passwordVisibleCB2.setSelectedIcon(visible); // Icon when checkbox is checked (password visible)
            passwordVisibleCB2.addActionListener(new PasswordVisible2()); // Action listener to toggle visibility
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////  
            passwordMatchIndicator = new JLabel("Passwords must macth");
            passwordMatchIndicator.setBounds(200, 250, 200, 20);
            passwordMatchIndicator.setForeground(Color.decode("#8A6E4B"));
            
            // Adding Document listener to check if passwords match dynamically 
            confirmPasswordField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) { // Check password on insert
                    updatePasswordMatchIndicator();
                }
            
                @Override 
                public void removeUpdate(DocumentEvent e) { // Check password on remove
                    updatePasswordMatchIndicator();
                }
                
                @Override
                public void changedUpdate(DocumentEvent e) { // Check password on change
                    updatePasswordMatchIndicator();
                }
                
                // Method to update password match indicator based on user input
                private void updatePasswordMatchIndicator() {
                    String password = new String(signUpPasswordField.getPassword()); // Get main password
                    String passwordMatch = new String(confirmPasswordField.getPassword()); // Get confirmation password
                    
                    if (passwordMatch.isEmpty()) {
                        passwordMatchIndicator.setText("Passwords must macth");
                        passwordMatchIndicator.setForeground(Color.decode("#8A6E4B"));
                    } else if (!passwordMatch.equals(password)) {
                        passwordMatchIndicator.setText("Passwords do not match");
                        passwordMatchIndicator.setForeground(Color.decode("#A15C4B"));
                    } else if (passwordMatch.equals(password)) {
                        passwordMatchIndicator.setText("Passwords Match");
                        passwordMatchIndicator.setForeground(Color.decode("#4E7D3A"));
                    }
                }
            }); 
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////                      
            securityQuestionLabel = new JLabel("Security Question");   
            securityQuestionLabel.setForeground(Color.WHITE); 
            securityQuestionLabel.setBounds(50, 280, 110, 20); //+30 diff from above

            // Creating an array of security questions dropdown
            String[] questions = {"Favourite food", "First pet's name", "Birth's city", "Highschool name", "Favourite color", "Your nickname"};
            securityQuestionDropdown = new JComboBox(questions); // This JComboBox allows users to select a security question
            securityQuestionDropdown.setBounds(200, 280, 110, 20);
            
            // Creating the text field for entering the security answer
            securityAnswerTxt = new JTextField();
            securityAnswerTxt.setBounds(314, 280, 86, 20);
            securityAnswerTxt.setToolTipText("Use alphanumeric values only"); // Tooltip to guide user about the resctrictions 
            securityAnswerTxt.getDocument().addDocumentListener(new DocumentListener() { // Adding a document listener to monitor changes in the security answer text field 
                @Override
                public void insertUpdate(DocumentEvent e) { // When the text is inserted 
                    updateSecurityQuestionIconAndToolTip(); // Update the icon and tooltip based on input validity
                }

                @Override
                public void removeUpdate(DocumentEvent e) { // When text is removed 
                    updateSecurityQuestionIconAndToolTip(); // Update the icon and tooltip
                }

                @Override
                public void changedUpdate(DocumentEvent e) { // When formatting changes 
                    updateSecurityQuestionIconAndToolTip(); // Update the icon and tooltip
                }

                // Method to update the security answer icon and tooltip based on user input 
                private void updateSecurityQuestionIconAndToolTip() {
                    String securityAnswerIcn = securityAnswerTxt.getText().trim(); // Get trimmed user input
                    
                    String securityAnswerStatus = getSecurityAnswerStatus(); // Validate the answer format
                    
                    securityAnswerIcon.setToolTipText(securityAnswerStatus); // Set tooltip to indicate validity
                    
                    /* Determine which icon to display based on the vailidity of input */
                    if (securityAnswerIcn.isEmpty()) { // No iicon if input is empty
                        securityAnswerIcon.setIcon(null); // No icon if input is empty
                    } else if (securityAnswerIcn.matches("[a-zA-Z0-9 ]+")) {
                        securityAnswerIcon.setIcon(new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\CorrectGold.png")); // Set the correct icon if the security answer matches the required format
                    } else {
                        securityAnswerIcon.setIcon(new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\IncorrectGold.png")); // Set the incorrect icon if the security answer entered does not match the required format
                    }
                }

                // Method to check wether the security answer is in the correct format or not
                private String getSecurityAnswerStatus() {
                    String securityAnsStatus = securityAnswerTxt.getText().trim(); // Get the trimmed text input 
                    
                    // Check if input contains only letters, numbers and spaces
                    if (!securityAnsStatus.matches("[a-zA-Z0-9 ]+")) {
                        return "Wrong format used!"; // Return error message if invalid characters are detected 
                    }
                    return null; // Return null if the input format is valid
                }
            });
            
            // Creating the security answer validation icon label
            securityAnswerIcon = new JLabel(); 
            securityAnswerIcon.setBounds(414, 280, 20, 20);
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////   
            birthdayLabel = new JLabel("Birthday");
            birthdayLabel.setForeground(Color.WHITE);
            birthdayLabel.setBounds(50, 320, 110, 20);

            /* Day Dropdown */  
            dayDropdown = new JComboBox<>(); // JComboBox for day selection
            for (int day = 1; day <= 31; day++) { // Loop to add days 1-31
                dayDropdown.addItem(day); // Add each day as an item in the dropdown
            }
            dayDropdown.setSelectedItem(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)); // Set default selection to current day
            dayDropdown.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); 
            dayDropdown.setBounds(200, 320, 50, 20);

            /* Month Dropdown */
            monthDropdown = new JComboBox<>(); // JComboBox for month selection
            for (int month = 1; month <= 12; month++) { // Loop to add months 1-12
                monthDropdown.addItem(getMonthName(month)); //  Add month names using helper Method
            }
            monthDropdown.setSelectedItem(getMonthName(Calendar.getInstance().get(Calendar.MONTH) + 1)); // Set default selection to current month
            monthDropdown.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); 
            monthDropdown.setBounds(250, 320, 90, 20);

            /* Year Dropdown */
            yearDropdown = new JComboBox<>(); // JComboBox for year selection
            int currentYear = Calendar.getInstance().get(Calendar.YEAR); // Loop to add years from current year to 1900
            for (int year = currentYear; year >= 1900; year--) {  // Add each year as an item in the dropdown
                yearDropdown.addItem(year);
            }
            yearDropdown.setSelectedItem(currentYear); // Set default selection to current year
            yearDropdown.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            yearDropdown.setBounds(340, 320, 60, 20);
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            genderLabel = new JLabel("Gender");
            genderLabel.setForeground(Color.WHITE);
            genderLabel.setBounds(50, 360, 110, 20);

            maleButton = new JRadioButton("Male");
            maleButton.setBackground(Color.decode("#222222"));
            maleButton.setForeground(Color.WHITE);
            maleButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            maleButton.setBounds(200, 360, 80, 20);

            femaleButton = new JRadioButton("Female");
            femaleButton.setBackground(Color.decode("#222222"));
            femaleButton.setForeground(Color.WHITE);
            femaleButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            femaleButton.setBounds(290, 360, 80, 20);

            // Creating a button group to ensure only one gender is selected at a time
            ButtonGroup group = new ButtonGroup(); // ButtonGroup is a group of male and female radio buttons
            group.add(maleButton); // Add male button to the group
            group.add(femaleButton); // Add female button to the group
          
            // Adding action listeners to update the gender icon when a selection is made
            maleButton.addActionListener(new GenderIconAction()); // Calls GenderIconAction when "Male" is selected 
            femaleButton.addActionListener(new GenderIconAction()); // Calls GenderIconAction when "Female" is selected
            
            genderIcon = new JLabel();
            genderIcon.setBounds(385, 360, 20, 20);
            genderIcon.setIcon(new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\IconGold16px.png")); // Set default icon of gender to a neutral gender icon
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////     
            line1 = new JLabel("-----------------------------------------------------------------------------------------------");
            line1.setForeground(Color.decode("#8A6E4B")); 
            line1.setBounds(50, 380, 380, 20); 
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            signUpButton = new JButton("Sign Up");
            signUpButton.setContentAreaFilled(false); // Disable default background behavior
            signUpButton.setOpaque(true);  // Make sure the background is opaque to see the color
            signUpButton.setFocusable(false);  // Prevents focus highlight on the button
            signUpButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); 
            signUpButton.setBackground(Color.decode("#876F4D"));
            signUpButton.setForeground(Color.WHITE);
            signUpButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
            signUpButton.setBounds(50, 430, 380, 50);
            signUpButton.addActionListener(e -> saveUserData()); // Calls the saveUserData method when the button is clicked
            //signUpButton.addActionListener(this);
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            icon = new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\iconX1.png"); // Loads the cancel button icon
            cancelButton = new JButton();
            cancelButton.setFocusable(false);
            cancelButton.setContentAreaFilled(false); // Disable default background behavior
            cancelButton.setOpaque(true);  // Make sure the background is opaque to see the color
            cancelButton.setFocusPainted(false); // Prevents focus border painting
            cancelButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            cancelButton.setBackground(Color.decode("#876F4D"));
            cancelButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
            cancelButton.setIcon(icon); // Sets the cancel button icon
            cancelButton.setBounds(410, 30, 30, 30);
            cancelButton.addActionListener(e -> {
                String usernameSignUp = usernameTxt.getText().trim(); // Get trimmed username input
                String emailSignUp = emailTxt.getText().trim(); // Get trimmed email input
                String passwordSignUp = new String(signUpPasswordField.getPassword()); // Get the password input
                String confirmPasswordSignUp = new String(confirmPasswordField.getPassword()); // Get the password confirmation input
                String answerSignUp = securityAnswerTxt.getText().trim(); // // Get trimmed security answer input
                
                // Check if any fields have been filled
                if (!usernameSignUp.isEmpty() || !emailSignUp.isEmpty() || !passwordSignUp.isEmpty() || !confirmPasswordSignUp.isEmpty() || !answerSignUp.isEmpty() || maleButton.isSelected() || femaleButton.isSelected()) {
                    
                    //Prompt a yes or no option for the user
                    int response = JOptionPane.showConfirmDialog(frame, "Do you want to exit without saving?", "Exit without saving", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    
                    if (response == JOptionPane.YES_OPTION) { // If the user confirms exit
                        frame.dispose(); // Close current window
                        new MainPage(new HashMap<>()); // Open the main page, it passes an empty HashMap as an argument because the MainPage has a constructor that accepts HashMap as a parameter
                    } else {
                        return; // Do nothing
                    }
                } else { // If all fields were empty do the follwing
                    frame.dispose(); // Close the current windows
                    new MainPage(new HashMap<>()); // Open the main page
                }
            });
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            
            /* These key listeners will automatically trigger the signUp button if the enter key is pressed in the keyboard */
            
            usernameTxt.addKeyListener(new KeyAdapter() { // Add a key listener to the usernameTxt textfield
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Check if Enter key is pressed
                        signUpButton.doClick(); // Simulate a click on the signup button
                    }
                }
            });

            emailTxt.addKeyListener(new KeyAdapter() { // Add a key lister to the emailTxt textfield
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Check if Enter key is pressed
                        signUpButton.doClick(); // Simulate a click on the signup button
                    }
                }
            });
            
            signUpPasswordField.addKeyListener(new KeyAdapter() { // Add a key lister to the signUpPasswordField textfield
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Check if Enter key is pressed
                        signUpButton.doClick(); // Simulate a click on the signup button
                    }
                }
            });
            
            confirmPasswordField.addKeyListener(new KeyAdapter() { // Add a key listener to the confirmPasswordField textfield
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Check if Enter key is pressed
                        signUpButton.doClick(); // Simulate a click on the signup button
                    }
                }
            });
            
            securityAnswerTxt.addKeyListener(new KeyAdapter() { // Add a key listener to the securityAnswerTxt textfield
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Check if Enter key is pressed
                        signUpButton.doClick(); // Simulate a click on the signup button
                    }
                }
            });
            
            dayDropdown.addKeyListener(new KeyAdapter() { // Add a key listener to the dayDrowpdown JComboBox 
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Check if Enter key is pressed
                        signUpButton.doClick(); // Simulate a click on the signup button
                    }
                }
            });
            
            monthDropdown.addKeyListener(new KeyAdapter() { // Add a key listener to the monthDropdown JComboBox
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Check if Enter key is pressed
                        signUpButton.doClick(); // Simulate a click on the signup button
                    }
                }
            });
            
            yearDropdown.addKeyListener(new KeyAdapter() { // Add a key listener to the yearDropDown JComboBox
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Check if Enter key is pressed
                        signUpButton.doClick(); // Simulate a click on the signup button
                    }
                }
            });
            
            securityAnswerTxt.addKeyListener(new KeyAdapter() { // Add a key listener to the securityAnswerTxt textfield
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Check if Enter key is pressed
                        signUpButton.doClick(); // Simulate a click on the signup button
                    }
                }
            });
            maleButton.addKeyListener(new KeyAdapter() { // Add a key listener to the maleButton JRadioButton 
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Check if Enter key is pressed
                        signUpButton.doClick(); // Simulate a click on the signup button
                    }
                }
            });
            
            femaleButton.addKeyListener(new KeyAdapter() { // Add a key listener to the femaleButton JRadioButton
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Check if Enter key is pressed
                        signUpButton.doClick(); // Simulate a click on the signup button
                    }
                }
            });
            ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            
            // Create a MouseAdapter object to handle mouse events for multiple buttons
            MouseAdapter listener = new MouseAdapter() {
            
                // Method triggered when the mouse is clicked on a button
            @Override
            public void mouseClicked(MouseEvent e) {
                JButton source = (JButton) e.getSource(); // Gets the button that was clicked 
                //System.out.println("You Clicked the mouse");
                source.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Change cursor icon to a hand 
                source.setBackground(Color.WHITE); // Sets the background color of the button to white
                source.setForeground(Color.BLACK); // Set the text color of the button to black
                //signUpButton.setFont(new Font("Arial", Font.PLAIN, 20));
                source.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); // Set the button border to white 
            }

            // Method triggered when a mouse button is pressed in a button
            @Override
            public void mousePressed(MouseEvent e) {
                JButton source = (JButton) e.getSource(); // Get the button that was pressed
                //System.out.println("You Pressed the mouse");
                source.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Changes the cursor icon to a hand
                source.setBackground(Color.WHITE); // Set the background color to white
                source.setForeground(Color.decode("#8A6E4B")); // Changes the text color of the button to Taupe color (Brown gold)
                //signUpButton.setFont(new Font("Arial", Font.PLAIN, 20));
                //source.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); // Set border
            }

            // Method triggered when a mouse button is released after being pressed
            @Override
            public void mouseReleased(MouseEvent e) {
                JButton source = (JButton) e.getSource(); // Gets the button that was released after being pressed
                source.setBackground(Color.decode("#876F4D")); // Set the background color of the button to khaki
                source.setForeground(Color.WHITE); // Set the text color of the button to white
                source.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); // Set the button border to white
            }

            // Method triggered when the button enters the button area
            @Override
            public void mouseEntered(MouseEvent e) {
                JButton source = (JButton) e.getSource(); // Get the button that the mouse entered
                //System.out.println("You Entered the mouse");
                source.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Changes the cursor icon to a hand
                source.setBackground(Color.decode("#514937")); // Set the background color of the button to dark brownish-green
                source.setForeground(Color.WHITE); // Set the text color of the button to white
                //signUpButton.setFont(new Font("Arial", Font.PLAIN, 20));
                source.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); // Set the button border to white 
            }

            // Method triggered when the mouse exits the button area
            @Override
            public void mouseExited(MouseEvent e) {
                JButton source = (JButton) e.getSource(); // Get the button that the mouse exited
                source.setBackground(Color.decode("#876F4D")); // Set the background color of the button to khaki
                source.setForeground(Color.WHITE); // Set the text color of the button to white
                source.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); // Set the button border color to white
            }
        };
        
        // Add same MouseAdapter (listener) to multiple buttons to apply behavior 
        signUpButton.addMouseListener(listener);
        cancelButton.addMouseListener(listener);

        // Add all components to the frame to make them visible
        frame.add(signUpLabel);
        frame.add(usernameLabel);
        frame.add(usernameTxt);
        frame.add(usernameIcon);
        frame.add(emailLabel);
        frame.add(emailTxt);
        frame.add(emailIcon);
        frame.add(passwordLabel);
        frame.add(signUpPasswordField);
        frame.add(confirmPasswordLabel);
        frame.add(confirmPasswordField);
        frame.add(passwordVisibleCB1);
        frame.add(passwordVisibleCB2);
        frame.add(passwordMatchIndicator);
        frame.add(passwordStrengthIndicator);            
        frame.add(birthdayLabel);
        frame.add(dayDropdown);
        frame.add(monthDropdown);
        frame.add(yearDropdown);
        frame.add(genderLabel);
        frame.add(maleButton);
        frame.add(femaleButton); 
        frame.add(genderIcon);
        frame.add(securityQuestionLabel);
        frame.add(securityQuestionDropdown);
        frame.add(securityAnswerTxt);
        frame.add(securityAnswerIcon);
        frame.add(signUpButton);
        frame.add(cancelButton);
        frame.add(line1);
        frame.setVisible(true);
    }

    // Action listener for handling gender selection and updating their gender icon
    private class GenderIconAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //Check if the source of the event is the male button
            if (e.getSource() == maleButton) { // If the source of the event is male then set the gender icon to male
                genderIcon.setIcon(new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\IconMaleGold16px.png"));
            } else if (e.getSource() == femaleButton) { // If the source of the event is female then set the gender icon to female
                genderIcon.setIcon(new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\IconFemaleGold16px.png"));
            }
        }
    }

    // Method to save the user data after validating input fields  
    private void saveUserData() {
        //Convert passwords into string for comparison
        String password1 = new String(signUpPasswordField.getPassword());
        String password2 = new String(confirmPasswordField.getPassword());

        /* Check for empty fields or invalid input */
        if (usernameTxt.getText().isEmpty() || emailTxt.getText().isEmpty() || signUpPasswordField.getPassword().length == 0 || 
            confirmPasswordField.getPassword().length == 0 || securityAnswerTxt.getText().isEmpty() || !maleButton.isSelected() && !femaleButton.isSelected()) { // Check if any field is empty
            JOptionPane.showMessageDialog(null, "Please fill all the fields", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!usernameTxt.getText().trim().matches("[a-zA-Z0-9_]+")) { // Check if the username contains invalid characters
            JOptionPane.showMessageDialog(null, "'Username' not valid [No spaces, and use only alphanumeric values and/or '_']", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!emailTxt.getText().trim().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.(com|ca|org|net|info)$")) { // Check if the email contains invalid characters and format
            JOptionPane.showMessageDialog(null, "Please enter a valid 'Email'", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (password1.length() < 8 || password1.length() > 16) { // Check if the password length is between 8 and 16 characters
            JOptionPane.showMessageDialog(null, "Password must be between 8 and 16 characters long", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!password1.matches("[^\\s]+")) {
            JOptionPane.showMessageDialog(null, "Spacing not allowed for the Password", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!password1.equals(password2)) { // Check if the two passwords match
            JOptionPane.showMessageDialog(null, "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!securityAnswerTxt.getText().trim().matches("[a-zA-Z0-9 ]+")) { // Check if the security answer contains only alphanumeric characters
            JOptionPane.showMessageDialog(null, "'Answer' not valid [Use only alphanumeric values]", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            
             String username = usernameTxt.getText().trim();
            // Check if the username already exists
            if (isUsernameTaken(username)) {
                JOptionPane.showMessageDialog(null, "This username is already taken. Please try another one.", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Stop further processing if username exists
            }

           String email = emailTxt.getText().trim();
            // Check if the Email already exists
            if (isEmailTaken(email)) {
                JOptionPane.showMessageDialog(null, "This Email is already in use.", "Error", JOptionPane.ERROR_MESSAGE);
                return; // Stop further processing if username exists
            }
                        
            String question = (String) securityQuestionDropdown.getSelectedItem(); // Get selected security question by the user
            String answer = securityAnswerTxt.getText().trim(); // Get the security answer input by the user
            
            // Declare birthday outside of the try-catch block so it's accessible later
            String birthday = ""; // Initialize as an empty string or set it to a default value
            
            int day = (int) dayDropdown.getSelectedItem(); // Get the selected day item by the user
            String monthName = (String) monthDropdown.getSelectedItem(); // Get the selected month item by the user
            int year = (int) yearDropdown.getSelectedItem(); // Get the selected year item by the user
            //String birthday = String.format("%d %s %d", day, month, year); // Create a formatted string representing the user's birthday in the format of day, month, year and assigns it ...
                                                                           // ... to the variable birthday %d are placeholders for integer values and %s are placeholders for string values
            // Convert month to a Calendar-compatible month number (0-based)
            int month = getMonthNumber(monthName) - 1;
            
            // Create Calendar object for selected date
            Calendar selectedCal = Calendar.getInstance();
            selectedCal.setLenient(false); // Strict date cheking (e.g. Feb 30 will give error)
            selectedCal.set(year, month, day);
            selectedCal.set(Calendar.HOUR_OF_DAY, 0);
            selectedCal.set(Calendar.MINUTE, 0);
            selectedCal.set(Calendar.SECOND, 0);
            selectedCal.set(Calendar.MILLISECOND, 0);

            try {
                Date selectedDate = selectedCal.getTime();
                
                // Today's date with time stripped
                Calendar todayCal = Calendar.getInstance();
                todayCal.set(Calendar.HOUR_OF_DAY, 0);
                todayCal.set(Calendar.MINUTE, 0);
                todayCal.set(Calendar.SECOND, 0);
                todayCal.set(Calendar.MILLISECOND, 0);
                Date todayDate = todayCal.getTime();
                
                if (selectedDate.after(todayDate)) {
                    JOptionPane.showMessageDialog(null, "Invalid date! Please select today or a past date only.", "Date Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    // Valid date
                    birthday = String.format("%d %s %d", day, monthName, year); // Set the birthday
                    String encryptedBirthday = encryptData(birthday); // Encrypt the birthday
                    //System.out.println("Valid birthday: " + birthday);
                    // Proceed with saving or encrypting
                    
                    // Writing user to 
                    String gender = maleButton.isSelected() ? "Male" : "Female"; // Determine gender based on selection

                /* Encrypt the collected data */
                String encryptedUsername = encryptData(username); // Encrypt the username
                String encryptedEmail = encryptData(email); // Encrypt the email
                String encryptedPassword = encryptData(password1); // Encrypt the password
                String encryptedQuestion = encryptData(question); //Encrypt the security question
                String encryptedAnswer = encryptData(answer); //Encrypt the security question's answer
                //String encryptedBirthday = encryptData(birthday); // Encrypt the birthday
                String encryptedGender = encryptData(gender); // Encrypt the gender

                // Attempt to write encrypted user data to file
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("user_data.txt", true))) {
                    writer.write(encryptedUsername + "," + encryptedEmail + "," + encryptedPassword + "," + encryptedQuestion + "," + encryptedAnswer + "," + encryptedBirthday + "," +encryptedGender); // Write user data
                    writer.newLine(); // add a new line after the data
                    JOptionPane.showMessageDialog(frame, "Sign up successful!"); 
                    frame.dispose();
                    mainPage.refreshLoginInfo(); // Refresh login info after signup
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(frame, "Error saving data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                
                frame.dispose();
                new MainPage(new HashMap<>()); // Create a new instance of the main page
                }
                
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(null, "Invalid date selected!", "Date Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // Action listener for toggling the visibility of the password field
    private class PasswordVisible1 implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //Check if the checkbox is selected for password visibility
            if (passwordVisibleCB1.isSelected()) {
                //Make the password visible by setting echo character to none
                signUpPasswordField.setEchoChar((char)0);
            } else {
                // Hide the password by setting echo character to a bullet
                signUpPasswordField.setEchoChar('\u2022');
            }
        }
    }

    // Action listener for toggling the visibility of the password field
    private class PasswordVisible2 implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Check if the checkBox is selected for password visibility
            if (passwordVisibleCB2.isSelected()) {
                // Make the password visible by setting echo character to none
                confirmPasswordField.setEchoChar((char)0);
            } else {
                // Hide the password by setting echo character to a bullet
                confirmPasswordField.setEchoChar('\u2022');
            }
        }
    }

    // Check if the username already exists in the user data file
    private boolean isUsernameTaken(String username) {
        try {
            // Open the file "user_data.txt" for reading
            BufferedReader reader = new BufferedReader(new FileReader("user_data.txt")); 
            String line; 
            while ((line = reader.readLine()) != null) { // Read every line of the file
                String[] userData = line.split(","); // Split every line by commas
                String decryptedUsername = decryptData(userData[0]); // Decrypt the username(first field)
                if (decryptedUsername.equalsIgnoreCase(username)) { // Compare with input username
                    reader.close(); 
                    return true; // Username already exists
                }
            }
            reader.close(); // Close the reader after processing
        } catch (IOException e) {
            e.printStackTrace(); 
        }
        return false; // Username not found, so it is not taken
    }

    // Method to check if email already exists in the user data file
     private boolean isEmailTaken(String email) {
        try {
            // Opens the file "user_data.txt for reading
            BufferedReader reader = new BufferedReader(new FileReader("user_data.txt"));
            String line;
            while ((line = reader.readLine()) != null) { // Read every line of the file
                String[] userData = line.split(","); // Split every line  by commas
                String decryptedEmail = decryptData(userData[1]); // Decrypt the email (second field)
                if (decryptedEmail.equalsIgnoreCase(email)) { // Compare with input email
                    reader.close();
                    return true; // Username already exists
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // Username not found
    }

    /* AES Encryption and Decryption methods */

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

            // Combine IV and encrypted data (IV Must be known for decryption)
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
      

    // Helper method to convert month number (1-12) to month name
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
                default: return -1; // For safety
            }
        }
            
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//    //Method to decrypt the data //Only used when I want to decrypt data                                                                                                    //                        
//    public void loadUserData() {                                                                                                                                            // 
//        try {                                                                                                                                                               //
//            // Simulating reading an encrypted string from a file (replace with actual file read)                                                                           //                                                                                               
//            String encryptedUsername = "2BcUT0PYhuRfLdXnqnfcY/LpXMRL6UB2cbxXgOwFFZo="; // Example encrypted Username                                                        //           
//            String encryptedEmail = "2QMfhNiTUIQzfs32yB/OO2lKOLaa9YcFW3VzmXL3+/Y="; // Example encrypted Email                                                              //
//            String encryptedPassword = "9+eT71lAUAnX4jvuNoX0Dp9+4FABheR4rX70uy6OEM4="; // Example encrypted Password                                                        //  
//            String encryptedQuestion = "66V+1US6mkscxYfqpC732HjHN1dDQJhYymFjR8Msoog="; // Example encrypted Security Question                                               //  
//            String encryptedAnswer = "0uGZoX+AELqOfjUpxTeOVfwIcUmcP1+8tOMjO49lIbM=";   //Example encrypted Security Question Answer                                         //  
//            String encryptedBirthday = "UjKvOLnH5vxCOlZdFBFoqKbC2iIOqitBeXhToS/btMw="; // Example encrypted Birthday                                                        //                
//            String encryptedGender = "vbiIUqqJwsjvqmCHJ3IrAotO2m1q8URMh+lIoTMMGMY="; // Example encrypted Gender                                                            //                                                                                                                                 
//                                                                                                                                                                            // 
//            // Decrypt the data using the decryptData method                                                                                                                //
//            String decryptedUsername = decryptData(encryptedUsername);                                                                                                      //                           
//            String decryptedEmail = decryptData(encryptedEmail);                                                                                                            // 
//            String decryptedPassword = decryptData(encryptedPassword);                                                                                                      //  
//            String decryptedQuestion = decryptData(encryptedQuestion);                                                                                                      //
//            String decryptedAnswer = decryptData(encryptedAnswer);                                                                                                          //
//            String decryptedBirthday = decryptData(encryptedBirthday);                                                                                                      //       
//            String decryptedGender = decryptData(encryptedGender);                                                                                                          // 
//                                                                                                                                                                            //                
//                                                                                                                                                                            // 
//            // Use the decrypted data as needed                                                                                                                             //
//            System.out.println("Decrypted Username: " + decryptedUsername);                                                                                                 //
//            System.out.println("Decrypted Email: " + decryptedEmail);                                                                                                       //
//            System.out.println("Decrypted Password: " + decryptedPassword);                                                                                                 //
//            System.out.println("Decrypted Question: " + decryptedQuestion);                                                                                                 //
//            System.out.println("Decrypted Answer: " + decryptedAnswer);                                                                                                     //
//            System.out.println("Decrypted Birthday: " + decryptedBirthday);                                                                                                 //            
//            System.out.println("Decrypted Gender: " + decryptedGender);                                                                                                     //                 
//                                                                                                                                                                            //
//        } catch (Exception e) {                                                                                                                                             //
//            e.printStackTrace();                                                                                                                                            //
//        }                                                                                                                                                                   //
//    }                                                                                                                                                                       //                                                    
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
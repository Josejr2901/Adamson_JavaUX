
package javaux;

/* Importing classes for graphical user interface (GUI) components and styling */
//import java.awt.Color; // Allows setting colors for UI components
//import java.awt.Cursor; // Enables changing the mouse cursor appearance
//import java.awt.Font; // Enables changing the mouse cursor appereance
import java.awt.*; /* Importing all AWT classes for UI custumazation and event handling */

/* Importing event handling classes */
//import java.awt.event.ActionEvent; // Represents an action event (ex. Button clicks)
//import java.awt.event.ActionListener; // Listenes for action events and executes code in response
//import java.awt.event.KeyAdapter; // Provides default implementation for handling keyboard events
//import java.awt.event.KeyEvent; // Rrepresents a key event (ex. Key presses)
//import java.awt.event.MouseAdapter; // Provides default implementation for handling mouse events
//import java.awt.event.MouseEvent; // Represents a mouse event (ex. clicks, movment)
import java.awt.event.*; /* Import all event handling classes */

/* Importing collections */
//import java.util.HashMap; // Implements a data structure for storing key-value pairs
//import java.util.Map; // Defines an interface for a key-value pair collections
import java.util.*; /* Import all collections */

/* Importing Swing components for GUI */
//import javax.swing.BorderFactory;  // Creates borders for UI elements
//import javax.swing.JButton; // Represents a clickable button
//import javax.swing.JCheckBox; // Represents a checkbox for user selection
//import javax.swing.JFrame; // Represents the main window of the application
//import javax.swing.JLabel; // Displays text or images in the UI
//import javax.swing.JOptionPane; // Displays pop-up dialogs (ex. alerts, confirmation)
//import javax.swing.JPasswordField; // A text field that hides input (for passwords)
//import javax.swing.JTextField; // A standart text input field
//import javax.swing.ImageIcon; // Loads and displays images in UI components
//import javax.swing.SwingConstants; // Provides constants for UI alignment (ex. CENTER, LEFT)
import javax.swing.*; /* Importing all java swing components for creating Graphical User Interface (GUI) */

/* Importing classes for handling document changes in text fields */
//import javax.swing.event.DocumentEvent; // Represents changes in a document (ex. text input)
//import javax.swing.event.DocumentListener; // Listens for document changes and react accordingly 
import javax.swing.event.*; /* Import all classes for handling document changes in text fields */

/* Importing file handling and I/O operations */
//import java.io.BufferedReader; // Reads text from a file efficiency 
//import java.io.FileReader; // Reads text files line by line
//import java.io.FileWriter; // Writes text data to a file
//import java.io.BufferedWriter; // Writes text to a file efficiency
//import java.io.File; // Represents file and directory pathnames
//import java.io.IOException;
import java.io.*; /* Import all file handling I/O operations */

/* Importing classes for file manipulation using the NIO package */
//import java.nio.file.Files; // Provides utility methods for a file operations (ex. read, write, delete)
//import java.nio.file.Paths; // Represents file paths in a system-indeoendent member 
import java.nio.file.*; /* Import all classes for file manipulation using the NIO package */

/* Importing cryptographic classes for encryption and decryption */
import javax.crypto.Cipher; // Provides encryption and decryption functionality
import javax.crypto.spec.SecretKeySpec; // Represents a secret key for system encryption

/* Importing encoding utility */
import java.util.Base64; // Provides method for encoding and decoding Base64 data

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class MainPage extends Buttons {

    private JFrame frame;
    private JButton signUpButton, loginButton, forgotPasswordButton;
    private JLabel namePageLabel, usernameLabel, usernameIcon, passwordLabel, rememberMeLabel, dontHaveAccLabel, line2;
    private JTextField usernameTxt;
    private JPasswordField mainPasswordField;
    private JCheckBox checkBox, passwordVisibleCheckBox;
    private ImageIcon visible, notVisible, checkBoxIcon, checkedCbIcon;
    private int failedAttempts = 0; //To keep track of how many times the user has attempted to log in with an incorrect password.
    private long blockTime = 0; //Stores the time stamp of when the account was blocked after reaching the maximum number of failed attempts. used in conjuction with BLOCK_DURATION to determine if the user is currently locked out
    private final int MAX_FAILED_ATTEMPTS = 3; //A constant that defines the maximum number of allowed failed login attempts before the account is locked
    private long BLOCK_DURATION = 30000; //Defining the lock duration in milliseconds *1 minute* [60 seconds * 1000 milliseconds = 1 minute]

    private Map<String, User> userData = new HashMap<>(); //Used to store user credentials
    private static final String SECRET_KEY = "mysecretkey12345"; // 16-byte key (128 bits)

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    
    public MainPage(Map<String, String> loginInfoOriginal) {
        
        UIManager.put("Panel.background", Color.decode("#222222"));
        UIManager.put("OptionPane.background", Color.decode("#222222"));

        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        UIManager.put("OptionPane.messageFont", new Font(null, Font.BOLD, 13));
        
        UIManager.put("Button.background", Color.decode("#876F4D"));
        UIManager.put("Button.foreground", Color.WHITE);
        //UIManager.put("Button.font", new Font("Arial", Font.PLAIN, 20));
        UIManager.put("Button.border", BorderFactory.createLineBorder(Color.WHITE));
        UIManager.put("Button.margin", new Insets(20, 20, 20, 20));
        
        userData = new HashMap<>(); // Initializes user data map.
        loadUserData(); // Load user data on startup

    // Check if session exists and auto-login if valid
    User userFromSession = loadSessionData(); // Load the session as a User object
    if (userFromSession != null) {
        // If user is valid, proceed to the logged-in page
        new LoggedInPage(userFromSession); // Pass the User object
        frame.dispose(); // Close the login frame
        return; // Skip the login form
    }
        
        // Regular login UI setup
        frame = new JFrame();
        frame.setTitle("Login to ADAMSON AI");
        
        //creating different objects of the ImageIcon class, initializing it with the image file at the given path. This is for later usage of icons
        ImageIcon image = new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\adamson-logo.png");
        visible = new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\visible1.png");
        notVisible = new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\notVisible1.png");
        checkBoxIcon = new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\checkBox.png");
        checkedCbIcon = new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\checkedCheckBox.png");
        
        frame.setIconImage(image.getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // To ensure the application exits when closed
        frame.setResizable(false); // Prevent window resizing
        frame.setSize(440, 450);
        frame.setLayout(null); // Disable default layout for absolute positioning
        frame.getContentPane().setBackground(Color.decode("#222222"));
        frame.setLocationRelativeTo(null);

        namePageLabel = new JLabel("ADAMSON AI");
        namePageLabel.setForeground(Color.WHITE);
        namePageLabel.setFont(new Font(null, Font.TYPE1_FONT, 30));
        namePageLabel.setBounds(0, 20, 400, 50);
        namePageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        usernameLabel = new JLabel("Username");
        usernameLabel.setForeground(Color.WHITE);
        usernameLabel.setBounds(50, 100, 70, 20);

        usernameTxt = new JTextField();
        usernameTxt.setBounds(150, 100, 200, 20);
        
        // Creating a Document listener to update tooldtips as icons that will give hints to users of their errors and successes in real-time
        usernameTxt.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { // When the text is inserted
                updateUsernameIconAndToolTip();         // Update the icon and tooltip based on input validity
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) { // When the text is removed 
                updateUsernameIconAndToolTip();         // Update the icon and tooltip
            }

            @Override
            public void changedUpdate(DocumentEvent e) { // When formatting chnages 
                updateUsernameIconAndToolTip();          // Update the icon and tooltip
            }

            // Method to update the security answer icon and tooltip based on user input
            private void updateUsernameIconAndToolTip() {
                String usernameIcn = usernameTxt.getText().trim(); // Get trimmed user input
                
                String usernameStatus = getUsernameStatus(); // Validate the username format 
                usernameIcon.setToolTipText(usernameStatus); // Set tooltip to indicate validity
                
                // Determines which icon to display based on the validity of input 
                if(usernameIcn.isEmpty()) {
                    usernameIcon.setIcon(null); // No icon if input is empty
                } else if (usernameExists(usernameIcn)) {
                    usernameIcon.setIcon(new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\CorrectGold.png"));
                } else if (!usernameExists(usernameIcn)) {
                    usernameIcon.setIcon(new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\IncorrectGold.png"));
                }
            }
            
            // I get paid aroud .84 cents per hour for vacation 
            // Method to check if the entered username exists in the user data and return its status
            private String getUsernameStatus() {
                String uNameStatus = usernameTxt.getText().trim(); // Get the trimmed text input 
                User user = userData.get(uNameStatus); // Retrive the user object associated with the entered username from userData (HashMap or similar storage)
                
                // Check if the username exists in the stored data
                if (user == null) {
                    return "Invalid username"; // Return error message if the user is empty
                }
                return null; // If the username is valid, return null indicating no error
            }
        });
        
        usernameIcon = new JLabel(); 
        usernameIcon.setBounds(364, 100, 20, 20);

        passwordLabel = new JLabel("Password");
        passwordLabel.setForeground(Color.WHITE);
        passwordLabel.setBounds(50, 140, 70, 20);

        mainPasswordField = new JPasswordField();
        mainPasswordField.setBounds(150, 140, 200, 20);

        checkBox = new JCheckBox();
        checkBox.setBackground(Color.decode("#222222"));
        checkBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        checkBox.setBounds(45, 180, 20, 20);
        checkBox.setIcon(checkBoxIcon);
        checkBox.setSelectedIcon(checkedCbIcon); 

        rememberMeLabel = new JLabel("Remember me");
        rememberMeLabel.setForeground(Color.WHITE);
        rememberMeLabel.setBounds(70, 180, 100, 20);

        forgotPasswordButton = new JButton("Forgot Password?");
        forgotPasswordButton.setContentAreaFilled(false); // Disable default background behavior
        forgotPasswordButton.setOpaque(true);  // Make sure the background is opaque to see the color
        forgotPasswordButton.setFocusable(false);
        forgotPasswordButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        forgotPasswordButton.setBackground(Color.decode("#876F4D"));           
        forgotPasswordButton.setForeground(Color.WHITE);
        forgotPasswordButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        forgotPasswordButton.setBounds(210, 180, 140, 20);
        forgotPasswordButton.addActionListener(e -> {
            new ForgotPasswordPage();
            frame.dispose();
        });
        
        passwordVisibleCheckBox = new JCheckBox();
        passwordVisibleCheckBox.setBackground(Color.decode("#222222"));
        passwordVisibleCheckBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        passwordVisibleCheckBox.setBounds(360, 140, 20, 20);
        passwordVisibleCheckBox.setIcon(notVisible);
        passwordVisibleCheckBox.setSelectedIcon(visible);
        passwordVisibleCheckBox.addActionListener(new PasswordVisible());
                
        /* These key listeners will automatically trigger the signUp button if the enter key is pressed in the keyboard */
        
        // Add key listener to trigger the loginButton when the Enter key is poressed
        usernameTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Check if the Enter key is pressed
                    loginButton.doClick(); // Simulate a click on the login button
                }
            }
        });

        mainPasswordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) { // Check if the Enter key is pressed
                if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Simulate a click on the login button
                    loginButton.doClick();
                }
            }
        });
        
        checkBox.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Check if the Enter key is pressed
                    loginButton.doClick(); // Simulate a click on the login button
                }
            }
        });
                      
        Buttons buttons = new Buttons();
        loginButton = new JButton("Log in");
        loginButton.setContentAreaFilled(false); // Disable default background behavior
        loginButton.setOpaque(true);  // Make sure the background is opaque to see the color
        buttons.setuploginButton(loginButton);
        loginButton.addActionListener(new LogInAction());

        line2 = new JLabel("----------------------------------------------------------------------------------");
        line2.setForeground(Color.decode("#8A6E4B"));
        line2.setBounds(50, 350, 330, 20);

        dontHaveAccLabel = new JLabel("Don't have an account?");
        dontHaveAccLabel.setForeground(Color.decode("#8A6E4B"));
        dontHaveAccLabel.setBounds(50, 370, 200, 20);
         
        signUpButton = new JButton("Sign Up");
        signUpButton.setContentAreaFilled(false); // Disable default background behavior
        signUpButton.setOpaque(true);  // Make sure the background is opaque to see the color
        signUpButton.setFocusable(false);
        signUpButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signUpButton.setBackground(Color.decode("#876F4D"));
        signUpButton.setForeground(Color.WHITE);
        signUpButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        signUpButton.setBounds(250, 370, 130, 20);
        signUpButton.addActionListener(e -> {
            frame.dispose();
            new SignUp(this);
            //new SignUp(this).loadUserData(); //Used to decrypt data            
        });
        
        // Creates a MouseAdapter object to handle mouse events for multiple buttons
        MouseAdapter listener = new MouseAdapter() {
            
            // Method triggered when the mouse is clicked on a button and it changes the cursor
            @Override
            public void mouseClicked(MouseEvent e) {
                JButton source = (JButton) e.getSource(); // Gets the button that was clicked.
                source.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Changes the cursor icon to a hand
                source.setBackground(Color.WHITE); // Sets the background color of the button to white
                source.setForeground(Color.WHITE); // Sets the text color of the button to white
                source.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); // Sets the button border to white
            }
            
            // Method triggered when a mouse button is pressed in a button. It modifies the button's appearance to provide visual feedback
            @Override
            public void mousePressed(MouseEvent e) {
                JButton source = (JButton) e.getSource(); // Gets the button that was pressed
                source.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Changes the cursor icon to a hand 
                source.setBackground(Color.WHITE); // Sets the background color of the button to white
                source.setForeground(Color.decode("#8A6E4B")); // Sets the text color of the button to a Taupe color (Brown gold)
            }
            
            // Method triggered when the mouse button is released after being pressed
            @Override
            public void mouseReleased(MouseEvent e) {
                JButton source = (JButton) e.getSource(); // Gets the button that was released after being pressed. It updates the button appearance back to its normal state
                source.setBackground(Color.decode("#876F4D")); // Sets background color of the button to khaki
                source.setForeground(Color.WHITE); // Sets the text color of the button to white
                source.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); // Sets the button border to white
            }
            
            // Method triggered when the mouse enters the button area and it provides visual feedback to indicate interactivity
            @Override
            public void mouseEntered(MouseEvent e) {
                JButton source = (JButton) e.getSource(); // Gets the button that the mouse entered
                source.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Changes the cursor icon to a hand 
                source.setBackground(Color.decode("#514937")); // Sets the background color of a button to a dark brownish-green
                source.setForeground(Color.WHITE); // Sets the text color of a button to white
                source.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); // Sets the button border to white
            }
            
            // Method triggered when the mouse exits the button area. It restores the button's default appearance
            @Override
            public void mouseExited(MouseEvent e) {
                JButton source = (JButton) e.getSource(); // Gets the button that the mouse exited
                source.setBackground(Color.decode("#876F4D")); // Sets the background color of the button to khaki
                source.setForeground(Color.WHITE); // Sets the text color of the button to white
                source.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); // Sets the button border to white
            }
        };
        
        // Add same MouseAdapter (listener) to multiple buttons to apply behavior 
        forgotPasswordButton.addMouseListener(listener);
        loginButton.addMouseListener(listener); 
        signUpButton.addMouseListener(listener);
        
        // Add all components to the frame to make them visible
        frame.add(namePageLabel);
        frame.add(usernameLabel);
        frame.add(usernameTxt);
        frame.add(usernameIcon);
        frame.add(passwordLabel);
        frame.add(mainPasswordField);
        frame.add(checkBox);
        frame.add(passwordVisibleCheckBox);
        frame.add(rememberMeLabel);
        frame.add(forgotPasswordButton);
        frame.add(dontHaveAccLabel);
        frame.add(line2);
        frame.add(loginButton);
        frame.add(signUpButton);
        frame.setVisible(true);
    }
    
    // Method to load the account lock status from a file
    private void loadLockStatus() {
        
        // Opens the file "lock_status.txt" for reading isomg a BufferedReader
        try (BufferedReader reader = new BufferedReader(new FileReader("lock_status.txt"))) {
            
            String line = reader.readLine(); // Reads the first line of the file
            
            if (line != null) { // Check if the file contains data
                String[] parts = line.split(",");
                
                if (parts.length == 4) { // Ensures the file contains exactly 2 parts (username and lock timestamp)
                    String lockedUser = parts[0]; // Extracts  the username that was locked
                    long lockedTime = Long.parseLong(parts[1]); // Converts the lock timestamp to a long value 
                    int savedBlockDuration = Integer.parseInt(parts[2]);
                    int savedFailedAttempts = Integer.parseInt(parts[3]);
                    
                    // Check if the lock duration has not yet expired
                    if ((lockedTime + savedBlockDuration) > System.currentTimeMillis()) {
                        blockTime = lockedTime; // Updates the blockTime with the stored lock timestamp
                        failedAttempts = MAX_FAILED_ATTEMPTS; // Sets failed attempts to the maximum limit
                        BLOCK_DURATION = savedBlockDuration;
                        failedAttempts = savedFailedAttempts;
                    } else {
                        // If the lock duration has expired, reset the lock status
                        failedAttempts = 0; // Reset the failed Attempts counter // I can use stored failedAttempts and every 3 value lock it idk
                        blockTime = 0; // Clears the block timestamp
                        BLOCK_DURATION = savedBlockDuration; // sets the block duration to the stored value in the txt file
                        new File("lock_status.txt").delete(); // Deletes the lock status file
                    }
                }
            }
        } catch (IOException e) {
            // Catches and prints an error message if there is an issue reading the file
            e.printStackTrace();
        }
    }
    
    // This method loads user data from "user_data.txt", decrypts it, and stores it in the HashMap (userData)
    private void loadUserData() {
        
        loadLockStatus(); // Load lockout status first to find out if the user is locked at the moment or not
        
        try (BufferedReader reader = new BufferedReader(new FileReader("user_data.txt"))) { // Open the "user_data.txt" file for reading
            String line; // Creates a variable that will temporarily hold the current line being read from the file
                         // It is declared outside the loop so that it can be used within the condition of while
            
            while ((line = reader.readLine()) != null) {  // reader.readLine() reads the next line in the file and assigns it to line
                                                          // The condition checks if the value of line is not null
                                                          // If line is null, it means the end of the file has been reached, and the loop stops
                                                                
                String[] parts = line.split(",");   // Split the line into an array using "," as the separator
                                                    // Each element in "parts" represents an encrypted piece of user data
                if (parts.length >= 7) { // Ensure there are enough elements (username, password, email, question, answer, birthday, gender)
                                         // If there are fewer than 7 values, the line is invalid and should be ignored
                                         
                    // Here we extract user-related information from the file and store it in variables while keeping the data in its encrypted form
                    String encryptedUsername = parts[0];
                    String encryptedPassword = parts[2];
                    String encryptedEmail = parts[1];
                    String encryptedQuestion = parts[3];
                    String encryptedAnswer = parts[4];
                    String encryptedBirthday = parts[5];
                    String encryptedGender = parts[6];

                    // Decrypt all stored user data 
                    String decryptedUsername = decryptData(encryptedUsername);
                    String decryptedPassword = decryptData(encryptedPassword);
                    String decryptedEmail = decryptData(encryptedEmail);
                    String decryptedQuestion = decryptData(encryptedQuestion);
                    String decryptedAnswer = decryptData(encryptedAnswer);
                    String decryptedBirthday = decryptData(encryptedBirthday);
                    String decryptedGender = decryptData(encryptedGender);
                    
                    // Ensure username and password are successfully decrypted before storing the user
                    if (decryptedUsername != null && decryptedPassword != null) { // Create a new User object using the decrypted values
                        User user = new User(decryptedUsername, decryptedPassword, decryptedEmail, decryptedQuestion, decryptedAnswer, decryptedBirthday, decryptedGender);
                        userData.put(decryptedUsername, user); // Store the User object in the HashMap with the username as the key
                    }
                }
            }
        } catch (IOException e) {
            // Handle file reading error
            JOptionPane.showMessageDialog(frame, "Error loading user data: " + e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private class PasswordVisible implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (passwordVisibleCheckBox.isSelected()) {
                mainPasswordField.setEchoChar((char)0);
            } else {
                mainPasswordField.setEchoChar('\u2022');
            }
        }
    }
    
    // Class to handle login button click events
    private class LogInAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
                                    
            // Retrieve the entered username and password in the JTextFields by the user
            String username = usernameTxt.getText().trim();
            String password = String.valueOf(mainPasswordField.getPassword());
            
            // To check if the account is currently locked due to failed attempts and if it is then display the following message
            if (isBlocked()) {
                long timeLeft = (blockTime + BLOCK_DURATION - System.currentTimeMillis()) / 1000; // Calculating how much time there's left to unblock action
                long minutes = timeLeft / 60; // calculate minutes, this extract minutes from total seconds
                long seconds = timeLeft % 60; // calculate remaining seconds, this extract remaining seconds after minutes
                JOptionPane.showMessageDialog(frame, "Account is locked. Please try again in " + minutes + " minute(s) and " + seconds + " second(s)", "Wait", JOptionPane.INFORMATION_MESSAGE);
                return; // Exit the function, preventing further login attempts
            }
            
            // Fetch the stored user data for the given username, so it can be used to verify the existance of an account an login
            // This is a looked up operation that searches for the given username in the HashMap.
            User user = userData.get(username);
            
            //If statement to check the username input, and if is not found in the HashMap then it gives the following message
            if (user == null) {
                JOptionPane.showMessageDialog(frame, "Invalid username.", "Error", JOptionPane.ERROR_MESSAGE); //
            } else if (user != null && password.isEmpty()) { // If the username exists but the password field is empty do the following
                JOptionPane.showMessageDialog(frame, "Please enter the password to proceed", "Enter Password", JOptionPane.INFORMATION_MESSAGE);
            } else if (!user.getPassword().equals(password)) { // Comparing the password from the HashMap with the latest password entered by user
                failedAttempts++; // Increment failed attempts counter if user enters right username but wrong password
                
                // If statment in case the failed attempts reach the maximum, then the following will be done:
                if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
                    blockTime = System.currentTimeMillis(); // Record the current time and date as a unix timestamp when the account is blocked, by doin this the program can calculate how...
                                                            // long  the account has been locked. Without this timestamp, it wouldn't know when to allow the user to try logging in again.
                    BLOCK_DURATION *= 2; // This is two double the time duration of each block
                    
                    saveLockStatus(username, blockTime); // Save the lock status to a file so the block time persists even after closing the program
                    
                    JOptionPane.showMessageDialog(frame, "Too many failed attempts. Login action is locked for " + BLOCK_DURATION / 60000 + " minute(s)", "Error", JOptionPane.ERROR_MESSAGE);
                // If the password is incorrect and the maximum attempts have been reached already
                } else {
                    // If the password is incorrect but the maximum attempts have not been reached yet
                    JOptionPane.showMessageDialog(frame, "Invalid Password. Attempts left: " + (MAX_FAILED_ATTEMPTS - failedAttempts), "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
                else { // This statement is in case both the username and password entered by the user are correct and do exist in the HashMap
                    
                        failedAttempts = 0; // Reset count of failed Attempts to - when the login is successful 
                        blockTime = 0;
                        BLOCK_DURATION = 60000; // Optional: reset block duration on successful login
 
                        File lockFile = new File("lock_status.txt");
                        if (lockFile.exists()) {
                            lockFile.delete();
                        }
                    
                    // Statement to check if the checkbox is currently selected
                    if (checkBox.isSelected()) {
                        String encryptedUsername = encryptData(username); // If it is selected then encrypt the entered username and store it as a String for later usage of it
                        String encryptedPassword = encryptData(password); // If it is selected then encrypt the entered password and store it as a String for later usage of it 
                        
                        // Try-catch statement to save encrypted credentials to a file called "session.txt"
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter("session.txt"))) { //BufferedWriter is a Java class write characters, arrays or strings to a file
                            writer.write(encryptedUsername + "\n" + encryptedPassword);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                    
                    // After checking if the checkbox was selected, open a new frame and close the current frame.
                    new SecurityQuestionLogInPage(user);
                    frame.dispose();
                }
            }
         
        // Function to save the lock status of an account. This is to preserve the lockout information even after the program is closed
        private void saveLockStatus(String username, long blockTime) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("lock_status.txt"))) {
                writer.write(username + "," + blockTime + "," + BLOCK_DURATION + "," + failedAttempts); // Save username, lock time, and block duration
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to check if the account is currently blocked due to too many failed login attempts
    private boolean isBlocked() {
        if (failedAttempts >= MAX_FAILED_ATTEMPTS) { // To check if the number of failed attempts has reached the maximum allowed
            long timeLeft = (blockTime + BLOCK_DURATION - System.currentTimeMillis()) / 1000;
            if (timeLeft > 0) { // This determines if there's still time left in the lock duration, and if there is, then it returns true and the  account stays locked
                return true;
            } else {                    // In the case that the time of lock duration has expired
                failedAttempts = 0;     // The failed attempts and ...
                blockTime = 0;          // blockTime is reset to 0, so now the user can try to login again
                //new File("lock_status.txt").delete(); // Furthermore, the txt file that contained the lock status is deleted to unlock the login possibility
                return false;                       // This is so the isBlocked() method is not activated again when clicking the signUpButton 
            }
        }
        return false; // In case the failed attempts are below the max limit, the account is not blocked, and the user can keep trying to login
    }
     
    // Method used to load session data from a file, this is used to remember the user info when the checkBox had been selected and succesfully logged in
    private User loadSessionData() {
        try {
            // Check if the session file exists
            if (!Files.exists(Paths.get("session.txt"))) {
                return null; // If the file doesn't exist, there's no session
            }

            // If the file exists, read the session data (encrypted username)
            try (BufferedReader reader = new BufferedReader(new FileReader("session.txt"))) {
                String encryptedUsername = reader.readLine(); //Read the first line
                if (encryptedUsername != null) { // Decrypt the username if it does exist                    
                    String username = decryptData(encryptedUsername); 
                    if (username != null && userData.containsKey(username)) { // Check if the decrypted username exists in the user data map 
                        return userData.get(username); // Return the User object if valid
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // No session or invalid session
}

    /* AES Encryption method and Decryption Methods */
    
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
    
    // Method to check if the username entered by the user exists
    private boolean usernameExists(String username) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("user_data.txt")); // Open the datafile for reading
            String line;
            
            while ((line = reader.readLine()) != null) { // Read each line of the file to check if the username exists 
                String[] userData = line.split(","); // This is to split the stored data and differentiate the username from other data information
                
                String decryptedUsername = decryptData(userData[0]); // This is to decrypt only the first element in the array to find the username
                
                // This is for when the username decrypted matches the input of the user, so it means the username do exist
                if (decryptedUsername.equals(username)) {
                    reader.close();
                    return true; // Username exists
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // Username not found
    }
    
    // This method is to refresh the login information for when the data is updated
    public void refreshLoginInfo() {
        // Logic to refresh the login information (e.g., reloading user data)
        System.out.println("Login information refreshed!");
        loadUserData(); // Reload the user data to ensure it's up-to-date
    }
    
 }

// Create a file just for the failed attempts that has to be updated every time a new attempt pf login is done unsuccessful 
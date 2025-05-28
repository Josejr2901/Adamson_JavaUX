
package javaux;

/* Importing Swing components for GUI development */
import javax.swing.*; // Imports all Swing UI components (e.g., JFrame, JButton, JLabel)
import java.awt.*; // Imports AWT classes for UI elements (e.g., Color, Font, Cursor)

/* Importing event hadling classes */
//import java.awt.event.ActionEvent; // Represents an action event (e.g, button clicks)
//import java.awt.event.ActionListener; // Listens for action events and executes code in response
//import java.awt.event.KeyAdapter; // Provides default implementation for handling keyboard events
//import java.awt.event.KeyEvent; // Represents a key event (e.g., key presses)
//import java.awt.event.MouseAdapter; // Provides default implementation for handling mouse events
//import java.awt.event.MouseEvent; // Represents a mouse event (e.g., clicks, movement)
import java.awt.event.*;

/* Importing file handling and I/O operations */
import java.io.*; // Imports all standart I/O classes (e.g., BufferedReader, FileReader, FileWriter)
//import java.util.Base64; // Provides methods for encoding and decoding Base64 data
//import java.util.HashMap; // Implements a data structure for storing key-value pairs
import java.util.*;

/* Importing cryptographic classes for encryption and decryption */
import javax.crypto.Cipher; // Provides encryption and decryption functionality
import javax.crypto.spec.SecretKeySpec; // Represents a secret key for symetric encryption

/* Importing classes for handling document changes in text fields */
import javax.swing.event.DocumentEvent; // Represents changes in a document (e.g., text input)
import javax.swing.event.DocumentListener; // Listens for document changes and reacts accordingly

import javax.crypto.spec.*;
import java.security.SecureRandom;

public class ChangeForgotPasswordPage {
    
    private JFrame frame;
    private JLabel usernameLabel, emailLabel, resetPasswordLabel, newPasswordLabel, confirmNewPasswordLabel, securityQuestionLabel, questionLabel, answerLabel, emailIcon, answerIcon;
    private JTextField emailTxt, securityAnswerTxt;
    private JCheckBox passwordVisibleCB1, passwordVisibleCB2;
    private JPasswordField newPasswordField, confirmNewPasswordField;
    private JButton resetPasswordButton, cancelButton;
    private int failedAttempts = 0;
    private long blockTime = 0;
    private final int MAX_FAILED_ATTEMPTS = 3;
    private int BLOCK_DURATION = 30000;
    
    // AE key for encryption and decryption 
    private static final String SECRET_KEY = "mysecretkey12345"; // 16-byte key (128 bits)
    
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    
    // Constructor to initialize the change password window
    ChangeForgotPasswordPage(String username, String email, String securityQuestion, String answer) {
               
        loadLockStatus();
        
        /* Retrieve user detail from user objkect */
        String currentSavedUsername = username;
        String currentSavedEmail = email;
        String currentSavedSecurityQuestion = securityQuestion;
        String currentSavedSecurityQuestionAnswer = answer;
        
        frame = new JFrame("Change Password - ADAMSON AI");
        frame.setSize(420, 440);
        frame.setResizable(false);
        frame.getContentPane().setBackground(Color.decode("#222222"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        
        // Set the application icon
        ImageIcon image = new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\adamson-logo.png");
        frame.setIconImage(image.getImage());
        
        // Load visibility icons for password fields
        ImageIcon visible = new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\visible1.png");
        ImageIcon notVisible = new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\notVisible1.png");
        
        resetPasswordLabel = new JLabel("Change Password of " + username);
        resetPasswordLabel.setForeground(Color.WHITE);
        resetPasswordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        resetPasswordLabel.setFont(new Font(null, Font.BOLD, 18));
        resetPasswordLabel.setBounds(0, 20, 400, 50);
        
        securityQuestionLabel = new JLabel("Security Quesion:");
        securityQuestionLabel.setForeground(Color.decode("#876F4D"));
        securityQuestionLabel.setBounds(50, 90, 120, 20);
        
        questionLabel = new JLabel(securityQuestion);
        questionLabel.setForeground(Color.decode("#876F4D"));
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        questionLabel.setBounds(180, 90, 150, 20);
        
        answerLabel = new JLabel("Security Answer");
        answerLabel.setForeground(Color.WHITE);
        answerLabel.setBounds(50, 130, 120, 20);
        
        securityAnswerTxt = new JTextField();
        securityAnswerTxt.setBounds(180, 130, 150, 20);
        
        // Creating a Document listener to update tooltips as icons that will give hints to users of their errors and successes in real time   
        securityAnswerTxt.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { // Validate username on text insertion
                UpdateAnswerIconAndToolTip(); 
            }

            @Override
            public void removeUpdate(DocumentEvent e) { // Validate username on text removal 
                UpdateAnswerIconAndToolTip();
            }

            @Override
            public void changedUpdate(DocumentEvent e) { // Validate username on text style change
                UpdateAnswerIconAndToolTip();
            }

            // Method to update the username field's icon and tooltip based on its validity
            private void UpdateAnswerIconAndToolTip() {
                String answerIcn = securityAnswerTxt.getText().trim(); // Get trimmed security answer text
                String answerStatus = getAnswerStatus(); // Validate the security answer
                
                answerIcon.setToolTipText(answerStatus); // Set tooltip with validation message
                
                // Set appropiate icon based on the security answer validation
                if (answerIcn.isEmpty()) { // No icon if emptu
                    answerIcon.setIcon(null); 
                } else if (answerIcn.equalsIgnoreCase(answer)) { // CorrectGold icon if the answer entered by the user matches the answer saved in the text file
                    answerIcon.setIcon(new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\CorrectGold.png")); 
                } else if (!answerIcn.equalsIgnoreCase(answer)) { // IncorrectGold icon if the answer entered by the user does not match the answer saved in the text file
                    answerIcon.setIcon(new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\IncorrectGold.png"));
                }
            }

            // Method to check wether the security answer is the correct format
            private String getAnswerStatus() {
                String ansStatus = securityAnswerTxt.getText().trim(); // Get the trimmed text input
                
                // Check if input by the user matches the answer in the text file
                if (!ansStatus.equals(answer)) {
                    return "Wrong answer!"; // Return error message if invalid characters are detected
                }
                return null; // Return null if the input format is valid
            }
       });
                
        answerIcon = new JLabel();
        answerIcon.setBounds(344, 130, 20, 20);
        
        emailLabel = new JLabel("Re-Enter your Email");
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setBounds(50, 170, 120, 20);

        emailTxt = new JTextField();
        emailTxt.setBounds(180, 170, 150, 20);
        
        // Creating a Document listener to update tooltip as icons that will give hints to the user of their errors and successes in real-time
        emailTxt.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { // Validate email on text insert
                UpdateEmailIconAndToolTip();
            }

            @Override
            public void removeUpdate(DocumentEvent e) { // Validate email on text removal
                UpdateEmailIconAndToolTip();
            }

            @Override
            public void changedUpdate(DocumentEvent e) { // Validate email on text style change
                UpdateEmailIconAndToolTip();
            }

            // Method to update the email field's icon and tooltip based on it's validity
            private void UpdateEmailIconAndToolTip() {
                String emailIcn = emailTxt.getText().trim(); // Get trimmed email text 
                String emailStatus = getEmailStatus(); // validate email status
                
                emailIcon.setToolTipText(emailStatus); // Set tooltip with validation message
                
                if (emailIcn.isEmpty()) { // No icon if empty
                    emailIcon.setIcon(null);
                } else if (emailIcn.equalsIgnoreCase(email)) { // Set the "CorrectGoldIcon" if the security answer entered by the user matches the one saved in the file
                    emailIcon.setIcon(new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\CorrectGold.png"));
                } else if (!emailIcn.equalsIgnoreCase(email)) {// Set the "IncorrectGoldIcon" if the security answer entered by the user does not match the one saved in the file
                    emailIcon.setIcon(new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\IncorrectGold.png"));
                }
            }

            // Method to get validation status of the entered email
            private String getEmailStatus() {
                String emailStat = emailTxt.getText().trim();
                
                // Check if the email entered by the user does not matches the email saved in the file
                if (!emailStat.equals(email)) {
                    return "Wrong email";
                }
                return null; // No issues found with the email
            }
        });
                
        // Label to display validation icons for the email fields 
        emailIcon = new JLabel();
        emailIcon.setBounds(344, 170, 20, 20);
        
        newPasswordLabel = new JLabel("New Password");
        newPasswordLabel.setForeground(Color.WHITE);
        newPasswordLabel.setBounds(50, 210, 120, 20);
        
        newPasswordField = new JPasswordField();
        newPasswordField.setToolTipText("8 to 16 characters long");
        newPasswordField.setBounds(180, 210, 150, 20);
        
        passwordVisibleCB1 = new JCheckBox();
        passwordVisibleCB1.setIcon(notVisible);
        passwordVisibleCB1.setBackground(Color.decode("#222222"));
        passwordVisibleCB1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        passwordVisibleCB1.setBounds(340, 210, 20, 20);
        passwordVisibleCB1.setSelectedIcon(visible);
        passwordVisibleCB1.addActionListener(new PasswordVisible1());
        
        confirmNewPasswordLabel = new JLabel("Confirm Password");
        confirmNewPasswordLabel.setForeground(Color.WHITE);
        confirmNewPasswordLabel.setBounds(50, 250, 120, 20);
        
        confirmNewPasswordField = new JPasswordField();
        confirmNewPasswordField.setToolTipText("8 to 16 characters long");
        confirmNewPasswordField.setBounds(180, 250, 150, 20);
        
        passwordVisibleCB2 = new JCheckBox();
        passwordVisibleCB2.setBackground(Color.decode("#222222"));
        passwordVisibleCB2.setIcon(notVisible);
        passwordVisibleCB2.setSelectedIcon(visible);
        passwordVisibleCB2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        passwordVisibleCB2.setBounds(340, 250, 20, 20);
        passwordVisibleCB2.addActionListener(new PasswordVisible2()); 
        
        resetPasswordButton = new JButton("Reset Password");
        resetPasswordButton.setContentAreaFilled(false);
        resetPasswordButton.setOpaque(true);
        resetPasswordButton.setFocusable(false);
        resetPasswordButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        resetPasswordButton.setBounds(50, 300, 310, 30);
        resetPasswordButton.setBackground(Color.decode("#876F4D"));
        resetPasswordButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        resetPasswordButton.setForeground(Color.WHITE);
        resetPasswordButton.addActionListener(e -> {
            
            // Retrieve and trim input values from text fields
            String currentEmail = emailTxt.getText().trim();
            String currentAnswer = securityAnswerTxt.getText().trim();
            String newPassword = new String(newPasswordField.getPassword());
            String reEnterPassword = new String(confirmNewPasswordField.getPassword());
            
            // Check if the account is locked due to multiple failed attemprs
            if (isBlocked()) {
                long timeLeft = (blockTime + BLOCK_DURATION - System.currentTimeMillis()) / 1000; // Calculate remaining lock time
                long minutes = timeLeft / 60; // Calculate minutes, this extract  minutes from the total seconds
                long seconds = timeLeft % 60; // Calculate remaining seconds, this extract remaining seconds after minutes
                JOptionPane.showMessageDialog(frame, "Account is locked. Please try again in " + minutes + " minute(s) and " + seconds + " second(s)", "Wait", JOptionPane.INFORMATION_MESSAGE); // Alert the user 
                return; // Stop further execution
            }
            
            // Ensure all fields are filled
            if (currentEmail.isEmpty() || currentAnswer.isEmpty() || newPassword.isEmpty() || reEnterPassword.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields", "Error", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // Ensure new password and confirmation match
            if (!newPassword.equals(reEnterPassword)) {
                JOptionPane.showMessageDialog(frame, "Password do not match", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
            if  (newPassword.length() < 8 || newPassword.length() > 16) {
                JOptionPane.showMessageDialog(frame, "Password must be between 8 and 16 characters", "Error", JOptionPane.INFORMATION_MESSAGE);
            }
            
            // Validate that password length must be between 8 and 16 characters
            else if (!currentEmail.equals(currentSavedEmail) || !currentAnswer.equals(currentSavedSecurityQuestionAnswer)) {
                JOptionPane.showMessageDialog(frame, "Invalid information entered. Attempts left: " + (MAX_FAILED_ATTEMPTS - failedAttempts - 1), "Warning", JOptionPane.WARNING_MESSAGE);
                
                failedAttempts++; // Increase the failed attempt counter
                
                // Lock the account if the maximum number of failed attempts is reached
                if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
                    blockTime = System.currentTimeMillis(); // Record the lock time
                    
                    BLOCK_DURATION *= 2;
                    
                    saveLockStatus(currentSavedUsername, blockTime); // Save the lock status to prevent further attempts
                    
                    JOptionPane.showMessageDialog(frame, "Too many failed attempts. This action is locked for " + BLOCK_DURATION / 60000 + " minute(s)", "Error", JOptionPane.ERROR_MESSAGE);                    
                }
            }
                        
            else {
                failedAttempts = 0;
                blockTime = 0;
                BLOCK_DURATION = 60000;
                
                File lockFile = new File("lock_forgot_password_reset_status.txt");
                if (lockFile.exists()) {
                    lockFile.delete();
                }
                
                // Loads existing user data from file
                HashMap<String, String> userData = loadUserData();
                
                // Create a unique key using encreypted email and security answer
                String key = currentEmail + ":" + currentAnswer;
                
                // Check if the provided email and answer match any stored user data
                if (userData.containsKey(key) && newPassword.equals(reEnterPassword)) {
                    
                    // Encrypt new password before saving
                    String encryptedNewPassword = encryptData(newPassword);
                    String encryptedEmail = encryptData(currentEmail);
                    
                    // Save new password to file
                    saveNewPasswordToFile(encryptedEmail, encryptedNewPassword);
                    
                    // Show success message
                    JOptionPane.showMessageDialog(frame, "Password reset successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                    
                    frame.dispose();
                    new MainPage(userData);
                }
            }
        });
        
        cancelButton = new JButton("Cancel");
        cancelButton.setContentAreaFilled(false);
        cancelButton.setOpaque(true);
        cancelButton.setFocusable(false);
        cancelButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cancelButton.setBounds(50, 340, 310, 30);
        cancelButton.setBackground(Color.decode("#876F4D"));
        cancelButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        cancelButton.setForeground(Color.WHITE);
        
        // Adding an action listener to the cancelButton to handle user interaction
        cancelButton.addActionListener(e -> {
         
            String securityQuestionCB = securityAnswerTxt.getText().trim(); // Retrieving and triming input from the security answer text field
            String emailCB = emailTxt.getText().trim(); // Retrieving and triming input from the email text fields
            String newPassword = new String(newPasswordField.getPassword()); // Retrieving the new password entered by the user (converting char array to strin)
            String confirmNewPassword = new String(confirmNewPasswordField.getPassword()); // Retrieving the confirmation password entered by the user

            // Checking if any of the fields contain user input
            if (!securityQuestionCB.isEmpty() || !emailCB.isEmpty() || !newPassword.isEmpty() || !confirmNewPassword.isEmpty()) {
                
                // Displaying a confirmatio dialog box to confirm ecisting without saving
                int response = JOptionPane.showConfirmDialog(frame, "Do you want to exit without saving?", "Exit whithout saving", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                
                // Checking if the user clicked "Yes" to exit
                if (response == JOptionPane.YES_OPTION) {
                    frame.dispose(); // Closes the current window
                    new MainPage(new HashMap<>()); // Opens the MainPage with an empty HashMap
                } else {
                    return; // If the user clicks "No", the functio stops and does nothing
                }
                
            } else {
                // If all fields are empty, directly close the window and open the MainPage
                frame.dispose(); // Closes the current frame
                new MainPage(new HashMap<>()); // Opens the MainPage without needing confirmation
            }
        });
        
        /* These key listeners will automatically trigger the resetPasswordButton button if the enter key is pressed in the keyboard */
        emailTxt.addKeyListener(new KeyAdapter() { // Add key listener to the emailTxt texfield
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Check if Enter key is pressed
                    resetPasswordButton.doClick(); // Simulate click on the resetPasswordButton button
                }
            }            
        });
        
        securityAnswerTxt.addKeyListener(new KeyAdapter() { // Add a key listener to the securityAnswerTxt textfield
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Check if Enter key is pressed
                    resetPasswordButton.doClick(); // Simulate a click on the resetPasswordButton button
                }
            }
        });
        
        newPasswordField.addKeyListener(new KeyAdapter() { // Add a key listener to the newPasswordField to the JPasswordField
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Check if the Enter key is pressed
                    resetPasswordButton.doClick(); // Simulate a click on the resetPasswordButton button
                }
            }
        });
        
        confirmNewPasswordField.addKeyListener(new KeyAdapter() { // Add a key listener to the confirmNewPasswordField JPasswordField
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Checl if the Enter key is pressed
                    resetPasswordButton.doClick(); // Simulate a click on the resetPasswordButton button
                }
            }
        });
        
        // Create a MouseAdapter object to handle mouse events for multiple buttons
        MouseAdapter listener = new MouseAdapter() {
        
            // Method triggered when the mouse is clicked on a button
            @Override
            public void mouseClicked(MouseEvent e) {
                JButton source = (JButton) e.getSource(); // Gets the button that was clicked
                source.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Changes the cursor icn to a hand
                source.setBackground(Color.WHITE); // Sets the background color of the button to white
                source.setForeground(Color.BLACK); // Sets the text color of the button to balck
                source.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); // Sets the button border to white
            }
            
            // Method triggered when a mouse is pressed in a button
            @Override
            public void mousePressed(MouseEvent e) {
                JButton source = (JButton) e.getSource(); // Gets the button that was pressed 
                source.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Set the icon of the cursor to a hand
                source.setBackground(Color.WHITE); // Set the background of the button to white
                source.setForeground(Color.decode("#8A6E4B")); // Set the text color of the button to Taupe color (Brown gold)
            }
            
            // Method triggered when a mouse enters the button area of a button
            @Override
            public void mouseEntered(MouseEvent e) {
                JButton source = (JButton) e.getSource(); // Get the button that the mouse entered
                source.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // The cursor icon was changed to a hand
                source.setBackground(Color.decode("#514937")); // Set background color of the button to a dark brownish-green 
                source.setForeground(Color.WHITE); // Set the text color of the button to white
                source.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); // Set the button border to white
            }
            
            // Method triggered when the mouse exits the button area
            @Override
            public void mouseExited(MouseEvent e) {
                JButton source = (JButton) e.getSource(); // Gets the button that the mouse exited
                source.setBackground(Color.decode("#876F4D")); // Sets the background color of the button to khaki
                source.setForeground(Color.WHITE); // Sets the color of the button to white 
                source.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); // Set the button
            }
        };
        
        // Add same MouseAdapter (listener) to multiple button
        resetPasswordButton.addMouseListener(listener);
        cancelButton.addMouseListener(listener);
        
        // Add all components to the flame to make them visible
        frame.add(emailLabel);
        frame.add(emailTxt);
        frame.add(emailIcon);
        frame.add(securityQuestionLabel);
        frame.add(questionLabel);
        frame.add(answerLabel);
        frame.add(securityAnswerTxt);
        frame.add(answerIcon);
        frame.add(resetPasswordLabel);
        frame.add(newPasswordLabel);
        frame.add(passwordVisibleCB1);
        frame.add(newPasswordField);
        frame.add(confirmNewPasswordLabel);
        frame.add(passwordVisibleCB2);
        frame.add(confirmNewPasswordField);
        frame.add(resetPasswordButton);
        frame.add(cancelButton);
        frame.setVisible(true);
    }
    
    // Method to load the lock status from a file 
    private void loadLockStatus() {
        
        // Opens the file "lock_forgot_password_reset_status.txt" for reading using a BufferedReader
        try (BufferedReader reader = new BufferedReader (new FileReader("lock_forgot_password_reset_status.txt"))) {
            
            String line = reader.readLine(); // Reads the first line of the file
            
            if (line != null) { // Check if the file contains data
                String[] parts = line.split(","); // Splits the line into an array using a comma as a delimeter
                
                if (parts.length == 4) { // Ensures the file contains exactly 2 parts (username and lock timestamp)
                    String lockedUser = parts[0]; // Extracts the username that was locked
                    long lockedTime = Long.parseLong(parts[1]); // Converts the lock timestamp to a long value
                    int savedBlockDuration = Integer.parseInt(parts[2]);
                    int savedFailedAttempts = Integer.parseInt(parts[3]);
                    
                    // Check if the lock duration has not yet expired
                    if ((lockedTime + savedBlockDuration) > System.currentTimeMillis()) {
                        blockTime = lockedTime; // Updates the blockTime with the stored lock timestamp
                        failedAttempts = MAX_FAILED_ATTEMPTS; // Sets failed attempts to the maximum limit
                        BLOCK_DURATION = savedBlockDuration;
                    } else {
                        // If the lock duration has expired, reset the lock status
                        failedAttempts = 0;  // Resets the failed attempts counter
                        blockTime = 0; // clearks the block timestamp
                        BLOCK_DURATION = savedBlockDuration; // Sets the block duration to it's default value
                        new File("lock_forgot_password_reset_status.txt").delete(); // Deletes the lock status file
                    }
                }
            }
        } catch (IOException e) {
            // Catches and prints an error message if there is an issue regarding the file
            e.printStackTrace();
        }
    }
    
    private void saveLockStatus(String username, long blockTime) {

        // Opens the file "lock_forgot_password_reset_status.txt"
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("lock_forgot_password_reset_status.txt"))) {
            writer.write(username + "," + blockTime + "," + BLOCK_DURATION + "," + failedAttempts);
        } catch (IOException e) {
            //Catches and prints an error message if there is an issue creating the file
            e.printStackTrace();
        }
    }
    
    // Action listener for toggling the visibility of the password field
    public class PasswordVisible1 implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Check if the cehckbox is selected for password visibility 
            if (passwordVisibleCB1.isSelected()) {
                // Make the password visible by setting echo character to none
                newPasswordField.setEchoChar((char)0);
            } else {
                // Hide the password by setting echo character to a bullet
                newPasswordField.setEchoChar('\u2022');
            }
        }
    }
    
    // Action listener for toggling the visibility of the password field
    public class PasswordVisible2 implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Check if the checkBox is selected for password visibility
            if (passwordVisibleCB2.isSelected()) {
                // Make the password  visible by setting echo character to none
                confirmNewPasswordField.setEchoChar((char)0);
            } else {
                // Hide the password by setting echo character to a bullet
                confirmNewPasswordField.setEchoChar('\u2022');
            }
        }
    }
         
      private HashMap<String, String> loadUserData() {
            HashMap<String, String> userData = new HashMap<>();
            
            // Open the file for reading
            try (BufferedReader reader = new BufferedReader(new FileReader("user_data.txt"))) {
                String line;
                
                while ((line = reader.readLine()) != null) { // Read each line of the file
                    String[] parts = line.split(","); // Split line by commas to extract user details
                    
                    // Ensure the line has enough data fields (at least 3)
                    if (parts.length >= 3) {
                        String decryptedEmail = decryptData(parts[1]); // Encrypt email
                        String decryptedAnswer = decryptData(parts[4]); // Split line by commas to extract user details
                        
                        // Store the encrypted email + answer as a key and password as a value
                        userData.put(decryptedEmail + ":" + decryptedAnswer, line);  
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return userData; // Return the user data map
        }
    
    // Method to update and save a new password in the "user_data.txt" file
    private void saveNewPasswordToFile(String encryptedEmail, String encryptedNewPassword) {
        try {
            // Open the original user data file
            File file = new File("user_data.txt");
            File tempFile = new File("user_data_temp.txt"); // Temporary file for modification
            
            // Create file readers and writers
            BufferedReader reader = new BufferedReader(new FileReader(file));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String currentAnswer = securityAnswerTxt.getText().trim();  // Get current username from label
            String currentEmail = emailTxt.getText().trim();  // Get current email from label
            
            String line;
          
            // Read each line of the original file
            while ((line = reader.readLine()) != null) {
                // Split line by commas to extract user details
                String[] parts = line.split(",");
                
                // Check if the line belongs to the user being updated
                if (parts.length >= 3) {
                    
                     String decryptedStoredEmail = decryptData(parts[1]);
                     String decryptedStoredAnswer = decryptData(parts[4]);
                     
                     if (decryptedStoredEmail.equals(currentEmail) && decryptedStoredAnswer.equals(currentAnswer)) {
                    
                    // Write the updated password to the temporary file
                    writer.write(parts[0] + "," + parts[1] + "," + encryptedNewPassword + "," + parts[3] + "," + parts[4] + "," + parts[5] + "," + parts[6]);
                     
                } else {
                    // Write unchanged lines to the temporary file
                    writer.write(line);
                     }
                } else {
                    writer.newLine(); // Move to the next line
                }
                writer.newLine();
            }

            // Close file streams
            reader.close();
            writer.close();

            // Replace the original file with the updated file
            if (file.delete()) {
                tempFile.renameTo(file);
            }
            
            if (!file.setReadOnly()) {
                System.out.println("Warning: Unable to set file to Read Only");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    // Method to check if the account is currently blocked due to too many failed login attempts
    private boolean isBlocked() {
        if (failedAttempts >= MAX_FAILED_ATTEMPTS) { // To check if the number of failed attempts has reached the maximum allowed
            long timeLeft = (blockTime + BLOCK_DURATION - System.currentTimeMillis()) / 1000;
            if (timeLeft > 0) {       // This determines if there's still time left in the lock duration, and if there is, then it returns true and the account stays locked
                return true;    
            } else {                  // In the case that the time of lock duration has expired
                failedAttempts = 0;   // The failed attempts and
                blockTime = 0;        // blockTime is resrt to 0, so now the user can try to login again
                return false;         // This is so the isBlocked() method is not activated again when clicking the signUpButton
            }
        }
        return false;                 // In case the failed attempts are below the max limit, the account is not blocked, and the user can keep trying to login
    }
    
     /* Encryption and decryption methods */

    public static String encryptData(String data) {
        try {
            // Generate a random IV
            // Random Initialization Vector (IV): crucial element used with symmetric ciphers to ensure that the same 
            // plaintext, when encrypted multiple times, produces different ciphertexts, enhancing security
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
        
}

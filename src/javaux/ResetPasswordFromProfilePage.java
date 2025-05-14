 
package javaux;

// Import all Swing components for creating a graphical user interface (GUI)
import javax.swing.*; // Includes all Swing UI components like JFrame, JButton, JLabel, JTextField, etc.

// Importing AWT classes for UI customization and event handling
import java.awt.*; // Provides core graphical components such as Color, Font, and Cursor.

// Importing event handling classes
//import java.awt.event.ActionEvent; // Represents an action event (e.g., button clicks)
//import java.awt.event.ActionListener; // Listens for action events and executes code in response
//import java.awt.event.KeyAdapter; // Provides a deafult implementation for handing keyboard events 
//import java.awt.event.KeyEvent; // Represents a key event (e.g., key presses)
//import java.awt.event.MouseAdapter; // Provides a deafult implementation for handling mouse events
//import java.awt.event.MouseEvent; // Represents a mouse event (e.g., clicks, movements)
import java.awt.event.*;

// Importing file handling and I/O operations
import java.io.*; // Includes all Java I/O classes for reading files, streams, and handling exceptions 

// Importing Base64 encoding and decoding utility
import java.util.Base64; // Provides methods for encoding and decoding Base64 data, commonly used in encryption 

// Importing collections framework
import java.util.HashMap; //

// Importing cryptographic classes for encryption and decryption
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

// Importing document change event listener for hundling text input changes
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import javax.crypto.spec.*;
import java.security.SecureRandom;

// Definition of the ResetPasswordFromProfilePage class
public class ResetPasswordFromProfilePage {

    // Declare instance variables for GUI components
    private JFrame frame;
    private JLabel usernameLabel, emailLabel, resetPasswordLabel, newPasswordLabel, reEnterPasswordLabel, securityQuestionLabel, questionLabel, answerLabel, emailIcon, answerIcon;
    private JTextField emailTxt, securityAnswerTxt;
    private JPasswordField newPasswordField, reEnterPasswordField;
    private JButton resetPasswordButton, cancelButton;
    private JCheckBox passwordVisibleCB1, passwordVisibleCB2;
    private ImageIcon image, visible, notVisible;
    private int failedAttempts = 0;
    private long blockTime = 0;
    private final int MAX_FAILED_ATTEMPTS = 3;
    private int BLOCK_DURATION = 30000;
    
    private User user;
     
    // AES key for encryption and decryption (same as before)
    private static final String SECRET_KEY = "mysecretkey12345"; // 16-byte key (128 bits)
    
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    
    // Constructor for the ResetPasswordFromProfile class
    public ResetPasswordFromProfilePage(User user) {
        
        loadLockStatus();
        
        /* Retrieve user detair from user object */
        String currentSavedUsername = user.getUsername(); // Retrieve the username from the user object
        String currentSavedEmail = user.getEmail(); // Retrieve the email from the user object
        String currentSavedSecurityQuestion = user.getQuestion(); // Retrieve the security question from the object
        String currentSavedSecurityQuestionAnswer = user.getAnswer(); // Retrieve the security anser from the object
        
        frame = new JFrame("ResetPassword - ADAMSON AI");
        frame.setSize(420, 440);
        frame.setResizable(false);
        frame.getContentPane().setBackground(Color.decode("#222222"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null); 

        image = new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\adamson-logo.png");
        frame.setIconImage(image.getImage());
        
        visible = new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\visible1.png");
        notVisible = new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\notVisible1.png");
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        resetPasswordLabel = new JLabel("Reset Password of " + currentSavedUsername);
        resetPasswordLabel.setForeground(Color.WHITE);
        resetPasswordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        resetPasswordLabel.setFont(new Font(null, Font.BOLD, 18));
        resetPasswordLabel.setBounds(0, 20, 400, 50);
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        securityQuestionLabel = new JLabel("Security Question");
        securityQuestionLabel.setForeground(Color.WHITE);
        securityQuestionLabel.setBounds(50, 90, 120, 20);

        questionLabel = new JLabel(currentSavedSecurityQuestion);
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
            public void insertUpdate(DocumentEvent e) { // Validate the security answer on text insertion
                UpdateAnswerIconAndToolTip();
            }

            @Override
            public void removeUpdate(DocumentEvent e) { // Validate the security answer on text removal
                UpdateAnswerIconAndToolTip();
            }

            @Override
            public void changedUpdate(DocumentEvent e) { // Validate the security answer on text style change
                UpdateAnswerIconAndToolTip();
            }

            // Method to update the security answer's icon and tooltip based on user input
            private void UpdateAnswerIconAndToolTip() {
                String answerIcn = securityAnswerTxt.getText().trim(); // Get trimmed user input 
                
                String answerStatus = getAnswerStatus(); // Validate the answer format
                
                answerIcon.setToolTipText(answerStatus); // Set tooltip to indicate validity
                
                /* Determine which icon to display based on the validity of input */
                if (answerIcn.isEmpty()) { // No icon if input is empty
                    answerIcon.setIcon(null);
                } else if (answerIcn.equals(currentSavedSecurityQuestionAnswer)) { // Set the "correct icon" if the security answer entered matches the stored answer
                    answerIcon.setIcon(new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\CorrectGold.png")); 
                } else if (!answerIcn.equals(currentSavedSecurityQuestionAnswer)) { // Set the "incorrect icon" if the security answer entered does not match the stored answer
                    answerIcon.setIcon(new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\IncorrectGold.png"));
                }
            }

            // Method to check wether the security answer is in the correct format or not
            private String getAnswerStatus() {
                String ansStatus = securityAnswerTxt.getText().trim(); // Get the trimmed text input 
                
                // Check if input matches the stored security answer or not
                if (!ansStatus.equals(currentSavedSecurityQuestionAnswer)) {
                    return "Incorrect answer!"; // Return null if input does not match the stored security answer
                }
                return null; // Return null if the input security answer is correct
            }
        });
         
        // Creating the security answer validation icon label
        answerIcon = new JLabel();
        answerIcon.setBounds(344, 130, 20, 20); 
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        emailLabel = new JLabel("Re-Enter your Email");
        emailLabel.setForeground(Color.WHITE);
        emailLabel.setBounds(50, 170, 120, 20);

        emailTxt = new JTextField();
        emailTxt.setBounds(180, 170, 150, 20);
        
        // Creating a Document Listener to update tooltips as icons that will give hints to users of their errors and successes in real time 
        emailTxt.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { // Validate the email on text insertion
                UpdateEmailIconAndToolTip();
            }

            @Override
            public void removeUpdate(DocumentEvent e) { // Validate the email on text removal
                UpdateEmailIconAndToolTip();
            }

            @Override
            public void changedUpdate(DocumentEvent e) { // Validate the email on text style change
                UpdateEmailIconAndToolTip();
            }

            // Method to update the email's icon and tooltip based on user input
            private void UpdateEmailIconAndToolTip() {
                String emailIcn = emailTxt.getText().trim(); // Get trimmed user input
                
                String emailStatus = getEmailStatus(); // Validate the user input email with stored email 
                
                emailIcon.setToolTipText(emailStatus); // Set tooltip to indicate validity
                
                /* Determine which icon to display based on the validity of inputs */
                if (emailIcn.isEmpty()) { // No icon if input is empty
                    emailIcon.setIcon(null);
                } else if (emailIcn.equals(currentSavedEmail)) { // Set the "Correct icon" if the email entered matches the stored email
                    emailIcon.setIcon(new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\CorrectGold.png"));
                } else if (!emailIcn.equals(currentSavedEmail)) { // Set the "Incorrect icon" if the email entered does not match the stored email
                    emailIcon.setIcon(new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\IncorrectGold.png"));
                }
            }

            // Method to check wether the email entered matches the stored email
            private String getEmailStatus() {
                String emailSts = emailTxt.getText().trim(); // Get the trimmed text input
                
                // Check if input matches the stored email or not
                if (!emailSts.equals(currentSavedEmail)) {
                    return "Wrong email!"; // Return null if the input does not match the store email
                }
                return null; // Return null if the input email is exists
            }
        });
        
        emailIcon = new JLabel(); 
        emailIcon.setBounds(344, 170, 20, 20);
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        newPasswordLabel = new JLabel("New Password");
        newPasswordLabel.setForeground(Color.WHITE);
        newPasswordLabel.setBounds(50, 210, 120, 20);

        newPasswordField = new JPasswordField();
        newPasswordField.setBounds(180, 210, 150, 20);
                
        passwordVisibleCB1 = new JCheckBox();
        passwordVisibleCB1.setBackground(Color.decode("#222222"));
        passwordVisibleCB1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        passwordVisibleCB1.setBounds(340, 210, 20, 20);
        passwordVisibleCB1.setIcon(notVisible);
        passwordVisibleCB1.setSelectedIcon(visible);
        passwordVisibleCB1.addActionListener(new PasswordVisible1());
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        reEnterPasswordLabel = new JLabel("Re-Enter Password");
        reEnterPasswordLabel.setForeground(Color.WHITE);
        reEnterPasswordLabel.setBounds(50, 250, 120, 20);

        reEnterPasswordField = new JPasswordField();
        reEnterPasswordField.setBounds(180, 250, 150, 20);
                
        passwordVisibleCB2 = new JCheckBox();
        passwordVisibleCB2.setBackground(Color.decode("#222222"));
        passwordVisibleCB2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        passwordVisibleCB2.setBounds(340, 250, 20, 20);
        passwordVisibleCB2.setIcon(notVisible);
        passwordVisibleCB2.setSelectedIcon(visible);
        passwordVisibleCB2.addActionListener(new PasswordVisible2());
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        resetPasswordButton = new JButton("Reset Password");
        resetPasswordButton.setContentAreaFilled(false);
        resetPasswordButton.setOpaque(true);
        resetPasswordButton.setFocusable(false);
        resetPasswordButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        resetPasswordButton.setBounds(50, 300, 310, 30);
        resetPasswordButton.setBackground(Color.decode("#876F4D"));
        resetPasswordButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        resetPasswordButton.setForeground(Color.WHITE);
        resetPasswordButton.addActionListener(e -> { // Add an action listener forthe resetPassword button
            
            // Retrieve and trim input values from text fields
            String currentEmail = emailTxt.getText().trim();
            String currentAnswer = securityAnswerTxt.getText().trim();
            String newPassword = new String(newPasswordField.getPassword());
            String reEnterPassword = new String(reEnterPasswordField.getPassword());
            
            // Check if the account is locked due to multiple failed attempts
            if (isBlocked()) {
                long timeLeft = (blockTime + BLOCK_DURATION - System.currentTimeMillis()) / 1000; // Calculate remaining lock time
                long minutes = timeLeft / 60; // Calculate minutes, this extract minutes from the total seconds
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
                return;
            }
            
            // Validate that password length must be between 8 and 16 characters
            if (newPassword.length() < 8 || newPassword.length() > 16) {
                JOptionPane.showMessageDialog(frame, "Password must be between 8 and 16 characters", "Error", JOptionPane.INFORMATION_MESSAGE); 
                return;
            }
            
            else if (!currentEmail.equals(currentSavedEmail) || !currentAnswer.equals(currentSavedSecurityQuestionAnswer)) {
                JOptionPane.showMessageDialog(frame, "Invalid information entered, Attempts left: " + (MAX_FAILED_ATTEMPTS - failedAttempts - 1), "Warning", JOptionPane.WARNING_MESSAGE);
                
                failedAttempts++; // Increase the failed attempt counter
                
                // Lock the account if the maximum number of failed attempts is reached
                if(failedAttempts >= MAX_FAILED_ATTEMPTS) {
                    blockTime = System.currentTimeMillis(); // Record the lock time
                    
                    BLOCK_DURATION *= 2;
                    
                    saveLockStatus(currentSavedUsername, blockTime);
                    
                    JOptionPane.showMessageDialog(frame, "Too many failed attempts. This action is locked for " + BLOCK_DURATION / 60000 + " minute(s)", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            
            else {
                
                failedAttempts = 0;
                blockTime = 0;
                BLOCK_DURATION = 60000;
                
                File lockFile = new File("lock_reset_password_status.txt");
                if (lockFile.exists()) {
                    lockFile.delete(); 
                }                  
                                
                // Loads existing user data from file
                HashMap<String, String> userData = loadUserData();
 
                // Create a unique key using encrypted email and security answer
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
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        cancelButton = new JButton("Cancel");
        cancelButton.setContentAreaFilled(false);
        cancelButton.setOpaque(true);
        cancelButton.setFocusable(false);
        cancelButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cancelButton.setBounds(50, 340, 310, 30);
        cancelButton.setBackground(Color.decode("#876F4D"));
        cancelButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> {
            String password = new String(newPasswordField.getPassword());
            String password2 = new String(reEnterPasswordField.getPassword());
            
            if (!password.isEmpty() || !password2.isEmpty()) {
                int response = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit without saving?", "Exit without saving", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                
                if (response == JOptionPane.YES_OPTION) {
                    frame.dispose();
                    new EditProfilePage(user);
                } else {
                    return;
                }
            } else {
                frame.dispose();
                new EditProfilePage(user);
            }
        });
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
        /* These key listeners will automatically trigger the resetPasswordButton button if the Enter key is pressed in the keyboard */
        
        securityAnswerTxt.addKeyListener(new KeyAdapter() { // Add key listener to the securityAnswerTxt textField
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Check if Enter key is pressed
                    resetPasswordButton.doClick(); // Simulate a click on the resetPasswordButton button
                }
            }
        });
                
        emailTxt.addKeyListener(new KeyAdapter() { // Add key listener to the emailTxt textField
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Check if Enter key is pressed
                    resetPasswordButton.doClick(); // Simulate a click on the resetPasswordButton button
                }
            }
        });

        newPasswordField.addKeyListener(new KeyAdapter() { // Add key listener to the newPasswordField textField
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Check if Enter key is pressed
                    resetPasswordButton.doClick(); // Simulate a click on the resetPasswordButton button
                }
            }
        });

        reEnterPasswordField.addKeyListener(new KeyAdapter() { // Add key listener to the reEnterPasswordField textField
            @Override 
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Check if Enter key is pressed
                    resetPasswordButton.doClick(); // Simulate a click on the resetPasswordButton button
                }
            }
        });
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
        // Create a MouseAdapter object to handle mouse events for multiple buttons
        MouseAdapter listener = new MouseAdapter() {

            // Method triggered when the mouse is clicked on a button
            @Override
            public void mouseClicked(MouseEvent e) {
                JButton source = (JButton) e.getSource(); // Gets the button that was clicked
                source.setBackground(Color.WHITE); // Sets the background color of the button to white
                source.setForeground(Color.BLACK); // Sets the text color of the button to black
                source.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Change cursor icon to a hand
                source.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); // Sets the button border to white
            }
            
            // Method triggered when a mouse button is pressed in a button
            @Override
            public void mousePressed(MouseEvent e) {
                JButton source = (JButton) e.getSource(); // Gets the button that was pressed
                source.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Changes the cursor icon to a hand
                source.setBackground(Color.WHITE); // Sets the background color of the button to white
                source.setForeground(Color.decode("#8A6E4B")); // Sets the text color of the button to a Taupe color (Brown gold)
            }
            
            //  Method triggered when a mouse button is released after being pressed 
            @Override
            public void mouseReleased(MouseEvent e) {
               JButton source = (JButton) e.getSource(); // Gets the button that was released after being pressed 
               source.setForeground(Color.WHITE); // Set text color of the button to white
               source.setBackground(Color.decode("#876F4D")); // Setthe background color of the button to khaki
               source.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); // Set the button border to white              
            }
            
            // Method trigered when the mouse enters the button area 
            @Override
            public void mouseEntered(MouseEvent e) {
                JButton source = (JButton) e.getSource(); // Gets the button that the mouse entered
                source.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Changes the cursor icon to a hand
                source.setBackground(Color.decode("#514937")); // Sets the background color of a button to a dark brownish-green
                source.setForeground(Color.WHITE); // Sets the text color of the button to white
                source.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); // Sets the button border to white
            }
            
            //  Method triggered when the mouse exits the button area
            @Override
            public void mouseExited(MouseEvent e) {
                JButton source = (JButton) e.getSource(); // Get the button that the mouse exited
                source.setBackground(Color.decode("#876F4D")); // Set the background color of the button to khaki
                source.setForeground(Color.WHITE); // Set the text color of the button to white
                source.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); // Set the button border color to white
            }
        };

        // Add same MouseAdapter (listener) to multiple buttons to apply behavior
        resetPasswordButton.addMouseListener(listener);
        cancelButton.addMouseListener(listener);
        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
        // Add all components to the frame to make them visible
        frame.add(resetPasswordLabel);
        frame.add(securityQuestionLabel);
        frame.add(questionLabel);
        frame.add(answerLabel);
        frame.add(securityAnswerTxt);
        frame.add(answerIcon);
        frame.add(emailLabel);
        frame.add(emailTxt);
        frame.add(emailIcon);
        frame.add(newPasswordLabel);
        frame.add(newPasswordField);
        frame.add(passwordVisibleCB1);
        frame.add(reEnterPasswordLabel);
        frame.add(reEnterPasswordField);
        frame.add(passwordVisibleCB2);
        frame.add(resetPasswordButton);
        frame.add(cancelButton);   
        frame.setVisible(true);       
    }
    
    // Load lock status from a file
    private void loadLockStatus() {
        try (BufferedReader reader = new BufferedReader (new FileReader("lock_reset_password_status.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String lockedUser = parts[0];
                    long lockedTime = Long.parseLong(parts[1]);
                    int savedBlockDuration = Integer.parseInt(parts[2]);

                    if ((lockedTime + savedBlockDuration) > System.currentTimeMillis()) {
                        blockTime = lockedTime;
                        failedAttempts = MAX_FAILED_ATTEMPTS;
                        BLOCK_DURATION = savedBlockDuration;
                    } else {
                        failedAttempts = 0;
                        blockTime = 0;
                        BLOCK_DURATION = 60000;
                        new File("lock_reset_password_status.txt").delete();
                    }
                }
            }
       } catch (IOException e) {
        e.printStackTrace();
        }
    }
    
    private void saveLockStatus(String username, long blockTime) {
        
        // Opens the file "lock_reset_password_status.txt" for reading using a BufferedReader
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("lock_reset_password_status.txt"))) {
            writer.write(username + "," + blockTime + "," + BLOCK_DURATION);         
        } catch (IOException e) {
            // Catches and prints an error message if there is an issue creating the file
            e.printStackTrace();
        }
    }
    
    // Action listener for toggling the visibility of the password field
    public class PasswordVisible1 implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Check if the checkbox is selected for password visibility 
            if (passwordVisibleCB1.isSelected()) {
                // Make the password visible by setting echo character to none
                newPasswordField.setEchoChar((char)0);
            } else {
                // Hide the password by setting echo character to a bullet 
                newPasswordField.setEchoChar('\u2022');
            }
        }
    }
    
    // Action listener for toggling visibility of the password field
    public class PasswordVisible2 implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Check if the checkBox is selected for password visibility 
             if (passwordVisibleCB2.isSelected()) {
                 // Make the password visible by setting echo character to none
                 reEnterPasswordField.setEchoChar((char)0);
             } else {
                 // Hide the password by setting echo character to a bullet
                 reEnterPasswordField.setEchoChar('\u2022');
             }
        }
    }
             
    // Method to lead user data from "user_data.txt" file
    private HashMap<String, String> loadUserData() {
        HashMap<String, String> userData = new HashMap<>();
        
        // Open the file for reading
        try (BufferedReader reader = new BufferedReader(new FileReader("user_data.txt"))) {
            String line;
            
            while ((line = reader.readLine()) != null) { // Read each line of the file
                String[] parts = line.split(","); // Split line by commas to extract user details
                
                // Ensure the line has enough data fields (at least 3)
                if (parts.length >= 3) {
                    String decryptedEmail = decryptData(parts[1]); // Encrypted email
                    String decryptedAnswer = decryptData(parts[4]); // Encrypted security answer
                    
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
            
            // Create file readers and writes
            BufferedReader reader = new BufferedReader(new FileReader(file));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            
            String currentAnswer = securityAnswerTxt.getText().trim();
            String currentEmail = emailTxt.getText().trim();
            
            // Read each line of the original file
            String line;
            
            while ((line = reader.readLine()) != null) {
                // Split line by commas to extract user details
                String[] parts = line.split(",");
                
                // Check if the line belongs to the user being updated
                if (parts.length >= 3) {
                    // Write the updated password to the temporary file
                    String decryptedStoredEmail = decryptData(parts[1]);
                    String decryptedStoredAnswer = decryptData(parts[4]);
                    
                    if (decryptedStoredEmail.equals(currentEmail) && decryptedStoredAnswer.equals(currentAnswer)) {
                    
                        writer.write(parts[0] + "," + parts[1] + "," + encryptedNewPassword + "," + parts[3] + "," + parts[4] + "," + parts[5] + "," + parts[6]); 
                    } else {
                        writer.write(line);
                    }
                    
                } else {
                    // Write unchanged lines to the temporary file
                    writer.write(line);
                }
                writer.newLine(); // More to the next line
            }
            
            // Close file streams
            reader.close();
            writer.close();
            
            // Replace the original file with updated file
            if (file.delete()) {
                tempFile.renameTo(file);
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Method to check if the account is currently blocked due to too many failed login attempts
    private boolean isBlocked() {
        if (failedAttempts >= MAX_FAILED_ATTEMPTS) { // To check if the number of failed attempts has reached the maximum allowed
            long timeLeft = (blockTime + BLOCK_DURATION - System.currentTimeMillis()) / 1000;
            if (timeLeft > 0) { // This determines if there's still time left in the lock duration, and if there is, then it returns true and the account stays locked
                return true;
            } else {                 // In the case that the time of lock duration has expired
                failedAttempts = 0;  // The failed attempts and
                blockTime = 0;       // blockTime is reset to 0, so now the user can try to login again  
                //new File("lock_reset_password_status.txt").delete(); // Furthermore, the txt file that contained the lock status is deleted to unlock the login possibility
                return false;                         // This is so the isBlocked() method is not activated again when clicking the signUpButton
            }
        }
        return false; // In case the failed attempts are below the max limit, the account is not blocked, and the user can keep trying to login
    }
        
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

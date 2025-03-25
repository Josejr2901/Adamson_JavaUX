
package javaux;

/* Importing Swing components for GUI development */
import javax.swing.*; // Imports all Swing UI components (e.g., JFrame, JButton, JLabel)
import java.awt.*; // Imports AWT classes for UI elements (e.g., Color, Font, Cursor)

/* Importing event hadling classes */
import java.awt.event.ActionEvent; // Represents an action event (e.g, button clicks)
import java.awt.event.ActionListener; // Listens for action events and executes code in response
import java.awt.event.KeyAdapter; // Provides default implementation for handling keyboard events
import java.awt.event.KeyEvent; // Represents a key event (e.g., key presses)
import java.awt.event.MouseAdapter; // Provides default implementation for handling mouse events
import java.awt.event.MouseEvent; // Represents a mouse event (e.g., clicks, movement)

/* Importing file handling and I/O operations */
import java.io.*; // Imports all standart I/O classes (e.g., BufferedReader, FileReader, FileWriter)
import java.util.Base64; // Provides methods for encoding and decoding Base64 data
import java.util.HashMap; // Implements a data structure for storing key-value pairs

/* Importing cryptographic classes for encryption and decryption */
import javax.crypto.Cipher; // Provides encryption and decryption functionality
import javax.crypto.spec.SecretKeySpec; // Represents a secret key for symetric encryption

/* Importing classes for handling document changes in text fields */
import javax.swing.event.DocumentEvent; // Represents changes in a document (e.g., text input)
import javax.swing.event.DocumentListener; // Listens for document changes and reacts accordingly

public class ChangePasswordPage {
    
    private JFrame frame;
    private JLabel usernameLabel, emailLabel, resetPasswordLabel, newPasswordLabel, confirmNewPasswordLabel, securityQuestionLabel, questionLabel, answerLabel, emailIcon, answerIcon;
    private JTextField emailTxt, securityAnswerTxt;
    private JCheckBox passwordVisibleCB1, passwordVisibleCB2;
    private JPasswordField newPasswordField, confirmNewPasswordField;
    private JButton resetPasswordButton, cancelButton;
    
    // AE key for encryption and decryption 
    private static final String SECRET_KEY = "mysecretkey12345"; // 16-byte key (128 bits)
    
    // Constructor to initialize the change password window
    ChangePasswordPage(String username, String email, String securityQuestion, String answer) {
        
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
        resetPasswordButton.addActionListener(new ResetPasswordAction());
        
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
                // frame /* The parent component for the dialog */
                // "Do you want to exit without saving?" /* Message to display in the dialog */
                // "Exit without saving" /* Title of the dialog window */
                // JPtionPane.WES_NO_OPTION /* Dialog with Yes/No Option */
                // JOptionPane.WARNING_MESSAGE /* Displays a warning icon */
                
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
    
    // Inner class to handle password reset actions
    private class ResetPasswordAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Retrieve and trim input values from text fields
            String email = emailTxt.getText().trim();
            String answer = securityAnswerTxt.getText().trim();
            String newPassword = new String(newPasswordField.getPassword());
            String confirmNewPassword = new String(confirmNewPasswordField.getPassword());
            
            // Ensure all fiels are filled
            if (email.isEmpty() || answer.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Ensure new password and confirmation match
            if (!newPassword.equals(confirmNewPassword)) {
                JOptionPane.showMessageDialog(frame, "Passwords do not match");
            }
            
            // Validate that password length must be between 8 and 16 characters
            if (newPassword.length() < 8 || newPassword.length() > 16) {
                JOptionPane.showMessageDialog(frame, "Password must be between 8 and 16 characters", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Load existing user data from file
            HashMap<String, String> userData = loadUserData();
            
            // Encrypt email and security answer for validation
            String encryptedEmail = encryptData(email);
            String encryptAnswer = encryptData(answer);
            
            // Create a unique key using encrypted email and security answer
            String key = encryptedEmail + ":" + encryptAnswer;
            
            // Check if the provided email and answer match any stored user data
            if (userData.containsKey(key)) {
                // Encrypt new password before saving
                String encryptedNewPassword = encryptData(newPassword);
                
                // Save new password to file
                saveNewPasswordToFile(encryptedEmail, encryptedNewPassword);
                
                // Show success message
                JOptionPane.showMessageDialog(frame, "Password reset succesful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                frame.dispose();
                new MainPage(userData);
            } else {
                // Display error message if email or security answer is incorrect
                JOptionPane.showMessageDialog(frame, "Email or security answer is incorrect!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // Method to lead user data from "user_data.txt" file
    private HashMap<String, String> loadUserData() {
        HashMap<String, String> userData = new HashMap<>();
        
        // Open the file for reading
        try (BufferedReader reader = new BufferedReader(new FileReader("user_data.txt"))) {
            String line;
            
            while ((line = reader.readLine()) != null) { // Read eah line of the file
                String[] parts = line.split(","); // Split line by commas to extract user details
                
                // Ensure the line has enough data fields (at least 3)
                if (parts.length >= 3) {
                    String encryptedEmail = parts[1]; // Encrypted email
                    String encryptedAnswer = parts[4]; // Encrypted security answer
                    String encryptedPassword = parts[2]; // Encrypted password
                    
                    // Store the encrypted email + answer as a key and passowrd as a value
                    userData.put(encryptedEmail + ":" + encryptedAnswer, encryptedPassword);
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

            String line;

            // Read each line of the original file
            while ((line = reader.readLine()) != null) {
                // Split line by commas to extract user details
                String[] parts = line.split(",");
                
                // Check if the line belongs to the user being updated
                if (parts.length >= 3 && parts[1].equals(encryptedEmail)) {
                    // Write the updated password to the temporary file
                    writer.write(parts[0] + "," + parts[1] + "," + encryptedNewPassword + "," + parts[3] + "," + parts[4] + "," + parts[5] + "," + parts[6]);
                } else {
                    // Write unchanged lines to the temporary file
                    writer.write(line);
                }
                writer.newLine(); // Move to the next line
            }

            // Close file streams
            reader.close();
            writer.close();

            // Replace the original file with the updated file
            if (file.delete()) {
                tempFile.renameTo(file);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Method to encrypt data, it is used for encrypting usernames and password, and any other data before storing them into a file
    private String encryptData(String data) {
        try {
            /* SECRET_KEY.getBytes() converts the string key into a byte array.
               SecretKeySpec wraps this byte array into an object that can be used by the AES algorithm.
               This ensures that the same key is used for both encryption and decryption  
            */
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES"); // Create an AES encryption key by using the predefined SECRET_KEY
            
            Cipher cipher = Cipher.getInstance("AES"); // Initialize to create an AES Cipher instance for encryption mode
            cipher.init(Cipher.ENCRYPT_MODE, keySpec); // Initialize cipher in ENCRYPT_MODE, this tells the cipher that we want to encrypt data using the secret key.
            
            byte[] encryptedBytes = cipher.doFinal(data.getBytes()); // Perform encryption on the input data
                                                                     // data.getBytes() converts the plaintext string into a byte array (AES works with bytes, not strings).
                                                                     // cipher.doFinal(data.getBytes()) performs the encryption:
                                                                                // It takes the input data.
                                                                                // Uses the AES encryption algorithm with the given secret key
                                                                                // Returns an encrypted byte array
                                                                                
            return Base64.getEncoder().encodeToString(encryptedBytes); // Convert the encrypted bytes into a Base64 string for easier storage
                                                                       // AES encryption produces binary data (not readable). Therefore...
                                                                       // Base64.getEncoder().encodeToString(encryptedBytes) converts the encrypted bytes into a readable Base64 string 
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
}

 
package javaux;

/* Import AWT classes for UI customization and event handling */
import java.awt.Color; // Allows customization of UI component colors
import java.awt.Cursor; // Enables changing the cursors appearance (e.g., hand cursor on hover)
import java.awt.Font; // Manages font style and size for UI text rendering

/* Importing event handling classes */
import java.awt.event.KeyAdapter; // Provides a default implementation for handling key events
import java.awt.event.KeyEvent; // Represents key events (e.g., key presses)
import java.awt.event.MouseAdapter; // Provides a deafult implementation for handling mouse events
import java.awt.event.MouseEvent; // Represents mouse actions (e.g., clicks, movement)

/* Import file handling and I/O operations */
import java.io.BufferedReader; // Reads text from a file efficiently 
import java.io.BufferedWriter; // Writes text to a file efficiently
import java.io.File; // Represents a file or a directory path
import java.io.FileReader; // Read data from a file character by character
import java.io.FileWriter; // Writes data to a file
import java.io.IOException; // Handles exceptions related to input/output operations

/* Importing file manipulation utilities */
import java.nio.file.Files; // Provides methods for reading, writing, and manipulating files
import java.nio.file.Paths; // Represents file paths in a system-independent way

/* Importing Base64 encoding and decoding utility */
import java.util.Base64; // Provides methods for encoding and decoding Base64 data

/* Importing collections */
import java.util.HashMap; // Implements a data structure for storing key-value pairs 

/* Importing cryptographic classes for encryption and decryption */
import javax.crypto.Cipher; // Provides encryption and decryption functionality
import javax.crypto.spec.SecretKeySpec; // Defines a secret key for encryption algorithms

/* Importing Swing components for creating a graphical user interface (GUI) */
import javax.swing.BorderFactory; // Creates border styles for UI elements
import javax.swing.ImageIcon; // Handles icons and images in the UI
import javax.swing.JButton; // Represents a clickable button in the UI
import javax.swing.JFrame; // Represents the main window of the application
import javax.swing.JLabel; // Displays text or images in the UI
import javax.swing.JOptionPane; // Displays pop-up dialogs (alers, messages, confirmation)
import javax.swing.JTextField; // Represents a single-line text input field
import javax.swing.SwingConstants; // Provides constant for UI alignment (e.g., left, right, cener)e

import javax.crypto.spec.*;
import java.security.SecureRandom;
 
public class securityQuestionDeleteProfile {
    
    private JFrame frame;
    private JLabel titleLabel, securityQuestionLabel;
    private JTextField securityAnswerTxt;
    private JButton confirmButton, cancelButton;
    private int failedAttempts = 0; // To keep track of how many times the user has attempted to log in with an incorrect password
    private long blockTime = 0; // Stores the time stamp of when the account was blocked after reaching the maximum number of failed attempts. Used in conjunction with BLOCK_DURATION to determine if the user is currently locked out
    private final int MAX_FAILED_ATTEMPTS = 3; // A constant that defines the maximum number of allowed failed login attempts before the account is locked
    private final long BLOCK_DURATION = 60000; // Defining the lock duration in milliseconds *1 minute* [60 seconds * 1000 milliseconds = 1 minute]
    
    // AES key for encryption and decryption 
    private static final String SECRET_KEY = "mysecretkey12345"; // 16-byte key (128 bits)

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    
    // Constructor method for the deleting a user profile with a security question
    public securityQuestionDeleteProfile(User user) {
        
        loadLockStatus(); // Load the lock status of the user account to check if account is temporaly blocked
        
        // Retrieve user details from the User object
        String username = user.getUsername(); // Get the username        
        //String currentUsername = user.getUsername(); // Store current username
        String currentEmail = user.getEmail(); // Get the user's email
        String securityQuestion = user.getQuestion(); // Get the security question set by the user
        String rightAnswer = user.getAnswer(); // Get the answer to the security question
        
        // Create and configure the JFrame for the delete account prompt
        frame = new JFrame("Delete Account ADAMSON-AI"); //Set the window title
        frame.setSize(400, 360); // Set the window size (x, y)
        frame.getContentPane().setBackground(Color.decode("#222222")); // Set the background color to dark grey 
        frame.setLayout(null); // Use absolute positioning (no layer manager)
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit the prgram when the window is closed
        frame.setLocationRelativeTo(null); // Center the window on the game
        frame.setResizable(false); // Disable resizing on of the window
        
        // Set the application icon
        ImageIcon image = new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\adamson-logo.png"); 
        frame.setIconImage(image.getImage()); // Set the application window icon
        
        // Craete and configure the title label
        titleLabel = new JLabel("Please answer before proceding"); // Set the label text
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center-align the text 
        titleLabel.setForeground(Color.WHITE); // Set the text color to white
        titleLabel.setFont(new Font(null, Font.BOLD, 18)); // Set the font style and size 
        titleLabel.setBounds(0, 20, 380, 50); // set the position and size (x, y, width, height)
        
        // Create and configure the security question label
        securityQuestionLabel = new JLabel(securityQuestion); // Display the user's security question 
        securityQuestionLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center-align the text
        securityQuestionLabel.setForeground(Color.decode("#876F4D")); // Set the text color (goldish brown)
        securityQuestionLabel.setFont(new Font(null, Font.BOLD, 16)); // Set the font style and size
        securityQuestionLabel.setBounds(90, 90, 200, 20); // Set position and size
        
        // Create and configure the security answer input field
        securityAnswerTxt = new JTextField();  // Create an input field for security answer
        securityAnswerTxt.setHorizontalAlignment(JTextField.CENTER); // Center-align the text 
        securityAnswerTxt.setBounds(90, 130, 200, 20); // Set position and size
        
        // Create and configure the confirm button
        confirmButton = new JButton("Confirm"); // Create a button labaled "Confirm"
        confirmButton.setContentAreaFilled(false); // Make the button background transparent
        confirmButton.setOpaque(true); // Enable opacity for background color
        confirmButton.setFocusable(false); // Remove focus border when clicked
        confirmButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Change cursor to a hand when hovered
        confirmButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); // Add a white border 
        confirmButton.setBackground(Color.decode("#876F4D")); // Set the background color (goldish brown)
        confirmButton.setForeground(Color.WHITE); // Set text color to white
        confirmButton.setBounds(50, 200, 280, 30); // Set position and size
         
        // Add an action listener for the confirm button
        confirmButton.addActionListener(e -> {
            
            String securityAnswer = securityAnswerTxt.getText().trim(); // Retrieve the user-inputted answer and remove spaces

            // Check if the account is locked due to multiple failed attempts
            if (isBlocked()) {
                long timeleft = (blockTime + BLOCK_DURATION - System.currentTimeMillis()) / 1000; // Calculate remaining lock time
                JOptionPane.showMessageDialog(frame, "Account is locked. Please try again in " + timeleft + " seconds"); // Alert the user 
                return; // Stop further execution
            }

            // Validate the security answer input
            if (securityAnswer.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please enter the answer to proceed", "Enter an answer", JOptionPane.INFORMATION_MESSAGE);
            } else if (!securityAnswer.equals(rightAnswer)) { // If the answer is incorrect
                JOptionPane.showMessageDialog(frame, "Invalid Password. Attempts left: " + (MAX_FAILED_ATTEMPTS - failedAttempts - 1), "Warning", JOptionPane.WARNING_MESSAGE);
                failedAttempts++; // Increase the failed attempt counter

                // Lock the account if the maximum number of failed attempts is reached 
                if(failedAttempts >= MAX_FAILED_ATTEMPTS) {
                    blockTime = System.currentTimeMillis(); // Record the lock time

                    saveLockStatus(username, blockTime); // Save the lock status to prevent further attemmpts 

                    JOptionPane.showMessageDialog(frame, "Too many failed attempts. This action is locked for 1 minute", "Account temporary blocked", JOptionPane.ERROR_MESSAGE);
                }
            } else { // If the answer is correct
                failedAttempts = 0; // Reset the failed attempts counter

                // Load user data from the database or file
                HashMap<String, String> userData = loadUserData(); 

                // Create a unique key using encrypted email and username
                String key = currentEmail + ":" + username;

                // Check if the user exists in the database
                if (userData.containsKey(key)) {
                    // Ask the user for the final confirmation before deletion
                    int response = JOptionPane.showConfirmDialog(frame, // The 
                            "Are you sure you want to delete your account? This action cannot be undone.",
                            "DeleteAccount", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                    if (response == JOptionPane.YES_OPTION) { // If the user confirms account deletion
                        deleteUserData(currentEmail, username); // Delete usr data

                        try{
                            Files.deleteIfExists(Paths.get("session.txt")); // Delete the session file (logs the user out) 
                        } catch (IOException ioException) {
                            ioException.printStackTrace(); // Print the error if the file detection fails
                        }

                        frame.dispose(); // Close the current window
                        new MainPage(userData); // Redirect to the main page
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "The information does not match any account in the database.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        cancelButton = new JButton("Cancel");
        cancelButton.setContentAreaFilled(false);
        cancelButton.setOpaque(true);
        cancelButton.setFocusable(false);
        cancelButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cancelButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        cancelButton.setBackground(Color.decode("#876F4D"));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBounds(50, 240, 280, 30);
        
        // Add an aciton listener to the cancel button to handle user interaction
        cancelButton.addActionListener(e -> {
            try {
                // Delete the "session.txt" file if it exists (Remove saved login session)
                Files.deleteIfExists(Paths.get("session.txt"));
            } catch (IOException ioException) {
                ioException.printStackTrace(); // Print error details if the file deletion fails
            }
            
            frame.dispose(); //  Close the current window
            new EditProfilePage(user); // Reopen the EditProfilePage page
        });
        
        // Add a key listener to the securityAnswerTxt field to handle Enter key events
        securityAnswerTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // If the key is pressed, simulate a click on the confirm button
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    confirmButton.doClick(); // Triggers the confirm button action
                }
            }
        });
        
        // Create a MouseAdapter object to handle mouse events for multiple buttons
        MouseAdapter listener = new MouseAdapter() {
            
            // Method triggered when the mouse is clicked on a button 
            @Override
            public void mouseClicked(MouseEvent e) {
                JButton source = (JButton) e.getSource(); // Get the button that was clicked 
                source.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Changes the cursor icon to a hand
                source.setBackground(Color.WHITE); // Sets the background color of the button to white
                source.setForeground(Color.decode("8A6E4B")); // Sets the text color of the button to black
            }
            
            // Method triggered when a mouse button is pressed in a button
            @Override 
            public void mousePressed(MouseEvent e) {
                JButton source = (JButton) e.getSource(); // Get the button that was pressed
                source.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Keep the cursor as a hand icon
                source.setBackground(Color.WHITE); // Set the background color of the button to white
                source.setForeground(Color.decode("8A6E4B")); // Set the text color of the button to a Taupe color (Brown gold)
            }
            
            // Method triggered when the mouse is released after being pressed
            @Override
            public void mouseReleased(MouseEvent e) {
                JButton source = (JButton) e.getSource(); // Get the button that was released 
                source.setBackground(Color.decode("#876F4D")); // Set the background color of the button to khaki
                source.setForeground(Color.WHITE); // Set the text color of the button to white
                source.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); // Set the button border to white
            }

            // Method triggered when the mouse enters the button area 
            @Override
            public void mouseEntered(MouseEvent e) {
                JButton source = (JButton) e.getSource(); // Get the button that the mouse entered
                source.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Change the cursor icon to a hand 
                source.setBackground(Color.decode("#514937")); // Set the background color of the button to dark brownish-green 
                source.setForeground(Color.WHITE); // Set text color of the button to white
                source.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); // Set the button border to white
            }
            
            // Method triggered when the mouse exits the button area
            @Override
            public void mouseExited(MouseEvent e) {
                JButton source = (JButton) e.getSource(); // Get the button the the mouse exited
                source.setBackground(Color.decode("#876F4D")); // Set the background color of the button to khaki
                source.setForeground(Color.WHITE); // Set the text color of the button to white
                source.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); // Set the button border to white
            }
        };
        
        // Add same MouseAdapter (listener) to multiple buttons to apply behavior
        confirmButton.addMouseListener(listener);
        cancelButton.addMouseListener(listener); 
        
        // Add all components to the frame to make them visible
        frame.add(titleLabel);
        frame.add(securityQuestionLabel);
        frame.add(securityAnswerTxt);
        frame.add(confirmButton);
        frame.add(cancelButton);
        frame.setVisible(true);        
    }
    
    // Method to check if the account is currently blocked due to too many failed login attempts
    private boolean isBlocked() {
        if (failedAttempts >= MAX_FAILED_ATTEMPTS) { // To check if failed attempts exceed the maximum allowed
            long timeLeft = (blockTime + BLOCK_DURATION - System.currentTimeMillis()) / 1000; // Calculate remaining block time
            if (timeLeft > 0) { // This determines if there's still time in the lock duration, and if there is, then it returns true and the account stays locked
                return true; // Account is still blocked
            } else {                // In the case that the time of lock duration has expired
                failedAttempts = 0; // Reset failed attempts to 0 after lock expired
                blockTime = 0;      // Reset blockTime to 0, so the user can try to login again
                new File("delete_user_lock_status.txt").delete(); // Furthermore, the txt file that contained the lock status is deleted to unlock the login possibility 
                return false;                               // This is so the isBlocked method is not activated again when clicking the signUpButton
            }
        }
        return false; // In case the failed attempts are below the max limits, the account is not blocked, and the user can keep trying to login
    } 
    
    private HashMap<String, String> loadUserData() {
        HashMap<String, String> userData = new HashMap<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader("user_data.txt"))) {
            String line;
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                
                if (parts.length >= 3) {
                    String decryptedEmail = decryptData(parts[1]);
                    String decryptedUsername = decryptData(parts[0]);
                    
                    userData.put(decryptedEmail + ":" + decryptedUsername, line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userData;
    }
    
    // Method to delete a user's data based on their encrypted email and username
    private void deleteUserData(String currentEmail,  String username) {
        try{
            File file = new File("user_data.txt"); // References the original user data file
            File tempFile = new File("user_data_temp.txt"); // Creates a temporary file to store the updated data without the deleted user
            
            // CHecks if the temporary file exists;  if not, creates a new one
            if (!tempFile.exists()) {
                tempFile.createNewFile(); // Creates a new file if it does not exist
            }
                                    
            // Opens the original file for reading and the temp file for writing
            BufferedReader reader = new BufferedReader(new FileReader(file));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            
            String line; // Variable to store each line read from the file
            boolean foundUser = false; // Flag to track if the user is found
            
            // Reads each line from the original file
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");// Splits the line into an array of parts using a comma as delimeter
                
                // Ensures the line contains at least 3 elements
                if (parts.length >= 3) {
                    // Extracts and trims the stored email and username
                    String storedEmail = decryptData(parts[1]); 
                    String storedUsername = decryptData(parts[0]);

                    // If the email and username match the provided encrypted credentials, mark as found and skip writing this line 
                    if (storedEmail.equals(currentEmail) && storedUsername.equals(username)) {
                        foundUser = true;
                        continue;
                    }
                }
               
                writer.write(line); // Writes the current line to the temporary file (only if it's only not the deleted user)
                writer.newLine(); // Adds a new line after writing 
            }
            
            // Close the file reader and writer
            reader.close();
            writer.close();
            
            // If the user was found, proceed with replacing the onld file
            if (foundUser) {
                // Deletes the original user data file
                if (file.delete()) {
                    // Renames the temporary file to replace the original user data file
                    boolean renamed = tempFile.renameTo(file);
                    
                    // If renaming fails, throe an exception
                    if (!renamed) {
                        throw new IOException("Failed to rename temp file to original file");
                    }
                    
                    // Displays a success message indicating the account was deleted
                    JOptionPane.showMessageDialog(frame, "Your account has been successfully deleted", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // If no matching account was found, display an error message
                    throw new IOException("No matching account found ");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "No matching account found to delete", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            // Handles any exceptions during file operations and prints an error message
            e.printStackTrace();
            
            // Displays an error message in case of an issue deleting account data
            JOptionPane.showMessageDialog(frame, "Error occurred while deleting account data", "Error", JOptionPane.ERROR_MESSAGE); 
        }
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

    // Method to load the account lock status from a file
    private void loadLockStatus() {
        
        // Opens the file "delete_user_lock_status.txt" for reading using a BufferedReader
        try (BufferedReader reader = new BufferedReader(new FileReader("delete_user_lock_status.txt"))) {
            
            String line = reader.readLine(); // Reads the first line of the file
            
            if (line != null) { // Check if the file contains data
                String[] parts = line.split(","); // Splits the line into an array using a comma as a delimeter
                
                if (parts.length == 2) { // Ensures the file contains exactly 2 parts (username and lock timestamp)
                    String lockedUser = parts[0]; // Extracts the username that was locked
                    long lockedTime = Long.parseLong(parts[1]); // Converts the lock timestamp to a long value
                    
                    // Checks if the lock duration has not yet expired
                    if ((lockedTime + BLOCK_DURATION) > System.currentTimeMillis()) {
                        blockTime = lockedTime; // Updates the blockTime with the stored lock timestamp
                        failedAttempts = MAX_FAILED_ATTEMPTS; // Sets failed attempts to the maximum limit
                    } else {
                        // If the lock duration has expired, reset the lock staus
                        failedAttempts = 0; // Resets the failed attempts counter
                        blockTime = 0; // Clears the block timestamp
                        new File("delete_user_lock_status.txt").delete(); // Deletes the lock status file
                    }
                }
            }
        } catch (IOException e) {
            // Catches and prints an error message if there is an issue reading the file
            e.printStackTrace();
        }
    }

    // Method to save the lock status of a user to a fil. This is to prevent the lockout information even after the program is closed 
    private void saveLockStatus(String username, long blockTime) {
        
        // Opens the file "delete_user_lock_dtatus.txt" for writing using a BufferedWriter
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("delete_user_lock_status.txt"))) {
            writer.write(username + "," + blockTime); // Writes the username and block timestamp, separated by comma
        } catch (IOException e) {
            // Catches and prints an enrror message if there is an issue writing to a file
            e.printStackTrace();
        }
    }
    
}

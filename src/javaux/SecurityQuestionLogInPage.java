
package javaux;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class SecurityQuestionLogInPage {

    private JFrame frame;
    private JLabel titleLabel, securityQuestionLabel;
    private JTextField securityAnswerTxt;
    private JButton continueButton, cancelButton;
    private int failedAttempts = 0; // To keep track of how many times the user has attempted to log in with an incorrect password.
    private long blockTime = 0; // Stores the time stamp of when the account was blocked after reaching the maximum number of failed attempts. used in conjuction with BLOCK_DURATION to determine if the user is currently locked out
    private final int MAX_FAILED_ATTEMPTS = 3; // A constant that defines the maximum number of allowed failed login attempts before the account is locked
    private int BLOCK_DURATION = 30000; // Defining the lock duration in milliseconds *1 minute* [60 seconds * 1000 milliseconds = 1 minute]
    
    private static final String SECRET_KEY = "mysecretkey12345"; // Scret key forr encryption. This is not used in the code but it is defined
        
    // Constructor method for the Security Questuion Login page
    public SecurityQuestionLogInPage(User user) {
        loadLockStatus(); // Load lock status from file [if any]
        
        // Extract used details
        String username = user.getUsername(); 
        String securityQuestion = user.getQuestion();
        String answer = user.getAnswer();
        
        // Setup JFrame [main window]
        frame = new JFrame("LogIn Menu ADAMSON-AI");
        //frame.setTitle("LogIn Menu ADAMSON-AI");
        frame.setSize(400, 360);
        frame.getContentPane().setBackground(Color.decode("#222222"));
        frame.setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false); 
        
        // Add a new window windowslistener 
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
               try{
                   // Delete the session file when the window is closed
                   Files.deleteIfExists(Paths.get("session.txt"));
               } catch (IOException ioException) {
                   ioException.printStackTrace(); // In case there is an error, print a stack trace
               }
                System.exit(0);  // This closes the application
            }
        });
        
        ImageIcon image = new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\adamson-logo.png");
        frame.setIconImage(image.getImage());
        
        titleLabel = new JLabel("Please answer the security question");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font(null, Font.BOLD, 18)); 
        titleLabel.setBounds(0, 20, 380, 50);
        
        securityQuestionLabel = new JLabel(securityQuestion);
        securityQuestionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        securityQuestionLabel.setForeground(Color.decode("#876F4D"));
        securityQuestionLabel.setFont(new Font(null, Font.BOLD, 16));
        securityQuestionLabel.setBounds(90, 90, 200, 20);
        
        securityAnswerTxt = new JTextField();
        securityAnswerTxt.setHorizontalAlignment(JTextField.CENTER);
        securityAnswerTxt.setBounds(90, 130, 200, 20);
        
        continueButton = new JButton("Continue");
        continueButton.setContentAreaFilled(false);
        continueButton.setOpaque(true);
        continueButton.setFocusable(false);
        continueButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        continueButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        continueButton.setBackground(Color.decode("#876F4D"));
        continueButton.setForeground(Color.WHITE); 
        continueButton.setBounds(50, 200, 280, 30); 
        continueButton.addActionListener(e -> {
            String securityAnswer = securityAnswerTxt.getText().trim(); // Get the entered answer from user
            
            // To check if the account is currently locked due to failed attempts and if it is then display the following message
            if (isBlocked()) {
                long timeLeft = (blockTime + BLOCK_DURATION - System.currentTimeMillis()) / 1000; // Calculating how much time there's left to unblock action
                long minutes = timeLeft / 60; // calculate minutes, this extract minutes from total seconds
                long seconds = timeLeft % 60; // calculate remaining seconds, this extract remaining seconds after minutes
                JOptionPane.showMessageDialog(frame, "Account is locked. Please try again in " + minutes + " minute(s) and " + seconds + " second(s)", "Wait", JOptionPane.INFORMATION_MESSAGE);
                return; // Exit the function, preventing further login attempts
            }
                        
            if (securityAnswer.isEmpty()) { // Check if answer is empty
                JOptionPane.showMessageDialog(frame, "Please enter the answer to proceed", "Enter an answer", JOptionPane.INFORMATION_MESSAGE);
            } else if (!securityAnswer.equals(answer)) { // Check if the entered answer matches the correct answer
                //JOptionPane.showMessageDialog(frame, "Invalid Password. Attempts left: " + (MAX_FAILED_ATTEMPTS - failedAttempts - 1), "Warning", JOptionPane.WARNING_MESSAGE);
                failedAttempts++; // Increment failed attempts
                
                if (failedAttempts >= MAX_FAILED_ATTEMPTS) {
                    blockTime = System.currentTimeMillis(); // Record the current time and date as a unix timestamp when the account is blocked, by doin this the program can calculate how...
                                                            // long  the account has been locked. Without this timestamp, it wouldn't know when to allow the user to try logging in again.
                    BLOCK_DURATION *= 2; // This is two double the time duration of each block
                    
                    saveLockStatus(username, blockTime); // Save the lock status to a file so the block time persists even after closing the program
                    
                    JOptionPane.showMessageDialog(frame, "Too many failed attempts. Login action is locked for " + BLOCK_DURATION / 60000 + " minute(s)", "Error", JOptionPane.ERROR_MESSAGE);
                // If the password is incorrect and the maximum attempts have been reached already
                } else {
                    // If the password is incorrect but the maximum attempts have not been reached yet
                    JOptionPane.showMessageDialog(frame, "Invalid Security Question. Attempts left: " + (MAX_FAILED_ATTEMPTS - failedAttempts), "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
                else { // This statement is in case both the username and password entered by the user are correct and do exist in the HashMap
                    
                    failedAttempts = 0;
                    blockTime = 0;
                    BLOCK_DURATION = 60000;
                    
                    File lockFile = new File("security_Question_lock_status.txt");
                    if (lockFile.exists()) {
                        lockFile.delete();
                    }
                              
                    // After checking if the checkbox was selected, open a new frame and close the current frame.
                    frame.dispose();
                    new LoggedInPage(user);
                }
        });
        
        // 
        cancelButton = new JButton("Cancel");
        cancelButton.setContentAreaFilled(false);
        cancelButton.setOpaque(true);
        cancelButton.setFocusable(false);
        cancelButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cancelButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        cancelButton.setBackground(Color.decode("#876F4D"));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBounds(50, 240, 280, 30);
        cancelButton.addActionListener( e -> {
            try {
                Files.deleteIfExists(Paths.get("session.txt")); // Delete session file
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            
            frame.dispose();
            new MainPage(new HashMap<>());
        });
        
        // Add key listener to trigger the continue buton when the Enter key is pressed
        securityAnswerTxt.addKeyListener(new KeyAdapter() {
            @Override 
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    continueButton.doClick(); // Simulate button click
                }
            }
        });
        
        // Mouse listener for button hover events
        MouseAdapter listener = new MouseAdapter() {
            
            // Method triggered when the mouse is clicked on a button and it changes the cursor
            @Override
            public void mouseClicked(MouseEvent e) {
                JButton source = (JButton) e.getSource(); // Gets the button that was clicked
                source.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Changes the cursor icon to a hand
                source.setBackground(Color.WHITE); // Sets the background color of the button to white
                source.setForeground(Color.decode("#8A6E4B")); // Changes the tex color of the button to Taupe color (brown gold)
            }
            
            // Method triggered when a mouse button is pressed in a button. It modifies the button's appearance to provide visual feedback
            @Override
            public void mousePressed(MouseEvent e) {
                JButton source = (JButton) e.getSource(); // Gets the button that was pressed 
                source.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Changes the cursor icon to a hand
                source.setBackground(Color.WHITE); // Changes the background color of the button to white
                source.setForeground(Color.decode("#8A6E4B"));  // Changes the text color of the button to Taupe color (brown gold) 
            }
            
            // Method triggered when the mouse button is released after being pressed
            @Override
            public void mouseReleased(MouseEvent e) {
                JButton source = (JButton) e.getSource(); // Gets the button
                source.setBackground(Color.decode("#876F4D"));
                source.setForeground(Color.WHITE);
                source.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); 
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                JButton source = (JButton) e.getSource();
                source.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                source.setBackground(Color.decode("#514937"));
                source.setForeground(Color.WHITE); 
                source.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                JButton source = (JButton) e.getSource();
                source.setBackground(Color.decode("#876F4D"));
                source.setForeground(Color.WHITE);
                source.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
            }
        };
        
        // Add mouse listener to button for hover effects
        continueButton.addMouseListener(listener);
        cancelButton.addMouseListener(listener); 
        
        // Add all components to the frame to make them visible
        frame.add(titleLabel);
        frame.add(securityQuestionLabel);
        frame.add(securityAnswerTxt);
        frame.add(continueButton);
        frame.add(cancelButton);
        frame.setVisible(true);
    }
    
    // Method to check if the account is currently blocked due to too many failed login attempts
    private boolean isBlocked() {
        if (failedAttempts >= MAX_FAILED_ATTEMPTS) { // To check if failed attempts exceed the maximum allowed
            long timeLeft = (blockTime + BLOCK_DURATION - System.currentTimeMillis()) / 1000; // Calculate remaining block Time
            if (timeLeft > 0) { // This determines if there's still time in the lock duration, and if there is, then it returns true and the account stays locked                 
                return true; // Account is still blocked
            } else {                // In the case that the time of lock duration has expired
                failedAttempts = 0; // Reset failed attempts to 0 after lock expires
                blockTime = 0;      // Reset blockTime to 0, so the user can try to login again
//                new File("security_Question_lock_status.txt").delete(); // Furthermore, the txt file that contained the lock status is deleted to unlock the login possibility 
                return false;                         // This is so the isBlocked() method is not activated again when clicking the singnUpButton
            }
        }
        return false; // In case the failed attempts are below the max limits, the account is not blocekd, and the user can keep trying to login
    }

    // Method to load the account lock status from a file
    private void loadLockStatus() {
        
        // Opens the file "security_Question_lock_status.txt" for reading using a BufferedReader
        try (BufferedReader reader = new BufferedReader(new FileReader("security_Question_lock_status.txt"))) {
            
            String line = reader.readLine(); // Reads the first line of the file
            
            if (line != null) { // Check if the file contains data
                String[] parts = line.split(","); // Splits the line into an array using comma as delimeter
                
                if (parts.length == 4) { // Ensures the file contains exactly 3 parts (username, locked time, and block duration)
                    String lockedUser = parts[0]; // Extracts the username that was locked
                    long lockedTime = Long.parseLong(parts[1]); // converts the lock timestamp to a long value
                    int savedBlockDuration = Integer.parseInt(parts[2]);  // Read saved block duration
                    int savedFailedAttempts = Integer.parseInt(parts[3]);
                    
                    // If lock duration is still active, apply it (Check if the lock duration has not yet expired)
                    if ((lockedTime + BLOCK_DURATION) > System.currentTimeMillis()) {
                        blockTime = lockedTime; // Restore block time
                        failedAttempts = MAX_FAILED_ATTEMPTS; // Set to max to enformce lock
                        BLOCK_DURATION = savedBlockDuration; // Restire saved block
                    } else {
                        // If the lock duration expires, reset lock status
                        failedAttempts = 0; // Resets the failed attempts counter
                        blockTime = 0; // Clears the block timestamp
                        BLOCK_DURATION = savedBlockDuration; // Sets BLOCK_DURATION to 60 seconds (1 min)
                        new File("security_Question_lock_status.txt").delete();
                    }
                }
            }
        } catch (IOException e) {
            // Catches and prints an error message if there is an issue reading the file
            e.printStackTrace(); 
        }
    }

    // Method to save the lock status of an account. This is to preserve the lockout information even after the program is closed
    private void saveLockStatus(String username, long blockTime) {
        
        // Opens the file "security_Question_lock_stauts.txt" for writing using a BufferedWriter
        //try (BufferedWriter writer = new BufferedWriter(new FileWriter(securityQuestionLockStatusFile))) { 
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("security_Question_lock_status.txt"))) {     
            writer.write(username + "," + blockTime + "," + BLOCK_DURATION + "," + failedAttempts); // Save username, lock time, and block duration
        } catch (IOException e) {
            // Catches and prints an error message if there is an issue writinf to a file
            e.printStackTrace();
        }       
        
    }
    
}

package javaux;

import javax.swing.*; // Import Swing for GUI components
import java.awt.*; // Import AWT for layout and color management 
import java.awt.event.*; // Import AWT event listeners
import java.io.*; // Import for file reading and writing
import java.util.Base64; // Import for Base64 encoding and decoding
import java.util.HashMap; // Import for using HashMap data structure 
import javax.crypto.Cipher; // Import for AES encryption and decryption
import javax.crypto.spec.SecretKeySpec; // Import for specifying the AES encryption key

import javax.crypto.spec.*;

public class ForgotPasswordPage {
    
    // Declaring GUI components
    private JFrame frame;
    private JLabel forgotPasswordLabel;
    private JTextField emailTxt;
    private JButton nextButton, cancelButton;
    
    // Define a static secret key for AES encryption/decryption
    private static final String SECRET_KEY = "mysecretkey12345";
    
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    
    // Constructor to initialize the forgot password page
    public ForgotPasswordPage() {
        // Create a new JFrame (Window) with a title
        frame = new JFrame("Forgot Password - ASAMSON AI");
        frame.setSize(400, 300); // Set the window size
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close the application on exit
        frame.setResizable(false); // Prevent resizing of the window
        frame.getContentPane().setBackground(Color.decode("#222222")); // Set background color
        frame.setLayout(null); // Use absolute positioning for elements
        frame.setLocationRelativeTo(null); // Center the window on screen
        
        // Set an icon image for the application window
        ImageIcon image = new ImageIcon("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\src\\adamson-logo.png"); // Loads a image icon for the application from the given path
        frame.setIconImage(image.getImage()); // Sets the icon for the JFrame window
        
        // JLabel prompting the user to enter their email
        forgotPasswordLabel = new JLabel("Enter Your Email");
        forgotPasswordLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center align text
        forgotPasswordLabel.setForeground(Color.WHITE); // Set text color to white
        forgotPasswordLabel.setFont(new Font(null, Font.BOLD, 18)); // Set font size and style
        forgotPasswordLabel.setBounds(100, 20, 180, 50); // set position and size
        
        // JTextField for email input
        emailTxt = new JTextField();
        emailTxt.setHorizontalAlignment(JTextField.CENTER); // Center align text inside field
        emailTxt.setBounds(90, 90, 200, 20); // Set position and size
        
        // JButton 'Next' for confirmation
        nextButton = new JButton("Next");
        nextButton.setContentAreaFilled(false); // Remove default background behavior
        nextButton.setOpaque(true); // Enable custom background
        nextButton.setFocusable(false); // Remove focus outline
        nextButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Change cursor to hand
        nextButton.setBounds(50, 150, 280, 30); // Set position and size
        nextButton.setBackground(Color.decode("#876F4D")); // Set background color
        nextButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); // Set border
        nextButton.setForeground(Color.WHITE); // Set foreground color
        nextButton.addActionListener(e -> { // Action listener to verify information entered by the user
            String email = emailTxt.getText().trim(); // Get the entered email and trim spaces
            String[] userData = emailExists(email); // Check ifemail exists in the file
            
            if (userData != null) { // if email exists, proceed to next step
                openEmailFrame(userData[0], userData[1], userData[2], userData[3]);
            } else { // If email does not exists do the following
                JOptionPane.showMessageDialog(frame, "Email not found", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Create a 'Cancel' button
        cancelButton = new JButton("Cancel");
        cancelButton.setContentAreaFilled(false);
        cancelButton.setOpaque(true);
        cancelButton.setFocusable(false);
        cancelButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cancelButton.setBounds(50, 190, 280, 30);
        cancelButton.setBackground(Color.decode("#876F4D"));
        cancelButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> { // Action listener for the Cancel button
            frame.dispose(); // Close the current window
            new MainPage(new HashMap<>()); // Open the main page with an empty HashMap
        });
        
        // Key listener that will automatically trigger the next button if the enter key is pressed in the keyboard
        emailTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Check if the Enter key is pressed
                    nextButton.doClick(); // Simulate a click on the nextButton button
                }
            }
        });
        
        // Create a MouseAdapter object to handle mouse events for multiple buttons
        MouseAdapter listener = new MouseAdapter() {
          
            // Methods triggeres when the mouse is clicked on a button that was clicked and it also chages the cursor to hand and modifies the button appearance
            @Override
            public void mouseClicked(MouseEvent e) {
                JButton source = (JButton) e.getSource(); // Gets the button that was clicked and it also changes the cursor to a hand and modifies the button appearance
                source.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Changes the cursor icon to a hand
                source.setBackground(Color.WHITE); // Sets the background color of the button to white
                source.setForeground(Color.WHITE); // Sets the text color of the button to black
                source.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1)); // Sets the button order to white
            }
            
            // Method triggered when a mouse button is pressed in a button. It modifies the button's appearance to provide visual feedback
            @Override
            public void mousePressed(MouseEvent e) {
                JButton source = (JButton) e.getSource(); // Gets the button that was pressed
                source.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Changes the cursor icon to a hand
                source.setBackground(Color.WHITE); // Sets the background color of the button to white
                source.setForeground(Color.decode("#8A6E4B")); // Sets the text color of the button to a TAupe color (Brown gold)
            }
            
            // Method triggered when a mouse button is released after being pressed 
            @Override
            public void mouseReleased(MouseEvent e) {
                JButton source = (JButton) e.getSource(); // Gets the button that was released after being pressed
                source.setBackground(Color.decode("#876F4D")); // Sets background color of the button to khaki 
                source.setForeground(Color.WHITE); // Sets te text color of the button to white
                source.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));  // Sets the button border to white 
            }
            
            // Method triggered when the mouse enters the button area and it provides visual feedback to indicate interactivity
            @Override 
            public void mouseEntered(MouseEvent e) {
                JButton source = (JButton) e.getSource(); // Gets the button that the mouse entered
                source.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Changes the cursor icon to hand
                source.setBackground(Color.decode("#514937")); // Sets the background color of a buttoon to a dark brownish-green 
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
        
        // Add same MouseAdapter (listener) to multiple buttons to apply the behavior
        nextButton.addMouseListener(listener);
        cancelButton.addMouseListener(listener);
        
        // Add all components to the frame to make them visible
        frame.add(forgotPasswordLabel);
        frame.add(emailTxt);
        frame.add(nextButton);
        frame.add(cancelButton);
        frame.setVisible(true);
    }
   
    // Method to check if an email exists in the file and retrieve user data if found
    private String[] emailExists(String email) {
        // Try-with-resources to automatically close the BufferedReader after execution
        try (BufferedReader br = new BufferedReader(new FileReader("user_data.txt"))) {
            String line; //Variable to store each line read from the file
            
            // Read the file line by line
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(","); // Split the line by commas to extract user data
                if (parts.length > 4) { // Ensure the file has at least 4 parts per line
                    String encryptedUsername = parts[0].trim(); // Extract and trim the encrypted username
                    String encryptedEmail = parts[1].trim(); // Extract and trim the encrypted email
                    String encryptedSecurityQuestion = parts[3].trim(); // Extract and trim the encrypted security question
                    String encryptedAnswer = parts[4].trim(); // Decrypt security answer
                                        
                    // Decrypt the ectracted data
                    String decryptedUsername = decryptData(encryptedUsername); // Decrypt username
                    String decryptedEmail = decryptData(encryptedEmail); // Decrypt email
                    String decryptedSecurityQuestion = decryptData(encryptedSecurityQuestion); // Decrypt security question
                    String decryptedAnswer = decryptData(encryptedAnswer); // Decrypt security answer
                    
                    // Check if the provided email matches the decrypted email from the file
                    if (decryptedEmail.equalsIgnoreCase(email)) {
                        return new String[] {decryptedUsername, decryptedEmail, decryptedSecurityQuestion, decryptedAnswer};
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Print error details if the file reading fails
        }
        return null; // Returns null if no match is found or if an error occues
    }

    // Method to open the security question page
    private void openEmailFrame(String username, String email, String securityQuestion, String answer) {
        new ChangeForgotPasswordPage(username, email, securityQuestion, answer); // Open the password chage page
        frame.dispose(); // Close current frame
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

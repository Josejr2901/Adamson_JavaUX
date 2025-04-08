/* 

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
            } else if (!securityAnswer.equals(answer)) { // If the answer is incorrect
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
                
                // Encrypt the username and email for verification
                String encryptedCurrentEmail = encryptData(currentEmail);
                String encryptedCurrentUsername = encryptData(username);
                
                // Create a unique key using encrypted email and username
                String key = encryptedCurrentEmail + ":" + encryptedCurrentUsername;
                
                // Check if the user exists in the database
                if (userData.containsKey(key)) {
                    // Ask the user for the final confirmation before deletion
                    int response = JOptionPane.showConfirmDialog(frame, // The 
                            "Are you sure you want to delete your account? This action cannot be undone.",
                            "DeleteAccount", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    
                    if (response == JOptionPane.YES_OPTION) { // If the user confirms account deletion
                        deleteUserData(encryptedCurrentEmail, encryptedCurrentUsername); // Delete usr data
                        
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

*/
/* 

int day = (int) dayDropdown.getSelectedItem();
String monthName = (String) monthDropdown.getSelectedItem(); // Still using getMonthName
int year = (int) yearDropdown.getSelectedItem();

// Convert month name to Calendar-compatible month number (0-based)
int month = getMonthNumber(monthName) - 1; // Calendar uses 0-based months

// Create Calendar object for selected date
Calendar selectedCal = Calendar.getInstance();
selectedCal.setLenient(false); // Strict date checking (e.g. Feb 30 will throw)
selectedCal.set(year, month, day);

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
        String birthday = String.format("%d %s %d", day, monthName, year);
        System.out.println("Valid birthday: " + birthday);
        // Proceed with saving or encrypting
    }

} catch (IllegalArgumentException e) {
    // Catch invalid calendar values
    JOptionPane.showMessageDialog(null, "Invalid date selected!", "Date Error", JOptionPane.ERROR_MESSAGE);
}

//////////////////////////////////////////////////////////////////////////

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

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

// Declare birthday outside of the try-catch block so it's accessible later
String birthday = ""; // Initialize as an empty string or set it to a default value

// Step 1: Get selected values from dropdowns
int day = (int) dayDropdown.getSelectedItem();
String monthName = (String) monthDropdown.getSelectedItem(); // Using getMonthName
int year = (int) yearDropdown.getSelectedItem();

// Convert month name to Calendar-compatible month number (0-based)
int month = getMonthNumber(monthName) - 1; // Calendar uses 0-based months

// Step 2: Create Calendar object for selected date
Calendar selectedCal = Calendar.getInstance();
selectedCal.setLenient(false); // Strict date checking (e.g. Feb 30 will throw)
selectedCal.set(year, month, day);

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

        // Step 3: Writing user data to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("user_data.txt", true))) {
            writer.write(encryptedUsername + "," + encryptedEmail + "," + encryptedPassword + "," + encryptedQuestion + "," + encryptedAnswer + "," + encryptedBirthday + "," + encryptedGender); // Write user data
            writer.newLine(); // add a new line after the data
            JOptionPane.showMessageDialog(frame, "Sign up successful!"); 
            frame.dispose();
            mainPage.refreshLoginInfo(); // Refresh login info after signup
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error saving data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
} catch (IllegalArgumentException e) {
    // Catch invalid calendar values
    JOptionPane.showMessageDialog(null, "Invalid date selected!", "Date Error", JOptionPane.ERROR_MESSAGE);
}

//////////////////////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

  
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
            selectedCal.setLenient(false); // Strict date cheking (e.g. Feb 30 will throw)
            selectedCal.set(year, month, day);

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

                //// Encrypt the collected data 
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


/////

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
*/


/* 

import javax.swing.*;
import java.awt.*;

public class CustomJOptionPane {
    public static void main(String[] args) {
        // Set background color for JOptionPane
        UIManager.put("Panel.background", new Color(30, 30, 30)); // Dark gray
        UIManager.put("OptionPane.background", new Color(30, 30, 30)); // Dialog background

        // Set foreground (font) color
        UIManager.put("OptionPane.messageForeground", Color.WHITE); // Text color

        // Optional: set font
        UIManager.put("OptionPane.messageFont", new Font("Arial", Font.BOLD, 14));

        // Customize buttons
        UIManager.put("Button.background", new Color(70, 70, 70));  // Button background
        UIManager.put("Button.foreground", Color.WHITE);            // Button text color
        UIManager.put("Button.font", new Font("Arial", Font.PLAIN, 13));
        UIManager.put("Button.border", BorderFactory.createLineBorder(Color.WHITE));


        // Show the customized dialog
        JFrame frame = new JFrame();
        JOptionPane.showMessageDialog(
            frame,
            "Error loading user data: Something went wrong!",
            "Error",
            JOptionPane.WARNING_MESSAGE
        );
    }
}




*/
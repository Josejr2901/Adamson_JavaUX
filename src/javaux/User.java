 
package javaux;

// The user class represents a user with various attributes
public class User {
    
    private String username; // Stores the username of the user
    private String password; // Stores the encrypted or plain-text password of the user
    private String email; // Stores the email address of the user
    private String question; // Stores the security question for account recovery
    private String answer; // Stores the answer to the security question 
    private String birthday;
    private String gender;

    // Constructor to initialize a User object with the provided details
    public User(String username, String password, String email, String question, String answer, String birthday, String gender) {

        this.username = username; // Assigns the provided username to the instance variable 
        this.password = password; // Assigns the provided password to the instance variable
        this.email = email; // Assigns the provided email to the instance variable
        this.question = question; // Assigns the provided security question to the instance variable 
        this.answer = answer; // Assigns the provided security answer to the instance variable
        this.birthday = birthday; // Assigns the provided birthday to the instance variable
        this.gender = gender; // Assigns the provided gender to the instance variable
    }

    // Getter methods to retrieve user details
    
    // Returns the username of the user
    public String getUsername() {
        return username;
    }

    // Returns the password of the user
    public String getPassword() {
        return password;
    }

    // Returns the email of the user
    public String getEmail() {
        return email;
    }

    // Returns the security question of the user
    public String getQuestion() {
        return question;
    }

    // Returns the security answer of the user
    public String getAnswer() {
        return answer;
    }

    // Returns the birthday of the user
    public String getBirthday() {
        return birthday;
    }

    // Returns the gender of the user
    public String getGender() {
        return gender;
    }
    
}

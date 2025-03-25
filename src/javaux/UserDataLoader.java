
package javaux;

// Importing Java file handling classes
import java.io.File; // Represents a file or diretory pathname in an abstract manner
import java.io.FileNotFoundException; // Exception thrown when an attempt to open a file fails

// Importing Java collections framework
import java.util.HashMap; // Implements a data structure that sores key-value pairs for fast retrieval
import java.util.Map; // Defines a collection that maps keys to values, providing an interface for HashMap

// Importing Java utility for reading input
import java.util.Scanner; // Used for reading input from files, user input, or other sources

public class UserDataLoader {
    public static Map<String, String> loadLoginInfoFromCsv(String filePath) { //Here to fix the error we change the method's return type to Map<String, String>
    //public static HashMap<String, String> loadLoginInfoFromCsv(String filePath) { //Here I am declaring the return type of the method as HashMap<String, String> 
        Map<String, String> loginInfo = new HashMap<>(); //Here I am returning a Map<String, String> and loginInfo is declared as Map, not HashMap.
                                                        //This is allowed because of Polymorphism, here Map is a parent type and HashMap is a subtype object and ...
                                                       //Java allows you to create a variable of type Map and assign it a HashMap instance because HashMap is a Map ...
                                                       //and it implements the Map interface.
                                                       //Map<String, String> → the interface. & new HashMap<>() → an instance of a class implementing that interface.
                                                       //It is better declaring it as Map instead of HashMap because This allows you to easily switch to a different Map implementation 
                                                       //(like TreeMap, LinkedHashMap, or ConcurrentHashMap) without changing the rest of the code.
                
        // Try-catch with resources "()" ensures Scanner is automatically closed after use
        try (Scanner scanner = new Scanner(new File(filePath))) { //This try-catch with (resource) will automatically close any resource(Scanner in this case) that implements the AutoCloseable or Closeable interface
            while (scanner.hasNextLine()) { // Read the file line by line
                String line = scanner.nextLine();
                String[] data = line.split(",");

                // Ensure there are enough columns for at least username and password
                if (data.length >= 3) {
                    String username = data[0].trim();  // Extract and trim the username
                    String password = data[2].trim();  // Extract and trim the password
                    loginInfo.put(username, password); // Store username and password in the HashMap loginInfo
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error loading user data: " + e.getMessage());
        }
        // Return the map containing login credentials to make the user data accessible for other parts of the program like authentication or displaying user information.
        return loginInfo; // The method expects a HashMap but the declared type is Map, not HashMap. That is why we change the method's return type to Map<String, String> at the beginning 
    }
}

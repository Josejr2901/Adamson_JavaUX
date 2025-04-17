
package javaux;

import java.util.Map;

public class JavaUX {
    public static void main(String[] args) {
        // Initialize MainPage with user data loaded from the CSV file
        // The method 'UserDataLoader.loadLoginInfoFromCsv()' reads the file and returns a Map<String, String>
        // This map stores usernames as keys and passwords as values.
        Map<String, String> loginInfo = UserDataLoader.loadLoginInfoFromCsv("C:\\Users\\Jose.m\\Documents\\NetBeansProjects\\JavaUX\\user_data.txt");
        new MainPage(loginInfo);      
        //Comment for Github
    }
}


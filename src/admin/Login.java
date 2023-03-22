/**
 * 
 */
package admin;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @author espin
 *
 */
public class Login {

	/**
	 * @param args
	 */
	private String username;
	private String password;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean isValidCredentials(String username, String password) {
		File credentials = new File("adminCredentials.txt");
		try (Scanner scanner = new Scanner(credentials)) {
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                String user = parts[0];
                String pass = parts[1];
                
                if (username.equals(user) && password.equals(pass)) {
                	return true;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: adminCredentials.txt");
            return false;
        }
		return false;
	}

}

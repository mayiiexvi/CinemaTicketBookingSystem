/**
 * 
 */
package admin;

import java.util.Scanner;

/**
 * @author espin
 *
 */
public class Admin {

	/**
	 * @param args
	 */
	public static void main(String[] args){
		String username;
		String password;
		
		Scanner keyboard = new Scanner(System.in);
		Login login = new Login();
		do {
			System.out.println("Login Details");
			System.out.println("------------------");
			System.out.print("Enter Username: ");
	        username = keyboard.nextLine();
	        System.out.print("Enter Password: ");
	        password = keyboard.nextLine();
	        
	        if(login.isValidCredentials(username, password)) {
	        	System.out.println("Login successful");
	        }
	        else {
	        	System.out.println("Invalid Username/Password. Please try again");
	        }
		}while(!login.isValidCredentials(username, password));
		
	}

}

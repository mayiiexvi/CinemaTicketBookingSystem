/**
 * 
 */
package admin;

import java.sql.Connection;
import java.util.Scanner;

import cinemaTicketBookingSystem.Movie;
import common.DatabaseConnection;
import common.User;

/**
 * @author 
 *
 */
public class Admin {

	/**
	 * @param args
	 */
	static Connection connection;
	public static void addToList() {
		try {
			Scanner keyboard = new Scanner(System.in);
			
			System.out.println("Enter movie name: ");
			String movieName = keyboard.nextLine();
			System.out.println("Enter movie synopsis: ");
			String synopsis = keyboard.nextLine();
			System.out.println("Enter movie price: ");
			String stringPrice = keyboard.nextLine();
			double price = Double.parseDouble(stringPrice);
			
			Movie.addToNowShowing(movieName, synopsis, price);

			keyboard.close();
		} catch (Exception e) {
			System.out.println("Error: " + e);
		}
		
		
	}
	
	public static void menu() {		
		Scanner keyboard = new Scanner(System.in);
        String num = "";
        int number = 0;
        
       
    	while (number != 5) {
    		try {
    			//System.out.println("Welcome Admin!");
    			System.out.println("--------------------------------------------");
    			System.out.println("1 - View all movies from list");
    			System.out.println("2 - Add movie to list");
    			System.out.println("3 - Update movie from list");
    			System.out.println("4 - Delete movie from list");
    			System.out.println("5 - Exit");
        		System.out.print("Please enter 1-5: ");
        		
            	num = keyboard.next();
            	number = Integer.parseInt(num);
            	if (number == 1) {
            		System.out.println("1");
            		Movie.viewAllNowShowing();
            	} else if (number == 2) {
            		System.out.println("2");
            		addToList();
            	} else if (number == 3) {
            		System.out.println("3");
            	} else if (number == 4) {
            		System.out.println("4");
            	} else if (number == 5) {
            		System.out.println("Thank you for using our program!");
            		keyboard.close();
            		System.exit(0);
            	} else {
            		throw new Exception();
            	}
            } catch (Exception e) {
            	System.out.println("Please only enter 1-5. Try again. Error: " + e);
            }
    	}
       
	}
	
	public static void main(String[] args){
		try {
			connection = DatabaseConnection.getInstance().getConnection();
			
			String username;
			String password;
			User userLogin;
			Scanner keyboard = new Scanner(System.in);
			do {
				System.out.println("Login Details");
				System.out.println("------------------");
				System.out.print("Enter Username: ");
		        username = keyboard.nextLine();
		        System.out.print("Enter Password: ");
		        password = keyboard.nextLine();
		        userLogin = User.isValidCredentials(connection, username, password);
		        
		        if(userLogin.getId() != 0) {
		        	System.out.println("Welcome, " + userLogin.getFirstName());
		        	menu();
		        }
		        else {
		        	System.out.println("Invalid Username/Password. Please try again\n");
		        }
			} while(userLogin.getId() == 0);
			
			keyboard.close();
		}
		catch (Exception e) {
			System.err.println(e.toString());
		}
		
	}

}

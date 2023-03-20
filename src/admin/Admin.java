/**
 * 
 */
package admin;

import java.util.Scanner;

import cinemaTicketBookingSystem.Movie;

/**
 * @author 
 *
 */
public class Admin {

	/**
	 * @param args
	 */
	
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
		System.out.println("Welcome Admin!");
		System.out.println("--------------------------------------------");
		System.out.println("1 - View all movies from list");
		System.out.println("2 - Add movie to list");
		System.out.println("3 - Update movie from list");
		System.out.println("4 - Delete movie from list");
		System.out.println("5 - Exit");
		
		Scanner keyboard = new Scanner(System.in);
        String num = "";
        boolean isValid = true;
        
       
    	try {
    		System.out.print("Please enter 1-5: ");
        	num = keyboard.next();
        	int number = Integer.parseInt(num);
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
	
	public static void main(String[] args){
		String username;
		String password;
		
		Scanner keyboard = new Scanner(System.in);
		Login login = new Login();
		do {
			System.out.flush();
			System.out.println("Login Details");
			System.out.println("------------------");
			System.out.print("Enter Username: ");
	        username = keyboard.nextLine();
	        System.out.print("Enter Password: ");
	        password = keyboard.nextLine();
	        
	        if(login.isValidCredentials(username, password)) {
	        	System.out.println("Login successful");
	        	menu();
	        }
	        else {
	        	System.out.println("Invalid Username/Password. Please try again\n");
	        }
		}while(!login.isValidCredentials(username, password));
		
	}

}

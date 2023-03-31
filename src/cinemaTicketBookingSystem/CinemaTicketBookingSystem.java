/**
 * 
 */
package cinemaTicketBookingSystem;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import admin.Admin;
import common.DataValidation;
import common.DatabaseConnection;
import guest.Guest;

/**
 * @author Sylvia Espina C0866311
 * @author Mufida Andi C0864756
 * @author Jenil Shivamkumar Varma - c0870543
 * @author Tich Vu Lu C0861736
 * @author Jay Shah C0868053
 */

/**
	Cinema Ticket Booking System : For booking movie ticket
	To run this app user needs to run the main method in the CinemaTicketBooking System class. 
	Before run the this app please ensure that you are connected with database.
	User name: java2 and password: java2  
  */
public class CinemaTicketBookingSystem {

	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {

		// for connecting the database
		Connection connection = DatabaseConnection.getInstance().getConnection();
		DatabaseConnection.checkAndInitializeDatabase(connection);
		
		// Validation for keep asking till getting the valid input from the user
		Scanner keyboard = new Scanner(System.in);
        String num = "";
        int number = 0;
        
        while (number != 3) {
        	try {
        		menu();		
	        	number = DataValidation.readPositiveInt("Please select the option:");
	        	if (number == 1) {
	        		Admin.main(null);      // Calling main method of the Admin class 
	        	} else if (number == 2) {
	        		Guest.main(null);
	        	} else if (number == 3) {
	        		System.out.println("Thank you for using our program!");
	                keyboard.close();
	        		System.exit(0);
	        	} else {
	        		// showing the message while selecting the wrong input
	        		System.out.println("Oops! Wrong option selected.");
	        		System.out.println("Please select the correct option.");
	        		throw new Exception();
	        	}
	        } catch (Exception e) {
	        	System.out.println(e.toString());
	        }
        };

	}
	
	/**
	 * Show main menu of the program
	 */
	public static void menu() {        
		System.out.println("Welcome to our Cinema Ticket Booking System!");
		System.out.println("--------------------------------------------");
		System.out.println("1 - Login as Admin");
		System.out.println("2 - Login as Guest");
		System.out.println("3 - Exit");			
	}

}

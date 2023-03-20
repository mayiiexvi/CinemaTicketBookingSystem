/**
 * 
 */
package cinemaTicketBookingSystem;
import admin.Admin;
import java.util.Scanner;

import admin.Login;
import guest.Guest;

/**
 * @author mufida
 *
 */
public class CinemaTicketBookingSystem {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		menu();		
	}
	
	public static void menu() {        
		System.out.println("Welcome to our Cinema Ticket Booking System!");
		System.out.println("--------------------------------------------");
		System.out.println("1 - Login as Admin");
		System.out.println("2 - Login as Guest");
		System.out.println("3 - Exit");
		
		Scanner keyboard = new Scanner(System.in);
        String num = "";
        boolean isValid = true;
        
        do {
        	try {
        		System.out.print("Please enter 1/2: ");
	        	num = keyboard.next();
	        	int number = Integer.parseInt(num);
	        	if (number == 1) {
	        		Admin.main(null);
	        	} else if (number == 2) {
	        		Guest.main(null);
	        	} else if (number == 3) {
	        		System.out.println("Thank you for using our program!");
	        		System.exit(0);
	        	} else {
	        		throw new Exception();
	        	}
	        } catch (Exception e) {
	        	System.out.println("Please only enter 1 or 2. Try again.");
	        	isValid = false;
	        }
        } while (!isValid);

        keyboard.close();
	}

}

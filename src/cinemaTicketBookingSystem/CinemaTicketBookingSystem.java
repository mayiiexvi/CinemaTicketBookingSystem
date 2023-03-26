/**
 * 
 */
package cinemaTicketBookingSystem;
import java.util.Scanner;

import admin.Admin;
import common.DataValidation;
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
		Scanner keyboard = new Scanner(System.in);
        String num = "";
        int number = 0;
        
        while (number != 3) {
        	try {
        		menu();		
	        	number = DataValidation.readPositiveInt("Please enter 1/2: ");
	        	if (number == 1) {
	        		Admin.main(null);
	        	} else if (number == 2) {
	        		Guest.main(null);
	        	} else if (number == 3) {
	        		System.out.println("Thank you for using our program!");
	                keyboard.close();
	        		System.exit(0);
	        	} else {
	        		throw new Exception();
	        	}
	        } catch (Exception e) {
	        	System.out.println(e.toString());
	        }
        };

	}
	
	public static void menu() {        
		System.out.println("Welcome to our Cinema Ticket Booking System!");
		System.out.println("--------------------------------------------");
		System.out.println("1 - Login as Admin");
		System.out.println("2 - Login as Guest");
		System.out.println("3 - Exit");			
	}

}

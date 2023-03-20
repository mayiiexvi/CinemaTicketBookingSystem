/**
 * 
 */
package guest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import cinemaTicketBookingSystem.Movie;

/**
 * @author 
 *
 */
public class Guest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		menu();

	}
	public static void menu() {
		System.out.println("Welcome Guest!");
		System.out.println("--------------------------------------------");
		System.out.println("1 - View now showing");
		System.out.println("2 - Choose a movie");
		System.out.println("3 - Exit");
		
		Scanner keyboard = new Scanner(System.in);
        String num = "";
        boolean isValid = true;
       
    	try {
    		System.out.print("Please enter 1-3: ");
        	num = keyboard.next();
        	int number = Integer.parseInt(num);
        	if (number == 1) {
        		viewNowShowing();
        	}
        	else if(number == 2) {
        		chooseAMovie();
        	}
        	else if (number == 3) {
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
	
	public static void viewNowShowing() throws IOException {
		String option;
		
		Movie.viewAllNowShowing();
		
		/*To add print where user is asked if they want to proceed with choosing movie or exit*/
	}
	
	public static void chooseAMovie() throws IOException {
		Scanner keyboard = new Scanner(System.in);
		String num;
		System.out.println("Now Showing");
		System.out.println("--------------------------------------------");

		for(String movie: Movie.movieList()) {
			System.out.println(movie);
		}
	
		while(true){
			//Not final yet. 
    		System.out.print("Please enter movie ID: ");
        	num = keyboard.next();
        	int number = Integer.parseInt(num);
        	if (number < Movie.movieList().size() && number >= 1) {
        		viewSeating();
        		keyboard.close();
        		break;
        	} else {
        		System.out.println("Movie ID Provided do not exist. Please try again.");
        	}
        } 
       
			
	}
	
	public static void viewSeating() {
		char[] seats = {'e','d', 'c', 'b', 'a'};
		for(int i = 0; i < seats.length; i++) {
			if(seats[i] == 'a' || seats[i] == 'b') {
				rowAB(seats[i]);
			}
			else if(seats[i] == 'e') {
				System.out.println(" __ __ __ __                      __ __ __ __ __ ");
				for(int seatNumber = 1; seatNumber <= 9; seatNumber++) {
					
					if(seatNumber == 1) {
						System.out.printf("|%s%d|", seats[i], seatNumber);
					}
					else if(seatNumber == 5) {
						System.out.printf("                    |%s%d|", seats[i], seatNumber);
					}
					else
					{
						System.out.printf("%s%d|", seats[i], seatNumber);
					}
				}
			}
			else {
				rowCD(seats[i]);
			}
		}
			
		System.out.printf("\n  ____________________________________________");
		System.out.printf("\n |                  Screen                    |");
		System.out.println();
	}
	
	public static void rowAB(char row) {
		System.out.println("\n    __ __ __    __ __ __ __ __    __ ___ ___ ");
		for(int seatNumber = 1; seatNumber <= 11; seatNumber++) {
			
			if(seatNumber == 1) {
				System.out.printf("   |%s%d|", row,seatNumber);
			}
			else if(seatNumber == 4 || seatNumber == 9) {
				System.out.printf("  |%s%d|",  row,seatNumber);
			}
			else
			{
				System.out.printf("%s%d|", row, seatNumber);
			}
		}
	}
	
	public static void rowCD(char row) {
		System.out.println("\n __ __ __ __    __ __ __ __ __    ___ ___ ___ ___ ");
		for(int seatNumber = 1; seatNumber <= 13; seatNumber++) {
			
			if(seatNumber == 1) {
				System.out.printf("|%s%d|", row, seatNumber);
			}
			else if(seatNumber == 5 || seatNumber == 10) {
				System.out.printf("  |%s%d|", row, seatNumber);
			}
			else
			{
				System.out.printf("%s%d|", row, seatNumber);
			}
		}
	}
}

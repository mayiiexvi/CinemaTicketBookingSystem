/**
 * 
 */
package guest;

import java.io.IOException;
import java.util.Arrays;
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
	public static void main(String[] args) {menu();
		Scanner keyboard = new Scanner(System.in);
        int number = 0;
        int movieId = 0;
        int numOfSeats = 0;
       
        while(number != 5) {
        	try {
        		menu(); 
    			number = keyboard.nextInt();
            	if (number == 1) {
            		viewNowShowing();
            	}
            	else if(number == 2) {
            		movieId = chooseAMovie();
            	}
            	else if(number == 3) {
            		numOfSeats = chooseSeat(movieId);
            	}
            	else if(number == 4) {
            		payment(movieId, numOfSeats);
            	}
            	else if (number == 5) {
            		System.out.println("Thank you for using our program!");
            		System.exit(0);
            	} else {
            		throw new Exception();
            	}	
    		}
    		 catch (Exception e) {
    	        	System.out.println("Please enter valid input. Try again.\nError: " + e);
	        }
        }
		keyboard.close();
	}
	
	public static void menu() {
		System.out.println("\nWelcome Guest!");
		System.out.println("--------------------------------------------");
		System.out.println("1 - View now showing");
		System.out.println("2 - Choose a movie");
		System.out.println("3 - Choose your seats"); //Remove this we should only show 1,2 and 5
		System.out.println("4 - Payment");
		System.out.println("5 - Exit");
		System.out.print("Please enter 1-5: ");       
	}
	
	public static void viewNowShowing() throws IOException {
		//Movie.viewAllNowShowing();
		
		/*To add print where user is asked if they want to proceed with choosing movie or exit*/
	}
	
	public static int chooseAMovie() throws IOException {
		Scanner keyboard = new Scanner(System.in);
    String[] seats = new String[100];
    int number = 0;
		System.out.println("Now Showing");
		System.out.println("--------------------------------------------");

		for(String movie: Movie.movieList()) {
			System.out.println(movie);
		}
	
		while(true){
			//Not final yet. 
    		System.out.print("Please enter movie ID: ");
        	number = keyboard.nextInt();
        	if (number <= Movie.movieList().size() && number >= 1) {
            viewSeat(seats);
            break;
        	} else {
        		System.out.println("Movie ID Provided do not exist. Please try again.");
        	}
        }
		//keyboard.close();
		return number;
	}	
	
	public static void viewSeat(String[] seatSelected) {
		String[][] seats = new String [5][14];
		String seatLetter = null;
		int seatsRowLength = 0;
		System.out.println("\nCinema Hall 1"); //Will be updated to allow multiple cinema
		System.out.println("--------------");
		for(int row = 0; row < seats.length; row++) {
			if(row == 0) {
				//System.out.println(" __ __ __ __                      __ __ __ __ __ "); --will be modified
				System.out.println("");
				seatLetter = "e";
				seatsRowLength = 10;
			}else if(row == 1) {
				//System.out.println("\n __ __ __ __    __ __ __ __ __    ___ ___ ___ ___ "); --will be modified
				System.out.println("");
				seatLetter = "d";
				seatsRowLength = 14;
			}else if(row == 2) {
				//System.out.println("\n __ __ __ __    __ __ __ __ __    ___ ___ ___ ___ "); --will be modified
				System.out.println("");
				seatLetter = "c";
				seatsRowLength = 14;
			}else if(row ==3) {
				//System.out.println("\n    __ __ __    __ __ __ __ __    __ ___ ___ "); --will be modified
				System.out.println("");
				seatLetter = "b";
				seatsRowLength = 12;
			}else if(row == 4) {
				//System.out.println("\n    __ __ __    __ __ __ __ __    __ ___ ___ "); --will be modified
				System.out.println("");
				seatLetter = "a";
				seatsRowLength = 12;
			}
			for(int column = 0; column < seatsRowLength; column++) {
				seats[row][column] = seatLetter + column;
				if (seatSelected.length > 0) {
					for (String selectedSeat: seatSelected) {
						if(seats[row][column].equals(selectedSeat)) {
							seats[row][column] = "X";
						}
					}
				}
				
				// ROW E
				if (row == 0) {
					if(column == 5) {
						System.out.printf("|                    |%s", seats[row][column]);
					}else {
						if(column > 0) {
							System.out.printf("|%s", seats[row][column]);
							if(column == 9) {
								System.out.printf("|");
							}
						}
					}
				}
				// ROWS C & D
				else if (row >= 1 && row <= 2) {
					if(column == 5 || column == 10) {
						System.out.printf("|  |%s", seats[row][column]);
					}else {
						if(column > 0) {
							System.out.printf("|%s", seats[row][column]);
							if(column == 13) {
								System.out.printf("|");
							}
						}
					}
				}

				// ROWS A & B
				else if (row >= 3 && row <= 4) {
					if(column == 4 || column == 9) {
						System.out.printf("  |%s|", seats[row][column]);
					}else {
						if(column == 0) {
							System.out.printf("   |");
						}
						else {
							System.out.printf("%s|", seats[row][column]);
						}
					}
				}
				
			}
			
		}
		System.out.printf("\n  ____________________________________________");
		System.out.printf("\n |                  Screen                    |");
		System.out.println();

	}
	
  public static int chooseSeat(int movieId) {
		if(movieId == 0)
		{
			System.out.println("Select a Movie first!!");
			return 0;
		}
		Scanner keyboard = new Scanner(System.in);
		System.out.println("How many seats do you want to select?: ");
		int num = keyboard.nextInt();
		String seats[] = new String[num];
		String[] validSeats = {"a1","a2","a3","a4","a5","a6","a7","a8","a9","a10","a11","b1","b2","b3","b4","b5","b6","b7","b8","b9","b10","b11","c1","c2","c3","c4","c5","c6","c7","c8","c9","c10","c11","c12","c13","d1","d2","d3","d4","d5","d6","d7","d8","d9","d10","d11","d12","d13","e1","e2","e3","e4","e5","e6","e7","e8","e9"};
		for(int i = 0; i < seats.length; i++)
		{
			System.out.println("Enter Your Seat Number for seat-" + i+1 + ": ");
			seats[i] = keyboard.next();
			while(!Arrays.asList(validSeats).contains(seats[i]))
			{
				System.out.println("Please Enter Valid Seat Number: ");
				seats[i] = keyboard.next();
			}
		}
		System.out.println("Seleected seats are as follows : ");
		for (String seat : seats) {
			System.out.print(seat);
		}
		System.out.println();
		keyboard.close();
		return num;
	}
	
	
	public static void payment(int movieId, int numOfSeats) throws IOException {
		if(movieId == 0 || numOfSeats == 0)
		{
			System.out.println("Select a Movie and choose seats first!!");
			return;
		}
		Scanner keyboard = new Scanner(System.in);
		double price =10; // will update when we use database
		double subtotal = price*numOfSeats;
		double tax = subtotal * 0.05;
		double total = subtotal + tax;
		System.out.println("You have to Pay "+ total);
		System.out.println("Enter how much you paid : ");
		double paid = keyboard.nextDouble();
		double difference = total - paid;
		if(difference > 0 )
		{
			System.out.println("Please pay the full amount to confirm seats!!");
		}
		else if(difference < 0)
		{
			System.out.println("You paid "+ Math.abs(difference) + "extra.\n Don't forget to take your changes");
		}
		else
		{
			System.out.println("Your Seats are confirmed!!");
		}
		keyboard.close();
	}
	
}

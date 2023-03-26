/**
 * 
 */
package guest;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import common.DataValidation;
import common.DatabaseConnection;
import common.Movie;
import common.MovieSeatReservation;
import common.Showtime;
import common.Ticket;
import common.User;


/**
 * @author 
 *
 */
public class Guest {

	/**
	 * @param args
	 */
	static Connection connection;
	// Text color in console 
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
	public static ArrayList<Showtime> showtimes;
	
	public static void main(String[] args) throws Exception{
		connection = DatabaseConnection.getInstance().getConnection();
		showtimes = Showtime.getAvailableShowtimes(connection);
		ArrayList<Movie> movies = new ArrayList<>();
		Scanner keyboard = new Scanner(System.in);
        int number = 0;
        int movieId = 0;
        int numOfSeats = 0;
       
        while (number != 3) {
        	try {
        		menu();		
	        	number = DataValidation.readPositiveInt("Please enter 1/3: ");
	        	if (number == 1) {
	        		viewNowShowing(showtimes);
	        	} else if (number == 2) {
	        		chooseAMovie();
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
		System.out.println("\nWelcome Guest!");
		System.out.println("--------------------------------------------");
		System.out.println("1 - View now showing");
		System.out.println("2 - Choose a movie");
		System.out.println("3 - Exit");
	}
	
	public static void viewNowShowing(ArrayList<Showtime> showtimes) throws SQLException {
		
		if (showtimes.isEmpty()) {
			System.out.println("\nThere are no now showing movies. You can come back as soon as we have one.");
		} else {
			System.out.println("\n           NOW SHOWING            ");
			System.out.println(    "             *******            ");
			
			for (Showtime showtime : showtimes) {
				System.out.println("Showtime Id\t" + ANSI_BLUE + showtime.getId() + ANSI_RESET);
				System.out.println("Showtime:\t" + showtime.getShowTimeFormatted());
				//System.out.println("Movie Id:\t" + showtime.getMovie().getId());
				System.out.println("Movie Name:\t" + showtime.getMovie().getMovieName());
				//System.out.println("Release Date:\t" + showtime.getMovie().getReleaseDate());
				System.out.println("Hall:\t\t" + showtime.getHall().getName());
				System.out.println("Avalable Seats: " + ANSI_GREEN + showtime.getAvailableSeats() +ANSI_RESET);
				// Show more here
				System.out.println();
			}

		}
		/*To add print where user is asked if they want to proceed with choosing movie or exit*/
	}
	public static void chooseAMovie() throws SQLException{
		Scanner keyboard = new Scanner(System.in);
	    final int CINEMA_CAPACITY = 57;
	    viewNowShowing(showtimes);
	    boolean flag = true;
	    while(flag) {
		    int showtimeID = DataValidation.readPositiveInt("\nPlease enter ID of the showtime you want to watch: ");
		    Showtime showtime = showtimeCheckExists(showtimes, showtimeID);
		    if(showtime != null) {
		    	System.out.println(showtime.getMovie());
				viewSeat(showtime.getMovie().getId());
				String[] numOfSeats = chooseSeat(showtime.getMovie().getId());
				review(showtime, numOfSeats);
		    	flag = false;
		    } else {
		    	System.out.println("Showtime ID Provided do not exist. Please try again.");
		    }
	    }
	    int a =1;
	}
	public static String[] reservedSeats(int movieId) throws SQLException {
		ArrayList<MovieSeatReservation> movieSeatReservation = new ArrayList<MovieSeatReservation>();
		movieSeatReservation = MovieSeatReservation.selectedSeats(connection, movieId);
		String[] reservedSeats = new String[movieSeatReservation.size()];
		if(!movieSeatReservation.isEmpty()) {
			for(int i = 0; i< movieSeatReservation.size(); i++) {
				reservedSeats[i] = movieSeatReservation.get(i).getSeat_number();
			}
		}
		
		return reservedSeats;
	}
	public static void viewSeat(int movieId) throws SQLException {
		String[][] seats = new String [5][14];
		String seatLetter = null;
		int seatsRowLength = 0;
		System.out.println("--------------");
		for(int row = 0; row < seats.length; row++) {
			if(row == 0) {
				System.out.println("");
				seatLetter = "e";
				seatsRowLength = 10;
			}else if(row == 1) {
				System.out.println("");
				seatLetter = "d";
				seatsRowLength = 14;
			}else if(row == 2) {
				System.out.println("");
				seatLetter = "c";
				seatsRowLength = 14;
			}else if(row ==3) {
				System.out.println("");
				seatLetter = "b";
				seatsRowLength = 12;
			}else if(row == 4) {
				System.out.println("");
				seatLetter = "a";
				seatsRowLength = 12;
			}
			for(int column = 0; column < seatsRowLength; column++) {
				seats[row][column] = seatLetter + column;
				if (reservedSeats(movieId).length > 0) {
					for (String selectedSeat: reservedSeats(movieId)) {
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
	
	public static String[] chooseSeat(int movieId) throws SQLException {
		if(movieId == 0)
		{
			System.out.println("Select a Movie first!!");
			return null;
		}
		Scanner keyboard = new Scanner(System.in);
		System.out.println("\nHow many seats do you want to select?: ");
		int num = keyboard.nextInt();
		String seats[] = new String[num];
		String[] validSeats = {"a1","a2","a3","a4","a5","a6","a7","a8","a9","a10","a11","b1","b2","b3","b4","b5","b6","b7","b8","b9","b10","b11","c1","c2","c3","c4","c5","c6","c7","c8","c9","c10","c11","c12","c13","d1","d2","d3","d4","d5","d6","d7","d8","d9","d10","d11","d12","d13","e1","e2","e3","e4","e5","e6","e7","e8","e9"};
		
		
		for(int i = 0; i < seats.length; i++)
		{
			System.out.println("Enter Your Seat Number for seat-" + (i+1) + ": ");
			seats[i] = keyboard.next();
			while(Arrays.asList(reservedSeats(movieId)).contains(seats[i]) || !Arrays.asList(validSeats).contains(seats[i]))
			{
				System.out.println("Please Enter Valid Seat Number: ");
				seats[i] = keyboard.next();
			}
		}
		System.out.println("Selected seats are as follows : ");
		for (String seat : seats) {
			System.out.println(seat);
		}
		System.out.println();
		return seats;
	}
	
	public static void review(Showtime showtime, String[] seats) {
		int number = 0;
		double subtotal = showtime.getPrice() *seats.length ;
		double tax = subtotal * 0.05;
		double total = subtotal + tax;
		System.out.println("Order Details");
		System.out.println(showtime.getMovie());
		System.out.println("Selected seats are as follows : ");
		for (String seat : seats) {
			System.out.println(seat);
		}
		System.out.println("Total Price: $" +total);
		
		while (number != 3) {
        	try {
        		System.out.println("1. Proceed to Checkout");	
        		System.out.println("2. Update Order Details");
        		System.out.println("3. Cancel");
	        	number = DataValidation.readPositiveInt("Please enter 1-3: ");
	        	if (number == 1) {
	        		payment(showtime, seats);
	        		break;
	        	} else if (number == 2) {
	        		chooseAMovie();
	        		break;
	        	} else if (number == 3) {
	        		System.out.println("Thank you for using our program!");
	        	} else {
	        		throw new Exception();
	        	}
	        } catch (Exception e) {
	        	System.out.println(e.toString());
	        }
        };
		
	}
	public static void payment(Showtime showtime, String[] seats)throws SQLException {
		if(showtime.getId() == 0 || seats.length == 0)
		{
			System.out.println("Select a Movie and choose seats first!!");
			return;
		}
		Scanner keyboard = new Scanner(System.in);
		/* START Require user information */
		System.out.println("Customer information. Leave it empty to skip for some fields");
		String firstName = DataValidation.inputStringNotEmpty("First Name: ");
		System.out.print("Last Name: ");
		String lastName = keyboard.nextLine();
		System.out.print("Phone No: ");
		String phoneNo = keyboard.nextLine();
		System.out.print("Email: ");
		String email = keyboard.nextLine();
		User customer = new User(firstName, lastName, "USER", email, phoneNo);
		customer = User.insert(connection, customer); // Resign customer to get the ID generated
		/* END Require user information */
		
		double subtotal = showtime.getPrice() * seats.length;
		double tax = subtotal * 0.05;
		double total = subtotal + tax;
		System.out.println("You have to Pay $"+ total);
		System.out.println("Enter how much you paid : ");
		double paid = keyboard.nextDouble();
		double difference = total - paid;
		if(difference > 0 )
		{
			System.out.println("Please pay the full amount to confirm seats!!");
		}
		else {
			
			if(difference < 0)
			{
				System.out.println("You paid "+ Math.abs(difference) + "extra.\nDon't forget to take your changes");
			}
			else
			{
				System.out.println("Your Seats are confirmed!!");
			}
			for(String seat: seats) {
				MovieSeatReservation movieSeatReservation = new MovieSeatReservation(showtime.getMovie().getId(), seat, true);
				MovieSeatReservation.insert(connection, movieSeatReservation);
				
				/* START Adding Ticket stuff*/
				Ticket ticket = new Ticket(customer, showtime, seat);
				Ticket.insert(connection, ticket);
				/* END Adding Ticket stuff*/
			}
			reloadShowTime(); // load new data from db to ArrayList
		}
		
	}
	public static Showtime showtimeCheckExists(ArrayList<Showtime> showtimes, int id) {
		for (Showtime showtime : showtimes) {
			if(showtime.getId() == id) {
				return showtime;
			}
		}
		return null;
	}
	public static void reloadShowTime() throws SQLException {
		showtimes = Showtime.getAvailableShowtimes(connection);
	}
}

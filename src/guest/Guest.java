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


/**
 * @author 
 *
 */
public class Guest {

	/**
	 * @param args
	 */
	static Connection connection;
	
	public static void main(String[] args) throws Exception{
		connection = DatabaseConnection.getInstance().getConnection();
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
	        		viewNowShowing(movies);
	        	} else if (number == 2) {
	        		chooseAMovie(movies);
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
	
	public static void viewNowShowing(ArrayList<Movie> movies) throws SQLException {
		movies = Movie.listAll(connection);
		if (movies.isEmpty()) {
			System.out.println("\nThere are no now showing movies. You can come back as soon as we have one.");
		} else {
			System.out.println("\n           NOW SHOWING            ");
			System.out.print(    "             *******            ");
			for (int i=0; i<movies.size(); i++) {
				System.out.println("\n" + movies.get(i));
			}
		}
		/*To add print where user is asked if they want to proceed with choosing movie or exit*/
	}
	public static void chooseAMovie(ArrayList<Movie> movies) throws SQLException{
		ArrayList<Movie> movieDetails = new ArrayList<Movie>();
		Scanner keyboard = new Scanner(System.in);
	    final int CINEMA_CAPACITY = 57;
	    viewNowShowing(movies);
		while(true){
			System.out.print("\nPlease enter ID of the movie you want to watch: ");
			String movieIdString = keyboard.next();
			int movieId = Integer.parseInt(movieIdString);
			movieDetails = Movie.listMovieDetails(connection, movieId);
			if(movieDetails.isEmpty()) {
				System.out.println("Movie ID Provided do not exist. Please try again.");
			}else {
				if(reservedSeats(movieId).length == CINEMA_CAPACITY) {
					System.out.println("Sorry for the inconvinience. Movie is already fully booked.");
				}else {
					System.out.println(movieDetails.get(0));
					viewSeat(movieId);
					String[] numOfSeats = chooseSeat(movieId);
					review(movies, movieDetails.get(0), numOfSeats);
					break;
				}
				
			}
			
			for (int i=0; i<movies.size(); i++) {
				System.out.println("\n" + movies.get(i));
			}
	    }
	
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
	
	public static void review(ArrayList<Movie> movies, Movie movie, String[] seats) {
		int number = 0;
		double subtotal = movie.getPrice() *seats.length ;
		double tax = subtotal * 0.05;
		double total = subtotal + tax;
		System.out.println("Order Details");
		System.out.println(movie);
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
	        		payment(movie, seats);
	        		break;
	        	} else if (number == 2) {
	        		chooseAMovie(movies);
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
	public static void payment(Movie movie, String[] seats)throws SQLException {
		if(movie.getId() == 0 || seats.length == 0)
		{
			System.out.println("Select a Movie and choose seats first!!");
			return;
		}
		Scanner keyboard = new Scanner(System.in);
		double subtotal = movie.getPrice() * seats.length;
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
				MovieSeatReservation movieSeatReservation = new MovieSeatReservation(movie.getId(), seat, true);
				MovieSeatReservation.insert(connection, movieSeatReservation);
			}
			
		}
		
	}
	
}

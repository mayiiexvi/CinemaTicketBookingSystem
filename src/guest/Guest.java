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
				System.out.println("Price:\t\t" + ANSI_BLUE+ "$" + String.format("%.2f", showtime.getPrice()) + ANSI_RESET );
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
				//viewSeat(showtime.getMovie().getId());
		    	ArrayList<String> validSeats = viewSeat(showtime, new String[0]);
				String[] numOfSeats = chooseSeat(showtime, validSeats, showtime.getMovie().getId());
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
	public static boolean isBookedSeat(ArrayList<Ticket> seatsBooked, String seatCode) {
		for (Ticket ticket : seatsBooked) {
			if(ticket.getSeatCode().equals(seatCode)) {
				return true;
			}
		}
		return false;
	}
	public static boolean isHidenSeat(String[] hidenSeats, String seatCode) {
		for (String seat : hidenSeats) {
			if(seat.equals(seatCode)) {
				return true;
			}
		}
		return false;
	}
	public static String fixedLengthString(String string, int length) {
	    return String.format("%-" + length + "s", string);
	}
	public static ArrayList<String> viewSeat(Showtime showtime, String[] selectedSeats) throws SQLException {
		ArrayList<Ticket> seatsBooked = Ticket.getTicketsByShowTimeID(connection, showtime.getId());
		int numRows = showtime.getHall().getSeatingRows();
		int numCols = showtime.getHall().getSeatingCols();
		String[] hidenSeats = showtime.getHall().getHidenSeats().split(",");
		System.out.print("     ");
//        for (int i = 1; i <= numCols; i++) {
//            System.out.print(i + "    ");
//        }
        System.out.println();
        ArrayList<String> validSeats = new ArrayList<>(); 
        for (int i = 0; i < numRows; i++) {
        	System.out.print("   ");
            for (int j = 0; j < numCols; j++) {
            	String seatCode = (char) ('A' + i)+String.valueOf(j+1);
                if (isBookedSeat(seatsBooked,seatCode )) {
                    System.out.print(ANSI_RED + fixedLengthString("X", 5) + ANSI_RESET);
                } else {
                	if(isHidenSeat(hidenSeats, seatCode)) {
                		System.out.print(fixedLengthString(" ", 5));
                	} else {
                		if(Arrays.asList(selectedSeats).contains(seatCode)) {
                			System.out.print(ANSI_GREEN + fixedLengthString(seatCode, 5) + ANSI_RESET);
                		} else {
                		System.out.print(fixedLengthString(seatCode, 5));
                		}
                		validSeats.add(seatCode);
                	}
                }
            }
            System.out.println();
        }
        //int tmp = (numCols*5 + 3 -6)/2;
        //System.out.println(Character.toString('_').repeat(numCols*5 + 3));
		//System.out.println("|" + Character.toString(' ').repeat(tmp) + "Screen" + Character.toString(' ').repeat(tmp) + "|");
        String screen1 = "____________________________________________";
        String screen2 = "|                  Screen                    |";
        int tmp = ((numCols*5 + 3) - screen1.length()) /2 ;
        System.out.println(Character.toString(' ').repeat(tmp+1) + screen1 + Character.toString(' ').repeat(tmp));
        System.out.println(Character.toString(' ').repeat(tmp) + screen2 + Character.toString(' ').repeat(tmp));
		System.out.println();
		return validSeats;
	}
	
	public static String[] chooseSeat(Showtime showtime, ArrayList<String> validSeats, int movieId) throws SQLException {
		if(movieId == 0)
		{
			System.out.println("Select a Movie first!!");
			return null;
		}
		Scanner keyboard = new Scanner(System.in);
		int num = DataValidation.readPositiveInt("How many seats do you want to select?: ");
		String seats[] = new String[num];
		//String[] validSeats = {"a1","a2","a3","a4","a5","a6","a7","a8","a9","a10","a11","b1","b2","b3","b4","b5","b6","b7","b8","b9","b10","b11","c1","c2","c3","c4","c5","c6","c7","c8","c9","c10","c11","c12","c13","d1","d2","d3","d4","d5","d6","d7","d8","d9","d10","d11","d12","d13","e1","e2","e3","e4","e5","e6","e7","e8","e9"};
		
		
		for(int i = 0; i < seats.length; i++)
		{
			System.out.println("Enter Your Seat Number for seat-" + (i+1) + ": ");
			seats[i] = keyboard.next().toUpperCase();
			while(!validSeats.contains(seats[i]))
			{
				System.out.println("Please Enter Valid Seat Number: ");
				seats[i] = keyboard.next().toUpperCase();
			}
		}
		viewSeat(showtime, seats);
		String tmp = "";
		for (String seat : seats) {
			tmp += seat + ", ";
		}
		tmp = tmp.substring(0, tmp.length()-2);
		System.out.println("Selected seats are as follows: " + tmp);
		return seats;
	}
	
	public static void review(Showtime showtime, String[] seats) {
		int number = 0;
		double subtotal = showtime.getPrice() *seats.length ;
		double tax = subtotal * 0.05;
		double total = subtotal + tax;
		System.out.println("Order Details");
		System.out.println(showtime.getMovie());
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
		String firstName = DataValidation.inputStringNotEmpty("First Name(*): ");
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
			
			ArrayList<Ticket> tickets = new ArrayList<>();
			for(String seat: seats) {
				MovieSeatReservation movieSeatReservation = new MovieSeatReservation(showtime.getMovie().getId(), seat, true);
				MovieSeatReservation.insert(connection, movieSeatReservation);
				/* START Adding Ticket stuff*/
				Ticket ticket = new Ticket(customer, showtime, seat);
				Ticket.insert(connection, ticket);
				tickets.add(ticket);
				/* END Adding Ticket stuff*/
			}
			printTicketSummary(tickets, total, paid, difference);
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
	
	public static void printTicketSummary(ArrayList<Ticket> tickets, double total, double received, double changes) {
	    System.out.println("========================================");
	    System.out.println("             Ticket Summary             ");
	    System.out.println("========================================");
	    System.out.printf("%-20s%20s\n", "Movie:", tickets.get(0).getShowtime().getMovie().getMovieName());
	    System.out.printf("%-20s%20s\n", "Hall:", tickets.get(0).getShowtime().getHall().getName());
	    System.out.printf("%-20s%20s\n", "Showtime:", tickets.get(0).getShowtime().getShowTimeFormatted());
	    String seats = "";
	    for (Ticket ticket : tickets) {
	    	seats += ticket.getSeatCode() +", ";
		}
	    seats = seats.substring(0, seats.length()-2);
	    System.out.printf("%-20s%20s\n", "Seat Number:", seats);
	    System.out.printf("%-20s%20s\n", "Ticket Price:", "$"+String.format("%.2f",tickets.get(0).getShowtime().getPrice()));
	    System.out.printf("%-20s%20s\n", "Total Price:", "$"+String.format("%.2f",total));
	    System.out.printf("%-20s%20s\n", "Money Received:", "$"+String.format("%.2f",received));
	    System.out.printf("%-20s%20s\n", "Change:", "$"+String.format("%.2f",Math.abs(changes)));
	    System.out.println("========================================");
	    System.out.println("Thank you! Enjoy your movie! ");
	}

}

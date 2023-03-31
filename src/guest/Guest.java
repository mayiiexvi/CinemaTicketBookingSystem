/**
 * 
 */
package guest;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import cinemaTicketBookingSystem.CinemaTicketBookingSystem;
import common.Constant;
import common.DataValidation;
import common.DatabaseConnection;
import common.Movie;
import common.Showtime;
import common.Ticket;
import common.User;


/**
 * DatabaseConnection class
 * @author Sylvia Espina C0866311
 * @author Mufida Andi C0864756
 * @author Jenil Shivamkumar Varma C0870543
 * @author Tich Vu Lu C0861736
 * @author Jay Shah C0868053
 */
public class Guest {

	/**
	 * @param args
	 */
	static Connection connection;
	public static ArrayList<Showtime> showtimes;
	
	/**
	 * main method
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception{
		connection = DatabaseConnection.getInstance().getConnection();
		showtimes = Showtime.getAvailableShowtimes(connection);
		Scanner keyboard = new Scanner(System.in);
        int number = 0;
       
        while (number != 3) {
        	try {
        		menu();		
	        	number = DataValidation.readPositiveInt("Please enter 1/3: ");
	        	if (number == 1) {
	        		viewNowShowing(showtimes);
	        	} else if (number == 2) {
	        		chooseAMovie();
	        	} else if (number == 3) {
	        		CinemaTicketBookingSystem.main(null);
	        		keyboard.close();
	        	} else {
	        		throw new Exception();
	        	}
	        }
        	
        	catch (Exception e) {
	        	System.out.println(e.toString());
	        }
        };
	}
	
	/**
	 * Main menu
	 */
	public static void menu() {
		System.out.println("\nWelcome Guest!");
		System.out.println("--------------------------------------------");
		System.out.println("1 - View now showing");
		System.out.println("2 - Choose a movie");
		System.out.println("3 - Exit");
	}
	
	/**
	 * List of movie now showing
	 * @param showtimes
	 * @throws SQLException
	 */
	public static void viewNowShowing(ArrayList<Showtime> showtimes) throws SQLException {
		
		if (showtimes.isEmpty()) {
			System.out.println("\nThere are no now showing movies. You can come back as soon as we have one.");
		} else {
			System.out.println("\n           NOW SHOWING            ");
			System.out.println(    "             *******            ");
			
			for (Showtime showtime : showtimes) {
				System.out.println("Showtime Id\t" + Constant.ANSI_BLUE + showtime.getId() + Constant.ANSI_RESET);
				System.out.println("Showtime:\t" + showtime.getShowTimeFormatted());
				System.out.println("Movie Name:\t" + showtime.getMovie().getMovieName());
				System.out.println("Hall:\t\t" + showtime.getHall().getName());
				System.out.println("Price:\t\t" + Constant.ANSI_BLUE+ "$" + String.format("%.2f", showtime.getPrice()) + Constant.ANSI_RESET );
				System.out.println("Avalable Seats: " + Constant.ANSI_GREEN + showtime.getAvailableSeats() + Constant.ANSI_RESET);
				// Show more here
				System.out.println();
			}

		}
	}
	
	/**
	 * Choose a movie
	 * @throws SQLException
	 */
	public static void chooseAMovie() throws SQLException{
	    viewNowShowing(showtimes);
	    boolean flag = true;
		if (!showtimes.isEmpty()) {
		    while(flag) {
			    int showtimeID = DataValidation.readPositiveInt("\nPlease enter ID of the showtime you want to watch: ");
			    Showtime showtime = Showtime.showtimeCheckExists(showtimes, showtimeID);
			    if(showtime != null) {
			    	System.out.println(showtime.getMovie());
			    	
			    	ArrayList<String> validSeats = Showtime.viewSeat(connection, showtime, new String[0]);
					String[] numOfSeats = chooseSeat(showtime, validSeats, showtime.getMovie().getId());
					review(showtime, numOfSeats);
			    	flag = false;
			    } else {
			    	System.out.println("Showtime ID Provided do not exist. Please try again.");
			    }
		    }
		}
	}
	

	/**
	 * Choose seats
	 * @param showtime
	 * @param validSeats
	 * @param movieId
	 * @return
	 * @throws SQLException
	 */
	public static String[] chooseSeat(Showtime showtime, ArrayList<String> validSeats, int movieId) throws SQLException {

		if(movieId == 0)
		{
			System.out.println("Select a Movie first!!");
			return null;
		}
		Scanner keyboard = new Scanner(System.in);
		// Check if number of seats input > available seats. 
		int num = DataValidation.readPositiveInt("How many seats do you want to select?: ", validSeats.size(), "You provided a number of seats that is greater than our available seats. Please try again!");
		String seats[] = new String[num];
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
		Showtime.viewSeat(connection, showtime, seats);
		String tmp = "";
		for (String seat : seats) {
			tmp += seat + ", ";
		}
		tmp = tmp.substring(0, tmp.length()-2);
		System.out.println("Selected seats are as follows: " + tmp);
		return seats;
		
		
	}
	
	/**
	 * Review order
	 * @param showtime
	 * @param seats
	 */
	public static void review(Showtime showtime, String[] seats) {
		int number = 0;
		double subtotal = showtime.getPrice() *seats.length ;
		double tax = subtotal * 0.05;
		double total = subtotal + tax;
		System.out.println("Order Details");
		System.out.println(showtime.getMovie());
		System.out.printf("Total Price: $%,.2f\n" ,total);
		
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
	        		CinemaTicketBookingSystem.main(null);
	        	} else {
	        		throw new Exception();
	        	}
	        } catch (Exception e) {
	        	System.out.println(e.toString());
	        }
        };
		
	}
	
	/**
	 * Do a payment
	 * @param showtime
	 * @param seats
	 * @throws SQLException
	 */
	public static void payment(Showtime showtime, String[] seats)throws SQLException {
		boolean isPaid = false;
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
		System.out.printf("\nYou have to Pay $%,.2f", total);
		while(!isPaid) {
			double paid = DataValidation.readPositiveDouble("\nEnter how much you paid : ");
			double difference = total - paid;
			if(difference > 0 )
			{
				System.out.println("Please pay the full amount to confirm seats!!");
			}
			else {
				
				if(difference < 0)
				{
					System.out.printf("You paid $%.2f extra.\nDon't forget to take your changes\n",  Math.abs(difference) );
				}
				else
				{
					System.out.println("Your Seats are confirmed!!");
				}
				
				ArrayList<Ticket> tickets = new ArrayList<>();
				for(String seat: seats) {
					/* START Adding Ticket stuff*/
					Ticket ticket = new Ticket(customer, showtime, seat);
					Ticket.insert(connection, ticket);
					tickets.add(ticket);
					/* END Adding Ticket stuff*/
				}
				printTicketSummary(tickets, total, paid, difference);
				reloadShowTime(); // load new data from db to ArrayList
				isPaid = true;
			}
		}
	}
	
	/**
	 * reload a list of showtime in the database
	 * @throws SQLException
	 */
	public static void reloadShowTime() throws SQLException {
		showtimes = Showtime.getAvailableShowtimes(connection);
	}
	
	/**
	 * Print tickets summary
	 * @param tickets
	 * @param total
	 * @param received
	 * @param changes
	 */
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

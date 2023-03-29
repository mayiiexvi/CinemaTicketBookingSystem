/**
 * 
 */
package admin;

import java.util.Scanner;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import common.DataValidation;
import common.DatabaseConnection;
import common.Hall;
import common.Movie;
import common.Showtime;
import common.User;
/**
 * @author Sylvia Espina C0866311
 * @author Mufida Andi C0864756
 * @author Jenil Shivamkumar Varma - c0870543
 * @author Tich Vu Lu C0861736
 * @author Jay Shah C0868053
 */
public class Admin {

	/**
	 * @param args
	 */

	static Connection connection;
	static User userLogin;
	static Scanner keyboard = new Scanner(System.in);
	static final String DATE_FORMAT = "MM/dd/yyyy";
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
	public static String mainMenu() {
		System.out.println("\n         CINEMA MANAGEMENT MENU         ");
		System.out.println("----------------------------------------------");
        System.out.println("Please select an option:");
		System.out.println("1. Movies Management");
		System.out.println("2. Showtimes Management");
		System.out.println("3. Exit");
		System.out.print("Your choice: ");
		return keyboard.nextLine();
	}
	public static boolean doLogin() throws SQLException {
		String username;
		String password;
		do {
			System.out.println("Login Details");
			System.out.println("------------------");
			System.out.print("Enter Username: ");
	        username = keyboard.nextLine();
	        System.out.print("Enter Password: ");
	        password = keyboard.nextLine();
	        userLogin = User.isValidCredentials(connection, username, password);
	        if(userLogin.getId() != 0) {
	        	return true;
	        }
	        else {
	        	System.out.println("Invalid Username/Password. Please try again\n");
	        }
		} while(userLogin.getId() == 0);
		return false;
	}
	
	public static void main(String[] args){
		try {
			connection = DatabaseConnection.getInstance().getConnection();
			if(doLogin()) {
				System.out.println("\nWelcome, " + userLogin.getFirstName() +"!");
				String choice = "";
	        	do {
	        		choice = mainMenu();
	        		if(choice.equals("1")) { // movies main process
	        			moviesProcess();
	        		} else if(choice.equals("2")) { // showtimes main process
	        			showtimesProcess();
	        		} else if(choice.equals("3")) { // main program exit
	        			System.out.println("\nThank you for using our program!");
	        			System.out.println("           *******            ");
	        			System.exit(0);
	        		} else {
	        			System.out.println("Bad input! Please try again!");
	        		}
	        	} while(!choice.equals("3"));
			}
			
			/* Ending main program. Cleaning stuff here*/
			keyboard.close();
			connection.close();
		}
		catch (Exception e) {
			System.err.println(e.toString());
		}
		
	}
	public static String showtimesMenu() {
		System.out.println("\n         SHOWTIMES MENU         ");
		System.out.println("----------------------------------------------");
		System.out.println("1 - View all showtimes");
		System.out.println("2 - Add showtime");
		System.out.println("3 - Update showtime");
		System.out.println("4 - Delete showtime");
		System.out.println("5 - Exit");
		System.out.print("Your choice: ");
		return keyboard.nextLine();
	}
	public static void showtimesProcess() throws SQLException {
		String choice = "";
    	do {
    		choice = showtimesMenu();
    		if(choice.equals("1")) {
    			viewAllShowtimes();
    		} else if(choice.equals("2")) {
    			addShowtime();
    		} else if(choice.equals("3")) {
    			updateShowtime();
    		} else if(choice.equals("4")) {
    			deleteShowtime();
    		} else if(choice.equals("5")) {
    			// go back
    		} else {
    			System.out.println("Bad input! Please try again!");
    		}
    	} while(!choice.equals("5"));
	}
	private static void deleteShowtime() throws SQLException{
		// TODO Auto-generated method stub
		ArrayList<Showtime> showtimes = viewAllShowtimes();
		System.out.println("\n          Delete A SHOWTIME            ");
		System.out.println(  "            *******            ");
		int showtimeID = DataValidation.readPositiveInt("Please choose a showtime: ");
		Showtime showtime = Showtime.showtimeCheckExists(showtimes, showtimeID);		
		if(showtime != null) {			
			while(true) {
				try {
					System.out.println("Are you sure you want to delete the showtime " + showtimeID +" ? y/n");
					String userInput = keyboard.nextLine();
					if(userInput.toUpperCase().equals("Y")) {
						Showtime.delete(connection, showtimeID);
						break;
					}else if (userInput.toUpperCase().equals("N")) {
						System.out.println("Show time is not deleted.");
						break;
					}else {
						System.out.println("Please enter y for Yes or n for No");
					}
				}catch(Exception e){
					System.out.println("Invalid input format!");
				}
			}
						
		}else {
			System.out.println("Show time is not exist");
		}
		
	}
	private static void updateShowtime() throws SQLException{
		ArrayList<Showtime> showtimes = viewAllShowtimes();
		System.out.println("\n          UPDATE A SHOWTIME            ");
		System.out.println(  "            *******            ");
		ArrayList<Movie> movies = Movie.listAll(connection);
		ArrayList<Hall> halls = Hall.listAll(connection);
		int showtimeID = DataValidation.readPositiveInt("Please choose a showtime: ");
		Showtime showtime = Showtime.showtimeCheckExists(showtimes, showtimeID);
		if(showtime != null){
			/*Update the movie*/
			while(true) {
				try {
        			for (Movie movie : movies) {
        				System.out.println(ANSI_GREEN + movie.getId() + ". " + movie.getMovieName() + ANSI_RESET);
        			}
        			System.out.println("Current show [" + ANSI_BLUE + showtime.getMovie().getMovieName() + ANSI_RESET +"] ");
            		System.out.print("Please choose a new movie or leave it empty to skip: ");
        			String input = keyboard.nextLine();
        			if (!input.isEmpty()) {
        				int movieID = Integer.parseInt(input);
    	                if(Movie.checkMovieExist(movies, movieID)) {
    	        			showtime.setMovie(new Movie(movieID));
    	        			break;
    	        		} else {
    	        			System.out.println("The movie ID provided does not exist.");
    	        		}
        	        } else {
        	            System.out.println("Skipping update movie");
        	            break;
        	        }
	            } catch (Exception e) {
	            	System.out.println("Invalid input format!");
	            }
			}
			/*Update the hall*/
			while(true) {
				try {
        			for (Hall hall : halls) {
        				System.out.println(ANSI_GREEN + hall.getId() + ". " + hall.getName() + ANSI_RESET);
        			}
        			System.out.println("Current show at [" + ANSI_BLUE + showtime.getHall().getName() + ANSI_RESET +"] ");
            		System.out.print("Please choose a new hall or leave it empty to skip: ");
        			String input = keyboard.nextLine();
        			if (!input.isEmpty()) {
        				int hallID = Integer.parseInt(input);
    	                if(Hall.checkHallExist(halls, hallID)) {
    	        			showtime.setHall(new Hall(hallID));
    	        			break;
    	        		} else {
    	        			System.out.println("The movie ID provided does not exist.");
    	        		}
        	        } else {
        	            System.out.println("Skipping update hall");
        	            break;
        	        }
	            } catch (Exception e) {
	            	System.out.println("Invalid input format!");
	            }
			}
			/*The rest information */
			SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
			Date date = showtime.getShowtime();
			while(true) {
				try {
					System.out.println("Showtime [" + ANSI_BLUE + formatter.format(showtime.getShowtime()) + ANSI_RESET + "] Enter date (" + DATE_FORMAT+ "): ");
					String input = keyboard.nextLine();
					if (!input.isEmpty()) {
			        	date = formatter.parse(input);
			            break;
        	        } else {
        	            System.out.println("Skipping update date");
        	            break;
        	        }
				} catch (Exception e) {
	            	System.out.println("Invalid input format!");
	            }
			}
			formatter = new SimpleDateFormat("HH:mm");
			Date time = showtime.getShowtime();
			while(true) {
				try {
					System.out.println("Showtime [" + ANSI_BLUE + formatter.format(showtime.getShowtime()) + ANSI_RESET + "] Enter time (HH:mm): ");
					String input = keyboard.nextLine();
					if (!input.isEmpty()) {
			        	time = formatter.parse(input);
			            break;
        	        } else {
        	            System.out.println("Skipping update time");
        	            break;
        	        }
				} catch (Exception e) {
	            	System.out.println("Invalid input format!");
	            }
			}
			Date showDateTime = new Date(date.getYear(), date.getMonth(), date.getDate(), time.getHours(), time.getMinutes(), time.getSeconds());
			showtime.setShowtime(new Timestamp(showDateTime.getTime()));
			/////
			while(true) {
				try {
					System.out.println("Price for a ticket["+ ANSI_BLUE + showtime.getPrice() + ANSI_RESET +"]: ");
					String input = keyboard.nextLine();
					if (!input.isEmpty()) {
			        	double price = Double.parseDouble(input);
			        	showtime.setPrice(price);
			            break;
        	        } else {
        	            System.out.println("Skipping update price");
        	            break;
        	        }
				} catch (Exception e) {
	            	System.out.println("Invalid input format!");
	            }
			}
			// All information has completed update
			Showtime.update(connection, showtime);

			
		} else {
			System.out.println("The showtime ID provided does not exist.");
		}
		
		
		
	}
	private static void addShowtime() throws SQLException{
		viewAllShowtimes();
		System.out.println("\n          ADD A SHOWTIME            ");
		System.out.println(  "            *******            ");
		ArrayList<Movie> movies = Movie.listAll(connection);
		ArrayList<Hall> halls = Hall.listAll(connection);
		System.out.println("Please choose a movie: ");
		for (Movie movie : movies) {
			System.out.println(movie.getId() + ". " + movie.getMovieName());
		}
		int selectedMovie = DataValidation.readPositiveInt("Your choice: ");
		if(Movie.checkMovieExist(movies, selectedMovie)) {
			System.out.println("Please choose a hall: ");
			for (Hall hall : halls) {
				System.out.println(hall.getId() + ". " + hall.getName());
			}
			int selectedHall = DataValidation.readPositiveInt("Your choice: ");
			if(Hall.checkHallExist(halls, selectedHall)) {
				Date showDateTime = inputShowTimeDateTime();
				double price = DataValidation.readPositiveDouble("Price for a ticket: ");
				Movie movie = new Movie(selectedMovie);
				Hall hall = new Hall(selectedHall);
				Showtime showtime = new Showtime(movie, hall, new Timestamp(showDateTime.getTime()), price);
				Showtime.insert(connection, showtime);
			} else {
				System.out.println("The Hall ID provided does not exist.");
			}
		} else {
			System.out.println("The movie ID provided does not exist.");
		}
		
	}
	private static ArrayList<Showtime> viewAllShowtimes() throws SQLException {
		ArrayList<Showtime> showtimes = Showtime.getAvailableShowtimes(connection);
		Showtime.displayShowtimes(showtimes);
		return showtimes;
	}
	public static Movie takeMovieDetails() {
		Movie movie = new Movie();
		try {
			
    		System.out.print("\nEnter movie name: ");
    		String movieName = keyboard.nextLine();
    		
    		System.out.print("Enter synopsis: ");
    		String synopsis = keyboard.nextLine();

    		System.out.print("Enter release date: ");
    		String releaseDateString = keyboard.nextLine();

    		Double price = DataValidation.readPositiveDouble("Enter price: ");

    		movie = new Movie(movieName, synopsis, releaseDateString, price);    		
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return movie;	
	}
	
	public static String moviesMenu() {
		System.out.println("\n         MOVIES MENU         ");
		System.out.println("----------------------------------------------");
		System.out.println("1 - View all movies from list");
		System.out.println("2 - Add movie to list");
		System.out.println("3 - Update movie from list");
		System.out.println("4 - Delete movie from list");
		System.out.println("5 - Exit");
		System.out.print("Your choice: ");
		return keyboard.nextLine(); 
	}
	
	public static void moviesProcess() throws SQLException {
		String choice = "";
    	do {
    		choice = moviesMenu();
    		if(choice.equals("1")) {
    			viewAllMovies();
    		} else if(choice.equals("2")) {
    			addMovie();
    		} else if(choice.equals("3")) {
    			updateMovie();
    		} else if(choice.equals("4")) {
    			deleteMovie();
    		} else if(choice.equals("5")) {
    			// go back
    		} else {
    			System.out.println("Bad input! Please try again!");
    		}
    	} while(!choice.equals("5"));
	}
	
	private static void deleteMovie() throws SQLException {
		ArrayList<Movie> movies = Movie.listAll(connection);
		System.out.println("\n          CURRENT MOVIES            ");
		System.out.println(  "            *******            ");
		for (int i=0; i<movies.size(); i++) {
			System.out.println("Movie:  " + movies.get(i).getId() + "\t" + movies.get(i).getMovieName());
		}
		
		System.out.println("\n          DELETE A MOVIE            ");
		System.out.println(  "              *******            ");
		System.out.print("\nEnter movie id: ");
		String movieIdString = keyboard.next();
		boolean isValidId = false;
		int movieId = Integer.parseInt(movieIdString);
		
		while(!isValidId)
		{
			for (int i=0; i<movies.size(); i++) {
				if(movieId == movies.get(i).getId())
				{
					isValidId = true;
					Movie.delete(connection, movieId);
					break;
				}
					
			}
			if(!isValidId)
			{
				System.out.println("\nMovie doesn't exist!! \nPlease enter valid movie ID!!");
				System.out.print("\nEnter movie id: ");
				movieIdString = keyboard.next();
				movieId = Integer.parseInt(movieIdString);
			}
		}
		
	}
	private static void updateMovie() throws SQLException {
		ArrayList<Movie> movies = Movie.listAll(connection);
		Movie updateMovie = movies.get(0);
		
		System.out.println("\n          CURRENT MOVIES            ");
		System.out.println(  "            *******            ");
		for (int i=0; i<movies.size(); i++) {
			System.out.println("Movie:  " + movies.get(i).getId() + "\t" + movies.get(i).getMovieName());
		}

		System.out.println("\n          UPDATE A MOVIE            ");
		System.out.println(  "             *******            ");
		System.out.print("\nEnter movie id: ");
		String movieIdString = keyboard.next();
		
		boolean isValidId = false;
		int movieId = Integer.parseInt(movieIdString);
		
		while(!isValidId)
		{
			for (int i=0; i<movies.size(); i++) {
				if(movieId == movies.get(i).getId())
				{
					isValidId = true;
					updateMovie = movies.get(i);
					break;
				}
					
			}
			if(!isValidId)
			{
				System.out.println("\nMovie doesn't exist!! \nPlease enter valid movie ID!!");
				System.out.print("\nEnter movie id: ");
				movieIdString = keyboard.next();
				movieId = Integer.parseInt(movieIdString);
			}
		}
		
		Movie movieDetails = takeMovieDetails();
		
		updateMovie.setMovieName(movieDetails.getMovieName());
		updateMovie.setSynopsis(movieDetails.getSynopsis());
		updateMovie.setReleaseDate(movieDetails.getReleaseDate());
		updateMovie.setPrice(movieDetails.getPrice());

		Movie.update(connection, updateMovie);
		
	}
	private static void addMovie() throws SQLException {
		System.out.println("\n          ADD A MOVIE            ");
		System.out.println(  "            *******            ");
		Movie movie = takeMovieDetails();
		Movie.insert(connection, movie);   
		
	}
	private static void viewAllMovies() throws SQLException {
		ArrayList<Movie> movies = Movie.listAll(connection);
		if (movies.isEmpty()) {
			System.out.println("\nNo movies in the list. Choose 2 to add.");
		} else {
			System.out.println("\n           NOW SHOWING            ");
			System.out.print(    "             *******            ");
			for (int i=0; i<movies.size(); i++) {
				System.out.println("\n" + movies.get(i));
			}
		}
		
	}

	@SuppressWarnings("deprecation")
	public static Timestamp inputShowTimeDateTime() {
        
        System.out.print("");
        Date date = DataValidation.readPositiveDate("Enter date ("+DATE_FORMAT+"): ");
        Date time = DataValidation.readPositiveTime("Enter time (HH:mm): ");
        Date dateTime = new Date(date.getYear(), date.getMonth(), date.getDate(), time.getHours(), time.getMinutes(), time.getSeconds());
        Timestamp timestamp = new Timestamp(dateTime.getTime());
        return timestamp;
    }
	
	@SuppressWarnings("deprecation")
	public static Date inputShowTimeDateTime(Date showtime) {
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		
        System.out.print("");
        Date date = DataValidation.readPositiveDate("Enter date ("+DATE_FORMAT+") [" + ANSI_BLUE+ formatter.format(showtime) + ANSI_RESET + "]: ");
        formatter = new SimpleDateFormat("hh:mm");
        Date time = DataValidation.readPositiveTime("Enter time (HH:mm) [" + ANSI_BLUE+ formatter.format(showtime) + ANSI_RESET + "]: ");
        Date dateTime = new Date(date.getYear(), date.getMonth(), date.getDate(), time.getHours(), time.getMinutes(), time.getSeconds());
        return dateTime;
    }
}

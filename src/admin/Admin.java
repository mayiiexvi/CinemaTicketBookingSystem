/**
 * 
 */
package admin;

import java.util.Scanner;

import cinemaTicketBookingSystem.CinemaTicketBookingSystem;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import common.Constant;
import common.DataValidation;
import common.DatabaseConnection;
import common.Hall;
import common.Movie;
import common.Showtime;
import common.Ticket;
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
	

	/**
	 * Main menu of admin management
	 * @return
	 */
	public static String mainMenu() {
		System.out.println("\n         CINEMA MANAGEMENT MENU         ");
		System.out.println("----------------------------------------------");
        System.out.println("Please select an option:");
		System.out.println("1. Movies Management");
		System.out.println("2. Showtimes Management");
		System.out.println("3. Tickets Management");
		System.out.println("4. Exit");
		System.out.print("Your choice: ");
		return keyboard.nextLine();
	}
	
	/**
	 * Login function
	 * @return
	 * @throws SQLException
	 */
	public static boolean doLogin() throws SQLException {
		String username;
		String password;
		do {
			System.out.println("Login Details");
			System.out.println("------------------");
			System.out.print("Enter username: ");
	        username = keyboard.nextLine();
	        System.out.print("Enter password: ");
	        password = keyboard.nextLine();
	        userLogin = User.isValidCredentials(connection, username, password);
	        if(userLogin.getId() != 0) {
	        	return true;
	        }
	        else {
	        	System.out.println("Invalid username/password. Please try again!\n");
	        }
		} while(userLogin.getId() == 0);
		return false;
	}
	
	/**
	 * Main method
	 * @param args
	 */
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
	        		} else if(choice.equals("3")) {
	        			ticketsProcess();
	        		} else if(choice.equals("4")) { // main program exit
	        			CinemaTicketBookingSystem.main(null);
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
	
	/**
	 * SHOWTIME menu
	 * @return
	 */
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
	
	/**
	 * Showtime main process
	 * @throws SQLException
	 */
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
    			CinemaTicketBookingSystem.main(null);
    		} else {
    			System.out.println("Bad input! Please try again!");
    		}
    	} while(!choice.equals("5"));
	}
	
	/**
	 * Delete a showtime
	 * @throws SQLException
	 */
	private static void deleteShowtime() throws SQLException{
		ArrayList<Showtime> showtimes = viewAllShowtimes();
		System.out.println("\n          Delete A SHOWTIME            ");
		System.out.println(  "            *******            ");
		int showtimeID = DataValidation.readPositiveInt("Please choose a showtime: ");
		Showtime showtime = Showtime.showtimeCheckExists(showtimes, showtimeID);		
		if(showtime != null) {
			if(!Showtime.isPossibleToDelete(showtime)) {
				return;
			}
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
	/**
	 * Update a showtime
	 * @throws SQLException
	 */
	private static void updateShowtime() throws SQLException{
		ArrayList<Showtime> showtimes = viewAllShowtimes();
		System.out.println("\n          UPDATE A SHOWTIME            ");
		System.out.println(  "            *******            ");
		ArrayList<Movie> movies = Movie.listAll(connection);
		ArrayList<Hall> halls = Hall.listAll(connection);
		int showtimeID = DataValidation.readPositiveInt("Please choose a showtime: ");
		Showtime showtime = Showtime.showtimeCheckExists(showtimes, showtimeID);
		if(showtime != null){
			if(!Showtime.isPossibleToUpdate(showtime)) {
				return;
			}
			/*Update the movie*/
			while(true) {
				try {
        			for (Movie movie : movies) {
        				System.out.println(Constant.ANSI_GREEN + movie.getId() + ". " + movie.getMovieName() + Constant.ANSI_RESET);
        			}
        			System.out.println("Current show [" + Constant.ANSI_BLUE + showtime.getMovie().getMovieName() + Constant.ANSI_RESET +"] ");
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
        				System.out.println(Constant.ANSI_GREEN + hall.getId() + ". " + hall.getName() + Constant.ANSI_RESET);
        			}
        			System.out.println("Current show at [" + Constant.ANSI_BLUE + showtime.getHall().getName() + Constant.ANSI_RESET +"] ");
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
			
			/*The showtime date & time information */
			SimpleDateFormat formatter ;
			Date date = showtime.getShowtime();
			Date showDateTime;
			boolean hasUpdate = false;
			while(true) {
				try {
					while(true) {
						try {
							formatter = new SimpleDateFormat(Constant.DATE_FORMAT);
							System.out.println("Showtime [" + Constant.ANSI_BLUE + formatter.format(showtime.getShowtime()) + Constant.ANSI_RESET + "] Enter date (" + Constant.DATE_FORMAT+ "): ");
							String input = keyboard.nextLine();
							if (!input.isEmpty()) {
					        	date = formatter.parse(input);
					        	hasUpdate = true;
					            break;
		        	        } else {
		        	            System.out.println("Skipping update date");
		        	            break;
		        	        }
						} catch (Exception e) {
			            	System.out.println("Invalid input format!");
			            }
					}
					Date time = showtime.getShowtime();
					while(true) {
						try {
							formatter = new SimpleDateFormat("HH:mm");
							System.out.println("Showtime [" + Constant.ANSI_BLUE + formatter.format(showtime.getShowtime()) + Constant.ANSI_RESET + "] Enter time (HH:mm) 24-Hour Format: ");
							String input = keyboard.nextLine();
							if (!input.isEmpty()) {
					        	time = formatter.parse(input);
					        	hasUpdate = true;
					            break;
		        	        } else {
		        	            System.out.println("Skipping update time");
		        	            break;
		        	        }
						} catch (Exception e) {
			            	System.out.println("Invalid input format!");
			            }
					}
					if(hasUpdate) {
						showDateTime = new Date(date.getYear(), date.getMonth(), date.getDate(), time.getHours(), time.getMinutes(), time.getSeconds());
						Date now = new Date();
						long oneHourLaterMillis = now.getTime() + (60 * 60 * 1000);
				        Date oneHourLater = new Date(oneHourLaterMillis);
				        if(showDateTime.after(oneHourLater)) { // is equal to or is later than
				        	showtime.setShowtime(new Timestamp(showDateTime.getTime()));
				        	break;
				        } else {
				        	System.out.println("Showtime must be an hour later or more");
				        }
					} else {
						break; // No update
					}
					
				} catch (Exception e) {
	            	System.out.println("Invalid input format!");
	            }
			}
			
			/* The showtime price */
			while(true) {
				try {
					System.out.println("Price for a ticket["+ Constant.ANSI_BLUE + showtime.getPrice() + Constant.ANSI_RESET +"]: ");
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
	
	/**
	 * 
	 * @throws SQLException
	 */
	private static void addShowtime() throws SQLException{
		viewAllShowtimes();
		System.out.println("\n          ADD A SHOWTIME            ");
		System.out.println(  "            ***            ");
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
				Date showDateTime;
				while(true) {
					try {
						showDateTime = inputShowTimeDateTime();
						Date now = new Date();
						long oneHourLaterMillis = now.getTime() + (60 * 60 * 1000);
				        Date oneHourLater = new Date(oneHourLaterMillis);
				        if(showDateTime.after(oneHourLater)) { // is equal to or is later than
				        	break;
				        } else {
				        	System.out.println("Showtime must be an hour later or more");
				        }
					} catch (Exception e) {
		            	System.out.println("Invalid input format!");
		            }
				}
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
	/**
	 * List all showtimes
	 * @return
	 * @throws SQLException
	 */
	private static ArrayList<Showtime> viewAllShowtimes() throws SQLException {
		ArrayList<Showtime> showtimes = Showtime.getAllShowtimes(connection);
		Showtime.displayShowtimes(showtimes);
		return showtimes;
	}
	
	/**
	 * Create a new instance of movie
	 * @return
	 */
	public static Movie takeMovieDetails() {
		Movie movie = new Movie();
		Format formatter = new SimpleDateFormat("MM/dd/yyyy");
		try {
			
			String movieName = DataValidation.inputStringNotEmpty("\nEnter movie name: ");    		
			String synopsis = DataValidation.inputStringNotEmpty("Enter synopsis: ");
    		String releaseDateString = formatter.format(DataValidation.readPositiveDate("Enter release date ("+Constant.DATE_FORMAT+"): "));

    		movie = new Movie(movieName, synopsis, releaseDateString);    		
			
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return movie;	
	}
	
	/**
	 * Movies main menu
	 * @return
	 */
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
	
	/**
	 * Movie main process
	 * @throws SQLException
	 */
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
	
	/**
	 * Delete a movie
	 * @throws SQLException
	 */
	private static void deleteMovie() throws SQLException {
		ArrayList<Movie> movies = Movie.listAll(connection);
		
		System.out.println("\n          CURRENT MOVIES            ");
		System.out.println(  "            *******            ");
		for (int i=0; i<movies.size(); i++) {
			System.out.println("Movie:  " + movies.get(i).getId() + "\t" + movies.get(i).getMovieName());
		}
		
		System.out.println("\n          DELETE A MOVIE            ");
		System.out.println(  "              *******            ");
		boolean isValidId = false, dontDelete = false;
		
		while(!isValidId) {
			int movieId = DataValidation.readPositiveInt("\nEnter movie id: ");
			for(int i=0; i<movies.size(); i++) {
				if(movies.get(i).getId() == movieId) {
					isValidId = true;
					break;
				}
				
			}
			
			if(isValidId) {
				ArrayList<Showtime> showtimes = Showtime.getAllShowtimes(connection);
				for(Showtime showtime: showtimes) {
					if(showtime.getMovie().getId() == movieId) {
						System.out.println(showtime.getMovie().getId());
						dontDelete = true;
						break;
					}
				}
				
				if(!dontDelete) {
					while(true) {
						System.out.println("Are you sure you want to delete the movie " + movieId +" ? y/n");
						String userInput = keyboard.nextLine();
						if(userInput.toUpperCase().equals("Y")) {
							try {
								Movie.delete(connection, movieId);
								break;
							}catch(Exception e){
								System.out.println("An error is encountered. Cannot delete movie. Please try again!");
							}
						}else if (userInput.toUpperCase().equals("N")) {
							System.out.println("Movie is not deleted.");
							break;
						}else {
							System.out.println("Please enter y for Yes or n for No");
						}
					}
				}else {
					System.out.println("Movie has associated showtime, so you need to delete it first");
				}
			}else {
				System.out.println("\nMovie doesn't exist!! \nPlease enter valid movie ID!!");
			}
		}		
	}
	/**
	 * Update a movie
	 * @throws SQLException
	 */
	private static void updateMovie() throws SQLException{
		Date releaseDateFormat;
		SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
		Format formatter = new SimpleDateFormat("MM/dd/yyyy");
		ArrayList<Movie> movies = Movie.listAll(connection);
		Movie updateMovie = movies.get(0);
		
		System.out.println("\n          CURRENT MOVIES            ");
		System.out.println(  "            *******            ");
		for (int i=0; i<movies.size(); i++) {
			System.out.println("Movie:  " + movies.get(i).getId() + "\t" + movies.get(i).getMovieName());
		}

		System.out.println("\n          UPDATE A MOVIE            ");
		System.out.println(  "             *******            ");
		
		boolean isValidId = false;
		int movieId = 0;
		
		while(!isValidId)
		{
			movieId = DataValidation.readPositiveInt("Enter movie id: ");
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
			}
		}
		ArrayList<Movie> movieDetails = Movie.listMovieDetails(connection, movieId);
		for(Movie movie: movieDetails) {
			System.out.println(movie);
		}
		
		System.out.print("\nEnter new movie name or leave it empty to skip: ");
		String movieName = keyboard.nextLine();
		if(movieName.isEmpty()) {
			movieName = movieDetails.get(0).getMovieName();
		}
		System.out.println("Skipping update movie name");
		
		System.out.print("Enter new synopsis or leave it empty to skip: ");
		String synopsis = keyboard.nextLine();
		if(synopsis.isEmpty()) {
			synopsis = movieDetails.get(0).getSynopsis();
		}
		System.out.println("Skipping update synopsis");
		
		System.out.println("Enter new release date or leave it empty to skip: ");
		String releaseDate = keyboard.nextLine();
		if(releaseDate.isEmpty()) {
			releaseDate = movieDetails.get(0).getReleaseDate();
			System.out.println("Skipping update releases date");
		}else {
			try {
				releaseDateFormat = dateFormatter.parse(releaseDate);
				releaseDate = formatter.format(releaseDateFormat);
			} catch (Exception e) {
				System.out.println(e.toString());
			}
		}
		updateMovie.setMovieName(movieName);
		updateMovie.setSynopsis(synopsis);
		updateMovie.setReleaseDate(releaseDate);
		
		Movie.update(connection, updateMovie);
		
		
	}
	
	/**
	 * Add a movie
	 * @throws SQLException
	 */
	private static void addMovie() throws SQLException {
		System.out.println("\n          ADD A MOVIE            ");
		System.out.println(  "            *******            ");
		Movie movie = takeMovieDetails();
		Movie.insert(connection, movie);   
		
	}
	
	/**
	 * View all movies 
	 * @throws SQLException
	 */
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

	/**
	 * input a valid date with a specific format
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Timestamp inputShowTimeDateTime() {
        
        System.out.print("");
        Date date = DataValidation.readPositiveDate("Enter date ("+Constant.DATE_FORMAT+"): ");
        Date time = DataValidation.readPositiveTime("Enter time (HH:mm): ");
        Date dateTime = new Date(date.getYear(), date.getMonth(), date.getDate(), time.getHours(), time.getMinutes(), time.getSeconds());
        Timestamp timestamp = new Timestamp(dateTime.getTime());
        return timestamp;
    }
	
	/**
	 * input a valid time with a specific format
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Date inputShowTimeDateTime(Date showtime) {
		SimpleDateFormat formatter = new SimpleDateFormat(Constant.DATE_FORMAT);
		
        System.out.print("");
        Date date = DataValidation.readPositiveDate("Enter date ("+Constant.DATE_FORMAT+") [" + Constant.ANSI_BLUE+ formatter.format(showtime) + Constant.ANSI_RESET + "]: ");
        formatter = new SimpleDateFormat("hh:mm");
        Date time = DataValidation.readPositiveTime("Enter time (HH:mm) 24-Hour Format [" + Constant.ANSI_BLUE+ formatter.format(showtime) + Constant.ANSI_RESET + "]: ");
        Date dateTime = new Date(date.getYear(), date.getMonth(), date.getDate(), time.getHours(), time.getMinutes(), time.getSeconds());
        return dateTime;
    }
	/*------------------------------------TICKETS----------------------------------------*/
	/**
	 * Update a ticket
	 * @throws SQLException
	 */
	public static void updateTicket() throws SQLException {
		System.out.println("\n          UPDATE TICKETS            ");
		System.out.println(  "            *******            ");
		ArrayList<Showtime> showtimes = viewAllShowtimes();
		int showtimeID = DataValidation.readPositiveInt("Please choose a showtime: ");
		Showtime showtime = Showtime.showtimeCheckExists(showtimes, showtimeID);
		if(showtime != null) {
			if(!Showtime.isPossibleToUpdate(showtime)) {
				return;
			}
			// Update ticket here
			ArrayList<Ticket> tickets = Ticket.getTicketsByShowTimeID(connection, showtimeID);
			Ticket.displayTickets(tickets);
			if(tickets.size() == 0) {
				System.out.println("There is no ticket to update");
				return;
			}
			int ticketID = DataValidation.readPositiveInt("Please choose a ticket: ");
			Ticket ticket = Ticket.ticketCheckExists(tickets, ticketID);
			String currentSeat = ticket.getSeatCode();
			if(ticket != null) {
				// show seats screen 
				String[] selectedSeat = new String[] {ticket.getSeatCode()};
				ArrayList<String> validSeats = Showtime.viewSeat(connection, showtime, selectedSeat);
				
				// Update seat number
				boolean hasUpdate = false;
				while(true) {
					try {
						System.out.println("Seat Number ["+ Constant.ANSI_BLUE + ticket.getSeatCode() + Constant.ANSI_RESET +"] or leave it empty to skip: ");
						String input = keyboard.nextLine().toUpperCase();
						if (!input.isEmpty()) {
							if(validSeats.contains(input)) {
								ticket.setSeatCode(input);
								hasUpdate = true;
								break;
							} else {
								System.out.println("Please Enter Valid Seat Number: ");
							}
	        	        } else {
	        	            System.out.println("Skipping update price");
	        	            break;
	        	        }
					} catch (Exception e) {
		            	System.out.println("Invalid input format!");
		            }
				}
				if(hasUpdate) {
					Ticket.update(connection, ticket);
					System.out.println("Seat Number has changed from " + currentSeat + " to " + ticket.getSeatCode());
				}
			} else {
				System.out.println("The ticket ID provided does not exist.");
			}
		} else {
			System.out.println("The showtime ID provided does not exist.");
		}
	}
	
	/**
	 * Delete a ticket
	 * @throws SQLException
	 */
	public static void deleteTicket() throws SQLException {
		System.out.println("\n          DELETE TICKETS            ");
		System.out.println(  "            *******            ");
		ArrayList<Showtime> showtimes = viewAllShowtimes();
		int showtimeID = DataValidation.readPositiveInt("Please choose a showtime: ");
		Showtime showtime = Showtime.showtimeCheckExists(showtimes, showtimeID);
		if(showtime != null) {
			if(!Showtime.isPossibleToDelete(showtime)) {
				return;
			}
			// Delete ticket here
			ArrayList<Ticket> tickets = Ticket.getTicketsByShowTimeID(connection, showtimeID);
			Ticket.displayTickets(tickets);
			if(tickets.size() == 0) {
				System.out.println("There is no ticket to delete");
				return;
			}
			int ticketID = DataValidation.readPositiveInt("Please choose a ticket: ");
			Ticket ticket = Ticket.ticketCheckExists(tickets, ticketID);
			if(ticket != null) {
				while(true) {
					try {
						System.out.println("Are you sure you want to delete this ticket ID " + ticketID +" ? y/n");
						String userInput = keyboard.nextLine();
						if(userInput.toUpperCase().equals("Y")) {
							Ticket.delete(connection, ticketID);
							break;
						}else if (userInput.toUpperCase().equals("N")) {
							System.out.println("Ticket is not deleted.");
							break;
						}else {
							System.out.println("Please enter y for Yes or n for No");
						}
					}catch(Exception e){
						System.out.println("Invalid input format!");
					}
				}
			} else {
				System.out.println("The ticket ID provided does not exist.");
			}
			
		} else {
			System.out.println("The showtime ID provided does not exist.");
		}
	}
	
	/**
	 * View all tickets
	 * @throws SQLException
	 */
	public static void viewTickets() throws SQLException {
		System.out.println("\n          VIEW TICKETS            ");
		System.out.println(  "            *******            ");
		ArrayList<Showtime> showtimes = viewAllShowtimes();
		int showtimeID = DataValidation.readPositiveInt("Please choose a showtime: ");
		Showtime showtime = Showtime.showtimeCheckExists(showtimes, showtimeID);
		if(showtime != null) {
			ArrayList<Ticket> tickets = Ticket.getTicketsByShowTimeID(connection, showtimeID);
			String[] seats = new String[tickets.size()];
			for(int i=0;i<seats.length;i++) {
				seats[i] = tickets.get(i).getSeatCode();
			}
			Showtime.viewSeat(connection, showtime, seats);
			System.out.println("List of tickets");
			Ticket.displayTickets(tickets);
		} else {
			System.out.println("The showtime ID provided does not exist.");
		}
	}
	
	/**
	 * Ticket main menu
	 * @return
	 */
	public static String ticketsMenu() {
		System.out.println("\n         TICKETS MENU         ");
		System.out.println("----------------------------------------------");
		System.out.println("1 - View tickets");
		System.out.println("2 - Update ticket");
		System.out.println("3 - Delete ticket");
		System.out.println("4 - Exit");
		System.out.print("Your choice: ");
		return keyboard.nextLine();
	}
	
	/**
	 * Ticket main process
	 * @throws SQLException
	 */
	public static void ticketsProcess() throws SQLException {
		String choice = "";
    	do {
    		choice = ticketsMenu();
    		if(choice.equals("1")) {
    			viewTickets();
    		} else if(choice.equals("2")) {
    			updateTicket();
    		} else if(choice.equals("3")) {
    			deleteTicket();
    		} else if(choice.equals("4")) {
    			// go back
    		} else {
    			System.out.println("Bad input! Please try again!");
    		}
    	} while(!choice.equals("4"));
	}
}

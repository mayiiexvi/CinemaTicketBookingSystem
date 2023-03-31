package common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

/**
 * Showtime class
 * @author Sylvia Espina C0866311
 * @author Mufida Andi C0864756
 * @author Jenil Shivamkumar Varma C0870543
 * @author Tich Vu Lu C0861736
 * @author Jay Shah C0868053
 */

public class Showtime {
	/*----------- Fields ----------- */
	private int id;
	private Movie movie;
	private Hall hall;
	private Timestamp showtime;
	private double price;
	private int availableSeats;
	
	/*----------- Getter Setter ----------- */
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the movie
	 */
	public Movie getMovie() {
		return movie;
	}
	/**
	 * @param movie the movie to set
	 */
	public void setMovie(Movie movie) {
		this.movie = movie;
	}
	/**
	 * @return the hall
	 */
	public Hall getHall() {
		return hall;
	}
	/**
	 * @param hall the hall to set
	 */
	public void setHall(Hall hall) {
		this.hall = hall;
	}
	/**
	 * @return the showtime
	 */
	public Timestamp getShowtime() {
		return showtime;
	}
	/**
	 * @param showtime the showtime to set
	 */
	public void setShowtime(Timestamp showtime) {
		this.showtime = showtime;
	}
	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}
	/**
	 * @return the availableSeats
	 */
	public int getAvailableSeats() {
		return availableSeats;
	}
	/**
	 * @param availableSeats the availableSeats to set
	 */
	public void setAvailableSeats(int availableSeats) {
		this.availableSeats = availableSeats;
	}
	/**
	 * This function is set to format the showtime
	 * @return
	 */
	public String getShowTimeFormatted() {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		return formatter.format(showtime);
	}
	/*----------- Constructors----------- */
	/**
	 * Default constructor
	 */
	public Showtime() {
	}
	
	/**
	 * Constructor with fields
	 * @param id
	 * @param movie
	 * @param hall
	 * @param showtime
	 * @param price
	 */
	public Showtime(int id, Movie movie, Hall hall, Timestamp showtime, double price) {
		this.id = id;
		this.movie = movie;
		this.hall = hall;
		this.showtime = showtime;
		this.price = price;
	}
	
	/**
	 * Constructor with fields
	 * @param movie
	 * @param hall
	 * @param showtime
	 * @param price
	 */
	public Showtime(Movie movie, Hall hall, Timestamp showtime, double price) {
		this.movie = movie;
		this.hall = hall;
		this.showtime = showtime;
		this.price = price;
	}
	/*----------- Methods ----------- */
	
	/**
	 * Get a list of available showtimes
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	public static ArrayList<Showtime> getAvailableShowtimes(Connection connection) throws SQLException{
		ArrayList<Showtime> showtimes = new ArrayList<>();
		String query = "SELECT A.ID, A.SHOWTIME, A.PRICE, B.ID AS HALL_ID, B.NAME AS HALL_NAME,\r\n"
				+ "C.ID AS MOVIE_ID, C.MOVIE_NAME, C.synopsis, C.release_date,\r\n"
				+ "((SELECT SEATING_ROWS * SEATING_COLS FROM HALLS WHERE ID = B.ID) - (SELECT COUNT(*) AS SEATBOOKED FROM TICKETS WHERE SHOWTIME_ID = A.ID)) \r\n"
				+ "- (SELECT (LENGTH(B.HIDENSEATS) - LENGTH(REPLACE(HIDENSEATS, ',', '')) + 1) ) AS AVAILABLE_SEATS,\r\n"
				+ "B.SEATING_ROWS, B.SEATING_COLS, HIDENSEATS, C.DURATION\r\n"
				+ "FROM SHOWTIME A INNER JOIN HALLS B ON A.HALL_ID = B.ID\r\n"
				+ "INNER JOIN MOVIES C ON A.MOVIE_ID = C.ID\r\n"
				+ "WHERE A.SHOWTIME >= DATE_SUB(NOW(), INTERVAL 1 HOUR)\r\n"
				+ "ORDER BY A.ID DESC;";
		PreparedStatement statement = connection.prepareStatement(query);
		ResultSet resultSet = statement.executeQuery();
		while (resultSet.next()) {
			Movie mv = new Movie(resultSet.getInt("MOVIE_ID"), resultSet.getString("MOVIE_NAME"), resultSet.getString("synopsis"), resultSet.getString("release_date"));
			Hall hall = new Hall(resultSet.getInt("HALL_ID"), resultSet.getString("HALL_NAME"), resultSet.getInt("SEATING_ROWS"), resultSet.getInt("SEATING_COLS"), resultSet.getString("HIDENSEATS") );
			Showtime showtime = new Showtime();
			showtime.setId(resultSet.getInt("ID"));
			showtime.setPrice(resultSet.getDouble("PRICE"));
			Timestamp timestamp = resultSet.getTimestamp("SHOWTIME");
			if (timestamp != null) {
				showtime.setShowtime(timestamp);
			}
			showtime.setAvailableSeats(resultSet.getInt("AVAILABLE_SEATS"));
			showtime.setHall(hall);
			showtime.setMovie(mv);
			showtimes.add(showtime);
		}
		return showtimes;
	}
	/**
	 * Get a list of available showtimes
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	public static ArrayList<Showtime> getAllShowtimes(Connection connection) throws SQLException{
		ArrayList<Showtime> showtimes = new ArrayList<>();
		String query = "SELECT A.ID, A.SHOWTIME, A.PRICE, B.ID AS HALL_ID, B.NAME AS HALL_NAME,\r\n"
				+ "C.ID AS MOVIE_ID, C.MOVIE_NAME, C.synopsis, C.release_date,\r\n"
				+ "((SELECT SEATING_ROWS * SEATING_COLS FROM HALLS WHERE ID = B.ID) - (SELECT COUNT(*) AS SEATBOOKED FROM TICKETS WHERE SHOWTIME_ID = A.ID)) \r\n"
				+ "- (SELECT (LENGTH(B.HIDENSEATS) - LENGTH(REPLACE(HIDENSEATS, ',', ''))) - 1) AS AVAILABLE_SEATS,\r\n"
				+ "B.SEATING_ROWS, B.SEATING_COLS, HIDENSEATS, C.DURATION\r\n"
				+ "FROM SHOWTIME A INNER JOIN HALLS B ON A.HALL_ID = B.ID\r\n"
				+ "INNER JOIN MOVIES C ON A.MOVIE_ID = C.ID\r\n"
				+ "ORDER BY A.ID DESC;";
		PreparedStatement statement = connection.prepareStatement(query);
		ResultSet resultSet = statement.executeQuery();
		while (resultSet.next()) {
			Movie mv = new Movie(resultSet.getInt("MOVIE_ID"), resultSet.getString("MOVIE_NAME"), resultSet.getString("synopsis"), resultSet.getString("release_date"), resultSet.getInt("DURATION"));
			Hall hall = new Hall(resultSet.getInt("HALL_ID"), resultSet.getString("HALL_NAME"), resultSet.getInt("SEATING_ROWS"), resultSet.getInt("SEATING_COLS"), resultSet.getString("HIDENSEATS") );
			Showtime showtime = new Showtime();
			showtime.setId(resultSet.getInt("ID"));
			showtime.setPrice(resultSet.getDouble("PRICE"));
			Timestamp timestamp = resultSet.getTimestamp("SHOWTIME");
			if (timestamp != null) {
				showtime.setShowtime(timestamp);
			}
			showtime.setAvailableSeats(resultSet.getInt("AVAILABLE_SEATS"));
			showtime.setHall(hall);
			showtime.setMovie(mv);
			showtimes.add(showtime);
		}
		return showtimes;
	}
	
	/**
	 * This function is set to check whether a specific showtime id exists in the available showtime list.
	 * @param showtimes
	 * @param id
	 * @return
	 */
	public static Showtime showtimeCheckExists(ArrayList<Showtime> showtimes, int id) {
		// 1. Foreach way
		/*
		for (Showtime showtime : showtimes) {
			if(showtime.getId() == id) {
				return showtime;
			}
		}
		return null;
		*/
		
		// 2. Lambda Expression way
		Optional<Showtime> showtime = showtimes.stream()
                .filter(p -> p.getId() == id)
                .findFirst();
		if(showtime.isPresent()) {
			return showtime.get();
		} else {
			return null;
		}
	}
	
	/**
	 * Insert a showtime function
	 * @param connection
	 * @param showtime
	 * @throws SQLException
	 */
	public static void insert(Connection connection, Showtime showtime) throws SQLException {
		String query = "INSERT INTO showtime (movie_id, hall_id, showtime, price) VALUES (?, ?, ?, ?)";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, showtime.getMovie().getId());
		statement.setInt(2, showtime.getHall().getId());
		statement.setTimestamp(3, showtime.getShowtime());
		statement.setDouble(4, showtime.getPrice());
		statement.executeUpdate();
		System.out.println("Showtime inserted successfully\n");
	}

	/**
	 * Update a showtime function
	 * @param connection
	 * @param showtime
	 * @throws SQLException
	 */
	public static void update(Connection connection, Showtime showtime) throws SQLException {
		String query = "UPDATE showtime SET movie_id = ?, hall_id = ?, showtime = ?, price = ? WHERE id = ?";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, showtime.getMovie().getId());
		statement.setInt(2, showtime.getHall().getId());
		statement.setTimestamp(3, showtime.getShowtime());
		statement.setDouble(4, showtime.getPrice());
		statement.setInt(5, showtime.getId());
		statement.executeUpdate();
		System.out.println("Showtime updated successfully\n");
	}

	/**
	 * Delete a showtime function
	 * @param connection
	 * @param showtime_id
	 * @throws SQLException
	 */
	public static void delete(Connection connection, int showtime_id) throws SQLException {
		String query = "DELETE FROM showtime WHERE id = ?";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, showtime_id);
		statement.executeUpdate();
		System.out.println("Showtime deleted successfully\n");
	}

	/**
	 * This function displays a list of Showtimes in table format
	 * @param showtimes
	 */
	public static void displayShowtimes(ArrayList<Showtime> showtimes) {
	    System.out.println("+-----+--------+------------------+-------+--------------+---------------------+----------+-------------+----------+---------+");
	    System.out.println("| ID  | Movie  | Movie Name       | Hall  | Hall Name    | Showtime            | Price    | Available   | Duration | Status  |");
	    System.out.println("|     | ID     |                  | ID    |              |                     |          | Seats       | (min)    |         |");
	    System.out.println("+-----+--------+------------------+-------+--------------+---------------------+----------+-------------+----------+---------+");
	    for (Showtime showtime : showtimes) {
	    	Date now = new Date();
			long finishMillis = showtime.getShowtime().getTime() + (showtime.getMovie().getDuration() * 60 * 1000);
	        Date finishTime = new Date(finishMillis);
	        if(now.after(showtime.getShowtime()) && now.before(finishTime)) { // It's showing
	        	// Now showing
	        	System.out.println(Constant.ANSI_RED + String.format("| %-3d | %-6d | %-16s | %-5d | %-12s | %-19s | $%-7.2f | %-11d | %-8d | %-7s |",
		                showtime.getId(), showtime.getMovie().getId(), showtime.getMovie().getMovieName(),
		                showtime.getHall().getId(), showtime.getHall().getName(), showtime.getShowTimeFormatted(),
		                showtime.getPrice(), showtime.getAvailableSeats(), showtime.getMovie().getDuration(), "Showing") + Constant.ANSI_RESET);
	        } else if(now.before(showtime.getShowtime())){
	        	// Open
	        	System.out.println(Constant.ANSI_GREEN + String.format("| %-3d | %-6d | %-16s | %-5d | %-12s | %-19s | $%-7.2f | %-11d | %-8d | %-7s |",
		                showtime.getId(), showtime.getMovie().getId(), showtime.getMovie().getMovieName(),
		                showtime.getHall().getId(), showtime.getHall().getName(), showtime.getShowTimeFormatted(),
		                showtime.getPrice(), showtime.getAvailableSeats(), showtime.getMovie().getDuration(), "Open  ") + Constant.ANSI_RESET);
	        } else {
	        	// Closed
	        	System.out.println(String.format("| %-3d | %-6d | %-16s | %-5d | %-12s | %-19s | $%-7.2f | %-11d | %-8d | %-7s |",
		                showtime.getId(), showtime.getMovie().getId(), showtime.getMovie().getMovieName(),
		                showtime.getHall().getId(), showtime.getHall().getName(), showtime.getShowTimeFormatted(),
		                showtime.getPrice(), showtime.getAvailableSeats(), showtime.getMovie().getDuration(), "Closed"));
	        }
	        
	    }
	    System.out.println("+-----+--------+------------------+-------+--------------+---------------------+----------+-------------+----------+---------+");
	}
	
	/**
	 * This function checks if a showtime is possible to update.
	 * @param showtime
	 * @return
	 */
	public static boolean isPossibleToUpdate(Showtime showtime){
		Date now = new Date();
    	long finishMillis = showtime.getShowtime().getTime() + (showtime.getMovie().getDuration() * 60 * 1000);
        Date finishTime = new Date(finishMillis);
        if(now.after(showtime.getShowtime()) && now.before(finishTime)) { // It's showing
        	// Now showing
        	System.out.println("This show is in progress and cannot be updated.");
        	return false;
        } else if(now.before(showtime.getShowtime())){
        	// Open
        	return true;
        } else {
        	// Closed
        	System.out.println("This show is closed and cannot be updated.");
        	return false;
        }
	}
	
	/**
	 * This function checks if a showtime is possible to delete.
	 * @param showtime
	 * @return
	 */
	public static boolean isPossibleToDelete(Showtime showtime){
		Date now = new Date();
    	long finishMillis = showtime.getShowtime().getTime() + (showtime.getMovie().getDuration() * 60 * 1000);
        Date finishTime = new Date(finishMillis);
        if(now.after(showtime.getShowtime()) && now.before(finishTime)) { // It's showing
        	// Now showing
        	System.out.println("This show is in progress and cannot be deleted.");
        	return false;
        } else if(now.before(showtime.getShowtime())){
        	// Open
        	return true;
        } else {
        	// Closed
        	return true;
        }
	}
	
	/**
	 * This function checks if the selected seat is in the hidden seats or not
	 * @param hidenSeats
	 * @param seatCode
	 * @return
	 */
	public static boolean isHidenSeat(String[] hidenSeats, String seatCode) {
		for (String seat : hidenSeats) {
			if(seat.equals(seatCode)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * fixed length string
	 * @param string
	 * @param length
	 * @return
	 */
	public static String fixedLengthString(String string, int length) {
	    return String.format("%-" + length + "s", string);
	}
	
	/**
	 * This function displays seats (total seats, available seats and booked seats)
	 * @param connection
	 * @param showtime
	 * @param selectedSeats
	 * @return
	 * @throws SQLException
	 */
	public static ArrayList<String> viewSeat(Connection connection, Showtime showtime, String[] selectedSeats) throws SQLException {
		ArrayList<Ticket> seatsBooked = Ticket.getTicketsByShowTimeID(connection, showtime.getId());
		int numRows = showtime.getHall().getSeatingRows();
		int numCols = showtime.getHall().getSeatingCols();
		String[] hidenSeats = showtime.getHall().getHidenSeats().split(",");
		System.out.print("     ");
        System.out.println();
        ArrayList<String> validSeats = new ArrayList<>(); 
        for (int i = 0; i < numRows; i++) {
        	System.out.print("   ");
            for (int j = 0; j < numCols; j++) {
            	String seatCode = (char) ('A' + i)+String.valueOf(j+1);
                if (Ticket.isBookedSeat(seatsBooked,seatCode )) {
                    System.out.print(Constant.ANSI_RED + fixedLengthString(seatCode, 5) + Constant.ANSI_RESET);
                } else {
                	if(isHidenSeat(hidenSeats, seatCode)) {
                		System.out.print(fixedLengthString(" ", 5));
                	} else {
                		if(Arrays.asList(selectedSeats).contains(seatCode)) {
                			System.out.print(Constant.ANSI_GREEN + fixedLengthString(seatCode, 5) + Constant.ANSI_RESET);
                		} else {
                		System.out.print(fixedLengthString(seatCode, 5));
                		}
                		validSeats.add(seatCode);
                	}
                }
            }
            System.out.println();
        }
        String screen1 = "____________________________________________";
        String screen2 = "|                  Screen                    |";
        int tmp = ((numCols*5 + 3) - screen1.length()) /2 ;
        System.out.println(Character.toString(' ').repeat(tmp+1) + screen1 + Character.toString(' ').repeat(tmp));
        System.out.println(Character.toString(' ').repeat(tmp) + screen2 + Character.toString(' ').repeat(tmp));
		System.out.println();
		return validSeats;
	}
}

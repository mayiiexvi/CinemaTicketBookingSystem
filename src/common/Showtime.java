package common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * @author Tich
 *
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
	public String getShowTimeFormatted() {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		return formatter.format(showtime);
	}
	/*----------- Constructors----------- */
	/**
	 * 
	 */
	public Showtime() {
	}
	/**
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
				+ "- (SELECT (LENGTH(B.HIDENSEATS) - LENGTH(REPLACE(HIDENSEATS, ',', ''))) - 1) AS AVAILABLE_SEATS,\r\n"
				+ "B.SEATING_ROWS, B.SEATING_COLS, HIDENSEATS\r\n"
				+ "FROM SHOWTIME A INNER JOIN HALLS B ON A.HALL_ID = B.ID\r\n"
				+ "INNER JOIN MOVIES C ON A.MOVIE_ID = C.ID\r\n"
				+ "ORDER BY A.ID DESC;";
		PreparedStatement statement = connection.prepareStatement(query);
		ResultSet resultSet = statement.executeQuery();
		while (resultSet.next()) {
			Movie mv = new Movie(resultSet.getInt("MOVIE_ID"), resultSet.getString("MOVIE_NAME"), resultSet.getString("synopsis"), resultSet.getString("release_date"));
			Hall hall = new Hall(resultSet.getInt("HALL_ID"), resultSet.getString("HALL_NAME"), resultSet.getInt("SEATING_ROWS"), resultSet.getInt("SEATING_COLS"), resultSet.getString("HIDENSEATS") );
			Showtime showtime = new Showtime();
			showtime.setId(resultSet.getInt("ID"));
			showtime.setPrice(resultSet.getDouble("PRICE"));
			//showtime.setShowtime(resultSet.getDate("SHOWTIME"));
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
	
	public boolean checkAvailableSeat() {
		return true;
	}
	/**
	 * This function is set to check whether a specific showtime id exists in the available showtime list.
	 * @param showtimes
	 * @param id
	 * @return
	 */
	public static Showtime showtimeCheckExists(ArrayList<Showtime> showtimes, int id) {
		for (Showtime showtime : showtimes) {
			if(showtime.getId() == id) {
				return showtime;
			}
		}
		return null;
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

	public static void displayShowtimes(ArrayList<Showtime> showtimes) {
	    System.out.println("+-----+----------+----------------+-------+----------------+---------------------+-------+--------------+");
	    System.out.println("| ID  | Movie ID | Movie Name     | Hall  | Hall Name      | Showtime            | Price | Available    |");
	    System.out.println("|     |          |                | ID    |                |                     |       | Seats        |");
	    System.out.println("+-----+----------+----------------+-------+----------------+---------------------+-------+--------------+");
	    for (Showtime showtime : showtimes) {
	        System.out.printf("| %1$-3d | %2$-8d | %3$-14s | %4$-5d | %5$-14s | %6$-19s | $%7$-5.2f | %8$-12d |\n",
	                showtime.getId(), showtime.getMovie().getId(), showtime.getMovie().getMovieName(),
	                showtime.getHall().getId(), showtime.getHall().getName(), showtime.getShowTimeFormatted(),
	                showtime.getPrice(), showtime.getAvailableSeats());
	    }
	    System.out.println("+-----+----------+----------------+-------+----------------+---------------------+-------+--------------+");
	}

}

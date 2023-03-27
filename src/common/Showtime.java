package common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Tich
 *
 */
public class Showtime {
	/*----------- Fields ----------- */
	private int id;
	private Movie movie;
	private Hall hall;
	private Date showtime;
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
	public Date getShowtime() {
		return showtime;
	}
	/**
	 * @param showtime the showtime to set
	 */
	public void setShowtime(Date showtime) {
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
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");
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
	public Showtime(int id, Movie movie, Hall hall, Date showtime, double price) {
		this.id = id;
		this.movie = movie;
		this.hall = hall;
		this.showtime = showtime;
		this.price = price;
	}
	
	/*----------- Methods ----------- */
	
	public static ArrayList<Showtime> getAvailableShowtimes(Connection connection) throws SQLException{
		ArrayList<Showtime> showtimes = new ArrayList<>();
		String query = "SELECT A.ID, A.SHOWTIME, A.PRICE, B.ID AS HALL_ID, B.NAME AS HALL_NAME,\r\n"
				+ "C.ID AS MOVIE_ID, C.MOVIE_NAME, C.synopsis, C.release_date,\r\n"
				+ "((SELECT SEATING_ROWS * SEATING_COLS FROM HALLS WHERE ID = B.ID) - (SELECT COUNT(*) AS SEATBOOKED FROM TICKETS WHERE SHOWTIME_ID = A.ID)) AS AVAILABLE_SEATS,\r\n"
				+ "B.SEATING_ROWS, B.SEATING_COLS, B.HIDENSEATS\r\n"
				+ "FROM SHOWTIME A INNER JOIN HALLS B ON A.HALL_ID = B.ID\r\n"
				+ "INNER JOIN MOVIES C ON A.MOVIE_ID = C.ID;";
		PreparedStatement statement = connection.prepareStatement(query);
		ResultSet resultSet = statement.executeQuery();
		while (resultSet.next()) {
			Movie mv = new Movie(resultSet.getInt("MOVIE_ID"), resultSet.getString("MOVIE_NAME"), resultSet.getString("synopsis"), resultSet.getString("release_date"), 0.0);
			Hall hall = new Hall(resultSet.getString("HALL_NAME"), resultSet.getInt("SEATING_ROWS"), resultSet.getInt("SEATING_COLS"), resultSet.getString("HIDENSEATS") );
			Showtime showtime = new Showtime();
			showtime.setId(resultSet.getInt("ID"));
			showtime.setPrice(resultSet.getDouble("PRICE"));
			//showtime.setShowtime(resultSet.getDate("SHOWTIME"));
			Timestamp timestamp = resultSet.getTimestamp("SHOWTIME");
			if (timestamp != null) {
				showtime.setShowtime(new java.util.Date(timestamp.getTime()));
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
}

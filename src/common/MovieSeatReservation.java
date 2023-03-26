/**
 * 
 */
package common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MovieSeatReservation {
	private int id;
	private int movie_id;
	private String seat_number;
	private boolean reserved;
	
	public MovieSeatReservation() {
		this.id = 0;
		this.movie_id = 0;
		this.seat_number = "";
		this.reserved = false;
	}
	
	/**
	 * @param id
	 * @param movie_id
	 * @param seat_number
	 * @param reserved
	 */
	public MovieSeatReservation(int id, int movie_id, String seat_number, boolean reserved) {
		this.id = id;
		this.movie_id = movie_id;
		this.seat_number = seat_number;
		this.reserved = reserved;
	}
  
	/**
	 * @param movieName
	 * @param synopsis
	 * @param releaseDate
	 */
	public MovieSeatReservation(int movie_id, String seat_number, boolean reserved) {
		this.movie_id = movie_id;
		this.seat_number = seat_number;
		this.reserved = reserved;
	}
	
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
	 * @return the movie_id
	 */
	public int getMovie_id() {
		return movie_id;
	}

	/**
	 * @param movie_id the movie_id to set
	 */
	public void setMovie_id(int movie_id) {
		this.movie_id = movie_id;
	}

	/**
	 * @return the seat_number
	 */
	public String getSeat_number() {
		return seat_number;
	}

	/**
	 * @param seat_number the seat_number to set
	 */
	public void setSeat_number(String seat_number) {
		this.seat_number = seat_number;
	}

	/**
	 * @return the reserved
	 */
	public boolean isReserved() {
		return reserved;
	}

	/**
	 * @param reserved the reserved to set
	 */
	public void setReserved(boolean reserved) {
		this.reserved = reserved;
	}
	
	
	public static ArrayList<MovieSeatReservation> selectedSeats(Connection connection, int movie_id) throws SQLException {
		String query = "SELECT * FROM seatreservation WHERE movie_id =?";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, movie_id);
		ResultSet resultSet = statement.executeQuery();
		ArrayList<MovieSeatReservation> movieSeatsReserved = new ArrayList<>();
		while(resultSet.next()) {
			int id = resultSet.getInt("id");
			String seat_number = resultSet.getString("seat_number");
			boolean reserved = resultSet.getBoolean("reserved");
			MovieSeatReservation msr = new MovieSeatReservation(id, movie_id, seat_number, reserved);
			movieSeatsReserved.add(msr);
		}
		
		return movieSeatsReserved;
	}

	public static void insert(Connection connection, MovieSeatReservation movieSeatReservation) throws SQLException {
		String query = "INSERT INTO seatreservation (movie_id, seat_number, reserved) VALUES (?, ?, ?)";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, movieSeatReservation.getMovie_id());
		statement.setString(2, movieSeatReservation.getSeat_number());
		statement.setBoolean(3, movieSeatReservation.isReserved());
		statement.executeUpdate();
	}

}

/**
 * 
 */
package common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class Movie {
	private int id;
	private String movieName;
	private String synopsis;
	private Date releaseDate;
	
	/**
	 * @param id
	 * @param movieName
	 * @param synopsis
	 * @param releaseDate
	 */
	public Movie(int id, String movieName, String synopsis, Date releaseDate) {
		this.id = id;
		this.movieName = movieName;
		this.synopsis = synopsis;
		this.releaseDate = releaseDate;
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
	 * @return the movieName
	 */
	public String getMovieName() {
		return movieName;
	}
	/**
	 * @param movieName the movieName to set
	 */
	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}
	/**
	 * @return the synopsis
	 */
	public String getSynopsis() {
		return synopsis;
	}
	/**
	 * @param synopsis the synopsis to set
	 */
	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}
	/**
	 * @return the releaseDate
	 */
	public Date getReleaseDate() {
		return releaseDate;
	}
	/**
	 * @param releaseDate the releaseDate to set
	 */
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}
	
	public static void insert(Connection connection, Movie movie) throws SQLException {
		String query = "INSERT INTO movies (movie_name, synopsis, release_date) VALUES (?, ?, ?)";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(1, movie.getMovieName());
		statement.setString(2, movie.getSynopsis());
		statement.setDate(3, (java.sql.Date) movie.getReleaseDate());
		statement.executeUpdate();
		System.out.println("Movie inserted successfully");
	}

	public static void update(Connection connection, Movie movie) throws SQLException {
		String query = "UPDATE movies SET movie_name = ?, synopsis = ?, release_date = ? WHERE id = ?";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(1, movie.getMovieName());
		statement.setString(2, movie.getSynopsis());
		statement.setDate(3, (java.sql.Date) movie.getReleaseDate());
		statement.setInt(4, movie.getId());
		statement.executeUpdate();
		System.out.println("Movie updated successfully");
	}

	public static void delete(Connection connection, int movie_id) throws SQLException {
		String query = "DELETE FROM movies WHERE id = ?";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, movie_id);
		statement.executeUpdate();
		System.out.println("Movie deleted successfully");
	}

	public static ArrayList<Movie> listAll(Connection connection) throws SQLException {
		String query = "SELECT * FROM movies";
		PreparedStatement statement = connection.prepareStatement(query);
		ResultSet resultSet = statement.executeQuery();
		ArrayList<Movie> movies = new ArrayList<>();
		while (resultSet.next()) {
			int id = resultSet.getInt("id");
			String name = resultSet.getString("movie_name");
			String synopsis = resultSet.getString("synopsis");
			Date release_date = resultSet.getDate("release_date");
			Movie mv = new Movie(id, name, synopsis, release_date);
			movies.add(mv);
		}
		return movies;
	}

}

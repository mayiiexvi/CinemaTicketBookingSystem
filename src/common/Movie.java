/**
 * 
 */
package common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Movie {
	
	/*----------- Fields ----------- */
	private int id;
	private String movieName;
	private String synopsis;
	private String releaseDate;
	private double price;
	
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
	public String getReleaseDate() {
		return releaseDate;
	}
  
	/**
	 * @param releaseDate the releaseDate to set
	 */
  
	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}
	/**
	 * 
	 * @return
	 */
	public double getPrice() {
		return price;
	}
	/**
	 * 
	 * @param price
	 */
	public void setPrice(double price) {
		this.price = price;
	}
	
	/*----------- Constructors ----------- */
	/**
	 * Default constructor
	 */
	public Movie() {
		this.movieName = "";
		this.synopsis = "";
		this.releaseDate = "";
		this.price = 0.0;
	}
	
	/**
	 * Constructor with full fields
	 * @param id
	 * @param movieName
	 * @param synopsis
	 * @param releaseDate
	 */
	public Movie(int id, String movieName, String synopsis, String releaseDate, double price) {
		this.id = id;
		this.movieName = movieName;
		this.synopsis = synopsis;
		this.releaseDate = releaseDate;
		this.price = price;
	}
	
	public Movie(String movieName, String synopsis, String releaseDate, double price) {
		this.movieName = movieName;
		this.synopsis = synopsis;
		this.releaseDate = releaseDate;
		this.setPrice(price);
	}
  
	
	
	public String toString() {
		return ("ID:\t\t" + id + "\n"
				+ "Movie name:\t" + movieName + "\n"
				+ "Synopsis:\t" + synopsis + "\n"
				+ "Release date:\t" + releaseDate  
				//+"\n" + "Price: " + price
				 );
	}
	
	public static void insert(Connection connection, Movie movie) throws SQLException {
		String query = "INSERT INTO movies (movie_name, synopsis, release_date, price) VALUES (?, ?, ?, ?)";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(1, movie.getMovieName());
		statement.setString(2, movie.getSynopsis());
		statement.setString(3, movie.getReleaseDate());
		statement.setDouble(4, movie.getPrice());
		statement.executeUpdate();
		System.out.println("Movie inserted successfully");
	}

	public static void update(Connection connection, Movie movie) throws SQLException {
		String query = "UPDATE movies SET movie_name = ?, synopsis = ?, release_date = ?, price = ? WHERE id = ?";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(1, movie.getMovieName());
		statement.setString(2, movie.getSynopsis());
		statement.setString(3, movie.getReleaseDate());
		statement.setDouble(4, movie.getPrice());
		statement.setInt(5, movie.getId());
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
			String release_date = resultSet.getString("release_date");
			double price = resultSet.getDouble("price");
			Movie mv = new Movie(id, name, synopsis, release_date, price);
			movies.add(mv);
		}
		return movies;
	}
	
	public static ArrayList<Movie> listMovieDetails(Connection connection, int movie_id) throws SQLException {
		String query = "SELECT * FROM movies WHERE id =?";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, movie_id);
		ResultSet resultSet = statement.executeQuery();
		ArrayList<Movie> movies = new ArrayList<>();
		while(resultSet.next()) {
			String name = resultSet.getString("movie_name");
			String synopsis = resultSet.getString("synopsis");
			String release_date = resultSet.getString("release_date");
			double price = resultSet.getDouble("price");
			Movie mv = new Movie(movie_id, name, synopsis, release_date, price);
			movies.add(mv);
		}
		
		return movies;
	}
	
	public static ArrayList<Movie> selectedSeats(Connection connection, int movie_id) throws SQLException {
		String query = "SELECT * FROM seatselection WHERE movie_id =?";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, movie_id);
		ResultSet resultSet = statement.executeQuery();
		ArrayList<Movie> movies = new ArrayList<>();
		while(resultSet.next()) {
			String name = resultSet.getString("movie_name");
			String synopsis = resultSet.getString("synopsis");
			String release_date = resultSet.getString("release_date");
			double price = resultSet.getDouble("price");
			Movie mv = new Movie(movie_id, name, synopsis, release_date, price);
			movies.add(mv);
		}
		
		return movies;
	}
	
	public static Movie getByID(Connection connection, int movieId) throws SQLException {
		String query = "SELECT * FROM movies WHERE id =? ";
		PreparedStatement statement = connection.prepareStatement(query);
		ResultSet resultSet = statement.executeQuery();
		Movie mv = new Movie();
		while (resultSet.next()) {
			int id = resultSet.getInt("id");
			String name = resultSet.getString("movie_name");
			String synopsis = resultSet.getString("synopsis");
			String release_date = resultSet.getString("release_date");
			double price = resultSet.getDouble("price");
			mv = new Movie(id, name, synopsis, release_date, price);
		}
		return mv;
	}

}

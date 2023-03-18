/**
 * 
 */
package cinemaTicketBookingSystem;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Scanner;

/**
 * @author mufida
 *
 */
public class Movie {

	/**
	 * @param args
	 */
	
	private String movieName, synopsis;
	private double price;
	private Date date, time;
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
	 * @return the price
	 */
	public Double getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(Double price) {
		this.price = price;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the time
	 */
	public Date getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(Date time) {
		this.time = time;
	}
	
	/**- saveToFile
	- updateFile
	- deleteMovie
	- nowShowing
	 * @throws IOException 
	**/
	private static long counter = 0;

    public static long generateId() throws IOException {
    	File file = new File("movies.txt");
		Scanner infile = new Scanner(file);
		String readFile = "";

		
		while (infile.hasNextLine()) {
			readFile = infile.nextLine();
			++counter;
		};
		infile.close();
        return ++counter;
    }
	
	
	public static void addToNowShowing(String movieName, String synopsis, double price) throws IOException {
		try {
			FileWriter file = new FileWriter("movies.txt", true);
			PrintWriter outfile = new PrintWriter(file);
			
			outfile.println("ID: " + generateId() + " | Movie Name: " + movieName + " | Synopsis: " + synopsis + " | Price: $" + price);
			System.out.println("Movie added to the list!");
			outfile.close();
		} catch (Exception e) {
			System.out.println("Add to List failed. Error: " + e);
		}
		

	}
	
	public static void viewAllNowShowing() throws IOException {
		File file = new File("movies.txt");
		Scanner infile = new Scanner(file);
		String readFile = "";
		
		while(infile.hasNextLine()) {
			readFile = infile.nextLine();
			System.out.println(readFile);
		}
		
		infile.close();

	}
	
	public static void updateNowShowing(int movie_id, String movieName, String synopsis) throws IOException {
		File file = new File("movies.txt");
		Scanner infile = new Scanner(file);
		
		
		
		infile.close();

	}
	
	public static void main(String[] args) throws IOException {
	// Create separate class for Movie (movieName, synopsis, price, date, time)
		//
		System.out.println(generateId());;
		
		
	}

}

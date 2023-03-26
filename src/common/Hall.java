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

/**
 * @author Tich
 *
 */
public class Hall {
	/*----------- Fields ----------- */
	private int id;
	private String name;
	private int seatingRows;
	private int seatingCols;
	
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the seatingRows
	 */
	public int getSeatingRows() {
		return seatingRows;
	}
	/**
	 * @param seatingRows the seatingRows to set
	 */
	public void setSeatingRows(int seatingRows) {
		this.seatingRows = seatingRows;
	}
	/**
	 * @return the seatingCols
	 */
	public int getSeatingCols() {
		return seatingCols;
	}
	/**
	 * @param seatingCols the seatingCols to set
	 */
	public void setSeatingCols(int seatingCols) {
		this.seatingCols = seatingCols;
	}
	
	/*----------- Constructors ----------- */
	

	public Hall(String name) {
		this.name = name;
	}
	
	/**
	 * @param id
	 * @param name
	 * @param seatingRows
	 * @param seatingCols
	 */
	public Hall(int id, String name, int seatingRows, int seatingCols) {
		this.id = id;
		this.name = name;
		this.seatingRows = seatingRows;
		this.seatingCols = seatingCols;
	}
	/*----------- Methods ----------- */
	
	public static void insert(Connection connection, Hall hall) throws SQLException {
		//
	}

	public static void update(Connection connection, Movie movie) throws SQLException {
		//
	}

	public static void delete(Connection connection, int hall_id) throws SQLException {
		//
	}

	public static ArrayList<Hall> listAll(Connection connection) throws SQLException {
		String query = "SELECT * FROM halls";
		PreparedStatement statement = connection.prepareStatement(query);
		ResultSet resultSet = statement.executeQuery();
		ArrayList<Hall> halls = new ArrayList<>();
		while (resultSet.next()) {
			int id = resultSet.getInt("id");
			String name = resultSet.getString("name");
			int seating_rows = resultSet.getInt("seating_rows");
			int seating_cols = resultSet.getInt("seating_cols");
			Hall hall = new Hall(id, name, seating_rows, seating_cols);
			halls.add(hall);
		}
		return halls;
	}

}

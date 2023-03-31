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
 * Hall class
 * @author Sylvia Espina C0866311
 * @author Mufida Andi C0864756
 * @author Jenil Shivamkumar Varma C0870543
 * @author Tich Vu Lu C0861736
 * @author Jay Shah C0868053
 */
public class Hall {
	/*----------- Fields ----------- */
	private int id;
	private String name;
	private int seatingRows;
	private int seatingCols;
	private String hidenSeats;
	
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
	/**
	 * @return the hidenSeats
	 */
	public String getHidenSeats() {
		return hidenSeats;
	}
	
	/**
	 * @param hidenSeats the hidenSeats to set
	 */
	public void setHidenSeats(String hidenSeats) {
		this.hidenSeats = hidenSeats;
	}
	/**
	 * @param id the id to set
	 */
	public Hall(int id) {
		this.id = id;
	}

	/*----------- Constructors ----------- */
	/**
	 * Constructor with fields
	 * @param id
	 * @param name
	 * @param seatingRows
	 * @param seatingCols
	 * @param hidenSeats
	 */
	public Hall(int id, String name, int seatingRows, int seatingCols, String hidenSeats) {
		this.id = id;
		this.name = name;
		this.seatingRows = seatingRows;
		this.seatingCols = seatingCols;
		this.hidenSeats = hidenSeats;
	}
	
	/**
	 * Constructor with fields
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

	/**
	 * List all Halls
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
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
	/**
	 * Check if it is a valid hall
	 * @param halls
	 * @param selectedHall
	 * @return
	 */
	public static boolean checkHallExist(ArrayList<Hall> halls, int selectedHall) {
		for (Hall hall : halls) {
			if(hall.getId() == selectedHall) {
				return true;
			}
		}
		return false;
	}

}

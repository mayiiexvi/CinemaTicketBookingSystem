/**
 * 
 */
package common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Ticket class
 * @author Sylvia Espina C0866311
 * @author Mufida Andi C0864756
 * @author Jenil Shivamkumar Varma C0870543
 * @author Tich Vu Lu C0861736
 * @author Jay Shah C0868053
 */
public class Ticket {
	/*----------- Fields ----------- */
	private int id;
	private User user;
	private Showtime showtime;
	private int seat_row;
	private int seat_col;
	private String seatCode;
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
	 * @return the user
	 */
	public User getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}
	/**
	 * @return the showtime
	 */
	public Showtime getShowtime() {
		return showtime;
	}
	/**
	 * @param showtime the showtime to set
	 */
	public void setShowtime(Showtime showtime) {
		this.showtime = showtime;
	}
	/**
	 * @return the seat_row
	 */
	public int getSeat_row() {
		return seat_row;
	}
	/**
	 * @param seat_row the seat_row to set
	 */
	public void setSeat_row(int seat_row) {
		this.seat_row = seat_row;
	}
	/**
	 * @return the seat_col
	 */
	public int getSeat_col() {
		return seat_col;
	}
	/**
	 * @param seat_col the seat_col to set
	 */
	public void setSeat_col(int seat_col) {
		this.seat_col = seat_col;
	}
	
	/**
	 * @return the seatCode
	 */
	public String getSeatCode() {
		return seatCode;
	}
	/**
	 * @param seatCode the seatCode to set
	 */
	public void setSeatCode(String seatCode) {
		this.seatCode = seatCode;
	}
	/*----------- Constructors ----------- */
	/**
	 * Default constructor
	 */
	public Ticket() {
	}
	
	/**
	 * Constructor with fields
	 * @param user
	 * @param showtime
	 */
	public Ticket(User user, Showtime showtime, String seatCode) {
		this.user = user;
		this.showtime = showtime;
		this.seatCode = seatCode;
	}
	/**
	 * Constructor with fields
	 * @param id
	 * @param user
	 * @param showtime
	 * @param seat_row
	 * @param seat_col
	 */
	public Ticket(int id, User user, Showtime showtime, int seat_row, int seat_col) {
		this.id = id;
		this.user = user;
		this.showtime = showtime;
		this.seat_row = seat_row;
		this.seat_col = seat_col;
	}
	/*----------- Methods ----------- */
	
	/**
	 * Insert a new ticket into database
	 * @param connection
	 * @param ticket
	 * @return
	 * @throws SQLException
	 */
	public static Ticket insert(Connection connection, Ticket ticket) throws SQLException {
		String query = "INSERT INTO tickets (user_id, showtime_id, seatCode) VALUES (?, ?, ?)";
		PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		statement.setInt(1, ticket.getUser().getId());
		statement.setInt(2, ticket.getShowtime().getId());
		statement.setString(3, ticket.getSeatCode());
		int affectedRows = statement.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Creating ticket failed, no rows affected.");
        }
        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
            	ticket.setId(generatedKeys.getInt(1));
            }
            else {
                throw new SQLException("Creating ticket failed, no ID obtained.");
            }
        }
		return ticket;
	}
	

	/**
	 * Check if a seat is booked
	 * @param seatsBooked
	 * @param seatCode
	 * @return
	 */
	public static boolean isBookedSeat(ArrayList<Ticket> seatsBooked, String seatCode) {
		for (Ticket ticket : seatsBooked) {
			if(ticket.getSeatCode().equals(seatCode)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * List all tickets by showtime, get more user information
	 * @param connection
	 * @param showtimeID
	 * @return
	 * @throws SQLException
	 */
	public static ArrayList<Ticket> getTicketsByShowTimeID(Connection connection, int showtimeID) throws SQLException{
		ArrayList<Ticket> tickets = new ArrayList<>();
		String query = "SELECT A.ID, A.USER_ID, A.SEATCODE,\r\n"
				+ "B.FIRST_NAME, B.LAST_NAME, B.EMAIL, B.PHONE FROM TICKETS A INNER JOIN USERS B ON A.USER_ID = B.ID\r\n"
				+ "WHERE A.SHOWTIME_ID = ?";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setInt(1, showtimeID);
		ResultSet resultSet = statement.executeQuery();
		while (resultSet.next()) {
			Ticket ticket = new Ticket();
			User user = new User(resultSet.getInt("USER_ID"), resultSet.getString("FIRST_NAME"), resultSet.getString("LAST_NAME"), resultSet.getString("EMAIL"), resultSet.getString("PHONE"));
			ticket.setUser(user);
			ticket.setId(resultSet.getInt("ID"));
			ticket.setSeatCode(resultSet.getString("SEATCODE"));
			tickets.add(ticket);
		}
		return tickets;
	}
	
	/**
	 * Display a list of tickets in list format
	 * @param tickets
	 */
	public static void displayTickets(ArrayList<Ticket> tickets) {

        // Display the list of tickets
        System.out.format("%-10s %-12s %-10s %-15s %-15s %-15s %-25s\n",
                "Ticket ID", "Seat Number", "User ID", "First Name", "Last Name", "Phone", "Email");
        System.out.println("--------------------------------------------------------------------------------------------");

        for (Ticket ticket : tickets) {
            System.out.format("%-10d %-12s %-10d %-15s %-15s %-15s %-25s\n",
                    ticket.getId(), ticket.getSeatCode(), ticket.getUser().getId(), ticket.getUser().getFirstName(),
                    ticket.getUser().getLastName(), ticket.getUser().getPhone(), ticket.getUser().getEmail());
        }
    }
}

package common;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class Test {

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		Connection connection = DatabaseConnection.getInstance().getConnection();
		//ArrayList<Hall> halls =  Hall.listAll(connection);
		//ArrayList<Showtime> showtimes = Showtime.getAvailableShowtimes(connection);
		
		
		DatabaseConnection.checkAndInitializeDatabase(connection);
		
		int a =1;
	}

}

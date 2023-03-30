/**
 * 
 */
package common;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
/**
 * @author Tich
 *
 */
public class DatabaseConnection {

	private static DatabaseConnection instance;
	private Connection connection;
	private String url = "jdbc:mysql://localhost:3306/cinematicketbookingsystem";
	private String user = "java2";
	private String password = "java2";

	private DatabaseConnection() throws SQLException {
		try {
			//Class.forName("com.mysql.jdbc.Driver");
			this.connection = DriverManager.getConnection(url, user, password);
		} catch (Exception ex) {
			System.out.println("Database Connection Creation Failed : " + ex.getMessage());
		}
	}

	public Connection getConnection() {
		return connection;
	}

	public static DatabaseConnection getInstance() throws SQLException {
		if (instance == null) {
			instance = new DatabaseConnection();
		} else if (instance.getConnection().isClosed()) {
			instance = new DatabaseConnection();
		}

		return instance;
	}
	
	public static void checkAndInitializeDatabase(Connection connection) throws SQLException {
		try {
			DatabaseMetaData meta = connection.getMetaData();
			ArrayList<Table> tables = initializeData();
			for (Table table : tables) {
				boolean isExists = false;
				ResultSet res = meta.getTables(null, "cinematicketbookingsystem", table.getTableName(), new String[] {"TABLE"});
				  while (res.next()) {
				     if(res.getString("TABLE_NAME").equals(table.getTableName())) {
				    	 isExists = true;
				     }
					 break;
				  }
				 if(!isExists) {
		            PreparedStatement statement = connection.prepareStatement(table.getSqlCreate());
		            statement.executeUpdate();
		            System.out.println(table + " table created successfully!");
		            for (String sql : table.getSqls()) {
		            	statement = connection.prepareStatement(sql);
			            statement.executeUpdate();
			            System.out.println("1 row inserted");
					}
				 }
			}
		} catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	public static ArrayList<Table> initializeData(){
		ArrayList<Table> tables = new ArrayList<>();
		Table tbl;
		String tableName;
		String sqlCreate;
		ArrayList<String> sql;
		// users
		tableName = "users";
		sqlCreate = "CREATE TABLE `users` (\r\n"
				+ "  `id` int NOT NULL AUTO_INCREMENT,\r\n"
				+ "  `first_name` varchar(100) DEFAULT NULL,\r\n"
				+ "  `last_name` varchar(100) DEFAULT NULL,\r\n"
				+ "  `user_name` varchar(100) DEFAULT NULL,\r\n"
				+ "  `password` varchar(100) DEFAULT NULL,\r\n"
				+ "  `role` varchar(45) DEFAULT NULL,\r\n"
				+ "  `email` varchar(100) DEFAULT NULL,\r\n"
				+ "  `phone` varchar(100) DEFAULT NULL,\r\n"
				+ "  PRIMARY KEY (`id`)\r\n"
				+ ");\r\n"
				+ "";
		sql= new ArrayList<>();
		sql.add("INSERT INTO `users` (`id`,`first_name`,`last_name`,`user_name`,`password`,`role`,`email`,`phone`) VALUES (1,'Administrator',NULL,'admin','password','ADMIN','admin@gmail.com',NULL);");
		sql.add("INSERT INTO `users` (`id`,`first_name`,`last_name`,`user_name`,`password`,`role`,`email`,`phone`) VALUES (2,'User Test',NULL,'user','password','USER','user@gmail.com','22695245192');");
		tbl = new Table(tableName, sqlCreate, sql);
		tables.add(tbl);
		
		// movies
		tableName = "movies";
		sqlCreate = "CREATE TABLE `movies` (\r\n"
				+ "  `id` int NOT NULL AUTO_INCREMENT,\r\n"
				+ "  `movie_name` varchar(100) DEFAULT NULL,\r\n"
				+ "  `synopsis` varchar(100) DEFAULT NULL,\r\n"
				+ "  `release_date` varchar(100) DEFAULT NULL,\r\n"
				+ "  PRIMARY KEY (`id`)\r\n"
				+ ");";
		sql= new ArrayList<>();
		sql.add("INSERT INTO `movies` (`id`,`movie_name`,`synopsis`,`release_date`) VALUES (1,'John Wick 4','With the price on his head ever increasing, legendary hit man John Wick takes his fight against...','1/1/2024');");
		sql.add("INSERT INTO `movies` (`id`,`movie_name`,`synopsis`,`release_date`) VALUES (2,'Strange Things','In 1980s Indiana, a group of young friends witness supernatural forces and secret government exploit','2/2/2024');");
		tbl = new Table(tableName, sqlCreate, sql);
		tables.add(tbl);
		
		
		// halls
		tableName = "halls";
		sqlCreate = "CREATE TABLE `halls` (\r\n"
				+ "  `id` int NOT NULL AUTO_INCREMENT,\r\n"
				+ "  `name` varchar(100) DEFAULT NULL,\r\n"
				+ "  `seating_rows` int DEFAULT NULL,\r\n"
				+ "  `seating_cols` int DEFAULT NULL,\r\n"
				+ "  `hidenseats` varchar(500) DEFAULT NULL,\r\n"
				+ "  PRIMARY KEY (`id`));";
		sql= new ArrayList<>();
		sql.add("INSERT INTO `halls` (`id`,`name`,`seating_rows`,`seating_cols`,`hidenseats`) VALUES (1,'Galaxy 1',5,16,'A5,A6,A7,A8,A9,A10,A11,B5,C5,D5,E5,B11,C11,D11,E11,D1,E1,D16,E16');");
		sql.add("INSERT INTO `halls` (`id`,`name`,`seating_rows`,`seating_cols`,`hidenseats`) VALUES (2,'Galaxy 2',8,11,'E1,F1,H1,G1,E11,F11,G11,H11,E2,E3,E4,E5,E6,E7,E8,E9,E10');");
		sql.add("INSERT INTO `halls` (`id`,`name`,`seating_rows`,`seating_cols`,`hidenseats`) VALUES (3,'Galaxy 3',12,14,'A4,B4,C4,D4,E4,F4,G4,H4,I4,J4,K4,L4,A11,B11,C11,D11,E11,F11,G11,H11,I11,J11,K11,L11');");
		tbl = new Table(tableName, sqlCreate, sql);
		tables.add(tbl);
		
		// tickets
		tableName = "tickets";
		sqlCreate = "CREATE TABLE `tickets` (\r\n"
				+ "  `id` int NOT NULL AUTO_INCREMENT,\r\n"
				+ "  `user_id` int DEFAULT NULL,\r\n"
				+ "  `showtime_id` int DEFAULT NULL,\r\n"
				+ "  `seat_row` int DEFAULT NULL,\r\n"
				+ "  `seat_col` int DEFAULT NULL,\r\n"
				+ "  `seatCode` varchar(45) DEFAULT NULL,\r\n"
				+ "  PRIMARY KEY (`id`)\r\n"
				+ ") ;";
		sql= new ArrayList<>();
		tbl = new Table(tableName, sqlCreate, sql);
		tables.add(tbl);
				
		// showtime
		tableName = "showtime";
		sqlCreate = "CREATE TABLE `showtime` (\r\n"
				+ "  `id` int NOT NULL AUTO_INCREMENT,\r\n"
				+ "  `movie_id` int DEFAULT NULL,\r\n"
				+ "  `hall_id` int DEFAULT NULL,\r\n"
				+ "  `showtime` datetime DEFAULT NULL,\r\n"
				+ "  `price` decimal(8,2) DEFAULT NULL,\r\n"
				+ "  PRIMARY KEY (`id`),\r\n"
				+ "  KEY `movie_id` (`movie_id`),\r\n"
				+ "  CONSTRAINT `showtime_ibfk_1` FOREIGN KEY (`movie_id`) REFERENCES `movies` (`id`)\r\n"
				+ ");";
		sql= new ArrayList<>();
		sql.add("INSERT INTO `showtime` (`id`,`movie_id`,`hall_id`,`showtime`,`price`) VALUES (1,1,1,DATE_ADD(NOW(), INTERVAL 3 HOUR),60.00);");
		sql.add("INSERT INTO `showtime` (`id`,`movie_id`,`hall_id`,`showtime`,`price`) VALUES (2,1,2,DATE_ADD(NOW(), INTERVAL 4 HOUR),30.00);");
		sql.add("INSERT INTO `showtime` (`id`,`movie_id`,`hall_id`,`showtime`,`price`) VALUES (3,2,1,DATE_ADD(NOW(), INTERVAL 4 HOUR),25.00);");
		sql.add("INSERT INTO `showtime` (`id`,`movie_id`,`hall_id`,`showtime`,`price`) VALUES (4,2,3,'2023-05-10 21:10:00',98.00);");
		tbl = new Table(tableName, sqlCreate, sql);
		tables.add(tbl);
		
		// seatreservation
//		tableName = "seatreservation";
//		sqlCreate = "CREATE TABLE `seatreservation`(\r\n"
//				+ "	`id` int NOT NULL AUTO_INCREMENT,\r\n"
//				+ "    `movie_id` int NOT NULL,\r\n"
//				+ "    `movie_time` datetime,\r\n"
//				+ "    `seat_number` varchar(5) NOT NULL,\r\n"
//				+ "    `reserved` boolean NOT NULL DEFAULT false,\r\n"
//				+ "    PRIMARY KEY (`id`)\r\n"
//				+ ");";
//		sql= new ArrayList<>();
//		tbl = new Table(tableName, sqlCreate, sql);
//		tables.add(tbl);
		
		return tables;
	}

}

class Table {
	public String tableName;
	public String sqlCreate;
	public ArrayList<String> sqls;
	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getSqlCreate() {
		return sqlCreate;
	}

	public void setSqlCreate(String sqlCreate) {
		this.sqlCreate = sqlCreate;
	}

	public ArrayList<String> getSqls() {
		return sqls;
	}

	public void setSqls(ArrayList<String> sqls) {
		this.sqls = sqls;
	}

	Table(String tableName, String sqlCreate, ArrayList<String> sqls){
		this.tableName = tableName;
		this.sqlCreate = sqlCreate;
		this.sqls = sqls;
	}
}

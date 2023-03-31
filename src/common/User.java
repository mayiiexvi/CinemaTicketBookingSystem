/**
 * 
 */
package common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class User {
	/**
	 * User class
	 * @author Sylvia Espina C0866311
	 * @author Mufida Andi C0864756
	 * @author Jenil Shivamkumar Varma C0870543
	 * @author Tich Vu Lu C0861736
	 * @author Jay Shah C0868053
	 */
	/*----------- Fields ----------- */
	private int id;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String role;
	private String email;
	private String phone;
	
	
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
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}
	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	/*----------- Constructors ----------- */
	/**
	 * Default constructor
	 */
	public User() {}
	
	
	/**
	 * Constructor with fields
	 * @param firstName
	 * @param lastName
	 * @param role
	 * @param email
	 * @param phone
	 */
	public User(String firstName, String lastName, String role, String email,
			String phone) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
		this.email = email;
		this.phone = phone;
	}
	/**
	 * Constructor with fields
	 * @param id
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param phone
	 */
	public User(int id, String firstName, String lastName, String email, String phone) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
		this.email = email;
		this.phone = phone;
	}

	/**
	 * Check if it is a valid credential user
	 * @param connection
	 * @param username
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	public static User isValidCredentials(Connection connection, String username, String password) throws SQLException {
		String query = "SELECT * FROM users WHERE user_name = ? AND password = ?";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(1, username);
		statement.setString(2, password);
		ResultSet resultSet = statement.executeQuery();
		
		ArrayList<User> listUsers = new ArrayList<User>();
		while (resultSet.next()) {
			User user = new User();
			user.setId(resultSet.getInt("id"));
			user.setFirstName(resultSet.getString("first_name"));
			user.setLastName(resultSet.getString("last_name"));
			user.setUsername(resultSet.getString("user_name"));
			user.setRole(resultSet.getString("role"));
			user.setEmail(resultSet.getString("email"));
			user.setPhone(resultSet.getString("phone"));
			listUsers.add(user);
		}
		if(listUsers.size() == 1) {
			return listUsers.get(0);
		} else { return new User();}
		
	}
	/**
	 * Insert a new user into database
	 * @param connection
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	public static User insert(Connection connection, User user) throws SQLException {
		String query = "INSERT INTO users (first_name, last_name, role, email, phone) VALUES (?, ?, ?, ?, ?)";
		PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		statement.setString(1, user.getFirstName());
		statement.setString(2, user.getLastName());
		statement.setString(3, user.getRole());
		statement.setString(4, user.getEmail());
		statement.setString(5, user.getPhone());
		int affectedRows = statement.executeUpdate();
        if (affectedRows == 0) {
            throw new SQLException("Creating user failed, no rows affected.");
        }
        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getInt(1));
            }
            else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        }
		return user;
	}
}

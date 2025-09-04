package bankingapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class DatabaseManager implements DataAccess{
	
	private Connection db;
	private String db_url, db_username, db_password;
	private Map<String,User> users;
	
	//Query Statements
	private String getUserTable = "SELECT * From users";
	private String putUser = "INSERT into users (email, password) values(?,?)";
	private String updateTransactionList = "INSERT into transactions(user_id, transaction) values((SELECT user_id FROM users WHERE email=?),?)";
	private String updateBalance= "UPDATE users SET balance = ? WHERE email=?";
	
	
	public DatabaseManager(String url, String user, String pass) throws SQLException {
		db_url = url;
		db_username = user;
		db_password = pass;
		db = DriverManager.getConnection(db_url,db_username,db_password);
		Statement statement = db.createStatement();
		ResultSet rs = statement.executeQuery(getUserTable);
		users = new HashMap<>();
		while(rs.next()) {
			System.out.println("email:"+rs.getString("email")+", password:"+rs.getString("password"));
			User u = new User(rs.getString("email"),rs.getString("password"));
			users.put(rs.getString("email"), u);
		}
		rs.close();
		statement.close();
		
	}
	
	public void close() throws SQLException{
		db.close();
	}

	@Override
	public boolean userExists(String username) {
		// TODO Auto-generated method stub
		return users.containsKey(username);
	}

	@Override
	public void addUser(User user) {
		// TODO Auto-generated method stub
		try {
			PreparedStatement statement = db.prepareStatement(putUser);
			statement.setString(1,user.getUsername());  
			statement.setString(2,user.getHashedPassword());
			statement.executeUpdate();
			users.put(user.getUsername(), user);
			System.out.println(user.getUsername()+" Added Successfully!!");
			statement.close();
		}catch(Exception e) {
			System.out.println("Error occured while trying to put user:"+ user.getUsername());
			System.out.println(e.getMessage());
		}
	}

	@Override
	public User getUser(String username) {
		// TODO Auto-generated method stub
		return users.get(username);
	}
	
	
	public void updateUserBalance(User user) {
		try {
			PreparedStatement s = db.prepareStatement(updateBalance);
			s.setDouble(1, user.getBalance());
			s.setString(2, user.getUsername());
			s.executeUpdate();
			System.out.println("Balance of "+user.getUsername()+" updated to "+user.getBalance());		
		}catch(Exception e) {
			System.out.println("Error occured while updating balance.");
			System.out.println(e.getMessage());
		}
	}
	
	public void updateUserTransactionHistory(User user) {
		try {
			//Add a subquery first.
			PreparedStatement s = db.prepareStatement(updateTransactionList);
			s.setString(1, user.getUsername());
			for(String transaction:user.getTransactionHistory()) {
				s.setString(2, transaction);
				s.executeUpdate();
			}
			System.out.println("Transaction History of "+user.getUsername()+" updated.");		
		}catch(Exception e) {
			System.out.println("Error occured while updating Transactions List. "+ user.getUsername());
			System.out.println(e.getMessage());
		}
	}
}

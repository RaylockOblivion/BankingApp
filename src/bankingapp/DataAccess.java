package bankingapp;

public interface DataAccess {
	
	public boolean userExists(String username);
	//change this to return boolean to error handle
	public void addUser(User user);
	public User getUser(String username);
	
	public void updateUserBalance(User user);
	public void updateUserTransactionHistory(User user);
}

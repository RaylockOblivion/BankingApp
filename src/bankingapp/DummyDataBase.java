package bankingapp;

public class DummyDataBase implements DataAccess {

	@Override
	public boolean userExists(String username) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addUser(User user) {
		// TODO Auto-generated method stub

	}

	@Override
	public User getUser(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateUserBalance(User user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateUserTransactionHistory(User user) {
		// TODO Auto-generated method stub
		
	}

}

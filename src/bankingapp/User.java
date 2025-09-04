package bankingapp;
import java.util.ArrayList;

public class User {
	private String username;
	private String hashedPassword;
	private double balance;
	private ArrayList<String> transactionHistory;
	
	public User(String username,String hashedPassword) {
		this.username = username;
		this.hashedPassword = hashedPassword;
		this.balance=0.0;
		this.transactionHistory = new ArrayList<>();
		addTransaction("Account created");
	}
	
	//Method to deposit money
	public void deposit(double amt) {
		balance += amt;
		addTransaction("Deposited: $"+amt);
	}
	
	//Method to withdraw money
	public boolean withdraw(double amt) {
		if(balance >= amt) {
			balance -= amt;
			addTransaction("Withdrew: $"+amt);
			return true;
		}
		else return false; //Insufficient funds
	}
	
	//Method to add transactions to history
	public void addTransaction(String description) {
		transactionHistory.add(description);
	}
	
	//Method to display transaction History
	public void showTransactionHistory() {
		System.out.println("\nTransaction History for "+ username + ":");
		for (String transaction: transactionHistory) {
			System.out.println(transaction);
		}
	}
	
	public ArrayList<String> getTransactionHistory(){
		return transactionHistory;
	}
	
	//Method to transfer funds to another user
	public void transfer(User recipient, double amt) {
		if(withdraw(amt)) {
			recipient.deposit(amt);
			addTransaction("Transferred: $"+amt+" to "+ recipient.getUsername());
			recipient.addTransaction("Receieved: $"+amt+" from "+ username);
		}
	}

	//Getters
	public String getUsername() {
		return username;
	}
	
	public String getHashedPassword() {
		return hashedPassword;
	}
	
	public double getBalance() {
		return balance;
	}
}

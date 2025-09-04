package bankingapp;
import java.util.Scanner;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class BankingSystem {
	//class variables
	private Scanner scanner;
	private DataAccess record;
	

	//Constructor
	public BankingSystem(Scanner sc, DataAccess da) {
		scanner = sc;
		record = da;
	}
	
	//Core Loop
	public void start() {
		while(true) {
			printMainMenu();
			
			int userSelection = scanner.nextInt();
			scanner.nextLine(); //Eat empty line
			
			switch(userSelection) {
			case 1:
				createAccount();
				break;
			case 2:
				User user = login();
				if(user!=null) handleUserOperations(user);
				break;
			case 3:
				System.out.println("Exiting...");
				return;
			default:
				System.out.println("Invalid option. Try again.");
			}
		}
	}
	
	
	private static void printMainMenu() {
		System.out.println();
		System.out.println("******************************");
		System.out.println("*                            *");
		System.out.println("*    Welcome To Ray Bank     *");
		System.out.println("*                            *");
		System.out.println("******************************");
		System.out.println("\t1.Create Account");
		System.out.println("\t2.Login");
		System.out.println("\t3.Exit");
		System.out.print("Choose an option:");
	}
	
	private static void printUserMenu() {
		System.out.println("------------------------------");
		System.out.println("   Account Menu:");
		System.out.println("\t1.Check Balance");
		System.out.println("\t2.Deposit");
		System.out.println("\t3.Withdraw");
		System.out.println("\t4.Transfer");
		System.out.println("\t5.Transaction History");
		System.out.println("\t6.Logout");
		System.out.print("Choose an option:");
	}
	
	//Handle operations for a logged in user
	private void handleUserOperations(User user) {
		while(true) {
			
			printUserMenu();
			
			int option = scanner.nextInt();
			scanner.nextLine(); // Eat empty line
			
			switch(option) {
				case 1:
					System.out.println("Balance: $"+user.getBalance());
					break;
				case 2:
					deposit(user);
					break;
				case 3:
					withdraw(user);
					break;
				case 4:
					transfer(user);
					break;
				case 5:
					user.showTransactionHistory();
					break;
				case 6:
					record.updateUserTransactionHistory(user);
					System.out.println("Logging out...");
					System.out.println("------------------------------");
					return;
				default:
					System.out.println("---------------X--------------");
					System.out.println("Invalid option. Try again.");
			}
		}
	}
	
	//Method to create a new user
	public void createAccount() {
		System.out.println("Enter a username: ");
		String username = scanner.nextLine();
		
		if(record.userExists(username)) {
			System.out.println("Username already exists. Try a different one.");
			return;
		}
		
		System.out.println("Enter a password:");
		String password = scanner.nextLine();
		String hashedPassword = hashPassword(password);
		
		User newUser = new User(username, hashedPassword);
		record.addUser(newUser);
		System.out.println("Account created successfully!");
	}
	
	//Method to authenticate a user
	public User login() {
		System.out.println("Enter your username:");
		String username = scanner.nextLine();
		
		if(!record.userExists(username)) {
			System.out.println("Username does not exist.");
			return null;
		}
		
		System.out.println("Enter your password: ");
		String password = scanner.nextLine();
		String hashedPassword = hashPassword(password);
		
		User user = record.getUser(username);
		if (user.getHashedPassword().equals(hashedPassword)) {
			System.out.println("Login Successful!");
			return user;
		}else System.out.println("Incorrect Password.");
		return null;
	}
	
	//Deposit money into an account
	public void deposit(User user) {
		System.out.println("Enter amount to deposit: ");
		double amt = scanner.nextDouble();
		scanner.nextLine(); //Eat empty line
		user.deposit(amt);
		record.updateUserBalance(user);
		System.out.println("Deposited $"+amt+". New balance $"+user.getBalance());
	}
	
	//Withdraw money from an account
	public void withdraw(User user) {
		System.out.println("Enter amount to withdraw: ");
		double amt = scanner.nextDouble();
		scanner.nextLine(); //Eat empty line
		
		if(user.withdraw(amt)) {
			System.out.println("Withdrew $"+amt+".New balance: $"+user.getBalance());
			record.updateUserBalance(user);
		}else System.out.println("Insufficient funds. Balance: $"+user.getBalance());
	}
	
	//Transfer money between accounts
	public void transfer(User sender) {
		System.out.println("Enter recipient's username: ");
		String recipientUsername = scanner.nextLine();
		
		if(!record.userExists(recipientUsername)) {
			System.out.println("Recipient username not found.");
			return;
		}
		
		User recipient = record.getUser(recipientUsername);
		System.out.println("Enter amount to transfer:");
		double amt = scanner.nextDouble();
		scanner.nextLine(); //Eat new Line
		
		sender.transfer(recipient, amt);
		record.updateUserBalance(sender);
		record.updateUserBalance(recipient);
		record.updateUserTransactionHistory(recipient);
		System.out.println("Transferred $"+amt+" to "+recipientUsername);
	}
	
	//Hash password using SHA-256
	private String hashPassword(String password) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(password.getBytes());
			StringBuilder hexString = new StringBuilder();
			for(byte b:hash) {
				hexString.append(String.format("%02x", b));
			}
			return hexString.toString();
		}
		catch(NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

}

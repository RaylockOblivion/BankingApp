package bankingapp;
import java.util.Scanner;

public class Main {
	

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		
		String db_url= "jdbc:postgresql://localhost:5433/Banking";
		String db_username="postgres";
		String db_password="postray";
		
		DataAccess db;
		try {
			db = new DatabaseManager(db_url,db_username,db_password);
		}catch(Exception e) {
			db = new DummyDataBase();
			System.out.println("Using a dummy Database Object for some reason.");
			System.out.println(e.getMessage());
		}
		BankingSystem bankingSystem = new BankingSystem(scanner, db);
		bankingSystem.start();
		
		scanner.close();
	}
}

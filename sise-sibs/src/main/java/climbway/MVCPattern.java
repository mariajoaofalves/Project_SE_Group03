package climbway;

import java.util.Scanner;

import pt.ulisboa.tecnico.learnjava.bank.domain.Account;
import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.domain.Person;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;

public class MVCPattern {

	public static void main(String[] args) throws BankException, AccountException, ClientException {
		Scanner s = new Scanner(System.in);

		User model = new User();

		UserView view = new UserView();

		UserController controller = new UserController(model, view);

		String input;

		Services services = new Services();

		Sibs sibs = new Sibs(100, services);

		Bank bank = new Bank("CGC");

		Person person1 = new Person("Maria", "Alves", "Street", "967440681");
		Client client1 = new Client(bank, person1, "123456789", 33);

		Person person2 = new Person("João", "Alves", "Street", "966906844");
		Client client2 = new Client(bank, person2, "123456788", 33);

		Person person3 = new Person("Pedro", "Casaleiro", "Street", "964391860");
		Client client3 = new Client(bank, person3, "123456787", 33);

		Person person4 = new Person("Patrícia", "Matos", "Street", "927338098");
		Client client4 = new Client(bank, person4, "123456700", 33);

		Person person5 = new Person("Ana", "Pinto", "Street", "967440682");
		Client client5 = new Client(bank, person5, "123456000", 33);

		String Iban1 = bank.createAccount(Bank.AccountType.CHECKING, client1, 100, 0);
		String Iban2 = bank.createAccount(Bank.AccountType.CHECKING, client2, 100, 0);
		String Iban3 = bank.createAccount(Bank.AccountType.CHECKING, client3, 100, 0);
		String Iban4 = bank.createAccount(Bank.AccountType.CHECKING, client4, 100, 0);
		String Iban5 = bank.createAccount(Bank.AccountType.CHECKING, client5, 100, 0);

		Account account1 = services.getAccountByIban(Iban1);
		Account account2 = services.getAccountByIban(Iban2);
		Account account3 = services.getAccountByIban(Iban3);
		Account account4 = services.getAccountByIban(Iban4);
		Account account5 = services.getAccountByIban(Iban5);

		while (controller.isRunning()) {

			input = s.nextLine();

			controller.setUserInput(input);

			controller.updateView();
		}

		s.close();

	}

}

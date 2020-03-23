package climbway;

import java.util.ArrayList;
import java.util.Scanner;

import pt.ulisboa.tecnico.learnjava.bank.domain.Account;
import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class MVCPattern {

	public Bank bank;

	public static void main(String[] args)
			throws BankException, AccountException, ClientException, SibsException, OperationException {

		Scanner s = new Scanner(System.in);

		User model = new User();

		UserView view = new UserView();

		UserController controller = new UserController(model, view);

		Services services = new Services();

		Sibs sibs = new Sibs(100, services);

		String input;

		Bank bank = new Bank("CGC");

		Client client1 = new Client(bank, "Maria", "Alves", "123456789", "967440681", "Street", 33);
		Client client2 = new Client(bank, "João", "Alves", "123456788", "966906844", "Street", 33);
		Client client3 = new Client(bank, "Pedro", "Casaleiro", "123456787", "964391860", "Street", 33);
		Client client4 = new Client(bank, "Patrícia", "Matos", "123456700", "927338098", "Street", 33);

		String Iban1 = bank.createAccount(Bank.AccountType.CHECKING, client1, 100, 0);
		String Iban2 = bank.createAccount(Bank.AccountType.CHECKING, client2, 100, 0);
		String Iban3 = bank.createAccount(Bank.AccountType.CHECKING, client3, 100, 0);
		String Iban4 = bank.createAccount(Bank.AccountType.CHECKING, client4, 100, 0);

		Account account1 = services.getAccountByIban(Iban1);
		Account account2 = services.getAccountByIban(Iban2);
		Account account3 = services.getAccountByIban(Iban3);
		Account account4 = services.getAccountByIban(Iban4);

		int code1 = model.associateMbway(Iban1, "967440681");
		model.confirmMbway(String.valueOf(code1), "967440681");

		int code2 = model.associateMbway(Iban2, "966906844");
		model.confirmMbway(String.valueOf(code2), "966906844");

		int code3 = model.associateMbway(Iban3, "964391860");
		model.confirmMbway(String.valueOf(code3), "964391860");

		int code4 = model.associateMbway(Iban4, "927338098");
		model.confirmMbway(String.valueOf(code4), "927338098");

		model.transferMbway("967440681", "966906844", "50");

		ArrayList<String> listFriends = new ArrayList<String>();

		listFriends.add("966906844");
		listFriends.add("964391860");
		listFriends.add("927338098");

		System.out.println(model.validateFriends(listFriends));
		System.out.println(model.validateFriendsAccounts(listFriends, 100));

		// System.out.println(services.getAccountByIban(targetIban).getBalance());

		// System.out.println(services.getAccountByIban(sourceIban).getBalance());

		while (controller.isRunning()) {

			input = s.nextLine();

			controller.setUserInput(input);

			controller.updateView();
		}

		s.close();

	}

}

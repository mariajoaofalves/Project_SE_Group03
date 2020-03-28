package climbway;

import java.util.Scanner;

import pt.ulisboa.tecnico.learnjava.bank.domain.Account;
import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
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

		Client client1 = new Client(bank, "Maria", "Alves", "123456789", "967440681", "Street", 33);
		Client client2 = new Client(bank, "João", "Alves", "123456788", "966906844", "Street", 33);
		Client client3 = new Client(bank, "Pedro", "Casaleiro", "123456787", "964391860", "Street", 33);
		Client client4 = new Client(bank, "Patrícia", "Matos", "123456700", "927338098", "Street", 33);
		Client client5 = new Client(bank, "Ana", "Pinto", "123456000", "967440682", "Street", 33);
		Client client6 = new Client(bank, "Francisca", "Candeias", "123450000", "967440683", "Street", 33);
		Client client7 = new Client(bank, "Catarina", "Ferreira", "423456787", "967440684", "Street", 33);
		Client client8 = new Client(bank, "Rita", "Sampaio", "823456700", "967440685", "Street", 33);
		Client client9 = new Client(bank, "Margarida", "Bica", "923456787", "967440686", "Street", 33);
		Client client10 = new Client(bank, "Manuela", "Alves", "123056788", "967777777", "Street", 33);
		Client client11 = new Client(bank, "Paulo", "Afredo", "123456777", "966666666", "Street", 33);
		Client client12 = new Client(bank, "Pedro", "Casaleiro", "123456666", "964444444", "Street", 33);
		Client client13 = new Client(bank, "Patrícia", "Silva", "123453000", "927777777", "Street", 33);
		Client client14 = new Client(bank, "Patocina", "Matos", "123456555", "921111111", "Street", 33);
		Client client15 = new Client(bank, "Paula", "Bernardete", "123456111", "927333333", "Street", 33);
		Client client16 = new Client(bank, "Paula", "Bernardete", "100000001", "927321113", "Street", 33);

		String Iban1 = bank.createAccount(Bank.AccountType.CHECKING, client1, 100, 0);
		String Iban2 = bank.createAccount(Bank.AccountType.CHECKING, client2, 100, 0);
		String Iban3 = bank.createAccount(Bank.AccountType.CHECKING, client3, 100, 0);
		String Iban4 = bank.createAccount(Bank.AccountType.CHECKING, client4, 100, 0);
		String Iban5 = bank.createAccount(Bank.AccountType.CHECKING, client5, 100, 0);
		String Iban6 = bank.createAccount(Bank.AccountType.CHECKING, client6, 100, 0);
		String Iban7 = bank.createAccount(Bank.AccountType.CHECKING, client7, 100, 0);
		String Iban8 = bank.createAccount(Bank.AccountType.CHECKING, client8, 100, 0);
		String Iban9 = bank.createAccount(Bank.AccountType.CHECKING, client9, 100, 0);
		String Iban10 = bank.createAccount(Bank.AccountType.CHECKING, client10, 100, 0);
		String Iban11 = bank.createAccount(Bank.AccountType.CHECKING, client11, 100, 0);
		String Iban12 = bank.createAccount(Bank.AccountType.CHECKING, client12, 100, 0);
		String Iban13 = bank.createAccount(Bank.AccountType.CHECKING, client13, 100, 0);
		String Iban14 = bank.createAccount(Bank.AccountType.CHECKING, client14, 100, 0);
		String Iban15 = bank.createAccount(Bank.AccountType.CHECKING, client15, 100, 0);
		String Iban16 = bank.createAccount(Bank.AccountType.CHECKING, client16, 100, 0);

		Account account1 = services.getAccountByIban(Iban1);
		Account account2 = services.getAccountByIban(Iban2);
		Account account3 = services.getAccountByIban(Iban3);
		Account account4 = services.getAccountByIban(Iban4);
		Account account5 = services.getAccountByIban(Iban5);
		Account account6 = services.getAccountByIban(Iban6);
		Account account7 = services.getAccountByIban(Iban7);
		Account account8 = services.getAccountByIban(Iban8);
		Account account9 = services.getAccountByIban(Iban9);
		Account account10 = services.getAccountByIban(Iban10);
		Account account11 = services.getAccountByIban(Iban11);
		Account account12 = services.getAccountByIban(Iban12);
		Account account13 = services.getAccountByIban(Iban13);
		Account account14 = services.getAccountByIban(Iban14);
		Account account15 = services.getAccountByIban(Iban15);
		Account account16 = services.getAccountByIban(Iban16);

		int code1 = model.associateMbway(Iban1, "967440681");
		model.confirmMbway(String.valueOf(code1), "967440681");

		int code2 = model.associateMbway(Iban2, "966906844");
		model.confirmMbway(String.valueOf(code2), "966906844");

		int code3 = model.associateMbway(Iban3, "964391860");
		model.confirmMbway(String.valueOf(code3), "964391860");

		int code4 = model.associateMbway(Iban4, "927338098");
		model.confirmMbway(String.valueOf(code4), "927338098");

		int code5 = model.associateMbway(Iban5, "967440682");
		model.confirmMbway(String.valueOf(code5), "967440682");

		int code6 = model.associateMbway(Iban6, "967440683");
		model.confirmMbway(String.valueOf(code6), "967440683");

		int code7 = model.associateMbway(Iban7, "967440684");
		model.confirmMbway(String.valueOf(code7), "967440684");

		int code8 = model.associateMbway(Iban8, "967440685");
		model.confirmMbway(String.valueOf(code8), "967440685");

		int code9 = model.associateMbway(Iban9, "967440686");
		model.confirmMbway(String.valueOf(code9), "967440686");

		int code10 = model.associateMbway(Iban10, "967777777");
		model.confirmMbway(String.valueOf(code10), "967777777");

		int code11 = model.associateMbway(Iban11, "966666666");
		model.confirmMbway(String.valueOf(code11), "966666666");

		int code12 = model.associateMbway(Iban12, "964444444");
		model.confirmMbway(String.valueOf(code12), "964444444");

		int code13 = model.associateMbway(Iban13, "927777777");
		model.confirmMbway(String.valueOf(code13), "927777777");

		int code14 = model.associateMbway(Iban14, "921111111");
		model.confirmMbway(String.valueOf(code14), "921111111");

		int code15 = model.associateMbway(Iban15, "927333333");
		model.confirmMbway(String.valueOf(code15), "927333333");

//      System.out.println(model.transferMbway("967440681", "966906844", "50"));
//		System.out.println(services.getAccountByIban(Iban16).getBalance());
//		System.out.println(services.getAccountByIban(Iban1).getBalance());

//		model.friends("967440681", "10");
//		model.friends("966906844", "10");
//		model.friends("964391860", "10");
//		model.friends("927338098", "10");
//
//		model.mbwaySplitBill("4", "40");
//
//		System.out.println(services.getAccountByIban(Iban1).getBalance());
//		System.out.println(services.getAccountByIban(Iban2).getBalance());
//		System.out.println(services.getAccountByIban(Iban3).getBalance());
//		System.out.println(services.getAccountByIban(Iban4).getBalance());

		while (controller.isRunning()) {

			input = s.nextLine();

			controller.setUserInput(input);

			controller.updateView();
		}

		s.close();

	}

}

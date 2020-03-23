package climbway;

import java.util.ArrayList;
import java.util.HashMap;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;

public class User {
	String iban;
	String phoneNumber;
	String userInput;
	Bank bank;

	HashMap<String, UserAccount> collection = new HashMap<String, UserAccount>();

	Services services = new Services();
	Sibs sibs = new Sibs(100, services);

	public void exit() {
		System.out.println("You exit the app");
		return;
	}

	public String getUserInput() {
		return this.userInput;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public void setUserInput(String userInput) {
		this.userInput = userInput;
	}

	public int associateMbway(String iban, String phoneNumber) {
		if ((iban != null) && (phoneNumber != null) && iban.length() == 6 && phoneNumber.length() == 9
				&& phoneNumber.matches("[0-9]+")) {
			if ((!collection.containsKey(phoneNumber))) {
				UserAccount userAccount = new UserAccount();
				int max = 999999;
				int min = 100000;
				int code = (int) (Math.random() * (max - min + 1) + min);
				userAccount.setPhoneNumber(phoneNumber);
				userAccount.setIban(iban);
				userAccount.setValidationCode(String.valueOf(code));
				collection.put(phoneNumber, userAccount);
				return code;
			} else {
				return 0;
			}
		} else {
			return 1;
		}
	}

	public boolean confirmMbway(String code, String phoneNumber) {
		if (collection.get(phoneNumber).getValidationCode().equals(code)) {
			return true;
		} else {
			collection.remove(phoneNumber, collection.get(phoneNumber));
			return false;
		}
	}

	public int transferMbway(String sourcePhoneNumber, String targetPhoneNumber, String amount) {
		int transfer = 0;
		if ((sourcePhoneNumber != null) && (targetPhoneNumber != null) && (amount != null)
				&& sourcePhoneNumber.length() == 9 && targetPhoneNumber.length() == 9
				&& sourcePhoneNumber.matches("[0-9]+") && targetPhoneNumber.matches("[0-9]+")
				&& amount.matches("[0-9]+")) {
			if (collection.containsKey(sourcePhoneNumber) && collection.containsKey(targetPhoneNumber)) {
				int amountInt = Integer.parseInt(amount);
				try {
					sibs.transfer(collection.get(sourcePhoneNumber).getIban(),
							collection.get(targetPhoneNumber).getIban(), amountInt);
					transfer = 1; // success transfer
				} catch (Exception e) {
					transfer = 2; // invalid amount
				}
			} else {
				transfer = 3; // phoneNumer is not associated with MBWAY
			}
		} else {
			transfer = 4; // invalid input
		}
		return transfer;
	}

	public boolean validateFriends(ArrayList listFriends) {
		int numberFriends = listFriends.size();
		int i;
		int counter = 0;
		for (i = 0; i < listFriends.size(); i++) {
			if (collection.containsKey(listFriends.get(i))) {
				counter++;
			}
		}
		if (counter == numberFriends) {
			return true;
		} else {
			return false;
			// System.out.println("One of your friends is not associated with MB WAY!");
		}
	}

	public boolean validateFriendsAccounts(ArrayList listFriends, int billAmountInt) {
		int numberFriends = listFriends.size();
		int i;
		int counter = 0;
		for (i = 0; i < listFriends.size(); i++) {
			if ((services.getAccountByIban(collection.get(listFriends.get(i)).getIban()).getBalance()) >= (billAmountInt
					/ (numberFriends + 1))) {
				counter++;
			}
		}
		if (counter == numberFriends) {
			return true;
		} else {
			return false;
			// System.out.println("Oh no! One friend does not have money to pay!");
		}
	}

	public boolean validateUser(String phoneNumber, int billAmountInt) {
		if (collection.containsKey(phoneNumber)
				&& (services.getAccountByIban(collection.get(phoneNumber).getIban()).getBalance() >= billAmountInt)) {
			return true;
		} else {
			return false;
			// System.out.println("Oh no! you are not associated with MB WAY or you do not
			// have money to pay!");
		}
	}

	public ArrayList receiveListFriends(ArrayList list) {
		return list;
	}

	public void mbwaySplitBill(int numberFriends, String billAmount) throws AccountException {
		int billAmountInt = Integer.parseInt(billAmount);
		ArrayList list= receiveListFriends()
		if (numberFriends > list.size()) {
			System.out.println("Oh no! One friend is missing!");
		} else if (numberFriends < list.size()) {
			System.out.println("Oh no! Too many friends!");
		} else {
			int amount = (billAmountInt / (1 + numberFriends));
			if (validateUser(phoneNumber, billAmountInt) && validateFriends(list) == true
					&& validateFriendsAccounts(list, billAmountInt) == true) {
				try {
					services.withdraw(collection.get(phoneNumber).getIban(), billAmountInt);
					int i = 0;
					for (i = 0; i < numberFriends; i++) {
						sibs.transfer(collection.get(phoneNumber).getIban(), collection.get(list.get(i)).getIban(),
								amount);
					}
				} catch (Exception e) {
					System.out.println("Something is wrong. Did you set the bill amount right?");
				}
			}

		}

	}
}

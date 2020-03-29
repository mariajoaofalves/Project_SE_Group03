package climbway;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;

public class User {
	String iban;
	String phoneNumber;
	String userInput;

	HashMap<String, UserAccount> collection = new HashMap<String, UserAccount>();
	HashMap<String, String> friends = new HashMap<String, String>();

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
		if ((iban != null) && (phoneNumber != null) && iban.length() <= 7 && phoneNumber.length() == 9
				&& phoneNumber.matches("[0-9]+")) {
			if (services.getAccountByIban(iban) != null) {
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
				return 2;
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

	public List<Integer> transferMbway(String sourcePhoneNumber, String targetPhoneNumber, String amount) {
		List<Integer> list = new ArrayList<Integer>();
		int transfer = 0;
		int balance = 0;
		if ((sourcePhoneNumber != null) && (targetPhoneNumber != null) && (amount != null)
				&& sourcePhoneNumber.length() == 9 && targetPhoneNumber.length() == 9
				&& sourcePhoneNumber.matches("[0-9]+") && targetPhoneNumber.matches("[0-9]+")
				&& amount.matches("[0-9]+")) {
			if (collection.containsKey(sourcePhoneNumber) && collection.containsKey(targetPhoneNumber)) {
				int amountInt = Integer.parseInt(amount);
				try {
					sibs.transfer(collection.get(sourcePhoneNumber).getIban(),
							collection.get(targetPhoneNumber).getIban(), amountInt);
					sibs.processOperations();
					transfer = 1; // success transfer
					balance = services.getAccountByIban(collection.get(sourcePhoneNumber).getIban()).getBalance();
				} catch (Exception e) {
					transfer = 2; // invalid amount
				}
			} else {
				transfer = 3; // phoneNumer is not associated with MBWAY
			}
		} else {
			transfer = 4; // invalid input
		}
		list.add(transfer);
		list.add(balance);
		return list;
	}

	public int friends(String phoneNumber, String Amount) {

		int AmountInt = Integer.parseInt(Amount);
		int friend = 0;
		if (friends.get(phoneNumber) != null) {
			friend = 1;
		} else if (!collection.containsKey(phoneNumber)) {
			friend = 2;
		} else if ((services.getAccountByIban(collection.get(phoneNumber).getIban()).getBalance()) <= AmountInt) {
			friend = 3;
		} else {
			// according to MBWAY specifications you can only split bill with atmost 14
			// friends. Having the user as the first element in the list, it has the maximum
			// size of 15.
			if (friends.size() < 15) {
				friends.put(phoneNumber, Amount);
				friend = 4;
			} else {
				friend = 5;
			}
		}
		return friend;
	}

	public int mbwaySplitBill(String numberFriends, String billAmount) {
		int billAmountInt = Integer.parseInt(billAmount);
		int numberFriendsInt = Integer.parseInt(numberFriends);
		int split = 0;
		if (numberFriendsInt > friends.size()) {
			split = 1;
		} else if (numberFriendsInt < friends.size()) {
			split = 2;
		} else {
			int total_fAmount = 0;
			for (HashMap.Entry<String, String> entry : friends.entrySet()) {
				int f_amount = Integer.parseInt(entry.getValue()); // returns amount assigned to each friend
				total_fAmount += f_amount;
			}
			if (total_fAmount == billAmountInt) {
				try {
					for (HashMap.Entry<String, String> entry : friends.entrySet()) {
						int f_amount = Integer.parseInt(entry.getValue());
						services.withdraw(collection.get(entry.getKey()).getIban(), f_amount);
					}
					split = 3; // withdraw was successful in every friends accounts
				} catch (Exception e) {
					split = 4; // one of the withdraws was unsuccessful
				}
			} else {
				split = 5; // total_fAmount != billAmount
			}
		}
		return split;
	}
}

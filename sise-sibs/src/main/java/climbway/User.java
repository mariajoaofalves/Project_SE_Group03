package climbway;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;

public class User {
	String iban;
	String phoneNumber;
	String userInput;

	HashMap<String, UserAccount> collection = new HashMap<String, UserAccount>();
	ArrayList<Pair<String, Integer>> friends = new ArrayList<Pair<String, Integer>>();

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

	public int friends(String phoneNumber, String Amount) {

		int AmountInt = Integer.parseInt(Amount);
		int friend = 0;
		if (!collection.containsKey(phoneNumber)) {
			friend = 1;
		} else if ((services.getAccountByIban(collection.get(phoneNumber).getIban()).getBalance()) <= AmountInt) {
			friend = 2;
		} else {
			// according to MBWAY specifications you can only split bill atmost with 14
			// friends . The first element of the list is the user, therefore the
			// list has the maximum size of 15.
			if (friends.size() < 15) {
				friends.put(phoneNumber, Amount);
				friend = 3;
			} else {
				friend = 4;
			}
		}
		return friend;
	}

	public void mbwaySplitBill(String numberFriends, String billAmount) {
		int billAmountInt = Integer.parseInt(billAmount);
		int numberFriendsInt = Integer.parseInt(numberFriends);
		Set<String> friendsKeys = friends.keySet();
		if (numberFriendsInt > friends.size()) {
			System.out.println("Oh no! One friend is missing!");
		} else if (numberFriendsInt < friends.size()) {
			System.out.println("Oh no! Too many friends!");
		} else {
			int i = 0;
			for (i = 0; i < friendsKeys.size(); i++) {
				try {
					services.withdraw(collection.get(friendsKeys[i]).getIban(), friends.get());
				} catch (Exception e) {
				}

			}
		}
	}

//	services.withdraw(collection.get(phoneNumber).getIban(),billAmountInt);
//
//	int i = 0;for(i=0;i<numberFriendsInt;i++)
//	{
//		sibs.transfer(collection.get(phoneNumber).getIban(), collection.get(friends.get(i)).getIban(), amount);
//	}}catch(
//	Exception e)
//	{
//		System.out.println("Something is wrong. Did you set the bill amount right?");

}

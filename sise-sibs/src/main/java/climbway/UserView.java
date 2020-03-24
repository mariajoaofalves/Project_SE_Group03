package climbway;

public class UserView {

	public void printExitMessage() {
		System.out.println("You exited MB WAY. See you soon!");
	}

	public void printAssociateUserMbway(int code) {
		if (code == 0) {
			System.out.println("Your phone number is already registered in MB WAY!");
		} else if (code == 1) {
			System.out.println("The phone number or Iban given are invalids. Please try again!");
		} else {
			System.out.println("Your code: " + code + " (don´t share this with anyone)!");
		}
	}

	public void printConfirmUserMbway(boolean confirm) {
		if (confirm == true) {
			System.out.println("MBWAY association confirmed successfully!");
		} else {
			System.out.println("Wrong confirmation code. Try association again!");

		}
	}

	public void printTransferUserMbway(int transfer) {
		if (transfer == 1) {
			System.out.println("Transfer performed successfully!");
		} else if (transfer == 2) {
			System.out.println("Not enough money on the source account!");
		} else if (transfer == 3) {
			System.out.println("Wrong target or source phone number. Phone number(s) isn´t associated with MB WAY!");
		} else if (transfer == 4) {
			System.out.println("The phone numbers or amount given are invalids. Please try again!");
		}
	}

	public void printFriendsUser(int friend) {
		if (friend == 1) {
			System.out.println("This friend is already listed, please try another friend");
		} else if (friend == 2) {
			System.out.println("This friend is not associated with MB WAY. Notify your friend!");
		} else if (friend == 3) {
			System.out.println("Oh no! Your friend does not have money to pay. Notify your friend!");
		} else if (friend == 4) {
			System.out.println("Your friend was added to the list to split the bill!");
		} else if (friend == 5) {
			System.out.println("Oh no! You can not add more friends.");
		}
	}

	public void printMbwayUserSplitBill(int split) {
		if (split == 1) {
			System.out.println("Oh no! One friend is missing to split this bill!");
		} else if (split == 2) {
			System.out.println("Oh no! Too many friends were provided in your split-list!");
		} else if (split == 3) {
			System.out.println("Your bill was successfully splitted");
		} else if (split == 4) {
			System.out.println("One of the withdraws was not performed!");
		} else if (split == 5) {
			System.out.println("Something is wrong... Did you set the bill amount right?");
		}
	}

}

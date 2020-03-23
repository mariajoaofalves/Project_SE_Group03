package climbway;

import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;

public class UserController {

	private User model;
	private UserView view;

	private Services services;
	private Sibs sibs;
	private int nrOperations = 100;
	private boolean isRunning;

	public Boolean isRunning() {
		return this.isRunning;
	}

	private String[] processInput(String input) {
		return input.split(" ");
	}

	public void stopRunning() {
		this.isRunning = false;
	}

	public String getUserInput() {
		return model.getUserInput();
	}

	public void setUserInput(String userInput) {
		model.setUserInput(userInput);
	}

	public UserController(User model, UserView view) {
		this.model = model;
		this.view = view;

		this.isRunning = true;
		this.services = new Services();
		this.sibs = new Sibs(nrOperations, this.services);
	}

	private void associateUserMbway(String[] userArgs) {
		String iban = userArgs[1];
		String phoneNumber = userArgs[2];

		view.printAssociateUserMbway(model.associateMbway(iban, phoneNumber));
	}

	private void confirmUserMbway(String[] userArgs) {
		String code = userArgs[1];
		String phoneNumber = userArgs[2];

		view.printConfirmUserMbway(model.confirmMbway(code, phoneNumber));

	}

	private void transferUserMbway(String[] userArgs) {
		String sourcePhoneNumber = userArgs[1];
		String targetPhoneNumber = userArgs[2];
		String amount = userArgs[3];

		view.printTransferUserMbway(model.transferMbway(sourcePhoneNumber, targetPhoneNumber, amount));

	}

	private void friendsUser(String[] userArgs) {
		String phoneNumber = userArgs[1];
		String Amount = userArgs[2];
		view.printFriendsUser(model.friends(phoneNumber, Amount));
	}

//	private void mbwayUserSplitBill(String[] userArgs) {
//		String numberFriends = userArgs[1];
//		String billAmount = userArgs[2];
//		ArrayList<String> friends = new ArrayList<String>();
//		while ( || (!userArgs.equals("splitbill-mbway"))) {
//			String phoneNumber = userArgs[3:];
//			friends.add(phoneNumber);
//		}
//		view.printMbwayUserSplitBill(model.mbwaySplitBill(numberFriends, billAmount, friends));
//	}

	public void updateView() {
		String[] userArgs = this.processInput(this.getUserInput());

		String command = userArgs[0];

		switch (command) {
		case "exit":
			view.printExitMessage();
			this.stopRunning();
			break;

		case "associate-mbway":
			associateUserMbway(userArgs);
			break;

		case "confirm-mbway":
			confirmUserMbway(userArgs);
			break;

		case "transfer-mbway":
			transferUserMbway(userArgs);
			break;

		case "friends-mbway":
			friendsUser(userArgs);
			break;

		}

	}
}

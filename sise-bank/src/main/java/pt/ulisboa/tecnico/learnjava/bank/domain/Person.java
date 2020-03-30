package pt.ulisboa.tecnico.learnjava.bank.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;

public class Person {

	private final String firstName;
	private final String lastName;
	private final String address;
	private final String phoneNumber;

	public Person(String firstName, String lastName, String address, String phoneNumber) throws ClientException {

		checkParameters(phoneNumber);

		this.firstName = firstName;
		this.lastName = lastName;
		this.address = address;
		this.phoneNumber = phoneNumber;

	}

	private void checkParameters(String phoneNumber) throws ClientException {
		if (phoneNumber.length() != 9 || !phoneNumber.matches("[0-9]+")) {
			throw new ClientException();
		}
	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public String getPhoneNumber() {
		return this.phoneNumber;
	}

	public String getAddress() {
		return this.address;
	}

}

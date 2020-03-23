package climbway;

public class UserAccount {

	public String iban;
	public String validationCode;
	public String phoneNumber;

	public UserAccount() {
		this.phoneNumber = "";
		this.iban = "";
		this.validationCode = "";
	}

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getValidationCode() {
		return validationCode;
	}

	public void setValidationCode(String validationCode) {
		this.validationCode = validationCode;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}

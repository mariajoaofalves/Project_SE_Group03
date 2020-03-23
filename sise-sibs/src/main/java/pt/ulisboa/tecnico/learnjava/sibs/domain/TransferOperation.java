package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class TransferOperation extends Operation {

	Services services;

	private String sourceIban;
	private String targetIban;
	private String state;
	private final int value;

	public TransferOperation(String sourceIban, String targetIban, int value) throws OperationException {
		super(Operation.OPERATION_TRANSFER, value);

		if (invalidString(sourceIban) || invalidString(targetIban)) {
			throw new OperationException();
		}

		this.sourceIban = sourceIban;
		this.targetIban = targetIban;
		this.state = "Registered";
		this.value = value;
		this.services = new Services();
	}

	private boolean invalidString(String name) {
		return name == null || name.length() == 0;
	}

	@Override
	public int commission() {
		return (int) Math.round(super.commission() + getValue() * 0.05);
	}

	public String getSourceIban() {
		return this.sourceIban;
	}

	public String getTargetIban() {
		return this.targetIban;
	}

	public String getState() {
		return this.state;
	}

	@Override
	public int getValue() {
		return this.value;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void process() throws SibsException, OperationException, AccountException {
		if (getState().equals("Registered")) {
			services.withdraw(sourceIban, value);
			setState("Withdrawn");
		} else if (getState().equals("Withdrawn")) {
			if (sourceIban.substring(0, 3).equals(targetIban.substring(0, 3))) {
				services.deposit(targetIban, value);
				setState("Deposited");
			} else {
				int comission = commission();
				services.withdraw(sourceIban, comission);
				services.deposit(targetIban, value);
				setState("Deposited");
			}
			setState("Completed");
		} else if (getState().contentEquals("Cancelled")) {
			return;
		}
	}

	public void cancel() throws AccountException {
		if (getState().equals("Completed")) {
			return;
		} else {
			if (getState().equals("Withdrawn")) {
				services.deposit(sourceIban, value);
			}
			setState("Cancelled");
		}
	}

}

package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import state.Registered;
import state.State;

public class TransferOperation extends Operation {

	Services services;

	private String sourceIban;
	private String targetIban;
	private final int value;
	private State currentState;

	public TransferOperation(String sourceIban, String targetIban, int value) throws OperationException {
		super(Operation.OPERATION_TRANSFER, value);

		if (invalidString(sourceIban) || invalidString(targetIban)) {
			throw new OperationException();
		}
		this.sourceIban = sourceIban;
		this.targetIban = targetIban;
		this.value = value;
		this.services = new Services();
		this.currentState = new Registered();

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

	@Override
	public int getValue() {
		return this.value;
	}

	public Services getServices() {
		return this.services;
	}

	public State getState() {
		return this.currentState;
	}

	public void setState(State state) {
		this.currentState = state;
	}

	public void process(Services services) throws AccountException, OperationException {
		this.currentState.process(this, services);
	}

	public void cancel(Services services) throws AccountException, OperationException {
		this.currentState.cancel(this, services);
	}

}

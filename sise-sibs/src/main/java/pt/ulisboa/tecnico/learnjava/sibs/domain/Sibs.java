package pt.ulisboa.tecnico.learnjava.sibs.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;
import state.Cancelled;
import state.Completed;
import state.Retry;

public class Sibs {
	final Operation[] operations;
	Services services;

	public Sibs(int maxNumberOfOperations, Services services) {
		this.operations = new Operation[maxNumberOfOperations];
		this.services = services;
	}

	public void validateAccountByIban(String sourceIban, String targetIban) throws SibsException {
		if (!this.services.existingAccount(sourceIban) || !this.services.existingAccount(targetIban)
				|| this.services.inactiveAccount(sourceIban) || this.services.inactiveAccount(targetIban)) {
			throw new SibsException();
		}
	}

	public void transfer(String sourceIban, String targetIban, int amount) throws SibsException, OperationException {
		validateAccountByIban(sourceIban, targetIban);
		TransferOperation transferOperation = new TransferOperation(sourceIban, targetIban, amount);
		addOperation(transferOperation);
	}

	public void payment(String targetIban, int amount) throws SibsException, OperationException {
		if (services.existingAccount(targetIban) == true && services.inactiveAccount(targetIban) == false) {
			PaymentOperation paymentOperation = new PaymentOperation(targetIban, amount);
			addOperation(paymentOperation);
		}
	}

	public int addOperation(Operation operation) throws OperationException, SibsException {
		int position = -1;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] == null) {
				position = i;
				break;
			}
		}
		if (position == -1) {
			throw new SibsException();
		}

		this.operations[position] = operation;
		return position;
	}

	public void processOperations() throws AccountException, OperationException {
		for (Operation operation : this.operations) {
			if (operation != null && (operation instanceof TransferOperation)) {
				TransferOperation transferOperation = (TransferOperation) operation;
				if (verifyState(transferOperation)) {
					try {
						transferOperation.process(services);
					} catch (Exception e) {
						if (!(transferOperation.getState() instanceof Retry)) {
							(new Retry()).process(transferOperation, services);
						}
					}
				}
			}
		}
	}

	public boolean verifyState(TransferOperation transferOperation) {
		boolean valid = true;
		if ((transferOperation.getState() instanceof Cancelled) || (transferOperation.getState() instanceof Completed)
				|| (transferOperation.getState() instanceof Error)) {
			valid = false;
		}
		return valid;
	}

	public void cancelOperation(int id) throws SibsException, AccountException, OperationException {
		if (getOperation(id) != null && getOperation(id) instanceof TransferOperation) {
			TransferOperation transferOperation = (TransferOperation) getOperation(id);
			if (verifyState(transferOperation)) {
				transferOperation.cancel(services);
			}
		} else {
			throw new OperationException("Null Position or Operation is not type Transfer");
		}

	}

	public void removeOperation(int position) throws SibsException, OperationException {
		if (position < 0 || position > this.operations.length) {
			throw new SibsException();
		}
		this.operations[position] = null;
	}

	public Operation getOperation(int position) throws SibsException, OperationException {
		if (position < 0 || position > this.operations.length) {
			throw new SibsException();
		}
		return this.operations[position];
	}

	public int getNumberOfOperations() {
		int result = 0;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] != null) {
				result++;
			}
		}
		return result;
	}

	public int getTotalValueOfOperations() {
		int result = 0;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] != null) {
				result = result + this.operations[i].getValue();
			}
		}
		return result;
	}

	public int getTotalValueOfOperationsForType(String type) {
		int result = 0;
		for (int i = 0; i < this.operations.length; i++) {
			if (this.operations[i] != null && this.operations[i].getType().equals(type)) {
				result = result + this.operations[i].getValue();
			}
		}
		return result;
	}
}

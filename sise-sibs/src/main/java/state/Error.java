package state;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class Error implements State {

	@Override
	public void process(TransferOperation wrapper, Services services) throws AccountException, OperationException {
		throw new OperationException("Error is a final state unable to be processed!");

	}

	@Override
	public void cancel(TransferOperation wrapper, Services services) throws AccountException, OperationException {
		throw new OperationException("Error is a final state unable to be cancelled!");
	}

}

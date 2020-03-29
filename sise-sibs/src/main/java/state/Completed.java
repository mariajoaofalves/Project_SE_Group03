package state;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class Completed implements State {

	@Override
	public void process(TransferOperation wrapper) throws OperationException {
		throw new OperationException("Can't process a completed state!");
	}

	@Override
	public void cancel(TransferOperation wrapper) throws AccountException, OperationException {
		throw new OperationException("Can't cancel a completed state!");
	}

}

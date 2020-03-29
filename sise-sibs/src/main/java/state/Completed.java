package state;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class Completed implements State {

	@Override
	public void process(TransferOperation wrapper, Services services) throws OperationException {
		throw new OperationException("Can't process a completed state!");
	}

	@Override
	public void cancel(TransferOperation wrapper, Services services) throws AccountException, OperationException {
		throw new OperationException("Can't cancel a completed state!");
	}

}

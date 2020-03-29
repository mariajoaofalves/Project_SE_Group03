package state;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class Retry implements State {
	private int counterRetries = 3;

	@Override
	public void process(TransferOperation wrapper) throws AccountException, OperationException {
		while (counterRetries > 0) {
			counterRetries--;
			wrapper.process();
		}
		wrapper.setState(new Error());
	}

	@Override
	public void cancel(TransferOperation wrapper) throws AccountException, OperationException {
		wrapper.setState(new Cancelled());
	}

}

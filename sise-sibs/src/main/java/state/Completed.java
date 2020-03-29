package state;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;

public class Completed implements State {

	@Override
	public void process(TransferOperation wrapper) {
		wrapper.setState(new Cancelled());
	}

	@Override
	public void cancel(TransferOperation wrapper) throws AccountException {
		// TODO Auto-generated method stub
	}

}

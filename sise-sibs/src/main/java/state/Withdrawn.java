package state;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;

public class Withdrawn implements State {

	@Override
	public void process(TransferOperation wrapper) throws AccountException {
		wrapper.getServices().deposit(wrapper.getTargetIban(), wrapper.getValue());
		if (wrapper.getSourceIban().substring(0, 3).equals(wrapper.getTargetIban().substring(0, 3))) {
			wrapper.setState(new Completed());
		} else {
			wrapper.setState(new Deposited());
		}

	}

	@Override
	public void cancel(TransferOperation wrapper) throws AccountException {
		wrapper.getServices().deposit(wrapper.getSourceIban(), wrapper.getValue());
		wrapper.setState(new Cancelled());
	}

}

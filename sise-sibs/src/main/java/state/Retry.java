package state;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public class Retry implements State {
	private int attempts = 3;
	private State previousState = null;

	@Override
	public void process(TransferOperation wrapper, Services services) throws AccountException, OperationException {

		if (!(wrapper.getState() instanceof Retry)) {
			this.previousState = wrapper.getState();
			wrapper.setState(this);
		} else {

			if (attempts > 0) {
				keepTrying(wrapper, services);
			} else {
				this.previousState.cancel(wrapper, services);
				wrapper.setState(new Error());
			}
		}
		this.attempts -= 1;
	}

	@Override
	public void cancel(TransferOperation wrapper, Services services) throws AccountException, OperationException {
		this.previousState.cancel(wrapper, services);
	}

	public void keepTrying(TransferOperation wrapper, Services services) {
		try {
			this.previousState.process(wrapper, services);
		} catch (Exception e) {
			//
		}
	}

}

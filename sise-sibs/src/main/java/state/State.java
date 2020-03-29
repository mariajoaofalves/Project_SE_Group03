package state;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;

public interface State {

	void process(TransferOperation wrapper) throws AccountException;

	void cancel(TransferOperation wrapper) throws AccountException;

}

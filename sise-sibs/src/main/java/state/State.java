package state;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;

public interface State {

	void process(TransferOperation wrapper) throws AccountException, OperationException;

	void cancel(TransferOperation wrapper) throws AccountException, OperationException;

}

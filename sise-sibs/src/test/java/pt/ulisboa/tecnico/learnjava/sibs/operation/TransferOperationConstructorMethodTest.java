package pt.ulisboa.tecnico.learnjava.sibs.operation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.sibs.domain.Operation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import state.Registered;

public class TransferOperationConstructorMethodTest {
	String SOURCE_IBAN = "CGDCK1";
	String TARGET_IBAN = "BPICK2";
	int VALUE = 100;

	@Test
	public void success() throws OperationException {
		TransferOperation operation = new TransferOperation(this.SOURCE_IBAN, this.TARGET_IBAN, 100);
		assertEquals(Operation.OPERATION_TRANSFER, operation.getType());
		assertEquals(100, operation.getValue());
		assertEquals(this.SOURCE_IBAN, operation.getSourceIban());
		assertEquals(this.TARGET_IBAN, operation.getTargetIban());
		assertTrue(operation.getState() instanceof Registered);
	}

	@Test(expected = OperationException.class)
	public void nonPositiveValue() throws OperationException {
		new TransferOperation(this.SOURCE_IBAN, this.TARGET_IBAN, 0);
	}

	@Test(expected = OperationException.class)
	public void nullSourceIban() throws OperationException {
		new TransferOperation(null, this.TARGET_IBAN, 100);
	}

	@Test(expected = OperationException.class)
	public void emptySourceIban() throws OperationException {
		new TransferOperation("", this.TARGET_IBAN, 100);
	}

	@Test(expected = OperationException.class)
	public void nullTargetIban() throws OperationException {
		new TransferOperation(this.SOURCE_IBAN, null, 100);
	}

	@Test(expected = OperationException.class)
	public void emptyTargetIban() throws OperationException {
		new TransferOperation(this.SOURCE_IBAN, "", 100);
	}

}

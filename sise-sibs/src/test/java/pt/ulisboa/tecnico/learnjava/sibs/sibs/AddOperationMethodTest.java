package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Operation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.PaymentOperation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class AddOperationMethodTest {
	private static final String SOURCE_IBAN = "SourceIban";
	private static final String TARGET_IBAN = "TargetIban";
	private static final int VALUE = 100;
	TransferOperation transferOperation;
	PaymentOperation paymentOperation;
	private Sibs sibs;

	@Before
	public void setUp() throws OperationException {
		this.sibs = new Sibs(3, new Services());
		transferOperation = new TransferOperation(SOURCE_IBAN, TARGET_IBAN, VALUE);
		paymentOperation = new PaymentOperation(TARGET_IBAN, 200);
	}

	@Test
	public void success() throws OperationException, SibsException {
		int position = this.sibs.addOperation(transferOperation);
		Operation operation = this.sibs.getOperation(position);
		assertEquals(1, this.sibs.getNumberOfOperations());
		assertTrue(operation instanceof TransferOperation);
		assertEquals(VALUE, operation.getValue());
	}

	@Test
	public void successWithDelete() throws OperationException, SibsException {
		int position = this.sibs.addOperation(transferOperation);
		this.sibs.addOperation(transferOperation);
		this.sibs.addOperation(transferOperation);
		this.sibs.removeOperation(position);
		position = this.sibs.addOperation(paymentOperation);
		Operation operation = this.sibs.getOperation(position);
		assertEquals(3, this.sibs.getNumberOfOperations());
		assertTrue(operation instanceof PaymentOperation);
		assertEquals(200, operation.getValue());
	}

	@Test(expected = SibsException.class)
	public void failIsFull() throws OperationException, SibsException {
		this.sibs.addOperation(transferOperation);
		this.sibs.addOperation(transferOperation);
		this.sibs.addOperation(transferOperation);
		this.sibs.addOperation(transferOperation);
	}

	@After
	public void tearDown() {
		this.sibs = null;
	}

}

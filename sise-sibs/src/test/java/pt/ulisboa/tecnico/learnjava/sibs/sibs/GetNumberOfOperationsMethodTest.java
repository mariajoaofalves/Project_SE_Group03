package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.PaymentOperation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class GetNumberOfOperationsMethodTest {
	private static final String TARGET_IBAN = "TargetIban";
	private static final int VALUE = 100;
	PaymentOperation paymentOperation;
	private Sibs sibs;

	@Before
	public void setUp() throws OperationException, SibsException {
		this.sibs = new Sibs(3, new Services());
		paymentOperation = new PaymentOperation(TARGET_IBAN, VALUE);
		this.sibs.addOperation(paymentOperation);
	}

	@Test
	public void success() throws SibsException, OperationException {
		this.sibs.addOperation(paymentOperation);

		assertEquals(2, this.sibs.getNumberOfOperations());
	}

	@After
	public void tearDown() {
		this.sibs = null;
	}

}

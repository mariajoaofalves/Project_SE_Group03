package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.PaymentOperation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class GetTotalValueOfOperationsMethodTest {
	private static final String SOURCE_IBAN = "SourceIban";
	private static final String TARGET_IBAN = "TargetIban";

	private Sibs sibs;
	TransferOperation transferOperation;
	PaymentOperation paymentOperation;

	@Before
	public void setUp() throws OperationException, SibsException {
		this.sibs = new Sibs(3, new Services());
		paymentOperation = new PaymentOperation(TARGET_IBAN, 100);
		this.sibs.addOperation(paymentOperation);
		transferOperation = new TransferOperation(SOURCE_IBAN, TARGET_IBAN, 100);
		this.sibs.addOperation(transferOperation);
	}

	@Test
	public void successTwo() throws SibsException, OperationException {
		assertEquals(200, this.sibs.getTotalValueOfOperations());
	}

	@After
	public void tearDown() {
		this.sibs = null;
	}

}

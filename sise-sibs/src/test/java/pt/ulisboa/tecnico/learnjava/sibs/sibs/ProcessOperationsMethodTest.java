package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Operation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;
import state.Cancelled;
import state.Completed;
import state.Registered;
import state.Withdrawn;

public class ProcessOperationsMethodTest {
	private static final String ADDRESS = "Ave.";
	private static final String PHONE_NUMBER = "987654321";
	private static final String LAST_NAME = "Silva";
	private static final String FIRST_NAME = "Antonio";

	private Sibs sibs;
	private Bank bank;
	private Client sourceClient;
	private Client targetClient;
	private Services services;
	private String sourceIban;
	private String targetIban;

	@Before
	public void setUp() throws OperationException, SibsException, BankException, ClientException, AccountException {

		this.services = new Services();
		this.sibs = new Sibs(100, this.services);
		this.bank = new Bank("CGD");
		this.sourceClient = new Client(this.bank, FIRST_NAME, LAST_NAME, "123456789", PHONE_NUMBER, ADDRESS, 33);
		this.targetClient = new Client(this.bank, FIRST_NAME, LAST_NAME, "123456780", PHONE_NUMBER, ADDRESS, 22);

		this.sourceIban = this.bank.createAccount(Bank.AccountType.CHECKING, this.sourceClient, 1000, 0);
		this.targetIban = this.bank.createAccount(Bank.AccountType.CHECKING, this.targetClient, 1000, 0);
	}

	@Test
	public void success() throws OperationException, SibsException, AccountException {

		sibs.transfer(sourceIban, targetIban, 100);
		TransferOperation transferOperation1 = (TransferOperation) sibs.getOperation(0);
		assertTrue(transferOperation1.getState() instanceof Registered);

		sibs.processOperations();
		assertTrue(transferOperation1.getState() instanceof Withdrawn);
		assertEquals(900, services.getAccountByIban(sourceIban).getBalance());
		assertEquals(1000, services.getAccountByIban(targetIban).getBalance());
		sibs.transfer(sourceIban, targetIban, 200);
		TransferOperation transferOperation2 = (TransferOperation) sibs.getOperation(1);
		sibs.transfer(sourceIban, targetIban, 300);
		assertEquals(3, this.sibs.getNumberOfOperations());
		TransferOperation transferOperation3 = (TransferOperation) sibs.getOperation(2);
		assertTrue(transferOperation2.getState() instanceof Registered);
		assertTrue(transferOperation3.getState() instanceof Registered);

		sibs.processOperations();
		assertTrue(transferOperation1.getState() instanceof Completed);
		assertEquals(1100, services.getAccountByIban(targetIban).getBalance());
		assertTrue(transferOperation2.getState() instanceof Withdrawn);
		assertTrue(transferOperation3.getState() instanceof Withdrawn);
		assertEquals(400, services.getAccountByIban(sourceIban).getBalance());

		sibs.processOperations();
		assertTrue(transferOperation1.getState() instanceof Completed);
		assertTrue(transferOperation2.getState() instanceof Completed);
		assertTrue(transferOperation3.getState() instanceof Completed);
		assertEquals(400, services.getAccountByIban(sourceIban).getBalance());
		assertEquals(1600, services.getAccountByIban(targetIban).getBalance());
		assertEquals(600, this.sibs.getTotalValueOfOperations());
		assertEquals(600, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_TRANSFER));
		assertEquals(0, this.sibs.getTotalValueOfOperationsForType(Operation.OPERATION_PAYMENT));

		sibs.transfer(sourceIban, targetIban, 100);
		TransferOperation transferOperation4 = (TransferOperation) sibs.getOperation(3);
		assertTrue(transferOperation4.getState() instanceof Registered);
		sibs.processOperations();
		assertTrue(transferOperation4.getState() instanceof Withdrawn);
		assertEquals(300, services.getAccountByIban(sourceIban).getBalance());
		transferOperation4.cancel();
		assertTrue(transferOperation4.getState() instanceof Cancelled);
		assertEquals(400, services.getAccountByIban(sourceIban).getBalance());
		sibs.processOperations();
		assertEquals(400, services.getAccountByIban(sourceIban).getBalance());
		assertEquals(1600, services.getAccountByIban(targetIban).getBalance());
		assertTrue(transferOperation4.getState() instanceof Cancelled);

	}

	@After
	public void tearDown() {
		Bank.clearBanks();
	}

}

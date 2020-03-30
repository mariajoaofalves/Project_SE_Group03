package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.domain.Person;
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
import state.Deposited;
import state.Registered;
import state.Withdrawn;

public class ProcessOperationsMethodTest {
	private static final String ADDRESS = "Ave.";
	private static final String PHONE_NUMBER = "987654321";
	private static final String LAST_NAME = "Silva";
	private static final String FIRST_NAME = "Antonio";

	private Sibs sibs;
	private Bank bank;
	private Bank targetBank;
	private Client sourceClient;
	private Client targetClient;
	private Client targetClient1;
	private Services services;
	private String sourceIban;
	private String targetIban;
	private String targetIban1;

	@Before
	public void setUp() throws OperationException, SibsException, BankException, ClientException, AccountException {

		this.services = new Services();
		this.sibs = new Sibs(100, this.services);
		this.bank = new Bank("CGD");
		this.targetBank = new Bank("BPI");
		Person personSource = new Person(FIRST_NAME, LAST_NAME, ADDRESS, PHONE_NUMBER);
		this.sourceClient = new Client(this.bank, personSource, "123456789", 33);
		Person personTarget = new Person(FIRST_NAME, LAST_NAME, ADDRESS, PHONE_NUMBER);
		this.targetClient = new Client(this.bank, personTarget, "123456780", 22);
		this.targetClient1 = new Client(this.targetBank, personTarget, "123333222", 22);

		this.sourceIban = this.bank.createAccount(Bank.AccountType.CHECKING, this.sourceClient, 1000, 0);
		this.targetIban = this.bank.createAccount(Bank.AccountType.CHECKING, this.targetClient, 1000, 0);
		this.targetIban1 = this.targetBank.createAccount(Bank.AccountType.CHECKING, this.targetClient1, 1000, 0);

	}

	@Test
	public void successSameBank() throws OperationException, SibsException, AccountException {
		sibs.transfer(sourceIban, targetIban, 100);
		TransferOperation transferOperation = (TransferOperation) sibs.getOperation(0);
		assertTrue(transferOperation.getState() instanceof Registered);

		sibs.processOperations();
		assertTrue(transferOperation.getState() instanceof Withdrawn);
		assertEquals(900, services.getAccountByIban(sourceIban).getBalance());
		assertEquals(1000, services.getAccountByIban(targetIban).getBalance());
		sibs.processOperations();
		assertTrue(transferOperation.getState() instanceof Completed);
		assertEquals(900, services.getAccountByIban(sourceIban).getBalance());
		assertEquals(1100, services.getAccountByIban(targetIban).getBalance());

	}

	@Test
	public void successDiferentBanks() throws SibsException, OperationException, AccountException {
		sibs.transfer(sourceIban, targetIban1, 50);
		TransferOperation transferOperation = (TransferOperation) sibs.getOperation(0);
		sibs.processOperations();
		assertTrue(transferOperation.getState() instanceof Withdrawn);
		assertEquals(950, services.getAccountByIban(sourceIban).getBalance());
		assertEquals(1000, services.getAccountByIban(targetIban1).getBalance());
		sibs.processOperations();
		assertTrue(transferOperation.getState() instanceof Deposited);
		assertEquals(950, services.getAccountByIban(sourceIban).getBalance());
		assertEquals(1050, services.getAccountByIban(targetIban1).getBalance());
		sibs.processOperations();
		assertTrue(transferOperation.getState() instanceof Completed);
		assertEquals(946, services.getAccountByIban(sourceIban).getBalance());
	}

	@Test
	public void successSameBank3Operations() throws OperationException, SibsException, AccountException {

		sibs.transfer(sourceIban, targetIban, 100);
		TransferOperation transferOperation1 = (TransferOperation) sibs.getOperation(0);
		sibs.processOperations();
		sibs.transfer(sourceIban, targetIban, 200);
		TransferOperation transferOperation2 = (TransferOperation) sibs.getOperation(1);
		sibs.transfer(sourceIban, targetIban, 300);
		TransferOperation transferOperation3 = (TransferOperation) sibs.getOperation(2);

		assertEquals(3, this.sibs.getNumberOfOperations());
		assertTrue(transferOperation1.getState() instanceof Withdrawn);
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
	}

	@Test
	public void successSameBankCancelWithdrawnState() throws OperationException, SibsException, AccountException {
		sibs.transfer(sourceIban, targetIban, 100);
		TransferOperation transferOperation = (TransferOperation) sibs.getOperation(0);
		assertTrue(transferOperation.getState() instanceof Registered);

		sibs.processOperations();
		assertTrue(transferOperation.getState() instanceof Withdrawn);
		assertEquals(900, services.getAccountByIban(sourceIban).getBalance());
		transferOperation.cancel(services);
		assertTrue(transferOperation.getState() instanceof Cancelled);
		assertEquals(1000, services.getAccountByIban(sourceIban).getBalance());

		sibs.processOperations();
		assertEquals(1000, services.getAccountByIban(sourceIban).getBalance());
		assertEquals(1000, services.getAccountByIban(targetIban).getBalance());
		assertTrue(transferOperation.getState() instanceof Cancelled);
	}

	@Test
	public void successDifferentBanksCancelDepositedState() throws OperationException, SibsException, AccountException {

		sibs.transfer(sourceIban, targetIban1, 50);
		TransferOperation transferOperation = (TransferOperation) sibs.getOperation(0);

		sibs.processOperations();
		assertTrue(transferOperation.getState() instanceof Withdrawn);
		assertEquals(950, services.getAccountByIban(sourceIban).getBalance());
		assertEquals(1000, services.getAccountByIban(targetIban1).getBalance());

		sibs.processOperations();
		assertTrue(transferOperation.getState() instanceof Deposited);
		assertEquals(950, services.getAccountByIban(sourceIban).getBalance());
		assertEquals(1050, services.getAccountByIban(targetIban1).getBalance());

		transferOperation.cancel(services);

		assertTrue(transferOperation.getState() instanceof Cancelled);
		assertEquals(1000, services.getAccountByIban(sourceIban).getBalance());
		assertEquals(1000, services.getAccountByIban(targetIban1).getBalance());

		sibs.processOperations();
		assertTrue(transferOperation.getState() instanceof Cancelled);
	}

	@After
	public void tearDown() {
		Bank.clearBanks();
	}

}

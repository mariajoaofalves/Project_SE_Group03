package pt.ulisboa.tecnico.learnjava.sibs.operation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.domain.Client;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.BankException;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;
import state.Cancelled;
import state.Completed;
import state.Deposited;
import state.Registered;
import state.Withdrawn;

public class TransferOperationStateMethodTest {

	Services services;
	Bank sourceBank;
	Bank targetBank;
	Client clientSource;
	Client clientTarget;
	Client client1Source;
	String sourceIban;
	String targetIban;
	String target1Iban;
	Sibs sibs;

	@Before
	public void setUp() throws ClientException, BankException, AccountException {
		this.services = new Services();
		this.sibs = new Sibs(100, this.services);
		this.sourceBank = new Bank("CGD");
		this.targetBank = new Bank("BPI");
		this.clientSource = new Client(this.sourceBank, "Manuel", "Alves", "123456789", "987654321", "Street", 33);
		this.clientTarget = new Client(this.targetBank, "Maria", "Alves", "123456788", "987654321", "Street", 33);
		this.client1Source = new Client(this.sourceBank, "Mariana", "Almeida", "123456781", "987654321", "Street", 33);
		this.sourceIban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, clientSource, 100, 0);
		this.targetIban = this.targetBank.createAccount(Bank.AccountType.CHECKING, clientTarget, 100, 0);
		this.target1Iban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, client1Source, 100, 0);
	}

	@Test
	public void transferOperatioStateDifferentBanks()
			throws OperationException, AccountException, SibsException, BankException, ClientException {
		TransferOperation operation = new TransferOperation(sourceIban, targetIban, 50);
		assertTrue(operation.getState() instanceof Registered);
		operation.process();
		assertTrue(operation.getState() instanceof Withdrawn);
		operation.process();
		assertTrue(operation.getState() instanceof Deposited);
		assertFalse(operation.getState() instanceof Completed);
		operation.process();
		assertTrue(operation.getState() instanceof Completed);
		assertEquals(46, services.getAccountByIban(sourceIban).getBalance());
		assertEquals(150, services.getAccountByIban(targetIban).getBalance());
	}

	@Test
	public void transferOperatioStateSameBank()
			throws OperationException, AccountException, SibsException, BankException, ClientException {
		TransferOperation operation = new TransferOperation(sourceIban, target1Iban, 50);
		assertTrue(operation.getState() instanceof Registered);
		operation.process();
		assertTrue(operation.getState() instanceof Withdrawn);
		operation.process();
		assertFalse(operation.getState() instanceof Deposited);
		assertTrue(operation.getState() instanceof Completed);
		assertEquals(50, services.getAccountByIban(sourceIban).getBalance());
		assertEquals(150, services.getAccountByIban(target1Iban).getBalance());
	}

	@Test
	public void TransferOperationStateWithdrawnCancel() throws OperationException, SibsException, AccountException { // different
																														// Bank
		TransferOperation operation = new TransferOperation(sourceIban, targetIban, 50);
		assertTrue(operation.getState() instanceof Registered);
		operation.process();
		assertTrue(operation.getState() instanceof Withdrawn);
		operation.cancel();
		assertEquals(100, services.getAccountByIban(sourceIban).getBalance());
		assertEquals(100, services.getAccountByIban(targetIban).getBalance());
		assertTrue(operation.getState() instanceof Cancelled);
	}

	@Test
	public void TransferOperationStateDepositedCancel() throws OperationException, SibsException, AccountException { // different
																														// Bank
		TransferOperation operation = new TransferOperation(sourceIban, targetIban, 50);
		assertTrue(operation.getState() instanceof Registered);
		operation.process();
		assertTrue(operation.getState() instanceof Withdrawn);
		operation.process();
		assertTrue(operation.getState() instanceof Deposited);
		operation.cancel();
		assertEquals(100, services.getAccountByIban(sourceIban).getBalance());
		assertEquals(100, services.getAccountByIban(targetIban).getBalance());
		assertTrue(operation.getState() instanceof Cancelled);
	}

	@Test
	public void TransferOperationStateCompletedCancel() throws OperationException, SibsException, AccountException { // different
																														// Bank
		TransferOperation operation = new TransferOperation(sourceIban, targetIban, 50);
		assertTrue(operation.getState() instanceof Registered);
		operation.process();
		operation.process();
		assertTrue(operation.getState() instanceof Deposited);
		operation.process();
		assertTrue(operation.getState() instanceof Completed);
		assertEquals(46, services.getAccountByIban(sourceIban).getBalance());
		assertEquals(150, services.getAccountByIban(targetIban).getBalance());
		try {
			operation.cancel();
			fail();
		} catch (Exception e) {
		}
		assertTrue(operation.getState() instanceof Completed);
		assertFalse(operation.getState() instanceof Cancelled);
		assertEquals(46, services.getAccountByIban(sourceIban).getBalance());
		assertEquals(150, services.getAccountByIban(targetIban).getBalance());
	}

	@After
	public void tearDown() {
		Bank.clearBanks();
	}

}

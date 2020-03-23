package pt.ulisboa.tecnico.learnjava.sibs.operation;

import static org.junit.Assert.assertEquals;

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

public class TransferOperationMethodTest {

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
		this.client1Source = new Client(this.sourceBank, "Mariana", "Casa", "123456781", "987654321", "Street", 33);
		this.sourceIban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, clientSource, 100, 0);
		this.targetIban = this.targetBank.createAccount(Bank.AccountType.CHECKING, clientTarget, 100, 0);
		this.target1Iban = this.sourceBank.createAccount(Bank.AccountType.CHECKING, client1Source, 100, 0);
	}

	@Test
	public void successProcessDifferentBank()
			throws OperationException, AccountException, SibsException, BankException, ClientException {
		TransferOperation operation = new TransferOperation(sourceIban, targetIban, 50);
		operation.process();
		assertEquals("Withdrawn", operation.getState());
		operation.process();
		assertEquals("Completed", operation.getState());
		assertEquals(46, services.getAccountByIban(sourceIban).getBalance());
		assertEquals(150, services.getAccountByIban(targetIban).getBalance());
	}

	@Test
	public void successProcessSameBank()
			throws OperationException, AccountException, SibsException, BankException, ClientException {
		TransferOperation operation = new TransferOperation(sourceIban, target1Iban, 50);
		operation.process();
		assertEquals("Withdrawn", operation.getState());
		operation.process();
		assertEquals("Completed", operation.getState());
		assertEquals(50, services.getAccountByIban(sourceIban).getBalance());
		assertEquals(150, services.getAccountByIban(target1Iban).getBalance());
	}

	@Test
	public void successCancel() throws OperationException, SibsException, AccountException {
		TransferOperation operation = new TransferOperation(sourceIban, targetIban, 50);
		operation.process();
		operation.cancel();
		assertEquals(100, services.getAccountByIban(sourceIban).getBalance());
		assertEquals(100, services.getAccountByIban(targetIban).getBalance());
		assertEquals("Cancelled", operation.getState());
		operation.process();
		assertEquals(100, services.getAccountByIban(sourceIban).getBalance());
		assertEquals(100, services.getAccountByIban(targetIban).getBalance());
		assertEquals("Cancelled", operation.getState());
	}

	@Test
	public void successCancelCompleted() throws OperationException, SibsException, AccountException {
		TransferOperation operation = new TransferOperation(sourceIban, targetIban, 50);
		operation.process();
		operation.process();
		assertEquals("Completed", operation.getState());
		assertEquals(46, services.getAccountByIban(sourceIban).getBalance());
		assertEquals(150, services.getAccountByIban(targetIban).getBalance());
		operation.cancel();
		assertEquals("Completed", operation.getState());
		assertEquals(46, services.getAccountByIban(sourceIban).getBalance());
		assertEquals(150, services.getAccountByIban(targetIban).getBalance());
	}

	@After
	public void tearDown() {
		Bank.clearBanks();
	}

}

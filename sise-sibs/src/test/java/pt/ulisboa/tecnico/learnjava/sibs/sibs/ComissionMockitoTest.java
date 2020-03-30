package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Operation;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;
import state.Completed;
import state.Deposited;

public class ComissionMockitoTest {

	private Sibs sibs;;

	@Test
	public void successSameBank() throws SibsException, OperationException, AccountException {
		Services serviceMock = mock(Services.class);
		Sibs sibsMock = new Sibs(100, serviceMock);

		String sourceIban = "CGCCK1";
		String targetIban = "CGCCK2";

		when(serviceMock.existingAccount(sourceIban)).thenReturn(true);
		when(serviceMock.existingAccount(targetIban)).thenReturn(true);
		when(serviceMock.sameBank(sourceIban, targetIban)).thenReturn(true);

		sibsMock.transfer(sourceIban, targetIban, 100);
		sibsMock.processOperations();
		sibsMock.processOperations();

		verify(serviceMock, times(1)).withdraw(sourceIban, 100);
		verify(serviceMock, times(1)).deposit(targetIban, 100);

		assertTrue(((TransferOperation) sibsMock.getOperation(0)).getState() instanceof Completed);

		assertEquals(1, sibsMock.getNumberOfOperations());
		assertEquals(100, sibsMock.getTotalValueOfOperations());
		assertEquals(100, sibsMock.getTotalValueOfOperationsForType(Operation.OPERATION_TRANSFER));
		assertEquals(0, sibsMock.getTotalValueOfOperationsForType(Operation.OPERATION_PAYMENT));
	}

	@Test
	public void successDiferentBanks() throws AccountException, SibsException, OperationException {
		Services serviceMock = mock(Services.class);
		Sibs sibsMock = new Sibs(100, serviceMock);
		String sourceIban = "CGCCK1";
		String targetIban2 = "BCPCK3";

		when(serviceMock.existingAccount(sourceIban)).thenReturn(true);
		when(serviceMock.existingAccount(targetIban2)).thenReturn(true);
		when(serviceMock.sameBank(sourceIban, targetIban2)).thenReturn(false);

		sibsMock.transfer(sourceIban, targetIban2, 50);
		sibsMock.processOperations();
		sibsMock.processOperations();
		assertTrue(((TransferOperation) sibsMock.getOperation(0)).getState() instanceof Deposited);
		sibsMock.processOperations();
		assertTrue(((TransferOperation) sibsMock.getOperation(0)).getState() instanceof Completed);

		verify(serviceMock, times(1)).withdraw(sourceIban, 50);
		verify(serviceMock, times(1)).deposit(targetIban2, 50);
		verify(serviceMock, times(1)).withdraw(sourceIban, 4);

		assertEquals(46, serviceMock.getAccountByIban(sourceIban).getBalance());
		assertEquals(1, sibsMock.getNumberOfOperations());
		assertEquals(54, sibsMock.getTotalValueOfOperations());
		assertEquals(50, sibsMock.getTotalValueOfOperationsForType(Operation.OPERATION_TRANSFER));
		assertEquals(0, sibsMock.getTotalValueOfOperationsForType(Operation.OPERATION_PAYMENT));

	}

	@After
	public void tearDown() {
		Bank.clearBanks();
	}

}

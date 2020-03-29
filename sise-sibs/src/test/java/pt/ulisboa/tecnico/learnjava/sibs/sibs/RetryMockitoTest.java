package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.domain.TransferOperation;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;
import state.Registered;

public class RetryMockitoTest {

	@Test
	public void retryStateWithdraw() throws AccountException, SibsException, OperationException {
		Services servicesMock = mock(Services.class);
		Sibs sibsMock = new Sibs(3, servicesMock);
		String sourceIban = "CGCCK1";
		String targetIban = "CGCCK2";

		when(servicesMock.existingAccount(sourceIban)).thenReturn(true);
		when(servicesMock.existingAccount(targetIban)).thenReturn(true);
		when(servicesMock.sameBank(sourceIban, targetIban)).thenReturn(true);

		doThrow(AccountException.class).when(servicesMock).withdraw(sourceIban, 100);

		sibsMock.transfer(sourceIban, targetIban, 100);
		sibsMock.processOperations();
		sibsMock.processOperations();
		sibsMock.processOperations();
		sibsMock.processOperations();

		verify(servicesMock, times(0)).withdraw(sourceIban, 100);
		verify(servicesMock, never()).deposit(targetIban, 100);
		assertTrue(((TransferOperation) sibsMock.getOperation(0)).getState() instanceof Registered);
	}

	@After
	public void tearDown() {
		Bank.clearBanks();
	}

}

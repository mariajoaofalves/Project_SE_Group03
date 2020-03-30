package pt.ulisboa.tecnico.learnjava.sibs.sibs;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Test;

import pt.ulisboa.tecnico.learnjava.bank.domain.Bank;
import pt.ulisboa.tecnico.learnjava.bank.exceptions.AccountException;
import pt.ulisboa.tecnico.learnjava.bank.services.Services;
import pt.ulisboa.tecnico.learnjava.sibs.domain.Sibs;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.OperationException;
import pt.ulisboa.tecnico.learnjava.sibs.exceptions.SibsException;

public class TransferMockitoTest {

	@Test
	public void transferFailNoTargetIban() throws AccountException, SibsException, OperationException {
		Services servicesMock = mock(Services.class);
		Sibs sibsMock = new Sibs(3, servicesMock);
		String sourceIban = "CGCCK1";
		String targetIban = "CGCCK2";

		when(servicesMock.existingAccount(sourceIban)).thenReturn(true);
		when(servicesMock.existingAccount(targetIban)).thenReturn(false);
		when(servicesMock.sameBank(sourceIban, targetIban)).thenReturn(true);

		try {
			sibsMock.transfer(sourceIban, targetIban, 100);
			fail();
		} catch (Exception e) {
		}
	}

	@After
	public void tearDown() {
		Bank.clearBanks();
	}

}

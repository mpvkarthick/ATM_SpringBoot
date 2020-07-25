package com.mannepk.project.atm.service;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.context.ContextConfiguration;

import com.mannepk.project.atm.exception.ATMException;
import com.mannepk.project.atm.model.Transaction;
import com.mannepk.project.atm.store.GlobalStore;

@ContextConfiguration(classes = WithdrawService.class)
@RunWith(PowerMockRunner.class)
public class WithDrawServiceTest {

	@Mock
	GlobalStore globalStore;

	@InjectMocks
	WithdrawService withdrawService;

	Transaction transaction;

	Transaction updatedTransaction;

	List<String> availableDenominations;

	String denominations = "20,10,5,1";

	String input;

	Map<Integer, Integer> currencyStore;

	Double amount;

	Map<Integer, Integer> dispensedCurrency;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		transaction = new Transaction();
		transaction.setId(UUID.randomUUID().toString());
		transaction.setType("Credit");
		availableDenominations = Arrays.asList(denominations.split(","));
		currencyStore = new HashMap<Integer, Integer>();
		currencyStore.put(20, 3);
		currencyStore.put(10, 8);
		currencyStore.put(5, 38);
		currencyStore.put(1, 4);

	}

	@Test
	public final void testExecute() throws ATMException {
		amount = 75.0;
		transaction.setId(UUID.randomUUID().toString());
		transaction.setType("Debit");
		transaction.setAmount(amount);

		PowerMockito.when(globalStore.getBalance()).thenReturn(334.0);
		PowerMockito.when(globalStore.getAvailableDenominations()).thenReturn(
				denominations.split(","));
		PowerMockito.when(globalStore.getCurrencyStore()).thenReturn(
				currencyStore);

		Transaction t = withdrawService.execute(transaction);

		assertTrue(t.getStatus().equalsIgnoreCase("Success"));

		assertTrue(t.getDispensedCurrency().get(20) == 3);
		assertTrue(t.getDispensedCurrency().get(10) == 1);
		assertTrue(t.getDispensedCurrency().get(5) == 1);

		assertTrue(t.getCurrency().get(20) == 0);
		assertTrue(t.getCurrency().get(10) == 7);
		assertTrue(t.getCurrency().get(5) == 37);
		assertTrue(t.getCurrency().get(1) == 4);

		assertTrue(t.getBalance() == 259);

	}

	@Test(expected = ATMException.class)
	public final void testExecuteWithException() throws ATMException {
		currencyStore.put(20, 0);
		currencyStore.put(10, 0);
		currencyStore.put(5, 27);
		currencyStore.put(1, 2);
		
		amount = 63.0;
		transaction.setId(UUID.randomUUID().toString());
		transaction.setType("Debit");
		transaction.setAmount(amount);

		PowerMockito.when(globalStore.getBalance()).thenReturn(137.0);
		PowerMockito.when(globalStore.getAvailableDenominations()).thenReturn(
				denominations.split(","));
		PowerMockito.when(globalStore.getCurrencyStore()).thenReturn(
				currencyStore);

		withdrawService.execute(transaction);
	}

}

package com.mannepk.project.atm.service;

import static org.junit.Assert.assertTrue;

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

@ContextConfiguration(classes = DepositService.class)
@RunWith(PowerMockRunner.class)
public class DepositServiceTest {

    @Mock
    GlobalStore globalStore;

    @InjectMocks
    DepositService depositService;

    Transaction transaction;

    Transaction updatedTransaction;

    List<String> availableDenominations;

    String denominations = "20,10,5,1";

    String input;

    Map<Integer, Integer> currency;

    Map<Integer, Integer> dispensedCurrency;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        transaction = new Transaction();
        transaction.setId(UUID.randomUUID().toString());
        transaction.setType("Credit");
        availableDenominations = Arrays.asList(denominations.split(","));
       
    }

    @Test
    public final void testExecute() throws ATMException {
        input = "10s: 8, 5s: 20";
        transaction.setInput(input);
        Map<Integer, Integer> currencyStore = new HashMap<Integer, Integer>();
        availableDenominations.stream()
                .forEach(c -> currencyStore.put(Integer.parseInt(c), 0));
        
        PowerMockito.when(globalStore.getAvailableDenominations()).thenReturn(denominations.split(","));
        PowerMockito.when(globalStore.getCurrencyStore()).thenReturn(currencyStore);
        
        Transaction t = depositService.execute(transaction);

        assertTrue(t.getStatus().equalsIgnoreCase("Success"));
        assertTrue(t.getCurrency().get(10) == 8);
        assertTrue(t.getCurrency().get(5) == 20);
        assertTrue(t.getBalance()== 180);

    }
    
    @Test(expected = ATMException.class)
    public final void testExecuteWithException() throws ATMException{
    	
    	input = "10s: 8, 5s: 20s";
        transaction.setInput(input);
        Map<Integer, Integer> currencyStore = new HashMap<Integer, Integer>();
        availableDenominations.stream()
                .forEach(c -> currencyStore.put(Integer.parseInt(c), 0));
        
        PowerMockito.when(globalStore.getAvailableDenominations()).thenReturn(denominations.split(","));
        PowerMockito.when(globalStore.getCurrencyStore()).thenReturn(currencyStore);
        
        depositService.execute(transaction);

    }
}

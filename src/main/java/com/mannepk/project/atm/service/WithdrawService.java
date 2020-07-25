/**
 *
 */
package com.mannepk.project.atm.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mannepk.project.atm.exception.ATMException;
import com.mannepk.project.atm.model.Transaction;
import com.mannepk.project.atm.store.GlobalStore;

/**
 * @author Karthik Mannepalli
 *
 */
@Service("withdrawService")
public class WithdrawService implements TransactionService<Transaction> {

    @Autowired
    GlobalStore globalStore;

    @Override
    public Transaction execute(Transaction t) throws ATMException {
        Map<Integer, Integer> dispensedCurrency = new HashMap<Integer, Integer>();
        //double balance = globalStore.debit(t.getAmount(),dispensedCurrency);
        Map<Integer, Integer> globalCurrencyStore;
        double balance = 0.0;
        double tempAmount = t.getAmount();
        double amount = t.getAmount();
        if (amount > globalStore.getBalance() || amount <= 0) {
            throw new ATMException("Incorrect or insufficient funds");
        }
        int billDenom, count = 0;
        for (String denomination : globalStore.getAvailableDenominations()) {
            billDenom = Integer.parseInt(denomination);
            if (globalStore.getCurrencyStore().get(billDenom) == 0) {
                continue;
            }
            if (amount >= billDenom) {
                count = ((int) (amount / billDenom)) > globalStore.getCurrencyStore().get(billDenom) ? globalStore.getCurrencyStore().get(billDenom) : ((int) (amount / billDenom));
                dispensedCurrency.put(billDenom, count);
                amount = amount - billDenom * count;
            }

        }
        if (amount > 0) {
            throw new ATMException("Requested withdraw amount is not dispensable");
        }
        globalCurrencyStore = globalStore.getCurrencyStore();
        dispensedCurrency.entrySet().stream().forEach(d -> {
        	globalCurrencyStore.put(d.getKey(), globalCurrencyStore.get(d.getKey()) - d.getValue());
        });
        
        balance = globalStore.getBalance() - tempAmount;
        globalStore.setBalance(balance);
        globalStore.setCurrencyStore(globalCurrencyStore);
        t.setDispensedCurrency(dispensedCurrency);
        t.setBalance(balance);
        t.setStatus("Success");
        t.setCurrency(globalCurrencyStore);
        return t;
    }

}

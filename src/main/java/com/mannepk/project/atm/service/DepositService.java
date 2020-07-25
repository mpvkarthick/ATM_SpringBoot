/**
 * 
 */
package com.mannepk.project.atm.service;

import com.mannepk.project.atm.exception.ATMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mannepk.project.atm.model.Transaction;
import com.mannepk.project.atm.store.GlobalStore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Karthik Mannepalli
 *
 */
@Service("depositService")
public class DepositService implements TransactionService<Transaction> {

	@Autowired
	GlobalStore globalStore;
	
	@Override
	public Transaction execute(Transaction t) throws ATMException {

		// Step 1 extract denominations and amount
		// Step 2 run the rules
		// Step 3, in case of no errors, calculate the
		// System.out.print("Input inside ATM "+input);
		// System.out.print(globalStore.toString());
		List<String> availableDenominations = Arrays.asList(globalStore.getAvailableDenominations());
		Map<Integer, Integer> currency = new HashMap<Integer, Integer>();
		double balance =0;
		String input = t.getInput();
		int denomination = 0;
		int billCount = 0;
		List<String> billInfos = Arrays.asList(input.split(","));
		double totalDepositAmount = 0;
		// rules to be executed
		// 1. valid integers provided - Assuming the format is correct
		// 2. No Negative Numbers
		// 3. Valid Denominations Provided
		// 4. Total input amount cannot be zero
		for (String billInfo : billInfos) {
			// billInfo.split("s: ");
			try {
				denomination = Integer.parseInt(billInfo.split("s: ")[0].trim());
				billCount = Integer.parseInt(billInfo.split("s: ")[1].trim());
			} catch (NumberFormatException nf) {
				throw new ATMException("Incorrect deposit amount");
			}

			// Valid positive integer Check
			if (denomination < 0 || billCount < 0) {
				throw new ATMException("Incorrect deposit amount");
			}
			// Valid Denomination Check
			if (!availableDenominations.contains(billInfo.split("s: ")[0].trim())) {
				throw new ATMException("Invalid Denomination");
			}
			currency.put(denomination, billCount);
			totalDepositAmount += denomination * billCount;
		}
		// if total amount is 0, throw an error
		if (totalDepositAmount == 0) {
			throw new ATMException("Deposit amount cannot be zero");
		}
		// No error, proceed to adding denominations to store and generating balance
		t.setAmount(totalDepositAmount);
		t.setCurrency(currency);
		globalStore.getCurrencyStore().entrySet().stream().forEach(c -> {
			currency.put(c.getKey(), currency.getOrDefault(c.getKey(), 0) + c.getValue());
		});
		globalStore.setCurrencyStore(currency);
		balance = globalStore.getBalance()+totalDepositAmount;
		globalStore.setBalance(balance);
		t.setBalance(balance);
		t.setStatus("Success");
		return t;
	}

}

/**
 * 
 */
package com.mannepk.project.atm.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.mannepk.project.atm.exception.ATMException;
import com.mannepk.project.atm.model.Transaction;

/**
 * @author Karthik Mannepalli
 *
 */
@Service
public class ATM {

	@Autowired
	@Qualifier("depositService")
	TransactionService<Transaction> depositService;

	@Autowired
	@Qualifier("withdrawService")
	TransactionService<Transaction> withdrawService;

	public void deposit(String input,  Transaction transaction)
			throws ATMException {


		transaction.setId(UUID.randomUUID().toString());
		transaction.setType("Credit");
		transaction.setInput(input);
		transaction = depositService.execute(transaction);

	}

	public void withdraw(Double amount, Transaction transaction) throws ATMException {
		transaction.setId(UUID.randomUUID().toString());
		transaction.setType("Debit");
		transaction.setAmount(amount);
		transaction = withdrawService.execute(transaction);
	}
}

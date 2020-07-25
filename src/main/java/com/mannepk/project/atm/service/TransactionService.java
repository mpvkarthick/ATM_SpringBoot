/**
 * 
 */
package com.mannepk.project.atm.service;

import org.springframework.stereotype.Service;

import com.mannepk.project.atm.exception.ATMException;
import com.mannepk.project.atm.model.Transaction;

/**
 * @author Karthik Mannepalli
 *
 */
@Service
public interface TransactionService<T> {

	public T execute(Transaction t) throws ATMException;
	
}

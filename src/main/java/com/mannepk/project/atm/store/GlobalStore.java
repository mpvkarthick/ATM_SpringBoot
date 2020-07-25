package com.mannepk.project.atm.store;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GlobalStore {

	/**
	 * stores the mapping of available currency in bank
	 */
	private Map<Integer, Integer> currencyStore;

	private String[] availableDenominations;

	private double balance;

	public GlobalStore() {
		super();
	}

	public GlobalStore(String allowedCurrency) {
		super();
		this.availableDenominations = allowedCurrency.split(",");
		Arrays.sort(availableDenominations, (a, b) -> Integer.parseInt(b)
				- Integer.parseInt(a));
		currencyStore = new HashMap<Integer, Integer>();
		Arrays.asList(this.availableDenominations)
				.stream()
				.forEach(
						currency -> currencyStore.put(
								Integer.parseInt(currency), 0));
	}

	/**
	 * @return the balance
	 */
	public double getBalance() {
		return balance;
	}

	/**
	 * @param balance
	 *            the balance to set
	 */
	public void setBalance(double balance) {
		this.balance = balance;
	}

	/**
	 * @return the availableDenominations
	 */
	public String[] getAvailableDenominations() {
		return availableDenominations;
	}

	/**
	 * @param availableDenominations
	 *            the availableDenominations to set
	 */
	public void setAvailableDenominations(String[] availableDenominations) {
		this.availableDenominations = availableDenominations;
	}

	public Map<Integer, Integer> getCurrencyStore() {
		return currencyStore;
	}

	public void setCurrencyStore(Map<Integer, Integer> currencyStore) {
		this.currencyStore = currencyStore;
	}

	@Override
	public String toString() {
		return "GlobalStore [currencyStore=" + currencyStore + "]";
	}

}

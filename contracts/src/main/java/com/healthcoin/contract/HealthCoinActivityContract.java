package com.healthcoin.contract;

import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.transactions.LedgerTransaction;

public class HealthCoinActivityContract implements Contract {
	public static final String ID = "com.healthcoin.contract.HealthCoinActivityContract";
	@Override
	public void verify(LedgerTransaction arg0) throws IllegalArgumentException {
		// TODO Auto-generated method stub

	}
	public interface Commands extends CommandData {
		class Create implements Commands {
		}
	}
	

}

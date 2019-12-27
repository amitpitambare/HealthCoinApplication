package com.healthcoin.contract;

import java.util.List;

import com.healthcoin.state.HealthCoinActivityState;
import static net.corda.core.contracts.ContractsDSL.requireThat;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.Contract;
import net.corda.core.transactions.LedgerTransaction;

public class HealthCoinActivityContract implements Contract {
	public static final String ID = "com.healthcoin.contract.HealthCoinActivityContract";
	@Override
	public void verify(LedgerTransaction tx) throws IllegalArgumentException {
		List<HealthCoinActivityState> inpStaes=tx.inputsOfType(HealthCoinActivityState.class);
		requireThat(require ->{
			 require.using("There is must be zero input State" , inpStaes.size()<1);
			 require.using("There is must be one ouput State of type HealthCoinApplication State" , tx.getOutputs().get(0).getData() instanceof HealthCoinActivityState);
			// require.using("There is must be one ouput State of type HealthCoinApplication State" , tx.getOutputs().get(0).getData() instanceof HealthCoinActivityState);
			 return require;
		 });
		

	}
	public interface Commands extends CommandData {
		class Create implements Commands {
		}
	}
	

}

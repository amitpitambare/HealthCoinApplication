package com.healthcoin.state;

import java.util.Arrays;
import java.util.List;

import com.healthcoin.contract.HealthCoinActivityContract;

import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.ContractState;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;


@BelongsToContract(HealthCoinActivityContract.class)
public class HealthCoinActivityState implements ContractState {
	private final Party completedBy;
	private final String activityName;
	private final Long caloriesBurned;



	public HealthCoinActivityState(Party completedBy, String activityName, Long caloriesBurned) {
		
		this.completedBy = completedBy;
		this.activityName = activityName;
		this.caloriesBurned = caloriesBurned;
	}

	public Party getCompletedBy() {
		return completedBy;
	}

	public String getActivityName() {
		return activityName;
	}

	public Long getCaloriesBurned() {
		return caloriesBurned;
	}

	

	@Override
	public List<AbstractParty> getParticipants() {
	
		return Arrays.asList(completedBy);
	}

}

package com.healthcoin.flows;

import java.util.Optional;

import com.google.common.collect.ImmutableList;
import com.healthcoin.contract.HealthCoinActivityContract;
import com.healthcoin.state.HealthCoinActivityState;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.contracts.Command;
import net.corda.core.flows.FinalityFlow;
import net.corda.core.flows.FlowException;
import net.corda.core.flows.FlowLogic;
import net.corda.core.flows.StartableByRPC;
import net.corda.core.identity.Party;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;
import net.corda.core.utilities.ProgressTracker;

@StartableByRPC
public class HealthActivityLoggerFlow extends FlowLogic<SignedTransaction> {
	private final ProgressTracker progressTracker = new ProgressTracker();

	private final Party completedBy;
	private final String activityName;
	private final Long caloriesBurned;

	public HealthActivityLoggerFlow(Party completedBy, String activityName, Long caloriesBurned) {

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
    public ProgressTracker getProgressTracker() {
        return progressTracker;
    }
	@Override
	@Suspendable
	public SignedTransaction call() throws FlowException {
		
		  final Party notary = getServiceHub().getNetworkMapCache().getNotaryIdentities().get(0);

          // Stage 1.
        //  progressTracker.setCurrentStep(GENERATING_TRANSACTION);
          // Generate an unsigned transaction.
		  
		  Optional<Party> admin = getServiceHub().getIdentityService().partiesFromName("Admin", true).stream().findFirst();
          Party me = getOurIdentity();
         
          HealthCoinActivityState activityState = new HealthCoinActivityState(me,activityName,caloriesBurned );
         
          final Command<HealthCoinActivityContract.Commands.Create> txCommand = new Command<>(
                  new HealthCoinActivityContract.Commands.Create(),
                  ImmutableList.of(activityState.getCompletedBy().getOwningKey()));
         
          final TransactionBuilder txBuilder = new TransactionBuilder(notary)
                  .addOutputState(activityState, HealthCoinActivityContract.ID)
                  .addCommand(txCommand);

          // Stage 2.
         
          // Verify that the transaction is valid.
          txBuilder.verify(getServiceHub());

          // Stage 3.
         
          // Sign the transaction.
          final SignedTransaction fullySignedTx = getServiceHub().signInitialTransaction(txBuilder);

      
         final SignedTransaction finalTx= subFlow(new FinalityFlow(fullySignedTx));
         subFlow(new ReportToAdminFlow.SendToAdminFlow(admin.get(), finalTx));

        return finalTx;
	}

}

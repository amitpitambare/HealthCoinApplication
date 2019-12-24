package com.healthcoin.service;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.healthcoin.flows.HealthCoinIssuanceFlow;
import com.healthcoin.state.HealthCoinActivityState;

import net.corda.core.identity.Party;
import net.corda.core.messaging.DataFeed;
import net.corda.core.node.AppServiceHub;
import net.corda.core.node.services.CordaService;
import net.corda.core.node.services.Vault.Page;
import net.corda.core.node.services.Vault.Update;
import net.corda.core.serialization.SingletonSerializeAsToken;
import rx.Observer;

@CordaService
public class AutoIssueTokenService extends SingletonSerializeAsToken {
	private AppServiceHub serviceHub;

	public AutoIssueTokenService(AppServiceHub serviceHub) {
		this.serviceHub = serviceHub;
		// code ran at service creation / node startup
		init();
	}

	private void init() {

		issueTokens();

	}

	private void issueTokens() {
		Party ourIdentity = serviceHub.getMyInfo().getLegalIdentities().get(0);
		Optional<Party> adminParty = serviceHub.getIdentityService().partiesFromName("Admin", true).stream()
				.findFirst();

		if (ourIdentity.equals(adminParty.get())) {
			DataFeed<Page<HealthCoinActivityState>, Update<HealthCoinActivityState>> dataFeed = serviceHub
					.getVaultService().trackBy(HealthCoinActivityState.class);

			Observer<Update<HealthCoinActivityState>> observer = new Observer<Update<HealthCoinActivityState>>() {

				@Override
				public void onCompleted() {
					// TODO Auto-generated method stub

				}

				@Override
				public void onError(Throwable e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onNext(Update<HealthCoinActivityState> t) {

					processState(t);
				}

			};
			dataFeed.getUpdates().subscribe(observer);
		}

		// dataFeed.getUpdates().subscribe(s -> processState(s));

	}

	private void processState(Update<HealthCoinActivityState> updates) {
		// LoyalityActivityState state;
		updates.getProduced().forEach(message -> {
			HealthCoinActivityState state = message.getState().getData();
			ExecutorService executor = Executors.newSingleThreadExecutor();

			if (state.getCaloriesBurned() > 1000) {
				executor.submit(() -> {

					serviceHub
							.startFlow(new HealthCoinIssuanceFlow("HealthCoin", new Long(100), state.getCompletedBy()));
				});
			} else {
				executor.submit(() -> {

					serviceHub
							.startFlow(new HealthCoinIssuanceFlow("HealthCoin", new Long(50), state.getCompletedBy()));

				});
			}
		});
		// return state;
	}
}

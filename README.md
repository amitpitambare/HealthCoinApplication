# HealthCoinsApplication
Sample Corda Application on Token SDK  based on Health coin use case .

*CorDapp Nodes:*

* Admin:  This party issues fungible tokens to the employee when employee logs thier activity.
* Employee: This party initially log thier  health activity like running ,cycling along with calories burned on that activity .Once employee log their activity they recieve fungible tokens that they redeem with partner merchants(HealthCoin) from the Admin.
* Merchant:This party recieve's token in exchange of an equivalant off ledger thing ,the merchant can then redeem these tokens on their side.
* Notary: notary node to check double-spend of input states then verify and sign final transaction.


**This CorDapp example business-logic flows as below:**

* 1. The `Employee` party logs the `Activity` state of type `LoyaltyActivityState` on ledger.
* 2. There would be Corda Service running  in the background on the `Admin` Node which would listen to LoyaltyActivityState created by `Employee`  and for every `LoyaltyActivityState` it would issue `FungibleToken` with Token Name as `HealthCoin`. 
* 3. The `Employee` party can then review and view the HealthCoins issued by `Admin` and redeem those by transfering those to the `Merchant` party.
* 4. The `Merchant` party - offline review and validate the `HealthCoins` data received along with `FungibleToken` state. If everything is good, he can initiate the `RedeemLoyaltyPointsFlow` flow to redeem the tokens . 

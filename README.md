# Round Up Savings Goal

## The Challenge
We’d like you to develop a **“round-up”** feature for Digital banking customers using our public developer API that is available to all customers and partners.
For a customer, take all the transactions in a given week and round them up to the nearest pound. For example with spending of **£4.35**, **£5.20** and **£0.87**, the round-up would be **£1.58**. This amount should then be transferred into a savings goal, helping the customer save for future adventures.

## Approach/Assumptions:

As part of the solution for this challenge, I have made below assumptions:

* Customer already have created a savings goal in his/her account.
* Customer will select one of his/her savings goal, from those in the list, and then trigger the round-up amount calculation, for the selected savings goal.
* The round-up amount calculated, for the related week, will be then transferred into the selected savings goal.
    * **SavingsGoalId** is expected to be past to the API for the Round Up calculation, as query parameter.
* The week, is either passed as parameter to the API, or is calculated during runtime,  as shown below, if the user does not specify the weekly window:
```   
    maxTransactionTimestamp = LocalDateTime.now() -> 2022-11-11T15:35:22.802Z
    minTransactionTimestamp = LocalDateTime.now().minusDays(7) -> 2022-11-04T15:35:22.801Z
    
    LocalDateTime.now() -> is the moment when the API call is triggered.
```        


## API Flow

* Retrieve Account Details
    *  Here I have filtered the accounts based on:
        *  **accountType = PRIMARY**
        *  **currency = GBP**
    * From this call I retrieve **accountUid** and **categoryId** necessary for the subsequent calls.

    * **Starling Endpoint called**  ```https://api-sandbox.starlingbank.com/api/v2/accounts```


* Retrieve all the transactions, for the given week, associated to the account.

    * **Endpoint called**  ```https://api-sandbox.starlingbank.com/api/v2/feed/account/0c2a9c84-1fde-4a57-91a4-5590d28e3758/category/b15c88bf-7d8d-46ab-a82f-6df9fd55f48a/transactions-between?minTransactionTimestamp=2022-11-04T15:35:22.801Z&maxTransactionTimestamp=2022-11-11T15:35:22.802Z```

* Calculate the Savings Goal Amount (in MinorUnit)
    * For each transaction the following is processed:
        * Fetch the transaction amount **minorUnit**
        * Convert this amount into Money and then in BigDecimal
        * Calculate the Round Up value
    * The total Round Up amount is than re-converted into minorUnit as expected by the Savings Goal.

* Fetch the Savings Goal Details where we need to add the money calculated from the weekly transactions.

    * **Endpoint called**  ```https://api-sandbox.starlingbank.com/api/v2/account/0c2a9c84-1fde-4a57-91a4-5590d28e3758/savings-goals/d84b33b2-05cf-4a8f-9b52-6cf95e5ed790```

* Update the Savings Goal Total Saved amount after adding the new value.

    * **Endpoint called**  ```https://api-sandbox.starlingbank.com/api/v2/account/0c2a9c84-1fde-4a57-91a4-5590d28e3758/savings-goals/d84b33b2-05cf-4a8f-9b52-6cf95e5ed790/add-money/3025159c-e22f-4ead-8233-c3bce5ae32f0```


## Tech Used

For this project I have used:

- Spring Boot
- Java 11
- Spring Web
- Lombok
- Docker
- Swagger for OpenAPI specs

## Running the application

The Token present with this build expires at 09:45 15/11/22.
If the existing token has expired, please do generate a new token from the developer sandbox on Starling Bank's API portal and place it in
```
src/main/resources/application.properties
```
To start the application, open the terminal from the root folder run the following command:
```
docker-compose up -d --build
```
### Data Test
1. Check Savings Goal Money Amount before executing the Round Up update:
```
https://api-sandbox.starlingbank.com/api/v2/account/0c2a9c84-1fde-4a57-91a4-5590d28e3758/savings-goals/d84b33b2-05cf-4a8f-9b52-6cf95e5ed790
```
2. Run Round Up API
```
localhost:8080/api/v1/savings/update-goal-amount?savingsGoalId=d84b33b2-05cf-4a8f-9b52-6cf95e5ed790
```
To run with a defined weekly window, please run the below API. For this week the Round Up Amount is equal to 1792 minorUnits - £17.92:
* minTransactionTimestamp = 2022-11-04T15:35:22.801Z
* maxTransactionTimestamp = 2022-11-11T15:35:22.802Z
```
localhost:8080/api/v1/savings/update-goal-amount?savingsGoalId=d84b33b2-05cf-4a8f-9b52-6cf95e5ed790&dateFrom=2022-11-04T15:35:22.801Z&dateTo=2022-11-11T15:35:22.802Z
```
3. Check again the Savings Goal values by running the API at 1.

## Swagger - Open API Spec

After docker compose command, once it is completed successfully, you can access below endpoints:

- Swagger Endpoint: Available at [http://localhost:80](http://localhost:80)
- API Endpoint: Available at [http://localhost:8080/api/v1/savings/update-goal-amount](http://localhost:8080/api/v1/savings/update-goal-amount)

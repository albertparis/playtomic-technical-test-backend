# Wallets Service
In Playtomic, we have a service to manage our wallets. Our players can top-up their wallets using a credit card and spend that money on our platform (bookings, racket rentals, ...)

That service has the following operations:
- You can query your balance.
- You can top-up your wallet. In this case, we charge the amount using a third-party payments platform (stripe, paypal, redsys).
- You can spend your balance on purchases in Playtomic. 
- You can return these purchases, and your money is refunded.
- You can check your history of transactions.

This exercise consists of building a proof of concept of that wallet service.
You have to code endpoints for these operations:
1. Get a wallet using its identifier.
1. Top-up money in that wallet using a credit card number. It has to charge that amount internally using a third-party platform.

You don't have to write the following operations, but we will discuss possible solutions during the interview:
1. How to spend money from the wallet.
1. How to refund that money.

The basic structure of a wallet is its identifier and its current balance. If you think you need extra fields, add them. We will discuss it in the interview. 

So you can focus on these problems, you have here a maven project with a Spring Boot application. It already contains
the basic dependencies and an H2 database. There are development and test profiles.

You can also find an implementation of the service that would call to the real payments platform (StripePaymentService).
This implementation is calling to a simulator deployed in one of our environments. Take into account
that this simulator will return 422 http error codes under certain conditions.

Consider that this service must work in a microservices environment in high availability. You should care about concurrency too.

You can spend as much time as you need but we think that 4 hours is enough to show [the requirements of this job.](OFFER.md)
You don't have to document your code, but you can write down anything you want to explain or anything you have skipped.
You don't need to write tests for everything, but we would like to see different types of tests.

# Challenge solution

## Assumptions
Given that this will be a high availability service, I anticipate the need for an external database,
rather than the included H2 DB, which is typically used for easy development.

In such cases, I would normally set up the service to use the H2 DB for tests, while integrating an external database
like PostgreSQL for development, using Docker, along with the necessary migrations.

## Installation
1. **Clone the repository**:
    ```sh
    git clone
    ```

2. **Build the project using Maven**:
    ```sh
    mvn clean install
    ```

3. **Run the application**:
    ```sh
    mvn spring-boot:run
    ```
   
## Usage
### Example Endpoints

- **Top-Up Wallet**: `POST /{id}/top-up`
- **Get Wallet**: `GET /{id}`

You can either go to `https://technical-test-backend.fly.dev/swagger-ui.html` or `http://localhost:8090/swagger-ui.html`
to access the swagger docs or use the following examples:

### Top-up wallet
```http
POST {{apiUrl}}/{{id}}/top-up
Content-Type: application/json

{
"creditCard": "4242424242424242",
"amount": 1000
}
```

### Get wallet
```http
GET {{apiUrl}}/{{id}}
```
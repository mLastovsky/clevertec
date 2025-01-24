# Check Processing Application

This project is a Java-based application for processing and generating purchase receipts. It is implemented using **vanilla Java** (Java SE) with a focus on clean code principles.

## Features

- **Generate receipts in CSV format**:
  - Includes date, time, product details, discounts, and totals.
  - Supports discount cards with a percentage-based discount.
- **Console-based receipt output**:
  - Formatted display of receipts for terminal-based use.
- **Error handling**:
  - Outputs error messages to CSV when errors occur.

## Prerequisites

To run this project, ensure you have the following installed:

- Java Development Kit (JDK) 21 or higher

## How to Run

1. **Clone the Repository**:

   ```bash
   git clone https://github.com/mLastovsky/clevertec.git
   cd clevertec
   ```

2. **Run the Application**:
   The main entry point for the application is `CheckRunner.java`. Use the following command structure to run the application:

   ```bash
   java -cp src ./src/main/java/ru/clevertec/check/CheckRunner.java id-quantity discountCard=xxxx balanceDebitCard=xxxx
   ```

   ### Command Breakdown:

   - `id-quantity`: At least one pair of product IDs and quantities (e.g., `3-1` for product ID 3, quantity 1) must be provided.
   - `discountCard=xxxx`: Discount card number (e.g., `1111`). This parameter is optional and can be omitted.
   - `balanceDebitCard=xxxx`: The balance available on a debit card.

   #### Example:

   ```bash
   java -cp src ./src/main/java/ru/clevertec/check/CheckRunner.java 3-1 2-5 5-1 discountCard=1111 balanceDebitCard=100
   ```

   This example generates a CSV file (`result.csv`) containing product details, prices, discounts, and totals. The receipt is also displayed in the console.

3. **Result Output**:

   - The application generates a CSV receipt file at `src/main/result.csv`.
   - Errors, if any, are logged in the same CSV format.

## Reference to the Task

This project is based on the requirements outlined in the provided task description. You can find the original task [here](https://drive.google.com/file/d/14DYEDzYeGHqLIXQXKTQFHluLJQsSsX26/view).


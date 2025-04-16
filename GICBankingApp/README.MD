# GICBankingApp

## Overview
GICBankingApp is a simple console-based banking system that handles operations on bank accounts. The system supports the following features:
- Input banking transactions (Deposit and Withdrawal)
- Input interest rules
- Calculate interest based on predefined rules
- Print account statements for a specified period

## Features
- Deposit and withdraw money from accounts
- Maintain transaction history
- Compute interest on account balances
- Generate formatted account statements

## Prerequisites
- Java Development Kit (JDK) 17 or later
- Apache Maven
- IntelliJ IDEA (or any Java-supported IDE)

## Steps to Run the Code
1. Download the source code from the [GitHub repository](#) (Replace with actual link).
2. Extract and save the source into your workspace.
3. Open **IntelliJ IDEA** and navigate to **File -> Open**.
4. Browse to the source location and click **OK**.
5. Once the project is imported into IntelliJ, double-click on the project folder.
6. Navigate to `src/main/java` in the left navigation pane.
7. Expand and locate `com.gic.banking.BankingSystem` class.
8. Right-click on the `BankingSystem` class and select **Run**.

## Steps to Run the Tests
1. Once the project is imported, open the **terminal** in IntelliJ.
2. Type the following command and press **Enter**:
   ```sh
   mvn clean test jacoco:report
   ```
3. After execution completes, navigate to `target/site/jacoco/index.html`.
4. Open `index.html` in a web browser to check the test coverage.

## Steps to Run the Executable File
1. Download `GICBankingApp.zip` and extract it into your workspace.
2. Navigate to the `GICBankingApp` folder.
3. Locate the `run-gic-banking-app.bat` file.
4. Double-click on `run-gic-banking-app.bat` to execute the application.

## Sample Inputs

### Transactions
```
20230505 AC001 D 100.00
20230601 AC001 D 150.00
20230626 AC001 W 20.00
20230626 AC001 W 100.00
```

### Interest Rate Rules
```
20230101 RULE01 1.95
20230520 RULE02 1.90
20230615 RULE03 2.20
```

### Statement Period
```
AC001 202306
```

### Expected Output
```
Account: AC001
| Date     | Txn Id     | Type | Amount  | Balance |
|----------|-----------|------|---------|---------|
| 20230601 | 20230601-1 | D    | 150.00  | 250.00  |
| 20230626 | 20230626-1 | W    | 20.00   | 230.00  |
| 20230626 | 20230626-2 | W    | 100.00  | 130.00  |
| 20230630 |           | I    | 0.39    | 130.39  |
```

## Additional Sample

### Transactions
```
20250201 AC002 D 500.00
20250205 AC002 W 150.00
20250210 AC002 D 200.00
20250215 AC002 W 250.00
20250221 AC002 D 200.00
20250227 AC002 D 100.00
```

### Interest Rate Rules
```
20230210 RULE04 2.20
20230221 RULE05 2.80
```

### Statement Period
```
AC002 202502
```

### Expected Output
```
Account: AC002
| Date	 | Txn Id	 | Type	 | Amount	 | Balance	 |
| 20250201	 | 20250201-1	 | D	 | 500.00	 | 500.00	 |
| 20250205	 | 20250205-1	 | W	 | 150.00	 | 350.00	 |
| 20250210	 | 20250210-1	 | D	 | 200.00	 | 550.00	 |
| 20250215	 | 20250215-1	 | W	 | 250.00	 | 300.00	 |
| 20250221	 | 20250221-1	 | D	 | 200.00	 | 500.00	 |
| 20250227	 | 20250227-1	 | D	 | 100.00	 | 600.00	 |
| 20250228	 | 				 | I	 |   0.75	 | 600.75	 |
```

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Author
Dinesh Madushanka

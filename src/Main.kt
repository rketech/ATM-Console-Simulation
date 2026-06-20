/*
    ----- ATM Console Simulation -------
        Step 1: ATM Simulation (Console Project)
            Build it in stages:

            Version 1

                Check Balance
                Deposit Money
                Withdraw Money
                Exit

            Version 2

                PIN Verification
                Multiple Accounts
                Transaction History

            Version 3

                Use Classes properly
                Account class
                Transaction class
                sealed class Result for success/error messages
*/
// -------  Implementing Class Design & State Management -------

// Preparing for Transaction History
enum class TransactionType {
    DEPOSIT,
    WITHDRAW,
    TRANSFER_IN,
    TRANSFER_OUT
}

data class Transaction(
    val transactionType: TransactionType,
    val transactionAmount: Double
)

// Creating sealed class for better error / information handeling
sealed class TransactionResult {

    data class Success(
        // Success is a child of TransactionResult.
        val transaction: Transaction, // created transaction object inorder to handle duplicate value which is already present in Transaction class
        val updatedBalance: Double,
    ) : TransactionResult()

    data class Error(
        val message: String,
    ) : TransactionResult()
}

class Account(
    val accountNumber: String,
    private val pin: Int,
    private var balance: Double
) {
    // PIN Verification Behavior
    fun verifyPin(enteredPin: Int?): Boolean {
        return enteredPin == pin
    }

    // Check Balance Behavior
    fun checkBalance(): Double {
        return balance
    }

    // Deposit Behavior
    fun deposit(amount: Double?): TransactionResult {
        if (amount != null) {
            if (amount <= 0) {
                return TransactionResult.Error("Enter a valid amount")
            }
            balance += amount
            val transaction = Transaction(TransactionType.DEPOSIT, amount)
            transactions.add(transaction)
            return TransactionResult.Success(transaction, balance)
        }
        return TransactionResult.Error("Enter a valid amount")
    }

    // Withdraw Behavior
    fun withdraw(amount: Double?): TransactionResult {
        if (amount != null) {
            if (amount <= 0) {
                return TransactionResult.Error("Enter a valid amount")
            } else if (balance < amount) {
                return TransactionResult.Error("Insufficient funds")
            } else {
                balance -= amount
                val transaction = Transaction(TransactionType.WITHDRAW, amount)
                transactions.add(transaction)
                return TransactionResult.Success(transaction, balance)
            }
        } else {
            return TransactionResult.Error("Enter a valid amount")
        }
    }

    // Preparing for Transaction History
    private val transactions = mutableListOf<Transaction>()

    fun showTransactions() {
        if (transactions.isEmpty()) {
            println("No transactions found")
            return
        }
        for (t in transactions) {
            println("Type: ${t.transactionType} | Amount: ${t.transactionAmount}")
        }
    }

    // Preparing Transfer Fund
    fun transfer(
        destinationAccount: Account,
        amount: Double?,
    ): TransactionResult {
        if (amount == null || amount <= 0) {
            return TransactionResult.Error("Enter a valid amount")
        }
        if (destinationAccount == this) {
            return TransactionResult.Error("Cannot transfer to the same account")
        }
        if (balance < amount) {
            return TransactionResult.Error("Insufficient funds")
        }
        balance -= amount
        destinationAccount.balance += amount

        val senderTransaction = Transaction(TransactionType.TRANSFER_OUT, amount)
        val receiverTransaction = Transaction(TransactionType.TRANSFER_IN, amount)

        // Adding Transfer Transaction History for both parties
        transactions.add(senderTransaction)
        destinationAccount.transactions.add(receiverTransaction)

        return TransactionResult.Success(senderTransaction, balance)
    }
}

fun fundTransfer(
    sourceAccount: Account,
    accounts: List<Account>,
) {
    print("Enter destination account number: ")
    val destinationAccountNumber = readlnOrNull()

    val destinationAccount = accounts.find { it.accountNumber == destinationAccountNumber }

    if (destinationAccount == null) {
        println("Destination account not found")
        return
    }else{
        print("Enter the transfer amount: ")
        val amount = readlnOrNull()?.toDoubleOrNull()

        when (val result = sourceAccount.transfer(destinationAccount, amount)) {
            is TransactionResult.Success -> {
                println("Successfully funded from ${sourceAccount.accountNumber} to ${destinationAccount.accountNumber}")
                println("Transaction Type: ${TransactionType.TRANSFER_OUT}")
                println("Transfer Amount: $amount")
            }
            is TransactionResult.Error -> {
                println("Error: ${result.message}")
            }
        }
    }
}

fun login(accounts: List<Account>): Account? // account: List<Account> means Give me all available accounts. The function needs them because it has to search for the account number entered by the user.
{
    print("Enter your account number: ")
    val enteredAccount = readlnOrNull()

    val selectedAccount = accounts.find { it.accountNumber == enteredAccount }

    // If the user entered account number do not match the database, the program will exit via below code
    if (selectedAccount == null) {
        println("No account found")
        return null // because if it were null, this code would execute: return and main() would end.
    }


    var loginAttempts = 0

    // Validating ATM PIN
    while (loginAttempts < 3) {
        print("Enter PIN: ") // is an action (a function call)
        val enteredPin = readlnOrNull()?.toIntOrNull() // Variable dies at the end of the loop iteration. So Val

        if (selectedAccount.verifyPin(enteredPin)) {
            println("PIN verified successfully")
            return selectedAccount // contains the logged-in account
        } else {
            loginAttempts++
            println("Incorrect PIN")
            println("Attempts Remaining: ${3 - loginAttempts}")
        }
    }
    if (loginAttempts == 3) {
        println("Maximum login attempts reached")
        println("Account Locked")
        return null// The return exits main() immediately all attempts are exhausted. No ATM Menu is provided
    }
    return null
}

fun showMenu(
    account: Account,
    accounts: List<Account>
) {
    var menuChoice: Int?

    // Entering ATM Menu
    do {
        println()
        println("-----ATM Menu-----")
        println()
        println("   1. Check Balance")
        println("   2. Deposit Money")
        println("   3. Withdraw Money")
        println("   4. Show Transaction History")
        println("   5. Transfer Money")
        println("   6. Exit")
        println()
        print("Enter choice: ")
        menuChoice =
            readlnOrNull()?.toIntOrNull() // toIntOrNull() protects the app from crashing if there is any null input

        when (menuChoice) {
            1 -> println("Current Balance for ${account.accountNumber} is : ${account.checkBalance()} INR") // If the balance changes later through deposits and withdrawals, this option will automatically show the updated amount.
            2 -> {
                print("Enter the deposit amount: ")
                val amount = readlnOrNull()?.toDoubleOrNull()
                when (val result = account.deposit(amount)) {
                    is TransactionResult.Success -> {
                        println("Amount deposited successfully")
                        println("Transaction Type: ${result.transaction.transactionType}")
                        println("Transaction Amount: ${result.transaction.transactionAmount}")
                        println("Updated Balance: ${result.updatedBalance}")
                    }

                    is TransactionResult.Error -> {
                        println("Error: ${result.message}")
                    }
                }
            }

            3 -> {
                print("Enter the withdrawal amount: ")
                val amount = readlnOrNull()?.toDoubleOrNull()
                when (val result = account.withdraw(amount)) {
                    is TransactionResult.Success -> {
                        println("Amount withdrawal successfully")
                        println("Transaction Type: ${result.transaction.transactionType}")
                        println("Transaction Amount: ${result.transaction.transactionAmount}")
                        println("Updated Balance: ${result.updatedBalance}")
                    }

                    is TransactionResult.Error -> {
                        println("Error: ${result.message}")
                    }
                }
            }

            4 -> {
                println("Transaction History for Account Number: ${account.accountNumber}")
                account.showTransactions()
            }

            5 -> {
                fundTransfer(account, accounts)
            }

            6 -> {
                println("Exiting ATM")
                // while (menuChoice != 5) is already present so removing break. So break is not required as Both do the same job
            }

            else -> println("Invalid Choice")
        }
    } while (menuChoice != 6)
    println()
    println("Thank you for using ATM Console Simulation")
}

fun main() {
    println()
    println("=================================")
    println("      ATM Console Simulation     ")
    println("=================================")
    println()

    // Balance can never be null so null safety operator is not required
    // Multiple Account Handling
    val accounts = listOf(
        Account("AC007", 777, 7000.0),
        Account("AC008", 888, 8000.0),
        Account("AC009", 999, 9000.0)
    )

    // Replaced the Kotlin's collection function with login function under clean design
    val selectedAccount = login(accounts) ?: return // contains the logged-in account

    showMenu(selectedAccount, accounts)

} // Main Function Ends



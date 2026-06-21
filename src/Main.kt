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

fun fundTransfer(
    sourceAccount: Account,
    accounts: List<Account>,
) {
    print("Enter destination account number: ")
    val destinationAccountNumber = readlnOrNull()

    val destinationAccount = accounts.find { it.accountNumber == destinationAccountNumber }

    when (destinationAccount) {
        null -> {
            println("Destination account not found")
            return
        }
        sourceAccount -> {
            println("Cannot transfer to the same account")
            return
        }
        else -> {
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



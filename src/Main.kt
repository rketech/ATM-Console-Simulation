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

// Implementing Class Design & State Management
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
    fun deposit(amount: Double?): Boolean {
        if (amount != null) {
            if (amount <= 0) {
                println("Enter a valid amount")
                return false
            }
            balance += amount
            return true
        }
        println("Enter a valid amount")
        return false
    }

    // Withdraw Behavior
    fun withdraw(amount: Double?): Boolean {
        if (amount != null) {
            if (amount <= 0) {
                println("Enter a valid amount")
                return false
            } else if (balance < amount) {
                println("Insufficient funds")
                return false
            } else {
                balance -= amount
                return true
            }
        } else {
            println("Enter a valid amount")
            return false
        }

    }
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

    print("Enter your account number: ")
    val enteredAccount = readlnOrNull()

    // Replaced the manual account search loop with Kotlin's collection function:
    val selectedAccount = accounts.find { it.accountNumber == enteredAccount } // contains the logged-in account

    // If the user entered account number do not match the database, the program will exit via below code
    if (selectedAccount == null) {
        println("Account Not Found")
        return // because if it were null, this code would execute: return and main() would end.
    }

    //val account = selectedAccount // contains the logged-in account

    var menuChoice: Int?
    var loginAttempts = 0

    // Validating ATM PIN
    while (loginAttempts < 3) {
        print("Enter PIN: ") // is an action (a function call)
        val enteredPin = readlnOrNull()?.toIntOrNull() // Variable dies at the end of the loop iteration. So Val

        if (selectedAccount.verifyPin(enteredPin)) {
            println("PIN verified successfully")
            break
        } else {
            loginAttempts++
            println("Incorrect PIN")
            println("Attempts Remaining: ${3 - loginAttempts}")
        }
    }
    if (loginAttempts == 3) {
        println("Maximum login attempts reached")
        println("Account Locked")
        return // The return exits main() immediately all attempts are exhausted. No ATM Menu is provided
    }

    // Entering ATM Menu
    do {
        println()
        println("-----ATM Menu-----")
        println()
        println("   1. Check Balance")
        println("   2. Deposit Money")
        println("   3. Withdraw Money")
        println("   4. Exit")
        println()
        print("Enter choice: ")
        menuChoice =
            readlnOrNull()?.toIntOrNull() // toIntOrNull() protects the app from crashing if there is any null input

        when (menuChoice) {
            1 -> println("Current Balance for ${selectedAccount.accountNumber} is : ${selectedAccount.checkBalance()} INR") // If the balance changes later through deposits and withdrawals, this option will automatically show the updated amount.
            2 -> {
                print("Enter the deposit amount: ")
                val amount = readlnOrNull()?.toDoubleOrNull()
                if (selectedAccount.deposit(amount)) {
                    println("Amount Deposited Successfully")
                    println("Updated Account Balance : ${selectedAccount.checkBalance()}")
                } else {
                    println("Deposit Not Successful")
                }
            }

            3 -> {
                print("Enter the withdrawal amount: ")
                val amount = readlnOrNull()?.toDoubleOrNull()
                if (selectedAccount.withdraw(amount)) {
                    println("Amount Withdrawal Successfully")
                    println("Updated Account Balance : ${selectedAccount.checkBalance()}")
                } else {
                    println("Withdrawal Not Successful")
                }
            }

            4 -> {
                println("Exiting ATM")
                // while (menuChoice != 4) is already present so removing break. So break is not required as Both do the same job
            }

            else -> println("Invalid Choice")
        }
    } while (menuChoice != 4)
    println()
    println("Thank you for using ATM Console Simulation")
} // Main Function Ends



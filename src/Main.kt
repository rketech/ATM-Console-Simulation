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

    val account = Account(
        accountNumber = "ACCOUNT007",
        pin = 111,
        balance = 7000.0 // Balance can never be null so null safety operator is not required
    )
    //var balanceAmount = 5000.0 // Balance can never be null so null safety operator is not required
    var menuChoice: Int?
    var loginAttempts = 0

    // Validating ATM PIN
    while (loginAttempts < 3) {
        print("Enter PIN: ") // is an action (a function call)
        val enteredPin = readlnOrNull()?.toIntOrNull() // Variable dies at the end of the loop iteration. So Val

        if (account.verifyPin(enteredPin)) {
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
            1 -> println("Current Balance for ${account.accountNumber} is : ${account.checkBalance()} INR") // If the balance changes later through deposits and withdrawals, this option will automatically show the updated amount.
            2 -> {
                print("Enter the deposit amount: ")
                val amount = readlnOrNull()?.toDoubleOrNull()
                if (account.deposit(amount)) {
                    println("Amount Deposited Successfully")
                    println("Updated Account Balance : ${account.checkBalance()}")
                } else {
                    println("Deposit Not Successful")
                }
            }

            3 -> {
                print("Enter the withdrawal amount: ")
                val amount = readlnOrNull()?.toDoubleOrNull()
                if (account.withdraw(amount)) {
                    println("Amount Withdrawal Successfully")
                    println("Updated Account Balance : ${account.checkBalance()}")
                } else {
                    println("Withdrawal Not Successful")
                }
            }

            4 -> {
                println("Exiting ATM")
                // break // while (menuChoice != 4) is already present so removing break. Both do the same job
            }

            else -> println("Invalid Choice")
        }
    } while (menuChoice != 4)
    println()
    println("Thank you for using ATM Console Simulation")
} // Main Function Ends



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

fun main() {
    println("=================================")
    println("      ATM Console Simulation     ")
    println("=================================")

    var balanceAmount = 5000.0 // Balance can never be null so null safety operator is not required
    var menuChoice: Int?

    do {
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
            1 -> println("Current Balance: ${balanceAmount} INR") // If the balance changes later through deposits and withdrawals, this option will automatically show the updated amount.
            2 -> {
                balanceAmount =
                    deposit(balanceAmount) // taking the value returned by the function and storing it back into the account balance. WARNING NOTE** One small logical issue Imagine this scenario: Current Balance = 5000 User chooses Deposit Deposit Amount = abc Your function prints: Invalid Amount and returns: 5000 But then the code in main() still prints: Amount deposited Successfully The updated balance within account is : 5000.0 INR That's not actually true because the deposit failed. Why this happens Your function returns the balance in both cases: Success return balance + depositAmount Failure return balance So main() cannot tell whether the operation succeeded or failed. For now Leave it exactly as it is.We'll improve this later when we learn:Data Classes Sealed Classes Result objects  Those concepts solve this problem elegantly. For Version 1, your current approach is perfectly acceptable.
                println("Amount deposited Successfully")
                println("The updated balance within account after deposit is : $balanceAmount INR")
            }

            3 -> {
                balanceAmount = withdraw(balanceAmount)
                println("Amount withdrawn Successfully")
                println("The updated balance within account after withdrawal is : $balanceAmount INR")
            }

            4 -> println("Exiting ATM")
            else -> println("Invalid Choice")
        }
    } while (menuChoice != 4)
} // Main Function Ends

/* Deposit Function
    1. Asks for the deposit amount.
    2. Reads the amount.
    3. Adds it to balanceAmount.
    4. Displays:
        a. Deposit Successful
        b. Updated Balance
*/
fun deposit(balance: Double): Double {

    print("Enter the deposit amount: ")

    val depositAmount = readln().toDoubleOrNull()

    return if (depositAmount != null && depositAmount > 0) {
        balance + depositAmount
    } else {
        println("Invalid Amount")
        balance
    }
}

/* Withdraw Function
    1. Ask user for withdrawal amount.
    2. Validate the amount.
    3. Check if balance is sufficient.
    4. If sufficient, deduct amount and return new balance.
    5. Otherwise, display error and return old balance.
*/
fun withdraw(balance: Double): Double {

    print("Enter the withdraw amount: ")

    val withdrawAmount = readln().toDoubleOrNull()

    return when {
        withdrawAmount == null -> {
            println("Invalid Amount")
            balance
        }

        withdrawAmount > balance -> {
            println("Insufficient funds")
            balance
        }

        withdrawAmount <= 0 -> {
            println("Amount must be greater than 0")
            balance
        }

        else -> {
            balance - withdrawAmount
        }
    }
}



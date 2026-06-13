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
        println("ATM Menu")
        println()
        println("   1. Check Balance")
        println("   2. Deposit Money")
        println("   3. Withdraw Money")
        println("   4. Exit")
        println()
        print("Enter choice: ")
        menuChoice = readLine()?.toIntOrNull() // toIntOrNull() protects the app from crashing if there is any null input

        when (menuChoice) {
            1 -> println("Current Balance: ${balanceAmount} INR") // If the balance changes later through deposits and withdrawals, this option will automatically show the updated amount.
            2 -> println("Deposit Money Selected")
            3 -> println("Withdraw Money Selected")
            4 -> println("Exiting ATM")
            else -> println("Invalid Choice")
        }
    } while (menuChoice != 4)
} // Main Function Ends
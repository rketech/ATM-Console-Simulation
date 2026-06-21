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
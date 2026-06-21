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
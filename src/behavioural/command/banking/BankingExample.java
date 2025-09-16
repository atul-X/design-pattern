package behavioural.command.banking;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Banking Transaction System - Command Pattern Example
 *
 * This example demonstrates the Command Pattern in a real-world banking scenario.
 *
 * Key Components:
 * - BankingCommand: Command interface with audit trail support
 * - BankAccount: Receiver that performs actual banking operations
 * - Concrete Commands: DepositCommand, WithdrawCommand, TransferCommand
 * - BankingController: Invoker that manages transactions and compliance
 *
 * Benefits Demonstrated:
 * - Transaction encapsulation: Each operation is a separate command object
 * - Undo functionality: All transactions can be reversed for error correction
 * - Audit trail: Complete logging of all transactions for compliance
 * - Batch processing: Multiple transactions with rollback capability
 * - Daily limits: Transaction limits and monitoring
 * - Error handling: Robust validation and error recovery
 */
public class BankingExample {

    public static void main(String[] args) {
        System.out.println("🏦 Banking Transaction System");
        System.out.println("═══════════════════════════════════════════════════════════");
        System.out.println("Demonstrating Command Pattern with real-world banking operations");
        System.out.println();

        // Create bank accounts (Receivers)
        BankAccount johnAccount = new BankAccount("ACC-001", "John Smith", 5000.0, "Checking");
        BankAccount janeAccount = new BankAccount("ACC-002", "Jane Doe", 3000.0, "Savings");
        BankAccount corpAccount = new BankAccount("ACC-003", "TechCorp Inc.", 50000.0, "Business");

        // Create the banking controller (Invoker)
        BankingController controller = new BankingController();

        // Demonstrate basic banking operations
        demonstrateBasicOperations(controller, johnAccount, janeAccount);

        // Demonstrate transfer operations
        demonstrateTransferOperations(controller, johnAccount, janeAccount, corpAccount);

        // Demonstrate batch transactions
        demonstrateBatchTransactions(controller, johnAccount, janeAccount);

        // Demonstrate undo functionality
        demonstrateUndoFunctionality(controller);

        // Show audit trail and reports
        demonstrateAuditTrail(controller, johnAccount, janeAccount, corpAccount);

        // Interactive demo (optional)
        System.out.println("\n🎮 Would you like to try the interactive banking demo? (y/n)");
        Scanner scanner = new Scanner(System.in);
        String response = scanner.nextLine().toLowerCase();

        if (response.equals("y") || response.equals("yes")) {
            interactiveDemo(controller, johnAccount, janeAccount, corpAccount);
        }

        scanner.close();

        System.out.println("\n✅ Banking Transaction System demo completed!");
        System.out.println("📚 This example demonstrates the Command Pattern's benefits in banking:");
        System.out.println("   • Encapsulation of transaction requests as objects");
        System.out.println("   • Support for undo operations and error recovery");
        System.out.println("   • Complete audit trail for regulatory compliance");
        System.out.println("   • Batch transaction processing with rollback");
        System.out.println("   • Daily transaction limits and monitoring");
        System.out.println("   • Decoupling of transaction logic from execution control");
    }

    private static void demonstrateBasicOperations(BankingController controller,
                                                 BankAccount johnAccount,
                                                 BankAccount janeAccount) {
        System.out.println("💰 BASIC BANKING OPERATIONS DEMONSTRATION");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        // Deposit operations
        controller.executeTransaction(new DepositCommand(johnAccount, 1500.0));
        controller.executeTransaction(new DepositCommand(janeAccount, 800.0));

        // Withdrawal operations
        controller.executeTransaction(new WithdrawCommand(johnAccount, 500.0));
        controller.executeTransaction(new WithdrawCommand(janeAccount, 200.0));

        System.out.println();
    }

    private static void demonstrateTransferOperations(BankingController controller,
                                                    BankAccount johnAccount,
                                                    BankAccount janeAccount,
                                                    BankAccount corpAccount) {
        System.out.println("🔄 TRANSFER OPERATIONS DEMONSTRATION");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        // Transfer between personal accounts
        controller.executeTransaction(new TransferCommand(johnAccount, janeAccount, 1000.0));

        // Transfer to business account
        controller.executeTransaction(new TransferCommand(janeAccount, corpAccount, 500.0));

        // Large transfer (might hit daily limits in interactive mode)
        controller.executeTransaction(new TransferCommand(corpAccount, johnAccount, 2000.0));

        System.out.println();
    }

    private static void demonstrateBatchTransactions(BankingController controller,
                                                   BankAccount johnAccount,
                                                   BankAccount janeAccount) {
        System.out.println("📦 BATCH TRANSACTION DEMONSTRATION");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        // Create a batch of transactions (salary processing scenario)
        System.out.println("🏢 Processing monthly salary batch...");
        boolean batchSuccess = controller.executeTransactions(Arrays.asList(
            new DepositCommand(johnAccount, 4000.0), // Salary deposit
            new WithdrawCommand(johnAccount, 1200.0), // Rent payment
            new WithdrawCommand(johnAccount, 300.0),  // Utilities
            new TransferCommand(johnAccount, janeAccount, 500.0) // Family transfer
        ));

        if (batchSuccess) {
            System.out.println("✅ Salary processing batch completed successfully");
        } else {
            System.out.println("❌ Salary processing batch failed - all transactions rolled back");
        }

        System.out.println();
    }

    private static void demonstrateUndoFunctionality(BankingController controller) {
        System.out.println("↩️ UNDO FUNCTIONALITY DEMONSTRATION");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        controller.showTransactionHistory();

        System.out.println("\n🔄 Undoing last 2 transactions...");
        controller.undoLastTransaction();
        controller.undoLastTransaction();

        System.out.println();
    }

    private static void demonstrateAuditTrail(BankingController controller,
                                            BankAccount johnAccount,
                                            BankAccount janeAccount,
                                            BankAccount corpAccount) {
        System.out.println("📊 AUDIT TRAIL AND REPORTING DEMONSTRATION");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        // Show account information
        johnAccount.printAccountInfo();
        System.out.println();
        janeAccount.printAccountInfo();
        System.out.println();
        corpAccount.printAccountInfo();
        System.out.println();

        // Show transaction histories
        johnAccount.printTransactionHistory(5);
        System.out.println();

        // Show controller reports
        controller.showAuditTrail();
        controller.showDailyTransactionSummary();

        System.out.println();
    }

    private static void interactiveDemo(BankingController controller,
                                      BankAccount johnAccount,
                                      BankAccount janeAccount,
                                      BankAccount corpAccount) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("\n🎮 INTERACTIVE BANKING SYSTEM");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");

        while (running) {
            System.out.println("\nAvailable operations:");
            System.out.println("1. Deposit to John's account");
            System.out.println("2. Withdraw from John's account");
            System.out.println("3. Deposit to Jane's account");
            System.out.println("4. Withdraw from Jane's account");
            System.out.println("5. Transfer from John to Jane");
            System.out.println("6. Transfer from Jane to Corp");
            System.out.println("7. Show account information");
            System.out.println("8. Show transaction history");
            System.out.println("9. Show audit trail");
            System.out.println("10. Undo last transaction");
            System.out.println("11. Show daily summary");
            System.out.println("0. Exit");
            System.out.print("\nEnter your choice: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        System.out.print("Enter deposit amount: $");
                        double depositAmount = Double.parseDouble(scanner.nextLine());
                        controller.executeTransaction(new DepositCommand(johnAccount, depositAmount));
                        break;
                    case 2:
                        System.out.print("Enter withdrawal amount: $");
                        double withdrawAmount = Double.parseDouble(scanner.nextLine());
                        controller.executeTransaction(new WithdrawCommand(johnAccount, withdrawAmount));
                        break;
                    case 3:
                        System.out.print("Enter deposit amount: $");
                        double janeDepositAmount = Double.parseDouble(scanner.nextLine());
                        controller.executeTransaction(new DepositCommand(janeAccount, janeDepositAmount));
                        break;
                    case 4:
                        System.out.print("Enter withdrawal amount: $");
                        double janeWithdrawAmount = Double.parseDouble(scanner.nextLine());
                        controller.executeTransaction(new WithdrawCommand(janeAccount, janeWithdrawAmount));
                        break;
                    case 5:
                        System.out.print("Enter transfer amount (John to Jane): $");
                        double transferAmount1 = Double.parseDouble(scanner.nextLine());
                        controller.executeTransaction(new TransferCommand(johnAccount, janeAccount, transferAmount1));
                        break;
                    case 6:
                        System.out.print("Enter transfer amount (Jane to Corp): $");
                        double transferAmount2 = Double.parseDouble(scanner.nextLine());
                        controller.executeTransaction(new TransferCommand(janeAccount, corpAccount, transferAmount2));
                        break;
                    case 7:
                        johnAccount.printAccountInfo();
                        System.out.println();
                        janeAccount.printAccountInfo();
                        System.out.println();
                        corpAccount.printAccountInfo();
                        break;
                    case 8:
                        controller.showTransactionHistory();
                        break;
                    case 9:
                        controller.showAuditTrail();
                        break;
                    case 10:
                        controller.undoLastTransaction();
                        break;
                    case 11:
                        controller.showDailyTransactionSummary();
                        break;
                    case 0:
                        running = false;
                        System.out.println("👋 Exiting banking system...");
                        break;
                    default:
                        System.out.println("❌ Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input. Please enter a valid number.");
            }
        }
    }
}

package org.example.controller;

import org.example.entity.AccountApplication;
import org.example.entity.Account;
import org.example.entity.Transaction;
import org.example.entity.User;
import org.example.service.AccountApplicationService;
import org.example.service.AccountService;
import org.example.service.TransactionService;
import org.example.service.UserService;

import java.sql.SQLException;
import java.util.Scanner;

public class BankController {

    AccountService accountService;
    UserService userService;
    TransactionService transactionService;
    AccountApplicationService accountApplicationService;
    String status;
    int login;

    private Scanner scanner;

    public BankController() {
        scanner = new Scanner(System.in);
        accountService = new AccountService();
        userService = new UserService();
        accountApplicationService = new AccountApplicationService();
        transactionService = new TransactionService();
    }

    public String getStatus() {
        return status;
    }

    public void handleCreateAccount() {

        System.out.printf("Enter Application ID:");
        int application_id = scanner.nextInt();
        scanner.nextLine();
        System.out.printf("Enter Account Number:");
        String account_number = scanner.nextLine().trim();
        System.out.printf("Enter Balance: ");
        Double balance = scanner.nextDouble();

        try{
            AccountApplication application = accountApplicationService.findApplicationById(application_id);
            if(application == null || application.getStatus().equals("PENDING") || application.getStatus().equals("DENIED")){
                System.out.println("Account Application not approved");
                return;
            }
            Account accountDTO = accountService.getAccountById(accountService.addAccount(new Account(account_number, application.getAccountType(), balance), application_id));
            System.out.println("Account created");
            System.out.println(accountDTO.toString());
        }catch(SQLException e){
            System.out.printf(e.getMessage());
        }
    }

    public void handleFindAccountById(){
        System.out.printf("Enter Account ID: ");
        int ID = scanner.nextInt();

        try{
            Account accountDTO = accountService.getAccountById(ID);
            if(accountDTO != null){
                System.out.println("Account found");
                System.out.printf(accountDTO.toString());
            }else{
                System.out.println("Account does not exist");
            }
        }catch(SQLException e){
            System.out.printf(e.getMessage());
        }

    }

    public void handleCheckPersonal(){
        try{
            System.out.printf(userService.getUserById(login).toStringCustomer());
        }catch(SQLException e){
            System.out.printf(e.getMessage());
        }

    }

    public void handleFindAccountByAccountNumber(){
        System.out.printf("Enter Account Number: ");
        String number = scanner.nextLine().trim();
        try{
            Account accountDTO = accountService.getAccountByNumber(number);
            if(accountDTO != null){
                System.out.println("Account found");
                System.out.printf(accountDTO.toString());
            }else{
                System.out.printf("Account does not exist");
            }
        }catch(SQLException e){
            System.out.printf(e.getMessage());
        }

    }

    public void handleUpdateAccount(){
        System.out.println("Re-enter the new details for the account");
        System.out.printf("Enter Account Number:");
        String account_number = scanner.nextLine().trim();
        System.out.printf("Enter Account Type (Acceptable are SAVINGS or CHECKING): ");
        String account_type = scanner.nextLine().trim();
        System.out.printf("Enter Balance: ");
        Double balance = scanner.nextDouble();
        System.out.printf("Enter to-be-updated account's ID: ");
        int id = scanner.nextInt();

        try{
            Account accountDTO = new Account(id, accountService.getAccountById(id).getApplicationId(), account_number, account_type, balance);
            boolean update = accountService.updateAccount(accountDTO, id);
            if(update){
                System.out.println("Account updated");
                System.out.printf(accountDTO.toString());
            }else{
                System.out.println("Account update failed");
            }
        }catch(SQLException e){
            System.out.printf(e.getMessage());
        }

    }

    public void handleDeleteAccount() {
        System.out.println("Enter ID of account to be deleted: ");
        int id = scanner.nextInt();

        try{
            boolean delete = accountService.removeAccount(id);
            if(delete){
                System.out.printf("Account deleted");
            }else{
                System.out.printf("Account delete failed");
            }
        }catch(SQLException e){
            System.out.printf(e.getMessage());
        }
    }

    public void handleCreateUser() {
        System.out.printf("Enter Username:");
        String username = scanner.nextLine().trim();
        System.out.printf("Enter Password: ");
        String password = scanner.nextLine().trim();
        System.out.printf("Enter First Name: ");
        String first_name = scanner.nextLine().trim();
        System.out.printf("Enter Last Name: ");
        String last_name = scanner.nextLine().trim();
        System.out.printf("Enter email: ");
        String email = scanner.nextLine().trim();
        System.out.printf("Enter role(Acceptable CUSTOMER or EMPLOYEE): ");
        String role = scanner.nextLine().trim();

        try{
            User user = userService.getUserById(userService.addUser(new User(username, password, first_name, last_name, email, role)));
            System.out.println("User created");
            System.out.println(user.toString());
        }catch(SQLException e){
            System.out.printf(e.getMessage());
        }

    }

    public void handleRegisterUser() {
        System.out.printf("Enter Username:");
        String username = scanner.nextLine().trim();
        System.out.printf("Enter Password: ");
        String password = scanner.nextLine().trim();
        System.out.printf("Enter First Name: ");
        String first_name = scanner.nextLine().trim();
        System.out.printf("Enter Last Name: ");
        String last_name = scanner.nextLine().trim();
        System.out.printf("Enter email: ");
        String email = scanner.nextLine().trim();

        try{
            User user = userService.getUserById(userService.addUser(new User(username, password, first_name, last_name, email, "CUSTOMER")));
            System.out.println("User created");
            System.out.println(user.toStringCustomer());
        }catch(SQLException e){
            System.out.printf(e.getMessage());
        }

    }

    public void handleFindUserById(){
        System.out.printf("Enter ID:");
        int id = scanner.nextInt();

        try{
            User user = userService.getUserById(id);
            System.out.println("User found");
            System.out.println(user.toString());
        }catch(SQLException e){
            System.out.printf(e.getMessage());
        }
    }

    public void handleLogin(){
        System.out.printf("Enter Username: ");
        String user = scanner.nextLine().trim();
        System.out.println();
        System.out.printf("Enter Password: ");
        String pass = scanner.nextLine().trim();

        try{
            User userDTO = userService.login(user, pass);
            status = userDTO.getRole();
            login = userDTO.getId();
            System.out.println("User login successful");
        }catch(SQLException e){
            System.out.printf(e.getMessage());
        }
    }

    public void handleUpdateUser(){
        System.out.printf("Enter ID of user to be changed: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Re-enter the new details for the user");
        System.out.println();
        System.out.printf("Enter Username:");
        String user = scanner.nextLine().trim();
        System.out.printf("Password: ");
        String pass = scanner.nextLine().trim();
        System.out.printf("Enter First Name: ");
        String first_name = scanner.nextLine().trim();
        System.out.printf("Enter Last Name: ");
        String last_name = scanner.nextLine().trim();
        System.out.printf("Enter Email: ");
        String email = scanner.nextLine().trim();
        System.out.printf("Enter Role: ");
        String role = scanner.nextLine().trim();

        try{
            if(userService.getUserById(id) == null){
                System.out.println("User does not exist");
                return;
            }
            User userDTO = new User(user, pass, first_name, last_name, email, role);
            boolean update = userService.updateUser(userDTO, id);
            if(update){
                System.out.println("User updated");
                System.out.println(userDTO.toString());
            }else{
                System.out.println("User update failed");
            }
        }catch(SQLException e){
            System.out.printf(e.getMessage());
        }

    }

    public void handleDeleteUser() {
        System.out.println("Enter ID of user to be deleted: ");
        int id = scanner.nextInt();

        try{
            boolean delete = userService.removeUser(id);
            if(delete){
                System.out.println("User deleted");
            }else{
                System.out.println("User delete failed");
            }
        }catch(SQLException e){
            System.out.printf(e.getMessage());
        }
    }

    public void handleCreateTransaction(){
        System.out.printf("Enter account number:");
        String account_number = scanner.nextLine().trim();
        System.out.printf("Enter Transaction Type: \n1. DEPOSIT\n2. WITHDRAW\n3. TRANSFER\n4. CANCEL\n");
        int type;
        while(true){
            type = scanner.nextInt();
            if(type > 4 || type < 1){
                System.out.printf("Enter valid Transaction Type:");
            }else{
                break;
            }
        }
        String other_account_number = "";
        if(type == 3){
            System.out.printf("Enter account transferred from: ");
            scanner.nextLine();
            other_account_number = scanner.nextLine().trim();
        }
        System.out.printf("Enter Amount (Non-negative): ");
        double amount;
        while (true) {
            amount = scanner.nextDouble();
            if(amount >= 0){
                break;
            }
            System.out.printf("Enter Valid Amount: ");
        }

        try{
            Account accountDTO = accountService.getAccountByNumber(account_number);
            Transaction transaction = new Transaction(accountService.getAccountByNumber(account_number).getAccountId(), amount);

            if(type == 1){
                transaction.setTransactionType("DEPOSIT");
                accountDTO.setBalance(accountDTO.getBalance() + amount);
            }else if (type == 2){
                transaction.setTransactionType("WITHDRAWAL");
                if(accountDTO.getBalance() - amount < 0){
                    accountDTO.setBalance(0);
                }else{
                    accountDTO.setBalance(accountDTO.getBalance() - amount);
                }
            }else{
                Account otherAccountDTO = accountService.getAccountByNumber(other_account_number);
                if (otherAccountDTO == null) {
                    System.out.println("Transferring account not found.");
                    return;
                }
                if(amount > otherAccountDTO.getBalance()){
                    System.out.println("Requested amount cannot be taken from other account.");
                    return;
                }
                Transaction otherTransaction = new Transaction(accountService.getAccountByNumber(other_account_number).getAccountId(), "TRANSFER_OUT", amount);
                otherAccountDTO.setBalance(otherAccountDTO.getBalance() - amount);
                accountService.updateAccount(otherAccountDTO, otherAccountDTO.getAccountId());
                accountDTO.setBalance(accountDTO.getBalance() + amount);
                transaction.setTransactionType("TRANSFER_IN");
                transactionService.addTransaction(otherTransaction);
            }
            accountService.updateAccount(accountDTO, accountDTO.getAccountId());
            transactionService.addTransaction(transaction);
            System.out.println("Transaction completed.");
        }catch(SQLException e){
            System.out.printf(e.getMessage());
        }
    }

    public void handleFindTransaction(){
        System.out.printf("Enter Transaction ID: ");
        int id = scanner.nextInt();

        try{
            Transaction transaction = transactionService.findTransaction(id);
            if(transaction != null) {
                System.out.println("Account found");
                System.out.println(transaction.toString());
            }
        }catch(SQLException e){
            System.out.printf(e.getMessage());
        }
    }

    public void handleCreateApplication(){
        int choice;
        while(true){
            System.out.printf("Choose Account Type\n1. CHECKING\n2. SAVINGS\n");
            choice = scanner.nextInt();
            if(choice != 1 && choice != 2){
                System.out.println("Invalid Input");
            }else{
                break;
            }
        }

        try{
            if(choice == 1){
                accountApplicationService.addApplication(login, "CHECKING");
            }else{
                accountApplicationService.addApplication(login, "SAVINGS");
            }
            System.out.println("Account Application Created");


        }catch(SQLException e){
            System.out.printf(e.getMessage());
        }
    }

    public void handleFindApplication(){
        System.out.printf("Enter Application ID: ");
        int id = scanner.nextInt();

        try{
            AccountApplication applicationDTO = accountApplicationService.findApplicationById(id);
            if(applicationDTO != null) {
                System.out.println("Account found");
                System.out.printf(applicationDTO.toString());
            }
        }catch(SQLException e){
            System.out.printf(e.getMessage());
        }
    }

    public void handleUpdateApplication(){
        System.out.printf("Enter Application ID: ");
        int id = scanner.nextInt();
        System.out.printf("Enter Application Status: \n1. APPROVED\n2. DENIED\n3. PENDING\n4. CANCEL\n");
        int approve;
        while(true){
            approve = scanner.nextInt();
            if(approve > 4 || approve < 1){
                System.out.printf("Enter valid Approval Status:");
            }else{
                break;
            }
        }

        try{
            AccountApplication accountApplicationDTO = accountApplicationService.findApplicationById(id);
            if(accountApplicationDTO != null) {
                System.out.println("Account Application found");
                System.out.printf(accountApplicationDTO.toString());
                if(approve == 1){
                    accountApplicationDTO.setStatus("APPROVED");
                }else if(approve == 2){
                    accountApplicationDTO.setStatus("DENIED");
                }else if(approve == 3){
                    accountApplicationDTO.setStatus("PENDING");
                }else{
                    return;
                }
                if(accountApplicationService.updateApplication(accountApplicationDTO, login)){
                    handleCreateAccount();
                    System.out.println("Application updated successfully");
                }else{
                    System.out.println("Application update failed");
                }
            }
        }catch(SQLException e){
            System.out.printf(e.getMessage());
        }
    }

}

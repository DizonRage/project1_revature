package org.example;

//import org.example.controller.BankController;
import org.example.controller.BankController;
import org.example.repository.AccountDAO;
import org.example.model.Account;
import org.example.util.DatabaseConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        BankController controller = new BankController();
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("1. Login\n2. Register\n3. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine();
            if(choice == 1){
                controller.handleLogin();
                while(true){
                    if(controller.getStatus().equals("EMPLOYEE")){
                        System.out.println("MENU\n1. Info Check\n2. Edit Info\n3. Account Applications\n4. Exit");
                        int decision = scanner.nextInt();
                        if(decision == 1) {
                            while (true) {
                                System.out.println("Type:\n1. Account Check\n2. User Check\n3. Transaction Check\n4. Application Check\n5. exit");
                                decision = scanner.nextInt();
                                if(decision == 1){
                                    controller.handleFindAccountByAccountNumber();
                                }else if(decision == 2){
                                    controller.handleFindUserById();
                                }else if(decision == 3){
                                    controller.handleFindTransaction();
                                }else if(decision == 4){
                                    controller.handleFindApplication();
                                }else if(decision == 5){
                                    break;
                                }else{
                                    System.out.println("Invalid Input");
                                }
                            }

                        }else if(decision == 2){
                            while (true) {
                                System.out.println("Type:\n1. Account\n2. User\n3. Application\n4. Exit");
                                decision = scanner.nextInt();
                                System.out.println();
                                if(decision == 1){
                                    controller.handleUpdateAccount();
                                }else if(decision == 2){
                                    controller.handleUpdateUser();
                                }else if(decision == 3){
                                    controller.handleUpdateApplication();
                                }else if(decision == 4){
                                    break;
                                }else{
                                    System.out.println("Invalid Input");
                                }
                            }
                        }else if(decision == 3){
                            controller.handleUpdateApplication();
                        }else if(decision == 4){
                            break;
                        }else{
                            System.out.println("Invalid Input");
                        }
                    }else{
                        System.out.println("MENU\n1. Info Check\n2. Account Application\n3. Transaction\n4. Exit\n");
                        int decision = scanner.nextInt();
                        if(decision == 1) {
                            while (true) {
                                System.out.println("Type:\n1. Account Check\n2. User Check\n3. Exit\n");
                                decision = scanner.nextInt();
                                if(decision == 1){
                                    controller.handleFindAccountByAccountNumber();
                                }else if(decision == 2){
                                    controller.handleCheckPersonal();
                                }else if(decision == 3){
                                    break;
                                }else{
                                    System.out.println("Invalid Input");
                                }
                            }

                        }else if(decision == 2){
                            controller.handleCreateApplication();
                        }else if(decision == 3){
                            controller.handleCreateTransaction();
                        }else if(decision == 4){
                            break;
                        }else{
                            System.out.println("Invalid Input");
                        }

                    }
                }
            }else if(choice == 2) {
                controller.handleRegisterUser();
            }else if(choice == 3){
                return;
            }else{
                System.out.println("Invalid Option");
            }


        }
    }
}

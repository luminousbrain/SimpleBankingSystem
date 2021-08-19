package ru.gontarenko.banking;

import ru.gontarenko.banking.entity.Card;

import java.util.Scanner;

public class BankApp {
    private final Scanner scanner = new Scanner(System.in);
    private final BankingSystem bankingSystem;
    private Card card;

    public BankApp(String[] args) {
        if (args.length > 1 && args[0].equals("-fileName")) {
            DBManager.setUrl(args[1]);
        } else {
            DBManager.setUrl("default.db");
        }
        bankingSystem = new BankingSystem();
    }

    public void run() {
        while (true) {
            System.out.println("1. Create an account");
            System.out.println("2. Log into account");
            System.out.println("0. Exit");
            switch (scanner.nextLine()) {
                case "1":
                    bankingSystem.createNewAccount();
                    break;
                case "2":
                    logIn();
                    break;
                case "0":
                    System.out.println("\nBye!");
                    return;
            }
        }
    }

    private void logIn() {
        try {
            System.out.println("\nEnter your card number:");
            String  cardNumber = scanner.nextLine();
            System.out.println("Enter your PIN:");
            String pin = scanner.nextLine();
            card = bankingSystem.logIntoAccount(cardNumber, pin);
            accountMenu(card);
        } catch (Exception e) {
            System.out.println("\nWrong card number or PIN!\n");
        }
    }

    private void accountMenu(Card card) {
        while (true) {
            System.out.println("\n1. Balance");
            System.out.println("2. Add income");
            System.out.println("3. Do transfer");
            System.out.println("4. Close account");
            System.out.println("5. Log out");
            System.out.println("0. Exit");
            switch (scanner.nextLine()) {
                case "1":
                    System.out.printf("\nBalance: %d\n", card.getBalance());
                    break;
                case "2":
                    addIncome();
                    break;
                case "3":
                    doTransfer();
                    break;
                case "4":
                    closeAccount();
                    card = null;
                    return;
                case "5":
                    card = null;
                    System.out.println("\nYou have successfully logged out!\n");
                    return;
                case "0":
                    System.out.println("\nBye!");
                    System.exit(0);
                default:
                    System.out.println("Wrong input!");
            }
        }
    }

    private void closeAccount() {
        bankingSystem.closeAccount(this.card);
    }

    private void doTransfer() {
        try {
            System.out.println("\nTransfer\nEnter card number:");
            String number = scanner.nextLine();
            if (this.card.getCardNumber().equals(number)) {
                throw new RuntimeException("\nYou can't transfer money to the same account!\n");
            }
            bankingSystem.checkCard(number);
            System.out.println("Enter how much money you want to transfer:");
            int money = Integer.parseInt(scanner.nextLine());
            if (money > this.card.getBalance()) {
                throw new RuntimeException("Not enough money!\n");
            }
            bankingSystem.transfer(card, number, money);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void addIncome() {
        try {
            System.out.println("\nEnter income:");
            int income = Integer.parseInt(scanner.nextLine());
            this.card.addBalance(income);
            bankingSystem.updateBalance(card, income);
            System.out.println("Income was added!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
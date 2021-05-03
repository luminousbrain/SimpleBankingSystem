package ru.gontarenko.banking.entities;

public class Card {
    private int id;
    private final String cardNumber;
    private String pin;
    private int balance;

    public Card(String cardNumber, String pin) {
        this.cardNumber = cardNumber;
        this.pin = pin;
        balance = 0;
    }

    public Card(int id, String cardNumber, String pin, int balance) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = balance;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void addBalance(int income) {
        this.balance += income;
    }
}

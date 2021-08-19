package ru.gontarenko.banking;

import ru.gontarenko.banking.dao.CardDAO;
import ru.gontarenko.banking.dao.CardDAOImpl;
import ru.gontarenko.banking.entity.Card;

import java.util.Random;

public class BankingSystem {
    private static final Random random = new Random();

    private final CardDAO cardDAO;

    public BankingSystem() {
        this.cardDAO = new CardDAOImpl();
    }

    public Card logIntoAccount(String cardNumber, String pin) {
        Card bankAccount = cardDAO.findByCardNumber(cardNumber);
        if (bankAccount == null || !bankAccount.getPin().equals(pin)) {
            throw new RuntimeException("Account not found");
        }
        System.out.println("\nYou have successfully logged in!");
        return bankAccount;
    }

    public void createNewAccount() {
        System.out.println("\nYour card has been created");
        String cardNumber = createCardNumber();
        String pin = createPin();
        cardDAO.save(new Card(cardNumber, pin));
        System.out.println("Your card number:");
        System.out.println(cardNumber);
        System.out.println("Your card PIN:");
        System.out.println(pin + "\n");
    }

    private String createPin() {
        StringBuilder pin = new StringBuilder();
        while (pin.length() != 4) {
            pin.append(random.nextInt(10));
        }
        return pin.toString();
    }

    private String createCardNumber() {
        StringBuilder number = new StringBuilder("400000");
        while (number.length() != 15) {
            number.append(random.nextInt(10));
        }
        number.append(checkSum(number));
        if (checkLuhn(number.toString())) {
            return number.toString();
        }
        return createCardNumber();
    }

    public void closeAccount(Card card) {
        cardDAO.delete(card);
    }

    public void updateBalance(Card card, int income) {
        cardDAO.updateBalance(card, income);
    }

    public void checkCard(String number) {
        if (!checkLuhn(number)) {
            throw new RuntimeException("\nProbably you made a mistake in the card number. Please try again!");
        }
        if (cardDAO.findByCardNumber(number) == null) {
            throw new RuntimeException("\nSuch a card does not exist.");
        }
    }

    public void transfer(Card card, String number, int money) {
        if (cardDAO.transfer(card, number, money)) {
            card.setBalance(card.getBalance() - money);
            System.out.println("Success!");
        } else {
            System.out.println("Something goes wrong!");
        }
    }

    private boolean checkLuhn(String number) {
        int nDigits = number.length();
        int nSum = 0;
        boolean isSecond = false;
        for (int i = nDigits - 1; i >= 0; i--) {
            int d = number.charAt(i) - '0';
            if (isSecond){
                d = d * 2;
            }
            nSum += d / 10;
            nSum += d % 10;
            isSecond = !isSecond;
        }
        return nSum % 10 == 0;
    }

    private int checkSum(StringBuilder number) {
        long num = Long.parseLong(number.toString());
        int sum = 0;
        for (int i = 0; i < number.length(); i++) {
            long l = num % 10;
            if (i % 2 == 0) {
                l *= 2;
                if (l > 9) {
                    l -= 9;
                }
            }
            num = num / 10;
            sum += l;
        }
        return 10 - sum % 10;
    }
}
package ru.gontarenko.banking.dao;

import ru.gontarenko.banking.entities.Card;

public interface CardDAO {
    Card findByCardNumber(String cardNumber);

    void save(Card newCard);

    void delete(Card card);

    void updateBalance(Card card, int income);

    boolean transfer(Card card, String number, int money);
}

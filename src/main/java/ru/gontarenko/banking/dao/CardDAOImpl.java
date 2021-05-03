package ru.gontarenko.banking.dao;

import ru.gontarenko.banking.entities.Card;
import ru.gontarenko.banking.DBManager;

import java.sql.*;

public class CardDAOImpl implements CardDAO {
    private final String initTable = "CREATE TABLE IF NOT EXISTS card (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "number TEXT UNIQUE NOT NULL," +
            "pin TEXT NOT NULL," +
            "balance INTEGER DEFAULT 0" +
            ");";
    private final String findByNumber = "SELECT * FROM card WHERE number = (?);";
    private final String insertCard = "INSERT INTO card(number, pin, balance) VALUES (?, ?, ?);";
    private final String updateBalance = "UPDATE card SET balance = (balance + ?) WHERE number = ?;";
    private final String deleteCard = "DELETE FROM card WHERE number = (?)";


    public CardDAOImpl() {
        initTable();
    }

    private void initTable() {
        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(initTable)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Card findByCardNumber(String cardNumber) {
        Card card = null;
        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(findByNumber)) {
            preparedStatement.setString(1, cardNumber);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    card = new Card(
                            resultSet.getInt("id"),
                            resultSet.getString("number"),
                            resultSet.getString("pin"),
                            resultSet.getInt("balance")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return card;
    }

    @Override
    public void save(Card newCard) {
        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertCard)) {
            preparedStatement.setString(1, newCard.getCardNumber());
            preparedStatement.setString(2, newCard.getPin());
            preparedStatement.setInt(3, newCard.getBalance());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Card card) {
        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteCard)) {
            preparedStatement.setString(1, card.getCardNumber());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateBalance(Card card, int income) {
        try (Connection connection = DBManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateBalance)) {
            preparedStatement.setInt(1, income);
            preparedStatement.setString(2, card.getCardNumber());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean transfer(Card card, String number, int money) {
        try (Connection connection = DBManager.getConnection()) {
            connection.setAutoCommit(false);
            Savepoint savepoint = connection.setSavepoint();

            try (PreparedStatement statement = connection.prepareStatement(updateBalance)) {
                statement.setInt(1, -money);
                statement.setString(2, card.getCardNumber());
                statement.executeUpdate();

                statement.setInt(1, money);
                statement.setString(2, number);
                statement.executeUpdate();
            } catch (SQLException e) {
                connection.rollback(savepoint);
                return false;
            }

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}

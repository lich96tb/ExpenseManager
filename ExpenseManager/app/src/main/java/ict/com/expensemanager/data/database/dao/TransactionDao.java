package ict.com.expensemanager.data.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import ict.com.expensemanager.data.database.entity.Transaction;

/**
 * Created by nguyenanhtrung on 22/01/2018.
 */

@Dao
public interface TransactionDao {

    @Insert
    Long insertTransaction(Transaction transaction);

    @Query("SELECT * FROM `Transaction` WHERE id_transaction IN (:transactionId)")
    Transaction getTransactionById(int transactionId);

    @Insert
    void insertAll(Transaction... transactions);

    @Query("DELETE FROM 'transaction'")
    void deleteAllTransaction();

    @Query("SELECT transaction_name,price,time FROM 'transaction' WHERE id_user = :idUser  AND time BETWEEN :time1 AND :time2")
    List<timeTransaction> getByDate(int idUser,long time1, long time2);

    @Query("SELECT COUNT(*) FROM 'Transaction' WHERE id_event = :idevent")
    int transactionByEvent(int idevent);

    @Query("DELETE FROM 'Transaction' WHERE id_wallet = :idWallet")
    void deleteTransactionByIdWallet (int idWallet);

    @Query("SELECT SUM(price) FROM 'Transaction' WHERE id_event = :idevent")
    double transactionMoneyByEvent(int idevent);

    @Query("SELECT COUNT(*) FROM 'Transaction' WHERE id_category = :id_category")
    int transactionByCategory(int id_category);

    @Query("SELECT SUM(price) FROM 'Transaction' WHERE id_category = :id_category")
    double transactionMoneyByCategory(int id_category);

    @Query("SELECT COUNT(*) FROM 'Transaction' WHERE id_wallet = :id_wallet")
    int transactionByWallet(int id_wallet);

    @Query("SELECT SUM(price) FROM 'Transaction' WHERE id_wallet = :id_wallet")
    double transactionMoneyByWallet(int id_wallet);

    @Query("SELECT transaction_name,price,wallet_name,category_icon FROM Wallet JOIN `Transaction` ON " +
            "`Transaction`.id_wallet = Wallet.id_wallet LEFT JOIN Category ON `Transaction`.id_category = Category.id_category" +
            " WHERE `Transaction`.id_user = :idUser")
    List<TransactionWalletCategory> getTransactionByWalletCategory(int idUser);





    static class timeTransaction {
        String transaction_name;
        double price;
        long time;

        public String getTransaction_name() {
            return transaction_name;
        }

        public void setTransaction_name(String transaction_name) {
            this.transaction_name = transaction_name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }
    }

    class TransactionWalletCategory {
        String transaction_name;
        double price;
        String wallet_name;
        int category_icon;

        public int getCategory_icon() {
            return category_icon;
        }

        public void setCategory_icon(int category_icon) {
            this.category_icon = category_icon;
        }


        public String getTransaction_name() {
            return transaction_name;
        }

        public void setTransaction_name(String transaction_name) {
            this.transaction_name = transaction_name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getWallet_name() {
            return wallet_name;
        }

        public void setWallet_name(String wallet_name) {
            this.wallet_name = wallet_name;
        }

    }

    @Query("SELECT transaction_name FROM `Transaction` WHERE id_user IN (:userId)")
    String[] getTransactionName(int userId);

    @Query("SELECT * FROM `Transaction` WHERE id_user IN (:userId)")
    List<Transaction> getTransactionDetail(int userId);

    @Query("SELECT * FROM `Transaction` WHERE id_category = :idCategory")
    List<Transaction> getTransactionByCategory(int idCategory);

    @Query("SELECT * FROM `Transaction` WHERE id_wallet = :idWallet")
    List<Transaction> getTransactionByWallet(int idWallet);

    @Query("SELECT * FROM `Transaction` WHERE id_event IN (:idEvent)")
    List<Transaction> getTransactionByEvent(int idEvent);

    static class TransactionDetails {
        String transaction_name;
        double price;

        public String getTransaction_name() {
            return transaction_name;
        }

        public void setTransaction_name(String transaction_name) {
            this.transaction_name = transaction_name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }
}

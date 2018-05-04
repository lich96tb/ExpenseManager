package ict.com.expensemanager.data.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ict.com.expensemanager.data.database.entity.Wallet;

/**
 * <<<<<<< HEAD
 * <<<<<<< HEAD
 * Created by Tuan Huy on 1/22/2018.
 */
@Dao
public interface WalletDao {
    @Query("SELECT * FROM Wallet WHERE id_user =:id_user")
    List<Wallet> getAllWallets(int id_user);

    @Insert
    void insertAll(Wallet... wallets);

    @Query("DELETE FROM Wallet WHERE id_wallet LIKE :id_wallet")
    void deleleByID(int id_wallet);

    @Query("UPDATE Wallet SET wallet_name =:walletname , balance =:walletbalance WHERE id_wallet LIKE :id_wallet")
    void updateByID(int id_wallet, String walletname, double walletbalance);

    @Query("SELECT COUNT(*) FROM Wallet WHERE wallet_name LIKE :name")
    int getCountByName(String name);


    @Query("SELECT * FROM Wallet WHERE id_user IN (:userId)")
    List<Wallet> getWallets(int userId);

    @Update
    int updateWallet(Wallet wallet);

    @Query("UPDATE Wallet SET balance = :balance")
    int updateWalletBalance(double balance);

    @Query("SELECT balance FROM Wallet WHERE id_wallet IN (:walletId)")
    double getWalletBalanceById(int walletId);


    @Query("SELECT wallet_name FROM Wallet WHERE id_wallet LIKE (:walletId)")
    String getWalletNameReport(int walletId);


    @Query("SELECT * FROM Wallet WHERE id_wallet = :idwallet")
    Wallet getWalletByIdWallet(int idwallet);

    @Query("SELECT * FROM Wallet")
    List<Wallet> getAllWallet();

    @Query("SELECT wallet_name FROM Wallet WHERE id_user IN (:userId)")
    String[] getWalletName(int userId);

    @Query("SELECT id_wallet FROM Wallet WHERE wallet_name LIKE (:walletName)")
    int getWalletId(String walletName);


    @Query("SELECT wallet_name,balance FROM Wallet WHERE id_user IN (:userId)")
    List<WalletDetails> getWalletDetail(int userId);

    static class WalletDetails {
        String wallet_name;
        double balance;

        public String getWallet_name() {
            return wallet_name;
        }

        public void setWallet_name(String wallet_name) {
            this.wallet_name = wallet_name;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }
    }
}

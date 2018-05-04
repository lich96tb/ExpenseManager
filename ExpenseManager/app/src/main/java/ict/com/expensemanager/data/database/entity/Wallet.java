package ict.com.expensemanager.data.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by PHAMHOAN on 1/18/2018.
 */
@Entity(tableName = "Wallet", foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "id_user",
        childColumns = "id_user"))
public class Wallet implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_wallet")
    private int idWallet;
    @ColumnInfo(name = "wallet_name")
    private String walletName;
    @ColumnInfo(name = "balance")
    private double balance;
    @ColumnInfo(name = "id_user")
    private int idUser;

    public Wallet() {

    }

    public Wallet(String walletName, double balance, int idUser) {
        this.walletName = walletName;
        this.balance = balance;
        this.idUser = idUser;
    }

    @Ignore
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getIdWallet() {
        return idWallet;
    }

    public void setIdWallet(int idWallet) {
        this.idWallet = idWallet;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
}

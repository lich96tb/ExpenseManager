package ict.com.expensemanager.data.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by PHAMHOAN on 1/18/2018.
 */
@Entity(tableName = "Transaction")
public class Transaction {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_transaction")
    private int idTransaction;
    @ColumnInfo(name = "transaction_name")
    private String transactionName;
    @ColumnInfo(name = "id_user")
    @ForeignKey(entity = User.class, parentColumns = "id_user", childColumns = "id_user")
    private int idUser;
    @ColumnInfo(name = "id_event")
    @ForeignKey(entity = Event.class, parentColumns = "id_event", childColumns = "id_event")
    private int idEvent;
    @ColumnInfo(name = "id_wallet")
    @ForeignKey(entity = Wallet.class, parentColumns = "id_wallet", childColumns = "id_wallet")
    private int idWallet;
    @ColumnInfo(name = "id_category")
    @ForeignKey(entity = Category.class, parentColumns = "id_category", childColumns = "id_category",onDelete = ForeignKey.CASCADE)
    private int idCategory;
    @ColumnInfo(name = "price")
    private double price;
    @ColumnInfo(name = "time")
    private long time;

    public int getIdTransaction() {
        return idTransaction;
    }

    public void setIdTransaction(int idTransaction) {
        this.idTransaction = idTransaction;
    }

    public String getTransactionName() {
        return transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public int getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
    }

    public int getIdWallet() {
        return idWallet;
    }

    public void setIdWallet(int idWallet) {
        this.idWallet = idWallet;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
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

package ict.com.expensemanager.data.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by PHAMHOAN on 1/18/2018.
 */
@Entity(tableName = "TransactionPartner")
public class TransactionPartner {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_transaction_partner")
    private int idTransactionPartner;
    @ColumnInfo(name = "id_partner")
    @ForeignKey(entity = Partner.class, parentColumns = "id_partner", childColumns = "id_partner")
    private int idPartner;
    @ColumnInfo(name = "id_transaction")
    @ForeignKey(entity = Transaction.class, parentColumns = "id_transaction", childColumns = "id_transaction")
    private int idTransaction;

    public int getIdTransactionPartner() {
        return idTransactionPartner;
    }

    public void setIdTransactionPartner(int idTransactionPartner) {
        this.idTransactionPartner = idTransactionPartner;
    }

    public int getIdPartner() {
        return idPartner;
    }

    public void setIdPartner(int idPartner) {
        this.idPartner = idPartner;
    }

    public int getIdTransaction() {
        return idTransaction;
    }

    public void setIdTransaction(int idTransaction) {
        this.idTransaction = idTransaction;
    }
}

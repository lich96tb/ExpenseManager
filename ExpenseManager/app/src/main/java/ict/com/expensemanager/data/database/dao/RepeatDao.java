package ict.com.expensemanager.data.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import ict.com.expensemanager.data.database.entity.Repeat;

/**
 * Created by nguyenanhtrung on 22/01/2018.
 */

@Dao
public interface RepeatDao {

    @Insert
    long insertRepeatMode(Repeat repeat);

    @Query("SELECT * FROM Repeat WHERE id_transaction IN (:transactionId) ")
    Repeat getRepeatByTransactionId(int transactionId);

}

package ict.com.expensemanager.data.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

import ict.com.expensemanager.data.database.entity.TransactionPartner;

/**
 * Created by PHAMHOAN on 1/26/2018.
 */

@Dao
public interface TransactionPartnerDao {

    @Insert
    Long insertTransactionPartner(TransactionPartner transactionPartner);
}


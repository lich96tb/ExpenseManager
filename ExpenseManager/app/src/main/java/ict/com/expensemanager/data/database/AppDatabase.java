package ict.com.expensemanager.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import ict.com.expensemanager.data.database.dao.CategoryDao;
import ict.com.expensemanager.data.database.dao.EventDao;
import android.content.Context;

import ict.com.expensemanager.data.database.dao.CategoryDao;
import ict.com.expensemanager.data.database.dao.EventDao;
import ict.com.expensemanager.data.database.dao.PartnerDao;
import ict.com.expensemanager.data.database.dao.RepeatDao;

import ict.com.expensemanager.data.database.dao.RepeatTypeDao;
import ict.com.expensemanager.data.database.dao.TransactionDao;
import ict.com.expensemanager.data.database.dao.TransactionPartnerDao;
import ict.com.expensemanager.data.database.dao.TransactionDao;
import ict.com.expensemanager.data.database.dao.UserDao;
import ict.com.expensemanager.data.database.dao.WalletDao;
import ict.com.expensemanager.data.database.entity.Category;
import ict.com.expensemanager.data.database.entity.Event;
import ict.com.expensemanager.data.database.entity.Partner;
import ict.com.expensemanager.data.database.entity.Repeat;
import ict.com.expensemanager.data.database.entity.RepeatType;
import ict.com.expensemanager.data.database.entity.Transaction;
import ict.com.expensemanager.data.database.entity.TransactionPartner;
import ict.com.expensemanager.data.database.entity.User;
import ict.com.expensemanager.data.database.entity.Wallet;
import ict.com.expensemanager.util.AppKey;

/**
 * Created by PHAMHOAN on 1/18/2018.
 */

@Database(entities = {User.class, Category.class, Event.class, Partner.class,
        Repeat.class, RepeatType.class,
        Transaction.class,
        TransactionPartner.class, Wallet.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();

    public abstract WalletDao walletDao();

    public abstract CategoryDao categoryDao();

    public abstract EventDao eventDao();

    public abstract RepeatDao repeatDao();

    public abstract RepeatTypeDao repeatTypeDao();

    public abstract TransactionDao transactionDao();

    public abstract PartnerDao partnerDao();

    public abstract TransactionPartnerDao transactionPartnerDao();


    private static AppDatabase appDatabase = null;

    public static AppDatabase getInstance(Context applicationContext) {

        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(applicationContext, AppDatabase.class, AppKey.DATABASE_NAME).build();
            appDatabase.getOpenHelper().getWritableDatabase();
        }

        return appDatabase;
    }


}

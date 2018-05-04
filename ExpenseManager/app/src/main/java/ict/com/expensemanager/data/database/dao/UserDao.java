package ict.com.expensemanager.data.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

import android.arch.persistence.room.Query;

import java.util.List;
import ict.com.expensemanager.data.database.entity.User;

import android.arch.persistence.room.Query;

import java.util.List;

import ict.com.expensemanager.data.database.entity.User;

/**
 * Created by PHAMHOAN on 1/18/2018.
 */

@Dao
public interface UserDao {
    @Query("select * from User")
    List<User> loadAllUsers();
    @Query("select * from User where user_name=:name and password=:pass")
    List<User> SignInSql(String name,String pass);
    @Query("select *from User where user_name=:name")
    List<User>CheckName(String name);

    @Insert
    void insertUser(User...users);

    @Insert
    void addUser(User...user);
    @Insert
    void insertAll(User...users);

    @Query("SELECT * FROM User")
    List<User> getAllUser();

}

package ict.com.expensemanager.data.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import ict.com.expensemanager.data.database.entity.RepeatType;

/**
 * Created by PHAMHOAN on 1/26/2018.
 */

@Dao
public interface RepeatTypeDao {

    @Query("SELECT * FROM REPEATTYPE")
    List<RepeatType> getRepeatTypes();

    @Insert
    void addRepeatType(RepeatType...repeatTypes);

    @Query("SELECT COUNT(*) FROM RepeatType")
    int getCountRepeattype();
}
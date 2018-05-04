package ict.com.expensemanager.data.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by PHAMHOAN on 1/18/2018.
 */
@Entity(tableName = "RepeatType")
public class RepeatType {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_repeat_type")
    private int idRepeatType;
    @ColumnInfo(name = "repeat_type_name")
    private String repeat_type_name;

    public RepeatType(int idRepeatType, String repeat_type_name) {
        this.idRepeatType = idRepeatType;
        this.repeat_type_name = repeat_type_name;
    }

    public RepeatType() {
    }


    public int getIdRepeatType() {
        return idRepeatType;
    }

    public void setIdRepeatType(int idRepeatType) {
        this.idRepeatType = idRepeatType;
    }

    public String getRepeat_type_name() {
        return repeat_type_name;
    }

    public void setRepeat_type_name(String repeat_type_name) {
        this.repeat_type_name = repeat_type_name;
    }


}

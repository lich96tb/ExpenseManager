package ict.com.expensemanager.data.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by PHAMHOAN on 1/18/2018.
 */
@Entity(tableName = "Repeat")
public class Repeat {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_repeat")
    private int idRepeat;
    @ColumnInfo(name = "repeat_name")
    private String repeatName;
    @ColumnInfo(name = "number")
    private int number;
    @ColumnInfo(name = "id_transaction")
    @ForeignKey(entity = Transaction.class, parentColumns = "id_transaction", childColumns = "id_transaction")
    private int idTransaction;
    @ColumnInfo(name = "id_repeat_type")
    @ForeignKey(entity = RepeatType.class, parentColumns = "id_repeat_type", childColumns = "id_repeat_type")
    private int idRepeatType;
   ;
    @ColumnInfo (name = "time_repeat")
    private String timeRepeat;


    public int getIdRepeat() {
        return idRepeat;
    }

    public void setIdRepeat(int idRepeat) {
        this.idRepeat = idRepeat;
    }

    public String getRepeatName() {
        return repeatName;
    }

    public void setRepeatName(String repeatName) {
        this.repeatName = repeatName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getIdTransaction() {
        return idTransaction;
    }

    public void setIdTransaction(int idTransaction) {
        this.idTransaction = idTransaction;
    }

    public int getIdRepeatType() {
        return idRepeatType;
    }

    public void setIdRepeatType(int idRepeatType) {
        this.idRepeatType = idRepeatType;
    }

    public String getTimeRepeat() {
        return timeRepeat;
    }

    public void setTimeRepeat(String timeRepeat) {
        this.timeRepeat = timeRepeat;
    }
}

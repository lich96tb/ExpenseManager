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

@Entity(tableName = "Event", foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "id_user",
        childColumns = "id_user"))
public class Event implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_event")
    private int idEvent;

    public Event(String eventName, double expectedMoney, double balance, boolean status, int idUser) {
        this.eventName = eventName;
        this.expectedMoney = expectedMoney;
        this.balance = balance;
        this.status = status;
        this.idUser = idUser;
    }

    @ColumnInfo(name = "event_name")

    private String eventName;
    @ColumnInfo(name = "expected_money")
    private double expectedMoney;
    @ColumnInfo(name = "balance")
    private double balance;
    @ColumnInfo(name = "status")
    private boolean status;
    @ColumnInfo(name = "id_user")
    private int idUser;

    @Ignore
    private boolean isSelected = false;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(int idEvent) {
        this.idEvent = idEvent;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public double getExpectedMoney() {
        return expectedMoney;
    }

    public void setExpectedMoney(double expectedMoney) {
        this.expectedMoney = expectedMoney;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
}

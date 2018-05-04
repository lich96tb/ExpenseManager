package ict.com.expensemanager.data.database.dao;

import android.arch.persistence.room.Dao;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;

import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import ict.com.expensemanager.data.database.entity.Event;

@Dao
public interface EventDao {
    @Insert
    void addEvent(Event... event);

    @Query("SELECT * FROM Event WHERE status = 0 AND id_user = :idUser")
    List<Event> getFinishEvent(int idUser);

    @Query("SELECT * FROM Event WHERE id_event = :idEvent")
    Event getEventById(int idEvent);


    @Query("SELECT COUNT(*) FROM Event WHERE event_name LIKE :name AND id_user = :idUser")
    int getCountName(String name, int idUser);

    @Delete
    void deleteEvent(Event... events);

    @Update
    void updateEvent(Event... events);


    @Query("SELECT * FROM EVENT WHERE id_user IN (:userId) AND status = 1")
    List<Event> getEventsStarting(int userId);


    @Query("UPDATE Event SET balance = :balance")
    int updateEventBalance(double balance);

    @Query("SELECT balance FROM Event WHERE id_event IN (:eventId)")
    double getEventBalanceById(int eventId);

    @Query("SELECT * FROM Event")
    List<Event> getAllEvent();

    @Query("SELECT id_event FROM Event WHERE event_name LIKE (:eventName)")
    int getEventId(String eventName);


    @Query("SELECT event_name FROM Event WHERE id_event LIKE (:eventId)")
    String getEventNameReport(int eventId);

    @Query("SELECT event_name FROM Event WHERE id_user LIKE (:userId)")
    String[] getEventName(int userId);

    @Query("SELECT event_name,balance FROM Event WHERE id_user IN (:userId)")
    List<EventDao.EventDetails> getEventDetail(int userId);

    static class EventDetails {
        String event_name;
        double balance;

        public String getEvent_name() {
            return event_name;
        }

        public void setEvent_name(String event_name) {
            this.event_name = event_name;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }
    }

}

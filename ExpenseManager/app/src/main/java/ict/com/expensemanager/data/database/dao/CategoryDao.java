package ict.com.expensemanager.data.database.dao;

import android.arch.persistence.room.Dao;


import android.arch.persistence.room.Insert;

import android.arch.persistence.room.Query;

import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import ict.com.expensemanager.data.database.entity.Category;


@Dao
public interface CategoryDao {
    @Query("SELECT * FROM Category WHERE id_user =:id_user")
    List<Category> getAllCategories(int id_user);

    @Insert
    void insertAll(Category... categories);

    @Query("DELETE FROM Category WHERE id_category LIKE :id_category")
    void deleleByID(int id_category);

    @Query("UPDATE Category SET category_name =:category_name , category_icon =:category_icon WHERE id_category LIKE :id_category")
    void updateByID(int id_category, String category_name, int category_icon);

    @Query("SELECT COUNT(*) FROM Category WHERE category_name LIKE :name")
    int getCountByName(String name);

    @Query("SELECT id_category FROM Category WHERE category_name LIKE (:categoryName)")
    int getCategoryId(String categoryName);

    @Query("SELECT * FROM Category WHERE id_category = :idcategory")
    Category getCategoryByIdCategory(int idcategory);

    @Query("SELECT category_name FROM Category WHERE id_user LIKE (:userId)")
    String[] getCategorName(int userId);

    @Query("SELECT category_name FROM Category WHERE id_category LIKE (:categoryId)")
    String getCategoryNameReport(int categoryId);

    @Query("SELECT * FROM Category WHERE id_user IN (:userId)")
    List<Category> getCategories(int userId);

    @Query("SELECT category_name,price FROM Category INNER JOIN 'transaction' ON Category.id_user = 'transaction'.id_user;")
    List<categoryTransaction> getCategory();

    static class categoryTransaction {
        String category_name;
        double price;

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }
}

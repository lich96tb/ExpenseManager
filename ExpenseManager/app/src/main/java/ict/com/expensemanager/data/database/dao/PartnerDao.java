package ict.com.expensemanager.data.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import ict.com.expensemanager.data.database.entity.Partner;

/**
 * Created by PHAMHOAN on 1/26/2018.
 */

@Dao
public interface PartnerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertPartners(Partner partner);

    @Query("SELECT id_partner FROM Partner WHERE partner_name LIKE :partnerName")
    int isPartnerAlreadyExists(String partnerName);
}

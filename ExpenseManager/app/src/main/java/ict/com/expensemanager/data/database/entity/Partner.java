package ict.com.expensemanager.data.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by PHAMHOAN on 1/18/2018.
 */
@Entity (tableName = "Partner")
public class Partner implements Serializable {
    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name = "id_partner")
    private int idPartner;
    @ColumnInfo (name = "partner_name")
    private String partnerName;



    public int getIdPartner() {
        return idPartner;
    }

    public void setIdPartner(int idPartner) {
        this.idPartner = idPartner;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    @Override
    public String toString() {
        return partnerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Partner)) return false;

        Partner partner = (Partner) o;

        if (getIdPartner() != partner.getIdPartner()) return false;
        return getPartnerName() != null ? getPartnerName().equals(partner.getPartnerName()) : partner.getPartnerName() == null;
    }

    @Override
    public int hashCode() {
        int result = getIdPartner();
        result = 31 * result + (getPartnerName() != null ? getPartnerName().hashCode() : 0);
        return result;
    }
}

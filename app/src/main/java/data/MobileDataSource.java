package data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rashid on 17-Mar-17.
 */

public class MobileDataSource {
    private SQLiteDatabase database;
    public MobileDataSource() {
        database = ServiceDeskDataSource.GetDbAccess().open();
    }
    public boolean SaveMobile(String[] mobileNumbers,String customerId) {
        boolean status=true;
        for (String mobile : mobileNumbers) {
            if (mobile != null && !mobile.equals("")) {
                ContentValues c = new ContentValues();
                c.put("mobile", mobile);
                c.put("customer_id", customerId);
                long result = database.insert("mobile_numbers", null, c);
                if (result < 0) {
                    status = false;
                    break;
                }
            }
        }
        return status;
    }


    public String[] GetAllMobiles(String mobile) {
        String[] mobileNumbers=new String[3];
        int customerId=0;
        Cursor result = database.rawQuery("select customer_id from mobile_numbers where mobile='"+mobile+"';", null);
        if (result.getCount() > 0) {
            while (result.moveToNext()) {
                customerId =result.getInt(result.getColumnIndex("customer_id"));
                break;
            }
        }
        result.close();
        if(customerId>0) {
            result = database.rawQuery("select mobile from mobile_numbers where customer_id='" + customerId + "';", null);
            if (result.getCount() > 0) {
                int i=0;
                while (result.moveToNext()) {
                    mobileNumbers[i] = result.getString(result.getColumnIndex("mobile"));
                    i++;
                }
            }
        }
        result.close();
        return mobileNumbers;
    }


    public boolean DeleteMobile(String customerId)
    {
        boolean status=false;
        int result=database.delete("mobile_numbers","customer_id='"+customerId+"'",null);
        if (result > -1) {
            status = true;
        }
        return status;
    }

    public List<String> GetBrandNames() {
        List<String> brandnames = new ArrayList<String>();
        Cursor result = database.rawQuery("select name from brand_names", null);
        if (result.getCount() > 0) {
            while (result.moveToNext()) {
                brandnames.add(result.getString(result.getColumnIndex("name")));
            }
        }
        return brandnames;
    }
}

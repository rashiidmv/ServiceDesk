package data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Rashid on 15-Mar-17.
 */

public class LocationDataSource {
    private SQLiteDatabase database;

    public LocationDataSource() {
        database = ServiceDeskDataSource.GetDbAccess().open();
    }

    public void SaveLocation(String location) {
        ContentValues c = new ContentValues();
        c.put("name", location);
        database.insert("location_details", null, c);
    }

    public int getProductPrice(String productName) {
        int productPrice = 0;
        Cursor cursor = database.rawQuery("select unit_price from product_details where name='" + productName + "';", null);
        while (cursor.moveToNext()) {
            productPrice = cursor.getInt(cursor.getColumnIndex("unit_price"));
        }
        return productPrice;
    }

    public List<String> GetLocations() {
        List<String> locations = new ArrayList<String>();
        Cursor result = database.rawQuery("select name from location_details", null);
        if (result.getCount() > 0) {
            while (result.moveToNext()) {
                locations.add(result.getString(result.getColumnIndex("name")));
            }
        }
        return locations;
    }
}

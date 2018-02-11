package data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rashid on 17-Mar-17.
 */

public class AddressDataSource {
    private SQLiteDatabase database;

    public AddressDataSource() {
        database = ServiceDeskDataSource.GetDbAccess().open();
    }

    public void SaveBrandName(String brandName) {
        ContentValues c = new ContentValues();
        c.put("name", brandName);
        database.insert("brand_names", null, c);
    }

    public long SaveAddress(Address address) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("customer_id", address.customer_id);
        contentValues.put("door_number", address.door_number);
        contentValues.put("building_number", address.building_number);
        contentValues.put("house_name", address.house_name);
        contentValues.put("floor_number", address.floor_number);
        contentValues.put("cross_number", address.cross_number);
        contentValues.put("main_number", address.main_number);
        contentValues.put("landmark", address.landmark);
        contentValues.put("location_name", address.location_name);
        long result = database.insert("address_details", null, contentValues);
        return result;
    }

    public boolean UpdateAddress(Address address) {
        boolean status = false;
        ContentValues contentValues = new ContentValues();
        contentValues.put("customer_id", address.customer_id);
        contentValues.put("door_number", address.door_number);
        contentValues.put("building_number", address.building_number);
        contentValues.put("house_name", address.house_name);
        contentValues.put("floor_number", address.floor_number);
        contentValues.put("cross_number", address.cross_number);
        contentValues.put("main_number", address.main_number);
        contentValues.put("landmark", address.landmark);
        contentValues.put("location_name", address.location_name);
        int result = database.update("address_details", contentValues, "customer_id='" + address.customer_id + "'", null);
        if (result > 0)
            status = true;
        return status;
    }

    public Address GetAddress(String customer_id) {
        Address address = new Address();
        Cursor cursor = database.rawQuery("select name from address_details where customer_id='" + customer_id + "'", null);
        if (cursor.getCount() > 0) {
            address.customer_id = customer_id;
            address.building_number = cursor.getString(cursor.getColumnIndex("building_number"));
            address.house_name = cursor.getString(cursor.getColumnIndex("house_name"));
            address.floor_number = cursor.getInt(cursor.getColumnIndex("floor_number"));
            address.cross_number = cursor.getString(cursor.getColumnIndex("cross_number"));
            address.main_number = cursor.getString(cursor.getColumnIndex("main_number"));
            address.landmark = cursor.getString(cursor.getColumnIndex("landmark"));
            address.location_name = cursor.getString(cursor.getColumnIndex("location_name"));
        }
        return address;
    }

    public static class Address {
        public String customer_id;
        public String door_number;
        public String building_number;
        public String house_name;
        public int floor_number;
        public String cross_number;
        public String main_number;
        public String landmark;
        public String location_name;
    }
}


package data;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Calendar;

public class MySQLiteHelper extends SQLiteOpenHelper {

    //Database name and versoin
    private static final String DATABASE_NAME = "ServiceManager.db";
    private static final int DATABASE_VERSION = 2;

    //BrandName table creation
    public static final String TABLE_Login = "login_details";
    public static final String COLUMN_username = "username";
    public static final String COLUMN_password = "password";
    public static final String COLUMN_usertype = "usertype";
    private static final String CREATE_TABLE_Login = "create table "
            + TABLE_Login + "( " + COLUMN_username
            + " text primary key, " + COLUMN_password
            + " text not null, "+COLUMN_usertype
            + " text not null);";


    //BrandName table creation
    public static final String TABLE_BrandName = "product_details";
    public static final String COLUMN_Name = "product_name";
    public static final String COLUMN_UnitPrice = "unit_price";
    private static final String CREATE_TABLE_BrandName = "create table "
            + TABLE_BrandName + "( " + COLUMN_Name
            + " text primary key, " + COLUMN_UnitPrice
            + " integer not null);";

    //LocationCode table creation
    public static final String TABLE_LocationCode = "location_details";
    public static final String COLUMN_LocationId = "_id";
    public static final String COLUMN_Location_Name = "name";
    private static final String CREATE_TABLE_LocationCode = "create table "
            + TABLE_LocationCode + "( " + COLUMN_LocationId
            + " integer primary key autoincrement, " + COLUMN_Location_Name
            + " text not null);";

    //CustomerDetails table creation
    public static final String TABLE_CustomerDetails = "customer_details";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CustomerName = "name";
    public static final String COLUMN_NumberOfCanes = "maximum_cane";
    public static final String COLUMN_PreferedBrand = "brand_name";
    public static final String COLUMN_DepositeAmount="deposite_amount";
    public static final String COLUMN_DepositeGivenDate = "deposite_given_date";
    private static final String CREATE_TABLE_CustomerDetails = "create table "
            + TABLE_CustomerDetails + "( " + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_CustomerName
            + " text not null, " + COLUMN_NumberOfCanes
            + " integer not null, " + COLUMN_DepositeAmount
            + " integer not null, " + COLUMN_DepositeGivenDate
            + " text, " + COLUMN_PreferedBrand
            + " text not null,"
            + " FOREIGN KEY(brand_name) REFERENCES product_details(name));";

    //MobileNumbers table creation
    public static final String TABLE_MobileNumbers = "mobile_numbers";
    public static final String COLUMN_CustomerIdInMobile = "customer_id";
    public static final String COLUMN_MobileNumber = "mobile";
    private static final String CREATE_TABLE_MobileNumbers = "create table "
            + TABLE_MobileNumbers + "( " + COLUMN_CustomerIdInMobile
            + " integer not null, " + COLUMN_MobileNumber
            + " text not null,"
            + " FOREIGN KEY(customer_id) REFERENCES customer_details(_id));";

    //AddressDetails table creation
    public static final String TABLE_AddressDetails = "address_details";
    public static final String COLUMN_CustomerIdInAddress = "customer_id";
    public static final String COLUMN_BuildingNumber = "building_number";
    public static final String COLUMN_HouseName = "house_name";
    public static final String COLUMN_FloorNumber = "floor_number";
    public static final String COLUMN_DoorNumber = "door_number";
    public static final String COLUMN_CrossNumber = "cross_number";
    public static final String COLUMN_MainNumber = "main_number";
    public static final String COLUMN_Landmark = "landmark";
    public static final String COLUMN_LocationName = "location_name";
    private static final String CREATE_TABLE_AddressDetails = "create table "
            + TABLE_AddressDetails + "( " + COLUMN_CustomerIdInAddress
            + " integer not null, " + COLUMN_BuildingNumber
            + " text, " + COLUMN_HouseName
            + " text, " + COLUMN_FloorNumber
            + " integer, " + COLUMN_DoorNumber
            + " text, " + COLUMN_CrossNumber
            + " text, " + COLUMN_MainNumber
            + " text, " + COLUMN_Landmark
            + " text, " + COLUMN_LocationName
            + " integer not null,"
            + " FOREIGN KEY(customer_id) REFERENCES customer_details(_id)"
            + " FOREIGN KEY(location_name) REFERENCES location_details(name));";

    //OrderStatus table creation
    public static final String TABLE_OrderStatus = "order_status";
    public static final String COLUMN_Status = "status";
    private static final String CREATE_TABLE_OrderStatus = "create table "
            + TABLE_OrderStatus + "( " + COLUMN_Status
            + " text primary key);";


    //OrderDetails table creation
    public static final String TABLE_OrderDetails = "order_details";
    public static final String COLUMN_Id = "_id";
    public static final String COLUMN_CustomerId = "customer_id";
    public static final String COLUMN_Mobile = "mobile";
    public static final String COLUMN_Quantity = "quantity";
    public static final String COLUMN_BrandName = "brand_name";
    public static final String COLUMN_OrderStatus = "order_status";
    public static final String COLUMN_ExpectedDelivery = "expected_date_time";
    public static final String COLUMN_OrderDateTime = "order_date_time";
    public static final String COLUMN_ConfirmedDateTime = "confirm_date_time";
    public static final String COLUMN_DeliveredDateTime = "delivered_date_time";
    public static final String COLUMN_Mode = "payment_mode";
    public static final String COLUMN_Amount = "amount";
    private static final String CREATE_TABLE_OrderDetails = "create table "
            + TABLE_OrderDetails + "( " + COLUMN_Id
            + " integer primary key autoincrement, " + COLUMN_CustomerId
            + " integer not null, " + COLUMN_Mobile
            + " text not null, " + COLUMN_Quantity
            + " text not null, " + COLUMN_BrandName
            + " text not null, " + COLUMN_OrderStatus
            + " text, " + COLUMN_ExpectedDelivery
            + " text not null, " + COLUMN_OrderDateTime
            + " text, " + COLUMN_ConfirmedDateTime
            + " text, " + COLUMN_DeliveredDateTime
            + " text, " + COLUMN_Mode
            + " text, " + COLUMN_Amount
            + " integer not null,"
            + " FOREIGN KEY(customer_id) REFERENCES customer_details(_id)"
            + " FOREIGN KEY(brand_name) REFERENCES product_details(name)"
            + " FOREIGN KEY(order_status) REFERENCES order_status(status));";

    //new_customers table creation
    public static final String TABLE_NewCustomer = "new_customers";
    public static final String COLUMN_NewMobile = "mobile";
    public static final String COLUMN_RegistrationStatus = "status";
    public static final String COLUMN_NumberOfAttempts="attempts";     //Maximum attempts 5
    private static final String CREATE_TABLE_NewCustomer = "create table "
            + TABLE_NewCustomer + "( " + COLUMN_NewMobile
            + " integer not null, " + COLUMN_RegistrationStatus
            + " text not null, " +COLUMN_NumberOfAttempts
            + " integer not null)";



    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_CustomerDetails);
        database.execSQL(CREATE_TABLE_MobileNumbers);
        database.execSQL(CREATE_TABLE_LocationCode);
        database.execSQL(CREATE_TABLE_AddressDetails);
        database.execSQL(CREATE_TABLE_OrderStatus);
        database.execSQL(CREATE_TABLE_BrandName);
        database.execSQL(CREATE_TABLE_OrderDetails);
        database.execSQL(CREATE_TABLE_Login);
        database.execSQL(CREATE_TABLE_NewCustomer);
        FillDummyData(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CustomerDetails);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MobileNumbers);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LocationCode);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AddressDetails);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OrderStatus);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BrandName);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OrderDetails);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Login);
        db.execSQL("DROP TABLE IF EXISTS " +TABLE_NewCustomer);
        onCreate(db);
    }

    private void FillDummyData(SQLiteDatabase database) {

        ContentValues product_details = new ContentValues();
        product_details.put("product_name", "Bisleri");
        product_details.put("unit_price", "50");
        database.insert("product_details", null, product_details);

        product_details = new ContentValues();
        product_details.put("product_name", "Aquafina");
        product_details.put("unit_price", 30);
        database.insert("product_details", null, product_details);

        ContentValues customer_details = new ContentValues();
        customer_details.put("name", "Rashid");
        customer_details.put("maximum_cane", 2);
        customer_details.put("deposite_amount",150);
        Calendar calendar = Calendar.getInstance();
        customer_details.put("deposite_given_date", calendar.getTime().toString());

        customer_details.put("brand_name", "Bisleri");
        database.insert("customer_details", null, customer_details);

        customer_details = new ContentValues();
        customer_details.put("name", "Hiba");
        customer_details.put("maximum_cane", 1);
        customer_details.put("deposite_amount",200);
        customer_details.put("deposite_given_date", calendar.getTime().toString());
        customer_details.put("brand_name", "Aquafina");
        database.insert("customer_details", null, customer_details);


        ContentValues mobile_numbers = new ContentValues();
        mobile_numbers.put("customer_id", 1);
        mobile_numbers.put("mobile", "6505556789");
        database.insert("mobile_numbers", null, mobile_numbers);

        mobile_numbers = new ContentValues();
        mobile_numbers.put("customer_id", 1);
        mobile_numbers.put("mobile", "9620370920");
        database.insert("mobile_numbers", null, mobile_numbers);

        mobile_numbers = new ContentValues();
        mobile_numbers.put("customer_id", 2);
        mobile_numbers.put("mobile", "9539842025");
        database.insert("mobile_numbers", null, mobile_numbers);

        ContentValues location_details = new ContentValues();
        location_details.put("_id", 1);
        location_details.put("name", "Domlur Layout");
        database.insert("location_details", null, location_details);

        ContentValues address_details = new ContentValues();
        address_details.put("customer_id", 1);
        address_details.put("building_number", "223");
        address_details.put("house_name", "Chikkanna Home");
        address_details.put("floor_number", 21);
        address_details.put("door_number", "100");
        address_details.put("cross_number", "5");
        address_details.put("main_number", "1");
        address_details.put("landmark", "AVJ Medical store opposite");
        address_details.put("location_name", "Domlur Layout");
        database.insert("address_details", null, address_details);

        address_details = new ContentValues();
        address_details.put("customer_id", 2);
        address_details.put("building_number", "223");
        address_details.put("house_name", "Chikkanna Home");
        address_details.put("floor_number", 24);
        address_details.put("door_number", "100");
        address_details.put("cross_number", "5");
        address_details.put("main_number", "1");
        address_details.put("landmark", "AVJ Medical store opposite");
        address_details.put("location_name", "Domlur Layout");
        database.insert("address_details", null, address_details);

        ContentValues login_details=new ContentValues();
        login_details.put("username","rashid");
        login_details.put("password","rashid");
        login_details.put("usertype","admin");
        database.insert(TABLE_Login,null,login_details);


    }
}
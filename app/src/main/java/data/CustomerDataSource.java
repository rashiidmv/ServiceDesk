package data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import wds.servicedesk.Utility;

/**
 * Created by Rashid on 06-Mar-17.
 */

public class CustomerDataSource {
    private SQLiteDatabase database;
    //  private MySQLiteHelper dbHelper;
    private String[] allColumns = {MySQLiteHelper.COLUMN_ID,
            MySQLiteHelper.COLUMN_Name,
            MySQLiteHelper.COLUMN_NumberOfCanes,
    };

    public CustomerDataSource() {
        database = ServiceDeskDataSource.GetDbAccess().open();
    }

    public long SaveCustomer(Customer customer) {
        ContentValues c = new ContentValues();
        c.put("name", customer.name);
        c.put("maximum_cane", customer.cane);
        c.put("brand_name", customer.brand);
        c.put("deposite_amount",0);
        c.put("deposite_given_date","Not given deposite yet.");
        long result = database.insert("customer_details", null, c);
        return result;
    }

    public boolean UpdateCustomer(Customer customer) {
        boolean status = false;
        ContentValues c = new ContentValues();
        c.put("name", customer.name);
        c.put("maximum_cane", customer.cane);
        c.put("brand_name", customer.brand);
        int result = database.update("customer_details", c, "_id='" + customer.id + "'", null);
        if (result > 0)
            status = true;
        return status;
    }

    public boolean UpdateDeposite(Customer customer) {
        boolean status = false;
        ContentValues c = new ContentValues();
        c.put("deposite_amount", customer.depositeAmount);
        Calendar calendar = Calendar.getInstance();
        c.put("deposite_given_date", calendar.getTime().toString());
        int result = database.update("customer_details", c, "_id='" + customer.id + "'", null);
        if (result > 0)
            status = true;
        return status;
    }

    public static class Customer {
        public String id;
        public String name;
        public String cane;
        public String buildingNumber;
        public String houseName;
        public int floorNumber;
        public String doorNumber;
        public String cross;
        public String main;
        public String landmark;
        public String brand;
        public String depositeAmount;
        public String[] mobiles = new String[3];
        public String location;

        public String getName() {
            return name;
        }

        public String getMobile1() {
            if (mobiles[0] != null && !mobiles[0].equals(""))
                return mobiles[0];
            return "";
        }

        public String getMobile2() {
            if (mobiles[1] != null && !mobiles[1].equals(""))
                return mobiles[1];
            return "";
        }

        public String getMobile3() {
            if (mobiles[2] != null && !mobiles[2].equals(""))
                return mobiles[2];
            return "";
        }
    }


    private List<Object> detailsList;

    public List<Object> GetDetails() {
        detailsList = new ArrayList<>();
        String query = "select order_details.customer_id, mobile, quantity, customer_details.brand_name, order_details._id, order_status, expected_date_time, building_number, house_name, floor_number, door_number, cross_number, main_number, landmark, location_name, name, product_details.unit_price from order_details  INNER JOIN address_details ON order_details.customer_id = address_details.customer_id INNER JOIN customer_details ON order_details.customer_id=customer_details._id INNER JOIN product_details ON product_details.product_name=customer_details.brand_name;";
        Cursor cursor = database.rawQuery(query, null);
        while (cursor.moveToNext()) {
            if (cursor.getString(cursor.getColumnIndex("order_status")).equals("Ordered")) {
                Details d = new Details();
                d.customerId = cursor.getString(cursor.getColumnIndex("customer_id"));
                d.customerName = cursor.getString(cursor.getColumnIndex("name"));
                d.mobile = cursor.getString(cursor.getColumnIndex("mobile"));
                d.quantity = cursor.getInt(cursor.getColumnIndex("quantity"));
                d.orderId = cursor.getInt(cursor.getColumnIndex("_id"));
                d.brand = cursor.getString(cursor.getColumnIndex("brand_name"));
                d.orderStatus = cursor.getString(cursor.getColumnIndex("order_status"));
                // d.expectedDateTime = cursor.getString(cursor.getColumnIndex("expected_date_time"));
                //   d.orderDateTime = cursor.getString(cursor.getColumnIndex("order_date_time"));
                //  d.confirmDateTime = cursor.getString(cursor.getColumnIndex("confirm_date_time"));
                //    d.deliveredDateTime = cursor.getString(cursor.getColumnIndex("delivered_date_time"));
                //   d.paymentMode = cursor.getString(cursor.getColumnIndex("payment_mode"));
                d.unitPrice = cursor.getInt(cursor.getColumnIndex("unit_price"));
                d.buildingNumber = cursor.getString(cursor.getColumnIndex("building_number"));
                d.houseName = cursor.getString(cursor.getColumnIndex("house_name"));
                d.floorNumber = cursor.getInt(cursor.getColumnIndex("floor_number"));
                d.doorNumber = cursor.getString(cursor.getColumnIndex("door_number"));
                d.cross = cursor.getString(cursor.getColumnIndex("cross_number"));
                d.main = cursor.getString(cursor.getColumnIndex("main_number"));
                d.landmark = cursor.getString(cursor.getColumnIndex("landmark"));
                d.location = cursor.getString(cursor.getColumnIndex("location_name"));
                String temp = cursor.getString(cursor.getColumnIndex("expected_date_time"));
                d.expectedDateTime = Utility.GetTimePart(temp);
                detailsList.add(d);
            }
        }
        cursor.close();
        query = "select * from new_customers where status='Fresher';";
        cursor = database.rawQuery(query, null);
        while (cursor.moveToNext()) {
            NewCustomerDettails d = new NewCustomerDettails();
            d.customerTag = cursor.getString(cursor.getColumnIndex("status"));
            d.customerMobile = cursor.getString(cursor.getColumnIndex("mobile"));
            detailsList.add(d);
        }
        cursor.close();
        return detailsList;
    }

    public List<Customer> GetCustomers() {
        List<Customer> customers = new ArrayList<Customer>();
        String query = "select _id, name,maximum_cane, brand_name,deposite_amount,deposite_given_date," +
                " building_number, house_name, floor_number, door_number, cross_number,main_number, landmark, location_name from customer_details INNER JOIN address_details ON customer_details._id = address_details.customer_id;";
        Cursor cursor = database.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Customer d = new Customer();
            d.id = cursor.getString(cursor.getColumnIndex("_id"));
            d.name = cursor.getString(cursor.getColumnIndex("name"));
            d.cane = cursor.getString(cursor.getColumnIndex("maximum_cane"));
            d.brand = cursor.getString(cursor.getColumnIndex("brand_name"));
            d.buildingNumber = cursor.getString(cursor.getColumnIndex("building_number"));
            d.houseName = cursor.getString(cursor.getColumnIndex("house_name"));
            d.floorNumber = cursor.getInt(cursor.getColumnIndex("floor_number"));
            d.doorNumber = cursor.getString(cursor.getColumnIndex("door_number"));
            d.cross = cursor.getString(cursor.getColumnIndex("cross_number"));
            d.main = cursor.getString(cursor.getColumnIndex("main_number"));
            d.location = cursor.getString(cursor.getColumnIndex("location_name"));
            d.landmark = cursor.getString(cursor.getColumnIndex("landmark"));
            d.depositeAmount = cursor.getString(cursor.getColumnIndex("deposite_amount"));

            customers.add(d);
        }
        cursor.close();
        for (Customer customer : customers) {
            query = "select * from mobile_numbers where customer_id='" + customer.id + "';";
            cursor = database.rawQuery(query, null);
            int i = 0;
            while (cursor.moveToNext()) {
                customer.mobiles[i++] = cursor.getString(cursor.getColumnIndex("mobile"));
            }
            cursor.close();
        }
        return customers;
    }

    public void UpdateOrderStatus(String status, long id) {
        Calendar calendar = Calendar.getInstance();
        String delivered_date_time = calendar.getTime().toString();
        String strSQL = "UPDATE order_details SET order_status ='" + status + "', delivered_date_time='" + delivered_date_time + "' WHERE _id = " + id;
        database.execSQL(strSQL);
    }

    public long GetOrderId(String mobile, String orderStatus) {
        String query = "select _id from order_details where mobile='" + mobile + "' and order_status='" + orderStatus + "';";
        Cursor c = database.rawQuery(query, null);
        long orderId = 0;
        while (c.moveToNext()) {
            orderId = c.getLong(c.getColumnIndex("_id"));
            break;
        }
        c.close();
        return orderId;
    }

    public int PrepareOrderDetails(String mobile, boolean IsLocal) {
        int statusCode = 0;//None
        Details d = new Details();
        d.mobile = mobile;
        String query = "select customer_id from mobile_numbers where mobile='" + mobile + "';";

        Cursor c = database.rawQuery(query, null);
        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                d.customerId = c.getString(c.getColumnIndex("customer_id"));
                break;
            }
            c.close();
            query = "select * from customer_details where _id=" + d.customerId;
            c = database.rawQuery(query, null);
            while (c.moveToNext()) {
                d.customerName = c.getString(c.getColumnIndex("name"));
                d.brand = c.getString(c.getColumnIndex("brand_name"));
                d.quantity = c.getInt(c.getColumnIndex("maximum_cane"));
            }
            c.close();

            query = "select unit_price from product_details where product_name='" + d.brand + "';";
            c = database.rawQuery(query, null);
            while (c.moveToNext()) {
                d.unitPrice = c.getInt(c.getColumnIndex("unit_price"));
            }
            c.close();

            query = "select * from address_details where customer_id=" + d.customerId;
            c = database.rawQuery(query, null);
            while (c.moveToNext()) {
                d.buildingNumber = c.getString(c.getColumnIndex("building_number"));
                d.houseName = c.getString(c.getColumnIndex("house_name"));
                d.floorNumber = c.getInt(c.getColumnIndex("floor_number"));
                d.doorNumber = c.getString(c.getColumnIndex("door_number"));
                d.cross = c.getString(c.getColumnIndex("cross_number"));
                d.main = c.getString(c.getColumnIndex("main_number"));
                d.location = c.getString(c.getColumnIndex("location_name"));
                d.landmark = c.getString(c.getColumnIndex("landmark"));
            }
            c.close();
            ContentValues order_details = new ContentValues();
            order_details.put("customer_id", d.customerId);
            order_details.put("quantity", d.quantity);
            order_details.put("brand_name", d.brand);
            d.orderStatus = "Ordered";
            order_details.put("order_status", d.orderStatus);

            order_details.put("mobile", d.mobile);
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            d.orderDateTime = Utility.GetTimePart(calendar.getTime().toString());
            order_details.put("order_date_time", calendar.getTime().toString());
            calendar.add(Calendar.MINUTE, 20);
            d.expectedDateTime = Utility.GetTimePart(calendar.getTime().toString());
            order_details.put("expected_date_time", calendar.getTime().toString());
            order_details.put("confirm_date_time", "d.confirmDateTime"); //TODO
            order_details.put("delivered_date_time", "d.deliveredDateTime"); //TODO
            order_details.put("payment_mode", d.paymentMode); //TODO
            order_details.put("amount", d.unitPrice);
            if (IsLocal)
                detailsList.add(d);
            else
                d.orderId = database.insert("order_details", null, order_details);
            statusCode = 2;//order placed
        } else {
            String[] returnedValues = IsValidForRegistration(mobile);
            if (Integer.parseInt(returnedValues[1]) < 5) {
                if (IsLocal) {
                    NewCustomerDettails details = new NewCustomerDettails();
                    details.customerMobile = mobile;
                    details.customerTag = "Fresher";
                    if (Integer.parseInt(returnedValues[1]) == 0 || !returnedValues[0].equals("Fresher")) {
                        detailsList.add(details);
                    }
                } else {
                    CaptureNewCustomer(mobile, Integer.parseInt(returnedValues[1]) + 1);
                    if (Integer.parseInt(returnedValues[1]) == 0 || !returnedValues[0].equals("Fresher")) {
                        statusCode = 1;
                    }
                }
            } else if (returnedValues[0].equals("Registered")) {
                statusCode = 3;
            }

        }
        return statusCode;

    }

    public static class Details {
        public String customerId;
        public String customerName;
        public String doorNumber;
        public int floorNumber;
        public String buildingNumber;
        public String houseName;
        public String cross;
        public String main;
        public String brand;
        public String mobile;
        public int quantity;
        public String orderStatus;
        public String expectedDateTime;
        public String orderDateTime;
        public String confirmDateTime;
        public String deliveredDateTime;
        public String paymentMode;
        public int unitPrice;
        public long orderId;
        public String landmark;
        public String location;


        public Details() {
        }

        public Details(String customerId,
                       String customerName, String doorNumber, int floorNumber, String buildingNumber,
                       String houseName, String cross, String main,
                       String brand, String mobile, int quantity,
                       String orderStatus, String expectedDateTime,
                       String orderDateTime,
                       String confirmDateTime,
                       String deliveredDateTime,
                       String paymentMode,
                       int unitPrice,
                       int orderId,
                       String landmark, String location) {
            this.customerId = customerId;
            this.customerName = customerName;
            this.doorNumber = doorNumber;
            this.floorNumber = floorNumber;
            this.buildingNumber = buildingNumber;
            this.houseName = houseName;
            this.cross = cross;
            this.main = main;
            this.brand = brand;
            this.mobile = mobile;
            this.quantity = quantity;
            this.orderStatus = orderStatus;
            this.expectedDateTime = expectedDateTime;
            this.orderDateTime = orderDateTime;
            this.confirmDateTime = confirmDateTime;
            this.deliveredDateTime = deliveredDateTime;
            this.paymentMode = paymentMode;
            this.unitPrice = unitPrice;
            this.orderId = orderId;
            this.landmark = landmark;
            this.location = location;

        }

        @Override
        public String toString() {
            return mobile;
        }

        public String getMobile() {
            return mobile;
        }

        public String getBrand() {
            return brand;
        }

        public String getCustomerName() {
            return customerName;
        }


    }

    public static class NewCustomerDettails {
        public String customerTag;
        public String customerMobile;

        public NewCustomerDettails() {
        }

        public NewCustomerDettails(String customerTag, String customerMobile) {
            this.customerTag = customerTag;
            this.customerMobile = customerMobile;
        }

        public String getMobile() {
            return customerMobile;
        }


        @Override
        public String toString() {
            return customerMobile;
        }


    }

    public String[] IsValidForRegistration(String mobile) {
        String[] returnedValues = new String[2];
        returnedValues[0] = "Fresher";
        returnedValues[1] = String.valueOf(0);
        String query = "select * from new_customers where mobile='" + mobile + "';";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            returnedValues[0] = cursor.getString(cursor.getColumnIndex("status"));
            returnedValues[1] = cursor.getString(cursor.getColumnIndex("attempts"));
        }
        cursor.close();
        return returnedValues;
    }

    public boolean CaptureNewCustomer(String mobile, int attempts) {
        Boolean status = false;
        ContentValues c = new ContentValues();
        c.put("mobile", mobile);
        c.put("status", "Fresher");
        c.put("attempts", attempts);
        long result = -1;
        if (attempts == 1) {
            result = database.insert("new_customers", null, c);
        } else {
            database.update("new_customers", c, "mobile='" + mobile + "'", null);
        }
        if (result > -1)
            status = true;
        return status;
    }

    public boolean UpdateRegistrationStatus(String mobile, String registrationStatus) {
        boolean status = false;
        ContentValues c = new ContentValues();
        c.put("status", registrationStatus);
        int result = database.update("new_customers", c, "mobile='" + mobile + "'", null);
        if (result > 0)
            status = true;
        return status;
    }

    public boolean RemoveCustomer(String mobile) {
        boolean status = false;
        ContentValues c = new ContentValues();
        c.put("status", "Unknown");
        int result = database.update("new_customers", c, "mobile='" + mobile + "'", null);
        if (result > 0)
            status = true;
        return status;
    }

    public int GetDepositeAmount(String customerId) {
        String query = "select deposite_amount from customer_details where _id='" + customerId + "';";
        Cursor c = database.rawQuery(query, null);
        int amount = 0;
        while (c.moveToNext()) {
            amount = c.getInt(c.getColumnIndex("deposite_amount"));
            break;
        }
        c.close();
        return amount;
    }
}

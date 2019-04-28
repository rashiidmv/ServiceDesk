package data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import wds.servicedesk.Utility;

/**
 * Created by Rashid on 09-Apr-17.
 */

public class ReportDataSource {
    private SQLiteDatabase database;

    public ReportDataSource() {
        database = ServiceDeskDataSource.GetDbAccess().open();
    }

    public int GetTotalOrders(String date) {
        Cursor result = database.rawQuery("select * from order_details;", null);
        int totalOrders = 0;
        if (result.getCount() > 0) {
            String day = Utility.GetDayPart(date);
            while (result.moveToNext()) {
                String orderDay = Utility.GetDayPart(result.getString(result.getColumnIndex("order_date_time")));
                String deliveredCancelDay = Utility.GetDayPart(result.getString(result.getColumnIndex("delivered_date_time")));
                if (day.equals(orderDay) || day.equals(deliveredCancelDay))
                    totalOrders++;
            }
        }
        // where order_status='Cancelled' OR order_status='Delivered' OR order_status='Ordered';"
        result.close();
        return totalOrders;
    }

    public int GetTotalCancelledOrders(String date) {
        Cursor result = database.rawQuery("select * from order_details where order_status='Cancelled';", null);
        int totalOrders = 0;
        if (result.getCount() > 0) {
            String day = Utility.GetDayPart(date);
            while (result.moveToNext()) {
                String tempDay = Utility.GetDayPart(result.getString(result.getColumnIndex("delivered_date_time")));
                if (day.equals(tempDay))
                    totalOrders++;
            }
        }       // where order_status='Cancelled' OR order_status='Delivered' OR order_status='Ordered';"
        result.close();
        return totalOrders;
    }

    public int GetTotalDeliverdOrders(String date) {
        Cursor result = database.rawQuery("select * from order_details where order_status='Delivered';", null);
        int totalOrders = 0;
        if (result.getCount() > 0) {
            String day = Utility.GetDayPart(date);
            while (result.moveToNext()) {
                String tempDay = Utility.GetDayPart(result.getString(result.getColumnIndex("delivered_date_time")));
                if (day.equals(tempDay))
                    totalOrders++;
            }
        }
        result.close();
        return totalOrders;
    }

    public Dictionary<String,ReportItems> GetTotalAmount(String date) {
        Dictionary<String,ReportItems> items= new Hashtable<String, ReportItems>();

        Cursor result = database.rawQuery("select * from order_details where order_status='Delivered';", null);
      //  int totalOrders = 0;
        if (result.getCount() > 0) {
            String day = Utility.GetDayPart(date);
            while (result.moveToNext()) {
                String tempDay = Utility.GetDayPart(result.getString(result.getColumnIndex("delivered_date_time")));
                if (day.equals(tempDay))
                {
                    String brand=result.getString(result.getColumnIndex("brand_name"));
                    int amount=result.getInt(result.getColumnIndex("amount"));
                    int quantity=result.getInt(result.getColumnIndex("quantity"));
                    if(items.get(brand) !=null)
                    {
                        ReportItems reportItems=items.get(brand);
                        reportItems.itemCount=reportItems.itemCount+quantity;
                        reportItems.itemAmount=reportItems.itemAmount+(amount*quantity);
                        reportItems.numberOfOrders=reportItems.numberOfOrders+1;
                    }
                    else
                    {
                        ReportItems reportItems =new ReportItems();
                        reportItems.numberOfOrders=1;
                        reportItems.brandName=brand;
                        reportItems.itemAmount=amount*quantity;
                        reportItems.itemCount=quantity;
                        reportItems.unitPrice=amount;
                        items.put(brand,reportItems);
                    }

            //        totalOrders=totalOrders+(result.getInt(result.getColumnIndex("quantity"))*result.getInt(result.getColumnIndex("amount")));
                }
            }
        }
        result.close();
        return items;
    }

    public int GetDepositeAmount(String date) {
        Cursor result = database.rawQuery("select * from customer_details where deposite_amount!=0;", null);
        int depositeAmount = 0;
        if (result.getCount() > 0) {
            String day = Utility.GetDayPart(date);
            while (result.moveToNext()) {
                String tempDay = Utility.GetDayPart(result.getString(result.getColumnIndex("deposite_given_date")));
                if (day.equals(tempDay))
                {
                    int amount=result.getInt(result.getColumnIndex("deposite_amount"));
                    depositeAmount=depositeAmount+amount;
                }
            }
        }
        result.close();
        return depositeAmount;
    }

    public  class ReportItems
    {
        public String brandName;
        public int itemCount;
        public int itemAmount;
        public int unitPrice;
        public int numberOfOrders;
    }
    public int GetTotalPendingOrders(String date) {
        Cursor result = database.rawQuery("select * from order_details where order_status='Ordered';", null);
        int totalOrders = 0;
        if (result.getCount() > 0) {
            String day = Utility.GetDayPart(date);
            while (result.moveToNext()) {
                String tempDay = Utility.GetDayPart(result.getString(result.getColumnIndex("order_date_time")));
                if (day.equals(tempDay))
                    totalOrders++;
            }
        }
        result.close();
        return totalOrders;
    }

}

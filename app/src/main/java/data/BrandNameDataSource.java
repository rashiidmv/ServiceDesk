package data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rashid on 16-Mar-17.
 */

public class BrandNameDataSource {
    private SQLiteDatabase database;

    public BrandNameDataSource() {
        database = ServiceDeskDataSource.GetDbAccess().open();
    }

    public List<String> GetBrandNames() {
        List<String> brandnames = new ArrayList<String>();
        Cursor result = database.rawQuery("select product_name from product_details", null);
        if (result.getCount() > 0) {
            while (result.moveToNext()) {
                brandnames.add(result.getString(result.getColumnIndex("product_name")));
            }
        }
        result.close();
        return brandnames;
    }

    public List<Product> GetProducts() {
        List<Product> products = new ArrayList<Product>();
        Cursor result = database.rawQuery("select * from product_details", null);
        if (result.getCount() > 0) {
            while (result.moveToNext()) {
                Product p = new Product();
                p.brandName = result.getString(result.getColumnIndex("product_name"));
                p.unitPrice = result.getInt(result.getColumnIndex("unit_price"));
                products.add(p);
            }
        }
        result.close();
        return products;
    }

    public boolean SaveProduct(Product product) {
        boolean status = false;
        ContentValues c = new ContentValues();
        c.put("product_name", product.brandName);
        c.put("unit_price", product.unitPrice);
        long result = database.insert("product_details", null, c);
        if (result > -1) {
            status = true;
        }
        return status;
    }

    public boolean DeleteProduct(String name) {
        boolean status = false;
        int result = database.delete("product_details", "product_name='" + name + "'", null);
        if (result > 0)
            status = true;
        return status;
    }

    public boolean UpdateProduct(String name,int price) {
        boolean status = false;
        ContentValues c=new ContentValues();
        c.put("unit_price",price);
        int result = database.update("product_details",c,"product_name='"+name+"'",null);
        if (result > 0)
            status = true;
        return status;
    }

    public static class Product {
        public String brandName;
        public int unitPrice;
    }

}

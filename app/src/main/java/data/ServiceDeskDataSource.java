package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Rashid on 11-Mar-17.
 */

public class ServiceDeskDataSource {
    private static SQLiteDatabase database;
    private static MySQLiteHelper dbHelper;
    private static ServiceDeskDataSource serviceDeskDataSource=null;
    private ServiceDeskDataSource() {
    }
    public static void SetDbAccees(Context context)
    {
        if(serviceDeskDataSource==null) {
            serviceDeskDataSource = new ServiceDeskDataSource();
            serviceDeskDataSource.dbHelper = new MySQLiteHelper(context);
            serviceDeskDataSource.database = serviceDeskDataSource.dbHelper.getWritableDatabase();
            //serviceDeskDataSource.FillDummyData();
        }
    }
    public static ServiceDeskDataSource GetDbAccess()
    {
        if(serviceDeskDataSource !=null)
            return serviceDeskDataSource;
        else
            return null;
    }
    public SQLiteDatabase open() //throws SQLException
    {
        if(serviceDeskDataSource!=null)
            return database;
        else
            return null;
    }
    public void close() {
        serviceDeskDataSource.dbHelper.close();
    }

}

package data;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Rashid on 21-Mar-17.
 */

public class LoginDataSource {
    private SQLiteDatabase database;
    public LoginDataSource() {
        database = ServiceDeskDataSource.GetDbAccess().open();
    }
    public  boolean CheckUserAndPassword(String username,String password)
    {
        boolean status=false;
        Cursor cursor=database.rawQuery("select * from login_details where username='"+username+"' and password='"+password+"';",null);
        if(cursor.getCount()>0)
        {
            status=true;
        }
        return  status;
    }
}

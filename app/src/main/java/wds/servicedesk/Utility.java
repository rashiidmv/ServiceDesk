package wds.servicedesk;


import android.app.AlertDialog;
import android.content.Context;
import android.icu.text.DateFormat;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Rashid on 13-Mar-17.
 */

public class Utility {
    public static String GetTimePart(String input) {
        String result = "";
        Date inputDate;
        String format = "EEE MMM dd HH:mm:ss z yyyy";
        SimpleDateFormat formater = new SimpleDateFormat(format);
        Calendar c = Calendar.getInstance();
        try {
            inputDate = formater.parse(input);
            c.setTime(inputDate);
            result = c.get(Calendar.HOUR) + ":" + (c.get(Calendar.MINUTE));
            if (c.get(Calendar.AM_PM) > 0) {
                result = result + " PM";
            } else {
                result = result + " AM";
            }
        } catch (ParseException e) {
            result = "";
        }
        return result;
    }

    public static String GetDayPart(String input) {
        String result = "";
        Date inputDate;
        String format = "EEE MMM dd HH:mm:ss z yyyy";
        SimpleDateFormat formater = new SimpleDateFormat(format);
        Calendar c = Calendar.getInstance();
        try {
            inputDate = formater.parse(input);
            c.setTime(inputDate);
            result = c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR);

        } catch (ParseException e) {
            result = "";
        }
        return result;
    }

    public static void showAlert(String message, Context c) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(c);
        dlgAlert.setMessage(message);
        dlgAlert.setTitle("Servie Desk");
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    public static String GetFloor(int floorNumber) {
        String whichFloor = "";
        int temp=floorNumber;

        if(temp ==0)
            whichFloor = "Ground Floor";
        else {
            floorNumber = floorNumber % 10;
            switch (floorNumber) {

                case 1:
                    whichFloor = temp + "st Floor";
                    break;
                case 2:
                    whichFloor = temp + "nd Floor";
                    break;
                case 3:
                    whichFloor = temp + "rd Floor";
                    break;
                default:
                    whichFloor = temp + "th Floor";
                    break;
            }
        }
        return whichFloor;
    }
}


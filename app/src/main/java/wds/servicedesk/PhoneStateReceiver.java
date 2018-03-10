package wds.servicedesk;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import data.CustomerDataSource;
import data.MobileDataSource;
import data.ServiceDeskDataSource;


/**
 * Created by Rashid on 03-Mar-17.
 */

public class PhoneStateReceiver extends BroadcastReceiver {
    public static PhoneStateReceiver handler = null;

    public PhoneStateReceiver() {
        if (handler == null)
            handler = this;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //   dataSource=new CustomerDataSource(context);
        ServiceDeskDataSource.SetDbAccees(context);
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        final String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {

            String temp = null;
            if (incomingNumber.contains("+"))
                temp = incomingNumber.substring(3);
            else
                temp = incomingNumber;

            ServiceDeskDataSource.SetDbAccees(context);
            CustomerDataSource customerDataSource = new CustomerDataSource();
            MobileDataSource mobileDataSource = new MobileDataSource();

            int statusCode = 0;
            String[] allMobils = mobileDataSource.GetAllMobiles(temp);
            for (String m : allMobils) {
                long orderId = 0;
                orderId = customerDataSource.GetOrderId(m, "Ordered");
                if (orderId > 0) {
                    statusCode = 3;
                    break;
                }
            }
            if (statusCode == 0) {
                statusCode = customerDataSource.PrepareOrderDetails(temp, false);  // false indicating application is not opened
            }
            try {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                Class<?> classTelephony = Class.forName(telephonyManager.getClass().getName());
                Method methodGetITelephony = classTelephony.getDeclaredMethod("getITelephony");
                methodGetITelephony.setAccessible(true);
                Object telephonyInterface = methodGetITelephony.invoke(telephonyManager);
                Class<?> telephonyInterfaceClass = Class.forName(telephonyInterface.getClass().getName());
                Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");
                methodEndCall.invoke(telephonyInterface);

                if (statusCode == 1) {

                    try {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage("+91" + temp, null,
                                "Hi, Our customer care executive will get in touch with you for delivery details. \nThank you for considering us.", null, null);
                        Toast.makeText(context, "SMS Sent!", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(context, "G SMS failed, Check SMS tariff", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                } else if (statusCode == 2) {

                    Calendar calander = Calendar.getInstance();
                    calander.add(Calendar.MINUTE, 20);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
                    String curTime = simpleDateFormat.format(calander.getTime());
                    try {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage("+91" + temp, null,
                                "Hi, Your order is confirmed.\nWe will be delivering before " + curTime + " If you want to cancel Please call on 9620370920", null, null);
                        Toast.makeText(context, "SMS Sent!",Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(context, "G SMS failed, Check SMS tariff", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                } else if (statusCode == 3) {    //if order exists
                    try {
                        SmsManager smsManager = SmsManager.getDefault();
                        smsManager.sendTextMessage("+91" + temp, null,
                                "Hi, Your order is pending with us. We will deliver it soon.\nThank you for considering us.", null, null);
                        Toast.makeText(context, "SMS Sent!", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(context, "G SMS failed, Check SMS tariff", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
//        if ((state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))) {
//            Toast.makeText(context, "Received State", Toast.LENGTH_SHORT).show();
//        }
//        if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
//            Toast.makeText(context, "Idle State", Toast.LENGTH_SHORT).show();
//        }
    }
}


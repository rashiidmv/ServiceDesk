package wds.servicedesk.reports;

import android.content.Context;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import wds.servicedesk.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TodaysDeliveredOrders.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TodaysDeliveredOrders#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodaysDeliveredOrders extends Fragment {

    private static final String TODAYSDELIVEREDORDERS = "TodayCancelledOrders";
    private static final String ADMINISTRATOR = "Admin";

    private int todaysDeliveredOrders;
    private boolean IsAdmin;
    TextView textView;
    TextView todaysTotalAmount;
    Button btnGetTodaysTotalAmount;

    LinearLayout allItems;
    public TodaysDeliveredOrders() {
        // Required empty public constructor
    }
    public static TodaysDeliveredOrders newInstance(int todaysDeliveredOrders) {
        TodaysDeliveredOrders fragment = new TodaysDeliveredOrders();
        Bundle args = new Bundle();
        args.putInt(TODAYSDELIVEREDORDERS,todaysDeliveredOrders);
        fragment.setArguments(args);
        return fragment;
    }

    public static TodaysDeliveredOrders newInstanceAdmin(int todaysDeliveredOrders,boolean IsAdmin) {
        TodaysDeliveredOrders fragment = new TodaysDeliveredOrders();
        Bundle args = new Bundle();
        args.putInt(TODAYSDELIVEREDORDERS,todaysDeliveredOrders);
        args.putBoolean(ADMINISTRATOR,IsAdmin);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IsAdmin=false;
        if (getArguments() != null) {
            todaysDeliveredOrders = getArguments().getInt(TODAYSDELIVEREDORDERS);
            if(getArguments().containsKey(ADMINISTRATOR))
            {
                IsAdmin=getArguments().getBoolean(ADMINISTRATOR);
            }
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_todays_delivered_orders, container, false);
        textView=(TextView)view.findViewById(R.id.TodaysDeliveredOrder);
        textView.setText(String.valueOf(todaysDeliveredOrders));
        if(IsAdmin) {
            btnGetTodaysTotalAmount = (Button)view.findViewById(R.id.btnGetTodaysTotalAmount);
            btnGetTodaysTotalAmount.setVisibility(View.VISIBLE);
            todaysTotalAmount = (TextView) view.findViewById(R.id.TodaysTotalAmount);
            todaysTotalAmount.setVisibility(View.VISIBLE);
            allItems = (LinearLayout) view.findViewById(R.id.ItemLayout);
        }
        return  view;
    }

}
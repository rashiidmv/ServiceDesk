package wds.servicedesk.reports;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import wds.servicedesk.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TodaysCancelledOrders.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TodaysCancelledOrders#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodaysCancelledOrders extends Fragment {
    private static final String TODAYSCANCELLEDORDERS = "TodayCancelledOrders";
    private int todaysCancelledOrders;
    TextView textView;

    public TodaysCancelledOrders() {
        // Required empty public constructor
    }
    public static TodaysCancelledOrders newInstance(int todaysCancelledOrder) {
        TodaysCancelledOrders fragment = new TodaysCancelledOrders();
        Bundle args = new Bundle();
        args.putInt(TODAYSCANCELLEDORDERS,todaysCancelledOrder);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            todaysCancelledOrders = getArguments().getInt(TODAYSCANCELLEDORDERS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_todays_cancelled_orders, container, false);
        textView=(TextView)view.findViewById(R.id.TodaysCancelledOrders);
        textView.setText(String.valueOf(todaysCancelledOrders));
        return  view;
    }
}

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
 * {@link TodaysAllOrders.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TodaysAllOrders#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodaysAllOrders extends Fragment {
    private static final String TODAYSTOTALORDERS = "TodaysTotalOrder";
    private int todaysTotalOrder;
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_todays_all_orders, container, false);
        textView=(TextView)view.findViewById(R.id.TodaysTotalOrder);
        textView.setText(String.valueOf(todaysTotalOrder));
        return  view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            todaysTotalOrder = getArguments().getInt(TODAYSTOTALORDERS);
        }
    }

    public static TodaysAllOrders newInstance(int todaysTotalOrder) {
        TodaysAllOrders fragment = new TodaysAllOrders();
        Bundle args = new Bundle();
        args.putInt(TODAYSTOTALORDERS, todaysTotalOrder);
        fragment.setArguments(args);
        return fragment;
    }
}

package wds.servicedesk.reports;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import wds.servicedesk.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TodaysPendingOrders#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodaysPendingOrders extends Fragment {
    private static final String TOTALPENDINGORDERS = "TotalPendingOrders";
    private int totalPendingOrders;
    TextView textView;

    public TodaysPendingOrders() {
        // Required empty public constructor
    }

    public static TodaysPendingOrders newInstance(int totalPendingOrders) {
        TodaysPendingOrders fragment = new TodaysPendingOrders();
        Bundle args = new Bundle();
        args.putInt(TOTALPENDINGORDERS, totalPendingOrders);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            totalPendingOrders = getArguments().getInt(TOTALPENDINGORDERS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todays_pending_orders, container, false);
        textView=(TextView) view.findViewById(R.id.TodaysPendingOrders);
        textView.setText(String.valueOf(totalPendingOrders));
        return view;
    }

}

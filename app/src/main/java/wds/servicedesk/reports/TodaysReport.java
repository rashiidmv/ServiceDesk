package wds.servicedesk.reports;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import data.ReportDataSource;
import wds.servicedesk.R;

public class TodaysReport extends AppCompatActivity {
    private int todaysTotalOrders = 0;
    private int todaysCancelledOrders = 0;
    private int todaysDeliveredOrders = 0;
    private int todaysPendingOrders = 0;
    private Bundle bundle;
    private boolean isAdmin;

    TodaysDeliveredOrders tab2;

    private ReportDataSource reportDataSource;

    public void GetTodaysTotalAmount(View view) {
        if (tab2 != null && tab2.todaysTotalAmount != null) {
            int totalAmount = 0;
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            Dictionary<String, ReportDataSource.ReportItems> allItems = reportDataSource.GetTotalAmount(calendar.getTime().toString());
            Enumeration<ReportDataSource.ReportItems> items = allItems.elements();
            while (items.hasMoreElements()) {
                TextView t = new TextView(view.getContext());
                t.setGravity(Gravity.CENTER_HORIZONTAL);
                ReportDataSource.ReportItems item = items.nextElement();
                t.setText(item.brandName + " = " + item.itemCount + " X " + item.itemAmount);
                totalAmount = totalAmount + item.itemAmount;
                tab2.allItems.addView(t);
            }
            tab2.todaysTotalAmount.setText("Total Amount = " + totalAmount);
            tab2.btnGetTodaysTotalAmount.setEnabled(false);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        isAdmin = true;
        bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("IsAdmin")) {
            isAdmin = bundle.getBoolean("IsAdmin");
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("All"));
        tabLayout.addTab(tabLayout.newTab().setText("Delivered"));
        tabLayout.addTab(tabLayout.newTab().setText("Canceled"));
        tabLayout.addTab(tabLayout.newTab().setText("Pending"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        reportDataSource = new ReportDataSource();
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        todaysTotalOrders = reportDataSource.GetTotalOrders(calendar.getTime().toString());
        todaysCancelledOrders = reportDataSource.GetTotalCancelledOrders(calendar.getTime().toString());
        todaysDeliveredOrders = reportDataSource.GetTotalDeliverdOrders(calendar.getTime().toString());
        todaysPendingOrders = reportDataSource.GetTotalPendingOrders(calendar.getTime().toString());

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class PagerAdapter extends FragmentStatePagerAdapter {
        int mNumOfTabs;

        public PagerAdapter(FragmentManager fm, int NumOfTabs) {
            super(fm);
            this.mNumOfTabs = NumOfTabs;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    TodaysAllOrders tab1 = TodaysAllOrders.newInstance(todaysTotalOrders);
                    return tab1;
                case 1:
                    if (isAdmin)
                        tab2 = TodaysDeliveredOrders.newInstanceAdmin(todaysDeliveredOrders, true);
                    else
                        tab2 = TodaysDeliveredOrders.newInstance(todaysDeliveredOrders);
                    return tab2;
                case 2:
                    TodaysCancelledOrders tab3 = TodaysCancelledOrders.newInstance(todaysCancelledOrders);
                    return tab3;
                case 3:
                    TodaysPendingOrders tab4 = TodaysPendingOrders.newInstance(todaysPendingOrders);
                    return tab4;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumOfTabs;
        }
    }
}

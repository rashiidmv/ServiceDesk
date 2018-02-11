package wds.servicedesk;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import data.CustomerDataSource;
import data.LoginDataSource;
import data.MobileDataSource;
import data.ServiceDeskDataSource;
import wds.servicedesk.reports.TodaysReport;

/**
 * An activity representing a list of Home. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link HomeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class HomeListActivity extends AppCompatActivity {
    private PhoneStateReceiver1 receiver;
    private boolean mTwoPane;
    private CustomerDataSource datasource;
    private MobileDataSource mobileDataSource;
    private LoginDataSource loginDataSource;
    private SimpleItemRecyclerViewAdapter d;
    static final int REGISTER_CUSTOMER_FROM_DESK = 1;
    private List<Object> allItems;
    private boolean IsSeachViewClosed = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_list);

        ServiceDeskDataSource.SetDbAccees(this);
        datasource = new CustomerDataSource();
        loginDataSource = new LoginDataSource();
        mobileDataSource = new MobileDataSource();

        receiver = new PhoneStateReceiver1(new Handler(), this); // Create the receiver
        registerReceiver(receiver, new IntentFilter("android.intent.action.PHONE_STATE")); // Register receiver


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());


        View recyclerView = findViewById(R.id.home_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.home_detail_container) != null) {
            mTwoPane = true;
        }
    }

    public void floatingActionNewCustomerClick(View view) {
        //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //          .setAction("Action", null).show();

        Intent intent = new Intent(this, NewCustomer.class);
        startActivity(intent);
    }


    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        // recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DummyContent.ITEMS));

        d = new SimpleItemRecyclerViewAdapter(datasource.GetDetails());
        recyclerView.setAdapter(d);
    }

    public class PhoneStateReceiver1 extends BroadcastReceiver {
        private final Handler handler; // Handler used to execute code on the UI thread
        private final Context context1;

        public PhoneStateReceiver1(Handler handler, Context context) {
            this.handler = handler;
            this.context1 = context;
        }

        @Override
        public void onReceive(final Context context, Intent intent) {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            final String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                String temp = null;
                if (incomingNumber.contains("+"))
                    temp = incomingNumber.substring(3);
                else
                    temp = incomingNumber;

                int statusCode = 0;
                String[] allMobils = mobileDataSource.GetAllMobiles(temp);
                for (String m : allMobils) {
                    long orderId = 0;
                    orderId = datasource.GetOrderId(m, "Ordered");
                    if (orderId > 0) {
                        statusCode = 3;
                        break;
                    }
                }
                if (statusCode == 0) {
                    statusCode = datasource.PrepareOrderDetails(temp, true);  // true indicating application opened
                }
                if (statusCode == 1) {
                    Toast.makeText(context, "New Customer captured", Toast.LENGTH_SHORT).show();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        d.swap();
                    }
                });
                try {
                    TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                    Class<?> classTelephony = Class.forName(telephonyManager.getClass().getName());
                    Method methodGetITelephony = classTelephony.getDeclaredMethod("getITelephony");
                    methodGetITelephony.setAccessible(true);
                    Object telephonyInterface = methodGetITelephony.invoke(telephonyManager);
                    Class<?> telephonyInterfaceClass = Class.forName(telephonyInterface.getClass().getName());
                    Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");
                    methodEndCall.invoke(telephonyInterface);

//                    if (statusCode == 1) {   // New customer
//
//                        try {
//                            SmsManager smsManager = SmsManager.getDefault();
//                            smsManager.sendTextMessage("+91" + temp, null,
//                                    "Hi, Our executive will get in touch with you for delivery details. \nThank you for considering us.", null, null);
//                            Toast.makeText(context, "SMS Sent!", Toast.LENGTH_LONG).show();
//                        } catch (Exception e) {
//                            Toast.makeText(context, "SMS failed, Check SMS tariff", Toast.LENGTH_LONG).show();
//                            e.printStackTrace();
//                        }
//                    } else if (statusCode == 2) {    //Existing customer
//
//                        Calendar calander = Calendar.getInstance();
//                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
//                        String time = simpleDateFormat.format(calander.getTime());
//                        Date dt = new Date();
//                        int hours = dt.getHours();
//                        int minutes = dt.getMinutes() + 20;
//                        int seconds = dt.getSeconds();
//                        String curTime = hours + ":" + minutes + ":" + seconds;
//                        try {
//                            SmsManager smsManager = SmsManager.getDefault();
//                            smsManager.sendTextMessage("+91" + temp, null,
//                                    "Hi Your order is confirmed.\n We will be delivering before " + curTime + " If you want to cancel Please call on 9620370920", null, null);
//                            //    Toast.makeText(context, "SMS Sent!",Toast.LENGTH_LONG).show();
//                        } catch (Exception e) {
//                            Toast.makeText(context, "SMS failed, Check SMS tariff", Toast.LENGTH_LONG).show();
//                            e.printStackTrace();
//                        }
//                    } else if (statusCode == 3) {    //if order exists
//                        try {
//                            SmsManager smsManager = SmsManager.getDefault();
//                            smsManager.sendTextMessage("+91" + temp, null,
//                                    "Hi, Your order is pending with us. We will deliver it soon.\nThank you for considering us.", null, null);
//                            Toast.makeText(context, "SMS Sent!", Toast.LENGTH_LONG).show();
//                        } catch (Exception e) {
//                            Toast.makeText(context, "SMS failed, Check SMS tariff", Toast.LENGTH_LONG).show();
//                            e.printStackTrace();
//                        }
//                    }
                } catch (Exception ex) { // Many things can go wrong with reflection calls
                    ex.printStackTrace();
                }
            }
        }
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        //private final List<DummyContent.DummyItem> mValues;
        private List<Object> mValues;
        private final int ORDERDETAILS = 0, NEWCUSTOMERDETAILS = 1;

        // public SimpleItemRecyclerViewAdapter(List<DummyContent.DummyItem> items) {
        public SimpleItemRecyclerViewAdapter(List<Object> items) {
            mValues = items;
            allItems = mValues;
        }

        //   public void swap(List<CustomerDataSource.Customer> list){
        public void swap() {

            notifyDataSetChanged();
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
           /* View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.home_list_content, parent, false);
            return new ViewHolderOrderDetails(view);*/
            RecyclerView.ViewHolder viewHolder = null;
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            if (viewType == ORDERDETAILS) {
                View v1 = inflater.inflate(R.layout.home_list_content, parent, false);
                viewHolder = new ViewHolderOrderDetails(v1);
            } else if (viewType == NEWCUSTOMERDETAILS) {
                View v2 = inflater.inflate(R.layout.home_list_new_customer_details, parent, false);
                viewHolder = new ViewHolderNewCustomerDetails(v2);
            }
            return viewHolder;
        }

        @Override
        public int getItemViewType(int position) {
            if (mValues.get(position) instanceof CustomerDataSource.Details) {
                return ORDERDETAILS;
            } else if (mValues.get(position) instanceof CustomerDataSource.NewCustomerDettails) {
                return NEWCUSTOMERDETAILS;
            }
            return -1;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            switch (holder.getItemViewType()) {
                case NEWCUSTOMERDETAILS:
                    ViewHolderNewCustomerDetails viewHolderNewCustomerDetails = (ViewHolderNewCustomerDetails) holder;
                    CustomerDataSource.NewCustomerDettails newCustomerDettails = (CustomerDataSource.NewCustomerDettails) mValues.get(position);
                    viewHolderNewCustomerDetails.custmerTag.setText(newCustomerDettails.customerTag);
                    viewHolderNewCustomerDetails.customerMobile.setText(newCustomerDettails.customerMobile);

                    viewHolderNewCustomerDetails.customerRegSteps.setText(
                            "1. Call new customer.\n" +
                                    "2. Get delivery details, product preferences.\n" +
                                    "3. Click on Register button");
                    viewHolderNewCustomerDetails.btnRegisterNewCustomer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(v.getContext(), NewCustomer.class);
                            Bundle b = new Bundle();
                            b.putString("mobile", ((CustomerDataSource.NewCustomerDettails) mValues.get(position)).customerMobile);
                            b.putString("from", "Register");
                            b.putInt("Position", position);
                            intent.putExtras(b);
                            startActivityForResult(intent, REGISTER_CUSTOMER_FROM_DESK);
                        }
                    });
                    viewHolderNewCustomerDetails.btnRemoveNewCustomer.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(v.getContext());
                            dlgAlert.setMessage("Do you want to remove?");
                            dlgAlert.setTitle("Servie Desk");
                            dlgAlert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    boolean status = datasource.RemoveCustomer(((CustomerDataSource.NewCustomerDettails) mValues.get(position)).customerMobile);
                                    mValues.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, mValues.size());
                                    d.swap();
                                }
                            });
                            dlgAlert.setNegativeButton("NO", null);
                            dlgAlert.setCancelable(true);
                            dlgAlert.create().show();
                        }
                    });
                    break;
                case ORDERDETAILS:
                    ViewHolderOrderDetails viewHolderOrderDetails = (ViewHolderOrderDetails) holder;
                    CustomerDataSource.Details current = (CustomerDataSource.Details) mValues.get(position);
                    viewHolderOrderDetails.mItem = current;
                    viewHolderOrderDetails.custmerNameView.setText(current.customerName);
                    viewHolderOrderDetails.mobileView.setText(current.mobile);
                    if (current.doorNumber != null && !current.doorNumber.equals(""))
                        viewHolderOrderDetails.doorNumber.setText("Door# " + current.doorNumber + ",");
                    String whichFloor = Utility.GetFloor(current.floorNumber);
                    viewHolderOrderDetails.floorNumberView.setText(whichFloor);
                    if (current.buildingNumber != null && !current.buildingNumber.equals(""))
                        viewHolderOrderDetails.buildingNumberView.setText("#" + current.buildingNumber + ",");
                    viewHolderOrderDetails.houseNameView.setText(current.houseName);
                    if (current.cross != null && !current.cross.equals(""))
                        viewHolderOrderDetails.crossView.setText(current.cross + " Cross,");
                    if (current.main != null && !current.main.equals(""))
                        viewHolderOrderDetails.mainView.setText(current.main + " Main");
                    viewHolderOrderDetails.brandView.setText(current.brand);
                    viewHolderOrderDetails.quantity.setText(String.valueOf(current.quantity));
                    viewHolderOrderDetails.unitPrice.setText(String.valueOf(current.unitPrice));
                    viewHolderOrderDetails.totalAmount.setText(String.valueOf(current.unitPrice * current.quantity));
                    viewHolderOrderDetails.expectedDeliveryView.setText(current.expectedDateTime);
                    viewHolderOrderDetails.landmarkView.setText(current.landmark);
                    viewHolderOrderDetails.locationView.setText(current.location);


                    viewHolderOrderDetails.mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mTwoPane) {
                      /*  Bundle arguments = new Bundle();
                        arguments.putString(HomeDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        HomeDetailFragment fragment = new HomeDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.home_detail_container, fragment)
                                .commit();*/
                            } else {
                      /*  Context context = v.getContext();
                        Intent intent = new Intent(context, HomeDetailActivity.class);
                        intent.putExtra(HomeDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                        context.startActivity(intent);*/
                            }
                        }
                    });
                    viewHolderOrderDetails.btnCancelOrder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(v.getContext());
                            dlgAlert.setMessage("Do you want to Cancel order?");
                            dlgAlert.setTitle("Servie Desk");
                            dlgAlert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    CustomerDataSource.Details temp = (CustomerDataSource.Details) mValues.get(position);
                                    if (temp.orderId == 0)
                                        temp.orderId = datasource.GetOrderId(temp.mobile, temp.orderStatus);
                                    temp.orderStatus = "Cancelled";
                                    datasource.UpdateOrderStatus(temp.orderStatus, temp.orderId);
                                    mValues.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, mValues.size());
                                    d.swap();
                                }
                            });
                            dlgAlert.setNegativeButton("NO", null);
                            dlgAlert.setCancelable(true);
                            dlgAlert.create().show();


                        }
                    });
                    viewHolderOrderDetails.btnDelivered.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(v.getContext());
                            dlgAlert.setMessage("Order Delivered?");
                            dlgAlert.setTitle("Servie Desk");
                            dlgAlert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    CustomerDataSource.Details temp = (CustomerDataSource.Details) mValues.get(position);
                                    if (temp.orderId == 0)
                                        temp.orderId = datasource.GetOrderId(temp.mobile, temp.orderStatus);
                                    temp.orderStatus = "Delivered";
                                    datasource.UpdateOrderStatus(temp.orderStatus, temp.orderId);
                                    mValues.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, mValues.size());
                                    d.swap();
                                }
                            });
                            dlgAlert.setNegativeButton("No", null);
                            dlgAlert.setCancelable(true);
                            dlgAlert.create().show();
                        }
                    });
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolderOrderDetails extends RecyclerView.ViewHolder {
            public final LinearLayout layoutOrderDetails;
            public final LinearLayout layoutBuildingDetails;
            public final LinearLayout layoutCrossMainDetails;
            public final LinearLayout layoutButtons;
            public final TextView title;
            public final View mView;
            public final TextView custmerNameView;
            public final TextView doorNumber;
            public final TextView floorNumberView;
            public final TextView buildingNumberView;
            public final TextView houseNameView;
            public final TextView crossView;
            public final TextView mainView;
            public final TextView mobileView;
            public final TextView brandView;
            public final TextView expectedDeliveryView;
            public final TextView locationView;
            public final TextView landmarkView;
            public final TextView quantity;
            public final TextView totalAmount;
            public final TextView unitPrice;

            public final Button btnCancelOrder;
            public final Button btnDelivered;
//            public final Button btnRegisterNewCustomer;
//            public final Button btnRemoveNewCustomer;

            // public DummyContent.DummyItem mItem;
            public CustomerDataSource.Details mItem;

            public ViewHolderOrderDetails(View view) {
                super(view);
                layoutOrderDetails = (LinearLayout) view.findViewById(R.id.layout_order_details);
                layoutBuildingDetails = (LinearLayout) view.findViewById(R.id.layout_building_details);
                layoutCrossMainDetails = (LinearLayout) view.findViewById(R.id.layout_cross_main);
                layoutButtons = (LinearLayout) view.findViewById(R.id.layout_buttons);
                title = (TextView) findViewById(R.id.item_title);
                mView = view;
                custmerNameView = (TextView) view.findViewById(R.id.customername);
                doorNumber = (TextView) view.findViewById(R.id.doornumber);
                floorNumberView = (TextView) view.findViewById(R.id.floornumber);
                buildingNumberView = (TextView) view.findViewById(R.id.buildingnumber);
                houseNameView = (TextView) view.findViewById(R.id.housename);
                crossView = (TextView) view.findViewById(R.id.cross);
                mainView = (TextView) view.findViewById(R.id.main);
                mobileView = (TextView) view.findViewById(R.id.mobile);
                brandView = (TextView) view.findViewById(R.id.brand);
                quantity = (TextView) view.findViewById(R.id.quantity);
                totalAmount = (TextView) view.findViewById(R.id.totalamount);
                unitPrice = (TextView) view.findViewById(R.id.unitprice);
                expectedDeliveryView = (TextView) view.findViewById(R.id.delivery_datetime);
                landmarkView = (TextView) view.findViewById(R.id.landmark);
                locationView = (TextView) view.findViewById(R.id.location);
                btnCancelOrder = (Button) view.findViewById(R.id.btncancel);
                btnDelivered = (Button) view.findViewById(R.id.btnDelivered);
//                btnRegisterNewCustomer = (Button) view.findViewById(R.id.btnRegisterNewCustomer);
//                btnRemoveNewCustomer = (Button) view.findViewById(R.id.btnRemoveNewCustomer);
            }


            @Override
            public String toString() {
                return super.toString() + " '" + custmerNameView.getText() + "'";
            }
        }

        public class ViewHolderNewCustomerDetails extends RecyclerView.ViewHolder {
            public final TextView custmerTag;
            public final TextView customerMobile;
            public final TextView customerRegSteps;
            public final Button btnRegisterNewCustomer;
            public final Button btnRemoveNewCustomer;
            public final View mView;

            public ViewHolderNewCustomerDetails(View view) {
                super(view);
                mView = view;
                custmerTag = (TextView) view.findViewById(R.id.newCustomerTag);
                customerMobile = (TextView) view.findViewById(R.id.newCustomerMobile);
                customerRegSteps = (TextView) view.findViewById(R.id.customerRegSteps);
                btnRegisterNewCustomer = (Button) view.findViewById(R.id.btnRegisterNewCustomer);
                btnRemoveNewCustomer = (Button) view.findViewById(R.id.btnRemoveNewCustomer);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + customerMobile.getText() + "'";
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == REGISTER_CUSTOMER_FROM_DESK) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                if (bundle != null && bundle.containsKey("Position")) {
                    d.mValues.remove(bundle.getInt("Position"));
                    d.notifyItemRemoved(bundle.getInt("Position"));
                    //   d.notifyItemRangeChanged(bundle.getInt("Position"), d.mValues.size());
                    d.swap();

                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_menu, menu);//Menu Resource, Menu
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView =
                (SearchView) menu.findItem(R.id.homeSearch).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        final MenuItem searchItem = menu.findItem(R.id.homeSearch);


//        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
//
//            @Override
//            public boolean onMenuItemActionExpand(MenuItem item) {
//                // Do whatever you need
//                return true; // KEEP IT TO TRUE OR IT DOESN'T OPEN !!
//            }
//
//            @Override
//            public boolean onMenuItemActionCollapse(MenuItem item) {
//                // Do whatever you need
//                return true; // OR FALSE IF YOU DIDN'T WANT IT TO CLOSE!
//            }
//        });

        searchView.setQueryHint("Name/Mobile/Brand");
        View closeButton = searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetOriginal();
                searchView.setIconified(true);
                searchView.onActionViewCollapsed();
            }
        });

        MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                if (!IsSeachViewClosed) {
                    GetOriginal();
                }
                return true;
            }
        });
//        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener()
//        {
//            @Override
//            public void onFocusChange(View v, boolean newViewFocus)
//            {
//                if (!newViewFocus)
//                {
//                    //Collapse the action item.
//                    searchItem.collapseActionView();
//                    //Clear the filter/search query.
//                //    myFilterFunction("");
//                }
//            }
//        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override

            public boolean onQueryTextSubmit(String query) {
                final List<Object> filteredModelList = filter(allItems, query);
                d.mValues.retainAll(filteredModelList);
                d.notifyDataSetChanged();
                if (d.mValues.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Customer not found", Toast.LENGTH_SHORT).show();
                }
                IsSeachViewClosed = false;
                // customerListAdapter.replaceAll(filteredModelList);
                //  Toast.makeText(getApplicationContext(),query,Toast.LENGTH_SHORT).show();

                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //  Toast.makeText(getApplicationContext(),"Searhing started",Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        return true;
    }

    private void GetOriginal() {
        d.mValues.clear();
        d.mValues.addAll(datasource.GetDetails());
        d.notifyDataSetChanged();
        IsSeachViewClosed = true;
    }

    private static List<Object> filter(List<Object> models, String query) {
        final String lowerCaseQuery = query.toLowerCase();
        final List<Object> filteredModelList = new ArrayList<>();
        for (Object model : models) {
            if (model instanceof CustomerDataSource.NewCustomerDettails) {
                CustomerDataSource.NewCustomerDettails newCustomerDettails = (CustomerDataSource.NewCustomerDettails) model;
                final String mobile = newCustomerDettails.getMobile().toLowerCase();
                if (mobile.contains(lowerCaseQuery)) {
                    filteredModelList.add(model);
                }
            } else if (model instanceof CustomerDataSource.Details) {
                CustomerDataSource.Details details = (CustomerDataSource.Details) model;
                final String mobile = details.getMobile().toLowerCase();
                final String customerName = details.getCustomerName().toLowerCase();
                final String brandName = details.getBrand().toLowerCase();
                if (mobile.contains(lowerCaseQuery)
                        || customerName.contains(lowerCaseQuery)
                        || brandName.contains(lowerCaseQuery)
                        ) {
                    filteredModelList.add(model);
                }
            }
        }
        return filteredModelList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_add_user:
                Intent newUserIntent = new Intent(this, NewUser.class);
                showLoginScreen(newUserIntent);
                return true;
            case R.id.menu_add_product:
                Intent newProductIntent = new Intent(this, NewProduct.class);
                // startActivity(newProductIntent);
                showLoginScreen(newProductIntent);
                return true;
            case R.id.menu_today_report:
                Intent todayReportIntent = new Intent(this, TodaysReport.class);
                //startActivity(todayReportIntent);
                showLoginScreen(todayReportIntent);
                return true;
            case R.id.menu_settings:
                //  Intent settingsIntent = new Intent(this, Settings.class);
                // startActivity(settingsIntent);
                // showLoginScreen(todayReportIntent);
                return true;
        }
        return true;
    }

    private void showLoginScreen(final Intent nextScreen) {
        final AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
        final EditText txtUsername = new EditText(this);
        txtUsername.setHint("Enter username");
        txtUsername.setInputType(InputType.TYPE_CLASS_TEXT);
        final EditText txtPassord = new EditText(this);
        txtPassord.setHint("Enter password");
        txtPassord.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        final CheckBox isAdmin = new CheckBox(getApplicationContext());
        isAdmin.setText("I am admin");
        isAdmin.setChecked(false);
        final AlertDialog dialog = dlgAlert.create();
        Button btnLogin = new Button(this);
        btnLogin.setBackgroundResource(R.drawable.curved_button_shape);
        btnLogin.setText("Login");
        btnLogin.setWidth(94);
        btnLogin.setHeight(34);
        btnLogin.setTextColor(getResources().getColor(R.color.white));
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isAdmin.isChecked()) {
                    dialog.dismiss();
                    nextScreen.putExtra("IsAdmin", false);
                    startActivity(nextScreen);
                    return;
                }
                boolean loginSuccess = loginDataSource.CheckUserAndPassword(txtUsername.getText().toString().trim(), txtPassord.getText().toString().trim());
                if (loginSuccess) {
                    dialog.dismiss();
                    nextScreen.putExtra("IsAdmin", true);
                    startActivity(nextScreen);
                } else {
                    Toast.makeText(v.getContext(), "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button btnCancel = new Button(this);
        btnCancel.setBackgroundResource(R.drawable.curved_button_shape);
        btnCancel.setText("Cancel");
        btnCancel.setWidth(94);
        btnCancel.setHeight(34);
        btnCancel.setTextColor(getResources().getColor(R.color.white));
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnCancel.layout(2, 0, 0, 0);
        LinearLayout btnLayout = new LinearLayout(this);
        btnLayout.setOrientation(LinearLayout.HORIZONTAL);

        btnLayout.addView(btnLogin);
        btnLayout.addView(btnCancel);


        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setGravity(View.TEXT_ALIGNMENT_CENTER);
        mainLayout.addView(txtUsername);
        mainLayout.addView(txtPassord);
        mainLayout.addView(isAdmin);
        mainLayout.addView(btnLayout);

        dialog.setView(mainLayout);
        dialog.setTitle("Servie Desk");
        dialog.setCancelable(true);
        dialog.show();
    }

}

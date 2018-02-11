package wds.servicedesk;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import data.AddressDataSource;
import data.BrandNameDataSource;
import data.CustomerDataSource;
import data.LocationDataSource;
import data.MobileDataSource;
import data.ServiceDeskDataSource;

public class NewCustomer extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private MobileDataSource mobileDataSource;
    private AddressDataSource addressDataSource;
    private LocationDataSource locationDataSource;
    private BrandNameDataSource brandNameDataSource;
    private CustomerDataSource customerDataSource;
    private Spinner spinnerLocations;
    private Spinner spinnerBrandNames;
    private Spinner spinnerNumberOfCanes;
    private Spinner spinnerFloorNumber;

    private ArrayAdapter<String> locationAdapter;
    private ArrayAdapter<String> brandNamesadapter;
    private ArrayAdapter<String> floorAdapter;
    private ArrayAdapter<String> numberOfCaneadapter;

    private static final String[] numberOfCanes = {"How many cans?", "1", "2", "3", "4"};
    private static final String[] floorNumbers = {"Floor #", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"};
    private List<String> locations;
    private List<String> brandNames;

    private EditText addcustomername;
    private EditText addbuildingnumber;
    private EditText addhousename;
    private EditText adddooornumber;
    private EditText addcrossnumber;
    private EditText addmainnumber;
    private EditText addlandmark;
    private EditText addmobilenumber1;
    private EditText addmobilenumber2;
    private EditText addmobilenumber3;
    private Bundle bundle;

    private List<CustomerDataSource.Customer> allCustomers;
    CustomerDataSource.Customer currentCustomer;
    boolean IsUpdate = false;
    ListView showCustomersListview;
    CustomerListAdapter customerListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        addcustomername = (EditText) findViewById(R.id.addcustomername);
        addbuildingnumber = (EditText) findViewById(R.id.addbuildingnumber);
        addhousename = (EditText) findViewById(R.id.addhousename);
        adddooornumber = (EditText) findViewById(R.id.adddooornumber);
        addcrossnumber = (EditText) findViewById(R.id.addcrossnumber);
        addmainnumber = (EditText) findViewById(R.id.addmainnumber);
        addlandmark = (EditText) findViewById(R.id.addlandmark);
        addmobilenumber1 = (EditText) findViewById(R.id.addmobilenumber1);
        addmobilenumber2 = (EditText) findViewById(R.id.addmobilenumber2);
        addmobilenumber3 = (EditText) findViewById(R.id.addmobilenumber3);

        bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("mobile")) {
            addmobilenumber1.setText(bundle.get("mobile").toString());
            addmobilenumber1.setEnabled(false);
        }

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        //    ServiceDeskDataSource.SetDbAccees(this);
        mobileDataSource = new MobileDataSource();
        brandNameDataSource = new BrandNameDataSource();
        customerDataSource = new CustomerDataSource();
        brandNames = brandNameDataSource.GetBrandNames();
        brandNames.add(0, "Brands");


        spinnerBrandNames = (Spinner) findViewById(R.id.spinnerBrandNames);
        brandNamesadapter = new ArrayAdapter<String>(NewCustomer.this,
                android.R.layout.simple_spinner_item, brandNames);
        brandNamesadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBrandNames.setAdapter(brandNamesadapter);
        spinnerBrandNames.setOnItemSelectedListener(this);

        spinnerNumberOfCanes = (Spinner) findViewById(R.id.addnumberofcane);
        numberOfCaneadapter = new ArrayAdapter<String>(NewCustomer.this,
                android.R.layout.simple_spinner_item, numberOfCanes);
        numberOfCaneadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNumberOfCanes.setAdapter(numberOfCaneadapter);
        spinnerNumberOfCanes.setOnItemSelectedListener(this);

        spinnerFloorNumber = (Spinner) findViewById(R.id.addfloornumber);
        floorAdapter = new ArrayAdapter<String>(NewCustomer.this,
                android.R.layout.simple_spinner_item, floorNumbers);

        floorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFloorNumber.setAdapter(floorAdapter);
        spinnerFloorNumber.setOnItemSelectedListener(this);

        ServiceDeskDataSource.SetDbAccees(this);
        addressDataSource = new AddressDataSource();
        locationDataSource = new LocationDataSource();
        locations = locationDataSource.GetLocations();
        locations.add(0, "Locations");

        spinnerLocations = (Spinner) findViewById(R.id.spinnerLocations);
        locationAdapter = new ArrayAdapter<String>(NewCustomer.this,
                android.R.layout.simple_spinner_item, locations);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLocations.setAdapter(locationAdapter);
        spinnerLocations.setOnItemSelectedListener(this);

        customerListAdapter = new CustomerListAdapter(this,customerDataSource.GetCustomers());
        showCustomersListview = (ListView) findViewById(R.id.show_customers_listview);
        showCustomersListview.setAdapter(customerListAdapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                // Whatever you want to happen when the first item gets selected
                break;
            case 1:
                // Whatever you want to happen when the second item gets selected
                break;
            case 2:
                // Whatever you want to happen when the thrid item gets selected
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //Just for validation purpose
    String customerName;
    String buildNumber;
    String houseName;
    String floorNumber;
    String doorNumber;
    String cross;
    String main;
    String landmark;
    String numberOfCane;
    String brand;
    String mobile1;
    String mobile2;
    String mobile3;
    String location;

    private void TempData() {
        customerName = addcustomername.getText().toString();
        buildNumber = addbuildingnumber.getText().toString();
        houseName = addhousename.getText().toString();
        floorNumber = spinnerFloorNumber.getSelectedItem().toString();
        doorNumber = adddooornumber.getText().toString();
        cross = addcrossnumber.getText().toString();
        main = addmainnumber.getText().toString();
        landmark = addlandmark.getText().toString();
        numberOfCane = spinnerNumberOfCanes.getSelectedItem().toString();
        brand = spinnerBrandNames.getSelectedItem().toString();
        mobile1 = addmobilenumber1.getText().toString();
        mobile2 = addmobilenumber2.getText().toString();
        mobile3 = addmobilenumber3.getText().toString();
        location = spinnerLocations.getSelectedItem().toString();
    }

    @Override
    public void onBackPressed() {
        TempData();
        if ((customerName == null || customerName.trim().equals("")) &&
                (buildNumber == null || buildNumber.trim().equals("")) &&
                (houseName == null || houseName.trim().equals("")) &&
                (floorNumber == null || floorNumber.trim().equals("Floor #")) &&
                (doorNumber == null || doorNumber.trim().equals("")) &&
                (cross == null || cross.trim().equals("")) &&
                (main == null || main.trim().equals("")) &&
                (landmark == null || landmark.trim().equals("")) &&
                (numberOfCane == null || numberOfCane.trim().equals("How many cans?")) &&
                (brand == null || brand.trim().equals("Brands")) &&
                (mobile1 == null || mobile1.trim().equals("")) &&
                (mobile2 == null || mobile2.trim().equals("")) &&
                (mobile3 == null || mobile3.trim().equals("")) &&
                (location == null || location.trim().equals("Locations"))) {
            finish();
        } else if (!customerName.trim().equals("") || !buildNumber.trim().equals("")
                || !houseName.trim().equals("") || !floorNumber.trim().equals("Floor #")
                || !doorNumber.trim().equals("") || !cross.trim().equals("")
                || !main.trim().equals("") || !landmark.trim().equals("")
                || !numberOfCane.trim().equals("How many cans?") || !brand.trim().equals("Brands")
                || !mobile1.trim().equals("") || !mobile2.trim().equals("") || !mobile3.trim().equals("")
                || !location.trim().equals("Locations")) {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            dlgAlert.setMessage("Discard Changes ?");
            dlgAlert.setTitle("Service Desk");
            dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            dlgAlert.setNegativeButton("Cancel", null);
            dlgAlert.setCancelable(true);
            dlgAlert.create().show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_customer_menu, menu);//Menu Resource, Menu
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        final MenuItem searchItem = menu.findItem(R.id.search);
        searchView.setQueryHint("Name/Mobile");
        View closeButton = searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerListAdapter.values.clear();
                customerListAdapter.values.addAll(customerDataSource.GetCustomers());
                customerListAdapter.notifyDataSetChanged();
                searchView.setIconified(true);
                searchView.onActionViewCollapsed();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override

            public boolean onQueryTextSubmit(String query) {
                final List<CustomerDataSource.Customer> filteredModelList = filter(allCustomers, query);
                customerListAdapter.values.retainAll(filteredModelList);
                customerListAdapter.notifyDataSetChanged();
                if(customerListAdapter.values.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Customer not found",Toast.LENGTH_SHORT).show();
                }
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

    private static List<CustomerDataSource.Customer> filter(List<CustomerDataSource.Customer> models, String query) {
        final String lowerCaseQuery = query.toLowerCase();
        final List<CustomerDataSource.Customer> filteredModelList = new ArrayList<>();
        for (CustomerDataSource.Customer model : models) {
            final String name = model.getName().toLowerCase();
            final String mobile1 = model.getMobile1().toLowerCase();
            final String mobile2 = model.getMobile2().toLowerCase();
            final String mobile3= model.getMobile3().toLowerCase();
            if (name.contains(lowerCaseQuery) || mobile1.contains(lowerCaseQuery) || mobile2.contains(lowerCaseQuery) ||
                    mobile3.contains(lowerCaseQuery)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_save_customer:
                TempData();  //Get data for validations
                if (!ValidateFields()) //Validation fails
                    return true;

                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                CustomerDataSource.Customer customer = null;
                boolean success = false;
                if (!IsUpdate) {
                    customer = new CustomerDataSource.Customer();
                } else {
                    customer = currentCustomer;
                }
                customer.name = customerName;
                customer.cane = numberOfCane;
                customer.brand = brand;
                customer.doorNumber = doorNumber;
                customer.buildingNumber = buildNumber;
                customer.floorNumber = Integer.parseInt(floorNumber);
                customer.houseName = houseName;
                customer.cross = cross;
                customer.main = main;
                customer.landmark = landmark;
                customer.location = location;
                customer.mobiles[0] = mobile1;
                customer.mobiles[1] = mobile2;
                customer.mobiles[2] = mobile3;

                AddressDataSource.Address address = new AddressDataSource.Address();
                address.door_number = customer.doorNumber;
                address.building_number = customer.buildingNumber;
                address.floor_number = customer.floorNumber;
                address.house_name = customer.houseName;
                address.cross_number = customer.cross;
                address.main_number = customer.main;
                address.landmark = customer.landmark;
                address.location_name = customer.location;
                if (!IsUpdate) {
                    long result = customerDataSource.SaveCustomer(customer);
                    if (result > -1) {
                        customer.id = String.valueOf(result);
                        address.customer_id = customer.id;
                        addressDataSource.SaveAddress(address);
                        boolean status = mobileDataSource.SaveMobile(new String[]{mobile1, mobile2, mobile3}, address.customer_id);
                        if (status) {
                            success = true;
                            allCustomers.add(customer);
                            customerListAdapter.notifyDataSetChanged();
                            dlgAlert.setMessage("Customer Saved Successfully !");
                            dlgAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if (bundle != null && bundle.containsKey("from") &&
                                            bundle.get("from").toString().equals("Register")) {
                                        customerDataSource.UpdateRegistrationStatus(mobile1, "Registered");
                                        Intent data = new Intent();
                                        data.putExtra("Position", bundle.getInt("Position"));
                                        setResult(RESULT_OK, data);
                                        finish();
                                    }
                                }
                            });
                        }
                    }
                } else {
                    boolean status = customerDataSource.UpdateCustomer(currentCustomer);
                    if (status) {
                        status = false;
                        address.customer_id = currentCustomer.id;
                        status = addressDataSource.UpdateAddress(address);
                        if (status) {
                            status = false;
                            status = mobileDataSource.DeleteMobile(currentCustomer.id);
                            if (status) {
                                status = false;
                                status = mobileDataSource.SaveMobile(new String[]{currentCustomer.mobiles[0],
                                        currentCustomer.mobiles[1], currentCustomer.mobiles[2]}, currentCustomer.id);
                                if (status)
                                    success = true;
                            }
                        }
                    }
                    customerListAdapter.notifyDataSetChanged();
                    dlgAlert.setPositiveButton("OK", null);
                    dlgAlert.setMessage("Customer Updated Successfully !");
                }
                if (success) {
                    ClearFields();
                    addmobilenumber1.requestFocus();
                    dlgAlert.setTitle("Servie Desk");
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();
                }
                break;
            case R.id.menu_new_customer:
                if (bundle != null && bundle.containsKey("from") &&
                        bundle.get("from").toString().equals("Register")) {
                    bundle.remove("from");
                }
                IsUpdate = false;
                addmobilenumber1.setEnabled(true);
                ClearFields();
                addmobilenumber1.requestFocus();
                break;
            case R.id.search:
                Toast.makeText(this, "asdfas", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    //    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//       // System.out.println("onNewIntent SearchResults");
////        Toast.makeText(this,"Searhing started",Toast.LENGTH_SHORT).show();
//        handleIntent(intent);
//    }
//
//    private void handleIntent(Intent intent) {
//
//        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//            String query = intent.getStringExtra(SearchManager.QUERY);
//            Toast.makeText(this,"Searhing started"+query,Toast.LENGTH_SHORT).show();
//            System.out.println("Received query: " + query);
//            Log.d("Done",query);
//        }
//    }
    private boolean ValidateFields() {
        if (customerName.equals("") || customerName.length() < 3) {
            Utility.showAlert("Customer name is empty or not proper", this);
            addcustomername.requestFocus();
            return false;
        }
        if (mobile1.trim().equals("") || mobile1.length() != 10) {
            Utility.showAlert("Mobile number please", this);
            addmobilenumber1.requestFocus();
            return false;
        }
        if (!mobile2.trim().equals("") && mobile2.length() != 10) {
            Utility.showAlert("Invalid Mobile number 2", this);
            addmobilenumber2.requestFocus();
            return false;
        }
        if (!mobile3.trim().equals("") && mobile3.length() != 10) {
            Utility.showAlert("Invalid Mobile number 3", this);
            addmobilenumber3.requestFocus();
            return true;
        }
        if ((!mobile1.equals("") && mobile1.equals(mobile2)) || (!mobile2.equals("") && mobile2.equals(mobile3))
                || (!mobile3.equals("") && mobile3.equals(mobile1))) {
            Utility.showAlert("Mobile numbers are same", this);
            addmobilenumber2.requestFocus();
            return false;
        }
        if (floorNumber.equals("Floor #")) {
            Utility.showAlert("Select floor number", this);
            spinnerFloorNumber.requestFocus();
            return false;
        }
        if (numberOfCane.equals("How many cans?")) {
            Utility.showAlert("Select number of can required", this);
            spinnerNumberOfCanes.requestFocus();
            return false;
        }
        if (brand.equals("Brands")) {
            Utility.showAlert("Select brand can required", this);
            spinnerBrandNames.requestFocus();
            return false;
        }
        return true;
    }

    private void ClearFields() {
        addcustomername.setText("");
        addbuildingnumber.setText("");
        addhousename.setText("");
        spinnerFloorNumber.setSelection(0);
        adddooornumber.setText("");
        addcrossnumber.setText("");
        addmainnumber.setText("");
        addlandmark.setText("");
        spinnerNumberOfCanes.setSelection(0);
        spinnerBrandNames.setSelection(0);
        addmobilenumber1.setText("");
        addmobilenumber2.setText("");
        addmobilenumber3.setText("");
        spinnerLocations.setSelection(0);
    }

    public class CustomerListAdapter extends ArrayAdapter<CustomerDataSource.Customer> {
        private final Context context;
        private final List<CustomerDataSource.Customer> values;

        public CustomerListAdapter(Context context, List<CustomerDataSource.Customer> values) {
            super(context, -1, values);
            this.context = context;
            this.values = values;
            allCustomers = this.values;
        }

        public void replaceAll(List<CustomerDataSource.Customer> models) {
            for (int i = values.size() - 1; i >= 0; i--) {
                final CustomerDataSource.Customer model = values.get(i);
                if (!models.contains(model)) {
                    values.remove(model);
                }
            }
            //  values.addAll(models);
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.customer_list_items, parent, false);

            TextView showMobile = (TextView) rowView.findViewById(R.id.showMobile);
            TextView showCustomername = (TextView) rowView.findViewById(R.id.showCustomername);
            TextView showBuildingnumber = (TextView) rowView.findViewById(R.id.showBuildingnumber);
            TextView showHousename = (TextView) rowView.findViewById(R.id.showHousename);
            TextView showFloornumber = (TextView) rowView.findViewById(R.id.showFloornumber);
            TextView showDoornumber = (TextView) rowView.findViewById(R.id.showDoornumber);
            TextView showCross = (TextView) rowView.findViewById(R.id.showCross);
            TextView showMain = (TextView) rowView.findViewById(R.id.showMain);
            TextView showLocation = (TextView) rowView.findViewById(R.id.showLocation);
            TextView showLandmark = (TextView) rowView.findViewById(R.id.showLandmark);
            TextView showQuantity = (TextView) rowView.findViewById(R.id.showQuantity);
            TextView showBrand = (TextView) rowView.findViewById(R.id.showBrand);

            Button delete = (Button) rowView.findViewById(R.id.btnDeleteCustomer);
            Button edit = (Button) rowView.findViewById(R.id.btnEditCustomer);

            String mobiles = values.get(position).mobiles[0];
            if (values.get(position).mobiles[1] != null && !values.get(position).mobiles[1].equals(""))
                mobiles = mobiles + ", " + values.get(position).mobiles[1];
            if (values.get(position).mobiles[2] != null && !values.get(position).mobiles[2].equals(""))
                mobiles = mobiles + ", " + values.get(position).mobiles[2];
            showMobile.setText(mobiles);

            showCustomername.setText(values.get(position).name);
            if (values.get(position).buildingNumber != null && !values.get(position).buildingNumber.equals(""))
                showBuildingnumber.setText("#" + values.get(position).buildingNumber + ",");
            showHousename.setText(values.get(position).houseName);
            String whichFloor = Utility.GetFloor(values.get(position).floorNumber);
            showFloornumber.setText(whichFloor);
            if (values.get(position).doorNumber != null && !values.get(position).doorNumber.equals(""))
                showDoornumber.setText("Door# " + values.get(position).doorNumber + ",");
            if (values.get(position).cross != null && !values.get(position).cross.equals(""))
                showCross.setText(values.get(position).cross + " Cross,");
            if (values.get(position).main != null && !values.get(position).main.equals(""))
                showMain.setText(values.get(position).main + " Main");
            showLocation.setText(values.get(position).location);
            showLandmark.setText(values.get(position).landmark);
            showQuantity.setText("Quantity - " + values.get(position).cane);
            showBrand.setText("Brand - " + values.get(position).brand);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(v.getContext());
                    dlgAlert.setMessage("Are you sure to delete?");
                    dlgAlert.setTitle("Servie Desk");
                    dlgAlert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), "Not implemented", Toast.LENGTH_SHORT).show();
//                            if (brandNameDataSource.DeleteProduct(values.get(position).brandName)) {
//                                Toast.makeText(v.getContext(), "Deleted Successfully..!", Toast.LENGTH_SHORT).show();
//                                values.remove(position);
//                                notifyDataSetChanged();
//                            } else
//                                Toast.makeText(v.getContext(), "Deletion Failed!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    dlgAlert.setNegativeButton("NO", null);
                    dlgAlert.setCancelable(true);
                    dlgAlert.create().show();

                }
            });
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentCustomer = values.get(position);
                    IsUpdate = true;
                    addmobilenumber1.setEnabled(true);
                    if (currentCustomer.mobiles[0] != null && !currentCustomer.mobiles[0].equals(""))
                        addmobilenumber1.setText(currentCustomer.mobiles[0]);
                    if (currentCustomer.mobiles[1] != null && !currentCustomer.mobiles[1].equals(""))
                        addmobilenumber2.setText(currentCustomer.mobiles[1]);
                    if (currentCustomer.mobiles[2] != null && !currentCustomer.mobiles[2].equals(""))
                        addmobilenumber3.setText(currentCustomer.mobiles[2]);
                    addcustomername.setText(currentCustomer.name);
                    addbuildingnumber.setText(currentCustomer.buildingNumber);
                    addhousename.setText(currentCustomer.houseName);
                    int pos = floorAdapter.getPosition(String.valueOf(currentCustomer.floorNumber));
                    spinnerFloorNumber.setSelection(pos);
                    adddooornumber.setText(currentCustomer.doorNumber);
                    addcrossnumber.setText(currentCustomer.cross);
                    addmainnumber.setText(currentCustomer.main);
                    pos = locationAdapter.getPosition(currentCustomer.location);
                    spinnerLocations.setSelection(pos);
                    addlandmark.setText(currentCustomer.landmark);
                    pos = numberOfCaneadapter.getPosition(currentCustomer.cane);
                    spinnerNumberOfCanes.setSelection(pos);
                    pos = brandNamesadapter.getPosition(currentCustomer.brand);
                    spinnerBrandNames.setSelection(pos);
                    //    addproductname.setEnabled(false);
                }
            });
            return rowView;
        }
    }

}

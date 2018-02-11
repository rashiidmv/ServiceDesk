package wds.servicedesk;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import data.BrandNameDataSource;
import data.CustomerDataSource;

public class NewProduct extends AppCompatActivity {

    private EditText addproductname;
    private EditText addproductprice;
    ListView showProductsListview;
    boolean IsUpdate = false;
    BrandNameDataSource.Product currentProduct;
    private List<BrandNameDataSource.Product> allProducts;
    private boolean isAdmin;
    private Bundle bundle;
    ProductListAdapter productListAdapter;

    private BrandNameDataSource brandNameDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);
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


        brandNameDataSource = new BrandNameDataSource();

        addproductname = (EditText) findViewById(R.id.addproductname);
        addproductprice = (EditText) findViewById(R.id.addproductprice);
        showProductsListview = (ListView) findViewById(R.id.show_products_listview);

        productListAdapter = new ProductListAdapter(this, brandNameDataSource.GetProducts());
        showProductsListview.setAdapter(productListAdapter);

    }

    private void initiatePopupWindow() {
/*        LayoutInflater inflater = (LayoutInflater) NewProduct.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //LayoutInflater inflater = getLayoutInflater();
        View layout=inflater.inflate(R.layout.login_popup,(ViewGroup)findViewById(R.id.login_popup_viewgroup));
        View parent=inflater.inflate(
        R.layout.activity_new_product,(ViewGroup)findViewById(R.id.newproductparent));
        loginPopup=new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, true);
        loginPopup.showAtLocation(layout, Gravity.CENTER,0,0);*/

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!isAdmin) {
            Toast.makeText(this, "You are not Admin", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_product_menu, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_product_save:
                TempData();

                if (brandName.equals("") || brandName.length() < 2) {
                    Utility.showAlert("Brand name is empty or not proper", this);
                    addproductname.requestFocus();
                    return true;
                }
                if (unitPrice.trim().equals("")) {
                    Utility.showAlert("Unit price please", this);
                    addproductprice.requestFocus();
                    return true;
                }


                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                boolean status = false;
                if (!IsUpdate) {
                    BrandNameDataSource.Product product = new BrandNameDataSource.Product();
                    product.brandName = brandName;
                    product.unitPrice = Integer.parseInt(unitPrice);
                    status = brandNameDataSource.SaveProduct(product);
                    if (status) {
                        allProducts.add(product);
                        dlgAlert.setMessage("Product Saved Successfully !");
                    } else {
                        dlgAlert.setMessage("Failed to save, Product already exists!");
                    }
                } else {
                    currentProduct.unitPrice = Integer.parseInt(addproductprice.getText().toString());
                    status = brandNameDataSource.UpdateProduct(currentProduct.brandName, currentProduct.unitPrice);
                    if (status) {
                        addproductname.setEnabled(true);
                        dlgAlert.setMessage("Product Updated Successfully !");
                    } else {
                        dlgAlert.setMessage("Failed to update!");
                    }
                }

                if (status) {
                    addproductname.setText("");
                    addproductprice.setText("");
                    addproductname.requestFocus();
                    productListAdapter.notifyDataSetChanged();
                }
                dlgAlert.setTitle("Servie Desk");
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
                break;
            case R.id.menu_new_product:
                IsUpdate = false;
                addproductname.setEnabled(true);
                addproductname.setText("");
                addproductprice.setText("");
                addproductname.requestFocus();
                break;

        }
        return true;
    }

    String brandName;
    String unitPrice;

    private void TempData() {
        brandName = addproductname.getText().toString().trim();
        unitPrice = addproductprice.getText().toString().trim();
    }

    @Override
    public void onBackPressed() {
        TempData();
        if ((brandName == null || brandName.trim().equals("")) &&
                (unitPrice == null || unitPrice.trim().equals(""))
                ) {
            finish();
        } else if (!brandName.trim().equals("") || !unitPrice.trim().equals("")) {
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


    public class ProductListAdapter extends ArrayAdapter<BrandNameDataSource.Product> {
        private final Context context;
        private final List<BrandNameDataSource.Product> values;

        public ProductListAdapter(Context context, List<BrandNameDataSource.Product> values) {
            super(context, -1, values);
            this.context = context;
            this.values = values;
            allProducts = this.values;
        }


        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.product_list_items, parent, false);
            final TextView brandname = (TextView) rowView.findViewById(R.id.brandname);
            final TextView unitprice = (TextView) rowView.findViewById(R.id.unitprice);
            Button delete = (Button) rowView.findViewById(R.id.btnDeleteProduct);
            Button edit = (Button) rowView.findViewById(R.id.btnEditProduct);
            brandname.setText(values.get(position).brandName);
            unitprice.setText("Unit Price - " + String.valueOf(values.get(position).unitPrice));
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(v.getContext());
                    dlgAlert.setMessage("Are you sure to delete?");
                    dlgAlert.setTitle("Servie Desk");
                    dlgAlert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (IsUpdate && values.get(position).brandName.equals(addproductname.getText().toString())) {
                                addproductname.setEnabled(true);
                                addproductname.setText("");
                                addproductprice.setText("");
                            }
                            if (brandNameDataSource.DeleteProduct(values.get(position).brandName)) {
                                Toast.makeText(v.getContext(), "Deleted Successfully..!", Toast.LENGTH_SHORT).show();
                                productListAdapter.remove(values.get(position));
                                // values.remove(position);
                                notifyDataSetChanged();
                            } else
                                Toast.makeText(v.getContext(), "Deletion Failed!", Toast.LENGTH_SHORT).show();
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
                    currentProduct = values.get(position);
                    IsUpdate = true;
                    addproductname.setEnabled(false);
                    addproductname.setText(currentProduct.brandName);
                    addproductprice.setText(String.valueOf(currentProduct.unitPrice));
                }
            });
            return rowView;
        }
    }

}

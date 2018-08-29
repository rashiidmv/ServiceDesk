package wds.servicedesk.DataExportImport;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import data.ExportImportDataSource;
import data.LocationDataSource;
import wds.servicedesk.R;

import static android.app.Activity.RESULT_OK;

public class Export extends AppCompatActivity {
    private ExportImportDataSource exportImportDataSource;
    private boolean isAdmin;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        exportImportDataSource = new ExportImportDataSource();

        isAdmin = true;
        bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey("IsAdmin")) {
            isAdmin = bundle.getBoolean("IsAdmin");
        }
    }

    public void ExportAll(View view) {
        new Task().execute();
    }

    public void Import(View view) {
        Intent intent = new Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select a file"), 123);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;
        switch (requestCode) {
            case 123:
                if (resultCode == RESULT_OK) {
                    String filePath = data.getData().getPath();
                    String fileName = data.getData().getLastPathSegment();
                    File f = new File(filePath);
                    StringBuilder wholeData = new StringBuilder();
                    try {
                        BufferedReader br = new BufferedReader(new FileReader(filePath));
                        String line;
                        while ((line = br.readLine()) != null) {
                            wholeData.append(line);
                        }
                        br.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isAdmin) {
            Toast.makeText(this, "You are not Admin", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    class Task extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarExport);
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBarExport);
            progressBar.setVisibility(View.GONE);
            super.onPostExecute(result);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                List<String> tableNames;
                tableNames = exportImportDataSource.GetTableNames();

                try {
                    File storageDir = new File(Environment.getExternalStorageDirectory(), "ServiceDesk-" + Calendar.getInstance().getTime().toString());
                    if (!storageDir.exists()) {
                        if (!storageDir.mkdirs()) {
                            Log.d("App", "failed to create directory");
                        }
                    }
                    if (storageDir.exists()) {
                        for (String name : tableNames) {
                            File file = new File(storageDir, name + ".txt");
                            file.createNewFile();
                            FileWriter fileWriter = new FileWriter(file);
                            JSONArray result = exportImportDataSource.getResults(name);
                            fileWriter.write(result.toString());
                            fileWriter.close();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

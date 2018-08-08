package wds.servicedesk.DataExportImport;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import data.ExportImportDataSource;
import data.LocationDataSource;
import wds.servicedesk.R;

public class Export extends AppCompatActivity {
    private ExportImportDataSource exportImportDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        exportImportDataSource = new ExportImportDataSource();
    }

    public void ExportAll(View view) {
        List<String> tableNames;
        tableNames = exportImportDataSource.GetTableNames();

        try {
            File storageDir = new File(Environment.getExternalStorageDirectory(), "ServiceDesk");
            if (!storageDir.exists()) {
                if (!storageDir.mkdirs()) {
                    Log.d("App", "failed to create directory");
                }
            }
            if(storageDir.exists()){
                for (String name:tableNames) {
                    File file = new File(storageDir, name+".txt");
                    file.createNewFile();
                    FileWriter fileWriter=new FileWriter(file);
                    JSONArray result=exportImportDataSource.getResults(name);
                    fileWriter.write(result.toString());
                    fileWriter.close();

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(view.getContext(),e.getMessage(),Toast.LENGTH_SHORT);
        }
    }
}

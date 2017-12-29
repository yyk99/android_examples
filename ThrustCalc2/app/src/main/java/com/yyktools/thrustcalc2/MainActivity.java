package com.yyktools.thrustcalc2;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private double _diam; /* D */
    private double _pitch; /* E */
    private double _rpm;  /* F */
    private int _nBlades; /* G */
    private double _load, _hp, _thrust, _speed;

    public final static int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_showlog) {
            // start log browser
            android.content.Intent intent = new android.content.Intent(this, LogBrowserActivity.class);
            this.startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public String TXT(int id)
    {
        return getResources().getString(id);
    }

    public void aboutButtonPressed(View v) {
        TextView hpOutW = (TextView) findViewById(R.id.textOut1);
        TextView loadOutW = (TextView) findViewById(R.id.textOut2);

        hpOutW.setText(R.string.about_text);
        loadOutW.setText(R.string.version_text);
    }

    public void computeButtonPressed(View v) {
        EditText diamW = (EditText) findViewById(R.id.editDiam); /* D */
        EditText pitchW = (EditText) findViewById(R.id.editPitch);   /* E */
        EditText rpmW = (EditText) findViewById(R.id.editRPM);       /* F */
        EditText bladesW = (EditText) findViewById(R.id.editBlades); /* G */

        TextView hpOutW = (TextView) findViewById(R.id.textOut1);
        TextView loadOutW = (TextView) findViewById(R.id.textOut2);

        double diam = 0; /* D */
        double pitch = 0; /* E */
        double rpm = 0;  /* F */
        int nBlades = 2; /* G */

        try {
            diam = Double.parseDouble(diamW.getText().toString());
        } catch (Exception e) {
        }
        try {
            pitch = Double.parseDouble(pitchW.getText().toString());
        } catch (Exception e) {
        }
        try {
            rpm = Double.parseDouble(rpmW.getText().toString());
        } catch (Exception e) {
        }
        try {
            nBlades = Integer.parseInt(bladesW.getText().toString());
        } catch (Exception e) {
        }
        bladesW.setText(String.format(Locale.US, "%d", nBlades));

        ThrustCalculator tc = new ThrustCalculator();
        try {
            tc.calculate(diam, pitch, rpm, nBlades);
            double load, hp, thrust, speed;
            load = tc.getLoad();
            thrust = tc.getThrust();
            hp = tc.getHp();
            speed = tc.getSpeed();

            hpOutW.setText(String.format(Locale.US, "%.3fhp, %.2flbs, %.0fmph", hp, thrust, speed));
            loadOutW.setText(String.format(Locale.US, "%.0f prop load", load));
            // save input and results
            _diam = diam;
            _pitch = pitch;
            _rpm = rpm;
            _nBlades = nBlades;

            _hp = hp;
            _thrust = thrust;
            _speed = speed;
            _load = load;
        } catch (RuntimeException ex) {
            hpOutW.setText(String.format(Locale.US, "Calculation failed"));
            loadOutW.setText(String.format(Locale.US, "Err: %s", ex.getMessage()));
        }
    }

    public void recordButtonPressed(View v) {
        TextView hpOutW = (TextView) findViewById(R.id.textOut1);
        TextView loadOutW = (TextView) findViewById(R.id.textOut2);

        // Check for permission to write files
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
                return;
            }
        }

        Date now = new Date();

        CVSWriter log = new CVSWriter("thrust_hp.csv");
        String log_line = String.format(Locale.US,"%s,%g,%g,%g,%d,%g,%g,%g,%g",
                now.toString(),
                _diam, _pitch, _rpm, _nBlades,
                _hp, _thrust, _speed, _load
        );
        boolean ok = log.writeLine(log_line);
        if(!ok) {
            hpOutW.setText(String.format(Locale.US,"...failed"));
            loadOutW.setText(String.format(Locale.US,"...error..."));
        } else {
            hpOutW.setText(String.format(Locale.US,"...saved"));
            loadOutW.setText(String.format(Locale.US,"...in log file"));
        }
    }


}

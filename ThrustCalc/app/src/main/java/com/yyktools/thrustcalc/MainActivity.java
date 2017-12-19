package com.yyktools.thrustcalc;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
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

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

//import java.sql.Time;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private double _diam; /* D */
    private double _pitch; /* E */
    private double _rpm;  /* F */
    private int _nBlades; /* G */
    private double _load, _hp, _thrust, _speed;

    public final static int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 42;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

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

    //
    //  TODO: add memo field
    //

    //
    // Save current record to the log file
    //
    public void recordButtonPressed(View v) {
        EditText hpOutW = (EditText) findViewById(R.id.hpOut);
        EditText loadOutW = (EditText) findViewById(R.id.loadOut);

        // Check for permission to write files
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
        String log_line = String.format("%s,%g,%g,%g,%d,%g,%g,%g,%g",
                now.toString(),
                _diam, _pitch, _rpm, _nBlades,
                _hp, _thrust, _speed, _load
                );
        boolean ok = log.writeLine(log_line);
        if(!ok) {
            hpOutW.setText(String.format("...failed"));
            loadOutW.setText(String.format("...error..."));
        } else {
            hpOutW.setText(String.format("...saved"));
            loadOutW.setText(String.format("...in log file"));
        }
    }

    public void computeButtonPressed(View v) {
        EditText diamW = (EditText) findViewById(R.id.diameter); /* D */
        EditText pitchW = (EditText) findViewById(R.id.pitch);   /* E */
        EditText rpmW = (EditText) findViewById(R.id.rpm);       /* F */
        EditText bladesW = (EditText) findViewById(R.id.blades); /* G */

        EditText hpOutW = (EditText) findViewById(R.id.hpOut);
        EditText loadOutW = (EditText) findViewById(R.id.loadOut);

        double diam = 0; /* D */
        double pitch = 0; /* E */
        double rpm = 0;  /* F */
        int nBlades = 2; /* G */

        try {
            diam = Double.parseDouble(diamW.getText().toString());
        } catch (Exception e) { }
        try {
            pitch = Double.parseDouble(pitchW.getText().toString());
        } catch (Exception e) { }
        try {
            rpm = Double.parseDouble(rpmW.getText().toString());
        } catch (Exception e) { }
        try {
            nBlades = Integer.parseInt(bladesW.getText().toString());
        } catch (Exception e) { }
        bladesW.setText(Integer.toString(nBlades));

        double load, hp, thrust, speed;
        {
            double H = -0.05 * nBlades * nBlades + 0.65 * nBlades - 0.1;
            double I = pitch / diam;
            double J = diam * diam * diam * diam * pitch;  // load =POWER(D36,4)*E36
            load = J;
            double K = pitch * 0.000947 * rpm; // speed (mph) =E36*0.000947*F36
            speed = K;
            // hp =(POWER(F36,3)*POWER(D36,5))/1000000000000000000*I36*7.143*G36/2
            double L = Math.pow(rpm, 3) * Math.pow(diam, 5) / 1000000000000000000.0 * I * 7.143 * nBlades / 2;
            hp = L;
            // thrust(lds)  =(POWER(F36,2)*POWER(D36,4))/1000000000000*2.83*H36
            double M = (Math.pow(rpm, 2) * Math.pow(diam, 4) / 1000000000000.0 * 2.83 * H);
            thrust = M;
        }

        hpOutW.setText(String.format("%.3fhp, %.2flbs, %.0fmph", hp, thrust, speed));
        loadOutW.setText(String.format("%.0f prop load", load));
        // save input and results
        _diam = diam;
        _pitch = pitch;
        _rpm = rpm;
        _nBlades = nBlades;

        _hp = hp;
        _thrust = thrust;
        _speed = speed;
        _load = load;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.yyktools.thrustcalc/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.yyktools.thrustcalc/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}

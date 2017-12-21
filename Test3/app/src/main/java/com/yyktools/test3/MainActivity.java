package com.yyktools.test3;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.EditText;
import android.widget.TextView;

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

        return super.onOptionsItemSelected(item);
    }

    public void aboutButtonPressed(View v) {
        TextView hpOutW = (TextView) findViewById(R.id.textOut1);
        TextView loadOutW = (TextView) findViewById(R.id.textOut2);

        hpOutW.setText(String.format("Version:"));
        loadOutW.setText(String.format("2017/12/20.01"));
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
}

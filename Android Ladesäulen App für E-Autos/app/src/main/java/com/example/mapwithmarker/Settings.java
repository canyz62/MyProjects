package com.example.mapwithmarker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

public class Settings extends AppCompatActivity {

    public static final String EXTRA_UMKREIS = "com.example.application.example.EXTRA_UMKREIS";

    SeekBar seekBarUmkreis;
    TextView textViewUmkreis;
    Button ButtonUmkreis;
    Button Logout;
    TextView UserName;
    TextView RoleName;
    EditText Lat;
    EditText Long;
    private int UmkreisValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ButtonUmkreis = (Button) findViewById(R.id.ButtonUmkreis);
        ButtonUmkreis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            openActivityMapsUmkreis();
            }
        });

        Logout = (Button) findViewById(R.id.logout);
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityLogout();
            }
        });

        Lat = (EditText) findViewById(R.id.Breitengradtext);
        Long = (EditText) findViewById(R.id.Laengengradtext);


        seekBarUmkreis = (SeekBar)findViewById(R.id.SeekBarUmkreis);
        textViewUmkreis = (TextView) findViewById(R.id.TextViewUmkreis);

        seekBarUmkreis.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String Umkreis= getString(R.string.umkreis_0_km);
                textViewUmkreis.setText( Umkreis + " " + String.valueOf(progress) +" km");
                UmkreisValue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        UserName = (TextView)findViewById(R.id.Username);
        RoleName = (TextView) findViewById(R.id.Rolename);

        Intent NameIntent = getIntent();
        String Username = NameIntent.getExtras().getString("UsernameIntentToSettings");

        DBHelper DB = new DBHelper(this);
        String Role = DB.GetRole(Username);

        UserName.setText("Name: " + Username);

        RoleName.setText("Role: " + Role);

    }

    public void openActivityMapsUmkreis(){
        UmkreisValue= UmkreisValue*1000;

        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_UMKREIS, UmkreisValue);
        resultIntent.putExtra("latitude",Lat.getText().toString());
        resultIntent.putExtra("longitude",Long.getText().toString());
        setResult(this.RESULT_OK, resultIntent);
        finish();
    }

    public void openActivityLogout(){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}
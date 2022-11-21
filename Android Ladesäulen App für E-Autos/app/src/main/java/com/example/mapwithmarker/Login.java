package com.example.mapwithmarker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    private EditText Name;
    private EditText Password;
    private Button login;
    private Button register;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Name = (EditText) findViewById(R.id.edName);
        Password = (EditText) findViewById(R.id.edPassword);
        DB = new DBHelper(this);

        login = (Button) findViewById(R.id.btnLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user = Name.getText().toString();
                String pass = Password.getText().toString();

                if(user.equals("")||pass.equals(""))
                    Toast.makeText(Login.this, "Bitte alle Felder ausfüllen", Toast.LENGTH_SHORT).show();
                else{
                    Boolean checkuserpass = DB.checkusernamepassword(user, pass);
                    if(checkuserpass==true){
                        Toast.makeText(Login.this, "Einloggen erfolgreich", Toast.LENGTH_SHORT).show();
                        Intent intent  = new Intent(getApplicationContext(), MapsMarkerActivity.class);
                        intent.putExtra("UsernameIntent", user);

                        startActivity(intent);
                    }else{
                        Toast.makeText(Login.this, "Ungültige Eingaben", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        register = (Button) findViewById(R.id.btnToRegister);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openActivityRegister();

            }
        });


    }

    public void openMainActivity(Object user, Object pw) {
        if ((user == "admin") && (pw == "1")) {
            Intent intent = new Intent(this, MapsMarkerActivity.class);
            startActivity(intent);
        }

    }

    public void openActivityAfterLogin(Object user, Object pw) {
        Intent intent = new Intent(this, MapsMarkerActivity.class);
        startActivity(intent);
    }

    public void openActivityRegister() {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }
}


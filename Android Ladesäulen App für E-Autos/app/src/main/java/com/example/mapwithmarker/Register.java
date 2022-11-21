package com.example.mapwithmarker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    private EditText Name;
    private EditText Password;
    private EditText RePassword;
    private Button register;
    private Spinner Spinnerrole;

    String selectedrole;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        Spinnerrole = (Spinner) findViewById(R.id.spinnerrole);

        ArrayAdapter<String> SpinnerAdapter = new ArrayAdapter<String>(Register.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.rollen));
        SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinnerrole.setAdapter(SpinnerAdapter);

        Spinnerrole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedrole = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Name = (EditText) findViewById(R.id.edName2);
        Password = (EditText) findViewById(R.id.edPassword2);
        RePassword = (EditText) findViewById(R.id.edRePassword);
        DB = new DBHelper(this);


        register = (Button) findViewById(R.id.btnRegister2);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user = Name.getText().toString();
                String pass = Password.getText().toString();
                String repass = RePassword.getText().toString();
                String role = selectedrole;

                if(user.equals("")||pass.equals("")||repass.equals(""))
                Toast.makeText(Register.this, "Bitte alle Felder ausfüllen", Toast.LENGTH_SHORT).show();
                else{
                    if(pass.equals(repass)){
                        Boolean checkuser = DB.checkusername(user);
                        if(checkuser==false){
                            Boolean insert = DB.insertData(user, pass, role);
                            if(insert==true){
                                Toast.makeText(Register.this, "Registrierung erfolgreich", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),MapsMarkerActivity.class);
                                intent.putExtra("UsernameIntent", user);

                                startActivity(intent);
                            }else{
                                Toast.makeText(Register.this, "Registration fehlgeschhlagen", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(Register.this, "Konto existiert bereits. Bitte einloggen", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(Register.this, "Passwörter stimmen nicht überein", Toast.LENGTH_SHORT).show();
                    }
                } }

        });


    }


    public void openMainActivity(Object user, Object pw) {

        if ((user == "admin") && (pw == "1")) {
            Intent intent = new Intent(this, MapsMarkerActivity.class);
            startActivity(intent);
        }

    }

    public void openActivityAfterRegister(Object user, Object pw) {
        Intent intent = new Intent(this, MapsMarkerActivity.class);
        startActivity(intent);
    }
}
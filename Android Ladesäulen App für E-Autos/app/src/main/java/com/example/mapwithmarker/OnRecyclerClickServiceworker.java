package com.example.mapwithmarker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class OnRecyclerClickServiceworker extends AppCompatActivity {

    RecyclerViewItem RecyclerViewObject = null;

    public ImageView mImageView;
    public TextView mLadeSaeuleName;
    public TextView mLadeSaeuleHausnummer;
    public TextView mLadeSaeuleStrasse;
    public TextView mLadeSaeulePostal;
    public TextView mLadeSaeuleStadt;
    public TextView mLadeSauleConPower;
    public TextView DefektText;
    public Button ReparierenButton;

    public static boolean fromSearch = false;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";

    private String text;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_recycler_click_serviceworker);

        mImageView = findViewById(R.id.imageViewInfo);
        mLadeSaeuleName = findViewById(R.id.LadeSaeuleName);
        mLadeSaeuleHausnummer = findViewById(R.id.LadeSaeuleHausnummer);
        mLadeSaeuleStrasse = findViewById(R.id.LadeSaeuleStrasse);
        mLadeSaeulePostal = findViewById(R.id.LadeSaeulePostal);
        mLadeSaeuleStadt = findViewById(R.id.LadeSaeuleStadt);
        mLadeSauleConPower = findViewById(R.id.LadeSauleConPower);
        DefektText = findViewById(R.id.DefektText);
        ReparierenButton =findViewById(R.id.ReparierenButton);


        final Object object = getIntent().getSerializableExtra("Objekt");

        if(object instanceof RecyclerViewItem){
            RecyclerViewObject = (RecyclerViewItem) object;
        }

        if(RecyclerViewObject != null){
            mImageView.setImageResource(RecyclerViewObject.getImageResource());

            mLadeSaeuleName.setText(RecyclerViewObject.getText1());

            mLadeSaeuleHausnummer.setText(RecyclerViewObject.getText2());

            mLadeSaeuleStrasse.setText(RecyclerViewObject.getText3());

            mLadeSaeulePostal.setText(RecyclerViewObject.getText4());

            mLadeSaeuleStadt.setText(RecyclerViewObject.getText5());

            mLadeSauleConPower.setText(RecyclerViewObject.getText6() + " kW");

            DefektText.setText(RecyclerViewObject.getDefektText());
            //test = RecyclerViewObject.getDefektText();

        }

        //loadData();
        //updateViews();
        //DefektText.getText().toString();

        ReparierenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //DefektText.setText(DefektText.getText().toString());
                //RecyclerViewObject.setDefekt(true);
                //saveData();
                //RecyclerViewObject.setDefektText(DefektText.getText().toString());

                boolean isDefect = false;

                DefektText.setText("");
                String TextforDefekt = DefektText.getText().toString();

                final Object object = getIntent().getSerializableExtra("Objekt");

                if(object instanceof RecyclerViewItem){
                    RecyclerViewObject = (RecyclerViewItem) object;
                }

                Intent NameIntent = getIntent();
                int itemposition = NameIntent.getExtras().getInt("ItemPos");
                fromSearch = NameIntent.getExtras().getBoolean("fromSearch");

                boolean fromWhich = NameIntent.getExtras().getBoolean("fromWhich");
                Intent intent;
                if(!fromWhich){
                    intent = new Intent(OnRecyclerClickServiceworker.this, Search.class);
                }else{
                    intent = new Intent(OnRecyclerClickServiceworker.this, Favoriten.class);
                }
                intent.putExtra("Objekt2", RecyclerViewObject);
                intent.putExtra("ITEXT", TextforDefekt);
                intent.putExtra("IDEFEKT", isDefect);
                intent.putExtra("IPOS", itemposition);
                startActivity(intent);

            }
        });



    }
/*
    public void loadData(){

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        text = sharedPreferences.getString(TEXT, "");

    }

    public void updateViews(){

        DefektText.setText(text);

    } */
}



package com.example.mapwithmarker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.ArrayList;

public class Search extends AppCompatActivity {

    RecyclerViewItem RecyclerViewObject = null;

    private RecyclerView mRecyclerView;
    private Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static ArrayList<RecyclerViewItem> LadeStationen = new ArrayList<>();
    private static ArrayList<RecyclerViewItem> TempLadeStationen = new ArrayList<>();
    private static ArrayList<RecyclerViewItem> LadeStationInDerNaehe = new ArrayList<>();
    private static boolean DpisrunOnce = false;
    public Switch NaeheSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        if (DpisrunOnce) {

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            NaeheSwitch = findViewById(R.id.switchumkreis);

            if(OnRecyclerClickUser.fromSearch){
                Intent Intent = getIntent();
                int itempos = Intent.getExtras().getInt("IPOS");

                final Object object = getIntent().getSerializableExtra("Objekt2");
                if(object instanceof RecyclerViewItem){
                    RecyclerViewObject = (RecyclerViewItem) object;
                }
                TempLadeStationen.set(itempos,RecyclerViewObject);

                LadeStationen.clear();
                for (int i = 0 ; i<TempLadeStationen.size();i++){
                    LadeStationen.add(TempLadeStationen.get(i)) ;
                }
                OnRecyclerClickUser.fromSearch = false;
                OnRecyclerClickServiceworker.fromSearch = false;
            }
            else if(OnRecyclerClickServiceworker.fromSearch){
                Intent Intent = getIntent();
                int itempos = Intent.getExtras().getInt("IPOS");

                final Object object = getIntent().getSerializableExtra("Objekt2");
                if(object instanceof RecyclerViewItem){
                    RecyclerViewObject = (RecyclerViewItem) object;
                }
                TempLadeStationen.set(itempos,RecyclerViewObject);

                LadeStationen.clear();
                for (int i = 0 ; i<TempLadeStationen.size();i++){
                    LadeStationen.add(TempLadeStationen.get(i)) ;
                }
                OnRecyclerClickUser.fromSearch = false;
                OnRecyclerClickServiceworker.fromSearch = false;
            }
            else {
                TempLadeStationen.clear();
                for (int i = 0 ; i<LadeStationen.size();i++){
                    TempLadeStationen.add(LadeStationen.get(i)) ;
                }
            }
                mRecyclerView = findViewById(R.id.recyclerView);
                mRecyclerView.setHasFixedSize(true);
                mLayoutManager = new LinearLayoutManager(this);
                mAdapter = new Adapter(LadeStationen);

                mRecyclerView.setLayoutManager(mLayoutManager);
                mRecyclerView.setAdapter(mAdapter);

            if(MapsMarkerActivity.Role.equals("User")){
                if(MapsMarkerActivity.fromMapsActivity){

                    MapsMarkerActivity.fromMapsActivity = false;

                }else{
                    Intent NameIntent = getIntent();
                    String itemtext = NameIntent.getExtras().getString("ITEXT");
                    boolean defectstatus = NameIntent.getExtras().getBoolean("IDEFEKT");
                    int itemposition = NameIntent.getExtras().getInt("IPOS");

                    LadeStationen.get(itemposition).setDefektText(itemtext);
                    LadeStationen.get(itemposition).setDefekt(defectstatus);

                    if (MapsMarkerActivity.Stationen.get(itemposition).getModule_type() == ModuleType.STANDARD) {
                        LadeStationen.get(itemposition).setmImageResource(R.drawable.ic_normalestationdefekt);
                    }
                    if (MapsMarkerActivity.Stationen.get(itemposition).getModule_type() == ModuleType.FAST_CHARGING) {
                        LadeStationen.get(itemposition).setmImageResource(R.drawable.ic_schnellestationdefekt);
                    }
                }
            }
            else if(MapsMarkerActivity.Role.equals("Servicetechniker")){
                if(MapsMarkerActivity.fromMapsActivity){

                    MapsMarkerActivity.fromMapsActivity = false;

                }else{
                    Intent NameIntent = getIntent();
                    String itemtext = NameIntent.getExtras().getString("ITEXT");
                    boolean defectstatus = NameIntent.getExtras().getBoolean("IDEFEKT");
                    int itemposition = NameIntent.getExtras().getInt("IPOS");

                    LadeStationen.get(itemposition).setDefektText(itemtext);
                    LadeStationen.get(itemposition).setDefekt(defectstatus);

                    if (MapsMarkerActivity.Stationen.get(itemposition).getModule_type() == ModuleType.STANDARD) {
                        LadeStationen.get(itemposition).setmImageResource(R.drawable.ic_normalestation);
                    }
                    if (MapsMarkerActivity.Stationen.get(itemposition).getModule_type() == ModuleType.FAST_CHARGING) {
                        LadeStationen.get(itemposition).setmImageResource(R.drawable.ic_schnellestation);
                    }
                }
            }

            NaeheSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(!OnRecyclerClickUser.fromSearch || !OnRecyclerClickServiceworker.fromSearch){
                        LadeStationInDerNaehe.clear();
                        if(isChecked) {
                            for (int i = 0; i < MapsMarkerActivity.Stationen.size(); i++) {
                                if (MapsMarkerActivity.Stationen.get(i).isInDerNaehe()) {
                                    LadeStationInDerNaehe.add(LadeStationen.get(i));
                                }
                            }
                            mAdapter = new Adapter(LadeStationInDerNaehe);
                            mRecyclerView.setAdapter(mAdapter);
                        } else {
                            for (int i = 0; i < MapsMarkerActivity.Stationen.size(); i++) {
                                if (MapsMarkerActivity.Stationen.get(i).isInDerNaehe()) {
                                    LadeStationInDerNaehe.remove(LadeStationen.get(i));
                                }
                            }
                            mAdapter = new Adapter(LadeStationen);
                            mRecyclerView.setAdapter(mAdapter);
                        }
                    }
                }
            });
        } else {

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            for (int i = 0; i < MapsMarkerActivity.Stationen.size(); i++) {
                if (MapsMarkerActivity.Stationen.get(i).getModule_type() == ModuleType.STANDARD) {
                    LadeStationen.add(new RecyclerViewItem(i,R.drawable.ic_normalestation
                            , MapsMarkerActivity.Stationen.get(i).getOperator()
                            , MapsMarkerActivity.Stationen.get(i).getStreet(), String.valueOf(MapsMarkerActivity.Stationen.get(i).getNumber())
                            , String.valueOf(MapsMarkerActivity.Stationen.get(i).getPostal_code())
                            , MapsMarkerActivity.Stationen.get(i).getLocation(),MapsMarkerActivity.Stationen.get(i).getConn_power()));
                }
                if (MapsMarkerActivity.Stationen.get(i).getModule_type() == ModuleType.FAST_CHARGING) {
                    LadeStationen.add(new RecyclerViewItem(i,R.drawable.ic_schnellestation
                            , MapsMarkerActivity.Stationen.get(i).getOperator()
                            , MapsMarkerActivity.Stationen.get(i).getStreet()
                            , String.valueOf(MapsMarkerActivity.Stationen.get(i).getNumber())
                            , String.valueOf(MapsMarkerActivity.Stationen.get(i).getPostal_code())
                            , MapsMarkerActivity.Stationen.get(i).getLocation(),MapsMarkerActivity.Stationen.get(i).getConn_power()));
                }
            }

            NaeheSwitch = findViewById(R.id.switchumkreis);

            for (int i = 0 ; i<LadeStationen.size();i++){
                TempLadeStationen.add(LadeStationen.get(i)) ;
            }

            mRecyclerView = findViewById(R.id.recyclerView);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(this);
            mAdapter = new Adapter(LadeStationen);

            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);

            DpisrunOnce = true;
            MapsMarkerActivity.fromMapsActivity = false;

            NaeheSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (!OnRecyclerClickUser.fromSearch || !OnRecyclerClickServiceworker.fromSearch) {
                        LadeStationInDerNaehe.clear();
                        if (isChecked) {
                            for (int i = 0; i < MapsMarkerActivity.Stationen.size(); i++) {
                                if (MapsMarkerActivity.Stationen.get(i).isInDerNaehe()) {
                                    LadeStationInDerNaehe.add(LadeStationen.get(i));
                                }
                            }
                            mAdapter = new Adapter(LadeStationInDerNaehe);
                            mRecyclerView.setAdapter(mAdapter);
                        } else {
                            for (int i = 0; i < MapsMarkerActivity.Stationen.size(); i++) {
                                if (MapsMarkerActivity.Stationen.get(i).isInDerNaehe()) {
                                    LadeStationInDerNaehe.remove(LadeStationen.get(i));
                                }
                            }
                            mAdapter = new Adapter(LadeStationen);
                            mRecyclerView.setAdapter(mAdapter);
                        }
                    }
                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    public void onBackPressed() {
        Intent openMainActivity = new Intent(Search.this, MapsMarkerActivity.class);
        openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityIfNeeded(openMainActivity, 0);
    }
}
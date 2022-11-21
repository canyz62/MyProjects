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

public class Favoriten extends AppCompatActivity {

    RecyclerViewItem RecyclerViewObject = null;

    private RecyclerView mRecyclerView;
    private FavAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static ArrayList<RecyclerViewItem> FavLadeStationen = new ArrayList<>();
    private static ArrayList<RecyclerViewItem> TempLadeStationen = new ArrayList<>();
    private static ArrayList<RecyclerViewItem> LadeStationInDerNaehe = new ArrayList<>();
    public Switch NaeheSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoriten);


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

            FavLadeStationen.clear();
            for (int i = 0 ; i<TempLadeStationen.size();i++){
                FavLadeStationen.add(TempLadeStationen.get(i)) ;
            }
            OnRecyclerClickUser.fromSearch = false;
        }
        else if(OnRecyclerClickServiceworker.fromSearch){
            Intent Intent = getIntent();
            int itempos = Intent.getExtras().getInt("IPOS");

            final Object object = getIntent().getSerializableExtra("Objekt2");
            if(object instanceof RecyclerViewItem){
                RecyclerViewObject = (RecyclerViewItem) object;
            }
            TempLadeStationen.set(itempos,RecyclerViewObject);

            FavLadeStationen.clear();
            for (int i = 0 ; i<TempLadeStationen.size();i++){
                FavLadeStationen.add(TempLadeStationen.get(i)) ;
            }
            OnRecyclerClickServiceworker.fromSearch = false;
        }
        else {
            TempLadeStationen.clear();
            for (int i = 0 ; i<FavLadeStationen.size();i++){
                TempLadeStationen.add(FavLadeStationen.get(i)) ;
            }
        }

        for (int i = 0; i < FavLadeStationen.size(); i++) {
            FavLadeStationen.get(i).setFavoriteID(i);
        }

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new FavAdapter(FavLadeStationen);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        final Object object = getIntent().getSerializableExtra("Objekt2");
        if(object instanceof RecyclerViewItem){
            RecyclerViewObject = (RecyclerViewItem) object;
        }

        if (MapsMarkerActivity.Role.equals("User")) {
            if (MapsMarkerActivity.fromMapsActivity) {

                MapsMarkerActivity.fromMapsActivity = false;

            } else {
                Intent NameIntent = getIntent();
                String itemtext = NameIntent.getExtras().getString("ITEXT");
                boolean defectstatus = NameIntent.getExtras().getBoolean("IDEFEKT");
                int itemposition = NameIntent.getExtras().getInt("IPOS");

                FavLadeStationen.get(itemposition).setDefektText(itemtext);
                FavLadeStationen.get(itemposition).setDefekt(defectstatus);

                if (MapsMarkerActivity.Stationen.get(RecyclerViewObject.getID()).getModule_type() == ModuleType.STANDARD) {
                    FavLadeStationen.get(itemposition).setmImageResource(R.drawable.ic_normalestationdefekt);
                }
                if (MapsMarkerActivity.Stationen.get(RecyclerViewObject.getID()).getModule_type() == ModuleType.FAST_CHARGING) {
                    FavLadeStationen.get(itemposition).setmImageResource(R.drawable.ic_schnellestationdefekt);
                }
            }
        } else if (MapsMarkerActivity.Role.equals("Servicetechniker")) {
            if (MapsMarkerActivity.fromMapsActivity){

                MapsMarkerActivity.fromMapsActivity = false;

            }else{
                Intent NameIntent = getIntent();
                String itemtext = NameIntent.getExtras().getString("ITEXT");
                boolean defectstatus = NameIntent.getExtras().getBoolean("IDEFEKT");
                int itemposition = NameIntent.getExtras().getInt("IPOS");

                FavLadeStationen.get(itemposition).setDefektText(itemtext);
                FavLadeStationen.get(itemposition).setDefekt(defectstatus);

                if (MapsMarkerActivity.Stationen.get(RecyclerViewObject.getID()).getModule_type() == ModuleType.STANDARD) {
                    FavLadeStationen.get(itemposition).setmImageResource(R.drawable.ic_normalestation);
                }
                if (MapsMarkerActivity.Stationen.get(RecyclerViewObject.getID()).getModule_type() == ModuleType.FAST_CHARGING) {
                    FavLadeStationen.get(itemposition).setmImageResource(R.drawable.ic_schnellestation);
                }
            }
        }

        NaeheSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!OnRecyclerClickUser.fromSearch || !OnRecyclerClickServiceworker.fromSearch){
                    if (isChecked) {
                        LadeStationInDerNaehe.clear();
                        for (int i = 0; i < FavLadeStationen.size(); i++) {
                            if (FavLadeStationen.get(i).getIstInDerNaehe()) {
                                LadeStationInDerNaehe.add(FavLadeStationen.get(i));
                            }
                        }
                        mAdapter = new FavAdapter(LadeStationInDerNaehe);
                        mRecyclerView.setAdapter(mAdapter);
                    } else {
                        for (int i = 0; i < FavLadeStationen.size(); i++) {
                            if (FavLadeStationen.get(i).getIstInDerNaehe()) {
                                LadeStationInDerNaehe.remove(FavLadeStationen.get(i));
                            }
                        }
                        mAdapter = new FavAdapter(FavLadeStationen);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                }
            }
        });
        MapsMarkerActivity.fromMapsActivity = false;

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

    public static void setFav_list(RecyclerViewItem FavItem) {
        FavLadeStationen.add(FavItem);
    }

    public static void removeFav_list(RecyclerViewItem FavItem) {
        FavLadeStationen.remove(FavItem);
    }

    public static ArrayList<RecyclerViewItem> getFav_list() {
        return FavLadeStationen;
    }

    @Override
    public void onBackPressed() {
        Intent openMainActivity = new Intent(Favoriten.this, MapsMarkerActivity.class);
        openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivityIfNeeded(openMainActivity, 0);
    }
}
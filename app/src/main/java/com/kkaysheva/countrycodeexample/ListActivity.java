package com.kkaysheva.countrycodeexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.kkaysheva.countrycodeexample.adapter.Country;
import com.kkaysheva.countrycodeexample.adapter.CountryAdapter;
import com.kkaysheva.countrycodeexample.manager.CountryManager;

import static com.kkaysheva.countrycodeexample.MainActivity.EXTRA_COUNTRY;

public class ListActivity extends AppCompatActivity {

    private RecyclerView list;
    private String selectedCountry = "";
    private CountryAdapter adapter;
    private CountryManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        manager = CountryManager.getInstance(this);
        Intent intent = getIntent();
        if (intent != null) {
            selectedCountry = intent.getStringExtra(EXTRA_COUNTRY);
        }

        list = findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setHasFixedSize(true);
        adapter = new CountryAdapter(this::setCountry);
        adapter.setCountries(manager.getAllCountry());
        list.setAdapter(adapter);
    }

    private void setResult() {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_COUNTRY, selectedCountry);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void setCountry(Country country) {
        selectedCountry = country.getName();
        Log.d("LIST", "setCountry: selected " + country.getName());
    }

    @Override
    public void onBackPressed() {
        setResult();
    }
}

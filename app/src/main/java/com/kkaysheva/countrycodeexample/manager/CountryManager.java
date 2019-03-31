package com.kkaysheva.countrycodeexample.manager;

import android.content.Context;
import android.util.Log;

import com.kkaysheva.countrycodeexample.adapter.Country;
import com.kkaysheva.countrycodeexample.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class CountryManager {

    private static CountryManager instance;

    private List<Country> countries = new ArrayList<>(255);
    private Map<String, String> countriesMap = new HashMap<>(255, 1);
    private Map<String, String> codesMap = new HashMap<>(255, 1);
    private Map<String, String> languageMap = new HashMap<>(255, 1);
    private Map<String, String> phoneMaskMap = new HashMap<>(255, 1);

    private CountryManager(@NonNull Context context) {
        init(context);
    }

    public static CountryManager getInstance(@NonNull Context context) {
        if (instance == null) {
            synchronized (CountryManager.class) {
                if (instance == null) {
                    instance = new CountryManager(context);
                }
            }
        }
        return instance;
    }

    public List<Country> getAllCountry() {
        return countries;
    }

    public String getCountryNameByIso(String iso) {
        return languageMap.get(iso);
    }

    public String getCodeByCountryName(String name) {
        return countriesMap.get(name);
    }

    public String getCountryNameByCode(String code) {
        return codesMap.get(code);
    }

    public String getPhoneMaskByCode(String code) {
        return phoneMaskMap.get(code);
    }

    private void init(@NonNull Context context) {
        try (InputStream stream = context.getResources().openRawResource(R.raw.country);
             BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] args = line.split(";");
                String name = args[2];
                String dialCode = args[0];
                String iso = args[1];
                String mask = "";
                if (args.length > 3) {
                    mask = args[3];
                    phoneMaskMap.put(args[0], args[3]);
                }
                countries.add(new Country(name, iso, dialCode, mask));
                countriesMap.put(args[2], args[0]);
                codesMap.put(args[0], args[2]);
                languageMap.put(args[1], args[2]);
            }
            Collections.sort(countries);
            print();
        } catch (Exception e) {
            Log.e("MANAGER", "init: ", e);
        }
    }

    private void print() {
        for (Country country : countries) {
            Log.d("MANAGER", country.toString());
        }
        Log.d("MANAGER", String.valueOf(countriesMap.size()));
        Log.d("MANAGER", String.valueOf(codesMap.size()));
        Log.d("MANAGER", String.valueOf(languageMap.size()));
        Log.d("MANAGER", String.valueOf(phoneMaskMap.size()));
    }
}

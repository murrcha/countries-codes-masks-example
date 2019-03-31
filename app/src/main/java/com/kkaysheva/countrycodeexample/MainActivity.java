package com.kkaysheva.countrycodeexample;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.kkaysheva.countrycodeexample.manager.CountryManager;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    public static final String EXTRA_COUNTRY = "country_name";

    private TextView country;
    private EditText code;
    private EditText phone;
    private CountryManager countryManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countryManager = CountryManager.getInstance(this);

        country = findViewById(R.id.select_country);

        code = findViewById(R.id.code);
        phone = findViewById(R.id.phone);

        setCountry(getCountyNameFromLocale());

        country.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListActivity.class);
            intent.putExtra(EXTRA_COUNTRY, country.getText().toString());
            startActivityForResult(intent, REQUEST_CODE);
        });

        code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0) {
                    code.setText("+");
                }
                if (s.toString().length() == 1) {
                    country.setText("Select country");
                }
                if (s.toString().length() > 1) {
                    String codeText = code.getText().toString().substring(1);
                    String countryText = countryManager.getCountryNameByCode(codeText);
                    if (TextUtils.isEmpty(countryText)) {
                        country.setText("Select country");
                        phone.setHint(null);
                    } else {
                        country.setText(countryText);
                        String hint = countryManager.getPhoneMaskByCode(codeText);
                        phone.setHint(hint != null ? hint.replace('X', '–') : "");
                    }
                }
            }
        });

        phone.addTextChangedListener(new TextWatcher() {

            private int characterAction = -1;
            private int actionPosition;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (count == 0 && after == 1) {
                    characterAction = 1;
                } else if (count == 1 && after == 0) {
                    if (s.charAt(start) == ' ' && start > 0) {
                        characterAction = 3;
                        actionPosition = start - 1;
                    } else {
                        characterAction = 2;
                    }
                } else {
                    characterAction = -1;
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            boolean ignoreOnPhoneChange = false;
            @Override
            public void afterTextChanged(Editable s) {
                if (ignoreOnPhoneChange) {
                    return;
                }
                int start = phone.getSelectionStart();
                String phoneChars = "0123456789";
                String str = phone.getText().toString();
                if (characterAction == 3) {
                    str = str.substring(0, actionPosition) + str.substring(actionPosition + 1, str.length());
                    start--;
                }
                StringBuilder builder = new StringBuilder(str.length());
                for (int a = 0; a < str.length(); a++) {
                    String ch = str.substring(a, a + 1);
                    if (phoneChars.contains(ch)) {
                        builder.append(ch);
                    }
                }
                ignoreOnPhoneChange = true;
                String hint = phone.getHint().toString();
                if (hint != null) {
                    for (int a = 0; a < builder.length(); a++) {
                        if (a < hint.length()) {
                            if (hint.charAt(a) == ' ') {
                                builder.insert(a, ' ');
                                a++;
                                if (start == a && characterAction != 2 && characterAction != 3) {
                                    start++;
                                }
                            }
                        } else {
                            builder.insert(a, ' ');
                            if (start == a + 1 && characterAction != 2 && characterAction != 3) {
                                start++;
                            }
                            break;
                        }
                    }
                }
                s.replace(0, s.length(), builder);
                if (start >= 0) {
                    phone.setSelection(start <= phone.length() ? start : phone.length());
                }
                //phone.onTextChange();
                ignoreOnPhoneChange = false;
            }
        });
    }

    @NonNull
    private String getCountyNameFromLocale() {
        return countryManager.getCountryNameByIso(Locale.getDefault().getCountry());
    }

    private void setCountry(@NonNull String countryName) {
        country.setText(countryName);
        String codeFromName = countryManager.getCodeByCountryName(countryName);
        code.setText(String.format("+%s", codeFromName));
        String hint = countryManager.getPhoneMaskByCode(codeFromName);
        phone.setHint(hint != null ? hint.replace('X', '–') : "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String country = data != null ? data.getStringExtra(EXTRA_COUNTRY) : "";
                setCountry(country);
            }
        }
    }
}

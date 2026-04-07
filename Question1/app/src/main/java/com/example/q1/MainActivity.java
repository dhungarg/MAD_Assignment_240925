package com.example.q1;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String[] CODES = {"INR", "USD", "JPY", "EUR"};
    /** How many INR = 1 unit of currency (illustrative fixed rates for coursework). */
    private static final double[] INR_PER_UNIT = {1.0, 83.0, 0.56, 90.0};

    private TextInputEditText etAmount;
    private AutoCompleteTextView actvFrom;
    private AutoCompleteTextView actvTo;
    private TextView tvResult;
    private TextView tvRateHint;

    private int indexFrom = 0;
    private int indexTo = 1;

    private final DecimalFormat fmtAmount = new DecimalFormat("#,##0.####");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etAmount = findViewById(R.id.etAmount);
        actvFrom = findViewById(R.id.actvFrom);
        actvTo = findViewById(R.id.actvTo);
        tvResult = findViewById(R.id.tvResult);
        tvRateHint = findViewById(R.id.tvRateHint);

        MaterialButton btnSettings = findViewById(R.id.btnSettings);
        MaterialButton btnSwap = findViewById(R.id.btnSwap);

        actvFrom.setAdapter(new android.widget.ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, CODES));
        actvTo.setAdapter(new android.widget.ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, CODES));

        actvFrom.setText(CODES[indexFrom], false);
        actvTo.setText(CODES[indexTo], false);

        actvFrom.setOnItemClickListener((p, v, pos, id) -> {
            indexFrom = pos;
            recalc();
        });
        actvTo.setOnItemClickListener((p, v, pos, id) -> {
            indexTo = pos;
            recalc();
        });

        etAmount.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { recalc(); }
            @Override public void afterTextChanged(Editable s) {}
        });

        btnSwap.setOnClickListener(v -> {
            int t = indexFrom;
            indexFrom = indexTo;
            indexTo = t;
            actvFrom.setText(CODES[indexFrom], false);
            actvTo.setText(CODES[indexTo], false);
            recalc();
        });

        btnSettings.setOnClickListener(v ->
                startActivity(new Intent(this, SettingsActivity.class)));

        etAmount.setText("1");
        recalc();
    }

    private void recalc() {
        String s = etAmount.getText() != null ? etAmount.getText().toString().trim() : "";
        if (s.isEmpty() || s.equals(".") || s.equals("-") || s.equals("-.")) {
            tvResult.setText("—");
            tvRateHint.setText("");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(s);
        } catch (NumberFormatException e) {
            tvResult.setText("Invalid amount");
            tvRateHint.setText("");
            return;
        }

        double inInr = amount * INR_PER_UNIT[indexFrom];
        double converted = inInr / INR_PER_UNIT[indexTo];

        String from = CODES[indexFrom];
        String to = CODES[indexTo];

        tvResult.setText(String.format(Locale.US, "%s %s",
                fmtAmount.format(converted), to));

        double oneLine = INR_PER_UNIT[indexFrom] / INR_PER_UNIT[indexTo];
        tvRateHint.setText(String.format(Locale.US,
                "1 %s = %s %s (fixed demo rates)", from, fmtAmount.format(oneLine), to));
    }
}
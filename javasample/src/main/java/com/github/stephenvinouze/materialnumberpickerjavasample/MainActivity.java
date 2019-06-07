package com.github.stephenvinouze.materialnumberpickerjavasample;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.github.stephenvinouze.materialnumberpickercore.MaterialNumberPicker;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button defaultButton;
    private Button simpleButton;
    private Button customButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        defaultButton = findViewById(R.id.default_number_picker_button);
        simpleButton = findViewById(R.id.simple_number_picker_button);
        customButton = findViewById(R.id.custom_number_picker_button);

        defaultButton.setOnClickListener(this);
        simpleButton.setOnClickListener(this);
        customButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == defaultButton) {
            NumberPicker numberPicker = new NumberPicker(this);
            numberPicker.setMinValue(1);
            numberPicker.setMaxValue(10);
            presentPickerInAlert(numberPicker, getString(R.string.alert_default_title));
        } else if (view == simpleButton) {
            MaterialNumberPicker numberPicker = new MaterialNumberPicker(this);
            presentPickerInAlert(numberPicker, getString(R.string.alert_simple_title));
        } else if (view == customButton) {
            MaterialNumberPicker numberPicker = new MaterialNumberPicker(
                    this,
                    1,
                    50,
                    10,
                    ContextCompat.getColor(this, R.color.colorAccent),
                    ContextCompat.getColor(this, R.color.colorPrimary),
                    getResources().getDimensionPixelSize(R.dimen.numberpicker_textsize),
                    Typeface.BOLD_ITALIC,
                    false,
                    false,
                    "Hand.ttf",
                    new NumberPicker.Formatter() {
                        @Override
                        public String format(int i) {
                            return "Value " + i;
                        }
                    }
            );
            presentPickerInAlert(numberPicker, getString(R.string.alert_custom_title));
        }
    }

    private void presentPickerInAlert(final NumberPicker numberPicker, String title) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setView(numberPicker)
                .setNegativeButton(getString(android.R.string.cancel), null)
                .setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, getString(R.string.picker_value, numberPicker.getValue()), Toast.LENGTH_LONG).show();
                    }
                })
                .show();
    }

}

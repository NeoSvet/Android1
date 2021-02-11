package ru.neosvet.calculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

public class SettingsActivity extends AppCompatActivity {
    private RadioButton rbSystem, rbLight, rbDark;
    private Decorer decorer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        decorer = new Decorer(this);
        decorer.setThemeFor(this);
        setContentView(R.layout.activity_settings);
        initToolbar();

        initViews();
        initDecor();
        initListener();
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void initDecor() {
        switch (decorer.getTheme()) {
            case SYSTEM:
                rbSystem.setChecked(true);
                break;
            case LIGHT:
                rbLight.setChecked(true);
                break;
            case DARK:
                rbDark.setChecked(true);
                break;
        }
    }

    private void initListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { //Android 10+
            rbSystem.setOnCheckedChangeListener((view, checked) -> {
                if (!checked) return;
                decorer.setTheme(Decorer.Themes.SYSTEM);
                reload();
            });
        }

        rbLight.setOnCheckedChangeListener((view, checked) -> {
            if (!checked) return;
            decorer.setTheme(Decorer.Themes.LIGHT);
            reload();
        });

        rbDark.setOnCheckedChangeListener((view, checked) -> {
            if (!checked) return;
            decorer.setTheme(Decorer.Themes.DARK);
            reload();
        });
    }

    private void reload() {
        startActivity(new Intent(this, MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        this.finish();
        startActivity(new Intent(this, SettingsActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
    }

    private void initViews() {
        rbSystem = findViewById(R.id.rbSystem);
        rbLight = findViewById(R.id.rbLight);
        rbDark = findViewById(R.id.rbDark);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) { //Android ...-9
            rbSystem.setVisibility(View.GONE);
        }
    }
}
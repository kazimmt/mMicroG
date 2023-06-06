package org.microg.gms.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.color.DynamicColors;
import com.google.android.gms.R;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;

    private NavController getNavController() {
        return ((NavHostFragment) Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.navhost))).getNavController();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        NearbyPreferencesIntegration.Companion.preProcessSettingsIntent(intent);

        setContentView(R.layout.settings_root_activity);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            DynamicColors.applyToActivityIfAvailable(this);
        }

        appBarConfiguration = new AppBarConfiguration.Builder(getNavController().getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, getNavController(), appBarConfiguration);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(getNavController(), appBarConfiguration) || super.onSupportNavigateUp();
    }
}

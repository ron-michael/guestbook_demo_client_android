package net.ronmichael.trial.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import net.ronmichael.trial.R;

public class SettingsActivity extends PreferenceActivity {

    public static void launch(Context context) {
        // Launch the settings listing activity
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);

    }
}

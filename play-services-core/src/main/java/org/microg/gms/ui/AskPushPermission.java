package org.microg.gms.ui;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.R;

import org.microg.gms.gcm.GcmDatabase;

public class AskPushPermission extends FragmentActivity {
    public static final String EXTRA_REQUESTED_PACKAGE = "package";
    public static final String EXTRA_RESULT_RECEIVER = "receiver";
    public static final String EXTRA_EXPLICIT = "explicit";

    private GcmDatabase database;

    private String packageName;
    private ResultReceiver resultReceiver;
    private boolean answered;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = new GcmDatabase(this);

        packageName = getIntent().getStringExtra(EXTRA_REQUESTED_PACKAGE);
        resultReceiver = getIntent().getParcelableExtra(EXTRA_RESULT_RECEIVER);
        if (packageName == null || resultReceiver == null) {
            answered = true;
            finish();
            return;
        }

        if (database.getApp(packageName) != null) {
            resultReceiver.send(Activity.RESULT_OK, Bundle.EMPTY);
            answered = true;
            finish();
            return;
        }

        View gcmView = getLayoutInflater().inflate(R.layout.ask_gcm, null);

        // Create and show the AlertDialog
        try {
            PackageManager pm = getPackageManager();
            final ApplicationInfo info = pm.getApplicationInfo(packageName, 0);
            String label = pm.getApplicationLabel(info).toString();
            String raw = getString(R.string.gcm_allow_app_popup, label);
            SpannableString s = new SpannableString(raw);
            s.setSpan(new StyleSpan(Typeface.BOLD), raw.indexOf(label), raw.indexOf(label) + label.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setCancelable(false); // Disable canceling the dialog by tapping outside or pressing the back button
            alertDialogBuilder.setView(gcmView);
            ((TextView) gcmView.findViewById(R.id.permission_message)).setText(s);
            gcmView.findViewById(R.id.permission_allow_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (answered) return;
                    database.noteAppKnown(packageName, true);
                    answered = true;
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(EXTRA_EXPLICIT, true);
                    resultReceiver.send(Activity.RESULT_OK, bundle);
                    finish();
                }
            });
            gcmView.findViewById(R.id.permission_deny_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (answered) return;
                    database.noteAppKnown(packageName, false);
                    answered = true;
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(EXTRA_EXPLICIT, true);
                    resultReceiver.send(Activity.RESULT_CANCELED, bundle);
                    finish();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } catch (PackageManager.NameNotFoundException e) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!answered) {
            resultReceiver.send(Activity.RESULT_CANCELED, Bundle.EMPTY);
        }
        database.close();
    }
}

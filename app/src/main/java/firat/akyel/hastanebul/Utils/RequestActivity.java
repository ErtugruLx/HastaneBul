package firat.akyel.hastanebul.Utils;

/**
 * Created by Mimcrea on 22.04.2017.
 */


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.text.TextUtils;
import android.util.Log;

import firat.akyel.hastanebul.R;


/**
 * Created by mimcrea-last on 22.4.2017.
 */

public class RequestActivity extends AppCompatActivity {

    private static final String TAG = "RequestActivity";
    private static final String PERMISSIONS = "permissions";
    private static final String EXPLAIN = "explain";
    private static final int REQUEST_CODE = 1000;

    private String mExplain;
    private String[] mPermissions;

    public static Intent newIntent(Context context, String[] permissions,
                                   String explain, OnRequestPermissionsCallBack callBack) {
        Intent intent = new Intent(context, RequestActivity.class);
        Bundle extras = new Bundle();
        extras.putStringArray(PERMISSIONS, permissions);
        extras.putString(EXPLAIN, explain);
        intent.putExtras(extras);

        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();

        checkPermission();
    }

    private void initData() {
        Bundle extras = getIntent().getExtras();
        mExplain = extras.getString(EXPLAIN);
        mPermissions = extras.getStringArray(PERMISSIONS);
    }

    private void checkPermission() {
        int deniedIndex = checkSelfPermissions(mPermissions);
        if (deniedIndex != -1) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, mPermissions[deniedIndex])) {
                Log.d(TAG, "denied");
                if (TextUtils.isEmpty(mExplain)) {
                    requestPermission();
                }else {
                    showExplainDialog();
                }

            } else {
                Log.d(TAG, "start request permission");
                requestPermission();
            }
        } else {
            Log.d(TAG, "Authorized");
            Intent intent = new Intent();
            intent.putExtra(Constants.GRANT, true);
            sendMessage(intent);
        }
    }

    private int checkSelfPermissions(String[] permissions) {
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                return i;
            }
        }
        return -1;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,
                mPermissions, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:

                int index = PermissionUtils.verifyPermissions(grantResults);

                Intent intent = new Intent();
                Bundle args = new Bundle();
                if (index == -1) {
                    args.putBoolean(Constants.GRANT,true);
                }else {
                    args.putString(Constants.DENIED,permissions[index]);
                }
                intent.putExtras(args);

                sendMessage(intent);
                break;
        }
    }

    private void sendMessage(Intent intent) {
        intent.setAction(getPackageName());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        finish();
    }

    private void showExplainDialog() {
        new AlertDialog.Builder(RequestActivity.this)
                .setMessage(mExplain)
                .setPositiveButton("TAMAM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestPermission();
                    }
                })

                .show()
                .setCancelable(false);
    }

}
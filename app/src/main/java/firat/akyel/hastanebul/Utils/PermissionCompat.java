package firat.akyel.hastanebul.Utils;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


/**
 * Created by mimcrea-last on 22.4.2017.
 */

public class PermissionCompat {
    private Context mContext;
    private String mExplain;
    private String[] mPermissions;
    private OnRequestPermissionsCallBack mCallBack;
    private CallBackBroadcastReceiver mCallBackBroadcastReceiver = new CallBackBroadcastReceiver();

    public void request() {

        if (mPermissions == null || mPermissions.length == 0) {
            throw new NullPointerException(mPermissions == null
                    ? "mPermissions is null"
                    : "mPermissions is empty");
        }

        Intent intent = RequestActivity.newIntent(mContext, mPermissions, mExplain, mCallBack);
        mContext.startActivity(intent);
        LocalBroadcastManager.getInstance(mContext)
                .registerReceiver(mCallBackBroadcastReceiver,
                        new IntentFilter(mContext.getPackageName()));

    }

    public static class Builder {

        public PermissionCompat mPermissionCompat = new PermissionCompat();

        public Builder(Context context) {
            mPermissionCompat.mContext = context;
        }

        public Builder addPermissionRationale(String explain) {
            mPermissionCompat.mExplain = explain;
            return this;
        }

        public Builder addPermissions(String[] permissions) {
            mPermissionCompat.mPermissions = permissions;
            return this;
        }

        public Builder addRequestPermissionsCallBack(OnRequestPermissionsCallBack callBack) {
            mPermissionCompat.mCallBack = callBack;
            return this;
        }

        public PermissionCompat build() {
            return mPermissionCompat;
        }
    }

    class CallBackBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("BroadcastReceiver", intent.getAction().toString());

            if (mCallBack == null) {
                return;
            }

            boolean result = intent.getBooleanExtra(Constants.GRANT, false);
            if (result) {
                mCallBack.onGrant();
            }else {
                String permission = intent.getStringExtra(Constants.DENIED);
                mCallBack.onDenied(permission);
            }

            LocalBroadcastManager.getInstance(mContext).unregisterReceiver(mCallBackBroadcastReceiver);
        }
    }
}
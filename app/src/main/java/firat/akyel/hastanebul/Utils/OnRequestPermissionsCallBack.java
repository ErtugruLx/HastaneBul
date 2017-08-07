package firat.akyel.hastanebul.Utils;

/**
 * Created by Mimcrea on 22.04.2017.
 */

public interface OnRequestPermissionsCallBack {
    void onGrant();

    void onDenied(String permission);
}
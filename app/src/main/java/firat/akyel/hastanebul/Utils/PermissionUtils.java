package firat.akyel.hastanebul.Utils;



import android.content.pm.PackageManager;

/**
 * Created by mimcrea-last on 22.4.2017.
 */

public class PermissionUtils {
    public static int verifyPermissions(int[] grantResults) {

        if(grantResults.length < 1){
            return -1;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int i=0;i<grantResults.length;i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                return i;
            }
        }

        return -1;
    }
}
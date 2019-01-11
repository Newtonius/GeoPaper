package germanbros.geopaper;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    boolean is_permission_granted;
    private static final String TAG = "Main Activity";
    private static final int REQUEST_CODE = 31415;
    private static final int ERROR_DIALOG_REQUEST = 90210;

    private void verifyPermissions() {
        Log.d(TAG,"Request 'Set Wallpaper and GPS' permission.");

        String[] permissions = { Manifest.permission.SET_WALLPAPER, Manifest.permission.ACCESS_FINE_LOCATION };

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(), permissions[1]) == PackageManager.PERMISSION_GRANTED) {
            is_permission_granted = true;

        } else {
            is_permission_granted = false;
            ActivityCompat.requestPermissions(MainActivity.this, permissions, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        is_permission_granted = false;
        Log.d(TAG, "It's in permission results");
        switch (requestCode) {
            case REQUEST_CODE: {
                if(grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            is_permission_granted = false;
                            return;
                        }
                    }
                    is_permission_granted = true;
                }
            }
        }
    }

    // CHECK GOOGLE PLAY SERVICES
    public boolean isServicesOK() {
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);
        boolean service_check = false;

        if (available == ConnectionResult.SUCCESS) {
            Log.d(TAG, "isServicesOK: Google Services OK");
            service_check = true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Log.d(TAG, "isServicesOK: Google Services outdated - fix available.");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
            Toast.makeText(this, "Google services outdated - fix available.", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "isServicesOK: Google Services outdated - no fix available");
            service_check = false;
            Toast.makeText(this, "Google services outdated - no fix available.", Toast.LENGTH_SHORT).show();
        }
        return service_check;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean is_services_ok = isServicesOK();

        if(is_services_ok == false) {
            Toast.makeText(this, "Google Services not working.", Toast.LENGTH_SHORT).show();
        } else {
            verifyPermissions();
            assets();
        }
    }
}

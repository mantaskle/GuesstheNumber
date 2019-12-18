package mkle.ktu.guessthenumber;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final int COARSE_LOCATION_REQUEST_CODE = 0;

    boolean canAccessLoc = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button about_button = findViewById(R.id.about_btn);
        about_button.setOnClickListener(this);

        Button settings_button = findViewById(R.id.settings_btn);
        settings_button.setOnClickListener(this);

        Button leaderboard_button = findViewById(R.id.leaderboard_btn);
        leaderboard_button.setOnClickListener(this);

        if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //if(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION))
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, COARSE_LOCATION_REQUEST_CODE);
        }
    }

    public void onButtonClicked(View v) {
        Intent intent = new Intent(this, PlayActivity.class);
        startActivity(intent);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.settings_btn) {
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        if (v.getId() == R.id.about_btn) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.leaderboard_btn) {
            Intent intent = new Intent(this, LeaderboardActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] results) {
        switch (requestCode) {
            case COARSE_LOCATION_REQUEST_CODE:
                if (results.length > 0) {
                    if (results[0] == PackageManager.PERMISSION_GRANTED) {
                        canAccessLoc = true;
                    }
                }
                break;
            default:
                break;
        }
    }

}

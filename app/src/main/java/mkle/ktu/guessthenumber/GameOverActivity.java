package mkle.ktu.guessthenumber;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GameOverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        TextView gameOverText = findViewById(R.id.game_over_text);
        TextView gameOverTextInfo = findViewById(R.id.game_over_text_info);

        if (getIntent().hasExtra("HasWon")) {
            gameOverText.setText(getResources().getString(R.string.win_game_over));
            gameOverText.setTextColor(Color.CYAN);
            gameOverTextInfo.setText("The number was: " + getIntent().getExtras().getInt("guessedNumber") +
                    ".\nThere was " + getIntent().getExtras().getInt("turnsLeft") + " turn/turns left."
            );

            LeaderboardDatabaseHandler dbHandler = new LeaderboardDatabaseHandler(this);
            dbHandler.getAllEntries();
        } else {
            gameOverText.setText(getResources().getString(R.string.lose_game_over));
            gameOverText.setTextColor(Color.RED);
            gameOverTextInfo.setText(getResources().getString(R.string.lose_content));
        }
    }
}

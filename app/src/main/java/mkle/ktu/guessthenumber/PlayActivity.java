package mkle.ktu.guessthenumber;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayActivity extends AppCompatActivity {

    private static final int MIN_RANGE = 1;
    private static final int MAX_RANGE = 100;

    private static int numberToGuess;

    private static final int MAX_TURNS = 7;

    private int turns;

    private ListView listView;

    private final static String PREFS_NAME = "PlayerPrefs";
    private final static String KEY_NAME = "name";
    private final static String DEFAULT_NAME = "Player";

    //simple adapter
    /*private List<HashMap<String,String>> simpleHistory;
    private SimpleAdapter simpleAdapter;/


    //array adapter
    /*private List<String> guessHistory;
    private ArrayAdapter<String> arrayAdapter;*/

    private List<HistoryEntry> customHistory;
    private HistoryAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        turns = 0;

        TextView guessRangeText = findViewById(R.id.guess_range_text);
        guessRangeText.setText(String.format(getResources().getString(R.string.guess_range_text), MIN_RANGE, MAX_RANGE));

        TextView turnsLeftText = findViewById(R.id.turns_left_text);
        turnsLeftText.setText(String.format(getResources().getString(R.string.turns_left_text), MAX_TURNS));

        Random random = new Random();
        numberToGuess = MIN_RANGE + random.nextInt(MAX_RANGE - MIN_RANGE);

        listView = findViewById(R.id.history_list);

        //simple adapter
        /*simpleHistory = new ArrayList<>();
        simpleAdapter = new SimpleAdapter(this,
                simpleHistory,
                R.layout.layout_simple_history_item,
                new String[]{"number", "result","turn"},
                new int[] {R.id.simple_number, R.id.simple_result, R.id.simple_turn});*/

        //array adapter
        /*guessHistory = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, guessHistory);
        listView.setAdapter(arrayAdapter);*/

        customHistory = new ArrayList<>();
        historyAdapter = new HistoryAdapter(this, customHistory);

        //listView.setAdapter(arrayAdapter);
        //listView.setAdapter(simpleAdapter);
        listView.setAdapter(historyAdapter);
    }

    public void onGuess(View v) {
        LeaderboardDatabaseHandler db = new LeaderboardDatabaseHandler(this);

        SharedPreferences sharedPrefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        EditText guessedNumberField = findViewById(R.id.number_field);

        TextView guessResult = findViewById(R.id.guess_result_text);

        if (guessedNumberField.getText().toString().trim().length() == 0) {
            guessResult.setText("Guess field is empty.");
            return;
        }

        int guessedNumber = Integer.parseInt(guessedNumberField.getText().toString());

        turns++;

        TextView turnsLeftText = findViewById(R.id.turns_left_text);
        turnsLeftText.setText(String.format(getResources().getString(R.string.turns_left_text), (MAX_TURNS - turns)));

        boolean hasWon = false;

        String guessResultString = "";
        /*HashMap<String, String> entry = new HashMap<>();
        entry.put("number", Integer.toString(guessedNumber));
        entry.put("turn", Integer.toString(turns));*/

        HistoryEntry newEntry = new HistoryEntry();

        if (guessedNumber > numberToGuess) {
            guessResultString = String.format(getResources().getString(R.string.number_was_high), guessedNumber);
            guessResult.setText(guessResultString);
            newEntry.result = 1;
            //entry.put("result", "high.");
        } else if (guessedNumber < numberToGuess) {
            guessResultString = String.format(getResources().getString(R.string.number_was_low), guessedNumber);
            guessResult.setText(guessResultString);
            //entry.put("result", "low.");
            newEntry.result = -1;
        } else {
            //Open win activity
            hasWon = true;
            db.addEntry(new LeaderboardEntry(sharedPrefs.getString(KEY_NAME, DEFAULT_NAME), 1, numberToGuess, turns));
            System.out.println("added winning entry");

            Intent intent = new Intent(this, GameOverActivity.class);
            intent.putExtra("HasWon", true);
            intent.putExtra("guessedNumber", guessedNumber);
            intent.putExtra("turnsLeft", (MAX_TURNS - turns));
            startActivity(intent);
            finish();
        }

        if (turns == MAX_TURNS && !hasWon) {
            //add game data to db
            db.addEntry(new LeaderboardEntry(sharedPrefs.getString(KEY_NAME, DEFAULT_NAME), -1, numberToGuess, turns));
            System.out.println("added losing entry");


            //Open lose activity
            Intent intent = new Intent(this, GameOverActivity.class);
            startActivity(intent);
            finish();
        }

        //clear guess field
        guessedNumberField.setText("");

        newEntry.guesssedNumber = guessedNumber;
        newEntry.turn = turns;

        customHistory.add(newEntry);
        historyAdapter.notifyDataSetChanged();

        //array adapter
        //guessHistory.add(guessResultString);
        //arrayAdapter.notifyDataSetChanged();

        // simple
        // simpleHistory.add(entry);
        //simpleAdapter.notifyDataSetChanged();
    }
}

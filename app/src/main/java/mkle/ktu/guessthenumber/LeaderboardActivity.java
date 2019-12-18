package mkle.ktu.guessthenumber;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity implements WebTask.WebTaskCompleteListener {

    ListView listView;
    ListView onlineListView;
    private List<LeaderboardEntry> customLeaderboard;
    private List<LeaderboardEntry> customOnlineLeaderboard;

    TextView leaderboardOnlineEmpty;

    LeaderboardDatabaseHandler db = new LeaderboardDatabaseHandler(this);

    LeaderboardAdapter leaderboardAdapter;
    LeaderboardAdapter leaderboardOnlineAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        customLeaderboard = new ArrayList<>();
        customOnlineLeaderboard = new ArrayList<>();

        TextView leaderboardEmpty = findViewById(R.id.leaderboard_empty);

        listView = (ListView) findViewById(R.id.leaderboard_list);
        onlineListView = (ListView) findViewById(R.id.leaderboard_online_list);

        leaderboardAdapter = new LeaderboardAdapter(this, customLeaderboard);
        leaderboardOnlineAdapter = new LeaderboardAdapter(this, customOnlineLeaderboard);
        listView.setAdapter(leaderboardAdapter);
        onlineListView.setAdapter(leaderboardOnlineAdapter);

        customLeaderboard.addAll(db.getAllEntries());

        Collections.sort(customLeaderboard, new Comparator<LeaderboardEntry>() {
            @Override
            public int compare(LeaderboardEntry left, LeaderboardEntry right) {
                return Double.compare(left.getTotalTurns(), right.getTotalTurns());
            }
        });

        WebTask webTask = new WebTask();
        webTask.setCompleteListener(this);
        webTask.execute("http://gamegarden.lt/api");

        leaderboardAdapter.notifyDataSetChanged();
        leaderboardOnlineAdapter.notifyDataSetChanged();

        if (customLeaderboard.size() > 0)
        {
            leaderboardEmpty.setHeight(0);
        }

    }

    public void onWebTaskComplete (String inString)
    {
        try {
            JSONArray jsonArray = new JSONArray(inString);
            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if(jsonObject != null)
                {
                    String playerName = jsonObject.getString("name");
                    int result = jsonObject.getInt("result");
                    int number = jsonObject.getInt("number");
                    customOnlineLeaderboard.add(new LeaderboardEntry(playerName, result, number, -1));
                    Log.d("TAG", String.format("Name: %s --- Result: %d --- Number: %d", playerName, result, number));

                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        TextView leaderboardOnlineEmpty = findViewById(R.id.leaderboard_online_empty);

        if (customOnlineLeaderboard.size() > 0)
        {
            leaderboardOnlineEmpty.setHeight(0);
        }
        leaderboardOnlineAdapter.notifyDataSetChanged();
    }
}

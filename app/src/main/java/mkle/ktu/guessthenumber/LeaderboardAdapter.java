package mkle.ktu.guessthenumber;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class LeaderboardAdapter extends BaseAdapter {

    Context mContext;
    List<LeaderboardEntry> entries;

    public LeaderboardAdapter(Context inContext, List<LeaderboardEntry> inData) {
        mContext = inContext;
        entries = inData;
    }

    public int getCount() {
        if (entries == null) {
            return 0;
        }
        return entries.size();
    }

    public Object getItem(int position) {
        if (position < 0 && position > entries.size()) {
            return -1;
        }
        return entries.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.layout_leaderboard_item, null);
        }

        TextView placeView = view.findViewById(R.id.leaderboard_place);
        TextView nameView = view.findViewById(R.id.leaderboard_name);
        TextView resultView = view.findViewById(R.id.leaderboard_result);
        TextView numberView = view.findViewById(R.id.leaderboard_number);
        TextView turnView = view.findViewById(R.id.leaderboard_turns);

        placeView.setText(Integer.toString(position + 1));

        String name = entries.get(position).getName();
        nameView.setText(name);

        int number = entries.get(position).getNumber();
        numberView.setText(Integer.toString(number));

        int turns = entries.get(position).getTotalTurns();
        if (turns < 0)
            turnView.setText("N/A");
        else
            turnView.setText(Integer.toString(turns));

        if (entries.get(position).getResult() > 0) {
            resultView.setText("won");
        } else {
            resultView.setText("lost");
        }

        return view;
    }
}

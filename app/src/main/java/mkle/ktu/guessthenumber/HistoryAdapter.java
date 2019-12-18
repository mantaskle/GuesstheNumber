package mkle.ktu.guessthenumber;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class HistoryAdapter extends BaseAdapter {

    Context mContext;
    List<HistoryEntry> mData;

    public HistoryAdapter(Context inContext, List<HistoryEntry> inData) {
        mContext = inContext;
        mData = inData;
    }

    public int getCount() {
        return mData.size();
    }

    public Object getItem(int position) {
        // add range checks +
        if (position < 0 && position > mData.size()) {
            return -1;
        }
        return mData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.layout_simple_history_item, null);
        }

        TextView numberView = view.findViewById(R.id.simple_number);
        TextView resultView = view.findViewById(R.id.simple_result);
        TextView turnView = view.findViewById(R.id.simple_turn);

        int number = mData.get(position).guesssedNumber;
        numberView.setText(Integer.toString(number));

        if (mData.get(position).result > 0) {
            resultView.setText("high.");
        } else {
            resultView.setText("low.");
        }

        int turn = mData.get(position).turn;
        turnView.setText(Integer.toString(turn));

        return view;
    }
}

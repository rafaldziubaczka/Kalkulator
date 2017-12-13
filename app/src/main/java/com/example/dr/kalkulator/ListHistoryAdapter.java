package com.example.dr.kalkulator;

import android.content.ClipData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by DR on 12.12.2017.
 */

public class ListHistoryAdapter extends ArrayAdapter<History> {

    public ListHistoryAdapter(Context context, int resource, List<History> items) {
        super(context, resource, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.list_history_item, null);
        }

        History history = getItem(position);

        if (history != null) {
//            TextView tt1 = (TextView) v.findViewById(R.id.id);
            TextView exp = (TextView) v.findViewById(R.id.ItemHistoryExp);
            TextView result = (TextView) v.findViewById(R.id.ItemHistoryResult);

//            if (tt1 != null) {
//                tt1.setText(p.getId());
//            }

            if (exp != null) {
                exp.setText(history.getExpression());
            }

            if (result != null) {
                result.setText(history.getResult().toString());
            }
        }

        return v;
    }
}

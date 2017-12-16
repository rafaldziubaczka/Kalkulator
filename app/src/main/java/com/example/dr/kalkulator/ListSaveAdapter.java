package com.example.dr.kalkulator;

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

public class ListSaveAdapter extends ArrayAdapter<Result> {

    public ListSaveAdapter(Context context, int resource, List<Result> items) {
        super(context, resource, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.list_save_item, null);
        }

        Result result = getItem(position);

        if (result != null) {
            TextView name = (TextView) v.findViewById(R.id.ItemSaveName);
            TextView value = (TextView) v.findViewById(R.id.ItemSaveValue);

            if (name != null) {
                name.setText(result.getName()+":");
            }

            if (value != null) {
                value.setText(result.getValue());
            }
        }

        return v;
    }
}

package com.anhnt.kovidict.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.anhnt.kovidict.myapplication.R;
import com.anhnt.kovidict.myapplication.entities.Origin;
import java.util.ArrayList;
import java.util.List;

public class OriginAdapter extends ArrayAdapter<Origin> {
    private List<Origin> vocabs;

    private static class ViewHolder {
        TextView origin;

        private ViewHolder() {
        }
    }

    public OriginAdapter(Context context, List<Origin> vocabs) {
        super(context, R.layout.dict_item, vocabs);
        this.vocabs = vocabs;
    }

    public void addItems(List<Origin> newItems) {
        if (newItems != null && newItems.size() > 0) {
            if (this.vocabs == null) {
                this.vocabs = new ArrayList<>();
            }
            this.vocabs.addAll(newItems);
            notifyDataSetChanged();
        }
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        Origin vocab = getItem(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.dict_item, parent, false);
            viewHolder.origin = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.origin.setText(vocab.origin);
        if (position % 2 == 0) {
            convertView.setBackgroundResource(R.drawable.item_selector);
        } else {
            convertView.setBackgroundResource(R.drawable.item_selector_alternate);
        }
        return convertView;
    }
}
package com.anhnt.kovidict.myapplication.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.anhnt.kovidict.myapplication.ItemClickPressed;
import com.anhnt.kovidict.myapplication.R;
import com.anhnt.kovidict.myapplication.entities.Meaning;
import com.anhnt.kovidict.myapplication.entities.Origin;

public class MeaningFragment extends Fragment {
    private ImageView favourite;
    private ItemClickPressed itemClickListener;
    private Origin org;
    private TextView tvMeaning;
    private TextView tvOrigin;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.itemClickListener = (ItemClickPressed) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFileSelectedListener");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_meaning, container, false);
        this.tvOrigin = (TextView) rootView.findViewById(R.id.title_name);
        this.tvMeaning = (TextView) rootView.findViewById(R.id.meaning);
        this.favourite = (ImageView) rootView.findViewById(R.id.favourite);
        this.favourite.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                try {
                    if (MeaningFragment.this.org.favourite == 1) {
                        MeaningFragment.this.org.favourite = 0;
                        MeaningFragment.this.favourite.setBackgroundResource(R.drawable.favourite);
                        Toast.makeText(MeaningFragment.this.getActivity().getApplicationContext(), "Đã xóa khỏi mục Yêu thích", Toast.LENGTH_LONG).show();
                    } else {
                        MeaningFragment.this.org.favourite = 1;
                        MeaningFragment.this.favourite.setBackgroundResource(R.drawable.favourite_pressed);
                        Toast.makeText(MeaningFragment.this.getActivity().getApplicationContext(), "Đã thêm vào mục Yêu thích.", Toast.LENGTH_LONG).show();
                    }
                    MeaningFragment.this.org.save();
                } catch (Exception e) {
                }
                MeaningFragment.this.itemClickListener.updateFavourite();
            }
        });
        return rootView;
    }

    public void setViewContent(Origin origin, Meaning meaning) {
        this.org = origin;
        this.tvOrigin.setText(origin.origin);
        this.tvMeaning.setText(Html.fromHtml(getHtml(meaning.meaning)));
        if (origin.favourite == 1) {
            this.favourite.setBackgroundResource(R.drawable.favourite_pressed);
        } else {
            this.favourite.setBackgroundResource(R.drawable.favourite);
        }
    }

    private String getHtml(String s) {
        String[] buffer = s.split("\n");
        StringBuffer result = new StringBuffer("");
        for (String string : buffer) {
            // TODO: fixed here
            String string2 = string;
            if (string2.length() > 1) {
                String[] a;
                if (string2.charAt(0) == '@') {
                    string2 = "<font color=\"red\">" + string2 + "</font>";
                }
                if (string2.charAt(0) == '=') {
                    string2 = string2.replace("=", "");
                    a = string2.split("-");
                    if (a.length > 1) {
                        string2 = "<font color=\"blue\">" + a[0] + "</font> " + a[1];
                    }
                }
                if (string2.charAt(0) == '-' && string2.charAt(1) == '\u2020') {
                    string2 = string2.replace("\u2020", "");
                    a = string2.split("\u2021");
                    if (a.length > 1) {
                        string2 = "<font color=\"blue\">" + a[0] + "</font> " + a[1];
                    }
                }
                if (string2.charAt(0) == '\u25e6' && string2.charAt(1) == '\u2020') {
                    string2 = string2.replace("\u2020", "");
                    a = string2.split("\u2021");
                    if (a.length > 1) {
                        string2 = "<font color=\"blue\">" + a[0] + "</font> " + a[1];
                    }
                }
                if (string2.charAt(0) == '*') {
                    string2 = "<font color=\"green\">" + string2 + "</font>";
                }
            }
            result.append(string2.replace("\u2021", ""));
            result.append("<br/>");
        }
        return result.toString();
    }

}
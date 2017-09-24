package com.anhnt.kovidict.myapplication.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.anhnt.kovidict.myapplication.ItemClickPressed;
import com.anhnt.kovidict.myapplication.R;
import com.anhnt.kovidict.myapplication.adapter.OriginAdapter;
import com.anhnt.kovidict.myapplication.entities.Controller;
import com.anhnt.kovidict.myapplication.entities.Origin;
import com.anhnt.kovidict.myapplication.util.IndexableListView;
import java.util.List;

public class FavouriteFragment extends Fragment {
    private OriginAdapter adapter;
    private Controller c;
    private ItemClickPressed itemClickListener;
    private List<Origin> listOrigin;
    private IndexableListView listView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.c = new Controller(getArguments().getInt("type_dict"));
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.itemClickListener = (ItemClickPressed) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFileSelectedListener");
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favourite, container, false);
        this.listView = (IndexableListView) rootView.findViewById(R.id.listview_favourite);
        this.listView.setFastScrollEnabled(true);
        this.listOrigin = this.c.getFavourites();
        this.adapter = new OriginAdapter(getActivity(), this.listOrigin);
        this.listView.setAdapter(this.adapter);
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                FavouriteFragment.this.itemClickListener.onItemPressed(FavouriteFragment.this.listOrigin.get(position));
            }
        });
        return rootView;
    }

    public void updateListView() {
        try {
            this.listOrigin = this.c.getFavourites();
            this.adapter = new OriginAdapter(getActivity(), this.listOrigin);
            this.listView.setAdapter(this.adapter);
            this.adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
package com.anhnt.kovidict.myapplication.fragment;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anhnt.kovidict.myapplication.ItemClickPressed;
import com.anhnt.kovidict.myapplication.R;
import com.anhnt.kovidict.myapplication.adapter.OriginAdapter;
import com.anhnt.kovidict.myapplication.entities.Controller;
import com.anhnt.kovidict.myapplication.entities.Origin;
import com.anhnt.kovidict.myapplication.util.EndlessListView;
import com.anhnt.kovidict.myapplication.util.EndlessListView.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

public class VocabolaryFragment extends Fragment {

    private final int REQ_CODE_SPEECH_INPUT = 100;
    private final int ITEMS_SHOW;
    private OriginAdapter adapter;
    private Controller controller;
    private EndlessListView endlessListView;
    private ItemClickPressed itemClickListener;
    private List<Origin> listOrigin;
    private OnLoadMoreListener loadMoreListener;
    private int mCount;
    private boolean mHaveMoreDataToLoad;
    private TextView textSearch;
    private ImageView imageVoice;

    private class EditextEventHandler implements TextWatcher {
        final long DELAY;

        private EditextEventHandler() {
            this.DELAY = 50;
        }

        public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
            VocabolaryFragment.this.getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    VocabolaryFragment.this.mCount = 0;
                    VocabolaryFragment.this.updateListView();
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
        }

        public void afterTextChanged(Editable arg0) {
        }
    }

    private class LoadMore extends AsyncTask<Void, Void, List<Origin>> {
        private LoadMore() {
        }

        protected void onPreExecute() {
            super.onPreExecute();
            VocabolaryFragment vocabolaryFragment = VocabolaryFragment.this;
            vocabolaryFragment.mCount = vocabolaryFragment.mCount + 1;
        }

        protected List<Origin> doInBackground(Void... params) {
            List<Origin> list = VocabolaryFragment.this.controller.search(VocabolaryFragment.this.textSearch.getText().toString(), VocabolaryFragment.this.mCount * 30, 30);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return list;
        }

        protected void onPostExecute(List<Origin> result) {
            super.onPostExecute(result);
            VocabolaryFragment.this.adapter.addItems(result);
            VocabolaryFragment.this.endlessListView.loadMoreCompleat();
            VocabolaryFragment.this.mHaveMoreDataToLoad = VocabolaryFragment.this.mCount < 10;
        }
    }

    public VocabolaryFragment() {
        this.ITEMS_SHOW = 30;
        this.loadMoreListener = new OnLoadMoreListener() {
            public boolean onLoadMore() {
                if (VocabolaryFragment.this.mHaveMoreDataToLoad) {
                    VocabolaryFragment.this.loadMoreData();
                } else {
                    // do nothing
                }
                return VocabolaryFragment.this.mHaveMoreDataToLoad;
            }
        };
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.controller = new Controller(getArguments().getInt("type_dict"));
        this.mCount = 0;
        this.mHaveMoreDataToLoad = true;
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
        View rootView = inflater.inflate(R.layout.fragment_vocabolary, container, false);
        this.endlessListView = (EndlessListView) rootView.findViewById(R.id.listview_origin);
        this.endlessListView.setFastScrollEnabled(true);
        this.listOrigin = this.controller.getOrigins();
        this.adapter = new OriginAdapter(getActivity(), this.listOrigin);
        this.endlessListView.setAdapter(this.adapter);
        this.endlessListView.setOnLoadMoreListener(this.loadMoreListener);
        this.endlessListView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                VocabolaryFragment.this.hideKeyboard();
                VocabolaryFragment.this.itemClickListener.onItemPressed((Origin) VocabolaryFragment.this.adapter.getItem(position));
            }
        });
        this.textSearch = (TextView) rootView.findViewById(R.id.textSearch);
        this.textSearch.addTextChangedListener(new EditextEventHandler());
        this.imageVoice = (ImageView) rootView.findViewById(R.id.imageVoice);
        this.imageVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
        return rootView;
    }

    private void updateListView() {
        this.listOrigin = this.controller.search(this.textSearch.getText().toString());
        this.adapter.clear();
        this.adapter.addAll(this.listOrigin);
        this.adapter.notifyDataSetChanged();
    }

    private void loadMoreData() {
        new LoadMore().execute(new Void[]{null});
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 2);
        }
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getActivity(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == getActivity().RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    textSearch.setText(result.get(0));
                }
                break;
            }

        }
    }
}
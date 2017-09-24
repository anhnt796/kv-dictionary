package com.anhnt.kovidict.myapplication;

import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import com.anhnt.kovidict.myapplication.dialog.InfoDialog;
import com.anhnt.kovidict.myapplication.entities.Controller;
import com.anhnt.kovidict.myapplication.entities.Meaning;
import com.anhnt.kovidict.myapplication.entities.Origin;
import com.anhnt.kovidict.myapplication.fragment.FavouriteFragment;
import com.anhnt.kovidict.myapplication.fragment.MeaningFragment;
import com.anhnt.kovidict.myapplication.adapter.TabsPagerAdapter;

public class MainActivity extends FragmentActivity implements TabListener, ItemClickPressed {
    private static final int TAB_START = 0;
    private static final int TAB_VOCABOLARY = 1;
    private static final int TAB_MEANING = 2;
    private static final int TAB_FAVOURITE = 3;
    private TabsPagerAdapter mAdapter;
    private int type_dict;
    private ViewPager viewPager;
    private PagerTabStrip tabStrip;
    private Dialog dialog;

    public MainActivity() {
        this.type_dict = 0;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.type_dict = getIntent().getExtras().getInt("type_dict");
        this.viewPager = (ViewPager) findViewById(R.id.pager);
        this.mAdapter = new TabsPagerAdapter(getSupportFragmentManager(), this.type_dict);
        this.viewPager.setAdapter(this.mAdapter);
        this.viewPager.setCurrentItem(TAB_START);
        this.tabStrip = (PagerTabStrip) findViewById(R.id.tab_strip);
        ((ViewPager.LayoutParams) this.tabStrip.getLayoutParams()).isDecor = true;
        this.tabStrip.setTextColor(getResources().getColor(R.color.white));
    }


    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }

    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        this.viewPager.setCurrentItem(tab.getPosition());
    }

    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }

    public void onItemPressed(Origin origin) {
        Meaning meaning = new Controller(this.type_dict).getMeaning(origin.getId().longValue());
        this.viewPager.setCurrentItem(TAB_MEANING);
        ((MeaningFragment) this.mAdapter.getItem(TAB_MEANING)).setViewContent(origin, meaning);
    }

    public void updateFavourite() {
        ((FavouriteFragment) this.mAdapter.getItem(TAB_FAVOURITE)).updateListView();
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    private String getTitleBar() {
        return "Han Viet Dictionary";
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getResources().getString(R.string.exit_dialog_title));
                builder.setMessage(getResources().getString(R.string.exit_dialog_message));
                builder.setPositiveButton(getResources().getString(R.string.exit_dialog_btn_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.exit_dialog_btn_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                break;
            case R.id.about_us:
                if (this.dialog == null || !this.dialog.isShowing()) {
                    this.dialog = new InfoDialog(this);
                    this.dialog.show();
                }

        }
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getResources().getString(R.string.exit_dialog_title));
                builder.setMessage(getResources().getString(R.string.exit_dialog_message));
                builder.setPositiveButton(getResources().getString(R.string.exit_dialog_btn_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.exit_dialog_btn_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;

        }
        return false;
    }
}
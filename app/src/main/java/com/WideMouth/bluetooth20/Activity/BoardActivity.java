package com.WideMouth.bluetooth20.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.WideMouth.bluetooth20.R;
import com.WideMouth.bluetooth20.Util.WMUtil;
import com.WideMouth.bluetooth20.WMView.WMCommonListItemView;
import com.WideMouth.bluetooth20.WMView.WMGroupListView;
import com.WideMouth.bluetooth20.fragment.AboutFragment;
import com.WideMouth.bluetooth20.fragment.BoardFragment;
import com.WideMouth.bluetooth20.fragment.DetailsFragment;
import com.WideMouth.bluetooth20.fragment.ProjectFragment;
import com.WideMouth.bluetooth20.fragment.SettingsFragment;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BoardActivity extends AppCompatActivity {

    Fragment[] fragments = {new SettingsFragment(),new DetailsFragment(),new AboutFragment()};

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WMUtil.setSystemBarStyle(BoardActivity.this, getWindow(), true,
                false, true, true, true);
        setContentView(R.layout.activity_board);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getIntent().getStringExtra(BoardFragment.TITTLE));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.board_layout, fragments[getIntent().getIntExtra(BoardFragment.FRAGMENTINDEX, 0)]);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

}

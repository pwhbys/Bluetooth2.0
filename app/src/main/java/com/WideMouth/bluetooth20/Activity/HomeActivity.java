package com.WideMouth.bluetooth20.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.Toast;

import com.WideMouth.bluetooth20.R;
import com.WideMouth.bluetooth20.Util.SoundPoolUtil;
import com.WideMouth.bluetooth20.Util.WMUtil;
import com.WideMouth.bluetooth20.WMView.WMNavigationBar.Anim;
import com.WideMouth.bluetooth20.WMView.WMNavigationBar.WMNavigationBar;
import com.WideMouth.bluetooth20.fragment.BoardFragment;
import com.WideMouth.bluetooth20.fragment.ProjectFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {


    public static WMNavigationBar navigationBar;

    private ProjectFragment projectFragment = new ProjectFragment();

    private BoardFragment boardFragment = new BoardFragment();

    private String[] tabText = {"主页", "看板"};
    //未选中icon
    private int[] normalIcon = {R.drawable.icon_tab_home, R.drawable.icon_tab_board};
    //选中时icon
    private int[] selectIcon = {R.drawable.icon_tab_home_selected, R.drawable.icon_tab_board_selected};
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SoundPoolUtil.initialize(this);
        WMUtil.setSystemBarStyle(HomeActivity.this, getWindow(), true,
                false, true, true, true);
        setContentView(R.layout.activity_hom);
//        toolbar = findViewById(R.id.toolbar);
//
//        toolbar.setTitle(tabText[0]);
//        setSupportActionBar(toolbar);
//        ActionBar actionBar = getSupportActionBar();

        navigationBar = findViewById(R.id.navigationBar);
        fragments.add(projectFragment);
        fragments.add(boardFragment);

        navigationBar.titleItems(tabText)
                .tabTextSize(0)
                .normalIconItems(normalIcon)
                .selectIconItems(selectIcon)
                .iconSize(26)
                .addIconSize(30)
                .lineHeight(0)
                .hasPadding(false)
                .navigationHeight(50)
                .fragmentList(fragments)
                .fragmentManager(getSupportFragmentManager())
                .addLayoutRule(WMNavigationBar.RULE_BOTTOM)
                .addLayoutBottom(80)
                .canScroll(true)
                .mode(WMNavigationBar.MODE_NORMAL)
                .anim(Anim.ZoomIn)
                .build();
    }

    private long exitTime = System.currentTimeMillis()-2000;
    @Override
    public void onBackPressed() {
        // TODO 自动生成的方法存根
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }

}

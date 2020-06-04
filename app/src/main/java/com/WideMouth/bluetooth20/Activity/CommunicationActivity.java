package com.WideMouth.bluetooth20.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.WideMouth.bluetooth20.R;
import com.WideMouth.bluetooth20.Util.WMUtil;
import com.WideMouth.bluetooth20.fragment.BoardFragment;
import com.WideMouth.bluetooth20.fragment.ChatFragment;
import com.WideMouth.bluetooth20.fragment.ControlFragment;
import com.WideMouth.bluetooth20.fragment.ProjectFragment;
import com.WideMouth.bluetooth20.fragment.ConnectFragment;
import com.WideMouth.bluetooth20.WMView.WMNavigationBar.Anim;
import com.WideMouth.bluetooth20.WMView.WMNavigationBar.WMNavigationBar;
import com.google.android.material.snackbar.Snackbar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.util.ArrayList;
import java.util.List;

public class CommunicationActivity extends AppCompatActivity {

    private static final String TAG = "CommunicationActivity";

    public static String projectName = "";
    public static int projectId = -1;


    public static LocalBroadcastManager localBroadcastManager;


    Toolbar toolbar;

    MenuItem addItem;

    public static WMNavigationBar navigationBar;

    //    private ProjectFragment homeFragment = new ProjectFragment();
    private ConnectFragment connectFragment = new ConnectFragment();
    private ChatFragment chatFragment = new ChatFragment();
    private ControlFragment controlFragment = new ControlFragment();

    private String[] tabText = {"搜索面板", "聊天窗口", "控制面板"};
    //未选中icon
    private int[] normalIcon = {R.drawable.icon_tab_scan, R.drawable.icon_tab_chat, R.drawable.icon_tab_control};
    //选中时icon
    private int[] selectIcon = {R.drawable.icon_tab_scan_selected, R.drawable.icon_tab_chat_selected, R.drawable.icon_tab_control_selected};
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        projectName = intent.getStringExtra(ProjectFragment.projectName);
        projectId = intent.getIntExtra(ProjectFragment.projectId, -1);
        WMUtil.setSystemBarStyle(CommunicationActivity.this, getWindow(), true,
                false, true, true, true);
        setContentView(R.layout.activity_communication);

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle(projectName);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.icon_direction_down);
        }

        navigationBar = findViewById(R.id.navigationBar);
        fragments.add(connectFragment);
        fragments.add(chatFragment);
        fragments.add(controlFragment);


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
                .addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
//                        toolbar.setTitle(projectName + "\u2022" + tabText[position]);
                        if (position == 1) {
                            navigationBar.showInputView();
                        } else {
                            navigationBar.hideInputView();
                            InputMethodManager manager = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
                            if (manager != null)
                                manager.hideSoftInputFromWindow(navigationBar.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                        }
                        if (position == 2) {
                            addItem.setVisible(true);
                        } else {
                            addItem.setVisible(false);
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                })
                .fragmentManager(getSupportFragmentManager())
                .addLayoutRule(WMNavigationBar.RULE_BOTTOM)

                .addLayoutBottom(80)
                .canScroll(true)
                .mode(WMNavigationBar.MODE_NORMAL)
                .anim(Anim.ZoomIn)
                .build();


//        QMUISlider qmuiSlider;
//        navigationBar.setAddViewLayout(addView);
    }


    public static void goConnectFragment() {
        Snackbar.make(navigationBar, "未连接设备", Snackbar.LENGTH_LONG)
                .setAction("连接", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        navigationBar.selectTab(0);
                    }
                })
                .show();
    }

    public void goBackHomeActivity() {
        if (!WMUtil.settings.isNotice()) {
            finish();
            return;
        }
        new QMUIDialog.MessageDialogBuilder(this)
                .setTitle("提示")
                .setMessage("是否返回主界面！")
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(0, "返回", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                        finish();
                    }
                })
                .create(R.style.Dialog).show();
    }

//    public void notifyDataSetChanged() {
//        connectFragment.notifyDataSetChanged();
//        chatFragment.notifyDataSetChanged();
//        controlFragment.notifyDataSetChanged();
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        addItem = menu.findItem(R.id.addMenuItem);
        addItem.setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.settings:
                Intent intent = new Intent(this, BoardActivity.class)
                        .putExtra(BoardFragment.TITTLE, "设置")
                        .putExtra(BoardFragment.FRAGMENTINDEX, 0);
                startActivity(intent);
                break;
            case R.id.addMenuItem:
                switch (navigationBar.getSelectedTabPosition()) {
                    case 2:
                        controlFragment.addAction();
                        break;
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private long exitTime = System.currentTimeMillis() - 2000;

    @Override
    public void onBackPressed() {
        // TODO 自动生成的方法存根
        if (System.currentTimeMillis() - exitTime > 2000) {
            Toast.makeText(this, "再按一次返回主界面", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

package com.WideMouth.bluetooth20.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuPopupHelper;
import androidx.fragment.app.Fragment;

import com.WideMouth.bluetooth20.R;
import com.WideMouth.bluetooth20.Util.WMUtil;
import com.WideMouth.bluetooth20.WMView.WMCommonListItemView;
import com.WideMouth.bluetooth20.WMView.WMGroupListView;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;

import java.lang.reflect.Field;


public class SettingsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final WMGroupListView groupListView = new WMGroupListView(getContext());

        WMCommonListItemView itemSound = groupListView.createItemView("消息通知音效");
        itemSound.setAccessoryType(WMCommonListItemView.ACCESSORY_TYPE_SWITCH);
        itemSound.getSwitch().setChecked(WMUtil.settings.isSound());
        itemSound.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                WMUtil.settings.setSound(isChecked);
            }
        });

        WMCommonListItemView itemMessage = groupListView.createItemView("Toast显示收发消息");
        itemMessage.setAccessoryType(WMCommonListItemView.ACCESSORY_TYPE_SWITCH);
        itemMessage.getSwitch().setChecked(WMUtil.settings.isMessage());
        itemMessage.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                WMUtil.settings.setMessage(isChecked);
            }
        });

        WMCommonListItemView itemBroad = groupListView.createItemView("开启系统广播通知");
        itemBroad.setDetailText("Toast显示广播过滤器的action值");
        itemBroad.setOrientation(WMCommonListItemView.VERTICAL);
        itemBroad.setAccessoryType(WMCommonListItemView.ACCESSORY_TYPE_SWITCH);
        itemBroad.getSwitch().setChecked(WMUtil.settings.isBroad());
        itemBroad.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                WMUtil.settings.setBroad(isChecked);
            }
        });

//        WMCommonListItemView itemNotice = groupListView.createItemView("退出通信界面时弹出退出提示");
//        itemNotice.setAccessoryType(WMCommonListItemView.ACCESSORY_TYPE_SWITCH);
//        itemNotice.getSwitch().setChecked(WMUtil.settings.isNotice());
//        itemNotice.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                WMUtil.settings.setNotice(isChecked);
//            }
//        });

//        WMCommonListItemView itemSave = groupListView.createItemView("保存聊天记录");
//        itemSave.setAccessoryType(WMCommonListItemView.ACCESSORY_TYPE_SWITCH);
//        itemSave.setOrientation(WMCommonListItemView.VERTICAL);
//        itemSave.setDetailText("消息量：最后100条");
//        itemSave.getSwitch().setChecked(WMUtil.settings.isSave());
//        itemSave.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                WMUtil.settings.setSave(isChecked);
//            }
//        });

        WMCommonListItemView itemCreateProject = groupListView.createItemView("无项目时自动创建默认项目");
        itemCreateProject.setDetailText("项目默认名:" + ProjectFragment.defaultProject);
        itemCreateProject.setOrientation(WMCommonListItemView.VERTICAL);
        itemCreateProject.setAccessoryType(WMCommonListItemView.ACCESSORY_TYPE_SWITCH);
        itemCreateProject.getSwitch().setChecked(WMUtil.settings.isCreateProject());
        itemCreateProject.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                WMUtil.settings.setCreateProject(isChecked);
            }
        });

        WMCommonListItemView itemConnectDevice = groupListView.createItemView("打开项目时开启蓝牙搜索");
        itemConnectDevice.setAccessoryType(WMCommonListItemView.ACCESSORY_TYPE_SWITCH);
        itemConnectDevice.getSwitch().setChecked(WMUtil.settings.isScan());
        itemConnectDevice.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                WMUtil.settings.setScan(isChecked);
            }
        });

        WMCommonListItemView itemOrder = groupListView.createItemView("控制指令加入聊天消息队列");
        itemOrder.setAccessoryType(WMCommonListItemView.ACCESSORY_TYPE_SWITCH);
        itemOrder.getSwitch().setChecked(WMUtil.settings.isOrder());
        itemOrder.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                WMUtil.settings.setOrder(isChecked);
            }
        });

        WMCommonListItemView itemCreatePanel = groupListView.createItemView("项目无控制面板时自动创建");
        itemCreatePanel.setAccessoryType(WMCommonListItemView.ACCESSORY_TYPE_SWITCH);
        itemCreatePanel.getSwitch().setChecked(WMUtil.settings.isCreatePanel());
        itemCreatePanel.getSwitch().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                WMUtil.settings.setCreatePanel(isChecked);
            }
        });

        final WMCommonListItemView itemPeriod = groupListView.createItemView("消息接收整合时间（毫秒）");
        itemPeriod.setDetailText(String.valueOf(WMUtil.settings.getPeriod()));
        itemPeriod.setAccessoryType(WMCommonListItemView.ACCESSORY_TYPE_NONE);

        final WMCommonListItemView itemBackground = groupListView.createItemView("项目界面背景图");
        itemBackground.setAccessoryType(WMCommonListItemView.ACCESSORY_TYPE_CUSTOM);
        final ImageView imageView = new ImageView(getContext());
        int size = WMUtil.dip2px(getContext(), 50);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(size, size);
        imageView.setImageResource(WMUtil.settings.getImageRes());
        itemBackground.addAccessoryCustomView(imageView, layoutParams);


        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof WMCommonListItemView) {
                    CharSequence text = ((WMCommonListItemView) v).getText();
                    switch (text.toString()) {
                        case "消息接收整合时间（毫秒）":
                            final String[] items = {"0ms", "10ms", "30ms", "50ms", "80ms", "100ms", "120ms", "180ms", "240ms", "320ms", "480ms"};
                            int checkedIndex = 2;
                            for (int i = 0; i < items.length; i++) {
                                if (items[i].substring(0, items[i].length() - 2).equals(String.valueOf(WMUtil.settings.getPeriod()))) {
                                    checkedIndex = i;
                                    break;
                                }
                            }
                            new QMUIDialog.CheckableDialogBuilder(getActivity())
                                    .setCheckedIndex(checkedIndex)
                                    .addItems(items, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            try {
                                                int period = Integer.parseInt(items[which].substring(0, items[which].length() - 2));
                                                WMUtil.settings.setPeriod(period);
                                                WMUtil.settings.save();
                                                itemPeriod.setDetailText(String.valueOf(period));
                                            } catch (Exception e) {
                                                Toast.makeText(getContext(), "未知错误！", Toast.LENGTH_SHORT).show();
                                            }
                                            dialog.dismiss();
                                        }
                                    })
                                    .create(R.style.Dialog).show();
                            break;

                        case "项目界面背景图":
                            int index = 0;
                            String[] names = {"办公", "写代码", "阳光沙滩"};
                            final int[] imaRes = {R.drawable.icon_svg_office_work, R.drawable.icon_svg_coding, R.drawable.icon_svg_sun};
                            for (int i = 0; i < imaRes.length; i++) {
                                if (imaRes[i] == WMUtil.settings.getImageRes()) {
                                    index = i;
                                    break;
                                }
                            }
                            new QMUIDialog.CheckableDialogBuilder(getActivity())
                                    .setCheckedIndex(index)
                                    .addItems(names, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            WMUtil.settings.setImageRes(imaRes[which]);
                                            WMUtil.settings.save();
                                            imageView.setImageResource(WMUtil.settings.getImageRes());
                                            ProjectFragment.imageView.setImageResource(WMUtil.settings.getImageRes());
                                            dialog.dismiss();
                                        }
                                    })
                                    .create(R.style.Dialog).show();
                            break;
                    }
                    if (((WMCommonListItemView) v).getAccessoryType() == WMCommonListItemView.ACCESSORY_TYPE_SWITCH) {
                        ((WMCommonListItemView) v).getSwitch().toggle();
                    }
                    WMUtil.settings.save();
                }
            }
        };


        int margin = QMUIDisplayHelper.dp2px(getContext(), 16);
        WMGroupListView.newSection(getContext())
                .setTitle("通知与提示")
                .addItemView(itemSound, onClickListener)
                .addItemView(itemMessage, onClickListener)
                .addItemView(itemBroad, onClickListener)
//                .addItemView(itemNotice, onClickListener)
                .setOnlyShowMiddleSeparator(true)
                .setMiddleSeparatorInset(margin, margin)
                .addTo(groupListView);

        WMGroupListView.newSection(getContext())
                .setTitle("默认项")
                .addItemView(itemCreateProject, onClickListener)
                .addItemView(itemCreatePanel, onClickListener)
                .setOnlyShowMiddleSeparator(true)
                .setMiddleSeparatorInset(margin, margin)
                .addTo(groupListView);

        WMGroupListView.newSection(getContext())
                .setTitle("通信与记录")
                .addItemView(itemPeriod, onClickListener)
                .addItemView(itemOrder, onClickListener)
                .addItemView(itemConnectDevice, onClickListener)
//                .addItemView(itemSave, onClickListener)
                .setOnlyShowMiddleSeparator(true)
                .setMiddleSeparatorInset(margin, margin)
                .addTo(groupListView);

        WMGroupListView.newSection(getContext())
                .setTitle("UI界面")
                .addItemView(itemBackground, onClickListener)
                .setOnlyShowMiddleSeparator(true)
                .setMiddleSeparatorInset(margin, margin)
                .addTo(groupListView);

        return groupListView;
    }

}
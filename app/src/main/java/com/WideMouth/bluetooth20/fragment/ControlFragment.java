package com.WideMouth.bluetooth20.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.WideMouth.bluetooth20.Activity.CommunicationActivity;
import com.WideMouth.bluetooth20.ControlGroup.ControlButton;
import com.WideMouth.bluetooth20.ControlGroup.ControlItem;
import com.WideMouth.bluetooth20.ControlGroup.ControlPanel;
import com.WideMouth.bluetooth20.ControlGroup.ControlSlider;
import com.WideMouth.bluetooth20.ControlGroup.ControlSwitch;
import com.WideMouth.bluetooth20.R;
import com.WideMouth.bluetooth20.Util.WMUtil;
import com.WideMouth.bluetooth20.WMView.WMButton;
import com.WideMouth.bluetooth20.WMView.WMFloatLayout;
import com.google.android.material.tabs.TabLayout;
import com.qmuiteam.qmui.widget.QMUISlider;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class ControlFragment extends Fragment {
    private static final String TAG = "ControlFragment";
    public static final String ACTION_ORDER = "com.widemouth.bluetooth20.action.order";
    RecyclerView recyclerView;
    ControlPanelAdapter controlPanelAdapter;

    int lastProgress = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        recyclerView = new RecyclerView(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        controlPanelAdapter = new ControlPanelAdapter();
        recyclerView.setAdapter(controlPanelAdapter);
        return recyclerView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void addAction() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        View view = View.inflate(getContext(), R.layout.controlpanel_add, null);
        final TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        final NumberPicker numberPicker = view.findViewById(R.id.numberPicker);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(9);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                TabLayout.Tab selectedTab = tabLayout.getTabAt(tabLayout.getSelectedTabPosition());
                selectedTab.setTag(newVal);
            }
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getTag() == null) {
                    numberPicker.setValue(0);
                    return;
                }
                numberPicker.setValue((Integer) tab.getTag());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        dialog.setView(view)
                .setPositiveButton("添加", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ControlPanel controlPanel = new ControlPanel();
                        controlPanel.setProject_id(CommunicationActivity.projectId);
                        for (int i = 0; i < tabLayout.getTabCount(); i++) {
                            TabLayout.Tab tab = tabLayout.getTabAt(i);
                            if (tab.getTag() == null || (int) tab.getTag() == 0) {
                                continue;
                            }
                            controlPanel.save();
                            int num = (int) tab.getTag();
                            if (i == 0) {
                                for (int j = 0; j < num; j++) {
                                    ControlSlider controlSlider = new ControlSlider();
                                    controlSlider.setControlPanel_id(controlPanel.getId());
                                    controlSlider.setProgress(0);
                                    controlSlider.setText("滑杆" + j);
                                    controlSlider.setMinProgress(0);
                                    controlSlider.setMaxProgress(100);
                                    controlSlider.save();
                                }
                            } else if (i == 1) {
                                for (int j = 0; j < num; j++) {
                                    ControlButton controlButton = new ControlButton();
                                    controlButton.setControlPanel_id(controlPanel.getId());
                                    controlButton.setOn(false);
                                    controlButton.setOnText("按钮" + j);
                                    controlButton.setOffText("按钮" + j);
                                    controlButton.setOnMessage("on");
                                    controlButton.setOffMessage("off");
                                    controlButton.save();
                                }
                            } else if (i == 2) {
                                for (int j = 0; j < num; j++) {
                                    ControlSwitch controlSwitch = new ControlSwitch();
                                    controlSwitch.setControlPanel_id(controlPanel.getId());
                                    controlSwitch.setOn(false);
                                    controlSwitch.setText("开关" + j);
                                    controlSwitch.setOnMessage("on");
                                    controlSwitch.setOffMessage("off");
                                    controlSwitch.save();
                                }
                            }
                        }
                        controlPanel.save();
                        controlPanelAdapter.addControlPanel(controlPanel);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

//    public void notifyDataSetChanged() {
//        Project project = ProjectFragment.project;
//        List<ControlPanel> controlPanelList = LitePal.where("project_id = ?", "" + project.getId()).find(ControlPanel.class);
//        if (controlPanelList == null || controlPanelList.size() == 0) {
//            createNormalControlPanel(project.getId());
//            controlPanelList.add(controlPanel);
//        }
//        controlPanelAdapter.setControlPanelList(controlPanelList);
//    }


    public static ControlPanel createNormalControlPanel(int projectId) {
        ControlPanel controlPanel = new ControlPanel();
        controlPanel.setProject_id(projectId);
        controlPanel.save();
        ControlSlider controlSlider = new ControlSlider();
        controlSlider.setControlPanel_id(controlPanel.getId());
        controlSlider.setText("滑杆");
        controlSlider.setMinProgress(0);
        controlSlider.setMaxProgress(100);
        controlSlider.setProgress(0);
        controlSlider.save();
        for (int i = 0; i < 3; i++) {
            ControlButton controlButton = new ControlButton();
            controlButton.setControlPanel_id(controlPanel.getId());
            controlButton.setOnText("按钮" + i);
            controlButton.setOnMessage("on");
            controlButton.setOffText("按钮" + i);
            controlButton.setOffMessage("off");
            controlButton.setOn(false);
            controlButton.save();
        }
        for (int i = 0; i < 2; i++) {
            ControlSwitch controlSwitch = new ControlSwitch();
            controlSwitch.setControlPanel_id(controlPanel.getId());
            controlSwitch.setText("开关");
            controlSwitch.setOnMessage("on");
            controlSwitch.setOffMessage("off");
            controlSwitch.setOn(false);
            controlSwitch.save();
        }
        return controlPanel;
    }

    class ControlPanelAdapter extends RecyclerView.Adapter<ControlPanelAdapter.ViewHolder> {
        private List<ControlPanel> controlPanelList;

        public ControlPanelAdapter() {
            this.controlPanelList = LitePal.where("project_id = ?", String.valueOf(CommunicationActivity.projectId)).find(ControlPanel.class);
            if (WMUtil.settings.isCreatePanel()) {
                if (controlPanelList == null || controlPanelList.size() == 0) {
                    controlPanelList.add(createNormalControlPanel(CommunicationActivity.projectId));
                }
            }
        }

        public void addControlPanel(ControlPanel controlPanel) {
            this.controlPanelList.add(controlPanel);
            notifyItemInserted(getItemCount() - 1);
        }


        @NonNull
        @Override
        public ControlPanelAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_control_panel, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            holder.container.removeAllViews();
            final ControlPanel controlPanel = controlPanelList.get(position);
            final List<ControlSlider> controlSliderList = controlPanel.getControlSliderList();
            final List<ControlButton> controlButtonList = controlPanel.getControlButtonList();
            final List<ControlSwitch> controlSwitchList = controlPanel.getControlSwitchList();
            final List<ControlItem> controlItemList = new ArrayList<>();
            controlItemList.addAll(controlSliderList);
            controlItemList.addAll(controlButtonList);
            controlItemList.addAll(controlSwitchList);
            holder.editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final PopupMenu popupMenu = new PopupMenu(getContext(), v);
                    popupMenu.getMenuInflater().inflate(R.menu.controlpanel_edit_menu, popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.edit:
                                    View view = View.inflate(getContext(), R.layout.controlpanel_edit, null);
                                    final TabLayout controlItemTabLayout = view.findViewById(R.id.controlItemTabLayout);
                                    final TabLayout attrTabLayout = view.findViewById(R.id.attrTabLayout);
                                    final EditText editText = view.findViewById(R.id.editText);
                                    editText.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            editText.requestFocus();
                                            InputMethodManager manager = ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE));
                                            if (manager != null) manager.showSoftInput(editText, 0);
                                        }
                                    });
                                    for (int i = 0; i < controlSliderList.size(); i++) {
                                        controlItemTabLayout.addTab(controlItemTabLayout.newTab().setText("滑杆" + i).setTag(controlSliderList.get(i)));
                                    }
                                    for (int i = 0; i < controlButtonList.size(); i++) {
                                        controlItemTabLayout.addTab(controlItemTabLayout.newTab().setText("按钮" + i).setTag(controlButtonList.get(i)));
                                    }
                                    for (int i = 0; i < controlSwitchList.size(); i++) {
                                        controlItemTabLayout.addTab(controlItemTabLayout.newTab().setText("开关" + i).setTag(controlSwitchList.get(i)));
                                    }

                                    TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
                                        @Override
                                        public void onTabSelected(TabLayout.Tab tab) {
                                            attrTabLayout.removeAllTabs();
                                            ControlItem controlItem = controlItemList.get(tab.getPosition());
                                            for (int i = 0; i < 4; i++) {
                                                if (controlItem.getAttrName(i) == null) {
                                                    continue;
                                                }
                                                attrTabLayout.addTab(attrTabLayout.newTab().setText(controlItem.getAttrName(i)));
                                            }
                                        }

                                        @Override
                                        public void onTabUnselected(TabLayout.Tab tab) {

                                        }

                                        @Override
                                        public void onTabReselected(TabLayout.Tab tab) {

                                        }
                                    };
                                    final TextWatcher textWatcher = new TextWatcher() {
                                        @Override
                                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                        }

                                        @Override
                                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                                        }

                                        @Override
                                        public void afterTextChanged(Editable s) {
                                            ControlItem controlItem = controlItemList.get(controlItemTabLayout.getSelectedTabPosition());
                                            controlItem.setAttr(s.toString(), attrTabLayout.getSelectedTabPosition());
                                            controlItem.saveByLitePal();
                                        }
                                    };
                                    TabLayout.OnTabSelectedListener onTabSelectedListener1 = new TabLayout.OnTabSelectedListener() {
                                        @Override
                                        public void onTabSelected(TabLayout.Tab tab) {
                                            ControlItem controlItem = controlItemList.get(controlItemTabLayout.getSelectedTabPosition());
                                            editText.removeTextChangedListener(textWatcher);
                                            editText.setText(controlItem.getAttr(tab.getPosition()));
                                            editText.setInputType(controlItem.getInputType(tab.getPosition()));
                                            editText.addTextChangedListener(textWatcher);
                                        }

                                        @Override
                                        public void onTabUnselected(TabLayout.Tab tab) {

                                        }

                                        @Override
                                        public void onTabReselected(TabLayout.Tab tab) {

                                        }
                                    };
                                    onTabSelectedListener.onTabSelected(controlItemTabLayout.getTabAt(0));
                                    controlItemTabLayout.addOnTabSelectedListener(onTabSelectedListener);
                                    onTabSelectedListener1.onTabSelected(attrTabLayout.getTabAt(0));
                                    attrTabLayout.addOnTabSelectedListener(onTabSelectedListener1);
                                    editText.addTextChangedListener(textWatcher);

                                    new AlertDialog.Builder(getContext())
                                            .setView(view)
                                            .setPositiveButton("完成", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                @Override
                                                public void onDismiss(DialogInterface dialog) {
                                                    notifyItemChanged(position);
                                                }
                                            })
                                            .show();
                                    break;

                                case R.id.delete:
                                    new QMUIDialog.MessageDialogBuilder(getActivity())
                                            .setTitle("项目删除")
                                            .setMessage("确定要删除吗？")
                                            .addAction("取消", new QMUIDialogAction.ActionListener() {
                                                @Override
                                                public void onClick(QMUIDialog dialog, int index) {
                                                    dialog.dismiss();
                                                }
                                            })
                                            .addAction(0, "删除", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                                                @Override
                                                public void onClick(QMUIDialog dialog, int index) {
                                                    controlPanel.delete();
                                                    controlPanelList.remove(position);
                                                    notifyItemRemoved(position);
                                                    Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss();
                                                }
                                            })
                                            .create(R.style.Dialog).show();
                                    break;
                                case R.id.clean:
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });

            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            for (final ControlSlider controlSlider : controlSliderList) {
                View view = View.inflate(getContext(), R.layout.item_slider, null);
                QMUISlider slider = view.findViewById(R.id.slider);
                TextView sliderText = view.findViewById(R.id.sliderText);
                final TextView progressText = view.findViewById(R.id.progress);
                slider.setCurrentProgress(controlSlider.getProgress());
                final int min = controlSlider.getMinProgress();
                final int max = controlSlider.getMaxProgress();
                int p = min + (max - min) * controlSlider.getProgress() / 100;
                progressText.setText(String.valueOf(p));
                slider.setCallback(new QMUISlider.Callback() {
                    @Override
                    public void onProgressChange(QMUISlider slider, int progress, int tickCount, boolean fromUser) {
                        if (!fromUser) {
                            lastProgress = progress;
                            return;
                        }
                        if (progress == lastProgress) {
                            lastProgress = progress;
                            return;
                        }
                        lastProgress = progress;
                        int p = min + (max - min) * progress / 100;
                        progressText.setText(String.valueOf(p));
                        sendData("" + p);
                    }

                    @Override
                    public void onTouchDown(QMUISlider slider, int progress, int tickCount, boolean hitThumb) {

                    }

                    @Override
                    public void onTouchUp(QMUISlider slider, int progress, int tickCount) {

                    }

                    @Override
                    public void onStartMoving(QMUISlider slider, int progress, int tickCount) {

                    }

                    @Override
                    public void onStopMoving(QMUISlider slider, int progress, int tickCount) {
                        controlSlider.setProgress(progress);
                        controlSlider.save();
                    }
                });
                sliderText.setText(controlSlider.getText());
                holder.container.addView(view, layoutParams);
            }


            int size = WMUtil.dip2px(getContext(), 80);
            ViewGroup.LayoutParams buttonLayoutParams = new ViewGroup.LayoutParams(size, size);
            for (final ControlButton controlButton : controlButtonList) {
                final WMButton button = new WMButton(getContext());
                button.setBackground(getContext().getDrawable(R.drawable.orange_background));
                if (controlButton.isOn()) {
                    button.setText(controlButton.getOnText());
                } else {
                    button.setText(controlButton.getOffText());
                }
                button.setTag(controlButton.isOn());
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ((Boolean) v.getTag()) {
                            if (sendData(controlButton.getOnMessage())) {
                                button.setText(controlButton.getOffText());
                                button.setTag(false);
                                controlButton.setOn(false);
                                controlButton.save();
                            }
                        } else {
                            if (sendData(controlButton.getOffMessage())) {
                                button.setText(controlButton.getOnText());
                                button.setTag(true);
                                controlButton.setOn(true);
                                controlButton.save();
                            }
                        }
                    }
                });
                holder.container.addView(button, buttonLayoutParams);
            }


            for (final ControlSwitch controlSwitch : controlSwitchList) {
                int w = WMUtil.dip2px(getContext(), 150);
                int h = WMUtil.dip2px(getContext(), 36);
                ViewGroup.LayoutParams switchLayoutParams = new ViewGroup.LayoutParams(w, h);
                View view = View.inflate(getContext(), R.layout.item_switch, null);
                Switch seekBar = view.findViewById(R.id.seekBar);
                TextView seekBarText = view.findViewById(R.id.seekBarText);
                seekBar.setChecked(controlSwitch.isOn());
                seekBarText.setText(controlSwitch.getText());
                seekBar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        String message = "";
                        if (isChecked) {
                            message = controlSwitch.getOffMessage();
                        } else {
                            message = controlSwitch.getOnMessage();
                        }
                        sendData(message);

                        controlSwitch.setOn(isChecked);
                        controlSwitch.save();
                    }
                });
                holder.container.addView(view, switchLayoutParams);
            }


        }

        public boolean sendData(String data) {
            if (ConnectFragment.sendData(data)) {
                if (WMUtil.settings.isOrder()) {
                    Intent intent = new Intent(ACTION_ORDER);
                    intent.putExtra(ACTION_ORDER, data);
                    CommunicationActivity.localBroadcastManager.sendBroadcast(intent);
                }
                return true;
            } else {
                CommunicationActivity.goConnectFragment();
                return false;
            }
        }

        @Override
        public int getItemCount() {
            return controlPanelList.size();
        }

        public void setControlPanelList(List<ControlPanel> controlPanelList) {
            this.controlPanelList = controlPanelList;
            notifyDataSetChanged();
            recyclerView.scrollToPosition(getItemCount() - 1);
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            WMFloatLayout container;
            ImageButton editButton;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                container = itemView.findViewById(R.id.container);
                editButton = itemView.findViewById(R.id.editButton);
            }
        }
    }
}
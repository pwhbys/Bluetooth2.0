package com.WideMouth.bluetooth20.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.WideMouth.bluetooth20.Activity.BoardActivity;
import com.WideMouth.bluetooth20.R;
import com.WideMouth.bluetooth20.WMView.WMCommonListItemView;
import com.WideMouth.bluetooth20.WMView.WMGroupListView;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;

public class BoardFragment extends Fragment {

    public static Fragment fragment;

    public static final String TITTLE = "tittle";
    public static final String FRAGMENTINDEX = "fragmentIndex";

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_board, null, false);
        WMGroupListView groupListView = view.findViewById(R.id.groupListView);

        WMCommonListItemView itemSettings = groupListView.createItemView(
                ContextCompat.getDrawable(getContext(), R.drawable.icon_settings_selected),
                "设置",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        itemSettings.setOrientation(WMCommonListItemView.VERTICAL);
        itemSettings.setTag(0);

        WMCommonListItemView itemState = groupListView.createItemView(
                ContextCompat.getDrawable(getContext(), R.drawable.icon_idea_selected),
                "指南",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        itemState.setOrientation(WMCommonListItemView.VERTICAL);
        itemState.setTag(1);

        WMCommonListItemView itemExplain = groupListView.createItemView(
                ContextCompat.getDrawable(getContext(), R.drawable.icon_explain_selected),
                "详情",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        itemExplain.setOrientation(WMCommonListItemView.VERTICAL);

        WMCommonListItemView itemFeedBack = groupListView.createItemView(
                ContextCompat.getDrawable(getContext(), R.drawable.icon_feedback),
                "反馈",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        itemFeedBack.setOrientation(WMCommonListItemView.VERTICAL);

        WMCommonListItemView itemComment = groupListView.createItemView(
                ContextCompat.getDrawable(getContext(), R.drawable.icon_comment_selected),
                "评论",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        itemComment.setOrientation(WMCommonListItemView.VERTICAL);

        WMCommonListItemView itemAbout = groupListView.createItemView(
                ContextCompat.getDrawable(getContext(), R.drawable.icon_about_selected),
                "关于",
                null,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_CHEVRON);
        itemAbout.setOrientation(WMCommonListItemView.VERTICAL);
        itemAbout.setTag(2);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof WMCommonListItemView) {
                    if (v.getTag() != null && v.getTag() instanceof Integer) {
                        Intent intent = new Intent(getContext(), BoardActivity.class)
                                .putExtra(TITTLE, ((WMCommonListItemView) v).getText().toString())
                                .putExtra(FRAGMENTINDEX, (Integer) v.getTag());
                        startActivity(intent);
                    }
                    switch (((WMCommonListItemView) v).getText().toString()) {
                        case "详情":
                            Intent link = new Intent(Intent.ACTION_VIEW);
                            link.setData(Uri.parse("https://www.cnblogs.com/WideMouth/p/13037001.html"));
                            startActivity(link);
                            break;
                        case "反馈":
                            Intent mail = new Intent(Intent.ACTION_SENDTO);
                            mail.setData(Uri.parse("mailto:3310275200@qq.com"));
                            mail.putExtra(Intent.EXTRA_SUBJECT, "反馈");
                            mail.putExtra(Intent.EXTRA_TEXT, "WideMouth");
                            startActivity(mail);
                            break;
                    }
                    if (((WMCommonListItemView) v).getAccessoryType() == WMCommonListItemView.ACCESSORY_TYPE_SWITCH) {
                        ((WMCommonListItemView) v).getSwitch().toggle();
                    }
                }
            }
        };

        int size = QMUIDisplayHelper.dp2px(getContext(), 20);
        int margin = QMUIDisplayHelper.dp2px(getContext(), 16);
        WMGroupListView.newSection(getContext())
                .setTitle("使用习惯")
//                .setDescription("Section 1 的描述")
                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(itemSettings, onClickListener)
                .setMiddleSeparatorInset(margin, margin)
                .setOnlyShowMiddleSeparator(true)
                .addTo(groupListView);

        WMGroupListView.newSection(getContext())
                .setTitle("详情与说明")
//                .setDescription("Section 1 的描述")
                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(itemState, onClickListener)
                .addItemView(itemExplain, onClickListener)
                .setMiddleSeparatorInset(margin, margin)
                .setOnlyShowMiddleSeparator(true)
                .addTo(groupListView);

        WMGroupListView.newSection(getContext())
                .setTitle("关于与互动")
//                .setDescription("Section 1 的描述")
                .setLeftIconSize(size, ViewGroup.LayoutParams.WRAP_CONTENT)
                .addItemView(itemFeedBack, onClickListener)
                .addItemView(itemComment, onClickListener)
                .addItemView(itemAbout, onClickListener)
                .setMiddleSeparatorInset(margin, margin)
                .setOnlyShowMiddleSeparator(true)
                .addTo(groupListView);
        return view;
    }
}
package com.WideMouth.bluetooth20.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.WideMouth.bluetooth20.R;
import com.WideMouth.bluetooth20.Util.WMUtil;
import com.google.android.material.textfield.TextInputEditText;

public class DetailsFragment extends Fragment {

    CharSequence[] q = {
            "1.可以与那些设备进行通信？",
            "2.蓝牙搜索方法与通信机制",
            "3.注册了哪些系统广播？",
            "4.蓝牙连接与通信遇到问题？"};
    CharSequence[] a = {"蓝牙串口通信是手机端串口通信工具，支持连接蓝牙2.0协议的蓝牙模块（如HC-02、HC-05、HC-06等）",
            "\u2022搜索方法：采用传统蓝牙搜索方法（BluetoothAdapter类的startDiscovery()方法）\n\u2022通信机制：获取配对蓝牙的BluetoothSocket实例，并调用getOutputStream()与getOutputStream()方法得到输入输出流实现通信"
            , "BluetoothAdapter.ACTION_DISCOVERY_STARTED（开始搜索）\nBluetoothDevice.ACTION_FOUND（发现蓝牙设备）\nBluetoothAdapter.ACTION_DISCOVERY_FINISHED（搜索结束）\nBluetoothDevice.ACTION_ACL_CONNECTED（设备已连接）\nBluetoothDevice.ACTION_ACL_DISCONNECTED（设备断开）\nBluetoothAdapter.ACTION_STATE_CHANGED（蓝牙状态改变）",
            "\u2022在刚打开蓝牙的数秒内，进行搜索可能出现搜索不到设备的情况，请完成一个搜索后重新搜索；\n\u2022蓝牙连接状态下非正常断开（如杀死进程）后，下次连接可能会连接不成功，请尝试重新连接几次；\n\u2022APP未进行设备适配，部分机型可能出现崩溃闪退情况，如出现此类问题，请前往设置检查是否同意所有权限或清除数据重启，如未解决请及时反馈；"};


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView textView = new TextView(getContext());
        int p = WMUtil.dip2px(getContext(), 20);
        textView.setPadding(p, p, p, p);
        int lineSpace = WMUtil.dip2px(getContext(), 8);
        textView.setLineSpacing(lineSpace, 1);
        SpannableStringBuilder s = new SpannableStringBuilder();
        ForegroundColorSpan foregroundColorSpanQ = new ForegroundColorSpan(getContext().getColor(R.color.orange_100));
        ForegroundColorSpan foregroundColorSpanA = new ForegroundColorSpan(getContext().getColor(R.color.qmui_config_color_gray_3));
        AbsoluteSizeSpan absoluteSizeSpanQ = new AbsoluteSizeSpan(15, true);
        AbsoluteSizeSpan absoluteSizeSpanA = new AbsoluteSizeSpan(14, true);
        for (int i = 0; i < q.length; i++) {
            s.append(q[i]);
            s.setSpan(CharacterStyle.wrap(foregroundColorSpanQ), s.length() - q[i].length(), s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            s.setSpan(CharacterStyle.wrap(absoluteSizeSpanQ), s.length() - q[i].length(), s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            s.append("\n");

            s.append(a[i]);
            s.setSpan(CharacterStyle.wrap(foregroundColorSpanA), s.length() - a[i].length(), s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            s.setSpan(CharacterStyle.wrap(absoluteSizeSpanA), s.length() - a[i].length(), s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            s.append("\n\n");
        }
        textView.setText(s);

        TextView link=new TextView(getContext());
        container.addView(textView);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
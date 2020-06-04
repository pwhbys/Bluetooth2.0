package com.WideMouth.bluetooth20.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.WideMouth.bluetooth20.Activity.CommunicationActivity;
import com.WideMouth.bluetooth20.ItemClass.Device;
import com.WideMouth.bluetooth20.R;
import com.WideMouth.bluetooth20.Util.SoundPoolUtil;
import com.WideMouth.bluetooth20.Util.WMUtil;
import com.WideMouth.bluetooth20.WMView.WMLoadingView;
import com.WideMouth.bluetooth20.WMView.WMScanFrameLayout;
import com.qmuiteam.qmui.span.QMUIAlignMiddleImageSpan;
import com.qmuiteam.qmui.span.QMUIMarginImageSpan;

import org.litepal.LitePal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class ConnectFragment extends Fragment {

    private static final String TAG = "ConnectFragment";
    private final static String MY_UUID = "00001101-0000-1000-8000-00805F9B34FB";   //SPP服务UUID号
    public static final String ACTION_RECEIVED = "com.widemouth.bluetooth20.action.RECEIVED";
    private List<Device> deviceList = new ArrayList<>();

    private static Context context;


    public boolean isConnected = false;
    public static BluetoothSocket socket;
    private InputStream inputStream;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice bluetoothDevice;
    private RecyclerView deviceRecyclerView;
    private DeviceAdapter deviceAdapter;

    private WMScanFrameLayout scanView;

    private WMLoadingView loadingView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        context = getContext();

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }

        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED));

        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));

        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));

        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED));

        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED));

        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));

//        getActivity().registerReceiver(broadcastReceiver, new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED));
        if (WMUtil.settings.isScan()) {
            scanView.post(new Runnable() {
                @Override
                public void run() {
                    startDiscovery();
                }
            });
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, null);
        scanView = view.findViewById(R.id.scanView);
        deviceRecyclerView = view.findViewById(R.id.deviceRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        deviceRecyclerView.setLayoutManager(layoutManager);
        deviceAdapter = new DeviceAdapter(deviceList);
        deviceRecyclerView.setAdapter(deviceAdapter);
        scanView.setOnCenterCircleClickListener(new WMScanFrameLayout.OnCenterCircleClickListener() {
            @Override
            public void onClick(boolean showRipple, String text) {
                switch (text) {
                    case WMScanFrameLayout.START:
                        startDiscovery();
                        break;
                    case WMScanFrameLayout.STOP:
                        scanView.cancelShowRipple();
                        scanView.setText(WMScanFrameLayout.START);
                        bluetoothAdapter.cancelDiscovery();
                        break;
                    case WMScanFrameLayout.DISCONNECT:
                        close(true, WMScanFrameLayout.START);
                        break;
                }
            }
        });
        return view;
    }

    private void startDiscovery() {
        scanView.startShowRipple();
        scanView.setText(WMScanFrameLayout.STOP);
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
        }
        bluetoothAdapter.startDiscovery();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
        // 关闭服务查找

        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
        // 注销action接收器
        getActivity().unregisterReceiver(broadcastReceiver);

        close(false, null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    // 查找到设备和搜索完成action监听器
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // 查找到设备action
            if (WMUtil.settings.isBroad()) {
                Toast.makeText(context, "系统广播\n[" + action + "]", Toast.LENGTH_LONG).show();
            }
            switch (action) {
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    deviceList.clear();
                    deviceAdapter.notifyDataSetChanged();
                    break;
                case BluetoothDevice.ACTION_FOUND:
                    // 得到蓝牙设备
                    bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    int position = 0;
                    if (bluetoothDevice.getBondState() != BluetoothDevice.BOND_BONDED) {
                        position = deviceList.size();
                    }
                    Device device = new Device(bluetoothDevice.getName(), bluetoothDevice.getAddress(), bluetoothDevice.getBondState());
                    if (deviceList.contains(device)) {
                        return;
                    }
                    deviceList.add(position, device);
                    deviceAdapter.notifyItemInserted(position);
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    scanView.cancelShowRipple();
                    if (scanView.getText().equals(WMScanFrameLayout.STOP)) {
                        scanView.setText(WMScanFrameLayout.START);
                    }
                    break;
                case BluetoothDevice.ACTION_ACL_CONNECTED:
                    break;
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                            BluetoothAdapter.ERROR);
                    if (state != BluetoothAdapter.STATE_OFF) {
                        break;
                    }
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    if (scanView.getText().equals(WMScanFrameLayout.DISCONNECT)) {
                        scanView.setText(WMScanFrameLayout.START);
                        loadingView.removeLoadingView();
                        loadingView.setImageResource(R.drawable.icon_notice_exception);
                    }
                    close(false, null);
                    break;
            }
        }

    };

    public void close(boolean updateUI, String text) {
        if (updateUI) {
            scanView.setText(text);
            loadingView.removeLoadingView();
            loadingView.setImageResource(R.drawable.icon_notice_exception);
        }
        isConnected = false;
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            socket = null;
        }
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            inputStream = null;
        }
    }

    //接收数据线程
    class ReadThread extends Thread {

        public void run() {

            int num = 0;
            byte[] buffer = new byte[1024];
            byte[] buffer_new = new byte[1024];
            int i = 0;
            int n = 0;
            //接收线程
            while (isConnected) {
                try {
                    while (isConnected && inputStream.available() == 0) {

                    }
                    final StringBuilder m = new StringBuilder();
                    while (isConnected) {
                        Thread.sleep(WMUtil.settings.getPeriod());
                        num = inputStream.read(buffer);         //读入数据
                        n = 0;
                        for (i = 0; i < num; i++) {
                            if ((buffer[i] == 0x0d) && (buffer[i + 1] == 0x0a)) {
                                buffer_new[n] = 0x0a;
                                i++;
                            } else {
                                buffer_new[n] = buffer[i];
                            }
                            n++;
                        }
                        final String s = new String(buffer_new, 0, n, "GB2312");
                        m.append(s);
                        if (inputStream.available() == 0) break;  //短时间没有数据才跳出进行显示
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (TextUtils.isEmpty(m)) {
                                return;
                            }
                            if (WMUtil.settings.isSound()) {
                                SoundPoolUtil.play(SoundPoolUtil.message_received_sound_id);
                            }
                            if (WMUtil.settings.isMessage()) {
                                Toast.makeText(getActivity(), "[接收]" + m, Toast.LENGTH_SHORT).show();
                            }
                            Intent intent = new Intent(ACTION_RECEIVED);
                            intent.putExtra(ACTION_RECEIVED, m.toString());
                            CommunicationActivity.localBroadcastManager.sendBroadcast(intent);
                        }
                    });

                } catch (IOException e) {
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

    public static boolean sendData(String data) {
        int i = 0;
        int n = 0;
        try {
            if (socket == null) {
                return false;
            }
            OutputStream outputStream = socket.getOutputStream();   //蓝牙连接输出流
            byte[] bos = data.getBytes("GB2312");
            for (i = 0; i < bos.length; i++) {
                if (bos[i] == 0x0a) n++;
            }
            byte[] bos_new = new byte[bos.length + n];
            n = 0;
            for (i = 0; i < bos.length; i++) { //手机中换行为0a,将其改为0d 0a后再发送
                if (bos[i] == 0x0a) {
                    bos_new[n] = 0x0d;
                    n++;
                    bos_new[n] = 0x0a;
                } else {
                    bos_new[n] = bos[i];
                }
                n++;
            }
            outputStream.write(bos_new);
        } catch (IOException e) {
            return false;
        }
        if (WMUtil.settings.isSound()) {
            SoundPoolUtil.play(SoundPoolUtil.message_send_sound_id);
        }

        if (WMUtil.settings.isMessage()) {
            Toast.makeText(context, "[发送]" + data, Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {

        int num = 1;

        private List<Device> deviceList;

        public DeviceAdapter(List<Device> deviceList) {
            this.deviceList = deviceList;
//            Device device = LitePal.where("project_id = ?", String.valueOf(CommunicationActivity.projectId)).findLast(Device.class);
//            if (device != null) {
//                this.deviceList.add(device);
//            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_device, parent, false);
            final ViewHolder viewHolder = new ViewHolder(view);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isConnected) {

                    } else {
                        int position = viewHolder.getAdapterPosition();
                        final Device device = deviceList.get(position);
                        if (bluetoothAdapter.isDiscovering()) {
                            bluetoothAdapter.cancelDiscovery();
                        }
                        loadingView = viewHolder.loadingView;
                        viewHolder.loadingView.setImageResource(0);
                        viewHolder.loadingView.showLoadingView();
                        scanView.setText("连接中");
                        bluetoothDevice = bluetoothAdapter.getRemoteDevice(device.getAddress());
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                socketConnect(bluetoothDevice);
                            }
                        }, 1000);
                    }

                }
            });
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Device device = deviceList.get(position);
            SpannableStringBuilder s = new SpannableStringBuilder();
            String name = String.valueOf(device.getName());
            String address = String.valueOf(device.getAddress());
            s.append(name)
                    .append("[tag]")
                    .append("\n")
                    .append(address)
                    .setSpan(new ForegroundColorSpan(0xFF858C96), s.length() - address.length(), s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            Drawable drawable;
            if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
                drawable = getContext().getDrawable(R.drawable.icon_tag_paired);
            } else {
                drawable = getContext().getDrawable(R.drawable.icon_tag_new);
            }
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            s.setSpan(new QMUIMarginImageSpan(drawable, QMUIAlignMiddleImageSpan.ALIGN_MIDDLE, 12, 0),
                    name.length(), name.length() + "[tag]".length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.info.setText(s);
        }


        @Override
        public int getItemCount() {
            return deviceList.size();
        }


        public void socketConnect(final BluetoothDevice device) {
            //连接socket
            new Thread() {
                public void run() {
                    try {
                        socket = device.createRfcommSocketToServiceRecord(UUID.fromString(MY_UUID));
                        socket.connect();//核心语句
                    } catch (IOException e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                close(true, WMScanFrameLayout.START);
                                Toast.makeText(getContext(), "连接失败!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }
                    //打开接收线程
                    try {
                        inputStream = socket.getInputStream();   //得到蓝牙数据输入流
                    } catch (IOException e) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                close(true, WMScanFrameLayout.START);
                                Toast.makeText(getContext(), "连接失败!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return;
                    }
                    isConnected = true;
                    new ReadThread().start();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            scanView.setText(WMScanFrameLayout.DISCONNECT);
                            loadingView.removeLoadingView();
                            loadingView.setImageResource(R.drawable.icon_notice_connected);
                        }
                    });
                }
            }.start();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView info;
            WMLoadingView loadingView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                info = itemView.findViewById(R.id.device_info);
                loadingView = itemView.findViewById(R.id.loadingView);
            }
        }
    }
}
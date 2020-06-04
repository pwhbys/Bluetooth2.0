package com.WideMouth.bluetooth20.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.WideMouth.bluetooth20.Activity.CommunicationActivity;
import com.WideMouth.bluetooth20.ControlGroup.ControlPanel;
import com.WideMouth.bluetooth20.ItemClass.Message;
import com.WideMouth.bluetooth20.R;
import com.WideMouth.bluetooth20.Util.WMUtil;
import com.WideMouth.bluetooth20.WMView.WMNavigationBar.WMNavigationBar;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import static com.WideMouth.bluetooth20.Activity.CommunicationActivity.localBroadcastManager;
import static com.WideMouth.bluetooth20.fragment.ChatFragment.MessageAdapter.MESSAGE_TYPE_RECEIVED;
import static com.WideMouth.bluetooth20.fragment.ChatFragment.MessageAdapter.MESSAGE_TYPE_SEND;

public class ChatFragment extends Fragment {

    private static final String TAG = "ChatFragment";

    List<Message> messageList = new ArrayList<>();
    RecyclerView messageRecyclerView;
    MessageAdapter messageAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        messageRecyclerView = new RecyclerView(getContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        messageRecyclerView.setLayoutManager(layoutManager);
        messageAdapter = new MessageAdapter(messageList);
        messageRecyclerView.setAdapter(messageAdapter);
        return messageRecyclerView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        localBroadcastManager.registerReceiver(broadcastReceiver, new IntentFilter(WMNavigationBar.ACTION_INPUT));
        localBroadcastManager.registerReceiver(broadcastReceiver, new IntentFilter(ConnectFragment.ACTION_RECEIVED));
        localBroadcastManager.registerReceiver(broadcastReceiver, new IntentFilter(ControlFragment.ACTION_ORDER));
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action) {
                case WMNavigationBar.ACTION_INPUT:
                    String text = intent.getStringExtra(WMNavigationBar.ACTION_INPUT);
                    if (ConnectFragment.sendData(text)) {
                        Message message = new Message();
                        message.setText(text);
                        message.setType(MESSAGE_TYPE_SEND);
                        messageList.add(message);
                        messageAdapter.notifyItemRangeChanged(messageList.size() - 2, 2);
                        messageRecyclerView.scrollToPosition(messageList.size() - 1);
                    } else {
                        CommunicationActivity.goConnectFragment();
                    }
                    break;
                case ConnectFragment.ACTION_RECEIVED:
                    String s = intent.getStringExtra(ConnectFragment.ACTION_RECEIVED);
                    Message message = new Message();
                    message.setText(s);
                    message.setType(MESSAGE_TYPE_RECEIVED);
                    messageList.add(message);
                    messageAdapter.notifyItemRangeChanged(messageList.size() - 2, 2);
                    messageRecyclerView.scrollToPosition(messageList.size() - 1);
                    break;
                case ControlFragment.ACTION_ORDER:
                    if (WMUtil.settings.isOrder()) {
                        String order = intent.getStringExtra(ControlFragment.ACTION_ORDER);
                        Message o = new Message();
                        o.setText(order);
                        o.setType(MESSAGE_TYPE_SEND);
                        messageList.add(o);
                        messageAdapter.notifyItemRangeChanged(messageList.size() - 2, 2);
                        messageRecyclerView.scrollToPosition(messageList.size() - 1);
                    }
                    break;
            }
        }
    };


    class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
        public static final int MESSAGE_TYPE_RECEIVED = 1;
        public static final int MESSAGE_TYPE_SEND = 2;
        private List<Message> messageList;

        public MessageAdapter(List<Message> messageList) {
            this.messageList = messageList;
//            this.messageList.addAll(LitePal.where("project_id = ?", String.valueOf(CommunicationActivity.projectId)).find(Message.class));
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Message message = messageList.get(position);
            holder.textView.setText(message.getText());
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) holder.textView.getLayoutParams();
            Context context = holder.textView.getContext();
            int o = WMUtil.dip2px(context, 10);
            int m = WMUtil.dip2px(context, 100);
            if (message.getType() == MESSAGE_TYPE_RECEIVED) {
                layoutParams.gravity = Gravity.LEFT;
                layoutParams.leftMargin = o;
                layoutParams.rightMargin = m;
                holder.textView.setBackground(getContext().getDrawable(R.drawable.radius_4dp_corner_white_background));
            } else if (message.getType() == MESSAGE_TYPE_SEND) {
                layoutParams.gravity = Gravity.RIGHT;
                layoutParams.leftMargin = m;
                layoutParams.rightMargin = o;
                holder.textView.setBackground(getContext().getDrawable(R.drawable.orange_background));
            }
            int n = WMUtil.dip2px(context, 8);
            int b = WMUtil.dip2px(context, 120);
            if (position == messageList.size() - 1) {
                layoutParams.bottomMargin = b;
            } else {
                layoutParams.bottomMargin = n;
            }
            holder.textView.setLayoutParams(layoutParams);
        }

        @Override
        public int getItemCount() {
            return messageList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                this.textView = itemView.findViewById(R.id.messageTextView);
            }
        }

    }
}
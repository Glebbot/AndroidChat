package com.example.androidchat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidchat.R;
import com.example.androidchat.SharedPrefsUtil;
import com.example.androidchat.adapters.ChannelsAdapter;
import com.example.androidchat.adapters.ChatAdapter;
import com.example.androidchat.databinding.ActivityChatBinding;
import com.example.androidchat.model.Channel;
import com.example.androidchat.model.ChatMessage;
import com.example.androidchat.model.User;
import com.example.androidchat.retrofit.RetrofitService;
import com.example.androidchat.retrofit.UserApi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;
    private List<ChatMessage> chatMessages;
    private ChatAdapter chatAdapter;
    private RetrofitService retrofitService = new RetrofitService();
    private UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        TextView title = findViewById(R.id.title_this_channel);
        String savedText = SharedPrefsUtil.getChannelName(this);
        title.setText(savedText);
        SetListeners();
        init();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                viewUpdate();
            }
        }, 5000, 1000);

    }
    private void sendMessage() {

        EditText inputMsg = findViewById(R.id.inputMessage);
        String messageText = String.valueOf(inputMsg.getText());

        if(!messageText.equals("")) {
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.message = messageText;
            chatMessage.senderName = SharedPrefsUtil.getUserLogin(this);
            chatMessage.channelName = SharedPrefsUtil.getChannelName(this);
            long timestamp = new Date().getTime();
            Date date = new Date(timestamp);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
            String formatDate = simpleDateFormat.format(date);
            chatMessage.datetime = formatDate;

            userApi.sendMessage(chatMessage)
                    .enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {

                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            Toast.makeText(ChatActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            Logger.getLogger(ChatActivity.class.getName()).log(Level.SEVERE, "error occured", t);
                        }
                    });
            inputMsg.setText("");

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    init();
                }
            }, 100);
        }
    }
    private void viewUpdate() {
        int messageCount = chatMessages.size();
        Channel channel = new Channel();
        channel.setLogin(SharedPrefsUtil.getUserLogin(this));
        channel.setName(SharedPrefsUtil.getChannelName(this));

        userApi.updateMessages(channel)
                .enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        if(response.isSuccessful()) {

                            int result = response.body();
                            if (result > messageCount)
                            {
                                init();
                            }
                        }
                        else{
                            Toast.makeText(ChatActivity.this, "Messages list updating failed", Toast.LENGTH_SHORT ).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        Toast.makeText(ChatActivity.this,"No connection server", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void init() {

        RetrofitService retrofitService = new RetrofitService();
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);
        Channel channel = new Channel();
        channel.setName(SharedPrefsUtil.getChannelName(this));
        channel.setLogin(SharedPrefsUtil.getUserLogin(this));
        userApi.getMessages(channel)
                .enqueue(new Callback<List<ChatMessage>>() {
                    @Override
                    public void onResponse(Call<List<ChatMessage>> call, Response<List<ChatMessage>> response) {
                        if (response.isSuccessful()) {
                            chatMessages = response.body();
                            if (chatMessages != null && chatMessages.size()>0) {
                                chatAdapter = new ChatAdapter(chatMessages, SharedPrefsUtil.getUserLogin(ChatActivity.this));
                                binding.chatRecyclerView.setAdapter(chatAdapter);
                                binding.chatRecyclerView.setVisibility(View.VISIBLE);
                            }
                            else {
                                Toast.makeText(ChatActivity.this, "No messages", Toast.LENGTH_SHORT).show();

                            }
                        }
                        else {
                            Toast.makeText(ChatActivity.this, "Failed to load messages", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<List<ChatMessage>> call, Throwable t) {
                        Toast.makeText(ChatActivity.this, "Server not responding", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(SignUpActivity.class.getName()).log(Level.SEVERE, "error occured", t);
                    }
                });

    }
    private void SetListeners() {
        binding.imageBackChat.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ChannelsActivity.class)));
        binding.imageInfo.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ChatInfoActivity.class)));
        binding.sendMessage.setOnClickListener(v -> sendMessage());
    }
}

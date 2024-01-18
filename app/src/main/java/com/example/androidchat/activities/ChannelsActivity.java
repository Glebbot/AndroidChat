package com.example.androidchat.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidchat.R;
import com.example.androidchat.SharedPrefsUtil;
import com.example.androidchat.adapters.ChannelsAdapter;
import com.example.androidchat.databinding.ActivitySignInBinding;
import com.example.androidchat.databinding.ActivityUsersBinding;
import com.example.androidchat.listeners.ChannelListener;
import com.example.androidchat.model.Channel;
import com.example.androidchat.model.User;
import com.example.androidchat.retrofit.RetrofitService;
import com.example.androidchat.retrofit.UserApi;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChannelsActivity extends AppCompatActivity implements ChannelListener {
    private ActivityUsersBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SetListeners();

        getChannels();

    }
    private void getChannels() {
        loading(true);
        RetrofitService retrofitService = new RetrofitService();
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);
        User user = new User();
        user.setEmail(SharedPrefsUtil.getUserLogin(this));
        userApi.getUserChannel(user)
                .enqueue(new Callback<List<Channel>>() {
                    @Override
                    public void onResponse(Call<List<Channel>> call, Response<List<Channel>> response) {
                        if (response.isSuccessful()) {
                            List<Channel> channels = response.body();
                            if (channels != null && channels.size()>0) {
                                ChannelsAdapter channelsAdapter = new ChannelsAdapter(channels, ChannelsActivity.this);
                                binding.usersRecyclerView.setAdapter(channelsAdapter);
                                binding.usersRecyclerView.setVisibility(View.VISIBLE);
                            }
                            else {
                                Toast.makeText(ChannelsActivity.this, "No channels", Toast.LENGTH_SHORT).show();
                                showErrorMessage();
                            }
                        }
                        else {
                            Toast.makeText(ChannelsActivity.this, "Failed to load chanenls", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<List<Channel>> call, Throwable t) {
                        Toast.makeText(ChannelsActivity.this, "Server not responding", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(SignUpActivity.class.getName()).log(Level.SEVERE, "error occured", t);
                    }
                });
        loading(false);
    }
    private void showErrorMessage() {
        binding.textErrorMessage.setText(String.format("%s","No channels available"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }
    private void loading(Boolean isLoading) {
        if(isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        }
        else {
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }
    private void SetListeners() {
        binding.FabNewChat.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), AddChannelActivity.class)));
        binding.imageBack.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ProfileActivity.class)));
    }

    @Override
    public void onChannelClicked(Channel channel) {
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        SharedPrefsUtil.saveChannelName(this,channel.getName());
        SharedPrefsUtil.saveChannelDescr(this, channel.getDescription());
        startActivity(intent);
    }
}

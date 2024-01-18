package com.example.androidchat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidchat.R;
import com.example.androidchat.SharedPrefsUtil;
import com.example.androidchat.adapters.ChannelsAdapter;
import com.example.androidchat.adapters.UsersAdapter;
import com.example.androidchat.databinding.ActivityChatGroupInfoBinding;
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


public class ChatInfoActivity extends AppCompatActivity {
    private ActivityChatGroupInfoBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityChatGroupInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        TextView title = findViewById(R.id.title_channel);
        String savedText = SharedPrefsUtil.getChannelName(this);
        title.setText(savedText);
        TextView description = findViewById(R.id.description);
        String savedDesc = SharedPrefsUtil.getChannelDescr(this);
        description.setText(savedDesc);
        SetListeners();
        getUsers();

    }
    private void getUsers() {
        RetrofitService retrofitService = new RetrofitService();
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);
        Channel channel = new Channel();
        channel.setName(SharedPrefsUtil.getChannelName(this));
        userApi.getUsers(channel)
                .enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        if (response.isSuccessful()) {
                            List<User> users = response.body();
                            if (users != null && users.size()>0) {
                                UsersAdapter usersAdapter = new UsersAdapter(users);
                                binding.usersRecyclerView.setAdapter(usersAdapter);
                                binding.usersRecyclerView.setVisibility(View.VISIBLE);
                            }
                            else {
                                Toast.makeText(ChatInfoActivity.this, "No users", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(ChatInfoActivity.this, "Failed to load users", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        Toast.makeText(ChatInfoActivity.this, "Server not responding", Toast.LENGTH_SHORT).show();
                        Logger.getLogger(SignUpActivity.class.getName()).log(Level.SEVERE, "error occured", t);
                    }
                });
    }
    private void SetListeners() {
        binding.decorate.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ChatActivity.class)));

    }
}

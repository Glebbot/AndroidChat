package com.example.androidchat.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.example.androidchat.R;
import com.example.androidchat.SharedPrefsUtil;
import com.example.androidchat.databinding.ActivityAddChannelBinding;
import com.example.androidchat.model.Channel;
import com.example.androidchat.model.User;
import com.example.androidchat.retrofit.RetrofitService;
import com.example.androidchat.retrofit.UserApi;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddChannelActivity extends AppCompatActivity {
    private ActivityAddChannelBinding binding;
    private Dialog dialog;
    private Button addButton;
    private Button enterButton;

    private RetrofitService retrofitService = new RetrofitService();
    private UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityAddChannelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        addButton = findViewById(R.id.btn_add_channel);
        addButton.setOnClickListener(v -> createChannelDialog());

        enterButton = findViewById(R.id.btn_enter_the_channel);
        enterButton.setOnClickListener(v -> enterChannelDialog());
        SetListeners();



    }

    private void createChannelDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_create_channel_dialog);
        initializeComponentsCreate();
        dialog.show();
    }
    private void enterChannelDialog() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_enter_the_channel_dialog);
        initializeComponentsEnter();
        dialog.show();
    }

    private void initializeComponentsCreate() {
        EditText textField = dialog.findViewById(R.id.input_channel_create);
        EditText description = dialog.findViewById(R.id.channel_description);
        Button backButton = dialog.findViewById(R.id.dialog_button_back);
        Button okButton = dialog.findViewById(R.id.dialog_button_ok);

        backButton.setOnClickListener(v -> {
            dialog.dismiss();
            addButton.setEnabled(true);
        });
        okButton.setOnClickListener(v -> {
            String enteredName = textField.getText().toString();
            String enteredDescription = description.getText().toString();
            Channel channel = new Channel();
            channel.setName(enteredName);
            channel.setDescription(enteredDescription);
            String savedLogin = SharedPrefsUtil.getUserLogin(this);
            channel.setLogin(savedLogin);
            userApi.saveChannel(channel)
                    .enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if (response.isSuccessful()) {
                                Boolean result = response.body();
                                if (result != null && result) {
                                    Toast.makeText(AddChannelActivity.this, "Channel created", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(AddChannelActivity.this, "This channel already exists", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(AddChannelActivity.this, "Failed to create channel", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            Toast.makeText(AddChannelActivity.this, "Server not responding", Toast.LENGTH_SHORT).show();
                            Logger.getLogger(SignUpActivity.class.getName()).log(Level.SEVERE, "error occured", t);
                        }
                    });

            dialog.dismiss();
            addButton.setEnabled(true);

        });
        addButton.setEnabled(false);
    }
    private void initializeComponentsEnter() {
        EditText textField = dialog.findViewById(R.id.enter_the_channel);
        Button backButton = dialog.findViewById(R.id.dialog_enter_button_back);
        Button okButton = dialog.findViewById(R.id.dialog_enter_button_ok);

        backButton.setOnClickListener(v -> {
            dialog.dismiss();
            enterButton.setEnabled(true);
        });
        okButton.setOnClickListener(v -> {
            String enteredName = textField.getText().toString();
            Channel channel = new Channel();
            channel.setName(enteredName);
            String savedLogin = SharedPrefsUtil.getUserLogin(this);
            channel.setLogin(savedLogin);
            userApi.enterChannel(channel)
                    .enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if (response.isSuccessful()) {
                                Boolean result = response.body();
                                if (result != null && result) {
                                    Toast.makeText(AddChannelActivity.this, "You have joined the channel", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(AddChannelActivity.this, "This channel does not exists", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(AddChannelActivity.this, "Failed to join channel", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            Toast.makeText(AddChannelActivity.this, "Server not responding", Toast.LENGTH_SHORT).show();
                            Logger.getLogger(SignUpActivity.class.getName()).log(Level.SEVERE, "error occured", t);
                        }
                    });
            dialog.dismiss();
            enterButton.setEnabled(true);

        });
        enterButton.setEnabled(false);
    }

    private void SetListeners(){
        binding.btnCloseDialog.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), ChannelsActivity.class)));
    }
}


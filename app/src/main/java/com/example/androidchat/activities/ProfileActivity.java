package com.example.androidchat.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidchat.R;
import com.example.androidchat.SharedPrefsUtil;
import com.example.androidchat.databinding.ActivityProfileBinding;
import com.example.androidchat.databinding.ActivitySignInBinding;
import com.example.androidchat.databinding.ActivitySignUpBinding;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        initializeComponents();

    }

    private void setListeners(){
        binding.ButtonSignOut.setOnClickListener(v-> signOut());
        binding.imageBack.setOnClickListener(v->startActivity(new Intent(getApplicationContext(), ChannelsActivity.class)));
    }

    private void initializeComponents(){
        TextView fullName = findViewById(R.id.full_name);
        //fullName.setText(SharedPrefsUtil.getUserName(this));
        TextView userEmail = findViewById(R.id.userEmail);
        userEmail.setText(SharedPrefsUtil.getUserLogin(this));

    }

    private void signOut(){
        SharedPrefsUtil.saveChannelDescr(this, "");
        SharedPrefsUtil.saveChannelName(this, "");
        SharedPrefsUtil.saveUserLogin(this, "");
        SharedPrefsUtil.saveUserPassword(this, "");
        //SharedPrefsUtil.saveUserName(this, "");
        startActivity(new Intent(getApplicationContext(), SignInActivity.class));
    }
}

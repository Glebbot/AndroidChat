package com.example.androidchat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidchat.R;
import com.example.androidchat.databinding.ActivitySignInBinding;
import com.example.androidchat.databinding.ActivitySignUpBinding;
import com.example.androidchat.model.User;
import com.example.androidchat.retrofit.RetrofitService;
import com.example.androidchat.retrofit.UserApi;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SetListeners();
        initializeComponents();
    }

    private void initializeComponents()
    {
        EditText inputEditEmail = findViewById(R.id.inputEmail);
        EditText inputEditName = findViewById(R.id.inputName);
        EditText inputEditPassword = findViewById(R.id.inputPassword);
        EditText inputEditConfirmPassword = findViewById(R.id.inputConfirmPassword);
        Button buttonSave = findViewById(R.id.ButtonSignUp);

        RetrofitService retrofitService = new RetrofitService();
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);
        buttonSave.setOnClickListener(view -> {
            String name = String.valueOf(inputEditName.getText());
            String email = String.valueOf(inputEditEmail.getText());
            String password = String.valueOf(inputEditPassword.getText());
            String confirmPassword = String.valueOf(inputEditConfirmPassword.getText());

            User user = new User();
            user.setFull_name(name);
            user.setEmail(email);
            user.setPassword(password);
            userApi.save(user)
                    .enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            Toast.makeText(SignUpActivity.this, "Sign up finished", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(SignUpActivity.this, "Sign up failed", Toast.LENGTH_SHORT).show();
                            Logger.getLogger(SignUpActivity.class.getName()).log(Level.SEVERE, "error occured", t);
                        }
                    });
        });
    }

    private void SetListeners() {
        binding.SignIn.setOnClickListener(v -> onBackPressed());
    }
}
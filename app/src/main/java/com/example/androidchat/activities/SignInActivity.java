package com.example.androidchat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidchat.R;
import com.example.androidchat.SharedPrefsUtil;
import com.example.androidchat.databinding.ActivitySignInBinding;
import com.example.androidchat.model.LoginUser;
import com.example.androidchat.retrofit.RetrofitService;
import com.example.androidchat.retrofit.UserApi;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {
    private ActivitySignInBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SetListeners();
        initializeComponents();
    }

    private void initializeComponents() {
        EditText inputEditEmail = findViewById(R.id.inputEmail);
        EditText inputEditPassword = findViewById(R.id.inputPassword);
        Button buttonSignIn = findViewById(R.id.ButtonSignIn);

        RetrofitService retrofitService = new RetrofitService();
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);
        buttonSignIn.setOnClickListener(view -> {
            String email = String.valueOf(inputEditEmail.getText());
            String password = String.valueOf(inputEditPassword.getText());

            LoginUser loginUser = new LoginUser();
            loginUser.setEmail(email);
            loginUser.setPassword(password);
            userApi.saveLogin(loginUser)
                    .enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if (response.isSuccessful()) {
                                Boolean result = response.body();
                                if (result != null && result) {
                                    Toast.makeText(SignInActivity.this, "Sign in success", Toast.LENGTH_SHORT).show();
                                    SharedPrefsUtil.saveUserLogin(SignInActivity.this, email);
                                    SharedPrefsUtil.saveUserPassword(SignInActivity.this, password);
                                    startActivity(new Intent(getApplicationContext(), ChannelsActivity.class));
                                }
                                else {
                                    Toast.makeText(SignInActivity.this, "Incorrect login or password", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(SignInActivity.this, "Sign in failed", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            Toast.makeText(SignInActivity.this, "Server not responding", Toast.LENGTH_SHORT).show();
                            Logger.getLogger(SignUpActivity.class.getName()).log(Level.SEVERE, "error occured", t);
                        }
                    });
        });
    }

    private void SetListeners() {
        binding.CreateNewAccount.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));
    }
}
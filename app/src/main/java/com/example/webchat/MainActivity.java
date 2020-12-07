package com.example.webchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText loginEditText = findViewById(R.id.login);
        EditText passwordEditText = findViewById(R.id.password);

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> {
            new LoginTask().execute(loginEditText.getText().toString(), passwordEditText.getText().toString());
        });

        Button registrationButton = findViewById(R.id.registration);
        registrationButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
        });

    }

    private class LoginTask extends AsyncTask<String, Void, User>{

        @Override
        protected User doInBackground(String... strings) {
            User user = null;

            try {
                socket = new Socket(getString(R.string.server_addr), 8888);
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(new Message(Methods.METHOD_LOGIN, strings[0], strings[1]));

                Log.i("Arguments", "Login: " + strings[0]);
                Log.i("Arguments", "Password: " + strings[1]);

                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                Message inputMessage = (Message)inputStream.readObject();

                user = inputMessage.getLoginedUser();

                outputStream.close();
                inputStream.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }


            return user;
        }

        @Override
        protected void onPostExecute(User user) {
            if(user == null)
                Toast.makeText(MainActivity.this, "Invalid login or password", Toast.LENGTH_LONG).show();
            else {
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra("EXTRA_NICKNAME", user.getNickname());
                Log.i("Arguments", "Nickname: " + user.getNickname());
                startActivity(intent);
            }
        }
    }

}
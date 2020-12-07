package com.example.webchat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RegistrationActivity extends AppCompatActivity {

    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        EditText loginEditText = findViewById(R.id.login);
        EditText passwordEditText = findViewById(R.id.password);
        EditText nicknameEditText = findViewById(R.id.nickname);

        Button cancelButton = findViewById(R.id.cancel);
        cancelButton.setOnClickListener(v -> {
            finish();
        });

        Button okButton = findViewById(R.id.ok);
        okButton.setOnClickListener(v -> {
            new RegistrationTask().execute(
                    loginEditText.getText().toString(),
                    passwordEditText.getText().toString(),
                    nicknameEditText.getText().toString());
        });

    }

    private class RegistrationTask extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... strings) {
            try {
                socket = new Socket(getString(R.string.server_addr), 8888);

                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(new Message(Methods.METHOD_REGISTRATION, strings[0], strings[1], strings[2]));
                outputStream.close();
                finish();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
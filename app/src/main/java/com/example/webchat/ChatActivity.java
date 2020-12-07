package com.example.webchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private Long fromId;
    private String author;
    private ChatAdapter adapter;
    private EditText message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        fromId = 1L;
        author = getIntent().getStringExtra("EXTRA_NICKNAME");

        RecyclerView chatWindow = findViewById(R.id.chat_window);

        chatWindow.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        adapter = new ChatAdapter(author);
        chatWindow.setAdapter(adapter);

        message = findViewById(R.id.chat_textbox);

        new Thread(new MessagesUpdater()).start();

        Button sendButton = findViewById(R.id.send);
        sendButton.setOnClickListener(v -> {
            new SendMessageTask().execute(new ChatMessage(author, message.getText().toString()));
        });
    }

    private class MessagesUpdater implements Runnable{

        @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()){

                try {
                    Socket socket = new Socket(getString(R.string.server_addr), 8888);

                    ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                    outputStream.writeObject(new Message(Methods.METHOD_GET_MESSAGES, fromId));

                    ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                    Message inputMessage = (Message) inputStream.readObject();
                    MessagesStack.addMessages(inputMessage.getNewMessages());

                    if (inputMessage.getNewMessages().size() > 0){
                        fromId = inputMessage.getNewMessages().get(inputMessage.getNewMessages().size() - 1).getId();

                        runOnUiThread(() -> {
                            adapter.notifyDataSetChanged();
                        });
                    }


                    outputStream.close();
                    inputStream.close();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
    
    private class SendMessageTask extends AsyncTask<ChatMessage, Void, Void>{

        @Override
        protected Void doInBackground(ChatMessage... chatMessages) {

            try {
                Socket socket = new Socket(getString(R.string.server_addr), 8888);

                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(
                        new Message(Methods.METHOD_SEND_MESSAGE,
                                new ChatMessage(chatMessages[0].getAuthor(), chatMessages[0].getText())));

                runOnUiThread(() -> {
                    message.setText("");
                });

                outputStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }
}
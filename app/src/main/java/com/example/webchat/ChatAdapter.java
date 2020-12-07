package com.example.webchat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private class MessageViewHolder extends RecyclerView.ViewHolder{

        private TextView author;
        private TextView message;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.author);
            message = itemView.findViewById(R.id.message);
        }
    }

    private String author;

    public ChatAdapter(String author) {
        this.author = author;
    }

    @Override
    public int getItemViewType(int position) {
        if (author.equals(MessagesStack.getAll().get(position).getAuthor()))
            return 1;
        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = new View(parent.getContext());
        if (viewType == 0)
            view = inflater.inflate(R.layout.message_layout, parent, false);
        else if (viewType == 1)
            view = inflater.inflate(R.layout.your_message_layout, parent, false);

        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = MessagesStack.getAll().get(position);

        ((MessageViewHolder)holder).author.setText(message.getAuthor());
        ((MessageViewHolder)holder).message.setText(message.getText());
    }

    @Override
    public int getItemCount() {
        return MessagesStack.getAll().size();
    }
}

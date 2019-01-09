package com.gr03.amos.bikerapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gr03.amos.bikerapp.Models.Message;
import com.gr03.amos.bikerapp.R;
import com.gr03.amos.bikerapp.SaveSharedPreference;

import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;

public class MessageListRecyclerViewAdapter extends RealmRecyclerViewAdapter<Message, MessageListRecyclerViewAdapter.ViewHolder> {
    private RealmResults<Message> mData;
    private LayoutInflater mInflater;
    private Context context;

    // data is passed into the constructor
    public MessageListRecyclerViewAdapter(Context context, RealmResults<Message> data) {
        super(data, true);
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.chat_list_row, parent, false);
        return new MessageListRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mData.get(position).getId_user() == SaveSharedPreference.getUserID(context)) {
            holder.user.setVisibility(View.VISIBLE);
            holder.user.setText(mData.get(position).getMessage());
        } else {
            holder.friend.setVisibility(View.VISIBLE);
            holder.friend.setText(mData.get(position).getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView user;
        TextView friend;

        ViewHolder(View itemView) {
            super(itemView);
            user = itemView.findViewById(R.id.user_chat);
            friend = itemView.findViewById(R.id.friend_chat);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }
    }
}

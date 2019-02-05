package com.gr03.amos.bikerapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gr03.amos.bikerapp.ChatActivity;
import com.gr03.amos.bikerapp.Models.Chat;
import com.gr03.amos.bikerapp.Models.Friend;
import com.gr03.amos.bikerapp.Models.Message;
import com.gr03.amos.bikerapp.NetworkLayer.ResponseHandler;
import com.gr03.amos.bikerapp.R;
import com.gr03.amos.bikerapp.NetworkLayer.Requests;
import com.gr03.amos.bikerapp.SaveSharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.Sort;

public class ShowConversationWithFriendsListRecyclerViewAdapter extends RealmRecyclerViewAdapter<Chat, ShowConversationWithFriendsListRecyclerViewAdapter.ViewHolder> {
    private RealmResults<Chat> mData;
    private LayoutInflater mInflater;
    private Context context;

    // data is passed into the constructor
    public ShowConversationWithFriendsListRecyclerViewAdapter(Context context, RealmResults<Chat> data) {
        super(data, true);
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.show_my_conversation_list, parent, false);
        return new ShowConversationWithFriendsListRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Realm.init(context);
        Realm realm = Realm.getDefaultInstance();
        Chat chat = realm.where(Chat.class).equalTo("id_chat", mData.get(position).getId_chat()).findFirst();

        try {
            holder.friendName.setText(chat.getTitle());
            holder.timeStamp.setText(chat.getLast_send().toString());
            holder.lastMessage.setText(chat.getLast_message());

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, ChatActivity.class);
                ArrayList<Integer> participants = new ArrayList<>();
                participants.addAll(chat.getParticipants());
                intent.putExtra("chatUser", participants);
                intent.putExtra("id_chat", chat.getId_chat());
                Log.i("this chat", String.valueOf(participants));
                context.startActivity(intent);
            });
        } catch (Exception e) {
            Log.w("ShowConversationAdapter", e.toString());
            holder.friendName.setText("NA");
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    private int loadChat(ArrayList<Integer> chatUser) {
        JSONObject request = new JSONObject();

        try {
            JSONArray jsonUserIds = new JSONArray();
            for (int userId : chatUser) {
                jsonUserIds.put(userId);
            }
            request.put("id_users", jsonUserIds);

            JSONObject response = Requests.getResponse("loadChat", request, "PUT", context);

            if (response.has("id_chat")) {
                return response.getInt("id_chat");
            } else {
                Toast.makeText(context, "Error loading chat", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView friendName;
        TextView timeStamp;
        TextView lastMessage;

        ViewHolder(View itemView) {
            super(itemView);
            friendName = itemView.findViewById(R.id.friend_name);
            timeStamp = itemView.findViewById(R.id.last_message_time);
            lastMessage = itemView.findViewById(R.id.last_message);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }
    }

}

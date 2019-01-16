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
import com.gr03.amos.bikerapp.Models.Friend;
import com.gr03.amos.bikerapp.Models.Message;
import com.gr03.amos.bikerapp.R;
import com.gr03.amos.bikerapp.Requests;
import com.gr03.amos.bikerapp.SaveSharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.Sort;

public class ShowConversationWithFriendsListRecyclerViewAdapter extends RealmRecyclerViewAdapter<Friend, ShowConversationWithFriendsListRecyclerViewAdapter.ViewHolder> {
    private RealmResults<Friend> mData;
    private LayoutInflater mInflater;
    private Context context;

    // data is passed into the constructor
    public ShowConversationWithFriendsListRecyclerViewAdapter(Context context, RealmResults<Friend> data) {
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
        ArrayList<Integer> chatUser = new ArrayList<>();
        chatUser.add(SaveSharedPreference.getUserID(context));
        chatUser.add(Math.toIntExact(mData.get(position).getId()));
        int chatId = loadChat(chatUser);
        Log.i("before chat", String.valueOf(chatId));
        Realm.init(context);
        Realm realm = Realm.getDefaultInstance();
        Message message = realm.where(Message.class).equalTo("id_chat", chatId).sort("time_created", Sort.ASCENDING).findFirst();

        try {
            holder.friendName.setText(mData.get(position).getFirst_name());
            holder.timeStamp.setText(message.getTime_created());
            holder.lastMessage.setText(message.getMessage());

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("chatUser", chatUser);
                Log.i("this chat", String.valueOf(chatUser));
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

            JSONObject response = Requests.getJSONResponse("loadChat", request, "PUT");

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

package com.example.admin.chatapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Chat_Room extends AppCompatActivity {

    private Button sendmsg;
    private EditText inputmsg;
    private TextView chatconversation;

    private String username,roomname;
    private DatabaseReference root ;
    private String tempkey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat__room);

        sendmsg = (Button) findViewById(R.id.btn_send);
        inputmsg = (EditText) findViewById(R.id.msg_input);
        chatconversation = (TextView) findViewById(R.id.textView);

        username = getIntent().getExtras().get("user_name").toString();
        roomname = getIntent().getExtras().get("room_name").toString();
        setTitle(" Room - " + roomname);

        root = FirebaseDatabase.getInstance().getReference().child(roomname);

        sendmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, Object> map = new HashMap<String, Object>();
                tempkey = root.push().getKey();
                root.updateChildren(map);

                DatabaseReference message_root = root.child(tempkey);
                Map<String, Object> map2 = new HashMap<String, Object>();
                map2.put("name", username);
                map2.put("msg", inputmsg.getText().toString());

                message_root.updateChildren(map2);
            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                append_chat_conversation(dataSnapshot);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
        private String chat_msg,chat_user_name;

        private void append_chat_conversation(DataSnapshot dataSnapshot) {

        Iterator i = dataSnapshot.getChildren().iterator();

        while (i.hasNext()){

            chat_msg = (String) ((DataSnapshot)i.next()).getValue();
            chat_user_name = (String) ((DataSnapshot)i.next()).getValue();

            chatconversation.append(chat_user_name +" : "+chat_msg +" \n");
        }




    }
}

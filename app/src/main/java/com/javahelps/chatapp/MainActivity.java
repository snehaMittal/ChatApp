package com.javahelps.chatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private int SIGN_IN_REQUEST_CODE = 1 ;
    RelativeLayout activityMain ;
    FloatingActionButton fab ;
    ListView listView;
    FirebaseListAdapter<ChatMessage> adapter;
    ArrayList<String> list;
    EditText editText;;

    HashMap<String, Integer> hashMap;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_sign_out){
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Snackbar.make(activityMain , "You have been sign out" , Snackbar.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
        return true ;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu , menu);
        return true ;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_REQUEST_CODE){
            Snackbar.make(activityMain , "Successfully Signed In . Welcome !" , Snackbar.LENGTH_SHORT).show();
            displayChatMessages();
        }
        else {
            Snackbar.make(activityMain , "We couldn't sign you in . Please try again later" , Snackbar.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activityMain = (RelativeLayout)findViewById(R.id.activity_main);
        fab = (FloatingActionButton)findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText)findViewById(R.id.input);
                FirebaseDatabase.getInstance().getReference().push().setValue(new ChatMessage(input.getText().toString() ,
                        FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                input.setText("");
                displayChatMessages();
            }
        });
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build() , SIGN_IN_REQUEST_CODE);
        }
        else {
            Snackbar.make(activityMain , "Welcome " + FirebaseAuth.getInstance().getCurrentUser().getEmail() ,
                    Snackbar.LENGTH_SHORT).show();
            displayChatMessages();
        }

    }

    private void displayChatMessages() {

        ListView listOfMessages = (ListView)findViewById(R.id.list_of_messages);
        Query query = FirebaseDatabase.getInstance().getReference();
        FirebaseListOptions<ChatMessage> options = new FirebaseListOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage.class)
                .setLayout(R.layout.list_item)
                .build();
        adapter = new FirebaseListAdapter<ChatMessage>(options) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                TextView messageText , messageUser , messageTime ;
                messageText = (TextView)v.findViewById(R.id.messge_text);
                messageUser = (TextView)v.findViewById(R.id.messge_user);
                messageTime = (TextView)v.findViewById(R.id.messge_time);

                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)" , model.getMessagetime()));
            }
        };
        listOfMessages.setAdapter(adapter);
    }
}

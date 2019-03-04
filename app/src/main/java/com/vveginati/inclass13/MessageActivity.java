package com.vveginati.inclass13;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.vveginati.inclass13.adapter.MessageAdapter;
import com.vveginati.inclass13.beans.Mails;
import com.vveginati.inclass13.util.BaseUtil;

import java.util.ArrayList;
import java.util.Collections;

public class MessageActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    ArrayList<Mails> mailsArrayList;
    ListView listView_mails;
    public static String KEY_INTENT_READMSG="KEY_INTENT_READMSG";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        setTitle(R.string.inbox);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        listView_mails=findViewById(R.id.listView_mails);


        listView_mails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Mails selectedMail = mailsArrayList.get(i);
                BaseUtil.getmThreadRef().child(selectedMail.getMsgkey()).child(BaseUtil.MESSAGE_READ).setValue(Boolean.TRUE);
                Intent intent = new Intent(MessageActivity.this,ReadmsgActivity.class);
                intent.putExtra(KEY_INTENT_READMSG,selectedMail);
                startActivity(intent);
            }
        });

        BaseUtil.getmThreadRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mailsArrayList = new ArrayList<>();
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot child : children) {
                    Mails value = child.getValue(Mails.class);
                    mailsArrayList.add(value);
                }

                Collections.reverse(mailsArrayList);
                setMailsAdapter(mailsArrayList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void setMailsAdapter(ArrayList<Mails> mails){
            MessageAdapter adapter = new MessageAdapter(this,R.layout.adapter_mails_list,mails);
            listView_mails.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_newmail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_new_mail:
                Intent intent_compose = new Intent(MessageActivity.this,ComposeMsgActivity.class);
                startActivity(intent_compose);
                return true;
            case R.id.item_logout:
                BaseUtil.getFirebaseAuth().signOut();
                Intent intent_logout = new Intent(this, MainActivity.class);
                startActivity(intent_logout);
                finishAffinity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}

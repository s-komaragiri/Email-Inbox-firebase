package com.vveginati.inclass13;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.vveginati.inclass13.beans.Mails;
import com.vveginati.inclass13.util.BaseUtil;

public class ReadmsgActivity extends AppCompatActivity {

    private TextView textView_senderName;
    private TextView textView_detailedMsg;
    private String msgkey;
    private Mails selectedMail;
    public static String KEY_INTENT_COMPOSE_REPLY="KEY_INTENT_COMPOSE_REPLY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readmsg);
        setTitle(R.string.readMsg);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        this.selectedMail = (Mails) getIntent().getExtras().getSerializable(MessageActivity.KEY_INTENT_READMSG);
        this.msgkey=selectedMail.getMsgkey();

        textView_detailedMsg=findViewById(R.id.textView_detailedMsg);
        textView_senderName=findViewById(R.id.textView_read_senderName);

        textView_senderName.setText(selectedMail.getSenderName());
        textView_detailedMsg.setText(selectedMail.getDetailedMessage());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_reply:
                composereply();
                return true;
            case R.id.item_deleteMail:
deleteMail();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void deleteMail(){
        BaseUtil.getmThreadRef().child(msgkey).removeValue();
        finish();
    }

    public void composereply(){
        Intent intent = new Intent(ReadmsgActivity.this,ComposeMsgActivity.class);
        intent.putExtra(KEY_INTENT_COMPOSE_REPLY,selectedMail);
        startActivity(intent);
        finish();

    }
}

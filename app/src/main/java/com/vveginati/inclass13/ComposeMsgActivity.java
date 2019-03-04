package com.vveginati.inclass13;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.vveginati.inclass13.beans.Mails;
import com.vveginati.inclass13.beans.User;
import com.vveginati.inclass13.util.BaseUtil;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ComposeMsgActivity extends AppCompatActivity {

    String recipient;
    String sender;
    String key=null;
    Boolean isReply = Boolean.FALSE;
    private TextView textView_receipient;
    private EditText editText_compose_msg;
    private ImageButton imageButton_chooseRecipient;
    private Button button_send;
    private ArrayList<String> recipent_list;
    private ArrayList<String> usersKey;
    private AlertDialog.Builder alertDialog;
    Mails selectedMail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_msg);
        setTitle(R.string.cmpMessage);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        sender=BaseUtil.getFirebaseAuth().getCurrentUser().getDisplayName();

        if (getIntent().getExtras() != null) {

            selectedMail = ( Mails) getIntent().getExtras().getSerializable(ReadmsgActivity.KEY_INTENT_COMPOSE_REPLY);
            recipient=selectedMail.getSenderName();
            key=selectedMail.getSentkey();
            isReply = Boolean.TRUE;

        }

        textView_receipient = findViewById(R.id.textView_compose_recipient);
        editText_compose_msg = findViewById(R.id.editText_mailContent);
        imageButton_chooseRecipient = findViewById(R.id.imageButton_chooseRecipient);
        button_send = findViewById(R.id.button_sendMail);

        if (isReply) {
            textView_receipient.setText(recipient);
            imageButton_chooseRecipient.setEnabled(Boolean.FALSE);
        }

        imageButton_chooseRecipient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    BaseUtil.getmDatabase().child(BaseUtil.DB_NAME_USERS).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            recipent_list=new ArrayList<>();
                            usersKey=new ArrayList<>();
                            Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                            for (DataSnapshot child : children) {
                                User value = child.getValue(User.class);
                                recipent_list.add(value.getFirstName() + " " + value.getLastName());
                                usersKey.add(value.getUser_key());
                            }

                            final String str[] = recipent_list.toArray(new String[recipent_list.size()]);

                            alertDialog = new AlertDialog.Builder(ComposeMsgActivity.this);

                            alertDialog.setTitle(R.string.usersList).
                                    setItems(str, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            ComposeMsgActivity.this.recipient = recipent_list.get(i);
                                            ComposeMsgActivity.this.key = usersKey.get(i);
                                            textView_receipient.setText(ComposeMsgActivity.this.recipient);
                                        }
                                    });

                            alertDialog.create();
                            alertDialog.show();


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (key != null) {
                    String msg = editText_compose_msg.getText().toString();

                    Map<String,Object> timestamp = new HashMap<String,Object>();
                    timestamp.put("timestamp", ServerValue.TIMESTAMP);


                    Mails mails = new Mails(ComposeMsgActivity.this.sender, msg, Boolean.FALSE);
                    String msgKey = BaseUtil.getmDatabase().child(BaseUtil.DB_NAME_MESSAGES).child(key).push().getKey();
                    String uid=BaseUtil.getFirebaseAuth().getCurrentUser().getUid();
                    mails.setMsgkey(msgKey);
                    mails.setSentkey(uid);
                    mails.setMailTimeStamp(timestamp);

                    BaseUtil.getmDatabase().child(BaseUtil.DB_NAME_MESSAGES).child(key).child(mails.getMsgkey()).setValue(mails);
                    Toast.makeText(ComposeMsgActivity.this, R.string.sentToast, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ComposeMsgActivity.this, R.string.contact_blank, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}

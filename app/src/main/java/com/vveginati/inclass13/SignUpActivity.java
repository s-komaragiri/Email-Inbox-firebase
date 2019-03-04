package com.vveginati.inclass13;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vveginati.inclass13.beans.User;
import com.vveginati.inclass13.util.BaseUtil;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = SignUpActivity.class.getName();
    public static final String USERNAME = "USERNAME";
    public static final String USERS_KEY = "USERS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTitle(R.string.msg_signup);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final EditText firstName = findViewById(R.id.editText_firstname);
        final EditText lastName = findViewById(R.id.editText_lastName);
        final EditText emailId = findViewById(R.id.editText_email);
        final EditText password = findViewById(R.id.editText_password);
        final EditText repeatPassword = findViewById(R.id.editText_repeatPassword);

        Button button_signup = findViewById(R.id.button_signup);
        Button button_cancel = findViewById(R.id.button_cancel);

        button_cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(firstName.getText().length()>0 &&lastName.getText().length()>0 &&emailId.getText().toString().length()>0&&password.getText().toString().length()>0&&repeatPassword.getText().toString().length()>0) {
                    if (password.getText().toString().equals(repeatPassword.getText().toString())) {
                        performSignUp(firstName.getText().toString(), lastName.getText().toString(), password.getText().toString(), emailId.getText().toString());

                    }else{
                        Toast.makeText(SignUpActivity.this,R.string.password_confirm_password_message , Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SignUpActivity.this,R.string.emaill_password_message , Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void performSignUp(final String firstName, final String lastName, String password, final String emailId) {

        BaseUtil.getFirebaseAuth().createUserWithEmailAndPassword(emailId,password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                   // Toast.makeText(SignUpActivity.this, "Successful", Toast.LENGTH_SHORT).show();

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(firstName +" "+lastName)
                            .build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        //String userKey = BaseUtil.getmDatabase().push().getKey();
                                        User userObj =new User(firstName,lastName,emailId);
                                        userObj.setUser_key(BaseUtil.getFirebaseAuth().getCurrentUser().getUid());

                                        BaseUtil.getmDatabase().child(BaseUtil.DB_NAME_USERS).child(userObj.getUser_key()).setValue(userObj).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                if(task.isSuccessful()){
                                                    Intent messageActivity = new Intent(SignUpActivity.this,MessageActivity.class);
                                                    startActivity(messageActivity);
                                                    finish();
                                                }
                                            }
                                        });

                                        Log.d("Demo", "User profile updated.");
                                    }
                                }
                            });
                }else{
                    Log.d(TAG,task.getException().toString());
                    Toast.makeText(SignUpActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

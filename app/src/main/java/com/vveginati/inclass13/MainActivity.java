package com.vveginati.inclass13;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vveginati.inclass13.util.BaseUtil;

public class MainActivity extends AppCompatActivity {


    FirebaseAuth.AuthStateListener mAuthListener;
    EditText editTextUserName;
    EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.login_activity);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);


        if(FirebaseAuth.getInstance().getCurrentUser() != null){

            Intent threadActivity = new Intent(MainActivity.this, MessageActivity.class);
            startActivity(threadActivity);
            finish();
            return;
        }


        setTitle(R.string.login_activity);
        setContentView(R.layout.activity_main);

        editTextUserName = findViewById(R.id.editText_email);
        editTextPassword = findViewById(R.id.editText_password);

        findViewById(R.id.button_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewview) {

                String userName = editTextUserName.getText().toString();
                String password = editTextPassword.getText().toString();

                if(null!= userName&& null!=password && userName.length()>0 && password.length()>0){
                    validateLogin(userName, password);
                }
            }
        });

        findViewById(R.id.button_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent threadActivity = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(threadActivity);
                finish();
            }
        });
    }

    public void validateLogin(String uname, String password) {
        BaseUtil.getFirebaseAuth().signInWithEmailAndPassword(uname, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String uid=task.getResult().getUser().getUid();
                    Intent intent = new Intent(MainActivity.this, MessageActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(MainActivity.this, R.string.login_unsuccessful, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}

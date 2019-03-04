package com.vveginati.inclass13.util;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BaseUtil {

    public static final String DB_NAME_MESSAGES="MESSAGES";
    public static final String DB_NAME_USERS="USERS";

    public static final String MESSAGE_READ="read";

    public static FirebaseAuth getFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    public static DatabaseReference getmDatabase() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public static DatabaseReference getmThreadRef() {
        String currentUID = BaseUtil.getFirebaseAuth().getCurrentUser().getUid();
        return FirebaseDatabase.getInstance().getReference().child(BaseUtil.DB_NAME_MESSAGES).child(currentUID);
    }
}

package developers.bmsce.mank.com.foodorder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rey.material.widget.CheckBox;


import developers.bmsce.mank.com.foodorder.Common.Common;
import developers.bmsce.mank.com.foodorder.Model.User;
import io.paperdb.Paper;

import static android.support.constraint.Constraints.TAG;
public class SigninActivity extends AppCompatActivity {

    EditText et_phone,et_name, et_password;

    //add Firebase Database stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    CheckBox ckbRemmeber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);


        ckbRemmeber = (CheckBox)findViewById(R.id.ckbremember);

        Paper.init(this);

        et_phone = findViewById(R.id.et_phone);
        et_password = findViewById(R.id.et_password);
        Button btnsubmit = findViewById(R.id.btn_submit);


        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("user");
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ckbRemmeber.isChecked()) {

                    Paper.book().write(Common.USER_KEY, et_phone.getText().toString());
                    Paper.book().write((Common.PWD_KEY), et_password.getText().toString());

                }

                final ProgressDialog progressDialog = new ProgressDialog(SigninActivity.this);
                progressDialog.setMessage("Please wait");
                progressDialog.show();
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.

                     if(dataSnapshot.child(et_phone.getText().toString()).exists()) {
                         progressDialog.dismiss();
                         User user = dataSnapshot.child(et_phone.getText().toString()).getValue(User.class);
                         user.setPhone(et_phone.getText().toString());
                         if (user.getPassword().equals(et_password.getText().toString())) {


                             Intent intent = new Intent(SigninActivity.this, Home.class);
                             Common.currentUser = user;
                             startActivity(intent);
                             finish();


                             Toast.makeText(SigninActivity.this, "sign In Succsfull", Toast.LENGTH_SHORT).show();
                         } else {

                             Toast.makeText(SigninActivity.this, "Sign in failed", Toast.LENGTH_SHORT).show();
                         }
//                        Object value = dataSnapshot.getValue();
//                        Log.d(TAG, "Value is: " + value);
                     }else {

                         Toast.makeText(SigninActivity.this,"User not exist",Toast.LENGTH_SHORT).show();
                     }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });
            }
        });

    }
}

package developers.bmsce.mank.com.foodorder;

import android.app.ProgressDialog;
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

import developers.bmsce.mank.com.foodorder.Model.User;

import static android.support.constraint.Constraints.TAG;

public class SigneUpActivity extends AppCompatActivity {


    EditText et_phone,et_name, et_password;

    //add Firebase Database stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signe_up);

        et_name = findViewById(R.id.et_sname);
        et_phone = findViewById(R.id.et_sphone);
        et_password = findViewById(R.id.et_spassword);
        Button btnsubmit = findViewById(R.id.btn_ssubmit);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("user");
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(SigneUpActivity.this);
                progressDialog.setMessage("Please wait");
                progressDialog.show();
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.

                        if(dataSnapshot.child(et_phone.getText().toString()).exists()) {
                            progressDialog.dismiss();



                            Toast.makeText(SigneUpActivity.this, "phone Number already registered", Toast.LENGTH_SHORT).show();

//                            User user = dataSnapshot.child(et_phone.getText().toString()).getValue(User.class);
//                            if (user.getPassword().equals(et_password.getText().toString())) {
//                                Toast.makeText(SigneUpActivity.this, "sign In Succsfull", Toast.LENGTH_SHORT).show();
//                            } else {
//
//                                Toast.makeText(SigneUpActivity.this, "Sign in failed", Toast.LENGTH_SHORT).show();
//                            }
////                        Object value = dataSnapshot.getValue();
////                        Log.d(TAG, "Value is: " + value);
                        }else {
                            progressDialog.dismiss();
                            User user =new User(et_name.getText().toString(),et_password.getText().toString());
                            myRef.child(et_phone.getText().toString()).setValue(user);

                            Toast.makeText(SigneUpActivity.this,"Signe Up Succesfull",Toast.LENGTH_SHORT).show();

                            finish();
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

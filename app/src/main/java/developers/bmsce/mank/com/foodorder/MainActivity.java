package developers.bmsce.mank.com.foodorder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import developers.bmsce.mank.com.foodorder.Common.Common;
import developers.bmsce.mank.com.foodorder.Model.User;
import io.paperdb.Paper;

import static android.support.constraint.Constraints.TAG;

public class MainActivity extends AppCompatActivity {



    Button signeup, signin;

    //add Firebase Database stuff
    private FirebaseDatabase mFirebaseDatabase;

    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Paper.init(this);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("user");

        signeup = findViewById(R.id.btn_signup);
        signin = findViewById(R.id.btn_sigin);




        signeup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), SigneUpActivity.class);
                startActivity(intent);


            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
                startActivity(intent);

            }
        });

        String phone = Paper.book().read(Common.USER_KEY);
        String pwd = Paper.book().read(Common.PWD_KEY);

        if (phone !=null && pwd !=null) {

            login(phone, pwd);


        }
    }



    private void login(final String phone, final String pwd) {

        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                if(dataSnapshot.child(phone).exists()) {
                    progressDialog.dismiss();
                    User user = dataSnapshot.child(phone).getValue(User.class);
                    user.setPhone(phone);
                    if (user.getPassword().equals(pwd)) {


                        Intent intent = new Intent(MainActivity.this, Home.class);
                        Common.currentUser = user;
                        startActivity(intent);
                        finish();


                        Toast.makeText(MainActivity.this, "sign In Succsfull", Toast.LENGTH_SHORT).show();
                    } else {

                        Toast.makeText(MainActivity.this, "Sign in failed", Toast.LENGTH_SHORT).show();
                    }
//                        Object value = dataSnapshot.getValue();
//                        Log.d(TAG, "Value is: " + value);
                }else {

                    Toast.makeText(MainActivity.this,"User not exist",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


    }
}

package developers.bmsce.mank.com.foodorder;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.Base64Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import developers.bmsce.mank.com.foodorder.Common.Common;
import developers.bmsce.mank.com.foodorder.Database.Database;
import developers.bmsce.mank.com.foodorder.Model.MyResponse;
import developers.bmsce.mank.com.foodorder.Model.Notification;
import developers.bmsce.mank.com.foodorder.Model.Order;
import developers.bmsce.mank.com.foodorder.Model.Request;
import developers.bmsce.mank.com.foodorder.Model.Sender;
import developers.bmsce.mank.com.foodorder.Model.Token;
import developers.bmsce.mank.com.foodorder.Remote.APIService;
import developers.bmsce.mank.com.foodorder.ViewHolder.CartAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Cart extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference requests;

    RecyclerView recycler_cart;
    RecyclerView.LayoutManager layoutManager;

    TextView textTotalPrice;
    Button btn_placeorder;

    List<Order> cart = new ArrayList<>();

    APIService mService;
    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        textTotalPrice = findViewById(R.id.total);
        btn_placeorder = findViewById(R.id.btnPlaceOrder);

        //Auth
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");




        recycler_cart = findViewById(R.id.listcart);
        recycler_cart.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_cart.setLayoutManager(layoutManager);


        loadListFood();


        btn_placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cart.size() > 0) {
                    showAlertDialog();
                }else {

                    Toast.makeText(Cart.this,"Cart is empty",Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void showAlertDialog() {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("One More Step");
        alertDialog.setMessage("Enter Delivery Address");
  //      final EditText editadrres = new EditText(Cart.this);
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT
//        );

        LayoutInflater inflater = this.getLayoutInflater();
        View order_address_comment = inflater.inflate(R.layout.order_cpmment_address_layout,null);

        final EditText edtAdress = order_address_comment.findViewById(R.id.edtAddress);
        final EditText edtcomments = order_address_comment.findViewById(R.id.edtComment);


//        editadrres.setLayoutParams(lp);
//        alertDialog.setView(editadrres);
        alertDialog.setView(order_address_comment);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new Database(getBaseContext()).cleanCart();

                Request request = new Request(
                        Common.currentUser.getPhone(),
                        Common.currentUser.getName(),
                        edtAdress.getText().toString(),
                        textTotalPrice.getText().toString(),
                        edtcomments.getText().toString(),
                        cart,
                        "0"
                );


                String order_number =String.valueOf(System.currentTimeMillis());
                requests.child(order_number).setValue(request);

                Toast.makeText(Cart.this,"Thanks a lot : Order has been placed",Toast.LENGTH_SHORT).show();
                finish();



                sendNotificationOrder(order_number);






            }
        });

    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    });
alertDialog.show();

    }

    private void sendNotificationOrder(final String order_number) {

        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query data = tokens.orderByChild("serverToken").equalTo(true);
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {

                    Token serverToken = postSnapshot.getValue(Token.class);

                    Notification notification = new Notification("Lakshmi Restaurrent", "You have new order" + order_number);
                    Sender content = new Sender(serverToken.getToken(), notification);

                    mService.sendNotification(content)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {

                                    if(response.code() == 200){
                                        if (response.body().success == 1) {

                                            Toast.makeText(Cart.this,"Thanks a lot : Order has been placed",Toast.LENGTH_SHORT).show();
                                            finish();


                                        }else {
                                            Toast.makeText(Cart.this,"Thanks a lot : Order has been placed",Toast.LENGTH_SHORT).show();

                                        }

                                    }



                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                    Log.e("ERROR", "onFailure: "+t.getMessage() );




                                }
                            });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {


        if (item.getTitle().equals(Common.DELETE)) {

            deleteCart(item.getOrder());
        }

        return true;
    }

    private void deleteCart(int order) {

        cart.remove(order);

        new Database(this).cleanCart();

        for (Order item : cart) {
            new Database(this).addToCart(item);


        }
        loadListFood();
    }

    private void loadListFood() {

        cart = new Database(this).getCarts();
        adapter = new CartAdapter(cart,this);
        adapter.notifyDataSetChanged();
        recycler_cart.setAdapter(adapter);


        int total =0;
        for (Order order : cart) {
            total += (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity()));

            Locale locale = new Locale("en", "US");
            NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
            textTotalPrice.setText(fmt.format(total));
        }

    }
}

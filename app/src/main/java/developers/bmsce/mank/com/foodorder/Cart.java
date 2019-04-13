package developers.bmsce.mank.com.foodorder;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.util.Base64Utils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import developers.bmsce.mank.com.foodorder.Common.Common;
import developers.bmsce.mank.com.foodorder.Database.Database;
import developers.bmsce.mank.com.foodorder.Model.Order;
import developers.bmsce.mank.com.foodorder.Model.Request;
import developers.bmsce.mank.com.foodorder.ViewHolder.CartAdapter;

public class Cart extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference requests;

    RecyclerView recycler_cart;
    RecyclerView.LayoutManager layoutManager;

    TextView textTotalPrice;
    Button btn_placeorder;

    List<Order> cart = new ArrayList<>();

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
        final EditText editadrres = new EditText(Cart.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        editadrres.setLayoutParams(lp);
        alertDialog.setView(editadrres);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                Request request = new Request(
                        Common.currentUser.getPhone(),
                        Common.currentUser.getName(),
                        editadrres.getText().toString(),
                        textTotalPrice.getText().toString(),
                        cart
                );


                requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);

                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Cart.this,"Thanks a lot : Order has been placed",Toast.LENGTH_SHORT).show();



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

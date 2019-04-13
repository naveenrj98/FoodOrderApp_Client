package developers.bmsce.mank.com.foodorder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import developers.bmsce.mank.com.foodorder.Common.Common;
import developers.bmsce.mank.com.foodorder.Model.Food;
import developers.bmsce.mank.com.foodorder.Model.Request;
import developers.bmsce.mank.com.foodorder.ViewHolder.FoodViewHolder;
import developers.bmsce.mank.com.foodorder.ViewHolder.OrderViewHolder;

public class OrderStatus extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference requests;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request,OrderViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);




        //Auth
        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");




        recyclerView = findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


          if(getIntent() ==null) {
              loadOrders(Common.currentUser.getPhone());
          }else {

              loadOrders(getIntent().getStringExtra("userPhone"));
          }




    }

    private void loadOrders(String phone) {

        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(
                Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                requests.orderByChild("phone").equalTo(phone)
        ) {
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder, Request model, int position) {


                viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                viewHolder.txtOrderStatus.setText(converCodeToStatus(model.getStatus()));
                viewHolder.txtOrderphone.setText(model.getPhone());
                viewHolder.txtOrderAddress.setText(model.getAddress());



            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    private String converCodeToStatus(String status) {

        if(status.equals("0"))
        {
            return  "Placed";
        }else if(status.equals("1")){
            return "On my Way";


        }else {

            return "Shipped";
        }


    }
}

package developers.bmsce.mank.com.foodorder;

import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import developers.bmsce.mank.com.foodorder.Database.Database;
import developers.bmsce.mank.com.foodorder.Model.Food;
import developers.bmsce.mank.com.foodorder.Model.Order;
import developers.bmsce.mank.com.foodorder.R;

public class FoodDetail extends AppCompatActivity {

    TextView food_name, food_price, food_desription;
    ImageView food_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton elegantNumberButton;

    String foodId = "";
    FirebaseDatabase database;
    DatabaseReference foods;

    Food currentfood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);




        //Auth
        database = FirebaseDatabase.getInstance();
        foods = database.getReference("Foods");

        elegantNumberButton = findViewById(R.id.number_button);
        btnCart = findViewById(R.id.btnCart);

        food_image = findViewById(R.id.img_food);
        food_name = findViewById(R.id.food_name);
        food_price = findViewById(R.id.food_price);
        food_desription = findViewById(R.id.food_description);

        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);


        if (getIntent() != null) {
            foodId = getIntent().getStringExtra("FoodId");

        }
        if(!foodId.isEmpty() && foodId !=null){

            getDetailFood(foodId);


        }


        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 new Database(getBaseContext()).addToCart(new Order(
                         foodId,
                         currentfood.getName(),
                         elegantNumberButton.getNumber(),
                         currentfood.getPrice(),
                         currentfood.getDiscount()


                 ));

                Toast.makeText(FoodDetail.this,"Added to the cart",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getDetailFood(final String foodId) {

        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentfood = dataSnapshot.getValue(Food.class);

                Picasso.with(getBaseContext()).load(currentfood.getImage())
                        .into(food_image);
                collapsingToolbarLayout.setTitle(currentfood.getName());
                food_price.setText(currentfood.getPrice());
                food_name.setText(currentfood.getName());
                food_desription.setText(currentfood.getDescription());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}

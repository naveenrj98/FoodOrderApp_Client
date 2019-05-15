package developers.bmsce.mank.com.foodorder;

import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.lang.reflect.Array;
import java.util.Arrays;

import developers.bmsce.mank.com.foodorder.Common.Common;
import developers.bmsce.mank.com.foodorder.Database.Database;
import developers.bmsce.mank.com.foodorder.Model.Food;
import developers.bmsce.mank.com.foodorder.Model.Order;
import developers.bmsce.mank.com.foodorder.Model.Rating;
import developers.bmsce.mank.com.foodorder.R;

public class FoodDetail extends AppCompatActivity implements RatingDialogListener{

    TextView food_name, food_price, food_desription;
    ImageView food_image;

    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart,btnraingBar;
    ElegantNumberButton elegantNumberButton;

    RatingBar  ratingBar ;

    String foodId = "";
    FirebaseDatabase database;
    DatabaseReference foods;
    DatabaseReference ratingTBl;

    Food currentfood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);




        //Auth
        database = FirebaseDatabase.getInstance();
        foods = database.getReference("Foods");
        ratingTBl = database.getReference("Rating");

        elegantNumberButton = findViewById(R.id.number_button);
        btnCart = findViewById(R.id.btnCart);
        btnraingBar = findViewById(R.id.btnrateing);


        food_image = findViewById(R.id.img_food);
        food_name = findViewById(R.id.food_name);
        food_price = findViewById(R.id.food_price);
        food_desription = findViewById(R.id.food_description);
        ratingBar = findViewById(R.id.ratingbar);


        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);


        if (getIntent() != null) {
            foodId = getIntent().getStringExtra("FoodId");

        }
        if(!foodId.isEmpty() && foodId !=null){

            getDetailFood(foodId);
            getRatingFood(foodId);



        }


        btnraingBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                shoeRatingDialog();
            }
        });
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

    private void getRatingFood(String foodId) {

        Query foodRating = ratingTBl.orderByChild("foodId").equalTo(foodId);

        foodRating.addValueEventListener(new ValueEventListener() {
            int count =0, sum=0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postDataSnapshot1 : dataSnapshot.getChildren()) {

                    Rating item = postDataSnapshot1.getValue(Rating.class);

                    sum += Integer.parseInt(item.getRatevalue());
                    count++;

                }
                if (count !=0 ) {

                    float average = sum/count;
                    ratingBar.setRating(average);


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void shoeRatingDialog() {

        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Vry Bad","Not Good","Quite Ok","Very Good","Excellent"))
                .setDefaultRating(1)
                .setDescription("Please give your feedback by giving some stars to RJF")
                .setTitleTextColor(R.color.colorPrimary)
                .setDescriptionTextColor(R.color.colorPrimary)
                .setHint("Please comment here.....")
                .setHintTextColor(R.color.colorAccent)
                .setCommentTextColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.ratingDialogFadeAnim)
                .create(FoodDetail.this)
                .show();
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

    @Override
    public void onPositiveButtonClicked(int value, String comments) {
        final Rating rating = new Rating(Common.currentUser.getPhone(),
                foodId,
                String.valueOf(value),
                comments);

        ratingTBl.child(Common.currentUser.getPhone()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(Common.currentUser.getPhone()).exists()) {


                    ratingTBl.child(Common.currentUser.getPhone()).removeValue();
                    ratingTBl.child(Common.currentUser.getPhone()).setValue(rating);


                }else {

                    ratingTBl.child(Common.currentUser.getPhone()).setValue(rating);
                }
                Toast.makeText(FoodDetail.this,"THanks for your great feedback boss",Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onNegativeButtonClicked() {

    }
}

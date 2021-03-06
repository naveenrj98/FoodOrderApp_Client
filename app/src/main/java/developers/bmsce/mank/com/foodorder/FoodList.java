package developers.bmsce.mank.com.foodorder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import developers.bmsce.mank.com.foodorder.Common.Common;
import developers.bmsce.mank.com.foodorder.Database.Database;
import developers.bmsce.mank.com.foodorder.Interface.ItemClickListener;
import developers.bmsce.mank.com.foodorder.Model.Category;
import developers.bmsce.mank.com.foodorder.Model.Food;
import developers.bmsce.mank.com.foodorder.ViewHolder.FoodViewHolder;
import developers.bmsce.mank.com.foodorder.ViewHolder.MenuViewHolder;

public class FoodList extends AppCompatActivity {



    FirebaseDatabase database;
    DatabaseReference foodList;

    RecyclerView recycler_food;
    RecyclerView.LayoutManager layoutManager;

    String categoryId="";
    FirebaseRecyclerAdapter<Food,FoodViewHolder> adptor;

    Database localdatabse;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        //Auth
        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Foods");


        localdatabse = new Database(this);



        recycler_food = findViewById(R.id.recycler_food);
        recycler_food.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_food.setLayoutManager(layoutManager);

         if(getIntent() != null){
             categoryId = getIntent().getStringExtra("CategoryId");

         }
        if (!categoryId.isEmpty() && categoryId != null) {
            loadListFood(categoryId);
        }



    }

    private void loadListFood(String categoryId) {

        adptor = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class,
                R.layout.food_item,
                FoodViewHolder.class,
                foodList.orderByChild("menuId").equalTo(categoryId)) {
            @Override
            protected void populateViewHolder(final FoodViewHolder viewHolder, final Food model, final int position) {


                viewHolder.foodname.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.foodimage);

                if (localdatabse.isFavorites(adptor.getRef(position).getKey())) {

                    viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);


                }
                viewHolder.fav_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!localdatabse.isFavorites(adptor.getRef(position).getKey())) {


                            localdatabse.addToFavorites(adptor.getRef(position).getKey());
                            viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);
                            Toast.makeText(FoodList.this,""+model.getName()+"was Added to favorits",Toast.LENGTH_SHORT).show();

                        }else {

                            localdatabse.removeFromFavorites(adptor.getRef(position).getKey());
                            viewHolder.fav_image.setImageResource(R.drawable.ic_favorite_black_24dp);
                            Toast.makeText(FoodList.this,""+model.getName()+"was removed from favorites",Toast.LENGTH_SHORT).show();


                        }


                    }
                });
                final Food local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        Toast.makeText(FoodList.this,""+local.getName(),Toast.LENGTH_SHORT).show();

                        Intent foodlist = new Intent(FoodList.this, FoodDetail.class);
                        foodlist.putExtra("FoodId", adptor.getRef(position).getKey());
                        startActivity(foodlist);


                    }
                });

            }
        };

        recycler_food.setAdapter(adptor);
    }
}

package developers.bmsce.mank.com.foodorder.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

import developers.bmsce.mank.com.foodorder.Common.Common;
import developers.bmsce.mank.com.foodorder.Interface.ItemClickListener;
import developers.bmsce.mank.com.foodorder.Model.Order;
import developers.bmsce.mank.com.foodorder.R;

 class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener {

public TextView text_cart_name,text_price;
public ImageView img_cart_count;

private ItemClickListener itemClickListener;

    public void setText_cart_name(TextView text_cart_name) {
        this.text_cart_name = text_cart_name;
    }

    public CartViewHolder(View itemView) {
        super(itemView);

        text_cart_name = itemView.findViewById(R.id.cart_item_name);
        text_price = itemView.findViewById(R.id.cart_item_price);
        img_cart_count = itemView.findViewById(R.id.cart_item_count);

        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

    }

     @Override
     public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo) {



         contextMenu.setHeaderTitle("Select the Action");


        // contextMenu.add(0, 0, getAdapterPosition(), Common.UPDATE);
         contextMenu.add(0, 1, getAdapterPosition(), Common.DELETE);

     }
 }

public class CartAdapter extends RecyclerView.Adapter<CartViewHolder>{


    private List<Order> listData = new ArrayList<>();
    public Context context;

    public CartAdapter(List<Order> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemview = inflater.inflate(R.layout.cart_layout, parent, false);
        return new CartViewHolder(itemview);



    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {

        TextDrawable drawable = TextDrawable.builder()
                .buildRound("" + listData.get(position).getQuantity(), Color.RED);
        holder.img_cart_count.setImageDrawable(drawable);

        Locale locale = new Locale("en", "US");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        int price = (Integer.parseInt(listData.get(position).getPrice()));
        holder.text_price.setText(fmt.format(price));
        holder.text_cart_name.setText(listData.get(position).getProductName());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}


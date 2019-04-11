package developers.bmsce.mank.com.foodorder.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import developers.bmsce.mank.com.foodorder.Interface.ItemClickListener;
import developers.bmsce.mank.com.foodorder.R;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

  public TextView textMenuName;
 public   ImageView imageView;

private ItemClickListener itemClickListener;
    public MenuViewHolder(View itemView) {
        super(itemView);

        textMenuName = itemView.findViewById(R.id.menu_name);
        imageView = itemView.findViewById(R.id.menu_image);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }
}

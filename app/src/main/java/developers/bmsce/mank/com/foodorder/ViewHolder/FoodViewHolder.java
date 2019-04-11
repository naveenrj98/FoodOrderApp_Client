package developers.bmsce.mank.com.foodorder.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import developers.bmsce.mank.com.foodorder.Interface.ItemClickListener;
import developers.bmsce.mank.com.foodorder.R;



    public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView foodname;
        public ImageView foodimage;

        private ItemClickListener itemClickListener;
        public FoodViewHolder(View itemView) {
            super(itemView);

            foodname = itemView.findViewById(R.id.food_name);
            foodimage = itemView.findViewById(R.id.food_image);

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

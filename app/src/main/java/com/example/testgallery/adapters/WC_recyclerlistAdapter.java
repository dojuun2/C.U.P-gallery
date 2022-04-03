
package com.example.testgallery.adapters;

        import android.content.Context;
        import android.graphics.drawable.Drawable;
        import android.provider.ContactsContract;

        import androidx.annotation.NonNull;

        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;
        import androidx.recyclerview.widget.RecyclerView;

        import com.bumptech.glide.Glide;
        import com.example.testgallery.R;

        import java.util.ArrayList;

public class WC_recyclerlistAdapter extends RecyclerView.Adapter<WC_recyclerlistAdapter.ItemViewHolder>  {

    private ArrayList<String> listData;




    public void addItem(ArrayList<String> imageList) {
        listData = new ArrayList<>();
        this.listData = imageList;


    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wc_gogolist, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {

        return listData.size();

    }



    class ItemViewHolder extends RecyclerView.ViewHolder {


        private ImageView imageView;
        private Context context;

        ItemViewHolder(View itemView) {
            super(itemView);


            imageView = itemView.findViewById(R.id.item_wc_gogolist);
            context = itemView.getContext();

        }

        void onBind(String img) {

            Glide.with(context).load(img).into(imageView);
        }
    }
}

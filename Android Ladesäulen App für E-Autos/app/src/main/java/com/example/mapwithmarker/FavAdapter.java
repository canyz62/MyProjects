package com.example.mapwithmarker;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.MyViewHolder> implements Filterable {
    private static ArrayList<RecyclerViewItem> mExampleList;
    private static ArrayList<RecyclerViewItem> mExampleListFull;
    public boolean fromWhich = false; //true = Search  &  false = favoriten
    public boolean fromSearch = false;

    public /*static*/ class MyViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public TextView mTextView4;
        public TextView mTextView5;
        public CheckBox FavCheckbox;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.imageViewRF);
            mTextView1 = itemView.findViewById(R.id.textviewRF);
            mTextView2 = itemView.findViewById(R.id.textviewR2F);
            mTextView3 = itemView.findViewById(R.id.textviewR3F);
            mTextView4 = itemView.findViewById(R.id.textviewR4F);
            mTextView5 = itemView.findViewById(R.id.textviewR5F);
            FavCheckbox = itemView.findViewById(R.id.like_button_cbfav);

            FavCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        Toast.makeText(itemView.getContext(), "Von Favoriten entfernt", Toast.LENGTH_SHORT).show();
                        int position = getBindingAdapterPosition();
                        RecyclerViewItem FavItem2= mExampleList.get(position);
                        Favoriten.removeFav_list(FavItem2);

                        FavItem2.setSelected(false);

                        notifyItemRemoved(position);
                        //this line below gives you the animation and also updates the
                        //list items after the deleted item
                        notifyItemRangeChanged(position, getItemCount());

                    } else {
                        Toast.makeText(itemView.getContext(), "Zu Favoriten hinzugefügt", Toast.LENGTH_SHORT).show();

                        int position = getBindingAdapterPosition();
                        RecyclerViewItem FavItem = mExampleList.get(position);

                        Favoriten.setFav_list(FavItem);
                        FavItem.setSelected(true);
                    }
                }
            });
        }
    }

    public FavAdapter(ArrayList<RecyclerViewItem> exampleList){
        mExampleList = exampleList;
        mExampleListFull = new ArrayList<>(exampleList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_recyclerviewfav, parent, false);
        MyViewHolder evh = new MyViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RecyclerViewItem currentItem = mExampleList.get(position);

        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView2.setText(currentItem.getText2());
        holder.mTextView3.setText(currentItem.getText3());
        holder.mTextView4.setText(currentItem.getText4());
        holder.mTextView5.setText(currentItem.getText5());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = currentItem.getFavoriteID();

                if(MapsMarkerActivity.Role.equals("User")){
                    Intent intent = new Intent(v.getContext(), OnRecyclerClickUser.class);

                    intent.putExtra("Objekt2", mExampleList.get(holder.getAbsoluteAdapterPosition()));
                    intent.putExtra("ItemPos",position);
                    intent.putExtra("fromSearch", fromSearch);
                    fromWhich = true;
                    intent.putExtra("fromWhich",fromWhich);

                    v.getContext().startActivity(intent);
                }

                if(MapsMarkerActivity.Role.equals("Servicetechniker")){
                    Intent intent = new Intent(v.getContext(), OnRecyclerClickServiceworker.class);

                    intent.putExtra("Objekt", mExampleList.get(holder.getAbsoluteAdapterPosition()));
                    intent.putExtra("ItemPos",position);
                    intent.putExtra("fromSearch", fromSearch);
                    fromWhich = true;
                    intent.putExtra("fromWhich",fromWhich);

                    v.getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<RecyclerViewItem> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0){
                filteredList.addAll(mExampleListFull);
            }
            else{
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(RecyclerViewItem item : mExampleListFull) {
                    if(item.getText1().toLowerCase().contains(filterPattern)
                            || item.getText2().toLowerCase().contains(filterPattern)
                            || item.getText3().toLowerCase().contains(filterPattern)
                            || item.getText4().toLowerCase().contains(filterPattern)
                            || item.getText5().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mExampleList.clear();
            mExampleList.addAll((List) results.values);

            if(mExampleList.size() != 23396){
                fromSearch = true;
            }else{
                fromSearch = false;
            }

            notifyDataSetChanged();
        }
    };
}

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

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> implements Filterable {
    private static ArrayList<RecyclerViewItem> mExampleList;
    private static ArrayList<RecyclerViewItem> mExampleListFull;
    private static boolean AlreadyChecked = false;
    public boolean fromWhich = false; //true = Search  &  false = favoriten
    public boolean fromSearch = false;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2;
        public TextView mTextView3;
        public TextView mTextView4;
        public TextView mTextView5;
        public CheckBox FavCheckbox;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.imageViewR);
            mTextView1 = itemView.findViewById(R.id.textviewR);
            mTextView2 = itemView.findViewById(R.id.textviewR2);
            mTextView3 = itemView.findViewById(R.id.textviewR3);
            mTextView4 = itemView.findViewById(R.id.textviewR4);
            mTextView5 = itemView.findViewById(R.id.textviewR5);
            FavCheckbox = itemView.findViewById(R.id.like_button_cb);


            FavCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        int position = getBindingAdapterPosition();
                        RecyclerViewItem FavItem = mExampleList.get(position);

                        int position2 = FavItem.getID();

                        if (!FavItem.isSelected()) {
                            Toast.makeText(itemView.getContext(), "Zu Favoriten hinzugefügt", Toast.LENGTH_SHORT).show();
                            FavItem.setSelected(true);

                            if(MapsMarkerActivity.Stationen.get(position2).isInDerNaehe()){
                                FavItem.setIstInDerNaehe(true);
                            }

                            Favoriten.setFav_list(FavItem);
                        }
                    }else{
                        Toast.makeText(itemView.getContext(), "Von Favoriten entfernt", Toast.LENGTH_SHORT).show();

                        int position = getBindingAdapterPosition();
                        RecyclerViewItem FavItem2 = mExampleList.get(position);

                        FavItem2.setSelected(false);
                        Favoriten.removeFav_list(FavItem2);
                    }
                }
            });
        }
    }

    public Adapter(ArrayList<RecyclerViewItem> exampleList) {
        mExampleList = exampleList;
        mExampleListFull = new ArrayList<>(exampleList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_recyclerview, parent, false);
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


        holder.FavCheckbox.setChecked(currentItem.isSelected());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int position = currentItem.getID();

                if(MapsMarkerActivity.Role.equals("User")){
                    Intent intent = new Intent(v.getContext(), OnRecyclerClickUser.class);

                    intent.putExtra("Objekt2", mExampleList.get(holder.getAbsoluteAdapterPosition()));
                    intent.putExtra("ItemPos",position);
                    intent.putExtra("fromSearch", fromSearch);
                    fromWhich = false;
                    intent.putExtra("fromWhich",fromWhich);

                    v.getContext().startActivity(intent);
                }

                if(MapsMarkerActivity.Role.equals("Servicetechniker")){
                    Intent intent = new Intent(v.getContext(), OnRecyclerClickServiceworker.class);

                    intent.putExtra("Objekt", mExampleList.get(holder.getAbsoluteAdapterPosition()));
                    intent.putExtra("ItemPos",position);
                    intent.putExtra("fromSearch", fromSearch);
                    fromWhich = false;
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

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mExampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (RecyclerViewItem item : mExampleListFull) {
                    if (item.getText1().toLowerCase().contains(filterPattern)
                            || item.getText2().toLowerCase().contains(filterPattern)
                            || item.getText3().toLowerCase().contains(filterPattern)
                            || item.getText4().toLowerCase().contains(filterPattern)
                            || item.getText5().toLowerCase().contains(filterPattern)) {
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
                OnRecyclerClickUser.fromSearch = true;
                OnRecyclerClickServiceworker.fromSearch = true;
            }else{
                fromSearch = false;
                OnRecyclerClickUser.fromSearch = false;
                OnRecyclerClickServiceworker.fromSearch = false;
            }

            notifyDataSetChanged();
        }
    };
}

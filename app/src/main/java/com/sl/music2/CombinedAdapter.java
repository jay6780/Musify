package com.sl.music2;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CombinedAdapter extends RecyclerView.Adapter<CombinedAdapter.CombinedViewHolder> {

    private List<CombinedItem> items;
    private OnItemClickListener listener;

    public CombinedAdapter(List<CombinedItem> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CombinedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_combined, parent, false);
        return new CombinedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CombinedViewHolder holder, int position) {
        CombinedItem item = items.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class CombinedViewHolder extends RecyclerView.ViewHolder {

        private TextView combinedTextView;
        private ImageView combinedImageView; // Add ImageView for displaying images

        CombinedViewHolder(@NonNull View itemView) {
            super(itemView);

            combinedTextView = itemView.findViewById(R.id.combinedTextView);
            combinedImageView = itemView.findViewById(R.id.combinedImageView); // Initialize ImageView

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(items.get(getAdapterPosition()));
                    }
                }
            });
        }

        void bind(CombinedItem item, OnItemClickListener listener) {
            if (item.isArtist()) {
                combinedTextView.setText(item.getArtist().getName());
                String imageUrl = item.getArtist().getImageUrl();
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    Picasso.get().load(imageUrl).into(combinedImageView);
                } else {
                    // Handle the case of empty or null image URL (e.g., show a placeholder image)
                    // You can customize this behavior based on your app's design
                    combinedImageView.setImageResource(R.drawable.ic_baseline_music_note_24);
                }
            } else {
                combinedTextView.setText(item.getTrack().getName() + " - " + item.getTrack().getArtist().getName());
                String imageUrl = item.getTrack().getImageUrl();
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    Picasso.get().load(imageUrl).into(combinedImageView);
                } else {
                    // Handle the case of empty or null image URL (e.g., show a placeholder image)
                    // You can customize this behavior based on your app's design
                    combinedImageView.setImageResource(R.drawable.ic_baseline_music_note_24);
                }
            }
        }
    }


        public interface OnItemClickListener {
        void onItemClick(CombinedItem item);
    }
}

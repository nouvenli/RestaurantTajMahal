package com.openclassrooms.tajmahal.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.tajmahal.R;
import com.openclassrooms.tajmahal.domain.model.Review;

public class ReviewAdapter extends ListAdapter<Review, ReviewAdapter.ViewHolder> {

    /**
     * constructeur pour les avis précédent
     */
    public ReviewAdapter() {
        super(new ItemCallback());
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /**Crée la vue à partir de item_review.xml
         * retourne un nouveau viewholder
         */
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Appelle holder.bind() avec l'élément à la position donnée
        holder.bind(getItem(position));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        // Déclare les variables pour chaque élément du layout
        private final TextView tvReviewerName;
        private final TextView tvReviewerComment;
        private final RatingBar rbReviewRating;
        private final ImageView ivReviewerAvatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Initialise les variables avec findViewById
            tvReviewerName = itemView.findViewById(R.id.tvReviewerName);
            tvReviewerComment = itemView.findViewById(R.id.tvReviewerComment);
            rbReviewRating = itemView.findViewById(R.id.rbReviewRating);
            ivReviewerAvatar = itemView.findViewById(R.id.ivReviewerAvatar);
        }

        public void bind(Review review) {
            // Remplit les TextView, ImageView, RatingBar avec les données de review
            tvReviewerName.setText(review.getUsername());
            tvReviewerComment.setText(review.getComment());
            rbReviewRating.setRating(review.getRate());
            //ivReviewerAvatar.setImageDrawable(review.getPicture());
            // ivReviewerAvatar   je ne sais pas quoi prendre je n'ai pas setPicture dans la liste
        }
    }

    private static class ItemCallback extends DiffUtil.ItemCallback<Review> {
        // Compare les Reviews
        // Compare les Review
        @Override
        public boolean areItemsTheSame(@NonNull Review oldItem, @NonNull Review newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Review oldItem, @NonNull Review newItem) {
            return oldItem.equals(newItem);
        }

    }
}

package com.openclassrooms.tajmahal.ui.reviews;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.tajmahal.data.repository.RestaurantRepository;
import com.openclassrooms.tajmahal.domain.model.Review;

import java.util.List;

import javax.inject.Inject;

public class ReviewViewModel extends ViewModel {
    private final RestaurantRepository restaurantRepository;

    @Inject
    public ReviewViewModel(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public LiveData<List<Review>> getReviews() {
        return restaurantRepository.getReviews();
    }

    /**
     * Add a new review to the list.
     * Creates a review object and sends it to the Repository.
     *
     * @param username The name of the user
     * @param picture  The URL of the user's profile picture
     * @param comment  The review comment
     * @param rate     The rating (1-5 stars)
     */
    public void addReview(String username, String picture, String comment, int rate) {
        Review newReview = new Review(username, picture, comment, rate);
        restaurantRepository.addReview(newReview);
    }


}

package com.openclassrooms.tajmahal.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.openclassrooms.tajmahal.data.service.RestaurantApi;
import com.openclassrooms.tajmahal.domain.model.Restaurant;
import com.openclassrooms.tajmahal.domain.model.Review;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;


/**
 * This is the repository class for managing restaurant data. Repositories are responsible
 * for coordinating data operations from data sources such as network APIs, databases, etc.
 * <p>
 * Typically in an Android app built with architecture components, the repository will handle
 * the logic for deciding whether to fetch data from a network source or use data from a local cache.
 *
 * @see Restaurant
 * @see RestaurantApi
 */
@Singleton
public class RestaurantRepository {

    // The API interface instance that will be used for network requests related to restaurant data.
    private final RestaurantApi restaurantApi;
    //creation mutableLiveData pour les reviews
    private final MutableLiveData<List<Review>> reviewsLiveData = new MutableLiveData<>();
    /**
     * Constructs a new instance of {@link RestaurantRepository} with the given {@link RestaurantApi}.
     *
     * @param restaurantApi The network API interface for fetching restaurant data.
     */
    @Inject
    public RestaurantRepository(RestaurantApi restaurantApi) {
        this.restaurantApi = restaurantApi;
        // initialize the reviewsLiveData when the repository is created
        reviewsLiveData.setValue(restaurantApi.getReviews());
    }

    /**
     * Fetches the restaurant details.
     * <p>
     * This method will make a network call using the provided {@link RestaurantApi} instance
     * to fetch restaurant data. Note that error handling and any transformations on the data
     * would need to be managed.
     *
     * @return LiveData holding the restaurant details.
     */
    public LiveData<Restaurant> getRestaurant() {
        return new MutableLiveData<>(restaurantApi.getRestaurant());
    }
    /**
     * Retrieves the list of user reviews.
     *
     * @return LiveData containing the list of reviews
     */
    public LiveData<List<Review>> getReviews() {
        return reviewsLiveData;
    }

    /* update reviews and notifies observers
     *
    @param review - the review to add
     */
    public void addReview(Review review) {
        restaurantApi.addReview(review);
        // ✅ Crée une NOUVELLE liste pour que LiveData détecte le changement
        reviewsLiveData.setValue(new ArrayList<>(restaurantApi.getReviews()));
    }
}

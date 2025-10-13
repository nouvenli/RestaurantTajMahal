package com.openclassrooms.tajmahal;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.openclassrooms.tajmahal.data.repository.RestaurantRepository;
import com.openclassrooms.tajmahal.domain.model.Review;
import com.openclassrooms.tajmahal.ui.reviews.ReviewViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;

/**
 * Review view model test class
 * test uniquement le view model pas le repository
 * Création d'un faux restaurantRepository vide
 */
public class ReviewViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private RestaurantRepository mockRepository;

    private ReviewViewModel viewModel;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        viewModel = new ReviewViewModel(mockRepository);
    }

    /**
     * test 1 - ajout d'un avis valide
     * le fragment gère les avis sans commentaires ou sans rate
     */
    @Test
    public void testAddReview_ValidReview() {
        // donnée à prendre en compte
        String userName = "John Doe";
        String userComment = "Great restaurant!";
        String pictureUrl = "https://example.com/image.jpg";
        int rating = 4;
        // appel de la méthode à tester
        viewModel.addReview(userName, userComment, pictureUrl, rating);
        // vérification du résultat
        verify(mockRepository).addReview(any(Review.class));
    }

    /**
     * test 2 - vérifie l'appel au repository
     * test si le repository est appelé
     */
    @Test
    public void addReview_CallsRepository() {
        // donnée à prendre en compte
        String userName = "John Doe";
        String userComment = "Great restaurant!";
        String pictureUrl = "https://example.com/image.jpg";
        int rating = 4;
        // appel de la méthode à tester
        viewModel.addReview(userName, userComment, pictureUrl, rating);
        // vérification du résultat
        verify(mockRepository).addReview(argThat(review ->
                review.getUsername().equals(userName) &&
                        review.getPicture().equals(userComment) &&
                        review.getComment().equals(pictureUrl) &&
                        review.getRate() == rating));
    }

    /**
     * test 3 - test la récupération des avis
     * retourne quelque chose
     * retourne le bon nombre d'éléments
     * retourne dans le bon ordre
     */
    public void getReviews_ReturnsReviews() {
        // donnée à prendre en compte
        List<Review> expectedReviews = Arrays.asList(new Review("John Doe", "https://example.com/image.jpg", "Great restaurant!", 4), new Review("Jane Smith", "https://example.com/image2.jpg", "Excellent service!", 5));
        MutableLiveData<List<Review>> liveData = new MutableLiveData<>(expectedReviews);
        when(mockRepository.getReviews()).thenReturn(liveData);

        // appel de la méthode à tester
        LiveData<List<Review>> result = viewModel.getReviews();

        // vérification du résultat
        assertNotNull(result);
        assertEquals(2, result.getValue().size());
        assertEquals("John Doe", result.getValue().get(0).getUsername());

    }

    /**
     * test 4 - test rating limite inférieure
     */
    @Test
    public void testAddReview_MinimalRating() {
        // donnée à prendre en compte
        String userName = "John Doe";
        String userComment = "Great restaurant!";
        String pictureUrl = "https://example.com/image.jpg";
        int rating = 1;
        // appel de la méthode à tester
        viewModel.addReview(userName, userComment, pictureUrl, rating);
        // vérification du résultat
        verify(mockRepository).addReview(any(Review.class));
    }

    /**
     * test 5 - test rating limite supérieure
     */
    @Test
    public void testAddReview_MaximalRating() {
        // donnée à prendre en compte
        String userName = "John Doe";
        String userComment = "Great restaurant!";
        String pictureUrl = "https://example.com/image.jpg";
        int rating = 5;
        // appel de la méthode à tester
        viewModel.addReview(userName, userComment, pictureUrl, rating);
        // vérification du résultat
        verify(mockRepository).addReview(any(Review.class));
    }

}


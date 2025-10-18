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
import static org.mockito.Mockito.never;
import static org.junit.Assert.assertTrue;

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
     * le fragment gère les avis sans commentaire ou sans rate
     */
    @Test
    public void processNewReview_withValidData_shouldCallRepository() {
        // Arrange
        String comment = "Great restaurant!";
        int rating = 4;

        // Act
        viewModel.processNewReview(comment, rating);

        // Assert
        verify(mockRepository).addReview(any(Review.class));
    }

    /**
     * test 2 - vérifie l'appel au repository
     * test si le repository est appelé
     */
    @Test
    public void processNewReview_shouldCallRepositoryWithCorrectData() {
        // Arrange
        String comment = "Très bon service";
        int rating = 4;

        // Act
        viewModel.processNewReview(comment, rating);

        // Assert
        verify(mockRepository).addReview(argThat(review ->
                review.getUsername().equals("Manon Garcia") &&
                        review.getPicture().equals("https://xsgames.co/randomusers/assets/avatars/female/20.jpg") &&
                        review.getComment().equals(comment) &&
                        review.getRate() == rating
        ));
    }

    /**
     * test 3 - test la récupération des avis
     * retourne quelque chose
     * retourne le bon nombre d'éléments
     * retourne dans le bon ordre
     */
    @Test
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
     * test 4 - test si le commentaire est vide
     */
    @Test
    public void processNewReview_withEmptyComment_shouldSetCommentError() {
        // Arrange
        String comment = "";
        int rating = 5;

        // Act
        viewModel.processNewReview(comment, rating);

        // Assert
        assertNotNull(viewModel.getCommentError().getValue());
        assertEquals("Désolés, le commentaire ne peut pas être vide", viewModel.getCommentError().getValue());
        verify(mockRepository, never()).addReview(any(Review.class));
    }

    /**
     * test 5 - test si la note est nulle
     */
    @Test
    public void processNewReview_withZeroRating_shouldSetRatingError() {
        // Arrange
        String comment = "Great restaurant!";
        int rating = 0;

        // Act
        viewModel.processNewReview(comment, rating);

        // Assert
        assertNotNull(viewModel.getRatingError().getValue());
        assertEquals("Merci de donner une note", viewModel.getRatingError().getValue());
        verify(mockRepository, never()).addReview(any(Review.class));
    }

    /**
     * test 6 - test si le review est ajouté
     */
    @Test
    public void processNewReview_withValidData_shouldAddReviewAndEmitSuccess() {
        // Arrange
        String comment = "Excellent restaurant!";
        int rating = 5;

        // Act
        viewModel.processNewReview(comment, rating);

        // Assert
        verify(mockRepository).addReview(any(Review.class));
        assertTrue(viewModel.getReviewAddSuccessEvent().getValue());
    }
}

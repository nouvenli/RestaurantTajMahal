package com.openclassrooms.tajmahal.ui.reviews;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.openclassrooms.tajmahal.R;
import com.openclassrooms.tajmahal.adapter.ReviewAdapter;
import com.openclassrooms.tajmahal.databinding.FragmentReviewBinding;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;


/**
 * Fragment that displays a list of reviews for a restaurant and allows users to add new reviews.
 * uses ReviewViewModel to manage data and ReviewAdapter to display the list of reviews.
 */

@AndroidEntryPoint
public class ReviewFragment extends Fragment {

    private FragmentReviewBinding binding;
    private ReviewViewModel reviewViewModel;
    private ReviewAdapter adapter;

// ---lifecycle methods ---

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentReviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewModel();  //connexion entre fragment et données
        setupRecyclerView(); // crée l'adapter
        setupUI(); // mise en place de l'interface
        setupRestaurantInfo();
        observeViewModelData();
        setupAddReviewButton();
        setupBackButton();

    }

    // ---setup methods ---
    private void setupViewModel() {
        reviewViewModel = new ViewModelProvider(this).get(ReviewViewModel.class);
    }

    /**
     * cree l'adapteur
     * configure le recyclerview pour utiliser l'adapter
     * disposition des items liste verticale
     */
    private void setupRecyclerView() {
        adapter = new ReviewAdapter();
        binding.rvReviews.setAdapter(adapter);
        binding.rvReviews.setLayoutManager(new LinearLayoutManager(requireContext()));

    }

    private void setupUI() {
        Window window = requireActivity().getWindow();

        // Réactive la barre de statut normale
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

        // Fond blanc pour la barre
        window.setStatusBarColor(Color.WHITE);

        // Texte noir sur la barre (icônes sombres)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    private void setupRestaurantInfo() {
        binding.tvRestaurantName.setText("TajMahal");
        binding.tvUserName.setText("Manon Garcia");

        Glide.with(requireContext()).load("https://xsgames.co/randomusers/assets/avatars/female/20.jpg").circleCrop().into(binding.ivAvatarUser);
    }
// --- ViewModel Observation  ---

    /**
     * observateur de la liste des avis ,et des èvènements du ViewModel
     * récupère le liveData depuis le ViewModel
     * vérifie(observe) s'il y a des changements
     * met à jour l'affichage du RecyclerView
     */
    private void observeViewModelData() {
        // observe liste avis

        reviewViewModel.getReviews().observe(getViewLifecycleOwner(), reviews -> {
            adapter.submitList(null);
            adapter.submitList(new ArrayList<>(reviews), () -> {
                binding.rvReviews.smoothScrollToPosition(0);
            });
        });

        // observe èvènement de validation et erreur comment et rating
        reviewViewModel.getCommentError().observe(getViewLifecycleOwner(), error -> {
            // Affiche l'erreur si non null, sinon l'enlève.
            binding.etUserComment.setError(error);
        });

        reviewViewModel.getRatingError().observe(getViewLifecycleOwner(), error -> {
            // Utilise un Toast pour une erreur de rating
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show();
            }
        });

        // observe succes
        reviewViewModel.getReviewAddSuccessEvent().observe(getViewLifecycleOwner(), isSuccess -> {
            if (isSuccess) {
                // Réinitialisation de l'UI si l'ajout a réussi - C'EST UNE MANIPULATION D'UI
                binding.etUserComment.setText("");
                binding.rbRatingBarUser.setRating(0);
                Toast.makeText(requireContext(), "Avis ajouté avec succès", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // --- user interaction ---

    /**
     * extrait entrée utilisateur
     * est ce que le bouton Valider est pressé
     */
    private void setupAddReviewButton() {
        binding.btValidation.setOnClickListener(v -> {
            // recupère les infos utilisateurs et rating
            String comment = binding.etUserComment.getText().toString().trim();
            float rating = binding.rbRatingBarUser.getRating();

            // appel à ViewModel pour traitement
            reviewViewModel.processNewReview(comment, (int) rating);
        });
    }

    private void setupBackButton() {
        binding.ivGoBack.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }


    public static ReviewFragment newInstance() {
        return new ReviewFragment();
    }
}
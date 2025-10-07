package com.openclassrooms.tajmahal.ui.reviews;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.openclassrooms.tajmahal.R;
import com.openclassrooms.tajmahal.adapter.ReviewAdapter;
import com.openclassrooms.tajmahal.databinding.FragmentReviewBinding;

import dagger.hilt.android.AndroidEntryPoint;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

@AndroidEntryPoint
public class ReviewFragment extends Fragment {

    private FragmentReviewBinding binding;
    private ReviewViewModel reviewViewModel;
    private ReviewAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentReviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViewModel();  //connexion entre fragment et données
        setupRecyclerView(); // crée l'adapter
        observeReviews();
        setupAddReviewButton();
        setupBackButton();
    }

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

    /**
     * observateur de la liste des avis
     * pour mise à jour de l'affichage du RV
     * récupère le liveData depuis le ViewModel
     * vérifie(observe) s'il y a des changements
     * met à jour l'affichage du RecyclerView
     */
    private void observeReviews() {
        reviewViewModel.getReviews().observe(requireActivity(), reviews -> {
            adapter.submitList(reviews);
        });
    }

    /**
     * est ce que le bouton Valider est pressé
     * detecte le clic
     */
    private void setupAddReviewButton() {
        binding.btValidation.setOnClickListener(v -> {
            // recupère les infos utilisateur et rating
            String comment = binding.etUserComment.getText().toString().trim();
            float rating = binding.rbRatingBarUser.getRating();

            // validation commentaire
            if (comment.isEmpty()) {
                binding.etUserComment.setError("Désolés, le commentaire ne peut pas être vide");
                return;
            }

            // validation rating
            if (rating == 0) {
                Toast.makeText(requireContext(), "Merci de donner une note", Toast.LENGTH_SHORT).show();
                return;
            }

            // Ajout de l'avis
            String username = binding.tvUserName.getText().toString();
            String picture = "https://xsgames.co/randomusers/assets/avatars/female/20.jpg";

            reviewViewModel.addReview(username, picture, comment, (int) rating);

            // mise à zéro du formulaire
            binding.etUserComment.setText("");
            binding.rbRatingBarUser.setRating(0);

            Toast.makeText(requireContext(), "Avis ajouté avec succès", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupBackButton() {
        binding.goBack.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }

    public static ReviewFragment newInstance() {
        return new ReviewFragment();
    }
}
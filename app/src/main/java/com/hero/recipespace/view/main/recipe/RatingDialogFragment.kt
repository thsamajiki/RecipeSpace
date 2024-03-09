package com.hero.recipespace.view.main.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.hero.recipespace.databinding.FragmentDialogRatingBinding
import com.hero.recipespace.ext.hideLoading
import com.hero.recipespace.ext.setProgressPercent
import com.hero.recipespace.ext.showLoading
import com.hero.recipespace.view.LoadingState
import com.hero.recipespace.view.main.recipe.viewmodel.RatingDialogViewModel
import com.hero.recipespace.view.main.recipe.viewmodel.RecipeRateUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RatingDialogFragment : DialogFragment(), View.OnClickListener {
    private var _binding: FragmentDialogRatingBinding? = null
    private val binding: FragmentDialogRatingBinding
        get() = _binding!!

    private val viewModel by viewModels<RatingDialogViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDialogRatingBinding.inflate(inflater, container, false)

        setupViewModel()
        setupListeners()

        return binding.root
    }

    private fun setupViewModel() {
        with(viewModel) {
            lifecycleScope.launch {
                rate.observe(viewLifecycleOwner) {
                    binding.ratingBar.rating = it.rate
                }
            }

            lifecycleScope.launch {
                loadingState.collect { state ->
                    when (state) {
                        LoadingState.Hidden -> hideLoading()
                        LoadingState.Idle -> {}
                        LoadingState.Loading -> showLoading()
                        is LoadingState.Progress -> setProgressPercent(state.value)
                    }
                }
            }

            lifecycleScope.launch {
                recipeRateUiState.collect { state ->
                    when (state) {
                        is RecipeRateUiState.Success -> {
                            Toast.makeText(context, "평가가 완료되었습니다.", Toast.LENGTH_SHORT).show()

                            // 평가 완료했을 때 평가 완료된 데이터를 내려주자.
                            val result =
                                Bundle().apply {
                                    putParcelable(
                                        Result.KEY_RECIPE,
                                        recipe.value,
                                    )
                                }
                            setFragmentResult(TAG, result)
                            dismiss()
                        }

                        is RecipeRateUiState.Failed -> {
                            Toast.makeText(context, "평가가 반영되지 않았습니다. 다시 시도해주세요", Toast.LENGTH_SHORT)
                                .show()
                        }

                        RecipeRateUiState.Idle -> {}
                    }
                }
            }
        }
    }

    private fun setupListeners() {
        binding.tvCancel.setOnClickListener {
            dismiss()
        }
        binding.tvConfirm.setOnClickListener {
            requestUpdateRate()
        }
    }

    private fun requestUpdateRate() {
        val rating = binding.ratingBar.rating
        if (rating == 0f) {
            Toast.makeText(context, "평점을 매겨주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.requestUpdateRateData(rating)
    }

    object Result {
        const val KEY_RECIPE = "key_recipe"
    }

    companion object {
        fun newInstance(recipeKey: String): RatingDialogFragment =
            RatingDialogFragment().apply {
                arguments =
                    Bundle().apply {
                        putString(
                            RatingDialogViewModel.KEY_RECIPE,
                            recipeKey,
                        )
                    }
            }

        const val TAG = "RatingDialog"
    }

    override fun onClick(view: View) {
    }
}

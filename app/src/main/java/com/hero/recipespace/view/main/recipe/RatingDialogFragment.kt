package com.hero.recipespace.view.main.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.hero.recipespace.databinding.FragmentDialogRatingBinding
import com.hero.recipespace.domain.rate.entity.RateEntity
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.ext.hideLoading
import com.hero.recipespace.ext.setProgressPercent
import com.hero.recipespace.ext.showLoading
import com.hero.recipespace.view.LoadingState
import com.hero.recipespace.view.main.recipe.viewmodel.RateRecipeUiState
import com.hero.recipespace.view.main.recipe.viewmodel.RatingDialogViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RatingDialogFragment : DialogFragment(), View.OnClickListener {

    private var _binding: FragmentDialogRatingBinding? = null
    private val binding: FragmentDialogRatingBinding
        get() = _binding!!

//    private val recipe: RecipeEntity by lazy {
//        requireArguments().getParcelable(KEY_RECIPE)!!
//    }

    private val viewModel by viewModels<RatingDialogViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogRatingBinding.inflate(inflater, container, false)

        setupViewModel()
        setupListeners()

        return binding.root
    }

    // TODO: 2022-12-18 rate 옵저버 구현 (다른 사람들도 평가를 하므로 옵저버를 통해 rate 값이 계속 변경되어야 한다.
    private fun setupViewModel() {
        with(viewModel) {
            lifecycleScope.launch {
                rate.observe(viewLifecycleOwner) {

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

            // TODO: 2022-12-18 빨간줄 없애기
            lifecycleScope.launch {
                rateRecipeUiState.collect { state ->
                    when (state) {
                        is RateRecipeUiState.Success -> {
                            Toast.makeText(context, "평가가 완료되었습니다.", Toast.LENGTH_SHORT).show()

                            // 평가완료했을 때 평가완료된 데이터를 내려주자.
//                            val result = Bundle().apply {
//                                putParcelable(
//                                    Result.RECIPE_KEY,
////                                    recipe
//                                )
//                            }
//                            setFragmentResult(TAG, result)
//                            dismiss()
                        }
                        is RateRecipeUiState.Failed -> {
                            Toast.makeText(context, "평가가 반영되지 않았습니다. 다시 시도해주세요", Toast.LENGTH_SHORT)
                                .show()
                        }
                        RateRecipeUiState.Idle -> {}
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
//            uploadRating()
            val userKey = viewModel.rate.value?.userKey
            if (userKey == null) {
                viewModel.requestAddRateData() // db 에서 userKey 가 들어가야 함
            } else {
                viewModel.requestUpdateRateData()
            }
        }
    }

    private fun uploadRating() {
        val rating = binding.ratingBar.rating
        if (rating == 0f) { // TODO: 2022-12-18 이건 어디에다가 붙히는게 좋을까요?
            Toast.makeText(context, "평점을 매겨주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        val rate: RateEntity = makeRateData(rating)
//        FirebaseData.getInstance()
//            .uploadRating(recipe, rate, object : OnCompleteListener<RecipeData> {
//                override fun onComplete(isSuccess: Boolean, response: Response<RecipeData>?) {
//                    if (isSuccess) {
//                        Toast.makeText(context, "평가가 완료되었습니다.", Toast.LENGTH_SHORT).show()
//
//                        // 평가완료했을 때 평가완료된 데이터를 내려주자.
//                        val result = Bundle().apply {
//                            putParcelable(
//                                Result.RECIPE_KEY,
//                                response?.getData()?.toEntity()
//                            )
//                        }
//                        setFragmentResult(TAG, result)
//                        dismiss()
//                    } else {
//                        Toast.makeText(context, "평가가 실패하였습니다. 다시 시도해주세요", Toast.LENGTH_SHORT)
//                            .show()
//                    }
//                }
//            })
    }

    private fun makeRateData(rate: Float): RateEntity {
        val userKey: String = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
        val userName: String = FirebaseAuth.getInstance().currentUser?.displayName.toString()
        val profileImageUrl: String = FirebaseAuth.getInstance().currentUser?.photoUrl.toString()

        return RateEntity(
            key = "",   // RateServiceImpl 에서 document.id를 넣어주면 된다고 생각해서 "" 을 대입함
            userKey = userKey,
            rate = rate,
            date = Timestamp.now()
        )
    }

    object Result {
        const val RECIPE_KEY = "recipe_key"
    }

    companion object {
        fun newInstance(recipe: RecipeEntity): RatingDialogFragment =
            RatingDialogFragment().apply {
                arguments = Bundle().apply {
//                    putParcelable(KEY_RECIPE, recipe)
                }
            }

        private const val KEY_RECIPE = "recipe_key"

        const val TAG = "RatingDialog"
    }

    override fun onClick(view: View) {
    }
}
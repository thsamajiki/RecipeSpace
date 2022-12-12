package com.hero.recipespace.view.main.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.database.FirebaseData
import com.hero.recipespace.databinding.FragmentDialogRatingBinding
import com.hero.recipespace.domain.rate.entity.RateEntity
import com.hero.recipespace.domain.recipe.entity.RecipeEntity
import com.hero.recipespace.domain.recipe.mapper.toEntity
import com.hero.recipespace.listener.OnCompleteListener
import com.hero.recipespace.listener.Response
import com.hero.recipespace.view.main.recipe.viewmodel.RatingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RatingDialogFragment : DialogFragment(), View.OnClickListener {

    private var _binding: FragmentDialogRatingBinding? = null
    private val binding: FragmentDialogRatingBinding
        get() = _binding!!

    private val recipe: RecipeEntity by lazy {
        requireArguments().getParcelable(KEY_RECIPE)!!
    }

    private val viewModel by viewModels<RatingViewModel>()

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

    private fun setupViewModel() {
        with(viewModel) {

        }
    }

    private fun setupListeners() {
        binding.tvCancel.setOnClickListener {
            dismiss()
        }
        binding.tvConfirm.setOnClickListener {
            uploadRating()
        }
    }

    private fun uploadRating() {
        val rating = binding.ratingBar.rating
        if (rating == 0f) {
            Toast.makeText(context, "평점을 매겨주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        val rate: RateEntity = makeRateData(rating)
        FirebaseData.getInstance()
            .uploadRating(recipe, rate, object : OnCompleteListener<RecipeData> {
                override fun onComplete(isSuccess: Boolean, response: Response<RecipeData>?) {
                    if (isSuccess) {
                        Toast.makeText(context, "평가가 완료되었습니다.", Toast.LENGTH_SHORT).show()

                        // 평가완료했을 때 평가완료된 데이터를 내려주자.
                        val result = Bundle().apply {
                            putParcelable(
                                Result.RECIPE_KEY,
                                response?.getData()?.toEntity()
                            )
                        }
                        setFragmentResult(TAG, result)
                        dismiss()
                    } else {
                        Toast.makeText(context, "평가가 실패하였습니다. 다시 시도해주세요", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            })
    }

    private fun makeRateData(rate: Float): RateEntity {
        val userKey: String = FirebaseAuth.getInstance().currentUser.uid
        val userName: String = FirebaseAuth.getInstance().currentUser.displayName.toString()
        val profileImageUrl: String = FirebaseAuth.getInstance().currentUser.photoUrl.toString()
        val rateData = RateEntity(
            key = ,
            userKey = userKey,
            userName = userName,
            profileImageUrl = profileImageUrl,
            rate = rate,
            date = Timestamp.now()
        )
        rateData.date = Timestamp.now()
        rateData.profileImageUrl = profileImageUrl
        rateData.userKey = userKey
        rateData.key = userKey
        rateData.userName = userName
        rateData.rate = rate

        return rateData
    }

    object Result {
        const val RECIPE_KEY = "recipe_key"
    }

    companion object {
        fun newInstance(recipe: RecipeEntity): RatingDialogFragment =
            RatingDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_RECIPE, recipe)
                }
            }

        private const val KEY_RECIPE = "recipe_key"

        const val TAG = "RatingDialog"
    }

    override fun onClick(view: View) {
    }
}
package com.hero.recipespace.view.main.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.hero.recipespace.R
import com.hero.recipespace.data.rate.RateData
import com.hero.recipespace.data.recipe.RecipeData
import com.hero.recipespace.database.FirebaseData
import com.hero.recipespace.databinding.FragmentDialogRatingBinding
import com.hero.recipespace.listener.OnCompleteListener
import com.hero.recipespace.listener.OnRatingUploadListener
import com.hero.recipespace.listener.Response
import com.hero.recipespace.util.MyInfoUtil

class RatingDialogFragment : DialogFragment(), View.OnClickListener {

    private var _binding: FragmentDialogRatingBinding? = null
    private val binding: FragmentDialogRatingBinding
        get() = _binding!!
    private var recipeData: RecipeData? = null

    private var onRatingUploadListener: OnRatingUploadListener? = null

    fun setOnRatingUploadListener(onRatingUploadListener: OnRatingUploadListener?) {
        this.onRatingUploadListener = onRatingUploadListener
    }

    fun setRecipeData(recipeData: RecipeData) {
        this.recipeData = recipeData
    }

    companion object {
        const val TAG = "RatingDialog"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogRatingBinding.inflate(inflater, container, false)

        binding.tvCancel.setOnClickListener {
            dismiss()
        }
        binding.tvConfirm.setOnClickListener {
            uploadRating()
        }

        return binding.root
    }

    private fun uploadRating() {
        val rating = binding.ratingBar.rating
        if (rating == 0f) {
            Toast.makeText(context, "평점을 매겨주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        val rateData: RateData = makeRateData(rating)
        FirebaseData.getInstance()
            .uploadRating(recipeData, rateData, object : OnCompleteListener<RecipeData> {
                override fun onComplete(isSuccess: Boolean, response: Response<RecipeData>?) {
                    if (isSuccess) {
                        Toast.makeText(context, "평가가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        onRatingUploadListener!!.onRatingUpload(response.getData())
                        dismiss()
                    } else {
                        Toast.makeText(context, "평가가 실패하였습니다. 다시 시도해주세요", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            })
    }

    private fun makeRateData(rate: Float): RateData {
        val userKey: String = FirebaseAuth.getInstance().currentUser.uid
        val userName: String = FirebaseAuth.getInstance().currentUser.displayName.toString()
        val profileUrl: String = FirebaseAuth.getInstance().currentUser.photoUrl.toString()
        val rateData = RateData()
        rateData.date = Timestamp.now()
        rateData.profileImageUrl = profileUrl
        rateData.userKey = userKey
        rateData.key = userKey
        rateData.userName = userName
        rateData.rate = rate

        return rateData
    }

    override fun onClick(view: View) {
    }
}
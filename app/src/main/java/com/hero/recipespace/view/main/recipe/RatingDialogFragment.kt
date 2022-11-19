package com.hero.recipespace.view.main.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.fragment.app.DialogFragment
import com.google.firebase.Timestamp
import com.hero.recipespace.R
import com.hero.recipespace.data.RateData
import com.hero.recipespace.data.RecipeData
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
    private val recipeData: RecipeData? = null

    private var onRatingUploadListener: OnRatingUploadListener? = null

    fun setOnRatingUploadListener(onRatingUploadListener: OnRatingUploadListener?) {
        this.onRatingUploadListener = onRatingUploadListener
    }

    fun setRecipeData(recipeData: RecipeData) {
        this.recipeData = recipeData
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDialogRatingBinding.inflate(inflater, container, false)


        binding.tvCancel.setOnClickListener {
            finish()
        }
        binding.tvConfirm.setOnClickListener {
            uploadRating()
        }

        return binding.root
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tv_cancel -> finish()
            R.id.tv_confirm -> uploadRating()
        }
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
        val userKey: String = MyInfoUtil.getInstance().getKey()
        val userName: String = MyInfoUtil.getInstance().getUserName(getContext())
        val profileUrl: String = MyInfoUtil.getInstance().getProfileImageUrl(getContext())
        val rateData = RateData()
        rateData.date = Timestamp.now()
        rateData.profileImageUrl = profileUrl
        rateData.userKey = userKey
        rateData.key = userKey
        rateData.userName = userName
        rateData.rate = rate

        return rateData
    }
}
package com.hero.recipespace.view.main.recipe

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Timestamp
import com.hero.recipespace.R
import com.hero.recipespace.data.RateData
import com.hero.recipespace.data.RecipeData
import com.hero.recipespace.database.FirebaseData
import com.hero.recipespace.databinding.DialogRatingBinding
import com.hero.recipespace.listener.OnCompleteListener
import com.hero.recipespace.listener.OnRatingUploadListener
import com.hero.recipespace.listener.Response
import com.hero.recipespace.util.MyInfoUtil
import java.security.AccessController.getContext

class RatingDialog : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: DialogRatingBinding
    private val recipeData: RecipeData? = null

    private var onRatingUploadListener: OnRatingUploadListener? = null

    fun setOnRatingUploadListener(onRatingUploadListener: OnRatingUploadListener?) {
        this.onRatingUploadListener = onRatingUploadListener
    }

    fun setRecipeData(recipeData: RecipeData) {
        this.recipeData = recipeData
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogRatingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvCancel.setOnClickListener {

        }
        binding.tvConfirm.setOnClickListener {

        }
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
            Toast.makeText(applicationContext, "평점을 매겨주세요.", Toast.LENGTH_SHORT).show()
            return
        }
        val rateData: RateData = makeRateData(rating)
        FirebaseData.getInstance()
            .uploadRating(recipeData, rateData, object : OnCompleteListener<RecipeData> {
                override fun onComplete(isSuccess: Boolean, response: Response<RecipeData>?) {
                    if (isSuccess) {
                        Toast.makeText(applicationContext, "평가가 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        onRatingUploadListener!!.onRatingUpload(response.getData())
                        dismiss()
                    } else {
                        Toast.makeText(applicationContext, "평가가 실패하였습니다. 다시 시도해주세요", Toast.LENGTH_SHORT)
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
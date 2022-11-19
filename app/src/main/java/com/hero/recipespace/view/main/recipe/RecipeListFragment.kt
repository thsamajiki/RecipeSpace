package com.hero.recipespace.view.main.recipe

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hero.recipespace.R
import com.hero.recipespace.data.RecipeData
import com.hero.recipespace.database.FirebaseData
import com.hero.recipespace.databinding.FragmentRecipeListBinding
import com.hero.recipespace.listener.OnCompleteListener
import com.hero.recipespace.listener.OnRatingUploadListener
import com.hero.recipespace.listener.OnRecyclerItemClickListener
import com.hero.recipespace.listener.Response
import com.hero.recipespace.view.post.PostActivity

class RecipeListFragment : Fragment(), View.OnClickListener, OnRecyclerItemClickListener<RecipeData>,
    OnRatingUploadListener {

    private val recipeDataList = listOf<RecipeData>()
    private var _binding: FragmentRecipeListBinding? = null
    private val binding get() = _binding!!

    private lateinit var recipeListAdapter: RecipeListAdapter
    private lateinit var postResultLauncher: ActivityResultLauncher<Intent>

    companion object {
        fun newInstance() = RecipeListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_list, container, false)
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView(binding.rvRecipe)
        downloadRecipeData()

        binding.btnPost.setOnClickListener {
            val intent = Intent(requireActivity(), PostActivity::class.java)
            postResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    val recipeData: RecipeData = data.getParcelableExtra(RecipeListFragment.EXTRA_RECIPE_DATA)
                    if (recipeData != null) {
                        recipeDataList.add(0, recipeData)
                        recipeListAdapter.notifyItemInserted(0)
                        binding.rvRecipe.smoothScrollToPosition(0)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initRecyclerView(recyclerView: RecyclerView) {
        recipeListAdapter = RecipeListAdapter()

        recyclerView.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = recipeListAdapter
        }
    }

    private fun downloadRecipeData() {
        FirebaseData.getInstance()
            .downloadRecipeData(object : OnCompleteListener<List<RecipeData>> {
                override fun onComplete(isSuccess: Boolean, response: Response<List<RecipeData>>?) {
                    if (isSuccess && response.isNotEmpty()) {
                        recipeDataList.clear()
                        recipeDataList.addAll(response.getData())
                        recipeListAdapter.notifyDataSetChanged()
                    }
                }
            })
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_post -> {
                val intent = Intent(requireActivity(), PostActivity::class.java)
                startActivityForResult(intent, RecipeListFragment.POST_REQ_CODE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RecipeListFragment.POST_REQ_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val recipeData: RecipeData = data.getParcelableExtra(RecipeListFragment.EXTRA_RECIPE_DATA)
            if (recipeData != null) {
                recipeDataList.add(0, recipeData)
                recipeListAdapter.notifyItemInserted(0)
                binding.rvRecipe.smoothScrollToPosition(0)
            }
        }
    }

    //    ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
    //            new ActivityResultContracts.StartActivityForResult(),
    //            new ActivityResultCallback<ActivityResult>() {
    //                @Override
    //                public void onActivityResult(ActivityResult result) {
    //                    if (result.getResultCode() == Activity.RESULT_OK) {
    //                        Log.d(TAG, "MainActivity로 돌아왔다. ");
    //                    }
    //                }
    //            });

    override fun onItemClick(position: Int, view: View, data: RecipeData) {
        when(view.id) {
            R.id.mcv_rating_container -> {
                val ratingDialogFragment = RatingDialogFragment(requireActivity())
                ratingDialogFragment.setOnRatingUploadListener(this)
                ratingDialogFragment.setRecipeData(data)
                ratingDialogFragment.show()
            }
            else -> {
                val intent = Intent(requireActivity(), RecipeDetailActivity::class.java)
                intent.putExtra(RecipeListFragment.EXTRA_RECIPE_DATA, data)
                startActivity(intent)
            }
        }
    }

    override fun onRatingUpload(recipeData: RecipeData?) {
        val index = recipeDataList.indexOf(recipeData)
        recipeDataList[index] = recipeData
        recipeListAdapter.notifyItemChanged(index)
    }
}
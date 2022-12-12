package com.hero.recipespace.ext

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import androidx.fragment.app.commitNow
import com.hero.recipespace.R
import com.hero.recipespace.databinding.FragmentLoadingBinding

class LoadingFragment : Fragment() {

    private var _binding: FragmentLoadingBinding? = null
    private val binding: FragmentLoadingBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_loading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setOnTouchListener { _, _ -> true }

        binding.progressBar.max = 100
    }

    fun setProgress(@androidx.annotation.IntRange(from = 0, to = 100) percent: Int) {
        binding.progressBar.progress = percent
    }

    companion object {
        fun newInstance() =
            LoadingFragment()
    }
}

fun FragmentActivity.setLoading(isLoading: Boolean) {
    if (isLoading) {
        showLoading()
    } else {
        hideLoading()
    }
}

fun FragmentActivity.showLoading() {
    val result = runCatching {
        val loadingFragment =
            supportFragmentManager.findFragmentByTag(LoadingFragment::class.java.simpleName)

        if (loadingFragment != null) {
            supportFragmentManager.commitNow(allowStateLoss = true) {
                show(loadingFragment)
            }

        } else {
            supportFragmentManager.commitNow(allowStateLoss = true) {
                val fragment = LoadingFragment.newInstance()
                add(android.R.id.content, fragment, fragment::class.java.simpleName)
            }
        }
    }

    if (result.isFailure) {
        Log.e("LoadingFragment", result.exceptionOrNull()?.cause.toString())
    }
}

fun FragmentActivity.hideLoading() {
    val result = runCatching {
        val loadingFragment =
            supportFragmentManager.findFragmentByTag(LoadingFragment::class.java.simpleName)

        if (loadingFragment != null) {
            supportFragmentManager.commit(allowStateLoss = true) {
                remove(loadingFragment)
            }
        }
    }

    if (result.isFailure) {
        Log.e("LoadingFragment", result.exceptionOrNull()?.cause.toString())
    }
}

fun FragmentActivity.setProgressPercent(percent: Int) {
    val result = runCatching {
        val loadingFragment: LoadingFragment? =
            supportFragmentManager.findFragmentByTag(LoadingFragment::class.java.simpleName) as? LoadingFragment

        loadingFragment?.setProgress(percent)
    }

    if (result.isFailure) {
        Log.e("LoadingFragment", result.exceptionOrNull()?.cause.toString())
    }
}



fun Fragment.setLoading(isLoading: Boolean) {
    if (isLoading) {
        showLoading()
    } else {
        hideLoading()
    }
}

fun Fragment.showLoading() {
    val result = runCatching {
        val loadingFragment =
            parentFragmentManager.findFragmentByTag(LoadingFragment::class.java.simpleName)

        if (loadingFragment != null) {
            parentFragmentManager.commitNow(allowStateLoss = true) {
                show(loadingFragment)
            }

        } else {
            val containerId = parentFragment?.id ?: android.R.id.content
            parentFragmentManager.commitNow(allowStateLoss = true) {
                val fragment = LoadingFragment.newInstance()
                add(containerId, fragment, fragment::class.java.simpleName)
            }
        }
    }

    if (result.isFailure) {
        Log.e("LoadingFragment", result.exceptionOrNull()?.cause.toString())
    }
}

fun Fragment.hideLoading() {
    val result = runCatching {
        val loadingFragment =
            parentFragmentManager.findFragmentByTag(LoadingFragment::class.java.simpleName)

        if (loadingFragment != null) {
            parentFragmentManager.commit(allowStateLoss = true) {
                remove(loadingFragment)
            }
        }
    }

    if (result.isFailure) {
        Log.e("LoadingFragment", result.exceptionOrNull()?.cause.toString())
    }
}

fun Fragment.setProgressPercent(percent: Int) {
    val result = runCatching {
        val loadingFragment: LoadingFragment? =
            parentFragmentManager.findFragmentByTag(LoadingFragment::class.java.simpleName) as? LoadingFragment

        loadingFragment?.setProgress(percent)
    }

    if (result.isFailure) {
        Log.e("LoadingFragment", result.exceptionOrNull()?.cause.toString())
    }
}
package com.hero.recipespace.view.main.account.setting.notice

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.google.android.material.card.MaterialCardView
import com.hero.recipespace.R
import com.hero.recipespace.data.NoticeData
import com.hero.recipespace.data.RecipeData
import com.hero.recipespace.databinding.ActivityNoticeListBinding
import com.hero.recipespace.databinding.ItemNoticeListBinding
import com.hero.recipespace.listener.OnRecyclerItemClickListener
import com.hero.recipespace.view.BaseAdapter
import kotlinx.coroutines.NonDisposableHandle.parent

class NoticeListAdapter(
): BaseAdapter<NoticeListAdapter.NoticeViewHolder, NoticeData>(), View.OnClickListener {

    private val noticeDataList = mutableListOf<NoticeData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {
        val binding = ItemNoticeListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return NoticeViewHolder(binding, getOnRecyclerItemClickListener()!!)
    }

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        val noticeData: NoticeData = noticeDataList[position]

        holder.setNotice(noticeData)
    }

    fun setNoticeList(noticeData: List<NoticeData>) {
        noticeDataList.clear()
        noticeDataList.addAll(noticeData)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return noticeDataList.size
    }

    override fun onClick(view: View?) {}

    class NoticeViewHolder(
        private val binding: ItemNoticeListBinding,
        private val itemClickListener: OnRecyclerItemClickListener<NoticeData>
    ) : RecyclerView.ViewHolder(binding.root) {

        fun setNotice(noticeData: NoticeData) {
            binding.tvTitleNoticeItem.text = noticeData.noticeTitle
            binding.tvContentNoticeItem.text = noticeData.noticeDesc
            binding.tvDateNoticeItem.text = noticeData.noticeDate

            itemView.setOnClickListener {
                itemClickListener.onItemClick(absoluteAdapterPosition, it, noticeData)

                if (binding.llContentNoticeItem.visibility == View.GONE) {
                    rotateView(binding.mcvNoticeItem)
                    binding.llContentNoticeItem.visibility = View.VISIBLE
                } else {
                    rotateView(binding.mcvNoticeItem)
                    binding.llContentNoticeItem.visibility = View.GONE
                }
            }
        }

        private fun rotateView(view: View) {
            var angle = 0
            if (view.rotation != -180f) {
                angle = -180
            }
            view.animate().rotation(angle.toFloat()).setDuration(200).start()
        }

    }
}
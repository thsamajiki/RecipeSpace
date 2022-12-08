package com.hero.recipespace.view.main.account.setting.notice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hero.recipespace.databinding.ItemNoticeListBinding
import com.hero.recipespace.domain.notice.entity.NoticeEntity
import com.hero.recipespace.view.BaseAdapter

class NoticeListAdapter(
    private val onClick: (NoticeEntity) -> Unit
): BaseAdapter<NoticeListAdapter.NoticeViewHolder, NoticeEntity>(), View.OnClickListener {

    private val noticeList = mutableListOf<NoticeEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {
        val binding = ItemNoticeListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return NoticeViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        val notice: NoticeEntity = noticeList[position]

        holder.setNotice(notice)
    }

    fun setNoticeList(noticeItem: List<NoticeEntity>) {
        noticeList.clear()
        noticeList.addAll(noticeItem)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return noticeList.size
    }

    override fun onClick(view: View?) {}

    class NoticeViewHolder(
        private val binding: ItemNoticeListBinding,
        private val onClick: (NoticeEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun setNotice(notice: NoticeEntity) {
            binding.root.setOnClickListener {
                onClick(notice)

                if (binding.llContentNoticeItem.visibility == View.GONE) {
                    rotateView(binding.mcvNoticeItem)
                    binding.llContentNoticeItem.visibility = View.VISIBLE
                } else {
                    rotateView(binding.mcvNoticeItem)
                    binding.llContentNoticeItem.visibility = View.GONE
                }
            }

            binding.notice = notice
            binding.executePendingBindings()
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
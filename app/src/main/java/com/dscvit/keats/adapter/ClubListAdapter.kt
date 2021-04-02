package com.dscvit.keats.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dscvit.keats.R
import com.dscvit.keats.databinding.ClubsListItemBinding
import com.dscvit.keats.model.clubs.ClubEntity

class ClubListAdapter(val context: Context, private val onClubListener: OnClubListener) :
    ListAdapter<ClubEntity, ClubListAdapter.ViewHolder>(ClubListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, onClubListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, context)
    }

    fun getClubId(position: Int): String {
        val item = getItem(position)
        return item.ClubId
    }

    class ViewHolder(
        private var binding: ClubsListItemBinding,
        private var onClubListener: OnClubListener
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        fun bind(clubs: ClubEntity, context: Context) {
            binding.mainClubCard.setOnClickListener(this)
            binding.clubName.text = clubs.ClubName
            binding.clubHostName.text = context.getString(R.string.club_host_text, clubs.HostName)
            binding.clubType.text = if (clubs.Private) {
                context.getString(R.string.club_type_private)
            } else {
                context.getString(R.string.club_type_public)
            }
            val clubImg = binding.clubPhoto
            val imgUrl = clubs.ClubPic.toUri().buildUpon().scheme("https").build()
            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.setColorSchemeColors(
                Color.argb(100, 244, 121, 18)
            )
            circularProgressDrawable.start()
            Glide.with(clubImg.context)
                .load(imgUrl)
                .apply(
                    RequestOptions()
                        .placeholder(circularProgressDrawable)
                        .error(R.drawable.ic_broken_image)
                )
                .into(clubImg)
        }

        companion object {
            fun from(parent: ViewGroup, onClubListener: OnClubListener): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ClubsListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding, onClubListener)
            }
        }

        override fun onClick(v: View?) {
            onClubListener.onClubClick(adapterPosition)
        }
    }

    interface OnClubListener {
        fun onClubClick(position: Int)
    }
}

class ClubListDiffCallback : DiffUtil.ItemCallback<ClubEntity>() {
    override fun areItemsTheSame(oldItem: ClubEntity, newItem: ClubEntity): Boolean {
        return oldItem.ClubId == newItem.ClubId
    }

    override fun areContentsTheSame(oldItem: ClubEntity, newItem: ClubEntity): Boolean {
        return oldItem == newItem
    }
}

package com.dscvit.keats.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dscvit.keats.R
import com.dscvit.keats.databinding.ClubsListItemBinding
import com.dscvit.keats.model.clubs.ClubEntity

class ClubListAdapter :
    ListAdapter<ClubEntity, ClubListAdapter.ViewHolder>(ClubListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position)
    }

    class ViewHolder(private var binding: ClubsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(clubs: ClubEntity, position: Int) {
            val background = if (position % 2 == 1) {
                R.drawable.club_list_item_bg_one
            } else {
                R.drawable.club_list_item_bg_two
            }
            binding.mainClubCard.setBackgroundResource(background)
            val hostNameText = "Host: ${clubs.HostName}"
            binding.clubName.text = clubs.ClubName
            binding.clubHostName.text = hostNameText
            binding.clubType.text = if (clubs.Private) {
                "Private"
            } else {
                "Public"
            }
            val clubHostImg = binding.clubHostPhoto
            val imgUrl = clubs.HostProfile.toUri().buildUpon().scheme("https").build()
            Glide.with(clubHostImg.context)
                .load(imgUrl)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.ic_default_photo)
                        .error(R.drawable.ic_broken_image)
                )
                .into(clubHostImg)
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ClubsListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class ClubListDiffCallback : DiffUtil.ItemCallback<ClubEntity>() {
    override fun areItemsTheSame(oldItem: ClubEntity, newItem: ClubEntity): Boolean {
        return oldItem.ClubName == newItem.ClubName
    }

    override fun areContentsTheSame(oldItem: ClubEntity, newItem: ClubEntity): Boolean {
        return oldItem == newItem
    }
}

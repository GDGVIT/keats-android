package com.dscvit.keats.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
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
        holder.bind(item)
    }

    class ViewHolder(private var binding: ClubsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(clubs: ClubEntity) {
            binding.clubName.text = clubs.ClubName
            binding.clubHostName.text = clubs.HostName
            binding.clubType.text = if (clubs.Private) {
                "Private"
            } else {
                "Public"
            }
            val clubHostImg = binding.clubHostPhoto
            Glide.with(clubHostImg.context)
                .load(clubs.HostProfile)
                .apply(
                    RequestOptions()
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

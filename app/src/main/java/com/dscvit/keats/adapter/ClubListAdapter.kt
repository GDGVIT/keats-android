package com.dscvit.keats.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
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

class ClubListAdapter(val context: Context) :
    ListAdapter<ClubEntity, ClubListAdapter.ViewHolder>(ClubListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, context)
    }

    class ViewHolder(private var binding: ClubsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(clubs: ClubEntity, context: Context) {
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
            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.setColorSchemeColors(
                Color.argb(100, 244, 121, 18)
            )
            circularProgressDrawable.start()
            Glide.with(clubHostImg.context)
                .load(imgUrl)
                .apply(
                    RequestOptions()
                        .placeholder(circularProgressDrawable)
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

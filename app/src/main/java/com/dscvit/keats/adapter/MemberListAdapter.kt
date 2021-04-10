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
import com.dscvit.keats.databinding.MembersListItemBinding
import com.dscvit.keats.model.profile.UserEntity

class MemberListAdapter(val context: Context, private val onMemberListener: OnMemberListener) :
    ListAdapter<UserEntity, MemberListAdapter.ViewHolder>(MemberListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, onMemberListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        return holder.bind(item, context)
    }

    fun getMemberDetails(position: Int): UserEntity {
        return getItem(position)
    }

    class ViewHolder(
        private var binding: MembersListItemBinding,
        private val onMemberListener: OnMemberListener
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        fun bind(user: UserEntity, context: Context) {
            binding.mainMemberCard.setOnClickListener(this)
            binding.memberName.text = user.UserName
            binding.memberBio.text = user.UserBio
            val userImg = binding.memberProfilePhotoImg
            val imgUrl = user.ProfilePic.toUri().buildUpon().scheme("https").build()
            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.setColorSchemeColors(
                Color.argb(100, 244, 121, 18)
            )
            circularProgressDrawable.start()
            Glide.with(userImg.context)
                .load(imgUrl)
                .apply(
                    RequestOptions()
                        .placeholder(circularProgressDrawable)
                        .error(R.drawable.ic_default_photo)
                )
                .into(userImg)
        }

        companion object {
            fun from(parent: ViewGroup, onMemberListener: OnMemberListener): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MembersListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding, onMemberListener)
            }
        }

        override fun onClick(v: View?) {
            onMemberListener.onMemberClick(adapterPosition)
        }
    }

    interface OnMemberListener {
        fun onMemberClick(position: Int)
    }
}

class MemberListDiffCallback : DiffUtil.ItemCallback<UserEntity>() {
    override fun areItemsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
        return oldItem.UserId == newItem.UserId
    }

    override fun areContentsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
        return oldItem == newItem
    }
}

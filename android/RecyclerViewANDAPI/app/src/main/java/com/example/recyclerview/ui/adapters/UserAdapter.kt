package com.example.recyclerview.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerview.data.dto.Result
import com.example.recyclerview.databinding.ProfileLayoutBinding
import com.example.recyclerview.logic.users.GetAllUsers
import com.lottiefiles.dotlottie.core.model.Config
import com.lottiefiles.dotlottie.core.util.DotLottieSource
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class UserAdapter : ListAdapter<Result, UserAdapter.UserVH>(UserDiff()) {

    fun loadUsers(scope: CoroutineScope) {
        scope.launch {
            try {
                submitList(GetAllUsers()().results)
            } catch (e: Exception) {
                Log.e("UserAdapter", "Error al cargar usuarios", e)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserVH {
        val binding = ProfileLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return UserVH(binding)
    }

    override fun onBindViewHolder(holder: UserVH, position: Int) {
        holder.render(getItem(position))
    }

    override fun onViewAttachedToWindow(holder: UserVH) {
        super.onViewAttachedToWindow(holder)
        holder.loadAnimation()
    }

    class UserVH(val binding: ProfileLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun loadAnimation() {
            binding.lottieView.load(
                Config.Builder()
                    .source(DotLottieSource.Asset("texting.json"))
                    .autoplay(true)
                    .loop(true)
                    .speed(1f)
                    .build()
            )
        }

        fun render(result: Result) {
            binding.username.text = result.login.username
            binding.name.text = "${result.name.first} ${result.name.last}"
            Picasso.get().load(result.picture.large).into(binding.profileImg)
        }
    }

    class UserDiff : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean =
            oldItem.login.uuid == newItem.login.uuid

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean =
            oldItem == newItem
    }
}

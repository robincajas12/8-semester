package com.example.recyclerview.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerview.data.dto.Result
import com.example.recyclerview.databinding.ProfileLayoutBinding
import com.example.recyclerview.logic.users.GetAllUsers
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserVH>() {

    var userList: List<Result> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun loadUsers(scope: CoroutineScope) {
        scope.launch {
            try {
                userList = GetAllUsers()().results
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
        holder.render(userList[position])
    }

    override fun getItemCount(): Int = userList.size

    class UserVH(val binding: ProfileLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun render(result: Result) {
            binding.username.text = result.login.username
            binding.name.text = "${result.name.first} ${result.name.last}"
            Picasso.get().load(result.picture.large).into(binding.profileImg)
        }
    }
}

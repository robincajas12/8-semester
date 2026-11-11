package com.example.recyclerview.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerview.data.dto.UserDto
import com.example.recyclerview.databinding.ProfileLayoutBinding
import com.example.recyclerview.logic.users.GetAllUsers
import com.squareup.picasso.Picasso

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserVH>(){
    var userList : List<UserDto> = GetAllUsers()()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserVH {

        val biding : ProfileLayoutBinding = ProfileLayoutBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return UserVH(biding)
    }

    override fun onBindViewHolder(
        holder: UserVH,
        position: Int
    ) {
        holder.render(userList[position])
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserVH(val binding : ProfileLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun render(userDto : UserDto)
        {
            binding.username.text = userDto.username
            binding.name.text = userDto.name
            Picasso.get().load(userDto.image).into(binding.profileImg)
        }
    }


}

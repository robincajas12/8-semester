package com.mobiledev.firstapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobiledev.firstapp.R
import com.mobiledev.firstapp.data.UserDTO
import com.mobiledev.firstapp.databinding.UserListLayoutBinding
import com.mobiledev.firstapp.logic.userUseCase.GetAllUsersUserCase
import com.squareup.picasso.Picasso

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserVH>() {
    var userList : List<UserDTO> = listOf()

    class UserVH(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        private var biding = UserListLayoutBinding.bind(itemView)
        fun render(userDTO: UserDTO)
        {
            biding.username.text = userDTO.username
            biding.name.text = userDTO.name
            Picasso.get().load(userDTO.image).into(biding.imgUser);

            // biding.imgUser.resources userDTO.
        }

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserVH {
        val myInflater = LayoutInflater.from(parent.context)
        return UserVH(myInflater.inflate(
            R.layout.user_list_layout,
            parent, false))
    }

    override fun onBindViewHolder(
        holder: UserVH,
        position: Int
    ) {
        holder.render(userList[position])
    }

    override fun getItemCount(): Int = userList.size

}
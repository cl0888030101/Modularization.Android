package com.eyeque.eyequeconnect.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.eyeque.eyequeconnect.databinding.RoleItemLayoutBinding
import okhttp3.internal.notify

class RoleAdapter(private var roleList: MutableList<String>): RecyclerView.Adapter<RoleAdapter.RoleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoleViewHolder {
        val binding = RoleItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RoleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RoleViewHolder, position: Int) {
        holder.bind(roleList[position])
    }

    override fun getItemCount(): Int {
        return roleList.size
    }

    class RoleViewHolder(val binding: RoleItemLayoutBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(roleName: String){
            binding.roleValueTv.text = roleName
        }
    }

    fun updateRoleList(roleList: MutableList<String>){
        this.roleList = roleList
        notifyDataSetChanged()
    }
}
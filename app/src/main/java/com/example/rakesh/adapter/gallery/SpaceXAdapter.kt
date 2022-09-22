package com.example.rakesh.adapter.gallery

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.rakesh.R
import com.example.rakesh.databinding.ItemMissionBinding
import com.example.rakesh.model.DataResponseItem

class SpaceXAdapter(private val listener: OnItemClickLisener) :
    RecyclerView.Adapter<SpaceXAdapter.ViewHolder>() {

    private var list: List<DataResponseItem> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun addItems(items: List<DataResponseItem>) {
        list = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemMissionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class ViewHolder(private val binding: ItemMissionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: DataResponseItem) {
            binding.apply {
                missionName.text = data.mission_name
                rocketName.text = data.rocket.rocket_name
                launchDate.text = data.launch_date_local
                launchSite.text = data.launch_site.site_name
                Glide.with(itemView)
                    .load(data.links.mission_patch)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(imageView)

                missionName.setOnClickListener {
                    listener.onItemClick(
                        text = data.mission_name,
                        data = data
                    )
                }
                rocketName.setOnClickListener {
                    listener.onItemClick(
                        text = data.rocket.rocket_name,
                        data = data
                    )
                }
                launchDate.setOnClickListener {
                    listener.onItemClick(
                        text = data.launch_date_local,
                        data = data
                    )
                }
                launchSite.setOnClickListener {
                    listener.onItemClick(
                        text = data.launch_site.site_name,
                        data = data
                    )
                }
            }
        }
    }

    interface OnItemClickLisener {
        fun onItemClick(text: String, data: DataResponseItem)
    }

    override fun getItemCount() = list.size
    override fun getItemViewType(position: Int) = position


}
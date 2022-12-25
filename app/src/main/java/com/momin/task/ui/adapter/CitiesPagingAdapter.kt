package com.momin.task.ui.adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.momin.task.BaseApplication
import com.momin.task.R
import com.momin.task.data.local.entity.DataEntity
import com.momin.task.databinding.ItemCityWeatherBinding
import java.util.*

class CitiesPagingAdapter :
    PagingDataAdapter<DataEntity, CitiesPagingAdapter.CityViewHolder>(DIFF_CALLBACK) {
    class CityViewHolder(val binding: ItemCityWeatherBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("ResourceType")
        fun bind(data: DataEntity?) {
            data?.let { it ->
                it.weather.forEach() {
                    binding.tvWeather.text = it.main

                    //check for weather type and set the icon..
                    checkForWeather(it.main)
                }
            }
        }

        private fun checkForWeather(weatherType: String) {
            when (weatherType) {
                "Clouds" -> {
                    binding.parentView.setCardBackgroundColor(
                        BaseApplication.context!!.resources.getColor(
                            R.color.bg_blue
                        )
                    )
                    binding.ivWeather.setImageDrawable(
                        BaseApplication.context!!.resources.getDrawable(
                            R.drawable.cloudy
                        )
                    )
                }
                "Clear" -> {
                    binding.parentView.setCardBackgroundColor(
                        BaseApplication.context!!.resources.getColor(
                            R.color.bg_orange
                        )
                    )
                    binding.ivWeather.setImageDrawable(
                        BaseApplication.context!!.resources.getDrawable(
                            R.drawable.sun
                        )
                    )
                }
                "Rain" -> {
                    binding.parentView.setCardBackgroundColor(
                        BaseApplication.context!!.resources.getColor(
                            R.color.bg_purple
                        )
                    )
                    binding.ivWeather.setImageDrawable(
                        BaseApplication.context!!.resources.getDrawable(
                            R.drawable.rain
                        )
                    )
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataEntity>() {

            override fun areItemsTheSame(oldItem: DataEntity, newItem: DataEntity): Boolean =
                oldItem.id == newItem.id

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: DataEntity, newItem: DataEntity): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCityWeatherBinding.inflate(layoutInflater, parent, false)
        return CityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        getItem(position)?.let {
            val data = getItem(position)
            holder.binding.data = data
            holder.bind(data)
            holder.binding.parentView.setOnClickListener {
                Toast.makeText(BaseApplication.context, data?.cityName, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

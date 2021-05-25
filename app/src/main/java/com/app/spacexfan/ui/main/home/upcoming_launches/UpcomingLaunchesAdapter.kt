package com.app.spacexfan.ui.main.home.upcoming_launches

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.spacexfan.R
import com.app.spacexfan.data.model.upcoming_launches.UpcomingLaunchesModelItem

class UpcomingLaunchesAdapter(
    private var launches: List<UpcomingLaunchesModelItem>,
    private val onLaunchClick: (launch: UpcomingLaunchesModelItem) -> Unit
)
    : RecyclerView.Adapter<UpcomingLaunchesAdapter.LaunchesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaunchesViewHolder {
        val view = LayoutInflater .from(parent.context)
            .inflate(R.layout.item_upcoming_launches, parent, false)
        return LaunchesViewHolder(view)
    }
    override fun getItemCount(): Int {
        return launches.size
    }

    override fun onBindViewHolder(holder: LaunchesViewHolder, position: Int) {
        holder.bind(launches[position])
    }

    fun updateLaunches(launches: List<UpcomingLaunchesModelItem>) {
        this.launches = launches
        notifyDataSetChanged()
    }

    fun clearAdapter() {
        this.launches = listOf()
        notifyDataSetChanged()
    }

    inner class LaunchesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textName : TextView = itemView.findViewById(R.id.textName)
        private val textDate : TextView = itemView.findViewById(R.id.textDate)
        private val textFlightNumber : TextView = itemView.findViewById(R.id.textFlightNumber)

        @SuppressLint("SetTextI18n")
        fun bind(launch: UpcomingLaunchesModelItem) {
            itemView.setOnClickListener {
                onLaunchClick.invoke(launch)
            }
            textName.text=launch.name
            textDate.text=launch.date_utc
            textFlightNumber.text=launch.flight_number.toString()
        }
    }
}
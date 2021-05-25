package com.app.spacexfan.ui.main.home.favorite_rockets

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.spacexfan.R
import com.app.spacexfan.data.model.rockets.RocketsModelItem
import com.app.spacexfan.utils.FavoriteInterface
import com.app.spacexfan.utils.GlideUtils

class FavoriteRocketsAdapter(private var rockets: List<RocketsModelItem>, private val favoriteInterface: FavoriteInterface,
                             private val onRocketClick: (rocket: RocketsModelItem) -> Unit)
    : RecyclerView.Adapter<FavoriteRocketsAdapter.RocketsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RocketsViewHolder {
        val view = LayoutInflater .from(parent.context)
            .inflate(R.layout.item_rockets, parent, false)
        return RocketsViewHolder(view)
    }
    override fun getItemCount(): Int {
        return rockets.size
    }

    override fun onBindViewHolder(holder: RocketsViewHolder, position: Int) {
        holder.bind(rockets[position])
    }

    fun updateRockets(rockets: List<RocketsModelItem>) {
        this.rockets = rockets
        notifyDataSetChanged()
    }

    fun clearAdapter() {
        this.rockets = listOf()
        notifyDataSetChanged()
    }

    inner class RocketsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgRocket : ImageView = itemView.findViewById(R.id.imgRocket)
        private val btnFavorite : ImageView = itemView.findViewById(R.id.btnFavorite)
        private val textName : TextView = itemView.findViewById(R.id.textName)

        @SuppressLint("SetTextI18n")
        fun bind(rocket: RocketsModelItem) {
            btnFavorite.setOnClickListener {
                if (rocket.isFavorite) {
                    rocket.isFavorite=false
                    favoriteInterface.favoriteClick(rocket)
                    btnFavorite.setImageDrawable(ContextCompat.getDrawable(btnFavorite.context,R.drawable.ic_star_empty))
                }
                else {
                    rocket.isFavorite=true
                    favoriteInterface.favoriteClick(rocket)
                    btnFavorite.setImageDrawable(ContextCompat.getDrawable(btnFavorite.context, R.drawable.ic_star))
                }
            }
            itemView.setOnClickListener {
                onRocketClick.invoke(rocket)
            }

            if (rocket.isFavorite)
                btnFavorite.setImageDrawable(ContextCompat.getDrawable(btnFavorite.context, R.drawable.ic_star))
            else
                btnFavorite.setImageDrawable(ContextCompat.getDrawable(btnFavorite.context, R.drawable.ic_star_empty))

            if (rocket.flickr_images.isNotEmpty())
                GlideUtils.urlToImageView(imgRocket.context,rocket.flickr_images[0],imgRocket)

            textName.text=rocket.name
        }
    }
}
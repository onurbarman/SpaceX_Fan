package com.app.spacexfan.ui.main.home.rockets

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.spacexfan.R
import com.app.spacexfan.data.model.favorites.FavoritesItem
import com.app.spacexfan.data.model.rockets.RocketsModel
import com.app.spacexfan.data.model.rockets.RocketsModelItem
import com.app.spacexfan.databinding.FragmentRocketsBinding
import com.app.spacexfan.ui.main.MainActivity
import com.app.spacexfan.ui.main.home.rocket_detail.RocketDetailFragment
import com.app.spacexfan.utils.FavoriteInterface
import com.app.spacexfan.utils.UpdateFragment
import com.app.spacexfan.utils.Utils
import com.google.firebase.database.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RocketsFragment : Fragment(R.layout.fragment_rockets), FavoriteInterface, UpdateFragment {

    private val viewModel: RocketsViewModel by viewModels()
    private lateinit var binding: FragmentRocketsBinding
    private lateinit var rocketsAdapter: RocketsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRocketsBinding.bind(view)
        initView()
        listenRocketsData()
    }

    private fun initView() {
        rocketsAdapter = RocketsAdapter(listOf(),this){rocket -> rocketClick(rocket) }
        binding.run {
            recyclerViewRockets.run {
                layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL,false)
                adapter=rocketsAdapter
            }
        }
        if (Utils.isNetworkAvailable(requireContext()))
            viewModel.getRockets()
        else
            Utils.showToast(requireContext(),"Lütfen internet bağlantınızı kontrol ediniz.")
    }

    private fun getFavoritesData(rockets: RocketsModel) {
        val reference: Query = FirebaseDatabase.getInstance().getReference("vestelTest")
        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val listFavorites: ArrayList<FavoritesItem> = arrayListOf()
                for(data in snapshot.children){
                    val item: FavoritesItem? = data.getValue(FavoritesItem::class.java)
                    item?.let {
                        listFavorites.add(item)
                    }
                }

                if (listFavorites.isNotEmpty()) {
                    for (rocket in rockets) {
                        for (favorite in listFavorites){
                            if (rocket.id==favorite.id)
                                rocket.isFavorite=true
                        }
                    }
                }

                rocketsAdapter.updateRockets(rockets)
                reference.removeEventListener(this)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun listenRocketsData(){
        viewModel.postRockets.observe(requireActivity(),{
            it?.let {
                if (it.isNotEmpty()){
                    getFavoritesData(it)
                }
            } ?: Utils.showToast(requireContext(),"Roketler getirilirken bir sorun oluştu")
        })
    }

    private fun rocketClick(rocket: RocketsModelItem) {
        val rocketDetailFragment = RocketDetailFragment(rocket.id,rocket.isFavorite,this)
        (activity as MainActivity).loadFragment(rocketDetailFragment,"RocketDetailFragment")
    }

    override fun favoriteClick(rocket: RocketsModelItem) {
        Utils.setFavoriteStatus(rocket)
    }

    override fun update() {
        if (Utils.isNetworkAvailable(requireContext())) {
            rocketsAdapter.clearAdapter()
            viewModel.getRockets()
        }
        else
            Utils.showToast(requireContext(),"Lütfen internet bağlantınızı kontrol ediniz.")
    }

}
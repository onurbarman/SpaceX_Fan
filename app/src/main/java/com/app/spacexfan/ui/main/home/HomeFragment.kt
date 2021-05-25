package com.app.spacexfan.ui.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.app.spacexfan.R
import com.app.spacexfan.databinding.FragmentHomeBinding
import com.app.spacexfan.ui.main.home.favorite_rockets.FavoriteRocketsFragment
import com.app.spacexfan.ui.main.home.rockets.RocketsFragment
import com.app.spacexfan.ui.main.home.upcoming_launches.UpcomingLaunchesFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: FragmentHomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        binding.navigationView.setOnNavigationItemSelectedListener(this)
        initView()
    }

    private fun initView() {
        val rocketsFragment = RocketsFragment()
        openFragment(rocketsFragment)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_rockets -> {
                val rocketsFragment = RocketsFragment()
                openFragment(rocketsFragment)
                return true
            }
            R.id.navigation_favorites -> {
                val favoritesFragment = FavoriteRocketsFragment()
                openFragment(favoritesFragment)
                return true
            }
            R.id.navigation_launches -> {
                val launchesFragment = UpcomingLaunchesFragment()
                openFragment(launchesFragment)
                return true
            }
        }
        return false
    }

    private fun openFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment).addToBackStack(null)
        }.commit()
    }
}
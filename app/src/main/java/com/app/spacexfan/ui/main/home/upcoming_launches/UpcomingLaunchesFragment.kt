package com.app.spacexfan.ui.main.home.upcoming_launches

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.spacexfan.R
import com.app.spacexfan.data.model.upcoming_launches.UpcomingLaunchesModelItem
import com.app.spacexfan.databinding.FragmentUpcomingLaunchesBinding
import com.app.spacexfan.ui.main.MainActivity
import com.app.spacexfan.ui.main.home.launch_detail.LaunchDetailFragment
import com.app.spacexfan.utils.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpcomingLaunchesFragment : Fragment(R.layout.fragment_upcoming_launches) {

    private val viewModel: UpcomingLaunchesViewModel by viewModels()
    private lateinit var binding: FragmentUpcomingLaunchesBinding
    private lateinit var launchesAdapter: UpcomingLaunchesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentUpcomingLaunchesBinding.bind(view)

        initView()
        listenRocketsData()
    }

    private fun initView() {
        launchesAdapter = UpcomingLaunchesAdapter(listOf()) {launch -> launchClick(launch) }
        binding.run {
            recyclerViewLaunches.run {
                layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
                adapter=launchesAdapter
            }
        }
        if (Utils.isNetworkAvailable(requireContext()))
            viewModel.getUpcomingLaunches()
        else
            Utils.showToast(requireContext(),"Lütfen internet bağlantınızı kontrol ediniz.")
    }



    private fun listenRocketsData(){
        viewModel.postUpcomingLaunches.observe(requireActivity(),{
            it?.let {
                if (it.isNotEmpty()){
                    launchesAdapter.updateLaunches(it)
                }
            } ?: Utils.showToast(requireContext(),"Roketler getirilirken bir sorun oluştu")
        })
    }

    private fun launchClick(launch: UpcomingLaunchesModelItem) {
        val launchDetailFragment = LaunchDetailFragment(launch.id)
        (activity as MainActivity).loadFragment(launchDetailFragment,"LaunchDetailFragment")
    }

}
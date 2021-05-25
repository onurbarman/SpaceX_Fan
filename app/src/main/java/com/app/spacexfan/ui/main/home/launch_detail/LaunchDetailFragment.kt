package com.app.spacexfan.ui.main.home.launch_detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.app.spacexfan.R
import com.app.spacexfan.databinding.FragmentLaunchDetailBinding
import com.app.spacexfan.ui.main.MainActivity
import com.app.spacexfan.utils.GlideUtils
import com.app.spacexfan.utils.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LaunchDetailFragment(private val id: String) : Fragment(R.layout.fragment_launch_detail) {
    private val viewModel: LaunchDetailViewModel by viewModels()
    private lateinit var binding: FragmentLaunchDetailBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLaunchDetailBinding.bind(view)

        initView()
        initClick()
        listenLaunchDetailData()
    }

    private fun initView() {
        if (Utils.isNetworkAvailable(requireContext()))
            viewModel.getLaunchDetail(id)
        else
            Utils.showToast(requireContext(), "Lütfen internet bağlantınızı kontrol ediniz.")
    }

    private fun initClick() {
        binding.btnBack.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }
    }

    private fun listenLaunchDetailData() {
        viewModel.postLaunchDetail.observe(requireActivity(), {
            if (it != null) {
                binding.run {
                    textName.text = it.name
                    textDate.text = it.date_utc
                    textFlightNumber.text = it.flight_number.toString()
                    it.links.patch.small?.run {
                        imgLaunch.visibility = View.VISIBLE
                        GlideUtils.urlToImageView(requireContext(), this, imgLaunch)
                    }
                }
            } else
                Utils.showToast(
                    requireContext(),
                    "Fırlatma detayları getirilirken bir sorun oluştu."
                )
        })
    }

}
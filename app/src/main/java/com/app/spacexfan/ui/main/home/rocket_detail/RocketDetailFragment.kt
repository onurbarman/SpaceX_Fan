package com.app.spacexfan.ui.main.home.rocket_detail

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.app.spacexfan.R
import com.app.spacexfan.data.model.rockets.RocketsModelItem
import com.app.spacexfan.databinding.FragmentRocketDetailBinding
import com.app.spacexfan.ui.main.MainActivity
import com.app.spacexfan.utils.GlideUtils
import com.app.spacexfan.utils.UpdateFragment
import com.app.spacexfan.utils.Utils
import com.jama.carouselview.enums.IndicatorAnimationType
import com.jama.carouselview.enums.OffsetType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RocketDetailFragment(private val id: String, private var isFavorite: Boolean,private val updateFragment: UpdateFragment) : Fragment(R.layout.fragment_rocket_detail) {
    private val viewModel: RocketDetailViewModel by viewModels()
    private lateinit var binding: FragmentRocketDetailBinding
    private var currentRocket: RocketsModelItem? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRocketDetailBinding.bind(view)

        initView()
        initClick()
        listenRocketDetailData()
    }

    private fun initView() {
        if (isFavorite)
            binding.btnFavorite.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_star))

        if (Utils.isNetworkAvailable(requireContext()))
            viewModel.getRocketDetail(id)
        else
            Utils.showToast(requireContext(),"Lütfen internet bağlantınızı kontrol ediniz.")
    }

    private fun initClick() {
        binding.run {

            btnBack.setOnClickListener {
                (activity as MainActivity).onBackPressed()
            }

            btnFavorite.setOnClickListener {
                currentRocket?.let {
                    if (it.isFavorite){
                        it.isFavorite=false
                        btnFavorite.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_star_empty))
                    }
                    else{
                        it.isFavorite=true
                        btnFavorite.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_star))
                    }

                    Utils.setFavoriteStatus(it)
                }
            }
        }
    }

    private fun initCarousel(images: List<String>) {
        binding.carouselView.apply {
            size = images.size
            resource = R.layout.carousel_home_item
            autoPlay = true
            indicatorAnimationType = IndicatorAnimationType.THIN_WORM
            carouselOffset = OffsetType.CENTER
            setCarouselViewListener { view, position ->
                val imageView = view.findViewById<ImageView>(R.id.imgCarousel)
                var imgPath : String? = images[position]
                if (imgPath.isNullOrEmpty())
                    imgPath=""
                GlideUtils.urlToImageView(requireContext(),imgPath,imageView)
            }
            show()
        }
    }


    private fun listenRocketDetailData(){
        viewModel.postRocketDetail.observe(requireActivity(),{
            it?.let {
                binding.run {
                    currentRocket=it
                    currentRocket!!.isFavorite=isFavorite
                    textName.text=it.name
                    textDesciption.text=it.description
                    initCarousel(it.flickr_images)

                }
            } ?: Utils.showToast(requireContext(),"Roket detayları getirilirken bir sorun oluştu.")
        })
    }

    override fun onDetach() {
        updateFragment.update()
        super.onDetach()
    }

}
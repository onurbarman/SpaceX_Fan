package com.app.spacexfan.ui.main.home.favorite_rockets

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.spacexfan.R
import com.app.spacexfan.data.model.favorites.FavoritesItem
import com.app.spacexfan.data.model.rockets.RocketsModelItem
import com.app.spacexfan.databinding.BottomSheetFirebaseAuthBinding
import com.app.spacexfan.databinding.FragmentFavoriteRocketsBinding
import com.app.spacexfan.ui.main.MainActivity
import com.app.spacexfan.ui.main.home.rocket_detail.RocketDetailFragment
import com.app.spacexfan.utils.FavoriteInterface
import com.app.spacexfan.utils.UpdateFragment
import com.app.spacexfan.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executor

@AndroidEntryPoint
class FavoriteRocketsFragment : Fragment(R.layout.fragment_favorite_rockets), FavoriteInterface, UpdateFragment {

    private lateinit var binding: FragmentFavoriteRocketsBinding
    private lateinit var favoritesAdapter: FavoriteRocketsAdapter

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoriteRocketsBinding.bind(view)

        initView()
        initClick()
    }

    private fun initClick() {
        binding.btnLogin.setOnClickListener {
            if (this::biometricPrompt.isInitialized)
                if (this::promptInfo.isInitialized)
                    biometricPrompt.authenticate(promptInfo)
        }
    }

    private fun initView() {
        auth = FirebaseAuth.getInstance()
        executor = ContextCompat.getMainExecutor(requireContext())
        favoritesAdapter = FavoriteRocketsAdapter(listOf(),this){rocket -> rocketClick(rocket) }
        binding.run {
            recyclerViewRockets.run {
                layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL,false)
                adapter=favoritesAdapter
            }
        }

        initBiometricLogin()
    }

    private fun initBiometricLogin() {
        biometricPrompt = BiometricPrompt(requireActivity(), executor, object :
            BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                openLoginBottomSheet()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                binding.run {
                    layoutPermissionDenied.visibility=View.GONE
                    constraintFavorites.visibility=View.VISIBLE
                }
                getFavorites()
            }

            override fun onAuthenticationFailed() {
                openLoginBottomSheet()
            }
        })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Parmak İzi İle Giriş")
            .setDescription("Giriş Yapmak İçin Parmak İzinizi Okutunuz.")
            .setNegativeButtonText("E Posta Kullan")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }


    @SuppressLint("InflateParams")
    private fun openLoginBottomSheet() {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_firebase_auth, null)
        val dialogBinding: BottomSheetFirebaseAuthBinding = BottomSheetFirebaseAuthBinding.bind(view)
        dialog.setContentView(view)
        dialog.setCancelable(false)
        dialog.behavior.isDraggable=false
        dialog.show()

        if (view != null) {
            dialogBinding.run {
                btnLogin.setOnClickListener {
                    loginWithFirebase(dialogBinding.editEmail.text.toString(),dialogBinding.editPassword.text.toString(),dialog)
                    
                }

                btnCancel.setOnClickListener {
                    binding.layoutPermissionDenied.visibility=View.VISIBLE
                    dialog.dismiss()
                }
            }

        }
    }

    private fun loginWithFirebase(email: String, password: String, dialog: BottomSheetDialog) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    binding.run {
                        layoutPermissionDenied.visibility=View.GONE
                        constraintFavorites.visibility=View.VISIBLE
                    }
                    dialog.dismiss()
                    getFavorites()
                } else {
                    Utils.showToast(requireContext(),"Giriş Bilgilerinizi Kontrol Ediniz.")
                }
            }
    }

    private fun getFavorites() {
        val reference: Query = FirebaseDatabase.getInstance().getReference("vestelTest")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listFavorites: ArrayList<FavoritesItem> = arrayListOf()
                val listRockets : ArrayList<RocketsModelItem> = arrayListOf()
                for(data in snapshot.children){
                    val item: FavoritesItem? = data.getValue(FavoritesItem::class.java)
                    item?.let {
                        listFavorites.add(item)
                    }
                }

                if (listFavorites.isNotEmpty()) {
                    for (item in listFavorites){
                        item.run {
                            val listImages: ArrayList<String> = arrayListOf()
                            listImages.add(image)
                            val rocket = RocketsModelItem(id,name,"",listImages,true)
                            listRockets.add(rocket)
                        }

                    }
                    favoritesAdapter.clearAdapter()
                    favoritesAdapter.updateRockets(listRockets)
                }

                reference.removeEventListener(this)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun rocketClick(rocket: RocketsModelItem) {
        val rocketDetailFragment = RocketDetailFragment(rocket.id,rocket.isFavorite,this)
        (activity as MainActivity).loadFragment(rocketDetailFragment,"RocketDetailFragment")
    }

    override fun favoriteClick(rocket: RocketsModelItem) {
        Utils.setFavoriteStatus(rocket)
    }

    override fun onDetach() {
        FirebaseAuth.getInstance().signOut()
        super.onDetach()
    }

    override fun update() {
        getFavorites()
    }
}
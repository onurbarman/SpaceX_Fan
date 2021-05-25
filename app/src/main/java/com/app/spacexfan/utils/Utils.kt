package com.app.spacexfan.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.Toast
import com.app.spacexfan.data.model.rockets.RocketsModelItem
import com.app.spacexfan.data.remote.Resource
import com.google.firebase.database.*
import retrofit2.Response

object Utils {
    fun showToast(context : Context, message : String) = Toast.makeText(context,message, Toast.LENGTH_SHORT).show()

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }

    fun setFavoriteStatus(
        rocket: RocketsModelItem
    ) {
        val reference = FirebaseDatabase.getInstance().getReference("vestelTest")
        if (rocket.isFavorite) {
            reference.child(rocket.id).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        val hashMap: HashMap<String, String> = hashMapOf()
                        hashMap.put("id", rocket.id)
                        hashMap.put("name", rocket.name)
                        if (rocket.flickr_images.isNotEmpty())
                            hashMap.put("image", rocket.flickr_images[0])
                        else
                            hashMap.put("image", "")
                        reference.child(rocket.id).setValue(hashMap)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })

        } else {
            reference.child(rocket.id).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        reference.child(rocket.id).removeValue()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        }
    }

    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): Resource<T> {
        return try {
            val myResp = call.invoke()
            if (myResp.isSuccessful) {
                Resource.success(myResp.body()!!)
            } else {
                Resource.error(myResp.errorBody()?.string() ?: "Something goes wrong")
            }

        } catch (e: Exception) {
            Resource.error(e.message ?: "Internet error runs")
        }
    }
}
package com.lsm.supermemories.ui.memories.list

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.lsm.supermemories.MainApplication
import com.lsm.supermemories.R

class MapsFragment : Fragment()//,OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
{
    private val callback = OnMapReadyCallback { googleMap ->

        val context = MainApplication.applicationContext()
        var sharedPreferences = context.getSharedPreferences("latitude", Context.MODE_PRIVATE)
        val one = sharedPreferences.getFloat("latitude", 0.0F)
        var sharedPreferences2 = context.getSharedPreferences("longitude", Context.MODE_PRIVATE)
        val two = sharedPreferences2.getFloat("longitude", 0.0F)
        val sydney = LatLng(one.toDouble(), two.toDouble())
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        val editor2: SharedPreferences.Editor = sharedPreferences2.edit()
        editor.remove("latitude").clear().apply()
        editor2.remove("longitude").clear().apply()
        googleMap.addMarker(MarkerOptions().position(sydney).title("Your last location"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}

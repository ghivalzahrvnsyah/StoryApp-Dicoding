package com.ghivalhrvnsyah.storyappdicoding.view.maps

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ghivalhrvnsyah.storyappdicoding.R
import com.ghivalhrvnsyah.storyappdicoding.ViewModelFactory
import com.ghivalhrvnsyah.storyappdicoding.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val viewModel by viewModels<MapsViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private val boundsBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mendapatkan fragment peta dan memberi tahu saat peta siap digunakan.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    // Mengatur opsi menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        return true
    }

    // Mengatur tindakan saat opsi menu dipilih
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.normal_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_NORMAL
                true
            }
            R.id.satellite_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
                true
            }
            R.id.terrain_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
                true
            }
            R.id.hybrid_type -> {
                mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    // Mendapatkan marker dan menampilkan cerita pada peta
    private fun getMarker() {
        viewModel.getMarker().observe(this) { marker ->
            marker.listStory.forEach { storyMarker ->
                val latLng = LatLng(storyMarker.lat ?: 0.0, storyMarker.lon ?: 0.0)
                mMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(storyMarker.name)
                        .snippet(storyMarker.description)
                )
                boundsBuilder.include(latLng)
            }
            val bounds: LatLngBounds = boundsBuilder.build()
            mMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(
                    bounds,
                    resources.displayMetrics.widthPixels,
                    resources.displayMetrics.heightPixels,
                    300
                )
            )
        }
    }

    // Saat peta siap digunakan
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        getMarker()
    }
}

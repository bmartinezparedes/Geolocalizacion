package com.kiskos.geolocalizacion

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.kiskos.geolocalizacion.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val PERMISO_LOCALIZACION: Int=3
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        comprobarPermisos()
        //Hago visible los botones para apliar y desampliar el mapa
        mMap.uiSettings.isZoomControlsEnabled=true
        //Creo una variable con una latitud y longitud
        val centro = LatLng(42.23656846001073, -8.714151073325072)
        //Añado al mapa una marca a partir de la variable anterior y le añado un titulo
        mMap.addMarker(MarkerOptions().position(centro).title("Centro de explotacion legal"))
        //Le digo al mapa que centre la camara en el punto
        mMap.moveCamera(CameraUpdateFactory.newLatLng(centro))
    }

    private fun comprobarPermisos(){
        when{
            ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED->{
                Log.i("Permisos","permiso garantozado")
                mensajeUsuario("Tienes Permisos")
                }
            shouldShowRequestPermissionRationale (Manifest.permission.ACCESS_FINE_LOCATION
            )->{
                mensajeUsuario("Da permisos en ajustes")
            }
            else->{
                requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),PERMISO_LOCALIZACION)
            }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISO_LOCALIZACION->{
                if(grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    mMap.isMyLocationEnabled = true
                }
            }
            else->{
                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            }
        }
    }

    fun mensajeUsuario(mensaje:String){
        Toast.makeText(this,mensaje,Toast.LENGTH_LONG).show()
    }

}
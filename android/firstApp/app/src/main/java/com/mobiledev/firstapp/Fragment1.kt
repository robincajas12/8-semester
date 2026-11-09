package com.mobiledev.firstapp

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mobiledev.firstapp.databinding.Fragment1Binding

class Fragment1 : Fragment() {

    // Binding solo válido entre onCreateView y onDestroyView
    private var _binding: Fragment1Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = Fragment1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvFragment1.text = "Fragment 1"

        // Navega al Fragment 2 cambiando el item del bottom navigation.
        // MainActivity escucha el cambio y hace el replace del fragmento.
        binding.btnGoToFragment2.setOnClickListener {
            (requireActivity() as MainActivity).binding.bottomNavigation.selectedItemId = R.id.f2
        }

        // Abre la app de mapas en una ubicación random con un Intent geo:
        // intent.resolveActivity() verifica que exista una Activity que abra el geo:
        binding.btnOpenMap.setOnClickListener {
            val lat = (-90..90).random()
            val lng = (-180..180).random()
            val uri = Uri.parse("geo:$lat,$lng?q=$lat,$lng")
            val intent = Intent(Intent.ACTION_VIEW, uri)

          /*  intent.resolveActivity(requireContext().packageManager)?.let {
                startActivity(intent)
            }*/


            val playstore = Intent(Intent.ACTION_VIEW)
            playstore.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.whatsapp"))
            startActivity(playstore)
        }
    }
    private fun checkPackages(name: String) : Boolean
    {
        try {

             requireContext().packageManager.getPackageInfo(name, PackageManager.GET_ACTIVITIES)
            return true
        }catch (ex: Exception)
        {
            Log.d("error", "XD")
        }
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

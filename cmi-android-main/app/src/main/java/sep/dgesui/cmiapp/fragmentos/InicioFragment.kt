package sep.dgesui.cmiapp.fragmentos

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import sep.dgesui.cmiapp.Communicator
import sep.dgesui.cmiapp.Home
import sep.dgesui.cmiapp.R
import sep.dgesui.cmiapp.databinding.ActivityMainBinding

class InicioFragment : Fragment(R.layout.fragment_inicio) {

    private lateinit var communicator: Communicator
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        communicator = requireActivity() as Communicator

        when {
            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
                binding.root.showSnackbar(view,
                    "Permiso requerido",
                    Snackbar.LENGTH_INDEFINITE,
                    null){}
            }

            else -> {
                val requestPermissionLauncher = (requireActivity() as Home).requestPermissionLauncher
                requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }

        view.findViewById<Button>(R.id.botonModuloI).setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView,
                    communicator.sendTokenToModuloIFragment(arguments?.getString("token")))
                addToBackStack("moduloUnoFragment")
                commit()
            }
        }
        view.findViewById<Button>(R.id.botonModuloII).setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView,
                        communicator.sendTokenToModuloIIFragment(arguments?.getString("token")))
                addToBackStack("moduloDosFragment")
                commit()
            }
        }
        view.findViewById<Button>(R.id.botonModuloIII).setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView,
                        communicator.sendTokenToModuloIIIFragment(arguments?.getString("token")))
                addToBackStack("moduloTresFragment")
                commit()
            }
        }
    }

    fun View.showSnackbar(
        view: View,
        msg: String,
        length: Int,
        actionMessage: CharSequence?,
        action: (View) -> Unit
    ) {
        val snackbar = Snackbar.make(view, msg, length)
        if (actionMessage != null) {
            snackbar.setAction(actionMessage) {
                action(this)
            }.show()
        } else {
            snackbar.show()
        }
    }
}
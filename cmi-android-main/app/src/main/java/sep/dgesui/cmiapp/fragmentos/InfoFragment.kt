package sep.dgesui.cmiapp.fragmentos

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import sep.dgesui.cmiapp.Communicator
import sep.dgesui.cmiapp.R

class InfoFragment : Fragment(R.layout.fragment_info) {

    private lateinit var communicator: Communicator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        communicator = requireActivity() as Communicator
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().supportFragmentManager.beginTransaction().apply {
                    val bundle = Bundle()
                    bundle.putString("token",arguments?.getString("token"))
                    val fragment = InicioFragment()
                    fragment.arguments = bundle
                    replace(R.id.fragmentContainerView,fragment)
                    addToBackStack(null)
                    commit()
                }
            }
        })
    }

}
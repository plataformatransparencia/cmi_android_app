package sep.dgesui.cmiapp.filtros

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import sep.dgesui.cmiapp.R

class FiltroPeriodo(val selectedItemFiltroPeriodo: SelectedItemFiltroPeriodo) : DialogFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = arguments?.getStringArray("response")?.let {
                ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item,
                    it.asList())
            }

        val spinner = view.findViewById<Spinner>(R.id.spinner)
        spinner.adapter = adapter

        view.findViewById<Button>(R.id.aplicar_filtro).setOnClickListener {
            selectedItemFiltroPeriodo.periodoSeleccionado(spinner.selectedItem.toString())
            selectedItemFiltroPeriodo.filtroAplicado(true)
            dialog?.dismiss()
        }

        view.findViewById<ImageView>(R.id.close).setOnClickListener {
            dialog?.dismiss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.filtro_periodo,container,false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
    }
}
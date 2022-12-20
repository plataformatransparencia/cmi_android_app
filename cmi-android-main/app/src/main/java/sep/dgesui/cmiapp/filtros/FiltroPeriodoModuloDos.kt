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
import org.json.JSONArray
import org.json.JSONObject
import sep.dgesui.cmiapp.R

class FiltroPeriodoModuloDos(val selectedItemFiltroPeriodoModuloDos: SelectedItemFiltroPeriodoModuloDos) : DialogFragment() {

    private val periodos = mutableListOf<String>()
    private val subsistemas = mutableListOf<String>()
    private val entidadesFederativas = mutableListOf<String>()
    private val universidades = mutableListOf<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val responseJson = arguments?.getString("response")?.let { JSONObject(it) }
        val periodosJsonArray = JSONArray(arguments?.getString("responsePeriodo"))
        val subsistemasJsonArray = responseJson?.getJSONArray("subsistemas")
        val entidadesFederativasJsonArray = responseJson?.getJSONArray("entidadesFederativas")
        val universidadesJsonArray = responseJson?.getJSONArray("universidades")

        if(periodos.isEmpty()){
            for (index in 0 until periodosJsonArray.length()){
                periodos.add(periodosJsonArray.getString(index))
            }
        }
        if (subsistemas.isEmpty()){
            for (index in 0 until subsistemasJsonArray?.length()!!){
                subsistemas.add(subsistemasJsonArray.getString(index))
            }
        }
        if (entidadesFederativas.isEmpty()){
            for (index in 0 until entidadesFederativasJsonArray?.length()!!){
                entidadesFederativas.add(entidadesFederativasJsonArray.getString(index))
            }
        }
        if (universidades.isEmpty()){
            for (index in 0 until universidadesJsonArray?.length()!!){
                universidades.add(universidadesJsonArray.getString(index))
            }
        }

        val adapterEjercicioFiscal = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item,periodos)
        val spinnerEjercicioFiscal = view.findViewById<Spinner>(R.id.spinner_periodo)
        spinnerEjercicioFiscal.adapter = adapterEjercicioFiscal

        val adapterEntidadFederativa = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item,entidadesFederativas)
        val spinnerEntidadFederativa = view.findViewById<Spinner>(R.id.spinner_entidad_federativa)
        spinnerEntidadFederativa.adapter = adapterEntidadFederativa

        val adapterSubsistema = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item,subsistemas)
        val spinnerSubsistema = view.findViewById<Spinner>(R.id.spinner_subsistema)
        spinnerSubsistema.adapter = adapterSubsistema

        val adapterUniversidad = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item,universidades)
        val spinnerUniversidad = view.findViewById<Spinner>(R.id.spinner_universidad)
        spinnerUniversidad.adapter = adapterUniversidad


        view.findViewById<ImageView>(R.id.close).setOnClickListener {
            dialog?.dismiss()
        }

        view.findViewById<Button>(R.id.aplicar_filtro).setOnClickListener {
            selectedItemFiltroPeriodoModuloDos.periodoSeleccionadoModuloII(spinnerEjercicioFiscal.selectedItem.toString())
            selectedItemFiltroPeriodoModuloDos.entidadFederativaSeleccionadaModuloII(spinnerEntidadFederativa.selectedItem.toString())
            selectedItemFiltroPeriodoModuloDos.subsistemaSeleccionadoModuloII(spinnerSubsistema.selectedItem.toString())
            selectedItemFiltroPeriodoModuloDos.universidadSeleccionadaModuloII(spinnerUniversidad.selectedItem.toString())
            selectedItemFiltroPeriodoModuloDos.filtroAplicadoModuloII(true)
            dialog?.dismiss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.filtro_periodo_modulo_dos,container,false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
    }
}
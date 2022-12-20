package sep.dgesui.cmiapp.filtros

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import androidx.fragment.app.DialogFragment
import org.json.JSONObject

class FiltroEjercicioFiscal(val selectedItemFiltroEjercicioFiscal: SelectedItemFiltroEjercicioFiscal) : DialogFragment() {

    private val anios = mutableListOf<String>()
    private val subsistemas = mutableListOf<String>()
    private val entidadesFederativas = mutableListOf<String>()
    private val universidades = mutableListOf<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val responseJson = arguments?.getString("response")?.let { JSONObject(it) }
        val aniosJsonArray = responseJson?.getJSONArray("anios")
        val subsistemasJsonArray = responseJson?.getJSONArray("subsistemas")
        val entidadesFederativasJsonArray = responseJson?.getJSONArray("entidadesFederativas")
        val universidadesJsonArray = responseJson?.getJSONArray("universidades")

        if(anios.isEmpty()){
            for (index in 0 until aniosJsonArray?.length()!!){
                anios.add(aniosJsonArray.getString(index))
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

        val adapterEjercicioFiscal = ArrayAdapter(requireContext(), R.layout.simple_spinner_dropdown_item,anios)
        val spinnerEjercicioFiscal = view.findViewById<Spinner>(sep.dgesui.cmiapp.R.id.spinner_ejercicio_fiscal)
        spinnerEjercicioFiscal.adapter = adapterEjercicioFiscal

        val adapterEntidadFederativa = ArrayAdapter(requireContext(), R.layout.simple_spinner_dropdown_item,entidadesFederativas)
        val spinnerEntidadFederativa = view.findViewById<Spinner>(sep.dgesui.cmiapp.R.id.spinner_entidad_federativa)
        spinnerEntidadFederativa.adapter = adapterEntidadFederativa

        val adapterSubsistema = ArrayAdapter(requireContext(), R.layout.simple_spinner_dropdown_item,subsistemas)
        val spinnerSubsistema = view.findViewById<Spinner>(sep.dgesui.cmiapp.R.id.spinner_subsistema)
        spinnerSubsistema.adapter = adapterSubsistema

        val adapterUniversidad = ArrayAdapter(requireContext(), R.layout.simple_spinner_dropdown_item,universidades)
        val spinnerUniversidad = view.findViewById<Spinner>(sep.dgesui.cmiapp.R.id.spinner_universidad)
        spinnerUniversidad.adapter = adapterUniversidad


        view.findViewById<ImageView>(sep.dgesui.cmiapp.R.id.close).setOnClickListener {
            dialog?.dismiss()
        }

        view.findViewById<Button>(sep.dgesui.cmiapp.R.id.aplicar_filtro).setOnClickListener {
            selectedItemFiltroEjercicioFiscal.anioSeleccionado(spinnerEjercicioFiscal.selectedItem.toString())
            selectedItemFiltroEjercicioFiscal.entidadFederativaSeleccionada(spinnerEntidadFederativa.selectedItem.toString())
            selectedItemFiltroEjercicioFiscal.subsistemaSeleccionado(spinnerSubsistema.selectedItem.toString())
            selectedItemFiltroEjercicioFiscal.universidadSeleccionada(spinnerUniversidad.selectedItem.toString())
            selectedItemFiltroEjercicioFiscal.filtroAplicado(true)
            dialog?.dismiss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(sep.dgesui.cmiapp.R.layout.filtro_ejercicio_fiscal,container,false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
    }
}
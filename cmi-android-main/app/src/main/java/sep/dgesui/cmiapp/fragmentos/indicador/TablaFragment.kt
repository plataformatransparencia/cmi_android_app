package sep.dgesui.cmiapp.fragmentos.indicador

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import sep.dgesui.cmiapp.Communicator
import sep.dgesui.cmiapp.R
import sep.dgesui.cmiapp.fragmentos.modulos.ModuloDosFragment
import sep.dgesui.cmiapp.fragmentos.modulos.ModuloTresFragment
import sep.dgesui.cmiapp.fragmentos.modulos.ModuloUnoFragment
import sep.dgesui.cmiapp.modelos.CategoriasModuloUno
import sep.dgesui.cmiapp.modelos.IndicadorModuloTres
import java.util.HashMap

class TablaFragment(private val indicadorOrigen: IndicadorFragment) : Fragment(R.layout.fragment_tabla) {

    private lateinit var communicator: Communicator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        communicator = requireActivity() as Communicator

        if(indicadorOrigen.moduloOrigen() is ModuloUnoFragment){
            val categories = categoriesOfModuloI()
            view.findViewById<LinearLayout>(R.id.parentTabla).addView(valoresIndicadorModuloI(categories))
        }else if(indicadorOrigen.moduloOrigen() is ModuloTresFragment){
            val categories = categoriesOfModuloIII()
            view.findViewById<LinearLayout>(R.id.parentTabla)
                .addView(valoresIndicadorModuloIII(categories))
        }else if (indicadorOrigen.moduloOrigen() is ModuloDosFragment){
            val categories = categoriesOfModuloII()
            view.findViewById<LinearLayout>(R.id.parentTabla)
                .addView(valoresIndicadorModuloII(categories))
        }
    }



    private fun valoresIndicadorModuloI(valores:Array<String>): ScrollView {
        val scrollView = ScrollView(context)
        scrollView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)

        val verticalLinearLayout = LinearLayout(context)
        verticalLinearLayout.orientation = LinearLayout.VERTICAL
        verticalLinearLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)

        valores.forEach { valor ->
            val linearLayout = LinearLayout(context)
            linearLayout.orientation = LinearLayout.HORIZONTAL
            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(0,100,0,0)
            linearLayout.layoutParams = layoutParams

            val imageView = ImageView(context)
            imageView.setImageResource(R.drawable.forward_icon)
            imageView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            imageView.setOnClickListener {
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.fragmentContainerView,
                        communicator.sendResponseToSeccionValoresFragment(arguments?.getString("response"),
                        indicadorOrigen,valor))
                    addToBackStack("seccionValoresFragment")
                    commit()
                }
            }

            val textView = TextView(context)
            textView.text = valor
            textView.setTextColor(resources.getColor(R.color.gob_green_light))
            textView.setTextSize(14f)
            textView.layoutParams = TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT,1f)

            linearLayout.addView(textView)
            linearLayout.addView(imageView)

            verticalLinearLayout.addView(linearLayout)
        }

        scrollView.addView(verticalLinearLayout)
        return scrollView
    }

    private fun valoresIndicadorModuloII(valores: Array<String>): ScrollView {
        val scrollView = ScrollView(context)
        scrollView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)

        val verticalLinearLayout = LinearLayout(context)
        verticalLinearLayout.orientation = LinearLayout.VERTICAL
        verticalLinearLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)

        when (indicadorOrigen.indicador().numIdentificacion()){
            7,8,9,10,11 -> {
                val layoutParamsTextView = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
                layoutParamsTextView.setMargins(0,100,0,0)

                val textViewSubsistema = TextView(context)
                textViewSubsistema.text = "Por Subsistemas"
                textViewSubsistema.setTypeface(null, Typeface.BOLD)
                textViewSubsistema.setTextColor(resources.getColor(R.color.gob_green_light))
                textViewSubsistema.setTextSize(14f)
                textViewSubsistema.layoutParams = layoutParamsTextView

                verticalLinearLayout.addView(textViewSubsistema)

                val categories = categoriesSubsistemaOfModuloII()

                if (categories.size == 0){
                    val noResultMessageLayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        100)
                    noResultMessageLayoutParams.setMargins(0,100,0,0)
                    val noResultMessage = TextView(context)
                    noResultMessage.layoutParams = noResultMessageLayoutParams
                    noResultMessage.text = "No hay datos para el filtro seleccionado."
                    noResultMessage.setTypeface(null, Typeface.BOLD)
                    noResultMessage.setTextSize(16f)
                    noResultMessage.background = resources.getDrawable(R.drawable.mensaje_sin_resultado_forma)
                    noResultMessage.gravity = Gravity.CENTER

                    verticalLinearLayout.addView(noResultMessage)
                }else{
                    for (index in 0 until categories.size){
                        val linearLayout = LinearLayout(context)
                        linearLayout.orientation = LinearLayout.HORIZONTAL
                        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
                        layoutParams.setMargins(0,100,0,0)
                        linearLayout.layoutParams = layoutParams

                        val imageView = ImageView(context)
                        imageView.setImageResource(R.drawable.forward_icon)
                        imageView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
                        imageView.setOnClickListener {
                            parentFragmentManager.beginTransaction().apply {
                                replace(R.id.fragmentContainerView,
                                    communicator.sendDataToSeccionValoresFragment(
                                        mapOf("tablaUniversidad" to null,
                                            "tablaSubsistema" to arguments?.getString("tablaSubsistema"),
                                        "ficha" to arguments?.getString("ficha")),
                                        indicadorOrigen,
                                        categories.get(index),
                                        index))
                                addToBackStack("seccionValoresFragment")
                                commit()
                            }
                        }

                        val textView = TextView(context)
                        textView.text = categories.get(index)
                        textView.setTextColor(resources.getColor(R.color.gob_green_light))
                        textView.setTextSize(14f)
                        textView.layoutParams = TableLayout.LayoutParams(
                            TableLayout.LayoutParams.WRAP_CONTENT,
                            TableLayout.LayoutParams.WRAP_CONTENT,1f)

                        linearLayout.addView(textView)
                        linearLayout.addView(imageView)
                        verticalLinearLayout.addView(linearLayout)
                    }
                }

                val textViewUniversidad = TextView(context)
                textViewUniversidad.text = "Por Universidades"
                textViewUniversidad.setTypeface(null, Typeface.BOLD)
                textViewUniversidad.setTextColor(resources.getColor(R.color.gob_green_light))
                textViewUniversidad.setTextSize(14f)
                textViewUniversidad.layoutParams = layoutParamsTextView

                verticalLinearLayout.addView(textViewUniversidad)
            }
        }

        if (valores.size == 0){
            val noResultMessageLayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                100)
            noResultMessageLayoutParams.setMargins(0,100,0,0)
            val noResultMessage = TextView(context)
            noResultMessage.layoutParams = noResultMessageLayoutParams
            noResultMessage.text = "No hay datos para el filtro seleccionado."
            noResultMessage.setTypeface(null, Typeface.BOLD)
            noResultMessage.setTextSize(16f)
            noResultMessage.background = resources.getDrawable(R.drawable.mensaje_sin_resultado_forma)
            noResultMessage.gravity = Gravity.CENTER

            verticalLinearLayout.addView(noResultMessage)
        }else{
            for (index in 0 until valores.size){
                val linearLayout = LinearLayout(context)
                linearLayout.orientation = LinearLayout.HORIZONTAL
                val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
                layoutParams.setMargins(0,100,0,0)
                linearLayout.layoutParams = layoutParams

                val imageView = ImageView(context)
                imageView.setImageResource(R.drawable.forward_icon)
                imageView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
                when (indicadorOrigen.indicador().numIdentificacion()) {
                    1,2,3,5,6,12,13,14 -> {
                        imageView.setOnClickListener {
                            parentFragmentManager.beginTransaction().apply {
                                replace(R.id.fragmentContainerView,
                                    communicator.sendDataToSeccionUnoFragment(arguments?.getString("response"),arguments?.getString("ficha"),
                                        indicadorOrigen,valores.get(index),index))
                                addToBackStack("seccionUnoFragment")
                                commit()
                            }
                        }
                    }
                    7, 8, 9, 10, 11 -> {
                        imageView.setOnClickListener {
                            parentFragmentManager.beginTransaction().apply {
                                replace(R.id.fragmentContainerView,
                                    communicator.sendDataToSeccionValoresFragment(
                                        mapOf("tablaUniversidad" to arguments?.getString("tablaUniversidad"),
                                            "tablaSubsistema" to null,
                                            "ficha" to arguments?.getString("ficha")),
                                        indicadorOrigen,
                                        valores.get(index),
                                        index))
                                addToBackStack("seccionValoresFragment")
                                commit()
                            }
                        }
                    }
                }


                val textView = TextView(context)
                textView.text = valores.get(index)
                textView.setTextColor(resources.getColor(R.color.gob_green_light))
                textView.setTextSize(14f)
                textView.layoutParams = TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT,1f)

                linearLayout.addView(textView)
                linearLayout.addView(imageView)

                verticalLinearLayout.addView(linearLayout)

            }
        }

        scrollView.addView(verticalLinearLayout)

        return scrollView
    }

    private fun valoresIndicadorModuloIII(valores:Array<String>): ScrollView {
        val scrollView = ScrollView(context)
        scrollView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)

        val verticalLinearLayout = LinearLayout(context)
        verticalLinearLayout.orientation = LinearLayout.VERTICAL
        verticalLinearLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)

        if(valores.size == 0){
            val noResultMessageLayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                100)
            noResultMessageLayoutParams.setMargins(0,100,0,0)
            val noResultMessage = TextView(context)
            noResultMessage.layoutParams = noResultMessageLayoutParams
            noResultMessage.text = "No hay datos para el filtro seleccionado."
            noResultMessage.setTypeface(null, Typeface.BOLD)
            noResultMessage.setTextSize(16f)
            noResultMessage.background = resources.getDrawable(R.drawable.mensaje_sin_resultado_forma)
            noResultMessage.gravity = Gravity.CENTER

            verticalLinearLayout.addView(noResultMessage)
        }else{
            for(index in 0 until valores.size){
                val linearLayout = LinearLayout(context)
                linearLayout.orientation = LinearLayout.HORIZONTAL
                val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
                layoutParams.setMargins(0,100,0,0)
                linearLayout.layoutParams = layoutParams

                val imageView = ImageView(context)
                imageView.setImageResource(R.drawable.forward_icon)
                imageView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
                imageView.setOnClickListener {
                    parentFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentContainerView,
                            communicator.sendDataToSeccionUnoFragment(
                                arrayOf(arguments?.getString("tabla"),
                                    arguments?.getString("totales"),arguments?.getString("ficha")),
                                indicadorOrigen,
                                valores.get(index),
                                arguments?.getString("token"),
                                index))
                        addToBackStack("seccionUnoFragment")
                        commit()
                    }
                }

                val textView = TextView(context)
                textView.text = valores.get(index)
                textView.setTextColor(resources.getColor(R.color.gob_green_light))
                textView.setTextSize(14f)
                textView.layoutParams = TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT,1f)

                linearLayout.addView(textView)
                linearLayout.addView(imageView)

                verticalLinearLayout.addView(linearLayout)
            }
        }

        scrollView.addView(verticalLinearLayout)
        return scrollView
    }

    private fun categoriesOfModuloI(): Array<String> {
        var categories : Array<String> = emptyArray()
        when(indicadorOrigen.indicador().numIdentificacion()){
                1 -> { categories = CategoriasModuloUno.MOD1_IND_1 }
                2 -> { categories = CategoriasModuloUno.MOD1_IND_2 }
                3 -> { categories = CategoriasModuloUno.MOD1_IND_3 }
                4 -> { categories = CategoriasModuloUno.MOD1_IND_4 }
                5 -> { categories = CategoriasModuloUno.MOD1_IND_5 }
                6 -> { categories = CategoriasModuloUno.MOD1_IND_6 }
                7 -> { categories = CategoriasModuloUno.MOD1_IND_7 }
                8 -> { categories = CategoriasModuloUno.MOD1_IND_8 }
        }
        return categories
    }

    private fun categoriesOfModuloII():Array<String>{
        val categories = mutableListOf<String>()
        when (indicadorOrigen.indicador().numIdentificacion()){
            1 -> {
                val responseArray = JSONArray(arguments?.getString("response"))
                for (index in 0 until responseArray.length()){
                    categories.add(responseArray
                        .getJSONObject(index)
                        .getJSONObject("entidadFederativa")
                        .getString("valor"))
                }
            }
            2,3 -> {
                val responseArray = JSONArray(arguments?.getString("response"))
                for (index in 0 until responseArray.length()){
                    categories.add(responseArray
                        .getJSONObject(index)
                        .getJSONObject("universidad")
                        .getString("nombre"))
                }
            }
            6,13,14 -> {
                val responseArray = JSONArray(arguments?.getString("response"))
                for (index in 0 until responseArray.length()){
                    categories.add(responseArray
                        .getJSONObject(index)
                        .getString("nombreUniversidad"))
                }
            }
            5 -> {
                val responseArray = JSONArray(arguments?.getString("response"))
                for (index in 0 until responseArray.length()){
                    categories.add(responseArray
                        .getJSONObject(index)
                        .getString("nombre"))
                }
            }
            12 -> {
                val responseArray = JSONArray(arguments?.getString("response"))
                for (index in 0 until responseArray.length()){
                    categories.add(responseArray
                        .getJSONObject(index)
                        .getString("entidadFederativa"))
                }
            }
            7,8,9,10,11 -> {
                val responseArray = JSONArray(arguments?.getString("tablaUniversidad"))
                for (index in 0 until responseArray.length()){
                    categories.add(responseArray
                        .getJSONObject(index)
                        .getJSONObject("universidad")
                        .getString("nombre"))
                }
            }
        }
        return categories.toTypedArray()
    }

    private fun categoriesSubsistemaOfModuloII():Array<String>{
        val categories = mutableListOf<String>()
        val responseArray = JSONArray(arguments?.getString("tablaSubsistema"))
        for (index in 0 until responseArray.length()){
            categories.add(responseArray
                .getJSONObject(index)
                .getString("subsistema"))
        }
        return categories.toTypedArray()
    }

    private fun categoriesOfModuloIII():Array<String> {
        val categories = mutableListOf<String>()
        val reponseArray = JSONArray(arguments?.getString("tabla"))
        when((indicadorOrigen.indicador() as IndicadorModuloTres).valorRaiz()){
            "universidad" -> {
                for(index in 0 until reponseArray.length()) {
                    categories.add(reponseArray
                        .getJSONObject(index)
                        .getJSONObject("universidad")
                        .getString("nombre"))
                }
            }
            "entidadFederativa" -> {
                for(index in 0 until reponseArray.length()) {
                    categories.add(reponseArray
                        .getJSONObject(index)
                        .getJSONObject("entidadFederativa")
                        .getString("valor"))
                }
            }
            "subsistema" -> {
                for(index in 0 until reponseArray.length()) {
                    categories.add(reponseArray
                        .getJSONObject(index)
                        .getJSONObject("subsistema")
                        .getString("valor"))
                }
            }
            "oscCentro" -> {
                for(index in 0 until reponseArray.length()) {
                    categories.add(reponseArray
                        .getJSONObject(index)
                        .getJSONObject("oscCentro")
                        .getString("nombre"))
                }
            }
        }
        return categories.toTypedArray()
    }
}
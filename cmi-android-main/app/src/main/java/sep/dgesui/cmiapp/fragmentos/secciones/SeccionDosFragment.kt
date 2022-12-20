package sep.dgesui.cmiapp.fragmentos.secciones

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import org.json.JSONArray
import org.json.JSONObject
import sep.dgesui.cmiapp.Communicator
import sep.dgesui.cmiapp.R
import sep.dgesui.cmiapp.fragmentos.indicador.IndicadorFragment
import sep.dgesui.cmiapp.fragmentos.modulos.ModuloTresFragment
import sep.dgesui.cmiapp.modelos.EtiquetasModuloTres
import java.text.NumberFormat
import java.util.*


class SeccionDosFragment(private val indicadorOrigen: IndicadorFragment,
                         private val titulo:String) : Fragment(R.layout.fragment_seccion_dos) , SeccionInterface {

    private lateinit var communicator: Communicator
    private lateinit var tabSeleccionado : String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        communicator = requireActivity() as Communicator

        view.findViewById<LinearLayout>(R.id.parentSeccionDos).addView(tituloSeccion())
        if(indicadorOrigen.moduloOrigen() is ModuloTresFragment)
            apartadoListaValoresModuloIII(view)
    }

    private fun tituloSeccion(): LinearLayout {
        val linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.HORIZONTAL
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(0,60,0,0)
        linearLayout.layoutParams = layoutParams

        val imageView = ImageView(context)
        imageView.setImageResource(R.drawable.back_icon)
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        imageView.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val textView = TextView(context)
        textView.text = titulo
        textView.setTextColor(resources.getColor(R.color.gob_green_light))
        textView.setTypeface(null, Typeface.BOLD)
        textView.setTextSize(16f)
        textView.gravity = Gravity.CENTER
        val textViewLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        textViewLayoutParams.setMargins(60,0,60,0)
        textView.layoutParams = textViewLayoutParams

        linearLayout.addView(imageView)
        linearLayout.addView(textView)

        return linearLayout
    }

    private fun apartadoListaValoresModuloIII(view: View){
        val scrollView = ScrollView(context)
        scrollView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)

        val verticalLinearLayout = LinearLayout(context)
        verticalLinearLayout.orientation = LinearLayout.VERTICAL
        verticalLinearLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)

        totalesRegistrados().forEach { (etiqueta , valor) ->
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(0,60,0,0)

            val label = TextView(context)
            label.text = etiqueta
            label.setTypeface(null, Typeface.BOLD)
            label.setTextColor(resources.getColor(R.color.gob_green_light))
            label.setTextSize(14f)
            label.layoutParams = layoutParams

            val value = TextView(context)
            value.text = valor
            value.setTextColor(resources.getColor(R.color.gob_green_light))
            value.setTextSize(14f)
            value.layoutParams = layoutParams

            verticalLinearLayout.addView(label)
            verticalLinearLayout.addView(value)
        }

        mesesRegistrados().forEach { mes ->
            val linearLayout = LinearLayout(context)
            linearLayout.orientation = LinearLayout.HORIZONTAL
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(0,60,0,0)
            linearLayout.layoutParams = layoutParams

            val imageView = ImageView(context)
            imageView.setImageResource(R.drawable.forward_icon)
            imageView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            imageView.setOnClickListener {
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.fragmentContainerView,
                        communicator.sendDataToSeccionValoresFragment(
                            arrayOf(arguments?.getString("tabla"),
                                arguments?.getString("totales"),arguments?.getString("ficha")),
                            this@SeccionDosFragment,
                            mes,
                            arguments?.getString("token"),
                            arguments?.getInt("indexOfElement"),
                            tabSeleccionado))
                    addToBackStack("seccionDosFragment")
                    commit()
                }
            }

            val textView = TextView(context)
            textView.text = mes
            textView.setTextColor(resources.getColor(R.color.gob_green_light))
            textView.setTextSize(14f)
            textView.layoutParams = TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT,1f)

            linearLayout.addView(textView)
            linearLayout.addView(imageView)
            verticalLinearLayout.addView(linearLayout)
        }

        subtotalesRegistrados().forEach { (etiqueta, valor) ->
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(0,60,0,0)

            val label = TextView(context)
            label.text = etiqueta
            label.setTypeface(null, Typeface.BOLD)
            label.setTextColor(resources.getColor(R.color.gob_green_light))
            label.setTextSize(14f)
            label.layoutParams = layoutParams

            val value = TextView(context)
            value.text = valor
            value.setTextColor(resources.getColor(R.color.gob_green_light))
            value.setTextSize(14f)
            value.layoutParams = layoutParams

            verticalLinearLayout.addView(label)
            verticalLinearLayout.addView(value)
        }

        scrollView.addView(verticalLinearLayout)
        view.findViewById<LinearLayout>(R.id.parentSeccionDos).addView(scrollView)
    }

    private fun firstLetterInUpperCase(mes:String) : String = mes.replaceFirst(mes.get(0),mes.get(0).uppercaseChar())

    private fun mesesRegistrados():Array<String>{
        val meses = mutableListOf<String>()
        val jsonTabla = JSONArray(arguments?.getString("tabla"))
        val jsonValue = arguments?.getInt("indexOfElement")?.let { jsonTabla.getJSONObject(it) }
        when(indicadorOrigen.indicador().numIdentificacion()){
            2 -> {
                when(titulo){
                    "Calendarizado" -> {
                        tabSeleccionado = "Calendarizado"
                        val listaCalendarizado = jsonValue?.getJSONArray("listaCalendarizado")
                        for(index in 0 until listaCalendarizado?.length()!!){
                            val calendarizado = listaCalendarizado.getJSONObject(index)
                            meses.add(firstLetterInUpperCase(calendarizado?.getString("mes")!!))
                        }
                    }
                    "Reportado por la universidad" -> {
                        tabSeleccionado = "Reportado por la universidad"
                        val listaReportado = jsonValue?.getJSONArray("listaReportado")
                        for(index in 0 until listaReportado?.length()!!){
                            val reportado = listaReportado.getJSONObject(index)
                            meses.add(firstLetterInUpperCase(reportado?.getString("mes")!!))
                        }
                    }
                    "Información de Dirección de Subsidio a Universidades" -> {
                        tabSeleccionado = "Información de Dirección de Subsidio a Universidades"
                        val listaPlataforma = jsonValue?.getJSONArray("listaPlataforma")
                        for(index in 0 until listaPlataforma?.length()!!){
                            val plataforma = listaPlataforma.getJSONObject(index)
                            meses.add(firstLetterInUpperCase(plataforma?.getString("mes")!!))
                        }
                    }
                }
            }
            3 -> {
                when(titulo){
                    "Calendarizado" -> {
                        tabSeleccionado = "Calendarizado"
                        val aportaciones = jsonValue?.getJSONArray("aportaciones")
                        for(index in 0 until aportaciones?.length()!!){
                            val aportacion = aportaciones.getJSONObject(index)
                            meses.add(firstLetterInUpperCase(aportacion?.getString("mes")!!))
                        }
                    }
                    "Información de Dirección de Subsidio a Universidades" -> {
                        tabSeleccionado = "Información de Dirección de Subsidio a Universidades"
                        val aportaciones = jsonValue?.getJSONArray("aportaciones")
                        for(index in 0 until aportaciones?.length()!!){
                            val aportacion = aportaciones.getJSONObject(index)
                            meses.add(firstLetterInUpperCase(aportacion?.getString("mes")!!))
                        }
                    }
                }
            }
        }
        return meses.toTypedArray()
    }

    private fun totalesRegistrados():MutableMap<String,String>{
        val totalesRegistrados = mutableMapOf<String,String>()
        val jsonTotales = arguments?.getString("totales")?.let { JSONObject(it) }
        when(indicadorOrigen.indicador().numIdentificacion()){
            2 -> {
                when(titulo){
                    "Calendarizado" -> {
                        totalesRegistrados.put(EtiquetasModuloTres.IND_2_CALENDARIZADO.get(0),doubleFormat(jsonTotales?.getString("calendarizado")!!))
                        totalesRegistrados.put(EtiquetasModuloTres.IND_2_CALENDARIZADO.get(1),doubleFormat(jsonTotales.getString("comprobado")))
                    }
                    "Reportado por la universidad" -> {
                        totalesRegistrados.put(EtiquetasModuloTres.IND_2_REPORTADO_UNIVERSIDAD.get(0),doubleFormat(jsonTotales?.getString("reportado")!!))
                    }
                    "Información de Dirección de Subsidio a Universidades" -> {
                        totalesRegistrados.put(EtiquetasModuloTres.IND_2_SEGUN_PLATAFORMA.get(0),doubleFormat(jsonTotales?.getString("reportado")!!))
                        totalesRegistrados.put(EtiquetasModuloTres.IND_2_SEGUN_PLATAFORMA.get(1),doubleFormat(jsonTotales?.getString("adeudo")!!))
                    }
                }
            }
            3 -> {
                when(titulo){
                    "Calendarizado" -> {
                        totalesRegistrados.put(EtiquetasModuloTres.IND_3_CALENDARIZADO.get(0),doubleFormat(jsonTotales?.getString("calendarizado")!!))
                        totalesRegistrados.put(EtiquetasModuloTres.IND_3_CALENDARIZADO.get(1),doubleFormat(jsonTotales.getString("reportado")))
                    }
                    "Información de Dirección de Subsidio a Universidades" -> {
                        totalesRegistrados.put(EtiquetasModuloTres.IND_3_SEGUN_PLATAFORMA.get(0),doubleFormat(jsonTotales?.getString("reportado")!!))
                        totalesRegistrados.put(EtiquetasModuloTres.IND_3_SEGUN_PLATAFORMA.get(1),doubleFormat(jsonTotales?.getString("adeudo")!!))
                    }
                }
            }
        }
        return totalesRegistrados
    }

    private fun subtotalesRegistrados():MutableMap<String,String>{
        val subtotalesRegistrados = mutableMapOf<String,String>()
        val jsonTabla = JSONArray(arguments?.getString("tabla"))
        val jsonValue = arguments?.getInt("indexOfElement")?.let { jsonTabla.getJSONObject(it) }
        when(indicadorOrigen.indicador().numIdentificacion()){
            2 -> {
                when(titulo){
                    "Calendarizado" -> {
                        subtotalesRegistrados.put(EtiquetasModuloTres.IND_2_CALENDARIZADO.get(6),doubleFormat(jsonValue?.getString("totalCalendarizado")!!))
                        subtotalesRegistrados.put(EtiquetasModuloTres.IND_2_CALENDARIZADO.get(7),doubleFormat(jsonValue.getString("totalComprobado")))
                    }
                    "Reportado por la universidad" -> {
                        subtotalesRegistrados.put(EtiquetasModuloTres.IND_2_REPORTADO_UNIVERSIDAD.get(4),doubleFormat(jsonValue?.getString("totalReportado")!!))
                    }
                }
            }
            3 -> {
                when(titulo){
                    "Calendarizado" -> {
                        subtotalesRegistrados.put(EtiquetasModuloTres.IND_3_CALENDARIZADO.get(6),doubleFormat(jsonValue?.getString("totalCalendarizado")!!))
                        subtotalesRegistrados.put(EtiquetasModuloTres.IND_3_CALENDARIZADO.get(7),doubleFormat(jsonValue.getString("totalReportado")))
                    }
                    "Información de Dirección de Subsidio a Universidades" -> {
                        subtotalesRegistrados.put(EtiquetasModuloTres.IND_3_SEGUN_PLATAFORMA.get(5),doubleFormat(jsonValue?.getString("totalReportado")!!))
                        subtotalesRegistrados.put(EtiquetasModuloTres.IND_3_SEGUN_PLATAFORMA.get(6),doubleFormat(jsonValue.getString("totalAdeudosMensuales")))
                    }
                }
            }
        }

        return subtotalesRegistrados
    }

    private fun doubleFormat(number: String):String {
        try {
            return "$ "+NumberFormat.getNumberInstance(Locale.US).format(number.toDouble())
        }catch (e : NumberFormatException){
            return number
        }
    }

    override fun indicadorOrigen(): IndicadorFragment = indicadorOrigen

    override fun instancia(): Fragment = this@SeccionDosFragment

}
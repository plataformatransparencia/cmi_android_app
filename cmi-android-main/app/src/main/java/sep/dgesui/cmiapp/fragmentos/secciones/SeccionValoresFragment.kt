package sep.dgesui.cmiapp.fragmentos.secciones

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Base64
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject
import sep.dgesui.cmiapp.R
import sep.dgesui.cmiapp.fragmentos.indicador.IndicadorFragment
import sep.dgesui.cmiapp.fragmentos.modulos.ModuloDosFragment
import sep.dgesui.cmiapp.fragmentos.modulos.ModuloTresFragment
import sep.dgesui.cmiapp.fragmentos.modulos.ModuloUnoFragment
import sep.dgesui.cmiapp.modelos.EtiquetasModuloUno
import sep.dgesui.cmiapp.modelos.EtiquetasModuloTres
import sep.dgesui.cmiapp.modelos.EtiquetasModuloDos
import sep.dgesui.cmiapp.modelos.IndicadorModuloUno
import sep.dgesui.cmiapp.modelos.IndicadorModuloTres
import java.text.NumberFormat
import java.util.*

class SeccionValoresFragment(private val titulo: String) : Fragment(R.layout.fragment_seccion_valores) {

    private var seccionOrigen: SeccionInterface? = null
    private var indicadorOrigen: IndicadorFragment? = null
    private lateinit var imageView : ImageView
    private var chartsModuloIII : Array<ImageView> = emptyArray()

    constructor(seccionOrigen: SeccionInterface, titulo: String) : this(titulo) {
        this.seccionOrigen = seccionOrigen
    }
    constructor(indicadorOrigen: IndicadorFragment, titulo: String) : this(titulo){
        this.indicadorOrigen = indicadorOrigen
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageView = ImageView(requireContext())

        view.findViewById<LinearLayout>(R.id.parentSeccionValores).addView(titulo())

        if(indicadorOrigen != null && indicadorOrigen!!.moduloOrigen() is ModuloUnoFragment){
            view.findViewById<LinearLayout>(R.id.parentSeccionValores).addView(listaValoresModuloI())
        } else if(seccionOrigen != null && seccionOrigen!!.indicadorOrigen().moduloOrigen() is ModuloTresFragment){
            requestChartsModuloIII(view.findViewById(R.id.parentSeccionValores))
        }else if(indicadorOrigen != null && indicadorOrigen!!.moduloOrigen() is ModuloDosFragment){
            view.findViewById<LinearLayout>(R.id.parentSeccionValores).addView(listaValoresModuloII())
        }
    }

    private fun titulo():LinearLayout{
        val linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.HORIZONTAL
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
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
        val textViewLayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        textViewLayoutParams.setMargins(60,0,60,0)
        textView.layoutParams = textViewLayoutParams

        linearLayout.addView(imageView)
        linearLayout.addView(textView)

        return linearLayout
    }

    private fun listaValoresModuloI():ScrollView{
        val scrollView = ScrollView(context)
        scrollView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)

        val verticalLinearLayout = LinearLayout(context)
        verticalLinearLayout.orientation = LinearLayout.VERTICAL
        verticalLinearLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)

        val horizontalScrollView = HorizontalScrollView(context)
        val layoutParamshorizontalScrollView = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT)
        layoutParamshorizontalScrollView.setMargins(0,60,0,0)
        horizontalScrollView.layoutParams = layoutParamshorizontalScrollView

        val linearLayoutHorizontalScrollView = LinearLayout(context)
        linearLayoutHorizontalScrollView.orientation = LinearLayout.HORIZONTAL
        linearLayoutHorizontalScrollView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)

        valoresDeIndicadorModuloUno().forEach { (etiqueta,valor) ->
            val linearLayout = LinearLayout(context)
            linearLayout.orientation = LinearLayout.HORIZONTAL
            linearLayout.gravity = Gravity.CENTER
            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(0,100,0,0)
            linearLayout.layoutParams = layoutParams

            val label = TextView(context)
            label.text = etiqueta+" "+valor
            label.setTextColor(resources.getColor(R.color.gob_green_light))
            label.setTextSize(16f)
            label.layoutParams = TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT,1f)

            linearLayout.addView(label)

            verticalLinearLayout.addView(linearLayout)
        }

        linearLayoutHorizontalScrollView.addView(imageView)
        horizontalScrollView.addView(linearLayoutHorizontalScrollView)
        verticalLinearLayout.addView(horizontalScrollView)

        val layoutParamsFuenteText = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParamsFuenteText.setMargins(0,100,0,0)

        val fuenteText = TextView(context)
        fuenteText.text = "Fuente:"
        fuenteText.setTextColor(resources.getColor(R.color.gob_green_light))
        fuenteText.setTextSize(14f)
        fuenteText.layoutParams = layoutParamsFuenteText

        verticalLinearLayout.addView(fuenteText)

        fuentesIndicadorModuloI().forEach {
            verticalLinearLayout.addView(it)
        }

        scrollView.addView(verticalLinearLayout)

        return scrollView
    }

    private fun valoresDeIndicadorModuloUno():MutableMap<String,String>{
        val valores : MutableMap<String,String> = HashMap()
        val jsonResponse = arguments?.getString("response")?.let { JSONObject(it) }
        val jsonTabla: JSONObject = jsonResponse?.getString("tabla")?.let { JSONObject(it) }!!
        when(indicadorOrigen!!.indicador().numIdentificacion()){
            1 -> {
                when(titulo){
                    "Total" -> {
                        addChartModuloI(jsonResponse.getString("graficaTotal"))
                        valores.put(EtiquetasModuloUno.IND_1_TOTAL.get(0), integerFormat(jsonTabla.getString("matriculaTotalGlobal")))
                        valores.put(EtiquetasModuloUno.IND_1_TOTAL.get(1), integerFormat(jsonTabla.getString("censoTotalGlobal")))
                        valores.put(EtiquetasModuloUno.IND_1_TOTAL.get(2), porcentajeFormat(jsonTabla.getString("tasaEscolarizacionGlobal")))
                    }
                    "Mujeres" -> {
                        addChartModuloI(jsonResponse.getString("graficaMujeres"))
                        valores.put(EtiquetasModuloUno.IND_1_MUJERES.get(0), integerFormat(jsonTabla.getString("matriculaMujGlobal")))
                        valores.put(EtiquetasModuloUno.IND_1_MUJERES.get(1), integerFormat(jsonTabla.getString("censoMujGlobal")))
                        valores.put(EtiquetasModuloUno.IND_1_MUJERES.get(2), porcentajeFormat(jsonTabla.getString("tasaMujeresGlobal")))
                    }
                    "Hombres" -> {
                        addChartModuloI(jsonResponse.getString("graficaHombres"))
                        valores.put(EtiquetasModuloUno.IND_1_HOMBRES.get(0), integerFormat(jsonTabla.getString("matriculaHomGlobal")))
                        valores.put(EtiquetasModuloUno.IND_1_HOMBRES.get(1), integerFormat(jsonTabla.getString("censoHomGlobal")))
                        valores.put(EtiquetasModuloUno.IND_1_HOMBRES.get(2), porcentajeFormat(jsonTabla.getString("tasaHombresGlobal")))
                    }
                }
            }
            2 -> {
                when(titulo){
                    "Total" -> {
                        addChartModuloI(jsonResponse.getString("graficaTotal"))
                        valores.put(EtiquetasModuloUno.IND_2_TOTAL.get(0),porcentajeFormat(jsonTabla.getString("contribucion")))
                    }
                    "Mujeres" -> {
                        addChartModuloI(jsonResponse.getString("graficaMujeres"))
                        valores.put(EtiquetasModuloUno.IND_2_MUJERES.get(0),porcentajeFormat(jsonTabla.getString("contribucionMujeres")))
                    }
                    "Hombres" -> {
                        addChartModuloI(jsonResponse.getString("graficaHombres"))
                        valores.put(EtiquetasModuloUno.IND_2_HOMBRES.get(0),porcentajeFormat(jsonTabla.getString("contribucionHombres")))
                    }
                }
            }
            3 -> {
                when(titulo){
                    "Total" -> {
                        addChartModuloI(jsonResponse.getString("graficaTotal"))
                        valores.put(EtiquetasModuloUno.IND_3_TOTAL.get(0),integerFormat(jsonTabla.getString("totalTSUPA")))
                        valores.put(EtiquetasModuloUno.IND_3_TOTAL.get(1),integerFormat(jsonTabla.getString("totalLicenciatura")))
                        valores.put(EtiquetasModuloUno.IND_3_TOTAL.get(2),integerFormat(jsonTabla.getString("matriculaTotal")))
                        valores.put(EtiquetasModuloUno.IND_3_TOTAL.get(3),porcentajeFormat(jsonTabla.getString("contrubucionDGESUI")))
                    }
                    "Mujeres" -> {
                        addChartModuloI(jsonResponse.getString("graficaMujeres"))
                        valores.put(EtiquetasModuloUno.IND_3_MUJERES.get(0),integerFormat(jsonTabla.getString("totalMujeres")))
                        valores.put(EtiquetasModuloUno.IND_3_MUJERES.get(1),porcentajeFormat(jsonTabla.getString("contribucionDGESUIMujeres")))
                    }
                    "Hombres" -> {
                        addChartModuloI(jsonResponse.getString("graficaHombres"))
                        valores.put(EtiquetasModuloUno.IND_3_HOMBRES.get(0),integerFormat(jsonTabla.getString("totalHombres")))
                        valores.put(EtiquetasModuloUno.IND_3_HOMBRES.get(1),porcentajeFormat(jsonTabla.getString("contribucionDGESUIHombres")))
                    }
                    "Con discapacidad" -> {
                        addChartModuloI(jsonResponse.getString("graficaDiscapacitados"))
                        valores.put(EtiquetasModuloUno.IND_3_DISCAPACIDAD.get(0),integerFormat(jsonTabla.getString("totalDiscapacitados")))
                        valores.put(EtiquetasModuloUno.IND_3_DISCAPACIDAD.get(1),porcentajeFormat(jsonTabla.getString("porcentajeConDiscapacidad")))
                    }
                    "Hablantes de lengua indígena" -> {
                        addChartModuloI(jsonResponse.getString("graficaHablantesLenguaIndigena"))
                        valores.put(EtiquetasModuloUno.IND_3_HLI.get(0),integerFormat(jsonTabla.getString("totalHablanteLenguaIndigena")))
                        valores.put(EtiquetasModuloUno.IND_3_HLI.get(1),porcentajeFormat(jsonTabla.getString("porcentajeHablanteLenguaIndigena")))
                    }
                    "Escolarizado" -> {
                        addChartModuloI(jsonResponse.getString("graficaEscolarizado"))
                        valores.put(EtiquetasModuloUno.IND_3_ESCOLARIZADO.get(0),integerFormat(jsonTabla.getString("totalEscolarizado")))
                        valores.put(EtiquetasModuloUno.IND_3_ESCOLARIZADO.get(1),porcentajeFormat(jsonTabla.getString("porcentajeEscolarizado")))
                    }
                    "No escolarizado" -> {
                        addChartModuloI(jsonResponse.getString("graficaNoEscolarizado"))
                        valores.put(EtiquetasModuloUno.IND_3_NO_ESCOLARIZADO.get(0),integerFormat(jsonTabla.getString("totalNoEscolarizado")))
                        valores.put(EtiquetasModuloUno.IND_3_NO_ESCOLARIZADO.get(1),porcentajeFormat(jsonTabla.getString("porcentajeNoEscolarizado")))
                    }
                    "Mixto" -> {
                        addChartModuloI(jsonResponse.getString("graficaMixto"))
                        valores.put(EtiquetasModuloUno.IND_3_MIXTO.get(0),integerFormat(jsonTabla.getString("totalMixto")))
                        valores.put(EtiquetasModuloUno.IND_3_MIXTO.get(1),porcentajeFormat(jsonTabla.getString("porcentajeMixto")))
                    }
                }
            }
            4 -> {
                when(titulo){
                    "Total" -> {
                        addChartModuloI(jsonResponse.getString("graficaTotal"))
                        valores.put(EtiquetasModuloUno.IND_4_TOTAL.get(0),integerFormat(jsonTabla.getString("total")))
                        valores.put(EtiquetasModuloUno.IND_4_TOTAL.get(1),integerFormat(jsonTabla.getString("totalEnRangoDeEdad")))
                        valores.put(EtiquetasModuloUno.IND_4_TOTAL.get(2),porcentajeFormat(jsonTabla.getString("tasaGeneral")))
                    }
                    "Mujeres" -> {
                        addChartModuloI(jsonResponse.getString("graficaMujeres"))
                        valores.put(EtiquetasModuloUno.IND_4_MUJERES.get(0),integerFormat(jsonTabla.getString("mujeres")))
                        valores.put(EtiquetasModuloUno.IND_4_MUJERES.get(1),integerFormat(jsonTabla.getString("mujeresEnRangoDeEdad")))
                        valores.put(EtiquetasModuloUno.IND_4_MUJERES.get(2),porcentajeFormat(jsonTabla.getString("tasaEscolarizacionMujeres")))
                    }
                    "Hombres" -> {
                        addChartModuloI(jsonResponse.getString("graficaHombres"))
                        valores.put(EtiquetasModuloUno.IND_4_HOMBRES.get(0),integerFormat(jsonTabla.getString("hombres")))
                        valores.put(EtiquetasModuloUno.IND_4_HOMBRES.get(1),integerFormat(jsonTabla.getString("hombresEnRangoDeEdad")))
                        valores.put(EtiquetasModuloUno.IND_4_HOMBRES.get(2),porcentajeFormat(jsonTabla.getString("tasaEscolarizacionHombres")))
                    }
                    "Con discapacidad" -> {
                        addChartModuloI(jsonResponse.getString("graficaDiscapacitados"))
                        valores.put(EtiquetasModuloUno.IND_4_DISCAPACIDAD.get(0),integerFormat(jsonTabla.getString("discapacitados")))
                        valores.put(EtiquetasModuloUno.IND_4_DISCAPACIDAD.get(1),integerFormat(jsonTabla.getString("discapacitadosEnRangoDeEdad")))
                        valores.put(EtiquetasModuloUno.IND_4_DISCAPACIDAD.get(2),porcentajeFormat(jsonTabla.getString("tasaDiscapacitados")))
                    }
                    "Hablantes de lengua indígena" -> {
                        addChartModuloI(jsonResponse.getString("graficaHablantesLenguaIndigena"))
                        valores.put(EtiquetasModuloUno.IND_4_HLI.get(0),integerFormat(jsonTabla.getString("hablantesLenguaIndigena")))
                        valores.put(EtiquetasModuloUno.IND_4_HLI.get(1),integerFormat(jsonTabla.getString("hablantesLenguaIndigenaEnRangoDeEdad")))
                        valores.put(EtiquetasModuloUno.IND_4_HLI.get(2),porcentajeFormat(jsonTabla.getString("tasaHablantesLenguaIndigena")))
                    }
                }
            }
            5 -> {
                when(titulo){
                    "Total" -> {
                        addChartModuloI(jsonResponse.getString("graficaTotal"))
                        valores.put(EtiquetasModuloUno.IND_5_TOTAL.get(0),integerFormat(jsonTabla.getString("egresadosCicloAnteriorGeneral")))
                        valores.put(EtiquetasModuloUno.IND_5_TOTAL.get(1),integerFormat(jsonTabla.getString("nuevoIngresoSeisCiclosAtrasGeneral")))
                        valores.put(EtiquetasModuloUno.IND_5_TOTAL.get(2),porcentajeFormat(jsonTabla.getString("eficienciaTerminalGeneral")))
                    }
                    "Mujeres" -> {
                        addChartModuloI(jsonResponse.getString("graficaMujeres"))
                        valores.put(EtiquetasModuloUno.IND_5_MUJERES.get(0),integerFormat(jsonTabla.getString("egresadosCicloAnteriorMujeres")))
                        valores.put(EtiquetasModuloUno.IND_5_MUJERES.get(1),integerFormat(jsonTabla.getString("nuevoIngresoSeisCiclosAtrasMujeres")))
                        valores.put(EtiquetasModuloUno.IND_5_MUJERES.get(2),porcentajeFormat(jsonTabla.getString("eficienciaTerminalMujeres")))
                    }
                    "Hombres" -> {
                        addChartModuloI(jsonResponse.getString("graficaHombres"))
                        valores.put(EtiquetasModuloUno.IND_5_HOMBRES.get(0),integerFormat(jsonTabla.getString("egresadosCicloAnteriorHombres")))
                        valores.put(EtiquetasModuloUno.IND_5_HOMBRES.get(1),integerFormat(jsonTabla.getString("nuevoIngresoSeisCiclosAtrasHombres")))
                        valores.put(EtiquetasModuloUno.IND_5_HOMBRES.get(2),porcentajeFormat(jsonTabla.getString("eficienciaTerminalHombres")))
                    }
                    "Con discapacidad" -> {
                        addChartModuloI(jsonResponse.getString("graficaDiscapacitados"))
                        valores.put(EtiquetasModuloUno.IND_5_DISCAPACIDAD.get(0),integerFormat(jsonTabla.getString("egresadosCicloAnteriorDiscapacitados")))
                        valores.put(EtiquetasModuloUno.IND_5_DISCAPACIDAD.get(1),integerFormat(jsonTabla.getString("nuevoIngresoSeisCiclosAtrasDiscapacitados")))
                        valores.put(EtiquetasModuloUno.IND_5_DISCAPACIDAD.get(2),porcentajeFormat(jsonTabla.getString("eficienciaTerminalDiscapacitados")))
                    }
                }
            }
            6 -> {
                val periodo = (indicadorOrigen!!.indicador() as IndicadorModuloUno).periodoParam()
                val anteriorPeriodo = (indicadorOrigen!!.indicador() as IndicadorModuloUno).anteriorPeriodo()
                when(titulo){
                    "Total" -> {
                        addChartModuloI(jsonResponse.getString("graficaTotal"))
                        valores.put(EtiquetasModuloUno.IND_6_TOTAL.get(0),integerFormat(jsonTabla.getString("matriculaPeriodoAnteriorGeneral")))
                        valores.put(EtiquetasModuloUno.IND_6_TOTAL.get(1)+anteriorPeriodo+" (V23):",
                            integerFormat(jsonTabla.getString("egresadosPeriodoAnteriorGeneral")))
                        valores.put(EtiquetasModuloUno.IND_6_TOTAL.get(2)+periodo+" (incluye escolarizado y no escolarizado) (v177):",
                            integerFormat(jsonTabla.getString("matriculaGeneral")))
                        valores.put(EtiquetasModuloUno.IND_6_TOTAL.get(3)+periodo+" (V90):",
                            integerFormat(jsonTabla.getString("nuevoIngresoGeneral")))
                        valores.put(EtiquetasModuloUno.IND_6_TOTAL.get(4)+periodo+":",
                            integerFormat(jsonTabla.getString("abandonoGeneral")))
                        valores.put(EtiquetasModuloUno.IND_6_TOTAL.get(5)+periodo+" pregrado:",
                            porcentajeFormat(jsonTabla.getString("tasaAbandonoGeneral")))
                        valores.put(EtiquetasModuloUno.IND_6_TOTAL.get(6)+periodo+":",
                            porcentajeFormat(jsonTabla.getString("tasaRetencionGeneral")))
                    }
                    "Mujeres" -> {
                        addChartModuloI(jsonResponse.getString("graficaMujeres"))
                        valores.put(EtiquetasModuloUno.IND_6_MUJERES.get(0), integerFormat(jsonTabla.getString("matriculaPeriodoAnteriorMujeres")))
                        valores.put(EtiquetasModuloUno.IND_6_MUJERES.get(1)+anteriorPeriodo+" (V23):",
                            integerFormat(jsonTabla.getString("egresadosPeriodoAnteriorMujeres")))
                        valores.put(EtiquetasModuloUno.IND_6_MUJERES.get(2)+periodo+" (incluye escolarizado y no escolarizado) (v177):",
                            integerFormat(jsonTabla.getString("matriculaMujeres")))
                        valores.put(EtiquetasModuloUno.IND_6_MUJERES.get(3)+periodo+" (V90):",
                            integerFormat(jsonTabla.getString("nuevoIngresoMujeres")))
                        valores.put(EtiquetasModuloUno.IND_6_MUJERES.get(4)+periodo+":",
                            integerFormat(jsonTabla.getString("abandonoMujeres")))
                        valores.put(EtiquetasModuloUno.IND_6_MUJERES.get(5)+periodo+" pregrado:",
                            porcentajeFormat(jsonTabla.getString("tasaAbandonoMujeres")))
                        valores.put(EtiquetasModuloUno.IND_6_MUJERES.get(6)+periodo+":",
                            porcentajeFormat(jsonTabla.getString("tasaRetencionMujeres")))
                    }
                    "Hombres" -> {
                        addChartModuloI(jsonResponse.getString("graficaHombres"))
                        valores.put(EtiquetasModuloUno.IND_6_HOMBRES.get(0),integerFormat(jsonTabla.getString("matriculaPeriodoAnteriorHombres")))
                        valores.put(EtiquetasModuloUno.IND_6_HOMBRES.get(1)+anteriorPeriodo+" (V23):",
                            integerFormat(jsonTabla.getString("egresadosPeriodoAnteriorHombres")))
                        valores.put(EtiquetasModuloUno.IND_6_HOMBRES.get(2)+periodo+" (incluye escolarizado y no escolarizado) (v177):",
                            integerFormat(jsonTabla.getString("matriculaHombres")))
                        valores.put(EtiquetasModuloUno.IND_6_HOMBRES.get(3)+periodo+" (V90):",
                            integerFormat(jsonTabla.getString("nuevoIngresoHombres")))
                        valores.put(EtiquetasModuloUno.IND_6_HOMBRES.get(4)+periodo+":",
                            integerFormat(jsonTabla.getString("abandonoHombres")))
                        valores.put(EtiquetasModuloUno.IND_6_HOMBRES.get(5)+periodo+" pregrado:",
                            porcentajeFormat(jsonTabla.getString("tasaAbandonoHombres")))
                        valores.put(EtiquetasModuloUno.IND_6_HOMBRES.get(6)+periodo+":",
                            porcentajeFormat(jsonTabla.getString("tasaRetencionHombres")))
                    }
                    "Con discapacidad" -> {
                        addChartModuloI(jsonResponse.getString("graficaDiscapacitados"))
                        valores.put(EtiquetasModuloUno.IND_6_DISCAPACIDAD.get(0),integerFormat(jsonTabla.getString("matriculaPeriodoAnteriorDiscapacitados")))
                        valores.put(EtiquetasModuloUno.IND_6_DISCAPACIDAD.get(1)+anteriorPeriodo+" (V23):",
                            integerFormat(jsonTabla.getString("egresadosPeriodoAnteriorDiscapacitados")))
                        valores.put(EtiquetasModuloUno.IND_6_DISCAPACIDAD.get(2)+periodo+" (incluye escolarizado y no escolarizado) (v177):",
                            integerFormat(jsonTabla.getString("matriculaDiscapacitados")))
                        valores.put(EtiquetasModuloUno.IND_6_DISCAPACIDAD.get(3)+periodo+" (V90):",
                            integerFormat(jsonTabla.getString("nuevoIngresoDiscapacitados")))
                        valores.put(EtiquetasModuloUno.IND_6_DISCAPACIDAD.get(4)+periodo+":",
                            integerFormat(jsonTabla.getString("abandonoDiscapacitados")))
                        valores.put(EtiquetasModuloUno.IND_6_DISCAPACIDAD.get(5)+periodo+" pregrado:",
                            porcentajeFormat(jsonTabla.getString("tasaAbandonoDiscapacitados")))
                        valores.put(EtiquetasModuloUno.IND_6_DISCAPACIDAD.get(6)+periodo+":",
                            porcentajeFormat(jsonTabla.getString("tasaRetencionDiscapacitados")))
                    }
                    "Hablantes de lengua indígena" -> {
                        addChartModuloI(jsonResponse.getString("graficaHablantesLenguaIndigena"))
                        valores.put(EtiquetasModuloUno.IND_6_HLI.get(0),integerFormat(jsonTabla.getString("matriculaPeriodoAnteriorHablantesLenguaIndigena")))
                        valores.put(EtiquetasModuloUno.IND_6_HLI.get(1)+anteriorPeriodo+" (V23):",
                            integerFormat(jsonTabla.getString("egresadosPeriodoAnteriorHablantesLenguaIndigena")))
                        valores.put(EtiquetasModuloUno.IND_6_HLI.get(2)+periodo+" (incluye escolarizado y no escolarizado) (v177):",
                            integerFormat(jsonTabla.getString("matriculaHablantesLenguaIndigena")))
                        valores.put(EtiquetasModuloUno.IND_6_HLI.get(3)+periodo+" (V90):",
                            integerFormat(jsonTabla.getString("nuevoIngresoHablantesLenguaIndigena")))
                        valores.put(EtiquetasModuloUno.IND_6_HLI.get(4)+periodo+":",
                            integerFormat(jsonTabla.getString("abandonoHablantesLenguaIndigena")))
                        valores.put(EtiquetasModuloUno.IND_6_HLI.get(5)+periodo+" pregrado:",
                            porcentajeFormat(jsonTabla.getString("tasaAbandonoHablantesLenguaIndigena")))
                        valores.put(EtiquetasModuloUno.IND_6_HLI.get(6)+periodo+":",
                            porcentajeFormat(jsonTabla.getString("tasaRetencionHablantesLenguaIndigena")))
                    }
                }
            }
            7 -> {
                val periodo = (indicadorOrigen!!.indicador() as IndicadorModuloUno).periodoParam()
                when(titulo){
                    "Total" -> {
                        addChartModuloI(jsonResponse.getString("grafica"))
                        valores.put(EtiquetasModuloUno.IND_7_TOTAL.get(0) + periodo!!.split("-")[0].trim() + ": " ,doubleFormat(jsonTabla.getString("aportacion")))
                        valores.put(EtiquetasModuloUno.IND_7_TOTAL.get(1) + periodo!!.split("-")[0].trim() + ": ",porcentajeFormatFourDecimals(jsonTabla.getString("subsidio")))
                    }
                }
            }
            8 -> {
                val periodo = (indicadorOrigen!!.indicador() as IndicadorModuloUno).periodoParam()
                when(titulo){
                    "Total" -> {
                        addChartModuloI(jsonResponse.getString("grafica"))
                        valores.put(EtiquetasModuloUno.IND_8_TOTAL.get(0) + periodo!!.split("-")[0].trim() + ": ",porcentajeFormatFourDecimals(jsonTabla.getString("subsidio")))
                    }
                }
            }
        }
        return valores
    }

    private fun addChartModuloI(stringChart:String) {
        val decodedResponse = Base64.decode(stringChart,Base64.DEFAULT)
        val bitMap = BitmapFactory.decodeByteArray(decodedResponse,0,decodedResponse.size)
        val heightPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,bitMap.height.toFloat(),resources.displayMetrics)
        val widthPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,bitMap.width.toFloat(),resources.displayMetrics)
        val layoutParams = LinearLayout.LayoutParams(widthPx.toInt(),heightPx.toInt())
        imageView.setImageBitmap(bitMap)
        imageView.layoutParams = layoutParams
    }

    private fun fuentesIndicadorModuloI(): MutableList<LinearLayout>{
        val fuentes = mutableListOf<LinearLayout>()
        val arrayResponse = arguments?.getString("response")?.let { JSONObject(it) }
        val referencias = arrayResponse?.getJSONArray("referencias")
        if (referencias != null) {
            for(index in 0 until referencias.length()){
                val linearLayout = LinearLayout(context)
                linearLayout.orientation = LinearLayout.HORIZONTAL
                linearLayout.gravity = Gravity.CENTER
                val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
                layoutParams.setMargins(0,100,0,0)
                linearLayout.layoutParams = layoutParams

                val contador = TextView(context)
                contador.text = (index.plus(1)).toString()+"."
                contador.setTextColor(resources.getColor(R.color.gob_green_light))
                contador.setTextSize(14f)
                contador.setPadding(0,0,30,0)
                contador.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)

                val textView = TextView(context)
                textView.setTextSize(14f)
                textView.layoutParams = TableLayout.LayoutParams(
                    TableLayout.LayoutParams.WRAP_CONTENT,
                    TableLayout.LayoutParams.WRAP_CONTENT,1f)

                val nombreFuente = referencias.getJSONObject(index).getString("texto")

                if(!referencias.getJSONObject(index).getString("url").matches("".toRegex())){
                    val spannable = SpannableString(nombreFuente)
                    val clickableSpan = object : ClickableSpan(){
                        override fun onClick(p0: View) {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(referencias.getJSONObject(index).getString("url")))
                            requireContext().startActivity(intent)
                        }
                        override fun updateDrawState(ds: TextPaint) {
                            super.updateDrawState(ds)
                            ds.color = resources.getColor(R.color.gob_link)
                        }
                    }
                    spannable.setSpan(clickableSpan, 0, nombreFuente.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    textView.movementMethod = LinkMovementMethod.getInstance()
                    textView.text = spannable
                }else{
                    textView.text = referencias.getJSONObject(index).getString("texto")
                    textView.setTextColor(resources.getColor(R.color.gob_green_light))
                }

                linearLayout.addView(contador)
                linearLayout.addView(textView)
                fuentes.add(linearLayout)
            }
        }
        return fuentes
    }

    private fun requestChartsModuloIII(linearLayout: LinearLayout){
        var universidadParam : String? = "Todas"
        var subsistemaParam : String? = "Todos"
        var entidadFederativaParam : String? = "Todas"
        val jsonTabla = JSONArray(arguments?.getString("tabla"))
        val jsonValue = arguments?.getInt("indexOfElement")?.let { jsonTabla.getJSONObject(it) }
        when((seccionOrigen?.indicadorOrigen()?.indicador() as IndicadorModuloTres).valorRaiz()){
            "universidad" -> {
                universidadParam = jsonValue?.getJSONObject("universidad")?.getString("nombre")
                entidadFederativaParam = "Todas"
                subsistemaParam = "Todos"
            }
            "entidadFederativa" -> {
                entidadFederativaParam = jsonValue?.getJSONObject("entidadFederativa")?.getString("valor")
                universidadParam = "Todas"
                subsistemaParam = "Todos"
            }
            "subsistema" -> {
                subsistemaParam = jsonValue?.getJSONObject("subsistema")?.getString("valor")
                entidadFederativaParam = "Todas"
                universidadParam = "Todas"
            }
            "oscCentro" -> {
                universidadParam = jsonValue?.getJSONObject("oscCentro")?.getString("nombre")
                entidadFederativaParam = "Todas"
                subsistemaParam = "Todos"
            }
        }
        val urlChart = (seccionOrigen?.indicadorOrigen()?.indicador() as IndicadorModuloTres).enlaces()[1]+"?universidad="+universidadParam+"&subsistema="+subsistemaParam+"&entidadFederativa"+entidadFederativaParam
        val queue = Volley.newRequestQueue(context)
        val stringRequestToChart = object : StringRequest(
            Method.GET,urlChart,
            {
                val response = JSONArray(it)
                val charts = mutableListOf<String>()
                for (index in 0 until response.length())
                    charts.add(response.getString(index))
                listaValoresModuloIII(linearLayout,charts)
            },
            {error ->
                error.message?.let { Log.d("error", it) }
            }){
            override fun getHeaders(): MutableMap<String, String> {
                val headers :MutableMap<String,String> = HashMap()
                headers.put("Authorization","Bearer "+arguments?.getString("token"))
                return headers
            }
        }
        queue.add(stringRequestToChart)
    }

    private fun listaValoresModuloIII(linearLayout: LinearLayout, charts: List<String>){
        val scrollView = ScrollView(context)
        scrollView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)

        val verticalLinearLayout = LinearLayout(context)
        verticalLinearLayout.orientation = LinearLayout.VERTICAL
        verticalLinearLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)

        valoresDeIndicadorModuloTres(charts).forEach { (etiqueta, valor) ->
            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(0,60,0,0)

            val label = TextView(context)
            label.text = etiqueta.split(".").get(0)
            label.setTypeface(null, Typeface.BOLD)
            label.setTextColor(resources.getColor(R.color.gob_green_light))
            label.setTextSize(16f)
            label.layoutParams = layoutParams

            val value = TextView(context)
            value.text = valor
            value.setTextColor(resources.getColor(R.color.gob_green_light))
            value.setTextSize(16f)
            value.layoutParams = layoutParams

            verticalLinearLayout.addView(label)
            verticalLinearLayout.addView(value)
        }

        chartsModuloIII.forEach {
            val horizontalScrollView = HorizontalScrollView(context)
            val layoutParamshorizontalScrollView = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT)
            layoutParamshorizontalScrollView.setMargins(0,60,0,0)
            horizontalScrollView.layoutParams = layoutParamshorizontalScrollView

            val linearLayoutHorizontalScrollView = LinearLayout(context)
            linearLayoutHorizontalScrollView.orientation = LinearLayout.HORIZONTAL
            linearLayoutHorizontalScrollView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)

            linearLayoutHorizontalScrollView.addView(it)
            horizontalScrollView.addView(linearLayoutHorizontalScrollView)
            verticalLinearLayout.addView(horizontalScrollView)

        }

        val layoutParamsFuenteText = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParamsFuenteText.setMargins(0,100,0,0)

        val label = TextView(context)
        label.text = "Fuente"
        label.setTypeface(null, Typeface.BOLD)
        label.setTextColor(resources.getColor(R.color.gob_green_light))
        label.setTextSize(16f)
        label.layoutParams = layoutParamsFuenteText
        verticalLinearLayout.addView(label)

        val jsonFicha = arguments?.getString("ficha")?.let { JSONObject(it) }

        val fuenteText = TextView(context)
        fuenteText.text = jsonFicha?.getString("fuente")
        fuenteText.setTextColor(resources.getColor(R.color.gob_green_light))
        fuenteText.setTextSize(14f)
        fuenteText.layoutParams = layoutParamsFuenteText
        verticalLinearLayout.addView(fuenteText)

        scrollView.addView(verticalLinearLayout)
        linearLayout.addView(scrollView)
    }

    private fun listaValoresModuloII():ScrollView {
        val scrollView = ScrollView(context)
        scrollView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)

        val verticalLinearLayout = LinearLayout(context)
        verticalLinearLayout.orientation = LinearLayout.VERTICAL
        verticalLinearLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)

        valoresDeIndicadorModuloDos().forEach { (etiqueta,valor) ->
            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(0,60,0,0)

            val label = TextView(context)
            label.text = etiqueta.split(".").get(0)
            label.setTypeface(null, Typeface.BOLD)
            label.setTextColor(resources.getColor(R.color.gob_green_light))
            label.setTextSize(16f)
            label.layoutParams = layoutParams

            val value = TextView(context)
            value.text = valor
            value.setTextColor(resources.getColor(R.color.gob_green_light))
            value.setTextSize(16f)
            value.layoutParams = layoutParams

            verticalLinearLayout.addView(label)
            verticalLinearLayout.addView(value)

        }

        val layoutParamsFuenteText = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParamsFuenteText.setMargins(0,100,0,0)

        val label = TextView(context)
        label.text = "Fuente"
        label.setTypeface(null, Typeface.BOLD)
        label.setTextColor(resources.getColor(R.color.gob_green_light))
        label.setTextSize(16f)
        label.layoutParams = layoutParamsFuenteText
        verticalLinearLayout.addView(label)

        val jsonFicha = arguments?.getString("ficha")?.let { JSONObject(it) }
        val fuentes = jsonFicha?.getJSONArray("fuentes")
        fuentes?.let {
            for (index in 0 until it.length()){
                val fuenteText = TextView(context)
                fuenteText.text = fuentes.getJSONObject(index).getString("fuente")
                fuenteText.setTextColor(resources.getColor(R.color.gob_green_light))
                fuenteText.setTextSize(14f)
                fuenteText.layoutParams = layoutParamsFuenteText


                verticalLinearLayout.addView(fuenteText)
            }
        }



        scrollView.addView(verticalLinearLayout)
        return scrollView
    }

    private fun valoresDeIndicadorModuloTres(charts: List<String>):MutableMap<String,String?>{
        val valores : MutableMap<String,String?> = LinkedHashMap()
        when(seccionOrigen?.indicadorOrigen()?.indicador()?.numIdentificacion()){
            1 -> {
                val jsonTabla = JSONArray(arguments?.getString("tabla"))
                val jsonValue = arguments?.getInt("indexOfElement")?.let { jsonTabla.getJSONObject(it) }
                when(titulo){
                    "Instrumento" -> {
                        valores.put(EtiquetasModuloTres.IND_1_INSTRUMENTO.get(0),jsonValue?.getString("instrumento"))
                        valores.put(EtiquetasModuloTres.IND_1_INSTRUMENTO.get(1),jsonValue?.getString("estatusConvenio"))
                    }
                    "Montos asignados" -> {
                        addChartsModuloIII(listOf(0,1,2),charts)
                        valores.put(EtiquetasModuloTres.IND_1_MONTOS_ASIGNADOS.get(0),doubleFormat(jsonValue?.getString("montoFederal")!!))
                        valores.put(EtiquetasModuloTres.IND_1_MONTOS_ASIGNADOS.get(1),doubleFormat(jsonValue.getString("montoEstatal")))
                        valores.put(EtiquetasModuloTres.IND_1_MONTOS_ASIGNADOS.get(2),doubleFormat(jsonValue.getString("montoPublico")))
                        valores.put(EtiquetasModuloTres.IND_1_MONTOS_ASIGNADOS.get(3),porcentajeFormat(jsonValue.getString("porcentajeFederal")))
                        valores.put(EtiquetasModuloTres.IND_1_MONTOS_ASIGNADOS.get(4),porcentajeFormat(jsonValue.getString("porcentajeEstatal")))
                    }
                    "Matrícula" -> {
                        addChartsModuloIII(listOf(3),charts)
                        valores.put(EtiquetasModuloTres.IND_1_MATRICULA.get(0),integerFormat(jsonValue?.getString("matriculaSuperior")!!))
                        valores.put(EtiquetasModuloTres.IND_1_MATRICULA.get(1),integerFormat(jsonValue.getString("matriculaMediaSuperior")))
                        valores.put(EtiquetasModuloTres.IND_1_MATRICULA.get(2),integerFormat(jsonValue.getString("matriculaTotal")))
                        valores.put(EtiquetasModuloTres.IND_1_MATRICULA.get(3),integerFormat(jsonValue.getString("matriculaTotalPonderada")))
                    }
                    "Subsidio por estudiante" -> {
                        addChartsModuloIII(listOf(4),charts)
                        valores.put(EtiquetasModuloTres.IND_1_SUBSIDIO_POR_ALUMNO.get(0),doubleFormat(jsonValue?.getString("subsidioFederalPorAlumno")!!))
                        valores.put(EtiquetasModuloTres.IND_1_SUBSIDIO_POR_ALUMNO.get(1),doubleFormat(jsonValue.getString("subsidioEstatalPorAlumno")))
                        valores.put(EtiquetasModuloTres.IND_1_SUBSIDIO_POR_ALUMNO.get(2),doubleFormat(jsonValue.getString("subsidioPublicoPorAlumno")))
                    }
                }
            }
            2 -> {
                val tabSeleccionado = arguments?.getString("tabSeleccionado")
                val jsonTabla = JSONArray(arguments?.getString("tabla"))
                val jsonValue = arguments?.getInt("indexOfElement")?.let { jsonTabla.getJSONObject(it) }
                var indexOfMonth : Int? = null
                when(tabSeleccionado!!){
                    "Calendarizado" -> {
                        addChartsModuloIII(listOf(0),charts)
                        val jsonListaCalendarizada = jsonValue?.getJSONArray("listaCalendarizado")
                        for (index in 0 until jsonListaCalendarizada?.length()!!){
                            val month = jsonListaCalendarizada.getJSONObject(index)
                            if (month.getString("mes").equals(titulo,true))
                                indexOfMonth = index
                        }
                        val jsonOfMonth = jsonListaCalendarizada.getJSONObject(indexOfMonth!!)
                        val jsonComprobaciones =  jsonOfMonth.getJSONArray("comprobaciones")
                        valores.put(EtiquetasModuloTres.IND_2_CALENDARIZADO.get(2),
                            doubleFormat(jsonOfMonth.getString("aportacion")))
                        for (index in 0 until jsonComprobaciones.length()){
                            valores.put(EtiquetasModuloTres.IND_2_CALENDARIZADO.get(3)+"."+index,
                                doubleFormat(jsonComprobaciones.getJSONObject(index).getString("comprobadaCLC")))
                            valores.put(EtiquetasModuloTres.IND_2_CALENDARIZADO.get(4)+"."+index,
                                jsonComprobaciones.getJSONObject(index).getString("numeroCLC"))
                            valores.put(EtiquetasModuloTres.IND_2_CALENDARIZADO.get(5)+"."+index,
                                jsonComprobaciones.getJSONObject(index).getString("fechaPago"))
                        }
                    }
                    "Reportado por la universidad" -> {
                        val jsonListaReportado = jsonValue?.getJSONArray("listaReportado")
                        for (index in 0 until jsonListaReportado?.length()!!){
                            val month = jsonListaReportado.getJSONObject(index)
                            if (month.getString("mes").equals(titulo,true))
                                indexOfMonth = index
                        }
                        val jsonOfMonth = jsonListaReportado.getJSONObject(indexOfMonth!!)
                        val jsonReportes = jsonOfMonth.getJSONArray("reportes")
                        for (index in 0 until jsonReportes.length()){
                            valores.put(EtiquetasModuloTres.IND_2_REPORTADO_UNIVERSIDAD.get(1)+"."+index,
                                doubleFormat(jsonReportes.getJSONObject(index).getString("aportacion")))
                            valores.put(EtiquetasModuloTres.IND_2_REPORTADO_UNIVERSIDAD.get(2)+"."+index,
                                jsonReportes.getJSONObject(index).getString("numeroTransferencia"))
                            valores.put(EtiquetasModuloTres.IND_2_REPORTADO_UNIVERSIDAD.get(3)+"."+index,
                                jsonReportes.getJSONObject(index).getString("fechaPago"))
                        }
                    }
                    "Según plataforma" -> {
                        val jsonListaPlataforma = jsonValue?.getJSONArray("listaPlataforma")
                        for (index in 0 until jsonListaPlataforma?.length()!!){
                            val month = jsonListaPlataforma.getJSONObject(index)
                            if (month.getString("mes").equals(titulo,true))
                                indexOfMonth = index
                        }
                        val jsonOfMonth = jsonListaPlataforma.getJSONObject(indexOfMonth!!)
                        valores.put(EtiquetasModuloTres.IND_2_SEGUN_PLATAFORMA.get(2),
                            doubleFormat(jsonOfMonth.getString("aportacion")))
                        valores.put(EtiquetasModuloTres.IND_2_SEGUN_PLATAFORMA.get(3),jsonOfMonth.getString("observacion"))
                        valores.put(EtiquetasModuloTres.IND_2_SEGUN_PLATAFORMA.get(4),jsonOfMonth.getString("adeudoMensual"))
                    }
                }
            }
            3 -> {
                val tabSeleccionado = arguments?.getString("tabSeleccionado")
                val jsonTabla = JSONArray(arguments?.getString("tabla"))
                val jsonValue = arguments?.getInt("indexOfElement")?.let { jsonTabla.getJSONObject(it) }
                var indexOfMonth : Int? = null
                when(tabSeleccionado!!){
                    "Calendarizado" -> {
                        addChartsModuloIII(listOf(0),charts)
                        val jsonAportaciones = jsonValue?.getJSONArray("aportaciones")
                        for (index in 0 until jsonAportaciones?.length()!!){
                            val month = jsonAportaciones.getJSONObject(index)
                            if (month.getString("mes").equals(titulo,true))
                                indexOfMonth = index
                        }
                        val jsonOfMonth = jsonAportaciones.getJSONObject(indexOfMonth!!)
                        val jsonReportes = jsonOfMonth.getJSONArray("reportes")
                        valores.put(EtiquetasModuloTres.IND_3_CALENDARIZADO.get(2),doubleFormat(jsonOfMonth.getString("calendarizada")))
                        for (index in 0 until jsonReportes.length()) {
                            valores.put(EtiquetasModuloTres.IND_3_CALENDARIZADO.get(3)+"."+index,
                                doubleFormat(jsonReportes.getJSONObject(index).getString("monto")))
                            valores.put(EtiquetasModuloTres.IND_3_CALENDARIZADO.get(4)+"."+index,
                                jsonReportes.getJSONObject(index).getString("numeroTransferencia"))
                            valores.put(EtiquetasModuloTres.IND_3_CALENDARIZADO.get(5)+"."+index,
                                jsonReportes.getJSONObject(index).getString("fechaPago"))
                        }
                    }
                    "Según plataforma" -> {
                        val jsonAportaciones = jsonValue?.getJSONArray("aportaciones")
                        for (index in 0 until jsonAportaciones?.length()!!){
                            val month = jsonAportaciones.getJSONObject(index)
                            if (month.getString("mes").equals(titulo,true))
                                indexOfMonth = index
                        }
                        val jsonOfMonth = jsonAportaciones.getJSONObject(indexOfMonth!!)
                        valores.put(EtiquetasModuloTres.IND_3_SEGUN_PLATAFORMA.get(2),doubleFormat(jsonOfMonth.getString("totalReportado")))
                        valores.put(EtiquetasModuloTres.IND_3_SEGUN_PLATAFORMA.get(3),jsonOfMonth.getString("observacion"))
                        valores.put(EtiquetasModuloTres.IND_3_SEGUN_PLATAFORMA.get(4),doubleFormat(jsonOfMonth.getString("adeudoMensual")))
                    }
                }
            }
            4 -> {
                val jsonTabla = JSONArray(arguments?.getString("tabla"))
                val jsonValue = arguments?.getInt("indexOfElement")?.let { jsonTabla.getJSONObject(it) }
                val totalesInd4 = arguments?.getString("totales")?.let { JSONObject(it) }
                when(titulo){
                    "Monto público" -> {
                        addChartsModuloIII(listOf(0),charts)
                        valores.put(EtiquetasModuloTres.IND_4_MONTO_PUBLICO.get(0),doubleFormat(jsonValue?.getString("montoPublico")!!))
                        valores.put(EtiquetasModuloTres.IND_4_MONTO_PUBLICO.get(1),"Formalizado")
                        valores.put(EtiquetasModuloTres.IND_4_MONTO_PUBLICO.get(2),doubleFormat(jsonValue?.getString("totalMinistrado")!!))
                        valores.put(EtiquetasModuloTres.IND_4_MONTO_PUBLICO.get(3), doubleFormat(totalesInd4?.getString("asignado")!!))
                        valores.put(EtiquetasModuloTres.IND_4_MONTO_PUBLICO.get(4), doubleFormat(totalesInd4.getString("ministrado")))
                    }
                    "Aportación federal" -> {
                        valores.put(EtiquetasModuloTres.IND_4_APORTACION_FEDERAL.get(0),doubleFormat(jsonValue?.getString("montoConvenioSEP")!!))
                        valores.put(EtiquetasModuloTres.IND_4_APORTACION_FEDERAL.get(1),doubleFormat(jsonValue?.getString("montoMinistradoSEP")!!))
                        valores.put(EtiquetasModuloTres.IND_4_APORTACION_FEDERAL.get(2),jsonValue?.getString("fechaEjecucionSEP"))
                        valores.put(EtiquetasModuloTres.IND_4_APORTACION_FEDERAL.get(3),jsonValue?.getString("observacionFederal"))
                        valores.put(EtiquetasModuloTres.IND_4_APORTACION_FEDERAL.get(4),doubleFormat(totalesInd4?.getString("aportacionSEP")!!))
                        valores.put(EtiquetasModuloTres.IND_4_APORTACION_FEDERAL.get(5),doubleFormat(totalesInd4?.getString("ministradoSEP")!!))
                    }
                    "Aportación estatal" -> {
                        val ministracionJsonValue = jsonValue?.getJSONArray("ministraciones")
                        valores.put(EtiquetasModuloTres.IND_4_APORTACION_ESTATAL.get(0),doubleFormat(jsonValue?.getString("montoConvenioEstado")!!))
                        for (index in 0 until ministracionJsonValue?.length()!!){
                            valores.put(EtiquetasModuloTres.IND_4_APORTACION_ESTATAL.get(1)+"."+index,
                                doubleFormat(ministracionJsonValue.getJSONObject(index).getString("monto")))
                            valores.put(EtiquetasModuloTres.IND_4_APORTACION_ESTATAL.get(2)+"."+index,
                                ministracionJsonValue.getJSONObject(index).getString("fechaEjecucion"))
                            valores.put(EtiquetasModuloTres.IND_4_APORTACION_ESTATAL.get(3)+"."+index,
                                ministracionJsonValue.getJSONObject(index).getString("observacion"))
                        }
                        valores.put(EtiquetasModuloTres.IND_4_APORTACION_ESTATAL.get(4),doubleFormat(totalesInd4?.getString("aportacionEstado")!!))
                        valores.put(EtiquetasModuloTres.IND_4_APORTACION_ESTATAL.get(5),doubleFormat(totalesInd4?.getString("ministradoEstado")!!))
                    }
                }
            }
            5 -> {
                val jsonTabla = JSONArray(arguments?.getString("tabla"))
                val jsonTotales = arguments?.getString("totales")
                val jsonValue = arguments?.getInt("indexOfElement")?.let { jsonTabla.getJSONObject(it) }
                when(titulo){
                    "PRODEP S247" -> {
                        addChartsModuloIII(listOf(0),charts)
                        valores.put(EtiquetasModuloTres.IND_5_PRODEP_S247.get(0),doubleFormat(jsonTotales!!))
                        valores.put(EtiquetasModuloTres.IND_5_PRODEP_S247.get(1),jsonValue?.getString("instrumento"))
                        valores.put(EtiquetasModuloTres.IND_5_PRODEP_S247.get(2),jsonValue?.getString("estatusConvenio"))
                        valores.put(EtiquetasModuloTres.IND_5_PRODEP_S247.get(3),jsonValue?.getString("fechaProdep"))
                        valores.put(EtiquetasModuloTres.IND_5_PRODEP_S247.get(4),doubleFormat(jsonValue?.getString("montoFederalProdep")!!))
                    }
                }
            }
            6 -> {
                val jsonTabla = JSONArray(arguments?.getString("tabla"))
                val jsonValue = arguments?.getInt("indexOfElement")?.let { jsonTabla.getJSONObject(it) }
                val totalesInd6 = JSONArray(arguments?.getString("totales"))
                when(titulo){
                    "Recurso extraordinario" -> {
                        addChartsModuloIII(listOf(0),charts)
                        valores.put(EtiquetasModuloTres.IND_6_RECURSO_EXTRAORDINARIO.get(0),doubleFormat(jsonValue?.getString("montoFederalRecExt")!!))
                        valores.put(EtiquetasModuloTres.IND_6_RECURSO_EXTRAORDINARIO.get(1),jsonValue.getString("instrumentoRecExt"))
                        valores.put(EtiquetasModuloTres.IND_6_RECURSO_EXTRAORDINARIO.get(2),jsonValue.getString("fechaExt"))
                        valores.put(EtiquetasModuloTres.IND_6_RECURSO_EXTRAORDINARIO.get(2),jsonValue.getString("estatusRecRxt"))
                        valores.put(EtiquetasModuloTres.IND_6_RECURSO_EXTRAORDINARIO.get(3),doubleFormat(totalesInd6.getString(0)))
                    }
                    "Regreso a clases" -> {
                        addChartsModuloIII(listOf(1),charts)
                        valores.put(EtiquetasModuloTres.IND_6_REGRESO_CLASES.get(0),doubleFormat(jsonValue?.getString("montoRegresoClases")!!))
                        valores.put(EtiquetasModuloTres.IND_6_REGRESO_CLASES.get(1),jsonValue.getString("instrumentoRegresoClases"))
                        valores.put(EtiquetasModuloTres.IND_6_REGRESO_CLASES.get(2),jsonValue.getString("estatusRegresoClases"))
                        valores.put(EtiquetasModuloTres.IND_6_REGRESO_CLASES.get(3),jsonValue.getString("fechaFirmaConvenioRegresoClases"))
                        valores.put(EtiquetasModuloTres.IND_6_REGRESO_CLASES.get(4),doubleFormat(totalesInd6.getString(1)))
                    }
                    "Inclusión Estancias U006" -> {
                        addChartsModuloIII(listOf(2),charts)
                        valores.put(EtiquetasModuloTres.IND_6_INCLUSION_ESTANCIAS_U006.get(0),doubleFormat(jsonValue?.getString("montoInclusionEstancias")!!))
                        valores.put(EtiquetasModuloTres.IND_6_INCLUSION_ESTANCIAS_U006.get(1),jsonValue.getString("instrumentoInclusionEstancias"))
                        valores.put(EtiquetasModuloTres.IND_6_INCLUSION_ESTANCIAS_U006.get(2),jsonValue.getString("estatusInclusionEstancias"))
                        valores.put(EtiquetasModuloTres.IND_6_INCLUSION_ESTANCIAS_U006.get(3),jsonValue.getString("fechaFirmaConvenioInclusionEstancias"))
                        valores.put(EtiquetasModuloTres.IND_6_INCLUSION_ESTANCIAS_U006.get(4),doubleFormat(totalesInd6.getString(2)))
                    }
                    "Incremento Salarial U006" -> {
                        addChartsModuloIII(listOf(3),charts)
                        valores.put(EtiquetasModuloTres.IND_6_INCREMENTO_SALARIAL_U006.get(0),jsonValue?.getString("instrumentoIncrementoSalarial"))
                        valores.put(EtiquetasModuloTres.IND_6_INCREMENTO_SALARIAL_U006.get(1),jsonValue?.getString("estatusIncrementoSalarial"))
                        valores.put(EtiquetasModuloTres.IND_6_INCREMENTO_SALARIAL_U006.get(2),jsonValue?.getString("informacionIncrementoSalarial"))
                        valores.put(EtiquetasModuloTres.IND_6_INCREMENTO_SALARIAL_U006.get(3),doubleFormat(jsonValue?.getString("montoFederalIncrementoSalarial")!!))
                        valores.put(EtiquetasModuloTres.IND_6_INCREMENTO_SALARIAL_U006.get(4),jsonValue.getString("clcIncrementoSalarial"))
                        valores.put(EtiquetasModuloTres.IND_6_INCREMENTO_SALARIAL_U006.get(5),doubleFormat(jsonValue.getString("montoEstatalIncSalU006")))
                        valores.put(EtiquetasModuloTres.IND_6_INCREMENTO_SALARIAL_U006.get(6),doubleFormat(jsonValue.getString("montoPublicoIncSalU006")))
                        valores.put(EtiquetasModuloTres.IND_6_INCREMENTO_SALARIAL_U006.get(7),doubleFormat(totalesInd6.getString(3)))
                    }
                    "Déficit U006" -> {
                        addChartsModuloIII(listOf(4),charts)
                        valores.put(EtiquetasModuloTres.IND_6_DEFICIT_U006.get(0),doubleFormat(jsonValue?.getString("deficit")!!))
                        valores.put(EtiquetasModuloTres.IND_6_DEFICIT_U006.get(1),jsonValue.getString("instrumentoDeficit"))
                        valores.put(EtiquetasModuloTres.IND_6_DEFICIT_U006.get(2),jsonValue.getString("estatusDeficit"))
                        valores.put(EtiquetasModuloTres.IND_6_DEFICIT_U006.get(3),jsonValue.getString("fechaFirmaConvenioDeficitU006"))
                        valores.put(EtiquetasModuloTres.IND_6_DEFICIT_U006.get(4),doubleFormat(jsonValue.getString("montoEstatalDeficitU006")))
                        valores.put(EtiquetasModuloTres.IND_6_DEFICIT_U006.get(5),doubleFormat(jsonValue.getString("montoPublicoDeficitU006")))
                        valores.put(EtiquetasModuloTres.IND_6_DEFICIT_U006.get(6),doubleFormat(totalesInd6.getString(4)))
                    }
                }
            }
            7 -> {
                val jsonTabla = JSONArray(arguments?.getString("tabla"))
                val jsonTotales = arguments?.getString("totales")
                val jsonValue = arguments?.getInt("indexOfElement")?.let { jsonTabla.getJSONObject(it) }
                when(titulo){
                    "Recursos asignados" -> {
                        addChartsModuloIII(listOf(0),charts)
                        valores.put(EtiquetasModuloTres.IND_7_RECURSOS_ASIGNADOS.get(0),jsonValue?.getString("instrumento"))
                        valores.put(EtiquetasModuloTres.IND_7_RECURSOS_ASIGNADOS.get(1),jsonValue?.getString("estatusConvenio"))
                        valores.put(EtiquetasModuloTres.IND_7_RECURSOS_ASIGNADOS.get(2),jsonValue?.getString("fechaFormalizacion"))
                        valores.put(EtiquetasModuloTres.IND_7_RECURSOS_ASIGNADOS.get(3),doubleFormat(jsonValue?.getString("recursosAsignados")!!))
                        valores.put(EtiquetasModuloTres.IND_7_RECURSOS_ASIGNADOS.get(4),
                            jsonValue.getString("observacionEspecifica")
                        )
                        valores.put(EtiquetasModuloTres.IND_7_RECURSOS_ASIGNADOS.get(5),doubleFormat(jsonTotales!!))
                    }
                }
            }
            8 -> {
                val jsonTabla = JSONArray(arguments?.getString("tabla"))
                val jsonValue = arguments?.getInt("indexOfElement")?.let { jsonTabla.getJSONObject(it) }
                when(titulo){
                    "Cobertura" -> {
                        addChartsModuloIII(listOf(0,1),charts)
                        valores.put(EtiquetasModuloTres.IND_8_COBERTURA.get(0),jsonValue?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_8_COBERTURA.get(1),integerFormat(jsonValue?.getString("poblacion18A22HombreCobertura")!!))
                        valores.put(EtiquetasModuloTres.IND_8_COBERTURA.get(2),integerFormat(jsonValue.getString("poblacion18A22MujerCobertura")))
                        valores.put(EtiquetasModuloTres.IND_8_COBERTURA.get(3),integerFormat(jsonValue.getString("poblacion18A22TotalCobertura")))
                        valores.put(EtiquetasModuloTres.IND_8_COBERTURA.get(4),porcentajeFormat(jsonValue.getString("porcentajeCoberturaHombreCobertura")))
                        valores.put(EtiquetasModuloTres.IND_8_COBERTURA.get(5),porcentajeFormat(jsonValue.getString("porcentajeCoberturaMujerCobertura")))
                        valores.put(EtiquetasModuloTres.IND_8_COBERTURA.get(6),porcentajeFormat(jsonValue.getString("tasaBrutaCobertura")))
                    }
                    "Cobertura 2012-2013" -> {
                        addChartsModuloIII(listOf(5),charts)
                        valores.put(EtiquetasModuloTres.IND_8_COBERTURA_2012_2013.get(0),jsonValue?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_8_COBERTURA_2012_2013.get(1),integerFormat(jsonValue?.getString("poblacion18A22HombreCobertura2013")!!))
                        valores.put(EtiquetasModuloTres.IND_8_COBERTURA_2012_2013.get(2),integerFormat(jsonValue?.getString("poblacion18A22MujerCobertura2013")!!))
                        valores.put(EtiquetasModuloTres.IND_8_COBERTURA_2012_2013.get(3),integerFormat(jsonValue?.getString("poblacion18A22TotalCobertura2013")!!))
                        valores.put(EtiquetasModuloTres.IND_8_COBERTURA_2012_2013.get(4),porcentajeFormat(jsonValue?.getString("tasaBrutaCobertura2013")!!))
                    }
                    "Matrícula Nivel Superior" -> {
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_NIVEL_SUPERIOR.get(0),jsonValue?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_NIVEL_SUPERIOR.get(1),integerFormat(jsonValue?.getString("matriculaLicenciaturaNs")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_NIVEL_SUPERIOR.get(2),integerFormat(jsonValue?.getString("totalMatriculaTsuLicenciaturaNs")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_NIVEL_SUPERIOR.get(3),integerFormat(jsonValue?.getString("matriculaTsuLicenciaturaHombresNs")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_NIVEL_SUPERIOR.get(4),integerFormat(jsonValue?.getString("matriculaTsuLicenciaturaMujeresNs")!!))
                    }
                    "Matrícula Posgrado" -> {
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_POSGRADO.get(0),jsonValue?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_POSGRADO.get(1),integerFormat(jsonValue?.getString("matriculaEspecialidadNs")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_POSGRADO.get(2),integerFormat(jsonValue?.getString("matriculaMaestriaNs")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_POSGRADO.get(3),integerFormat(jsonValue?.getString("matriculaDoctoradoNs")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_POSGRADO.get(4),integerFormat(jsonValue?.getString("totalMatriculaPosgradoNs")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_POSGRADO.get(5),integerFormat(jsonValue?.getString("matriculaPosgradoHombresNs")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_POSGRADO.get(6),integerFormat(jsonValue?.getString("matriculaPosgradoMujeresNs")!!))
                    }
                    "Matrícula Nivel Medio Superior" -> {
                        addChartsModuloIII(listOf(2),charts)
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_NIVEL_MEDIO_SUPERIOR.get(0),jsonValue?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_NIVEL_MEDIO_SUPERIOR.get(1),integerFormat(jsonValue?.getString("totalEmsEgresadosPublicoParticularNms")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_NIVEL_MEDIO_SUPERIOR.get(2),integerFormat(jsonValue?.getString("emsEgresadosPublicoNms")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_NIVEL_MEDIO_SUPERIOR.get(3),integerFormat(jsonValue?.getString("emsEgresadosPublicoFederalNms")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_NIVEL_MEDIO_SUPERIOR.get(4),integerFormat(jsonValue?.getString("emsEgresadosPublicoEstatalNms")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_NIVEL_MEDIO_SUPERIOR.get(5),integerFormat(jsonValue?.getString("emsEgresadosPublicoAutonomoNms")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_NIVEL_MEDIO_SUPERIOR.get(6),integerFormat(jsonValue?.getString("emsEgresadosParticularNms")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_NIVEL_MEDIO_SUPERIOR.get(7),integerFormat(jsonValue?.getString("totalPrimerIngresoPublicoParticularNms")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_NIVEL_MEDIO_SUPERIOR.get(8),integerFormat(jsonValue?.getString("primerIngresoPublicoTsuLicNms")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_NIVEL_MEDIO_SUPERIOR.get(9),integerFormat(jsonValue?.getString("primerIngresoPaticularTsuLicNms")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_NIVEL_MEDIO_SUPERIOR.get(10),porcentajeFormat(jsonValue?.getString("totalIndiceAbsorcion")!!))
                    }
                    "Desglose IES" -> {
                        valores.put(EtiquetasModuloTres.IND_8_DESGLOSE_IES.get(0),jsonValue?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_8_DESGLOSE_IES.get(1),integerFormat(jsonValue?.getString("numeroIesEstadoPublicasParticularesDies")!!))
                        valores.put(EtiquetasModuloTres.IND_8_DESGLOSE_IES.get(2),integerFormat(jsonValue?.getString("upeEstadoDies")!!))
                        valores.put(EtiquetasModuloTres.IND_8_DESGLOSE_IES.get(3),integerFormat(jsonValue?.getString("upeasEstadoDies")!!))
                        valores.put(EtiquetasModuloTres.IND_8_DESGLOSE_IES.get(4),integerFormat(jsonValue?.getString("uiEstadoDies")!!))
                    }
                    "Matrícula ES Modalidad" -> {
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_ES_MODALIDAD.get(0),jsonValue?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_ES_MODALIDAD.get(1),integerFormat(jsonValue?.getString("matriculaEscolTsuLicPosDesMod")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_ES_MODALIDAD.get(2),integerFormat(jsonValue?.getString("matriculaNoEscolTsuLicPosDesMod")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_ES_MODALIDAD.get(3),integerFormat(jsonValue?.getString("matriculaMixtaTsuLicPosDesMod")!!))
                    }
                    "PTC, Perfil Deseable, SNI" -> {
                        valores.put(EtiquetasModuloTres.IND_8_PTC_PERFIL_SNI.get(0),jsonValue?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_8_PTC_PERFIL_SNI.get(1),integerFormat(jsonValue?.getString("totalPtc")!!))
                        valores.put(EtiquetasModuloTres.IND_8_PTC_PERFIL_SNI.get(2),integerFormat(jsonValue?.getString("conPerfilDeseableVigente")!!))
                        valores.put(EtiquetasModuloTres.IND_8_PTC_PERFIL_SNI.get(3),integerFormat(jsonValue?.getString("conSniVigente")!!))
                    }
                    "Matrícula Discapacidad ES" -> {
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_DISCAPACIDAD_ES.get(0),jsonValue?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_DISCAPACIDAD_ES.get(1),integerFormat(jsonValue?.getString("matriculaDiscapacidadTsuLicPosDesDiscapacidad")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_DISCAPACIDAD_ES.get(2),integerFormat(jsonValue?.getString("hombresDesDiscapacidad")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_DISCAPACIDAD_ES.get(3),integerFormat(jsonValue?.getString("mujeresDesDiscapacidad")!!))
                    }
                    "Matrícula HLI ES" -> {
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_HLI_ES.get(0),jsonValue?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_HLI_ES.get(1),integerFormat(jsonValue?.getString("matriculaHliTsuLicPosDesHli")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_HLI_ES.get(2),integerFormat(jsonValue?.getString("hombresDeshli")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_HLI_ES.get(3),integerFormat(jsonValue?.getString("mujeresDeshli")!!))
                    }
                    "Matrícula por áreas de la CMPE" -> {
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_AREAS_CMPE.get(0),jsonValue?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_AREAS_CMPE.get(1),integerFormat(jsonValue?.getString("educacionCmpe")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_AREAS_CMPE.get(2),integerFormat(jsonValue?.getString("artesHumanidadesCmpe")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_AREAS_CMPE.get(3),integerFormat(jsonValue?.getString("cienciasSocialesDerechoCmpe")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_AREAS_CMPE.get(4),integerFormat(jsonValue?.getString("administracionNegociosCmpe")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_AREAS_CMPE.get(5),integerFormat(jsonValue?.getString("cienciasNaturalesMatematicasStadisticaCmpe")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_AREAS_CMPE.get(6),integerFormat(jsonValue?.getString("teconologiasInformacionComunicacionCmpe")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_AREAS_CMPE.get(7),integerFormat(jsonValue?.getString("ingenieriaManufacturaConstruccionCmpe")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_AREAS_CMPE.get(8),integerFormat(jsonValue?.getString("agronomiaVeterinariaCmpe")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_AREAS_CMPE.get(9),integerFormat(jsonValue?.getString("cienciasSaludCmpe")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_AREAS_CMPE.get(10),integerFormat(jsonValue?.getString("serviciosCmpe")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_AREAS_CMPE.get(11),integerFormat(jsonValue?.getString("totalMatriculaAreasCmpe")!!))
                        valores.put("Referencia","E: Educación" + System.getProperty("line.separator") +
                                "AH: Artes y Humanidades" + System.getProperty("line.separator") +
                                "CSD: Ciencias Sociales y Derecho" + System.getProperty("line.separator") +
                                "AN: Administración y Negocios" + System.getProperty("line.separator") +
                                "CNME: Ciencias Naturales, Matemáticas y Estadística." + System.getProperty("line.separator") +
                                "TIC: Tecnologías de la Información y la Comunicación" + System.getProperty("line.separator") +
                                "IMC: Ingeniería, Manufactura y Construcción" + System.getProperty("line.separator") +
                                "AV: Agronomía y Veterinaria" + System.getProperty("line.separator") +
                                "CS: Ciencias de la Salud" + System.getProperty("line.separator") +
                                "S: Servicios")
                    }
                    "Matrícula Buena Calidad Normal, TSU y Lic" -> {
                        addChartsModuloIII(listOf(3,4),charts)
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_BUENA_CALIDAD_NORMAL_TSU_LIC.get(0),jsonValue?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_BUENA_CALIDAD_NORMAL_TSU_LIC.get(1),integerFormat(jsonValue?.getString("matriculaCalidadBc")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_BUENA_CALIDAD_NORMAL_TSU_LIC.get(2),integerFormat(jsonValue?.getString("matriculaTotalBc")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_BUENA_CALIDAD_NORMAL_TSU_LIC.get(3),integerFormat(jsonValue?.getString("matriculaEvaluableBc")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_BUENA_CALIDAD_NORMAL_TSU_LIC.get(4),porcentajeFormat(jsonValue?.getString("porcentajeCoberturaTotalCalidadBc")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_BUENA_CALIDAD_NORMAL_TSU_LIC.get(5),porcentajeFormat(jsonValue?.getString("porcentajeCoberturaEvaluableBc")!!))
                    }
                    "Matrícula Subsistema ES" -> {
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_SUBSISTEMA_ES.get(0),jsonValue?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_SUBSISTEMA_ES.get(1),integerFormat(jsonValue?.getString("upeSubsistema")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_SUBSISTEMA_ES.get(2),integerFormat(jsonValue?.getString("upeasSubsistema")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_SUBSISTEMA_ES.get(3),integerFormat(jsonValue?.getString("uiSubsistema")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_SUBSISTEMA_ES.get(4),integerFormat(jsonValue?.getString("upfSubsistema")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_SUBSISTEMA_ES.get(5),integerFormat(jsonValue?.getString("utSubsistema")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_SUBSISTEMA_ES.get(6),integerFormat(jsonValue?.getString("upolSubsistema")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_SUBSISTEMA_ES.get(7),integerFormat(jsonValue?.getString("institutosTecnologicosSubsistema")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_SUBSISTEMA_ES.get(8),integerFormat(jsonValue?.getString("iesEntidadesFederativasSubsistema")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_SUBSISTEMA_ES.get(9),integerFormat(jsonValue?.getString("normalesSubsistema")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_SUBSISTEMA_ES.get(10),integerFormat(jsonValue?.getString("upnProvinciaSubsistema")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_SUBSISTEMA_ES.get(11),integerFormat(jsonValue?.getString("camSubsistema")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_SUBSISTEMA_ES.get(12),integerFormat(jsonValue?.getString("particularesSubsistema")!!))
                        valores.put(EtiquetasModuloTres.IND_8_MATRICULA_SUBSISTEMA_ES.get(13),integerFormat(jsonValue?.getString("totalSubsistema")!!))
                    }
                }
            }
            9 -> {
                val jsonTabla = JSONArray(arguments?.getString("tabla"))
                val jsonValue = arguments?.getInt("indexOfElement")?.let { jsonTabla.getJSONObject(it) }
                val totalesInd9 = JSONArray(arguments?.getString("totales"))
                when(titulo){
                    "Matrícula Nivel Educativo" -> {
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_MATRICULA_NIVEL_EDUCATIVO.get(0),integerFormat(totalesInd9.getString(0)))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_MATRICULA_NIVEL_EDUCATIVO.get(1),integerFormat(totalesInd9.getString(1)))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_MATRICULA_NIVEL_EDUCATIVO.get(2),integerFormat(totalesInd9.getString(2)))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_MATRICULA_NIVEL_EDUCATIVO.get(3),integerFormat(totalesInd9.getString(3)))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_MATRICULA_NIVEL_EDUCATIVO.get(4),integerFormat(totalesInd9.getString(4)))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_MATRICULA_NIVEL_EDUCATIVO.get(5),integerFormat(totalesInd9.getString(5)))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_MATRICULA_NIVEL_EDUCATIVO.get(6),integerFormat(totalesInd9.getString(6)))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_MATRICULA_NIVEL_EDUCATIVO.get(7),integerFormat(totalesInd9.getString(7)))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_MATRICULA_NIVEL_EDUCATIVO.get(8),integerFormat(totalesInd9.getString(8)))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_MATRICULA_NIVEL_EDUCATIVO.get(9),integerFormat(totalesInd9.getString(9)))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_MATRICULA_NIVEL_EDUCATIVO.get(10),integerFormat(totalesInd9.getString(10)))

                        valores.put(EtiquetasModuloTres.IND_9_MATRICULA_NIVEL_EDUCATIVO.get(0),integerFormat(jsonValue?.getString("tsuPaNe")!!))
                        valores.put(EtiquetasModuloTres.IND_9_MATRICULA_NIVEL_EDUCATIVO.get(1),integerFormat(jsonValue?.getString("licenciaturaNe")!!))
                        valores.put(EtiquetasModuloTres.IND_9_MATRICULA_NIVEL_EDUCATIVO.get(2),integerFormat(jsonValue?.getString("totalTsuLicNe")!!))
                        valores.put(EtiquetasModuloTres.IND_9_MATRICULA_NIVEL_EDUCATIVO.get(3),integerFormat(jsonValue?.getString("especialidadNe")!!))
                        valores.put(EtiquetasModuloTres.IND_9_MATRICULA_NIVEL_EDUCATIVO.get(4),integerFormat(jsonValue?.getString("maestriaNe")!!))
                        valores.put(EtiquetasModuloTres.IND_9_MATRICULA_NIVEL_EDUCATIVO.get(5),integerFormat(jsonValue?.getString("doctoradoNe")!!))
                        valores.put(EtiquetasModuloTres.IND_9_MATRICULA_NIVEL_EDUCATIVO.get(6),integerFormat(jsonValue?.getString("totalPosgradoNe")!!))
                        valores.put(EtiquetasModuloTres.IND_9_MATRICULA_NIVEL_EDUCATIVO.get(7),integerFormat(jsonValue?.getString("totalTsuLicPosNe")!!))
                        valores.put(EtiquetasModuloTres.IND_9_MATRICULA_NIVEL_EDUCATIVO.get(8),integerFormat(jsonValue?.getString("hombresEsNe")!!))
                        valores.put(EtiquetasModuloTres.IND_9_MATRICULA_NIVEL_EDUCATIVO.get(9),integerFormat(jsonValue?.getString("mujeresEsNe")!!))
                        valores.put(EtiquetasModuloTres.IND_9_MATRICULA_NIVEL_EDUCATIVO.get(10),integerFormat(jsonValue?.getString("totalEsNe")!!))
                    }
                    "Total Discapacidad ES" -> {
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_DISCAPACIDAD_ES_TOTAL.get(0),integerFormat(totalesInd9.getString(11)))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_DISCAPACIDAD_ES_TOTAL.get(1),integerFormat(totalesInd9.getString(12)))

                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_DISCAPACIDAD_ES.get(0),integerFormat(jsonValue?.getString("totalHombresDiscapacidadEsNe")!!))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_DISCAPACIDAD_ES.get(1),integerFormat(jsonValue?.getString("totalMujeresDiscapacidadEsNe")!!))
                    }
                    "Total HLI ES" -> {
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_HLI_ES_TOTAL.get(0),integerFormat(totalesInd9.getString(13)))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_HLI_ES_TOTAL.get(1),integerFormat(totalesInd9.getString(14)))

                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_HLI_ES.get(0),integerFormat(jsonValue?.getString("totalHombresHliEsNe")!!))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_HLI_ES.get(1),integerFormat(jsonValue?.getString("totalMujeresHliEsNe")!!))
                    }
                    "Egresados ES" -> {
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_EGRESADOS_ES.get(0),integerFormat(totalesInd9.getString(15)))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_EGRESADOS_ES.get(1),integerFormat(totalesInd9.getString(16)))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_EGRESADOS_ES.get(2),integerFormat(totalesInd9.getString(17)))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_EGRESADOS_ES.get(3),integerFormat(totalesInd9.getString(18)))

                        valores.put(EtiquetasModuloTres.IND_9_EGRESADOS_ES.get(0),integerFormat(jsonValue?.getString("hombresEgresados")!!))
                        valores.put(EtiquetasModuloTres.IND_9_EGRESADOS_ES.get(1),integerFormat(jsonValue?.getString("mujeresEgresados")!!))
                        valores.put(EtiquetasModuloTres.IND_9_EGRESADOS_ES.get(2),integerFormat(jsonValue?.getString("totalDiscapacidadEgresados")!!))
                        valores.put(EtiquetasModuloTres.IND_9_EGRESADOS_ES.get(3),integerFormat(jsonValue?.getString("totalHliEgresados")!!))
                    }
                    "Titulados Es" -> {
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_TITULADOS_ES.get(0),integerFormat(totalesInd9.getString(19)))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_TITULADOS_ES.get(1),integerFormat(totalesInd9.getString(20)))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_TITULADOS_ES.get(2),integerFormat(totalesInd9.getString(21)))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_TITULADOS_ES.get(3),integerFormat(totalesInd9.getString(22)))

                        valores.put(EtiquetasModuloTres.IND_9_TITULADOS_ES.get(0),integerFormat(jsonValue?.getString("hombresTitulados")!!))
                        valores.put(EtiquetasModuloTres.IND_9_TITULADOS_ES.get(1),integerFormat(jsonValue?.getString("mujeresTitulados")!!))
                        valores.put(EtiquetasModuloTres.IND_9_TITULADOS_ES.get(2),integerFormat(jsonValue?.getString("totalDiscapacidadTitulados")!!))
                        valores.put(EtiquetasModuloTres.IND_9_TITULADOS_ES.get(3),integerFormat(jsonValue?.getString("totalHliTitulados")!!))
                    }
                    "Matrícula por Modalidad" -> {
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_MATRICULA_MODALIDAD.get(0),integerFormat(totalesInd9.getString(23)))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_MATRICULA_MODALIDAD.get(1),integerFormat(totalesInd9.getString(24)))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_MATRICULA_MODALIDAD.get(2),integerFormat(totalesInd9.getString(25)))

                        valores.put(EtiquetasModuloTres.IND_9_MATRICULA_MODALIDAD.get(0),integerFormat(jsonValue?.getString("escolarizadaMm")!!))
                        valores.put(EtiquetasModuloTres.IND_9_MATRICULA_MODALIDAD.get(1),integerFormat(jsonValue?.getString("noEscolarizadaMm")!!))
                        valores.put(EtiquetasModuloTres.IND_9_MATRICULA_MODALIDAD.get(2),integerFormat(jsonValue?.getString("mixtaMm")!!))
                    }
                    "Matrícula por Programa" -> {
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_MATRICULA_PROGRAMA.get(0),integerFormat(totalesInd9.getString(26)))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_MATRICULA_PROGRAMA.get(1),integerFormat(totalesInd9.getString(27)))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_MATRICULA_PROGRAMA.get(2),integerFormat(totalesInd9.getString(28)))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_MATRICULA_PROGRAMA.get(3),integerFormat(totalesInd9.getString(29)))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_MATRICULA_PROGRAMA.get(4),integerFormat(totalesInd9.getString(30)))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_MATRICULA_PROGRAMA.get(5),integerFormat(totalesInd9.getString(31)))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_MATRICULA_PROGRAMA.get(6),integerFormat(totalesInd9.getString(32)))

                        valores.put(EtiquetasModuloTres.IND_9_MATRICULA_PROGRAMA.get(0),integerFormat(jsonValue?.getString("tsuPaMp")!!))
                        valores.put(EtiquetasModuloTres.IND_9_MATRICULA_PROGRAMA.get(1),integerFormat(jsonValue?.getString("licenciaturaMp")!!))
                        valores.put(EtiquetasModuloTres.IND_9_MATRICULA_PROGRAMA.get(2),integerFormat(jsonValue?.getString("especialidadMp")!!))
                        valores.put(EtiquetasModuloTres.IND_9_MATRICULA_PROGRAMA.get(3),integerFormat(jsonValue?.getString("maestriaMp")!!))
                        valores.put(EtiquetasModuloTres.IND_9_MATRICULA_PROGRAMA.get(4),integerFormat(jsonValue?.getString("doctoradoMp")!!))
                        valores.put(EtiquetasModuloTres.IND_9_MATRICULA_PROGRAMA.get(5),integerFormat(jsonValue?.getString("totalHombresMp")!!))
                        valores.put(EtiquetasModuloTres.IND_9_MATRICULA_PROGRAMA.get(6),integerFormat(jsonValue?.getString("totalMujeresMp")!!))
                    }
                    "Total de Programas" -> {
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_PROGRAMAS_TOTAL.get(0),integerFormat(totalesInd9.getString(33)))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_PROGRAMAS_TOTAL.get(1),integerFormat(totalesInd9.getString(34)))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_PROGRAMAS_TOTAL.get(2),integerFormat(totalesInd9.getString(35)))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_PROGRAMAS_TOTAL.get(3),integerFormat(totalesInd9.getString(36)))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_PROGRAMAS_TOTAL.get(4),integerFormat(totalesInd9.getString(37)))

                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_PROGRAMAS.get(0),integerFormat(jsonValue?.getString("tsuPaTp")!!))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_PROGRAMAS.get(1),integerFormat(jsonValue?.getString("licenciaturaTp")!!))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_PROGRAMAS.get(2),integerFormat(jsonValue?.getString("especialidadTp")!!))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_PROGRAMAS.get(3),integerFormat(jsonValue?.getString("maestriaTp")!!))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_PROGRAMAS.get(4),integerFormat(jsonValue?.getString("doctoradoTp")!!))
                    }
                    "Matrícula por áreas de la CMPE" -> {
                        addChartsModuloIII(listOf(0),charts)
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_MATRICULA_AREA_CMPE.get(0),integerFormat(totalesInd9.getString(38)))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_MATRICULA_AREA_CMPE.get(1),integerFormat(totalesInd9.getString(39)))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_MATRICULA_AREA_CMPE.get(2),integerFormat(totalesInd9.getString(40)))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_MATRICULA_AREA_CMPE.get(3),integerFormat(totalesInd9.getString(41)))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_MATRICULA_AREA_CMPE.get(4),integerFormat(totalesInd9.getString(42)))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_MATRICULA_AREA_CMPE.get(5),integerFormat(totalesInd9.getString(43)))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_MATRICULA_AREA_CMPE.get(6),integerFormat(totalesInd9.getString(44)))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_MATRICULA_AREA_CMPE.get(7),integerFormat(totalesInd9.getString(45)))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_MATRICULA_AREA_CMPE.get(8),integerFormat(totalesInd9.getString(46)))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_MATRICULA_AREA_CMPE.get(9),integerFormat(totalesInd9.getString(47)))
                        valores.put(EtiquetasModuloTres.IND_9_TOTAL_MATRICULA_AREA_CMPE.get(10),integerFormat(totalesInd9.getString(48)))

                        valores.put(EtiquetasModuloTres.IND_9_MATRICULA_AREA_CMPE.get(0),integerFormat(jsonValue?.getString("educacionMaCmpe")!!))
                        valores.put(EtiquetasModuloTres.IND_9_MATRICULA_AREA_CMPE.get(1),integerFormat(jsonValue?.getString("artesHumanidadesMaCmpe")!!))
                        valores.put(EtiquetasModuloTres.IND_9_MATRICULA_AREA_CMPE.get(2),integerFormat(jsonValue?.getString("cienciasSocialesDerechoMaCmpe")!!))
                        valores.put(EtiquetasModuloTres.IND_9_MATRICULA_AREA_CMPE.get(3),integerFormat(jsonValue?.getString("administracionNegociosMaCmpe")!!))
                        valores.put(EtiquetasModuloTres.IND_9_MATRICULA_AREA_CMPE.get(4),integerFormat(jsonValue?.getString("cienciasNaturalesMatematicasEstadisticaMaCmpe")!!))
                        valores.put(EtiquetasModuloTres.IND_9_MATRICULA_AREA_CMPE.get(5),integerFormat(jsonValue?.getString("ticMaCmpe")!!))
                        valores.put(EtiquetasModuloTres.IND_9_MATRICULA_AREA_CMPE.get(6),integerFormat(jsonValue?.getString("ingenieriaManufacturaConstruccionMaCmpe")!!))
                        valores.put(EtiquetasModuloTres.IND_9_MATRICULA_AREA_CMPE.get(7),integerFormat(jsonValue?.getString("agronomiaVeterinariaMaCmpe")!!))
                        valores.put(EtiquetasModuloTres.IND_9_MATRICULA_AREA_CMPE.get(8),integerFormat(jsonValue?.getString("cienciasSaludMaCmpe")!!))
                        valores.put(EtiquetasModuloTres.IND_9_MATRICULA_AREA_CMPE.get(9),integerFormat(jsonValue?.getString("serviciosMaCmpe")!!))
                        valores.put(EtiquetasModuloTres.IND_9_MATRICULA_AREA_CMPE.get(10),integerFormat(jsonValue?.getString("totalMatriculaEs")!!))
                        valores.put("Referencia","E: Educación" + System.getProperty("line.separator") +
                                "AH: Artes y Humanidades" + System.getProperty("line.separator") +
                                "CSD: Ciencias Sociales y Derecho" + System.getProperty("line.separator") +
                                "AN: Administración y Negocios" + System.getProperty("line.separator") +
                                "CNME: Ciencias Naturales, Matemáticas y Estadística." + System.getProperty("line.separator") +
                                "TIC: Tecnologías de la Información y la Comunicación" + System.getProperty("line.separator") +
                                "IMC: Ingeniería, Manufactura y Construcción" + System.getProperty("line.separator") +
                                "AV: Agronomía y Veterinaria" + System.getProperty("line.separator") +
                                "CS: Ciencias de la Salud" + System.getProperty("line.separator") +
                                "S: Servicios")
                    }
                }
            }
            10 -> {
                val jsonTabla = JSONArray(arguments?.getString("tabla"))
                val jsonValue = arguments?.getInt("indexOfElement")?.let { jsonTabla.getJSONObject(it) }
                val indicadorIES = jsonValue?.getJSONObject("indicadorIES")
                val sisup = jsonValue?.getJSONObject("sisup")
                when(titulo){
                    "Bachillerato Tecnológico y Profesional Técnico" -> {
                        valores.put(EtiquetasModuloTres.IND_10_BACHILLERATO_PROFESIONAL_TECNICO.get(0),indicadorIES?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_10_BACHILLERATO_PROFESIONAL_TECNICO.get(1),integerFormat(indicadorIES?.getString("bachilleratoTecnologico")!!))
                        valores.put(EtiquetasModuloTres.IND_10_BACHILLERATO_PROFESIONAL_TECNICO.get(2),integerFormat(indicadorIES?.getString("profesionalTecnico")!!))
                        valores.put(EtiquetasModuloTres.IND_10_BACHILLERATO_PROFESIONAL_TECNICO.get(3),integerFormat(indicadorIES?.getString("subtotalMatriculaBgBt")!!))
                        valores.put(EtiquetasModuloTres.IND_10_BACHILLERATO_PROFESIONAL_TECNICO.get(4),integerFormat(indicadorIES?.getString("subtotalMatriculaBtPt")!!))
                        valores.put(EtiquetasModuloTres.IND_10_BACHILLERATO_PROFESIONAL_TECNICO.get(5),integerFormat(indicadorIES?.getString("totalMatriculaBgBt")!!))
                    }
                    "EMS" -> {
                        valores.put(EtiquetasModuloTres.IND_10_EMS.get(0),indicadorIES?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_10_EMS.get(1),integerFormat(indicadorIES?.getString("hombresMatriculaEms")!!))
                        valores.put(EtiquetasModuloTres.IND_10_EMS.get(2),integerFormat(indicadorIES?.getString("mujeresMatriculaEms")!!))
                        valores.put(EtiquetasModuloTres.IND_10_EMS.get(3),integerFormat(indicadorIES?.getString("totalMatriculaEms")!!))
                        valores.put(EtiquetasModuloTres.IND_10_EMS.get(4),integerFormat(indicadorIES?.getString("conDiscapacidadMatriculaEms")!!))
                        valores.put(EtiquetasModuloTres.IND_10_EMS.get(5),integerFormat(indicadorIES?.getString("hliMatriculaEms")!!))
                    }
                    "ES Nivel Educativo" -> {
                        valores.put(EtiquetasModuloTres.IND_10_ES_NIVEL_EDUCATIVO.get(0),indicadorIES?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_10_ES_NIVEL_EDUCATIVO.get(1),integerFormat(indicadorIES?.getString("tsuPaNeMatriculaEs")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ES_NIVEL_EDUCATIVO.get(2),integerFormat(indicadorIES?.getString("licenciaturaNeMatriculaEs")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ES_NIVEL_EDUCATIVO.get(3),integerFormat(indicadorIES?.getString("especialidadNeMatriculaEs")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ES_NIVEL_EDUCATIVO.get(4),integerFormat(indicadorIES?.getString("maestriaNeMatriculaEs")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ES_NIVEL_EDUCATIVO.get(5),integerFormat(indicadorIES?.getString("doctoradoNeMatriculaEs")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ES_NIVEL_EDUCATIVO.get(6),integerFormat(indicadorIES?.getString("totalEs")!!))
                    }
                    "ES Modalidad, Hombres y Mujeres" -> {
                        valores.put(EtiquetasModuloTres.IND_10_ES_MODALIDAD_HOMBRES_MUJERES.get(0),indicadorIES?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_10_ES_MODALIDAD_HOMBRES_MUJERES.get(1),integerFormat(indicadorIES?.getString("hombresEs")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ES_MODALIDAD_HOMBRES_MUJERES.get(2),integerFormat(indicadorIES?.getString("mujeresEs")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ES_MODALIDAD_HOMBRES_MUJERES.get(3),integerFormat(indicadorIES?.getString("escolarizadaEs")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ES_MODALIDAD_HOMBRES_MUJERES.get(4),integerFormat(indicadorIES?.getString("noEscolarizadaEs")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ES_MODALIDAD_HOMBRES_MUJERES.get(5),integerFormat(indicadorIES?.getString("mixtaEs")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ES_MODALIDAD_HOMBRES_MUJERES.get(6),integerFormat(indicadorIES?.getString("matriculaTotalEmsEs")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ES_MODALIDAD_HOMBRES_MUJERES.get(7),integerFormat(indicadorIES?.getString("matriculaTotalPonderada")!!))
                    }
                    "Áreas de la CMPE" -> {
                        valores.put(EtiquetasModuloTres.IND_10_AREAS_CMPE.get(0),indicadorIES?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_10_AREAS_CMPE.get(1),integerFormat(indicadorIES?.getString("educacionCmpe")!!))
                        valores.put(EtiquetasModuloTres.IND_10_AREAS_CMPE.get(2),integerFormat(indicadorIES?.getString("artesHumanidadesCmpe")!!))
                        valores.put(EtiquetasModuloTres.IND_10_AREAS_CMPE.get(3),integerFormat(indicadorIES?.getString("cienciasSocialesDerechoCmpe")!!))
                        valores.put(EtiquetasModuloTres.IND_10_AREAS_CMPE.get(4),integerFormat(indicadorIES?.getString("administracionNegociosCmpe")!!))
                        valores.put(EtiquetasModuloTres.IND_10_AREAS_CMPE.get(5),integerFormat(indicadorIES?.getString("cienciasNaturalesMatematicasEstadisticaCmpe")!!))
                        valores.put(EtiquetasModuloTres.IND_10_AREAS_CMPE.get(6),integerFormat(indicadorIES?.getString("ticCmpe")!!))
                        valores.put(EtiquetasModuloTres.IND_10_AREAS_CMPE.get(7),integerFormat(indicadorIES?.getString("ingenieriaManufacturaConstruccionCmpe")!!))
                        valores.put(EtiquetasModuloTres.IND_10_AREAS_CMPE.get(8),integerFormat(indicadorIES?.getString("agronomiaVeterinariaCmpe")!!))
                        valores.put(EtiquetasModuloTres.IND_10_AREAS_CMPE.get(9),integerFormat(indicadorIES?.getString("cienciasSaludCmpe")!!))
                        valores.put(EtiquetasModuloTres.IND_10_AREAS_CMPE.get(10),integerFormat(indicadorIES?.getString("serviciosCmpe")!!))
                        valores.put(EtiquetasModuloTres.IND_10_AREAS_CMPE.get(11),integerFormat(indicadorIES?.getString("totalMatriculaEs")!!))
                        valores.put("Referencia","E: Educación" + System.getProperty("line.separator") +
                                "AH: Artes y Humanidades" + System.getProperty("line.separator") +
                                "CSD: Ciencias Sociales y Derecho" + System.getProperty("line.separator") +
                                "AN: Administración y Negocios" + System.getProperty("line.separator") +
                                "CNME: Ciencias Naturales, Matemáticas y Estadística." + System.getProperty("line.separator") +
                                "TIC: Tecnologías de la Información y la Comunicación" + System.getProperty("line.separator") +
                                "IMC: Ingeniería, Manufactura y Construcción" + System.getProperty("line.separator") +
                                "AV: Agronomía y Veterinaria" + System.getProperty("line.separator") +
                                "CS: Ciencias de la Salud" + System.getProperty("line.separator") +
                                "S: Servicios")
                    }
                    "Oferta Educativa (activos) CMPE" -> {
                        valores.put(EtiquetasModuloTres.IND_10_OFERTA_EDUCATIVA_CMPE.get(0),indicadorIES?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_10_OFERTA_EDUCATIVA_CMPE.get(1),integerFormat(indicadorIES?.getString("educacionOfertaCmpe")!!))
                        valores.put(EtiquetasModuloTres.IND_10_OFERTA_EDUCATIVA_CMPE.get(2),integerFormat(indicadorIES?.getString("artesHumanidadesOfertaCmpe")!!))
                        valores.put(EtiquetasModuloTres.IND_10_OFERTA_EDUCATIVA_CMPE.get(3),integerFormat(indicadorIES?.getString("cienciasSocialesDerechoOfertaCmpe")!!))
                        valores.put(EtiquetasModuloTres.IND_10_OFERTA_EDUCATIVA_CMPE.get(4),integerFormat(indicadorIES?.getString("administracionNegociosOfertaCmpe")!!))
                        valores.put(EtiquetasModuloTres.IND_10_OFERTA_EDUCATIVA_CMPE.get(5),integerFormat(indicadorIES?.getString("cienciasNaturalesMatematicasEstadisticaOfertaCmpe")!!))
                        valores.put(EtiquetasModuloTres.IND_10_OFERTA_EDUCATIVA_CMPE.get(6),integerFormat(indicadorIES?.getString("ticOfertaCmpe")!!))
                        valores.put(EtiquetasModuloTres.IND_10_OFERTA_EDUCATIVA_CMPE.get(7),integerFormat(indicadorIES?.getString("ingenieriaManufacturaConstruccionOfertaCmpe")!!))
                        valores.put(EtiquetasModuloTres.IND_10_OFERTA_EDUCATIVA_CMPE.get(8),integerFormat(indicadorIES?.getString("agronomiaVeterinariaOfertaCmpe")!!))
                        valores.put(EtiquetasModuloTres.IND_10_OFERTA_EDUCATIVA_CMPE.get(9),integerFormat(indicadorIES?.getString("cienciasSaludOfertaCmpe")!!))
                        valores.put(EtiquetasModuloTres.IND_10_OFERTA_EDUCATIVA_CMPE.get(10),integerFormat(indicadorIES?.getString("serviciosOfertaCmpe")!!))
                        valores.put(EtiquetasModuloTres.IND_10_OFERTA_EDUCATIVA_CMPE.get(11),integerFormat(indicadorIES?.getString("totalOferta")!!))
                        valores.put(EtiquetasModuloTres.IND_10_OFERTA_EDUCATIVA_CMPE.get(12),integerFormat(indicadorIES?.getString("numeroProgramasEducativos")!!))
                        valores.put("Referencia","E: Educación" + System.getProperty("line.separator") +
                                "AH: Artes y Humanidades" + System.getProperty("line.separator") +
                                "CSD: Ciencias Sociales y Derecho" + System.getProperty("line.separator") +
                                "AN: Administración y Negocios" + System.getProperty("line.separator") +
                                "CNME: Ciencias Naturales, Matemáticas y Estadística." + System.getProperty("line.separator") +
                                "TIC: Tecnologías de la Información y la Comunicación" + System.getProperty("line.separator") +
                                "IMC: Ingeniería, Manufactura y Construcción" + System.getProperty("line.separator") +
                                "AV: Agronomía y Veterinaria" + System.getProperty("line.separator") +
                                "CS: Ciencias de la Salud" + System.getProperty("line.separator") +
                                "S: Servicios")
                    }
                    "Estudiantes TSU y Lic" -> {
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_TSU_LIC.get(0),indicadorIES?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_TSU_LIC.get(1),integerFormat(indicadorIES?.getString("matriculaTsu")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_TSU_LIC.get(2),integerFormat(indicadorIES?.getString("matriculaLicenciatura")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_TSU_LIC.get(3),integerFormat(indicadorIES?.getString("totalMatriculaTsuLic")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_TSU_LIC.get(4),integerFormat(indicadorIES?.getString("hombresDesgloseTsuLic")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_TSU_LIC.get(5),integerFormat(indicadorIES?.getString("mujeresDesgloseTsuLic")!!))
                    }
                    "Estudiantes Posgrado" -> {
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_POSGRADO.get(0),indicadorIES?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_POSGRADO.get(1),integerFormat(indicadorIES?.getString("matriculaEspecialidad")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_POSGRADO.get(2),integerFormat(indicadorIES?.getString("matriculaMaestria")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_POSGRADO.get(3),integerFormat(indicadorIES?.getString("matriculaDoctorado")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_POSGRADO.get(4),integerFormat(indicadorIES?.getString("totalMatriculaPosgrado")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_POSGRADO.get(5),integerFormat(indicadorIES?.getString("hombresPosgrado")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_POSGRADO.get(6),integerFormat(indicadorIES?.getString("mujeresPosgrado")!!))
                    }
                    "Estudiantes Discapacidad TSU, LIC, POSG" -> {
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_DISCAPACIDAD_TSU_LIC_POSG.get(0),indicadorIES?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_DISCAPACIDAD_TSU_LIC_POSG.get(1),integerFormat(indicadorIES?.getString("matriculaDiscapacidadTsuLic")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_DISCAPACIDAD_TSU_LIC_POSG.get(2),integerFormat(indicadorIES?.getString("matriculaDiscapacidadPosg")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_DISCAPACIDAD_TSU_LIC_POSG.get(3),integerFormat(indicadorIES?.getString("totalMatriculaDiscapacidadTsuLicPosg")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_DISCAPACIDAD_TSU_LIC_POSG.get(4),integerFormat(indicadorIES?.getString("hombresDiscapacidad")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_DISCAPACIDAD_TSU_LIC_POSG.get(5),integerFormat(indicadorIES?.getString("mujeresDiscapacidad")!!))
                    }
                    "Estudiantes HLI TSU, LIC, POSG" -> {
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_HLI_TSU_LIC_POSG.get(0),indicadorIES?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_HLI_TSU_LIC_POSG.get(1),integerFormat(indicadorIES?.getString("matriculaHliTsuLic")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_HLI_TSU_LIC_POSG.get(2),integerFormat(indicadorIES?.getString("matriculaHliPosg")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_HLI_TSU_LIC_POSG.get(3),integerFormat(indicadorIES?.getString("totalMatriculaHliTsuLicPosg")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_HLI_TSU_LIC_POSG.get(4),integerFormat(indicadorIES?.getString("hombresHli")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_HLI_TSU_LIC_POSG.get(5),integerFormat(indicadorIES?.getString("mujeresHli")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_HLI_TSU_LIC_POSG.get(6),integerFormat(indicadorIES?.getString("totalHli")!!))
                    }
                    "Estudiantes Titulados" -> {
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_TITULADOS.get(0),indicadorIES?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_TITULADOS.get(1),integerFormat(indicadorIES?.getString("hombresTitulados")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_TITULADOS.get(2),integerFormat(indicadorIES?.getString("mujeresTitulados")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_TITULADOS.get(3),integerFormat(indicadorIES?.getString("totalDiscapacidadTitulados")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_TITULADOS.get(4),integerFormat(indicadorIES?.getString("totalHliTitulados")!!))
                    }
                    "Estudiantes Nuevo Ingreso TSU, LIC, POSG" -> {
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_NUEVO_INGRESO_TSU_LIC_POSG.get(0),indicadorIES?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_NUEVO_INGRESO_TSU_LIC_POSG.get(1),integerFormat(indicadorIES?.getString("alumnosNuevoIngresoTsuLic")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_NUEVO_INGRESO_TSU_LIC_POSG.get(2),integerFormat(indicadorIES?.getString("alumnosNuevoIngresoPosg")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_NUEVO_INGRESO_TSU_LIC_POSG.get(3),integerFormat(indicadorIES?.getString("totalAlumnosNuevoIngresoTsuLicPosg")!!))
                    }
                    "Estudiantes Egresados TSU, LIC, POSG" -> {
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_EGRESADOS_TSU_LIC_POSG.get(0),indicadorIES?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_EGRESADOS_TSU_LIC_POSG.get(1),integerFormat(indicadorIES?.getString("alumnosEgresadosTsuLic")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_EGRESADOS_TSU_LIC_POSG.get(2),integerFormat(indicadorIES?.getString("alumnosEgresadosPosg")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_EGRESADOS_TSU_LIC_POSG.get(3),integerFormat(indicadorIES?.getString("totalAlumnosEgresadosTsuLicPosg")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_EGRESADOS_TSU_LIC_POSG.get(4),integerFormat(indicadorIES?.getString("hombresEgresados")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_EGRESADOS_TSU_LIC_POSG.get(5),integerFormat(indicadorIES?.getString("mujeresEgresados")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_EGRESADOS_TSU_LIC_POSG.get(6),integerFormat(indicadorIES?.getString("totalDiscapacidadEgresados")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_EGRESADOS_TSU_LIC_POSG.get(7),integerFormat(indicadorIES?.getString("totalHliEgresados")!!))
                    }
                    "Oferta Educativa (activos) NE y TP" -> {
                        valores.put(EtiquetasModuloTres.IND_10_OFERTA_EDUCATIVA_NE_TP.get(0),indicadorIES?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_10_OFERTA_EDUCATIVA_NE_TP.get(1),integerFormat(indicadorIES?.getString("ofertaEducativaProgramasTsuLic")!!))
                        valores.put(EtiquetasModuloTres.IND_10_OFERTA_EDUCATIVA_NE_TP.get(2),integerFormat(indicadorIES?.getString("ofertaEducativaProgramasPosgrado")!!))
                        valores.put(EtiquetasModuloTres.IND_10_OFERTA_EDUCATIVA_NE_TP.get(3),integerFormat(indicadorIES?.getString("totalOfertaEducativaProgramasActivosTsuLicPosg")!!))
                        valores.put(EtiquetasModuloTres.IND_10_OFERTA_EDUCATIVA_NE_TP.get(4),integerFormat(indicadorIES?.getString("programasRscolarizadosTsuLicPosg")!!))
                        valores.put(EtiquetasModuloTres.IND_10_OFERTA_EDUCATIVA_NE_TP.get(5),integerFormat(indicadorIES?.getString("programasMixtoTsuLicPosg")!!))
                        valores.put(EtiquetasModuloTres.IND_10_OFERTA_EDUCATIVA_NE_TP.get(6),integerFormat(indicadorIES?.getString("programasNoEscolarizadosTsuLicPosg")!!))
                    }
                    "Plantilla Administrativo" -> {
                        valores.put(EtiquetasModuloTres.IND_10_PLANTILLA_ADMINISTRATIVO.get(0),indicadorIES?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_10_PLANTILLA_ADMINISTRATIVO.get(1),integerFormat(indicadorIES?.getString("plantillaPersonalAdministrativo")!!))
                        valores.put(EtiquetasModuloTres.IND_10_PLANTILLA_ADMINISTRATIVO.get(2),integerFormat(indicadorIES?.getString("plantillaMandosMediosDuperiores")!!))
                        valores.put(EtiquetasModuloTres.IND_10_PLANTILLA_ADMINISTRATIVO.get(3),integerFormat(indicadorIES?.getString("plantillaDocenteInvestigadorInvestigadoresAuxInvestigador")!!))
                        valores.put(EtiquetasModuloTres.IND_10_PLANTILLA_ADMINISTRATIVO.get(4),integerFormat(indicadorIES?.getString("otrosChoferesPersonalLimpiezaServiciosGeneralesEtc")!!))
                    }
                    "Personal Docente" -> {
                        valores.put(EtiquetasModuloTres.IND_10_PERSONAL_DOCENTE.get(0),indicadorIES?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_10_PERSONAL_DOCENTE.get(1),integerFormat(indicadorIES?.getString("personalDocentePtc")!!))
                        valores.put(EtiquetasModuloTres.IND_10_PERSONAL_DOCENTE.get(2),integerFormat(indicadorIES?.getString("personalDocentePtct")!!))
                        valores.put(EtiquetasModuloTres.IND_10_PERSONAL_DOCENTE.get(3),integerFormat(indicadorIES?.getString("personalDocenteMt")!!))
                        valores.put(EtiquetasModuloTres.IND_10_PERSONAL_DOCENTE.get(4),integerFormat(indicadorIES?.getString("personalDocentePh")!!))
                        valores.put(EtiquetasModuloTres.IND_10_PERSONAL_DOCENTE.get(5),integerFormat(indicadorIES?.getString("totalPersonalDocentePtcPtctMtPh")!!))
                    }
                    "Programas Buena Calidad, Evaluables y Competitividad Académica" -> {
                        valores.put(EtiquetasModuloTres.IND_10_PROGRAMAS_BUENA_CALIDAD_EVALUABLES_COMPETITIVIDAD_ACADEMICA.get(0),indicadorIES?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_10_PROGRAMAS_BUENA_CALIDAD_EVALUABLES_COMPETITIVIDAD_ACADEMICA.get(1),integerFormat(indicadorIES?.getString("numeroProgramasBc")!!))
                        valores.put(EtiquetasModuloTres.IND_10_PROGRAMAS_BUENA_CALIDAD_EVALUABLES_COMPETITIVIDAD_ACADEMICA.get(2),integerFormat(indicadorIES?.getString("matriculaProgramasBc")!!))
                        valores.put(EtiquetasModuloTres.IND_10_PROGRAMAS_BUENA_CALIDAD_EVALUABLES_COMPETITIVIDAD_ACADEMICA.get(3),integerFormat(indicadorIES?.getString("numeroProgramasEva")!!))
                        valores.put(EtiquetasModuloTres.IND_10_PROGRAMAS_BUENA_CALIDAD_EVALUABLES_COMPETITIVIDAD_ACADEMICA.get(4),integerFormat(indicadorIES?.getString("numeroProgramasNoEva")!!))
                        valores.put(EtiquetasModuloTres.IND_10_PROGRAMAS_BUENA_CALIDAD_EVALUABLES_COMPETITIVIDAD_ACADEMICA.get(5),integerFormat(indicadorIES?.getString("matriculaProgramasEva")!!))
                        valores.put(EtiquetasModuloTres.IND_10_PROGRAMAS_BUENA_CALIDAD_EVALUABLES_COMPETITIVIDAD_ACADEMICA.get(6),integerFormat(indicadorIES?.getString("matriculaProgramasNoEva")!!))
                    }
                    "% Programas" -> {
                        addChartsModuloIII(listOf(0,1),charts)
                        valores.put(EtiquetasModuloTres.IND_10_PORCENTAJE_PROGRAMAS.get(0),indicadorIES?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_10_PORCENTAJE_PROGRAMAS.get(1),porcentajeFormat(indicadorIES?.getString("porcentajeProgramasTsuLicEvaBc")!!))
                        valores.put(EtiquetasModuloTres.IND_10_PORCENTAJE_PROGRAMAS.get(2),porcentajeFormat(indicadorIES?.getString("porcentajeMatriculaAtendidaProgamasTsuLicenciaturaEvaBc")!!))
                        valores.put(EtiquetasModuloTres.IND_10_PORCENTAJE_PROGRAMAS.get(3),porcentajeFormat(indicadorIES?.getString("porcentajeCompetitividadAcademica")!!))
                        valores.put(EtiquetasModuloTres.IND_10_PORCENTAJE_PROGRAMAS.get(4),porcentajeFormat(indicadorIES?.getString("competitividadCapacidadAcademica")!!))
                    }
                    "Número Programas TSU, LIC, POSG" -> {
                        valores.put(EtiquetasModuloTres.IND_10_NUMERO_PROGRAMAS_TSU_LIC_POSG.get(0),indicadorIES?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_10_NUMERO_PROGRAMAS_TSU_LIC_POSG.get(1),integerFormat(indicadorIES?.getString("tsuPa")!!))
                        valores.put(EtiquetasModuloTres.IND_10_NUMERO_PROGRAMAS_TSU_LIC_POSG.get(2),integerFormat(indicadorIES?.getString("licenciatura")!!))
                        valores.put(EtiquetasModuloTres.IND_10_NUMERO_PROGRAMAS_TSU_LIC_POSG.get(3),integerFormat(indicadorIES?.getString("especialidad")!!))
                        valores.put(EtiquetasModuloTres.IND_10_NUMERO_PROGRAMAS_TSU_LIC_POSG.get(4),integerFormat(indicadorIES?.getString("maestria")!!))
                        valores.put(EtiquetasModuloTres.IND_10_NUMERO_PROGRAMAS_TSU_LIC_POSG.get(5),integerFormat(indicadorIES?.getString("doctorado")!!))
                    }
                    "% Número Programas" -> {
                        addChartsModuloIII(listOf(2,3,4),charts)
                        valores.put(EtiquetasModuloTres.IND_10_PORCENTAJE_NUMERO_PROGRAMAS.get(0),indicadorIES?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_10_PORCENTAJE_NUMERO_PROGRAMAS.get(1),porcentajeFormat(indicadorIES?.getString("porcentajeCac")!!))
                        valores.put(EtiquetasModuloTres.IND_10_PORCENTAJE_NUMERO_PROGRAMAS.get(2),porcentajeFormat(indicadorIES?.getString("porcentajePerfilDeseable")!!))
                        valores.put(EtiquetasModuloTres.IND_10_PORCENTAJE_NUMERO_PROGRAMAS.get(3),porcentajeFormat(indicadorIES?.getString("porcentajeSni")!!))
                        valores.put(EtiquetasModuloTres.IND_10_PORCENTAJE_NUMERO_PROGRAMAS.get(4),integerFormat(indicadorIES?.getString("capacidadAcademica")!!))
                    }
                    "Estudiantes Nivel Superior Modalidad Escolar" -> {
                        addChartsModuloIII(listOf(5),charts)
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_NIVEL_SUPERIOR_MODALIDAD_ESCOLAR.get(0),indicadorIES?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_NIVEL_SUPERIOR_MODALIDAD_ESCOLAR.get(1),integerFormat(indicadorIES?.getString("tecnicoSuperiorANSModalidadEscolar")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_NIVEL_SUPERIOR_MODALIDAD_ESCOLAR.get(2),integerFormat(indicadorIES?.getString("licenciaProfesionalANSModalidadEscolar")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_NIVEL_SUPERIOR_MODALIDAD_ESCOLAR.get(3),integerFormat(indicadorIES?.getString("licenciaturaANSModalidadEscolar")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_NIVEL_SUPERIOR_MODALIDAD_ESCOLAR.get(4),integerFormat(indicadorIES?.getString("posgradoANSModalidadEscolar")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_NIVEL_SUPERIOR_MODALIDAD_ESCOLAR.get(5),integerFormat(indicadorIES?.getString("totalEscolarANSModalidadEscolar")!!))
                    }
                    "Estudiantes Nivel Superior Modalidad no Escolarizada" -> {
                        addChartsModuloIII(listOf(6),charts)
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_NIVEL_SUPERIOR_MODALIDAD_NO_ESCOLARIZADA.get(0),indicadorIES?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_NIVEL_SUPERIOR_MODALIDAD_NO_ESCOLARIZADA.get(1),integerFormat(indicadorIES?.getString("tecnicoSuperiorANSModalidadNoEscolarizada")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_NIVEL_SUPERIOR_MODALIDAD_NO_ESCOLARIZADA.get(2),integerFormat(indicadorIES?.getString("licenciaProfesionalANSModalidadNoEscolarizada")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_NIVEL_SUPERIOR_MODALIDAD_NO_ESCOLARIZADA.get(3),integerFormat(indicadorIES?.getString("licenciaturaANSModalidadNoEscolarizada")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_NIVEL_SUPERIOR_MODALIDAD_NO_ESCOLARIZADA.get(4),integerFormat(indicadorIES?.getString("posgradoANSModalidadNoEscolarizada")!!))
                        valores.put(EtiquetasModuloTres.IND_10_ALUMNOS_NIVEL_SUPERIOR_MODALIDAD_NO_ESCOLARIZADA.get(5),integerFormat(indicadorIES?.getString("totalNoEscolarizadaANSModalidadNoEscolarizada")!!))
                    }
                    "Personal de Facultades, Escuelas, Centros, Divisiones o Departamentos" -> {
                        addChartsModuloIII(listOf(7),charts)
                        valores.put(EtiquetasModuloTres.IND_10_PERSONAS_FACULTADES_ESCUELAS_CENTROS_DIVISIONES_DEPARTAMENTOS.get(0),indicadorIES?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_10_PERSONAS_FACULTADES_ESCUELAS_CENTROS_DIVISIONES_DEPARTAMENTOS.get(1),integerFormat(indicadorIES?.getString("directivoPF")!!))
                        valores.put(EtiquetasModuloTres.IND_10_PERSONAS_FACULTADES_ESCUELAS_CENTROS_DIVISIONES_DEPARTAMENTOS.get(2),integerFormat(indicadorIES?.getString("docenteNoIncluyaPiPF")!!))
                        valores.put(EtiquetasModuloTres.IND_10_PERSONAS_FACULTADES_ESCUELAS_CENTROS_DIVISIONES_DEPARTAMENTOS.get(3),integerFormat(indicadorIES?.getString("docenteInvestigadorDocenteAuxiliarInvestigadorPF")!!))
                        valores.put(EtiquetasModuloTres.IND_10_PERSONAS_FACULTADES_ESCUELAS_CENTROS_DIVISIONES_DEPARTAMENTOS.get(4),integerFormat(indicadorIES?.getString("investigadorPF")!!))
                        valores.put(EtiquetasModuloTres.IND_10_PERSONAS_FACULTADES_ESCUELAS_CENTROS_DIVISIONES_DEPARTAMENTOS.get(5),integerFormat(indicadorIES?.getString("auxiliarInvestigadorNoIncluyeSSPPPF")!!))
                        valores.put(EtiquetasModuloTres.IND_10_PERSONAS_FACULTADES_ESCUELAS_CENTROS_DIVISIONES_DEPARTAMENTOS.get(6),integerFormat(indicadorIES?.getString("administrativoPF")!!))
                        valores.put(EtiquetasModuloTres.IND_10_PERSONAS_FACULTADES_ESCUELAS_CENTROS_DIVISIONES_DEPARTAMENTOS.get(7),integerFormat(indicadorIES?.getString("otrosPF")!!))
                        valores.put(EtiquetasModuloTres.IND_10_PERSONAS_FACULTADES_ESCUELAS_CENTROS_DIVISIONES_DEPARTAMENTOS.get(8),integerFormat(indicadorIES?.getString("totalPersonalFacultadesPF")!!))
                        valores.put(EtiquetasModuloTres.IND_10_PERSONAS_FACULTADES_ESCUELAS_CENTROS_DIVISIONES_DEPARTAMENTOS.get(9),integerFormat(indicadorIES?.getString("directivo2PF")!!))
                    }
                    "Personal en Áreas Centrales" -> {
                        addChartsModuloIII(listOf(8),charts)
                        valores.put(EtiquetasModuloTres.IND_10_PERSONAL_AREAS_CENTRALES.get(0),indicadorIES?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_10_PERSONAL_AREAS_CENTRALES.get(1),integerFormat(indicadorIES?.getString("directivoPAC")!!))
                        valores.put(EtiquetasModuloTres.IND_10_PERSONAL_AREAS_CENTRALES.get(2),integerFormat(indicadorIES?.getString("auxiliarInvestigadorNoIncluyeSSPPPAC")!!))
                        valores.put(EtiquetasModuloTres.IND_10_PERSONAL_AREAS_CENTRALES.get(3),integerFormat(indicadorIES?.getString("administrativoPAC")!!))
                        valores.put(EtiquetasModuloTres.IND_10_PERSONAL_AREAS_CENTRALES.get(4),integerFormat(indicadorIES?.getString("otrosPAC")!!))
                        valores.put(EtiquetasModuloTres.IND_10_PERSONAL_AREAS_CENTRALES.get(5),integerFormat(indicadorIES?.getString("totalPersonalAreasCentralesPAC")!!))
                    }
                    "Total Personal Institución" -> {
                        addChartsModuloIII(listOf(9),charts)
                        valores.put(EtiquetasModuloTres.IND_10_TOTAL_PERSONAL_INSTITUCION.get(0),indicadorIES?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_10_TOTAL_PERSONAL_INSTITUCION.get(1),integerFormat(indicadorIES?.getString("totalPersonalInstitucion")!!))
                    }
                    "Total de Personal Docente Escolarizado" -> {
                        addChartsModuloIII(listOf(10),charts)
                        valores.put(EtiquetasModuloTres.IND_10_TOTAL_PERSONAL_DOCENTE_ESCOLARIZADO.get(0),indicadorIES?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_10_TOTAL_PERSONAL_DOCENTE_ESCOLARIZADO.get(1),integerFormat(indicadorIES?.getString("personalTiempoCompletoTPDE")!!))
                        valores.put(EtiquetasModuloTres.IND_10_TOTAL_PERSONAL_DOCENTE_ESCOLARIZADO.get(2),integerFormat(indicadorIES?.getString("personalTresCuartosTiempoTPDE")!!))
                        valores.put(EtiquetasModuloTres.IND_10_TOTAL_PERSONAL_DOCENTE_ESCOLARIZADO.get(3),integerFormat(indicadorIES?.getString("personalMedioTiempoTPDE")!!))
                        valores.put(EtiquetasModuloTres.IND_10_TOTAL_PERSONAL_DOCENTE_ESCOLARIZADO.get(4),integerFormat(indicadorIES?.getString("personalHoraOAsignaturaTPDE")!!))
                        valores.put(EtiquetasModuloTres.IND_10_TOTAL_PERSONAL_DOCENTE_ESCOLARIZADO.get(5),integerFormat(indicadorIES?.getString("totalTPDE")!!))
                    }
                    "Total de Personal Docente no Escolarizado" -> {
                        addChartsModuloIII(listOf(11),charts)
                        valores.put(EtiquetasModuloTres.IND_10_TOTAL_PERSONAL_DOCENTE_NO_ESCOLARIZADO.get(0),indicadorIES?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_10_TOTAL_PERSONAL_DOCENTE_NO_ESCOLARIZADO.get(1),integerFormat(indicadorIES?.getString("personalTiempoCompletoTPDNE")!!))
                        valores.put(EtiquetasModuloTres.IND_10_TOTAL_PERSONAL_DOCENTE_NO_ESCOLARIZADO.get(2),integerFormat(indicadorIES?.getString("personalTresCuartosTiempoTPDNE")!!))
                        valores.put(EtiquetasModuloTres.IND_10_TOTAL_PERSONAL_DOCENTE_NO_ESCOLARIZADO.get(3),integerFormat(indicadorIES?.getString("personalMedioTiempoTPDNE")!!))
                        valores.put(EtiquetasModuloTres.IND_10_TOTAL_PERSONAL_DOCENTE_NO_ESCOLARIZADO.get(4),integerFormat(indicadorIES?.getString("personalHoraOAsignaturaTPDNE")!!))
                        valores.put(EtiquetasModuloTres.IND_10_TOTAL_PERSONAL_DOCENTE_NO_ESCOLARIZADO.get(5),integerFormat(indicadorIES?.getString("totalTPDNE")!!))
                    }
                    "Carrera, Programas y Estudiantes de la Modalidad Escolarizada" -> {
                        addChartsModuloIII(listOf(12),charts)
                        valores.put(EtiquetasModuloTres.IND_10_CARRERA_PROGRAMAS_ALUMNOS_MODALIDAD_ESCOLARIZADA.get(0),indicadorIES?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_10_CARRERA_PROGRAMAS_ALUMNOS_MODALIDAD_ESCOLARIZADA.get(1),integerFormat(indicadorIES?.getString("tecnicoSuperiorCarreraPYAME")!!))
                        valores.put(EtiquetasModuloTres.IND_10_CARRERA_PROGRAMAS_ALUMNOS_MODALIDAD_ESCOLARIZADA.get(2),integerFormat(indicadorIES?.getString("licenciaturaProfesionalCarreraPYAME")!!))
                        valores.put(EtiquetasModuloTres.IND_10_CARRERA_PROGRAMAS_ALUMNOS_MODALIDAD_ESCOLARIZADA.get(3),integerFormat(indicadorIES?.getString("licenciaturaCarreraPYAME")!!))
                        valores.put(EtiquetasModuloTres.IND_10_CARRERA_PROGRAMAS_ALUMNOS_MODALIDAD_ESCOLARIZADA.get(4),integerFormat(indicadorIES?.getString("especialidadCarreraPYAME")!!))
                        valores.put(EtiquetasModuloTres.IND_10_CARRERA_PROGRAMAS_ALUMNOS_MODALIDAD_ESCOLARIZADA.get(5),integerFormat(indicadorIES?.getString("maestriaCarreraPYAME")!!))
                        valores.put(EtiquetasModuloTres.IND_10_CARRERA_PROGRAMAS_ALUMNOS_MODALIDAD_ESCOLARIZADA.get(6),integerFormat(indicadorIES?.getString("doctoradoCarreraPYAME")!!))
                        valores.put(EtiquetasModuloTres.IND_10_CARRERA_PROGRAMAS_ALUMNOS_MODALIDAD_ESCOLARIZADA.get(7),integerFormat(indicadorIES?.getString("totalCarreraPYAME")!!))
                    }
                    "Carrera, Programas, y Estudiantes de la Modalidad no Escolarizada" -> {
                        addChartsModuloIII(listOf(13),charts)
                        valores.put(EtiquetasModuloTres.IND_10_CARRERA_PROGRAMAS_ALUMNOS_MODALIDAD_NO_ESCOLARIZADA.get(0),indicadorIES?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_10_CARRERA_PROGRAMAS_ALUMNOS_MODALIDAD_NO_ESCOLARIZADA.get(1),integerFormat(indicadorIES?.getString("tecnicoSuperiorCarreraPYAMNE")!!))
                        valores.put(EtiquetasModuloTres.IND_10_CARRERA_PROGRAMAS_ALUMNOS_MODALIDAD_NO_ESCOLARIZADA.get(2),integerFormat(indicadorIES?.getString("licenciaturaProfesionalCarreraPYAMNE")!!))
                        valores.put(EtiquetasModuloTres.IND_10_CARRERA_PROGRAMAS_ALUMNOS_MODALIDAD_NO_ESCOLARIZADA.get(3),integerFormat(indicadorIES?.getString("licenciaturaCarreraPYAMNE")!!))
                        valores.put(EtiquetasModuloTres.IND_10_CARRERA_PROGRAMAS_ALUMNOS_MODALIDAD_NO_ESCOLARIZADA.get(4),integerFormat(indicadorIES?.getString("especialidadCarreraPYAMNE")!!))
                        valores.put(EtiquetasModuloTres.IND_10_CARRERA_PROGRAMAS_ALUMNOS_MODALIDAD_NO_ESCOLARIZADA.get(5),integerFormat(indicadorIES?.getString("maestriaCarreraPYAMNE")!!))
                        valores.put(EtiquetasModuloTres.IND_10_CARRERA_PROGRAMAS_ALUMNOS_MODALIDAD_NO_ESCOLARIZADA.get(6),integerFormat(indicadorIES?.getString("doctoradoCarreraPYAMNE")!!))
                        valores.put(EtiquetasModuloTres.IND_10_CARRERA_PROGRAMAS_ALUMNOS_MODALIDAD_NO_ESCOLARIZADA.get(7),integerFormat(indicadorIES?.getString("totalCarreraPYAMNE")!!))
                    }
                    "Desglose Profesores de Tiempo Completo (PTC)" -> {
                        valores.put(EtiquetasModuloTres.IND_10_DESGLOSE_PROFESORES_TIEMPO_COMPLETO.get(0),indicadorIES?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_10_DESGLOSE_PROFESORES_TIEMPO_COMPLETO.get(1),integerFormat(sisup?.getString("licenciaturaPTC")!!))
                        valores.put(EtiquetasModuloTres.IND_10_DESGLOSE_PROFESORES_TIEMPO_COMPLETO.get(2),integerFormat(sisup?.getString("maestriaPTC")!!))
                        valores.put(EtiquetasModuloTres.IND_10_DESGLOSE_PROFESORES_TIEMPO_COMPLETO.get(3),integerFormat(sisup?.getString("doctoradoPTC")!!))
                        valores.put(EtiquetasModuloTres.IND_10_DESGLOSE_PROFESORES_TIEMPO_COMPLETO.get(4),integerFormat(sisup?.getString("posgradoPTC")!!))
                        valores.put(EtiquetasModuloTres.IND_10_DESGLOSE_PROFESORES_TIEMPO_COMPLETO.get(5),integerFormat(sisup?.getString("totalPTC")!!))
                    }
                    "Desglose Profesores Perfil Deseable vigente y SNI" -> {
                        valores.put(EtiquetasModuloTres.IND_10_DESGLOSE_PROFESORES_PERFIL_DESEABLE_VIGENTE_SNI.get(0),indicadorIES?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_10_DESGLOSE_PROFESORES_PERFIL_DESEABLE_VIGENTE_SNI.get(1),integerFormat(sisup?.getString("perfilDeseableVigente")!!))
                        valores.put(EtiquetasModuloTres.IND_10_DESGLOSE_PROFESORES_PERFIL_DESEABLE_VIGENTE_SNI.get(2),integerFormat(sisup?.getString("snic")!!))
                        valores.put(EtiquetasModuloTres.IND_10_DESGLOSE_PROFESORES_PERFIL_DESEABLE_VIGENTE_SNI.get(3),integerFormat(sisup?.getString("snic1")!!))
                        valores.put(EtiquetasModuloTres.IND_10_DESGLOSE_PROFESORES_PERFIL_DESEABLE_VIGENTE_SNI.get(4),integerFormat(sisup?.getString("snic2")!!))
                        valores.put(EtiquetasModuloTres.IND_10_DESGLOSE_PROFESORES_PERFIL_DESEABLE_VIGENTE_SNI.get(5),integerFormat(sisup?.getString("snic3")!!))
                        valores.put(EtiquetasModuloTres.IND_10_DESGLOSE_PROFESORES_PERFIL_DESEABLE_VIGENTE_SNI.get(6),integerFormat(sisup?.getString("totalSNIVigente")!!))
                    }
                    "Cuerpos Académicos (CA)" -> {
                        valores.put(EtiquetasModuloTres.IND_10_CUERPOS_ACADEMICOS.get(0),indicadorIES?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_10_CUERPOS_ACADEMICOS.get(1),integerFormat(sisup?.getString("totalCAEF")!!))
                        valores.put(EtiquetasModuloTres.IND_10_CUERPOS_ACADEMICOS.get(2),integerFormat(sisup?.getString("totalCAEC")!!))
                        valores.put(EtiquetasModuloTres.IND_10_CUERPOS_ACADEMICOS.get(3),integerFormat(sisup?.getString("totalCAC")!!))
                        valores.put(EtiquetasModuloTres.IND_10_CUERPOS_ACADEMICOS.get(4),integerFormat(sisup?.getString("totalCA")!!))
                    }
                    "Desglose CAEF" -> {
                        valores.put(EtiquetasModuloTres.IND_10_DESGLOSE_CAEF.get(0),indicadorIES?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_10_DESGLOSE_CAEF.get(1),integerFormat(sisup?.getString("areaCSAgropecuariasCAEF")!!))
                        valores.put(EtiquetasModuloTres.IND_10_DESGLOSE_CAEF.get(2),integerFormat(sisup?.getString("areaCSSaludCAEF")!!))
                        valores.put(EtiquetasModuloTres.IND_10_DESGLOSE_CAEF.get(3),integerFormat(sisup?.getString("areaCSNaturalesExactasCAEF")!!))
                        valores.put(EtiquetasModuloTres.IND_10_DESGLOSE_CAEF.get(4),integerFormat(sisup?.getString("areaCSSocialesAdministrativasCAEF")!!))
                        valores.put(EtiquetasModuloTres.IND_10_DESGLOSE_CAEF.get(5),integerFormat(sisup?.getString("areaCSIngenieriaTecnologiaCAEF")!!))
                        valores.put(EtiquetasModuloTres.IND_10_DESGLOSE_CAEF.get(6),integerFormat(sisup?.getString("areaEducacionHumanidadesArtesCAEF")!!))
                    }
                    "Desglose CAEC" -> {
                        valores.put(EtiquetasModuloTres.IND_10_DESGLOSE_CAEC.get(0),indicadorIES?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_10_DESGLOSE_CAEC.get(1),integerFormat(sisup?.getString("areaCSAgropecuariasCAEC")!!))
                        valores.put(EtiquetasModuloTres.IND_10_DESGLOSE_CAEC.get(2),integerFormat(sisup?.getString("areaCSSaludCAEC")!!))
                        valores.put(EtiquetasModuloTres.IND_10_DESGLOSE_CAEC.get(3),integerFormat(sisup?.getString("areaCSNaturalesExactasCAEC")!!))
                        valores.put(EtiquetasModuloTres.IND_10_DESGLOSE_CAEC.get(4),integerFormat(sisup?.getString("areaCSSocialesAdministrativasCAEC")!!))
                        valores.put(EtiquetasModuloTres.IND_10_DESGLOSE_CAEC.get(5),integerFormat(sisup?.getString("areaCSIngenieriaTecnologiaCAEC")!!))
                        valores.put(EtiquetasModuloTres.IND_10_DESGLOSE_CAEC.get(6),integerFormat(sisup?.getString("areaEducacionHumanidadesArtesCAEC")!!))
                    }
                    "Desglose CAC" -> {
                        valores.put(EtiquetasModuloTres.IND_10_DESGLOSE_CAC.get(0),indicadorIES?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_10_DESGLOSE_CAC.get(1),integerFormat(sisup?.getString("areaCSAgropecuariasCAC")!!))
                        valores.put(EtiquetasModuloTres.IND_10_DESGLOSE_CAC.get(2),integerFormat(sisup?.getString("areaCSSaludCAC")!!))
                        valores.put(EtiquetasModuloTres.IND_10_DESGLOSE_CAC.get(3),integerFormat(sisup?.getString("areaCSNaturalesExactasCAC")!!))
                        valores.put(EtiquetasModuloTres.IND_10_DESGLOSE_CAC.get(4),integerFormat(sisup?.getString("areaCSSocialesAdministrativasCAC")!!))
                        valores.put(EtiquetasModuloTres.IND_10_DESGLOSE_CAC.get(5),integerFormat(sisup?.getString("areaCSIngenieriaTecnologiaCAC")!!))
                        valores.put(EtiquetasModuloTres.IND_10_DESGLOSE_CAC.get(6),integerFormat(sisup?.getString("areaEducacionHumanidadesArtesCAC")!!))
                    }
                    "Desglose CA" -> {
                        valores.put(EtiquetasModuloTres.IND_10_DESGLOSE_CA.get(0),indicadorIES?.getString("cicloDatos"))
                        valores.put(EtiquetasModuloTres.IND_10_DESGLOSE_CA.get(1),integerFormat(sisup?.getString("areaCSAgropecuariasCA")!!))
                        valores.put(EtiquetasModuloTres.IND_10_DESGLOSE_CA.get(2),integerFormat(sisup?.getString("areaCSSaludCA")!!))
                        valores.put(EtiquetasModuloTres.IND_10_DESGLOSE_CA.get(3),integerFormat(sisup?.getString("areaCSNaturalesExactasCA")!!))
                        valores.put(EtiquetasModuloTres.IND_10_DESGLOSE_CA.get(4),integerFormat(sisup?.getString("areaCSSocialesAdministrativasCA")!!))
                        valores.put(EtiquetasModuloTres.IND_10_DESGLOSE_CA.get(5),integerFormat(sisup?.getString("areaCSIngenieriaTecnologiaCA")!!))
                        valores.put(EtiquetasModuloTres.IND_10_DESGLOSE_CA.get(6),integerFormat(sisup?.getString("areaEducacionHumanidadesArtesCA")!!))
                    }
                }
            }
        }
        return valores
    }

    private fun valoresDeIndicadorModuloDos():MutableMap<String,String>{
        val valores : MutableMap<String,String> = HashMap()
        when (indicadorOrigen!!.indicador().numIdentificacion()){
            1 -> {
                val jsonResponse = JSONArray(arguments?.getString("response"))
                val jsonValue = arguments?.getInt("indexOfElement")?.let { jsonResponse.getJSONObject(it) }
                when (titulo){
                    "Total" -> {
                        valores.put(EtiquetasModuloDos.IND_1_TOTAL.get(0),integerFormat(jsonValue?.getString("matriculaTotal")!!))
                        valores.put(EtiquetasModuloDos.IND_1_TOTAL.get(1),integerFormat(jsonValue.getString("censoTotal")))
                        valores.put(EtiquetasModuloDos.IND_1_TOTAL.get(2),porcentajeFormat(jsonValue.getString("tasaEscolarizacion")))
                    }
                    "Mujeres" -> {
                        valores.put(EtiquetasModuloDos.IND_1_TOTAL.get(0),integerFormat(jsonValue?.getString("matriculaMuj")!!))
                        valores.put(EtiquetasModuloDos.IND_1_TOTAL.get(1),integerFormat(jsonValue?.getString("censoMuj")!!))
                        valores.put(EtiquetasModuloDos.IND_1_TOTAL.get(2),porcentajeFormat(jsonValue?.getString("tasaMujeres")!!))
                    }
                    "Hombres" -> {
                        valores.put(EtiquetasModuloDos.IND_1_TOTAL.get(0),integerFormat(jsonValue?.getString("matriculaHom")!!))
                        valores.put(EtiquetasModuloDos.IND_1_TOTAL.get(1),integerFormat(jsonValue?.getString("censoHom")!!))
                        valores.put(EtiquetasModuloDos.IND_1_TOTAL.get(2),porcentajeFormat(jsonValue?.getString("tasaHombres")!!))
                    }
                }
            }
            2 -> {
                val jsonResponse = JSONArray(arguments?.getString("response"))
                val jsonValue = arguments?.getInt("indexOfElement")?.let { jsonResponse.getJSONObject(it) }
                when (titulo){
                    "Total" -> {
                        valores.put(EtiquetasModuloDos.IND_2_TOTAL.get(0),porcentajeFormat(jsonValue?.getString("contribucion")!!))
                    }
                    "Mujeres" -> {
                        valores.put(EtiquetasModuloDos.IND_2_MUJERES.get(0),porcentajeFormat(jsonValue?.getString("contribucionMujeres")!!))
                    }
                    "Hombres" -> {
                        valores.put(EtiquetasModuloDos.IND_2_HOMBRES.get(0),porcentajeFormat(jsonValue?.getString("contribucionHombres")!!))
                    }
                }
            }
            3 -> {
                val jsonResponse = JSONArray(arguments?.getString("response"))
                val jsonValue = arguments?.getInt("indexOfElement")?.let { jsonResponse.getJSONObject(it) }
                when (titulo){
                    "Tasa Bruta Escolarización DGESUI" -> {
                        valores.put(EtiquetasModuloDos.IND_3_TASA_BRUTA_ESCOLARIZACION_DGESUI.get(0),integerFormat(jsonValue?.getString("totalTSUPA")!!))
                        valores.put(EtiquetasModuloDos.IND_3_TASA_BRUTA_ESCOLARIZACION_DGESUI.get(1),integerFormat(jsonValue?.getString("totalLicenciatura")!!))
                        valores.put(EtiquetasModuloDos.IND_3_TASA_BRUTA_ESCOLARIZACION_DGESUI.get(2),integerFormat(jsonValue?.getString("matriculaTotal")!!))
                        valores.put(EtiquetasModuloDos.IND_3_TASA_BRUTA_ESCOLARIZACION_DGESUI.get(3),porcentajeFormat(jsonValue?.getString("contribucionDGESUI")!!))
                    }
                    "Tasa Bruta Escolarización Mujeres" -> {
                        valores.put(EtiquetasModuloDos.IND_3_TASA_BRUTA_ESCOLARIZACION_MUJERES.get(0),integerFormat(jsonValue?.getString("totalMujeres")!!))
                        valores.put(EtiquetasModuloDos.IND_3_TASA_BRUTA_ESCOLARIZACION_MUJERES.get(1),porcentajeFormat(jsonValue?.getString("contribucionDGESUIMujeres")!!))
                    }
                    "Tasa Bruta Escolarización Hombres" -> {
                        valores.put(EtiquetasModuloDos.IND_3_TASA_BRUTA_ESCOLARIZACION_HOMBRES.get(0),integerFormat(jsonValue?.getString("totalHombres")!!))
                        valores.put(EtiquetasModuloDos.IND_3_TASA_BRUTA_ESCOLARIZACION_HOMBRES.get(1),porcentajeFormat(jsonValue?.getString("contribucionDGESUIHombres")!!))
                    }
                    "Tasa Bruta Escolarización Con Discapacidad" -> {
                        valores.put(EtiquetasModuloDos.IND_3_TASA_BRUTA_ESCOLARIZACION_DISCAPACIDAD.get(0),integerFormat(jsonValue?.getString("totalDiscapacitados")!!))
                        valores.put(EtiquetasModuloDos.IND_3_TASA_BRUTA_ESCOLARIZACION_DISCAPACIDAD.get(1),porcentajeFormat(jsonValue?.getString("porcentajeConDiscapacidad")!!))
                    }
                    "Tasa Bruta Escolarización HLI" -> {
                        valores.put(EtiquetasModuloDos.IND_3_TASA_BRUTA_ESCOLARIZACION_HLI.get(0),integerFormat(jsonValue?.getString("totalHablanteLenguaIndigena")!!))
                        valores.put(EtiquetasModuloDos.IND_3_TASA_BRUTA_ESCOLARIZACION_HLI.get(1),porcentajeFormat(jsonValue?.getString("porcentajeHablanteLenguaIndigena")!!))
                    }
                    "Porcentaje Modalidad" -> {
                        valores.put(EtiquetasModuloDos.IND_3_PORCENTAJE_MODALIDAD.get(0),integerFormat(jsonValue?.getString("totalEscolarizado")!!))
                        valores.put(EtiquetasModuloDos.IND_3_PORCENTAJE_MODALIDAD.get(1),porcentajeFormat(jsonValue?.getString("porcentajeEscolarizado")!!))
                        valores.put(EtiquetasModuloDos.IND_3_PORCENTAJE_MODALIDAD.get(2),integerFormat(jsonValue?.getString("totalNoEscolarizado")!!))
                        valores.put(EtiquetasModuloDos.IND_3_PORCENTAJE_MODALIDAD.get(3),porcentajeFormat(jsonValue?.getString("porcentajeNoEscolarizado")!!))
                        valores.put(EtiquetasModuloDos.IND_3_PORCENTAJE_MODALIDAD.get(4),integerFormat(jsonValue?.getString("totalMixto")!!))
                        valores.put(EtiquetasModuloDos.IND_3_PORCENTAJE_MODALIDAD.get(5),porcentajeFormat(jsonValue?.getString("porcentajeMixto")!!))
                    }
                }
            }
            5 -> {
                val jsonResponse = JSONArray(arguments?.getString("response"))
                val jsonValue = arguments?.getInt("indexOfElement")?.let { jsonResponse.getJSONObject(it) }
                when (titulo){
                    "Desglose Profesores de Tiempo Completo (PTC)" -> {
                        valores.put(EtiquetasModuloDos.IND_5_DESGLOSE_PROFESORES_TIEMPO_COMPLETO.get(0),integerFormat(jsonValue?.getString("totalPtc")!!))
                        valores.put(EtiquetasModuloDos.IND_5_DESGLOSE_PROFESORES_TIEMPO_COMPLETO.get(1),integerFormat(jsonValue?.getString("ptcLicenciatura")!!))
                        valores.put(EtiquetasModuloDos.IND_5_DESGLOSE_PROFESORES_TIEMPO_COMPLETO.get(2),integerFormat(jsonValue?.getString("ptcMaestria")!!))
                        valores.put(EtiquetasModuloDos.IND_5_DESGLOSE_PROFESORES_TIEMPO_COMPLETO.get(3),integerFormat(jsonValue?.getString("ptcDoctorado")!!))
                        valores.put(EtiquetasModuloDos.IND_5_DESGLOSE_PROFESORES_TIEMPO_COMPLETO.get(4),integerFormat(jsonValue?.getString("ptcPosgrado")!!))
                    }
                    "Desglose Profesores Perfil Deseable Vigente y SNI" -> {
                        valores.put(EtiquetasModuloDos.IND_5_DESGLOSE_PROFESORES_PERFIL_DESEABLE_SNI.get(0),integerFormat(jsonValue?.getString("perfilDeseable")!!))
                        valores.put(EtiquetasModuloDos.IND_5_DESGLOSE_PROFESORES_PERFIL_DESEABLE_SNI.get(1),integerFormat(jsonValue?.getString("snic")!!))
                        valores.put(EtiquetasModuloDos.IND_5_DESGLOSE_PROFESORES_PERFIL_DESEABLE_SNI.get(2),integerFormat(jsonValue?.getString("sni1")!!))
                        valores.put(EtiquetasModuloDos.IND_5_DESGLOSE_PROFESORES_PERFIL_DESEABLE_SNI.get(3),integerFormat(jsonValue?.getString("sni2")!!))
                        valores.put(EtiquetasModuloDos.IND_5_DESGLOSE_PROFESORES_PERFIL_DESEABLE_SNI.get(4),integerFormat(jsonValue?.getString("sni3")!!))
                        valores.put(EtiquetasModuloDos.IND_5_DESGLOSE_PROFESORES_PERFIL_DESEABLE_SNI.get(5),integerFormat(jsonValue?.getString("totalSniVigente")!!))
                    }
                    "Porcentaje PTC con Perfil Deseable" -> {
                        valores.put(EtiquetasModuloDos.IND_5_PORCENTAJE_PTC_PERFIL_DESEABLE.get(0),porcentajeFormatNotMultiplyBy100(jsonValue?.getString("porcentajeGrafica")!!))
                    }
                }
            }
            6 -> {
                val jsonResponse = JSONArray(arguments?.getString("response"))
                val jsonValue = arguments?.getInt("indexOfElement")?.let { jsonResponse.getJSONObject(it) }
                when (titulo){
                    "Desglose Cuerpos Académicos en Formación (CAEF)" -> {
                        valores.put(EtiquetasModuloDos.IND_6_DESGLOSE_CUERPOS_ACADEMICOS_FORMACION.get(0),integerFormat(jsonValue?.getString("caefCienciasAgropecuarias")!!))
                        valores.put(EtiquetasModuloDos.IND_6_DESGLOSE_CUERPOS_ACADEMICOS_FORMACION.get(1),integerFormat(jsonValue?.getString("caefCienciasSocialesAdministrativas")!!))
                        valores.put(EtiquetasModuloDos.IND_6_DESGLOSE_CUERPOS_ACADEMICOS_FORMACION.get(2),integerFormat(jsonValue?.getString("caefCienciasSalud")!!))
                        valores.put(EtiquetasModuloDos.IND_6_DESGLOSE_CUERPOS_ACADEMICOS_FORMACION.get(3),integerFormat(jsonValue?.getString("caefCienciasIngenieriaTecnologia")!!))
                        valores.put(EtiquetasModuloDos.IND_6_DESGLOSE_CUERPOS_ACADEMICOS_FORMACION.get(4),integerFormat(jsonValue?.getString("caefCienciasNaturalezExactas")!!))
                        valores.put(EtiquetasModuloDos.IND_6_DESGLOSE_CUERPOS_ACADEMICOS_FORMACION.get(5),integerFormat(jsonValue?.getString("caefCienciasEducacionHumanidadesArte")!!))
                    }
                    "Desglose Cuerpos Académicos en Consolidación (CAEC)" -> {
                        valores.put(EtiquetasModuloDos.IND_6_DESGLOSE_CUERPOS_ACADEMICOS_CONSOLIDACION.get(0),integerFormat(jsonValue?.getString("caecCienciasAgropecuarias")!!))
                        valores.put(EtiquetasModuloDos.IND_6_DESGLOSE_CUERPOS_ACADEMICOS_CONSOLIDACION.get(1),integerFormat(jsonValue?.getString("caecCienciasSocialesAdministrativas")!!))
                        valores.put(EtiquetasModuloDos.IND_6_DESGLOSE_CUERPOS_ACADEMICOS_CONSOLIDACION.get(2),integerFormat(jsonValue?.getString("caecCienciasSalud")!!))
                        valores.put(EtiquetasModuloDos.IND_6_DESGLOSE_CUERPOS_ACADEMICOS_CONSOLIDACION.get(3),integerFormat(jsonValue?.getString("caecCienciasIngenieriaTecnologia")!!))
                        valores.put(EtiquetasModuloDos.IND_6_DESGLOSE_CUERPOS_ACADEMICOS_CONSOLIDACION.get(4),integerFormat(jsonValue?.getString("caecCienciasNaturalezExactas")!!))
                        valores.put(EtiquetasModuloDos.IND_6_DESGLOSE_CUERPOS_ACADEMICOS_CONSOLIDACION.get(5),integerFormat(jsonValue?.getString("caecCienciasEducacionHumanidadesArte")!!))
                    }
                    "Desglose Cuerpos Académicos Consolidados (CAC)" -> {
                        valores.put(EtiquetasModuloDos.IND_6_DESGLOSE_CUERPOS_ACADEMICOS_CONSOLIDADOS.get(0),integerFormat(jsonValue?.getString("cacCienciasAgropecuarias")!!))
                        valores.put(EtiquetasModuloDos.IND_6_DESGLOSE_CUERPOS_ACADEMICOS_CONSOLIDADOS.get(1),integerFormat(jsonValue?.getString("cacCienciasSocialesAdministrativas")!!))
                        valores.put(EtiquetasModuloDos.IND_6_DESGLOSE_CUERPOS_ACADEMICOS_CONSOLIDADOS.get(2),integerFormat(jsonValue?.getString("cacCienciasSalud")!!))
                        valores.put(EtiquetasModuloDos.IND_6_DESGLOSE_CUERPOS_ACADEMICOS_CONSOLIDADOS.get(3),integerFormat(jsonValue?.getString("cacCienciasIngenieriaTecnologia")!!))
                        valores.put(EtiquetasModuloDos.IND_6_DESGLOSE_CUERPOS_ACADEMICOS_CONSOLIDADOS.get(4),integerFormat(jsonValue?.getString("cacCienciasNaturalezExactas")!!))
                        valores.put(EtiquetasModuloDos.IND_6_DESGLOSE_CUERPOS_ACADEMICOS_CONSOLIDADOS.get(5),integerFormat(jsonValue?.getString("cacCienciasEducacionHumanidadesArte")!!))
                    }
                    "Cuerpos Académicos (CA)" -> {
                        valores.put(EtiquetasModuloDos.IND_6_CUERPOS_ACADEMICOS.get(0),integerFormat(jsonValue?.getString("totalCaef")!!))
                        valores.put(EtiquetasModuloDos.IND_6_CUERPOS_ACADEMICOS.get(1),integerFormat(jsonValue?.getString("totalCaec")!!))
                        valores.put(EtiquetasModuloDos.IND_6_CUERPOS_ACADEMICOS.get(2),integerFormat(jsonValue?.getString("totalCac")!!))
                        valores.put(EtiquetasModuloDos.IND_6_CUERPOS_ACADEMICOS.get(3),integerFormat(jsonValue?.getString("totalCa")!!))
                    }
                    "%CAEF/CA" -> {
                        valores.put(EtiquetasModuloDos.IND_6_PORCENTAJE_CAEF_CA.get(0),jsonValue?.getString("porCaefCienciasAgropecuarias")!!)
                        valores.put(EtiquetasModuloDos.IND_6_PORCENTAJE_CAEF_CA.get(1),jsonValue?.getString("porCaefCienciasSocialesAdministrativas")!!)
                        valores.put(EtiquetasModuloDos.IND_6_PORCENTAJE_CAEF_CA.get(2),jsonValue?.getString("porCaefCienciasSalud")!!)
                        valores.put(EtiquetasModuloDos.IND_6_PORCENTAJE_CAEF_CA.get(3),jsonValue?.getString("porCaefCienciasIngenieriaTecnologia")!!)
                        valores.put(EtiquetasModuloDos.IND_6_PORCENTAJE_CAEF_CA.get(4),jsonValue?.getString("porCaefCienciasNaturalezExactas")!!)
                        valores.put(EtiquetasModuloDos.IND_6_PORCENTAJE_CAEF_CA.get(5),jsonValue?.getString("porCaefCienciasEducacionHumanidadesArte")!!)
                        valores.put(EtiquetasModuloDos.IND_6_PORCENTAJE_CAEF_CA.get(6),jsonValue?.getString("porCaefTotalCaef")!!)
                    }
                    "%CAEC/CA" -> {
                        valores.put(EtiquetasModuloDos.IND_6_PORCENTAJE_CAEC_CA.get(0),jsonValue?.getString("porCaecCienciasAgropecuarias")!!)
                        valores.put(EtiquetasModuloDos.IND_6_PORCENTAJE_CAEC_CA.get(1),jsonValue?.getString("porCaecCienciasSocialesAdministrativas")!!)
                        valores.put(EtiquetasModuloDos.IND_6_PORCENTAJE_CAEC_CA.get(2),jsonValue?.getString("porCaecCienciasSalud")!!)
                        valores.put(EtiquetasModuloDos.IND_6_PORCENTAJE_CAEC_CA.get(3),jsonValue?.getString("porCaecCienciasIngenieriaTecnologia")!!)
                        valores.put(EtiquetasModuloDos.IND_6_PORCENTAJE_CAEC_CA.get(4),jsonValue?.getString("porCaecCienciasNaturalezExactas")!!)
                        valores.put(EtiquetasModuloDos.IND_6_PORCENTAJE_CAEC_CA.get(5),jsonValue?.getString("porCaecCienciasEducacionHumanidadesArte")!!)
                        valores.put(EtiquetasModuloDos.IND_6_PORCENTAJE_CAEC_CA.get(6),jsonValue?.getString("porCaecTotalCaec")!!)
                    }
                    "%CAC/CA" -> {
                        valores.put(EtiquetasModuloDos.IND_6_PORCENTAJE_CAC_CA.get(0),jsonValue?.getString("porCacCienciasAgropecuarias")!!)
                        valores.put(EtiquetasModuloDos.IND_6_PORCENTAJE_CAC_CA.get(1),jsonValue?.getString("porCacCienciasSocialesAdministrativas")!!)
                        valores.put(EtiquetasModuloDos.IND_6_PORCENTAJE_CAC_CA.get(2),jsonValue?.getString("porCacCienciasSalud")!!)
                        valores.put(EtiquetasModuloDos.IND_6_PORCENTAJE_CAC_CA.get(3),jsonValue?.getString("porCacCienciasIngenieriaTecnologia")!!)
                        valores.put(EtiquetasModuloDos.IND_6_PORCENTAJE_CAC_CA.get(4),jsonValue?.getString("porCacCienciasNaturalezExactas")!!)
                        valores.put(EtiquetasModuloDos.IND_6_PORCENTAJE_CAC_CA.get(5),jsonValue?.getString("porCacCienciasEducacionHumanidadesArte")!!)
                        valores.put(EtiquetasModuloDos.IND_6_PORCENTAJE_CAC_CA.get(6),jsonValue?.getString("porCacTotalCac")!!)
                    }
                }
            }
            7 -> {
                if(arguments?.getString("tablaUniversidad")!=null){
                    val jsonResponse = JSONArray(arguments?.getString("tablaUniversidad"))
                    val jsonValue = arguments?.getInt("indexOfElement")?.let { jsonResponse.getJSONObject(it) }
                    valores.put(EtiquetasModuloDos.IND_7.get(0),jsonValue?.getString("otorgado")!!)
                    valores.put(EtiquetasModuloDos.IND_7.get(1),jsonValue?.getString("solicitado")!!)
                    valores.put(EtiquetasModuloDos.IND_7.get(2),porcentajeFormatNoAplica(jsonValue?.getString("porcentaje")!!))
                }else if (arguments?.getString("tablaSubsistema")!=null){
                    val jsonResponse = JSONArray(arguments?.getString("tablaSubsistema"))
                    val jsonValue = arguments?.getInt("indexOfElement")?.let { jsonResponse.getJSONObject(it) }
                    valores.put(EtiquetasModuloDos.IND_7.get(0),jsonValue?.getString("totalOtorgado")!!)
                    valores.put(EtiquetasModuloDos.IND_7.get(1),jsonValue?.getString("totalSolicitado")!!)
                    valores.put(EtiquetasModuloDos.IND_7.get(2),porcentajeFormatNoAplica(jsonValue?.getString("totalPorcentaje")!!))
                }
            }
            8 -> {
                if(arguments?.getString("tablaUniversidad")!=null){
                    val jsonResponse = JSONArray(arguments?.getString("tablaUniversidad"))
                    val jsonValue = arguments?.getInt("indexOfElement")?.let { jsonResponse.getJSONObject(it) }
                    valores.put(EtiquetasModuloDos.IND_8.get(0),jsonValue?.getString("otorgado")!!)
                    valores.put(EtiquetasModuloDos.IND_8.get(1),jsonValue?.getString("solicitado")!!)
                    valores.put(EtiquetasModuloDos.IND_8.get(2),porcentajeFormatNoAplica(jsonValue?.getString("porcentaje")!!))
                }else if (arguments?.getString("tablaSubsistema")!=null){
                    val jsonResponse = JSONArray(arguments?.getString("tablaSubsistema"))
                    val jsonValue = arguments?.getInt("indexOfElement")?.let { jsonResponse.getJSONObject(it) }
                    valores.put(EtiquetasModuloDos.IND_8.get(0),jsonValue?.getString("totalOtorgado")!!)
                    valores.put(EtiquetasModuloDos.IND_8.get(1),jsonValue?.getString("totalSolicitado")!!)
                    valores.put(EtiquetasModuloDos.IND_8.get(2),porcentajeFormatNoAplica(jsonValue?.getString("totalPorcentaje")!!))
                }
            }
            9 -> {
                val jsonObj = JSONObject(arguments?.getString("tablaUniversidad"))
                if(jsonObj.getJSONArray("tabla") != null){
                    val jsonResponse = jsonObj.getJSONArray("tabla")
                    val jsonValue = arguments?.getInt("indexOfElement")?.let { jsonResponse.getJSONObject(it) }
                    valores.put(EtiquetasModuloDos.IND_9.get(0),jsonValue?.getString("otorgado")!!)
                    valores.put(EtiquetasModuloDos.IND_9.get(1),jsonValue?.getString("solicitado")!!)
                    valores.put(EtiquetasModuloDos.IND_9.get(2),porcentajeFormatNoAplica(jsonValue?.getString("porcentaje")!!))
                }else if (arguments?.getString("tablaSubsistema")!=null){
                    val jsonResponse = JSONArray(arguments?.getString("tablaSubsistema"))
                    val jsonValue = arguments?.getInt("indexOfElement")?.let { jsonResponse.getJSONObject(it) }
                    valores.put(EtiquetasModuloDos.IND_9.get(0),jsonValue?.getString("totalOtorgado")!!)
                    valores.put(EtiquetasModuloDos.IND_9.get(1),jsonValue?.getString("totalSolicitado")!!)
                    valores.put(EtiquetasModuloDos.IND_9.get(2),porcentajeFormatNoAplica(jsonValue?.getString("totalPorcentaje")!!))
                }
            }
            10 -> {
                if(arguments?.getString("tablaUniversidad")!=null){
                    val jsonResponse = JSONArray(arguments?.getString("tablaUniversidad"))
                    val jsonValue = arguments?.getInt("indexOfElement")?.let { jsonResponse.getJSONObject(it) }
                    valores.put(EtiquetasModuloDos.IND_10.get(0),jsonValue?.getString("evaluados")!!)
                    valores.put(EtiquetasModuloDos.IND_10.get(1),jsonValue?.getString("subenGrado")!!)
                    valores.put(EtiquetasModuloDos.IND_10.get(2),porcentajeFormatNoAplica(jsonValue?.getString("porcentaje")!!))
                }else if (arguments?.getString("tablaSubsistema")!=null){
                    val jsonResponse = JSONArray(arguments?.getString("tablaSubsistema"))
                    val jsonValue = arguments?.getInt("indexOfElement")?.let { jsonResponse.getJSONObject(it) }
                    valores.put(EtiquetasModuloDos.IND_10.get(0),jsonValue?.getString("totalEvaluados")!!)
                    valores.put(EtiquetasModuloDos.IND_10.get(1),jsonValue?.getString("totalSubenGrado")!!)
                    valores.put(EtiquetasModuloDos.IND_10.get(2),porcentajeFormatNoAplica(jsonValue?.getString("totalPorcentaje")!!))
                }
            }
            11 -> {
                if(arguments?.getString("tablaUniversidad")!=null){
                    val jsonResponse = JSONArray(arguments?.getString("tablaUniversidad"))
                    val jsonValue = arguments?.getInt("indexOfElement")?.let { jsonResponse.getJSONObject(it) }
                    valores.put(EtiquetasModuloDos.IND_11_CUERPOS_ACADEMICOS.get(0),jsonValue?.getString("otorgado")!!)
                    valores.put(EtiquetasModuloDos.IND_11_CUERPOS_ACADEMICOS.get(1),jsonValue?.getString("solicitado")!!)
                    valores.put(EtiquetasModuloDos.IND_11_CUERPOS_ACADEMICOS.get(2),porcentajeFormatNoAplica(jsonValue?.getString("porcentaje")!!))
                }else if (arguments?.getString("tablaSubsistema")!=null){
                    val jsonResponse = JSONArray(arguments?.getString("tablaSubsistema"))
                    val jsonValue = arguments?.getInt("indexOfElement")?.let { jsonResponse.getJSONObject(it) }
                    valores.put(EtiquetasModuloDos.IND_11_SUBSISTEMA.get(0),jsonValue?.getString("totalOtorgado")!!)
                    valores.put(EtiquetasModuloDos.IND_11_SUBSISTEMA.get(1),jsonValue?.getString("totalSolicitado")!!)
                    valores.put(EtiquetasModuloDos.IND_11_SUBSISTEMA.get(2),porcentajeFormatNoAplica(jsonValue?.getString("totalPorcentaje")!!))
                }
            }
            12 -> {
                val jsonResponse = JSONArray(arguments?.getString("response"))
                val jsonValue = arguments?.getInt("indexOfElement")?.let { jsonResponse.getJSONObject(it) }
                valores.put(EtiquetasModuloDos.IND_12_INDICE_ABSORCION.get(0),integerFormat(jsonValue?.getString("totalEmsEgresadosPublicoParticular")!!))
                valores.put(EtiquetasModuloDos.IND_12_INDICE_ABSORCION.get(1),integerFormat(jsonValue?.getString("emsEgresadosPublico")!!))
                valores.put(EtiquetasModuloDos.IND_12_INDICE_ABSORCION.get(2),integerFormat(jsonValue?.getString("emsEgresadosPublicoFederal")!!))
                valores.put(EtiquetasModuloDos.IND_12_INDICE_ABSORCION.get(3),integerFormat(jsonValue?.getString("emsEgresadosPublicoEstatal")!!))
                valores.put(EtiquetasModuloDos.IND_12_INDICE_ABSORCION.get(4),integerFormat(jsonValue?.getString("emsEgresadosPublicoAutonomo")!!))
                valores.put(EtiquetasModuloDos.IND_12_INDICE_ABSORCION.get(5),integerFormat(jsonValue?.getString("emsEgresadosPublicoParticular")!!))
                valores.put(EtiquetasModuloDos.IND_12_INDICE_ABSORCION.get(6),integerFormat(jsonValue?.getString("totalPrimerIngresoPublicoParticular")!!))
                valores.put(EtiquetasModuloDos.IND_12_INDICE_ABSORCION.get(7),integerFormat(jsonValue?.getString("primerIngresoPublicoTSULIC")!!))
                valores.put(EtiquetasModuloDos.IND_12_INDICE_ABSORCION.get(8),integerFormat(jsonValue?.getString("primerIngresoParticularTSULIC")!!))
                valores.put(EtiquetasModuloDos.IND_12_INDICE_ABSORCION.get(9),porcentajeFormat(jsonValue?.getString("totalIndiceAbsorcion")!!))
            }
            13 -> {
                val jsonResponse = JSONArray(arguments?.getString("response"))
                val jsonValue = arguments?.getInt("indexOfElement")?.let { jsonResponse.getJSONObject(it) }
                valores.put(EtiquetasModuloDos.IND_13_MONTO_PROMEDIO_ALUMNO.get(0),jsonValue?.getString("montoFederal")!!)
                valores.put(EtiquetasModuloDos.IND_13_MONTO_PROMEDIO_ALUMNO.get(1),jsonValue?.getString("montoEstatal")!!)
                valores.put(EtiquetasModuloDos.IND_13_MONTO_PROMEDIO_ALUMNO.get(2),jsonValue?.getString("montoPublico")!!)
                valores.put(EtiquetasModuloDos.IND_13_MONTO_PROMEDIO_ALUMNO.get(3),jsonValue?.getString("aportFed")!!)
                valores.put(EtiquetasModuloDos.IND_13_MONTO_PROMEDIO_ALUMNO.get(4),jsonValue?.getString("aportEst")!!)
                valores.put(EtiquetasModuloDos.IND_13_MONTO_PROMEDIO_ALUMNO.get(5),jsonValue?.getString("matriculaSuperior")!!)
                valores.put(EtiquetasModuloDos.IND_13_MONTO_PROMEDIO_ALUMNO.get(6),jsonValue?.getString("matriculaMediaSuperior")!!)
                valores.put(EtiquetasModuloDos.IND_13_MONTO_PROMEDIO_ALUMNO.get(7),jsonValue?.getString("matriculaTotal")!!)
                valores.put(EtiquetasModuloDos.IND_13_MONTO_PROMEDIO_ALUMNO.get(8),jsonValue?.getString("matriculaPond")!!)
                valores.put(EtiquetasModuloDos.IND_13_MONTO_PROMEDIO_ALUMNO.get(9),jsonValue?.getString("subFedAlum")!!)
                valores.put(EtiquetasModuloDos.IND_13_MONTO_PROMEDIO_ALUMNO.get(10),jsonValue?.getString("subEstAlum")!!)
                valores.put(EtiquetasModuloDos.IND_13_MONTO_PROMEDIO_ALUMNO.get(11),jsonValue?.getString("subsTotAlum")!!)
                valores.put(EtiquetasModuloDos.IND_13_MONTO_PROMEDIO_ALUMNO.get(12),jsonValue?.getString("montPubAlum")!!)
            }
            14 -> {
                val jsonResponse = JSONArray(arguments?.getString("response"))
                val jsonValue = arguments?.getInt("indexOfElement")?.let { jsonResponse.getJSONObject(it) }
                valores.put(EtiquetasModuloDos.IND_14_MONTO_PROMEDIO_INSTITUCIONES.get(0),jsonValue?.getString("montoFederal")!!)
                valores.put(EtiquetasModuloDos.IND_14_MONTO_PROMEDIO_INSTITUCIONES.get(1),jsonValue?.getString("montoEstatal")!!)
                valores.put(EtiquetasModuloDos.IND_14_MONTO_PROMEDIO_INSTITUCIONES.get(2),jsonValue?.getString("montoPublico")!!)
                valores.put(EtiquetasModuloDos.IND_14_MONTO_PROMEDIO_INSTITUCIONES.get(3),jsonValue?.getString("aportFed")!!)
                valores.put(EtiquetasModuloDos.IND_14_MONTO_PROMEDIO_INSTITUCIONES.get(4),jsonValue?.getString("aportEst")!!)
            }
        }
        return valores
    }

    private fun addChartsModuloIII(indexOfCharts:List<Int>,charts: List<String>){
        val imageViewCharts = mutableListOf<ImageView>()
        indexOfCharts.map { charts[it] }.forEach {
            val decodedResponse = Base64.decode(it,Base64.DEFAULT)
            val bitMap = BitmapFactory.decodeByteArray(decodedResponse,0,decodedResponse.size)
            val heightPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,bitMap.height.toFloat(),resources.displayMetrics)
            val widthPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,bitMap.width.toFloat(),resources.displayMetrics)
            val layoutParams = LinearLayout.LayoutParams(widthPx.toInt(),heightPx.toInt())
            val imageView = ImageView(context)
            imageView.setImageBitmap(bitMap)
            imageView.layoutParams = layoutParams
            imageViewCharts.add(imageView)
        }
        chartsModuloIII = imageViewCharts.toTypedArray()
    }

    private fun integerFormat(number:String):String = NumberFormat.getNumberInstance(Locale.US).format(number.toInt())

    private fun doubleFormat(number: String):String {
        val numberFormat = NumberFormat.getNumberInstance(Locale.US)
        numberFormat.minimumFractionDigits = 2
        numberFormat.maximumFractionDigits = 2
        try {
            return "$ "+numberFormat.format(number.toDouble())
        }catch (e : NumberFormatException){
            return number
        }
    }

    private fun porcentajeFormat(number:String):String{
        var doubleNumber = number.toDouble()
        doubleNumber*=100
        return String.format("%.${2}f",doubleNumber)+" %"
    }

    private fun porcentajeFormatNoAplica(number:String):String{
        if(number.toDoubleOrNull() != null){
            var doubleNumber = number.toDouble()
            doubleNumber*=100
            return String.format("%.${2}f",doubleNumber)+" %"
        }else{
            return number
        }

    }

    private fun porcentajeFormatFourDecimals(number:String):String{
        var doubleNumber = number.toDouble()
        doubleNumber*=100
        return String.format("%.${4}f",doubleNumber)+" %"
    }

    private fun porcentajeFormatNotMultiplyBy100(number: String):String{
        return String.format("%.${2}f",number.toDouble())+" %"
    }
}
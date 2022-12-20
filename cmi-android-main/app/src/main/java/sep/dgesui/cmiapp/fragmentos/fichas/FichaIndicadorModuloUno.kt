package sep.dgesui.cmiapp.fragmentos.fichas

import android.app.DownloadManager
import android.content.Context
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import org.json.JSONArray
import org.json.JSONObject
import sep.dgesui.cmiapp.R
import sep.dgesui.cmiapp.fragmentos.fichas_pdf.DownloadBroadcastReceiver

class FichaIndicadorModuloUno(private val nombreFicha:String) : Fragment(R.layout.fragment_ficha_indicador_modulo_uno) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tituloIndicador(view)
        pdfIcon(view)
        ficha(view)
    }

    private fun tituloIndicador(view: View){
        val imageView = ImageView(context)
        imageView.setImageResource(R.drawable.back_icon)
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)
        imageView.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val titulo = TextView(context)
        titulo.text = nombreFicha
        titulo.setTextColor(resources.getColor(R.color.gob_green_light))
        titulo.setTypeface(null, Typeface.BOLD)
        titulo.setTextSize(16f)
        titulo.gravity = Gravity.CENTER
        val tituloLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        tituloLayoutParams.setMargins(60,0,60,0)
        titulo.layoutParams = tituloLayoutParams

        view.findViewById<LinearLayout>(R.id.tituloFicha).addView(imageView)
        view.findViewById<LinearLayout>(R.id.tituloFicha).addView(titulo)
    }

    private fun pdfIcon(view: View){
        val imageView = ImageView(context)
        imageView.setImageResource(R.drawable.pdf_icon)
        val layoutParams = LinearLayout.LayoutParams(140, 140)
        layoutParams.setMargins(0,100,0,0)
        imageView.layoutParams = layoutParams
        DownloadBroadcastReceiver.fileName = getFileNameByIndicador().toString()
        DownloadBroadcastReceiver.fragmentManager = parentFragmentManager
        imageView.setOnClickListener {
            val fileName = getFileNameByIndicador()
            val request = DownloadManager.Request(Uri.parse("https://dgesui.ses.sep.gob.mx/cmi/webservice/"+fileName+"/ficha.pdf"))
            request.setTitle(fileName.append(".pdf"))
            request.setMimeType("application/pdf")
            request.allowScanningByMediaScanner()
            request.setAllowedOverMetered(true)
            request.addRequestHeader("Authorization","Bearer "+arguments?.getString("token"))
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,fileName.toString())
            val dm = requireActivity().baseContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            dm.enqueue(request)
        }
        view.findViewById<LinearLayout>(R.id.pdfIcon).addView(imageView)
    }

    private fun getFileNameByIndicador():StringBuilder{
        val fileName : StringBuilder = StringBuilder("")
        when(arguments?.getInt("numIdentificacionIndicador")){
            1 -> {fileName.append("tasa-bruta-escolarizacion-cobertura")}
            2 -> {fileName.append("tasa-bruta-escolarizacion-cobertura-entidad-federativa")}
            3 -> {fileName.append("tasa-bruta-escolarizacion-cobertura-institucion")}
            4 -> {fileName.append("tasa-bruta-escolarizacion-poblacion-primeros-cuatro-deciles")}
            5 -> {fileName.append("porcentaje-eficiencia-terminal-sistema-educativo-nacional")}
            6 -> {fileName.append("porcentaje-abandono-escolar")}
            7 -> {fileName.append("gasto-federal-educacion-porcentaje-pib-ies")}
            8 -> {fileName.append("gasto-federal-educacion-porcentaje-pib-entidad")}
        }
        return fileName
    }

    private fun obtenerComponenteSistemico(componenteSistemicoJson:JSONArray):String{
        val componentesSistemicos : StringBuilder = StringBuilder()
        for(index in 0 until componenteSistemicoJson.length()){
                val componenteSistemico = componenteSistemicoJson.getString(index)
                componentesSistemicos.append(componenteSistemico)
        }
        return componentesSistemicos.toString()
    }

    private fun obtenerNivelesDesagregacion(nivelesDesagregacionJson:JSONArray):String{
        val nivelesDesagregacion : StringBuilder = StringBuilder()
        for(index in 0 until nivelesDesagregacionJson.length()){
                val nivelDesagregacion = nivelesDesagregacionJson
                    .getJSONObject(index)
                    .getString("valor")
                if(index.compareTo(nivelesDesagregacionJson.length()-1)==0){
                    nivelesDesagregacion.append(nivelDesagregacion)
                }else{
                    nivelesDesagregacion.append(nivelDesagregacion).append(", ")
                }
        }
        return nivelesDesagregacion.toString()
    }

    private fun obtenerDimensionCalidadEducativa(dimensionesCalidadEducativaJson:JSONArray):String{
        val dimensionesCalidadEducativa : StringBuilder = StringBuilder()
        for(index in 0 until dimensionesCalidadEducativaJson.length()){
            val dimensionCalidadEducativa = dimensionesCalidadEducativaJson
                .getString(index)
            if(index.compareTo(dimensionesCalidadEducativaJson.length()-1)==0){
                dimensionesCalidadEducativa.append(dimensionCalidadEducativa)
            }else{
                dimensionesCalidadEducativa.append(dimensionCalidadEducativa).append(", ")
            }
        }
        return dimensionesCalidadEducativa.toString()
    }

    private fun ficha(view: View){
        val jsonFicha = arguments?.getString("ficha")?.let { JSONObject(it) }
        val nivelesDesagregacionJson = jsonFicha?.getJSONArray("nivelesDesagregacion")
        val componenteSistemicoJson = jsonFicha?.getJSONArray("componenteSistemico")
        val contribucionInstitucionesDGESUI = jsonFicha?.getJSONObject("contribucionInstitucionesDGESUI")?.getString("valor")
        val unidadMedida = jsonFicha?.getJSONObject("unidadMedida")?.getString("valor")
        val dimensionesCalidadEducativaJson = jsonFicha?.getJSONArray("dimensionCalidadEducativa")

        val elementosFicha : Map<String,String?> =
            mapOf("Nombre del Indicador" to jsonFicha?.getString("nombreIndicador"),
                "Codigo del indicador" to jsonFicha?.getString("codigoIndicador"),
                "Objetivo Prioritario/Meta" to jsonFicha?.getString("objetivoPrioritario"),
                "Definición o Descripción" to jsonFicha?.getString("definicionDescripcion"),
                "Fuente" to jsonFicha?.getString("fuente"),
                "Niveles de desagregación" to nivelesDesagregacionJson?.let { obtenerNivelesDesagregacion(it) },
                "Contribución de las Instituciones de la DGESUI" to contribucionInstitucionesDGESUI,
                "Unidad de medida" to unidadMedida,
                "Tendencia esperada" to jsonFicha?.getString("tendenciaEsperada"),
                "Periodicidad o frecuencia de medición" to jsonFicha?.getString("periodoRecoleccionDatos"),
                "Método de cálculo" to jsonFicha?.getString("metodoCalculo"),
                "Observaciones" to jsonFicha?.getString("observaciones"),
                "Componente Sistémico o Categorías de MIR (fin, propósito, actividad)" to componenteSistemicoJson?.let { obtenerComponenteSistemico(it) },
                "Dimensión de calidad educativa" to dimensionesCalidadEducativaJson?.let { obtenerDimensionCalidadEducativa(it) })

        val fichaLinearLayout = view.findViewById<LinearLayout>(R.id.ficha)

        val titulo = TextView(context)
        titulo.text = "Elementos del indicador o parámetro"
        titulo.setTypeface(null, Typeface.BOLD)
        titulo.setTextColor(resources.getColor(R.color.gob_green_light))
        titulo.setTextSize(14f)
        titulo.gravity = Gravity.CENTER
        titulo.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        fichaLinearLayout.addView(titulo)

        elementosFicha.forEach { (etiqueta,valor) ->
            val linearLayoutLabel = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            linearLayoutLabel.setMargins(0,60,0,0)

            val label = TextView(context)
            label.text = etiqueta
            label.setTypeface(null, Typeface.BOLD)
            label.setTextColor(resources.getColor(R.color.gob_green_light))
            label.setTextSize(14f)
            label.layoutParams = linearLayoutLabel

            val linearLayoutValue = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            linearLayoutValue.setMargins(0,30,0,0)

            val value = TextView(context)
            value.text = valor
            value.setTextColor(resources.getColor(R.color.gob_green_light))
            value.setTextSize(14f)
            value.layoutParams = linearLayoutValue

            fichaLinearLayout.addView(label)
            fichaLinearLayout.addView(value)
        }

        apartadoTituloDos(fichaLinearLayout)

        apartadoTituloTres(fichaLinearLayout)

        apartadoFinal(fichaLinearLayout)
    }

    private fun obtenerNombresVariables(variablesJson:JSONArray):List<String>{
        val nombresVariables : MutableList<String> = mutableListOf()
        for(index in 0 until variablesJson.length()){
            val nombreVariable = variablesJson.getJSONObject(index).getString("nombre")
            nombresVariables.add(nombreVariable)
        }
        return nombresVariables
    }

    private fun obtenerFuentes(variablesJson:JSONArray):List<String>{
        val fuentes : MutableList<String> = mutableListOf()
        for(index in 0 until variablesJson.length()){
            val fuente = variablesJson.getJSONObject(index).getJSONObject("fuente").getString("valor")
            fuentes.add(fuente)
        }
        return fuentes
    }

    private fun apartadoTituloDos(linearLayout: LinearLayout){
        val layoutParamsTituloDos = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParamsTituloDos.setMargins(0,100,0,0)

        val tituloDos = TextView(context)
        tituloDos.text = "Aplicación del método de cálculo del indicador para la obtención del valor de la línea base"
        tituloDos.setTextColor(resources.getColor(R.color.gob_green_light))
        tituloDos.setTypeface(null, Typeface.BOLD)
        tituloDos.setTextSize(14f)
        tituloDos.gravity = Gravity.CENTER
        tituloDos.layoutParams = layoutParamsTituloDos

        linearLayout.addView(tituloDos)

        val jsonFicha = arguments?.getString("ficha")?.let { JSONObject(it) }
        val nombresVariablesJson = jsonFicha?.getJSONArray("variables")?.let { obtenerNombresVariables(it) }
        val fuentesJson = jsonFicha?.getJSONArray("variables")?.let { obtenerFuentes(it) }

        for(index in 0 until nombresVariablesJson?.size!!){
            val linearLayoutLabel = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            linearLayoutLabel.setMargins(0,60,0,0)

            val linearLayoutValue = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            linearLayoutValue.setMargins(0,30,0,60)

            val label = TextView(context)
            label.text = "Nombre de variable"
            label.setTextColor(resources.getColor(R.color.gob_green_light))
            label.setTextSize(14f)
            label.setTypeface(null, Typeface.BOLD)
            label.layoutParams = linearLayoutLabel

            val value = TextView(context)
            value.text = nombresVariablesJson[index]
            value.setTextColor(resources.getColor(R.color.gob_green_light))
            value.setTextSize(14f)
            value.layoutParams = linearLayoutValue

            val labelFuente = TextView(context)
            labelFuente.text = "Fuente"
            labelFuente.setTextColor(resources.getColor(R.color.gob_green_light))
            labelFuente.setTextSize(14f)
            labelFuente.setTypeface(null, Typeface.BOLD)
            label.layoutParams = linearLayoutLabel

            val valueFuente = TextView(context)
            valueFuente.text = fuentesJson?.get(index)!!
            valueFuente.setTextColor(resources.getColor(R.color.gob_green_light))
            valueFuente.setTextSize(14f)
            valueFuente.layoutParams = linearLayoutValue


            linearLayout.addView(label)
            linearLayout.addView(value)
            linearLayout.addView(labelFuente)
            linearLayout.addView(valueFuente)
        }
    }
    
    private fun apartadoTituloTres(linearLayout: LinearLayout){
        val layoutParamsTituloTres = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParamsTituloTres.setMargins(0,100,0,0)

        val jsonFicha = arguments?.getString("ficha")?.let { JSONObject(it) }

        val tituloTres = TextView(context)
        tituloTres.text = "Valor de línea base y metas"
        tituloTres.setTextColor(resources.getColor(R.color.gob_green_light))
        tituloTres.setTypeface(null, Typeface.BOLD)
        tituloTres.setTextSize(14f)
        tituloTres.gravity = Gravity.CENTER
        tituloTres.layoutParams = layoutParamsTituloTres

        val linearLayoutVertical = LinearLayout(context)
        linearLayoutVertical.orientation = LinearLayout.VERTICAL
        linearLayoutVertical.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)

        val linearLayoutH = LinearLayout(context)
        linearLayoutH.orientation = LinearLayout.HORIZONTAL
        linearLayoutH.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)

        val elementosLineaBase : MutableMap<String,String?> =
            mutableMapOf("Valor" to jsonFicha?.getJSONObject("lineaBase")?.getString("valor"),
                "Año" to jsonFicha?.getJSONObject("lineaBase")?.getString("anio"))

        elementosLineaBase.forEach { (etiqueta,valor) ->
            val linearLayoutLineaBase = LinearLayout(context)
            linearLayoutLineaBase.orientation = LinearLayout.VERTICAL
            val layoutParamsLineaBase = TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT,1f)
            linearLayoutLineaBase.layoutParams = layoutParamsLineaBase

            val layoutParamsValue = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutParamsValue.setMargins(0,60,0,0)

            val label = TextView(context)
            label.text = etiqueta
            label.setTextColor(resources.getColor(R.color.gob_green_light))
            label.setTextSize(14f)
            label.setTypeface(null, Typeface.BOLD)
            label.layoutParams = layoutParamsValue

            val value = TextView(context)
            value.text = valor
            value.setTextColor(resources.getColor(R.color.gob_green_light))
            value.setTextSize(14f)
            value.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)

            linearLayoutLineaBase.addView(label)
            linearLayoutLineaBase.addView(value)
            linearLayoutH.addView(linearLayoutLineaBase)
        }

        linearLayout.addView(tituloTres)
        linearLayoutVertical.addView(linearLayoutH)

        val layoutParamsNotaLineaBase = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParamsNotaLineaBase.setMargins(0,60,0,0)

        val labelNotaLineaBase = TextView(context)
        labelNotaLineaBase.text = "Nota sobre línea base"
        labelNotaLineaBase.setTextColor(resources.getColor(R.color.gob_green_light))
        labelNotaLineaBase.setTypeface(null, Typeface.BOLD)
        labelNotaLineaBase.setTextSize(14f)
        labelNotaLineaBase.layoutParams = layoutParamsNotaLineaBase

        val layoutParamsValueNotaLineaBase = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParamsValueNotaLineaBase.setMargins(0,30,0,0)

        val valueNotaLineaBase = TextView(context)
        valueNotaLineaBase.text = jsonFicha?.getJSONObject("lineaBase")?.getString("notas")
        valueNotaLineaBase.setTextColor(resources.getColor(R.color.gob_green_light))
        valueNotaLineaBase.setTextSize(14f)
        valueNotaLineaBase.layoutParams = layoutParamsValueNotaLineaBase

        linearLayoutVertical.addView(labelNotaLineaBase)
        linearLayoutVertical.addView(valueNotaLineaBase)
        linearLayout.addView(linearLayoutVertical)
    }

    private fun apartadoFinal(linearLayout: LinearLayout){
        val jsonFicha = arguments?.getString("ficha")?.let { JSONObject(it) }

        val elementosFinal : MutableMap<String,String?> =
            mutableMapOf("META 2024" to jsonFicha?.getJSONObject("meta")?.getString("valor"),
            "Nota sobre la meta 2024" to jsonFicha?.getJSONObject("meta")?.getString("notas"))

        val linearLayoutLabel = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        linearLayoutLabel.setMargins(0,30,0,0)

        val linearLayoutValue = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        linearLayoutValue.setMargins(0,30,0,0)

        val linearLayoutV = LinearLayout(context)
        linearLayoutV.orientation = LinearLayout.VERTICAL
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(0,100,0,0)
        linearLayoutV.layoutParams = layoutParams

        elementosFinal.forEach { (etiqueta,valor) ->
            val label = TextView(context)
            label.text = etiqueta
            label.setTextColor(resources.getColor(R.color.gob_green_light))
            label.setTextSize(14f)
            label.setTypeface(null, Typeface.BOLD)
            label.layoutParams = linearLayoutLabel

            val value = TextView(context)
            value.text = valor
            value.setTextColor(resources.getColor(R.color.gob_green_light))
            value.setTextSize(14f)
            value.layoutParams = linearLayoutValue

            linearLayoutV.addView(label)
            linearLayoutV.addView(value)
        }
        linearLayout.addView(linearLayoutV)
    }
}
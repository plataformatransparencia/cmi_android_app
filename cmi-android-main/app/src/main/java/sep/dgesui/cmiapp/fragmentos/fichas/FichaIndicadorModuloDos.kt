package sep.dgesui.cmiapp.fragmentos.fichas

import android.app.DownloadManager
import android.content.Context
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TextView
import org.json.JSONArray
import org.json.JSONObject
import sep.dgesui.cmiapp.R
import sep.dgesui.cmiapp.fragmentos.fichas_pdf.DownloadBroadcastReceiver

class FichaIndicadorModuloDos(private val nombreFicha:String) : Fragment(R.layout.fragment_ficha_indicador_modulo_dos) {

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

        view.findViewById<LinearLayout>(R.id.tituloFichaModuloDos).addView(imageView)
        view.findViewById<LinearLayout>(R.id.tituloFichaModuloDos).addView(titulo)
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
        view.findViewById<LinearLayout>(R.id.pdfIconModuloDos).addView(imageView)
    }

    private fun getFileNameByIndicador():StringBuilder{
        val fileName : StringBuilder = StringBuilder("")
        when(arguments?.getInt("numIdentificacionIndicador")){
            1 -> {fileName.append("tasa-bruta-escolarizada")}
            2 -> {fileName.append("tasa-bruta-escolarizada-cobertura")}
            3 -> {fileName.append("tasa-bruta-escolarizacion-ies")}
            4 -> {fileName.append("porcentaje-documentos-normativos")}
            5 -> {fileName.append("porcentaje-profesores-tiempo-completo-ies-reconocimiento")}
            6 -> {fileName.append("porcentaje-cuerpos-academicos-consolidados")}
            7 -> {fileName.append("porcentaje-reconocimiento-perfil-deseable")}
            8 -> {fileName.append("porcentaje-solicitudes-apoyos-estudios-posgrado-aprobadas")}
            9 -> {fileName.append("porcentaje-apoyos-ies-incorporacion")}
            10 -> {fileName.append("porcentaje-cuerpos-academicos-ies")}
            11 -> {fileName.append("porcentaje-solicitudes-apoyo-aprobadas-proyectos-investigacion")}
            12 -> {fileName.append("porcentaje-abosorcion-alumnos-egresados")}
            13 -> {fileName.append("monto-promedio-recursos-radicados-alumnos")}
            14 -> {fileName.append("monto-promedio-recursos-radicados-instituciones")}
            15 -> {fileName.append("porcentaje-centros-organizaciones-sociedad-civil")}
            16 -> {fileName.append("porcentaje-apoyos-operacion-otorgados-centros")}
        }
        return fileName
    }

    private fun ficha(view: View){
        val jsonFicha = arguments?.getString("ficha")?.let { JSONObject(it) }
        val fuentes = jsonFicha?.getJSONArray("fuentes")
        val componentesSistemicos = jsonFicha?.getJSONArray("componenteSistemico")
        val dimensionesCalidad = jsonFicha?.getJSONArray("dimensionCalidadEducativa")

        val elementosFicha : Map<String,String?> =
            mapOf("Nombre del Indicador" to jsonFicha?.getString("nombreIndicador"),
                "Código del indicador" to jsonFicha?.getString("codigoIndicador"),
                "Objetivo" to jsonFicha?.getString("objetivo"),
                "Clasificación MIR" to jsonFicha?.getString("clasificacion"),
                "Definición o descripción" to jsonFicha?.getString("definicion"),
                "Fuente" to fuentes?.let { obtenerFuentes(it) },
                "Nivel de desagregación" to jsonFicha?.getString("nivelDesagregacion"),
                "Unidad de medida" to jsonFicha?.getString("unidadMedida"),
                "Tendencia esperada" to jsonFicha?.getString("tendenciaEsperada"),
                "Periodicidad o frecuencia de medición" to jsonFicha?.getString("periodicidad"),
                "Método de cálculo" to jsonFicha?.getString("metodoCalculo"),
                "Observaciones" to jsonFicha?.getString("observaciones"),
                "Componente Sistémico" to componentesSistemicos?.let { obtenerComponentesSistemicos(it) },
                "Dimensión de calidad educativa" to dimensionesCalidad?.let { obtenerDimensionCalidadEducativa(it) })

        val fichaLinearLayout = view.findViewById<LinearLayout>(R.id.fichaModuloDos)

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

        apartadoTituloCuatro(fichaLinearLayout)
    }

    private fun obtenerFuentes(fuentesJson : JSONArray):String{
        val fuentes : StringBuilder = StringBuilder()
        for(index in 0 until fuentesJson.length()){
            val fuente = fuentesJson
                .getJSONObject(index)
                .getString("fuente")
            fuentes.append(fuente+"\n\n")
        }
        return fuentes.toString()
    }

    private fun obtenerComponentesSistemicos(componentesSistemicosJson:JSONArray):String{
        val componentesSistemicos = StringBuilder()
        for (index in 0 until componentesSistemicosJson.length()){
            val contexto = componentesSistemicosJson
                .getJSONObject(index)
                .getString("contexto")
            val insumos = componentesSistemicosJson
                .getJSONObject(index)
                .getString("insumos")
            val procesos = componentesSistemicosJson
                .getJSONObject(index)
                .getString("procesos")
            val resultados = componentesSistemicosJson
                .getJSONObject(index)
                .getString("resultados")
            componentesSistemicos.append("Contexto  "+contexto+"\n\nInsumos/Recursos    "+insumos+"\n\nProcesos "+procesos+"\n\nResultados  "+resultados)
        }
        return componentesSistemicos.toString()
    }

    private fun obtenerDimensionCalidadEducativa(dimensionesCalidadJson:JSONArray):String{
        val dimensionesCalidad = StringBuilder()
        for (index in 0 until dimensionesCalidadJson.length()){
            val obligatoriedad = dimensionesCalidadJson
                .getJSONObject(index)
                .getString("obligatoriedad")
            val gratuidad = dimensionesCalidadJson
                .getJSONObject(index)
                .getString("gratuidad")
            val equidad = dimensionesCalidadJson
                .getJSONObject(index)
                .getString("equidad")
            val inclusion = dimensionesCalidadJson
                .getJSONObject(index)
                .getString("inclusion")
            val pertinencia = dimensionesCalidadJson
                .getJSONObject(index)
                .getString("pertinencia")
            val relevancia = dimensionesCalidadJson
                .getJSONObject(index)
                .getString("relevancia")
            val eficacia = dimensionesCalidadJson
                .getJSONObject(index)
                .getString("eficacia")
            val suficiencia = dimensionesCalidadJson
                .getJSONObject(index)
                .getString("suficiencia")
            val eficiencia = dimensionesCalidadJson
                .getJSONObject(index)
                .getString("eficiencia")
            val impacto = dimensionesCalidadJson
                .getJSONObject(index)
                .getString("impacto")
            dimensionesCalidad.append("Obligatoriedad  "+obligatoriedad+"\n\nGratuidad    "+gratuidad+"\n\nEquidad    "+equidad+
                    "\n\nInclusión  "+inclusion+"\n\nPertinencia    "+pertinencia+"\n\nRelevancia   "+relevancia+
                    "\n\nEficacia   "+eficacia+"\n\nSuficiencia    "+suficiencia+"\n\nEficiencia    "+eficiencia+
                    "\n\nImpacto    "+impacto)
        }
        return dimensionesCalidad.toString()
    }

    private fun apartadoTituloDos(linearLayout: LinearLayout){
        val layoutParamsTituloTres = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParamsTituloTres.setMargins(0,100,0,0)

        val jsonFicha = arguments?.getString("ficha")?.let { JSONObject(it) }

        val tituloTres = TextView(context)
        tituloTres.text = "APLICACIÓN DEL MÉTODO DE CÁLCULO DEL INDICADOR PARA LA OBTENCIÓN DEL VALOR DE LA LÍNEA BASE"
        tituloTres.setTextColor(resources.getColor(R.color.gob_green_light))
        tituloTres.setTypeface(null, Typeface.BOLD)
        tituloTres.setTextSize(14f)
        tituloTres.gravity = Gravity.CENTER
        tituloTres.layoutParams = layoutParamsTituloTres

        val linearLayoutVertical = LinearLayout(context)
        linearLayoutVertical.orientation = LinearLayout.VERTICAL
        linearLayoutVertical.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)


        linearLayout.addView(tituloTres)

        val layoutParamsNotaLineaBase = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParamsNotaLineaBase.setMargins(0,60,0,0)

        val layoutParamsValueNotaLineaBase = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParamsValueNotaLineaBase.setMargins(0,30,0,0)

        val labelVar1 = TextView(context)
        labelVar1.text = "Nombre variable 1"
        labelVar1.setTextColor(resources.getColor(R.color.gob_green_light))
        labelVar1.setTypeface(null, Typeface.BOLD)
        labelVar1.setTextSize(14f)
        labelVar1.layoutParams = layoutParamsNotaLineaBase

        val valueVar1 = TextView(context)
        valueVar1.text = jsonFicha?.getString("nombreVariable1")
        valueVar1.setTextColor(resources.getColor(R.color.gob_green_light))
        valueVar1.setTextSize(14f)
        valueVar1.layoutParams = layoutParamsValueNotaLineaBase

        val labelVar2 = TextView(context)
        labelVar2.text = "Nombre variable 2"
        labelVar2.setTextColor(resources.getColor(R.color.gob_green_light))
        labelVar2.setTypeface(null, Typeface.BOLD)
        labelVar2.setTextSize(14f)
        labelVar2.layoutParams = layoutParamsNotaLineaBase

        val valueVar2 = TextView(context)
        valueVar2.text = jsonFicha?.getString("nombreVariable2")
        valueVar2.setTextColor(resources.getColor(R.color.gob_green_light))
        valueVar2.setTextSize(14f)
        valueVar2.layoutParams = layoutParamsValueNotaLineaBase

        val labelFuente1 = TextView(context)
        labelFuente1.text = "Fuente de información variable 1"
        labelFuente1.setTextColor(resources.getColor(R.color.gob_green_light))
        labelFuente1.setTypeface(null, Typeface.BOLD)
        labelFuente1.setTextSize(14f)
        labelFuente1.layoutParams = layoutParamsNotaLineaBase

        val valueFuente1 = TextView(context)
        valueFuente1.text = jsonFicha?.getString("fuenteInfoVariable1")
        valueFuente1.setTextColor(resources.getColor(R.color.gob_green_light))
        valueFuente1.setTextSize(14f)
        valueFuente1.layoutParams = layoutParamsValueNotaLineaBase

        val labelFuente2 = TextView(context)
        labelFuente2.text = "Fuente de información variable 2"
        labelFuente2.setTextColor(resources.getColor(R.color.gob_green_light))
        labelFuente2.setTypeface(null, Typeface.BOLD)
        labelFuente2.setTextSize(14f)
        labelFuente2.layoutParams = layoutParamsNotaLineaBase

        val valueFuente2 = TextView(context)
        valueFuente2.text = jsonFicha?.getString("fuenteInfoVariable2")
        valueFuente2.setTextColor(resources.getColor(R.color.gob_green_light))
        valueFuente2.setTextSize(14f)
        valueFuente2.layoutParams = layoutParamsValueNotaLineaBase

        linearLayoutVertical.addView(labelVar1)
        linearLayoutVertical.addView(valueVar1)
        linearLayoutVertical.addView(labelFuente1)
        linearLayoutVertical.addView(valueFuente1)
        linearLayoutVertical.addView(labelVar2)
        linearLayoutVertical.addView(valueVar2)
        linearLayoutVertical.addView(labelFuente2)
        linearLayoutVertical.addView(valueFuente2)
        linearLayout.addView(linearLayoutVertical)
    }


    private fun apartadoTituloTres(linearLayout: LinearLayout){
        val layoutParamsTituloTres = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParamsTituloTres.setMargins(0,100,0,0)

        val jsonFicha = arguments?.getString("ficha")?.let { JSONObject(it) }

        val tituloTres = TextView(context)
        tituloTres.text = "VALOR DE LÍNEA BASE Y METAS"
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
            mutableMapOf("Valor" to jsonFicha?.getString("lineaBaseValor"),
                "Año" to jsonFicha?.getString("lineaBaseAnio"))

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
        valueNotaLineaBase.text = jsonFicha?.getString("lineaBaseNota")
        valueNotaLineaBase.setTextColor(resources.getColor(R.color.gob_green_light))
        valueNotaLineaBase.setTextSize(14f)
        valueNotaLineaBase.layoutParams = layoutParamsValueNotaLineaBase

        val labelMeta2024 = TextView(context)
        labelMeta2024.text = "META 2024"
        labelMeta2024.setTextColor(resources.getColor(R.color.gob_green_light))
        labelMeta2024.setTypeface(null, Typeface.BOLD)
        labelMeta2024.setTextSize(14f)
        labelMeta2024.layoutParams = layoutParamsNotaLineaBase

        val valueMeta2024 = TextView(context)
        valueMeta2024.text = jsonFicha?.getString("meta2024")
        valueMeta2024.setTextColor(resources.getColor(R.color.gob_green_light))
        valueMeta2024.setTextSize(14f)
        valueMeta2024.layoutParams = layoutParamsValueNotaLineaBase

        val labelNotaMeta2024 = TextView(context)
        labelNotaMeta2024.text = "META 2024"
        labelNotaMeta2024.setTextColor(resources.getColor(R.color.gob_green_light))
        labelNotaMeta2024.setTypeface(null, Typeface.BOLD)
        labelNotaMeta2024.setTextSize(14f)
        labelNotaMeta2024.layoutParams = layoutParamsNotaLineaBase

        val valueNotaMeta2024 = TextView(context)
        valueNotaMeta2024.text = jsonFicha?.getString("meta2024Nota")
        valueNotaMeta2024.setTextColor(resources.getColor(R.color.gob_green_light))
        valueNotaMeta2024.setTextSize(14f)
        valueNotaMeta2024.layoutParams = layoutParamsValueNotaLineaBase

        linearLayoutVertical.addView(labelNotaLineaBase)
        linearLayoutVertical.addView(valueNotaLineaBase)
        linearLayoutVertical.addView(labelMeta2024)
        linearLayoutVertical.addView(valueMeta2024)
        linearLayoutVertical.addView(labelNotaMeta2024)
        linearLayoutVertical.addView(valueNotaMeta2024)
        linearLayout.addView(linearLayoutVertical)
    }

    private fun apartadoTituloCuatro(linearLayout: LinearLayout){
        val layoutParamsTituloTres = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParamsTituloTres.setMargins(0,100,0,0)

        val jsonFicha = arguments?.getString("ficha")?.let { JSONObject(it) }

        val tituloTres = TextView(context)
        tituloTres.text = "METAS INTERMEDIAS"
        tituloTres.setTextColor(resources.getColor(R.color.gob_green_light))
        tituloTres.setTypeface(null, Typeface.BOLD)
        tituloTres.setTextSize(14f)
        tituloTres.gravity = Gravity.CENTER
        tituloTres.layoutParams = layoutParamsTituloTres

        val linearLayoutVertical = LinearLayout(context)
        linearLayoutVertical.orientation = LinearLayout.VERTICAL
        linearLayoutVertical.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)

        linearLayout.addView(tituloTres)

        val layoutParamsNotaLineaBase = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParamsNotaLineaBase.setMargins(0,60,0,0)

        val label2020 = TextView(context)
        label2020.text = "2020"
        label2020.setTextColor(resources.getColor(R.color.gob_green_light))
        label2020.setTypeface(null, Typeface.BOLD)
        label2020.setTextSize(14f)
        label2020.layoutParams = layoutParamsNotaLineaBase

        val layoutParamsValueNotaLineaBase = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParamsValueNotaLineaBase.setMargins(0,30,0,0)

        val value2020 = TextView(context)
        value2020.text = jsonFicha?.getString("metaIntermedia2020")
        value2020.setTextColor(resources.getColor(R.color.gob_green_light))
        value2020.setTextSize(14f)
        value2020.layoutParams = layoutParamsValueNotaLineaBase

        val label2021 = TextView(context)
        label2021.text = "2021"
        label2021.setTextColor(resources.getColor(R.color.gob_green_light))
        label2021.setTypeface(null, Typeface.BOLD)
        label2021.setTextSize(14f)
        label2021.layoutParams = layoutParamsNotaLineaBase

        val value2021 = TextView(context)
        value2021.text = jsonFicha?.getString("metaIntermedia2021")
        value2021.setTextColor(resources.getColor(R.color.gob_green_light))
        value2021.setTextSize(14f)
        value2021.layoutParams = layoutParamsValueNotaLineaBase

        val label2022 = TextView(context)
        label2022.text = "2022"
        label2022.setTextColor(resources.getColor(R.color.gob_green_light))
        label2022.setTypeface(null, Typeface.BOLD)
        label2022.setTextSize(14f)
        label2022.layoutParams = layoutParamsNotaLineaBase

        val value2022 = TextView(context)
        value2022.text = jsonFicha?.getString("metaIntermedia2022")
        value2022.setTextColor(resources.getColor(R.color.gob_green_light))
        value2022.setTextSize(14f)
        value2022.layoutParams = layoutParamsValueNotaLineaBase

        val label2023 = TextView(context)
        label2023.text = "2023"
        label2023.setTextColor(resources.getColor(R.color.gob_green_light))
        label2023.setTypeface(null, Typeface.BOLD)
        label2023.setTextSize(14f)
        label2023.layoutParams = layoutParamsNotaLineaBase

        val value2023 = TextView(context)
        value2023.text = jsonFicha?.getString("metaIntermedia2023")
        value2023.setTextColor(resources.getColor(R.color.gob_green_light))
        value2023.setTextSize(14f)
        value2023.layoutParams = layoutParamsValueNotaLineaBase

        val label2024 = TextView(context)
        label2024.text = "2024"
        label2024.setTextColor(resources.getColor(R.color.gob_green_light))
        label2024.setTypeface(null, Typeface.BOLD)
        label2024.setTextSize(14f)
        label2024.layoutParams = layoutParamsNotaLineaBase

        val value2024 = TextView(context)
        value2024.text = jsonFicha?.getString("metaIntermedia2024")
        value2024.setTextColor(resources.getColor(R.color.gob_green_light))
        value2024.setTextSize(14f)
        value2024.layoutParams = layoutParamsValueNotaLineaBase

        linearLayoutVertical.addView(label2020)
        linearLayoutVertical.addView(value2020)
        linearLayoutVertical.addView(label2021)
        linearLayoutVertical.addView(value2021)
        linearLayoutVertical.addView(label2022)
        linearLayoutVertical.addView(value2022)
        linearLayoutVertical.addView(label2023)
        linearLayoutVertical.addView(value2023)
        linearLayoutVertical.addView(label2024)
        linearLayoutVertical.addView(value2024)
        linearLayout.addView(linearLayoutVertical)
    }

}
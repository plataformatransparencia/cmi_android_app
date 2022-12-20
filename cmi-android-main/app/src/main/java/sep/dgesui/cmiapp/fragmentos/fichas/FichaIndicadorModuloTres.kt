package sep.dgesui.cmiapp.fragmentos.fichas

import android.app.DownloadManager
import android.content.Context
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import org.json.JSONArray
import org.json.JSONObject
import sep.dgesui.cmiapp.R
import sep.dgesui.cmiapp.fragmentos.fichas_pdf.DownloadBroadcastReceiver


class FichaIndicadorModuloTres(private val nombreFicha:String) : Fragment(R.layout.fragment_ficha_indicador_modulo_tres) {

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

        view.findViewById<LinearLayout>(R.id.tituloFichaModuloTres).addView(imageView)
        view.findViewById<LinearLayout>(R.id.tituloFichaModuloTres).addView(titulo)
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
        view.findViewById<LinearLayout>(R.id.pdfIconModuloTres).addView(imageView)
    }

    private fun getFileNameByIndicador():StringBuilder{
        val fileName : StringBuilder = StringBuilder("")
        when(arguments?.getInt("numIdentificacionIndicador")){
            1 -> {fileName.append("ordinario-u006")}
            2 -> {fileName.append("federal-u006")}
            3 -> {fileName.append("estatal-u006")}
            4 -> {fileName.append("universidades-en-crisis")}
            5 -> {fileName.append("extraordinario-s247")}
            6 -> {fileName.append("extraordinario-u006")}
            7 -> {fileName.append("u080")}
            8 -> {fileName.append("indicadores-entidad")}
            9 -> {fileName.append("indicadores-subsistema")}
            10 -> {fileName.append("indicadores-ies")}
        }
        return fileName
    }

    private fun ficha(view: View){
        val jsonFicha = arguments?.getString("ficha")?.let { JSONObject(it) }
        val nivelesDesagregacionJson = jsonFicha?.getJSONArray("nivelesDesagregacion")
        val componenteSistemicoJson = jsonFicha?.getJSONArray("componenteSistemico")
        val unidadMedida = jsonFicha?.getJSONObject("unidadMedida")?.getString("valor")
        val unidadesResponsables = jsonFicha?.getJSONArray("uidadesResponsable")
        val dimensionesCalidadEducativaJson = jsonFicha?.getJSONArray("dimensionCalidadEducativa")

        val elementosFicha : Map<String,String?> =
            mapOf("Nombre de la categoria" to jsonFicha?.getString("nombreCategoria"),
                "Código de la categoria" to jsonFicha?.getString("codigo"),
                "Definición o Descripción" to jsonFicha?.getString("definicionDescripcion"),
                "Fuente" to jsonFicha?.getString("fuente"),
                "Niveles de desagregación" to nivelesDesagregacionJson?.let { obtenerNivelesDesagregacion(it) },
                "Unidad de medida" to unidadMedida,
                "Periodicidad o frecuencia de medición" to jsonFicha?.getString("periodoRecoleccion"),
                "Unidad responsable de reportar el avance" to unidadesResponsables?.let { obtenerUnidadesResponsables(it) },
                "Observaciones" to jsonFicha?.getString("observaciones"),
                "Componente Sistémico" to componenteSistemicoJson?.let { obtenerComponenteSistemico(it) },
                "Dimensión de calidad educativa" to dimensionesCalidadEducativaJson?.let { obtenerDimensionCalidadEducativa(it) })

        val fichaLinearLayout = view.findViewById<LinearLayout>(R.id.fichaModuloTres)

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
    }

    private fun obtenerUnidadesResponsables(unidadesResponsablesJson : JSONArray):String{
        val unidadesResponsables : StringBuilder = StringBuilder()
        for(index in 0 until unidadesResponsablesJson.length()){
            val nivelDesagregacion = unidadesResponsablesJson
                .getJSONObject(index)
                .getString("valor")
            if(index.compareTo(unidadesResponsablesJson.length()-1)==0){
                unidadesResponsables.append(nivelDesagregacion)
            }else{
                unidadesResponsables.append(nivelDesagregacion).append(", ")
            }
        }
        return unidadesResponsables.toString()
    }

    private fun obtenerNivelesDesagregacion(nivelesDesagregacionJson: JSONArray):String{
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

    private fun obtenerComponenteSistemico(componenteSistemicoJson:JSONArray):String{
        val componentesSistemicos : StringBuilder = StringBuilder()
        for(index in 0 until componenteSistemicoJson.length()){
            val componenteSistemico = componenteSistemicoJson.getString(index).split(Regex("_"))
            if (index.compareTo(componenteSistemicoJson.length())-1 == 0){
                if (componenteSistemico.size == 2)
                    componentesSistemicos
                        .append(componenteSistemico.get(0))
                        .append("/")
                        .append(componenteSistemico.get(1))
                        .append(", ")
                else if(componenteSistemico.size == 1)
                    componentesSistemicos
                        .append(componenteSistemico.get(0))
                        .append(", ")
            }else{
                if (componenteSistemico.size == 2)
                    componentesSistemicos
                        .append(componenteSistemico.get(0))
                        .append("/")
                        .append(componenteSistemico.get(1))
                else if(componenteSistemico.size == 1)
                    componentesSistemicos
                        .append(componenteSistemico.get(0))
            }
        }
        return componentesSistemicos.toString()
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
}
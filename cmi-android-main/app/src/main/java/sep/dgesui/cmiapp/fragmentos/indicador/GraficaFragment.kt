package sep.dgesui.cmiapp.fragmentos.indicador

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import org.json.JSONArray
import org.json.JSONObject
import sep.dgesui.cmiapp.R
import sep.dgesui.cmiapp.fragmentos.modulos.ModuloDosFragment
import sep.dgesui.cmiapp.fragmentos.modulos.ModuloTresFragment

class GraficaFragment(private val indicadorSeleccionado:IndicadorFragment) : Fragment(R.layout.fragment_grafica) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (indicadorSeleccionado.moduloOrigen() is ModuloTresFragment)
            view.findViewById<LinearLayout>(R.id.parentGrafica).addView(apartadoGraficasModuloIII())
        else if (indicadorSeleccionado.moduloOrigen() is ModuloDosFragment)
            view.findViewById<LinearLayout>(R.id.parentGrafica).addView(apartadoGraficasModuloII())
    }

    private fun apartadoGraficasModuloIII():ScrollView{
        val scrollView = ScrollView(context)
        scrollView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)

        val verticalLinearLayout = LinearLayout(context)
        verticalLinearLayout.orientation = LinearLayout.VERTICAL
        verticalLinearLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)

        if (graficasModuloIII().size == 0){
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
            graficasModuloIII().forEach {
                val horizontalScrollView = HorizontalScrollView(context)
                val layoutParamshorizontalScrollView = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT)
                layoutParamshorizontalScrollView.setMargins(0,30,0,0)
                horizontalScrollView.layoutParams = layoutParamshorizontalScrollView

                val horizontalLinearLayout = LinearLayout(context)
                horizontalLinearLayout.orientation = LinearLayout.HORIZONTAL
                horizontalLinearLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)

                val heightPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,it.height.toFloat(),resources.displayMetrics)
                val widthPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,it.width.toFloat(),resources.displayMetrics)

                val imageView = ImageView(context)
                imageView.setImageBitmap(it)
                imageView.layoutParams = ViewGroup.LayoutParams(widthPx.toInt(),heightPx.toInt())

                horizontalLinearLayout.addView(imageView)
                horizontalScrollView.addView(horizontalLinearLayout)
                verticalLinearLayout.addView(horizontalScrollView)
            }
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

        return scrollView
    }

    private fun apartadoGraficasModuloII():ScrollView{
        val scrollView = ScrollView(context)
        scrollView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT)

        val verticalLinearLayout = LinearLayout(context)
        verticalLinearLayout.orientation = LinearLayout.VERTICAL
        verticalLinearLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)

        if (graficasModuloII().size == 0){
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
            graficasModuloII().forEach {
                val horizontalScrollView = HorizontalScrollView(context)
                val layoutParamshorizontalScrollView = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT)
                layoutParamshorizontalScrollView.setMargins(0,30,0,0)
                horizontalScrollView.layoutParams = layoutParamshorizontalScrollView

                val horizontalLinearLayout = LinearLayout(context)
                horizontalLinearLayout.orientation = LinearLayout.HORIZONTAL
                horizontalLinearLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT)

                val heightPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,it.height.toFloat(),resources.displayMetrics)
                val widthPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,it.width.toFloat(),resources.displayMetrics)

                val imageView = ImageView(context)
                imageView.setImageBitmap(it)
                imageView.layoutParams = ViewGroup.LayoutParams(widthPx.toInt(),heightPx.toInt())

                horizontalLinearLayout.addView(imageView)
                horizontalScrollView.addView(horizontalLinearLayout)
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

        }

        scrollView.addView(verticalLinearLayout)

        return scrollView
    }

    private fun graficasModuloIII():Array<Bitmap>{
        val charts = mutableListOf<Bitmap>()
        val jsonResponse = JSONArray(arguments?.getString("response"))
        for(index in 0 until jsonResponse.length()){
            val decodedResponse = Base64.decode(jsonResponse.getString(index),Base64.DEFAULT)
            try{
                charts.add(BitmapFactory.decodeByteArray(decodedResponse,0,decodedResponse.size))
            }catch (e : NullPointerException){
                e.printStackTrace()
            }
        }
        return charts.toTypedArray()
    }

    private fun graficasModuloII():Array<Bitmap>{
        val charts = mutableListOf<Bitmap>()
        when(indicadorSeleccionado.indicador().numIdentificacion()){
            5,12,13,14 -> {
                val jsonResponse = JSONArray(arguments?.getString("response"))
                for(index in 0 until jsonResponse.length()){
                    val decodedResponse = Base64.decode(jsonResponse.getString(index),Base64.DEFAULT)
                    try{
                        charts.add(BitmapFactory.decodeByteArray(decodedResponse,0,decodedResponse.size))
                    }catch (e : NullPointerException){
                        e.printStackTrace()
                    }
                }
            }
            7,8,9,10,11 -> {
                val jsonResponseUniversidad = JSONArray(arguments?.getString("graficaUniversidad"))
                val jsonResponseSubsistema = JSONArray(arguments?.getString("graficaSubsistema"))
                for(index in 0 until jsonResponseUniversidad.length()){
                    val decodedResponse = Base64.decode(jsonResponseUniversidad.getString(index),Base64.DEFAULT)
                    try{
                        charts.add(BitmapFactory.decodeByteArray(decodedResponse,0,decodedResponse.size))
                    }catch (e : NullPointerException){
                        e.printStackTrace()
                    }
                }
                for(index in 0 until jsonResponseSubsistema.length()){
                    val decodedResponse = Base64.decode(jsonResponseSubsistema.getString(index),Base64.DEFAULT)
                    try{
                        charts.add(BitmapFactory.decodeByteArray(decodedResponse,0,decodedResponse.size))
                    }catch (e : NullPointerException){
                        e.printStackTrace()
                    }
                }
            }
        }
        return charts.toTypedArray()
    }
}
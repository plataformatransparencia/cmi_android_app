package sep.dgesui.cmiapp.fragmentos.indicador

import android.graphics.Typeface
import android.os.Bundle
import android.os.Environment
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import jxl.Workbook
import jxl.WorkbookSettings
import jxl.write.Label
import org.json.JSONArray
import org.json.JSONObject
import sep.dgesui.cmiapp.Communicator
import sep.dgesui.cmiapp.R
import sep.dgesui.cmiapp.filtros.SelectedItemFiltroEjercicioFiscal
import sep.dgesui.cmiapp.filtros.SelectedItemFiltroPeriodoModuloDos
import sep.dgesui.cmiapp.fragmentos.modulos.ModuloDosFragment
import sep.dgesui.cmiapp.fragmentos.modulos.ModuloTresFragment
import sep.dgesui.cmiapp.fragmentos.modulos.ModuloUnoFragment
import sep.dgesui.cmiapp.modelos.*
import java.io.FileOutputStream
import java.text.NumberFormat
import java.util.*

class IndicadorFragment (private val moduloOrigen:Fragment,
                         private val indicadorModulo : IndicadorModulo) : Fragment(R.layout.fragment_indicador),
    SelectedItemFiltroEjercicioFiscal , SelectedItemFiltroPeriodoModuloDos {

    private lateinit var communicator: Communicator
    private lateinit var listIcon : ImageView
    private lateinit var chartIcon : ImageView
    private lateinit var responseToFilterIndicadorModuloIII : String
    private lateinit var responseToFilterIndicadorModuloII : String
    private lateinit var responseToPeriodoFilterIndicadorModuloII : String
    private lateinit var responseFichaModII : String
    private lateinit var responseFichaModIII : String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listIcon = ImageView(requireContext())
        chartIcon = ImageView(requireContext())
        communicator = requireActivity() as Communicator

        tituloIndicador(view)

        if(moduloOrigen is ModuloUnoFragment){
            requestToFichaIndicadorModuloI(indicadorModulo.enlaceFicha(),view)
            requestToIndicadorModuloI(indicadorModulo.enlaces().get(0))
        } else if(moduloOrigen is ModuloTresFragment){
            requestToFichaIndicadorModuloIII(indicadorModulo.enlaceFicha(),view)
            requestToFilterIndicadorModuloIII(view)
        }else if (moduloOrigen is ModuloDosFragment){
            requestToFichaIndicadorModuloII(indicadorModulo.enlaceFicha(),view)
            requestToFilterIndicadorModuloII(view)
        }
    }

    private fun requestToFichaIndicadorModuloI(url: String, view: View){
        val queue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(
            Method.GET,url,
            {
                val bytes = it.toByteArray(Charsets.ISO_8859_1)
                val response = String(bytes,Charsets.UTF_8)
                apartadoFichaIndicadorModuloI(view,response)
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
        queue.add(stringRequest)
    }

    private fun requestToIndicadorModuloI(url: String){
        val queue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(
            Method.GET,url,
            {
                val bytes = it.toByteArray(Charsets.ISO_8859_1)
                val response = String(bytes,Charsets.UTF_8)
                try {
                    parentFragmentManager.beginTransaction().apply {
                        replace(
                            R.id.fragmentContainerViewIndicador,
                            communicator.sendResponseToTablaFragment(response,
                                this@IndicadorFragment))
                        commit()
                    }
                }catch (e : IllegalStateException){
                    e.printStackTrace()
                }
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
        queue.add(stringRequest)
    }

    private fun requestToFilterIndicadorModuloIII(view: View){
        if((indicadorModulo as IndicadorModuloTres).anioParam()!=null&&
            indicadorModulo.subsistemaParam()!=null&&
            indicadorModulo.entidadFederativaParam()!=null&&
            indicadorModulo.universidadParam()!=null){
            apartadoFiltro(view)
            requestToIndicadorModuloIII(indicadorModulo.enlaces())
        }else{
            val queue = Volley.newRequestQueue(context)
            val stringRequest = object : StringRequest(
                Method.GET,
                indicadorModulo.enlaceFiltro(),
                {
                    val bytes = it.toByteArray(Charsets.ISO_8859_1)
                    this.responseToFilterIndicadorModuloIII = String(bytes,Charsets.UTF_8)
                    val responseJson = JSONObject(it)
                    val anio = responseJson.getJSONArray("anios").getString(0)
                    val subsistema = responseJson.getJSONArray("subsistemas").getString(0)
                    val entidadFederativa = responseJson.getJSONArray("entidadesFederativas").getString(0)
                    val universidad = responseJson.getJSONArray("universidades").getString(0)
                    indicadorModulo.anioParam(anio)
                    indicadorModulo.subsistemaParam(subsistema)
                    indicadorModulo.entidadFederativaParam(entidadFederativa)
                    indicadorModulo.universidadParam(universidad)
                    apartadoFiltro(view)
                    requestToIndicadorModuloIII(indicadorModulo.enlaces())
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
            queue.add(stringRequest)
        }
    }

    private fun requestToFilterIndicadorModuloII(view: View){
        if (indicadorModulo.numIdentificacion() == 1 ||
            indicadorModulo.numIdentificacion() == 2 ||
            indicadorModulo.numIdentificacion() == 3){
            if((indicadorModulo as IndicadorModuloDos).periodoParam()!=null&&
                indicadorModulo.subsistemaParam()!=null&&
                indicadorModulo.entidadFederativaParam()!=null&&
                indicadorModulo.universidadParam()!=null){
                apartadoFiltro(view)
                requestToIndicadorModuloII(indicadorModulo.enlaces(),view)
            }else{
                val queue = Volley.newRequestQueue(context)
                val stringRequest = object : StringRequest(
                    Method.GET,
                    indicadorModulo.enlaceFiltro(),
                    {
                        val bytes = it.toByteArray(Charsets.ISO_8859_1)
                        this.responseToFilterIndicadorModuloII = String(bytes,Charsets.UTF_8)
                        val responseJson = JSONObject(it)
                        val subsistema = responseJson.getJSONArray("subsistemas").getString(0)
                        val entidadFederativa = responseJson.getJSONArray("entidadesFederativas").getString(0)
                        val universidad = responseJson.getJSONArray("universidades").getString(0)
                        indicadorModulo.subsistemaParam(subsistema)
                        indicadorModulo.entidadFederativaParam(entidadFederativa)
                        indicadorModulo.universidadParam(universidad)
                        requestToPeriodosFilterIndicadorModuloII(view)
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
                queue.add(stringRequest)
            }
        }else{
            if((indicadorModulo as IndicadorModuloDos).anioParam()!=null&&
                indicadorModulo.subsistemaParam()!=null&&
                indicadorModulo.entidadFederativaParam()!=null&&
                indicadorModulo.universidadParam()!=null){
                apartadoFiltro(view)
                requestToIndicadorModuloII(indicadorModulo.enlaces(),view)
            }else{
                val queue = Volley.newRequestQueue(context)
                val stringRequest = object : StringRequest(
                    Method.GET,
                    indicadorModulo.enlaceFiltro(),
                    {
                        val bytes = it.toByteArray(Charsets.ISO_8859_1)
                        this.responseToFilterIndicadorModuloII = String(bytes,Charsets.UTF_8)
                        val responseJson = JSONObject(it)
                        val anio = responseJson.getJSONArray("anios").getString(0)
                        val subsistema = responseJson.getJSONArray("subsistemas").getString(0)
                        val entidadFederativa = responseJson.getJSONArray("entidadesFederativas").getString(0)
                        val universidad = responseJson.getJSONArray("universidades").getString(0)
                        indicadorModulo.anioParam(anio)
                        indicadorModulo.subsistemaParam(subsistema)
                        indicadorModulo.entidadFederativaParam(entidadFederativa)
                        indicadorModulo.universidadParam(universidad)
                        apartadoFiltro(view)
                        requestToIndicadorModuloII(indicadorModulo.enlaces(),view)
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
                queue.add(stringRequest)
            }
        }
    }

    private fun requestToPeriodosFilterIndicadorModuloII(view: View){
        val queue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(
            Method.GET,
            (indicadorModulo as IndicadorModuloDos).enlaceFiltroPeriodo(),
            {
                this.responseToPeriodoFilterIndicadorModuloII = it
                val responseJson = JSONArray(it)
                val periodoParam = responseJson.getString(0)
                indicadorModulo.periodoParam(periodoParam)
                apartadoFiltro(view)
                requestToIndicadorModuloII(indicadorModulo.enlaces(),view)
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
        queue.add(stringRequest)
    }

    private fun requestToFichaIndicadorModuloIII(url: String, view: View){
        val queue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(
            Method.GET,url,
            {
                val bytes = it.toByteArray(Charsets.ISO_8859_1)
                val response = String(bytes,Charsets.UTF_8)
                this.responseFichaModIII = response
                apartadoFichaIndicadorModuloIII(view,response)
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
        queue.add(stringRequest)
    }

    private fun requestToFichaIndicadorModuloII(url: String, view: View){
        val queue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(
            Method.GET,url,
            {
                val bytes = it.toByteArray(Charsets.ISO_8859_1)
                val response = String(bytes,Charsets.UTF_8)
                this.responseFichaModII = response
                apartadoFichaIndicadorModuloII(view,response)
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
        queue.add(stringRequest)
    }

    private fun requestToIndicadorModuloII(urls: Array<String>,view: View){
        val queue = Volley.newRequestQueue(context)
        var stringRequest : StringRequest? = null
        when (indicadorModulo.enlaces().size){
            1 -> {
                stringRequest = object : StringRequest(
                    Method.GET,urls[0],
                    {
                        val bytes = it.toByteArray(Charsets.ISO_8859_1)
                        val response = String(bytes,Charsets.UTF_8)
                        when (indicadorModulo.numIdentificacion()){
                            1,2,3,6 -> {
                                parentFragmentManager.beginTransaction().apply {
                                    replace(
                                        R.id.fragmentContainerViewIndicador,
                                        communicator.sendResponseToTablaFragment(response,responseFichaModII,
                                            this@IndicadorFragment))
                                    commit()
                                }
                            }
                            4,15,16 -> {
                                listaValoresModuloII(response,view)
                            }
                        }
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
            }
            2 -> {
                listIcon.setImageResource(R.drawable.tabla)
                listIcon.layoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT,1f)

                chartIcon.setImageResource(R.drawable.grafica)
                chartIcon.layoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT,1f)

                stringRequest = object : StringRequest(
                    Method.GET,urls[1],
                    { response ->
                        try {
                            parentFragmentManager.beginTransaction().apply {
                                replace(
                                    R.id.fragmentContainerViewIndicador,
                                    communicator.sendResponseToGraficaFragment(
                                        response,responseFichaModII ,
                                        this@IndicadorFragment))
                                commit()
                            }
                        }catch (e : IllegalStateException){
                            e.printStackTrace()
                        }
                        chartIcon.setOnClickListener {
                            parentFragmentManager.beginTransaction().apply {
                                replace(R.id.fragmentContainerViewIndicador,
                                    communicator.sendResponseToGraficaFragment(response,responseFichaModII,
                                        this@IndicadorFragment))
                                commit()
                            }
                        }
                        stringRequestToTableModuloII(urls[0])
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
            }
            4 -> {
                listIcon.setImageResource(R.drawable.tabla)
                listIcon.layoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT,1f)

                chartIcon.setImageResource(R.drawable.grafica)
                chartIcon.layoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT,1f)

                stringRequest = object : StringRequest(
                    Method.GET,urls[1],
                    {
                        stringRequestToChartSubsistemaModuloII(urls[3],it)
                        stringRequestToTableUniversidadModuloII(arrayOf(urls[0],urls[2]))
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
            }
        }
        queue.add(stringRequest)
    }


    private fun requestToIndicadorModuloIII(urls: Array<String>){
        val queue = Volley.newRequestQueue(context)
        val stringRequestToChart = object : StringRequest(
            Method.GET,urls[1],
            { response ->
                try {
                    parentFragmentManager.beginTransaction().apply {
                        replace(
                            R.id.fragmentContainerViewIndicador,
                            communicator.sendResponseToGraficaFragment(
                                response,responseFichaModIII,
                                this@IndicadorFragment))
                        commit()
                    }
                }catch (e : IllegalStateException){
                    e.printStackTrace()
                }
                chartIcon.setOnClickListener {
                    parentFragmentManager.beginTransaction().apply {
                        replace(R.id.fragmentContainerViewIndicador,
                            communicator.sendResponseToGraficaFragment(response,responseFichaModIII,this@IndicadorFragment))
                        commit()
                    }
                }
                if(urls.size <= 2){
                    stringRequestToTableModuloIII(arrayOf(urls[0]))
                }else{
                    stringRequestToTableModuloIII(arrayOf(urls[0],urls[2]))
                }
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

    private fun stringRequestToTableModuloIII(urls:Array<String>){
        if (context != null){
            val queue = Volley.newRequestQueue(context)
            val stringRequestToTable = object : StringRequest(
                Method.GET,urls[0],
                {
                    val bytes = it.toByteArray(Charsets.ISO_8859_1)
                    val response = String(bytes,Charsets.UTF_8)
                    if(urls.size > 1){
                        stringRequestToTotals(urls[1],response)
                    }else{
                        listIcon.setOnClickListener {
                            parentFragmentManager.beginTransaction().apply {
                                replace(R.id.fragmentContainerViewIndicador,
                                    communicator.sendReponseToTablaFragment(
                                        mapOf("tabla" to response,
                                        "ficha" to responseFichaModIII),
                                        arguments?.getString("token"),
                                        this@IndicadorFragment))
                                commit()
                            }
                        }
                    }
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
            queue.add(stringRequestToTable)
        }
    }

    private fun stringRequestToTableUniversidadModuloII(urls: Array<String>){
        if (context != null){
            val queue = Volley.newRequestQueue(context)
            val stringRequestToTable = object : StringRequest(
                Method.GET,urls[0],
                {
                    val bytes = it.toByteArray(Charsets.ISO_8859_1)
                    val response = String(bytes,Charsets.UTF_8)
                    stringRequestToTableSubsistemaModuloII(urls[1],response)
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
            queue.add(stringRequestToTable)
        }
    }

    private fun stringRequestToTableModuloII(url:String){
        if (context != null){
            val queue = Volley.newRequestQueue(context)
            val stringRequestToTable = object : StringRequest(
                Method.GET,url,
                {
                    val bytes = it.toByteArray(Charsets.ISO_8859_1)
                    val response = String(bytes,Charsets.UTF_8)
                    listIcon.setOnClickListener {
                        parentFragmentManager.beginTransaction().apply {
                            replace(R.id.fragmentContainerViewIndicador,
                                communicator.sendResponseToTablaFragment(response,responseFichaModII,
                                    this@IndicadorFragment))
                            commit()
                        }
                    }
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
            queue.add(stringRequestToTable)
        }
    }

    private fun stringRequestToChartSubsistemaModuloII(url:String, response: String){
        if (context != null){
            val queue = Volley.newRequestQueue(context)
            val stringRequestToTable = object : StringRequest(
                Method.GET,url,
                { responseSubsistema ->
                    try {
                        parentFragmentManager.beginTransaction().apply {
                            replace(R.id.fragmentContainerViewIndicador,
                                communicator.sendResponseToGraficaFragment(
                                    mapOf("graficaUniversidad" to response,
                                        "graficaSubsistema" to responseSubsistema,
                                        "ficha" to responseFichaModII),
                                    this@IndicadorFragment))
                            commit()
                        }
                    }catch (e : IllegalStateException){
                        e.printStackTrace()
                    }
                    chartIcon.setOnClickListener {
                        parentFragmentManager.beginTransaction().apply {
                            replace(R.id.fragmentContainerViewIndicador,
                                communicator.sendResponseToGraficaFragment(
                                    mapOf("graficaUniversidad" to response,
                                    "graficaSubsistema" to responseSubsistema,
                                        "ficha" to responseFichaModII),
                                    this@IndicadorFragment))
                            commit()
                        }
                    }
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
            queue.add(stringRequestToTable)
        }
    }

    private fun stringRequestToTableSubsistemaModuloII(url:String,response: String){
        if (context != null){
            val queue = Volley.newRequestQueue(context)
            val stringRequestToTable = object : StringRequest(
                Method.GET,url,
                {
                    val bytes = it.toByteArray(Charsets.ISO_8859_1)
                    val responseSubsistema = String(bytes,Charsets.UTF_8)
                    listIcon.setOnClickListener {
                        parentFragmentManager.beginTransaction().apply {
                            replace(R.id.fragmentContainerViewIndicador,
                                communicator.sendReponseToTablaFragment(
                                    mapOf("tablaUniversidad" to response,
                                "tablaSubsistema" to responseSubsistema,
                                    "ficha" to responseFichaModII),
                                    this@IndicadorFragment))
                            commit()
                        }
                    }
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
            queue.add(stringRequestToTable)
        }
    }

    private fun stringRequestToTotals(url:String,reponseFromTable:String){
        if (context != null){
            val queue = Volley.newRequestQueue(context)
            val stringRequestToTotals = object : StringRequest(
                Method.GET,url,
                {   response ->
                    listIcon.setOnClickListener {
                        parentFragmentManager.beginTransaction().apply {
                            replace(R.id.fragmentContainerViewIndicador,
                                communicator.sendReponseToTablaFragment(
                                    mapOf("tabla" to reponseFromTable,"totales" to response,"ficha" to responseFichaModIII),
                                    arguments?.getString("token"),
                                    this@IndicadorFragment))
                            commit()
                        }
                    }
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
            queue.add(stringRequestToTotals)
        }
    }

    fun moduloOrigen():Fragment = moduloOrigen
    fun indicador():IndicadorModulo = indicadorModulo

    private fun tituloIndicador(view: View){
        val imageView = ImageView(context)
        imageView.setImageResource(R.drawable.back_icon)
        imageView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        imageView.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val textView = TextView(context)
        textView.text = indicadorModulo.nombre()
        textView.setTextColor(resources.getColor(R.color.gob_green_light))
        textView.setTypeface(null,Typeface.BOLD)
        textView.setTextSize(16f)
        textView.gravity = Gravity.CENTER
        val textViewLayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        textViewLayoutParams.setMargins(60,0,60,0)
        textView.layoutParams = textViewLayoutParams

        view.findViewById<LinearLayout>(R.id.tituloLinearLayout).addView(imageView)
        view.findViewById<LinearLayout>(R.id.tituloLinearLayout).addView(textView)
    }

    private fun apartadoFichaIndicadorModuloI(view: View,ficha:String){
        view.findViewById<LinearLayout>(R.id.fichaLinearLayout).layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)

        val textView = TextView(context)
        textView.text = enlaceFichaModuloI(ficha)
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.setTextSize(15f)
        textView.layoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT,1f)

        val imageView = ImageView(context)
        imageView.setImageResource(R.drawable.excel_icon)
        textView.layoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT,1f)
        imageView.setOnClickListener {
            requestToCsvIndicadorModuloI(indicadorModulo as IndicadorModuloUno)
        }

        view.findViewById<LinearLayout>(R.id.fichaLinearLayout).addView(textView)
        view.findViewById<LinearLayout>(R.id.fichaLinearLayout).addView(imageView)
    }

    private fun requestToCsvIndicadorModuloI(indicadorModuloUno: IndicadorModuloUno){
        val queue = Volley.newRequestQueue(context)
        val stringRequestToTotals = object : StringRequest(
            Method.GET,indicadorModuloUno.enlaceExcel(),
            { response ->
                val outputStream = FileOutputStream(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS).path+"/"+indicadorModuloUno.nombreArchivo()+".xlsx")
                val xlsSettings = WorkbookSettings()
                xlsSettings.locale = Locale("en","EN")
                val xlsWorkBook = Workbook.createWorkbook(outputStream,xlsSettings)
                val xlsWorkSheet = xlsWorkBook.createSheet("data",0)

                val rows = response.split(System.lineSeparator())
                for(row in 0 until rows.size){
                    val cells = rows.get(row).split(",")
                    for(cell in 0 until cells.size){
                       xlsWorkSheet.addCell(Label(cell,row,cells[cell]))
                    }
                }
                xlsWorkBook.write()
                val toast = Toast.makeText(context,"Archivo almacenado", Toast.LENGTH_LONG)
                toast.show()
                xlsWorkBook.close()
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
        queue.add(stringRequestToTotals)
    }

    private fun requestToCsvIndicadorModuloIII(indicadorModuloTres: IndicadorModuloTres){
        val queue = Volley.newRequestQueue(context)
        val stringRequestToTotals = object : StringRequest(
            Method.GET,indicadorModuloTres.enlaceExcel(),
            { response ->
                val outputStream = FileOutputStream(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS).path+"/"+indicadorModuloTres.nombreArchivo()+".xlsx")
                val xlsSettings = WorkbookSettings()
                xlsSettings.locale = Locale("en","EN")
                val xlsWorkBook = Workbook.createWorkbook(outputStream,xlsSettings)
                val xlsWorkSheet = xlsWorkBook.createSheet("data",0)

                val rows = response.split(System.lineSeparator())
                for(row in 0 until rows.size){
                    val cells = rows.get(row).split(",")
                    for(cell in 0 until cells.size){
                        xlsWorkSheet.addCell(Label(cell,row,cells[cell]))
                    }
                }
                xlsWorkBook.write()
                val toast = Toast.makeText(context,"Archivo almacenado", Toast.LENGTH_LONG)
                toast.show()
                xlsWorkBook.close()
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
        queue.add(stringRequestToTotals)
    }

    private fun requestToCsvIndicadorModuloII(indicadorModuloDos: IndicadorModuloDos){
        val queue = Volley.newRequestQueue(context)
        val stringRequestToTotals = object : StringRequest(
            Method.GET,indicadorModuloDos.enlaceExcel(),
            { response ->
                val outputStream = FileOutputStream(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS).path+"/"+indicadorModuloDos.nombreArchivo()+".xlsx")
                val xlsSettings = WorkbookSettings()
                xlsSettings.locale = Locale("en","EN")
                val xlsWorkBook = Workbook.createWorkbook(outputStream,xlsSettings)
                val xlsWorkSheet = xlsWorkBook.createSheet("data",0)

                val rows = response.split(System.lineSeparator())
                for(row in 0 until rows.size){
                    val cells = rows.get(row).split(",")
                    for(cell in 0 until cells.size){
                        xlsWorkSheet.addCell(Label(cell,row,cells[cell]))
                    }
                }
                xlsWorkBook.write()
                val toast = Toast.makeText(context,"Archivo almacenado", Toast.LENGTH_LONG)
                toast.show()
                xlsWorkBook.close()
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
        queue.add(stringRequestToTotals)
    }


    private fun apartadoFichaIndicadorModuloIII(view: View,ficha:String){
        val linearLayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        linearLayoutParams.setMargins(0,60,0,0)
        view.findViewById<LinearLayout>(R.id.fichaLinearLayout).layoutParams = linearLayoutParams

        val textView = TextView(context)
        textView.text = enlaceFichaModuloIII(ficha)
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.setTextSize(15f)
        textView.layoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT,1f)

        val excelIcon = ImageView(context)
        excelIcon.setImageResource(R.drawable.excel_icon)
        excelIcon.layoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT,1f)
        excelIcon.setOnClickListener {
            requestToCsvIndicadorModuloIII(indicadorModulo as IndicadorModuloTres)
        }

        listIcon.setImageResource(R.drawable.tabla)
        listIcon.layoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT,1f)

        chartIcon.setImageResource(R.drawable.grafica)
        chartIcon.layoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT,1f)

        view.findViewById<LinearLayout>(R.id.fichaLinearLayout).addView(textView)
        view.findViewById<LinearLayout>(R.id.fichaLinearLayout).addView(excelIcon)
        view.findViewById<LinearLayout>(R.id.fichaLinearLayout).addView(listIcon)
        view.findViewById<LinearLayout>(R.id.fichaLinearLayout).addView(chartIcon)
    }

    private fun apartadoFichaIndicadorModuloII(view: View,ficha:String){
        val linearLayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
        linearLayoutParams.setMargins(0,60,0,0)
        view.findViewById<LinearLayout>(R.id.fichaLinearLayout).layoutParams = linearLayoutParams

        val textView = TextView(context)
        textView.text = enlaceFichaModuloII(ficha)
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.setTextSize(15f)
        textView.layoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT,1f)

        val excelIcon = ImageView(context)
        excelIcon.setImageResource(R.drawable.excel_icon)
        excelIcon.layoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT,1f)
        excelIcon.setOnClickListener {
            requestToCsvIndicadorModuloII(indicadorModulo as IndicadorModuloDos)
        }

        view.findViewById<LinearLayout>(R.id.fichaLinearLayout).addView(textView)
        view.findViewById<LinearLayout>(R.id.fichaLinearLayout).addView(excelIcon)
        view.findViewById<LinearLayout>(R.id.fichaLinearLayout).addView(listIcon)
        view.findViewById<LinearLayout>(R.id.fichaLinearLayout).addView(chartIcon)
    }

    private fun apartadoFiltro(view: View){
        val textView = TextView(context)
        textView.text = "Aplica un filtro a tu consulta"
        textView.setTextColor(resources.getColor(R.color.gob_green_light))
        textView.setTypeface(null,Typeface.BOLD)
        textView.setTextSize(15f)
        textView.layoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT,1f)

        val imageView = ImageView(context)
        imageView.setImageResource(R.drawable.filter_icon)
        imageView.layoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT,TableLayout.LayoutParams.WRAP_CONTENT,1f)
        imageView.setOnClickListener {
            if(indicadorModulo is IndicadorModuloTres){
                val dialog = communicator.
                sendResponseToFiltroEjercicioFiscal(responseToFilterIndicadorModuloIII,
                    this@IndicadorFragment)
                dialog.show(parentFragmentManager,"filtro_ejercicio_fiscal")
            }else if (indicadorModulo is IndicadorModuloDos){
                if (indicadorModulo.numIdentificacion() == 1 ||
                    indicadorModulo.numIdentificacion() == 2 ||
                    indicadorModulo.numIdentificacion() == 3){
                    val dialog = communicator
                        .sendResponseToFiltroPeriodoModuloII(responseToFilterIndicadorModuloII,
                            responseToPeriodoFilterIndicadorModuloII,
                            this@IndicadorFragment)
                    dialog.show(parentFragmentManager,"filtro_periodo_modulo_dos")
                }else{
                    val dialog = communicator.
                    sendResponseToFiltroEjercicioFiscal(responseToFilterIndicadorModuloII,
                        this@IndicadorFragment)
                    dialog.show(parentFragmentManager,"filtro_ejercicio_fiscal")
                }
            }
        }

        view.findViewById<LinearLayout>(R.id.filtroLinearLayout).addView(textView)
        view.findViewById<LinearLayout>(R.id.filtroLinearLayout).addView(imageView)
    }

    private fun enlaceFichaModuloI(ficha:String):SpannableString{
        val jsonFicha = JSONObject(ficha)
        val codigoFicha = jsonFicha.getString("codigoIndicador")
        val nombreIndicador = jsonFicha.getString("nombreIndicador")
        val nombreFicha = nombreFicha(codigoFicha,nombreIndicador)
        val spannable = SpannableString(nombreFicha)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.fragmentContainerView,
                        communicator.sendFichaDataToFichaIndicadorModuloUno(ficha,
                            arguments?.getString("token"),
                            indicadorModulo.numIdentificacion(),
                            nombreFicha))
                    addToBackStack("fichaIndicadorModuloI")
                    commit()
                }
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = resources.getColor(R.color.gob_link)
            }
        }

        spannable.setSpan(clickableSpan, 0, nombreFicha.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        return spannable
    }

    private fun enlaceFichaModuloII(ficha: String):SpannableString{
        val jsonFicha = JSONObject(ficha)
        val codigoFicha = jsonFicha.getString("codigoIndicador")
        val nombreIndicador = jsonFicha.getString("nombreIndicador")
        val nombreFicha = nombreFicha(codigoFicha,nombreIndicador)
        val spannable = SpannableString(nombreFicha)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.fragmentContainerView,
                        communicator.sendFichaDataToFichaIndicadorModuloDos(ficha,
                            arguments?.getString("token"),
                            indicadorModulo.numIdentificacion(),
                            nombreFicha))
                    addToBackStack("fichaIndicadorModuloII")
                    commit()
                }
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = resources.getColor(R.color.gob_link)
            }
        }

        spannable.setSpan(clickableSpan, 0, nombreFicha.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        return spannable
    }

    private fun enlaceFichaModuloIII(ficha:String):SpannableString{
        val jsonFicha = JSONObject(ficha)
        val codigoFicha = jsonFicha.getString("codigo")
        val nombreIndicador = jsonFicha.getString("nombreCategoria")
        val nombreFicha = nombreFicha(codigoFicha,nombreIndicador)
        val spannable = SpannableString(nombreFicha)
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.fragmentContainerView,
                        communicator.sendFichaDataToFichaIndicadorModuloTres(ficha,
                            arguments?.getString("token"),
                            indicadorModulo.numIdentificacion(),
                            nombreFicha))
                    addToBackStack("fichaIndicadorModuloIII")
                    commit()
                }
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = resources.getColor(R.color.gob_link)
            }
        }

        spannable.setSpan(clickableSpan, 0, nombreFicha.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        return spannable
    }

    private fun listaValoresModuloII(response: String,view: View) {
        val scrollView = ScrollView(context)
        scrollView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)

        val verticalLinearLayout = LinearLayout(context)
        verticalLinearLayout.orientation = LinearLayout.VERTICAL
        verticalLinearLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)

        valoresDeIndicadorModuloDos(response).forEach { (etiqueta,valor) ->
            val linearLayout = LinearLayout(context)
            linearLayout.orientation = LinearLayout.HORIZONTAL
            linearLayout.gravity = Gravity.CENTER
            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(0,100,0,0)
            linearLayout.layoutParams = layoutParams

            val label = TextView(context)
            label.text = etiqueta.split(".").get(0)+" "+valor
            label.setTextColor(resources.getColor(R.color.gob_green_light))
            label.setTextSize(16f)
            label.layoutParams = TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT,1f)

            linearLayout.addView(label)

            verticalLinearLayout.addView(linearLayout)
        }

        scrollView.addView(verticalLinearLayout)
        val linearLayout = view.findViewById<LinearLayout>(R.id.linearLayoutFragmentContainerView)
        if (linearLayout.childCount > 1)
            linearLayout.removeViewAt(linearLayout.childCount-1)
        view.findViewById<LinearLayout>(R.id.linearLayoutFragmentContainerView).addView(scrollView)
    }

    private fun valoresDeIndicadorModuloDos(response:String):MutableMap<String,String>{
        val valores : MutableMap<String,String> = HashMap()
        when(indicadorModulo.numIdentificacion()){
            4 -> {
                val jsonArray = JSONArray(response)
                for (index in 0 until jsonArray.length()){
                    valores.put(
                        EtiquetasModuloDos.IND_4.get(0)+"."+index,jsonArray
                        .getJSONObject(index)
                        .getString("anio"))
                    valores.put(
                        EtiquetasModuloDos.IND_4.get(1)+"."+index,jsonArray
                        .getJSONObject(index)
                        .getString("numeroDocNorProgram"))
                }
            }
            15 -> {
                val jsonArray = JSONArray(response)
                for (index in 0 until jsonArray.length()){
                    valores.put(
                        EtiquetasModuloDos.IND_15.get(0)+"."+index,jsonArray
                        .getJSONObject(index)
                        .getString("ejercicioFiscal"))
                    valores.put(
                        EtiquetasModuloDos.IND_15.get(1)+"."+index,jsonArray
                        .getJSONObject(index)
                        .getString("total"))
                    valores.put(""+"."+index,jsonArray
                        .getJSONObject(index)
                        .getString("nota"))
                }
            }
            16 -> {
                val jsonArray = JSONArray(response)
                for (index in 0 until jsonArray.length()){
                    valores.put(
                        EtiquetasModuloDos.IND_16.get(0)+"."+index,jsonArray
                        .getJSONObject(index)
                        .getString("ejercicioFiscal"))
                    valores.put(
                        EtiquetasModuloDos.IND_16.get(1)+"."+index,doubleFormat(jsonArray
                        .getJSONObject(index)
                        .getString("cantidad")))
                    valores.put(""+"."+index,jsonArray
                        .getJSONObject(index)
                        .getString("nota"))
                }
            }
        }
        return valores
    }

    private fun nombreFicha(codigo:String,nombreIndicador:String):String = codigo+" - "+nombreIndicador

    private fun doubleFormat(number: String):String {
        try {
            return "$ "+ NumberFormat.getNumberInstance(Locale.US).format(number.toDouble())
        }catch (e : NumberFormatException){
            return number
        }
    }

    override fun anioSeleccionado(anio: String) {
        if (indicadorModulo is IndicadorModuloTres)
            indicadorModulo.anioParam(anio)
        else if (indicadorModulo is IndicadorModuloDos)
            indicadorModulo.anioParam(anio)
    }

    override fun subsistemaSeleccionado(subsistema: String) {
        if (indicadorModulo is IndicadorModuloTres)
            indicadorModulo.subsistemaParam(subsistema)
        else if (indicadorModulo is IndicadorModuloDos)
            indicadorModulo.subsistemaParam(subsistema)
    }

    override fun entidadFederativaSeleccionada(entidadFederativa: String) {
        if (indicadorModulo is IndicadorModuloTres)
            indicadorModulo.entidadFederativaParam(entidadFederativa)
        else if (indicadorModulo is IndicadorModuloDos)
            indicadorModulo.entidadFederativaParam(entidadFederativa)
    }

    override fun universidadSeleccionada(universidad: String) {
        if (indicadorModulo is IndicadorModuloTres)
            indicadorModulo.universidadParam(universidad)
        else if (indicadorModulo is IndicadorModuloDos)
            indicadorModulo.universidadParam(universidad)
    }

    override fun filtroAplicado(aplicado: Boolean) {
        if(aplicado && indicadorModulo is IndicadorModuloTres)
            requestToIndicadorModuloIII(indicadorModulo.enlaces())
        else if (aplicado && indicadorModulo is IndicadorModuloDos)
            requestToIndicadorModuloII(indicadorModulo.enlaces(),requireView())
    }

    override fun periodoSeleccionadoModuloII(periodo: String) {
        (indicadorModulo as IndicadorModuloDos).periodoParam(periodo)
    }

    override fun subsistemaSeleccionadoModuloII(subsistema: String) {
        (indicadorModulo as IndicadorModuloDos).subsistemaParam(subsistema)
    }

    override fun entidadFederativaSeleccionadaModuloII(entidadFederativa: String) {
        (indicadorModulo as IndicadorModuloDos).entidadFederativaParam(entidadFederativa)
    }

    override fun universidadSeleccionadaModuloII(universidad: String) {
        (indicadorModulo as IndicadorModuloDos).universidadParam(universidad)
    }

    override fun filtroAplicadoModuloII(aplicado: Boolean) {
        requestToIndicadorModuloII(indicadorModulo.enlaces(),requireView())
    }
}
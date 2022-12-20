package sep.dgesui.cmiapp.fragmentos.modulos

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import sep.dgesui.cmiapp.Communicator
import sep.dgesui.cmiapp.R
import sep.dgesui.cmiapp.filtros.SelectedItemFiltroPeriodo
import sep.dgesui.cmiapp.modelos.IndicadorModuloUno

class ModuloUnoFragment : Fragment(R.layout.fragment_modulo_uno), SelectedItemFiltroPeriodo {

    private lateinit var communicator: Communicator
    private var periodoParam: String? = null
    private var anteriorPeriodo: String? = null
    private val periodos = mutableListOf<String>()

    companion object {
        lateinit var ROOT_URL: String
    }

    init {
        ROOT_URL = "https://dgesui.ses.sep.gob.mx/cmi/webservice"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        communicator = requireActivity() as Communicator

        if (periodoParam == null) {
            requestToPeriodos(view)
        } else {
            loadIndicadores(view)
        }

        view.findViewById<ImageView>(R.id.filter).setOnClickListener {
            val dialog = communicator
                .sendResponseToFiltroPeriodo(
                    periodos.toTypedArray(),
                    this@ModuloUnoFragment
                )
            dialog.show(parentFragmentManager, "filtro_periodo")
        }

        view.findViewById<ImageView>(R.id.back).setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun loadIndicadores(view: View) {
        val indicadores = listOf(
            periodoParam?.let {
                IndicadorModuloUno(
                    1, "Tasa bruta de escolarización (cobertura)",
                    ROOT_URL + "/tasa-bruta-escolarizacion-cobertura/",
                    ROOT_URL + "/tasa-bruta-escolarizacion-cobertura/ficha",
                    ROOT_URL + "/tasa-bruta-escolarizacion-cobertura/",
                    "tasa-bruta-escolarizacion-cobertura",
                    it
                )
            },
            periodoParam?.let {
                IndicadorModuloUno(
                    2, "Tasa bruta de escolarización (cobertura) por entidad federativa",
                    ROOT_URL + "/tasa-bruta-escolarizacion-cobertura-entidad-federativa/",
                    ROOT_URL + "/tasa-bruta-escolarizacion-cobertura-entidad-federativa/ficha",
                    ROOT_URL + "/tasa-bruta-escolarizacion-cobertura-entidad-federativa/",
                    "tasa-bruta-escolarizacion-cobertura-entidad-federativa",
                    it
                )
            },
            periodoParam?.let {
                IndicadorModuloUno(
                    3,
                    "Tasa bruta de escolarización (cobertura) por Institución de Educación Superior",
                    ROOT_URL + "/tasa-bruta-escolarizacion-cobertura-institucion/",
                    ROOT_URL + "/tasa-bruta-escolarizacion-cobertura-institucion/ficha",
                    ROOT_URL + "/tasa-bruta-escolarizacion-cobertura-institucion/",
                    "tasa-bruta-escolarizacion-cobertura-institucion",
                    it
                )
            },
            periodoParam?.let {
                IndicadorModuloUno(
                    4,
                    "Tasa bruta de escolarización de la población en los primeros cuatro deciles de ingreso",
                    ROOT_URL + "/tasa-bruta-escolarizacion-poblacion-primeros-cuatro-deciles/",
                    ROOT_URL + "/tasa-bruta-escolarizacion-poblacion-primeros-cuatro-deciles/ficha",
                    ROOT_URL + "/tasa-bruta-escolarizacion-poblacion-primeros-cuatro-deciles/",
                    "tasa-bruta-escolarizacion-poblacion-primeros-cuatro-deciles",
                    it
                )
            },
            periodoParam?.let {
                IndicadorModuloUno(
                    5, "Porcentaje de eficiencia terminal del Sistema Educativo Nacional",
                    ROOT_URL + "/porcentaje-eficiencia-terminal-sistema-educativo-nacional/",
                    ROOT_URL + "/porcentaje-eficiencia-terminal-sistema-educativo-nacional/ficha",
                    ROOT_URL + "/porcentaje-eficiencia-terminal-sistema-educativo-nacional/",
                    "porcentaje-eficiencia-terminal-sistema-educativo-nacional",
                    it
                )
            },
            periodoParam?.let {
                IndicadorModuloUno(
                    6, "Porcentaje de abandono escolar",
                    ROOT_URL + "/porcentaje-abandono-escolar/",
                    ROOT_URL + "/porcentaje-abandono-escolar/ficha",
                    ROOT_URL + "/porcentaje-abandono-escolar/",
                    "porcentaje-abandono-escolar",
                    it
                )
            },
            periodoParam?.let {
                IndicadorModuloUno(
                    7,
                    "Gasto federal en educación como porcentaje del Producto Interno Bruto (por Institución de Educación Superior) federativa)",
                    ROOT_URL + "/gasto-federal-educacion-porcentaje-pib-ies/",
                    ROOT_URL + "/gasto-federal-educacion-porcentaje-pib-ies/ficha",
                    ROOT_URL + "/gasto-federal-educacion-porcentaje-pib-ies/",
                    "gasto-federal-educacion-porcentaje-pib-ies",
                    it
                )
            },
            periodoParam?.let {
                IndicadorModuloUno(
                    8,
                    "Gasto federal en educación como porcentaje del Producto Interno Bruto (por entidad federativa)",
                    ROOT_URL + "/gasto-federal-educacion-porcentaje-pib-entidad/",
                    ROOT_URL + "/gasto-federal-educacion-porcentaje-pib-entidad/ficha",
                    ROOT_URL + "/gasto-federal-educacion-porcentaje-pib-entidad/",
                    "gasto-federal-educacion-porcentaje-pib-entidad",
                    it
                )
            }
        )

        val linearLayout = view.findViewById<LinearLayout>(R.id.parentIndicadoresModI)

        if (linearLayout.childCount > 7)
            linearLayout.removeAllViews()

        indicadores.forEach {
            view.findViewById<LinearLayout>(R.id.parentIndicadoresModI)
                .addView(it?.let { it1 -> propiedadesIndicador(it1) })
        }
    }

    private fun requestToPeriodos(view: View) {
        val queue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(
            Method.GET,
            ROOT_URL + "/filtro/periodos",
            {
                val responseJson = JSONObject(it)
                val responseArray = responseJson.getJSONArray("lista")
                for (index in 0 until responseArray.length()) {
                    this.periodos.add(responseArray.getString(index))
                }
                periodos.sortDescending()
                periodos.forEach { Log.d("element", it) }
                periodoParam = periodos.get(0)
                if (periodos.size > 1)
                    anteriorPeriodo = periodos.get(1)
                else
                    anteriorPeriodo = periodos.get(0)
                loadIndicadores(view)
            },
            { error ->
                error.message?.let { Log.d("error", it) }
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers: MutableMap<String, String> = HashMap()
                headers.put("Authorization", "Bearer " + arguments?.getString("token"))
                return headers
            }
        }
        queue.add(stringRequest)
    }

    private fun propiedadesIndicador(indicadorModuloUno: IndicadorModuloUno): LinearLayout {
        val linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.HORIZONTAL
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(0, 100, 0, 0)
        linearLayout.layoutParams = layoutParams

        val imageView = ImageView(context)
        imageView.setImageResource(R.drawable.forward_icon)
        imageView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        imageView.setOnClickListener {
            this.periodoParam?.let { it1 -> indicadorModuloUno.periodoParam(it1) }
            this.anteriorPeriodo?.let { indicadorModuloUno.anteriorPeriodo(it) }
            parentFragmentManager.beginTransaction().apply {
                replace(
                    R.id.fragmentContainerView,
                    communicator.sendTokenToIndicadorFragment(
                        arguments?.getString("token"),
                        this@ModuloUnoFragment, indicadorModuloUno
                    )
                )
                addToBackStack("moduloUnoFragment")
                commit()
            }
        }

        val textView = TextView(context)
        textView.text = indicadorModuloUno.nombre()
        textView.setTextColor(resources.getColor(R.color.gob_green_light))
        textView.setTextSize(14f)
        textView.layoutParams = TableLayout.LayoutParams(
            TableLayout.LayoutParams.WRAP_CONTENT,
            TableLayout.LayoutParams.WRAP_CONTENT,
            1f
        )

        linearLayout.addView(textView)
        linearLayout.addView(imageView)

        return linearLayout
    }

    override fun periodoSeleccionado(periodoSeleccionado: String) {
        this.periodoParam = periodoSeleccionado
    }

    override fun filtroAplicado(aplicado: Boolean) {
        if (aplicado)
            loadIndicadores(requireView())
    }
}
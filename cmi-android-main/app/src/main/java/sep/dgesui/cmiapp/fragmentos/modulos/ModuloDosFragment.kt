package sep.dgesui.cmiapp.fragmentos.modulos

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import sep.dgesui.cmiapp.Communicator
import sep.dgesui.cmiapp.R
import sep.dgesui.cmiapp.fragmentos.InicioFragment
import sep.dgesui.cmiapp.modelos.IndicadorModuloDos
import java.util.HashMap

class ModuloDosFragment : Fragment(R.layout.fragment_modulo_dos) {

    private lateinit var communicator: Communicator

    companion object {
        lateinit var ROOT_URL : String
    }
    init {
        ROOT_URL = "https://dgesui.ses.sep.gob.mx/cmi/webservice"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        communicator = requireActivity() as Communicator

        listOf(
            IndicadorModuloDos(1,"Indicador Tasa Bruta Escolarizada",
                arrayOf(ROOT_URL+"/tasa-bruta-escolarizada/"),
                ROOT_URL+"/tasa-bruta-escolarizada/ficha",
                ROOT_URL+"/tasa-bruta-escolarizada/",
                ROOT_URL+"/tasa-bruta-escolarizada/filtro",
                "tasa-bruta-escolarizada"),
            IndicadorModuloDos(2,"Indicador Tasa Bruta Escolarizada (Cobertura) por Entidad Federativa",
                arrayOf(ROOT_URL+"/tasa-bruta-escolarizada-cobertura/"),
                ROOT_URL+"/tasa-bruta-escolarizada-cobertura/ficha",
                ROOT_URL+"/tasa-bruta-escolarizada-cobertura/",
                ROOT_URL+"/tasa-bruta-escolarizada-cobertura/filtro",
                "tasa-bruta-escolarizada-cobertura"),
            IndicadorModuloDos(3,"Indicador Tasa Bruta de Escolarización (cobertura) por Institución de Educación Superior",
                arrayOf(ROOT_URL+"/tasa-bruta-escolarizacion-ies/"),
                ROOT_URL+"/tasa-bruta-escolarizacion-ies/ficha",
                ROOT_URL+"/tasa-bruta-escolarizacion-ies/",
                ROOT_URL+"/tasa-bruta-escolarizacion-ies/filtro",
                "tasa-bruta-escolarizacion-ies"),
            IndicadorModuloDos(4,"Porcentaje de Documentos Normativos en Operación en el Año T",
                arrayOf(ROOT_URL+"/porcentaje-documentos-normativos/"),
                ROOT_URL+"/porcentaje-documentos-normativos/ficha",
                ROOT_URL+"/porcentaje-documentos-normativos/",
                ROOT_URL+"/porcentaje-documentos-normativos/filtro",
                "porcentaje-documentos-normativos"),
            IndicadorModuloDos(5,"Porcentaje de Profesores de Tiempo Completo de Instituciones de Educación Superior con Reconocimiento al Perfil Deseable vigente",
                arrayOf(ROOT_URL+"/porcentaje-profesores-tiempo-completo-ies-reconocimiento/",
                    ROOT_URL+"/porcentaje-profesores-tiempo-completo-ies-reconocimiento/"),
                ROOT_URL+"/porcentaje-profesores-tiempo-completo-ies-reconocimiento/ficha",
                ROOT_URL+"/porcentaje-profesores-tiempo-completo-ies-reconocimiento/",
                ROOT_URL+"/porcentaje-profesores-tiempo-completo-ies-reconocimiento/filtro",
                "porcentaje-profesores-tiempo-completo-ies-reconocimiento"),
            IndicadorModuloDos(6,"Porcentaje de Cuerpos Académicos Consolidados y en Consolidación de las Instituciones de Educación Superior, por Área del Conocimiento",
                arrayOf(ROOT_URL+"/porcentaje-cuerpos-academicos-consolidados/"),
                ROOT_URL+"/porcentaje-cuerpos-academicos-consolidados/ficha",
                ROOT_URL+"/porcentaje-cuerpos-academicos-consolidados/",
                ROOT_URL+"/porcentaje-cuerpos-academicos-consolidados/filtro",
                "porcentaje-cuerpos-academicos-consolidados"),
            IndicadorModuloDos(7,"Porcentaje de Reconocimientos al Perfil Deseable Otorgados a Profesores de Tiempo Completo de Instituciones Públicas de Educación Superior",
                arrayOf(ROOT_URL+"/porcentaje-reconocimiento-perfil-deseable/",
                    ROOT_URL+"/porcentaje-reconocimiento-perfil-deseable/",
                    ROOT_URL+"/porcentaje-reconocimiento-perfil-deseable/subsistema/",
                    ROOT_URL+"/porcentaje-reconocimiento-perfil-deseable/subsistema/"),
                ROOT_URL+"/porcentaje-reconocimiento-perfil-deseable/ficha",
                ROOT_URL+"/porcentaje-reconocimiento-perfil-deseable/",
                ROOT_URL+"/porcentaje-reconocimiento-perfil-deseable/filtro",
                "porcentaje-reconocimiento-perfil-deseable"),
            IndicadorModuloDos(8,"Porcentaje de Solicitudes de Apoyos para Estudios de Posgrado Aprobadas",
                arrayOf(ROOT_URL+"/porcentaje-solicitudes-apoyos-estudios-posgrado-aprobadas/",
                    ROOT_URL+"/porcentaje-solicitudes-apoyos-estudios-posgrado-aprobadas/",
                    ROOT_URL+"/porcentaje-solicitudes-apoyos-estudios-posgrado-aprobadas/subsistema/",
                    ROOT_URL+"/porcentaje-solicitudes-apoyos-estudios-posgrado-aprobadas/subsistema/"),
                ROOT_URL+"/porcentaje-solicitudes-apoyos-estudios-posgrado-aprobadas/ficha",
                ROOT_URL+"/porcentaje-solicitudes-apoyos-estudios-posgrado-aprobadas/",
                ROOT_URL+"/porcentaje-solicitudes-apoyos-estudios-posgrado-aprobadas/filtro",
                "porcentaje-solicitudes-apoyos-estudios-posgrado-aprobadas"),
            IndicadorModuloDos(9,"Porcentaje de Apoyos en IES para la Incorporación de Nuevos Profesores de Tiempo Completo y la Reincorporación de Exbecarios Otorgados",
                arrayOf(ROOT_URL+"/porcentaje-apoyos-ies-incorporacion/",
                    ROOT_URL+"/porcentaje-apoyos-ies-incorporacion/",
                    ROOT_URL+"/porcentaje-apoyos-ies-incorporacion/subsistema/",
                    ROOT_URL+"/porcentaje-apoyos-ies-incorporacion/subsistema/"),
                ROOT_URL+"/porcentaje-apoyos-ies-incorporacion/ficha",
                ROOT_URL+"/porcentaje-apoyos-ies-incorporacion/",
                ROOT_URL+"/porcentaje-apoyos-ies-incorporacion/filtro",
                "porcentaje-apoyos-ies-incorporacion"),
            IndicadorModuloDos(10,"Porcentaje de Cuerpos Académicos en las IES que Cambian a un Grado de Consolidación Superior por Año",
                arrayOf(ROOT_URL+"/porcentaje-cuerpos-academicos-ies/",
                    ROOT_URL+"/porcentaje-cuerpos-academicos-ies/",
                    ROOT_URL+"/porcentaje-cuerpos-academicos-ies/subsistema/",
                    ROOT_URL+"/porcentaje-cuerpos-academicos-ies/subsistema/"),
                ROOT_URL+"/porcentaje-cuerpos-academicos-ies/ficha",
                ROOT_URL+"/porcentaje-cuerpos-academicos-ies/",
                ROOT_URL+"/porcentaje-cuerpos-academicos-ies/filtro",
                "porcentaje-cuerpos-academicos-ies"),
            IndicadorModuloDos(11,"Porcentaje de Solicitudes de Apoyo Aprobadas para Proyectos de Investigación a Cuerpos Académicos en IES",
                arrayOf(ROOT_URL+"/porcentaje-solicitudes-apoyo-aprobadas-proyectos-investigacion/",
                    ROOT_URL+"/porcentaje-solicitudes-apoyo-aprobadas-proyectos-investigacion/",
                    ROOT_URL+"/porcentaje-solicitudes-apoyo-aprobadas-proyectos-investigacion/subsistema/",
                    ROOT_URL+"/porcentaje-solicitudes-apoyo-aprobadas-proyectos-investigacion/subsistema/"),
                ROOT_URL+"/porcentaje-solicitudes-apoyo-aprobadas-proyectos-investigacion/ficha",
                ROOT_URL+"/porcentaje-solicitudes-apoyo-aprobadas-proyectos-investigacion/",
                ROOT_URL+"/porcentaje-solicitudes-apoyo-aprobadas-proyectos-investigacion/filtro",
                "porcentaje-solicitudes-apoyo-aprobadas-proyectos-investigacion"),
            IndicadorModuloDos(12,"Porcentaje de Absorción de Estudiantes Egresados de la Educación Media Superior que Ingresan a la Educación Superior de Instituciones en el Ámbito de la DGESUI Proporcionada por los Organismos Descentralizados Estatales",
                arrayOf(ROOT_URL+"/porcentaje-abosorcion-alumnos-egresados/",
                    ROOT_URL+"/porcentaje-abosorcion-alumnos-egresados/"),
                ROOT_URL+"/porcentaje-abosorcion-alumnos-egresados/ficha",
                ROOT_URL+"/porcentaje-abosorcion-alumnos-egresados/",
                ROOT_URL+"/porcentaje-abosorcion-alumnos-egresados/filtro",
                "porcentaje-abosorcion-alumnos-egresados"),
            IndicadorModuloDos(13,"Monto promedio de Recursos Radicados por Estudiante Inscrito a una Institución de Educación Superior Universitaria Perteneciente a la DGESUI",
                arrayOf(ROOT_URL+"/monto-promedio-recursos-radicados-alumnos/",
                    ROOT_URL+"/monto-promedio-recursos-radicados-alumnos/"),
                ROOT_URL+"/monto-promedio-recursos-radicados-alumnos/ficha",
                ROOT_URL+"/monto-promedio-recursos-radicados-alumnos/",
                ROOT_URL+"/monto-promedio-recursos-radicados-alumnos/filtro",
                "monto-promedio-recursos-radicados-alumnos"),
            IndicadorModuloDos(14,"Monto Promedio de Recursos Radicados a Instituciones de Educación Superior Universitaria Perteneciente a la DGESUI",
                arrayOf(ROOT_URL+"/monto-promedio-recursos-radicados-instituciones/",
                    ROOT_URL+"/monto-promedio-recursos-radicados-instituciones/"),
                ROOT_URL+"/monto-promedio-recursos-radicados-instituciones/ficha",
                ROOT_URL+"/monto-promedio-recursos-radicados-instituciones/",
                ROOT_URL+"/monto-promedio-recursos-radicados-instituciones/filtro",
                "monto-promedio-recursos-radicados-instituciones"),
            IndicadorModuloDos(15,"Porcentaje de Centros, Organizaciones y Sociedad Civil que Continúan con la Prestación de Servicios Vinculados a la Educación en el Año",
                arrayOf(ROOT_URL+"/porcentaje-centros-organizaciones-sociedad-civil/"),
                ROOT_URL+"/porcentaje-centros-organizaciones-sociedad-civil/ficha",
                ROOT_URL+"/porcentaje-centros-organizaciones-sociedad-civil/",
                ROOT_URL+"/porcentaje-centros-organizaciones-sociedad-civil/filtro",
                "porcentaje-centros-organizaciones-sociedad-civil"),
            IndicadorModuloDos(16,"Porcentaje de Apoyos  para la Operación Otorgados a Centros, Organizaciones y Sociedad Civil Vinculados al Tipo de Educación Superior Respecto a los Programados",
                arrayOf(ROOT_URL+"/porcentaje-apoyos-operacion-otorgados-centros/"),
                ROOT_URL+"/porcentaje-apoyos-operacion-otorgados-centros/ficha",
                ROOT_URL+"/porcentaje-apoyos-operacion-otorgados-centros/",
                ROOT_URL+"/porcentaje-apoyos-operacion-otorgados-centros/filtro",
                "porcentaje-apoyos-operacion-otorgados-centros")
        ).forEachIndexed { index, indicador ->
            when(index){
                0 -> {view.findViewById<LinearLayout>(R.id.parentIndicadoresModII).addView(propiedadesTituloIndicador("Indicadores del Programa Presupuestario G001 Normar los Servicios Educativos"))}
                4 -> {view.findViewById<LinearLayout>(R.id.parentIndicadoresModII).addView(propiedadesTituloIndicador("Indicadores del Programa Presupuestario S247 Programa para el Desarrollo Profesional Docente"))}
                11 -> {view.findViewById<LinearLayout>(R.id.parentIndicadoresModII).addView(propiedadesTituloIndicador("Indicadores del Programa Presupuestario U006 Subsidios para Organismos Descentralizados Estatales"))}
                14 -> {view.findViewById<LinearLayout>(R.id.parentIndicadoresModII).addView(propiedadesTituloIndicador("Indicadores del Programa Presupuestario U080 Apoyos a Centros y Organizaciones de Educación"))}
            }
            view.findViewById<LinearLayout>(R.id.parentIndicadoresModII)
                .addView(propiedadesIndicador(indicador))
        }

        view.findViewById<ImageView>(R.id.back).setOnClickListener {
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView, InicioFragment())
                commit()
            }
        }
    }

    private fun propiedadesIndicador(indicadorModuloDos: IndicadorModuloDos) : LinearLayout {
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
                    communicator.sendTokenToIndicadorFragment(arguments?.getString("token"),
                        this@ModuloDosFragment,indicadorModuloDos))
                addToBackStack("moduloDosFragment")
                commit()
            }
        }

        val textView = TextView(context)
        textView.text = indicadorModuloDos.nombre()
        textView.setTextColor(resources.getColor(R.color.gob_green_light))
        textView.setTextSize(14f)
        textView.layoutParams = TableLayout.LayoutParams(
            TableLayout.LayoutParams.WRAP_CONTENT,
            TableLayout.LayoutParams.WRAP_CONTENT,1f)

        linearLayout.addView(textView)
        linearLayout.addView(imageView)

        return linearLayout
    }

    private fun propiedadesTituloIndicador(titulo:String) : LinearLayout {
        val linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.HORIZONTAL
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(0,60,0,0)
        linearLayout.layoutParams = layoutParams

        val textView = TextView(context)
        textView.text = titulo
        textView.setTypeface(null, Typeface.BOLD)
        textView.setTextColor(resources.getColor(R.color.gob_green_light))
        textView.setTextSize(14f)
        textView.layoutParams = layoutParams

        linearLayout.addView(textView)

        return linearLayout
    }
}
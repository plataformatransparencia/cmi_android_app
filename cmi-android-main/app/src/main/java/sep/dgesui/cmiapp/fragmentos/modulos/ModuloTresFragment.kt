package sep.dgesui.cmiapp.fragmentos.modulos

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import sep.dgesui.cmiapp.Communicator
import sep.dgesui.cmiapp.R
import sep.dgesui.cmiapp.modelos.IndicadorModuloTres
import sep.dgesui.cmiapp.modelos.ValorRaiz
import java.time.LocalDateTime

class ModuloTresFragment : Fragment(R.layout.fragment_modulo_tres) {

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

        val indicadores = listOf(
            IndicadorModuloTres(1,"Ordinario U006",
                arrayOf(ROOT_URL +"/ordinario-u006/",
                        ROOT_URL +"/ordinario-u006/"), ValorRaiz.UNIVERSIDAD,
                ROOT_URL +"/ordinario-u006/ficha",
                ROOT_URL+"/ordinario-u006/",
                ROOT_URL+"/ordinario-u006/filtro",
                "ordinario-u006"),
            IndicadorModuloTres(2,"Federal U006",
                arrayOf(ROOT_URL +"/federal-u006/",
                        ROOT_URL +"/federal-u006/",
                        ROOT_URL +"/federal-u006/"),ValorRaiz.UNIVERSIDAD,
                ROOT_URL +"/federal-u006/ficha",
                ROOT_URL+"/federal-u006/",
                ROOT_URL+"/federal-u006/filtro",
                "federal-u006"),
            IndicadorModuloTres(3,"Estatal U006",
                arrayOf(ROOT_URL +"/estatal-u006/",
                        ROOT_URL +"/estatal-u006/",
                        ROOT_URL +"/estatal-u006/"),ValorRaiz.UNIVERSIDAD,
                ROOT_URL +"/estatal-u006/ficha",
                ROOT_URL+"/estatal-u006/",
                ROOT_URL+"/estatal-u006/filtro",
                "estatal-u006"),
            IndicadorModuloTres(4,"Universidades en crisis",
                arrayOf(ROOT_URL +"/universidades-en-crisis/",
                        ROOT_URL +"/universidades-en-crisis/",
                        ROOT_URL +"/universidades-en-crisis/"),ValorRaiz.UNIVERSIDAD,
                ROOT_URL +"/universidades-en-crisis/ficha",
                ROOT_URL+"/universidades-en-crisis/",
                ROOT_URL+"/universidades-en-crisis/filtro",
                ""),
            IndicadorModuloTres(5,"Extraordinario S247",
                arrayOf(ROOT_URL +"/extraordinario-s247/",
                        ROOT_URL +"/extraordinario-s247/",
                        ROOT_URL +"/extraordinario-s247/"),ValorRaiz.UNIVERSIDAD,
                ROOT_URL +"/extraordinario-s247/ficha",
                ROOT_URL+"/extraordinario-s247/",
                ROOT_URL+"/extraordinario-s247/filtro",
                "extraordinario-s247"),
            IndicadorModuloTres(6,"Extraordinario U006",
                arrayOf(ROOT_URL +"/extraordinario-u006/",
                        ROOT_URL +"/extraordinario-u006/",
                        ROOT_URL +"/extraordinario-u006/"),ValorRaiz.UNIVERSIDAD,
                ROOT_URL +"/extraordinario-u006/ficha",
                ROOT_URL+"/extraordinario-u006/",
                ROOT_URL+"/extraordinario-u006/filtro",
                "extraordinario-u006"),
            IndicadorModuloTres(7,"U080",
                arrayOf(ROOT_URL +"/u080/",
                        ROOT_URL +"/u080/",
                        ROOT_URL +"/u080/"),ValorRaiz.OSC_CENTRO,
                ROOT_URL +"/u080/ficha",
                ROOT_URL+"/u080/",
                ROOT_URL+"/u080/filtro",
                "u080"),
            IndicadorModuloTres(8,"Indicadores Entidad",
                    arrayOf(ROOT_URL +"/indicadores-entidad/",
                            ROOT_URL +"/indicadores-entidad/"),ValorRaiz.ENTIDAD_FEDERATIVA,
                ROOT_URL +"/indicadores-entidad/ficha",
                ROOT_URL+"/indicadores-entidad/",
                ROOT_URL+"/indicadores-entidad/filtro",
                "indicadores-entidad"),
            IndicadorModuloTres(9,"Indicadores Subsistema",
                arrayOf(ROOT_URL +"/indicadores-subsistema/",
                        ROOT_URL +"/indicadores-subsistema/",
                        ROOT_URL +"/indicadores-subsistema/"),ValorRaiz.SUBSISTEMA,
                ROOT_URL +"/indicadores-subsistema/ficha",
                ROOT_URL+"/indicadores-subsistema/",
                ROOT_URL+"/indicadores-subsistema/filtro",
                "indicadores-subsistema"),
            IndicadorModuloTres(10,"Indicadores IES",
                arrayOf(ROOT_URL +"/indicadores-ies/",
                        ROOT_URL +"/indicadores-ies/"),ValorRaiz.UNIVERSIDAD,
                ROOT_URL +"/indicadores-ies/ficha",
                ROOT_URL+"/indicadores-ies/",
                ROOT_URL+"/indicadores-ies/filtro",
                "indicadores-ies")
        )

        indicadores.forEach {
            view.findViewById<LinearLayout>(R.id.parentIndicadoresModIII)
                .addView(propiedadesIndicador(it))
        }

        view.findViewById<ImageView>(R.id.back).setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun propiedadesIndicador(indicadorModuloTres: IndicadorModuloTres) : LinearLayout {
        val linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.HORIZONTAL
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(0,100,0,0)
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
                    this@ModuloTresFragment,indicadorModuloTres))
                addToBackStack("moduloTresFragment")
                commit()
            }
        }

        val textView = TextView(context)
        textView.text = indicadorModuloTres.nombre()
        textView.setTextColor(resources.getColor(R.color.gob_green_light))
        textView.setTextSize(14f)
        textView.layoutParams = TableLayout.LayoutParams(
            TableLayout.LayoutParams.WRAP_CONTENT,
            TableLayout.LayoutParams.WRAP_CONTENT,1f)

        linearLayout.addView(textView)
        linearLayout.addView(imageView)

        return linearLayout
    }
}
package sep.dgesui.cmiapp.fragmentos.secciones

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import sep.dgesui.cmiapp.Communicator
import sep.dgesui.cmiapp.R
import sep.dgesui.cmiapp.fragmentos.indicador.IndicadorFragment
import sep.dgesui.cmiapp.fragmentos.modulos.ModuloDosFragment
import sep.dgesui.cmiapp.fragmentos.modulos.ModuloTresFragment
import sep.dgesui.cmiapp.modelos.CategoriasModuloDos
import sep.dgesui.cmiapp.modelos.CategoriasModuloTres

class SeccionUnoFragment(private val indicadorOrigen: IndicadorFragment,
                         private val titulo:String) :
    Fragment(R.layout.fragment_seccion_uno), SeccionInterface {

    private lateinit var communicator: Communicator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        communicator = requireActivity() as Communicator

        view.findViewById<LinearLayout>(R.id.parentSeccionUno).addView(tituloSeccion())
        if(indicadorOrigen.moduloOrigen() is ModuloTresFragment)
            view.findViewById<LinearLayout>(R.id.parentSeccionUno)
                .addView(apartadoListaValoresModuloIII(categoriesModuloIII()))
        if(indicadorOrigen.moduloOrigen() is ModuloDosFragment)
            view.findViewById<LinearLayout>(R.id.parentSeccionUno)
                .addView(apartadoListaValoresModuloII(categoriesModuloII()))
    }

    private fun tituloSeccion():LinearLayout{
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

    private fun apartadoListaValoresModuloIII(valores:Array<String>): ScrollView {
        val scrollView = ScrollView(context)
        scrollView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)

        val verticalLinearLayout = LinearLayout(context)
        verticalLinearLayout.orientation = LinearLayout.VERTICAL
        verticalLinearLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)

        valores.forEach { valor ->
            val linearLayout = LinearLayout(context)
            linearLayout.orientation = LinearLayout.HORIZONTAL
            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(0,100,0,0)
            linearLayout.layoutParams = layoutParams

            val imageView = ImageView(context)
            imageView.setImageResource(R.drawable.forward_icon)
            imageView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            when(indicadorOrigen.indicador().numIdentificacion()){
                1,4,5,6,7,8,9,10 -> {
                    imageView.setOnClickListener {
                        parentFragmentManager.beginTransaction().apply {
                            replace(R.id.fragmentContainerView,
                                communicator.sendDataToSeccionValoresFragment(
                                    arrayOf(arguments?.getString("tabla"),
                                        arguments?.getString("totales"),arguments?.getString("ficha")),
                                    this@SeccionUnoFragment,
                                    valor,
                                    arguments?.getString("token"),
                                    arguments?.getInt("indexOfElement")))
                            addToBackStack("seccionUnoFragment")
                            commit()
                        }
                    }
                }
                2,3 -> {
                    imageView.setOnClickListener {
                        parentFragmentManager.beginTransaction().apply {
                            replace(R.id.fragmentContainerView,
                                communicator.sendDataToSeccionDosFragment(
                                    arrayOf(arguments?.getString("tabla"),
                                        arguments?.getString("totales"),arguments?.getString("ficha")),
                                    indicadorOrigen,
                                    valor,
                                    arguments?.getString("token"),
                                    arguments?.getInt("indexOfElement")))
                            addToBackStack("seccionUnoFragment")
                            commit()
                        }
                    }
                }
            }


            val textView = TextView(context)
            textView.text = valor
            textView.setTextColor(resources.getColor(R.color.gob_green_light))
            textView.setTextSize(14f)
            textView.layoutParams = TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT,1f)

            linearLayout.addView(textView)
            linearLayout.addView(imageView)

            verticalLinearLayout.addView(linearLayout)
        }
        scrollView.addView(verticalLinearLayout)
        return scrollView
    }

    private fun apartadoListaValoresModuloII(valores:Array<String>):ScrollView{
        val scrollView = ScrollView(context)
        scrollView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)

        val verticalLinearLayout = LinearLayout(context)
        verticalLinearLayout.orientation = LinearLayout.VERTICAL
        verticalLinearLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)

        valores.forEach { valor ->
            val linearLayout = LinearLayout(context)
            linearLayout.orientation = LinearLayout.HORIZONTAL
            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(0,100,0,0)
            linearLayout.layoutParams = layoutParams

            val imageView = ImageView(context)
            imageView.setImageResource(R.drawable.forward_icon)
            imageView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT)
            imageView.setOnClickListener {
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.fragmentContainerView,
                        communicator.sendDataToSeccionValoresFragment(arguments?.getString("response"),arguments?.getString("ficha"),
                            indicadorOrigen, valor,
                            arguments?.getInt("indexOfElement")))
                    addToBackStack("seccionValoresFragment")
                    commit()
                }
            }

            val textView = TextView(context)
            textView.text = valor
            textView.setTextColor(resources.getColor(R.color.gob_green_light))
            textView.setTextSize(14f)
            textView.layoutParams = TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT,1f)

            linearLayout.addView(textView)
            linearLayout.addView(imageView)

            verticalLinearLayout.addView(linearLayout)
        }
        scrollView.addView(verticalLinearLayout)
        return scrollView
    }

    private fun categoriesModuloIII():Array<String>{
        var categories : Array<String> = emptyArray()
        when(indicadorOrigen.indicador().numIdentificacion()){
            1 -> { categories = CategoriasModuloTres.MOD3_IND_1 }
            2 -> { categories = CategoriasModuloTres.MOD3_IND_2 }
            3 -> { categories = CategoriasModuloTres.MOD3_IND_3 }
            4 -> { categories = CategoriasModuloTres.MOD3_IND_4 }
            5 -> { categories = CategoriasModuloTres.MOD3_IND_5 }
            6 -> { categories = CategoriasModuloTres.MOD3_IND_6 }
            7 -> { categories = CategoriasModuloTres.MOD3_IND_7 }
            8 -> { categories = CategoriasModuloTres.MOD3_IND_8 }
            9 -> { categories = CategoriasModuloTres.MOD3_IND_9 }
            10 -> { categories = CategoriasModuloTres.MOD3_IND_10 }
        }
        return categories
    }

    private fun categoriesModuloII():Array<String>{
        var categories : Array<String> = emptyArray()
        when(indicadorOrigen.indicador().numIdentificacion()){
            1 -> { categories = CategoriasModuloDos.MOD2_IND_1 }
            2 -> { categories = CategoriasModuloDos.MOD2_IND_2 }
            3 -> { categories = CategoriasModuloDos.MOD2_IND_3 }
            5 -> { categories = CategoriasModuloDos.MOD2_IND_5 }
            6 -> { categories = CategoriasModuloDos.MOD2_IND_6 }
            7 -> { categories = CategoriasModuloDos.MOD2_IND_7 }
            8 -> { categories = CategoriasModuloDos.MOD2_IND_8 }
            9 -> { categories = CategoriasModuloDos.MOD2_IND_9 }
            10 -> { categories = CategoriasModuloDos.MOD2_IND_10 }
            11 -> { categories = CategoriasModuloDos.MOD2_IND_11 }
            12 -> { categories = CategoriasModuloDos.MOD2_IND_12 }
            13 -> { categories = CategoriasModuloDos.MOD2_IND_13 }
            14 -> { categories = CategoriasModuloDos.MOD2_IND_14 }
        }
        return categories
    }

    override fun indicadorOrigen(): IndicadorFragment = indicadorOrigen
    override fun instancia(): Fragment = this@SeccionUnoFragment
}
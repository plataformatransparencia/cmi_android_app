package sep.dgesui.cmiapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import sep.dgesui.cmiapp.databinding.ActivityMainBinding
import sep.dgesui.cmiapp.filtros.*
import sep.dgesui.cmiapp.fragmentos.InfoFragment
import sep.dgesui.cmiapp.fragmentos.InicioFragment
import sep.dgesui.cmiapp.fragmentos.fichas.FichaIndicadorModuloDos
import sep.dgesui.cmiapp.fragmentos.fichas.FichaIndicadorModuloTres
import sep.dgesui.cmiapp.fragmentos.fichas.FichaIndicadorModuloUno
import sep.dgesui.cmiapp.fragmentos.indicador.GraficaFragment
import sep.dgesui.cmiapp.fragmentos.indicador.IndicadorFragment
import sep.dgesui.cmiapp.fragmentos.indicador.TablaFragment
import sep.dgesui.cmiapp.fragmentos.modulos.ModuloDosFragment
import sep.dgesui.cmiapp.fragmentos.modulos.ModuloTresFragment
import sep.dgesui.cmiapp.fragmentos.modulos.ModuloUnoFragment
import sep.dgesui.cmiapp.fragmentos.secciones.SeccionDosFragment
import sep.dgesui.cmiapp.fragmentos.secciones.SeccionUnoFragment
import sep.dgesui.cmiapp.fragmentos.secciones.SeccionValoresFragment
import sep.dgesui.cmiapp.modelos.IndicadorModulo

class Home : AppCompatActivity(),Communicator {

    private lateinit var toogle: ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolBar : Toolbar
    private lateinit var navigationView: NavigationView

    val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted ->
        if (isGranted) {
            Log.d("Permission: ", "Granted")
        } else {
            Log.d("Permission: ", "Denied")
        }
    }

    fun requestPermissionLauncher() : ActivityResultLauncher<String> = requestPermissionLauncher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        toolBar = findViewById(R.id.toolbar_home)
        drawerLayout = findViewById(R.id.drawer)
        navigationView = findViewById(R.id.navigationView)

        toogle = ActionBarDrawerToggle(this,drawerLayout,toolBar,R.string.open_drawer,R.string.close_drawer)
        drawerLayout.addDrawerListener(toogle)
        toogle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sharedPreferences = getSharedPreferences("access", MODE_PRIVATE)
        val user: String? = sharedPreferences.getString("user","")
        navigationView.getHeaderView(0).findViewById<TextView>(R.id.textViewUserName).text = user

        supportFragmentManager.beginTransaction().apply {
            val bundle = Bundle()
            bundle.putString("token",intent.getStringExtra("token"))
            val fragment = InicioFragment()
            fragment.arguments = bundle
            replace(R.id.fragmentContainerView,fragment)
            commit()
        }

        navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.info_item_menu -> {
                    supportFragmentManager.beginTransaction().apply {
                        val bundle = Bundle()
                        bundle.putString("token",intent.getStringExtra("token"))
                        val fragment = InfoFragment()
                        fragment.arguments = bundle
                        replace(R.id.fragmentContainerView,fragment)
                        addToBackStack("InicioFragment")
                        commit()
                    }
                }
                R.id.inicio_item_menu -> {
                    supportFragmentManager.beginTransaction().apply {
                        val bundle = Bundle()
                        bundle.putString("token",intent.getStringExtra("token"))
                        val fragment = InicioFragment()
                        fragment.arguments = bundle
                        replace(R.id.fragmentContainerView,fragment)
                        addToBackStack("inicioFragment")
                        commit()
                    }
                }
                R.id.logout_item_menu -> {
                    val queue = Volley.newRequestQueue(this)
                    val stringRequest : StringRequest = object : StringRequest(
                        Method.POST,
                        "https://dgesui.ses.sep.gob.mx/pruebascmi/auth/realms/CMI/protocol/openid-connect/logout",
                        {
                            startActivity(Intent(this,MainActivity::class.java))
                        },
                        {
                            it.message?.let { message -> Log.d("error", message) }
                        }){
                        override fun getParams(): MutableMap<String, String>? {
                            val params :MutableMap<String,String> = HashMap()
                            params.put("client_id","app_cmi")
                            params.put("refresh_token", intent.getStringExtra("refreshToken")!!)
                            return params
                        }
                    }
                    queue.add(stringRequest)
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toogle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun sendTokenToModuloIFragment(token: String?): ModuloUnoFragment {
        val bundle = Bundle()
        bundle.putString("token",token)
        val fragment = ModuloUnoFragment()
        fragment.arguments = bundle

        return fragment
    }

    override fun sendTokenToModuloIIFragment(token: String?): ModuloDosFragment {
        val bundle = Bundle()
        bundle.putString("token",token)
        val fragment = ModuloDosFragment()
        fragment.arguments = bundle

        return fragment
    }

    override fun sendTokenToModuloIIIFragment(token: String?): ModuloTresFragment {
        val bundle = Bundle()
        bundle.putString("token",token)
        val fragment = ModuloTresFragment()
        fragment.arguments = bundle

        return fragment
    }

    override fun sendTokenToInicioFragment(token: String?): InicioFragment {
        val bundle = Bundle()
        bundle.putString("token",token)
        val fragment = InicioFragment()
        fragment.arguments = bundle

        return fragment
    }

    override fun sendTokenToIndicadorFragment(
        token: String?,
        moduloOrigen: Fragment,
        indicadorModulo: IndicadorModulo
    ): IndicadorFragment {
        val bundle = Bundle()
        bundle.putString("token",token)
        val fragment = IndicadorFragment(moduloOrigen,indicadorModulo)
        fragment.arguments = bundle

        return fragment
    }

    override fun sendResponseToFiltroPeriodo(
        response: Array<String?>,
        selectedItemFiltroPeriodo: SelectedItemFiltroPeriodo
    ): FiltroPeriodo {
        val bundle = Bundle()
        bundle.putStringArray("response",response)
        val filtroPeriodo = FiltroPeriodo(selectedItemFiltroPeriodo)
        filtroPeriodo.arguments = bundle

        return filtroPeriodo
    }

    override fun sendResponseToFiltroEjercicioFiscal(
        response: String?,
        selectedItemFiltroEjercicioFiscal: SelectedItemFiltroEjercicioFiscal
    ): FiltroEjercicioFiscal {
        val bundle = Bundle()
        bundle.putString("response",response)
        val filtroEjercicioFiscal = FiltroEjercicioFiscal(selectedItemFiltroEjercicioFiscal)
        filtroEjercicioFiscal.arguments = bundle

        return filtroEjercicioFiscal
    }

    override fun sendResponseToFiltroPeriodoModuloII(
        response: String?,
        responsePeriodo: String?,
        selectedItemFiltroPeriodoModuloDos: SelectedItemFiltroPeriodoModuloDos
    ): FiltroPeriodoModuloDos {
        val bundle = Bundle()
        bundle.putString("response",response)
        bundle.putString("responsePeriodo",responsePeriodo)
        val filtroPeriodoModuloDos = FiltroPeriodoModuloDos(selectedItemFiltroPeriodoModuloDos)
        filtroPeriodoModuloDos.arguments = bundle

        return filtroPeriodoModuloDos
    }

    override fun sendResponseToTablaFragment(
        response: String?,
        indicadorOrigen: IndicadorFragment
    ): TablaFragment {
        val bundle = Bundle()
        bundle.putString("response",response)
        val fragment = TablaFragment(indicadorOrigen)
        fragment.arguments = bundle

        return fragment
    }

    override fun sendResponseToTablaFragment(
        response: String?,
        responseFicha: String?,
        indicadorOrigen: IndicadorFragment
    ): TablaFragment {
        val bundle = Bundle()
        bundle.putString("ficha",responseFicha)
        bundle.putString("response",response)
        val fragment = TablaFragment(indicadorOrigen)
        fragment.arguments = bundle

        return fragment
    }

    override fun sendReponseToTablaFragment(
        response: Map<String, String>,
        token: String?,
        indicadorOrigen: IndicadorFragment
    ): TablaFragment {
        val bundle = Bundle()
        response.forEach { (key, value) ->
            bundle.putString(key,value)
        }
        bundle.putString("token",token)
        val fragment = TablaFragment(indicadorOrigen)
        fragment.arguments = bundle

        return fragment
    }

    override fun sendReponseToTablaFragment(
        response: Map<String, String>,
        indicadorOrigen: IndicadorFragment
    ): TablaFragment {
        val bundle = Bundle()
        response.forEach { (key, value) ->
            bundle.putString(key,value)
        }
        val fragment = TablaFragment(indicadorOrigen)
        fragment.arguments = bundle

        return fragment
    }

    override fun sendResponseToSeccionValoresFragment(
        response: String?,
        indicadorFragment: IndicadorFragment,
        titulo: String
    ): SeccionValoresFragment {
        val bundle = Bundle()
        bundle.putString("response",response)
        val fragment = SeccionValoresFragment(indicadorFragment,titulo)
        fragment.arguments = bundle

        return fragment
    }

    override fun sendDataToSeccionDosFragment(
        response: Array<String?>,
        indicadorFragment: IndicadorFragment,
        titulo: String,
        token: String?,
        indexOfElement: Int?
    ): SeccionDosFragment {
        val bundle = Bundle()
        bundle.putString("tabla",response[0])
        bundle.putString("totales",response[1])
        bundle.putInt("indexOfElement",indexOfElement!!)
        bundle.putString("token",token)

        val fragment = SeccionDosFragment(indicadorFragment,titulo)
        fragment.arguments = bundle

        return fragment
    }

    override fun sendDataToSeccionValoresFragment(
        response: Map<String, String?>,
        indicadorFragment: IndicadorFragment,
        titulo: String,
        indexOfElement: Int?
    ): SeccionValoresFragment {
        val bundle = Bundle()
        response.forEach { (key, value) ->
            bundle.putString(key,value)
        }
        bundle.putInt("indexOfElement",indexOfElement!!)
        val fragment = SeccionValoresFragment(indicadorFragment,titulo)
        fragment.arguments = bundle

        return fragment
    }

    override fun sendDataToSeccionValoresFragment(
        response: String?,
        indicadorFragment: IndicadorFragment,
        titulo: String,
        indexOfElement: Int?
    ): SeccionValoresFragment {
        val bundle = Bundle()
        bundle.putString("response",response)
        bundle.putInt("indexOfElement",indexOfElement!!)
        val fragment = SeccionValoresFragment(indicadorFragment,titulo)
        fragment.arguments = bundle

        return fragment
    }

    override fun sendDataToSeccionValoresFragment(
        response: String?,
        responseFicha: String?,
        indicadorFragment: IndicadorFragment,
        titulo: String,
        indexOfElement: Int?
    ): SeccionValoresFragment {
        val bundle = Bundle()
        bundle.putString("ficha",responseFicha)
        bundle.putString("response",response)
        bundle.putInt("indexOfElement",indexOfElement!!)
        val fragment = SeccionValoresFragment(indicadorFragment,titulo)
        fragment.arguments = bundle

        return fragment
    }

    override fun sendDataToSeccionValoresFragment(
        response: Array<String?>,
        seccionDosFragment: SeccionDosFragment,
        titulo: String,
        token: String?,
        indexOfElement: Int?,
        tabSeleccionado: String
    ): SeccionValoresFragment {
        val bundle = Bundle()
        bundle.putString("tabla",response[0])
        bundle.putString("totales",response[1])
        bundle.putString("ficha",response[2])
        bundle.putInt("indexOfElement",indexOfElement!!)
        bundle.putString("tabSeleccionado",tabSeleccionado)
        bundle.putString("token",token)
        val fragment = SeccionValoresFragment(seccionDosFragment,titulo)
        fragment.arguments = bundle

        return fragment
    }

    override fun sendDataToSeccionValoresFragment(
        response: Array<String?>,
        seccionUnoFragment: SeccionUnoFragment,
        titulo: String,
        token: String?,
        indexOfElement: Int?
    ): SeccionValoresFragment {
        val bundle = Bundle()
        bundle.putString("tabla",response[0])
        bundle.putString("totales",response[1])
        bundle.putString("ficha",response[2])
        bundle.putInt("indexOfElement",indexOfElement!!)
        bundle.putString("token",token)
        val fragment = SeccionValoresFragment(seccionUnoFragment,titulo)
        fragment.arguments = bundle

        return fragment
    }

    override fun sendDataToSeccionUnoFragment(
        response: Array<String?>,
        indicadorFragment: IndicadorFragment,
        titulo: String,
        token: String?,
        indexOfElement: Int
    ): SeccionUnoFragment {
        val bundle = Bundle()
        bundle.putString("tabla",response[0])
        bundle.putString("totales",response[1])
        bundle.putString("ficha",response[2])
        bundle.putInt("indexOfElement",indexOfElement)
        bundle.putString("token",token)
        val fragment = SeccionUnoFragment(indicadorFragment,titulo)
        fragment.arguments = bundle

        return fragment
    }

    override fun sendDataToSeccionUnoFragment(
        response: String?,
        indicadorFragment: IndicadorFragment,
        titulo: String,
        indexOfElement: Int
    ): SeccionUnoFragment {
        val bundle = Bundle()
        bundle.putString("response",response)
        bundle.putInt("indexOfElement",indexOfElement)
        val fragment = SeccionUnoFragment(indicadorFragment,titulo)
        fragment.arguments = bundle

        return fragment
    }

    override fun sendDataToSeccionUnoFragment(
        response: String?,
        responseFicha: String?,
        indicadorFragment: IndicadorFragment,
        titulo: String,
        indexOfElement: Int
    ): SeccionUnoFragment {
        val bundle = Bundle()
        bundle.putString("response",response)
        bundle.putString("ficha",responseFicha)
        bundle.putInt("indexOfElement",indexOfElement)
        val fragment = SeccionUnoFragment(indicadorFragment,titulo)
        fragment.arguments = bundle

        return fragment
    }

    override fun sendResponseToGraficaFragment(
        response: String?,
        responseFicha: String?,
        indicadorSeleccionado: IndicadorFragment
    ): GraficaFragment {
        val bundle = Bundle()
        bundle.putString("response",response)
        bundle.putString("ficha",responseFicha)
        val fragment = GraficaFragment(indicadorSeleccionado)
        fragment.arguments = bundle

        return fragment
    }

    override fun sendResponseToGraficaFragment(
        response: Map<String, String>,
        indicadorSeleccionado: IndicadorFragment
    ): GraficaFragment {
        val bundle = Bundle()
        response.forEach { (key, value) ->
            bundle.putString(key,value)
        }
        val fragment = GraficaFragment(indicadorSeleccionado)
        fragment.arguments = bundle

        return fragment
    }

    override fun sendFichaDataToFichaIndicadorModuloUno(
        ficha: String?,
        token: String?,
        numIdentificacionIndicador: Int,
        nombreFicha: String
    ): FichaIndicadorModuloUno {
        val bundle = Bundle()
        bundle.putString("ficha",ficha)
        bundle.putString("token",token)
        bundle.putInt("numIdentificacionIndicador",numIdentificacionIndicador)
        val fragment = FichaIndicadorModuloUno(nombreFicha)
        fragment.arguments = bundle

        return fragment
    }

    override fun sendFichaDataToFichaIndicadorModuloDos(
        ficha: String?,
        token: String?,
        numIdentificacionIndicador: Int,
        nombreFicha: String
    ): FichaIndicadorModuloDos {
        val bundle = Bundle()
        bundle.putString("ficha",ficha)
        bundle.putString("token",token)
        bundle.putInt("numIdentificacionIndicador",numIdentificacionIndicador)
        val fragment = FichaIndicadorModuloDos(nombreFicha)
        fragment.arguments = bundle

        return fragment
    }

    override fun sendFichaDataToFichaIndicadorModuloTres(
        ficha: String?,
        token: String?,
        numIdentificacionIndicador: Int,
        nombreFicha: String
    ): FichaIndicadorModuloTres {
        val bundle = Bundle()
        bundle.putString("ficha",ficha)
        bundle.putString("token",token)
        bundle.putInt("numIdentificacionIndicador",numIdentificacionIndicador)
        val fragment = FichaIndicadorModuloTres(nombreFicha)
        fragment.arguments = bundle

        return fragment
    }
}
package sep.dgesui.cmiapp

import androidx.fragment.app.Fragment
import sep.dgesui.cmiapp.filtros.*
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

interface Communicator {
    fun sendTokenToModuloIFragment(token:String?):ModuloUnoFragment
    fun sendTokenToModuloIIFragment(token: String?):ModuloDosFragment
    fun sendTokenToModuloIIIFragment(token: String?):ModuloTresFragment
    fun sendTokenToInicioFragment(token: String?):InicioFragment
    fun sendTokenToIndicadorFragment(token: String?,
                                     moduloOrigen:Fragment,
                                     indicadorModulo: IndicadorModulo):IndicadorFragment
    fun sendResponseToFiltroPeriodo(response: Array<String?>,
                                    selectedItemFiltroPeriodo: SelectedItemFiltroPeriodo):FiltroPeriodo
    fun sendResponseToFiltroEjercicioFiscal(response: String?,
                                            selectedItemFiltroEjercicioFiscal: SelectedItemFiltroEjercicioFiscal):FiltroEjercicioFiscal
    fun sendResponseToFiltroPeriodoModuloII(response: String?,
                                            responsePeriodo: String?,
                                            selectedItemFiltroPeriodoModuloDos: SelectedItemFiltroPeriodoModuloDos):FiltroPeriodoModuloDos
    fun sendResponseToTablaFragment(response: String?,
                                    indicadorOrigen:IndicadorFragment):TablaFragment
    fun sendResponseToTablaFragment(response: String?,responseFicha: String?,
                                    indicadorOrigen:IndicadorFragment):TablaFragment
    fun sendReponseToTablaFragment(response: Map<String,String>,
                                   token: String?,
                                   indicadorOrigen:IndicadorFragment):TablaFragment
    fun sendReponseToTablaFragment(response: Map<String,String>,
                                   indicadorOrigen:IndicadorFragment):TablaFragment
    fun sendResponseToSeccionValoresFragment(response: String?,
                                            indicadorFragment: IndicadorFragment,
                                            titulo: String):SeccionValoresFragment
    fun sendDataToSeccionDosFragment(response: Array<String?>,
                                     indicadorFragment: IndicadorFragment,
                                     titulo: String,
                                     token: String?,
                                     indexOfElement:Int?):SeccionDosFragment
    fun sendDataToSeccionValoresFragment(response: Map<String,String?>,
                                         indicadorFragment: IndicadorFragment,
                                         titulo: String,
                                         indexOfElement:Int?): SeccionValoresFragment
    fun sendDataToSeccionValoresFragment(response: String?,
                                         indicadorFragment: IndicadorFragment,
                                         titulo: String,
                                         indexOfElement:Int?):SeccionValoresFragment
    fun sendDataToSeccionValoresFragment(response: String?,
                                         responseFicha: String?,
                                         indicadorFragment: IndicadorFragment,
                                         titulo: String,
                                         indexOfElement:Int?):SeccionValoresFragment
    fun sendDataToSeccionValoresFragment(response: Array<String?>,
                                         seccionDosFragment: SeccionDosFragment,
                                         titulo: String,
                                         token: String?,
                                         indexOfElement:Int?,
                                         tabSeleccionado : String):SeccionValoresFragment
    fun sendDataToSeccionValoresFragment(response: Array<String?>,
                                             seccionUnoFragment: SeccionUnoFragment,
                                             titulo: String,
                                             token: String?,
                                             indexOfElement:Int?):SeccionValoresFragment
    fun sendDataToSeccionUnoFragment(response: Array<String?>,
                                         indicadorFragment: IndicadorFragment,
                                         titulo: String,
                                         token: String?,
                                         indexOfElement:Int):SeccionUnoFragment
    fun sendDataToSeccionUnoFragment(response: String?,
                                     indicadorFragment: IndicadorFragment,
                                     titulo: String,
                                     indexOfElement:Int):SeccionUnoFragment
    fun sendDataToSeccionUnoFragment(response: String?,
                                     responseFicha: String?,
                                     indicadorFragment: IndicadorFragment,
                                     titulo: String,
                                     indexOfElement:Int):SeccionUnoFragment
    fun sendResponseToGraficaFragment(response:String?, responseFicha:String?,
                                      indicadorSeleccionado: IndicadorFragment):GraficaFragment
    fun sendResponseToGraficaFragment(response: Map<String,String>,
                                      indicadorSeleccionado: IndicadorFragment):GraficaFragment
    fun sendFichaDataToFichaIndicadorModuloUno(ficha:String?,
                                               token: String?,
                                               numIdentificacionIndicador:Int,
                                               nombreFicha:String): FichaIndicadorModuloUno
    fun sendFichaDataToFichaIndicadorModuloDos(ficha:String?,
                                               token: String?,
                                               numIdentificacionIndicador:Int,
                                               nombreFicha:String):FichaIndicadorModuloDos
    fun sendFichaDataToFichaIndicadorModuloTres(ficha:String?,
                                                token: String?,
                                                numIdentificacionIndicador: Int,
                                                nombreFicha: String): FichaIndicadorModuloTres
}
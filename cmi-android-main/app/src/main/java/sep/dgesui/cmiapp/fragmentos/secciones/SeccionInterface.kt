package sep.dgesui.cmiapp.fragmentos.secciones

import androidx.fragment.app.Fragment
import sep.dgesui.cmiapp.fragmentos.indicador.IndicadorFragment

interface SeccionInterface {
    fun indicadorOrigen():IndicadorFragment
    fun instancia():Fragment
}
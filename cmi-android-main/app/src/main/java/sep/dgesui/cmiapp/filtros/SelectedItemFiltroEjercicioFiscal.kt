package sep.dgesui.cmiapp.filtros

interface SelectedItemFiltroEjercicioFiscal {
    fun anioSeleccionado(anio:String)
    fun subsistemaSeleccionado(subsistema:String)
    fun entidadFederativaSeleccionada(entidadFederativa:String)
    fun universidadSeleccionada(universidad:String)
    fun filtroAplicado(aplicado:Boolean)
}
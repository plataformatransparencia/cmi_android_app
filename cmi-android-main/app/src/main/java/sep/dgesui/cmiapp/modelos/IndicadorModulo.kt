package sep.dgesui.cmiapp.modelos

interface IndicadorModulo {
    fun numIdentificacion():Int
    fun nombre():String
    fun enlaces():Array<String>
    fun enlaceFicha():String
    fun enlaceExcel():String
    fun nombreArchivo():String
}
package sep.dgesui.cmiapp.modelos

class IndicadorModuloUno(private val numIdentificacion:Int,
                              private val nombre:String,
                              private val enlace:String,
                              private val enlaceFicha:String,
                              private val enlaceExcel:String,
                              private val nombreArchivo:String,
                              private var periodoParam:String) : IndicadorModulo{

    private var anteriorPeriodo : String? = null

    override fun numIdentificacion(): Int = numIdentificacion
    override fun nombre(): String = nombre+" ("+periodoParam+")"
    override fun enlaces(): Array<String> = arrayOf(enlace.plus(periodoParam))
    override fun enlaceFicha(): String = enlaceFicha
    override fun enlaceExcel(): String = enlaceExcel.plus(periodoParam).plus(".csv?enc=iso-8859-1")
    override fun nombreArchivo(): String = nombreArchivo

    fun anteriorPeriodo(anteriorPeriodo:String){
        this.anteriorPeriodo = anteriorPeriodo
    }
    fun anteriorPeriodo():String? = anteriorPeriodo
    fun periodoParam(periodoParam: String){
        this.periodoParam = periodoParam
    }
    fun periodoParam():String = periodoParam
}
package sep.dgesui.cmiapp.modelos

class IndicadorModuloTres(private val numIdentificacion:Int,
                               private val nombre:String,
                               private val enlaces:Array<String>,
                               private val valorRaiz: ValorRaiz,
                               private val enlaceFicha:String,
                               private val enlaceExcel:String,
                               private val enlaceFiltro:String,
                               private val nombreArchivo:String) : IndicadorModulo {

    private var anioParam : String? = null
    private var universidadParam : String? = null
    private var subsistemaParam : String? = null
    private var entidadFederativaParam : String? = null

    override fun numIdentificacion(): Int = numIdentificacion
    override fun nombre(): String = nombre
    override fun enlaces(): Array<String> {
        val enlacesList = mutableListOf<String>()
        enlacesList.add(enlaces[0].plus(anioParam)
            .plus("?entidadFederativa="+entidadFederativaParam)
            .plus("&universidad="+universidadParam)
            .plus("&subsistema="+subsistemaParam))
        enlacesList.add(enlaces[1].plus(anioParam)
            .plus("/graficas")
            .plus("?entidadFederativa="+entidadFederativaParam)
            .plus("&universidad="+universidadParam)
            .plus("&subsistema="+subsistemaParam))
        if (enlaces.size>=3){
            enlacesList.add(enlaces[2]
                .plus(anioParam)
                .plus("/totales"))
        }
        return enlacesList.toTypedArray()
    }
    override fun enlaceFicha(): String = enlaceFicha
    override fun enlaceExcel(): String = enlaceExcel+anioParam+".csv?universidad="+universidadParam+"&subsistema="+subsistemaParam+"&entidadFederativa="+entidadFederativaParam+"&enc=iso-8859-1"
    override fun nombreArchivo(): String = nombreArchivo


    fun enlaceFiltro():String = enlaceFiltro
    fun valorRaiz():String = valorRaiz.valor
    fun anioParam(anioParam:String){
        this.anioParam = anioParam
    }
    fun anioParam(): String? = anioParam
    fun universidadParam(universidadParam:String){
        this.universidadParam = universidadParam
    }
    fun universidadParam(): String? = universidadParam
    fun subsistemaParam(subsistemaParam:String){
        this.subsistemaParam = subsistemaParam
    }
    fun subsistemaParam(): String? = subsistemaParam
    fun entidadFederativaParam(entidadFederativaParam:String){
        this.entidadFederativaParam = entidadFederativaParam
    }
    fun entidadFederativaParam(): String? = entidadFederativaParam
}

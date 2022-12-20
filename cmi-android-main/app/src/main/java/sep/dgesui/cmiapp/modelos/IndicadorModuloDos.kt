package sep.dgesui.cmiapp.modelos

import android.util.Log

class IndicadorModuloDos(
    private val numIdentificacion: Int,
    private val nombre: String,
    private val enlaces: Array<String>,
    private val enlaceFicha: String,
    private val enlaceExcel: String,
    private val enlaceFiltro: String,
    private val nombreArchivo: String
) : IndicadorModulo {

    private var anioParam: String? = null
    private var universidadParam: String? = null
    private var subsistemaParam: String? = null
    private var entidadFederativaParam: String? = null
    private var periodoParam: String? = null

    companion object {
        lateinit var ROOT_URL: String
    }

    init {
        ROOT_URL = "https://dgesui.ses.sep.gob.mx/cmi/webservice"
    }

    override fun numIdentificacion(): Int = numIdentificacion
    override fun nombre(): String = nombre
    override fun enlaces(): Array<String> {
        val enlacesList = mutableListOf<String>()
        when (enlaces.size) {
            1 -> {
                when (numIdentificacion) {
                    1 -> {
                        enlacesList.add(
                            enlaces[0]
                                .plus(periodoParam)
                                .plus("?entidadFederativa=" + entidadFederativaParam)
                        )
                    }
                    2 -> {
                        enlacesList.add(
                            enlaces[0]
                                .plus(periodoParam)
                                .plus("?entidadFederativa=" + entidadFederativaParam)
                                .plus("&universidad=" + universidadParam)
                        )
                    }
                    3 -> {
                        enlacesList.add(
                            enlaces[0]
                                .plus(periodoParam)
                                .plus("?entidadFederativa=" + entidadFederativaParam)
                                .plus("&universidad=" + universidadParam)
                                .plus("&subsistema=" + subsistemaParam)
                        )
                    }
                    4 -> {
                        enlacesList.add(
                            enlaces[0]
                                .plus(anioParam)
                        )
                    }
                    6, 15, 16 -> {
                        enlacesList.add(
                            enlaces[0]
                                .plus(anioParam)
                                .plus("?entidadFederativa=" + entidadFederativaParam)
                                .plus("&universidad=" + universidadParam)
                                .plus("&subsistema=" + subsistemaParam)
                        )
                    }
                }
            }
            2 -> {
                enlacesList.add(
                    enlaces[0]
                        .plus(anioParam)
                        .plus("?entidadFederativa=" + entidadFederativaParam)
                        .plus("&universidad=" + universidadParam)
                        .plus("&subsistema=" + subsistemaParam)
                )
                enlacesList.add(
                    enlaces[1].plus(anioParam).plus("/graficas")
                        .plus("?entidadFederativa=" + entidadFederativaParam)
                        .plus("&universidad=" + universidadParam)
                        .plus("&subsistema=" + subsistemaParam)
                )
            }
            4 -> {
                enlacesList.add(
                    enlaces[0].plus(anioParam)
                        .plus("?entidadFederativa=" + entidadFederativaParam)
                        .plus("&universidad=" + universidadParam)
                        .plus("&subsistema=" + subsistemaParam)
                )
                enlacesList.add(
                    enlaces[1].plus(anioParam).plus("/graficas")
                        .plus("?entidadFederativa=" + entidadFederativaParam)
                        .plus("&universidad=" + universidadParam)
                        .plus("&subsistema=" + subsistemaParam)
                )
                enlacesList.add(
                    enlaces[2].plus(anioParam)
                        .plus("?entidadFederativa=" + entidadFederativaParam)
                        .plus("&universidad=" + universidadParam)
                        .plus("&subsistema=" + subsistemaParam)
                )
                enlacesList.add(
                    enlaces[3].plus(anioParam).plus("/graficas")
                        .plus("?entidadFederativa=" + entidadFederativaParam)
                        .plus("&universidad=" + universidadParam)
                        .plus("&subsistema=" + subsistemaParam)
                )
            }
        }
        return enlacesList.toTypedArray()
    }

    override fun enlaceFicha(): String = enlaceFicha
    override fun enlaceExcel(): String {
        val enlace: StringBuilder = StringBuilder(enlaceExcel)
        when (numIdentificacion) {
            1 -> {
                enlace.append(periodoParam)
                    .append(".csv?entidadFederativa=")
                    .append(entidadFederativaParam + "&enc=iso-8859-1")
            }
            2 -> {
                enlace.append(periodoParam)
                    .append(".csv?entidadFederativa=")
                    .append(entidadFederativaParam)
                    .append("&universidad=")
                    .append(universidadParam + "&enc=iso-8859-1")
            }
            3 -> {
                enlace.append(periodoParam)
                    .append(".csv?subsistema=")
                    .append(subsistemaParam)
                    .append("&entidadFederativa=")
                    .append(entidadFederativaParam)
                    .append("&universidad=")
                    .append(universidadParam + "&enc=iso-8859-1")
            }
            4 -> {
                enlace.append(anioParam + ".csv?enc=iso-8859-1")
            }
            5, 6, 7, 8, 9, 10, 11, 13, 14, 15, 16 -> {
                enlace.append(anioParam)
                    .append(".csv?subsistema=")
                    .append(subsistemaParam)
                    .append("&entidadFederativa=")
                    .append(entidadFederativaParam)
                    .append("&universidad=")
                    .append(universidadParam + "&enc=iso-8859-1")
            }
            12 -> {
                enlace.append(anioParam)
                    .append(".csv?entidadFederativa=")
                    .append(entidadFederativaParam)
                    .append("&universidad=")
                    .append(universidadParam + "&enc=iso-8859-1")
            }

        }
        return enlace.toString()
    }

    override fun nombreArchivo(): String = nombreArchivo

    fun enlaceFiltro() = enlaceFiltro

    fun anioParam(anioParam: String) {
        this.anioParam = anioParam
    }

    fun anioParam(): String? = anioParam
    fun universidadParam(universidadParam: String) {
        this.universidadParam = universidadParam
    }

    fun universidadParam(): String? = universidadParam
    fun subsistemaParam(subsistemaParam: String) {
        this.subsistemaParam = subsistemaParam
    }

    fun subsistemaParam(): String? = subsistemaParam
    fun entidadFederativaParam(entidadFederativaParam: String) {
        this.entidadFederativaParam = entidadFederativaParam
    }

    fun entidadFederativaParam(): String? = entidadFederativaParam
    fun periodoParam(periodoParam: String) {
        this.periodoParam = periodoParam
    }

    fun periodoParam(): String? = periodoParam
    fun enlaceFiltroPeriodo(): String? {
        when (numIdentificacion) {
            1 -> return ROOT_URL + "/tasa-bruta-escolarizada/periodos"
            2 -> return ROOT_URL + "/tasa-bruta-escolarizada-cobertura/periodos"
            3 -> return ROOT_URL + "/tasa-bruta-escolarizacion-ies/periodos"
        }
        return null
    }
}
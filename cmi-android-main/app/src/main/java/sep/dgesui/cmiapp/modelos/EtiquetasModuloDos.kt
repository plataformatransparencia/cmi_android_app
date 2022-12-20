package sep.dgesui.cmiapp.modelos

class EtiquetasModuloDos {
    companion object {
        val IND_1_TOTAL = arrayOf("Matrícula Total (incluye TSU, Lic de escolarizado y no escolarizado)",
            "Población Total 18 a 22 años (censo 2020)",
            "Tasa Bruta de Escolarización (Cobertura) de la Población Total")
        val IND_1_MUJERES = arrayOf("Matrícula de Mujeres (incluye TSU, Lic de escolarizado y no escolarizado)",
            "Mujeres 18 a 22 años (censo 2020)",
            "Tasa Bruta de Escolarización de la Población de Mujeres")
        val IND_1_HOMBRES = arrayOf("Matrícula de Hombres (incluye TSU, Lic de escolarizado y no escolarizado)",
            "Hombres 18 a 22 años (censo 2020)",
            "Tasa Bruta de Escolarización de la Población de Hombres")
        val IND_2_TOTAL = arrayOf("Contribución de las Instituciones de los Subsistemas de la DGESUI al Programa Sectorial de Educación 2020-2024")
        val IND_2_MUJERES = arrayOf("Contribución de las Instituciones de los Subsistemas de la DGESUI al Programa Sectorial de Educación 2020-2024")
        val IND_2_HOMBRES = arrayOf("Contribución de las Instituciones de los Subsistemas de la DGESUI al Programa Sectorial de Educación 2020-2024")
        val IND_3_TASA_BRUTA_ESCOLARIZACION_DGESUI = arrayOf("Tecnico Superior Universitario (TSU)",
            "Licenciatura","Matrícula Total (incluye TSU, Lic de escolarizado y no escolarizado)",
            "Contribución de las Instituciones de los Subsistemas de la DGESUI al Programa Sectorial de Educación 2020-2024 (Total)")
        val IND_3_TASA_BRUTA_ESCOLARIZACION_MUJERES = arrayOf("Matrícula de Mujeres (incluye TSU, Lic de escolarizado y no escolarizado)",
            "Contribución de las Instituciones de los Subsistemas de la DGESUI al Programa Sectorial de Educación 2020-2024 (Mujeres)")
        val IND_3_TASA_BRUTA_ESCOLARIZACION_HOMBRES = arrayOf("Matrícula de Hombres (incluye TSU, Lic de escolarizado y no escolarizado)",
            "Contribución de las Instituciones de los Subsistemas de la DGESUI al Programa Sectorial de Educación 2020-2024 (Hombres)")
        val IND_3_TASA_BRUTA_ESCOLARIZACION_DISCAPACIDAD = arrayOf("Estudiantes con Discapacidad",
            "Tasa bruta de Escolarización de la Población Personas con Discapacidad")
        val IND_3_TASA_BRUTA_ESCOLARIZACION_HLI = arrayOf("Estudiantes Hablantes de Lengua Indígena",
            "Tasa Bruta de Escolarización de la Población Personas Hablantes de Lengua Indígena")
        val IND_3_PORCENTAJE_MODALIDAD = arrayOf("Estudiantes Escolarizado","Porcentaje de Estudiantes en la Modalidad de Escolarizado",
            "Estudiantes No Escolarizado","Porcentaje de Estudiantes en la Modalidad No Escolarizado",
            "Estudiantes Modalidad Mixta","Porcentaje de Estudiantes Modalidad Mixta")
        val IND_4 = arrayOf("Año","# de documentos normativos programados")
        val IND_5_DESGLOSE_PROFESORES_TIEMPO_COMPLETO = arrayOf("Total de PTC","PTC Licenciatura","PTC con Maestría",
            "PTC con Doctorado","PTC con Posgrado")
        val IND_5_DESGLOSE_PROFESORES_PERFIL_DESEABLE_SNI = arrayOf("Con Perfil Deseable Vigente","SNIC",
            "SNI1","SNI2","SNI3","Total SNI Vigente**")
        val IND_5_PORCENTAJE_PTC_PERFIL_DESEABLE = arrayOf("PTC con Perfil Deseable(%)")
        val IND_6_DESGLOSE_CUERPOS_ACADEMICOS_FORMACION = arrayOf("Ciencias Agropecuarias","Ciencias Sociales Y Administrativas",
            "Ciencias de la Salud","Ciencias Ingeniería Y Tecnología","Ciencias Naturalez Y Exactas",
            "Ciencias Educación, Humanidades Y Arte")
        val IND_6_DESGLOSE_CUERPOS_ACADEMICOS_CONSOLIDACION = arrayOf("Ciencias Agropecuarias","Ciencias Sociales Y Administrativas",
            "Ciencias de la Salud","Ciencias Ingeniería Y Tecnología","Ciencias Naturalez Y Exactas",
            "Ciencias Educación, Humanidades Y Arte")
        val IND_6_DESGLOSE_CUERPOS_ACADEMICOS_CONSOLIDADOS = arrayOf("Ciencias Agropecuarias","Ciencias Sociales Y Administrativas",
            "Ciencias de la Salud","Ciencias Ingeniería Y Tecnología","Ciencias Naturalez Y Exactas",
            "Ciencias Educación, Humanidades Y Arte")
        val IND_6_CUERPOS_ACADEMICOS = arrayOf("Total CAEF","Total CAEC","Total CAC","Total CA")
        val IND_6_PORCENTAJE_CAEF_CA = arrayOf("% Ciencias Agropecuarias CAEF/CA","% Ciencias Ciencias Sociales Y Administrativas CAEF/CA",
            "% Ciencias de la Salud CAEF/CA","% Ciencias Ingeniería Y Tecnología CAEF/CA",
            "% Ciencias Naturalez Y Exactas CAEF/CA","% Ciencias Educación, Humanidades Y Arte CAEF/CA",
            "% Total CAEF/ Total CA")
        val IND_6_PORCENTAJE_CAEC_CA = arrayOf("% Ciencias Agropecuarias CAEC/CA","% Ciencias Ciencias Sociales Y Administrativas CAEC/CA",
            "% Ciencias de la Salud CAEC/CA","% Ciencias Ingeniería Y Tecnología CAEC/CA",
            "% Ciencias Naturalez Y Exactas CAEC/CA","% Ciencias Educación, Humanidades Y Arte CAEC/CA",
            "% Total CAEC/ Total CA")
        val IND_6_PORCENTAJE_CAC_CA = arrayOf("% Ciencias Agropecuarias CAC/CA","% Ciencias Ciencias Sociales Y Administrativas CAC/CA",
            "% Ciencias de la Salud CAC/CA","% Ciencias Ingeniería Y Tecnología CAC/CA",
            "% Ciencias Naturalez Y Exactas CAC/CA","% Ciencias Educación, Humanidades Y Arte CAC/CA",
            "% Total CAC/ Total CA")
        val IND_7 = arrayOf("Otorgado","Solicitado",
            "Porcentaje de Reconocimientos al Perfil Deseable Otorgados a Profesores de Tiempo Completo de Instituciones Públicas de Educación Superior")
        val IND_8 = arrayOf("Otorgado","Solicitado",
            "Porcentaje de Solicitudes de Apoyos para Estudios de Posgrado Aprobadas")
        val IND_9 = arrayOf("Otorgado","Solicitado",
            "Porcentaje de Apoyos en IES para la Incorporación de Nuevos Profesores de Tiempo Completo y la Reincorporación de Exbecarios Otorgados")
        val IND_10 = arrayOf("Evaluados","Suben de Grado",
            "Porcentaje de Cuerpos Académicos en las IES que Cambian a un Grado de Consolidación Superior por Año")
        val IND_11_CUERPOS_ACADEMICOS = arrayOf("Otorgado","Solicitado","Porcentaje solicitudes aprobadas")
        val IND_11_SUBSISTEMA = arrayOf("Otorgado","Solicitado","Porcentaje solicitudes aprobadas")
        val IND_12_INDICE_ABSORCION = arrayOf("Total EMS egresados y público particular","EMS Egresados (Público)",
            "EMS Egresados (Público federal)","EMS Egresados (Público estatal)","EMS Egresados (Público autónomo)",
            "EMS Egresados (particular)","Total Primer Ingreso Público y Particular","Primer Ingreso (Público) (TSU y Lic)",
            "Primer Ingreso (Paticular) (TSU y Lic)","Total Índice de Absorción (%)")
        val IND_13_MONTO_PROMEDIO_ALUMNO = arrayOf("Monto Federal","Monto Estatal","Monto Público",
            "% Aportación Federal","% Aportación Estatal","Matrícula ESU","Matrícula Media Superior",
            "Matrícula Total","Matrícula Ponderada","Subsidio Federal por Estudiante","Subsidio Estatal Por Estudiante",
            "Subsidio por Estudiante","Porcentaje del Monto Público que se Asigna a cada Estudiante")
        val IND_14_MONTO_PROMEDIO_INSTITUCIONES = arrayOf("Monto Federal","Monto Estatal","Monto Público",
            "% Aportación Federal","% Aportación Estatal")
        val IND_15 = arrayOf("Año",
            "Centros y Organizaciones Vinculados a la Educación Susceptibles de Apoyo, los Cuales Contaron con Recursos para Realizar sus Actividades")
        val IND_16 = arrayOf("Año",
            "Apoyos Otorgados a Centros y Organizaciones de Educación que Participan en el Fondo")
    }
}
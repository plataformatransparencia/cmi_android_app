package sep.dgesui.cmiapp.modelos

class EtiquetasModuloTres {
    companion object{
        val IND_1_INSTRUMENTO = arrayOf("Instrumento","Estatus")
        val IND_1_MONTOS_ASIGNADOS = arrayOf("Monto federal asignado","Monto estatal asignado",
            "Monto público asignado","Porcentaje federal","Porcentaje estatal")
        val IND_1_MATRICULA = arrayOf("Superior universitaria","Matrícula media superior",
            "Matrícula total","Matrícula total ponderada")
        val IND_1_SUBSIDIO_POR_ALUMNO = arrayOf("Subsidio federal por estudiante","Subsidio estatal por estudiante",
            "Subsidio público por estudiante")
        val IND_2_CALENDARIZADO = arrayOf("Total Aportación calendarizada","Total Aportación comprobada CLC",
            "Aportación calendarizada","Aportación comprobada CLC",
            "Número de CLC","Fecha de pago CLC","Subtotal Aportación calendarizada",
            "Subtotal Aportación comprobada CLC")
        val IND_2_REPORTADO_UNIVERSIDAD = arrayOf("Total Aportación reportada por la universidad",
            "Aportación reportada por la universidad",
            "Número de transferencia","Fecha de pago","Subtotal Aportación reportada por la universidad")
        val IND_2_SEGUN_PLATAFORMA = arrayOf("Total Aportación según plataforma","Total Adeudos mensuales",
            "Aportación según plataforma",
            "Observación según portal de transparencia", "Adeudos mensuales")
        val IND_3_CALENDARIZADO = arrayOf("Total Aportación calendarizada","Total Aportación reportada en el sistema",
            "Aportación calendarizada", "Aportación reportada en el sistema", "Número de transferencia",
            "Fecha de pago","Subtotal Aportación calendarizada","Subtotal Aportación reportada en el sistema")
        val IND_3_SEGUN_PLATAFORMA = arrayOf("Total Aportación según plataforma","Total Adeudos mensuales",
            "Aportación según plataforma","Observación según portal de transparencia",
            "Adeudos mensuales","Subtotal Aportación según plataforma","Subtotal Adeudos mensuales")
        val IND_4_MONTO_PUBLICO = arrayOf("Recursos Asignados para las Universidades en Crisis",
            "Estatus de Convenio Universidades en Crisis", "Monto Ministrado. Universidades en Crisis",
            "Total Recursos Asignados para las Universidades en Crisis","Total Monto Ministrado. Universidades en Crisis")
        val IND_4_APORTACION_FEDERAL = arrayOf("Monto de convenio. Aportación SEP","Monto ministrado. Aportación SEP",
            "Fecha de ejecución","Observación","Total Monto de convenio. Aportación SEP","Total Monto ministrado. Aportación SEP")
        val IND_4_APORTACION_ESTATAL = arrayOf("Monto. Aportación estado","Monto ministrado. Aportación estatal",
            "Fecha de ejecución","Observación","Total Monto. Aportación estado","Total Monto ministrado. Aportación estatal")
        val IND_5_PRODEP_S247 = arrayOf("Total Monto Federal","Instrumento","Estatus","Fecha","Monto Federal")
        val IND_6_RECURSO_EXTRAORDINARIO = arrayOf("Monto","Instrumento","Fecha","Estatus","Total Monto")
        val IND_6_REGRESO_CLASES = arrayOf("Monto","Instrumento","Estatus","Fecha de Firma de Convenio","Total Monto")
        val IND_6_INCLUSION_ESTANCIAS_U006 = arrayOf("Monto","Instrumento","Estatus","Fecha de Firma de Convenio","Total Monto")
        val IND_6_INCREMENTO_SALARIAL_U006 = arrayOf("Instrumento","Estatus","Información","Monto Federal",
            "CLC","Monto Estatal","Monto Público","Total Monto Federal")
        val IND_6_DEFICIT_U006 = arrayOf("Monto","Instrumento","Estatus","Fecha de Firma de Convenio","Monto Estatal",
            "Monto Público","Total Monto")
        val IND_7_RECURSOS_ASIGNADOS = arrayOf("Instrumento","Estatus de Convenio",
            "Fecha de Formalización","Recursos Asignados","Observación Específica","Total")
        val IND_8_COBERTURA = arrayOf("Ciclo de datos","Población (18-22) (Hombre)","Población (18-22) (Mujer)",
            "Población (18-22) (Total)","% Cobertura (Hombre)","% Cobertura (Mujer)","Tasa bruta cobertura")
        val IND_8_COBERTURA_2012_2013 = arrayOf("Ciclo de datos","Población (18-22) (Hombre)","Población (18-22) (Mujer)",
            "Población (18-22) (Total)","Tasa bruta cobertura")
        val IND_8_MATRICULA_NIVEL_SUPERIOR = arrayOf("Ciclo de Datos","Matrícula Lic","Matrícula Total TSU y Lic",
            "Matrícula TSU y Lic Hombres","Matrícula TSU y Lic Mujeres")
        val IND_8_MATRICULA_POSGRADO = arrayOf("Ciclo de datos","Matrícula Especialidad","Matrícula Maestría",
            "Matrícula Doctorado","Total Matrícula Posgrado","Matrícula Posgrado Hombres","Matrícula Posgrado Mujeres")
        val IND_8_MATRICULA_NIVEL_MEDIO_SUPERIOR = arrayOf("Ciclo de datos","Total EMS Egresados Público y Particular",
            "EMS Egresados (Público)","EMS Egresados (Público Federal)","EMS Egresados (Público Estatal)",
            "EMS Egresados (Público Autónomo)", "EMS Egresados (Particular)","Total 1er Ingreso Público y Particular",
            "1er Ingreso (Público) (TSU y Lic)", "1er Ingreso (Particular) (TSU y Lic)","Total Indice de absorción %")
        val IND_8_DESGLOSE_IES = arrayOf("Ciclo de datos","IES en el Estado Públicas y Particulares","UPE en el Estado",
            "UPEA en el Estado","UI en el Estado")
        val IND_8_MATRICULA_ES_MODALIDAD = arrayOf("Ciclo de datos","Matrícula Escol (TSU, Lic y Pos)",
            "Matrícula No Escol (TSU, Lic y Pos)","Matrícula Mixta (TSU, Lic y Pos)")
        val IND_8_PTC_PERFIL_SNI = arrayOf("Ciclo de datos","Total de PTC","Con Perfil Deseable Vigente","Con SNI Vigente")
        val IND_8_MATRICULA_DISCAPACIDAD_ES = arrayOf("Ciclo de datos","Matrícula (Discapacidad TSU, Lic y Pos)",
            "Hombres","Mujeres")
        val IND_8_MATRICULA_HLI_ES = arrayOf("Ciclo de datos","Matrícula HLI TSU, Lic y Pos","Hombres","Mujeres")
        val IND_8_MATRICULA_AREAS_CMPE = arrayOf("Ciclo de datos","E","AH","CSD","AN","CNME","TIC","IMC","AV","CS",
            "Servicios","Total CMPE")
        val IND_8_MATRICULA_BUENA_CALIDAD_NORMAL_TSU_LIC = arrayOf("Ciclo de datos","Matrícula Calidad","Matrícula Total",
            "Matrícula Evaluable", "% de Cobertura Total de Calidad","% de Cobertura Evaluable")
        val IND_8_MATRICULA_SUBSISTEMA_ES = arrayOf("Ciclo de datos","UPE","UPEAS","UI","UPF","UT","UPOL",
            "IT","IESEF","N","UPN","CAM","P","Total")
        val IND_9_TOTAL_MATRICULA_NIVEL_EDUCATIVO = arrayOf("Total TSU/PA","Total Lic","Total TSU/LIC","Total Especialidad",
            "Total Maestría", "Total Doctorado","Total posgrado", "Total (TSU,Lic,Posg)",
            "Total Hombres ES","Total Mujeres ES","Total ES")
        val IND_9_MATRICULA_NIVEL_EDUCATIVO = arrayOf("TSU/PA","Lic","Total TSU/LIC","Especialidad","Maestría",
            "Doctorado","Total posgrado", "Total (TSU,Lic,Posg)","Hombres ES","Mujeres ES","Total ES")
        val IND_9_TOTAL_DISCAPACIDAD_ES_TOTAL = arrayOf("Total Hombres","Total Mujeres")
        val IND_9_TOTAL_DISCAPACIDAD_ES = arrayOf("Hombres","Mujeres")
        val IND_9_TOTAL_HLI_ES_TOTAL = arrayOf("Total Hombres","Total Mujeres")
        val IND_9_TOTAL_HLI_ES = arrayOf("Hombres","Mujeres")
        val IND_9_TOTAL_EGRESADOS_ES = arrayOf("Total Hombres","Total Mujeres","Total Discapacidad","Total HLI")
        val IND_9_EGRESADOS_ES = arrayOf("Hombres","Mujeres","Total Discapacidad","Total HLI")
        val IND_9_TOTAL_TITULADOS_ES = arrayOf("Total Hombres","Total Mujeres","Total Discapacidad","Total HLI")
        val IND_9_TITULADOS_ES = arrayOf("Hombres","Mujeres","Total Discapacidad","Total HLI")
        val IND_9_TOTAL_MATRICULA_MODALIDAD = arrayOf("Total Escolarizada","Total No escolarizada","Total Mixta")
        val IND_9_MATRICULA_MODALIDAD = arrayOf("Escolarizada","No escolarizada","Mixta")
        val IND_9_TOTAL_MATRICULA_PROGRAMA = arrayOf("Total TSU/PA","Total Licenciatura","Total Especialidad",
            "Total Maestría","Total Doctorado", "Total Hombres","Total Mujeres")
        val IND_9_MATRICULA_PROGRAMA = arrayOf("TSU/PA","Licenciatura","Especialidad","Maestría","Doctorado",
            "Total Hombres","Total Mujeres")
        val IND_9_TOTAL_PROGRAMAS_TOTAL = arrayOf("Total TSU/PA","Total Licenciatura","Total Especialidad",
            "Total Maestría","Total Doctorado")
        val IND_9_TOTAL_PROGRAMAS = arrayOf("TSU/PA","Licenciatura","Especialidad","Maestría","Doctorado")
        val IND_9_TOTAL_MATRICULA_AREA_CMPE = arrayOf("Total Educación","Total AH","Total CSD","Total AN","Total CNME",
            "Total TIC","Total IMC","Total AV","Total CS","Total Servicios","Total Matrícula ES")
        val IND_9_MATRICULA_AREA_CMPE = arrayOf("Educación","AH","CSD","AN","CNME","TIC","IMC","AV","CS",
            "Servicios","Total Matrícula ES")
        val IND_10_BACHILLERATO_PROFESIONAL_TECNICO = arrayOf("Ciclo de datos de matrícula","Bachillerato Tecnológico",
            "Profesional Técnico","Subtotal Matrícula","Subtotal Matrícula (Bachillerato Tecnológico+Profesional Técnico)",
            "Total Matrícula EMS")
        val IND_10_EMS = arrayOf("Ciclo de datos de matrícula","Hombres","Mujeres","Total","Discapacidad","HLI")
        val IND_10_ES_NIVEL_EDUCATIVO = arrayOf("Ciclo de datos de matrícula","TSU/PA","Licenciatura","Especialidad",
            "Maestría","Doctorado","TOTAL MATRÍCULA ES")
        val IND_10_ES_MODALIDAD_HOMBRES_MUJERES = arrayOf("Ciclo de datos de matrícula","Hombres ES","Mujeres ES",
            "Escolarizada", "No escolarizada","Mixta","Total EMS y ES","Matrícula total ponderada")
        val IND_10_AREAS_CMPE = arrayOf("Ciclo de datos de matrícula","E","AH","CSD","AN","CNME","TIC","IMC","AV",
            "CS","S","Total ES")
        val IND_10_OFERTA_EDUCATIVA_CMPE = arrayOf("Ciclo de datos de matrícula","E","AH","CSD","AN","CNME","TIC",
            "IMC","AV","CS","S", "Total de Oferta Educativa (TSU, Lic y Posg)","Número de programas educativos PNPC")
        val IND_10_ALUMNOS_TSU_LIC = arrayOf("Ciclo de datos de matrícula","Matrícula T.S.U.",
            "Matrícula Licenciatura", "Total Matrícula TSU y Licenciatura","Hombres","Mujeres")
        val IND_10_ALUMNOS_POSGRADO = arrayOf("Ciclo de datos de matrícula","Matrícula Especialidad",
            "Matrícula Maestría", "Matrícula Doctorado","Total Matrícula posgrado","Hombres","Mujeres")
        val IND_10_ALUMNOS_DISCAPACIDAD_TSU_LIC_POSG = arrayOf("Ciclo de datos de matrícula",
            "Matrícula (Con discapacidad) (TSU y Lic)",
            "Matrícula (Con discapacidad) (Posg)","Total Matrícula (Con discapacidad) (TSU, Lic y Posg)","Hombres",
            "Mujeres")
        val IND_10_ALUMNOS_HLI_TSU_LIC_POSG = arrayOf("Ciclo de datos de matrícula",
            "Matrícula (Hablante de lengua indígena)(TSU y Lic)",
            "Matrícula (Hablante de lengua indígena) (Posg)",
            "Total Matrícula (Hablante de lengua indígena) (TSU, Lic y Posg)",
            "Hombres","Mujeres","TOTAL")
        val IND_10_ALUMNOS_TITULADOS = arrayOf("Ciclo de datos de matrícula","Hombres","Mujeres",
            "Total con discapacidad", "Total de Hablantes de Lenguas Indígenas")
        val IND_10_ALUMNOS_NUEVO_INGRESO_TSU_LIC_POSG = arrayOf("Ciclo de Datos de Matrícula",
            "Estudiantes Nuevo Ingreso (TSU y Lic)", "Estudiantes Nuevo Ingreso (Posg)",
            "Total Estudiantes Nuevo Ingreso (TSU, Lic y Posg)")
        val IND_10_ALUMNOS_EGRESADOS_TSU_LIC_POSG = arrayOf("Ciclo de datos de matrícula",
            "Estudiantes egresados (TSU y Lic)", "Estudiantes egresados (Posg)","Estudiantes egresados (TSU, Lic y Posg)",
            "Hombres","Mujeres","Total con discapacidad", "Total HLI")
        val IND_10_OFERTA_EDUCATIVA_NE_TP = arrayOf("Ciclo de datos de matrícula",
            "Oferta Educativa (TSU y Licenciatura)", "Oferta Educativa (Programas Posgrado)",
            "Total de Oferta Educativa (activos TSU, Lic y Posg)",
            "Escolarizados (TSU, Lic y Posg)","Mixto (TSU, Lic y Posg)","No Escolarizados (TSU, Lic y Posg)")
        val IND_10_PLANTILLA_ADMINISTRATIVO = arrayOf("Ciclo de datos de matrícula",
            "Plantilla (personal administrativo)","Plantilla (Mandos medios y/o superiores)",
            "Plantilla (Docente Investigador, Investigadores, Aux. Investigador)",
            "Otros (choferes, personal de limpieza, servicios generales, etc)")
        val IND_10_PERSONAL_DOCENTE = arrayOf("Ciclo de datos de matrícula","Personal Docente PTC",
            "Personal Docente PTCT", "Personal Docente MT","Personal Docente PH",
            "Total Personal Docente (PTC, PTCT, MT,PH)")
        val IND_10_PROGRAMAS_BUENA_CALIDAD_EVALUABLES_COMPETITIVIDAD_ACADEMICA = arrayOf("Ciclo de Datos de Matrícula",
            "Número de Programas de Buena Calidad","Matrícula de Programas de Buena Calidad",
            "Número de Programas Evaluables", "Número de Programas (No Evaluable)","Matrícula de Programas Evaluables",
            "Matrícula (Programas no Evaluables)")
        val IND_10_PORCENTAJE_PROGRAMAS = arrayOf("Ciclo de datos de matrícula","% de Programas TSU y Licenciatura Evaluables de Buena Calidad",
            "% Matrícula Atendida en Progamas de TSU y Licenciatura Evaluables de Buena Calidad",
            "% Competitividad Académica","Competitividad y Capacidad Académica")
        val IND_10_NUMERO_PROGRAMAS_TSU_LIC_POSG = arrayOf("Ciclo de Datos de Matrícula","TSU/PA","Licenciatura",
            "Especialidad","Maestría","Doctorado")
        val IND_10_PORCENTAJE_NUMERO_PROGRAMAS = arrayOf("Ciclo de datos de matrícula","% CAC","% Perfil Deseable",
            "% S.N.I.","Capacidad Académica")
        val IND_10_ALUMNOS_NIVEL_SUPERIOR_MODALIDAD_ESCOLAR = arrayOf("Ciclo de datos de matrícula","Técnico superior",
            "Licencia Profesional","Licenciatura","Posgrado","Total Escolar")
        val IND_10_ALUMNOS_NIVEL_SUPERIOR_MODALIDAD_NO_ESCOLARIZADA = arrayOf("Ciclo de datos de matrícula",
            "Técnico superior", "Licencia Profesional","Licenciatura","Posgrado","Total No Escolarizada")
        val IND_10_PERSONAS_FACULTADES_ESCUELAS_CENTROS_DIVISIONES_DEPARTAMENTOS = arrayOf("Ciclo de datos de matrícula",
            "Directivo","Docente","Docente-Investigador y Docente-Auxiliar de Investigador",
            "Investigador","Auxiliar de Investigador","Administrativo","Otros","Total de Personal","Directivo")
        val IND_10_PERSONAL_AREAS_CENTRALES = arrayOf("Ciclo de datos de matrícula","Directivo",
            "Auxiliar de Investigador (no incluye servicio social y prácticas profesionales)","Administrativo",
            "Otros","Total Personal Áreas Centrales")
        val IND_10_TOTAL_PERSONAL_INSTITUCION = arrayOf("Ciclo de datos de matrícula","Total de Personal de la Institución")
        val IND_10_TOTAL_PERSONAL_DOCENTE_ESCOLARIZADO = arrayOf("Ciclo de datos de matrícula","Personal de Tiempo Completo",
            "Personal de Tres Cuartos de Tiempo","Personal de Medio Tiempo","Personal por Hora o Asignatura","Total")
        val IND_10_TOTAL_PERSONAL_DOCENTE_NO_ESCOLARIZADO = arrayOf("Ciclo de datos de matrícula",
            "Personal de Tiempo Completo","Personal de Tres Cuartos de Tiempo","Personal de Medio Tiempo",
            "Personal por Hora o Asignatura","Total")
        val IND_10_CARRERA_PROGRAMAS_ALUMNOS_MODALIDAD_ESCOLARIZADA = arrayOf("Ciclo de datos de matrícula",
            "Técnico Superior","Licenciatura Profesional","Licenciatura","Especialidad","Maestría",
            "Doctorado","Total")
        val IND_10_CARRERA_PROGRAMAS_ALUMNOS_MODALIDAD_NO_ESCOLARIZADA = arrayOf("Ciclo de datos de matrícula",
            "Técnico Superior","Licenciatura Profesional","Licenciatura","Especialidad","Maestría",
            "Doctorado","Total")
        val IND_10_DESGLOSE_PROFESORES_TIEMPO_COMPLETO = arrayOf("Ciclo de datos de matrícula","PTC Licenciatura",
            "PTC con Maestría","PTC con Doctorado","PTC con posgrado","Total de PTC")
        val IND_10_DESGLOSE_PROFESORES_PERFIL_DESEABLE_VIGENTE_SNI = arrayOf("Ciclo de datos de matrícula","Con Perfil Deseable Vigente",
            "SNIC","SNI1","SNI2","SNI3","Total SNI Vigente")
        val IND_10_CUERPOS_ACADEMICOS = arrayOf("Ciclo de datos de matrícula","Total CAEF","Total CAEC",
            "Total CAC","Total CA")
        val IND_10_DESGLOSE_CAEF = arrayOf("Ciclo de datos de matrícula","Área CS. Agropecuarias","Área CS. Salud",
            "Área CS. Naturales y Exactas","Área CS. Sociales y Administrativas","Área CS. Ingeniería y Tecnología",
            "Área Educación, Humanidades y Artes")
        val IND_10_DESGLOSE_CAEC = arrayOf("Ciclo de datos de matrícula","Área CS. Agropecuarias","Área CS. Salud",
            "Área CS. Naturales y Exactas","Área CS. Sociales y Administrativas","Área CS. Ingeniería y Tecnología",
            "Área Educación, Humanidades y Artes")
        val IND_10_DESGLOSE_CAC = arrayOf("Ciclo de datos de matrícula","Área CS. Agropecuarias","Área CS. Salud",
            "Área CS. Naturales y Exactas","Área CS. Sociales y Administrativas","Área CS. Ingeniería y Tecnología",
            "Área Educación, Humanidades y Artes")
        val IND_10_DESGLOSE_CA = arrayOf("Ciclo de datos de matrícula","Área CS. Agropecuarias","Área CS. Salud",
            "Área CS. Naturales y Exactas","Área CS. Sociales y Administrativas","Área CS. Ingeniería y Tecnología",
            "Área Educación, Humanidades y Artes")
    }
}
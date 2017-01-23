/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfaceAdministrar;

import ClasesAyuda.ResultadoBorrarTodoNovedades;
import Entidades.ActualUsuario;
import Entidades.Conceptos;
import Entidades.Empleados;
import Entidades.Formulas;
import Entidades.TempNovedades;
import Entidades.VWActualesReformasLaborales;
import Entidades.VWActualesTiposContratos;
import Entidades.VWActualesTiposTrabajadores;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

/**
 * Interface encargada de determinar las operaciones lógicas necesarias para la
 * pantalla 'CargueArchivos'.
 *
 * @author betelgeuse
 */
public interface AdministrarCargueArchivosInterface {

   public void obtenerConexion(String idSesion);

   /**
    * Método encargado de crear TempNovedades.
    *
    * @param listaTempNovedades Lista de los TempNovedades que se van a crear.
    */
   public void crearTempNovedades(List<TempNovedades> listaTempNovedades);

   /**
    * Método encargado de editar la TempNovedad.
    *
    * @param tempNovedades TempNovedades que se va a modificar.
    */
   public void modificarTempNovedades(TempNovedades tempNovedades);

   /**
    * Método encargado de borrar todos los RegistrosTempNovedades.
    *
    * @param usuarioBD Lista de los ExtrasRecargos que se van a eliminar.
    */
   public void borrarTempNovedad(TempNovedades tempNovedades);

   public void borrarRegistrosTempNovedades(String usuarioBD);

   /**
    * Método encargado de recuperar el usuario que esta usando el aplicativo.
    *
    * @return Retorna un ActualUsuario con la información del usuario.
    */
   public ActualUsuario actualUsuario();

   /**
    * Método encargado de recuperar la secuencia de la empresa asociada al
    * ParametroEstructura del usuario que está usando el aplicativo.
    *
    * @param usuarioBD Alias del usuario que está usando el aplicativo.
    * @return Retorna la secuencia de la empresa.
    */
   public BigInteger consultarParametrosEmpresa(String usuarioBD);

   /**
    * Método encargado de recuperar las TempNovedad cuyo ESTADO es igual a 'N' y
    * esta relacionada con el usuario que entra por parámetro.
    *
    * @param usuarioBD Alias del usuario asociado a la TempNovedad.
    * @return Retorna una lista de TempNovedades.
    */
   public List<TempNovedades> consultarTempNovedades(String usuarioBD);

   /**
    * Método encargado de averiguar si un empleado específico existe para una
    * empresa.<br>
    * <font color="blue"><strong>PRIMERA ETAPA VALIDACION</strong></font>
    *
    * @param codEmpleado Código del empleado.
    * @param secEmpresa Secuencia de la Empresa.
    * @return Retorna True si existe el empleado con el código igual al
    * parámetro 'codEmpleado' y pertenece a la empresa cuya secuencia coincide
    * con el parámetro.
    */
   public boolean verificarEmpleadoEmpresa(BigInteger codEmpleado, BigInteger secEmpresa);

   /**
    * Método encargado de validar que el código que entra por parámetro
    * existe.<br>
    * <font color="blue"><strong>PRIMERA ETAPA VALIDACION</strong></font>
    *
    * @param codConcepto Código del concepto.
    * @return True si el Código está en la base de datos.
    */
   public boolean verificarConcepto(BigInteger codConcepto);

   /**
    * Método encargado de validar si hay al menos una Periodicidad con el código
    * dado como parámetro.<br>
    * <font color="blue"><strong>PRIMERA ETAPA VALIDACION</strong></font>
    *
    * @param codPeriodicidad Código de la Periodicidad.
    * @return Retorna true si existe alguna Periodicidad con el código dado por
    * parámetro.
    */
   public boolean verificarPeriodicidad(BigInteger codPeriodicidad);

   /**
    * Método encargado de validar si existe por lo menos un tercero con el nit
    * que entra por parámetro.<br>
    * <font color="blue"><strong>PRIMERA ETAPA VALIDACION</strong></font>
    *
    * @param nitTercero Nit del Tercero.
    * @return Retorna True si existe al menos un Tercero con el nit del
    * parámetro, False de lo contrario.
    */
   public boolean verificarTercero(BigInteger nitTercero);

   /**
    * Método encargado de validar si un Empleado, perteneciente a una empresa
    * específica, esta 'ACTIVO' en el momento de la fechaHasta de la
    * liquidación.<br>
    * <font color="blue"><strong>SEGUNDA ETAPA VALIDACION</strong></font>
    *
    * @param codEmpleado Código del empleado.
    * @param secEmpresa Secuencia de la Empresa.
    * @return Retorna True si el empleado, cuyo código es igual al parámetro
    * 'codEmpleado' y que pertenece a la Empresa cuya secuencia coincide con el
    * parámetro 'secEmpresa', es de tipo 'ACTIVO'.
    */
   public boolean verificarTipoEmpleadoActivo(BigInteger codEmpleado, BigInteger secEmpresa);

   /**
    * Método encargado de recuperar un empleado de una empresa específica.
    * <font color="blue"><strong>SEGUNDA ETAPA VALIDACION</strong></font>
    *
    * @param codEmpleado Códígo del empleado.
    * @param secEmpresa Secuencia de la empresa a la que el usuario
    * pertenecería.
    * @return Retorna el empleado que cumple las características dadas por los
    * parámetros.
    */
   public Empleados consultarEmpleadoEmpresa(BigInteger codEmpleado, BigInteger secEmpresa);

   /**
    * Método encargado de recuperar el TipoTrabajador actual de un empleado. El
    * término actual está dado por la fechaHasta de las liquidaciones.
    *
    * @param secEmpleado Secuencia del empleado.
    * @return Retorna una VWActualesTiposTrabajadores con la información del
    * TipoTrabajador actual de un empleado.
    */
   public VWActualesTiposTrabajadores consultarActualTipoTrabajadorEmpleado(BigInteger secEmpleado);

   /**
    * Método encargado de recuperar la Reforma Laboral actual de un empleado. El
    * término actual está dado por la fechaHasta de las liquidaciones.
    *
    * @param secuencia Secuencia del empleado.
    * @return Retorna una VWActualAfiliacionPension con la información de la
    * Reforma Laboral actual de un empleado.
    */
   public VWActualesReformasLaborales consultarActualReformaLaboralEmpleado(BigInteger secuencia);

   /**
    * Método encargado de buscar el Tipo Contrato actual de un empleado. El
    * término actual está dado por la fechaHasta de las liquidaciones.
    *
    * @param secuencia Secuencia del empleado.
    * @return Retorna una VWActualesTiposContratos con la información del Tipo
    * Contrato actual de un empleado.
    */
   public VWActualesTiposContratos consultarActualTipoContratoEmpleado(BigInteger secuencia);

   /**
    * Método encargado de recuperar el concepto cuyo código existe y pertencen a
    * una empresa especifica.
    * <font color="blue"><strong>SEGUNDA ETAPA VALIDACION</strong></font>
    *
    * @param codigoConcepto Código del concepto que se quiere buscar.
    * @param secEmpresa Secuencia de la empresa a la que pertenece el concepto
    * @return Retorna el Concepto cuyo código concuerda con el parámetro
    * 'codigoConcepto' y que pertenecen a la empresa con secuencia igual a la
    * dada por parámetro.
    */
   public Conceptos verificarConceptoEmpresa(BigInteger codigoConcepto, BigInteger secEmpresa);

   /**
    * Método encargado de determinar el Tipo de un concepto específico. El Tipo
    * del concepto puede ser 'AUTOMATICO', 'SEMI-AUTOMATICO', 'MANUAL'
    * dependiendo de las relaciones que el concepto tenga.
    * <ul>
    * <li>Si el concepto esta asociado con FormulasConceptos y todas y cada una
    * de las formulas asociadas a esas FormulasConceptos estan relacionadas con
    * la tabla FormulasNovedades, entonces es de Tipo 'SEMI-AUTOMATICO'.</li>
    * <li>Si el concepto esta asociado con FormulasConceptos pero alguna de las
    * formulas asociadas a esas FormulasConceptos NO esta relacionada con la
    * tabla FormulasNovedades, entonces es de Tipo 'AUTOMATICO'.</li>
    * <li>En cualquier otro caso el concepto es de Tipo 'MANUAL'.</li>
    * </ul>
    *
    * @param secConcepto Secuencia del concepto al que se le determinará el
    * Tipo.
    * @return Retorna un String con el Tipo del concepto.
    */
   public String determinarTipoConcepto(BigInteger secConcepto);

   /**
    * Método encargado de validar si un concepto con TipoSalario, TipoContrato y
    * TipoTrabajador definidos, está relacionado con VigenciasConceptosRL,
    * VigenciasConceptosTC y VigenciasConceptosTT.
    * <font color="blue"><strong>SEGUNDA ETAPA VALIDACION</strong></font>
    *
    * @param secConcepto Secuencia del Concepto.
    * @param secTS Secuencia del TipoSalario
    * @param secTC Secuencia del TipoContrato
    * @param secTT Secuencia del TipoTrabajador
    * @return Retorna True si y solo si el concepto con las caracteristicas
    * dadas por los parámetros está relacionado con las tablas
    * VigenciasConceptosRL, VigenciasConceptosTC y VigenciasConceptosTT.
    */
   public boolean verificarZonaT(BigInteger secConcepto, BigInteger secTS, BigInteger secTC, BigInteger secTT);

   /**
    * Método encargado de recuperar las formulas habilitadas para hacer cargue a
    * la hora de subir un archivo plano.
    *
    * @return Retorna una lista de Formulas.
    */
   public List<Formulas> consultarFormulasCargue();

   /**
    * Método encargado de recuperar la Formula por default para cargar un
    * archivo plano.
    *
    * @return Retorna la Formula por default, 'LIQNOV'.
    */
   public Formulas consultarFormulaCargueInicial();

   /**
    * Método encargado de validar que una FormulaConcepto esté asociada con un
    * concepto al momento de hace el cargue de archivos.
    * <font color="blue"><strong>VALIDACION PARA CARGUE DE
    * ARCHIVOS</strong></font>
    *
    * @param secConcepto Secuencia del concepto.
    * @param secFormula Secuencia de la FormulaConcepto.
    * @return Retorna true si existe una relación entre la fórmula y el
    * concepto.
    */
   public boolean verificarFormulaCargueConcepto(BigInteger secConcepto, BigInteger secFormula);

   /**
    * Método encargado de validar la existencia de al menos una
    * VigenciaGrupoConcepto asociada a un Concepto específico y además que este
    * asociada con el GrupoConcepto uno (NIT).
    * <font color="blue"><strong>VALIDACION PARA CARGUE DE
    * ARCHIVOS</strong></font>
    *
    * @param secuencia Secuencia del Concepto.
    * @return Retorna True si existe al menos una VigenciaGrupoConcepto cuyo
    * Concepto tenga como secuencia la dada por el parámetro "secuencia".
    */
   public boolean verificarNecesidadTercero(BigInteger secuencia);

   /**
    * Método encargado de validar si existe al menos un Tercero con el nit
    * recibido por parámetro y que está relacionado con la empresa cuya
    * secuencia coincide con la recibida por parámetro.
    * <font color="blue"><strong>VALIDACION PARA CARGUE DE
    * ARCHIVOS</strong></font>
    *
    * @param nit Nit del Tercero.
    * @param secEmpresa Secuencia de la empresa con la que el Tercero estaría
    * relacionado.
    * @return Retorna True si existe al menos un Tercero que cumpla las
    * condiciones mencionadas, False de lo contrario.
    */
   public boolean verificarTerceroEmpresa(BigInteger nit, BigInteger secEmpresa);

   /**
    * Método encargado de RECUPERAR los datos del DocumentosSoporte de cada una
    * de las TempNovedades cuyo ESTADO es igual a 'C' y esta relacionada con el
    * usuario que entra por parámetro.
    *
    * @param usuarioBD Alias del usuario asociado a la TempNovedad.
    * @return Retorna una lista de String con los DocumentosSoporte de las
    * TempNovedades que cumplen las condiciones.
    */
   public List<String> consultarDocumentosSoporteCargadosUsuario(String usuarioBD);

   /**
    * Método encargado de cargar las TempNovedades a Novedades. Cuando una
    * TempNovedad es cargada en Novedades, cambia de Estado 'N' a estado 'C'.
    *
    * @param fechaReporte Fecha en la que se cargó el Archivo Plano a
    * TempNovedades.
    * @param nombreCorto Nombre corto de la formula.
    * @param usarFormula Indica si se va a usar la formula del concepto o la
    * formula pasada por parametro. Sus posibles valores son 'S' o 'N'.
    */
   public void cargarTempNovedades(Date fechaReporte, String nombreCorto, String usarFormula);

   /**
    * Método encargado de reversar una o varias Novedades (un cargue de
    * archivos). No es posible reversar un cargue de archivos si al menos una de
    * las novedades asociadas a este a sido liquidada.
    *
    * @param usuarioBD Secuencia del usuario que reportó las novedades.
    * @param documentoSoporte Texto referente al atributo documentoSoporte de la
    * Novedad(del cargue archivos).
    * @return Retorna el número de Novedades eliminadas, 0 si no se puede
    * reversar ningúna novedad (el cargue de archivos).
    */
   public int reversarNovedades(ActualUsuario usuarioBD, String documentoSoporte);

   /**
    * Método encargado de recuperar la información de lo acorrido al borrar
    * todas las novedades del usuario que está usando el aplicativo.
    *
    * @param usuarioBD Alias del Usuario que está
    * @param documentosSoporte Lista de String con los DocumentosSoportes
    * existentes.
    * @return Retorna un ResultadoBorrarTodoNovedades con los documentos que no
    * fueron borrados y la cantidad de registros eliminados.
    */
   public ResultadoBorrarTodoNovedades BorrarTodo(ActualUsuario usuarioBD, List<String> documentosSoporte);
   
   public String consultarRuta();
}

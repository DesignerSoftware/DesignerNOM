/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfaceAdministrar;

import Entidades.CentrosCostos;
import Entidades.Conceptos;
import Entidades.Cuentas;
import Entidades.Formulas;
import Entidades.FormulasConceptos;
import Entidades.GruposConceptos;
import Entidades.Procesos;
import Entidades.ReformasLaborales;
import Entidades.TiposCentrosCostos;
import Entidades.TiposContratos;
import Entidades.TiposTrabajadores;
import Entidades.VigenciasConceptosRL;
import Entidades.VigenciasConceptosTC;
import Entidades.VigenciasConceptosTT;
import Entidades.VigenciasCuentas;
import Entidades.VigenciasGruposConceptos;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 * Interface encargada de determinar las operaciones lógicas necesarias para la
 * pantalla 'DetalleConcepto'.
 *
 * @author Andres Pineda.
 */
public interface AdministrarDetalleConceptoInterface {

   /**
    * Método encargado de obtener el Entity Manager el cual tiene asociado la
    * sesion del usuario que utiliza el aplicativo.
    *
    * @param idSesion Identificador se la sesion.
    */
   public void obtenerConexion(String idSesion);

   /**
    * Método encargado de recuperar las VigenciasCuentas según el Concepto que
    * tengan asociado.
    *
    * @param secConcepto Secuencia del Concepto por el cual se filtrara la
    * búsqueda.
    * @return Retorna una lista de VigenciasCuentas.
    */
   public List<VigenciasCuentas> consultarListaVigenciasCuentasConcepto(BigInteger secConcepto);

   /**
    * Método encargado de crear VigenciasCuentas.
    *
    * @param listaVigenciasCuentas Lista de las VigenciasCuentas que se van a
    * crear.
    */
   public void crearVigenciasCuentas(List<VigenciasCuentas> listaVigenciasCuentas);

   /**
    * Método encargado de editar VigenciasCuentas.
    *
    * @param listaVigenciasCuentas Lista de las VigenciasCuentas que se van a
    * modificar.
    */
   public void modificarVigenciasCuentas(List<VigenciasCuentas> listaVigenciasCuentas);

   /**
    * Método encargado de borrar VigenciasCuentas.
    *
    * @param listaVigenciasCuentas Lista de las VigenciasCuentas que se van a
    * eliminar.
    */
   public void borrarVigenciasCuentas(List<VigenciasCuentas> listaVigenciasCuentas);

   /**
    * Método encargado de recuperar las VigenciasGruposConceptos según el
    * Concepto que tengan asociado.
    *
    * @param secConcepto Secuencia del Concepto por el cual se filtrara la
    * búsqueda.
    * @return Retorna una lista de VigenciasGruposConceptos.
    */
   public List<VigenciasGruposConceptos> consultarListaVigenciasGruposConceptosConcepto(BigInteger secConcepto);

   /**
    * Método encargado de crear VigenciasGruposConceptos.
    *
    * @param listaVigenGruposConceptos Lista de las VigenciasGruposConceptos que
    * se van a crear.
    */
   public void crearVigenciasGruposConceptos(List<VigenciasGruposConceptos> listaVigenGruposConceptos);

   /**
    * Método encargado de editar VigenGruposConceptos.
    *
    * @param listaVigenGruposConceptos Lista de las VigenGruposConceptos que se
    * van a modificar.
    */
   public void modificarVigenciasGruposConceptos(List<VigenciasGruposConceptos> listaVigenGruposConceptos);

   /**
    * Método encargado de borrar VigenciasGruposConceptos.
    *
    * @param listaVigenGruposConceptos Lista de las VigenciasGruposConceptos que
    * se van a eliminar.
    */
   public void borrarVigenciasGruposConceptos(List<VigenciasGruposConceptos> listaVigenGruposConceptos);

   /**
    * Método encargado de recuperar las VigenciasConceptosTT según el Concepto
    * que tengan asociado.
    *
    * @param secConcepto Secuencia del Concepto por el cual se filtrara la
    * búsqueda.
    * @return Retorna una lista de VigenciasConceptosTT.
    */
   public List<VigenciasConceptosTT> consultarListaVigenciasConceptosTTConcepto(BigInteger secConcepto);

   /**
    * Método encargado de crear VigenciasConceptosTT.
    *
    * @param listaVigenciasConceptosTT Lista de las VigenciasConceptosTT que se
    * van a crear.
    */
   public void crearVigenciasConceptosTT(List<VigenciasConceptosTT> listaVigenciasConceptosTT);

   /**
    * Método encargado de editar VigenciasConceptosTT.
    *
    * @param listaVigenciasConceptosTT Lista de las VigenciasConceptosTT que se
    * van a modificar.
    */
   public void modificarVigenciasConceptosTT(List<VigenciasConceptosTT> listaVigenciasConceptosTT);

   /**
    * Método encargado de borrar VigenciasConceptosTT.
    *
    * @param listaVigenciasConceptosTT Lista de las VigenciasConceptosTT que se
    * van a eliminar.
    */
   public void borrarVigenciasConceptosTT(List<VigenciasConceptosTT> listaVigenciasConceptosTT);

   /**
    * Método encargado de recuperar las VigenciasConceptosTC según el Concepto
    * que tengan asociado.
    *
    * @param secConcepto Secuencia del Concepto por el cual se filtrara la
    * búsqueda.
    * @return Retorna una lista de VigenciasConceptosTC.
    */
   public List<VigenciasConceptosTC> consultarListaVigenciasConceptosTCConcepto(BigInteger secConcepto);

   /**
    * Método encargado de crear VigenciasConceptosTC.
    *
    * @param listaVigenciasConceptosTC Lista de las VigenciasConceptosTC que se
    * van a crear.
    */
   public void crearVigenciasConceptosTC(List<VigenciasConceptosTC> listaVigenciasConceptosTC);

   /**
    * Método encargado de editar VigenciasConceptosTC.
    *
    * @param listaVigenciasConceptosTC Lista de las VigenciasConceptosTC que se
    * van a modificar.
    */
   public void modificarVigenciasConceptosTC(List<VigenciasConceptosTC> listaVigenciasConceptosTC);

   /**
    * Método encargado de borrar VigenciasConceptosTC.
    *
    * @param listaVigenciasConceptosTC Lista de las VigenciasConceptosTC que se
    * van a eliminar.
    */
   public void borrarVigenciasConceptosTC(List<VigenciasConceptosTC> listaVigenciasConceptosTC);

   /**
    * Método encargado de recuperar las VigenciasConceptosRL según el Concepto
    * que tengan asociado.
    *
    * @param secConcepto Secuencia del Concepto por el cual se filtrara la
    * búsqueda.
    * @return Retorna una lista de VigenciasConceptosRL.
    */
   public List<VigenciasConceptosRL> consultarListaVigenciasConceptosRLCConcepto(BigInteger secConcepto);

   /**
    * Método encargado de crear VigenciasConceptosRL.
    *
    * @param listaVigenciasConceptosRL Lista de las VigenciasConceptosRL que se
    * van a crear.
    */
   public void crearVigenciasConceptosRL(List<VigenciasConceptosRL> listaVigenciasConceptosRL);

   /**
    * Método encargado de editar VigenciasConceptosRL.
    *
    * @param listaVigenciasConceptosRL Lista de las VigenciasConceptosRL que se
    * van a modificar.
    */
   public void modificarVigenciasConceptosRL(List<VigenciasConceptosRL> listaVigenciasConceptosRL);

   /**
    * Método encargado de borrar VigenciasConceptosRL.
    *
    * @param listaVigenciasConceptosRL Lista de las VigenciasConceptosRL que se
    * van a eliminar.
    */
   public void borrarVigenciasConceptosRL(List<VigenciasConceptosRL> listaVigenciasConceptosRL);

   /**
    * Método encargado de recuperar las FormulasConceptos según el Concepto que
    * tengan asociado.
    *
    * @param secConcepto Secuencia del Concepto por el cual se filtrara la
    * búsqueda.
    * @return Retorna una lista de FormulasConceptos.
    */
   public List<FormulasConceptos> consultarListaFormulasConceptosConcepto(BigInteger secConcepto);

   /**
    * Método encargado de crear FormulasConceptos.
    *
    * @param listaFormulasConceptos Lista de las FormulasConceptos que se van a
    * crear.
    */
   public void crearFormulasConceptos(List<FormulasConceptos> listaFormulasConceptos);

   /**
    * Método encargado de editar FormulasConceptos.
    *
    * @param listaFormulasConceptos Lista de las FormulasConceptos que se van a
    * modificar.
    */
   public void modificarFormulasConceptos(List<FormulasConceptos> listaFormulasConceptos);

   /**
    * Método encargado de borrar FormulasConceptos.
    *
    * @param listaFormulasConceptos Lista de las FormulasConceptos que se van a
    * eliminar.
    */
   public void borrarFormulasConceptos(List<FormulasConceptos> listaFormulasConceptos);

   /**
    * Método encargado de recuperar el concepto del cual se van a basar los
    * detalles.
    *
    * @param secConcepto Secuencia del concepto.
    * @return Retorna el concepto cuya secuencia coincide con el valor del
    * parámetro.
    */
   public Conceptos consultarConceptoActual(BigInteger secConcepto);

   /**
    * Método encargado de recuperar los TiposCentrosCostos necesarios para la
    * lista de valores.
    *
    * @return Retorna una lista de TiposCentrosCostos.
    */
   public List<TiposCentrosCostos> consultarLOVTiposCentrosCostos();

   /**
    * Método encargado de recuperar los Cuentas necesarios para la lista de
    * valores.
    *
    * @return Retorna una lista de Cuentas.
    */
   public List<Cuentas> consultarLOVCuentas();

   /**
    * Método encargado de recuperar los CentrosCostos necesarios para la lista
    * de valores.
    *
    * @return Retorna una lista de CentrosCostos.
    */
   public List<CentrosCostos> consultarLOVCentrosCostos();

   /**
    * Método encargado de recuperar los GruposConceptos necesarios para la lista
    * de valores.
    *
    * @return Retorna una lista de GruposConceptos.
    */
   public List<GruposConceptos> consultarLOVGruposConceptos();

   /**
    * Método encargado de recuperar los TiposTrabajadores necesarios para la
    * lista de valores.
    *
    * @return Retorna una lista de TiposTrabajadores.
    */
   public List<TiposTrabajadores> consultarLOVTiposTrabajadores();

   /**
    * Método encargado de recuperar los TiposContratos necesarios para la lista
    * de valores.
    *
    * @return Retorna una lista de TiposContratos.
    */
   public List<TiposContratos> consultarLOVTiposContratos();

   /**
    * Método encargado de recuperar los ReformasLaborales necesarios para la
    * lista de valores.
    *
    * @return Retorna una lista de ReformasLaborales.
    */
   public List<ReformasLaborales> consultarLOVReformasLaborales();

   /**
    * Método encargado de recuperar los Formulas necesarios para la lista de
    * valores.
    *
    * @return Retorna una lista de Formulas.
    */
   public List<Formulas> consultarLOVFormulas();

   /**
    * Método encargado de recuperar los FormulasConceptos necesarios para la
    * lista de valores.
    *
    * @return Retorna una lista de FormulasConceptos.
    */
   public List<FormulasConceptos> consultarLOVFormulasConceptos();

   /**
    *
    * @return
    */
   public List<Procesos> consultarLOVProcesos();

   /**
    * Método encargado de recuperar el número de FormulasConceptos asociadas con
    * un concepto específico. Esto con el fin de determinar si se usa el
    * comportamiento automatico o no.
    *
    * @param secConcepto Secuencia del concepto.
    * @return Retorna el número de FormulasConceptos asociadas con el concepto
    * cuya secuencia coincida con el valor dado por parámetro.
    */
   public Long contarFormulasConceptosConcepto(BigInteger secConcepto);

   /**
    * Método encargado de recuperar el número de FormulasConceptos que estan
    * asociadas con un concepto específico y cuya Formula esta asociada con una
    * FormulaNovedad. Esto con el fin de determinar si se usa el comportamiento
    * Semiautomatico o no.
    *
    * @param secConcepto Secuencia del concepto.
    * @return Retorna el número de FormulasConceptos que cumplen con las
    * condiciones mencionadas.
    */
   public Long contarFormulasNovedadesConcepto(BigInteger secConcepto);

   /**
    * Método encargado de eliminar un concepto y los datos referentes a este.
    *
    * @param secConcepto Secuencia del Concepto.
    * @return Retorna True si fue posible eliminar el concepto, false de lo
    * contrario.
    */
   public boolean eliminarConceptoTotal(BigInteger secConcepto);

   /**
    * Método encargado de recuperar las SolucionesNodos según el Concepto que
    * tengan asociado.
    *
    * @param secConcepto Secuencia del Concepto por el cual se filtrara la
    * búsqueda.
    * @return Retorna una lista de SolucionesNodos.
    */
   public boolean verificarSolucionesNodosConcepto(BigInteger secConcepto);

   public int contarVigCuentasPorTipoccConceptoYCuentac(BigInteger tipoCC, BigInteger cuentaC, BigInteger concepto, Date fechaIni);

   public int contarVigCuentasPorTipoccConceptoYCuentad(BigInteger tipoCC, BigInteger cuentaD, BigInteger concepto, Date fechaIni);

   public CentrosCostos centroCostoLocalizacionTrabajador(BigInteger secEmpresa);

   public CentrosCostos centroCostoContabilidad(BigInteger secEmpresa);

   public List<Cuentas> cuenta2505(BigInteger secEmpresa);
}

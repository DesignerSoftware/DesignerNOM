/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.CambiosMasivos;
import Entidades.Causasausentismos;
import Entidades.CentrosCostos;
import Entidades.Clasesausentismos;
import Entidades.Conceptos;
import Entidades.Empleados;
import Entidades.Estructuras;
import Entidades.Formulas;
import Entidades.MotivosCambiosSueldos;
import Entidades.MotivosDefinitivas;
import Entidades.MotivosRetiros;
import Entidades.Papeles;
import Entidades.Parametros;
import Entidades.ParametrosCambiosMasivos;
import Entidades.Periodicidades;
import Entidades.Terceros;
import Entidades.TercerosSucursales;
import Entidades.TiposEntidades;
import Entidades.TiposSueldos;
import Entidades.Tiposausentismos;
import Entidades.Unidades;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarCambiosMasivosInterface {

   public void obtenerConexion(String idSesion);

   public List<Parametros> consultarEmpleadosParametros();

   public List<CambiosMasivos> consultarUltimosCambiosMasivos();

   public ParametrosCambiosMasivos consultarParametrosCambiosMasivos();

   /**
    *
    * @param parametro
    * @return
    */
   public boolean actualizarParametroCM(ParametrosCambiosMasivos parametro);

   public List<Estructuras> consultarLovCargos_Estructuras();

   public List<MotivosDefinitivas> consultarLovMotivosDefinitivas();

   public List<MotivosRetiros> consultarLovMotivosRetiros();

   public List<TiposEntidades> consultarLovTiposEntidades();

   public List<TercerosSucursales> consultarLovTercerosSucursales();

   public List<CentrosCostos> consultarLovCentrosCostos();

   public List<Periodicidades> consultarLovPeriodicidades();

   public List<Conceptos> consultarLovConceptos();

   public List<Formulas> consultarLovFormulas();

   public List<Terceros> consultarLovTerceros();

   public List<MotivosCambiosSueldos> consultarLovMotivosCambiosSueldos();

   public List<TiposSueldos> consultarLovTiposSueldos();

   public List<Tiposausentismos> consultarLovTiposausentismos();

   public List<Unidades> consultarLovUnidades();

   public List<Empleados> consultarLovEmpleados();

   /**
    *
    * @param secEmpleado
    * @return
    */
//   public List<Ibcs> consultarLovIbcs(BigInteger secEmpleado);
   public List<Papeles> consultarLovPapeles();

   public List<Causasausentismos> consultarLovCausasausentismos();

   public List<Clasesausentismos> consultarLovClasesausentismos();

   public void adicionaEstructuraCM(BigInteger secEstructura, Date fechaCambio);

   public void undoAdicionaEstructuraCM(BigInteger secEstructura, Date fechaCambio);

   public void adicionaVacacionCM(BigInteger ndias, Date fechaCambio, Date fechaPago);

   public void undoAdicionaVacacionCM(BigInteger ndias, Date fechaCambio, Date fechaPago);

   public void adicionaRetiroCM(String indemniza, BigInteger secMotivoDefinitiva, BigInteger secMotivoRetiro, Date fechaCambio);

   public void undoAdicionaRetiroCM(String indemniza, BigInteger secMotivoDefinitiva, BigInteger secMotivoRetiro, Date fechaCambio);

   public void adicionaAfiliacionCM(BigInteger secTipoEntidad, BigInteger secTerceroSuc, Date fechaCambio);

   public void undoAdicionaAfiliacionCM(BigInteger secTipoEntidad, BigInteger secTerceroSuc, Date fechaCambio);

   public void adicionaLocalizacionCM(BigInteger secEstructura, Date fechaCambio);

   public void undoAdicionaLocalizacionCM(BigInteger secEstructura, Date fechaCambio);

   public void adicionaSueldoCM(BigInteger secMotivoCS, BigInteger secTipoSueldo, BigInteger secUnidad, BigInteger valor, Date fechaCambio);

   public void undoAdicionaSueldoCM(BigInteger secMotivoCS, BigInteger secTipoSueldo, BigInteger secUnidad, BigInteger valor, Date fechaCambio);

   public void adicionaNovedadCM(String tipo, BigInteger secConcepto, BigInteger secPeriodicidad,
           BigInteger secTercero, BigInteger secFormula, BigInteger valor,
           BigInteger saldo, Date fechaCambioInicial, Date fechaCambioFinal,
           BigInteger unidadParteEntera, BigInteger unidadParteFraccion);

   public void undoAdicionaNovedadCM(String tipo, BigInteger secConcepto, BigInteger secPeriodicidad,
           BigInteger secTercero, BigInteger secFormula, BigInteger valor,
           BigInteger saldo, Date fechaCambioInicial, Date fechaCambioFinal);

   public void adicionaReingresoCM(Date fechaIni, Date fechaFin);

   public void adicionaEmplJefeCM(BigInteger secEmpleado, Date fechaCambio);

   public void adicionaPapelCM(BigInteger secPapel, Date fechaCambio);

   public void adicionaAusentismoCM(BigInteger secTipo, BigInteger secClase, BigInteger secCausa, BigInteger dias,
           BigInteger horas, Date fechaIniAusen, Date fechaFinAusen, Date fechaExpedicion,
           Date fechaIpago, Date fechaPago, BigInteger porcent, BigInteger baseliq, String forma);

   public void undoAdicionaAusentismoCM(BigInteger secTipo, BigInteger secClase,
           BigInteger secCausa, BigInteger dias, Date fechaIniAusen, Date fechaFinAusen);

}

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

   public boolean actualizarParametroCM(ParametrosCambiosMasivos parametro);

   public boolean comprobarConceptoManual(BigInteger secuenciaConcepto);

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

   public List<Papeles> consultarLovPapeles();

   public List<Causasausentismos> consultarLovCausasausentismos();

   public List<Clasesausentismos> consultarLovClasesausentismos();

   public void adicionaEstructuraCM2(BigInteger secEstructura, Date fechaCambio);

   public void undoAdicionaEstructuraCM2(BigInteger secEstructura, Date fechaCambio);

   public void adicionaVacacionCM2(BigInteger ndias, Date fechaCambio, Date fechaPago);

   public void undoAdicionaVacacionCM2(BigInteger ndias, Date fechaCambio, Date fechaPago);

   public void adicionaRetiroCM2(String indemniza, BigInteger secMotivoDefinitiva, BigInteger secMotivoRetiro, Date fechaCambio);

   public void undoAdicionaRetiroCM2(String indemniza, BigInteger secMotivoDefinitiva, BigInteger secMotivoRetiro, Date fechaCambio);

   public void adicionaAfiliacionCM2(BigInteger secTipoEntidad, BigInteger secTerceroSuc, Date fechaCambio);

   public void undoAdicionaAfiliacionCM2(BigInteger secTipoEntidad, BigInteger secTerceroSuc, Date fechaCambio);

   public void adicionaLocalizacionCM2(BigInteger secEstructura, Date fechaCambio);

   public void undoAdicionaLocalizacionCM2(BigInteger secEstructura, Date fechaCambio);

   public void adicionaSueldoCM2(BigInteger secMotivoCS, BigInteger secTipoSueldo, BigInteger secUnidad, BigInteger valor, Date fechaCambio);

   public void undoAdicionaSueldoCM2(BigInteger secMotivoCS, BigInteger secTipoSueldo, BigInteger secUnidad, BigInteger valor, Date fechaCambio);

   public void adicionaNovedadCM2(String tipo, BigInteger secConcepto, BigInteger secPeriodicidad,
           BigInteger secTercero, BigInteger secFormula, BigInteger valor,
           BigInteger saldo, Date fechaCambioInicial, Date fechaCambioFinal,
           BigInteger unidadParteEntera, BigInteger unidadParteFraccion);

   public void undoAdicionaNovedadCM2(String tipo, BigInteger secConcepto, BigInteger secPeriodicidad,
           BigInteger secTercero, BigInteger secFormula, BigInteger valor,
           BigInteger saldo, Date fechaCambioInicial, Date fechaCambioFinal);

   public void adicionaReingresoCM2(Date fechaIni, Date fechaFin);

   public void adicionaEmplJefeCM2(BigInteger secEmpleado, Date fechaCambio);

   public void adicionaPapelCM2(BigInteger secPapel, Date fechaCambio);

   public void adicionaAusentismoCM2(BigInteger secTipo, BigInteger secClase, BigInteger secCausa, BigInteger dias,
           BigInteger horas, Date fechaIniAusen, Date fechaFinAusen, Date fechaExpedicion,
           Date fechaIpago, Date fechaPago, BigInteger porcent, BigInteger baseliq, String forma);

   public void undoAdicionaAusentismoCM2(BigInteger secTipo, BigInteger secClase,
           BigInteger secCausa, BigInteger dias, Date fechaIniAusen, Date fechaFinAusen);

}

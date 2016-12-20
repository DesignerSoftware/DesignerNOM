/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.CambiosMasivos;
//import Entidades.CentrosCostos;
//import Entidades.Clasesausentismos;
//import Entidades.Conceptos;
//import Entidades.Empleados;
//import Entidades.Estructuras;
//import Entidades.Formulas;
//import Entidades.Ibcs;
//import Entidades.MotivosCambiosSueldos;
//import Entidades.MotivosDefinitivas;
//import Entidades.MotivosRetiros;
//import Entidades.Papeles;
import Entidades.Parametros;
import Entidades.ParametrosCambiosMasivos;
//import Entidades.Periodicidades;
//import Entidades.Terceros;
//import Entidades.TercerosSucursales;
//import Entidades.TiposEntidades;
//import Entidades.TiposSueldos;
//import Entidades.Tiposausentismos;
//import Entidades.Unidades;
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

   public boolean actualizarParametroCambioM(ParametrosCambiosMasivos parametro);
}

//   public List<Estructuras> consultarLovCargos_Extructuras();
//
//   public List<MotivosDefinitivas> consultarLovMotivosDefinitivas();
//
//   public List<MotivosRetiros> consultarLovMotivosRetiros();
//
//   public List<TiposEntidades> consultarLovTiposEntidades();
//
//   public List<TercerosSucursales> consultarLovTercerosSucursales();
//
//   public List<CentrosCostos> consultarLovCentrosCostos();
//
//   public List<Periodicidades> consultarLovPeriodicidades();
//
//   public List<Conceptos> consultarLovConceptos();
//
//   public List<Formulas> consultarLovFormulas();
//
//   public List<Terceros> consultarLovTerceros();
//
//   public List<MotivosCambiosSueldos> consultarLovMotivosCambiosSueldos();
//
//   public List<TiposSueldos> consultarLovTiposSueldos();
//
//   public List<Tiposausentismos> consultarLovTiposausentismos();
//
//   public List<Unidades> consultarLovUnidades();
//
//   public List<Empleados> consultarLovEmpleados();
//
//   public List<Clasesausentismos> consultarLovExtructuras();
//
//   public List<Ibcs> consultarLovIbcs();
//
//   public List<Papeles> consultarLovPapeles();

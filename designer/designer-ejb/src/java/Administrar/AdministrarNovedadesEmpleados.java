/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Conceptos;
import Entidades.Empleados;
import Entidades.Formulas;
import Entidades.Novedades;
import Entidades.Periodicidades;
import Entidades.PruebaEmpleados;
import Entidades.Terceros;
import Entidades.Usuarios;
import Entidades.VWActualesTiposTrabajadores;
import InterfaceAdministrar.AdministrarNovedadesEmpleadosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaConceptosInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaFormulasInterface;
import InterfacePersistencia.PersistenciaNovedadesInterface;
import InterfacePersistencia.PersistenciaPeriodicidadesInterface;
import InterfacePersistencia.PersistenciaPruebaEmpleadosInterface;
import InterfacePersistencia.PersistenciaSolucionesFormulasInterface;
import InterfacePersistencia.PersistenciaTercerosInterface;
import InterfacePersistencia.PersistenciaUsuariosInterface;
import InterfacePersistencia.PersistenciaVWActualesTiposTrabajadoresInterface;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

@Stateful
public class AdministrarNovedadesEmpleados implements AdministrarNovedadesEmpleadosInterface {

   private static Logger log = Logger.getLogger(AdministrarNovedadesEmpleados.class);

   @EJB
   PersistenciaPruebaEmpleadosInterface persistenciaPruebaEmpleados;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleados;
   @EJB
   PersistenciaVWActualesTiposTrabajadoresInterface persistenciaVWActualesTiposTrabajadores;
   @EJB
   PersistenciaNovedadesInterface persistenciaNovedades;
   @EJB
   PersistenciaConceptosInterface persistenciaConceptos;
   @EJB
   PersistenciaFormulasInterface persistenciaFormulas;
   @EJB
   PersistenciaPeriodicidadesInterface persistenciaPeriodicidades;
   @EJB
   PersistenciaTercerosInterface persistenciaTerceros;
   @EJB
   PersistenciaActualUsuarioInterface persistenciaActualUsuario;
   @EJB
   PersistenciaUsuariosInterface persistenciaUsuarios;
   @EJB
   PersistenciaSolucionesFormulasInterface persistenciaSolucionesFormulas;
   /**
    * Enterprise JavaBean.<br> Atributo que representa todo lo referente a la
    * conexión del usuario que está usando el aplicativo.
    */
   @EJB
   AdministrarSesionesInterface administrarSesiones;
   private EntityManagerFactory emf;
   private EntityManager em;

   private EntityManager getEm() {
      try {
         if (this.em != null) {
            if (this.em.isOpen()) {
               this.em.close();
            }
         }
         this.em = emf.createEntityManager();
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " getEm() ERROR : " + e);
      }
      return this.em;
   }

   @Override
   public void obtenerConexion(String idSesion) {
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }
   //Trae los empleados con Novedades dependiendo el Tipo de Trabajador que sea.

   @Override
   public int cuantosEmpleadosNovedad() {
      try {
         return persistenciaEmpleados.contarEmpleadosNovedad(getEm());
      } catch (Exception e) {
         log.error("Error AdministrarNovedadesEmpleados.cuantosEmpleadosNovedad : " + e);
         return -1;
      }
   }

   @Override
   public List<PruebaEmpleados> empleadosNovedadSoloAlgunos() {
      try {
         List<Empleados> listaEmpleados = persistenciaEmpleados.empleadosNovedadSoloAlgunos(getEm());
         List<PruebaEmpleados> listaEmpleadosNovedad = new ArrayList<PruebaEmpleados>();
         for (int i = 0; i < listaEmpleados.size(); i++) {
            //Traer los datos del empleado con sueldo actual
            PruebaEmpleados p = persistenciaPruebaEmpleados.empleadosAsignacion(getEm(), listaEmpleados.get(i).getSecuencia());

            if (p != null) {
//                p.setTipo(tipo);
               listaEmpleadosNovedad.add(p);
            } else {
               p = new PruebaEmpleados();
               p.setCodigo(listaEmpleados.get(i).getCodigoempleado());
               p.setId(listaEmpleados.get(i).getSecuencia());
               p.setNombre(listaEmpleados.get(i).getPersona().getNombreCompleto());
//                p.setTipo(tipo);
               p.setValor(null);
               listaEmpleadosNovedad.add(p);
            }
         }
         return listaEmpleadosNovedad;
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   //
//        List<Empleados> listaEmpleados = persistenciaEmpleados.empleadosNovedad(getEm());
//        List<PruebaEmpleados> listaEmpleadosNovedad = new ArrayList<PruebaEmpleados>();
//        for (int i = 0; i < listaEmpleados.size(); i++) {
//            //Traer los datos del empleado con sueldo actual
//            PruebaEmpleados p = persistenciaPruebaEmpleados.empleadosAsignacion(getEm(), listaEmpleados.get(i).getSecuencia());
//
//            if (p != null) {
////                p.setTipo(tipo);
//                listaEmpleadosNovedad.add(p);
//            } else {
//                p = new PruebaEmpleados();
//                p.setCodigo(listaEmpleados.get(i).getCodigoempleado());
//                p.setId(listaEmpleados.get(i).getSecuencia());
//                p.setNombre(listaEmpleados.get(i).getPersona().getNombreCompleto());
////                p.setTipo(tipo);
//                p.setValor(null);
//                listaEmpleadosNovedad.add(p);
//            }
//        }
   @Override
   public List<PruebaEmpleados> empleadosNovedades() {
      log.warn("Administrar.AdministrarNovedadesEmpleados.empleadosNovedades()");
      try {
         return persistenciaPruebaEmpleados.empleadosNovedadesEmple(getEm());
      } catch (Exception e) {
         log.warn("Error empleadosNovedad() e: " + e);
         return null;
      }
   }

   //Trae las novedades del empleado cuya secuencia se envía como parametro//
   @Override
   public List<Novedades> novedadesEmpleado(BigInteger secuenciaEmpleado) {
      log.warn("novedadesEmpleado() secuenciaEmpleado: " + secuenciaEmpleado);
      try {
         return persistenciaNovedades.novedadesEmpleado(getEm(), secuenciaEmpleado);
      } catch (Exception e) {
         log.error("Error AdministrarNovedadesEmpleados.novedadesEmpleado" + e);
         return null;
      }
   }

   @Override
   public List<Novedades> todasNovedades(BigInteger secuenciaEmpleado) {
      log.warn("Administrar.AdministrarNovedadesEmpleados.todasNovedades() secuenciaEmpleado:" + secuenciaEmpleado);
      try {
         return persistenciaNovedades.todasNovedadesEmpleado(getEm(), secuenciaEmpleado);
      } catch (Exception e) {
         log.error("Error AdministrarNovedadesEmpleados.todasNovedades" + e);
         return null;
      }
   }

   //Ver si está en soluciones formulas y de ser asi no borrarlo
   @Override
   public int solucionesFormulas(BigInteger secuenciaNovedad) {
      try {
         return persistenciaSolucionesFormulas.validarNovedadesNoLiquidadas(getEm(), secuenciaNovedad);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return 0;
      }
   }

   @Override
   public String alias() {
      try {
         return persistenciaActualUsuario.actualAliasBD(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public Usuarios usuarioBD(String alias) {
      try {
         return persistenciaUsuarios.buscarUsuario(getEm(), alias);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   //Procesa un solo empleado para volverlo Pruebaempleado
   @Override
   public PruebaEmpleados novedadEmpleado(BigInteger secuenciaEmpleado) {
      try {
         return persistenciaPruebaEmpleados.empleadosAsignacion(getEm(), secuenciaEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   //Busca el empleado con el Id que se envía
   @Override
   public Empleados elEmpleado(BigInteger secuenciaEmpleado) {
      try {
         return persistenciaEmpleados.buscarEmpleado(getEm(), secuenciaEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   //Listas de Conceptos, Formulas, Periodicidades, Terceros
   @Override
   public List<Conceptos> lovConceptos() {
      try {
         return persistenciaConceptos.buscarConceptosLovNovedades(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Formulas> lovFormulas() {
      try {
         return persistenciaFormulas.buscarFormulas(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Periodicidades> lovPeriodicidades() {
      try {
         return persistenciaPeriodicidades.consultarPeriodicidades(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Terceros> lovTerceros() {
      try {
         return persistenciaTerceros.buscarTerceros(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Empleados> lovEmpleados() {
      try {
         return persistenciaEmpleados.empleadosNovedad(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   // Que tipo de Trabajador es
   @Override
   public List<VWActualesTiposTrabajadores> tiposTrabajadores() {
      try {
         return persistenciaVWActualesTiposTrabajadores.tipoTrabajadorEmpleado(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public Date vigenciaTipoContratoSecEmpleado(BigInteger secuencia) {
      try {
         return persistenciaVWActualesTiposTrabajadores.consultarFechaVigencia(getEm(), secuencia);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void borrarNovedades(Novedades novedades) {
      try {
         persistenciaNovedades.borrar(getEm(), novedades);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearNovedades(Novedades novedades) {
      try {
         persistenciaNovedades.crear(getEm(), novedades);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void modificarNovedades(List<Novedades> listaNovedadesModificar) {
      try {
         for (int i = 0; i < listaNovedadesModificar.size(); i++) {
            log.warn("Modificando...");
            if (listaNovedadesModificar.get(i).getTercero().getSecuencia() == null) {
               listaNovedadesModificar.get(i).setTercero(null);
            }
            if (listaNovedadesModificar.get(i).getPeriodicidad().getSecuencia() == null) {
               listaNovedadesModificar.get(i).setPeriodicidad(null);
            }
            if (listaNovedadesModificar.get(i).getSaldo() == null) {
               listaNovedadesModificar.get(i).setSaldo(null);
            }
            if (listaNovedadesModificar.get(i).getUnidadesparteentera() == null) {
               listaNovedadesModificar.get(i).setUnidadesparteentera(null);
            }
            if (listaNovedadesModificar.get(i).getUnidadespartefraccion() == null) {
               listaNovedadesModificar.get(i).setUnidadespartefraccion(null);
            }
            persistenciaNovedades.editar(getEm(), listaNovedadesModificar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }
}

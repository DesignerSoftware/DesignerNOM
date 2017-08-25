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
import Entidades.Terceros;
import Entidades.Usuarios;
import InterfaceAdministrar.AdministrarNovedadesConceptosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaActualUsuarioInterface;
import InterfacePersistencia.PersistenciaConceptosInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaFormulasInterface;
import InterfacePersistencia.PersistenciaNovedadesInterface;
import InterfacePersistencia.PersistenciaPeriodicidadesInterface;
import InterfacePersistencia.PersistenciaSolucionesFormulasInterface;
import InterfacePersistencia.PersistenciaTercerosInterface;
import InterfacePersistencia.PersistenciaUsuariosInterface;
import InterfacePersistencia.PersistenciaVWActualesTiposTrabajadoresInterface;
import InterfacePersistencia.PersistenciaVigenciasTiposContratosInterface;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarNovedadesConceptos implements AdministrarNovedadesConceptosInterface {

   private static Logger log = Logger.getLogger(AdministrarNovedadesConceptos.class);

   @EJB
   PersistenciaNovedadesInterface persistenciaNovedades;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleados;
   @EJB
   PersistenciaVWActualesTiposTrabajadoresInterface persistenciaVWActualesTiposTrabajadores;
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
   @EJB
   PersistenciaVigenciasTiposContratosInterface persistenciaVigenciasTiposContratos;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
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

   //Trae las novedades del empleado cuya secuencia se envía como parametro//
   public List<Novedades> novedadesConcepto(BigInteger secuenciaConcepto) {
      try {
         return persistenciaNovedades.novedadesConcepto(getEm(), secuenciaConcepto);
      } catch (Exception e) {
         log.error("Error AdministrarNovedadesConceptos.conceptosNovedades" + e);
         return null;
      }
   }

   //Listas de Conceptos, Formulas, Periodicidades, Terceros
   public List<Conceptos> Conceptos() {
      try {
         return persistenciaConceptos.buscarConceptosLovNovedades(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".obtenerConexion ERROR: " + e);
         return null;
      }
   }

   public List<Terceros> Terceros() {
      try {
         return persistenciaTerceros.buscarTerceros(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".Terceros ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Formulas> lovFormulas() {
      try {
         return persistenciaFormulas.buscarFormulas(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".lovFormulas ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Periodicidades> lovPeriodicidades() {
      try {
         return persistenciaPeriodicidades.consultarPeriodicidades(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".lovPeriodicidades ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Terceros> lovTerceros() {
      try {
         return persistenciaTerceros.buscarTerceros(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".lovTerceros ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Empleados> lovEmpleados() {
      try {
         return persistenciaEmpleados.empleadosNovedad(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".lovEmpleados ERROR: " + e);
         return null;
      }
   }

   //Ver si está en soluciones formulas y de ser asi no borrarlo
   public int solucionesFormulas(BigInteger secuenciaNovedad) {
      try {
         return persistenciaSolucionesFormulas.validarNovedadesNoLiquidadas(getEm(), secuenciaNovedad);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".solucionesFormulas ERROR: " + e);
         return 0;
      }
   }
   //Usuarios

   public String alias() {
      try {
         return persistenciaActualUsuario.actualAliasBD(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".actualAliasBD ERROR: " + e);
         return null;
      }
   }

   public Usuarios usuarioBD(String alias) {
      try {
         return persistenciaUsuarios.buscarUsuario(getEm(), alias);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".usuarioBD ERROR: " + e);
         return null;
      }
   }

   @Override
   public void borrarNovedades(Novedades novedades) {
      try {
         persistenciaNovedades.borrar(getEm(), novedades);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarNovedadesERROR: " + e);
      }
   }

   @Override
   public void crearNovedades(Novedades novedades) {
      try {
         persistenciaNovedades.crear(getEm(), novedades);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearNovedades ERROR: " + e);
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
         log.error(this.getClass().getSimpleName() + ".modificarNovedades ERROR: " + e);
      }
   }

   public List<Novedades> todasNovedadesConcepto(BigInteger secuenciaConcepto) {
      try {
         return persistenciaNovedades.todasNovedadesConcepto(getEm(), secuenciaConcepto);
      } catch (Exception e) {
         log.error("Error AdministrarNovedadesConceptos.todasNovedadesConcepto" + e);
         return null;
      }
   }

   @Override
   public Date obtenerFechaContratacionEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaVigenciasTiposContratos.fechaFinalContratacionVacaciones(getEm(), secEmpleado);
      } catch (Exception e) {
         log.warn("Error obtenerFechaContratacionEmpleado Admi : " + e.toString());
         return null;
      }
   }
}

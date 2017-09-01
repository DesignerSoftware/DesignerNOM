/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Conceptos;
import Entidades.Empleados;
import Entidades.Novedades;
import Entidades.Periodicidades;
import Entidades.Terceros;
import InterfaceAdministrar.AdministrarEmplNovedadInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaConceptosInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaNovedadesInterface;
import InterfacePersistencia.PersistenciaPeriodicidadesInterface;
import InterfacePersistencia.PersistenciaTercerosInterface;
import java.math.BigInteger;
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
public class AdministrarEmplNovedad implements AdministrarEmplNovedadInterface {

   private static Logger log = Logger.getLogger(AdministrarEmplNovedad.class);

   @EJB
   PersistenciaNovedadesInterface persistenciaNovedades;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleado;
   @EJB
   PersistenciaConceptosInterface persistenciaConceptos;
   @EJB
   PersistenciaPeriodicidadesInterface persistenciaPeriodicidad;
   @EJB
   PersistenciaTercerosInterface persistenciaTercero;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
   @EJB
   AdministrarSesionesInterface administrarSesiones;

   private EntityManagerFactory emf;
   private EntityManager em; private String idSesionBck;

   private EntityManager getEm() {
      try {
         if (this.emf != null) { if (this.em != null) {
            if (this.em.isOpen()) {
               this.em.close();
            }
         }
         } else {
            this.emf = administrarSesiones.obtenerConexionSesionEMF(idSesionBck);
         }
         this.em = emf.createEntityManager();
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " getEm() ERROR : " + e);
      }
      return this.em;
   }

   @Override
   public void obtenerConexion(String idSesion) { idSesionBck = idSesion;
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public List<Novedades> listNovedadesEmpleado(BigInteger secuenciaE) {
      try {
         return persistenciaNovedades.todasNovedadesEmpleado(getEm(), secuenciaE);
      } catch (Exception e) {
         log.warn("Error listNovedadesEmpleado Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public Empleados actualEmpleado(BigInteger secuencia) {
      try {
         return persistenciaEmpleado.buscarEmpleadoSecuencia(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error actualEmpleado Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Conceptos> lovConceptos() {
      try {
         return persistenciaConceptos.buscarConceptos(getEm());
      } catch (Exception e) {
         log.warn("Error en lovConceptos:" + e.getMessage());
         return null;
      }
   }

   @Override
   public List<Periodicidades> lovPeriodicidades() {
      try {
         return persistenciaPeriodicidad.consultarPeriodicidades(getEm());
      } catch (Exception e) {
         log.warn("Error en lovPeriodicidades:" + e.getMessage());
         return null;
      }
   }

   @Override
   public List<Terceros> lovTerceros() {
      try {
         return persistenciaTercero.buscarTerceros(getEm());
      } catch (Exception e) {
         log.warn("Error en lovTerceros:" + e.getMessage());
         return null;
      }
   }

   @Override
   public void editarNovedad(List<Novedades> listaModificar) {
      try {
         for (int i = 0; i < listaModificar.size(); i++) {
            if (listaModificar.get(i).getTercero().getSecuencia() == null) {
               listaModificar.get(i).setTercero(null);
            }

            if (listaModificar.get(i).getPeriodicidad().getSecuencia() == null) {
               listaModificar.get(i).setPeriodicidad(null);
            }

            if (listaModificar.get(i).getConcepto() == null) {
               listaModificar.get(i).setConcepto(new Conceptos());
            }
            persistenciaNovedades.editar(getEm(), listaModificar.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarNovedad(List<Novedades> listaBorrar) {
      try {
         for (int i = 0; i < listaBorrar.size(); i++) {
            if (listaBorrar.get(i).getTercero().getSecuencia() == null) {
               listaBorrar.get(i).setTercero(null);
            }

            if (listaBorrar.get(i).getPeriodicidad().getSecuencia() == null) {
               listaBorrar.get(i).setPeriodicidad(null);
            }

            if (listaBorrar.get(i).getConcepto() == null) {
               listaBorrar.get(i).setConcepto(new Conceptos());
            }
            persistenciaNovedades.borrar(getEm(), listaBorrar.get(i));
         }
      } catch (Exception e) {
         log.warn("error en BorrarNovedad" + e.getMessage());
      }
   }

}

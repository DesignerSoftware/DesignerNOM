/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.NovedadesSistema;
import Entidades.Vacaciones;
import InterfaceAdministrar.AdministrarDetalleVacacionInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaNovedadesSistemaInterface;
import InterfacePersistencia.PersistenciaVacacionesInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
@LocalBean
public class AdministrarDetalleVacacion implements AdministrarDetalleVacacionInterface {

   private static Logger log = Logger.getLogger(AdministrarDetalleVacacion.class);

   @EJB
   PersistenciaNovedadesSistemaInterface persistenciaNovedadesSistema;
   @EJB
   PersistenciaVacacionesInterface persistenciaVacaciones;

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
   public void modificarDetalleVacacion(NovedadesSistema novedadSistema) {
      try {
         persistenciaNovedadesSistema.editar(getEm(), novedadSistema);
      } catch (Exception e) {
         log.warn("Error en modificarDetalleVacacion() Admi : " + e.toString());
      }
   }

   @Override
   public List<NovedadesSistema> novedadsistemaPorEmpleadoYVacacion(BigInteger secEmpleado, BigInteger secVacacion) {
      try {
         return persistenciaNovedadesSistema.novedadsistemaPorEmpleadoYVacacion(getEm(), secEmpleado, secVacacion);
      } catch (Exception e) {
         log.warn("Error en novedadsistemaPorEmpleadoYVacacion() Admi : " + e.toString());
         return null;
      }
   }

   /**
    *
    * @param secNovedadSistema
    * @return
    */
   @Override
   public BigDecimal consultarValorTotalDetalleVacacion(BigInteger secNovedadSistema) {
      try {
         return persistenciaNovedadesSistema.consultarValorTotalDetalleVacacion(getEm(), secNovedadSistema);
      } catch (Exception e) {
         log.warn("Error en consultarValorTotalDetalleVacacion() Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Vacaciones> periodosEmpleado(BigInteger secuenciaEmpleado) {
      try {
         return persistenciaVacaciones.periodoVacaciones(getEm(), secuenciaEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".periodosEmpleado() ERROR: " + e);
         return null;
      }
   }

}

package Administrar;

import Entidades.Recordatorios;
import InterfaceAdministrar.AdministrarGeneraConsultaInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaRecordatoriosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author Edwin Hastamorir
 */
@Stateful
public class AdministrarGeneraConsulta implements AdministrarGeneraConsultaInterface {

   private static Logger log = Logger.getLogger(AdministrarGeneraConsulta.class);

   @EJB
   AdministrarSesionesInterface administrarSesiones;
   @EJB
   PersistenciaRecordatoriosInterface persistenciaRecordatorios;

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
      log.warn("AdministrarGeneraConsulta.obtenerConexion");
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public Recordatorios consultarPorSecuencia(BigInteger secuencia) {
      try {
         return persistenciaRecordatorios.consultaRecordatorios(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("consultarPorSecuencia en " + this.getClass().getName() + ": ");
         e.printStackTrace();
         return null;
      }
   }

   @Override
   public List<String> ejecutarConsulta(BigInteger secuencia) {
      log.warn("AdministrarGeneraConsulta.ejecutarConsulta");
      try {
         return persistenciaRecordatorios.ejecutarConsultaRecordatorio(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("ejecutarConsulta en " + this.getClass().getName() + ": ");
         e.printStackTrace();
         return null;
      }
   }

   @Override
   @Remove
   public void salir() {
   }

}

package Administrar;

import Entidades.ObjetosDB;
import Entidades.Rastros;
import Entidades.RastrosValores;
import InterfaceAdministrar.AdministrarRastrosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaObjetosDBInterface;
import InterfacePersistencia.PersistenciaRastrosInterface;
import InterfacePersistencia.PersistenciaRastrosTablasInterface;
import InterfacePersistencia.PersistenciaRastrosValoresInterface;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

@Stateful
public class AdministrarRastros implements AdministrarRastrosInterface, Serializable {

   private static Logger log = Logger.getLogger(AdministrarRastros.class);

   @EJB
   PersistenciaObjetosDBInterface persistenciaObjetosDB;
   @EJB
   PersistenciaRastrosTablasInterface persistenciaRastrosTablas;
   @EJB
   PersistenciaRastrosInterface persistenciaRastros;
   @EJB
   PersistenciaRastrosValoresInterface persistenciaRastrosValores;
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

   //1 = EL NOMBRE DE LA TABLA NO EXISTE EN OBJETOSDB
   //2 = RASTROS DETECTADOS PARA EL REGISTRO
   //3 = EL REGISTRO DE LA TABLA NO TIENE RASTROS
   //4 = LA TABLA NO EXISTE EN RASTROS TABLAS PERO TIENE REGISTROS EN RASTROS.
   //5 = LA TABLA NO EXISTE EN RASTROS TABLAS Y NO TIENE REGISTROS EN RASTROS.
   @Override
   public int obtenerTabla(BigInteger secRegistro, String nombreTabla) {
      try {
         ObjetosDB objetoDB = persistenciaObjetosDB.obtenerObjetoTabla(getEm(), nombreTabla);
         if (objetoDB != null) {
            if (persistenciaRastrosTablas.verificarRastroTabla(getEm(), objetoDB.getSecuencia())) {
               if (persistenciaRastros.verificarRastroRegistroTabla(getEm(), secRegistro, nombreTabla)) {
                  return 2;
               } else {
                  return 3;
               }
            } else if (persistenciaRastros.verificarRastroRegistroTabla(getEm(), secRegistro, nombreTabla)) {
               return 4;
            } else {
               return 5;
            }
         } else {
            return 1;
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return 0;
      }
   }

   @Override
   public boolean verificarHistoricosTabla(String nombreTabla) {
      try {
         if (persistenciaRastros.verificarRastroHistoricoTabla(getEm(), nombreTabla)) {
            return true;
         } else {
            return false;
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return false;
      }
   }

   @Override
   public List<Rastros> rastrosTabla(BigInteger secRegistro, String nombreTabla) {
      try {
         return persistenciaRastros.rastrosTabla(getEm(), secRegistro, nombreTabla);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Rastros> rastrosTablaHistoricos(String nombreTabla) {
      try {
         return persistenciaRastros.rastrosTablaHistoricos(getEm(), nombreTabla);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Rastros> rastrosTablaHistoricosEliminados(String nombreTabla) {
      try {
         return persistenciaRastros.rastrosTablaHistoricosEliminados(getEm(), nombreTabla);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Rastros> rastrosTablaHistoricosEliminadosEmpleado(String nombreTabla) {
      try {
         return persistenciaRastros.rastrosTablaHistoricosEliminadosEmpleados(getEm(), nombreTabla);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Rastros> rastrosTablaFecha(Date fechaRegistro, String nombreTabla) {
      try {
         return persistenciaRastros.rastrosTablaFecha(getEm(), fechaRegistro, nombreTabla);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<RastrosValores> valorRastro(BigInteger secRastro) {
      try {
         return persistenciaRastrosValores.rastroValores(getEm(), secRastro);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public boolean existenciaEmpleadoTabla(String nombreTabla) {
      try {
         return persistenciaRastros.verificarEmpleadoTabla(getEm(), nombreTabla);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return false;
      }
   }

}

package Administrar;

import Entidades.ActualUsuario;
import Entidades.Empleados;
import Entidades.ReformasLaborales;
import Entidades.VigenciasReformasLaborales;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarVigenciasReformasLaboralesInterface;
import InterfacePersistencia.*;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author AndresPineda
 */
@Stateful
public class AdministrarVigenciasReformasLaborales implements AdministrarVigenciasReformasLaboralesInterface {

   private static Logger log = Logger.getLogger(AdministrarVigenciasReformasLaborales.class);

   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleado;
   @EJB
   PersistenciaReformasLaboralesInterface persistenciaReformasLaborales;
   @EJB
   PersistenciaVigenciasReformasLaboralesInterface persistenciaVigenciasReformasLaborales;
   @EJB
   PersistenciaActualUsuarioInterface persistenciaActualUsuario;
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
   //LOGS//
   private ActualUsuario actualUsuario;
   private final static Logger logger = Logger.getLogger("connectionSout");
   private Date fechaDia;
   private final SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");

   private void configurarLog() {
      //PropertyConfigurator.configure("log4j.properties");
   }

   @Override
   public void obtenerConexion(String idSesion) { idSesionBck = idSesion;
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
      //configurarLog();
   }

   @Override
   public List<VigenciasReformasLaborales> vigenciasReformasLaboralesEmpleado(BigInteger secEmpleado) {
      try {
         return persistenciaVigenciasReformasLaborales.buscarVigenciasReformasLaboralesEmpleado(getEm(), secEmpleado);
      } catch (Exception e) {
         logger.error("Metodo: vigenciasReformasLaboralesEmpleado - AdministrarVigenciasReformasLaborales - Fecha : " + format.format(fechaDia) + " - Usuario : " + actualUsuario.getAlias() + " - Error : " + e.toString());
         return null;
      }
   }

   @Override
   public void modificarVRL(List<VigenciasReformasLaborales> listVRLModificadas) {
      try {
         for (int i = 0; i < listVRLModificadas.size(); i++) {
            persistenciaVigenciasReformasLaborales.editar(getEm(), listVRLModificadas.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarVRL(VigenciasReformasLaborales vigenciasReformasLaborales) {
      try {
         persistenciaVigenciasReformasLaborales.borrar(getEm(), vigenciasReformasLaborales);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearVRL(VigenciasReformasLaborales vigenciasReformasLaborales) {
      try {
         persistenciaVigenciasReformasLaborales.crear(getEm(), vigenciasReformasLaborales);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public Empleados buscarEmpleado(BigInteger secuencia) {
      try {
         return persistenciaEmpleado.buscarEmpleadoSecuencia(getEm(), secuencia);
      } catch (Exception e) {
         logger.error("Metodo: buscarEmpleado - AdministrarVigenciasReformasLaborales - Fecha : " + format.format(fechaDia) + " - Usuario : " + actualUsuario.getAlias() + " - Error : " + e.toString());
         return null;
      }
   }

   @Override
   public List<ReformasLaborales> reformasLaborales() {
      try {
         return persistenciaReformasLaborales.buscarReformasLaborales(getEm());
      } catch (Exception e) {
         logger.error("Metodo: reformasLaborales - AdministrarVigenciasReformasLaborales - Fecha : " + format.format(fechaDia) + " - Usuario : " + actualUsuario.getAlias() + " - Error : " + e.toString());
         return null;
      }
   }

   @Override
   public ActualUsuario obtenerActualUsuario() {
      try {
         actualUsuario = persistenciaActualUsuario.actualUsuarioBD(getEm());
         return actualUsuario;
      } catch (Exception e) {
         logger.error("Metodo: obtenerActualUsuario - AdministrarVigenciasReformasLaborales - Fecha : " + format.format(fechaDia) + " - Usuario : " + actualUsuario.getAlias() + " - Error : " + e.toString());
         return null;
      }
   }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import Entidades.HVHojasDeVida;
import Entidades.HvEntrevistas;
import InterfaceAdministrar.AdministrarHvEntrevistasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaHVHojasDeVidaInterface;
import InterfacePersistencia.PersistenciaHvEntrevistasInterface;
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
public class AdministrarHvEntrevistas implements AdministrarHvEntrevistasInterface {

   private static Logger log = Logger.getLogger(AdministrarHvEntrevistas.class);

   @EJB
   PersistenciaHvEntrevistasInterface persistenciaHvEntrevistas;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleados;
   @EJB
   PersistenciaHVHojasDeVidaInterface persistenciaHVHojasDeVida;
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
   public void modificarHvEntrevistas(List<HvEntrevistas> listHvEntrevistas) {
      try {
         for (int i = 0; i < listHvEntrevistas.size(); i++) {
            log.warn("Modificando...");
            persistenciaHvEntrevistas.editar(getEm(), listHvEntrevistas.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarHvEntrevistas(List<HvEntrevistas> listHvEntrevistas) {
      try {
         for (int i = 0; i < listHvEntrevistas.size(); i++) {
            log.warn("Borrando...");
            persistenciaHvEntrevistas.borrar(getEm(), listHvEntrevistas.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearHvEntrevistas(List<HvEntrevistas> listHvEntrevistas) {
      try {
         for (int i = 0; i < listHvEntrevistas.size(); i++) {
            if (listHvEntrevistas.get(i).getHojadevida() == null) {
               listHvEntrevistas.get(i).setHojadevida(new HVHojasDeVida());
            }
            persistenciaHvEntrevistas.crear(getEm(), listHvEntrevistas.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<HvEntrevistas> consultarHvEntrevistasPorEmpleado(BigInteger secPersona) {
      try {
         return persistenciaHvEntrevistas.buscarHvEntrevistasPorEmpleado(getEm(), secPersona);
      } catch (Exception e) {
         log.warn("Error en AdministrarHvEntrevistas hvEntrevistasPorEmplado");
         return null;
      }
   }

   @Override
   public HvEntrevistas consultarHvEntrevista(BigInteger secHvEntrevista) {
      try {
         return persistenciaHvEntrevistas.buscarHvEntrevista(getEm(), secHvEntrevista);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public Empleados consultarEmpleado(BigInteger secuencia) {
      try {
         return persistenciaEmpleados.buscarEmpleadoSecuencia(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("ERROR AdministrarHvEntrevistas  buscarEmpleado ERROR =====" + e);
         return null;
      }
   }

   @Override
   public List<HVHojasDeVida> buscarHVHojasDeVida(BigInteger secuencia) {
      try {
         return persistenciaHvEntrevistas.buscarHvHojaDeVidaPorPersona(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("ERROR AdministrarHvEntrevistas  buscarHVHojasDeVida ERROR =====" + e);
         return null;
      }
   }

   @Override
   public HVHojasDeVida obtenerHojaVidaPersona(BigInteger secuencia) {
      try {
         return persistenciaHVHojasDeVida.hvHojaDeVidaPersona(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error obtenerHojaVidaPersona Admi : " + e.toString());
         return null;
      }
   }
}

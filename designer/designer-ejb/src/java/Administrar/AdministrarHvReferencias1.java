/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import Entidades.HVHojasDeVida;
import Entidades.HvReferencias;
import Entidades.Personas;
import Entidades.TiposFamiliares;
import InterfaceAdministrar.AdministrarHvReferencias1Interface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaHvReferencias1Interface;
import InterfacePersistencia.PersistenciaPersonasInterface;
import InterfacePersistencia.PersistenciaTiposFamiliaresInterface;
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
public class AdministrarHvReferencias1 implements AdministrarHvReferencias1Interface {

   private static Logger log = Logger.getLogger(AdministrarHvReferencias1.class);

   @EJB
   PersistenciaHvReferencias1Interface persistenciaHvReferencias;
   @EJB
   PersistenciaTiposFamiliaresInterface persistenciaTiposFamiliares;
   @EJB
   PersistenciaPersonasInterface persistenciaPersonas;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleado;
   HvReferencias hvReferencias;
   List<HVHojasDeVida> hvHojasDeVida;
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

   @Override
   public void borrarHvReferencias(List<HvReferencias> listaHvReferencias) {
      try {
         for (int i = 0; i < listaHvReferencias.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaHvReferencias.borrar(getEm(), listaHvReferencias.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearHvReferencias(List<HvReferencias> listaHvReferencias) {
      try {
         for (int i = 0; i < listaHvReferencias.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaHvReferencias.crear(getEm(), listaHvReferencias.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void modificarHvReferencias(List<HvReferencias> listaHvReferencias) {
      try {
         for (int i = 0; i < listaHvReferencias.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaHvReferencias.editar(getEm(), listaHvReferencias.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<HvReferencias> consultarHvReferenciasFamiliaresPorPersona(BigInteger secEmpleado) {
      try {
         return persistenciaHvReferencias.consultarHvReferenciasFamiliarPorPersona(getEm(), secEmpleado);
      } catch (Exception e) {
         log.warn("Error en AdministrarHvReferencias hvEntrevistasPorEmplado");
         return null;
      }
   }

   @Override
   public HvReferencias consultarHvReferencia(BigInteger secHvEntrevista) {
      try {
         persistenciaHvReferencias.buscarHvReferencia(getEm(), secHvEntrevista);
         return hvReferencias;
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public Personas consultarPersonas(BigInteger secuencia) {
      try {
         return persistenciaPersonas.buscarPersona(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("ERROR AdministrarHvReferencias  consultarPersonas ERROR =====" + e);
         return null;
      }
   }

   @Override
   public List<HVHojasDeVida> consultarHvHojasDeVida(BigInteger secuencia) {
      try {
         hvHojasDeVida = persistenciaHvReferencias.consultarHvHojaDeVidaPorPersona(getEm(), secuencia);
         return hvHojasDeVida;
      } catch (Exception e) {
         hvHojasDeVida = null;
         log.warn("ERROR AdministrarHvReferencias  buscarHvHojasDeVida ERROR =====" + e);
         return hvHojasDeVida;
      }
   }

   @Override
   public List<TiposFamiliares> consultarLOVTiposFamiliares() {
      try {
         return persistenciaTiposFamiliares.buscarTiposFamiliares(getEm());
      } catch (Exception e) {
         log.error("ERROR EN ADMINISTRAR HV REFERENCIAS 1 ERROR " + e);
         return null;
      }
   }

   @Override
   public Empleados empleadoActual(BigInteger secuenciaP) {
      try {
         return persistenciaEmpleado.buscarEmpleado(getEm(), secuenciaP);
      } catch (Exception e) {
         log.warn("Error empleadoActual Admi : " + e.toString());
         return null;
      }
   }
}

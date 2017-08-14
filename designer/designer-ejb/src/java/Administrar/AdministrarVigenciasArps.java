/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Cargos;
import Entidades.Estructuras;
import Entidades.VigenciasArps;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfaceAdministrar.AdministrarVigenciasArpsInterface;
import InterfacePersistencia.PersistenciaCargosInterface;
import InterfacePersistencia.PersistenciaEstructurasInterface;
import InterfacePersistencia.PersistenciaVigenciasArpsInterface;
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
public class AdministrarVigenciasArps implements AdministrarVigenciasArpsInterface {

   private static Logger log = Logger.getLogger(AdministrarVigenciasArps.class);

   @EJB
   AdministrarSesionesInterface administrarSesiones;
   @EJB
   PersistenciaVigenciasArpsInterface persistenciaVigenciasArp;
   @EJB
   PersistenciaEstructurasInterface persistenciaEstructuras;
   @EJB
   PersistenciaCargosInterface persistenciaCargos;
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
   public void modificarVArp(VigenciasArps vigarp) {
      try {
         persistenciaVigenciasArp.editar(getEm(), vigarp);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarVArp(VigenciasArps vigarp) {
      try {
         persistenciaVigenciasArp.borrar(getEm(), vigarp);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearVArp(VigenciasArps vigarp) {
      try {
         persistenciaVigenciasArp.crear(getEm(), vigarp);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public String buscarPorcentaje(BigInteger estructura, BigInteger cargo, Date fecha) {
      log.warn("estructura : " + estructura);
      log.warn("cargo : " + cargo);
      log.warn("fecha : " + fecha);
      try {
         return persistenciaVigenciasArp.actualARP(getEm(), estructura, cargo, fecha);
      } catch (Exception e) {
         log.warn("error en AdministrarVigenciasArps.buscarPorcentaje() : " + e.toString());
         return null;
      }
   }

   public int contarVigenciasARPsPorEstructuraYCargo(BigInteger estructura, BigInteger cargo) {
      try {
         return persistenciaVigenciasArp.contarVigenciasARPsPorEstructuraYCargo(getEm(), estructura, cargo);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return 0;
      }
   }

   public List<VigenciasArps> consultarVigenciasArps() {
      try {
         return persistenciaVigenciasArp.consultarVigenciasArps(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Estructuras> consultarTodoEstructuras() {
      try {
         return persistenciaEstructuras.buscarEstructuras(getEm());
      } catch (Exception ex) {
         return null;
      }
   }

   @Override
   public List<Cargos> consultarTodoCargos() {
      try {
         return persistenciaCargos.consultarCargos(getEm());
      } catch (Exception ex) {
         return null;
      }
   }
}

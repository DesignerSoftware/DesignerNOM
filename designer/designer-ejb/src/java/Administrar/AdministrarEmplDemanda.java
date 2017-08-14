/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Demandas;
import Entidades.Empleados;
import Entidades.MotivosDemandas;
import InterfaceAdministrar.AdministrarEmplDemandaInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaDemandasInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaMotivosDemandasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateless
public class AdministrarEmplDemanda implements AdministrarEmplDemandaInterface {

   private static Logger log = Logger.getLogger(AdministrarEmplDemanda.class);

   @EJB
   PersistenciaDemandasInterface persistenciaDemadas;
   @EJB
   PersistenciaMotivosDemandasInterface persistenciaMotivosDemandas;
   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleado;
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
   public Empleados actualEmpleado(BigInteger secuencia) {
      try {
         return persistenciaEmpleado.buscarEmpleado(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error actualEmpleado Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<MotivosDemandas> listMotivosDemandas() {
      try {
         return persistenciaMotivosDemandas.buscarMotivosDemandas(getEm());
      } catch (Exception e) {
         log.warn("Error listMotivosDemandas Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Demandas> listDemandasEmpleadoSecuencia(BigInteger secuencia) {
      try {
         return persistenciaDemadas.demandasPersona(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error listDemandasEmpleadoSecuencia Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearDemandas(List<Demandas> listD) {
      try {
         for (int i = 0; i < listD.size(); i++) {
            if (listD.get(i).getMotivo().getSecuencia() == null) {
               listD.get(i).setMotivo(null);
            }
            persistenciaDemadas.crear(getEm(), listD.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearDemandas Admi : " + e.toString());
      }
   }

   @Override
   public void editarDemandas(List<Demandas> listD) {
      try {
         for (int i = 0; i < listD.size(); i++) {
            if (listD.get(i).getMotivo().getSecuencia() == null) {
               listD.get(i).setMotivo(null);
            }
            persistenciaDemadas.editar(getEm(), listD.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarDemandas Admi : " + e.toString());
      }
   }

   @Override
   public void borrarDemandas(List<Demandas> listD) {
      try {
         for (int i = 0; i < listD.size(); i++) {
            if (listD.get(i).getMotivo().getSecuencia() == null) {
               listD.get(i).setMotivo(null);
            }
            persistenciaDemadas.borrar(getEm(), listD.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarDemandas Admi : " + e.toString());
      }
   }
}

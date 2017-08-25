/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empleados;
import Entidades.MotivosCesantias;
import Entidades.NovedadesSistema;
import Entidades.VWActualesTiposTrabajadores;
import InterfaceAdministrar.AdministrarNovedadlCesantiasRCInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEmpleadoInterface;
import InterfacePersistencia.PersistenciaMotivosCesantiasInterface;
import InterfacePersistencia.PersistenciaVWActualesTiposTrabajadoresInterface;
import InterfacePersistencia.PersistenciaVigenciasTiposContratosInterface;
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
public class AdministrarNovedadCesantiasRC implements AdministrarNovedadlCesantiasRCInterface {

   private static Logger log = Logger.getLogger(AdministrarNovedadCesantiasRC.class);

   @EJB
   PersistenciaEmpleadoInterface persistenciaEmpleados;
   @EJB
   PersistenciaVigenciasTiposContratosInterface persistenciaVigenciasTiposContratos;
   @EJB
   PersistenciaVWActualesTiposTrabajadoresInterface persistenciaVWActualesTiposTrabajadores;
   @EJB
   PersistenciaMotivosCesantiasInterface persistenciaMotivosCensantias;
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
   public List<Empleados> empleadosCesantias() {
      try {
         return persistenciaEmpleados.buscarEmpleadosActivos(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<VWActualesTiposTrabajadores> tiposTrabajadores() {
      try {
         return persistenciaVWActualesTiposTrabajadores.tipoTrabajadorEmpleado(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<MotivosCesantias> consultarMotivosCesantias() {
      try {
         return persistenciaMotivosCensantias.buscarMotivosCesantias(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Empleados> empleadoscesantiasnoliquidados() {
      try {
         return persistenciaEmpleados.consultarCesantiasnoLiquidadas(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<NovedadesSistema> novedadesnoliquidadas(BigInteger secuenciaEmpleado) {
      try {
         return persistenciaEmpleados.novedadescesantiasnoliquidadas(getEm(), secuenciaEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<NovedadesSistema> todasnovedadescesantias(BigInteger secuenciaEmpleado) {
      try {
         return persistenciaEmpleados.novedadescesantiasnoliquidadas(getEm(), secuenciaEmpleado);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

}

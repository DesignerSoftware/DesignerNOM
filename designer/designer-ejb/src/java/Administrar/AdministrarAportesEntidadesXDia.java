/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.AportesEntidadesXDia;
import InterfaceAdministrar.AdministrarAportesEntidadesXDiaInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaAportesEntidadesXDiaInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
@Local
public class AdministrarAportesEntidadesXDia implements AdministrarAportesEntidadesXDiaInterface {

   private static Logger log = Logger.getLogger(AdministrarAportesEntidadesXDia.class);

   @EJB
   AdministrarSesionesInterface administrarSesiones;
   @EJB
   PersistenciaAportesEntidadesXDiaInterface persistenciaAportesEntidadesXDia;

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
   public List<AportesEntidadesXDia> consultarAportesEntidadesXDia() {
      try {
         return persistenciaAportesEntidadesXDia.consultarAportesEntidadesXDia(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<AportesEntidadesXDia> consultarAportesEntidadesPorEmpleadoMesYAnio(BigInteger secEmpleado, short mes, short ano) {
      try {
         return persistenciaAportesEntidadesXDia.consultarAportesEntidadesPorEmpleadoMesYAnio(getEm(), secEmpleado, mes, ano);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void crearAportesEntidadesXDia(List<AportesEntidadesXDia> listaAE) {
      try {
         for (int i = 0; i < listaAE.size(); i++) {
            persistenciaAportesEntidadesXDia.crear(getEm(), listaAE.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearAportesEntidadesXDia Admi : " + e.toString());
      }
   }

   @Override
   public void editarAportesEntidadesXDia(List<AportesEntidadesXDia> listAE) {
      try {
         for (int i = 0; i < listAE.size(); i++) {
            persistenciaAportesEntidadesXDia.editar(getEm(), listAE.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarAportesEntidadesXDia Admi : " + e.toString());
      }
   }

   @Override
   public void borrarAportesEntidadesXDia(List<AportesEntidadesXDia> listAE) {
      try {
         for (int i = 0; i < listAE.size(); i++) {
            persistenciaAportesEntidadesXDia.borrar(getEm(), listAE.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarAportesEntidadesXDia Admi : " + e.toString());
      }
   }

   @Override
   public BigDecimal consultarTarifas(BigInteger secEmpresa, short mes, short ano, BigInteger secEmpleado, BigInteger secTipoEntidad) {
      try {
         return persistenciaAportesEntidadesXDia.cosultarTarifa(getEm(), secEmpresa, secEmpleado, mes, ano, secTipoEntidad);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

}

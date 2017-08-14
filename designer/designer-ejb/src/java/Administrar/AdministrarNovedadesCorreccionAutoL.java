/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empresas;
import Entidades.NovedadesCorreccionesAutoLiquidaciones;
import Entidades.SucursalesPila;
import Entidades.Terceros;
import Entidades.TiposEntidades;
import InterfaceAdministrar.AdministrarNovedadesCorreccionAutoLInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaNovedadesCorreccionAutoLInterface;
import InterfacePersistencia.PersistenciaVWActualesTiposTrabajadoresInterface;
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
public class AdministrarNovedadesCorreccionAutoL implements AdministrarNovedadesCorreccionAutoLInterface {

   private static Logger log = Logger.getLogger(AdministrarNovedadesCorreccionAutoL.class);

   @EJB
   PersistenciaNovedadesCorreccionAutoLInterface persistenciaNovedadesAuto;
   @EJB
   PersistenciaVWActualesTiposTrabajadoresInterface persistenciaVWActualesTiposTrabajadores;
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
   public List<Empresas> empresasNovedadAuto() {
      try {
         return persistenciaNovedadesAuto.listaEmpresas(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Terceros> tercerosNovedadAuto() {
      try {
         return persistenciaNovedadesAuto.listaTerceros(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<TiposEntidades> tiposEntidadesNovedadAuto() {
      try {
         return persistenciaNovedadesAuto.listaTiposEntidades(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<SucursalesPila> sucursalesNovedadAuto(BigInteger secuenciaEmpresa) {
      try {
         return persistenciaNovedadesAuto.listasucursalesPila(getEm(), secuenciaEmpresa);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<NovedadesCorreccionesAutoLiquidaciones> listaNovedades(BigInteger anio, BigInteger mes, BigInteger secuenciaEmpresa) {
      try {
         return persistenciaNovedadesAuto.listaNovedades(getEm(), anio, mes, secuenciaEmpresa);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public void borrarNovedades(NovedadesCorreccionesAutoLiquidaciones novedad) {
      try {
         persistenciaNovedadesAuto.borrar(getEm(), novedad);
      } catch (Exception e) {
         log.warn("Error borrarNovedades Admi : " + e.toString());
      }
   }

   @Override
   public void crearNovedades(NovedadesCorreccionesAutoLiquidaciones novedad) {
      try {
         persistenciaNovedadesAuto.crear(getEm(), novedad);
      } catch (Exception e) {
         log.warn("Error crearNovedades Admi : " + e.toString());
      }
   }

   @Override
   public void editarNovedades(NovedadesCorreccionesAutoLiquidaciones novedad) {
      try {
         persistenciaNovedadesAuto.editar(getEm(), novedad);
      } catch (Exception e) {
         log.warn("Error editarNovedades Admi : " + e.toString());
      }
   }

   // Add business logic below. (Right-click in editor and choose
   // "Insert Code > Add Business Method")
}

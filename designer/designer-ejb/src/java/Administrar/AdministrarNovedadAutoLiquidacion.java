/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Empresas;
import Entidades.NovedadesAutoLiquidaciones;
import Entidades.SucursalesPila;
import Entidades.Terceros;
import Entidades.TiposEntidades;
import InterfaceAdministrar.AdministrarNovedadAutoLiquidacionInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaNovedadesAutoLiquidacionInterface;
import InterfacePersistencia.PersistenciaVWActualesTiposTrabajadoresInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

@Stateful
@Local
public class AdministrarNovedadAutoLiquidacion implements AdministrarNovedadAutoLiquidacionInterface {

   private static Logger log = Logger.getLogger(AdministrarNovedadAutoLiquidacion.class);

   @EJB
   PersistenciaNovedadesAutoLiquidacionInterface persistenciaNovedadesAuto;
   @EJB
   PersistenciaVWActualesTiposTrabajadoresInterface persistenciaVWActualesTiposTrabajadores;
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
   public List<Empresas> empresasNovedadAuto() {
      try {
         return persistenciaNovedadesAuto.listaEmpresas(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".empresasNovedadAuto() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Terceros> tercerosNovedadAuto() {
      try {
         return persistenciaNovedadesAuto.listaTerceros(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".tercerosNovedadAuto() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<TiposEntidades> tiposEntidadesNovedadAuto() {
      try {
         return persistenciaNovedadesAuto.listaTiposEntidades(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".tiposEntidadesNovedadAuto() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<SucursalesPila> sucursalesNovedadAuto(BigInteger secuenciaEmpresa) {
      try {
         return persistenciaNovedadesAuto.listasucursalesPila(getEm(), secuenciaEmpresa);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".sucursalesNovedadAuto() ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<NovedadesAutoLiquidaciones> listaNovedades(BigInteger anio, BigInteger mes, BigInteger secuenciaEmpresa) {
      try {
         return persistenciaNovedadesAuto.listaNovedades(getEm(), anio, mes, secuenciaEmpresa);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".listaNovedades() ERROR: " + e);
         return null;
      }
   }

   @Override
   public void borrarNovedades(NovedadesAutoLiquidaciones novedad) {
      try {
         persistenciaNovedadesAuto.borrar(getEm(), novedad);
      } catch (Exception e) {
         log.warn("Error borrarNovedades Admi : " + e.toString());
      }
   }

   @Override
   public void crearNovedades(NovedadesAutoLiquidaciones novedad) {
      try {
         persistenciaNovedadesAuto.crear(getEm(), novedad);
      } catch (Exception e) {
         log.warn("Error crearNovedades Admi : " + e.toString());
      }
   }

   @Override
   public void editarNovedades(NovedadesAutoLiquidaciones novedad) {
      try {
         persistenciaNovedadesAuto.editar(getEm(), novedad);
      } catch (Exception e) {
         log.warn("Error editarNovedades Admi : " + e.toString());
      }
   }
}

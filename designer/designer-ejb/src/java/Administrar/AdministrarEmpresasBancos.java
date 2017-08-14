/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Bancos;
import Entidades.Ciudades;
import Entidades.Empresas;
import Entidades.EmpresasBancos;
import InterfaceAdministrar.AdministrarEmpresasBancosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaBancosInterface;
import InterfacePersistencia.PersistenciaCiudadesInterface;
import InterfacePersistencia.PersistenciaEmpresasBancosInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
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
public class AdministrarEmpresasBancos implements AdministrarEmpresasBancosInterface {

   private static Logger log = Logger.getLogger(AdministrarEmpresasBancos.class);

   //--------------------------------------------------------------------------
   //ATRIBUTOS
   //--------------------------------------------------------------------------    
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaEmpresasBancos'.
    */
   @EJB
   PersistenciaEmpresasBancosInterface persistenciaEmpresasBancos;
   @EJB
   PersistenciaBancosInterface persistenciaBancos;
   @EJB
   PersistenciaCiudadesInterface persistenciaCiudades;
   @EJB
   PersistenciaEmpresasInterface persistenciaEmpresas;
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
   //--------------------------------------------------------------------------
   //MÉTODOS
   //--------------------------------------------------------------------------

   @Override
   public void obtenerConexion(String idSesion) {
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   public void modificarEmpresasBancos(List<EmpresasBancos> listaEmpresasBancos) {
      try {
         for (int i = 0; i < listaEmpresasBancos.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaEmpresasBancos.editar(getEm(), listaEmpresasBancos.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void borrarEmpresasBancos(List<EmpresasBancos> listaEmpresasBancos) {
      try {
         for (int i = 0; i < listaEmpresasBancos.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaEmpresasBancos.borrar(getEm(), listaEmpresasBancos.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public void crearEmpresasBancos(List<EmpresasBancos> listaEmpresasBancos) {
      try {
         for (int i = 0; i < listaEmpresasBancos.size(); i++) {
            persistenciaEmpresasBancos.crear(getEm(), listaEmpresasBancos.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   public List<EmpresasBancos> consultarEmpresasBancos() {
      try {
         return persistenciaEmpresasBancos.consultarEmpresasBancos(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public EmpresasBancos consultarTipoIndicador(BigInteger secMotivoDemanda) {
      try {
         return persistenciaEmpresasBancos.consultarFirmaReporte(getEm(), secMotivoDemanda);
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public List<Bancos> consultarLOVBancos() {
      try {
         return persistenciaBancos.buscarBancos(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public List<Ciudades> consultarLOVCiudades() {
      try {
         return persistenciaCiudades.consultarCiudades(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public List<Empresas> consultarLOVEmpresas() {
      try {
         return persistenciaEmpresas.consultarEmpresas(getEm());
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }
}

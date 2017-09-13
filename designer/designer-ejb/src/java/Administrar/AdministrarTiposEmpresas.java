/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarTiposEmpresasInterface;
import Entidades.TiposEmpresas;
import InterfacePersistencia.PersistenciaTiposEmpresasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author user
 */
@Stateful
public class AdministrarTiposEmpresas implements AdministrarTiposEmpresasInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposEmpresas.class);

   @EJB
   PersistenciaTiposEmpresasInterface persistenciaTiposEmpresas;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
   @EJB
   AdministrarSesionesInterface administrarSesiones;

   private TiposEmpresas tiposEmpresasSeleccionada;
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
   public void modificarTiposEmpresas(List<TiposEmpresas> listTiposEmpresas) {
      try {
         for (int i = 0; i < listTiposEmpresas.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaTiposEmpresas.editar(getEm(), listTiposEmpresas.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".modificarTiposEmpresas() ERROR: " + e);
      }
   }

   @Override
   public void borrarTiposEmpresas(List<TiposEmpresas> listTiposEmpresas) {
      try {
         for (int i = 0; i < listTiposEmpresas.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaTiposEmpresas.borrar(getEm(), listTiposEmpresas.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".borrarTiposEmpresas() ERROR: " + e);
      }
   }

   @Override
   public void crearTiposEmpresas(List<TiposEmpresas> listTiposEmpresas) {
      try {
         for (int i = 0; i < listTiposEmpresas.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaTiposEmpresas.crear(getEm(), listTiposEmpresas.get(i));
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".crearTiposEmpresas() ERROR: " + e);
      }
   }

   @Override
   public List<TiposEmpresas> consultarTiposEmpresas() {
      try {
         return persistenciaTiposEmpresas.buscarTiposEmpresas(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarTiposEmpresas() ERROR: " + e);
         return null;
      }
   }

   @Override
   public TiposEmpresas consultarTipoEmpresa(BigInteger secTipoEmpresa) {
      try {
         return persistenciaTiposEmpresas.buscarTipoEmpresa(getEm(), secTipoEmpresa);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + ".consultarTipoEmpresa() ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarSueldosMercadosTipoEmpresa(BigInteger secuenciaSueldosMercados) {
      try {
         log.error("Secuencia Borrado Sueldos Proyectos" + secuenciaSueldosMercados);
         return persistenciaTiposEmpresas.contadorSueldosMercados(getEm(), secuenciaSueldosMercados);
      } catch (Exception e) {
         log.error("ERROR AministrarTiposEmpresas verificarBorradoSueldosMercados ERROR :" + e);
         return null;
      }
   }
}

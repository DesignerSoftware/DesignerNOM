/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.PryPlataformas;
import InterfaceAdministrar.AdministrarPryPlataformasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaPryPlataformasInterface;
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
public class AdministrarPryPlataformas implements AdministrarPryPlataformasInterface {

   private static Logger log = Logger.getLogger(AdministrarPryPlataformas.class);

   @EJB
   PersistenciaPryPlataformasInterface persistenciaPryPlataformas;
   private PryPlataformas pryPlataformasSeleccionado;
   private PryPlataformas pryPlataformas;
   private List<PryPlataformas> listPryPlataformas;
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
   public void modificarPryPlataformas(List<PryPlataformas> listaPryClientes) {
      try {
         for (int i = 0; i < listaPryClientes.size(); i++) {
            log.warn("Administrar Modificando...");
            persistenciaPryPlataformas.editar(getEm(), listaPryClientes.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarPryPlataformas(List<PryPlataformas> listaPryClientes) {
      try {
         for (int i = 0; i < listaPryClientes.size(); i++) {
            log.warn("Administrar Borrando...");
            persistenciaPryPlataformas.borrar(getEm(), listaPryClientes.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearPryPlataformas(List<PryPlataformas> listaPryClientes) {
      try {
         for (int i = 0; i < listaPryClientes.size(); i++) {
            log.warn("Administrar Creando...");
            persistenciaPryPlataformas.crear(getEm(), listaPryClientes.get(i));
         }
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public List<PryPlataformas> mostrarPryPlataformas() {
      try {
         listPryPlataformas = persistenciaPryPlataformas.buscarPryPlataformas(getEm());
         return listPryPlataformas;
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public PryPlataformas mostrarPryPlataformas(BigInteger secPryClientes) {
      try {
         pryPlataformas = persistenciaPryPlataformas.buscarPryPlataformaSecuencia(getEm(), secPryClientes);
         return pryPlataformas;
      } catch (Exception e) {
         log.warn(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public BigInteger contarProyectosPryPlataformas(BigInteger secuenciaProyectos) {
      try {
         log.error("Secuencia Borrado Competencias Cargos" + secuenciaProyectos);
         return persistenciaPryPlataformas.contadorProyectos(getEm(), secuenciaProyectos);
      } catch (Exception e) {
         log.error("ERROR AdministrarPryPlataformas verificarBorradoProyecto ERROR :" + e);
         return null;
      }
   }
}

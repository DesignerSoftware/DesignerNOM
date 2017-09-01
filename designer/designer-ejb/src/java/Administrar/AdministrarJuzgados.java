/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import InterfaceAdministrar.AdministrarJuzgadosInterface;
import Entidades.Ciudades;
import Entidades.Juzgados;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCiudadesInterface;
import InterfacePersistencia.PersistenciaJuzgadosInterface;
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
public class AdministrarJuzgados implements AdministrarJuzgadosInterface {

   private static Logger log = Logger.getLogger(AdministrarJuzgados.class);

   /**
    * CREACION EJB
    */
   @EJB
   PersistenciaJuzgadosInterface persistenciaJuzgados;
   @EJB
   PersistenciaCiudadesInterface persistenciaCiudades;
   /**
    * Enterprise JavaBean.<br>
    * Atributo que representa todo lo referente a la conexión del usuario que
    * está usando el aplicativo.
    */
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

   /**
    *
    */
   @Override
   public List<Ciudades> consultarLOVCiudades() {
      try {
         return persistenciaCiudades.consultarCiudades(getEm());
      } catch (Exception e) {
         log.warn("ADMINISTRARJUZGADOS BUSCARCIUDADES /n" + e.getMessage());
         return null;
      }
   }

   @Override
   public void modificarJuzgados(List<Juzgados> listaJuzgados) {
      try {
         for (int i = 0; i < listaJuzgados.size(); i++) {
            log.warn("Administrar Modificando");
            persistenciaJuzgados.editar(getEm(), listaJuzgados.get(i));
         }
      } catch (Exception e) {
         log.warn("AdministrarCentrosCostos: Falló al editar el CentroCosto /n" + e.getMessage());
      }
   }

   @Override
   public void borrarJuzgados(List<Juzgados> listaJuzgados) {
      try {
         for (int i = 0; i < listaJuzgados.size(); i++) {
            log.warn("Administrar Borrando");
            persistenciaJuzgados.borrar(getEm(), listaJuzgados.get(i));
         }
      } catch (Exception e) {
         log.warn("ERROR ADNUBUSTRARJUZGADOS BORRARJUZGADOS" + e.getMessage());
      }
   }

   @Override
   public void crearJuzgados(List<Juzgados> listaJuzgados) {
      try {
         for (int i = 0; i < listaJuzgados.size(); i++) {
            log.warn("Administrar Creando");
            persistenciaJuzgados.crear(getEm(), listaJuzgados.get(i));
         }
      } catch (Exception e) {
         log.warn("ERROR ADMINISTRARJUZGADOS CREAR JUZGADO " + e.getMessage());
      }
   }

   public List<Juzgados> consultarJuzgadosPorCiudad(BigInteger secCiudad) {
      try {
         return persistenciaJuzgados.buscarJuzgadosPorCiudad(getEm(), secCiudad);
      } catch (Exception e) {
         log.warn("Error en ADMINISTRARJUZGADOS BUSCARJUZGADOPORCIUDAD");
         return null;
      }
   }

   @Override
   public List<Juzgados> LOVJuzgadosPorCiudadGeneral() {
      try {
         return persistenciaJuzgados.buscarJuzgados(getEm());
      } catch (Exception e) {
         log.warn("Error en ADMINISTRARJUZGADOS BUSCARJUZGADOSPORCIUDADGENERAL " + e);
         return null;
      }
   }

   @Override
   public BigInteger verificarEerPrestamos(BigInteger secuenciaJuzgados) {
      try {
         log.warn("Administrar SecuenciaBorrar " + secuenciaJuzgados);
         return persistenciaJuzgados.contadorEerPrestamos(getEm(), secuenciaJuzgados);
      } catch (Exception e) {
         log.error("ERROR ADMINISTRARJUZGADOS VERIFICAREXTRASRECARGOS ERROR :" + e);
         return null;
      }
   }

   @Override
   public boolean isNumeric(String cadena) {
      try {
         Integer.parseInt(cadena);
         return true;
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return false;
      }
   }

}

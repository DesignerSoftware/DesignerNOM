/**
 * Documentación a cargo de AndresPineda
 */
package Administrar;

import Entidades.CentrosCostos;
import Entidades.Circulares;
import Entidades.Empresas;
import Entidades.Monedas;
import Entidades.VigenciasMonedasBases;
import InterfaceAdministrar.AdministrarEmpresasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCentrosCostosInterface;
import InterfacePersistencia.PersistenciaCircularesInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaMonedasInterface;
import InterfacePersistencia.PersistenciaVigenciasMonedasBasesInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'Empresas'.
 *
 * @author AndresPineda
 */
@Stateful
public class AdministrarEmpresas implements AdministrarEmpresasInterface {

   private static Logger log = Logger.getLogger(AdministrarEmpresas.class);

   //--------------------------------------------------------------------------
   //ATRIBUTOS
   //--------------------------------------------------------------------------    
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaEmpresas'.
    */
   @EJB
   PersistenciaEmpresasInterface persistenciaEmpresas;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaCirculares'.
    */
   @EJB
   PersistenciaCircularesInterface persistenciaCirculares;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaVigenciasMonedasBases'.
    */
   @EJB
   PersistenciaVigenciasMonedasBasesInterface persistenciaVigenciasMonedasBases;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaCentrosCostos'.
    */
   @EJB
   PersistenciaCentrosCostosInterface persistenciaCentrosCostos;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaMonedas'.
    */
   @EJB
   PersistenciaMonedasInterface persistenciaMonedas;
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

   //________________---------------_________________-------------------_______________//
   @Override
   public void obtenerConexion(String idSesion) {
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public List<Empresas> listaEmpresas() {
      try {
         return persistenciaEmpresas.buscarEmpresas(getEm());
      } catch (Exception e) {
         log.error("Error listaEmpresas Admi : " + e.toString());
         return null;
      }
   }

   public Empresas consultarEmpresaSecuencia(BigInteger secEmpresa) {
      try {
         return persistenciaEmpresas.buscarEmpresasSecuencia(getEm(), secEmpresa);
      } catch (Exception e) {
         log.error("Error AdministrarEmpresas consultarEmpresaSecuencia : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Empresas> listasEmpresasPorSecuenciaEmpresa(BigInteger secuencia) {
      try {
         return persistenciaEmpresas.buscarEmpresasLista(getEm(), secuencia);
      } catch (Exception e) {
         log.error("Error listaEmpresas Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearEmpresas(List<Empresas> listaE) {
      try {
         for (int i = 0; i < listaE.size(); i++) {
            if (listaE.get(i).getCentrocosto().getSecuencia() == null) {
               listaE.get(i).setCentrocosto(null);
            }
            persistenciaEmpresas.crear(getEm(), listaE.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearEmpresas Admi : " + e.toString());
      }
   }

   @Override
   public void editarEmpresas(List<Empresas> listaE) {
      try {
         for (int i = 0; i < listaE.size(); i++) {
            if (listaE.get(i).getCentrocosto().getSecuencia() == null) {
               listaE.get(i).setCentrocosto(null);
            }
            persistenciaEmpresas.editar(getEm(), listaE.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarEmpresas Admi : " + e.toString());
      }
   }

   @Override
   public void borrarEmpresas(List<Empresas> listaE) {
      try {
         for (int i = 0; i < listaE.size(); i++) {
            if (listaE.get(i).getCentrocosto().getSecuencia() == null) {
               listaE.get(i).setCentrocosto(null);
            }
            persistenciaEmpresas.borrar(getEm(), listaE.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarEmpresas Admi : " + e.toString());
      }
   }

   @Override
   public List<Circulares> listaCircularesParaEmpresa(BigInteger secuencia) {
      try {
         return persistenciaCirculares.buscarCircularesPorSecuenciaEmpresa(getEm(), secuencia);
      } catch (Exception e) {
         log.warn("Error listaCircularesParaEmpresa Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearCirculares(List<Circulares> listaC) {
      try {
         for (int i = 0; i < listaC.size(); i++) {
            persistenciaCirculares.crear(getEm(), listaC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearEmpresas Admi : " + e.toString());
      }
   }

   @Override
   public void editarCirculares(List<Circulares> listaC) {
      try {
         for (int i = 0; i < listaC.size(); i++) {
            persistenciaCirculares.editar(getEm(), listaC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarEmpresas Admi : " + e.toString());
      }
   }

   @Override
   public void borrarCirculares(List<Circulares> listaC) {
      try {
         for (int i = 0; i < listaC.size(); i++) {
            persistenciaCirculares.borrar(getEm(), listaC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarEmpresas Admi : " + e.toString());
      }
   }

   @Override
   public List<VigenciasMonedasBases> listaVigenciasMonedasBasesParaEmpresa(BigInteger secuencia) {
      try {
         List<VigenciasMonedasBases> lista = persistenciaVigenciasMonedasBases.buscarVigenciasMonedasBasesPorSecuenciaEmpresa(getEm(), secuencia);
         return lista;
      } catch (Exception e) {
         log.warn("Error listaVigenciasMonedasBasesParaEmpresa Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearVigenciasMonedasBases(List<VigenciasMonedasBases> listaVMB) {
      try {
         for (int i = 0; i < listaVMB.size(); i++) {
            persistenciaVigenciasMonedasBases.crear(getEm(), listaVMB.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearVigenciasMonedasBases Admi : " + e.toString());
      }
   }

   @Override
   public void editarVigenciasMonedasBases(List<VigenciasMonedasBases> listaVMB) {
      try {
         for (int i = 0; i < listaVMB.size(); i++) {
            persistenciaVigenciasMonedasBases.editar(getEm(), listaVMB.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarVigenciasMonedasBases Admi : " + e.toString());
      }
   }

   @Override
   public void borrarVigenciasMonedasBases(List<VigenciasMonedasBases> listaVMB) {
      try {
         for (int i = 0; i < listaVMB.size(); i++) {
            persistenciaVigenciasMonedasBases.borrar(getEm(), listaVMB.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarVigenciasMonedasBases Admi : " + e.toString());
      }
   }

   @Override
   public List<CentrosCostos> lovCentrosCostos() {
      try {
         return persistenciaCentrosCostos.buscarCentrosCostos(getEm());
      } catch (Exception e) {
         log.warn("Error lovCentrosCostos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Monedas> lovMonedas() {
      try {
         return persistenciaMonedas.consultarMonedas(getEm());
      } catch (Exception e) {
         log.warn("Error lovMonedas Admi : " + e.toString());
         return null;
      }
   }

   public String clonarEmpresa(short codOrigen, short codDestino) {
      try {
         return persistenciaEmpresas.clonarEmpresa(getEm(), codOrigen, codDestino);
      } catch (Exception e) {
         log.warn("AdministrarEmpresas.clonarEmpresa() ERROR : " + e);
         return "NO";
      }
   }

}

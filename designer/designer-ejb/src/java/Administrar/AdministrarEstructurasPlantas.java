/**
 * Documentación a cargo de AndresPineda
 */
package Administrar;

import Entidades.CentrosCostos;
import Entidades.Empresas;
import Entidades.Estructuras;
import Entidades.Organigramas;
import InterfaceAdministrar.AdministrarEstructurasPlantasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCentrosCostosInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaEstructurasInterface;
import InterfacePersistencia.PersistenciaOrganigramasInterface;
import InterfacePersistencia.PersistenciaPlantasPersonalesInterface;
import InterfacePersistencia.PersistenciaVWActualesCargosInterface;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 * Clase Stateful. <br> Clase encargada de realizar las operaciones lógicas para
 * la pantalla 'EstructuraPlanta'.
 *
 * @author AndresPineda
 */
@Stateful
public class AdministrarEstructurasPlantas implements AdministrarEstructurasPlantasInterface {

   private static Logger log = Logger.getLogger(AdministrarEstructurasPlantas.class);

   //-------------------------------------------------------------------------
   //ATRIBUTOS
   //--------------------------------------------------------------------------    
   @EJB
   PersistenciaEmpresasInterface persistenciaEmpresas;
   /**
    * Enterprise JavaBeans.<br> Atributo que representa la comunicación con la
    * persistencia 'persistenciaOrganigramas'.
    */
   @EJB
   PersistenciaOrganigramasInterface persistenciaOrganigramas;
   /**
    * Enterprise JavaBeans.<br> Atributo que representa la comunicación con la
    * persistencia 'persistenciaEstructuras'.
    */
   @EJB
   PersistenciaEstructurasInterface persistenciaEstructuras;
   /**
    * Enterprise JavaBeans.<br> Atributo que representa la comunicación con la
    * persistencia 'persistenciaCentrosCostos'.
    */
   @EJB
   PersistenciaCentrosCostosInterface persistenciaCentrosCostos;
   /**
    * Enterprise JavaBeans.<br> Atributo que representa la comunicación con la
    * persistencia 'persistenciaVWActualesCargos'.
    */
   @EJB
   PersistenciaVWActualesCargosInterface persistenciaVWActualesCargos;
   /**
    * Enterprise JavaBeans.<br> Atributo que representa la comunicación con la
    * persistencia 'persistenciaPlantasPersonales'.
    */
   @EJB
   PersistenciaPlantasPersonalesInterface persistenciaPlantasPersonales;
   /**
    * Enterprise JavaBean.<br> Atributo que representa todo lo referente a la
    * conexión del usuario que está usando el aplicativo.
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
   private Organigramas org;
   //--------------------------------------------------------------------------
   //MÉTODOS
   //--------------------------------------------------------------------------

   @Override
   public void obtenerConexion(String idSesion) { idSesionBck = idSesion;
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public List<Organigramas> listaOrganigramas() {
      List<Empresas> listaEmpresas = consultarEmpresas();
      List<Organigramas> listaOrganigramas = new ArrayList<Organigramas>();
      log.warn("listaEmpresas : " + listaEmpresas);

      if (listaEmpresas != null) {
         log.warn("listaEmpresas.size() : " + listaEmpresas.size());

         for (int i = 0; i < listaEmpresas.size(); i++) {
            try {
               List<Organigramas> lista = persistenciaOrganigramas.buscarOrganigramasEmpresa(getEm(),
                       listaEmpresas.get(i).getSecuencia());
               listaOrganigramas.addAll(lista);
            } catch (Exception e) {
               log.warn("Error listaOrganigramas Empresa: " + listaEmpresas.get(i).getSecuencia() + " ex: " + e.toString());
            }
         }
      } else {
         log.warn("listaEmpresas = null");
      }
      return listaOrganigramas;
   }

   @Override
   public List<Empresas> consultarEmpresas() {
      List<Empresas> listaEmpresas = null;
      try {
         listaEmpresas = persistenciaEmpresas.buscarEmpresas(getEm());
         return listaEmpresas;
      } catch (Exception e) {
         log.warn("Error AdministrarEstructurasPlantas.consutlarEmpresas()");
         e.printStackTrace();
         return listaEmpresas;
      }
   }

   @Override
   public List<Organigramas> listaTodosOrganigramas() {
      List<Organigramas> listaOrganigramas = new ArrayList<Organigramas>();
      try {
         listaOrganigramas = persistenciaOrganigramas.buscarOrganigramas(getEm());
         log.warn("Ya salio del EJB");
      } catch (Exception e) {
         log.warn("Error listaOrganigramas : "
                 + e.toString());
      }
      return listaOrganigramas;
   }

   @Override
   public void modificarOrganigramas(List<Organigramas> listaO) {
      try {
         for (int i = 0; i < listaO.size(); i++) {
            persistenciaOrganigramas.editar(getEm(), listaO.get(i));
         }
      } catch (Exception e) {
         log.warn("Error modificarOrganigramas Admi : " + e.toString());
      }
   }

   @Override
   public String cantidadCargosAControlar(BigInteger secEstructura) {
      try {
         String retorno = "";
         BigInteger valor = persistenciaPlantasPersonales.consultarCantidadEstructuras(getEm(), secEstructura);
         if (valor != null) {
            retorno = String.valueOf(valor);
         }
         return retorno;
      } catch (Exception e) {
         log.warn("Error cantidadCargosAControlar Admi : " + e.toString());
         String retorno = "";
         return retorno;
      }
   }

   @Override
   public String cantidadCargosEmplActivos(BigInteger secEstructura) {
      try {
         String retorno = "0";
         Long valor = persistenciaVWActualesCargos.conteoCodigosEmpleados(getEm(), secEstructura);
         if (valor > 0 && valor != null) {
            retorno = String.valueOf(valor);
         }
         return retorno;
      } catch (Exception e) {
         log.warn("Error cantidadCargosEmplActivos Admi : " + e.toString());
         String retorno = "0";
         return retorno;
      }
   }

   @Override
   public List<Estructuras> listaEstructurasPorSecuenciaOrganigrama(BigInteger secOrganigrama) {
      try {
         List<Estructuras> lista = persistenciaEstructuras.buscarEstructurasPorOrganigrama(getEm(), secOrganigrama);
         int tam = lista.size();
         if (tam > 0) {
            for (int i = 0; i < tam; i++) {
               BigInteger cantidad = persistenciaPlantasPersonales.consultarCantidadEstructuras(getEm(), lista.get(i).getSecuencia());
               Long real = persistenciaVWActualesCargos.conteoCodigosEmpleados(getEm(), lista.get(i).getSecuencia());
               if (cantidad != null) {
                  lista.get(i).setCantidadCargosControlar(cantidad.toString());
               } else {
                  lista.get(i).setCantidadCargosControlar("0");
               }
               if (real != null) {
                  lista.get(i).setCantidadCargosEmplActivos(real.toString());
               } else {
                  lista.get(i).setCantidadCargosEmplActivos("0");
               }
            }
         }
         return lista;
      } catch (Exception e) {
         log.warn("Error listaEstructurasPorSecuenciaOrganigrama Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearEstructura(List<Estructuras> listaE) {
      try {
         for (Estructuras recEstructura : listaE) {
            persistenciaEstructuras.crear(getEm(), recEstructura);
         }
      } catch (Exception e) {
         log.warn("Error crearEstructura Admi : " + e.toString());
      }
   }

   @Override
   public void editarEstructura(List<Estructuras> listaE) {
      try {
         for (Estructuras recEstructura : listaE) {
            persistenciaEstructuras.editar(getEm(), recEstructura);
         }
      } catch (Exception e) {
         log.warn("Error editarEstructura Admi : " + e.toString());
      }
   }

   @Override
   public void borrarEstructura(List<Estructuras> listaE) {
      try {
         for (Estructuras recEstructura : listaE) {
            persistenciaEstructuras.borrar(getEm(), recEstructura);
         }
      } catch (Exception e) {
         log.warn("Error borrarEstructura Admi : " + e.toString());
      }
   }

   @Override
   public List<Estructuras> lovEstructurasPadres(BigInteger secOrganigrama, BigInteger secEstructura) {
      try {
         return persistenciaEstructuras.buscarEstructurasPadres(getEm(), secOrganigrama, secEstructura);
      } catch (Exception e) {
         log.warn("Error lovEstructurasPadres Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<CentrosCostos> lovCentrosCostos(BigInteger secEmpresa) {
      try {
         return persistenciaCentrosCostos.buscarCentroCostoPorSecuenciaEmpresa(getEm(), secEmpresa);
      } catch (Exception e) {
         log.warn("Error lovCentrosCostos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Estructuras> lovEstructuras() {
      try {
         return persistenciaEstructuras.buscarEstructuras(getEm());
      } catch (Exception e) {
         log.warn("Error lovEstructuras Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void modificarOrganigrama(List<Organigramas> listOrganigramasModificados) {
      try {
         for (int i = 0; i < listOrganigramasModificados.size(); i++) {
            log.warn("Modificando...");
            org = listOrganigramasModificados.get(i);
            persistenciaOrganigramas.editar(getEm(), org);
         }
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void borrarOrganigrama(Organigramas organigrama) {
      try {
         persistenciaOrganigramas.borrar(getEm(), organigrama);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }

   @Override
   public void crearOrganigrama(Organigramas organigrama) {
      try {
         persistenciaOrganigramas.crear(getEm(), organigrama);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
      }
   }
}

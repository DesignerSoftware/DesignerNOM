/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.Cargos;
import Entidades.Empresas;
import Entidades.Estructuras;
import Entidades.Organigramas;
import InterfaceAdministrar.AdministrarEstructurasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCargosInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaEstructurasInterface;
import InterfacePersistencia.PersistenciaOrganigramasInterface;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
@Stateless
public class AdministrarEstructuras implements AdministrarEstructurasInterface {

   private static Logger log = Logger.getLogger(AdministrarEstructuras.class);

   //------------------------------------------------------------------------------------------
   //EJB
   //------------------------------------------------------------------------------------------
   @EJB
   PersistenciaEstructurasInterface persistenciaEstructuras;
   @EJB
   PersistenciaCargosInterface persistenciaCargos;
   @EJB
   PersistenciaOrganigramasInterface persistenciaOrganigramas;
   @EJB
   PersistenciaEmpresasInterface persistenciaEmpresas;
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
   //------------------------------------------------------------------------------------------
   //ATRIBUTOS
   //------------------------------------------------------------------------------------------
   //ESTRUCTURAS
   private List<Estructuras> estructuras;
   private List<Estructuras> estructurasLOV;
   private Estructuras estr;
   //CARGOS
   private List<Cargos> cargos;
   private Cargos cargo;
   private Organigramas org;
   //------------------------------------------------------------------------------------------
   //CONSTRUCTOR
   //------------------------------------------------------------------------------------------
/*
     * public AdministrarEstructuras() { //Estructuras persistenciaEstructuras =
     * new PersistenciaEstructuras(); estructurasLOV = new
     * ArrayList<Estructuras>(); //Cargos persistenciaCargos = new
     * PersistenciaCargos(); }
    */
   //------------------------------------------------------------------------------------------
   //METODOS DE LA INTERFACE
   //------------------------------------------------------------------------------------------
   //Estructuras--------------------------------------------------------

   @Override
   public void obtenerConexion(String idSesion) { idSesionBck = idSesion;
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public List<Estructuras> consultarTodoEstructuras() {
      try {
         estructuras = persistenciaEstructuras.buscarEstructuras(getEm());
      } catch (Exception ex) {
         estructuras = null;
      }
      return estructuras;
   }

   @Override
   public Estructuras consultarEstructuraPorSecuencia(BigInteger secuenciaE) {
      try {
         estr = persistenciaEstructuras.buscarEstructura(getEm(), secuenciaE);
      } catch (Exception ex) {
         estr = null;
      }
      return estr;
   }

   @Override
   public List<Estructuras> consultarNativeQueryEstructuras(String fechaVigencia) {
      try {
         estructurasLOV = persistenciaEstructuras.buscarlistaValores(getEm(), fechaVigencia);
         return estructurasLOV;
      } catch (Exception ex) {
         log.warn("Administrar: Fallo al consultar el nativeQuery");
         return estructurasLOV = null;
      }
   }
   //Cargos-----------------------------------------------------------

   @Override
   public List<Cargos> consultarTodoCargos() {
      try {
         cargos = persistenciaCargos.consultarCargos(getEm());
      } catch (Exception ex) {
         cargos = null;
      }
      return cargos;
   }

   @Override
   public Cargos consultarCargoPorSecuencia(BigInteger secuenciaC) {
      try {
         cargo = persistenciaCargos.buscarCargoSecuencia(getEm(), secuenciaC);
      } catch (Exception ex) {
         cargo = null;
      }
      return cargo;
   }

   //PANTALLA ESTRUCTURAS
   @Override
   public List<Estructuras> estructuraPadre(short codigoOrg) {
      try {
         List<Estructuras> listaEstructurasPadre;
         Organigramas organigrama = persistenciaOrganigramas.organigramaBaseArbol(getEm(), codigoOrg);
         if (organigrama != null) {
            listaEstructurasPadre = persistenciaEstructuras.estructuraPadre(getEm(), organigrama.getSecuencia());
         } else {
            listaEstructurasPadre = null;
         }
         return listaEstructurasPadre;
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Estructuras> estructurasHijas(BigInteger secEstructuraPadre, Short codigoEmpresa) {
      try {
         return persistenciaEstructuras.estructurasHijas(getEm(), secEstructuraPadre, codigoEmpresa);
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Organigramas> obtenerOrganigramas() {
      try {
//        List<Organigramas> listaOrganigramas;
//        listaOrganigramas = persistenciaOrganigramas.buscarOrganigramas(getEm());

         List<Empresas> listaEmpresas = consultarEmpresas();
         List<Organigramas> listaOrganigramas = new ArrayList<Organigramas>();
         log.warn("listaEmpresas : " + listaEmpresas);

         if (listaEmpresas != null) {
            log.warn("listaEmpresas.size() : " + listaEmpresas.size());

            for (int i = 0; i < listaEmpresas.size(); i++) {
               try {
                  List<Organigramas> lista = persistenciaOrganigramas.buscarOrganigramasEmpresa(getEm(), listaEmpresas.get(i).getSecuencia());
                  listaOrganigramas.addAll(lista);
               } catch (Exception e) {
                  log.warn("Error listaOrganigramas Empresa: " + listaEmpresas.get(i).getSecuencia() + " ex: " + e.toString());
               }
            }
         } else {
            log.warn("listaEmpresas = null");
         }
         return listaOrganigramas;
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   public List<Empresas> consultarEmpresas() {
      try {
         return persistenciaEmpresas.buscarEmpresas(getEm());
      } catch (Exception e) {
         log.warn("Error AdministrarEstructurasPlantas.consutlarEmpresas()");
         e.printStackTrace();
         return null;
      }
   }

   @Override
   public List<Empresas> obtenerEmpresas() {
      try {
         return persistenciaEmpresas.consultarEmpresas(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
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

   @Override
   public List<Estructuras> Estructuras() {
      try {
         return persistenciaEstructuras.estructuras(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }

   @Override
   public List<Estructuras> lovEstructuras() {
      try {
         return persistenciaEstructuras.estructuras(getEm());
      } catch (Exception e) {
         log.error(this.getClass().getSimpleName() + "." + new Exception().getStackTrace()[1].getMethodName() + " ERROR: " + e);
         return null;
      }
   }
}

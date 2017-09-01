/**
 * Documentación a cargo de Andres Pineda
 */
package Administrar;

import Entidades.Conceptos;
import Entidades.Formulas;
import Entidades.Grupostiposentidades;
import Entidades.TEFormulasConceptos;
import Entidades.TSFormulasConceptos;
import Entidades.TSGruposTiposEntidades;
import Entidades.TiposEntidades;
import Entidades.TiposSueldos;
import InterfaceAdministrar.AdministrarTiposSueldosInterface;
import InterfacePersistencia.PersistenciaConceptosInterface;
import InterfacePersistencia.PersistenciaFormulasInterface;
import InterfacePersistencia.PersistenciaGruposTiposEntidadesInterface;
import InterfacePersistencia.PersistenciaTEFormulasConceptosInterface;
import InterfacePersistencia.PersistenciaTSFormulasConceptosInterface;
import InterfacePersistencia.PersistenciaTSGruposTiposEntidadesInterface;
import InterfacePersistencia.PersistenciaTiposEntidadesInterface;
import InterfacePersistencia.PersistenciaTiposSueldosInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'TipoSueldo'.
 *
 * @author AndresPineda
 */
@Stateful
public class AdministrarTiposSueldos implements AdministrarTiposSueldosInterface {

   private static Logger log = Logger.getLogger(AdministrarTiposSueldos.class);

   //--------------------------------------------------------------------------    
   //ATRIBUTOS
   //--------------------------------------------------------------------------    
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaTiposSueldos'.
    */
   @EJB
   PersistenciaTiposSueldosInterface persistenciaTiposSueldos;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaTSFormulasConceptos'.
    */
   @EJB
   PersistenciaTSFormulasConceptosInterface persistenciaTSFormulasConceptos;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaTSGruposTiposEntidades'.
    */
   @EJB
   PersistenciaTSGruposTiposEntidadesInterface persistenciaTSGruposTiposEntidades;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaTEFormulasConceptos'.
    */
   @EJB
   PersistenciaTEFormulasConceptosInterface persistenciaTEFormulasConceptos;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaFormulas'.
    */
   @EJB
   PersistenciaFormulasInterface persistenciaFormulas;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaConceptos'.
    */
   @EJB
   PersistenciaConceptosInterface persistenciaConceptos;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaTiposEntidades'.
    */
   @EJB
   PersistenciaTiposEntidadesInterface persistenciaTiposEntidades;
   /**
    * Enterprise JavaBeans.<br>
    * Atributo que representa la comunicación con la persistencia
    * 'persistenciaGruposTiposEntidades'.
    */
   @EJB
   PersistenciaGruposTiposEntidadesInterface persistenciaGruposTiposEntidades;
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

   //--------------------------------------------------------------------------    
   //METODOS
   //--------------------------------------------------------------------------    
   ////TiposSueldos 
   @Override
   public void obtenerConexion(String idSesion) { idSesionBck = idSesion;
      try {
         emf = administrarSesiones.obtenerConexionSesionEMF(idSesion);
      } catch (Exception e) {
         log.fatal(this.getClass().getSimpleName() + " obtenerConexion ERROR: " + e);
      }
   }

   @Override
   public List<TiposSueldos> listaTiposSueldos() {
      try {
         return persistenciaTiposSueldos.buscarTiposSueldos(getEm());
      } catch (Exception e) {
         log.warn("Error listaTiposSueldos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearTiposSueldos(List<TiposSueldos> listaTS) {
      try {
         for (int i = 0; i < listaTS.size(); i++) {
            persistenciaTiposSueldos.crear(getEm(), listaTS.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearTiposSueldos Admi : " + e.toString());
      }
   }

   @Override
   public void editarTiposSueldos(List<TiposSueldos> listaTS) {
      try {
         for (int i = 0; i < listaTS.size(); i++) {
            persistenciaTiposSueldos.editar(getEm(), listaTS.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarTiposSueldos Admi : " + e.toString());
      }
   }

   @Override
   public void borrarTiposSueldos(List<TiposSueldos> listaTS) {
      try {
         for (int i = 0; i < listaTS.size(); i++) {
            persistenciaTiposSueldos.borrar(getEm(), listaTS.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarTiposSueldos Admi : " + e.toString());
      }
   }
   ////TiposSueldos

   ////TSFormulasConceptos 
   @Override
   public List<TSFormulasConceptos> listaTSFormulasConceptosParaTipoSueldoSecuencia(BigInteger secTipoSueldo) {
      try {
         log.warn("AdministrarTiposSueldos.listaTSFormulasConceptosParaTipoSueldoSecuencia()");
         return persistenciaTSFormulasConceptos.buscarTSFormulasConceptosPorSecuenciaTipoSueldo(getEm(), secTipoSueldo);
      } catch (Exception e) {
         log.warn("Error listaTSFormulasConceptosParaTipoSueldoSecuencia Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearTSFormulasConceptos(List<TSFormulasConceptos> listaTS) {
      try {
         for (int i = 0; i < listaTS.size(); i++) {
            if (listaTS.get(i).getEmpresa() == null) {
               listaTS.get(i).setEmpresa(null);
            }
            persistenciaTSFormulasConceptos.crear(getEm(), listaTS.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearTSFormulasConceptos Admi : " + e.toString());
      }
   }

   @Override
   public void editarTSFormulasConceptos(List<TSFormulasConceptos> listaTS) {
      try {
         for (int i = 0; i < listaTS.size(); i++) {
            if (listaTS.get(i).getEmpresa() == null) {
               listaTS.get(i).setEmpresa(null);
            }
            persistenciaTSFormulasConceptos.editar(getEm(), listaTS.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarTSFormulasConceptos Admi : " + e.toString());
      }
   }

   @Override
   public void borrarTSFormulasConceptos(List<TSFormulasConceptos> listaTS) {
      try {
         for (int i = 0; i < listaTS.size(); i++) {
            if (listaTS.get(i).getEmpresa() != null) {
               if (listaTS.get(i).getEmpresa().getSecuencia() == null) {
                  listaTS.get(i).setEmpresa(null);
               }
            }
            if (listaTS.get(i).getConcepto() != null) {
               if (listaTS.get(i).getConcepto().getSecuencia() == null) {
                  listaTS.get(i).setConcepto(null);
               }
            }
            if (listaTS.get(i).getFormula() != null) {
               if (listaTS.get(i).getFormula().getSecuencia() == null) {
                  listaTS.get(i).setFormula(null);
               }
            }
            if (listaTS.get(i).getTiposueldo() != null) {
               if (listaTS.get(i).getTiposueldo().getSecuencia() == null) {
                  listaTS.get(i).setTiposueldo(null);
               }
            }
            persistenciaTSFormulasConceptos.borrar(getEm(), listaTS.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarTSFormulasConceptos Admi : " + e.toString());
      }
   }

   ////TSFormulasConceptos
   ////TSGruposTiposEntidades
   @Override
   public List<TSGruposTiposEntidades> listaTSGruposTiposEntidadesParaTipoSueldoSecuencia(BigInteger secTipoSueldo) {
      try {
         log.warn("AdministrarTiposSueldos.listaTSGruposTiposEntidadesParaTipoSueldoSecuencia()");
         return persistenciaTSGruposTiposEntidades.buscarTSGruposTiposEntidadesPorSecuenciaTipoSueldo(getEm(), secTipoSueldo);
      } catch (Exception e) {
         log.warn("Error listaTSGruposTiposEntidadesParaTipoSueldoSecuencia Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearTSGruposTiposEntidades(List<TSGruposTiposEntidades> listaTS) {
      try {
         for (int i = 0; i < listaTS.size(); i++) {
            persistenciaTSGruposTiposEntidades.crear(getEm(), listaTS.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearTSGruposTiposEntidades Admi : " + e.toString());
      }
   }

   @Override
   public void editarTSGruposTiposEntidades(List<TSGruposTiposEntidades> listaTS) {
      try {
         for (int i = 0; i < listaTS.size(); i++) {
            persistenciaTSGruposTiposEntidades.editar(getEm(), listaTS.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarTSGruposTiposEntidades Admi : " + e.toString());
      }
   }

   @Override
   public void borrarTSGruposTiposEntidades(List<TSGruposTiposEntidades> listaTS) {
      try {
         for (int i = 0; i < listaTS.size(); i++) {
            persistenciaTSGruposTiposEntidades.borrar(getEm(), listaTS.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarTSGruposTiposEntidades Admi : " + e.toString());
      }
   }

   ////TSGruposTiposEntidades
   ////TEFormulasConceptos
   @Override
   public List<TEFormulasConceptos> listaTEFormulasConceptosParaTSGrupoTipoEntidadSecuencia(BigInteger secTSGrupo) {
      try {
         log.warn("AdministrarTiposSueldos.listaTEFormulasConceptosParaTSGrupoTipoEntidadSecuencia()");
         return persistenciaTEFormulasConceptos.buscarTEFormulasConceptosPorSecuenciaTSGrupoTipoEntidad(getEm(), secTSGrupo);
      } catch (Exception e) {
         log.warn("Error listaTEFormulasConceptosParaTSGrupoTipoEntidadSecuencia Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearTEFormulasConceptos(List<TEFormulasConceptos> listaTE) {
      try {
         for (int i = 0; i < listaTE.size(); i++) {
            listaTE.get(i).setEmpresa(listaTE.get(i).getConcepto().getEmpresa());
            persistenciaTEFormulasConceptos.crear(getEm(), listaTE.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearTEFormulasConceptos Admi : " + e.toString());
      }
   }

   @Override
   public void editarTEFormulasConceptos(List<TEFormulasConceptos> listaTE) {
      try {
         for (int i = 0; i < listaTE.size(); i++) {
            if (listaTE.get(i).getConcepto() == null) {
               listaTE.get(i).setConcepto(new Conceptos());
            }
            listaTE.get(i).setEmpresa(listaTE.get(i).getConcepto().getEmpresa());
            persistenciaTEFormulasConceptos.editar(getEm(), listaTE.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarTEFormulasConceptos Admi : " + e.toString());
      }
   }

   @Override
   public void borrarTEFormulasConceptos(List<TEFormulasConceptos> listaTE) {
      try {
         for (int i = 0; i < listaTE.size(); i++) {
            listaTE.get(i).setEmpresa(listaTE.get(i).getConcepto().getEmpresa());
            persistenciaTEFormulasConceptos.borrar(getEm(), listaTE.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarTEFormulasConceptos Admi : " + e.toString());
      }
   }

   @Override
   public List<TEFormulasConceptos> listaTEFormulasConceptos() {
      try {
         return persistenciaTEFormulasConceptos.buscarTEFormulasConceptos(getEm());
      } catch (Exception e) {
         return null;
      }
   }
   ////TEFormulasConceptos

   /// -- Listas de Valores --- ///
   @Override
   public List<Formulas> lovFormulas() {
      try {
         return persistenciaFormulas.buscarFormulas(getEm());
      } catch (Exception e) {
         log.warn("Error lovFormulas Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Conceptos> lovConceptos() {
      try {
         return persistenciaConceptos.buscarConceptos(getEm());
      } catch (Exception e) {
         log.warn("Error lovConceptos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Grupostiposentidades> lovGruposTiposEntidades() {
      try {
         return persistenciaGruposTiposEntidades.consultarGruposTiposEntidades(getEm());
      } catch (Exception e) {
         log.warn("Error lovGruposTiposEntidades Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<TiposEntidades> lovTiposEntidades(BigInteger secGrupo) {
      try {
         return persistenciaTiposEntidades.buscarTiposEntidadesPorSecuenciaGrupo(getEm(), secGrupo);
      } catch (Exception e) {
         log.warn("Error lovTiposEntidades Admi : " + e.toString());
         return null;
      }
   }

}

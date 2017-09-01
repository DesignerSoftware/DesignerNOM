package Administrar;

import Entidades.Cargos;
import Entidades.Competenciascargos;
import Entidades.DetallesCargos;
import Entidades.Empresas;
import Entidades.Enfoques;
import Entidades.EvalCompetencias;
import Entidades.GruposSalariales;
import Entidades.GruposViaticos;
import Entidades.ProcesosProductivos;
import Entidades.SueldosMercados;
import Entidades.TiposDetalles;
import Entidades.TiposEmpresas;
import InterfaceAdministrar.AdministrarCargosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCargosInterface;
import InterfacePersistencia.PersistenciaCompetenciasCargosInterface;
import InterfacePersistencia.PersistenciaDetallesCargosInterface;
import InterfacePersistencia.PersistenciaEmpresasInterface;
import InterfacePersistencia.PersistenciaEnfoquesInterface;
import InterfacePersistencia.PersistenciaEvalCompetenciasInterface;
import InterfacePersistencia.PersistenciaGruposSalarialesInterface;
import InterfacePersistencia.PersistenciaGruposViaticosInterface;
import InterfacePersistencia.PersistenciaProcesosProductivosInterface;
import InterfacePersistencia.PersistenciaSueldosMercadosInterface;
import InterfacePersistencia.PersistenciaTiposDetallesInterface;
import InterfacePersistencia.PersistenciaTiposEmpresasInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.apache.log4j.Logger;

@Stateful
public class AdministrarCargos implements AdministrarCargosInterface {

   private static Logger log = Logger.getLogger(AdministrarCargos.class);

   @EJB
   PersistenciaCargosInterface persistenciaCargos;
   @EJB
   PersistenciaSueldosMercadosInterface persistenciaSueldosMercados;
   @EJB
   PersistenciaEmpresasInterface persistenciaEmpresas;
   @EJB
   PersistenciaCompetenciasCargosInterface persistenciaCompetenciasCargos;
   @EJB
   PersistenciaTiposDetallesInterface persistenciaTiposDetalles;
   @EJB
   PersistenciaDetallesCargosInterface persistenciaDetallesCargos;
   @EJB
   PersistenciaGruposSalarialesInterface persistenciaGruposSalariales;
   @EJB
   PersistenciaGruposViaticosInterface persistenciaGruposViaticos;
   @EJB
   PersistenciaProcesosProductivosInterface persistenciaProcesosProductivos;
   @EJB
   PersistenciaTiposEmpresasInterface persistenciaTiposEmpresas;
   @EJB
   PersistenciaEvalCompetenciasInterface persistenciaEvalCompetencias;
   @EJB
   PersistenciaEnfoquesInterface persistenciaEnfoques;
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
   public List<Cargos> consultarCargos() {
      try {
         return persistenciaCargos.consultarCargos(getEm());
      } catch (Exception e) {
         log.warn("Error consultarCargos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Empresas> listaEmpresas() {
      try {
         return persistenciaEmpresas.consultarEmpresas(getEm());
      } catch (Exception e) {
         return null;
      }
   }

   @Override
   public List<Cargos> listaCargosParaEmpresa(BigInteger secEmpresa) {
      try {
         return persistenciaCargos.buscarCargosPorSecuenciaEmpresa(getEm(), secEmpresa);
      } catch (Exception e) {
         log.warn("Error listaCargosParaEmpresa Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearCargos(List<Cargos> listaC) {
      try {
         for (int i = 0; i < listaC.size(); i++) {
            if (listaC.get(i).getProcesoproductivo().getSecuencia() == null) {
               listaC.get(i).setProcesoproductivo(null);
            }
            if (listaC.get(i).getGruposalarial().getSecuencia() == null) {
               listaC.get(i).setGruposalarial(null);
            }
            if (listaC.get(i).getGrupoviatico().getSecuencia() == null) {
               listaC.get(i).setGrupoviatico(null);
            }
            persistenciaCargos.crear(getEm(), listaC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearCargos Admi : " + e.toString());
      }
   }

   @Override
   public void editarCargos(List<Cargos> listaC) {
      try {
         for (int i = 0; i < listaC.size(); i++) {
            if (listaC.get(i).getProcesoproductivo().getSecuencia() == null) {
               listaC.get(i).setProcesoproductivo(null);
            }
            if (listaC.get(i).getGruposalarial().getSecuencia() == null) {
               listaC.get(i).setGruposalarial(null);
            }
            if (listaC.get(i).getGrupoviatico().getSecuencia() == null) {
               listaC.get(i).setGrupoviatico(null);
            }
            persistenciaCargos.editar(getEm(), listaC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarCargos Admi : " + e.toString());
      }
   }

   @Override
   public void borrarCargos(List<Cargos> listaC) {
      try {
         for (int i = 0; i < listaC.size(); i++) {
            if (listaC.get(i).getProcesoproductivo().getSecuencia() == null) {
               listaC.get(i).setProcesoproductivo(null);
            }
            if (listaC.get(i).getGruposalarial().getSecuencia() == null) {
               listaC.get(i).setGruposalarial(null);
            }
            if (listaC.get(i).getGrupoviatico().getSecuencia() == null) {
               listaC.get(i).setGrupoviatico(null);
            }
            persistenciaCargos.borrar(getEm(), listaC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarCargos Admi : " + e.toString());
      }
   }

   @Override
   public List<SueldosMercados> listaSueldosMercadosParaCargo(BigInteger secCargo) {
      try {
         List<SueldosMercados> lista = persistenciaSueldosMercados.buscarSueldosMercadosPorSecuenciaCargo(getEm(), secCargo);
         return lista;
      } catch (Exception e) {
         log.warn("Error listaSueldosMercadosParaCargo Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearSueldosMercados(List<SueldosMercados> listaSM) {
      try {
         for (int i = 0; i < listaSM.size(); i++) {
            persistenciaSueldosMercados.crear(getEm(), listaSM.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearSueldosMercados Admi : " + e.toString());
      }
   }

   @Override
   public void editarSueldosMercados(List<SueldosMercados> listaSM) {
      try {
         for (int i = 0; i < listaSM.size(); i++) {
            persistenciaSueldosMercados.editar(getEm(), listaSM.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarSueldosMercados Admi : " + e.toString());
      }
   }

   @Override
   public void borrarSueldosMercados(List<SueldosMercados> listaSM) {
      try {
         for (int i = 0; i < listaSM.size(); i++) {
            persistenciaSueldosMercados.borrar(getEm(), listaSM.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarSueldosMercados Admi : " + e.toString());
      }
   }

   @Override
   public List<Competenciascargos> listaCompetenciasCargosParaCargo(BigInteger secCargo) {
      try {
         List<Competenciascargos> lista = persistenciaCompetenciasCargos.buscarCompetenciasCargosParaSecuenciaCargo(getEm(), secCargo);
         return lista;
      } catch (Exception e) {
         log.warn("Error listaCompetenciasCargosParaCargo Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearCompetenciasCargos(List<Competenciascargos> listaCC) {
      try {
         for (int i = 0; i < listaCC.size(); i++) {
            persistenciaCompetenciasCargos.crear(getEm(), listaCC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearCompetenciasCargos Admi : " + e.toString());
      }
   }

   @Override
   public void editarCompetenciasCargos(List<Competenciascargos> listaCC) {
      try {
         for (int i = 0; i < listaCC.size(); i++) {
            persistenciaCompetenciasCargos.editar(getEm(), listaCC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarCompetenciasCargos Admi : " + e.toString());
      }
   }

   @Override
   public void borrarCompetenciasCargos(List<Competenciascargos> listaCC) {
      try {
         for (int i = 0; i < listaCC.size(); i++) {
            persistenciaCompetenciasCargos.borrar(getEm(), listaCC.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarCompetenciasCargos Admi : " + e.toString());
      }
   }

   @Override
   public List<TiposDetalles> listaTiposDetalles() {
      try {
         List<TiposDetalles> lista = persistenciaTiposDetalles.buscarTiposDetalles(getEm());
         return lista;
      } catch (Exception e) {
         log.warn("Error listaTiposDetalles Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearTiposDetalles(List<TiposDetalles> listaTD) {
      try {
         for (int i = 0; i < listaTD.size(); i++) {
            persistenciaTiposDetalles.crear(getEm(), listaTD.get(i));
         }
      } catch (Exception e) {
         log.warn("Error crearTiposDetalles Admi : " + e.toString());
      }
   }

   @Override
   public void editarTiposDetalles(List<TiposDetalles> listaTD) {
      try {
         for (int i = 0; i < listaTD.size(); i++) {
            persistenciaTiposDetalles.editar(getEm(), listaTD.get(i));
         }
      } catch (Exception e) {
         log.warn("Error editarTiposDetalles Admi : " + e.toString());
      }
   }

   @Override
   public void borrarTiposDetalles(List<TiposDetalles> listaTD) {
      try {
         for (int i = 0; i < listaTD.size(); i++) {
            persistenciaTiposDetalles.borrar(getEm(), listaTD.get(i));
         }
      } catch (Exception e) {
         log.warn("Error borrarTiposDetalles Admi : " + e.toString());
      }
   }

   @Override
   public DetallesCargos detalleDelCargo(BigInteger secTipoDetalle, BigInteger secCargo) {
      try {
         return persistenciaDetallesCargos.buscarDetalleCargoParaSecuenciaTipoDetalle(getEm(), secTipoDetalle, secCargo);
      } catch (Exception e) {
         log.warn("Error detalleDelCargo Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearDetalleCargo(DetallesCargos detalleCargo) {
      try {
         persistenciaDetallesCargos.crear(getEm(), detalleCargo);
      } catch (Exception e) {
         log.warn("Error crearDetalleCargo Admi : " + e.toString());
      }
   }

   @Override
   public void editarDetalleCargo(DetallesCargos detalleCargo) {
      try {
         persistenciaDetallesCargos.editar(getEm(), detalleCargo);
      } catch (Exception e) {
         log.warn("Error editarDetalleCargo Admi : " + e.toString());
      }
   }

   @Override
   public void borrarDetalleCargo(DetallesCargos detalleCargo) {
      try {
         persistenciaDetallesCargos.borrar(getEm(), detalleCargo);
      } catch (Exception e) {
         log.warn("Error borrarDetalleCargo Admi : " + e.toString());
      }
   }

   @Override
   public int validarExistenciaCargoDetalleCargos(BigInteger secCargo) {
      try {
         List<DetallesCargos> detalle = persistenciaDetallesCargos.buscarDetallesCargosDeCargoSecuencia(getEm(), secCargo);
         return detalle.size();
      } catch (Exception e) {
         log.warn("Error validarExistenciaCargoDetalleCargos Admi : " + e.toString());
         return -1;
      }
   }

   @Override
   public List<GruposSalariales> lovGruposSalariales() {
      try {
         return persistenciaGruposSalariales.buscarGruposSalariales(getEm());
      } catch (Exception e) {
         log.warn("Error lovGruposSalariales Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<GruposViaticos> lovGruposViaticos() {
      try {
         return persistenciaGruposViaticos.buscarGruposViaticos(getEm());
      } catch (Exception e) {
         log.warn("Error lovGruposViaticos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<ProcesosProductivos> lovProcesosProductivos() {
      try {
         return persistenciaProcesosProductivos.consultarProcesosProductivos(getEm());
      } catch (Exception e) {
         log.warn("Error lovProcesosProductivos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<TiposEmpresas> lovTiposEmpresas() {
      try {
         return persistenciaTiposEmpresas.buscarTiposEmpresas(getEm());
      } catch (Exception e) {
         log.warn("Error lovTiposEmpresas Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<EvalCompetencias> lovEvalCompetencias() {
      try {
         return persistenciaEvalCompetencias.buscarEvalCompetencias(getEm());
      } catch (Exception e) {
         log.warn("Error lovEvalCompetencias Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Enfoques> lovEnfoques() {
      try {
         return persistenciaEnfoques.buscarEnfoques(getEm());
      } catch (Exception e) {
         log.warn("Error lovEnfoques Admi : " + e.toString());
         return null;
      }
   }

}

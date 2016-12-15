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

@Stateful
public class AdministrarCargos implements AdministrarCargosInterface {

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

   private EntityManager em;

   @Override
   public void obtenerConexion(String idSesion) {
      em = administrarSesiones.obtenerConexionSesion(idSesion);
   }

   @Override
   public List<Cargos> consultarCargos() {
      try {
         return persistenciaCargos.consultarCargos(em);
      } catch (Exception e) {
         System.out.println("Error consultarCargos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Empresas> listaEmpresas() {
      try {
         List<Empresas> lista = persistenciaEmpresas.consultarEmpresas(em);
         return lista;
      } catch (Exception e) {
         return null;
      }
   }

   @Override
   public List<Cargos> listaCargosParaEmpresa(BigInteger secEmpresa) {
      try {
         List<Cargos> lista = persistenciaCargos.buscarCargosPorSecuenciaEmpresa(em, secEmpresa);
         return lista;
      } catch (Exception e) {
         System.out.println("Error listaCargosParaEmpresa Admi : " + e.toString());
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
            persistenciaCargos.crear(em, listaC.get(i));
         }
      } catch (Exception e) {
         System.out.println("Error crearCargos Admi : " + e.toString());
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
            persistenciaCargos.editar(em, listaC.get(i));
         }
      } catch (Exception e) {
         System.out.println("Error editarCargos Admi : " + e.toString());
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
            persistenciaCargos.borrar(em, listaC.get(i));
         }
      } catch (Exception e) {
         System.out.println("Error borrarCargos Admi : " + e.toString());
      }
   }

   @Override
   public List<SueldosMercados> listaSueldosMercadosParaCargo(BigInteger secCargo) {
      try {
         List<SueldosMercados> lista = persistenciaSueldosMercados.buscarSueldosMercadosPorSecuenciaCargo(em, secCargo);
         return lista;
      } catch (Exception e) {
         System.out.println("Error listaSueldosMercadosParaCargo Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearSueldosMercados(List<SueldosMercados> listaSM) {
      try {
         for (int i = 0; i < listaSM.size(); i++) {
            persistenciaSueldosMercados.crear(em, listaSM.get(i));
         }
      } catch (Exception e) {
         System.out.println("Error crearSueldosMercados Admi : " + e.toString());
      }
   }

   @Override
   public void editarSueldosMercados(List<SueldosMercados> listaSM) {
      try {
         for (int i = 0; i < listaSM.size(); i++) {
            persistenciaSueldosMercados.editar(em, listaSM.get(i));
         }
      } catch (Exception e) {
         System.out.println("Error editarSueldosMercados Admi : " + e.toString());
      }
   }

   @Override
   public void borrarSueldosMercados(List<SueldosMercados> listaSM) {
      try {
         for (int i = 0; i < listaSM.size(); i++) {
            persistenciaSueldosMercados.borrar(em, listaSM.get(i));
         }
      } catch (Exception e) {
         System.out.println("Error borrarSueldosMercados Admi : " + e.toString());
      }
   }

   @Override
   public List<Competenciascargos> listaCompetenciasCargosParaCargo(BigInteger secCargo) {
      try {
         List<Competenciascargos> lista = persistenciaCompetenciasCargos.buscarCompetenciasCargosParaSecuenciaCargo(em, secCargo);
         return lista;
      } catch (Exception e) {
         System.out.println("Error listaCompetenciasCargosParaCargo Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearCompetenciasCargos(List<Competenciascargos> listaCC) {
      try {
         for (int i = 0; i < listaCC.size(); i++) {
            persistenciaCompetenciasCargos.crear(em, listaCC.get(i));
         }
      } catch (Exception e) {
         System.out.println("Error crearCompetenciasCargos Admi : " + e.toString());
      }
   }

   @Override
   public void editarCompetenciasCargos(List<Competenciascargos> listaCC) {
      try {
         for (int i = 0; i < listaCC.size(); i++) {
            persistenciaCompetenciasCargos.editar(em, listaCC.get(i));
         }
      } catch (Exception e) {
         System.out.println("Error editarCompetenciasCargos Admi : " + e.toString());
      }
   }

   @Override
   public void borrarCompetenciasCargos(List<Competenciascargos> listaCC) {
      try {
         for (int i = 0; i < listaCC.size(); i++) {
            persistenciaCompetenciasCargos.borrar(em, listaCC.get(i));
         }
      } catch (Exception e) {
         System.out.println("Error borrarCompetenciasCargos Admi : " + e.toString());
      }
   }

   @Override
   public List<TiposDetalles> listaTiposDetalles() {
      try {
         List<TiposDetalles> lista = persistenciaTiposDetalles.buscarTiposDetalles(em);
         return lista;
      } catch (Exception e) {
         System.out.println("Error listaTiposDetalles Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearTiposDetalles(List<TiposDetalles> listaTD) {
      try {
         for (int i = 0; i < listaTD.size(); i++) {
            persistenciaTiposDetalles.crear(em, listaTD.get(i));
         }
      } catch (Exception e) {
         System.out.println("Error crearTiposDetalles Admi : " + e.toString());
      }
   }

   @Override
   public void editarTiposDetalles(List<TiposDetalles> listaTD) {
      try {
         for (int i = 0; i < listaTD.size(); i++) {
            persistenciaTiposDetalles.editar(em, listaTD.get(i));
         }
      } catch (Exception e) {
         System.out.println("Error editarTiposDetalles Admi : " + e.toString());
      }
   }

   @Override
   public void borrarTiposDetalles(List<TiposDetalles> listaTD) {
      try {
         for (int i = 0; i < listaTD.size(); i++) {
            persistenciaTiposDetalles.borrar(em, listaTD.get(i));
         }
      } catch (Exception e) {
         System.out.println("Error borrarTiposDetalles Admi : " + e.toString());
      }
   }

   @Override
   public DetallesCargos detalleDelCargo(BigInteger secTipoDetalle, BigInteger secCargo) {
      try {
         DetallesCargos detalle = persistenciaDetallesCargos.buscarDetalleCargoParaSecuenciaTipoDetalle(em, secTipoDetalle, secCargo);
         return detalle;
      } catch (Exception e) {
         System.out.println("Error detalleDelCargo Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public void crearDetalleCargo(DetallesCargos detalleCargo) {
      try {
         persistenciaDetallesCargos.crear(em, detalleCargo);
      } catch (Exception e) {
         System.out.println("Error crearDetalleCargo Admi : " + e.toString());
      }
   }

   @Override
   public void editarDetalleCargo(DetallesCargos detalleCargo) {
      try {
         persistenciaDetallesCargos.editar(em, detalleCargo);
      } catch (Exception e) {
         System.out.println("Error editarDetalleCargo Admi : " + e.toString());
      }
   }

   @Override
   public void borrarDetalleCargo(DetallesCargos detalleCargo) {
      try {
         persistenciaDetallesCargos.borrar(em, detalleCargo);
      } catch (Exception e) {
         System.out.println("Error borrarDetalleCargo Admi : " + e.toString());
      }
   }

   @Override
   public int validarExistenciaCargoDetalleCargos(BigInteger secCargo) {
      try {
         List<DetallesCargos> detalle = persistenciaDetallesCargos.buscarDetallesCargosDeCargoSecuencia(em, secCargo);
         return detalle.size();
      } catch (Exception e) {
         System.out.println("Error validarExistenciaCargoDetalleCargos Admi : " + e.toString());
         return -1;
      }
   }

   @Override
   public List<GruposSalariales> lovGruposSalariales() {
      try {
         List<GruposSalariales> lista = persistenciaGruposSalariales.buscarGruposSalariales(em);
         return lista;
      } catch (Exception e) {
         System.out.println("Error lovGruposSalariales Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<GruposViaticos> lovGruposViaticos() {
      try {
         List<GruposViaticos> lista = persistenciaGruposViaticos.buscarGruposViaticos(em);
         return lista;
      } catch (Exception e) {
         System.out.println("Error lovGruposViaticos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<ProcesosProductivos> lovProcesosProductivos() {
      try {
         List<ProcesosProductivos> lista = persistenciaProcesosProductivos.consultarProcesosProductivos(em);
         return lista;
      } catch (Exception e) {
         System.out.println("Error lovProcesosProductivos Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<TiposEmpresas> lovTiposEmpresas() {
      try {
         List<TiposEmpresas> lista = persistenciaTiposEmpresas.buscarTiposEmpresas(em);
         return lista;
      } catch (Exception e) {
         System.out.println("Error lovTiposEmpresas Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<EvalCompetencias> lovEvalCompetencias() {
      try {
         List<EvalCompetencias> lista = persistenciaEvalCompetencias.buscarEvalCompetencias(em);
         return lista;
      } catch (Exception e) {
         System.out.println("Error lovEvalCompetencias Admi : " + e.toString());
         return null;
      }
   }

   @Override
   public List<Enfoques> lovEnfoques() {
      try {
         List<Enfoques> lista = persistenciaEnfoques.buscarEnfoques(em);
         return lista;
      } catch (Exception e) {
         System.out.println("Error lovEnfoques Admi : " + e.toString());
         return null;
      }
   }

}

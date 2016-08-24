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

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'TipoSueldo'.
 *
 * @author AndresPineda
 */
@Stateful
public class AdministrarTiposSueldos implements AdministrarTiposSueldosInterface {

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
    
    private EntityManager em;

    //--------------------------------------------------------------------------    
    //METODOS
    //--------------------------------------------------------------------------    
    ////TiposSueldos 
    
    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }
    
    @Override
    public List<TiposSueldos> listaTiposSueldos() {
        try {
            List<TiposSueldos> lista = persistenciaTiposSueldos.buscarTiposSueldos(em);
            return lista;
        } catch (Exception e) {
            System.out.println("Error listaTiposSueldos Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void crearTiposSueldos(List<TiposSueldos> listaTS) {
        try {
            for (int i = 0; i < listaTS.size(); i++) {
                persistenciaTiposSueldos.crear(em, listaTS.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error crearTiposSueldos Admi : " + e.toString());
        }
    }

    @Override
    public void editarTiposSueldos(List<TiposSueldos> listaTS) {
        try {
            for (int i = 0; i < listaTS.size(); i++) {
                persistenciaTiposSueldos.editar(em, listaTS.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error editarTiposSueldos Admi : " + e.toString());
        }
    }

    @Override
    public void borrarTiposSueldos(List<TiposSueldos> listaTS) {
        try {
            for (int i = 0; i < listaTS.size(); i++) {
                persistenciaTiposSueldos.borrar(em, listaTS.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error borrarTiposSueldos Admi : " + e.toString());
        }
    }
    ////TiposSueldos

    ////TSFormulasConceptos 
    @Override
    public List<TSFormulasConceptos> listaTSFormulasConceptosParaTipoSueldoSecuencia(BigInteger secTipoSueldo) {
        try {
            List<TSFormulasConceptos> lista = persistenciaTSFormulasConceptos.buscarTSFormulasConceptosPorSecuenciaTipoSueldo(em, secTipoSueldo);
            return lista;
        } catch (Exception e) {
            System.out.println("Error listaTSFormulasConceptosParaTipoSueldoSecuencia Admi : " + e.toString());
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
                persistenciaTSFormulasConceptos.crear(em, listaTS.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error crearTSFormulasConceptos Admi : " + e.toString());
        }
    }

    @Override
    public void editarTSFormulasConceptos(List<TSFormulasConceptos> listaTS) {
        try {
            for (int i = 0; i < listaTS.size(); i++) {
                if (listaTS.get(i).getEmpresa() == null) {
                    listaTS.get(i).setEmpresa(null);
                }
                persistenciaTSFormulasConceptos.editar(em, listaTS.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error editarTSFormulasConceptos Admi : " + e.toString());
        }
    }

    @Override
    public void borrarTSFormulasConceptos(List<TSFormulasConceptos> listaTS) {
        try {
            for (int i = 0; i < listaTS.size(); i++) {
                if (listaTS.get(i).getEmpresa() == null) {
                    listaTS.get(i).setEmpresa(null);
                }
                persistenciaTSFormulasConceptos.borrar(em, listaTS.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error borrarTSFormulasConceptos Admi : " + e.toString());
        }
    }
    ////TSFormulasConceptos

    ////TSGruposTiposEntidades
    @Override
    public List<TSGruposTiposEntidades> listaTSGruposTiposEntidadesParaTipoSueldoSecuencia(BigInteger secTipoSueldo) {
        try {
            List<TSGruposTiposEntidades> lista = persistenciaTSGruposTiposEntidades.buscarTSGruposTiposEntidadesPorSecuenciaTipoSueldo(em, secTipoSueldo);
            return lista;
        } catch (Exception e) {
            System.out.println("Error listaTSGruposTiposEntidadesParaTipoSueldoSecuencia Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void crearTSGruposTiposEntidades(List<TSGruposTiposEntidades> listaTS) {
        try {
            for (int i = 0; i < listaTS.size(); i++) {
                persistenciaTSGruposTiposEntidades.crear(em, listaTS.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error crearTSGruposTiposEntidades Admi : " + e.toString());
        }
    }

    @Override
    public void editarTSGruposTiposEntidades(List<TSGruposTiposEntidades> listaTS) {
        try {
            for (int i = 0; i < listaTS.size(); i++) {
                persistenciaTSGruposTiposEntidades.editar(em, listaTS.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error editarTSGruposTiposEntidades Admi : " + e.toString());
        }
    }

    @Override
    public void borrarTSGruposTiposEntidades(List<TSGruposTiposEntidades> listaTS) {
        try {
            for (int i = 0; i < listaTS.size(); i++) {
                persistenciaTSGruposTiposEntidades.borrar(em, listaTS.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error borrarTSGruposTiposEntidades Admi : " + e.toString());
        }
    }
    ////TSGruposTiposEntidades

    ////TEFormulasConceptos
    @Override
    public List<TEFormulasConceptos> listaTEFormulasConceptosParaTSGrupoTipoEntidadSecuencia(BigInteger secTSGrupo) {
        try {
            List<TEFormulasConceptos> lista = persistenciaTEFormulasConceptos.buscarTEFormulasConceptosPorSecuenciaTSGrupoTipoEntidad(em, secTSGrupo);
            return lista;
        } catch (Exception e) {
            System.out.println("Error listaTEFormulasConceptosParaTSGrupoTipoEntidadSecuencia Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void crearTEFormulasConceptos(List<TEFormulasConceptos> listaTE) {
        try {
            for (int i = 0; i < listaTE.size(); i++) {
                listaTE.get(i).setEmpresa(listaTE.get(i).getConcepto().getEmpresa());
                persistenciaTEFormulasConceptos.crear(em, listaTE.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error crearTEFormulasConceptos Admi : " + e.toString());
        }
    }

    @Override
    public void editarTEFormulasConceptos(List<TEFormulasConceptos> listaTE) {
        try {
            for (int i = 0; i < listaTE.size(); i++) {
                listaTE.get(i).setEmpresa(listaTE.get(i).getConcepto().getEmpresa());
                persistenciaTEFormulasConceptos.editar(em, listaTE.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error editarTEFormulasConceptos Admi : " + e.toString());
        }
    }

    @Override
    public void borrarTEFormulasConceptos(List<TEFormulasConceptos> listaTE) {
        try {
            for (int i = 0; i < listaTE.size(); i++) {
                listaTE.get(i).setEmpresa(listaTE.get(i).getConcepto().getEmpresa());
                persistenciaTEFormulasConceptos.borrar(em, listaTE.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error borrarTEFormulasConceptos Admi : " + e.toString());
        }
    }
    
    @Override
    public List<TEFormulasConceptos> listaTEFormulasConceptos() {
        try {
            List<TEFormulasConceptos> lista = persistenciaTEFormulasConceptos.buscarTEFormulasConceptos(em);
            return lista;
        } catch (Exception e) {
            return null;
        }
    }
    ////TEFormulasConceptos

    /// -- Listas de Valores --- ///
    @Override
    public List<Formulas> lovFormulas() {
        try {
            List<Formulas> lista = persistenciaFormulas.buscarFormulas(em);
            return lista;
        } catch (Exception e) {
            System.out.println("Error lovFormulas Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Conceptos> lovConceptos() {
        try {
            List<Conceptos> lista = persistenciaConceptos.buscarConceptos(em);
            return lista;
        } catch (Exception e) {
            System.out.println("Error lovConceptos Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<Grupostiposentidades> lovGruposTiposEntidades() {
        try {
            List<Grupostiposentidades> lista = persistenciaGruposTiposEntidades.consultarGruposTiposEntidades(em);
            return lista;
        } catch (Exception e) {
            System.out.println("Error lovGruposTiposEntidades Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<TiposEntidades> lovTiposEntidades(BigInteger secGrupo) {
        try {
            List<TiposEntidades> lista = persistenciaTiposEntidades.buscarTiposEntidadesPorSecuenciaGrupo(em, secGrupo);
            return lista;
        } catch (Exception e) {
            System.out.println("Error lovTiposEntidades Admi : " + e.toString());
            return null;
        }
    }

}

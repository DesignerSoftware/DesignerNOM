/**
 * Documentación a cargo de Andres Pineda
 */
package Administrar;

import Entidades.EscalafonesSalariales;
import Entidades.GruposSalariales;
import Entidades.TiposTrabajadores;
import InterfaceAdministrar.AdministrarEscalafonesSalarialesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaEscalafonesSalarialesInterface;
import InterfacePersistencia.PersistenciaGruposSalarialesInterface;
import InterfacePersistencia.PersistenciaTiposTrabajadoresInterface;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'EscalafonSalarial'.
 *
 * @author AndresPineda
 */
@Stateful
public class AdministrarEscalafonesSalariales implements AdministrarEscalafonesSalarialesInterface {

   private static Logger log = Logger.getLogger(AdministrarEscalafonesSalariales.class);

    //--------------------------------------------------------------------------
    //ATRIBUTOS
    //--------------------------------------------------------------------------    
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaEscalafonesSalariales'.
     */
    @EJB
    PersistenciaEscalafonesSalarialesInterface persistenciaEscalafonesSalariales;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaGruposSalariales'.
     */
    @EJB
    PersistenciaGruposSalarialesInterface persistenciaGruposSalariales;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaGruposSalariales'.
     */
    @EJB
    PersistenciaTiposTrabajadoresInterface persistenciaTiposTrabajadores;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;

    //--------------------------------------------------------------------------
    //MÉTODOS
    //--------------------------------------------------------------------------  
    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<EscalafonesSalariales> listaEscalafonesSalariales() {
        try {
            List<EscalafonesSalariales> lista = persistenciaEscalafonesSalariales.buscarEscalafones(em);
            return lista;
        } catch (Exception e) {
            log.warn("Error listaEscalafonesSalariales Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void crearEscalafonesSalariales(List<EscalafonesSalariales> listaES) {
        try {
            for (int i = 0; i < listaES.size(); i++) {
                persistenciaEscalafonesSalariales.crear(em,listaES.get(i));
            }
        } catch (Exception e) {
            log.warn("Error crearEscalafonesSalariales Admi : " + e.toString());
        }
    }

    @Override
    public void editarEscalafonesSalariales(List<EscalafonesSalariales> listaES) {
        try {
            for (int i = 0; i < listaES.size(); i++) {
                persistenciaEscalafonesSalariales.editar(em,listaES.get(i));
            }
        } catch (Exception e) {
            log.warn("Error editarEscalafonesSalariales Admi : " + e.toString());
        }
    }

    @Override
    public void borrarEscalafonesSalariales(List<EscalafonesSalariales> listaES) {
        try {
            for (int i = 0; i < listaES.size(); i++) {
                persistenciaEscalafonesSalariales.borrar(em,listaES.get(i));
            }
        } catch (Exception e) {
            log.warn("Error borrarEscalafonesSalariales Admi : " + e.toString());
        }
    }

    @Override
    public List<GruposSalariales> listaGruposSalarialesParaEscalafonSalarial(BigInteger secEscalafon) {
        try {
            List<GruposSalariales> lista = persistenciaGruposSalariales.buscarGruposSalarialesParaEscalafonSalarial(em,secEscalafon);
            return lista;
        } catch (Exception e) {
            log.warn("Error listaGruposSalarialesParaEscalafonSalarial Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void crearGruposSalariales(List<GruposSalariales> listaGS) {
        try {
            for (int i = 0; i < listaGS.size(); i++) {
                persistenciaGruposSalariales.crear(em,listaGS.get(i));
            }
        } catch (Exception e) {
            log.warn("Error crearGruposSalariales Admi : " + e.toString());
        }
    }

    @Override
    public void editarGruposSalariales(List<GruposSalariales> listaGS) {
        try {
            for (int i = 0; i < listaGS.size(); i++) {
                persistenciaGruposSalariales.editar(em,listaGS.get(i));
            }
        } catch (Exception e) {
            log.warn("Error editarGruposSalariales Admi : " + e.toString());
        }
    }

    @Override
    public void borrarGruposSalariales(List<GruposSalariales> listaGS) {
        try {
            for (int i = 0; i < listaGS.size(); i++) {
                persistenciaGruposSalariales.borrar(em,listaGS.get(i));
            }
        } catch (Exception e) {
            log.warn("Error persistenciaEscalafonesSalariales Admi : " + e.toString());
        }
    }

    @Override
    public List<TiposTrabajadores> lovTiposTrabajadores() {
        try {
            List<TiposTrabajadores> lista = persistenciaTiposTrabajadores.buscarTiposTrabajadores(em);
            return lista;
        } catch (Exception e) {
            log.warn("Error lovTiposTrabajadores Admi : " + e.toString());
            return null;
        }

    }

}

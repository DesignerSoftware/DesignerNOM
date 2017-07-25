/**
 * Documentación a cargo de Andres Pineda
 */
package Administrar;

import Entidades.Categorias;
import Entidades.Escalafones;
import Entidades.SubCategorias;
import InterfaceAdministrar.AdministrarEscalafonesInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCategoriasInterface;
import InterfacePersistencia.PersistenciaEscalafonesInterface;
import InterfacePersistencia.PersistenciaSubCategoriasInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'Escalafon'.
 *
 * @author AndresPineda
 */
@Stateless
public class AdministrarEscalafones implements AdministrarEscalafonesInterface {

   private static Logger log = Logger.getLogger(AdministrarEscalafones.class);

    //--------------------------------------------------------------------------
    //ATRIBUTOS
    //--------------------------------------------------------------------------    
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaEscalafones'.
     */
    @EJB
    PersistenciaEscalafonesInterface persistenciaEscalafones;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaCategorias'.
     */
    @EJB
    PersistenciaCategoriasInterface persistenciaCategorias;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaSubCategorias'.
     */
    @EJB
    PersistenciaSubCategoriasInterface persistenciaSubCategorias;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    //--------------------------------------------------------------------------
    //MÉTODOS
    //--------------------------------------------------------------------------    
    //@Override
    public List<Escalafones> listaEscalafones() {
        try {
            List<Escalafones> lista = persistenciaEscalafones.buscarEscalafones(em);
            return lista;
        } catch (Exception e) {
            log.warn("Error listaEscalafones Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void crearEscalafones(List<Escalafones> listaE) {
        try {
            for (int i = 0; i < listaE.size(); i++) {
                persistenciaEscalafones.crear(em,listaE.get(i));
            }
        } catch (Exception e) {
            log.warn("Error crearEscalafones Admi : " + e.toString());
        }
    }

    @Override
    public void editarEscalafones(List<Escalafones> listaE) {
        try {
            for (int i = 0; i < listaE.size(); i++) {
                persistenciaEscalafones.editar(em,listaE.get(i));
            }
        } catch (Exception e) {
            log.warn("Error editarEscalafones Admi : " + e.toString());
        }
    }

    @Override
    public void borrarEscalafones(List<Escalafones> listaE) {
        try {
            for (int i = 0; i < listaE.size(); i++) {
                persistenciaEscalafones.borrar(em,listaE.get(i));
            }
        } catch (Exception e) {
            log.warn("Error borrarEscalafones Admi : " + e.toString());
        }
    }

    @Override
    public List<Categorias> lovCategorias() {
        try {
            List<Categorias> lista = persistenciaCategorias.buscarCategorias(em);
            return lista;
        } catch (Exception e) {
            log.warn("Error lovCategorias Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<SubCategorias> lovSubCategorias() {
        try {
            List<SubCategorias> lista = persistenciaSubCategorias.consultarSubCategorias(em);
            return lista;
        } catch (Exception e) {
            log.warn("Error lovSubCategorias Admi : " + e.toString());
            return null;
        }
    }
}

/**
 * Documentación a cargo de Andres Pineda
 */
package Administrar;

import Entidades.Categorias;
import Entidades.ClasesCategorias;
import Entidades.Conceptos;
import Entidades.TiposSueldos;
import InterfaceAdministrar.AdministrarCategoriasInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCategoriasInterface;
import InterfacePersistencia.PersistenciaClasesCategoriasInterface;
import InterfacePersistencia.PersistenciaConceptosInterface;
import InterfacePersistencia.PersistenciaTiposSueldosInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla
 * 'CategoriaEsca'.
 *
 * @author betelgeuse
 */
@Stateful
public class AdministrarCategorias implements AdministrarCategoriasInterface {

    //--------------------------------------------------------------------------
    //ATRIBUTOS
    //--------------------------------------------------------------------------    
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaClasesCategorias'.
     */
    @EJB
    PersistenciaClasesCategoriasInterface persistenciaClasesCategorias;
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
     * 'persistenciaTiposSueldos'.
     */
    @EJB
    PersistenciaTiposSueldosInterface persistenciaTiposSueldos;
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia
     * 'persistenciaCategorias'.
     */
    @EJB
    PersistenciaCategoriasInterface persistenciaCategorias;

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

    @Override
    public List<Categorias> listaCategorias() {
        try {
            List<Categorias> lista = persistenciaCategorias.buscarCategorias(em);
            return lista;
        } catch (Exception e) {
            System.out.println("Error listaCategorias Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public void crearCategorias(List<Categorias> listaC) {
        try {
            for (int i = 0; i < listaC.size(); i++) {
                if (listaC.get(i).getConcepto().getSecuencia() == null) {
                    listaC.get(i).setConcepto(null);
                }
                persistenciaCategorias.crear(em,listaC.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error crearCategorias Admi : " + e.toString());
        }
    }

    @Override
    public void editarCategorias(List<Categorias> listaC) {
        try {
            for (int i = 0; i < listaC.size(); i++) {
                if (listaC.get(i).getConcepto().getSecuencia() == null) {
                    listaC.get(i).setConcepto(null);
                }
                persistenciaCategorias.editar(em,listaC.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error editarCategorias Admi : " + e.toString());
        }
    }

    @Override
    public void borrarCategorias(List<Categorias> listaC) {
        try {
            for (int i = 0; i < listaC.size(); i++) {
                if (listaC.get(i).getConcepto().getSecuencia() == null) {
                    listaC.get(i).setConcepto(null);
                }
                persistenciaCategorias.borrar(em,listaC.get(i));
            }
        } catch (Exception e) {
            System.out.println("Error borrarCategorias Admi : " + e.toString());
        }
    }

    @Override
    public List<ClasesCategorias> lovClasesCategorias() {
        try {
            List<ClasesCategorias> lista = persistenciaClasesCategorias.consultarClasesCategorias(em);
            return lista;
        } catch (Exception e) {
            System.out.println("Error lovClasesCategorias Admi : " + e.toString());
            return null;
        }
    }

    @Override
    public List<TiposSueldos> lovTiposSueldos() {
        try {
            List<TiposSueldos> lista = persistenciaTiposSueldos.buscarTiposSueldos(em);
            return lista;
        } catch (Exception e) {
            System.out.println("Error lovTiposSueldos Admi : " + e.toString());
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

}

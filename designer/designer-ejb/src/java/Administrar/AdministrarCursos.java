/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package Administrar;

import Entidades.Cursos;
import InterfaceAdministrar.AdministrarCursosInterface;
import InterfaceAdministrar.AdministrarSesionesInterface;
import InterfacePersistencia.PersistenciaCursosInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import org.apache.log4j.Logger;
/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla 'Cursos'.
 * @author betelgeuse
 */
@Stateful
public class AdministrarCursos implements AdministrarCursosInterface {

   private static Logger log = Logger.getLogger(AdministrarCursos.class);
    //--------------------------------------------------------------------------
    //ATRIBUTOS
    //--------------------------------------------------------------------------    
    /**
     * Enterprise JavaBeans.<br>
     * Atributo que representa la comunicación con la persistencia 'persistenciaCursos'.
     */
    @EJB
    PersistenciaCursosInterface persistenciaCursos;
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
    public List<Cursos> consultarCursos(){
        List<Cursos> listaCursos;
        listaCursos = persistenciaCursos.cursos(em);
        return listaCursos;
    }

    @Override
    public void crear(List<Cursos> listaCrear) {
        try {
            for (int i = 0; i < listaCrear.size(); i++) {
                persistenciaCursos.crear(em, listaCrear.get(i));
            }
        } catch (Exception e) {
            log.warn("Error en AdministrarCursos.crear : " + e.toString());
        }
    }

    @Override
    public void editar(List<Cursos> listaEditar) {
        try {
            for (int i = 0; i < listaEditar.size(); i++) {
                persistenciaCursos.editar(em, listaEditar.get(i));
            }
        } catch (Exception e) {
            log.warn("Error en AdministrarCursos.editar : " + e.toString());
        }
    }

    @Override
    public void borrar(List<Cursos> listaBorrar) {
        try {
            for (int i = 0; i < listaBorrar.size(); i++) {
                persistenciaCursos.borrar(em, listaBorrar.get(i));
            }
        } catch (Exception e) {
            log.warn("Error en AdministrarCursos.borrar : " + e.toString());
        }
    }
}



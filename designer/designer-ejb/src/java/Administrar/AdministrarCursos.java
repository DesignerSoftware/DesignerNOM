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
/**
 * Clase Stateful. <br>
 * Clase encargada de realizar las operaciones lógicas para la pantalla 'Cursos'.
 * @author betelgeuse
 */
@Stateful
public class AdministrarCursos implements AdministrarCursosInterface {
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
}



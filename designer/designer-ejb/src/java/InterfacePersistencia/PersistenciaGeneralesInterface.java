package InterfacePersistencia;

import Entidades.Generales;
import javax.persistence.EntityManager;

/**
 *
 * @author Felipphe Triviño
 */
public interface PersistenciaGeneralesInterface {

    /**
     * Método encargado de consultar en la base de datos las rutas que se
     * encuentran en generales.
     *
     * @return Retorna una entidad de tipo Generales que contiene las rutas.
     */
    public Generales obtenerRutas(EntityManager em);

    public String obtenerPreValidadContabilidad(EntityManager em);

    public String obtenerPreValidaBloqueAIngreso(EntityManager em);

    public String obtenerPathServidorWeb(EntityManager em);

    public String obtenerPathProceso(EntityManager em);
    
    public String obtenerPathError(EntityManager em);

}

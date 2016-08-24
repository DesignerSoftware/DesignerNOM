

package InterfacePersistencia;

import Entidades.JornadasSemanales;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;


public interface PersistenciaJornadasSemanalesInterface {
    
    public void crear(EntityManager em, JornadasSemanales jornadasSemanales);
    public void editar(EntityManager em, JornadasSemanales jornadasSemanales);
    public void borrar(EntityManager em, JornadasSemanales jornadasSemanales);
    public List<JornadasSemanales> buscarJornadasSemanales(EntityManager em);
    public JornadasSemanales buscarJornadaSemanalSecuencia(EntityManager em, BigInteger secuencia);
    public List<JornadasSemanales> buscarJornadasSemanalesPorJornadaLaboral(EntityManager em, BigInteger secuencia);
    
    
}

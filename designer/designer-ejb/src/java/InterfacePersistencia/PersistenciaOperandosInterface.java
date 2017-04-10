/**
 * Documentación a cargo de Hugo David Sin Gutiérrez
 */
package InterfacePersistencia;

import Entidades.Operandos;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la
 * tabla 'Operandos' de la base de datos.
 *
 * @author Andres Pineda.
 */
public interface PersistenciaOperandosInterface {

    /**
     * Método encargado de buscar todos los Operandos existentes en la base de
     * datos.
     *
     * @return Retorna una lista de Operandos.
     */
    public List<Operandos> buscarOperandos(EntityManager em);

    public void crear(EntityManager em, Operandos operandos);

    public void editar(EntityManager em, Operandos operandos);

    public void borrar(EntityManager em, Operandos operandos);

    public String valores(EntityManager em, BigInteger secuenciaOperando);

    public Operandos operandosPorSecuencia(EntityManager em, BigInteger secuencia);

    public List<Operandos> operandoPorConceptoSoporte(EntityManager em, BigInteger secConcepto);
    
    public String clonarOperando(EntityManager em, short codigoOrigen, String nombreDes, String descripcionDes);
}

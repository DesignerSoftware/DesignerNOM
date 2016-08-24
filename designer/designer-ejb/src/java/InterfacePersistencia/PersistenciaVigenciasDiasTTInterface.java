/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.VigenciasDiasTT;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaVigenciasDiasTTInterface {

    public void crear(EntityManager em, VigenciasDiasTT vigenciasDiasTT);

    public void editar(EntityManager em, VigenciasDiasTT vigenciasDiasTT);

    public void borrar(EntityManager em, VigenciasDiasTT vigenciasDiasTT);

    public List<VigenciasDiasTT> consultarDiasPorTT(EntityManager em, BigInteger secuenciaTT);
}

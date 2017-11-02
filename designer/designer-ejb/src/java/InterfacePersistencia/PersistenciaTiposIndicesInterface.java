/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.TiposIndices;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaTiposIndicesInterface {

    public String crear(EntityManager em, TiposIndices tiposIndices);

    public String editar(EntityManager em, TiposIndices tiposIndices);

    public String borrar(EntityManager em, TiposIndices tiposIndices);

    public List<TiposIndices> consultarTiposIndices(EntityManager em );

    public TiposIndices consultarTipoIndice(EntityManager em, BigInteger secuencia);

    public BigInteger contarIndicesTipoIndice(EntityManager em, BigInteger secuencia);
}

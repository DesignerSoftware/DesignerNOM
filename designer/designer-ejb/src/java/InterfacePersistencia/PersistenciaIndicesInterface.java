/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.Indices;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Local
public interface PersistenciaIndicesInterface {

    public void crear(EntityManager em, Indices indices);

    public void editar(EntityManager em, Indices indices);

    public void borrar(EntityManager em, Indices indices);

    public List<Indices> consultarIndices(EntityManager em);

    public BigInteger contarUsuariosIndicesIndice(EntityManager em, BigInteger secuencia);

    public BigInteger contarParametrosIndicesIndice(EntityManager em, BigInteger secuencia);

    public BigInteger contarResultadosIndicesSoportesIndice(EntityManager em, BigInteger secuencia);

    public BigInteger contarResultadosIndicesDetallesIndice(EntityManager em, BigInteger secuencia);

    public BigInteger contarResultadosIndicesIndice(EntityManager em, BigInteger secuencia);
    
     public BigInteger contarCodigosRepetidosIndices(EntityManager em, Short codigo);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.ElementosCausasAccidentes;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Local
public interface PersistenciaElementosCausasAccidentesInterface {

    public void crear(EntityManager em,ElementosCausasAccidentes elementosCausasAccidentes);

    public void editar(EntityManager em,ElementosCausasAccidentes elementosCausasAccidentes);

    public void borrar(EntityManager em,ElementosCausasAccidentes elementosCausasAccidentes);

    public ElementosCausasAccidentes buscarElementoCausaAccidente(EntityManager em,BigInteger secuenciaECA);

    public List<ElementosCausasAccidentes> buscarElementosCausasAccidentes(EntityManager em);

    public BigInteger contadorSoAccidentesMedicos(EntityManager em,BigInteger secuencia);

    public BigInteger contadorSoAccidentes(EntityManager em,BigInteger secuencia);

    public BigInteger contadorSoIndicadoresFr(EntityManager em,BigInteger secuencia);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.TiposUnidades;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Local
public interface PersistenciaTiposUnidadesInterface {

    public void crear(EntityManager em, TiposUnidades tiposUnidades);

    public void editar(EntityManager em, TiposUnidades tiposUnidades);

    public void borrar(EntityManager em, TiposUnidades tiposUnidades);

    public List<TiposUnidades> consultarTiposUnidades(EntityManager em );

    public TiposUnidades consultarTipoUnidad(EntityManager em, BigInteger secuencia);

    public BigInteger contarUnidadesTipoUnidad(EntityManager em, BigInteger secuencia);
}

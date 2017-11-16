/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.TiposAnexos;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Local
public interface PersistenciaTiposAnexosInterface {
    public String crear(EntityManager em,TiposAnexos tiposAnexos);

    public String editar(EntityManager em,TiposAnexos tiposAnexos);

    public String borrar(EntityManager em,TiposAnexos tiposAnexos);

    public TiposAnexos buscarTiposAnexos(EntityManager em,BigInteger sectiposAnexos);

    public List<TiposAnexos> buscarTiposAnexos(EntityManager em);

    public BigInteger contadorAnexos(EntityManager em,BigInteger sectiposAnexos);

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.IndicesExternos;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaIndicesExternosInterface {

    public void crear(EntityManager em, IndicesExternos indice);
    public void editar(EntityManager em, IndicesExternos indice);
    public void borrar(EntityManager em, IndicesExternos indice);
    public List<IndicesExternos> buscarIndicesExternos(EntityManager em);
}

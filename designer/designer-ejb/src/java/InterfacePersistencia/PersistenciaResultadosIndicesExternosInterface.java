/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.IndicesExternos;
import Entidades.ResultadosIndicesExternos;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaResultadosIndicesExternosInterface {

    public void crear(EntityManager em, ResultadosIndicesExternos resultadoindice);
    public void editar(EntityManager em, ResultadosIndicesExternos resultadoindice);
    public void borrar(EntityManager em, ResultadosIndicesExternos resultadoindice);
    public List<IndicesExternos> buscarIndicesExternos(EntityManager em);
    public List<ResultadosIndicesExternos> buscarResultadosIndicesExternos(EntityManager em);

}

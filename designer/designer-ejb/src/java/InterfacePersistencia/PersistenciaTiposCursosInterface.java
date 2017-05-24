/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.TiposCursos;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaTiposCursosInterface {

    public void crear(EntityManager em, TiposCursos tiposCursos);

    public void editar(EntityManager em, TiposCursos tiposCursos);

    public void borrar(EntityManager em, TiposCursos tiposCursos);

    public List<TiposCursos> consultarTiposCursos(EntityManager em);

    public TiposCursos consultarTipoCurso(EntityManager em, BigInteger secuencia);

    public BigInteger contarCursosTipoCurso(EntityManager em, BigInteger secuencia);
}

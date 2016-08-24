/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.TiposConclusiones;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Local
public interface PersistenciaTiposConclusionesInterface {

    public void crear(EntityManager em, TiposConclusiones tiposConclusiones);

    public void editar(EntityManager em, TiposConclusiones tiposConclusiones);

    public void borrar(EntityManager em, TiposConclusiones tiposConclusiones);

    public List<TiposConclusiones> consultarTiposConclusiones(EntityManager em);

    public TiposConclusiones consultarTipoConclusion(EntityManager em, BigInteger secuencia);

    public BigInteger contarChequeosMedicosTipoConclusion(EntityManager em, BigInteger secuencia);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.ConceptosRedondeos;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaConceptosRedondeosInterface {

    public List<ConceptosRedondeos> buscarConceptosRedondeos(EntityManager em);

    public void crear(EntityManager em,ConceptosRedondeos conceptosRedondeos);

    public void editar(EntityManager em,ConceptosRedondeos conceptosRedondeos);

    public void borrar(EntityManager em,ConceptosRedondeos conceptosRedondeos);
}

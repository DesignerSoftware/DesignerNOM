/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.ConceptosSoportes;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Local
public interface PersistenciaConceptosSoportesInterface {

    public void crear(EntityManager em,ConceptosSoportes conceptosSoportes);

    public void editar(EntityManager em,ConceptosSoportes conceptosSoportes);

    public void borrar(EntityManager em,ConceptosSoportes conceptosSoportes);

    public List<ConceptosSoportes> consultarConceptosSoportes(EntityManager em);

    public ConceptosSoportes consultarConceptoSoporte(EntityManager em,BigInteger secuencia);

    public BigInteger consultarConceptoSoporteConceptoOperador(EntityManager em,BigInteger concepto, BigInteger operador);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.ConceptosRetroactivos;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Local
public interface PersistenciaConceptosRetroactivosInterface {

    public void crear(EntityManager em, ConceptosRetroactivos conceptosRetroactivos);

    public void editar(EntityManager em, ConceptosRetroactivos conceptosRetroactivos);

    public void borrar(EntityManager em, ConceptosRetroactivos conceptosRetroactivos);

    public ConceptosRetroactivos buscarConceptoProyeccion(EntityManager em, BigInteger secuencia);

    public List<ConceptosRetroactivos> buscarConceptosRetroactivos(EntityManager em);
}

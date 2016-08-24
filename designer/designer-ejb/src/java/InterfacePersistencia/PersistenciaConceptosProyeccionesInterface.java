/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.ConceptosProyecciones;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Local
public interface PersistenciaConceptosProyeccionesInterface {

    public void crear(EntityManager em, ConceptosProyecciones contadorProyecciones);

    public void editar(EntityManager em, ConceptosProyecciones contadorProyecciones);

    public void borrar(EntityManager em, ConceptosProyecciones contadorProyecciones);

    public ConceptosProyecciones buscarConceptoProyeccion(EntityManager em, BigInteger secuencia);

    public List<ConceptosProyecciones> buscarConceptosProyecciones(EntityManager em);
}

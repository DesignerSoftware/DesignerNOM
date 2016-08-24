/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.ParametrosConjuntos;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaParametrosConjuntosInterface {

    public void crearParametros(EntityManager em, ParametrosConjuntos parametrosConjuntos);

    public void editarParametros(EntityManager em, ParametrosConjuntos parametrosConjuntos);

    public void borrarParametros(EntityManager em, ParametrosConjuntos parametrosConjuntos);

    public ParametrosConjuntos consultarParametros(EntityManager em);
}

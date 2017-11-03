/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.BloquesPantallas;
import Entidades.ObjetosBloques;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaObjetosBloquesInterface {

    public List<ObjetosBloques> consultarObjetosBloques(EntityManager em);

    public List<BloquesPantallas> consultarBloquesPantallas(EntityManager em);

    public String crear(EntityManager em, ObjetosBloques objeto);

    public String editar(EntityManager em, ObjetosBloques objeto);

    public String borrar(EntityManager em, ObjetosBloques objeto);
}

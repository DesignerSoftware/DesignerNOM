/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.ParametrosCorreccionesAutoL;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaParametrosCorreccionAutoLInterface {
     public void crearCorreccion(EntityManager em, ParametrosCorreccionesAutoL correccion);

    public void editarCorreccion(EntityManager em, ParametrosCorreccionesAutoL correccion);

    public void borrarCorreccion(EntityManager em, ParametrosCorreccionesAutoL correccion);
    
     public List<ParametrosCorreccionesAutoL> consultarParametrosCorreccionesAutoL(EntityManager em);
}

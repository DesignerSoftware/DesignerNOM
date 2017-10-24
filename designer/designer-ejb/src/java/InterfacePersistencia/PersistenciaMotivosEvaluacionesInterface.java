/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.MotivosEvaluaciones;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaMotivosEvaluacionesInterface {

    public String crear(EntityManager em, MotivosEvaluaciones motivo);

    public String editar(EntityManager em, MotivosEvaluaciones motivo);

    public String borrar(EntityManager em, MotivosEvaluaciones motivo);

    public List<MotivosEvaluaciones> buscarMotivosEvaluaciones(EntityManager em);

}

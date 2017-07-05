/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.PlantillasValidaLL;
import javax.persistence.EntityManager;

public interface PersistenciaPlantillasValidaLLInterface {

    public void crear(EntityManager em, PlantillasValidaLL plantillall);

    public void editar(EntityManager em, PlantillasValidaLL plantillall);

    public void borrar(EntityManager em, PlantillasValidaLL plantillall);
}

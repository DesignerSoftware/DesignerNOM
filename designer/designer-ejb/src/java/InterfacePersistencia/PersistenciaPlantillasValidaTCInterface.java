/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.PlantillasValidaTC;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaPlantillasValidaTCInterface {
    public void crear(EntityManager em, PlantillasValidaTC plantillatc);
    public void editar(EntityManager em, PlantillasValidaTC plantillatc);
    public void borrar(EntityManager em, PlantillasValidaTC plantillatc);
}

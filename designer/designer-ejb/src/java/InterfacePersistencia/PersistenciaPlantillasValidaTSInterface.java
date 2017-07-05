/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.PlantillasValidaTS;
import javax.persistence.EntityManager;

public interface PersistenciaPlantillasValidaTSInterface {

    public void crear(EntityManager em, PlantillasValidaTS plantillats);

    public void editar(EntityManager em, PlantillasValidaTS plantillats);

    public void borrar(EntityManager em, PlantillasValidaTS plantillats);
}

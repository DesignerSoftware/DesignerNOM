/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.PlantillasValidaRL;
import javax.persistence.EntityManager;

public interface PersistenciaPlantillasValidaRLInterface {

    public void crear(EntityManager em, PlantillasValidaRL plantillarl);

    public void editar(EntityManager em, PlantillasValidaRL plantillarl);

    public void borrar(EntityManager em, PlantillasValidaRL plantillarl);
}

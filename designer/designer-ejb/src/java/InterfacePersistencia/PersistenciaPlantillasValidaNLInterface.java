/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.PlantillasValidaNL;
import javax.persistence.EntityManager;

public interface PersistenciaPlantillasValidaNLInterface {

    public void crear(EntityManager em, PlantillasValidaNL plantillanl);

    public void editar(EntityManager em, PlantillasValidaNL plantillanl);

    public void borrar(EntityManager em, PlantillasValidaNL plantillanl);
}

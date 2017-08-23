/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.PlantillasValidaLL;
import Entidades.PlantillasValidaNL;
import Entidades.PlantillasValidaRL;
import Entidades.PlantillasValidaTC;
import Entidades.PlantillasValidaTS;
import Entidades.TiposTrabajadores;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaTiposTrabajadoresPlantillasInterface {

    public List<PlantillasValidaTC> consultarPlanillaTC(EntityManager em, BigInteger secTipoT);

    public List<PlantillasValidaTS> consultarPlanillaTS(EntityManager em, BigInteger secTipoT);

    public List<PlantillasValidaRL> consultarPlanillaRL(EntityManager em, BigInteger secTipoT);

    public List<PlantillasValidaLL> consultarPlanillaLL(EntityManager em, BigInteger secTipoT);

    public List<PlantillasValidaNL> consultarPlanillaNL(EntityManager em, BigInteger secTipoT);

    public List<TiposTrabajadores> consultarTiposTrabajadores(EntityManager em);
    
    public boolean consultarRegistrosSecundarios(EntityManager em, BigInteger secuencia);

}

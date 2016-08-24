/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package InterfacePersistencia;

import Entidades.EvalActividades;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Local
public interface PersistenciaEvalActividadesInterface {
    public void crear(EntityManager em,EvalActividades evalCompetencias);
    public void editar(EntityManager em,EvalActividades evalCompetencias);
    public void borrar(EntityManager em,EvalActividades evalCompetencias);

    public List<EvalActividades> consultarEvalActividades(EntityManager em);
    public EvalActividades consultarEvalActividad(EntityManager em,BigInteger secuencia);
    public BigInteger contarEvalPlanesDesarrollosEvalActividad(EntityManager em,BigInteger secuencia);
    public BigInteger contarCapNecesidadesEvalActividad(EntityManager em,BigInteger secuencia) ;
    public BigInteger contarCapBuzonesEvalActividad(EntityManager em,BigInteger secuencia);
}

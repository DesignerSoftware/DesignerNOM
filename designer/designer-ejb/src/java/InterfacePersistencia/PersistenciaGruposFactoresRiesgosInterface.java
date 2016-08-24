/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package InterfacePersistencia;

import Entidades.GruposFactoresRiesgos;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Local
public interface PersistenciaGruposFactoresRiesgosInterface {
public void crear(EntityManager em,GruposFactoresRiesgos grupoFactoresRiesgos);
    public void editar(EntityManager em,GruposFactoresRiesgos grupoFactoresRiesgos);
    public void borrar(EntityManager em,GruposFactoresRiesgos grupoFactoresRiesgos);
    public List<GruposFactoresRiesgos> consultarGruposFactoresRiesgos(EntityManager em);
    public GruposFactoresRiesgos consultarGrupoFactorRiesgo(EntityManager em,BigInteger secuencia);
    public BigInteger contarSoProActividadesGrupoFactorRiesgo(EntityManager em,BigInteger secuencia) ;
    public BigInteger contarSoIndicadoresGrupoFactorRiesgo(EntityManager em,BigInteger secuencia);
    public BigInteger contarFactoresRiesgoGrupoFactorRiesgo(EntityManager em,BigInteger secuencia);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.VWContabilidadDetallada;
import Entidades.VWContabilidadResumida1;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaVWContabilidadResumida1Interface {

    public List<VWContabilidadResumida1> buscarContabilidadResumidaParametroContable(EntityManager em, Date fechaInicial, Date fechaFinal, BigInteger Proceso);

    public List<VWContabilidadDetallada> buscarContabilidadDetalladaParametroContable(EntityManager em, Date fechaInicial, Date fechaFinal, BigInteger Proceso);

    public Integer abrirPeriodoContable(EntityManager em, Date fechaInicial, Date fechaFinal, BigInteger Proceso);
    
    public void actualizarPeriodoContable(EntityManager em, Date fechaInicial, Date fechaFinal, BigInteger Proceso);
}

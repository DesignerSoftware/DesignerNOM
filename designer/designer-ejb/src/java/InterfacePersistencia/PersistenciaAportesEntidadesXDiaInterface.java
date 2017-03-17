/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.AportesEntidadesXDia;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaAportesEntidadesXDiaInterface {

    public void crear(EntityManager em, AportesEntidadesXDia aporteEntidad);

    public void editar(EntityManager em, AportesEntidadesXDia aporteEntidad);

    public void borrar(EntityManager em, AportesEntidadesXDia aporteEntidad);

    public List<AportesEntidadesXDia> consultarAportesEntidadesXDia(EntityManager em);

    public List<AportesEntidadesXDia> consultarAportesEntidadesPorEmpleadoMesYAnio(EntityManager em, BigInteger secEmpleado, short mes, short ano);
    
    public BigDecimal cosultarTarifa(EntityManager em,BigInteger secEmpresa, BigInteger secEmpleado, short mes, short ano, BigInteger secTipoEntidad);
}

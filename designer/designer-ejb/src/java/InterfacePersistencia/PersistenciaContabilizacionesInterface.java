/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.Contabilizaciones;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Administrador
 */
public interface PersistenciaContabilizacionesInterface {

    public void crear(EntityManager em, Contabilizaciones contabilizaciones);

    public void editar(EntityManager em, Contabilizaciones contabilizaciones);

    public void borrar(EntityManager em, Contabilizaciones contabilizaciones);

    public List<Contabilizaciones> buscarContabilizaciones(EntityManager em);

    public Date obtenerFechaMaximaContabilizaciones(EntityManager em);

    public Date obtenerFechaMaximaContabilizacionesSAPBOV8(EntityManager em);

    public void actualizarFlahInterconContableSAPBOV8(EntityManager em, Date fechaIni, Date fechaFin, BigInteger proceso);

    public Integer obtenerContadorFlagGeneradoFechasSAP(EntityManager em, Date fechaIni, Date fechaFin);

    public Integer obtenerContadorFlagGeneradoFechasTotal(EntityManager em, Date fechaIni, Date fechaFin);

    public Integer obtenerContadorFlagGeneradoFechasDynamics(EntityManager em, Date fechaIni, Date fechaFin);

    public void actualizarFlahInterconContableSAPBO(EntityManager em, Date fechaIni, Date fechaFin, BigInteger proceso);

}

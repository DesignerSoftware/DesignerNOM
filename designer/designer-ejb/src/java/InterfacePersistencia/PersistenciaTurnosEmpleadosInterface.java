/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.TurnosEmpleados;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Administrador
 */
public interface PersistenciaTurnosEmpleadosInterface {

    public Date obtenerFechaInicialMinimaTurnosEmpleados(EntityManager em);

    public Date obtenerFechaFinalMaximaTurnosEmpleados(EntityManager em);

    public int ejecutarPKG_CONTARNOVEDADESLIQ(EntityManager em, Date fechaDesde, Date fechaHasta, BigDecimal emplDesde, BigDecimal emplHasta);

    public void ejecutarPKG_ELIMINARLIQUIDACION(EntityManager em, Date fechaDesde, Date fechaHasta, BigDecimal emplDesde, BigDecimal emplHasta);

    public void crear(EntityManager em, TurnosEmpleados turnosEmpleados);

    public void editar(EntityManager em, TurnosEmpleados turnosEmpleados);

    public void borrar(EntityManager em, TurnosEmpleados turnosEmpleados);

    public List<TurnosEmpleados> buscarTurnosEmpleadosPorEmpleado(EntityManager em, BigInteger secuencia);

}

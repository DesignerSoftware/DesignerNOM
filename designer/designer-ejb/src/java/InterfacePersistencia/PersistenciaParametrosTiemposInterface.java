/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.ParametrosTiempos;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Administrador
 */
public interface PersistenciaParametrosTiemposInterface {

    public void crear(EntityManager em, ParametrosTiempos parametrosTiempos);

    public void editar(EntityManager em, ParametrosTiempos parametrosTiempos);

    public void borrar(EntityManager em, ParametrosTiempos parametrosTiempos);

    public List<ParametrosTiempos> buscarParametrosTiempos(EntityManager em);

    public ParametrosTiempos buscarParametrosTiemposPorUsuarioBD(EntityManager em, String aliasBD);

    public void ejecutarPKG_INSERTARCUADRILLA(EntityManager em, BigInteger cuadrilla, Date fechaDesde, Date fechaHasta);

    public void ejecutarPKG_SIMULARTURNOSEMPLEADOS(EntityManager em, Date fechaDesde, Date fechaHasta, BigDecimal emplDesde, BigDecimal emplHasta);

    public void ejecutarPKG_LIQUIDAR(EntityManager em, Date fechaDesde, Date fechaHasta, BigDecimal emplDesde, BigDecimal emplHasta, String formulaLiquidacion);

    public void ejecutarPKG_ELIMINARSIMULACION(EntityManager em, BigInteger cuadrilla, Date fechaDesde, Date fechaHasta, BigDecimal emplDesde, BigDecimal emplHasta);

    public void ejecutarPKG_EliminarProgramacion(EntityManager em, Date fechaDesde, Date fechaHasta);
}

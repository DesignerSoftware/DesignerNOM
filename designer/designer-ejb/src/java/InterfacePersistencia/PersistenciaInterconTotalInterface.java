/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.InterconTotal;
import excepciones.ExcepcionBD;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Administrador
 */
public interface PersistenciaInterconTotalInterface {

    public void crear(EntityManager em, InterconTotal interconTotal);

    public void editar(EntityManager em, InterconTotal interconTotal);

    public void borrar(EntityManager em, InterconTotal interconTotal);

    public InterconTotal buscarInterconTotalSecuencia(EntityManager em, BigInteger secuencia);

    public List<InterconTotal> buscarInterconTotalParaParametroContable(EntityManager em, Date fechaInicial, Date fechaFinal);

    public Date obtenerFechaContabilizacionMaxInterconTotal(EntityManager em);

    public void actualizarFlagInterconTotal(EntityManager em, Date fechaInicial, Date fechaFinal, Short empresa);

    public void ejecutarPKGUbicarnuevointercon_total(EntityManager em, BigInteger secuencia, Date fechaInicial, Date fechaFinal, BigInteger proceso);

    public void actualizarFlagInterconTotalProcesoDeshacer(EntityManager em, Date fechaInicial, Date fechaFinal, BigInteger proceso);

    public void eliminarInterconTotal(EntityManager em, Date fechaInicial, Date fechaFinal, Short empresa, BigInteger proceso);

    public int contarProcesosContabilizadosInterconTotal(EntityManager em, Date fechaInicial, Date fechaFinal);

    public void cerrarProcesoContabilizacion(EntityManager em, Date fechaInicial, Date fechaFinal, Short empresa, BigInteger proceso);

    public void ejecutarPKGRecontabilizacion(EntityManager em, Date fechaIni, Date fechaFin) throws ExcepcionBD;

    public void ejecutarPKGCrearArchivoPlano(EntityManager em, int tipoTxt, Date fechaIni, Date fechaFin, BigInteger proceso, String nombreArchivo);
    
    public void ejecutarPKGCrearArchivoPlano_GEO(EntityManager em, int tipoTxt, Date fechaIni, Date fechaFin, BigInteger proceso, String nombreArchivo);
    

}

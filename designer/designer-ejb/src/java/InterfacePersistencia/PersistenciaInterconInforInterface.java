/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.InterconInfor;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaInterconInforInterface {
 
    public void crear(EntityManager em, InterconInfor interconSapBO);

    public void editar(EntityManager em, InterconInfor interconSapBO);

    public void borrar(EntityManager em, InterconInfor interconSapBO);

    public InterconInfor buscarInterconInforSecuencia(EntityManager em, BigInteger secuencia);

    public List<InterconInfor> buscarInterconInforParametroContable(EntityManager em, Date fechaInicial, Date fechaFinal);

    public Date obtenerFechaMaxInterconInfor(EntityManager em);

    public void actualizarFlagProcesoAnularInterfaseContableInfor(EntityManager em, Date fechaIni, Date fechaFin);

    public void ejeuctarPKGUbicarnuevointercon_Infor(EntityManager em, BigInteger secuencia, Date fechaIni, Date fechaFin, BigInteger proceso);

    public void ejecutarDeleteInterconInfor(EntityManager em, Date fechaIni, Date fechaFin, BigInteger proceso);

    public void cerrarProcesoLiquidacion(EntityManager em, Date fechaIni, Date fechaFin, BigInteger proceso);

    public void ejecutarPKGRecontabilizacion(EntityManager em, Date fechaIni, Date fechaFin);

    public int contarProcesosContabilizadosInterconInfor(EntityManager em, Date fechaInicial, Date fechaFinal);

    public void ejecutarPKGCrearArchivoPlanoInfor(EntityManager em, Date fechaIni, Date fechaFin, BigInteger proceso, String descripcionProceso, String nombreArchivo);

    public void actualizarFlagInterconInforProcesoDeshacer(EntityManager em, Date fechaInicial, Date fechaFinal, BigInteger proceso);
    
    public void cerrarProcesoContabilizacion(EntityManager em, Date fechaInicial, Date fechaFinal, Short empresa, BigInteger proceso);
    
    public void eliminarInterconInfor(EntityManager em, Date fechaInicial, Date fechaFinal, Short empresa, BigInteger proceso);
    
     public void actualizarFlagInterconInfor(EntityManager em, Date fechaInicial, Date fechaFinal, Short empresa);
       
}

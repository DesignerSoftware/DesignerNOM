/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.InterconSapBO;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Administrador
 */
public interface PersistenciaInterconSapBOInterface {

    public void crear(EntityManager em, InterconSapBO interconSapBO);

    public void editar(EntityManager em, InterconSapBO interconSapBO);

    public void borrar(EntityManager em, InterconSapBO interconSapBO);

    public InterconSapBO buscarInterconSAPBOSecuencia(EntityManager em, BigInteger secuencia);

    public List<InterconSapBO> buscarInterconSAPBOParametroContable(EntityManager em, Date fechaInicial, Date fechaFinal);

    public Date obtenerFechaMaxInterconSAPBO(EntityManager em);

    public void actualizarFlagProcesoAnularInterfaseContableSAPBOV8(EntityManager em, Date fechaIni, Date fechaFin);

    public void ejeuctarPKGUbicarnuevointercon_SAPBOV8(EntityManager em, BigInteger secuencia, Date fechaIni, Date fechaFin, BigInteger proceso);

    public void ejecutarDeleteInterconSAPBOV8(EntityManager em, Date fechaIni, Date fechaFin, BigInteger proceso);

    public void cerrarProcesoLiquidacion(EntityManager em, Date fechaIni, Date fechaFin, BigInteger proceso);

    public void ejecutarPKGRecontabilizacion(EntityManager em, Date fechaIni, Date fechaFin);

    public void ejeuctarPKGUbicarnuevointercon_SAPBO_VCA(EntityManager em, BigInteger secuencia, Date fechaIni, Date fechaFin, BigInteger proceso);

    public int contarProcesosContabilizadosInterconSAPBO(EntityManager em, Date fechaInicial, Date fechaFinal);

    public void ejeuctarPKGUbicarnuevointercon_SAPBO_PE(EntityManager em, BigInteger secuencia, Date fechaIni, Date fechaFin, BigInteger proceso);

    public void ejeuctarPKGUbicarnuevointercon_SAPBOPQ(EntityManager em, BigInteger secuencia, Date fechaIni, Date fechaFin, BigInteger proceso);

    public void ejeuctarPKGUbicarnuevointercon_SAPBOVHP(EntityManager em, BigInteger secuencia, Date fechaIni, Date fechaFin, BigInteger proceso);

    public void ejecutarPKGCrearArchivoPlanoSAPV8(EntityManager em, Date fechaIni, Date fechaFin, BigInteger proceso, String descripcionProceso, String nombreArchivo);

    public void ejecutarPKGCrearArchivoPlanoSAPVCA(EntityManager em, Date fechaIni, Date fechaFin, BigInteger proceso, String descripcionProceso, String nombreArchivo);

    public void ejecutarPKGCrearArchivoPlanoSAPPE(EntityManager em, Date fechaIni, Date fechaFin, BigInteger proceso, String descripcionProceso, String nombreArchivo);

    public void ejecutarPKGCrearArchivoPlanoSAPPQ(EntityManager em, Date fechaIni, Date fechaFin, BigInteger proceso, String descripcionProceso, String nombreArchivo);

    public void ejecutarPKGCrearArchivoPlanoSAPHP(EntityManager em, Date fechaIni, Date fechaFin, BigInteger proceso, String descripcionProceso, String nombreArchivo);

    public void actualizarFlagProcesoAnularInterfaseContableSAPBO(EntityManager em, Date fechaIni, Date fechaFin);

    public void ejeuctarPKGUbicarnuevointercon_SAPBO(EntityManager em, BigInteger secuencia, Date fechaIni, Date fechaFin, BigInteger proceso);

    public void ejecutarPKGCrearArchivoPlanoSAPBO(EntityManager em, Date fechaIni, Date fechaFin, BigInteger proceso, String descripcionProceso, String nombreArchivo);
    
    public void actualizarFlagInterconSapBoProcesoDeshacer(EntityManager em, Date fechaInicial, Date fechaFinal, BigInteger proceso);
    
    public void cerrarProcesoContabilizacion(EntityManager em, Date fechaInicial, Date fechaFinal, Short empresa, BigInteger proceso);
    
    public void eliminarInterconSapBO(EntityManager em, Date fechaInicial, Date fechaFinal, Short empresa, BigInteger proceso);
    
     public void actualizarFlagInterconSapBO(EntityManager em, Date fechaInicial, Date fechaFinal, Short empresa);
    
}

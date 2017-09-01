/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.InterconInfor;
import excepciones.ExcepcionBD;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaInterconInforInterface {
 
    public void crear(EntityManager em, InterconInfor intercon);

    public void editar(EntityManager em, InterconInfor intercon);

    public void borrar(EntityManager em, InterconInfor intercon);

    public InterconInfor buscarInterconInforSecuencia(EntityManager em, BigInteger secuencia);

    public List<InterconInfor> buscarInterconInforParametroContable(EntityManager em, Date fechaInicial, Date fechaFinal);

    public Date obtenerFechaMaxInterconInfor(EntityManager em);

     public void actualizarFlagInterconInfor(EntityManager em, Date fechaInicial, Date fechaFinal, Short empresa);
     
    public void ejecutarPKGUbicarNewInterCon_Infor(EntityManager em, BigInteger secuencia, Date fechaIni, Date fechaFin, BigInteger proceso);

    public void actualizarFlagInterconInforProcesoDeshacer(EntityManager em, Date fechaInicial, Date fechaFinal, BigInteger proceso);

    public void eliminarInterconInfor(EntityManager em, Date fechaInicial, Date fechaFinal, Short empresa, BigInteger proceso);

    public int contarProcesosContabilizadosInterconInfor(EntityManager em, Date fechaInicial, Date fechaFinal);

    public void cerrarProcesoContabilizacion(EntityManager em, Date fechaIni, Date fechaFin,Short empresa,BigInteger proceso);
    
    public void ejecutarPKGRecontabilizacion(EntityManager em, Date fechaIni, Date fechaFin) throws ExcepcionBD;
    
    public String ejecutarPKGCrearArchivoPlanoInfor(EntityManager em, Date fechaIni, Date fechaFin,BigInteger CodigoEmpresa, BigInteger proceso,String nombreArchivo);
    
}

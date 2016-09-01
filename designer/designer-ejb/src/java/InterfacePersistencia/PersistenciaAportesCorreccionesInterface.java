/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package InterfacePersistencia;

import Entidades.AportesCorrecciones;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaAportesCorreccionesInterface {
  public void crear(EntityManager em, AportesCorrecciones aportesEntidades);

    public void editar(EntityManager em, AportesCorrecciones aportesEntidades);

    public void borrar(EntityManager em, AportesCorrecciones aportesEntidades);

    public List<AportesCorrecciones> consultarAportesEntidades(EntityManager em);

    public List<AportesCorrecciones> consultarLovAportesEntidades(EntityManager em);
    
    public void borrarAportesCorreccionesProcesoAutomatico(EntityManager em, BigInteger secEmpresa, short mes, short ano);

    public String ejecutarPKGActualizarNovedadesCorreccion(EntityManager em, BigInteger secEmpresa, short mes, short ano);

    public String ejecutarPKGInsertarCorreccion(EntityManager em, Date fechaIni, Date fechaFin, BigInteger tipoTrabajador, BigInteger secEmpresa);
    
    public String ejecutarIncrementarCorreccion(EntityManager em, BigInteger secEmpresa, short mes, short ano);  
    
    public String ejecutarPKGIdentificaCorreccion(EntityManager em,BigInteger secEmpresa,short mes, short ano);
}

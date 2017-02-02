/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.AportesEntidades;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Administrador
 */
public interface PersistenciaAportesEntidadesInterface {

    public void crear(EntityManager em, AportesEntidades aportesEntidades);

    public void editar(EntityManager em, AportesEntidades aportesEntidades);

    public void borrar(EntityManager em, AportesEntidades aportesEntidades);

    public List<AportesEntidades> consultarAportesEntidades(EntityManager em);

    public List<AportesEntidades> consultarAportesEntidadesPorEmpresaMesYAnio(EntityManager em, BigInteger secEmpresa, short mes, short ano);

    public void borrarAportesEntidadesProcesoAutomatico(EntityManager em, BigInteger secEmpresa, short mes, short ano);

    public String ejecutarPKGActualizarNovedades(EntityManager em, BigInteger secEmpresa, short mes, short ano);

    public String ejecutarPKGInsertar(EntityManager em, Date fechaIni, Date fechaFin, BigInteger tipoTrabajador, BigInteger secEmpresa);
    
    public void ejecutarAcumularDiferencia(EntityManager em, BigInteger secEmpresa, short mes, short ano);

    public List<AportesEntidades> consultarAportesEntidadesPorEmpleado (EntityManager em, BigInteger secEmpleado, short mes, short ano);
    
}

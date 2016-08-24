/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.FirmasReportes;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Local
public interface PersistenciaFirmasReportesInterface {

    public void crear(EntityManager em,FirmasReportes tiposCursos);

    public void editar(EntityManager em,FirmasReportes tiposCursos);

    public void borrar(EntityManager em,FirmasReportes tiposCursos);

    public List<FirmasReportes> consultarFirmasReportes(EntityManager em);

    public FirmasReportes consultarFirmaReporte(EntityManager em,BigInteger secuencia);
}

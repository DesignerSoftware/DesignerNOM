/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.EmpresasBancos;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Local
public interface PersistenciaEmpresasBancosInterface {

    public void crear(EntityManager em,EmpresasBancos empresasBancos);

    public void editar(EntityManager em,EmpresasBancos empresasBancos);

    public void borrar(EntityManager em,EmpresasBancos empresasBancos);

    public List<EmpresasBancos> consultarEmpresasBancos(EntityManager em);

    public EmpresasBancos consultarFirmaReporte(EntityManager em,BigInteger secuencia);
}

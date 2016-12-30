/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.Bancos;
import Entidades.CuentasBancos;
import Entidades.Inforeportes;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
@Local
public interface PersistenciaCuentasBancosInterface {

    public void crear(EntityManager em, CuentasBancos cuentabanco);

    public void editar(EntityManager em, CuentasBancos cuentabanco);

    public void borrar(EntityManager em, CuentasBancos cuentabanco);

    public List<CuentasBancos> buscarCuentasBanco(EntityManager em);
    
    public List<Bancos> buscarBancos(EntityManager em);
    
    public List<Inforeportes> buscarReportes(EntityManager em);

}

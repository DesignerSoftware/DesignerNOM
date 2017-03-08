/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.HistoricosUsuarios;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaHistoricosUsuariosInterface {
   public List<HistoricosUsuarios> buscarHistoricosUsuarios(EntityManager em,BigInteger secUsuario);
    public void crear(EntityManager em,HistoricosUsuarios historicou);
    public void editar(EntityManager em,HistoricosUsuarios historicou);
    public void borrar(EntityManager em,HistoricosUsuarios historicou); 
}

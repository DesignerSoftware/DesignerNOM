/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.SoTiposAntecedentes;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaSoTiposAntecedentesInterface {
    public void crear(EntityManager em,SoTiposAntecedentes tipoantecedente);
    public void borrar(EntityManager em,SoTiposAntecedentes tipoantecedente);
    public void editar(EntityManager em,SoTiposAntecedentes tipoantecedente);
    public List<SoTiposAntecedentes> lovTiposAntecedentes(EntityManager em);
    public List<SoTiposAntecedentes> listaTiposAntecedentes(EntityManager em);
    
}

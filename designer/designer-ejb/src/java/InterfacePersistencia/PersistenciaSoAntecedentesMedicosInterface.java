/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.SoAntecedentes;
import Entidades.SoAntecedentesMedicos;
import Entidades.SoTiposAntecedentes;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaSoAntecedentesMedicosInterface {
  public void crear(EntityManager em, SoAntecedentesMedicos antecedenteM);

    public void borrar(EntityManager em, SoAntecedentesMedicos antecedenteM);

    public void editar(EntityManager em, SoAntecedentesMedicos antecedenteM);

    public List<SoAntecedentes> lovAntecedentes(EntityManager em, BigInteger secTipoAntecedente);

    public List<SoTiposAntecedentes> lovTiposAntecedentes(EntityManager em);  
    
    public List<SoAntecedentesMedicos> listaAntecedentesMedicos(EntityManager em,BigInteger secEmpleado);
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.SoAntecedentes;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaSoAntecedentesInterface {

    public void crear(EntityManager em, SoAntecedentes antecedente);

    public void borrar(EntityManager em, SoAntecedentes antecedente);

    public void editar(EntityManager em, SoAntecedentes antecedente);

    public List<SoAntecedentes> lovAntecedentes(EntityManager em, BigInteger secTipoAntecedente);

    public List<SoAntecedentes> listaAntecedentes(EntityManager em);
}

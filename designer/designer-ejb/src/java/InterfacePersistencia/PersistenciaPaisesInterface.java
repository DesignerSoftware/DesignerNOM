/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.Paises;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaPaisesInterface {

    public void crear(EntityManager em, Paises tiposAusentismos);

    public void editar(EntityManager em, Paises tiposAusentismos);

    public void borrar(EntityManager em, Paises tiposAusentismos);

    public List<Paises> consultarPaises(EntityManager em);

    public Paises consultarPais(EntityManager em, BigInteger secClaseCategoria);

    public BigInteger contarDepartamentosPais(EntityManager em, BigInteger secuencia);

    public BigInteger contarFestivosPais(EntityManager em, BigInteger secuencia);
}

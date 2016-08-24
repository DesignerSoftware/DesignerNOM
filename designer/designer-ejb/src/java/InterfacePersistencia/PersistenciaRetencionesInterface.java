/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.Retenciones;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaRetencionesInterface {

    public void crear(EntityManager em, Retenciones retenciones);

    public void editar(EntityManager em, Retenciones retenciones);

    public void borrar(EntityManager em, Retenciones retenciones);

    public List<Retenciones> buscarRetenciones(EntityManager em);

    public List<Retenciones> buscarRetencionesVig(EntityManager em, BigInteger secRetencion);
}

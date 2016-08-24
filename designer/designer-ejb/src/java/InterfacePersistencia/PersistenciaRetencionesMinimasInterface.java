/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.RetencionesMinimas;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaRetencionesMinimasInterface {

    public List<RetencionesMinimas> retenciones(EntityManager em);

    public void crear(EntityManager em, RetencionesMinimas retenciones);

    public void editar(EntityManager em, RetencionesMinimas retenciones);

    public void borrar(EntityManager em, RetencionesMinimas retenciones);

    public List<RetencionesMinimas> buscarRetencionesMinimasVig(EntityManager em, BigInteger secRetencion);

}

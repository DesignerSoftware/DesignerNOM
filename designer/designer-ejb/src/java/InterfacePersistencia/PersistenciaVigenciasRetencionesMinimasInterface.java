/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.VigenciasRetencionesMinimas;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaVigenciasRetencionesMinimasInterface {

    public void crear(EntityManager em, VigenciasRetencionesMinimas vretenciones);

    public void editar(EntityManager em, VigenciasRetencionesMinimas vretenciones);

    public void borrar(EntityManager em, VigenciasRetencionesMinimas vretenciones);

    public List<VigenciasRetencionesMinimas> buscarVigenciasRetencionesMinimas(EntityManager em );

}

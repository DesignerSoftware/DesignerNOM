/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package InterfacePersistencia;

import Entidades.VwTiposEmpleados;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaVwTiposEmpleadosInterface {
    public List<VwTiposEmpleados> buscarTiposEmpleadosPorTipo(EntityManager em, String tipo);
    public List<VwTiposEmpleados> buscarTiposEmpleados (EntityManager em);
}

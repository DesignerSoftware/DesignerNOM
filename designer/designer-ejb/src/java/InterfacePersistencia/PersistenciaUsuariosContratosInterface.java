/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.UsuariosContratos;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaUsuariosContratosInterface {

    public String crear(EntityManager em, UsuariosContratos usuariots);

    public String editar(EntityManager em, UsuariosContratos usuariots);

    public String borrar(EntityManager em, UsuariosContratos usuariots);

    public List<UsuariosContratos> buscarUsuariosContratos(EntityManager em);
}

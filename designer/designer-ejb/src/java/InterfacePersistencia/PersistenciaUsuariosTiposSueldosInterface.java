/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.UsuariosTiposSueldos;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author user
 */
public interface PersistenciaUsuariosTiposSueldosInterface {

    public void crear(EntityManager em, UsuariosTiposSueldos usuariots);

    public void editar(EntityManager em, UsuariosTiposSueldos usuariots);

    public void borrar(EntityManager em, UsuariosTiposSueldos usuariots);

    public List<UsuariosTiposSueldos> buscarUsuariosTiposSueldos(EntityManager em);

}

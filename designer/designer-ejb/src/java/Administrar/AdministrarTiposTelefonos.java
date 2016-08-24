/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Administrar;

import Entidades.TiposTelefonos;
import InterfaceAdministrar.AdministrarTiposTelefonosInterface;
import InterfacePersistencia.PersistenciaTiposTelefonosInterface;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import InterfaceAdministrar.AdministrarSesionesInterface;
import javax.persistence.EntityManager;

@Stateful
public class AdministrarTiposTelefonos implements AdministrarTiposTelefonosInterface{

    @EJB
    PersistenciaTiposTelefonosInterface persistenciaTiposTelefonos;
    /**
     * Enterprise JavaBean.<br>
     * Atributo que representa todo lo referente a la conexión del usuario que
     * está usando el aplicativo.
     */
    @EJB
    AdministrarSesionesInterface administrarSesiones;

    private TiposTelefonos tt;
    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }
    
    @Override
    public List<TiposTelefonos> tiposTelefonos() {
        List<TiposTelefonos> listaTiposTelefonos;
        listaTiposTelefonos = persistenciaTiposTelefonos.tiposTelefonos(em);
        return listaTiposTelefonos;
    }

    @Override
    public void modificarTipoTelefono(List<TiposTelefonos> listaTiposTelefonosModificar) {
        for (int i = 0; i < listaTiposTelefonosModificar.size(); i++) {
            System.out.println("Modificando...");
            if (listaTiposTelefonosModificar.get(i).getSecuencia() == null) {
                tt = listaTiposTelefonosModificar.get(i);
            } else {
                tt = listaTiposTelefonosModificar.get(i);
            }
            persistenciaTiposTelefonos.editar(em, tt);
        }
    }

    @Override
    public void borrarTipoTelefono(TiposTelefonos tipoTelefono) {
        persistenciaTiposTelefonos.borrar(em, tipoTelefono);
    }

    @Override
    public void crearTipoTelefono(TiposTelefonos tipoTelefono) {
        persistenciaTiposTelefonos.crear(em, tipoTelefono);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.DetallesTiposCotizantes;
import Entidades.TiposCotizantes;
import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author user
 */
public interface AdministrarTiposCotizantesInterface {

    public void obtenerConexion(String idSesion);
    public List<TiposCotizantes> tiposCotizantes();
    public void borrarTipoCotizante(TiposCotizantes tiposCotizantes);
    public void crearTipoCotizante(TiposCotizantes tiposCotizantes);
    public void modificarTipoCotizante(List<TiposCotizantes> listaTiposCotizantesModificar);
    
}

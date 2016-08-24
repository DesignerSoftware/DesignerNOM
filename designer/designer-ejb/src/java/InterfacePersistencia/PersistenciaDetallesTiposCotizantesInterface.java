/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package InterfacePersistencia;

import Entidades.DetallesTiposCotizantes;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;


public interface PersistenciaDetallesTiposCotizantesInterface {
    
    public void crear(EntityManager em,DetallesTiposCotizantes detallesTiposCotizantes);
    public void editar(EntityManager em,DetallesTiposCotizantes detallesTiposCotizantes);
    public void borrar(EntityManager em,DetallesTiposCotizantes detallesTiposCotizantes);
    public List<DetallesTiposCotizantes> detallesTiposCotizantes(EntityManager em,BigInteger tipoCotizante);
    
}

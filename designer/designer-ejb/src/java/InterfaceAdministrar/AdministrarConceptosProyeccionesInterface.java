/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package InterfaceAdministrar;

import Entidades.Conceptos;
import Entidades.ConceptosProyecciones;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarConceptosProyeccionesInterface {
    public void obtenerConexion(String idSesion);
    public void crearConceptosProyecciones(List<ConceptosProyecciones> lista);
    public void borrarConceptosProyecciones(List<ConceptosProyecciones> lista);
    public void modificarConceptosProyecciones(List<ConceptosProyecciones> lista);
    public List<ConceptosProyecciones> consultarConceptosProyecciones() ;
    public List<Conceptos> consultarLOVConceptos();
}

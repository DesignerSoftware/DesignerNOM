/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfaceAdministrar;

import Entidades.Conceptos;
import Entidades.ConceptosRetroactivos;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author user
 */
@Local
public interface AdministrarConceptosRetroactivosInterface {

    public void obtenerConexion(String idSesion);

    public void crearConceptosRetroactivos(List<ConceptosRetroactivos> lista);

    public void borrarConceptosRetroactivos(List<ConceptosRetroactivos> lista);

    public void modificarConceptosRetroactivos(List<ConceptosRetroactivos> lista);

    public List<ConceptosRetroactivos> consultarConceptosRetroactivos();

    public List<Conceptos> consultarLOVConceptos();

    public List<Conceptos> consultarLOVConceptosRetro();
}

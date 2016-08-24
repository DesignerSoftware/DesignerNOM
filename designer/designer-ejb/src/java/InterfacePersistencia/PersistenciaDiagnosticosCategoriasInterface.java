/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package InterfacePersistencia;

import Entidades.Diagnosticoscapitulos;
import Entidades.Diagnosticoscategorias;
import Entidades.Diagnosticossecciones;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Interface encargada de determinar las operaciones que se realizan sobre la tabla 'DiagnosticosCategorias' 
 * de la base de datos.
 * @author Viktor
 */
public interface PersistenciaDiagnosticosCategoriasInterface {
    public List<Diagnosticoscategorias> buscarCategorias(EntityManager em,BigDecimal secSeccion);
    public List<Diagnosticoscategorias> buscarDiagnosticos(EntityManager em);
    public void crear(EntityManager em,Diagnosticoscategorias diagnosticosCategorias);
    public void editar(EntityManager em,Diagnosticoscategorias diagnosticosCategorias);
    public void borrar(EntityManager em,Diagnosticoscategorias diagnosticosCategorias);
    public List<Diagnosticoscapitulos> buscarCapitulo(EntityManager em);
    public void crearCapitulo(EntityManager em,Diagnosticoscapitulos diagnosticosCapitulo);
    public void editarCapitulo(EntityManager em,Diagnosticoscapitulos diagnosticosCapitulo);
    public void borrarCapitulo(EntityManager em,Diagnosticoscapitulos diagnosticosCapitulo);
    public List<Diagnosticossecciones> buscarSeccion(EntityManager em,BigDecimal secCapitulo);
    public void crearSeccion(EntityManager em,Diagnosticossecciones diagnosticosSeccion);
    public void editarSeccion(EntityManager em,Diagnosticossecciones diagnosticosSeccion);
    public void borrarSeccion(EntityManager em,Diagnosticossecciones diagnosticosSeccion);
}

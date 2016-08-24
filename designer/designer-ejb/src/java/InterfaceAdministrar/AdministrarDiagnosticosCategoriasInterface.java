/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package InterfaceAdministrar;

import Entidades.Diagnosticoscapitulos;
import Entidades.Diagnosticoscategorias;
import Entidades.Diagnosticossecciones;
import java.math.BigDecimal;
import java.util.List;

 public interface AdministrarDiagnosticosCategoriasInterface {
 public void obtenerConexion(String idSesion);
 public List<Diagnosticoscategorias> consultarDiagnosticoCategoria(BigDecimal secSeccion);
 public void crearDiagnosticoCategoria(List<Diagnosticoscategorias> categorias);
 public void editarDiagnosticoCategoria(List<Diagnosticoscategorias> categorias);
 public void borrarDiagnosticoCategoria(List<Diagnosticoscategorias> categorias);
 public List<Diagnosticoscapitulos> consultarDiagnosticoCapitulo();
 public void crearDiagnosticoCapitulo(List<Diagnosticoscapitulos> capitulo);
 public void editarDiagnosticoCapitulo(List<Diagnosticoscapitulos> capitulo);
 public void borrarDiagnosticoCapitulo(List<Diagnosticoscapitulos> capitulo);
  public List<Diagnosticossecciones> consultarDiagnosticoSeccion(BigDecimal secCapitulo);
 public void crearDiagnosticoSeccion(List<Diagnosticossecciones> seccion);
 public void editarDiagnosticoSeccion(List<Diagnosticossecciones> seccion);
 public void borrarDiagnosticoSeccion(List<Diagnosticossecciones> seccion);
 
}

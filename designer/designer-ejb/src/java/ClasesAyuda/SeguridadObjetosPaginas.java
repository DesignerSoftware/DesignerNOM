/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ClasesAyuda;

/**
 *
 * @author user
 */
public class SeguridadObjetosPaginas {
    //Consecutivo del elemento
    private String id;
    //Nombre del objeto
    private String nombre;
    //Tipo del objeto
    private String tipo;
    //Nombre de la pantalla (NOMINAF, NOVEDAD, ...)
    private String pantalla;
    //Nombre del bloque (INFOLABORAL, OTROSPERSONAL, TABLAS, ...)
    private String bloque;
    //Nombre antiguo del objeto
    private String nombreObjeto;
    //Tipo de objeto [LISTA, MOSTRADO, BOTON, CASILLA, TEXTO]
    private String tipoObjeto;
    //Tipo de activación [DIRECTO, PROGRAMATICO]
    private String tipoActivacion;
    //Permiso por defecto.
    private String permisoDefecto;

    public SeguridadObjetosPaginas(String id, String nombre, String tipo, String pantalla, String bloque, String nombreObjeto, String tipoObjeto) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.pantalla = pantalla;
        this.bloque = bloque;
        this.nombreObjeto = nombreObjeto;
        this.tipoObjeto = tipoObjeto;
        this.tipoActivacion = "DIRECTA";
        this.permisoDefecto = "S";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPantalla() {
        return pantalla;
    }

    public void setPantalla(String pantalla) {
        this.pantalla = pantalla;
    }

    public String getBloque() {
        return bloque;
    }

    public void setBloque(String bloque) {
        this.bloque = bloque;
    }

    public String getNombreObjeto() {
        return nombreObjeto;
    }

    public void setNombreObjeto(String nombreObjeto) {
        this.nombreObjeto = nombreObjeto;
    }

    public String getTipoObjeto() {
        return tipoObjeto;
    }

    public void setTipoObjeto(String tipoObjeto) {
        this.tipoObjeto = tipoObjeto;
    }

    public String getTipoActivacion() {
        return tipoActivacion;
    }

    public void setTipoActivacion(String tipoActivacion) {
        this.tipoActivacion = tipoActivacion;
    }

    public String getPermisoDefecto() {
        return permisoDefecto;
    }

    public void setPermisoDefecto(String permisoDefecto) {
        this.permisoDefecto = permisoDefecto;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Entidades.Familiares;
import Entidades.Personas;
import Entidades.TiposFamiliares;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.faces.bean.ManagedBean;
import org.primefaces.component.datatable.DataTable;

/**
 *
 * @author user
 */
@ManagedBean
@Named(value = "controlFamiliares")
@SessionScoped
public class ControlFamiliares implements Serializable {

    private List<Familiares> listaFamiliares;
    private List<Familiares> listaFamiliaresCrear;
    private List<Familiares> listaFamiliaresBorrar;
    private List<Familiares> listaFamiliaresEditar;
    private List<Familiares> listaFamiliaresFiltrar;
    private Familiares familiarSeleccionado;
    private Familiares nuevoFamiliar;
    private Familiares duplicarFamiliar;
    private Familiares editarFamiliar;
    //LOV PERSONAS
    private List<Personas> lovPersonas;
    private List<Personas> filtrarLovPersonas;
    private Personas personaSeleccionada;
    //LOV TIPOS FAMILIARES
    private List<TiposFamiliares> lovTiposFamiliares;
    private List<TiposFamiliares> filtrarLovTiposFamiliares;
    private TiposFamiliares tipoFamiliarSeleccionado;
    //OTROS
    private int tipoActualizacion;
    private int bandera;
    private boolean guardado;
    private boolean aceptar;
    private BigInteger l;
    private int k;
    private int cualCelda, tipoLista;
    private boolean permitirIndex;
    private String altoTabla;
    private String infoRegistroFamiliar,infoRegistroPersonas,infoRegistroTipoFamiliar;
    private DataTable tablaC;
    private boolean activarLOV;

    public ControlFamiliares() {
    }

}

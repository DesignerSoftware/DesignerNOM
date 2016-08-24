/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.awt.BorderLayout;
import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author user
 */
@Entity
@Table(name = "ODISCABECERAS")
public class OdisCabeceras implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @JoinColumn(name = "TERCERO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Terceros tercero;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ANO")
    private BigInteger anio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MES")
    private BigInteger mes;
    @Column(name = "VALORTOTAL")
    private BigInteger valortotal;
    @Size(max = 20)
    @Column(name = "NUMEROAUTORIZACION")
    private String numeroautorizacion;
    @JoinColumn(name = "TIPOENTIDAD", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private TiposEntidades tipoentidad;
    @Size(max = 1)
    @Column(name = "INCLUIRDETALLES")
    private String incluirdetalles;
    @Size(min = 1, max = 3)
    @Column(name = "ORIGENINCAPACIDAD")
    private String origenincapacidad;
    @JoinColumn(name = "EMPRESA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Empresas empresa;
    @JoinColumn(name = "SUCURSAL_PILA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private SucursalesPila sucursalpila;
    @Transient
    private boolean checkDetalles;

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Terceros getTercero() {
        return tercero;
    }

    public void setTercero(Terceros tercero) {
        this.tercero = tercero;
    }

    public BigInteger getAnio() {
        return anio;
    }

    public void setAnio(BigInteger anio) {
        this.anio = anio;
    }

    public BigInteger getMes() {
        return mes;
    }

    public void setMes(BigInteger mes) {
        this.mes = mes;
    }

    public BigInteger getValortotal() {
        return valortotal;
    }

    public void setValortotal(BigInteger valortotal) {
        this.valortotal = valortotal;
    }

    public String getNumeroautorizacion() {
        return numeroautorizacion;
    }

    public void setNumeroautorizacion(String numeroautorizacion) {
        this.numeroautorizacion = numeroautorizacion;
    }

    public TiposEntidades getTipoentidad() {
        return tipoentidad;
    }

    public void setTipoentidad(TiposEntidades tipoentdad) {
        this.tipoentidad = tipoentdad;
    }

    public String getIncluirdetalles() {
        return incluirdetalles;
    }

    public void setIncluirdetalles(String incluirdetalles) {
        this.incluirdetalles = incluirdetalles;
    }

    public boolean isCheckDetalles() {
        if (incluirdetalles == null) {
            checkDetalles = false;
        } else if (incluirdetalles.equalsIgnoreCase("S")) {
            checkDetalles = true;
            setIncluirdetalles("S");
        } else if (incluirdetalles.equalsIgnoreCase("N")) {
            checkDetalles = false;
            setIncluirdetalles("N");
        }
        return checkDetalles;
    }

    public void setCheckDetalles(boolean checkDetalles) {
        this.checkDetalles = checkDetalles;
        if (checkDetalles){
            incluirdetalles = "S";
        } else{
            incluirdetalles = "N";
        }
    }

    public String getOrigenincapacidad() {
        return origenincapacidad;
    }

    public void setOrigenincapacidad(String origenincapacidad) {
        this.origenincapacidad = origenincapacidad;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public SucursalesPila getSucursalpila() {
        return sucursalpila;
    }

    public void setSucursalpila(SucursalesPila sucursalpila) {
        this.sucursalpila = sucursalpila;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (secuencia != null ? secuencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof OdisCabeceras)) {
            return false;
        }
        OdisCabeceras other = (OdisCabeceras) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.OdisCabeceras[ secuencia=" + secuencia + " ]";
    }

}

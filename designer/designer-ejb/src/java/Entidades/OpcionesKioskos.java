/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author user
 */
@Entity
@Table(name = "OPCIONESKIOSKOS")
public class OpcionesKioskos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "CODIGO")
    @Size(max = 20)
    @NotNull
    private String codigo;
    @Column(name = "DESCRIPCION")
    @Size(max = 50)
    @NotNull
    private String descripcion;
    @JoinColumn(name = "EMPRESA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @Column(name = "AYUDA")
    private String ayuda;
    @Column(name = "CLASE")
    @Size(max = 20)
    @NotNull
    private String clase;
    @Column(name = "TIPOREPORTE")
    @Size(max = 20)
    private String tiporeporte;
    @Column(name = "NOMBREARCHIVO")
    @Size(max = 50)
    private String nombrearchivo;
    @Column(name = "EXTENSION")
    @Size(max = 10)
    private String extension;
    @JoinColumn(name = "OPCIONKIOSKOPADRE", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private OpcionesKioskos opcionkioskopadre;

    public OpcionesKioskos() {
    }

    public OpcionesKioskos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public String getCodigo() {
        if (codigo == null) {
            codigo = "";
        }
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        if (descripcion == null) {
            descripcion = "";
        }
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Empresas getEmpresa() {
        if (empresa == null) {
            empresa = new Empresas();
        }
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public String getAyuda() {
        if (ayuda == null) {
            ayuda = "";
        }
        return ayuda;
    }

    public void setAyuda(String ayuda) {
        this.ayuda = ayuda;
    }

    public String getClase() {
        if (clase == null) {
            clase = "";
        }
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public String getTiporeporte() {
        if (tiporeporte == null) {
            tiporeporte = "";
        }
        return tiporeporte;
    }

    public void setTiporeporte(String tiporeporte) {
        this.tiporeporte = tiporeporte;
    }

    public String getNombrearchivo() {
        if (nombrearchivo == null) {
            nombrearchivo = "";
        }
        return nombrearchivo;
    }

    public void setNombrearchivo(String nombrearchivo) {
        this.nombrearchivo = nombrearchivo;
    }

    public String getExtension() {
        if (extension == null) {
            extension = "";
        }
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public OpcionesKioskos getOpcionkioskopadre() {
        return opcionkioskopadre;
    }

    public void setOpcionkioskopadre(OpcionesKioskos opcionkioskopadre) {
        this.opcionkioskopadre = opcionkioskopadre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (secuencia != null ? secuencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the secuencia fields are not set
        if (!(object instanceof OpcionesKioskos)) {
            return false;
        }
        OpcionesKioskos other = (OpcionesKioskos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.OpcionesKioskos[ secuencia=" + secuencia + " ]";
    }

}

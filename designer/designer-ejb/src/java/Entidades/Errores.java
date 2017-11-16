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
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author user
 */
@Entity
@Table(name = "ERRORES")
public class Errores implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CODIGO")
    private Integer codigo;
    @NotNull
    @Column(name = "CODIGOORACLE")
    private Integer codigooracle;
    @NotNull
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @Column(name = "SOLUCION")
    private String solucion;

     public Errores() {

    }

    public Errores(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Errores(BigInteger secuencia, Integer codigo, Integer codigooracle, String descripcion, String solucion) {
        this.secuencia = secuencia;
        this.codigo = codigo;
        this.codigooracle = codigooracle;
        this.descripcion = descripcion;
        this.solucion = solucion;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodigooracle() {
        return codigooracle;
    }

    public void setCodigooracle(Integer codigooracle) {
        this.codigooracle = codigooracle;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getSolucion() {
        return solucion;
    }

    public void setSolucion(String solucion) {
        this.solucion = solucion;
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
        if (!(object instanceof Errores)) {
            return false;
        }
        Errores other = (Errores) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Errores[ id=" + secuencia + " ]";
    }

}

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
@Table(name = "TIPOSANEXOS")
public class TiposAnexos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "CODIGO")
    private Integer codigo;
    @Column(name = "NOMBRE")
    private String nombre;

    public TiposAnexos() {

    }

    public TiposAnexos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public TiposAnexos(BigInteger secuencia, Integer codigo, String nombre) {
        this.secuencia = secuencia;
        this.codigo = codigo;
        this.nombre = nombre;
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
        System.out.println("Entidades.TiposAnexos.setCodigo() codigo: " + codigo);
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        System.out.println("Entidades.TiposAnexos.setNombre() nombre: " + nombre);
        this.nombre = nombre;
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
        if (!(object instanceof TiposAnexos)) {
            return false;
        }
        TiposAnexos other = (TiposAnexos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.TiposAnexos[ secuencia=" + secuencia + " ]";
    }

}

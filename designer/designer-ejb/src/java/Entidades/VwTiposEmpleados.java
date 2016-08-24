/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 *
 * @author user
 */
@Entity
@Table(name = "VWTIPOSEMPLEADOS")
public class VwTiposEmpleados implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @NotNull
    @Column(name = "RFEMPLEADO")
    private BigInteger rfEmpleado;
    @Column(name = "CODIGOEMPLEADO")
    private BigInteger codigoEmpleado;
    @Column(name = "NOMBRE")
    private String nombre;
    @Column(name = "PRIMERAPELLIDO")
    private String primerApellido;
    @Column(name = "SEGUNDOAPELLIDO")
    private String segundoApellido;
    @Column(name = "TIPO")
    private String tipo;
    @Column(name = "RFEMPRESA")
    private BigInteger rfEmpresa;
    @Column(name = "EMPRESA")
    private String empresa;
    @Transient
    private String nombreCompleto;
    
    public BigInteger getRfEmpleado() {
        return rfEmpleado;
    }

    public void setRfEmpleado(BigInteger rfEmpleado) {
        this.rfEmpleado = rfEmpleado;
    }

    public BigInteger getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(BigInteger codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrimerApellido() {
        return primerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return segundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public BigInteger getRfEmpresa() {
        return rfEmpresa;
    }

    public void setRfEmpresa(BigInteger rfEmpresa) {
        this.rfEmpresa = rfEmpresa;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }
    public String getNombreCompleto() {
        if (nombreCompleto == null) {
            nombreCompleto = primerApellido + " " + segundoApellido + " " + nombre;
            if (nombreCompleto.equals("  ")) {
                nombreCompleto = null;
            }
            return nombreCompleto;
        } else {
            return nombreCompleto;
        }
    }

    public void setNombreCompleto(String nombreCompleto) {
        if (nombreCompleto != null) {
            this.nombreCompleto = nombreCompleto.toUpperCase();
        } else {
            this.nombreCompleto = nombreCompleto;
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rfEmpleado != null ? rfEmpleado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VwTiposEmpleados)) {
            return false;
        }
        VwTiposEmpleados other = (VwTiposEmpleados) object;
        if ((this.rfEmpleado == null && other.rfEmpleado != null) || (this.rfEmpleado != null && !this.rfEmpleado.equals(other.rfEmpleado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.VwTiposEmpleados[ id=" + rfEmpleado + " ]";
    }

}

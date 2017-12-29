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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 *
 * @author user
 */
@Entity
public class FondosRotatorios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "CODIGO")
    private Integer codigo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @JoinColumn(name = "EMPRESA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Empresas empresa;
    @NotNull
    @Column(name = "PRESTAMOSAPROBADOS")
    private Integer prestamosaprobados;
    @NotNull
    @Column(name = "PRESTAMOSDESEMBOLSADOS")
    private Integer prestamosdesembolsados;
    @NotNull
    @Column(name = "APROPIACIONPRESUPUESTAL")
    private Integer apropiacionpresupuestal;
    @NotNull
    @Column(name = "SALDOCARTERA")
    private Integer saldocartera;

    public FondosRotatorios() {
    }

    public FondosRotatorios(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public FondosRotatorios(BigInteger secuencia, Integer codigo, String descripcion, Empresas empresa, Integer prestamosaprobados, Integer prestamosdesembolsados, Integer apropiacionpresupuestal, Integer saldocartera) {
        this.secuencia = secuencia;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.empresa = empresa;
        this.prestamosaprobados = prestamosaprobados;
        this.prestamosdesembolsados = prestamosdesembolsados;
        this.apropiacionpresupuestal = apropiacionpresupuestal;
        this.saldocartera = saldocartera;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public Integer getPrestamosaprobados() {
        return prestamosaprobados;
    }

    public void setPrestamosaprobados(Integer prestamosaprobados) {
        this.prestamosaprobados = prestamosaprobados;
    }

    public Integer getPrestamosdesembolsados() {
        return prestamosdesembolsados;
    }

    public void setPrestamosdesembolsados(Integer prestamosdesembolsados) {
        this.prestamosdesembolsados = prestamosdesembolsados;
    }

    public Integer getApropiacionpresupuestal() {
        return apropiacionpresupuestal;
    }

    public void setApropiacionpresupuestal(Integer apropiacionpresupuestal) {
        this.apropiacionpresupuestal = apropiacionpresupuestal;
    }

    public Integer getSaldocartera() {
        return saldocartera;
    }

    public void setSaldocartera(Integer saldocartera) {
        this.saldocartera = saldocartera;
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
        if (!(object instanceof FondosRotatorios)) {
            return false;
        }
        FondosRotatorios other = (FondosRotatorios) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.FondosRotatorios[ secuencia=" + secuencia + " ]";
    }

}

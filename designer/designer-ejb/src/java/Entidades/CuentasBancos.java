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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author user
 */
@Entity
@Table(name = "CUENTASBANCOS")
public class CuentasBancos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @JoinColumn(name = "BANCO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Bancos banco;
    @Size(max = 15)
    @Column(name = "CUENTA")
    private String cuenta;
    @Size(max = 50)
    @Column(name = "NOMBREARCHIVO")
    private String nombrearchivo;
    @Size(max = 50)
    @Column(name = "ARCHIVOSALIDA")
    private String archivosalida;
    @Size(max = 1)
    @Column(name = "TIPOCUENTA")
    private String tipocuenta;
    @Column(name = "CODIGOCONVENIO")
    private BigInteger codigoconvenio;
    @Size(max = 15)
    @Column(name = "NOMBRECONVENIO")
    private String nombreconvenio;
    @Column(name = "CODIGOTESORERIA")
    private BigInteger codigotesoreria;
    @JoinColumn(name = "TERCERO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Terceros tercero;
    @Column(name = "CODIGOREPUBLICA")
    private BigInteger codigorepublica;
    @JoinColumn(name = "INFOREPORTE", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Inforeportes inforeporte;
    @Size(max=1)
    @Column(name="CONSECUTIVO")
    private String consecutivo;

    public CuentasBancos() {
    }

    public CuentasBancos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public CuentasBancos(BigInteger secuencia, Bancos banco, String cuenta, String nombrearchivo, String archivosalida, String tipocuenta, BigInteger codigoconvenio, String nombreconvenio, BigInteger codigotesoreria, Terceros tercero, BigInteger codigorepublica, Inforeportes inforeporte, String consecutivo) {
        this.secuencia = secuencia;
        this.banco = banco;
        this.cuenta = cuenta;
        this.nombrearchivo = nombrearchivo;
        this.archivosalida = archivosalida;
        this.tipocuenta = tipocuenta;
        this.codigoconvenio = codigoconvenio;
        this.nombreconvenio = nombreconvenio;
        this.codigotesoreria = codigotesoreria;
        this.tercero = tercero;
        this.codigorepublica = codigorepublica;
        this.inforeporte = inforeporte;
        this.consecutivo = consecutivo;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Bancos getBanco() {
        return banco;
    }

    public void setBanco(Bancos banco) {
        this.banco = banco;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public String getNombrearchivo() {
        return nombrearchivo;
    }

    public void setNombrearchivo(String nombrearchivo) {
        this.nombrearchivo = nombrearchivo;
    }

    public String getArchivosalida() {
        return archivosalida;
    }

    public void setArchivosalida(String archivosalida) {
        this.archivosalida = archivosalida;
    }

    public String getTipocuenta() {
        return tipocuenta;
    }

    public void setTipocuenta(String tipocuenta) {
        this.tipocuenta = tipocuenta;
    }

    public BigInteger getCodigoconvenio() {
        return codigoconvenio;
    }

    public void setCodigoconvenio(BigInteger codigoconvenio) {
        this.codigoconvenio = codigoconvenio;
    }

    public String getNombreconvenio() {
        return nombreconvenio;
    }

    public void setNombreconvenio(String nombreconvenio) {
        this.nombreconvenio = nombreconvenio;
    }

    public Terceros getTercero() {
        return tercero;
    }

    public void setTercero(Terceros tercero) {
        this.tercero = tercero;
    }

    public BigInteger getCodigotesoreria() {
        return codigotesoreria;
    }

    public void setCodigotesoreria(BigInteger codigotesoreria) {
        this.codigotesoreria = codigotesoreria;
    }

    public BigInteger getCodigorepublica() {
        return codigorepublica;
    }

    public void setCodigorepublica(BigInteger codigorepublica) {
        this.codigorepublica = codigorepublica;
    }

    public Inforeportes getInforeporte() {
        return inforeporte;
    }

    public void setInforeporte(Inforeportes inforeporte) {
        this.inforeporte = inforeporte;
    }

    public String getConsecutivo() {
        return consecutivo;
    }

    public void setConsecutivo(String consecutivo) {
        this.consecutivo = consecutivo;
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
        if (!(object instanceof CuentasBancos)) {
            return false;
        }
        CuentasBancos other = (CuentasBancos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.CuentasBancos[ secuencia=" + secuencia + " ]";
    }

}

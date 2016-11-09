/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

/**
 *
 * @author user
 */
@Entity
@Table(name = "ENVIOCORREOS")
public class EnvioCorreos implements Serializable {

  private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @JoinColumn(name = "EMPLEADO", referencedColumnName = "CODIGOEMPLEADO")
    @ManyToOne
    private Empleados codigoEmpleado;
    @Column(name = "CORREO")
    private String correo;
    @Column(name = "ESTADO")
    private String estado;
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @JoinColumn(name = "REPORTE", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Inforeportes reporte;
    @Column(name = "REENVIAR")
    private String reenviar;
    @Column(name = "CORREOORIGEN")
    private String correoorigen;
    @Transient
    private String nombreEmpleado;
    @Transient
    private Boolean estadoReenviar;

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Empleados getCodigoEmpleado() {
        return codigoEmpleado;
    }

    public void setCodigoEmpleado(Empleados codigoEmpleado) {
        this.codigoEmpleado = codigoEmpleado;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Inforeportes getReporte() {
        return reporte;
    }

    public void setReporte(Inforeportes reporte) {
        this.reporte = reporte;
    }

    public String getReenviar() {
        return reenviar;
    }

    public void setReenviar(String reenviar) {
        this.reenviar = reenviar;
    }

    public String getCorreoorigen() {
        return correoorigen;
    }

    public void setCorreoorigen(String correoorigen) {
        this.correoorigen = correoorigen;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }

    public Boolean getEstadoReenviar() {
        if (reenviar != null) {
            if (reenviar.equals("S")) {
                estadoReenviar = true;
            } else {
                estadoReenviar = false;
            }
        } else {
            estadoReenviar = false;
        }
        return estadoReenviar;
    }

    public void setEstadoReenviar(Boolean estadoReenviar) {
        if (estadoReenviar == true) {
            reenviar = "S";
        } else {
            reenviar = "N";
        }
        this.estadoReenviar = estadoReenviar;
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
        if (!(object instanceof EnvioCorreos)) {
            return false;
        }
        EnvioCorreos other = (EnvioCorreos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.EnvioCorreos[ secuencia=" + secuencia + " ]";
    }
}

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author user
 */
@Entity
public class TempSoAusentismos implements Serializable {

       private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "TIPO")
    private BigInteger tipo;
    @Column(name = "EMPLEADO")
    private BigInteger empleado;
    @Column(name = "CLASE")
    private BigInteger clase;
    @Column(name = "CAUSA")
    private BigInteger causa;
    @Column(name = "DIAS")
    private BigInteger dias;
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "FECHAFINAUS")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechafinaus;
    @Column(name = "FECHAEXPEDICION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaexpedicion;
    @Column(name = "FECHAINIPAGO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechainipago;
    @Column(name = "FECHAFINPAGO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechafinpago;
    @Column(name = "PORCENTAJEINDIVIDUAL")
    private BigInteger porcentajeindividual;
    @Column(name = "BASELIQUIDACION")
    private BigInteger baseliquidacion;
    @Column(name = "FORMALIQUIDACION")
    @Size(max = 50)
    private String formaliquidacion;
    @Column(name = "USUARIOBD")
    @Size(max = 30)
    private String usuariobd;
    @Column(name = "FECHAREPORTE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechareporte;
    @Column(name = "ARCHIVO")
    @Size(max = 30)
    private String archivo;
    @Column(name = "ESTADO")
    @Size(max = 1)
    private String estado;
    @Column(name = "RESULTADOVALIDACION")
    @Size(max = 500)
    private String resultadovalidacion;
    @Column(name = "DOCUMENTOSOPORTE")
    @Size(max = 100)
    private String documentosoporte;

    public TempSoAusentismos() {
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getTipo() {
        return tipo;
    }

    public void setTipo(BigInteger tipo) {
        this.tipo = tipo;
    }

    public BigInteger getEmpleado() {
        return empleado;
    }

    public void setEmpleado(BigInteger empleado) {
        this.empleado = empleado;
    }

    public BigInteger getClase() {
        return clase;
    }

    public void setClase(BigInteger clase) {
        this.clase = clase;
    }

    public BigInteger getCausa() {
        return causa;
    }

    public void setCausa(BigInteger causa) {
        this.causa = causa;
    }

    public BigInteger getDias() {
        return dias;
    }

    public void setDias(BigInteger dias) {
        this.dias = dias;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getFechafinaus() {
        return fechafinaus;
    }

    public void setFechafinaus(Date fechafinaus) {
        this.fechafinaus = fechafinaus;
    }

    public Date getFechaexpedicion() {
        return fechaexpedicion;
    }

    public void setFechaexpedicion(Date fechaexpedicion) {
        this.fechaexpedicion = fechaexpedicion;
    }

    public Date getFechainipago() {
        return fechainipago;
    }

    public void setFechainipago(Date fechainipago) {
        this.fechainipago = fechainipago;
    }

    public Date getFechafinpago() {
        return fechafinpago;
    }

    public void setFechafinpago(Date fechafinpago) {
        this.fechafinpago = fechafinpago;
    }

    public BigInteger getPorcentajeindividual() {
        return porcentajeindividual;
    }

    public void setPorcentajeindividual(BigInteger porcentajeindividual) {
        this.porcentajeindividual = porcentajeindividual;
    }

    public BigInteger getBaseliquidacion() {
        return baseliquidacion;
    }

    public void setBaseliquidacion(BigInteger baseliquidacion) {
        this.baseliquidacion = baseliquidacion;
    }

    public String getFormaliquidacion() {
        return formaliquidacion;
    }

    public void setFormaliquidacion(String formaliquidacion) {
        this.formaliquidacion = formaliquidacion;
    }

    public String getUsuariobd() {
        if(usuariobd == null){
            usuariobd = "USER";
        }
        return usuariobd;
    }

    public void setUsuariobd(String usuariobd) {
        this.usuariobd = usuariobd;
    }

    public Date getFechareporte() {
        return fechareporte;
    }

    public void setFechareporte(Date fechareporte) {
        this.fechareporte = fechareporte;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public String getEstado() {
        if(estado == null){
            estado = "N";
        }
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getResultadovalidacion() {
        return resultadovalidacion;
    }

    public void setResultadovalidacion(String resultadovalidacion) {
        this.resultadovalidacion = resultadovalidacion;
    }

    public String getDocumentosoporte() {
        return documentosoporte;
    }

    public void setDocumentosoporte(String documentosoporte) {
        this.documentosoporte = documentosoporte;
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
        if (!(object instanceof TempSoAusentismos)) {
            return false;
        }
        TempSoAusentismos other = (TempSoAusentismos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.TempSoAusentismos[ secuencia=" + secuencia + " ]";
    }
    
}

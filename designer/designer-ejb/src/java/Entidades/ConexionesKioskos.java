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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author user
 */
@Entity
@Table(name = "CONEXIONESKIOSKOS")
public class ConexionesKioskos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @JoinColumn(name = "EMPLEADO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Empleados empleado;
    @Column(name = "SEUDONIMO")
    @Size(max = 20)
    private String seudonimo;
    @Column(name = "PWD")
    private String pwd;
    @Column(name = "ACTIVO")
    @Size(max = 1)
    private String activo;
    @JoinColumn(name = "PREGUNTA1", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private PreguntasKioskos pregunta1;
    @Column(name = "RESPUESTA1")
    @Size(max = 50)
    private String respuesta1;
    @JoinColumn(name = "PREGUNTA2", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private PreguntasKioskos pregunta2;
    @Column(name = "RESPUESTA2")
    @Size(max = 50)
    private String respuesta2;
    @JoinColumn(name = "PREGUNTA3", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private PreguntasKioskos pregunta3;
    @Column(name = "RESPUESTA3")
    @Size(max = 50)
    private String respuesta3;
    @Column(name = "ULTIMACONEXION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ultimaconexion;
    @Column(name = "FECHADESDE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechadesde;
    @Column(name = "FECHAHASTA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechahasta;
    @Column(name = "ENVIOCORREO")
    @Size(max = 1)
    private String enviocorreo;
    @Column(name = "OBSERVACIONES")
    @Size(max = 200)
    private String observaciones;
    @Column(name = "DIRIGIDOA")
    @Size(max = 200)
    private String dirigidoa;
    @Transient
    private String activoStr;

    public ConexionesKioskos() {
    }

    public ConexionesKioskos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public String getSeudonimo() {
        return seudonimo;
    }

    public void setSeudonimo(String seudonimo) {
        this.seudonimo = seudonimo;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getActivo() {
        if (activo == null) {
            activo = "";
        }
        return activo;
    }

    public void setActivo(String activo) {
        this.activo = activo;
    }

    public String getRespuesta1() {
        return respuesta1;
    }

    public void setRespuesta1(String respuesta1) {
        this.respuesta1 = respuesta1;
    }

    public String getRespuesta2() {
        return respuesta2;
    }

    public void setRespuesta2(String respuesta2) {
        this.respuesta2 = respuesta2;
    }

    public PreguntasKioskos getPregunta1() {
        if(pregunta1 == null){
            pregunta1 = new PreguntasKioskos();
        }
        return pregunta1;
    }

    public void setPregunta1(PreguntasKioskos pregunta1) {
        this.pregunta1 = pregunta1;
    }

    public PreguntasKioskos getPregunta2() {
        if(pregunta2 == null){
            pregunta2 = new PreguntasKioskos();
        }
        return pregunta2;
    }

    public void setPregunta2(PreguntasKioskos pregunta2) {
        this.pregunta2 = pregunta2;
    }

    public PreguntasKioskos getPregunta3() {
        if(pregunta3 == null){
            pregunta3 = new PreguntasKioskos();
        }
        return pregunta3;
    }

    public void setPregunta3(PreguntasKioskos pregunta3) {
        this.pregunta3 = pregunta3;
    }

    public String getRespuesta3() {
        return respuesta3;
    }

    public void setRespuesta3(String respuesta3) {
        this.respuesta3 = respuesta3;
    }

    public Date getUltimaconexion() {
        return ultimaconexion;
    }

    public void setUltimaconexion(Date ultimaconexion) {
        this.ultimaconexion = ultimaconexion;
    }

    public Date getFechadesde() {
        return fechadesde;
    }

    public void setFechadesde(Date fechadesde) {
        this.fechadesde = fechadesde;
    }

    public Date getFechahasta() {
        return fechahasta;
    }

    public void setFechahasta(Date fechahasta) {
        this.fechahasta = fechahasta;
    }

    public String getEnviocorreo() {
        return enviocorreo;
    }

    public void setEnviocorreo(String enviocorreo) {
        this.enviocorreo = enviocorreo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getDirigidoa() {
        return dirigidoa;
    }

    public void setDirigidoa(String dirigidoa) {
        this.dirigidoa = dirigidoa;
    }

    public String getActivoStr() {
        getActivo();
        if (activo.equalsIgnoreCase("S")) {
            activoStr = "ACTIVO";
        } else if(activo.equalsIgnoreCase("N")) {
            activoStr = "INACTIVO";
        } else if(activo.equalsIgnoreCase("")){
            activoStr = "";
        }
        return activoStr;
    }

    public void setActivoStr(String activoStr) {
        this.activoStr = activoStr;
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
        if (!(object instanceof ConexionesKioskos)) {
            return false;
        }
        ConexionesKioskos other = (ConexionesKioskos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.ConexionesKioskos[ secuencia=" + secuencia + " ]";
    }

}

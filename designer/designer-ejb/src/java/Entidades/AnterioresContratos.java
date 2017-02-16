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

/**
 *
 * @author user
 */
@Entity
@Table(name = "ANTERIORESCONTRATOS")
public class AnterioresContratos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "FECHAINICIAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechainicial;
    @Column(name = "FECHAFINAL")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechafinal;
    @NotNull
    @JoinColumn(name = "PERSONA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Personas persona;
    @JoinColumn(name = "CARGO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Cargos cargo;
    @Column(name = "TELEFONO")
    private BigInteger telefono;
    @Column(name = "JEFE")
    private String jefe;
    @Column(name = "EMPRESA")
    private String empresa;
    @Column(name = "DIASANT")
    private BigInteger diasant;
    @Column(name = "TIEMPOREGIMENCESANTIAS")
    private String tiemporegimencesantias;
    @Transient
    private boolean checktiemporegces;

    public AnterioresContratos() {
    }

    public AnterioresContratos(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Date getFechainicial() {
        return fechainicial;
    }

    public void setFechainicial(Date fechainicial) {
        this.fechainicial = fechainicial;
    }

    public Date getFechafinal() {
        return fechafinal;
    }

    public void setFechafinal(Date fechafinal) {
        this.fechafinal = fechafinal;
    }

    public Personas getPersona() {
        return persona;
    }

    public void setPersona(Personas persona) {
        this.persona = persona;
    }

    public Cargos getCargo() {
        if(cargo == null){
            cargo = new Cargos();
        }
        return cargo;
    }

    public void setCargo(Cargos cargo) {
        this.cargo = cargo;
    }

    public BigInteger getTelefono() {
        return telefono;
    }

    public void setTelefono(BigInteger telefono) {
        this.telefono = telefono;
    }

    public String getJefe() {
        return jefe;
    }

    public void setJefe(String jefe) {
        this.jefe = jefe;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public BigInteger getDiasant() {
        return diasant;
    }

    public void setDiasant(BigInteger diasant) {
        this.diasant = diasant;
    }
    
    public String getTiemporegimencesantias() {
        return tiemporegimencesantias;
    }

    public void setTiemporegimencesantias(String tiemporegimencesantias) {
        this.tiemporegimencesantias = tiemporegimencesantias;
    }

    public boolean isChecktiemporegces() {
        if("S".equals(tiemporegimencesantias)){
            checktiemporegces = true;
        }else{
            checktiemporegces = false;
        }
        return checktiemporegces;
    }

    public void setChecktiemporegces(boolean checktiemporegces) {
        this.checktiemporegces = checktiemporegces;
        if(checktiemporegces == true){
            tiemporegimencesantias = "S";
        }else{
            tiemporegimencesantias = null;
        }
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
        if (!(object instanceof AnterioresContratos)) {
            return false;
        }
        AnterioresContratos other = (AnterioresContratos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.AnterioresContratos[ secuencia=" + secuencia + " ]";
    }

}

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
import javax.validation.constraints.NotNull;

/**
 *
 * @author user
 */
@Entity
@Table(name = "SOANTECEDENTESMEDICOS")
public class SoAntecedentesMedicos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FECHA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @JoinColumn(name = "TIPOANTECEDENTE", referencedColumnName = "SECUENCIA")
    @NotNull
    @ManyToOne(optional = false)
    private SoTiposAntecedentes tipoantecedente;
    @JoinColumn(name = "ANTECEDENTE", referencedColumnName = "SECUENCIA")
    @NotNull
    @ManyToOne(optional = false)
    private SoAntecedentes antecedente;
    @Column(name = "DESCRIPCION")
    private String descripcion;
    @JoinColumn(name = "EMPLEADO", referencedColumnName = "SECUENCIA")
    @NotNull
    @ManyToOne(optional = false)
    private Empleados empleado;

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public SoTiposAntecedentes getTipoantecedente() {
        return tipoantecedente;
    }

    public void setTipoantecedente(SoTiposAntecedentes tipoantecedente) {
        this.tipoantecedente = tipoantecedente;
    }

    public SoAntecedentes getAntecedente() {
        return antecedente;
    }

    public void setAntecedente(SoAntecedentes antecedente) {
        this.antecedente = antecedente;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
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
        if (!(object instanceof SoAntecedentesMedicos)) {
            return false;
        }
        SoAntecedentesMedicos other = (SoAntecedentesMedicos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.SoAntecedentesMedicos[ secuencia=" + secuencia + " ]";
    }
    
}

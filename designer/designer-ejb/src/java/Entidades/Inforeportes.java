package Entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author user
 */
@Entity
@Table(name = "INFOREPORTES")
public class Inforeportes implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Column(name = "CODIGO")
    private BigInteger codigo;
    @Size(max = 100)
    @Column(name = "NOMBRE")
    private String nombre;
    @Size(max = 15)
    @Column(name = "TIPO")
    private String tipo;
    @Size(max = 60)
    @Column(name = "NOMBREREPORTE")
    private String nombrereporte;
    @Size(max = 2)
    @Column(name = "FECDESDE")
    private String fecdesde;
    @Size(max = 2)
    @Column(name = "FECHASTA")
    private String fechasta;
    @Size(max = 2)
    @Column(name = "EMDESDE")
    private String emdesde;
    @Size(max = 2)
    @Column(name = "EMHASTA")
    private String emhasta;
    @Size(max = 2)
    @Column(name = "LOCALIZACION")
    private String localizacion;
    @Size(max = 2)
    @Column(name = "ESTADO")
    private String estado;
    @Size(max = 2)
    @Column(name = "GRUPO")
    private String grupo;
    @Size(max = 2)
    @Column(name = "TERCERO")
    private String tercero;
    @Size(max = 2)
    @Column(name = "TRABAJADOR")
    private String trabajador;
    @Size(max = 2)
    @Column(name = "TIPOTRABAJADOR")
    private String tipotrabajador;
    @Size(max = 2)
    @Column(name = "SOLICITUD")
    private String solicitud;
    @Size(max = 2)
    @Column(name = "CIUDAD")
    private String ciudad;
    @Size(max = 2)
    @Column(name = "ESTADOCIVIL")
    private String estadocivil;
    @Size(max = 2)
    @Column(name = "TIPOTELEFONO")
    private String tipotelefono;
    @Size(max = 2)
    @Column(name = "DEPORTE")
    private String deporte;
    @Size(max = 2)
    @Column(name = "IDIOMA")
    private String idioma;
    @Size(max = 2)
    @Column(name = "AFICION")
    private String aficion;
    @Size(max = 2)
    @Column(name = "JEFEDIVISION")
    private String jefedivision;
    @Size(max = 2)
    @Column(name = "RODAMIENTO")
    private String rodamiento;
    @Size(max = 1)
    @Column(name = "ENVIOMASIVO")
    private String enviomasivo;
    @Column(name = "CONTADOR")
    private BigDecimal contador;
    @Size(max = 1000)
    @Column(name = "PARAMETROS")
    private String parametros;
    @Size(max = 3000)
    @Column(name = "AYUDA")
    private String ayuda;
    @Size(max = 10)
    @Column(name = "CODIGOMEMO")
    private String codigomemo;
    @JoinColumn(name = "MODULO", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Modulos modulo;
    @Transient
    private boolean estadoFechaDesde;
    @Transient
    private boolean estadoFechaHasta;
    @Transient
    private boolean estadoEmpleadoDesde;
    @Transient
    private boolean estadoEmpleadoHasta;
    @Transient
    private boolean estadoLocalizacion;
    @Transient
    private boolean estadoEstado;
    @Transient
    private boolean estadoGrupo;
    @Transient
    private boolean estadoTercero;
    @Transient
    private boolean estadoTrabajador;
    @Transient
    private boolean estadoTipoTrabajador;
    @Transient
    private boolean estadoSolicitud;
    @Transient
    private boolean estadoCiudad;
    @Transient
    private boolean estadoTipoTelefono;
    @Transient
    private boolean estadoEstadoCivil;
    @Transient
    private boolean estadoDeporte;
    @Transient
    private boolean estadoIdioma;
    @Transient
    private boolean estadoAficion;
    @Transient
    private boolean estadoJefeDivision;
    @Transient
    private boolean estadoRodamiento;
    @Transient
    private boolean estadoEnvioMasivo;
    @Transient
    private String estadoTipo;

    public Inforeportes() {
    }

    public Inforeportes(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getCodigo() {
        return codigo;
    }

    public void setCodigo(BigInteger codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        if (nombre == null) {
            nombre = "";
        } else {
            nombre = nombre.toUpperCase();
        }
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombrereporte() {
        if (nombrereporte == null) {
            nombrereporte = "";
        }
        //return nombrereporte.toUpperCase();
        return nombrereporte;
    }

    public void setNombrereporte(String nombrereporte) {
        this.nombrereporte = nombrereporte;
    }

    public String getFecdesde() {
        if (fecdesde == null) {
            fecdesde = "NO";
        }
        return fecdesde;
    }

    public void setFecdesde(String fecdesde) {
        this.fecdesde = fecdesde;
    }

    public String getFechasta() {
        if (fechasta == null) {
            fechasta = "NO";
        }
        return fechasta;
    }

    public void setFechasta(String fechasta) {
        this.fechasta = fechasta;
    }

    public String getEmdesde() {
        if (emdesde == null) {
            emdesde = "NO";
        }
        return emdesde;
    }

    public void setEmdesde(String emdesde) {
        this.emdesde = emdesde;
    }

    public String getEmhasta() {
        if (emhasta == null) {
            emhasta = "NO";
        }
        return emhasta;
    }

    public void setEmhasta(String emhasta) {
        this.emhasta = emhasta;
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getTercero() {
        return tercero;
    }

    public void setTercero(String tercero) {
        this.tercero = tercero;
    }

    public String getTrabajador() {
        return trabajador;
    }

    public void setTrabajador(String trabajador) {
        this.trabajador = trabajador;
    }

    public String getTipotrabajador() {
        return tipotrabajador;
    }

    public void setTipotrabajador(String tipotrabajador) {
        this.tipotrabajador = tipotrabajador;
    }

    public String getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(String solicitud) {
        this.solicitud = solicitud;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getEstadocivil() {
        return estadocivil;
    }

    public void setEstadocivil(String estadocivil) {
        this.estadocivil = estadocivil;
    }

    public String getTipotelefono() {
        return tipotelefono;
    }

    public void setTipotelefono(String tipotelefono) {
        this.tipotelefono = tipotelefono;
    }

    public String getDeporte() {
        return deporte;
    }

    public void setDeporte(String deporte) {
        this.deporte = deporte;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getAficion() {
        return aficion;
    }

    public void setAficion(String aficion) {
        this.aficion = aficion;
    }

    public String getJefedivision() {
        return jefedivision;
    }

    public void setJefedivision(String jefedivision) {
        this.jefedivision = jefedivision;
    }

    public String getRodamiento() {
        return rodamiento;
    }

    public void setRodamiento(String rodamiento) {
        this.rodamiento = rodamiento;
    }

    public String getEnviomasivo() {
        return enviomasivo;
    }

    public void setEnviomasivo(String enviomasivo) {
        this.enviomasivo = enviomasivo;
    }

    public BigDecimal getContador() {
        return contador;
    }

    public void setContador(BigDecimal contador) {
        this.contador = contador;
    }

    public String getParametros() {
        return parametros;
    }

    public void setParametros(String parametros) {
        this.parametros = parametros;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void setAyuda(String ayuda) {
        this.ayuda = ayuda;
    }

    public String getCodigomemo() {
        return codigomemo;
    }

    public void setCodigomemo(String codigomemo) {
        this.codigomemo = codigomemo;
    }

    public Modulos getModulo() {
        return modulo;
    }

    public void setModulo(Modulos modulo) {
        this.modulo = modulo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstadoTipo() {
        if (estadoTipo == null) {
            if (tipo == null) {
                estadoTipo = "PDF";

            } else {
                if (tipo.equalsIgnoreCase("PDF")) {
                    estadoTipo = "PDF";
                } else if (tipo.equalsIgnoreCase("HTML")) {
                    estadoTipo = "HTML";
                } else if (tipo.equalsIgnoreCase("XLS")) {
                    estadoTipo = "XLS";
                } else if (tipo.equalsIgnoreCase("DELIMITED")) {
                    estadoTipo = "PLANO";
                } else if (tipo.equalsIgnoreCase("XML")) {
                    estadoTipo = "XML";
                } else if (tipo.equalsIgnoreCase("SPREADSHEET")) {
                    estadoTipo = "CSV";
                } else if (tipo.equalsIgnoreCase("XHTML")) {
                    estadoTipo = "XHTML";
                } else if (tipo.equalsIgnoreCase("RTF")) {
                    estadoTipo = "RTF";
                }
            }
        }
        return estadoTipo;
    }

    public void setEstadoTipo(String estadoTipo) {
        this.estadoTipo = estadoTipo;
    }

    public boolean isEstadoFechaDesde() {
        if (fecdesde != null) {
            if (fecdesde.equals("SI")) {
                estadoFechaDesde = true;
            } else {
                estadoFechaDesde = false;
            }
        } else {
            estadoFechaDesde = false;
        }
        return estadoFechaDesde;
    }

    public void setEstadoFechaDesde(boolean estadoFechaDesde) {
        if (estadoFechaDesde == true) {
            fecdesde = "SI";
        } else {
            fecdesde = "NO";
        }
        this.estadoFechaDesde = estadoFechaDesde;
    }

    public boolean isEstadoFechaHasta() {
        if (fechasta != null) {
            if (fechasta.equals("SI")) {
                estadoFechaHasta = true;
            } else {
                estadoFechaHasta = false;
            }
        } else {
            estadoFechaHasta = false;
        }
        return estadoFechaHasta;
    }

    public void setEstadoFechaHasta(boolean estadoFechaHasta) {
        if (estadoFechaHasta == true) {
            fechasta = "SI";
        } else {
            fechasta = "NO";
        }
        this.estadoFechaHasta = estadoFechaHasta;
    }

    public boolean isEstadoEmpleadoDesde() {
        if (emdesde != null) {
            if (emdesde.equals("SI")) {
                estadoEmpleadoDesde = true;
            } else {
                estadoEmpleadoDesde = false;
            }
        } else {
            estadoEmpleadoDesde = false;
        }
        return estadoEmpleadoDesde;
    }

    public void setEstadoEmpleadoDesde(boolean estadoEmpleadoDesde) {
        if (estadoEmpleadoDesde == true) {
            emdesde = "SI";
        } else {
            emdesde = "NO";
        }
        this.estadoEmpleadoDesde = estadoEmpleadoDesde;
    }

    public boolean isEstadoEmpleadoHasta() {
        if (emhasta != null) {
            if (emhasta.equals("SI")) {
                estadoEmpleadoHasta = true;
            } else {
                estadoEmpleadoHasta = false;
            }
        } else {
            estadoEmpleadoHasta = false;
        }
        return estadoEmpleadoHasta;
    }

    public void setEstadoEmpleadoHasta(boolean estadoEmpleadoHasta) {
        if (estadoEmpleadoHasta == true) {
            emhasta = "SI";
        } else {
            emhasta = "NO";
        }
        this.estadoEmpleadoHasta = estadoEmpleadoHasta;
    }

    public boolean isEstadoLocalizacion() {
        if (localizacion != null) {
            if (localizacion.equals("SI")) {
                estadoLocalizacion = true;
            } else {
                estadoLocalizacion = false;
            }
        } else {
            estadoLocalizacion = false;
        }
        return estadoLocalizacion;
    }

    public void setEstadoLocalizacion(boolean estadoLocalizacion) {
        if (estadoLocalizacion == true) {
            localizacion = "SI";
        } else {
            localizacion = "NO";
        }
        this.estadoLocalizacion = estadoLocalizacion;
    }

    public boolean isEstadoEstado() {
        if (estado != null) {
            if (estado.equals("SI")) {
                estadoEstado = true;
            } else {
                estadoEstado = false;
            }
        } else {
            estadoEstado = false;
        }
        return estadoEstado;
    }

    public void setEstadoEstado(boolean estadoEstado) {
        if (estadoEstado == true) {
            estado = "SI";
        } else {
            estado = "NO";
        }
        this.estadoEstado = estadoEstado;
    }

    public boolean isEstadoGrupo() {
        if (grupo != null) {
            if (grupo.equals("SI")) {
                estadoGrupo = true;
            } else {
                estadoGrupo = false;
            }
        } else {
            estadoGrupo = false;
        }
        return estadoGrupo;
    }

    public void setEstadoGrupo(boolean estadoGrupo) {
        if (estadoGrupo == true) {
            grupo = "SI";
        } else {
            grupo = "NO";
        }
        this.estadoGrupo = estadoGrupo;
    }

    public boolean isEstadoTercero() {
        if (tercero != null) {
            if (tercero.equals("SI")) {
                estadoTercero = true;
            } else {
                estadoTercero = false;
            }
        } else {
            estadoTercero = false;
        }
        return estadoTercero;
    }

    public void setEstadoTercero(boolean estadoTercero) {
        if (estadoTercero == true) {
            tercero = "SI";
        } else {
            tercero = "NO";
        }
        this.estadoTercero = estadoTercero;
    }

    public boolean isEstadoTrabajador() {
        if (trabajador != null) {
            if (trabajador.equals("SI")) {
                estadoTrabajador = true;
            } else {
                estadoTrabajador = false;
            }
        } else {
            estadoTrabajador = false;
        }
        return estadoTrabajador;
    }

    public void setEstadoTrabajador(boolean estadoTrabajador) {
        if (estadoTrabajador == true) {
            trabajador = "SI";
        } else {
            trabajador = "NO";
        }
        this.estadoTrabajador = estadoTrabajador;
    }

    public boolean isEstadoTipoTrabajador() {
        if (tipotrabajador != null) {
            if (tipotrabajador.equals("SI")) {
                estadoTipoTrabajador = true;
            } else {
                estadoTipoTrabajador = false;
            }
        } else {
            estadoTipoTrabajador = false;
        }
        return estadoTipoTrabajador;
    }

    public void setEstadoTipoTrabajador(boolean estadoTipoTrabajador) {
        if (estadoTipoTrabajador == true) {
            tipotrabajador = "SI";
        } else {
            tipotrabajador = "NO";
        }
        this.estadoTipoTrabajador = estadoTipoTrabajador;
    }

    public boolean isEstadoSolicitud() {
        if (solicitud != null) {
            if (solicitud.equals("SI")) {
                estadoSolicitud = true;
            } else {
                estadoSolicitud = false;
            }
        } else {
            estadoSolicitud = false;
        }
        return estadoSolicitud;
    }

    public void setEstadoSolicitud(boolean estadoSolicitud) {
        if (estadoSolicitud == true) {
            solicitud = "SI";
        } else {
            solicitud = "NO";
        }
        this.estadoSolicitud = estadoSolicitud;
    }

    public boolean isEstadoCiudad() {
        if (ciudad != null) {
            if (ciudad.equals("SI")) {
                estadoCiudad = true;
            } else {
                estadoCiudad = false;
            }
        } else {
            estadoCiudad = false;
        }
        return estadoCiudad;
    }

    public void setEstadoCiudad(boolean estadoCiudad) {
        if (estadoCiudad == true) {
            ciudad = "SI";
        } else {
            ciudad = "NO";
        }
        this.estadoCiudad = estadoCiudad;
    }

    public boolean isEstadoTipoTelefono() {
        if (tipotelefono != null) {
            if (tipotelefono.equals("SI")) {
                estadoTipoTelefono = true;
            } else {
                estadoTipoTelefono = false;
            }
        } else {
            estadoTipoTelefono = false;
        }
        return estadoTipoTelefono;
    }

    public void setEstadoTipoTelefono(boolean estadoTipoTelefono) {
        if (estadoTipoTelefono == true) {
            tipotelefono = "SI";
        } else {
            tipotelefono = "NO";
        }
        this.estadoTipoTelefono = estadoTipoTelefono;
    }

    public boolean isEstadoEstadoCivil() {
        if (estadocivil != null) {
            if (estadocivil.equals("SI")) {
                estadoEstadoCivil = true;
            } else {
                estadoEstadoCivil = false;
            }
        } else {
            estadoEstadoCivil = false;
        }
        return estadoEstadoCivil;
    }

    public void setEstadoEstadoCivil(boolean estadoEstadoCivil) {
        if (estadoEstadoCivil == true) {
            estadocivil = "SI";
        } else {
            estadocivil = "NO";
        }
        this.estadoEstadoCivil = estadoEstadoCivil;
    }

    public boolean isEstadoDeporte() {
        if (deporte != null) {
            if (deporte.equals("SI")) {
                estadoDeporte = true;
            } else {
                estadoDeporte = false;
            }
        } else {
            estadoDeporte = false;
        }
        return estadoDeporte;
    }

    public void setEstadoDeporte(boolean estadoDeporte) {
        if (estadoDeporte == true) {
            deporte = "SI";
        } else {
            deporte = "NO";
        }
        this.estadoDeporte = estadoDeporte;
    }

    public boolean isEstadoIdioma() {
        if (idioma != null) {
            if (idioma.equals("SI")) {
                estadoIdioma = true;
            } else {
                estadoIdioma = false;
            }
        } else {
            estadoIdioma = false;
        }
        return estadoIdioma;
    }

    public void setEstadoIdioma(boolean estadoIdioma) {
        if (estadoIdioma == true) {
            idioma = "SI";
        } else {
            idioma = "NO";
        }
        this.estadoIdioma = estadoIdioma;
    }

    public boolean isEstadoAficion() {
        if (aficion != null) {
            if (aficion.equals("SI")) {
                estadoAficion = true;
            } else {
                estadoAficion = false;
            }
        } else {
            estadoAficion = false;
        }
        return estadoAficion;
    }

    public void setEstadoAficion(boolean estadoAficion) {
        if (estadoAficion == true) {
            aficion = "SI";
        } else {
            aficion = "NO";
        }
        this.estadoAficion = estadoAficion;
    }

    public boolean isEstadoJefeDivision() {
        if (jefedivision != null) {
            if (jefedivision.equals("SI")) {
                estadoJefeDivision = true;
            } else {
                estadoJefeDivision = false;
            }
        } else {
            estadoJefeDivision = false;
        }
        return estadoJefeDivision;
    }

    public void setEstadoJefeDivision(boolean estadoJefeDivision) {
        if (estadoJefeDivision == true) {
            jefedivision = "SI";
        } else {
            jefedivision = "NO";
        }
        this.estadoJefeDivision = estadoJefeDivision;
    }

    public boolean isEstadoRodamiento() {
        if (rodamiento != null) {
            if (rodamiento.equals("SI")) {
                estadoRodamiento = true;
            } else {
                estadoRodamiento = false;
            }
        } else {
            estadoRodamiento = false;
        }
        return estadoRodamiento;
    }

    public void setEstadoRodamiento(boolean estadoRodamiento) {
        if (estadoRodamiento == true) {
            rodamiento = "SI";
        } else {
            rodamiento = "NO";
        }
        this.estadoRodamiento = estadoRodamiento;
    }

    public boolean isEstadoEnvioMasivo() {
        if (enviomasivo != null) {
            if (enviomasivo.equals("S")) {
                estadoEnvioMasivo = true;
            } else {
                estadoEnvioMasivo = false;
            }
        } else {
            estadoEnvioMasivo = false;
        }
        return estadoEnvioMasivo;
    }

    public void setEstadoEnvioMasivo(boolean estadoEnvioMasivo) {
        if (estadoEnvioMasivo == true) {
            enviomasivo = "S";
        } else {
            enviomasivo = "N";
        }
        this.estadoEnvioMasivo = estadoEnvioMasivo;
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
        if (!(object instanceof Inforeportes)) {
            return false;
        }
        Inforeportes other = (Inforeportes) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Inforeportes[ secuencia=" + secuencia + " ]";
    }
}

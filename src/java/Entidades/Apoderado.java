/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidades;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author kevin
 */
@Entity
@Table(name = "apoderado")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Apoderado.findAll", query = "SELECT a FROM Apoderado a")
    , @NamedQuery(name = "Apoderado.findById", query = "SELECT a FROM Apoderado a WHERE a.id = :id")
    , @NamedQuery(name = "Apoderado.findByNombre", query = "SELECT a FROM Apoderado a WHERE a.nombre = :nombre")
    , @NamedQuery(name = "Apoderado.findByApPaterno", query = "SELECT a FROM Apoderado a WHERE a.apPaterno = :apPaterno")
    , @NamedQuery(name = "Apoderado.findByApMaterno", query = "SELECT a FROM Apoderado a WHERE a.apMaterno = :apMaterno")
    , @NamedQuery(name = "Apoderado.findByDni", query = "SELECT a FROM Apoderado a WHERE a.dni = :dni")
    , @NamedQuery(name = "Apoderado.findByFechaNacimiento", query = "SELECT a FROM Apoderado a WHERE a.fechaNacimiento = :fechaNacimiento")
    , @NamedQuery(name = "Apoderado.findByDireccion", query = "SELECT a FROM Apoderado a WHERE a.direccion = :direccion")
    , @NamedQuery(name = "Apoderado.findByTelefono", query = "SELECT a FROM Apoderado a WHERE a.telefono = :telefono")
    , @NamedQuery(name = "Apoderado.findByCorreo", query = "SELECT a FROM Apoderado a WHERE a.correo = :correo")
    , @NamedQuery(name = "Apoderado.findByUsuario", query = "SELECT a FROM Apoderado a WHERE a.usuario = :usuario")
    , @NamedQuery(name = "Apoderado.findByContrase\u00f1a", query = "SELECT a FROM Apoderado a WHERE a.contrase\u00f1a = :contrase\u00f1a")})
public class Apoderado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "ap_paterno")
    private String apPaterno;
    @Basic(optional = false)
    @Column(name = "ap_materno")
    private String apMaterno;
    @Basic(optional = false)
    @Column(name = "dni")
    private String dni;
    @Basic(optional = false)
    @Column(name = "fecha_nacimiento")
    private String fechaNacimiento;
    @Basic(optional = false)
    @Column(name = "direccion")
    private String direccion;
    @Basic(optional = false)
    @Column(name = "telefono")
    private String telefono;
    @Column(name = "correo")
    private String correo;
    @Basic(optional = false)
    @Column(name = "usuario")
    private String usuario;
    @Basic(optional = false)
    @Column(name = "contrase\u00f1a")
    private String contraseña;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idapoderado")
    private List<Alumnoapoderado> alumnoapoderadoList;

    public Apoderado() {
    }

    public Apoderado(Integer id) {
        this.id = id;
    }

    public Apoderado(Integer id, String nombre, String apPaterno, String apMaterno, String dni, String fechaNacimiento, String direccion, String telefono, String usuario, String contraseña) {
        this.id = id;
        this.nombre = nombre;
        this.apPaterno = apPaterno;
        this.apMaterno = apMaterno;
        this.dni = dni;
        this.fechaNacimiento = fechaNacimiento;
        this.direccion = direccion;
        this.telefono = telefono;
        this.usuario = usuario;
        this.contraseña = contraseña;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApPaterno() {
        return apPaterno;
    }

    public void setApPaterno(String apPaterno) {
        this.apPaterno = apPaterno;
    }

    public String getApMaterno() {
        return apMaterno;
    }

    public void setApMaterno(String apMaterno) {
        this.apMaterno = apMaterno;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    @XmlTransient
    public List<Alumnoapoderado> getAlumnoapoderadoList() {
        return alumnoapoderadoList;
    }

    public void setAlumnoapoderadoList(List<Alumnoapoderado> alumnoapoderadoList) {
        this.alumnoapoderadoList = alumnoapoderadoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Apoderado)) {
            return false;
        }
        Apoderado other = (Apoderado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entidades.Apoderado[ id=" + id + " ]";
    }
    
}

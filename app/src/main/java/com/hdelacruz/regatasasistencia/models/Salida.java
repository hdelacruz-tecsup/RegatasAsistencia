package com.hdelacruz.regatasasistencia.models;

public class Salida {

    private Long idSal;

    private String codigo_s;

    private String dni_s;

    private String fecha_s;

    private String hora_s;

    private String razon_s;

    private String imagen;

    public Long getIdSal() {
        return idSal;
    }

    public void setIdSal(Long idSal) {
        this.idSal = idSal;
    }

    public String getCodigo_s() {
        return codigo_s;
    }

    public void setCodigo_s(String codigo_s) {
        this.codigo_s = codigo_s;
    }

    public String getDni_s() {
        return dni_s;
    }

    public void setDni_s(String dni_s) {
        this.dni_s = dni_s;
    }

    public String getFecha_s() {
        return fecha_s;
    }

    public void setFecha_s(String fecha_s) {
        this.fecha_s = fecha_s;
    }

    public String getHora_s() {
        return hora_s;
    }

    public void setHora_s(String hora_s) {
        this.hora_s = hora_s;
    }

    public String getRazon_s() {
        return razon_s;
    }

    public void setRazon_s(String razon_s) {
        this.razon_s = razon_s;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    @Override
    public String toString() {
        return "Salida{" +
                "idSal=" + idSal +
                ", codigo_s='" + codigo_s + '\'' +
                ", dni_s=" + dni_s +
                ", fecha_s='" + fecha_s + '\'' +
                ", hora_s='" + hora_s + '\'' +
                ", razon_s='" + razon_s + '\'' +
                ", imagen='" + imagen + '\'' +
                '}';
    }
}

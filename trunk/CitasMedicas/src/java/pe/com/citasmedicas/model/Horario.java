package pe.com.citasmedicas.model;

import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 *
 * @author dew - Grupo 04
 */
public class Horario {

    private Integer horarioId;
    private Date fechaInicio;
    private Date fechaFin;
    private Medico medico;
    private Especialidad especialidad;
    private Cita cita;

    /**
     * @return the horarioId
     */
    public Integer getHorarioId() {
        return horarioId;
    }

    /**
     * @param horarioId the horarioId to set
     */
    public void setHorarioId(Integer horarioId) {
        this.horarioId = horarioId;
    }

    /**
     * @return the fechaInicio
     */
    public Date getFechaInicio() {
        return fechaInicio;
    }

    /**
     * @param fechaInicio the fechaInicio to set
     */
    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * @return the fechaFin
     */
    public Date getFechaFin() {
        return fechaFin;
    }

    /**
     * @param fechaFin the fechaFin to set
     */
    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    /**
     * @return the medico
     */
    public Medico getMedico() {
        return medico;
    }

    /**
     * @param medico the medico to set
     */
    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    /**
     * @return the especialidad
     */
    public Especialidad getEspecialidad() {
        return especialidad;
    }

    /**
     * @param especialidad the especialidad to set
     */
    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    /**
     * @return the cita
     */
    public Cita getCita() {
        return cita;
    }

    /**
     * @param cita the cita to set
     */
    public void setCita(Cita cita) {
        this.cita = cita;
    }

    /**
     * @return la hora de la inicio
     */
    public int getHoraInicio() {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(this.fechaInicio.getTime());
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * @return la hora de término
     */
    public int getHoraFin() {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(this.fechaFin.getTime());
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * @return el minuto de la hora de inicio
     */
    public int getMinutoInicio() {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(this.fechaInicio.getTime());
        return cal.get(Calendar.MINUTE);
    }

    /**
     * @return el minuto de la hora de término
     */
    public int getMinutoFin() {
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(this.fechaFin.getTime());
        return cal.get(Calendar.MINUTE);
    }

    /**
     * @return rango de hora
     */
    public String getRango() {
        Formatter formatter = new Formatter(Locale.getDefault());
        formatter.format("%1$tH:%1$tM - %2$tH:%2$tM", getFechaInicio(), getFechaFin());
        return formatter.toString();
    }

    @Override
    public String toString() {
        String cadena = "horarioId: " + getHorarioId() + "; " +
                        " especialidad: " + getEspecialidad().getNombre() + "; " +
                        " medico: " + getMedico().getNombreCompleto() + "; " +
                        " fechaInicio: " + getFechaInicio() + "; " +
                        " fechaFin: " + getFechaFin();
        return cadena;
    }
}

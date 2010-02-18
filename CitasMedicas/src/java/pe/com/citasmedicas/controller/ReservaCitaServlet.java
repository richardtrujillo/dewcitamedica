package pe.com.citasmedicas.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import pe.com.citasmedicas.model.Cita;
import pe.com.citasmedicas.model.Especialidad;
import pe.com.citasmedicas.model.Horario;
import pe.com.citasmedicas.model.Medico;
import pe.com.citasmedicas.model.Paciente;
import pe.com.citasmedicas.model.Persona;
import pe.com.citasmedicas.model.Usuario;
import pe.com.citasmedicas.service.CitaService;
import pe.com.citasmedicas.service.EspecialidadService;
import pe.com.citasmedicas.service.HorarioService;
import pe.com.citasmedicas.service.MedicoService;

/**
 *
 * @author rSaenz
 */
public class ReservaCitaServlet extends HttpServlet {

    //
    HttpSession sesion = null;
    //
    EspecialidadService especialidadService = null;
    MedicoService medicoService = null;
    HorarioService horarioService = null;
    CitaService citaService = null;
    //
    List<Especialidad> especialidades = null;
    List<Medico> medicos = null;
    Integer especialidadId = null;
    Integer medicoId = null;
    //
    List<Horario> horarioLunes = null;
    List<Horario> horarioMartes = null;
    List<Horario> horarioMiercoles = null;
    List<Horario> horarioJueves = null;
    List<Horario> horarioViernes = null;
    List<Horario> horarioSabado = null;
    List<Horario> horarioDomingo = null;
    //
    List<String> citasPendientes = null;
    String filtro = "";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doIt(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doIt(request, response);
    }

    private void doIt(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Se verifica la sesión
        sesion = request.getSession();

        if (sesion.getAttribute("usuario") == null) {
            System.out.println("la sesión ha caducado");
            response.sendRedirect(request.getContextPath() + "/loguin.jsp");
            return;
        }
        //
        especialidadService = new EspecialidadService();
        medicoService = new MedicoService();
        horarioService = new HorarioService();
        citaService = new CitaService();

        //
        String accion = request.getParameter("__ACTION");
        if (accion == null || accion.equalsIgnoreCase("")) {
            accion = "iniciar";
        }

        if (accion.equals("iniciar")) {
            iniciar(request, response);
        } else if (accion.equals("cboEspecialidad_onchange")) {
            cargaMedico(request, response);
        } else if (accion.equals("btnVerHorario_onclick")) {
            cargaHorario(request, response);
        } else if (accion.equals("reservar")) {
        }
        
        sesion.setAttribute("especialidades", especialidades);
        sesion.setAttribute("especialidadId", especialidadId);
        sesion.setAttribute("medicos", medicos);
        sesion.setAttribute("medicoId", medicoId);

        response.sendRedirect(request.getContextPath() + "/prc/reservar_cita.jsp");
        //request.getRequestDispatcher("prc/reservar_cita.jsp").forward(request, response);
    }

    private void iniciar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Se recuperan todas las especialidades
        especialidades = especialidadService.getEspecialidades();
        if (especialidades == null) {
            especialidades = new ArrayList<Especialidad>();
        }
        if (especialidades.size() > 0) {
            especialidadId = especialidades.get(0).getEspecialidadId();
        }
        if (especialidadId != null) {
            // Se recuperan todos los médicos de la especialidad seleccionada
            medicos = medicoService.getMedicosPorEspecialidad(especialidades.get(0));
            if (medicos == null) {
                medicos = new ArrayList<Medico>();
            }
            if (medicos.size() > 0) {
                medicoId = medicos.get(0).getPersonaId();
            }
        }
        cargarCitasPendientes();
        sesion.setAttribute("citasPendientes", citasPendientes);
    }

    private void cargaMedico(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        especialidades = (List<Especialidad>) sesion.getAttribute("especialidades");
        especialidadId = Integer.parseInt(request.getParameter("cboEspecialidad"));
        // Se recuperan todos los médicos de la especialidad seleccionada
        Especialidad especialidad = especialidadService.getEspecialidadPorId(especialidadId);
        if (especialidad != null) {
            medicos = medicoService.getMedicosPorEspecialidad(especialidad);
            if (medicos == null) {
                medicos = new ArrayList<Medico>();
            }
            if (medicos.size() > 0) {
                medicoId = medicos.get(0).getPersonaId();
            }
        }
    }

    private void cargaHorario(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        especialidadId = Integer.parseInt(request.getParameter("cboEspecialidad"));
        medicoId = Integer.parseInt(request.getParameter("cboMedico"));

        Especialidad especialidad = especialidadService.getEspecialidadPorId(especialidadId);
        Medico medico = medicoService.getMedicoPorId(medicoId);

        Calendar cal = new GregorianCalendar();
        String fecha = request.getParameter("txtSemana"); // obtener fecha desde el jsp
        System.out.println(fecha.substring(6));
        System.out.println(fecha.substring(3,5));
        System.out.println(fecha.substring(0,2));
        cal.set(Calendar.YEAR, Integer.parseInt(fecha.substring(6)));
        cal.set(Calendar.MONTH, Integer.parseInt(fecha.substring(3,5))-1);
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(fecha.substring(0,2)));

        if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            cal.add(Calendar.DATE, -1 * (cal.get(Calendar.DAY_OF_WEEK) - 2));
        } else {
            cal.add(Calendar.DATE, -6);
        }

        horarioLunes = horarioService.getHorariosPorEspecMedicoFecha(especialidad, medico, cal.getTime());
        cal.add(Calendar.DATE, 1);
        horarioMartes = horarioService.getHorariosPorEspecMedicoFecha(especialidad, medico, cal.getTime());
        cal.add(Calendar.DATE, 1);
        horarioMiercoles = horarioService.getHorariosPorEspecMedicoFecha(especialidad, medico, cal.getTime());
        cal.add(Calendar.DATE, 1);
        horarioJueves = horarioService.getHorariosPorEspecMedicoFecha(especialidad, medico, cal.getTime());
        cal.add(Calendar.DATE, 1);
        horarioViernes = horarioService.getHorariosPorEspecMedicoFecha(especialidad, medico, cal.getTime());
        cal.add(Calendar.DATE, 1);
        horarioSabado = horarioService.getHorariosPorEspecMedicoFecha(especialidad, medico, cal.getTime());
        cal.add(Calendar.DATE, 1);
        horarioDomingo = horarioService.getHorariosPorEspecMedicoFecha(especialidad, medico, cal.getTime());

        String filtro = "Filtro: Especialidad = " + especialidad.getNombre() +
                ", Medico = " + medico.getNombreCompleto() +
                ", Semana = ";
        sesion.setAttribute("horarioAtencion", horarioService.getHorarioAtencion());
        sesion.setAttribute("horarioLunes", horarioLunes);
        sesion.setAttribute("horarioMartes", horarioMartes);
        sesion.setAttribute("horarioMiercoles", horarioMiercoles);
        sesion.setAttribute("horarioJueves", horarioJueves);
        sesion.setAttribute("horarioViernes", horarioViernes);
        sesion.setAttribute("horarioSabado", horarioSabado);
        sesion.setAttribute("horarioDomingo", horarioDomingo);
        
        sesion.setAttribute("filtro", filtro);
    }

    private void cargarCitasPendientes(){
        citasPendientes = new ArrayList<String>();
        Usuario usuario = (Usuario)sesion.getAttribute("usuario");
        Persona paciente = null;
        if(usuario != null){
            paciente = usuario.getPersona();
        }
        if(paciente != null){
            List<Cita> citas = citaService.getCitasPendientes(paciente);
            for(Cita cita : citas){
                String citaPendiente = "";
                Formatter formatter = new Formatter(Locale.getDefault());
                formatter.format("%1$tm %1$te,%1$tY", cita.getHorario().getFechaInicio());
                citaPendiente = formatter.toString() + " : " + cita.getHorario().getEspecialidad().getNombre() + " - "
                        + cita.getHorario().getMedico().getNombreCompleto();
                citasPendientes.add(citaPendiente);
            }
        }
    }
}

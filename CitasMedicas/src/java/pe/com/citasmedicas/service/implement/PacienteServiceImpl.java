package pe.com.citasmedicas.service.implement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.com.citasmedicas.dao.PacienteDao;
import pe.com.citasmedicas.service.PacienteService;
import pe.com.citasmedicas.model.Paciente;

/**
 * Esta clase contiene los métodos de servicio implementados
 * @author dew - Grupo 04
 */
@Service
public class PacienteServiceImpl implements PacienteService {

    private PacienteDao pacienteDao;

    /**
     * Obtiene un Paciente por su Id
     * @return Paciente
     */
    @Override
    public Paciente getPacientePorId(Integer pacienteId) {
        return pacienteDao.getPacientePorId(pacienteId);
    }

    /**
     * @param pacienteDao the pacienteDao to set
     */
    @Autowired
    public void setPacienteDao(PacienteDao pacienteDao) {
        this.pacienteDao = pacienteDao;
    }
}

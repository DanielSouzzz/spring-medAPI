package app.springmedapi.service;

import app.springmedapi.entity.Medico;
import app.springmedapi.entity.dto.ListagemMedicoDTO;
import app.springmedapi.entity.dto.MedicoDTO;
import app.springmedapi.mapper.MedicoMapper;
import app.springmedapi.repository.MedicoRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MedicoService {
    private final MedicoRepository medicoRepository;
    private final MedicoMapper medicoMapper;

    public MedicoService(MedicoRepository medicoRepository, MedicoMapper medicoMapper) {
        this.medicoRepository = medicoRepository;
        this.medicoMapper = medicoMapper;
    }

    @Transactional
    public MedicoDTO createDoctor(MedicoDTO medicoDTO){
    Medico medico = medicoMapper.toMedico(medicoDTO);
    medico = medicoRepository.save(medico);

    return medicoMapper.toMedicoDTO(medico);
    }

    public Page<ListagemMedicoDTO> listarMedicos(Pageable pageable) {
        Page<Medico> medicos = medicoRepository.findAll(pageable);
        return medicos.map(medicoMapper::toListagemMedicoDTO);
    }
}
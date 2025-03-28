package app.springmedapi.service;

import app.springmedapi.entity.Endereco;
import app.springmedapi.entity.Medico;
import app.springmedapi.entity.medicoDTO.AtualizarMeditoDTO;
import app.springmedapi.entity.medicoDTO.DadosDetalhamentoMedicoDTO;
import app.springmedapi.entity.medicoDTO.ListarMedicoDTO;
import app.springmedapi.entity.medicoDTO.CadastrarMedicoDTO;
import app.springmedapi.mapper.EnderecoMapper;
import app.springmedapi.mapper.MedicoMapper;
import app.springmedapi.repository.MedicoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MedicoService {
    private final MedicoRepository medicoRepository;
    private final MedicoMapper medicoMapper;
    private final EnderecoMapper enderecoMapper;

    public MedicoService(MedicoRepository medicoRepository, MedicoMapper medicoMapper, EnderecoMapper enderecoMapper) {
        this.medicoRepository = medicoRepository;
        this.medicoMapper = medicoMapper;
        this.enderecoMapper = enderecoMapper;
    }

    @Transactional
    public DadosDetalhamentoMedicoDTO createDoctor(CadastrarMedicoDTO medicoDTO){
        Medico medico = medicoMapper.toMedico(medicoDTO);
        medico = medicoRepository.save(medico);

        return medicoMapper.toDetalhamentoMedicoDTO(medico);
    }

    public Page<ListarMedicoDTO> listarMedicos(Pageable pageable) {
        Page<Medico> medicos = medicoRepository.findAllByAtivoTrue(pageable);
        return medicos.map(medicoMapper::toListagemMedicoDTO);
    }
    @Transactional
    public DadosDetalhamentoMedicoDTO atualizarMedico(AtualizarMeditoDTO atualizaMeditoDTO){
        if (atualizaMeditoDTO.id() == null){
            throw new IllegalArgumentException("Id do médico não pode ser nulo");
        }
        Medico medico = medicoRepository.findById(atualizaMeditoDTO.id())
                .orElseThrow(() -> new IllegalArgumentException("Médico não encontrado"));

        medico.setNome(atualizaMeditoDTO.nome());
        medico.setTelefone(atualizaMeditoDTO.telefone());

        Endereco enderecoExistente = medico.getEndereco();
        enderecoExistente.setLogradouro(atualizaMeditoDTO.endereco().logradouro());
        enderecoExistente.setNumero(atualizaMeditoDTO.endereco().numero());
        enderecoExistente.setComplemento(atualizaMeditoDTO.endereco().complemento());
        enderecoExistente.setBairro(atualizaMeditoDTO.endereco().bairro());
        enderecoExistente.setCidade(atualizaMeditoDTO.endereco().cidade());
        enderecoExistente.setUf(atualizaMeditoDTO.endereco().uf());
        enderecoExistente.setCep(atualizaMeditoDTO.endereco().cep());
        medico = medicoRepository.save(medico);
        return medicoMapper.toDetalhamentoMedicoDTO(medico);
    }

    @Transactional
    public void deletarMedico(Long id) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Médico não encontrado"));
        medico.setAtivo(false);
        medicoRepository.save(medico);
    }

    public DadosDetalhamentoMedicoDTO detalharMedico(Long id) {
        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Médico não encontrado com ID: " + id));
        return medicoMapper.toDetalhamentoMedicoDTO(medico);
    }
}


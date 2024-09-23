package br.insper.curso.service;

import br.insper.curso.dto.CriarCursoDTO;
import br.insper.curso.dto.ReturnUsuarioDTO;
import br.insper.curso.model.Curso;
import br.insper.curso.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private RestTemplate restTemplate;

    public Curso criarCurso(CriarCursoDTO dto) {
        ReturnUsuarioDTO professor = restTemplate.getForObject("http://184.72.80.215:8080/usuario/" + dto.getCpfProfessor(), ReturnUsuarioDTO.class);
        if (professor == null) {
            throw new RuntimeException("Professor não encontrado");
        }

        Curso curso = new Curso();
        curso.setNome(dto.getNome());
        curso.setDescricao(dto.getDescricao());
        curso.setMaxAlunos(dto.getMaxAlunos());
        curso.setCpfProfessor(dto.getCpfProfessor());
        return cursoRepository.save(curso);
    }

    public List<Curso> listarCursos(String nomeFiltro) {
        if (nomeFiltro == null || nomeFiltro.isEmpty()) {
            return cursoRepository.findAll();
        } else {
            return cursoRepository.findAll().stream()
                    .filter(curso -> curso.getNome().toLowerCase().contains(nomeFiltro.toLowerCase()))
                    .collect(Collectors.toList());
        }
    }

    public Curso adicionarAluno(Long cursoId, String cpfAluno) {  // Retorna o curso atualizado
        Optional<Curso> optionalCurso = cursoRepository.findById(cursoId);
        if (optionalCurso.isPresent()) {
            Curso curso = optionalCurso.get();
            if (curso.getAlunos().size() < curso.getMaxAlunos()) {
                ReturnUsuarioDTO usuario = restTemplate.getForObject("http://184.72.80.215:8080/usuario/" + cpfAluno, ReturnUsuarioDTO.class);
                if (usuario != null) {
                    curso.getAlunos().add(cpfAluno);
                    return cursoRepository.save(curso); // Retorna o curso atualizado
                } else {
                    throw new RuntimeException("Aluno não encontrado");
                }
            } else {
                throw new RuntimeException("Número máximo de alunos atingido");
            }
        } else {
            throw new RuntimeException("Curso não encontrado");
        }
    }
}

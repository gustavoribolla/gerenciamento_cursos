package br.insper.curso.service;

import br.insper.curso.dto.CriarCursoDTO;
import br.insper.curso.dto.ReturnUsuarioDTO;
import br.insper.curso.model.Curso;
import br.insper.curso.repository.CursoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CursoServiceTests {

    @InjectMocks
    private CursoService cursoService;

    @Mock
    private CursoRepository cursoRepository;

    @Mock
    private RestTemplate restTemplate;

    private Curso curso;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        curso = new Curso();
        curso.setId(1L);
        curso.setNome("Curso de Teste");
        curso.setDescricao("Descrição do curso de teste");
        curso.setMaxAlunos(2);
        curso.setAlunos(new ArrayList<>());
    }

    @Test
    void testCriarCurso() {
        CriarCursoDTO dto = new CriarCursoDTO();
        dto.setNome("Curso de Teste");
        dto.setDescricao("Descrição do curso de teste");
        dto.setMaxAlunos(2);
        dto.setCpfProfessor("12345678900");

        ReturnUsuarioDTO professor = new ReturnUsuarioDTO();
        professor.setCpf("12345678900");
        professor.setNome("Professor Teste");

        when(restTemplate.getForObject(any(String.class), eq(ReturnUsuarioDTO.class))).thenReturn(professor);
        when(cursoRepository.save(any(Curso.class))).thenReturn(curso);

        Curso cursoCriado = cursoService.criarCurso(dto);

        assertNotNull(cursoCriado);
        assertEquals("Curso de Teste", cursoCriado.getNome());
    }

    @Test
    void testListarCursos() {
        List<Curso> cursos = new ArrayList<>();
        cursos.add(curso);

        when(cursoRepository.findAll()).thenReturn(cursos);

        List<Curso> cursosListados = cursoService.listarCursos(null);

        assertEquals(1, cursosListados.size());
        assertEquals("Curso de Teste", cursosListados.get(0).getNome());
    }

    @Test
    void testAdicionarAluno() {
        ReturnUsuarioDTO aluno = new ReturnUsuarioDTO();
        aluno.setCpf("12345678901");
        aluno.setNome("Aluno Teste");

        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(restTemplate.getForObject(any(String.class), eq(ReturnUsuarioDTO.class))).thenReturn(aluno);
        when(cursoRepository.save(any(Curso.class))).thenReturn(curso);

        Curso cursoAtualizado = cursoService.adicionarAluno(1L, "12345678901");

        assertEquals(1, cursoAtualizado.getAlunos().size());
        assertTrue(cursoAtualizado.getAlunos().contains("12345678901"));
    }

    @Test
    void testAdicionarAlunoCursoCheio() {
        curso.setAlunos(List.of("12345678901", "12345678902")); // Adicionando dois alunos
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cursoService.adicionarAluno(1L, "12345678903");
        });

        assertEquals("Número máximo de alunos atingido", exception.getMessage());
    }

    @Test
    void testAdicionarAlunoCursoNaoEncontrado() {
        when(cursoRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cursoService.adicionarAluno(1L, "12345678901");
        });

        assertEquals("Curso não encontrado", exception.getMessage());
    }

    @Test
    void testAdicionarAlunoUsuarioNaoEncontrado() {
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(restTemplate.getForObject(any(String.class), eq(ReturnUsuarioDTO.class))).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cursoService.adicionarAluno(1L, "12345678901");
        });

        assertEquals("Aluno não encontrado", exception.getMessage());
    }
}

package br.insper.curso.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriarCursoDTO {
    @NotEmpty
    private String nome;
    @NotEmpty
    private String descricao;
    private int maxAlunos;
    @NotEmpty
    private String cpfProfessor;
}

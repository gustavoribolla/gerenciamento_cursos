package br.insper.curso.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Alterado para Long

    private String nome;               // Nome do curso
    private String descricao;          // Descrição do curso
    private int maxAlunos;             // Número máximo de alunos

    @ElementCollection
    private List<String> alunos = new ArrayList<>(); // Lista de CPFs dos alunos

    private String cpfProfessor;        // CPF do professor
}

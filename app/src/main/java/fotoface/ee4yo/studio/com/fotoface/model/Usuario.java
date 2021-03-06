package fotoface.ee4yo.studio.com.fotoface.model;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import fotoface.ee4yo.studio.com.fotoface.config.ConfiguracaoFirebase;

public class Usuario {
    private String id;
    private String nome;
    private String sobrenome;
    private String matricula;
    private String curso;
    private String instituicao;
    private String email;
    private String senha;
    private String foto;

    private DatabaseReference databaseFireB;

    public Usuario() {
    }

    //Slava os dados do usuário no banco de dados
    public void salvar(){
        databaseFireB = ConfiguracaoFirebase.getDatabaseFireB();
        //Salva criando um nó usuários com a Id gerada para diferenciar os usuarios
        databaseFireB.child("usuarios").child(getId()).setValue(this);
    }

    public String getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(String instituicao) {
        this.instituicao = instituicao;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}

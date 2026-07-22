# 📚 StudyMatch - Gestão de Monitorias e Grupos de Estudo

**Projeto da disciplina de Programação Orientada a Objetos**  
**Curso:** Licenciatura em Ciência da Computação (UFPB)  

---

## 📖 Descrição do Projeto
O **StudyMatch** é um mini sistema desenvolvido em Java para o gerenciamento de sessões de monitoria e grupos de estudo. O objetivo do software é permitir o cadastro, controle e pesquisa dessas sessões, facilitando a organização acadêmica. 

O projeto foi construído aplicando os principais conceitos de Programação Orientada a Objetos, incluindo encapsulamento, tratamento de exceções, uso de coleções (`Map`), operações funcionais (`Streams`), persistência de dados em arquivos e testes automatizados. A arquitetura da aplicação isola a regra de negócios da interface gráfica através do padrão de projeto **Facade**.

---

## 🛠️ Tecnologias e Ferramentas Utilizadas
* **Linguagem:** Java (JDK 17+)
* **Interface Gráfica:** JavaFX
* **Testes Unitários:** JUnit 5
* **Armazenamento:** Serialização de Objetos (I/O) em arquivo `.dat`
* **Controle de Versão:** Git e GitHub

---

## ⚙️ Arquitetura e Estrutura do Código

O sistema foi dividido de forma a garantir alta coesão e baixo acoplamento:

* `br.ufpb.studymatch.entidades`: Classes de domínio (`SessaoEstudo`, `Aluno`), focadas unicamente em representar os dados. Implementam a interface `Serializable`.
* `br.ufpb.studymatch.negocio`: Coração do sistema. Contém a interface `ISistemaMonitoria` (Facade) e a classe concreta `AgendaDeMonitorias`, que gerencia os dados através de um `Map<String, SessaoEstudo>`.
* `br.ufpb.studymatch.persistencia`: Contém a classe `GravadorDeDados`, cuja única responsabilidade é realizar a leitura e gravação do estado do sistema em arquivo, lançando `IOException`.
* `br.ufpb.studymatch.excecoes`: Exceções customizadas da aplicação.
* `br.ufpb.studymatch.visao`: Controladores e telas desenvolvidas em JavaFX.

---

## ✨ Funcionalidades Implementadas (Interface do Sistema)

A interface `ISistemaMonitoria` expõe as seguintes operações:

1. **`cadastrarSessao(SessaoEstudo sessao)`**: Adiciona uma nova sessão ao `Map`. Lança exceção se o ID já existir.
2. **`removerSessao(String id)`**: Remove a sessão correspondente. Lança exceção se não for encontrada.
3. **`pesquisarSessaoPorId(String id)`**: Busca e retorna uma sessão específica.
4. **`atualizarSessao(String id, SessaoEstudo novaSessao)`**: Atualiza os dados de uma sessão existente.
5. **`listarTodasSessoes()`**: Retorna todas as sessões cadastradas.
6. **`listarSessoesPorDisciplina(String disciplina)`**: **(Uso de Streams - `filter`)** Filtra e retorna as sessões de uma matéria específica.
7. **`obterNomesDosMonitores()`**: **(Uso de Streams - `map`)** Retorna uma lista contendo apenas os nomes dos monitores cadastrados.
8. **`cadastrarAlunoEmSessao(String idSessao, Aluno aluno)`**: Inscreve um aluno em uma sessão.
9. **`salvarDados()`**: Persiste o `Map` de sessões em disco através do `GravadorDeDados`.
10. **`recuperarDados()`**: Carrega os dados do arquivo de volta para a memória durante a inicialização.

---

## 🧪 Testes Automatizados
O projeto conta com uma suíte de testes unitários (`AgendaDeMonitoriasTest`) desenvolvida com JUnit. Os testes exercitam a classe principal de negócios, cobrindo ao menos 5 cenários distintos:
* Cadastro de sessão com sucesso.
* Tentativa de cadastro de sessão duplicada (validação de exceção).
* Remoção de sessão existente e tentativa de remoção de ID inexistente.
* Pesquisa com sucesso.
* Listagem utilizando as funções de Stream.

---

## 🚀 Como Executar o Projeto

1. Clone este repositório:
   ```bash
   git clone https://github.com/FabricioOliveira08/studymatch.git

2. Abra o projeto na sua IDE de preferência (IntelliJ IDEA, Eclipse ou VS Code).

3. Certifique-se de que o SDK do JavaFX e o JUnit estão configurados no Build Path ou no pom.xml (caso use Maven).

4. Execute a classe principal localizada no pacote de visão para iniciar a interface gráfica.

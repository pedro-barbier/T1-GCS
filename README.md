# T1-GCS
#### Turma: 30 - 2026/1
#### Disciplina: Gerenciamento de Configuração de Software  
#### Prof. Daniel Callegari, 2026/1
#### Alunos: Guilherme Cariolatto Schmitz, Luiz Felipe Ramalho da Silveira, Pedro Henrique Barbieri, Eduardo Hoffmann Araújo, Eduardo Marcantonio Nascimento, Lucas Mocelin Fioravanti, Levi Sancho Ribeiro Perpetuo, Letícia Bergonci Camargo e Henrique Schmid Rolim
# Implementação do sistema
Sistema de autorização de exames médicos, para prática de aplicação de versionamento git em equipes.
## Como Contribuir

### 1. Configure seu Git (obrigatório antes do primeiro commit)

Certifique-se de que seu nome e e-mail estão configurados corretamente.
O professor avaliará as contribuições pelo histórico do Git, então é essencial
que seus commits estejam identificados com seu nome real e e-mail do GitHub.

```bash
git config --global user.name "Seu Nome Completo"
git config --global user.email "seu-email@github.com"
```

---

### 2. Clone o repositório (só na primeira vez)

```bash
git clone https://github.com/seu-usuario/gcs-autorizacao-exames.git
cd gcs-autorizacao-exames
```

---

### 3. Atualize sua cópia local antes de começar qualquer trabalho

Sempre parta do estado mais recente da `master` para evitar conflitos:

```bash
git checkout master
git pull origin master
```

---

### 4. Crie uma branch para sua contribuição

**Nunca trabalhe diretamente na `master`.**

Use os padrões abaixo conforme o tipo de contribuição:

#### Nova funcionalidade
```bash
git checkout -b feature/nome-da-feature master
```

Exemplos:
```bash
git checkout -b feature/cadastro-autorizacao master
git checkout -b feature/listagem-medico master
git checkout -b feature/marcar-exame-realizado master
git checkout -b feature/listagem-paciente master
git checkout -b feature/cadastro-usuario-admin master
git checkout -b feature/busca-admin master
git checkout -b feature/estatisticas-admin master
git checkout -b feature/selecao-usuario master
```

#### Correção de bug
```bash
git checkout -b hotfix/descricao-do-bug
```

Exemplos:
```bash
git checkout -b hotfix/validacao-data-autorizacao
git checkout -b hotfix/ordenacao-listagem
```

---

### 5. Desenvolva e faça commits frequentes

Faça commits pequenos e descritivos ao longo do desenvolvimento.
Evite commits gigantes com tudo de uma vez.

Padrão de mensagem de commit: "{tipo}: mensagem"

| Tipo     | Quando usar                                      |
|----------|--------------------------------------------------|
| `feat`   | nova funcionalidade                              |
| `fix`    | correção de bug                                  |
| `refactor` | melhoria de código sem mudar comportamento     |
| `docs`   | alterações em documentação                       |
| `test`   | adição ou ajuste de testes                       |
| `chore`  | tarefas de configuração, estrutura               |

Exemplos:

# feat – nova funcionalidade
git commit -m "feat: implementa cadastro de autorizacao de exame"
git commit -m "feat: adiciona listagem de autorizacoes por paciente"

# fix – correção de bug
git commit -m "fix: corrige validacao de data do exame realizado"
git commit -m "fix: ajusta filtro de autorizacoes por tipo de exame"

# refactor – melhoria interna de código
git commit -m "refactor: extrai metodo para ordenar autorizacoes por data"
git commit -m "refactor: simplifica logica de selecao de usuario atual"

# docs – documentação
git commit -m "docs: atualiza README com fluxo de trabalho do git"
git commit -m "docs: adiciona instrucoes de execucao do sistema"

# test – testes
git commit -m "test: adiciona testes para AutorizacaoService"
git commit -m "test: cobre regra de periodo maximo de 30 dias"

# chore – configuração / estrutura
git commit -m "chore: adiciona gitignore para arquivos .class"
git commit -m "chore: cria estrutura inicial de pacotes do projeto"

## Repositório

[https://github.com/seu-usuario/gcs-autorizacao-exames](https://github.com/seu-usuario/gcs-autorizacao-exames)

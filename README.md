# Risk Security Platform

Esse projeto é sobre um programa que permite ao usuário colocar uma atividade desejada para ser calculada o risco dessa atividade e, através de uma resposta de um agente LLM, recebe uma sugestão de como contornar ou melhorar o risco dela.

# Motivações

Para a conclusão desse trabalho, foram colocadas algumas obrigatoriedades para o aprendizado ideal da disciplina cursada. Logo, foram separadas certas funcionalidades do programa entre "utilizará SQL" ou "utilizará NoSQL".

## SQL

O banco SQL que usaremos será o PostgreSQL para todas as situações descritas abaixo.

As partes do programa que serão armazenadas em PostgreSQL são:
- Informações do Usuário: nome, e-mail, senha, atividade (se está ativo ou não), data de criação da conta e o tipo (se é analista de segurança ou gestor)
- Métricas CVSS: o vetor de string (CVSS, AV ou AC) e a vulnerabilidade
- RAG do Agente LLM: guardará os vetores embedding dos textos que serão usados pelo agente

## NoSQL

O banco NoSQL que usaremos será o MongoDB para todas as situações descritas abaixo.

As partes do programa que serão armazenadas em MongoDB são:
- Log de Acesso: data de criação do evento, o tipo de ação, o ip de origem, o recurso e se foi sucesso ou nao
- Histórico das vulnerabilidades/recomendação da IA: a atividade que foi analisada, se foi respondida ou não e a resposta que foi dada pelo agente

## Passo a passo

### 0. (EXTRA) Se você estiver utilizando Windows

**0.1 Instalação do VSCode**

O VSCode será a IDE recomendada para o manejo deste projeto

**0.2 Definição do perfil bash para o terminal do VSCode**

1. Pressione `CTRL + SHIFT + P`
2. Digite `Preferences: Open User Settings (JSON)` e pressione enter
3. Será aberto o arquivo `settings.json`
    1. Cole esse código no `settings.json`:
    ```bash
    "terminal.integrated.profiles.windows": {
        "Git Bash": {
            "path": "C:\\Program Files\\Git\\bin\\bash.exe",
            "source": "Git Bash"
        }
    },
    "terminal.integrated.defaultProfile.windows": "Git Bash"
    ```
4. Dê `CTRL + S` e feche o arquivo
5. Feche e abra o VSCode

### 1. Clonagem do repositório

Clone o repositório através do comando abaixo:

**Windows (com terminal Bash) & Linux.**
```bash
git clone https://github.com/hugoprd/risk-security-platform.git
cd risk-security-platform
git submodule update --init --recursive
```

### 2. Configuração do submódulo

Para configurar um projeto específico, navegue até o seu diretório e siga as instruções contidas no `README.md` daquele projeto.

#### 2.1 Configurando o `security-llm-agent`

O [security-llm-agent](https://github.com/hugoprd/security-llm-agent.git) é o componente responsável pela análise de segurança.

**As instruções detalhadas de instalação e uso do submódulo está contido no [README](https://github.com/hugoprd/security-llm-agent.git) dele.**

## 3. Contribuindo

Para contribuir com este projeto, siga os passos a partir do [CONTRIBUTING](CONTRIBUTING.md)

## 4. Executando o programa localmente

### 4.1. Com VSCode

Se estiver utilizando o VSCode como IDE, há necessidade de fazer os passos a seguir (não testei com outras IDE):

1. Instale o [Apache Maven](https://maven.apache.org/download.cgi)
2. Descompacte a pasta .zip
3. Mova a pasta para C:/ProgramFiles/Apache/Maven/ (se esse diretório não existir pode criar manualmente)
4. No terminal bash do VSCode, digite 
```bash
mvn -v
```
5. Estarei utilizando o DBaaS Neon para o meu Postgre, porém é possível utilizar da forma que desejar
6. No diretório do projeto "src/main/resources/", adicione um arquivo chamado ```application-local.properties``` e cole isso e substitua os valores para o seu caso:
```bash
spring.datasource.url=jdbc:{STRING_URL}
spring.datasource.username={USERNAME}
spring.datasource.password={PASSWORD}
```
7. Rode:
```bash
mvn clean install -DskipTests
mvn clean package -DskipTests
java -jar target/backend-vuln-0.0.1-SNAPSHOT.jar
```

## 5. Recepção de alteração nos submódulos

**Windows (com terminal Bash) & Linux.**
```bash
cd ~/git/risk-security-platform/
git pull --all
git checkout <branchname>
git status
git pull --recurse-submodules
git status
git submodule update --remote
git status
git commit -a -m "Atualiza submódulos"
git push origin <branchname>
```

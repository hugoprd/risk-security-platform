# Risk Security Platform

Esse projeto é sobre um programa que, ao usuário colocar uma atividade desejada, calcula o risco dessa atividade e, através de uma resposta de um agente LLM, recebe uma sugestão de como contornar ou melhorar o risco dela.

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

O `security-llm-agent` é o componente responsável pela análise de segurança.

**As instruções detalhadas de instalação e uso do submódulo está contido no [README](https://github.com/hugoprd/security-llm-agent.git) dele.**

## 3. Contribuindo

Para contribuir com este projeto, siga os passos a partir do [CONTRIBUTING](CONTRIBUTING.md)

## 4. Recepção de alteração nos submódulos

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

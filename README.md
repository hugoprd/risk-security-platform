# Risk Security Platform

Esse projeto é sobre um programa que, ao usuário colocar uma atividade desejada, calcula o risco dessa atividade e, através de uma resposta de um agente LLM, recebe uma sugestão de como contornar ou melhorar o risco dela.

## Passo a passo

### 1. Clonagem do repositório

Clone o repositório através do comando abaixo:

Linux.
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

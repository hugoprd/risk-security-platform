# Desenvolvimento do projeto

Este projeto é compartilhado com 3 desenvolvedores diferentes. Então é importante ter entre si um padrão para evitar conflitos e erros futuros e para garantir a qualidade do código.

## Processo de Pull Request

1. A partir da `develop`, crie uma nova branch, que tenha a ver com o que você irá trabalhar no momento, com um nome descritivo (ex.: `adiciona-analise-de-risco`)
2. Faça suas alterações e garanta que todos os testes estão passando.
3. Faça o commit dentro dessa branch
4. Envie o Pull Request com descrição clara do que foi feito
5.  **(EXTRA) Tente sempre estar atualizado junto com a branch principal**
    1.  Primeiramente, volte para a branch `develop`
    2.  Após isso, dê um `git pull`
    3.  Volte para a branch em que está trabalhando
    4.  Dê `git merge develop`

Para informações mais detalhadas, consulte os guias completos:

* **[Guia de configuração do Ambiente de Dev](./docs/DEV_SETUP.md)**
* **[Teste do projeto](./docs/TESTING.md)**
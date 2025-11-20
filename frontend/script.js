const API_URL = "http://localhost:8080/api/vulnerabilidades";

async function listarVulnerabilidades() {
    const tabela = document.getElementById("tabela-corpo");
    tabela.innerHTML = "<tr><td colspan='6'>Carregando...</td></tr>";

    try{
        const resposta = await fetch(API_URL);
        const lista = await resposta.json();

        tabela.innerHTML = "";

        lista.forEach(vuln => {
            const linha = tabela.insertRow();

            linha.insertCell().innerText = vuln.id_vulnerabilidade;
            linha.insertCell().innerText = vuln.titulo;
            linha.insertCell().innerText = vuln.pontuacao_cvss;
            linha.insertCell().innerText = vuln.status || "Aberto";

            const celulaBotao = linha.insertCell();
            const botao = document.createElement("button");
            botao.innerText = "Consultar IA";
            botao.className = "btn-ia";
            botao.onclick = () => chamarAgenteIA(vuln.id_vulnerabilidade, linha);
            celulaBotao.appendChild(botao);

            linha.insertCell().className = "resposta-ia"; 
        });
    }
    catch(erro){
        console.error(erro);
        tabela.innerHTML = "<tr><td colspan='6' style='color:red'>Erro ao conectar com o Backend Java.</td></tr>";
    }
}

async function chamarAgenteIA(id, linhaHTML){
    const celulaResposta = linhaHTML.cells[5];
    celulaResposta.innerHTML = "<span class='loading'>Consultando Agente LLM...</span>";

    try{
        const resposta = await fetch(`${API_URL}/${id}/recomendacao`, {
            method: "POST"
        });

        if(resposta.ok){
            const textoRecomendacao = await resposta.text(); 
            
            celulaResposta.innerText = textoRecomendacao;
        }
        else{
            celulaResposta.innerText = "Erro ao processar IA.";
        }
    }
    catch(erro){
        console.error(erro);
        celulaResposta.innerText = "Erro de conexão.";
    }
}
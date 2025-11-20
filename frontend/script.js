const BACKEND_URL = "http://localhost:8080/api/vulnerabilidades";

async function carregarVulnerabilidades(){
    const tbody = document.getElementById('tabelaVulnerabilidades').getElementsByTagName('tbody')[0];
    tbody.innerHTML = '<tr><td colspan="7">Carregando...</td></tr>';

    try{
        const response = await fetch(BACKEND_URL);
        
        if(!response.ok){
            throw new Error(`Erro HTTP! Status: ${response.status}`);
        }
        
        const vulnerabilidades = await response.json();
        
        tbody.innerHTML = '';

        vulnerabilidades.forEach(v => {
            const row = tbody.insertRow();
            row.insertCell().textContent = v.id_vulnerabilidade;
            row.insertCell().textContent = v.titulo;
            row.insertCell().textContent = v.sistema_impactado;
            row.insertCell().textContent = v.pontuacao_cvss;
            row.insertCell().textContent = v.criticidade;
            row.insertCell().textContent = v.status;
            
            // botão para chamar a IA
            const cellIA = row.insertCell();
            const btnIA = document.createElement('button');
            btnIA.textContent = 'Gerar Recomendação';
             
            // aqui chama a função pra implementar a IA
            btnIA.onclick = () => gerarRecomendacao(v.id_vulnerabilidade, row); 
            cellIA.appendChild(btnIA);
        });
    }
    catch(error){
        console.error("Erro ao carregar vulnerabilidades:", error);
        tbody.innerHTML = `<tr><td colspan="7" style="color: red;">Erro: ${error.message}</td></tr>`;
    }
}
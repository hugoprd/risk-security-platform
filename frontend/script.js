const API_BASE = "http://localhost:8080"; 

function irParaCadastro(){
    document.getElementById('tela-login').classList.add('oculto');
    document.getElementById('tela-cadastro').classList.remove('oculto');
}

function voltarParaLogin(){
    document.getElementById('tela-cadastro').classList.add('oculto');
    document.getElementById('tela-login').classList.remove('oculto');
}

function logout(){
    location.reload();
}

// ===== LOGIN DE USUÁRIO =====
document.getElementById('formLogin').addEventListener('submit', async (e) => {
    e.preventDefault();
    const email = document.getElementById('loginEmail').value;
    const senha = document.getElementById('loginSenha').value;
    const feedback = document.getElementById('feedbackLogin');

    feedback.textContent = "Entrando...";

    try{
        const response = await fetch(`${API_BASE}/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, senha })
        });

        if(response.ok){
            const usuario = await response.json();
            
            document.getElementById('idUsuarioLogado').value = usuario.id_usuario;
            document.getElementById('msgBemVindo').textContent = `Olá, ${usuario.nome}`;

            document.getElementById('tela-login').classList.add('oculto');
            document.getElementById('painel-principal').classList.remove('oculto');
            
            listarVulnerabilidades();
        }
        else{
            feedback.textContent = "Email ou senha incorretos.";
        }
    }
    catch(error){
        console.error(error);
        feedback.textContent = "Erro de conexão.";
    }
});

// ===== CADASTRO DE USUARIO =====
document.getElementById('formUsuario').addEventListener('submit', async (e) => {
    e.preventDefault();
    const feedback = document.getElementById('feedbackUsuario');
    
    const usuario = {
        nome: document.getElementById('nome').value,
        email: document.getElementById('email').value,
        senha: document.getElementById('senha').value,
        tipo_usuario: document.getElementById('tipo_usuario').value
    };

    try{
        const response = await fetch(`${API_BASE}/usuarios`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(usuario)
        });

        if(response.ok){
            alert("Conta criada com sucesso! Faça login agora.");
            document.getElementById('formUsuario').reset();
            
            voltarParaLogin();
        }
        else{
            feedback.textContent = "Erro ao cadastrar.";
        }
    }
    catch(error){
        console.error(error);
        feedback.textContent = "Erro no servidor.";
    }
});

// ===== CADASTRO DE VULNERABILIDADE =====
document.getElementById('formVulnerabilidade').addEventListener('submit', async (e) => {
    e.preventDefault();
    const feedback = document.getElementById('feedbackVulnerabilidade');
    feedback.textContent = "Enviando...";

    const idUsuario = document.getElementById('idUsuarioLogado').value;

    if(!idUsuario){
        alert("Erro: Usuário não identificado. Faça login novamente.");
        
        return;
    }

    const vulnerabilidade = {
        titulo: document.getElementById('titulo').value,
        descricao: document.getElementById('descricao').value,
        sistema_impactado: document.getElementById('sistema_impactado').value,
        status: document.getElementById('status').value,
        metricasCVSS: {
            vectorString: document.getElementById('vectorString').value
        }
    };

    try{
        const response = await fetch(`${API_BASE}/api/vulnerabilidades/usuario/${idUsuario}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(vulnerabilidade)
        });

        if(response.ok){
            feedback.textContent = "Vulnerabilidade registrada!";
            document.getElementById('formVulnerabilidade').reset();
            listarVulnerabilidades();
        }
        else{
            feedback.textContent = "Erro ao registrar.";
        }
    }
    catch(error){
        console.error(error);
        feedback.textContent = "Erro de conexão.";
    }
});

// ===== LISTAGEM DE VULNERABILIDADES =====
async function listarVulnerabilidades(){
    const tbody = document.getElementById('tabela-corpo');
    tbody.innerHTML = "<tr><td colspan='6'>Carregando...</td></tr>";

    try{
        const response = await fetch(`${API_BASE}/api/vulnerabilidades`);
        const lista = await response.json();
        tbody.innerHTML = "";

        if(lista.length === 0){
            tbody.innerHTML = "<tr><td colspan='6'>Nenhuma vulnerabilidade encontrada.</td></tr>";
            return;
        }

        lista.forEach(v => {
            const row = tbody.insertRow();
            row.insertCell().innerText = v.id_vulnerabilidade;
            row.insertCell().innerText = v.titulo;
            row.insertCell().innerText = v.pontuacao_cvss || "-";
            row.insertCell().innerText = v.status;
            
            const cellIA = row.insertCell();
            const btn = document.createElement("button");
            btn.innerText = "Consultar IA";
            btn.onclick = () => chamarIA(v.id_vulnerabilidade, row);
            cellIA.appendChild(btn);

            row.insertCell().className = "resp-ia";
        });
    }
    catch(error){
        console.error(error);
        tbody.innerHTML = "<tr><td colspan='6'>Erro ao carregar lista.</td></tr>";
    }
}

// ===== CHAMADA IA =====
async function chamarIA(id, row){
    const cell = row.cells[5];
    cell.innerText = "Consultando...";
    
    try{
        const response = await fetch(`${API_BASE}/api/vulnerabilidades/${id}/recomendacao`, { method: 'POST' });
        const text = await response.text();
        cell.innerText = text;
    }
    catch(error){
        cell.innerText = "Erro na IA.";
    }
}
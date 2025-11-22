const API_BASE = "http://localhost:8080";
let vulnerabilidadesCache = []; // apenas um cache pra não precisar chamar o backend toda hora
                                // se ja tiver aqui, n chama o back

// ===== MANEJAMENTO DE TELAS =====
function mostrarTelaCadastroUsuario(){
    document.getElementById('tela-login').style.display = 'none';
    document.getElementById('tela-cadastro-usuario').style.display = 'block';
}

function voltarLogin(){
    document.getElementById('tela-cadastro-usuario').style.display = 'none';
    document.getElementById('tela-login').style.display = 'block';
}

function navegar(viewId){
    document.querySelectorAll('.tela').forEach(t => t.classList.remove('ativa'));
    document.querySelectorAll('.menu-btn').forEach(b => b.classList.remove('active'));
    
    document.getElementById('view-' + viewId).classList.add('ativa');
    
    if(viewId === 'historico') carregarHistorico();
    if(viewId === 'ia-chat') carregarDropdownIA();
}

function logout() {
    location.reload();
}

// ===== LOGIN DE USUARIO =====
document.getElementById('formLogin').addEventListener('submit', async (e) => {
    e.preventDefault();
    const email = document.getElementById('loginEmail').value;
    const senha = document.getElementById('loginSenha').value;
    
    try{
        const res = await fetch(`${API_BASE}/auth/login`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({email, senha})
        });
        
        if(res.ok){
            const user = await res.json();
            document.getElementById('idUsuarioLogado').value = user.id_usuario;
            document.getElementById('user-display').innerText = user.nome;
            
            document.getElementById('tela-login').style.display = 'none';
            document.getElementById('app-principal').style.display = 'flex';
        }
        else{
            document.getElementById('feedbackLogin').innerText = "Credenciais inválidas";
        }
    }
    catch(err){
        console.error(err);
        alert("Erro ao conectar");
    }
});

// ===== CADASTRO DE USUARIO =====
document.getElementById('formUsuario').addEventListener('submit', async (e) => {
    e.preventDefault();
    const usuario = {
        nome: document.getElementById('nome').value,
        email: document.getElementById('email').value,
        senha: document.getElementById('senha').value,
        tipo_usuario: document.getElementById('tipo_usuario').value
    };
    
    const res = await fetch(`${API_BASE}/usuarios`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(usuario)
    });
    
    if(res.ok){
        alert("Cadastrado! Faça login.");
        voltarLogin();
    }
    else{
        alert("Erro ao cadastrar.");
    }
});

// ===== CADASTRO DE VULNERABILIDADE =====
document.getElementById('formVulnerabilidade').addEventListener('submit', async (e) => {
    e.preventDefault();
    const idUser = document.getElementById('idUsuarioLogado').value;
    
    const vuln = {
        titulo: document.getElementById('titulo').value,
        descricao: document.getElementById('descricao').value,
        sistema_impactado: document.getElementById('sistema_impactado').value,
        status: document.getElementById('status').value,
        metricasCVSS: { vectorString: document.getElementById('vectorString').value }
    };
    
    const res = await fetch(`${API_BASE}/api/vulnerabilidades/usuario/${idUser}`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(vuln)
    });
    
    if(res.ok){
        alert("Vulnerabilidade Salva!");
        document.getElementById('formVulnerabilidade').reset();
        navegar('historico');
    }
    else{
        alert("Erro ao salvar.");
    }
});

// ===== HISTORICO DAS VULNERABILIDADES =====
async function carregarHistorico(){
    const tbody = document.getElementById('tbody-historico');
    tbody.innerHTML = "<tr><td colspan='5'>Carregando...</td></tr>";
    
    try{
        const res = await fetch(`${API_BASE}/api/vulnerabilidades`);
        const lista = await res.json();
        vulnerabilidadesCache = lista;
        
        tbody.innerHTML = "";
        if(lista.length === 0) {
            tbody.innerHTML = "<tr><td colspan='5'>Nada encontrado.</td></tr>";
            return;
        }
        
        lista.forEach(v => {
            const rec = v.recomendacao ? "Já analisado" : "Pendente";
            
            const tr = `
                <tr>
                    <td>${v.id_vulnerabilidade}</td>
                    <td>${v.titulo}</td>
                    <td>${v.criticidade || '-'}</td>
                    <td>${v.status}</td>
                    <td>${rec}</td>
                </tr>
            `;
            tbody.innerHTML += tr;
        });
    }
    catch(err){
        tbody.innerHTML = "<tr><td colspan='5'>Erro ao carregar lista (Verifique o @JsonIgnore no Java).</td></tr>";
        console.error(err);
    }
}

// ===== RECOMENDACAO DA IA =====
function carregarDropdownIA(){
    const select = document.getElementById('select-vuln-ia');
    select.innerHTML = '<option value="">-- Selecione --</option>';
    
    if(vulnerabilidadesCache.length === 0) carregarHistorico(); 
    
    vulnerabilidadesCache.forEach(v => {
        const option = document.createElement('option');
        option.value = v.id_vulnerabilidade;
        option.text = `ID ${v.id_vulnerabilidade}: ${v.titulo}`;
        select.appendChild(option);
    });
}

function mostrarDetalhesVuln(){
    const id = document.getElementById('select-vuln-ia').value;
    const detalhesDiv = document.getElementById('detalhes-vuln');
    const containerResp = document.getElementById('resposta-ia-container');
    
    if(!id){
        detalhesDiv.style.display = 'none';
        
        return;
    }
    
    const vuln = vulnerabilidadesCache.find(v => v.id_vulnerabilidade == id);
    if(vuln){
        detalhesDiv.style.display = 'block';
        document.getElementById('desc-vuln-texto').innerText = vuln.descricao;
        
        if(vuln.recomendacao){
            containerResp.innerHTML = `<div class="chat-msg">${vuln.recomendacao}</div>`;
        }
        else{
            containerResp.innerHTML = "<em>Nenhuma recomendação gerada ainda.</em>";
        }
    }
}

async function pedirAjudaIA(){
    const id = document.getElementById('select-vuln-ia').value;
    const btn = document.getElementById('btn-ask-ia');
    const containerResp = document.getElementById('resposta-ia-container');
    
    if(!id) return alert("Selecione uma vulnerabilidade!");
    
    btn.disabled = true;
    btn.innerText = "IA Pensando...";
    containerResp.innerHTML = "<em>Gerando análise... aguarde...</em>";
    
    try{
        const res = await fetch(`${API_BASE}/api/vulnerabilidades/${id}/recomendacao`, {
            method: 'POST'
        });
        
        const texto = await res.text();
        
        containerResp.innerHTML = `<div class="chat-msg"><strong>Sugestão:</strong><br>${texto}</div>`;
        
        const vuln = vulnerabilidadesCache.find(v => v.id_vulnerabilidade == id);
        if(vuln) vuln.recomendacao = texto;
    }
    catch(err){
        containerResp.innerHTML = "<span style='color:red'>Erro ao falar com a IA.</span>";
    }
    finally{
        btn.disabled = false;
        btn.innerText = "Gerar Recomendação com IA";
    }
}
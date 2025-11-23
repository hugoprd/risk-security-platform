import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
  stages: [
    { duration: '30s', target: 100 }, // Sobe para 10 usuários
    { duration: '1m', target: 100 },  // Mantém 10 usuários por 1 minuto
    { duration: '10s', target: 0 },  // Desce para 0
  ],
  thresholds: {
    http_req_duration: ['p(95)<5000'], // 95% das requisições abaixo de 2s
    http_req_failed: ['rate<0.05'],    // Menos de 5% de erro
  },
};

const BASE_URL = 'http://127.0.0.1:8080/api';

export default function () {
  // LOGIN (Teste de Leitura e CPU)
  const payloadLogin = JSON.stringify({
    email: 'teste@email.com', // Garanta que esse user existe ou use um válido
    senha: '123',
  });

  const params = {
    headers: { 'Content-Type': 'application/json' },
  };

  let resLogin = http.post(`${BASE_URL}/auth/login`, payloadLogin, params);

  const loginSucesso = check(resLogin, { 'login realizado': (r) => r.status === 200 });

  // CADASTRO (Teste de Escrita) - Só faz se logou
  if (loginSucesso) {
    const randomId = Math.floor(Math.random() * 100000);

    const payloadVuln = JSON.stringify({
      titulo: `Vuln Carga ${randomId}`,
      descricao: "Teste via k6 no IntelliJ",
      sistema_impactado: "Servidor",
      criticidade: "ALTA",
      cve: "CVE-TESTE",
      metricasCVSS: { vectorString: "CVSS:3.1/AV:N/AC:L/PR:N/UI:N/S:U/C:H/I:H/A:H" }
    });


    let resCadastro = http.post(`${BASE_URL}/vulnerabilidades/usuario/1`, payloadVuln, params);

    check(resCadastro, { 'vulnerabilidade criada': (r) => r.status === 200 || r.status === 201 });
  }

  sleep(1);
}
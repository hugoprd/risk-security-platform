import http from 'k6/http';
import { check, sleep } from 'k6';

// Configuração de Carga
export const options = {
  stages: [
    { duration: '10s', target: 100 },
    { duration: '40s', target: 20 },
    { duration: '10s', target: 0 },
  ],
  thresholds: {
    http_req_duration: ['p(95)<500'],
    http_req_failed: ['rate<0.01'],
  },
};

const BASE_URL = 'http://127.0.0.1:8080/api';

export default function () {
  // LISTAR VULNERABILIDADES (Teste de Leitura - GET)

  let res = http.get(`${BASE_URL}/vulnerabilidades`);

  // Verifica se deu certo
  check(res, { 'listagem realizada': (r) => r.status === 200 });

  sleep(1);
}
Sistema de Reservas de Hotel
Introdução
Este projeto é um sistema de reservas para hotéis, com o objetivo de facilitar o gerenciamento de quartos e reservas.

Tecnologias Utilizadas
Scala: Linguagem de programação.
Akka HTTP: Framework para criar a API REST.
Slick: ORM para manipulação de dados.
Banco de Dados H2: Base de dados em memória para armazenamento temporário.
SBT: Ferramenta de build para gerenciar dependências e executar o projeto.
Passo a Passo para Instalação e Execução
Clone o Repositório e Entre no Diretório

bash
Copiar código
git clone https://github.com/seu-usuario/seu-projeto.git
cd seu-projeto
Atualize as Dependências

bash
Copiar código
sbt update
Compile o Projeto

bash
Copiar código
sbt compile
Execute o Projeto (servidor será iniciado em http://localhost:8080)

bash
Copiar código
sbt "~run"
Execute os Testes Automatizados

bash
Copiar código
sbt test
Endpoints da API - Exemplos de Requisições com curl
1. Adicionar Quarto ao Inventário
bash
Copiar código
curl -X POST http://localhost:8080/rooms -H "Content-Type: application/json" -d '{
  "roomId": "101"
}'
2. Remover Quarto do Inventário
bash
Copiar código
curl -X DELETE http://localhost:8080/rooms/101
3. Criar uma Reserva
bash
Copiar código
curl -X POST http://localhost:8080/reservations -H "Content-Type: application/json" -d '{
  "roomId": "101",
  "guestId": "guest123",
  "startDate": "2024-11-07T14:00",
  "endDate": "2024-11-07T16:00"
}'
4. Obter Ocupação para uma Data Específica
bash
Copiar código
curl -X GET http://localhost:8080/reservations/occupancy/2024-11-07
5. Criar uma Reserva com Dados Inválidos
bash
Copiar código
curl -X POST http://localhost:8080/reservations -H "Content-Type: application/json" -d '{
  "roomId": "",
  "guestId": "guest123",
  "startDate": "2024-11-07T14:00",
  "endDate": "2024-11-07T16:00"
}'
6. Criar Reserva em Conflito (tentativa de reservar o mesmo quarto em horários sobrepostos)
bash
Copiar código
curl -X POST http://localhost:8080/reservations -H "Content-Type: application/json" -d '{
  "roomId": "101",
  "guestId": "guest456",
  "startDate": "2024-11-07T15:00",
  "endDate": "2024-11-07T17:00"
}'
7. Criar Reserva com Janela de Limpeza de 4 Horas (violação da regra de limpeza)
bash
Copiar código
curl -X POST http://localhost:8080/reservations -H "Content-Type: application/json" -d '{
  "roomId": "101",
  "guestId": "guest789",
  "startDate": "2024-11-07T16:30",
  "endDate": "2024-11-07T18:00"
}'
8. Criar Reserva Após Janela de Limpeza (respeitando a regra de 4 horas)
bash
Copiar código
curl -X POST http://localhost:8080/reservations -H "Content-Type: application/json" -d '{
  "roomId": "101",
  "guestId": "guest789",
  "startDate": "2024-11-07T20:00",
  "endDate": "2024-11-07T22:00"
}'
Descrição das Funcionalidades
Funcionalidades Principais:
Adicionar/Remover Quarto: Gerenciamento do inventário de quartos, permitindo a adição e remoção de quartos.
Criar Reserva: Possibilita a criação de reservas para hóspedes, especificando quarto, período e hóspede.
Consultar Ocupação: Permite visualizar as reservas ativas em uma data específica.
Regras de Conflito e Limpeza: Implementa a lógica de conflitos de horários e uma janela de limpeza de 4 horas entre reservas.
Estrutura do Sistema:
Controladores (Controllers): Define os endpoints da API e gerencia requisições HTTP.
Serviços (Services): Implementa a lógica de negócios, como verificação de conflitos e janela de limpeza.
Acesso a Dados (DAO): Acesso ao banco de dados em memória H2 usando Slick.
Considerações de Segurança
Este projeto considera práticas recomendadas para evitar riscos comuns de segurança listados pela OWASP:

Injeção de SQL: O Slick é utilizado para consultas seguras, prevenindo injeções de SQL.
Exposição de Dados Sensíveis: Apenas informações necessárias são retornadas, sem dados confidenciais.
Falhas de Controle de Acesso: Como permitido pelo desafio, não foi implementado controle de acesso.
Configurações de Segurança: Mensagens de erro são genéricas para evitar a exposição de detalhes internos.
Validação de Dados: O roomId é validado para ser alfanumérico, e o período de reserva é verificado para prevenir datas conflitantes.

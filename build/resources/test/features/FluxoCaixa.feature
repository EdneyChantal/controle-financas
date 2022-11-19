Feature: Geração do Fluxo de Caixa
  Scenario: Gerar um exemplo de fluxo de caixa
    Given workspace
      | id | Description |
      | 1  | Padrao      |
    And  plano de natureza
      | idWorkspace | id | dsNatureplan | idNatureplanFather | indType |
      | 1           | 1  | Despesa      |                    | D       |
      | 1           | 2  | Alimentacao  | 1                  | D       |
      | 1           | 3  | Transporte   | 1                  | D       |
      | 1           | 4  | Escola       | 1                  | D       |
    And conta bancaria
      | idWorkspace | id | dsAccountbank | tipo | initialValue |
      | 1           | 1  | Conta 1       | N    | 50,00        |
      | 1           | 2  | Conta 2       | N    | 100,00       |
      | 1           | 3  | Conta 3       | N    | 200,00       |
    And Movimento Bancario
      | id | idAccountbank | idNatureplan | movementdate | movementvalue |
      | 1  | 1             | 2            | 15/01/2022   | -1200,00      |
      | 2  | 1             | 2            | 16/01/2022   | -1400,00      |
      | 3  | 1             | 2            | 17/01/2022   | -2200,00      |
      | 4  | 2             | 3            | 15/01/2022   | -200,00       |
      | 5  | 2             | 3            | 16/01/2022   | -400,00       |
      | 6  | 3             | 3            | 17/01/2022   | -200,00       |
      | 7  | 2             | 2            | 15/01/2022   | -200,00       |

    When gerarRelatorioFluxoCaixa em "14/01/2022"
    Then deve sair um relatório
      | id  | Descricao     | 10/01  | 11/01  | 12/01  | 13/01  | 14/01  | 15/01    | 16/01    | 17/01    | 31/01    | Total    |
      | 1   | Saldos        |        |        |        |        |        |          |          |          |          |          |
      | 1.1 | Saldo Inicial | 350,00 | 350,00 | 350,00 | 350,00 | 350,00 | 350,00   | -1050,00 | -2850,00 | -5250,00 |          |
      | 1.2 | Movimento     | 0      |        |        |        |        | -1400,00 | -1800,00 | -2400,00 |          |          |
      | 1.3 | Saldo Final   | 350,00 | 350,00 | 350,00 | 350,00 | 350,00 | -1050,00 | -2850,00 | -5250,00 | -5250,00 |          |
      | 1.4 | Saldo Conta 1 | 50,00  | 50,00  | 50,00  | 50,00  | 50,00  | -1150,00 | -2550,00 | -4750,00 |          |          |
      | 1.5 | Saldo Conta 2 | 100,00 | 100,00 | 100,00 | 100,00 | 100,00 | -100,00  | -500,00  | -500,00  |          |          |
      | 1.6 | Saldo Conta 3 | 200,00 | 200,00 | 200,00 | 200,00 | 200,00 | 200,00   | 200,00   | 0,00     |          |          |
      | 2   | Naturezas     |        |        |        |        |        |          |          |          |          |          |
      | 2.1 | Alimentacao   |        |        |        |        |        | -1200,00 | -1400,00 | -2200,00 |          | -4800,00 |
      | 2.2 | Transporte    |        |        |        |        |        | -200,00  | -400,00  | -200,00  |          | -800,00  |
      | 2.3 | Escola        |        |        |        |        |        |          |          |          |          |          |















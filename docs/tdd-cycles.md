# Registro dos ciclos de TDD — Exercício 1

Estes resumos foram obtidos das execuções reais dos testes com Maven durante o
desenvolvimento em 29/10/2026. Cada execução Red ocorreu antes da alteração
correspondente no código de produção; cada execução Green executou novamente
toda a suíte com `mvn -B test`.

| Comportamento adicionado | Red (falha esperada na construção) | Green (construção bem-sucedida) |
| --- | --- | --- |
| 1. Classificação de escaleno | 6 casos, 0 falhas, 6 erros | 6 casos, 0 falhas, 0 erros |
| 2. Classificação de equilátero | 8 casos, 2 falhas, 0 erros | 8 casos, 0 falhas, 0 erros |
| 3. Permutações de isósceles | 11 casos, 3 falhas, 0 erros | 11 casos, 0 falhas, 0 erros |
| 4. Lados com valor zero ou negativo | 18 casos, 7 falhas, 0 erros | 18 casos, 0 falhas, 0 erros |
| 5. Permutações da desigualdade triangular | 30 casos, 12 falhas, 0 erros | 30 casos, 0 falhas, 0 erros |
| 6. Limites de inteiros e prevenção de estouro | 38 casos, 0 falhas, 5 erros | 38 casos, 0 falhas, 0 erros |

O ciclo 1 começa com `UnsupportedOperationException` no esqueleto da classe.
O ciclo 6 apresenta exceções porque o estouro nas somas de valores `int` faz
triângulos válidos serem rejeitados; promover as somas para `long` resolve
essas falhas.

Após os seis ciclos, as asserções repetidas de rejeição foram extraídas para
`assertInvalidTriangle`. A execução final de `mvn -B clean verify` passou nos
38 casos e nas verificações de cobertura de linhas e ramificações. As evidências
estão em [coverage/exercise-1](coverage/exercise-1/).

O histórico do Git registra o esqueleto inicial, cada implementação que atingiu
a etapa Green e a etapa final de refatoração e documentação.

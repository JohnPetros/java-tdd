# Registro dos ciclos de TDD — Exercício 3

Estes resultados foram obtidos das execuções reais de `mvn -B test` durante o
desenvolvimento em 29/10/2026. As contagens incluem os 102 casos existentes dos
Exercícios 1 e 2, que continuaram passando durante todo o processo. Cada
execução Red ocorreu antes da alteração correspondente no código de produção;
cada execução Green executou novamente todos os exercícios.

| Comportamento adicionado | Red (falha esperada na construção) | Green (construção bem-sucedida) |
| --- | --- | --- |
| 1. Exemplo inicial de desenvolvedor | 103 casos, 0 falhas, 1 erro | 103 casos, 0 falhas, 0 erros |
| 2. Limite salarial de desenvolvedor e escalas decimais | 110 casos, 4 falhas, 0 erros | 110 casos, 0 falhas, 0 erros |
| 3. Descontos de DBA | 117 casos, 5 falhas, 0 erros | 117 casos, 0 falhas, 0 erros |
| 4. Descontos de testador | 124 casos, 5 falhas, 0 erros | 124 casos, 0 falhas, 0 erros |
| 5. Descontos de gerente | 131 casos, 4 falhas, 0 erros | 131 casos, 0 falhas, 0 erros |
| 6. Validação de entradas, precisão e valores elevados | 153 casos, 6 falhas, 0 erros | 153 casos, 0 falhas, 0 erros |

O esqueleto inicialmente lança `UnsupportedOperationException`. As execuções
Red seguintes demonstram a ausência das faixas de desconto e das verificações
de entrada. O último ciclo adiciona seis casos de entrada inválida que falham,
junto com casos de regressão de precisão e valores elevados que já passam.

Após os seis ciclos, as condições repetidas por cargo foram substituídas por
limites e descontos armazenados em `Role`. A calculadora passa a validar as
entradas, selecionar o desconto do cargo e aplicar uma única fórmula com
`BigDecimal`. Essa refatoração preserva o comportamento testado, incluindo a
comparação antes do arredondamento e o arredondamento final com `HALF_UP`
para duas casas decimais.

A execução final de `mvn -B clean verify` passa nos **153 casos**, incluindo
**51 do Exercício 3**, e nas verificações de **100% de cobertura de linhas e
ramificações**, sem exclusões.

Evidências: [cobertura e resumos de execução do Exercício 3](coverage/exercise-3/).
Os arquivos CSV e XML do JaCoCo contêm o pacote de salários extraído do relatório
da construção completa. Seus contadores de cobertura foram copiados diretamente
desse pacote.

O histórico do Git registra o esqueleto, cada implementação que atingiu a etapa
Green e a etapa final de refatoração e documentação.

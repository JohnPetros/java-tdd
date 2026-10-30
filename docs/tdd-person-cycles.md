# Registro dos ciclos de TDD — Exercício 2

Estes resumos foram obtidos das execuções reais de `mvn -B test` durante o
desenvolvimento em 29/10/2026. As contagens incluem os 38 casos existentes do
Exercício 1, que continuaram passando durante todo o processo. Cada execução
Red ocorreu antes da alteração correspondente no código de produção; cada
execução Green executou novamente os dois exercícios.

| Comportamento adicionado | Red (falha esperada na construção) | Green (construção bem-sucedida) |
| --- | --- | --- |
| 1. Pessoa válida e e-mail com partes mínimas | 39 casos, 0 falhas, 1 erro | 39 casos, 0 falhas, 0 erros |
| 2. Nome com várias partes formadas por letras | 57 casos, 11 falhas, 0 erros | 57 casos, 0 falhas, 0 erros |
| 3. Limites inclusivos de idade | 67 casos, 5 falhas, 0 erros | 67 casos, 0 falhas, 0 erros |
| 4. Associação obrigatória de e-mail | 70 casos, 2 falhas, 0 erros | 70 casos, 0 falhas, 0 erros |
| 5. Formato de todos os e-mails e erros por posição | 98 casos, 23 falhas, 0 erros | 98 casos, 0 falhas, 0 erros |
| 6. Pessoa nula e acúmulo de erros | 102 casos, 0 falhas, 1 erro | 102 casos, 0 falhas, 0 erros |

O esqueleto inicialmente lança `UnsupportedOperationException`. A última
execução Red revela uma `NullPointerException` quando a pessoa não é informada;
uma verificação inicial passa a retornar um erro de validação. O mesmo ciclo
confirma que os erros de nome, idade e de cada e-mail são acumulados e que
chamadas independentes não compartilham erros.

Após esses ciclos, a validação dos e-mails foi extraída para um método privado,
reduzindo o aninhamento e os acessos repetidos à associação. A execução completa
de `mvn -B clean verify` passou nos **102 casos** (64 do Exercício 2), incluindo
as verificações de **100% de cobertura de linhas e ramificações**, sem exclusões.

Evidências: [cobertura e resumos dos testes do Exercício 2](coverage/exercise-2/).
Os arquivos CSV e XML do JaCoCo contêm o pacote de validação de pessoas extraído
do relatório da construção completa. Seus contadores de cobertura foram
copiados diretamente desse pacote.

O histórico do Git registra o esqueleto, cada implementação que atingiu a etapa
Green e a etapa final de refatoração e documentação.

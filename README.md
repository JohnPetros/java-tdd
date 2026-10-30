# Java TDD — Exercícios com JUnit

Exercícios implementados com Java 21, JUnit 5 e Maven, seguindo a técnica de TDD.
O JaCoCo mede a cobertura do código de produção exercitado pelos testes.

- **Exercício 1:** classificar os lados de um triângulo como escaleno, isósceles
  ou equilátero.
- **Exercício 2:** validar uma pessoa (`Person`) e seus objetos `Email` associados
  por meio de `PersonDAO.isValidToInclude`, retornando uma lista de erros.
- **Exercício 3:** calcular o salário líquido de um funcionário usando o desconto
  correspondente ao cargo e ao salário-base.

A suíte completa possui **153 casos passando**: 38 do Exercício 1, 64 do
Exercício 2 e 51 do Exercício 3. Os três pacotes dos exercícios possuem **100%
de cobertura de linhas e ramificações**.

**Repositório:** [github.com/JohnPetros/java-tdd](https://github.com/JohnPetros/java-tdd)

## Pré-requisitos

- JDK **21** ou superior, com `JAVA_HOME` apontando para o JDK.
- Apache Maven **3.6.3** ou superior.
- Acesso à internet na primeira construção para baixar as dependências do Maven.

Confira as versões instaladas:

```sh
java -version
mvn -version
```

## Construção do projeto e execução dos testes

Obtenha o projeto:

```sh
git clone https://github.com/JohnPetros/java-tdd.git
cd java-tdd
```

Execute os comandos a seguir na raiz do repositório, onde está o arquivo `pom.xml`.

```sh
# Compilar, executar todos os testes, gerar o JAR e gerar e verificar a cobertura.
mvn clean verify

# Executar somente os testes, sem gerar o relatório HTML de cobertura.
mvn test

# Executar um grupo específico de testes parametrizados.
mvn '-Dtest=TriangleClassifierTest#classifiesEveryIsoscelesPermutation' test

# Executar somente os testes do Exercício 2.
mvn -Dtest=PersonDAOTest test

# Executar somente os testes do Exercício 3.
mvn -Dtest=SalaryCalculatorTest test
```

O JAR é gerado em `target/java-tdd-1.0.0-SNAPSHOT.jar`. O projeto é uma biblioteca:
as entradas são fornecidas por `TriangleClassifier.classify(int a, int b, int c)`,
`PersonDAO.isValidToInclude(Person person)` e `SalaryCalculator.calculate(Employee employee)`.
Os testes demonstram o comportamento desses métodos. Não é necessário executar
uma aplicação de console para rodar a suíte de testes.

## Exercício 1 — comportamento

Todos os lados devem ser positivos e satisfazer **as três** desigualdades estritas:

```text
a + b > c
a + c > b
b + c > a
```

Entradas inválidas lançam `IllegalArgumentException` e não são classificadas
como triângulos. Para entradas válidas, três lados iguais produzem `EQUILATERAL`,
exatamente dois lados iguais produzem `ISOSCELES` e três lados distintos produzem
`SCALENE`. A ordem dos valores não altera a classificação.

```java
import com.johnpetros.tdd.triangle.TriangleClassifier;
import com.johnpetros.tdd.triangle.TriangleType;

TriangleClassifier classifier = new TriangleClassifier();
TriangleType result = classifier.classify(3, 4, 5); // SCALENE
classifier.classify(5, 3, 5);                     // ISOSCELES
classifier.classify(3, 3, 3);                     // EQUILATERAL
classifier.classify(2, 3, 5);                     // lança IllegalArgumentException
```

As verificações da desigualdade promovem o primeiro operando de cada soma para
`long` **antes** da adição. Isso evita estouro quando a soma de dois lados válidos
do tipo `int` ultrapassa `Integer.MAX_VALUE`.

## Exercício 1 — casos de teste exigidos

Cada linha de um `@CsvSource` do JUnit é um caso executado separadamente.
A suíte do Exercício 1 contém **38 casos**, cobrindo todos os itens do enunciado:

| Item do enunciado | Método de teste | Entradas e resultado esperado |
| --- | --- | --- |
| 1. Escaleno válido | `classifiesValidScaleneTriangles` | Todas as 6 permutações de `(3, 4, 5)` → `SCALENE` |
| 2 e 4. Isósceles válido e pelo menos 3 permutações | `classifiesEveryIsoscelesPermutation` | `(5, 5, 3)`, `(5, 3, 5)`, `(3, 5, 5)` → `ISOSCELES` |
| 3. Equilátero válido | `classifiesValidEquilateralTriangles` | `(3, 3, 3)`, `(1, 1, 1)` → `EQUILATERAL` |
| 5. Um lado com valor zero | `rejectsZeroInAnyPosition` | `(0, 3, 3)`, `(3, 0, 3)`, `(3, 3, 0)` → exceção |
| 6. Um lado negativo | `rejectsNegativeInAnyPosition` | `(-1, 3, 3)`, `(3, -1, 3)`, `(3, 3, -1)` → exceção |
| 7 e 8. Soma de dois lados igual ao terceiro, em todas as permutações | `rejectsEveryDegeneratePermutation` | Todas as 6 permutações de `(2, 3, 5)` → exceção |
| 9 e 10. Soma de dois lados menor que o terceiro, em todas as permutações | `rejectsEveryTriangleInequalityViolationPermutation` | Todas as 6 permutações de `(2, 3, 6)` → exceção |
| 11. Três lados com valor zero | `rejectsThreeZeroSides` | `(0, 0, 0)` → exceção |
| Adicional: prevenção de estouro | `classifiesLargeValidSidesWithoutOverflow` | 5 casos válidos próximos de `Integer.MAX_VALUE`, cobrindo os 3 tipos de triângulo |
| Adicional: lados elevados inválidos | `rejectsLargeInvalidSides` | Todas as 3 permutações distintas de `(2147483647, 1, 1)` → exceção |

## Exercício 1 — evidências de cobertura de código

Medição realizada em **29/10/2026** com `mvn clean verify`, OpenJDK 21,
Maven 3.6.3, JUnit 5.13.4 e JaCoCo 0.8.14:

```text
Tests run: 38, Failures: 0, Errors: 0, Skipped: 0
All coverage checks have been met.
BUILD SUCCESS
```

| Classe de produção | Instruções | Linhas | Ramificações | Métodos |
| --- | --- | --- | --- | --- |
| `TriangleClassifier` | 67/67 (100%) | 10/10 (100%) | 22/22 (100%) | 2/2 (100%) |
| `TriangleType` | 21/21 (100%) | 4/4 (100%) | Não se aplica (sem ramificações) | 1/1 (100%) |
| **Total** | **88/88 (100%)** | **14/14 (100%)** | **22/22 (100%)** | **3/3 (100%)** |

As evidências copiadas diretamente dessa construção bem-sucedida estão
versionadas no projeto:

- [Relatório CSV do JaCoCo](docs/coverage/exercise-1/jacoco.csv)
- [Relatório XML do JaCoCo](docs/coverage/exercise-1/jacoco.xml)
- [Resumo de execução do JUnit](docs/coverage/exercise-1/test-results.txt)
- [Resumo de verificação do Maven](docs/coverage/exercise-1/verification.txt)

Para inspecionar a cobertura visualmente, execute `mvn clean verify` e abra
`target/site/jacoco/index.html` no navegador. Selecione o pacote e depois
`TriangleClassifier` para ver a cobertura por método e as linhas do código-fonte
destacadas. Os relatórios CSV e XML são gerados novamente no mesmo diretório.
Os relatórios de execução dos testes ficam em `target/surefire-reports/`.

A construção exige **100% de cobertura de linhas e ramificações** em todas as
classes de produção, sem exclusões. Esses números descrevem a cobertura obtida
pela **suíte completa**, e não a cobertura atribuída individualmente a cada
teste. A cobertura mostra qual código foi executado; as asserções verificam as
classificações e rejeições esperadas. As evidências versionadas representam
uma medição daquele momento e devem ser atualizadas após alterações no código.

## Exercício 2 — validação de pessoas

O modelo segue o diagrama fornecido:

- `Person`: `int id`, `String name`, `int age` e `List<Email> emails`.
- `Email`: `int id` e `String name` (o endereço de e-mail).
- `PersonDAO`: `List<String> isValidToInclude(Person person)`.

`Person` e `Email` são records do Java: classes de dados com construtores e
métodos de acesso gerados, como `person.name()` e `email.name()`. É possível
construir objetos com valores inválidos para que o DAO informe todos os erros
juntos. A operação de persistência `save`, apresentada no diagrama, está fora
do escopo do método de validação solicitado; não é necessário banco de dados.

O validador retorna uma **lista vazia** quando os quatro requisitos são atendidos.
Caso contrário, acumula **todos os erros**, na ordem: nome, idade e e-mails.

| Regra | Entrada aceita | Entrada rejeitada |
| --- | --- | --- |
| Nome | Pelo menos duas partes separadas por espaços em branco, contendo somente letras Unicode; por exemplo, `Ana Silva` ou `João Gonçalves` | Nulo, vazio, somente espaços, nome com uma única parte, dígitos, pontuação, hífens ou apóstrofos |
| Idade | Inteiros de **1 a 200**, inclusive | Zero, idades negativas ou acima de 200 |
| Associação de e-mail | Pelo menos um e-mail associado | Lista nula ou vazia |
| Nome de cada e-mail | `local@dominio.sufixo`, com pelo menos um caractere em cada parte | Objetos ou nomes de e-mail nulos, nomes vazios, partes ausentes, espaços em branco, `@` adicional ou segmentos de domínio vazios |

Espaços em branco nas extremidades do nome da pessoa são ignorados; espaços,
tabulações e espaços Unicode podem separar as partes do nome. Os endereços de
e-mail são verificados como recebidos, sem remover espaços nas extremidades.
São permitidos pontos e `+` na parte local, além de vários sufixos de domínio.
A implementação verifica o formato exigido pelo exercício, sem verificar a
entrega do e-mail nem implementar um analisador completo das regras de RFC.

Cada e-mail inválido gera seu próprio erro com a **posição iniciada em 1**.
Uma `Person` nula retorna `Person must be provided.` em vez de lançar exceção.
A validação não salva nem modifica os objetos recebidos, e cada chamada retorna
uma nova lista de erros.

```java
import java.util.List;
import com.johnpetros.tdd.person.Email;
import com.johnpetros.tdd.person.Person;
import com.johnpetros.tdd.person.PersonDAO;

PersonDAO dao = new PersonDAO();
Person valid = new Person(1, "Ana Silva", 30, List.of(new Email(1, "ana@example.com")));
List<String> validErrors = dao.isValidToInclude(valid); // []

Person invalid = new Person(2, "Ana", 0, List.of(new Email(2, "invalid")));
List<String> errors = dao.isValidToInclude(invalid);
// ["Name must contain at least two parts made only of letters.",
//  "Age must be between 1 and 200 inclusive.",
//  "Email at position 1 must match local@domain.suffix."]
```

### Casos de teste do Exercício 2

`PersonDAOTest` contém **64 casos**:

| Área | Casos | O que é verificado |
| --- | --- | --- |
| Caso válido inicial | 1 | Pessoa válida com partes mínimas no e-mail, `a@b.c` |
| Nome | 18 | Nomes com várias partes, acentos, espaços em branco, nomes ausentes, nomes com uma parte, dígitos e pontuação |
| Idade | 10 | Os dois limites inclusivos, valores válidos próximos, valores fora da faixa e limites de inteiros |
| Associação | 3 | Listas nulas ou vazias e vários e-mails associados |
| Formato e percurso dos e-mails | 28 | Formatos válidos, partes ausentes, valores nulos, espaços em branco, separadores extras e todos os e-mails de listas com entradas válidas e inválidas |
| Entrada nula e acúmulo de erros | 4 | Pessoa nula, várias falhas simultâneas e chamadas de validação independentes |

### Evidências de cobertura do Exercício 2

As evidências versionadas do Exercício 2 registram uma execução bem-sucedida
de `mvn clean verify` com **102 casos**: 38 do Exercício 1 e 64 do Exercício 2,
antes da inclusão do Exercício 3. As classes do Exercício 2 possuem **100% de
cobertura de linhas e ramificações**. A seguir estão os contadores medidos e
as evidências da construção versionadas:

| Classe de produção | Instruções | Linhas | Ramificações | Métodos |
| --- | --- | --- | --- | --- |
| `PersonDAO` | 99/99 (100%) | 21/21 (100%) | 22/22 (100%) | 4/4 (100%) |
| `Person` | 15/15 (100%) | 1/1 (100%) | Não se aplica (sem ramificações) | 1/1 (100%) |
| `Email` | 9/9 (100%) | 1/1 (100%) | Não se aplica (sem ramificações) | 1/1 (100%) |
| **Total** | **123/123 (100%)** | **23/23 (100%)** | **22/22 (100%)** | **6/6 (100%)** |

- [Cópia da medição em CSV do JaCoCo](docs/coverage/exercise-2/jacoco.csv)
- [Cópia da medição em XML do JaCoCo](docs/coverage/exercise-2/jacoco.xml)
- [Resumo de execução do JUnit do Exercício 2](docs/coverage/exercise-2/test-results.txt)
- [Resumo de verificação da construção completa](docs/coverage/exercise-2/verification.txt)

As cópias dos relatórios do JaCoCo contêm o pacote `com.johnpetros.tdd.person`
extraído do relatório da construção completa. Execute `mvn clean verify` e abra
`target/site/jacoco/index.html`; selecione esse pacote para inspecionar o
Exercício 2. Assim como no Exercício 1, as medições abrangem o código de produção
exercitado pela suíte completa, e a construção exige 100% de cobertura de linhas
e ramificações.

## Exercício 3 — calculadora de salários

`Employee` é um record do Java que contém `String name`, `String email`,
`BigDecimal baseSalary` e `Role role`. `SalaryCalculator.calculate(Employee)`
retorna o salário líquido do funcionário. Nome e e-mail são armazenados como
informações do funcionário; o cálculo depende do salário-base e do cargo.

O desconto incide sobre **todo o salário-base**, conforme o enunciado:

| Cargo no código | Cargo no enunciado | Limite | Desconto abaixo do limite | Desconto no limite ou acima |
| --- | --- | --- | --- | --- |
| `DEVELOPER` | DESENVOLVEDOR | 3.000,00 | 10% | 20% |
| `DBA` | DBA | 2.000,00 | 15% | 25% |
| `TESTER` | TESTADOR | 2.000,00 | 15% | 25% |
| `MANAGER` | GERENTE | 5.000,00 | 20% | 30% |

O cálculo é `salário líquido = salário-base × (1 − desconto)`. A igualdade
pertence à faixa de maior desconto: um desenvolvedor com salário-base de
`3000.00` recebe `2400.00`; com `2999.99`, recebe `2699.99`.

Todos os valores e taxas usam `BigDecimal` construído a partir de textos com
valores decimais. A comparação com o limite e a multiplicação usam o valor
original, sem arredondamento prévio. Como o enunciado não especifica o
arredondamento, esta implementação arredonda somente o salário líquido final
para **duas casas decimais com `RoundingMode.HALF_UP`**. São aceitos salários-base
com casas decimais adicionais. Por exemplo, um DBA com `0.10` recebe `0.09`, e
um desenvolvedor com `2999.999` permanece abaixo do limite e recebe `2700.00`
após o arredondamento final.

Salário-base zero é válido. Funcionário, cargo ou salário não informado, ou
salário negativo, provoca `IllegalArgumentException`.

```java
import java.math.BigDecimal;
import com.johnpetros.tdd.salary.Employee;
import com.johnpetros.tdd.salary.Role;
import com.johnpetros.tdd.salary.SalaryCalculator;

Employee employee = new Employee(
    "Ana Silva", "ana@example.com", new BigDecimal("3000.00"), Role.DEVELOPER
);
BigDecimal netSalary = new SalaryCalculator().calculate(employee); // 2400.00
```

### Casos de teste do Exercício 3

`SalaryCalculatorTest` contém **51 casos**. Os salários líquidos esperados são
informados explicitamente pelo `@CsvSource` do JUnit, e as asserções verificam
tanto o valor quanto a escala exigida de duas casas decimais.

| Área | Casos | O que é verificado |
| --- | --- | --- |
| Desenvolvedor | 8 | Exemplo inicial, zero, salário pequeno e valores abaixo, no limite e acima de 3000 |
| DBA | 7 | Zero, salário pequeno e valores abaixo, no limite e acima de 2000 |
| Testador | 7 | As mesmas faixas exigidas para DBA, verificadas de forma independente |
| Gerente | 7 | Zero, salário pequeno e valores abaixo, no limite e acima de 5000 |
| Arredondamento e precisão | 12 | Arredondamento de meio centavo, precisão adicional na entrada e comparação antes do arredondamento |
| Valores elevados | 4 | Um cálculo decimal exato com valor elevado para cada cargo |
| Entrada inválida | 6 | Funcionário, cargo ou salário não informado e três valores negativos |

O limite de cada cargo é testado com escalas decimais diferentes, como `3000`
e `3000.00`, que representam o mesmo valor.

### Evidências de cobertura do Exercício 3

Medição realizada em **29/10/2026** com `mvn clean verify`, JUnit 5.13.4
e JaCoCo 0.8.14:

```text
SalaryCalculatorTest: 51 cases, 0 failures, 0 errors
Full suite: 153 cases, 0 failures, 0 errors
All coverage checks have been met.
BUILD SUCCESS
```

| Classe de produção | Instruções | Linhas | Ramificações | Métodos |
| --- | --- | --- | --- | --- |
| `SalaryCalculator` | 46/46 (100%) | 9/9 (100%) | 8/8 (100%) | 2/2 (100%) |
| `Role` | 73/73 (100%) | 11/11 (100%) | 2/2 (100%) | 3/3 (100%) |
| `Employee` | 15/15 (100%) | 1/1 (100%) | Não se aplica (sem ramificações) | 1/1 (100%) |
| **Total** | **134/134 (100%)** | **21/21 (100%)** | **10/10 (100%)** | **6/6 (100%)** |

- [Cópia da medição em CSV do JaCoCo](docs/coverage/exercise-3/jacoco.csv)
- [Cópia da medição em XML do JaCoCo](docs/coverage/exercise-3/jacoco.xml)
- [Resumo de execução do JUnit do Exercício 3](docs/coverage/exercise-3/test-results.txt)
- [Resumo de verificação da construção completa](docs/coverage/exercise-3/verification.txt)

As cópias dos relatórios contêm o pacote `com.johnpetros.tdd.salary` extraído
do relatório da construção completa. Para inspecioná-lo visualmente, execute
`mvn clean verify`, abra `target/site/jacoco/index.html` e selecione esse pacote.
A cobertura é medida sobre a suíte completa, sem exclusões de código de produção.

## Processo de TDD

### Exercício 1

A implementação foi desenvolvida em pequenos ciclos
**Red → Green → Refactor** (falhar → passar → refatorar):

1. Começar com o esqueleto da classe e os testes de escaleno falhando; implementar o mínimo para fazê-los passar.
2. Adicionar testes de equilátero, observar as falhas e implementar a classificação.
3. Adicionar as três permutações de isósceles, observar as falhas e tratar os pares de lados iguais.
4. Adicionar lados com valor zero ou negativo, observar as falhas e rejeitar lados não positivos.
5. Adicionar permutações em que a soma é igual ou inferior ao terceiro lado, observar as falhas e aplicar a desigualdade triangular.
6. Adicionar testes dos limites de inteiros, observar as falhas e promover as somas para `long`.
7. Refatorar as asserções repetidas de entradas inválidas para um método auxiliar compartilhado; executar novamente todos os testes e as verificações de cobertura.

Cada etapa Green executou novamente toda a suíte existente. Consulte os
[resultados registrados dos ciclos](docs/tdd-cycles.md) e o histórico do Git
para acompanhar a evolução.

### Exercício 2

Seis ciclos Red → Green medidos introduziram o esqueleto das classes, a validação
do nome, os limites inclusivos de idade, a associação obrigatória de e-mail,
o formato de cada e-mail e o tratamento de pessoa nula com acúmulo de erros.
A validação dos e-mails foi então extraída para um método privado, e toda a
suíte e as verificações de cobertura passaram novamente. Consulte os
[resultados dos ciclos do Exercício 2](docs/tdd-person-cycles.md).

### Exercício 3

Seis ciclos Red → Green medidos introduziram o esqueleto da calculadora de
salários, o limite de desenvolvedor, os descontos de DBA, os descontos de
testador, os descontos de gerente e a validação das entradas. Casos de regressão
de precisão e valores elevados acompanharam o último ciclo. A refatoração final
moveu os limites e descontos de cada cargo para o enum `Role`, substituindo os
cálculos repetidos por uma única fórmula decimal. Os 153 casos e as verificações
de cobertura passaram. Consulte os
[resultados dos ciclos do Exercício 3](docs/tdd-salary-cycles.md).

## Estrutura do código-fonte

```text
src/main/java/com/johnpetros/tdd/triangle/
    TriangleClassifier.java
    TriangleType.java
src/test/java/com/johnpetros/tdd/triangle/
    TriangleClassifierTest.java
src/main/java/com/johnpetros/tdd/person/
    Person.java
    Email.java
    PersonDAO.java
src/test/java/com/johnpetros/tdd/person/
    PersonDAOTest.java
src/main/java/com/johnpetros/tdd/salary/
    Employee.java
    Role.java
    SalaryCalculator.java
src/test/java/com/johnpetros/tdd/salary/
    SalaryCalculatorTest.java
docs/
    tdd-cycles.md
    tdd-person-cycles.md
    tdd-salary-cycles.md
    coverage/exercise-1/
    coverage/exercise-2/
    coverage/exercise-3/
pom.xml
```

## Documentação das ferramentas

- [Guia do usuário do JUnit 5](https://docs.junit.org/5.13.4/user-guide/)
- [Plugin do JaCoCo para Maven](https://www.jacoco.org/jacoco/trunk/doc/maven.html)
- [Verificações de cobertura do JaCoCo](https://www.jacoco.org/jacoco/trunk/doc/check-mojo.html)

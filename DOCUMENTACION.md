# Documentación técnica — Adivina Quién

TP Parcial, Programación 3. Juego de adivinanzas de personajes ("Adivina Quién") con modo Humano vs Máquina y modo Máquina vs Máquina.

## 1. Cómo se juega

Hay 23 personajes, iguales para ambos jugadores, con 4 características declaradas: **género**, **calvicie**, **uso de lentes** y **color de pelo** (colorado, negro o amarillo).

Al arrancar una partida, cada jugador (humano o máquina) elige en secreto un personaje. Ese secreto queda fijo: no se puede cambiar durante la partida, y ningún otro jugador puede leerlo directamente. En cada turno, un jugador puede:

- **Preguntar un filtro** (ej. "¿es calvo?") sobre el secreto del rival, y usar la respuesta (sí/no) para descartar candidatos de su propia lista.
- **Adivinar directamente** un personaje. Si acierta, gana. Si falla, pierde la partida en el acto.

No hay límite de preguntas. Hay dos máquinas con estrategias distintas (una más asertiva que la otra), y un modo de exhibición donde dos máquinas juegan entre sí mostrando en consola todo su proceso de razonamiento, turno a turno.

Se persiste un marcador (`marcador.txt`) con la cantidad de partidas ganadas por cada usuario humano.

## 2. Arquitectura por paquetes

| Paquete | Responsabilidad |
|---|---|
| `enums` | Valores fijos del dominio: `Genero`, `ColorPelo` |
| `model` | Entidades del juego: `Personaje`, `Filtro`, `Jugada`, `Jugador` (y subclases), `Marcador` |
| `service` | Lógica reutilizable sin estado de partida: generación de datos, ordenamiento, búsqueda, estrategias de máquina, historial de preguntas, persistencia |
| `controller` | `PartidaController`, orquesta una partida completa: arma jugadores, corre el bucle de turnos, decide el ganador |

## 3. Patrones de diseño aplicados

### 3.1 Encapsulamiento fuerte del secreto (`model.Jugador`)

Este es el patrón que resuelve el requisito central de la consigna: *"la máquina no puede acceder directamente a la variable del personaje elegido por el jugador humano"*.

`Jugador` guarda `private final Personaje secreto` — **privado**, sin getter, en ningún punto del programa. La única forma de interactuar con el secreto de otro jugador es a través de dos métodos que devuelven `boolean`, nunca el objeto:

```java
public boolean esMiSecreto(Personaje candidato) {
    return secreto.getId().equals(candidato.getId());
}

public boolean respondeFiltro(Filtro filtro) {
    return filtro.cumple(secreto);
}
```

No es una convención ("no lo leas, aunque podrías") — es una restricción real del lenguaje: ninguna otra clase, ni siquiera `PartidaController`, puede escribir `otroJugador.secreto`. Además, `secreto` es `final`, así que una vez asignado en el constructor no se puede reasignar — eso garantiza *"el usuario al empezar a jugar no puede cambiar de personaje elegido"* sin necesitar ninguna validación adicional en tiempo de ejecución.

### 3.2 Template Method (`model.Jugador.decidirJugada`, `service.OrdenadorMergeSort`)

`Jugador` declara `public abstract Jugada decidirJugada(List<Filtro> filtrosDisponibles)`. `PartidaController` llama siempre a este mismo método, sin importarle si detrás hay un humano tipeando en consola (`JugadorHumano`) o una máquina calculando una jugada (`JugadorMaquina`) — el flujo del turno es idéntico, solo cambia el "cómo se decide".

El mismo patrón aparece en `OrdenadorMergeSort<T>`: implementa el algoritmo de Merge Sort completo una sola vez, y delega únicamente el criterio de comparación al método abstracto `vaPrimero(T, T)`. `OrdenadorPorGenero` y `OrdenadorPorVictorias` heredan el algoritmo entero sin duplicar una sola línea de la lógica de ordenamiento — solo dicen *qué* va antes de *qué*.

### 3.3 Strategy (`service.EstrategiaMaquina`, `service.Ordenador`)

`JugadorMaquina` no sabe razonar por sí mismo: delega toda decisión a un objeto `EstrategiaMaquina` inyectado por constructor. Esto permite tener **dos máquinas con el mismo tipo de jugador pero comportamiento distinto** (`EstrategiaBasica` vs. `EstrategiaAsertiva`) sin duplicar la clase `JugadorMaquina`, y sin usar `if/else` según "qué tipo de máquina es". `Ordenador<T>` cumple el mismo rol para el criterio de orden.

### 3.4 Factory (`service.GeneradorPersonaje`, `service.GeneradorFiltros`)

Centralizan la creación de los 23 personajes y de los 6 filtros. Ningún otro punto del programa instancia un `Personaje` o un `Filtro` a mano — todos piden la lista ya armada a estas clases, lo que evita inconsistencias (por ejemplo, dos personajes con el mismo id, o filtros duplicados).

### 3.5 Factory estático + objeto inmutable (`model.Jugada`)

`Jugada` tiene constructor `private` y dos métodos estáticos con nombre explícito: `crearJugadaPregunta(Filtro)` y `crearJugadaAdivinanza(Personaje)`. Es imposible construir una `Jugada` inconsistente (por ejemplo, una que declare ser de tipo "preguntar" pero tenga cargado un personaje adivinado) porque cada fábrica completa automáticamente el resto de los campos en `null`.

### 3.6 Observer (`service.HistorialPreguntas`)

Resuelve el requisito *"la Máquina 2 debe partir con la ventaja de conocer las preguntas hechas por la Máquina 1"*.

`HistorialPreguntas` es el *sujeto*: mantiene una lista de observadores y, cada vez que se registra una pregunta, les notifica a todos llamando `onPreguntaRegistrada(...)`. `JugadorMaquina implements HistorialPreguntas.Observador`, y al recibir la notificación agrega ese filtro a su propia lista de filtros ya usados.

La asimetría entre las dos máquinas **no está codificada dentro de las clases** — está en la *suscripción*, que decide `PartidaController` al armar el modo Máquina vs Máquina:

```java
HistorialPreguntas historial = new HistorialPreguntas();
historial.suscribir(maquina2); // Maquina 1 nunca se suscribe
```

**Aclaración honesta para la defensa**: esta ventaja es *mecánica*, no *epistémica*. Cada máquina investiga el secreto de un rival distinto, así que la respuesta que dio Máquina 1 sobre su rival no dice nada lógicamente sobre el secreto que investiga Máquina 2. Lo que gana Máquina 2 es no perder un turno repitiendo exactamente una pregunta que ya se hizo en la mesa, y arrancar su propio cálculo greedy con menos filtros para evaluar — una ventaja real, pero de eficiencia, no de información sobre el secreto ajeno.

## 4. Algoritmos aplicados

### 4.1 Ordenamiento — Merge Sort (`service.OrdenadorMergeSort`)

Implementado a mano (no se usó `Collections.sort`). Divide la lista recursivamente a la mitad hasta listas de 0 o 1 elemento, y combina dos mitades ya ordenadas comparando de a un elemento por vez (`mezclar`). Complejidad **O(n log n)**. Es **estable** a propósito (en caso de empate, se prioriza el elemento de la izquierda) para no alterar el orden de creación/autoincremento dentro de un mismo grupo de género — eso es lo que exige la consigna ("ordenados únicamente según su género").

Reutilizado, vía Template Method, para dos propósitos distintos sin duplicar código: ordenar personajes por género (`OrdenadorPorGenero`) y ordenar el marcador por cantidad de victorias (`OrdenadorPorVictorias`).

### 4.2 Divide y Conquista — Búsqueda binaria (`service.Buscador`)

Búsqueda recursiva sobre una lista ordenada por id. En cada llamada descarta la mitad del rango de búsqueda según si el id buscado es mayor o menor que el del medio. Complejidad **O(log n)**, contra O(n) de una búsqueda lineal.

**Nota técnica para la defensa**: estrictamente, la búsqueda binaria se clasifica como *decrease-and-conquer*, un caso particular de Divide y Conquista donde no hace falta resolver ambas mitades y combinar resultados (como sí hace Merge Sort) — alcanza con descartar una mitad completa, porque ya se sabe que la respuesta no puede estar ahí.

**Precondición y su riesgo**: `Buscador` exige que la lista esté ordenada por id. Por eso el proyecto mantiene **dos listas separadas** de los 23 personajes (`personajesPorId` y `personajesPorGenero` en `PartidaController`) en vez de una sola: si se usara la lista ordenada por género para buscar, hoy "casualmente" seguiría funcionando (los 12 personajes masculinos tienen id menor que los 11 femeninos, por el orden en que se declaran en `GeneradorPersonaje`), pero es una coincidencia del dataset actual, no una garantía estructural. Si alguien reordenara `GeneradorPersonaje` intercalando géneros, la búsqueda binaria empezaría a fallar de forma silenciosa (sin excepción, con resultados incorrectos). Mantener las dos vistas separadas es la decisión de diseño correcta, no una sobre-ingeniería.

### 4.3 Greedy — Estrategia asertiva (`service.EstrategiaAsertiva`)

Para cada filtro todavía no usado, cuenta cuántos de los candidatos restantes lo cumplen (`cumplen`) y cuántos no (`noCumplen`), y elige el filtro que minimiza `|cumplen - noCumplen|` — el que más cerca está de partir al grupo 50/50.

Es **greedy** (voraz) porque decide mirando un solo turno hacia adelante: no evalúa qué pasaría con la siguiente pregunta condicionada a la respuesta de esta, que sería el enfoque de un árbol de decisión completo (ver sección 5). No garantiza el mínimo de preguntas en el peor caso absoluto, pero converge notablemente más rápido que la estrategia sin optimizar, y esa diferencia es medible jugando el modo Máquina vs Máquina.

**Caso límite manejado explícitamente**: entre los 23 personajes hay pares con exactamente los mismos 4 atributos (ej. Felipe y Lautaro; Tomás y Bruno). Es posible que, incluso usando los 6 filtros disponibles, queden 2 candidatos empatados sin ninguna pregunta que los distinga. Ambas estrategias (`EstrategiaBasica` y `EstrategiaAsertiva`) manejan esto con un guard `if (filtrosDisponibles.isEmpty())`, que fuerza una adivinanza entre los candidatos empatados en vez de romperse — igual que pasaría en el juego de mesa real.

## 5. Algoritmos evaluados y **no** aplicados (y por qué)

- **Árbol de decisión óptimo precomputado**: se podría precalcular, para las 23 fichas y 6 filtros posibles, la secuencia de preguntas que minimiza el peor caso de forma matemáticamente óptima (como se resuelve el "Adivina Quién" real de mesa). Se descartó por relación costo/beneficio: con solo 6 filtros y 23 personajes, el algoritmo greedy turno a turno da resultados prácticamente indistinguibles del óptimo, y precomputar el árbol completo agrega una complejidad de implementación (y de explicación en la defensa) que no se justifica para el alcance de este TP.
- **Minimax / backtracking con exploración de árbol de juego**: no aplica porque este no es un juego adversarial de suma cero con jugadas alternadas sobre el mismo tablero. Cada jugador investiga el secreto de un rival de forma independiente — no hay "movimientos" del rival sobre un tablero compartido que anticipar, como sí ocurriría en, por ejemplo, un juego de mesa por turnos con un único estado global (ajedrez, tres en línea).
- **Estructuras de datos avanzadas (árboles balanceados, hash sets para candidatos, etc.)**: con 23 elementos, una `ArrayList` recorrida linealmente o partida con búsqueda binaria ya es más que suficiente; una estructura más sofisticada no aportaría una mejora medible y sí complejidad de código innecesaria.

## 6. Persistencia del marcador (`service.RecordService`)

Se persiste en un archivo de texto plano (`marcador.txt`), formato `usuario,partidasGanadas` por línea — no se usó una base de datos porque el volumen de datos (un puñado de usuarios) no lo justifica. `cargarMarcadores()` reconstruye el mapa completo en memoria al leer, y `registrarVictoria(usuario)` reescribe el archivo entero con el estado actualizado. Solo se registra la victoria cuando el ganador es el usuario humano (no se registran victorias de las máquinas), porque el marcador es explícitamente "por usuario", y las máquinas no son usuarios.

## 7. Cómo correr el proyecto

```
javac -d out $(find src -name "*.java")
java -cp out Main
```

Desde el menú principal se puede jugar contra cualquiera de las dos máquinas, ver el modo de exhibición Máquina vs Máquina (recomendado para la defensa: muestra en vivo el efecto del algoritmo greedy vs. la estrategia sin optimizar), y consultar el marcador acumulado.

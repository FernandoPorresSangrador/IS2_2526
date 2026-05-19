# PRÁCTICA 6 – REFACTORIZACIONES
## Ingeniería del Software II – 3º Grado en Ingeniería Informática
### Universidad de Cantabria

---

## 1. Descripción del Sistema Heredado

La aplicación gestiona conductores y transportes de una empresa. Cada conductor tiene datos personales (DNI como identificador, nombre, apellidos, dirección) y una lista de transportes realizados. El sueldo se calcula como: **700€ base + Σ(5€/hora + extra por tipo)** por cada transporte.

Tipos de transporte y extras:
- **Personas**: +0.5€/hora (no colectivo, <10 personas) o +1€/hora (colectivo, ≥10)
- **Mercancías**: +2€/tonelada
- **Mercancías Peligrosas**: +2€/tonelada + 50€ fijo

---

## 2. Métricas Iniciales (Proyecto Original)

### Reglas de cálculo aplicadas
- **CC** (Complejidad Ciclomática): nodos de condición + 1. Cada `case` de un `switch` suma 1; cada `||` o `&&` adicional en una condición suma 1.
- **CCog** (Complejidad Cognitiva): +1 por estructura de control; +N adicional por nivel de anidación (N = nivel actual); `else`/`else if` suman siempre 1; `switch` suma 1; secuencias de `||` o `&&` iguales suman 1 por secuencia.
- **CBO**: número de clases distintas del sistema con las que una clase está acoplada (AFF ∪ EFF, sin duplicar).
- **DIT**: longitud del camino hasta la raíz de la jerarquía de herencia.
- **NOC**: número de subclases inmediatas.

---

### Clase: `Transporte`

| Método | CC | CCog |
|--------|-----|-------|
| `Transporte(double, CategoriaTransporte, int)` | 5 | 4 |
| `horas()` | 1 | 0 |
| `categoria()` | 1 | 0 |
| `ton()` | 1 | 0 |
| `getPersonas()` | 1 | 0 |
| **TOTAL** | **WMC = 9** | **CCog total = 4** |

- **WMCn** = 9 / 5 = **1.80**
- **CCogn** = 4 / 5 = **0.80**
- **CBO** = 1 (depende de `CategoriaTransporte`)
- **DIT** = 0 (no hereda de ninguna clase del sistema)
- **NOC** = 0

**Cálculo CC del constructor:**
```
if (horas <= 0 || valor <= 0 || cat == null)  → CC: +1(if) +1(||) +1(||) = 3 nodos
if (cat.equals(...))                           → CC: +1(if)
Total: 1 + 4 = 5
```

**Cálculo CCog del constructor:**
```
if (...||...||...)  → +1(if) +1(primera ||) +1(segunda ||) = 3
if (cat.equals)     → +1(if, nivel 0)
CCog = 4
```

---

### Clase: `Conductor`

| Método | CC | CCog |
|--------|-----|-------|
| `Conductor(String,...)` | 5 | 4 |
| `dni()` | 1 | 0 |
| `getDni()` | 1 | 0 |
| `getNombre()` | 1 | 0 |
| `getApellido1()` | 1 | 0 |
| `apellido2()` | 1 | 0 |
| `getDire()` | 1 | 0 |
| `sueldo()` | 6 | 6 |
| `anhadeTransporte(Transporte)` | 1 | 0 |
| **TOTAL** | **WMC = 18** | **CCog total = 10** |

- **WMCn** = 18 / 9 = **2.00**
- **CCogn** = 10 / 9 = **1.11**
- **CBO** = 2 (`Transporte`, `CategoriaTransporte`)
- **DIT** = 0
- **NOC** = 0

**Cálculo CC de `sueldo()`:**
```
for (...)                → +1
case Mercancias          → +1
case MercanciasPeligrosas → +1
case Personas            → +1
if (t.getPersonas() < 10) → +1
Total: 1 + 5 = 6
```

**Cálculo CCog de `sueldo()`:**
```
for(nivel 0)        → +1           = 1
switch(nivel 1)     → +1+1(anid)   = 2
if(nivel 2, dentro de case Personas) → +1+2(anid) = 3
CCog = 1+2+3 = 6
```

---

### Clase: `GestionTransportes` (renombrada desde `gestionTransportes`)

| Método | CC | CCog |
|--------|-----|-------|
| `buscaConductor(String)` | 3 | 3 |
| `anhadeConductor(...)` | 2 | 1 |
| `conductores()` | 1 | 0 |
| **TOTAL** | **WMC = 6** | **CCog total = 4** |

- **WMCn** = 6 / 3 = **2.00**
- **CCogn** = 4 / 3 = **1.33**
- **CBO** = 1 (`Conductor`)
- **DIT** = 0, **NOC** = 0

**Cálculo CCog de `buscaConductor()`:**
```
for(nivel 0)    → +1           = 1
if(nivel 1)     → +1+1(anid)   = 2
CCog = 3
```

---

### Clase: `GestionTransportesGUI`

| Método | CC | CCog |
|--------|-----|-------|
| `main(String[])` | 15 | 20 |
| `mensaje(String, String)` | 1 | 0 |
| **TOTAL** | **WMC = 16** | **CCog total = 20** |

- **WMCn** = 16 / 2 = **8.00**
- **CCogn** = 20 / 2 = **10.00** ← muy elevado
- **CBO** = 5 (`GestionTransportes`, `Conductor`, `Transporte`, `CategoriaTransporte`, fundamentos)
- **DIT** = 0, **NOC** = 0

---

### Clase: `CategoriaTransporte` (enum)

- **WMC** = 0 (sin métodos de lógica)
- **CBO** = 0 (no usa otras clases del sistema)
- **DIT** = 0, **NOC** = 0

---

### Resumen métricas iniciales

| Clase | WMC | WMCn | CCog | CCogn | CBO (clases) | DIT | NOC |
|-------|-----|------|------|-------|------|-----|-----|
| `Transporte` | 9 | 1.80 | 4 | 0.80 | 1 (CategoriaTransporte) | 0 | 0 |
| `Conductor` | 18 | 2.00 | 10 | 1.11 | 2 (Transporte, CategoriaTransporte) | 0 | 0 |
| `GestionTransportes` | 6 | 2.00 | 4 | 1.33 | 1 (Conductor) | 0 | 0 |
| `GestionTransportesGUI` | 16 | 8.00 | 20 | 10.00 | 5 | 0 | 0 |
| `CategoriaTransporte` | 0 | — | 0 | — | 0 | 0 | 0 |

---

## 3. Code Smells Detectados

1. **Abuso de switch** (`Conductor.sueldo()`): el switch sobre `CategoriaTransporte` viola el principio Open/Closed. Cada nuevo tipo de transporte requiere modificar `Conductor`.
2. **Nombre de clase incorrecto** (`gestionTransportes`): no sigue la convención Java (PascalCase).
3. **Inconsistencia en getters** (`Conductor`): `dni()` y `apellido2()` no siguen la convención `get*`.
4. **Método `main` muy largo** (`GestionTransportesGUI`): más de 80 líneas en un solo método, CCog = 20.
5. **Bug en GestionTransportesGUI original**: imprime `conductor.getNombre()` dos veces en lugar de nombre y apellido.

---

## 4. Refactorizaciones Aplicadas

### Refactorización 1: Replace Type Code with Subclasses
**Code smell:** `switch` sobre `CategoriaTransporte` en `Conductor.sueldo()`.

Se elimina el enum `CategoriaTransporte` y se crea una jerarquía:
- `Transporte` (abstracta) con método abstracto `extraSueldo()`
- `TransportePersonas extends Transporte`
- `TransporteMercancias extends Transporte`
- `TransporteMercanciasPeligrosas extends TransporteMercancias`

El método `Conductor.sueldo()` pasa de CC=6/CCog=6 a CC=2/CCog=1.

### Refactorización 2: Extract Method (`extraSueldo()`)
**Code smell:** lógica de cálculo del extra mezclada con el bucle en `sueldo()`.

Cada subclase encapsula su propia lógica de cálculo en `extraSueldo()`.

### Refactorización 3: Rename Class / Rename Method
**Code smell:** `gestionTransportes` → `GestionTransportes`. `apellido2()` → se añade `getApellido2()` manteniendo compatibilidad.

### Refactorización 4: Pull Up Field
**Code smell:** `ton` es un atributo compartido entre `TransporteMercancias` y `TransporteMercanciasPeligrosas`.

`ton` se declara `protected` en `TransporteMercancias` y `TransporteMercanciasPeligrosas` lo hereda.

### Refactorización 5: Extract Method en `GestionTransportesGUI`
**Code smell:** método `main` excesivamente largo (CCog=20).

Se extraen: `procesarNuevoConductor()`, `procesarNuevoTransporte()`, `mostrarSueldoConductor()`, `mostrarMejorConductor()`.

### Corrección de bug (mejora adicional)
En `GestionTransportesGUI` original, la línea:
```java
msj += conductor.getNombre() + " " + conductor.getNombre() + "\n";  // BUG
```
Se corrige a:
```java
msj += conductor.getNombre() + " " + conductor.getApellido1() + "\n";
```

---

## 5. Métricas Finales (Proyecto Refactorizado)

### Clase: `Transporte` (abstracta)

| Método | CC | CCog |
|--------|-----|-------|
| `Transporte(double)` | 2 | 1 |
| `horas()` | 1 | 0 |
| `extraSueldo()` (abstracto) | 1 | 0 |
| **TOTAL** | **WMC = 4** | **CCog total = 1** |

- **WMCn** = 4/3 = **1.33**
- **CCogn** = 1/3 = **0.33**
- **CBO** = 0
- **DIT** = 0, **NOC** = 2 (`TransportePersonas`, `TransporteMercancias`)

---

### Clase: `TransportePersonas`

| Método | CC | CCog |
|--------|-----|-------|
| `TransportePersonas(double, int)` | 2 | 1 |
| `getPersonas()` | 1 | 0 |
| `extraSueldo()` | 2 | 1 |
| **TOTAL** | **WMC = 5** | **CCog total = 2** |

- **WMCn** = 5/3 = **1.67**
- **CCogn** = 2/3 = **0.67**
- **CBO** = 0
- **DIT** = 1, **NOC** = 0

---

### Clase: `TransporteMercancias`

| Método | CC | CCog |
|--------|-----|-------|
| `TransporteMercancias(double, int)` | 2 | 1 |
| `getTon()` | 1 | 0 |
| `extraSueldo()` | 1 | 0 |
| **TOTAL** | **WMC = 4** | **CCog total = 1** |

- **WMCn** = 4/3 = **1.33**
- **CCogn** = 1/3 = **0.33**
- **CBO** = 0
- **DIT** = 1, **NOC** = 1 (`TransporteMercanciasPeligrosas`)

---

### Clase: `TransporteMercanciasPeligrosas`

| Método | CC | CCog |
|--------|-----|-------|
| `TransporteMercanciasPeligrosas(double, int)` | 1 | 0 |
| `extraSueldo()` | 1 | 0 |
| **TOTAL** | **WMC = 2** | **CCog total = 0** |

- **WMCn** = 2/2 = **1.00**
- **CCogn** = 0/2 = **0.00**
- **CBO** = 0
- **DIT** = 2, **NOC** = 0

---

### Clase: `Conductor` (refactorizada)

| Método | CC | CCog |
|--------|-----|-------|
| `Conductor(String,...)` | 5 | 4 |
| `dni()` | 1 | 0 |
| `getDni()` | 1 | 0 |
| `getNombre()` | 1 | 0 |
| `getApellido1()` | 1 | 0 |
| `apellido2()` | 1 | 0 |
| `getApellido2()` | 1 | 0 |
| `getDire()` | 1 | 0 |
| `sueldo()` | **2** | **1** |
| `anhadeTransporte(Transporte)` | 1 | 0 |
| **TOTAL** | **WMC = 15** | **CCog total = 5** |

- **WMCn** = 15/10 = **1.50**
- **CCogn** = 5/10 = **0.50**
- **CBO** = 1 (`Transporte` — ya no depende de `CategoriaTransporte`)
- **DIT** = 0, **NOC** = 0

---

### Clase: `GestionTransportes` (refactorizada)

Igual que la original (solo Rename Class).

- **WMC** = 6, **WMCn** = 2.00, **CCog** = 4, **CCogn** = 1.33
- **CBO** = 1 (`Conductor`), **DIT** = 0, **NOC** = 0

---

### Clase: `GestionTransportesGUI` (refactorizada)

| Método | CC | CCog |
|--------|-----|-------|
| `main(String[])` | **8** | **7** |
| `procesarNuevoConductor(...)` | 2 | 1 |
| `procesarNuevoTransporte(...)` | 5 | 4 |
| `mostrarSueldoConductor(...)` | 2 | 1 |
| `mostrarMejorConductor(...)` | 6 | 6 |
| `mensaje(String, String)` | 1 | 0 |
| **TOTAL** | **WMC = 24** | **CCog total = 19** |

- **WMCn** = 24/6 = **4.00** (antes 8.00)
- **CCogn** = 19/6 = **3.17** (antes 10.00)
- **CBO** = 5 (`GestionTransportes`, `Conductor`, `Transporte`, `TransportePersonas`, `TransporteMercancias`, `TransporteMercanciasPeligrosas`, fundamentos) = **7**
- **DIT** = 0, **NOC** = 0

---

### Resumen métricas finales

| Clase | WMC | WMCn | CCog | CCogn | CBO | DIT | NOC |
|-------|-----|------|------|-------|-----|-----|-----|
| `Transporte` (abs.) | 4 | 1.33 | 1 | 0.33 | 0 | 0 | 2 |
| `TransportePersonas` | 5 | 1.67 | 2 | 0.67 | 0 | 1 | 0 |
| `TransporteMercancias` | 4 | 1.33 | 1 | 0.33 | 0 | 1 | 1 |
| `TransporteMercanciasPeligrosas` | 2 | 1.00 | 0 | 0.00 | 0 | 2 | 0 |
| `Conductor` | 15 | 1.50 | 5 | 0.50 | 1 | 0 | 0 |
| `GestionTransportes` | 6 | 2.00 | 4 | 1.33 | 1 | 0 | 0 |
| `GestionTransportesGUI` | 24 | 4.00 | 19 | 3.17 | 7 | 0 | 0 |

---

## 6. Análisis Comparativo de Métricas

### Comparación directa

| Clase / Métrica | WMCn antes | WMCn después | CCogn antes | CCogn después |
|-----------------|-----------|-------------|------------|--------------|
| `Transporte`/jerarquía | 1.80 | 1.38 (media) | 0.80 | 0.33 (media subclases) |
| `Conductor.sueldo()` | CC=6, CCog=6 | CC=2, CCog=1 | — | — |
| `GestionTransportesGUI` | WMCn=8.00 | WMCn=4.00 | CCogn=10.00 | CCogn=3.17 |

### Interpretación

**La refactorización 1 (Replace Type Code with Subclasses)** es la de mayor impacto. El método `Conductor.sueldo()` pasa de CC=6/CCog=6 a CC=2/CCog=1, una reducción del 67-83%. Esto refleja directamente la eliminación del `switch`, que es el code smell más grave del sistema original. La complejidad se distribuye ahora entre las subclases de `Transporte`, con valores bajos y homogéneos (CCog ≤ 1 por método).

**La refactorización 5 (Extract Method en GUI)** reduce el WMCn de `GestionTransportesGUI` de 8.00 a 4.00 y el CCogn de 10.00 a 3.17. Aunque el WMC total sube (más métodos), los métodos individuales son más pequeños, legibles y mantenibles. Este es un resultado típico y esperado del Extract Method.

**DIT y NOC** reflejan la nueva jerarquía: `Transporte` tiene NOC=2, `TransporteMercancias` tiene NOC=1, y las hojas tienen DIT=1 o 2. Valores bajos de DIT indican que la jerarquía no es excesivamente profunda, lo que es positivo para la mantenibilidad.

**CBO de `Conductor`** se reduce de 2 a 1: ya no depende de `CategoriaTransporte` (que desaparece), solo de la clase abstracta `Transporte`. Esto refleja el menor acoplamiento obtenido.

En conclusión, las métricas confirman de manera objetiva las mejoras de calidad: menor complejidad en los métodos clave, distribución más uniforme de la lógica entre clases bien definidas, y mejor separación de responsabilidades.

---

## 7. Instrucciones de uso con Git y Maven

### Configuración inicial del repositorio
```bash
# En la raíz del repositorio IS2_2526
git checkout main
git checkout -b practica6

mkdir Practica6
# Crear manualmente las carpetas de proyecto Maven dentro de Practica6/
# y crear los proyectos desde Eclipse apuntando a esas carpetas
```

### Instalación de fundamentos en repositorio local
```bash
mvn install:install-file -Dfile=fundamentos.jar \
    -DgroupId=es.unican.fundamentos \
    -DartifactId=fundamentos \
    -Dversion=1.0 \
    -Dpackaging=jar
```

### Compilar, testar y empaquetar
```bash
# Proyecto original
cd Practica6/transportes-original
mvn test          # ejecuta tests
mvn package       # genera jar y jar-with-dependencies

# Proyecto refactorizado
cd ../transportes-refactorizado
mvn test
mvn package       # genera transportes-refactorizado-0.0.1-SNAPSHOT-jar-with-dependencies.jar
```

### Subida y fusión final
```bash
git add Practica6/
git commit -m "Practica6: proyecto original con metricas"
git push

# ... (commits intermedios durante el desarrollo) ...

git commit -m "Practica6: refactorizaciones aplicadas y metricas finales"
git push

# Al finalizar, fusionar en main:
git checkout main
git merge practica6
git push
```

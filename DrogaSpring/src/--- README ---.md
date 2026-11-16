# 🚀 GUIA DEFINITIVA EXAMEN SPRING BOOT (MVC + THYMELEAF)

---

## 1. EL FLUJO: ¿Cómo viajan los datos? (La Regla de Oro)

Imagina tu aplicación como un restaurante:

```text
[ Navegador / HTML ] 
       ⇅  (Viajan DTOs dentro de ModelAndView / @ModelAttribute)
[ CONTROLLER ]  --> El Camarero (Recibe peticiones de la web y devuelve vistas HTML)
       ⇅  (Viajan DTOs)
[ SERVICE ]     --> El Traductor (Convierte DTO ↔ Entity con convertToDTO / convertToEntity)
       ⇅  (Viajan Entities)
[ REPOSITORY ]  --> El Almacén / Cocina (Guarda y busca en la lista static List<Entity> datos)
```

* **Entity (`DrogaEntity`, `RarezaEntity`)**: Solo vive en el `Repository` y entra al `Service` para traducirse. **NUNCA** llega al Controller ni al HTML.
* **DTO (`DrogaDTO`, `RarezaDTO`)**: Es el mensajero. Tiene exactamente los mismos atributos que la Entity, pero incluye los métodos `convertToDTO` y `convertToEntity`.

---

## 2. ORDEN PARA CREAR UN PROYECTO (O UNA CLASE NUEVA) EN EL EXAMEN

Si te piden añadir una entidad nueva (ej: `Proveedor`, `Categoria`, `Efecto`...), **hazlo SIEMPRE de abajo hacia arriba** para que no te salgan errores en rojo mientras programas:

1. **Paso 1: `Entity`** (`repositories/entities/NombreEntity.java`)
   * Pones `@Data`, `@AllArgsConstructor`, `@NoArgsConstructor`.
   * Pones los atributos. Si tiene relación con otra clase, pones la **Entity** de esa clase (`private RarezaEntity rarezaEntity;`).
   * **OBLIGATORIO**: Clic derecho -> *Generate* -> `equals() and hashCode()` -> **Marcar SOLO el campo `id`**.

2. **Paso 2: `DTO`** (`models/dto/NombreDTO.java`)
   * Copias y pegas los atributos de la Entity (pero si hay relación, cambias `RarezaEntity` por `RarezaDTO`).
   * Generas el `equals() and hashCode()` marcando **SOLO el `id`**.
   * Creas los dos métodos estáticos: `public static NombreDTO convertToDTO(NombreEntity entity)` y `public static NombreEntity convertToEntity(NombreDTO dto)`.
   * *Truco*: Pon siempre `if (entity == null) return new NombreDTO();` en la primera línea de los conversores de la clase secundaria para evitar `NullPointerException`.

3. **Paso 3: `Repository` y `RepositoryImpl`** (`repositories/dao/`)
   * Creas la interfaz con los 4 métodos (`findAll`, `findById`, `save`, `delete`) que reciben y devuelven **Entities**.
   * Creas la clase `Impl` con `@Repository` arriba del todo y `private static List<NombreEntity> datos = new ArrayList<>();`.
   * Creas el bloque `static { ... }` abajo para meter 3 o 4 datos de prueba iniciales.

4. **Paso 4: `Service` y `ServiceImpl`** (`services/`)
   * Creas la interfaz con los mismos 4 métodos pero recibiendo y devolviendo **DTOs**.
   * Creas la clase `Impl` con `@Service` arriba e inyectas el repositorio con `@Autowired`.

5. **Paso 5: `Controller`** (`Controller/`)
   * Pones `@Controller` y `@RequestMapping("/ruta")`.
   * Inyectas el Service con `@Autowired` (y si necesitas un `<select>`, inyectas también el otro Service).

6. **Paso 6: `HTMLs`** (`resources/templates/`)
   * Tabla principal (`lista.html`), Formulario (`formulario.html`) y Detalle (`ver.html`).

---

## 3. LOS 5 FALLOS TÍPICOS QUE HACEN QUE PETE EL EXAMEN (¡REVISAR SI ALGO FALLA!)

1. **No funciona el Editar, el Borrar o el Ver (devuelve `null` o `-1`)**:
   * **Causa 1**: Se te olvidó generar el `equals()` y `hashCode()` **SOLO por el `id`** en la `Entity`. Recuerda que `@Data` compara todos los campos; tienes que sobrescribirlo abajo del todo en la clase.
   * **Causa 2**: En el `formulario.html` se te olvidó poner el input oculto del ID dentro del `<form>`:
     ```html
     <input type="hidden" th:field="*{id}" />
     ```
     *(Sin esta línea, al darle a guardar cuando editas, el ID llega vacío y te crea un objeto nuevo en vez de machacar el que estabas editando).*

2. **Diferencia entre `Long id` e `int id` en el `save()` del Repository**:
   * Si tu ID es objeto (`Long` o `Integer`), cuando es nuevo vale `null`:
     ```java
     if (entity.getId() != null && findById(entity) != null) // EDITAR
     ```
   * Si tu ID es primitivo (`int` o `long`), nunca puede ser `null`, cuando es nuevo vale `0`:
     ```java
     if (entity.getId() != 0 && findById(entity) != null) // EDITAR
     ```

3. **Al guardar una Droga con Rareza, la Rareza se queda con el nombre en `null`**:
   * El `<select th:field="*{rarezaDTO.id}">` del formulario **solo envía el ID** de la rareza elegida, el resto de campos (nombre, nivel, porcentaje) llegan vacíos al controlador.
   * Por eso, en el `@PostMapping("/save")` del `DrogaController`, **antes** de hacer `drogaService.save(droga)`, tienes que buscar la rareza completa y enchufársela:
     ```java
     droga.setRarezaDTO(rarezaService.findById(droga.getRarezaDTO()));
     drogaService.save(droga);
     ```

4. **Error 404 / TemplateInputException al abrir una página**:
   * Revisa que el nombre en `new ModelAndView("nombreArchivo")` coincida **exactamente en mayúsculas y minúsculas** con el archivo `.html` en `templates/`.
   * Si quieres recargar la tabla después de guardar o borrar, no pongas el nombre del HTML, pon `return new ModelAndView("redirect:/rutaDelControlador");`.

5. **Error 500 al abrir el formulario de crear (`/add`) o editar (`/update/{id}`)**:
   * Verifica que el nombre que pones en `mav.addObject("droga", droga)` sea exactamente el mismo que pones en el HTML en `<form th:object="${droga}">`.
   * Si tienes un desplegable `<select>`, acuérdate de mandar `mav.addObject("listararezas", rarezaService.findAll())` **TANTO en `/add` COMO en `/update/{id}`**.

---

## 4. CHULETA RÁPIDA DE SÍMBOLOS THYMELEAF

| Símbolo | Nombre | ¿Cuándo se usa? | Ejemplo |
| :--- | :--- | :--- | :--- |
| **`${...}`** | Dólar | Para leer cualquier variable enviada con `mav.addObject(...)` | `th:each="d : ${listadrogas}"` <br> `th:text="${d.nombre}"` |
| **`*{...}`** | Asterisco | **SOLO dentro de un `<form th:object="${...}">`** para enlazar los `<input>` y `<select>` a los atributos del objeto | `<input type="text" th:field="*{nombre}" />` |
| **`@{...}`** | Arroba | Para todas las rutas y enlaces (`th:href` y `th:action`) | `th:href="@{/drogas/delete/{id}(id=${d.id})}"` |
| **`?.`** | Interrogante | Para que no pete el HTML si un objeto hijo puede ser `null` | `th:text="${droga.rarezaDTO?.nombre}"` |

---

## 5. "QUÉ ME PUEDEN PEDIR EN EL EXAMEN" (Recetas listas para copiar y adaptar)

### Caso A: "Haz un buscador o filtro en la tabla (ej: filtrar drogas por tipo o por rareza)"
1. **En `DrogaService` y `DrogaServiceImpl`** (creas un método que filtre la lista):
   ```java
   public List<DrogaDTO> findByRareza(int idRareza) {
       List<DrogaDTO> todas = findAll();
       List<DrogaDTO> filtradas = new ArrayList<>();
       for (int i = 0; i < todas.size(); i++) {
           if (todas.get(i).getRarezaDTO().getId() == idRareza) {
               filtradas.add(todas.get(i));
           }
       }
       return filtradas;
   }
   ```
2. **En `DrogaController`** (añades la ruta para ver las drogas de una rareza):
   ```java
   @GetMapping("/porRareza/{idRareza}")
   public ModelAndView filtrarPorRareza(@PathVariable("idRareza") int idRareza) {
       ModelAndView mav = new ModelAndView("drogas"); // Reutilizas la misma tabla drogas.html
       List<DrogaDTO> listaFiltrada = drogaService.findByRareza(idRareza);
       mav.addObject("listadrogas", listaFiltrada); // Le pasas la lista filtrada con el mismo nombre
       return mav;
   }
   ```
3. **En `rarezas.html`** (añades un botón en la columna Acciones):
   ```html
   <a th:href="@{/drogas/porRareza/{id}(id=${rareza.id})}">Ver Drogas de esta Rareza</a>
   ```

---

### Caso B: "Si borras una Rareza, que se borren también sus Drogas (o no dejar borrarla)"
Si te piden que al borrar una rareza en `RarezaController` se borren en cascada las drogas que tengan esa rareza:
1. En `RarezaController` inyectas también `@Autowired private DrogaService drogaService;`.
2. En el método `delete` de `RarezaController`, antes de borrar la rareza, recorres las drogas y borras las que coincidan:
   ```java
   @GetMapping("/delete/{idRareza}")
   public ModelAndView delete(@PathVariable("idRareza") int idRareza) {
       // 1. Buscamos todas las drogas y borramos las que tengan esta rareza
       List<DrogaDTO> todasLasDrogas = drogaService.findAll();
       for (int i = 0; i < todasLasDrogas.size(); i++) {
           DrogaDTO d = todasLasDrogas.get(i);
           if (d.getRarezaDTO() != null && d.getRarezaDTO().getId() == idRareza) {
               drogaService.delete(d);
           }
       }

       // 2. Ahora ya borramos la rareza tranquilamente
       RarezaDTO rareza = new RarezaDTO();
       rareza.setId(idRareza);
       rarezaService.delete(rareza);

       return new ModelAndView("redirect:/rareza");
   }
   ```

---

### Caso C: "Pinta un mensaje o color distinto en el HTML según una condición (`th:if`)"
Si te piden en la tabla que si la duración es mayor de 300 ponga "Efecto Largo" y si no "Efecto Corto":
```html
<td>
    <span th:if="${droga.duracionEfecto >= 300}">Efecto Largo</span>
    <span th:if="${droga.duracionEfecto < 300}">Efecto Corto</span>
</td>
```
O si la lista está vacía, mostrar un aviso:
```html
<p th:if="${listadrogas.isEmpty()}">No hay ninguna droga registrada todavía.</p>
```
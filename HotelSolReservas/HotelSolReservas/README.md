# Hotel Sol — Sistema de consulta de habitaciones

Aplicación de escritorio de ejemplo desarrollada con **Java 21, JavaFX, FXML y XML**. Utiliza el fichero `reservas.xml` aportado para cargar las habitaciones del Hotel Sol.

## Funcionalidades
- Muestra las 30 habitaciones con número, tipo, planta, capacidad, precio por noche, estado, vista y accesibilidad.
- Busca una habitación por su número.
- Informa si la habitación está disponible, ocupada, en mantenimiento o si no existe.
- Resume el número de habitaciones por estado.
- Permite filtrar la tabla por estado o por número.
- Lee los datos desde XML; no requiere base de datos.

## Requisitos
- JDK 21 instalado.
- Apache Maven 3.9 o posterior.
- Un IDE compatible con Maven (IntelliJ IDEA, Eclipse o NetBeans) es opcional.

## Ejecutar desde terminal
1. Descomprime la carpeta del proyecto.
2. Abre una terminal en la carpeta donde está `pom.xml`.
3. Comprueba que Java y Maven estén instalados:
   ```bash
   java -version
   mvn -version
   ```
4. Ejecuta:
   ```bash
   mvn clean javafx:run
   ```

La primera ejecución necesita conexión a Internet para descargar las dependencias de JavaFX desde Maven Central.

## Abrir en un IDE
Abre/importa la carpeta como proyecto Maven y espera a que el IDE descargue las dependencias. Después ejecuta la clase `es.hotelsol.reservas.HotelSolApp` o usa el objetivo Maven `javafx:run`.

## Estructura
```text
HotelSolReservas/
├── pom.xml
├── README.md
└── src/
    └── main/
        ├── java/
        │   ├── module-info.java
        │   └── es/hotelsol/reservas/
        │       ├── HotelSolApp.java
        │       ├── Habitacion.java
        │       ├── HotelXmlService.java
        │       └── ReservasController.java
        └── resources/
            ├── css/estilos.css
            ├── data/reservas.xml
            └── es/hotelsol/reservas/reservas-view.fxml
```

## Pruebas sugeridas
- `111`: disponible.
- `101`: ocupada.
- `123`: en mantenimiento (no disponible).
- `999`: no existe.
- Deja el campo vacío o introduce letras para comprobar la validación.

## Nota sobre los estados
El XML incluye `disponible`, `ocupada` y `mantenimiento`. La aplicación conserva esos estados. Para mantenimiento, la búsqueda indica explícitamente que la habitación no está disponible.

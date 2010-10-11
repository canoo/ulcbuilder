ULCBuilder is a Groovy based builder for ULC powered applications.

ULCBuilder and Groovy makes possible the use of a DSL-like language
to defining the UI of an application, in the same vein as SwingBuilder
does for Swing.

ULCBuilder is extensible via builder plugins. There are two officially
supported plugins at the moment: miglayout and formlayout. These plugins
allow the usage of two powerful layout managers. Builder plugins can
be used by simply adding their respective jars to the application's
classpath. If you rely on Maven or Ivy then you can configure the following
repository to pull ULCBuilder and its dependencies

  https://ci.canoo.com/nexus/content/repositories/public-releases

group:    com.canoo.ulc
artifact: ulcbuilder-core
version   :1.0

group:    com.canoo.ulc
artifact: ulcbuilder-miglayout
version   :1.0

group:    com.canoo.ulc
artifact: ulcbuilder-formlayout
version   :1.0

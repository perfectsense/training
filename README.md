# Capacitación de Brightspot Express

¿Desea aprender cómo desarrollarse en Brightspot Express? ¡No busque más!

Este proyecto le brinda todo lo que necesita para comenzar a aprender cómo realizar desarrollos front-end y back-end en Brightspot Express. Incluye un proyecto ligero de arquetipo 4.0 y todas las plantillas principales de Styleguide para usar como referencia para ayudarlo a llevar sus propias necesidades de publicación web desde el concepto hasta la creación.

## Prerrequisitos

- VirtualBox: `5.0.10` - [Descargar](http://download.virtualbox.org/virtualbox/5.0.10/VirtualBox-5.0.10-104061-OSX.dmg)
- Vagrant: `1.8.1` - [Descargar](https://releases.hashicorp.com/vagrant/1.8.1/vagrant_1.8.1.dmg)
- Maven: `3.5.2` - [Descargar](https://archive.apache.org/dist/maven/maven-3/3.5.2/binaries/apache-maven-3.5.2-bin.zip)
- Brew (Homebrew para Mac) - Instale usando el siguiente comando:

```bash
/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
```

- Node `9.8.0` - Instale usando el siguiente comando (requiere `brew`):

```bash
brew install node
```

- Gulp: `3.9.1` - Instale usando el siguiente comando (requiere `npm`):

```bash
npm install gulp -g
```

- Yarn: `1.5.1` - Instale usando el siguiente comando (requiere `brew`):

```bash
brew install yarn
```

#### Para instalar archivos binarios que están comprimidos al descargarlos, siga los siguientes pasos:

Agregue su directorio local de archivos binarios a su variable de entorno PATH si no tiene t ya.

```bash
export PATH=$PATH:/usr/local/bin
```

Descomprima el paquete de binarios:

```bash
unzip {name-of-binaries-zip}.zip
```

Copie los binarios a su ubicación local:

```bash
cp path/to/binaries/binary /usr/local/bin/binary
```

Verifique que se encuentre el binario:

```bash
which binary
```

La salida debería ser `/usr/local/bin/binary`. Si dice que no se encontró el comando, intente reiniciar su terminal.

Brew también es útil para instalar  

## Preparando el proyecto local

En una Mac (las instrucciones de Windows se publicarán próximamente), abra la aplicación `Terminal`. Cambie su directorio de trabajo a una ubicación donde desee que el código del proyecto viva. Para este ejemplo, usaremos `~/Documents`.

```bash
cd ~/Documents
```

Clone el proyecto GitHub en su carpeta `~/Documents` usando el siguiente comando:

```bash
git clone git@github.com:perfectsense/training.git
```

Cambie su directorio para el proyecto:

```bash
cd training
```

## Creación del proyecto Por primera vez

Proyectos de Brightspot utilizan Maven para la gestión de dependencias y las configuraciones de compilación. Para compilar el proyecto por primera vez

```bash
mvn clean install
```

## Configurando el ambiente

Proyecto incluye Brightspot Training `Vagrantfile` en la raíz del proyecto. Este archivo contiene toda la configuración necesaria para ejecutar el Brightspot Training CMS de forma inmediata con datos publicados previamente para el sitio de ejemplo de Marvel Cinematic Universe.

Antes de iniciar el Vagrant ambiente, debe borrar su caché de caja para asegurarse de que está importando el cuadro de Brightspot Training más actualizado:

```bash
rm -rf ~/.vagrant.d/boxes/brightspot-training
```

Ahora, inicie vagrant:

```bash
vagrant up
```

Cuando inicie el Vagrant ambiente, intentará montar el directorio actual en el que el `Vagrantfile` encuentra en el directorio `/ vagrant` dentro del ambiente. Esto permitirá al usuario enlazar el archivo compilado del sitio `*.war` con la carpeta ` webapps` de Tomcat para permitir la implementación automática cada vez que se complete una compilación.

Para hacer esto, primero querrá SSH en el ambiente de entrenamiento. Desde la misma carpeta que su `Vagrantfile`, ejecute este comando:

```bash
vagrant ssh
```

Ahora se iniciará sesión como usuario root en el entrenamiento de vagrant. Si no lo está, inicie sesión como usuario root:

```bash
sudo -i
```

Detenga el servicio Brightspot antes de configurar la aplicación web:

```bash
sv stop brightspot
```

Ahora tendrá que crear un enlace simbólico al  `*.war` archivo en el directorio webapps del Tomcat:

```bash
ln -s /vagrant/site/target/bex-training-site-1.0.0-SNAPSHOT.war /servers/brightspot/webapps/ROOT.war
```

Iniciar la copia de seguridad del servicio Brightspot:

```bash
sv start brightspot
```

Durante el desarrollo, es posible que desee mantener el SSH en el entorno de entrenamiento con los registros abiertos para la depuración. Puede seguir los registros de Catalina y dejarlos abiertos en la ventana:

```bash
tail -f /servers/brightspot/logs/catalina.out
```

## Contribuir

Consulte [Construyendo El Training Vagrant](docs/BUILDING.md) sobre instrucciones e información sobre cómo contribuir al proyecto de capacitación.

## Créditos

Escrito y publicado por [Mark Conigliaro](https://github.com/markconigliaro1) (Ingeniero de software en Perfect Sense Digital)

Tema Brightspot Platform y Frost escrito y desarrollado por Perfect Sense Digital

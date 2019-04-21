# Product Launch Module

## Configuracion del Proyecto

Setting up the training project.

1. Clonar el repositorio en el directorio del proyecto

2. Para construir el proyecto ejecuta:

   ```bash
   mvn clean install
   ```

3. Para arrancar Vagrant ejecuta:

   ```bash
   vagrant up
   ```

4. SSH al ambiente de Vagrant, vaya al directorio webapps y copie el archivo war

   ```bash
   vagrant ssh 
   cd /servers/brightspot/webapps
   cp /vagrant/site/target/bex-training-site-1.0.0-SNAPSHOT.war ROOT.war
   ```

5. Actualice el archivo`/etc/hosts` para agregar el mapeo para el IP de Vagrant

   ```bash
   172.28.128.101  dev.training.com
   ```

Añadir un alias a bash profile 

```bash
alias vredeploy='vagrant ssh -c "sudo service brightspot stop && sudo cp /vagrant/site/target/*.war /servers/brightspot/webapps/ROOT.war && sudo rm -r /servers/brightspot/webapps/ROOT && sudo service brightspot start"'
```

Es similar a construir un Symlink asi

```bash
ln -s /vagrant/site/target/bex-training-site-1.0.0-SNAPSHOT.war /servers/brightspot/webapps/ROOT.war
```



```
function build {
  mvn clean -Plibrary verify
}
```

La opción `-Plibrary verify` ejecuta todo a través de nuestras definiciones de estilo de verificación para garantizar que el código esté más estandarizado (útil cuando muchos desarrolladores están trabajando en el mismo proyecto y para cambiar entre diferentes proyectos).

## Brief Overview

### 1. Tutorial de CMS

### 2. Estructura y oraganización del proyecto

### 3. Demo rápida de FE



## Pasos para desarrollar 'Product Launch Module'

### 1.) Crear un modelo de datos y una plantilla

En el Styleguide de la raíz, cree un modelo de datos inicial y una plantilla para el nuevo módulo.

En `/styleguide/core/product/` agregué `ProductLaunchModule.json` y `ProductLaunchModule.hbs`

**ProductLaunchModule.json**

```json
{
  "_template": "/core/product/ProductLaunchModule.hbs",
  "media": {
    "_template": "/core/image/Image.hbs",
    "image": {
      "_image": true
    }
  },
  "title": "{{words}}",
  "description": "{{words}}",
  "endDate": "04/01/2333 05:00:00 PM"
}
```



**ProductLaunchModule.hbs**

```handlebars
<div class="ProductLaunchModule">

  {{#with media}}
    <div class="ProductLaunchModule-media">{{this}}</div>
  {{/with}}

  {{#with title}}
    <div class="ProductLaunchModule-title">{{this}}</div>
  {{/with}}

  {{#with description}}
    <div class="ProductLaunchModule-description">{{this}}</div>
  {{/with}}

  {{#with endDate}}
    <div class="ProductLaunchModule-endDate">{{this}}</div>
  {{/with}}

</div>

```

En el Styleguide de raíz, también es necesario agregar una referencia al nuevo modulo en el `/styleguide/_group/Modules.json` archivó. Esto garantiza que las definiciones de interfaz de vista correctas se creen en el momento de la construcción, lo que permite que el modulo se coloque en otros campos de las paginas. Para este ejemplo, estoy agregando el modulo al campo `content` en una pagina. 

```json
{
 "_include": "/core/product/ProductLaunchModule.json"
}

```



**Page.json** en Styleguide de raíz

```json
{
  "_wrapper": false,
  "_template": "/core/page/Page.hbs",
  "language": "",
  "charset": "",
  "extraStyles": {
    "_include": "/_group/ExtraStyles.json"
  },
  "viewport": "width=device-width, initial-scale=1",
  "title": "Brightspot Express",
  "description": "Brightspot Express is...",
  "keywords": "Brightspot, Brightspot Express",
  "canonicalLink": "http://www.brightspot.com/",
  "meta": {
    "_include": "/_group/Metas.json"
  },
  "feedLink": "{{html('<link type=\"application/rss+xml\" rel=\"alternate\" title=\"Brightspot Express\" href=\"http://www.brightspot.com/feed.rss\">')}}",
  "jsonLinkedData": "{{html('{ \"@type\": \"WebPage\" }')}}",
  "manifestLink": "/manifest.json",
  "favicons": [
    {
      "_include": "Favicon.json",
      "rel": "apple-touch-icon",
      "href": "/apple-touch-icon.png"
    },
    {
      "_include": "Favicon.json"
    },
    {
      "_include": "Favicon.json",
      "href": "/favicon-16x16.png"
    }
  ],
  "extraScripts": {
    "_include": "/_group/ExtraScripts.json"
  },
  "extraBodyAttributes": {
    "_include": "/_group/ExtraBodyAttributes.json"
  },
  "extraBodyItems": {
    "_include": "/_group/ExtraBodyItems.json"
  },
  "hat": {
    "_include": "/core/page/PageHeaderTextHat.json"
  },
  "logo": {
    "_include": "/core/page/PageLogo.json"
  },
  "footerLogo": {
    "_include": "/core/page/PageLogo.json"
  },
  "banner": {
    "_include": "/_group/Banners.json"
  },
  "navigation": {
    "_include": "/core/navigation/Navigation.json"
  },
  "social": {
    "_include": "/core/social/SocialBar.json"
  },
  "sectionNavigation": {
    "_include": "/core/navigation/Navigation.json"
  },
  "packageNavigation": {
    "_include": "/core/navigation/Navigation.json"
  },
  "searchAction": "#",
  "above": {
    "_include": "/_group/Modules.json",
    "_key": 0
  },
  "pageHeading": "Brightspot Express",
  "pageLead": {
    "_include": "/_group/Leads.json",
    "_random": true
  },
  "pageSubHeading": "{{html('Lorem ipsum dolor sit amet, consectetur adipiscing elit. <a href=\"http://www.brightspot.com/\">Fusce ultrices</a> quis dui quis vehicula. Vivamus id lectus lacinia, volutpat nulla in, vehicula urna. Pellentesque consequat mauris ex, sed vulputate erat commodo et.')}}",
  "main": {
    "_include": "/_group/Modules.json",
    "_random": true
  },
  "aside": {
    "_include": "/_group/Modules.json",
    "_random": true
  },
  "below": {
    "_include": "/_group/Modules.json",
    "_random": true
  },
  "footerNavigation": {
    "_include": "/core/navigation/Navigation.json"
  },
  "footerContent": {
    "_include": "/_group/FooterTypes.json",
    "_key": 0
  },
  "disclaimer": "{{html('&copy; 2017 Perfect Sense. Site powered by our <a href=\"http://www.brightspot.com/\">Brightspot</a> publishing platform.')}}",
  "contentId": "a123",
  "actions": {
    "_include": "/core/action-bar/ActionBar.json"
  },
  "_hidden": false
}

```



El campo utilizado para el contenido principal de una pagina es

```json
"main": {
	"_include": "/_group/Modules.json",
	"_random": true
}

```





### 2.)  Añadir a JSON y plantilla a el Styleguide

A continuación, debemos agregar los archivos JSON y .hbs correspondientes a nuestro Styleguide de tema. Este tema se aplica a nuestro sitio en Brightspot, que es el responsable de la prestación del frente. El modelo y la plantilla en el Styleguide de la raíz se pueden heredar y diseñar o modificar más adelante en otros temas. La plantilla de el Stlyeguide de raíz del tema puede necesitar un marcado y un estilo adicionales en comparación con la plantilla de el Stlyeguide de la raíz.

En `/styleguide/core/product` he agregado **ProductLaunchModule.json** y **ProductLaunchModule.hbs**

**ProductLaunchModule.hbs**

```handlebars
<div class="ProductLaunchModule" {{#with endDate}} data-enddate="{{this}}" {{/with}}>

  {{#with media}}
    <div class="ProductLaunchModule-media">{{this}}</div>
  {{/with}}

  {{#with endDate}}
    <div class="ProductLaunchModule-endDate">
      <div id="ProductLaunchModule-days"></div>
      <div id="ProductLaunchModule-hours"></div>
      <div id="ProductLaunchModule-minutes"></div>
      <div id="ProductLaunchModule-seconds"></div>
    </div>
  {{/with}}

  {{#with title}}
    <div class="ProductLaunchModule-title">{{this}}</div>
  {{/with}}

  {{#with description}}
    <div class="ProductLaunchModule-description">{{this}}</div>
  {{/with}}

</div>

```



**ProductLaunchModule.json**

```json
{
  "_template": "/core/product/ProductLaunchModule.hbs",
  "media": {
    "_template": "/core/image/Image.hbs",
    "image": {
      "_image": true
    }
  },
  "title": "Suspendisse laoreet congue dolor",
  "description": "Suspendisse laoreet congue dolor ut tempus. Suspendisse sed purus at odio mattis tincidunt. Nullam enim mauris, mollis vel augue eget, faucibus sagittis est.",
  "endDate": "02/23/2019 07:05:18 PM"
}

```



### 3.) **Añadir** JS y Less

En el mismo directorio `/styleguide/core/product` en el tema también he añadido .less  y javascript archivos de estilo de módulo y agregar la funcionalidad para el temporizador de cuenta atrás.

**ProductLaunchModule.less**

```less
.ProductLaunchModule {

  box-shadow: 0 10px 20px 0 rgba(0, 0, 0, 0.06);
  background: #fff;
  width: 50%;
  margin: 0 auto;
  padding: 10px;

  &-media {
    display: block;
    margin: auto;
    width: 100%;
    padding: 20px;
  }

  &-title {
    font-size: 24px;
    color: rgb(42, 42, 42);
    text-align: center;
    padding: 10px 0;
  }

  &-description {
    font-size: 16px;
    color: rgb(42, 42, 42);
    text-align: center;
    padding: 10px 0;
  }

  &-endDate {
    text-align: center;

    > div {
      font-size: 16px;
      display: inline-block;
      padding: 10px 0;
      margin: 0 10px;
      font-weight: bold;
      flex-flow: column;
    }
  }

  #ProductLaunchModule-days:before { //lesshint idSelector: false
    content: 'Days';
    display: flex;
    font-size: 11px;
    font-weight: lighter;
  }

  #ProductLaunchModule-hours:before { //lesshint idSelector: false
    content: 'Hours';
    display: flex;
    font-size: 11px;
    font-weight: lighter;
  }

  #ProductLaunchModule-minutes:before { //lesshint idSelector: false
    content: 'Minutes';
    display: flex;
    font-size: 11px;
    font-weight: lighter;
  }

  #ProductLaunchModule-seconds:before { //lesshint idSelector: false
    content: 'Seconds';
    display: flex;
    font-size: 11px;
    font-weight: lighter;
  }

}

```



**ProductLaunchModule.js**

```javascript
export class ProductLaunchModule {

  constructor (el) {
    el.animate([
      {opacity: 0, easing: 'ease-out'},
      {opacity: 1}
    ], {
      duration: 3000,
      iterations: 1
    })
    var endDate = el.getAttribute('data-enddate')
    if (endDate) {
      this.countdown(endDate)
    }
  }

  countdown (endDate) {
    let days, hours, minutes, seconds

    endDate = new Date(endDate).getTime()

    if (isNaN(endDate)) {
      return
    }

    setInterval(calculate, 1000)

    function calculate () {
      let startDate = new Date()
      startDate = startDate.getTime()

      let timeRemaining = parseInt((endDate - startDate) / 1000)

      if (timeRemaining >= 0) {
        days = parseInt(timeRemaining / 86400)
        timeRemaining = (timeRemaining % 86400)

        hours = parseInt(timeRemaining / 3600)
        timeRemaining = (timeRemaining % 3600)

        minutes = parseInt(timeRemaining / 60)
        timeRemaining = (timeRemaining % 60)

        seconds = parseInt(timeRemaining)

        document.getElementById('ProductLaunchModule-days').innerHTML = parseInt(days, 10)
        document.getElementById('ProductLaunchModule-hours').innerHTML = ('0' + hours).slice(-2)
        document.getElementById('ProductLaunchModule-minutes').innerHTML = ('0' + minutes).slice(-2)
        document.getElementById('ProductLaunchModule-seconds').innerHTML = ('0' + seconds).slice(-2)
      } else {
        return
      }
    }
  }

}

```



### 4.) **Añadir** All.less

Para seguir los patrones y estructura del proyecto existente también he añadido un archivo de All.less `/styleguide/core/product`, que incluye una declaración de importación de **ProductLaunchModule.less**. En `/styleguide/core/` hay un **All.less** archivo que incluye una import de `/styleguide/core/product/All.less` (i.e. @import 'product/All').

Esta estructura mantiene las cosas fácilmente organizadas si se agregan más estilos en el directorio `/styleguide/core/product`

**All.less** en `/styleguide/core/product` directorio

```less
@import 'ProductLaunchModule';


```



**All.less** en `/styleguide/core` directorio

```less
@import 'action-bar/All';
@import 'article/All';
@import 'carousel/All';
@import 'container/All';
@import 'gallery/All';
@import 'link/All';
@import 'list/All';
@import 'navigation/All';
@import 'page/All';
@import 'promo/All';
@import 'quote/All';
@import 'search/All';
@import 'social/All';
@import 'tab/All';
@import 'text/All';
@import 'video/All';
@import 'product/All';


```



### 5.)  Actualización de All.js de raíz

En el directorio raíz tema Styleguide hay un **All.js** archivo que es donde el código JavaScript para  **ProductLaunchModule.js** se incluye.

**All.js** en `/styleguide`

```javascript
/* eslint-disable no-unused-vars */
// All.js
import $ from 'jquery'
import plugins from 'pluginRegistry'

import { psdToggler } from './util/psd-toggler'
import lazysizes from 'lazysizes'
import AnchorTabs from './core/tab/AnchorTabs.js'
import Carousel from './core/carousel/Carousel.js'
import { googleAnalytics } from 'googleAnalytics'
import LightboxGallery from './core/gallery/LightboxGallery'
import ListMasonry from './core/list/ListMasonry'
import { PlyrFunctions } from './core/video/PlyrFunctions.js'
import SearchOverlay from './core/search/SearchOverlay.js'
import { Tabs } from 'tabs'
import VideoEvents from './core/video/VideoEvents.js'
import VideoLead from './core/video/VideoLead.js'
import { ProductLaunchModule } from './core/product/ProductLaunchModule'

plugins.register(Tabs, '[data-widget=Tabs]')
plugins.register(PlyrFunctions, '[data-embeddedvideo-container]')
plugins.register(ProductLaunchModule, '.ProductLaunchModule')


```



### 6.) **Añadir cortar la imagen**

Siguiente para agregar un cultivo para la imagen en el módulo he añadido una entrada cultivos de imágenes en el tema **/styleguide/_config.json** archivo

Next to add a crop for the image in the module I've added an image crop entry in the theme's **/styleguide/_config.json** file

La entrada de definición de recorte de imagen es

```json
"productLaunchImage": {
  "width": 600,
  "height": 350
}


```



La configuración de recorte de imagen para este módulo es:

```json
"/core/product/ProductLaunchModule.hbs": {
  "media": {
    "/core/image/Image.hbs": {
      "image": "productLaunchImage"
    }
  }
}


```



Esta entrada especifica que el tamaño de recorte 'productLaunchImage' se aplica al campo 'imagen' de la plantilla **Image.hbs** que se usa para el campo 'media' de la plantilla **ProductLaunchModule.hbs**.

### 7.) **Genere el proyecto actualizado**

Después de agregar todo en la guía de estilo de raíz y tema, es necesario ejecutar la compilación maven para generar las clases de interfaz de vista y procesar y empaquetar todos los archivos less y javascript. El procesamiento de los archivos less y javascript se gestiona con gulpfile.js, así como con las dependencias de javascript. Después de que se realiza la creación de Maven, se crea un nuevo archivo 'ProductLaunchModuleView.java' que se utiliza para conectar el modelo y ver las clases del modelo a la plantilla. 

*After everything is added in the root and theme styleguide, it is necessary to run the maven build to generate the view interface classes and process and package up all the less and javascript files.  The processing of the less and javascript files are managed with gulpfile.js as well as any javascript dependencies. After the maven build is done a new file 'ProductLaunchModuleView.java' is created which is used to connect the model and view model classes to the template.*  

### 8.) **Agregar clase de módulo**

 A continuación, puedo agregar mi clase modelo que generará la IU para mi tipo de contenido en Brightspot.

*Next I can add my model class which will generate the UI for my content type in Brightspot.*

**ProductLaunchModule.java**

```java
package bex.training.core.product;

import java.util.Date;

import brightspot.core.image.ImageOption;
import brightspot.core.module.ModuleType;

public class ProductLaunchModule extends ModuleType {

    private String title;

    private String description;

    private ImageOption media;

    private Date endDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ImageOption getMedia() {
        return media;
    }

    public void setMedia(ImageOption media) {
        this.media = media;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}


```

ModuleType.java es una clase abstracta que se utiliza para todos los módulos que se pueden colocar en el campo '' contenidos en las páginas.

campo de contenido de 'OneOffPage.java'

privado Lista <ModuleType> contenidos;

*ModuleType.java is an abstract class which is used for all of the modules which can be placed in the 'contents' field on Pages.* 

*contents field from 'OneOffPage.java'*

*private List<ModuleType> contents;*



### 9.) Add Module ViewModel

Siguiente. Necesito agregar mi ProductLaunchModuleViewModel que se usa para conectar el modelo al renderizador. El modelo de vista a veces contiene lógica adicional para modificar los datos provenientes del modelo para que la plantilla pueda utilizarlos correctamente. ProductLaunchModuleViewModel amplía ViewModel, que acepta un parámetro de tipo del modelo que vincula el modelo de vista con el modelo. El modelo de vista implementa la clase ProductLaunchModuleView que conecta la plantilla con el modelo de vista.

 *Next I need to add my ProductLaunchModuleViewModel which is used to wire up the model to the renderer. The view model sometimes contains additional logic for modifying the data coming from the model so that it can be used correctly by the template.  ProductLaunchModuleViewModel extends ViewModel which accepts a type parameter of the model binding the view model to the model.  The view model implements the ProductLaunchModuleView class which connects the template to the view model and model.*

**ProductLaunchModuleViewModel.java**

```java
package bex.training.core.product;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.psddev.cms.view.ViewModel;
import com.psddev.styleguide.core.product.ProductLaunchModuleView;
import com.psddev.styleguide.core.product.ProductLaunchModuleViewMediaField;

public class ProductLaunchModuleViewModel extends ViewModel<ProductLaunchModule> implements ProductLaunchModuleView {

    @Override
    public CharSequence getTitle() {
        return model.getTitle();
    }

    @Override
    public CharSequence getDescription() {
        return model.getDescription();
    }

    @Override
    public Iterable<? extends ProductLaunchModuleViewMediaField> getMedia() {
        return createViews(ProductLaunchModuleViewMediaField.class, model.getMedia());
    }

    @Override
    public CharSequence getEndDate() {

        if (model.getEndDate() != null) {
            String endDateFormat = "MM/dd/yyyy hh:mm:ss a";
            DateFormat df = new SimpleDateFormat(endDateFormat);
            return df.format(model.getEndDate());
        }

        return null;
    }
}


```



### 10.) **Volver a implementar archivo de War**

pasada que necesitamos para volver a ejecutar 'mvn instalación limpia' y luego reploy el archivo de la guerra en el directorio / webapps en el servidor para actualizar el código. El módulo ahora debería estar listo para programar en Brightspot.

  *Last we need to rerun 'mvn clean install' and then reploy the war file to the /webapps directory on the server to update the code.  The module should now be ready to program in Brightspot.* 

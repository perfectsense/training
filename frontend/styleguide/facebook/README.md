# Facebook Components

## [Facebook Javascript SDK](https://developers.facebook.com/docs/javascript/quickstart)

The Facebook SDK for JavaScript provides a rich set of client-side functionality that:

* Enables you to use the Like Button and other Social Plugins on your site.
* Enables you to use Facebook Login to lower the barrier for people to sign up on your site.
* Makes it easy to call into Facebook's Graph API.
* Launch Dialogs that let people perform various actions like sharing stories.

To include the SDK, you will need to create an Application in [Facebook's App Dashboard](https://developers.facebook.com/apps) and get the App ID value.

Next, you will need to include the javascript on your page. We provide a utility for doing so. Facebook recommends adding this `<script` directly after the opening `<body>`.

Example: 

```
public class PageViewModel extends ViewModel<Foo> implements PageView {

    @Override
    public Object getHead() {
        return new HeadView() {

            @Override
            public Collection<?> getItems() {
                Collection<Object> items = new ArrayList<>();
                items.add(new FacebookJavascriptSdkView.Builder()
                        .facebookAppId("YOUR APP ID")
                        .build());
                return items;
            }

            ...
        };
    }

    ...
}
```

## [Facebook Share Button](https://developers.facebook.com/docs/plugins/share-button)

To use the Facebook share plugin you will need to include the Facebook Javascript SDK. Follow the section above for details on how to include it.

### Option 1: Facebook Provided Share Button

The share button provided by facebook provides standard sharing functionality with a few options for customization.

![image](https://cloud.githubusercontent.com/assets/1299507/17573490/a4f1e24e-5f28-11e6-8543-69fa40d98199.png)

Example:

```
public class PageViewModel extends ViewModel<Foo> implements PageView {

    @Override
    public Object getBody() {
        return new FacebookShareButtonView.Builder()
                .size("large")
                .layout("button")
                .href("http://brightspot.com")
                .build();
    }

    @Override
    public Object getHead() {
        return new HeadView() {

            @Override
            public Collection<?> getItems() {
                Collection<Object> items = new ArrayList<>();
                items.add(new FacebookJavascriptSdkView.Builder()
                        .facebookAppId("YOUR APP ID"));
                return items;
            }

            ...
        };
    }

    ...
}
```

### Option 2: Custom Share Button

The Facebook Share Button above provides limited customization. However, Facebook also offers an alternative which allows you to implement your own button
using the [Web Share Dialog](https://developers.facebook.com/docs/sharing/reference/share-dialog#advancedtopics). This option allows you to implement your own styles
for the button. In your `ViewModel` you can implement the following logic to create your button View.

Example:

```
new FacebookShareDialogButtonView.Builder()
                .body("Share")
                .href(StringUtils.encodeUri("http://brightspot.com"))
                .facebookAppId("YOUR APP ID")
                .display("popup")
                .build();
```

## [Facebook Post Embed](https://developers.facebook.com/docs/plugins/embedded-posts)

![image](https://cloud.githubusercontent.com/assets/1299507/21621964/5eb6b962-d1c9-11e6-8f81-b5c2b06fd503.png)

Embedded Posts are a simple way to put public posts - by a Page or a person on Facebook - into the content of your web site or web page. Only public posts from Facebook Pages and profiles can be embedded.

Example implementation:

```
    new FacebookPostEmbedView.Builder()
            .href("https://www.facebook.com/20531316728/posts/10154009990506729/")
            .width(500)
            .build();
```

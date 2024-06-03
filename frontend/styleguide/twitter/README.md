# Twitter Components

## [Twitter Cards](https://dev.twitter.com/cards/overview)

Similar to the [Open Graph Protocol](http://ogp.me/), but used specifically by Twitter. These are designed to improve engagement with your website's Twitter content.

### Examples

Four types of cards are available:

[Summary Card](https://dev.twitter.com/cards/types/summary)

![image](https://cloud.githubusercontent.com/assets/1299507/17494758/1e435f165d8411e685cf0965fe3f4e65.png)

[Summary Card with Large Image](https://dev.twitter.com/cards/types/summarylargeimage)

![image](https://cloud.githubusercontent.com/assets/1299507/17494785/370400fa5d8411e697c7952655e170bc.png)

[Player Card](https://dev.twitter.com/cards/types/player)

![image](https://cloud.githubusercontent.com/assets/1299507/17494821/63dcf7805d8411e68cd6d4d5d0095419.png)

[App Card](https://dev.twitter.com/cards/types/app)

![image](https://cloud.githubusercontent.com/assets/1299507/17494848/7f3c0d045d8411e690ba86ca72847278.png)

### Implementation

To implement Twitter Cards you will need to include one of the available Twitter Card.

**Please note that a web page may only be decorated for one card type**

Example:

```
public class PageViewModel extends ViewModel<Article> implements PageView {

    @Override
    public Object getHead() {
        return new HeadView() {

            @Override
            public Collection<?> getItems() {
                Collection<Object> items = new ArrayList<>();
                items.add(new TwitterSummaryWithLargeImageCardView.Builder()
                    .site("brightspotcms")
                    .creator("@brightspotcms")
                    .description(model.getHeadline())
                    .title(model.getHeadline())
                    .build());

                return items;
            }

            ...
        };
    }

    ...
}
```

## [Tweet/Share Button](https://dev.twitter.com/web/tweetbutton)

Two options are available for Tweet/Share buttons.

#### Option 1: Default Tweet Button (Twitter provides styling and functionality)

![image](https://cloud.githubusercontent.com/assets/1299507/17569292/987e19aa5f1511e6892a975d91f0d57b.png)

The default tweet button is styled and maintained by twitter. It is controlled by the twitter widget javascript. To use the default Tweet Button, you will need to include the twitter widgets javascript and utilize the `TwitterTweetButtonView.Builder`. You can specify the information for twitter to use through various dataattributes.

Example:

```
public class PageViewModel extends ViewModel<Article> implements PageView {

    @Override
    public Object getBody() {

        // Use Builder to create View
        return new TwitterShareButtonView.Builder()
                .text("predefined text to tweet")
                .hashtags("comma,delimited,hashtags")
                .lang("en")
                .size("large")
                .build()

    }

    @Override
    public Object getHead() {
        return new HeadView() {

            @Override
            public Collection<?> getItems() {
                Collection<Object> items = new ArrayList<>();
                items.add(getSummaryCard());

                // Include Twitter Widgets Javascript
                items.add(new TwitterWidgetsJavaScriptView.Builder().build());
                return items;
            }

            ...
        };
    }

    ...
}
```


#### Option 2: Custom Tweet Button

For additional customization of the Tweet Button, you can implement the link to the [Tweet Web Intent](https://dev.twitter.com/web/tweetbutton/webintent) using the `TwitterWebIntentButton.Builder`.

Example:

```
    new TwitterTweetIntentButtonView.Builder()
            .intent("tweet")
            .queryString("?url=http%3A%2F%2Fbrightspot.com&hashtags=cms&text=foo&hashtags=foo,bar&screen_name=brightspotcms&via=brightspotcms&related=brightspotcms")
            .target("_blank")
            .build();
```



## [Embedded Single Tweet](https://dev.twitter.com/web/embeddedtweets)

Twitter's Tweets support the [oEmbed](http://oembed.com/) protocol for embedding tweets on your website.

### Implementation

Embedding tweets in your page requires two steps, and an optional third step for customization.

1. Include Twitter's Widget js in your page using TwitterWidgetJavascriptView.

Example:

```
public class PageViewModel extends ViewModel<Foo> implements PageView {

    ...

    @Override
    public Object getHead() {
        return new HeadView() {

            @Override
            public Collection<?> getItems() {
                Collection<Object> items = new ArrayList<>();
                items.add(new TwitterWidgetsJavaScriptView.Builder().build());
                return items;
            }

            ...
        };
    }
}
```

2. Generate the HTML for the Oembed version of the Tweet, this can be done using Brightspot CMS's [ExternalContent#getResponse](https://github.com/perfectsense/brightspotcms/blob/release/3.2/db/src/main/java/com/psddev/cms/db/ExternalContent.java#L28).

Example:

```
public class PageViewModel extends ViewModel<Foo> implements PageView {

    @Override
    public Object getBody() {

        ExternalContent tweet = new ExternalContent();
        tweet.setUrl("https://twitter.com/BrightspotCMS/status/718177471879884800");
        tweet.setMaximumHeight(200);
        tweet.setMaximumWidth(200);

        return new RawHtmlView.Builder()
                .html(tweet.getResponse().get("html"))
                .build();
    }

    @Override
    public Object getHead() {
        return new HeadView() {

            @Override
            public Collection<?> getItems() {
                Collection<Object> items = new ArrayList<>();
                items.add(new TwitterWidgetsJavaScriptView.Builder().build());
                return items;
            }

            ...
        };
    }
}
```

3. Optionally style/theme the embed using TwitterWidgetsThemeOptions.

Example:

```
public class PageViewModel extends ViewModel<Foo> implements PageView {

    @Override
    public Object getBody() {

        ExternalContent tweet = new ExternalContent();
        tweet.setUrl("https://twitter.com/BrightspotCMS/status/718177471879884800");
        tweet.setMaximumHeight(200);
        tweet.setMaximumWidth(200);

        return new RawHtmlView.Builder()
                .html(tweet.getResponse().get("html"))
                .build();
    }

    @Override
    public Object getHead() {
        return new HeadView() {

            @Override
            public Collection<?> getItems() {
                Collection<Object> items = new ArrayList<>();
                items.add(new TwitterWidgetsJavaScriptView.Builder().build());
                items.add(new TwitterWidgetsWebpagePropertiesView.Builder()
                        .theme("dark")
                        .borderColor("#ff0000")
                        .linkColor("#ff0000")
                        .build());
                return items;
            }

            ...
        };
    }
}

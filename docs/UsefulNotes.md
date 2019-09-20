# Useful Notes

## Explicit War File Deploy:

Examples from other Projects:
```
service brightspot stop && cp /Users/ryanbannon/new-atlas/site/target/new-atlas-site-1.0.0-SNAPSHOT.war /servers/brightspot/webapps/ROOT.war && rm -rf /servers/brightspot/webapps/ROOT && service brightspot restart && tail -f /servers/brightspot/logs/catalina.out
```

```
service golf-advisor stop && cp /Users/ryanbannon/golf-advisor/site/target/golfadvisor-site-1.0.0-SNAPSHOT.war /servers/golf-advisor/webapps/ROOT.war && rm -rf /servers/golf-advisor/webapps/ROOT && service golf-advisor restart && tail -f /servers/golf-advisor/logs/catalina.out
```

Example (template, need to add in your specific variables) for this project:

```
service brightspot stop && cp /Users/YOUR_MAC_USER_NAME/YOUR_TRAINING_REPO_NAME/site/target/new-atlas-site-1.0.0-SNAPSHOT.war /servers/brightspot/webapps/ROOT.war && rm -rf /servers/brightspot/webapps/ROOT && service brightspot restart && tail -f /servers/brightspot/logs/catalina.out
```

If you get an error that your vagrant can't find your war file, your directory may be mounted in `/vargant/` instead of `/Users/`.

## Docs Link:

[BSP Docs](http://docs.brightspot.com/)

## Brightspot Gartner Review Link

https://gtnr.it/2NnjxM0



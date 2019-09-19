# Useful Notes

## Explicit War File Deploy:

```
service brightspot stop && cp /Users/ryanbannon/new-atlas/site/target/new-atlas-site-1.0.0-SNAPSHOT.war /servers/brightspot/webapps/ROOT.war && rm -rf /servers/brightspot/webapps/ROOT && service brightspot restart && tail -f /servers/brightspot/logs/catalina.out
```

```
service golf-advisor stop && cp /Users/ryanbannon/golf-advisor/site/target/golfadvisor-site-1.0.0-SNAPSHOT.war /servers/golf-advisor/webapps/ROOT.war && rm -rf /servers/golf-advisor/webapps/ROOT && service golf-advisor restart && tail -f /servers/golf-advisor/logs/catalina.out
```
## Docs Link:

[BSP Docs](http://docs.brightspot.com/)

restore db: https://raw.githubusercontent.com/perfectsense/brightspot-vagrant/4a06cb17595a332ab8f25d6e33e9104f60d81f39/ops/chef/files/default/restore_modified.rb


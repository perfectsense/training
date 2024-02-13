---
title: Testing the Backend
grand_parent: Meal Exercise
parent: Backend Implementation
nav_order: 4
permalink: /meal-exercise/backend-testing.html
---

# Testing the Backend

You should always test your work, even if all the pieces aren’t in place yet. In our case, we haven’t completed the bundle work yet but we can still test what we’ve made so far.

Publish out a Recipe and a Meal containing that recipe (or see the [Testing in Docker](docker-testing.html) section where there are links to a more in-depth example you can publish). If you try to load the Meal page you’ll get a blank page as there’s nothing in the hbs file in the bundle. However, if you append `?_renderer=json` to the url, Brightspot will render the json generated from your viewmodels so you can see the data the backend is sending to the frontend. Scroll through it to see if you can recognize what’s coming from your viewmodels. Note that there will be a lot of other data in there, which is coming from AbstractContentPageViewModel and AbstractPageViewModel.

# Stock Hawk

### Overview

The purpose of this project is to incorporate changes from mock user feedback into an existing application.

### Changes

> "Right now I can't use this app with my screen reader. My friends love it, so I would love to download it, but the buttons don't tell my screen reader what they do."

+ Added `contentDescription` attribute to buttons and items within the stock list. 

> "We need to prepare Stock Hawk for the Egypt release. Make sure our translators know what to change and make sure the Arabic script will format nicely."

+ Added `translatable="false"` attribute to any strings that were used as keys. 
+ Created `values-ar` folder with `strings.xml` containing loosely translated arabic strings for testing.

> "Stock Hawk allows me to track the current price of stocks, but to track their prices over time, I need to use an external program. It would be wonderful if you could show more detail on a stock, including its price over time."

+ `StockDetail` activity created to display historical data using [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart) library.

> "I use a lot of widgets on my Android device, and I would love to have a widget that displays my stock quotes on my home screen."

+ TODO

> "I found a bug in your app. Right now when I search for a stock quote that doesn't exist, the app crashes."

+ Crash occurred when calling `quote.getPrice().floatValue()`. The object returned from `getPrice()` was null and `floatValue()` was called on a null reference. 
+ Logic was added to the `QuoteSyncJob` class to check if the stock is null before attempting to extract the pricing information. 

> "When I opened this app for the first time without a network connection, it was a confusing blank screen. I would love a message that tells me why the screen is blank or whether my stock quotes are out of date."

+ TODO

### Screenshots

![List](screenshots/list.png?raw=true) ![Add](screenshots/add.png?raw=true) ![Chart](screenshots/chart.png?raw=true)
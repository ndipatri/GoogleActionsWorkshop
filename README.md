# GoogleActionsWorkshop

Words have power! In this workshop, we’ll step outside of our Android comfort zone and create 
a conversational user experience using the Google Assistant running on our phones. 

We’ll manipulate the world around us with Internet of Things (IoT) hardware.  

Get ready to learn a taste of Kotlin-based Google Actions fulfillment with Google’s AppEngine, 
the Particle Photon Arduino platform and possibly Newton’s Laws of Motion as they pertain 
to Ping Pong balls! (BYOL - bring your own laptop)

 
### Things you can do before you come to the workshop 

During the workshop you will using the Google Cloud Platform. You will need to log in to
the Google Cloud Platform (https://console.cloud.google.com) using your Google account.

We'll be going over all of this during the workshop, but in the interest of saving the meager WiFi 
bandwidth that will be available, please feel free to install (or at least download) the following 
on your device ahead of time. The commands shown below are for the Mac:


* __HomeBrew__ - OSX build environment

```>/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)```

* __XCode__ - OSX build tools

```>xcode-select --install```

* __GIT__ - source control management

```>brew install git```

* __Gradle__ - build system

```>brew install gradle```

* __curl__ - command line web request tool

```>brew install curl```

* __Google Cloud Platform CLI__ - command line tools needed for pushing to AppEngine

     [Download](https://dl.google.com/dl/cloudsdk/channels/rapid/downloads/google-cloud-sdk-238.0.0-darwin-x86_64.tar.gz)
     
     [Installation Instructions](https://cloud.google.com/sdk/?hl=en_US&_ga=2.132115275.-514248634.1552053382&_gac=1.118830587.1552132197.Cj0KCQiA5Y3kBRDwARIsAEwloL5wCXwsyqONoluPWbThHNH8Vyo2w5ga_Y7ngqJpLhNajtnjTNNn8XIaAs16EALw_wcB)
     
* __IntelliJ__ - Java (and therefore Kotlin) build environment.  This will serve our needs better than 
Android Studio as Google Actions are not based on Android.

     [Download](https://www.jetbrains.com/idea/download/download-thanks.html?platform=mac)



### PARTS LIST

All necessary hardware (except for your laptop) will be provided at the workshop, but in case you want to 
build any of these components after you leave the workshop, here is a parts list with Amazon purchase links:


[Micro Servo](https://www.amazon.com/gp/product/B072V529YD/ref=ppx_yo_dt_b_asin_title_o04_s00?ie=UTF8&psc=1)

[Particle Photon IoT Dev Kit](https://store.particle.io/collections/photon)

[Breadboard Jumper Wires](https://www.amazon.com/Solderless-Flexible-Breadboard-Jumper-100pcs/dp/B005TZJ0AM/ref=sr_1_6?crid=2WX9X7QGL90SR&keywords=breadboard+jumper+wires&qid=1552495713&s=gateway&sprefix=breadboard+ju%2Caps%2C123&sr=8-6)

[Hot Glue Sticks](https://www.amazon.com/gp/product/B07FBDPWPV/ref=ox_sc_act_title_1?smid=ATVPDKIKX0DER&psc=1)

[⅜” x 12” wooden dowels](https://www.amazon.com/gp/product/B01BG8A8K6/ref=ppx_yo_dt_b_asin_title_o07_s01?ie=UTF8&psc=1)

[Popsicle Sticks](https://www.amazon.com/gp/product/B009EE38UM/ref=ppx_yo_dt_b_asin_title_o08_s00?ie=UTF8&psc=1)

[Rubber Bands](https://www.amazon.com/gp/product/B0787YYKLJ/ref=ppx_yo_dt_b_asin_title_o08_s01?ie=UTF8&psc=1)

[Ping Pong Balls](https://www.amazon.com/gp/product/B06XWNPM3H/ref=ppx_yo_dt_b_asin_title_o08_s01?ie=UTF8&psc=1)

[3” Zip Ties](https://www.amazon.com/400-Piece-Contractor-Multi-Purpose-Organizing/dp/B077TJ11JR/ref=lp_17347419011_1_3?srs=17347419011&ie=UTF8&qid=1552503529&sr=8-3&th=1)

[PLA 3D printer filament](https://www.amazon.com/gp/product/B07H9BFNS5/ref=ppx_yo_dt_b_asin_title_o09_s01?ie=UTF8&psc=1)

Here are [details instructions](https://slides.com/ndipatri/deck-15/fullscreen?token=nDQ96Z1a) on how to actually build one of these catapults. Please keep in
mind these instructions were originally created for 10 year olds :-)  You will need a hot glue gun!



### NOTE ON CREATING BILLING ACCOUNT

Near the end of this workshop, you will be using your voice to control some fun Ping Pong-related
toys.  Your custom Google Action will be making an output HTTP call to a webhook.  Most cloud platforms
require some form of verification before they let you do this.  Google uses your credit card.  So if you 
really want to have fun with your new Google Actions you're going to need to create a Google billing account.

You will be using your own laptop for this workshop.  Although I will go through the process of
creating the billing account for GCP during the workshop, you can also do this ahead of time. 

Start by logging in to the [Google Cloud Platform (GCP) console](https://console.cloud.google.com).

Once you are in the GCP console, click on the **Billing** tab on the left hand side and 
follow instructions.

The reason Google asks you to provide your credit card is because they give all GCP accounts
a $300 credit and they don't want people making multiple Google accounts to accumulate this
credit.  For our workshop, you will not be using any of this credit nor will you be 
charging your credit card.  Even after a year, your credit card will not be charged. 

Do not click the 'Upgrade' link at the top of the GCP console.  This will ensure your credit
card will never be charged. (and even then, it would only be charged if you used all your credit).

Also, **be aware that you are providing only Google your credit card on your laptop over HTTPS**, 
not to anyone at the conference nor to anyone associated with the conference. And, again, you can 
do this before attending the workshop.

If all this has scared you away from creating a GCP account, that's ok :-) You can still attend
the workshop and get through 95% of the content.  You just won't be able to shoot Ping Pong balls
with your voice :-(


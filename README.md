# GoogleActionsWorkshop

Words have power! In this workshop, we’ll step outside of our Android comfort zone and create a conversational user experience using the Google Assistant running on our phones. We’ll manipulate the world around us with Internet of Things (IoT) hardware.  

Get ready to learn a taste of Kotlin-based Google Actions fulfillment with Google’s AppEngine, the Particle Photon Arduino platform and possibly Newton’s Laws of Motion as they pertain to Ping Pong balls! (BYOL - bring your own laptop)

 
***IMPORTANT PRE-REQUISITE FOR THIS WORKSHOP***

During the workshop you will using the Google Cloud Platform. You will need to log in to
the Google Cloud Platform (https://console.cloud.google.com) using your Google account.
In addition, you will need to create a billing account using an active credit card.
 
See **NOTE** below for more details on why you will need to do this.

Once you are in the GCP console, click on the **Billing** tab on the left hand side and 
follow instructions. You can do this step before the workshop or during.


***DEVELOPMENT PLATFORM SETUP***

We'll be going over all of this during the workshop, but in the interest of saving the meager WiFi 
bandwidth that will be available, please feel free to install (or at least download) the following on 
your device ahead of time. The commands shown below are for the Mac:


* __HomeBrew__ - OSX build environment

```>/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)```

* __XCode__ - OSX build tools

```>xcode-select --install```

* __GIT__ - source control management

```>brew install git```

* __Gradle__ - build system

```./brew install gradle```

* __curl__ - command line web request tool

```>brew install curl```

* __Google Cloud Platform CLI__ - command line tools needed for pushing to AppEngine

     [Download](https://dl.google.com/dl/cloudsdk/channels/rapid/downloads/google-cloud-sdk-238.0.0-darwin-x86_64.tar.gz)
     
     [Installation Instructions](https://cloud.google.com/sdk/?hl=en_US&_ga=2.132115275.-514248634.1552053382&_gac=1.118830587.1552132197.Cj0KCQiA5Y3kBRDwARIsAEwloL5wCXwsyqONoluPWbThHNH8Vyo2w5ga_Y7ngqJpLhNajtnjTNNn8XIaAs16EALw_wcB)
     
* __IntelliJ__ - Java build environment.  This will serve our needs better than Android Studio.

     [Download](https://www.jetbrains.com/idea/download/download-thanks.html?platform=mac)



***NOTE ON CREATING BILLING ACCOUNT:*** 

You will be using your own laptop for this workshop.  Although I will go through the process of
creating the billing account for GCP during the workshop, you can also do this ahead of time. 
The reason Google asks you to provide your credit card is because they give all GCP accounts
a $300 credit and they don't want people making multiple Google accounts to accumulate this
credit.  For our workshop, you will not be using any of this credit nor will you be 
charging your credit card.  Even after a year, your credit card will not be charged. 

Do not click the 'Upgrade' link at the top of the GCP console.  This will ensure your credit
card will never be charged. (and even then, it would only be charged if you used all your credit).

Also, **be aware that you are providing only Google your credit card on your laptop over HTTPS**, 
not to anyone at the conference nor anyone associated with the conference. And, again, you can 
do this before attending the workshop.

If all this has scared you away from creating a GCP account, that's ok :-) You can still attend
the workshop and listen in and enjoy but you won't be able to create a Google Action using GCP
and therefore will not be able to launch a Ping Pong Catapult :-(

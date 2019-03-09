'use strict';

const functions = require('firebase-functions');

const {
  dialogflow,
  BasicCard,
  Permission,
  OptionItems,
  GoogleActionsV2OptionInfo,
  CarouselOptionItem,
  Carousel,
  Suggestions,
  Image
} = require('actions-on-google');

const Particle = require('particle-api-js');
const https = require('https');
const querystring = require('querystring');

const app = dialogflow({debug: true});
exports.roboButton = functions.https.onRequest(app);

// The following are intents that help with basic conversational
// issues (welcome, user persistence, no-response, etc.)

// The welcome intent has a follow-up intent.. so we can present Suggestions
app.intent('Default Welcome Intent', (conv) => {
  const name = conv.user.storage.userName;
  if (!name) {
    // Asks the user's permission to know their name, for personalization.
    conv.ask(new Permission({
      context: 'Hi there, to get to know you better',
      permissions: 'NAME',
    }));
  } else {
    return initializeConversation(conv, name);
  }
 });

function initializeConversation(conv, name) {
    const sessionParticle = new Particle()

    // initialize session with Particle endpoint for
    // quering/controller buttons
    return sessionParticle.login({
      username: 'ndipatri@gmail.com',
      password: ''
    })

    .then(particleToken => {
      var particleSessionToken = particleToken.body.access_token;

      // Store particle access token for subsequent intent processing ...
      conv.user.storage.particleSessionToken = particleSessionToken;

      return new Promise((resolve, reject) => {
        // Retrieve all buttons for use during conversation
        resolve(sessionParticle.listDevices({auth: particleSessionToken}))
      });
    })

    .then(buttonData => {

      // we only care about connected buttons
      var connectedButtonData = []

      // we're going to ask Particle for the state of all connected buttons
      var buttonStateRequests = []

      for (var index = 0; index<buttonData.body.length; index++) {
        if (buttonData.body[index].connected) {
          connectedButtonData.push(buttonData.body[index])

          buttonStateRequests.push(
            sessionParticle.getVariable({
              deviceId: buttonData.body[index].id,
              name: 'state',
              auth: conv.user.storage.particleSessionToken
            })
          );
        }
      }
      
      // Store connected button info for subsequent intent processing within
      // this conversation ...
      conv.user.storage.connectedButtonData = connectedButtonData;

      return new Promise((resolve, reject) => {
        resolve(Promise.all(buttonStateRequests))
      });
    })

    .then(connectedButtonsState => {

      console.log('connectedButtonsState: ', connectedButtonsState);

      var connectedButtonData = conv.user.storage.connectedButtonData;
      // assign each resulting 'state' variable to associated connected button
      for (var index = 0; index<connectedButtonData.length; index++) {
          var connectedButton = connectedButtonData[index]

          // 'result' field has format: '::off::spst::'
          var state = connectedButtonsState[index].body.result.split('::')[1]

          connectedButton.state = state
      }
      console.log('connectedButtonData: ', connectedButtonData);

      // ok, now all button data has been loaded. We can proceed with
      // conversation...

      return new Promise((resolve, reject) => {
        conv.ask(`Hi again, ${name}. What would you like to do?`);

        // These suggestions are input for Welcome follow-up intent:
        // 'Default Welcome Intent - custom'
        resolve(conv.ask(new Suggestions('control button', 'list buttons')));
      })
    })
}

// Handle the Welcome follow-up intent
// 'command' should be either 'list' or 'control'
app.intent('Default Welcome Intent - custom', (conv, { command }) => {
  return new Promise((resolve, reject) => {

    var connectedButtonData = conv.user.storage.connectedButtonData;

    // regardless of the command, we need the list of available buttons
    // as a string ...
    var connectedButtonsMsg = '';
    var firstButtonExampleText = null;
    for (var index = 0; index < connectedButtonData.length; index++) {
      var buttonName = connectedButtonData[index].name

      buttonName = buttonName.replace('_', ' ');

      if (!firstButtonExampleText) {
        firstButtonExampleText = "'Turn on " + buttonName + "'";
      }

      connectedButtonsMsg += " '" + buttonName + "' ";
    }

    if (connectedButtonsMsg) {
      if (command === 'list') {
        if (!conv.screen) {
          var message = "Choose from one of these connected buttons:\n" + connectedButtonsMsg + ".\n"
          message += "You can say things like " + firstButtonExampleText
          resolve(conv.ask(message)); // conversation will continue
        } else {
          resolve(conv.ask(`Choose from the following options ...`, generateCarousel(connectedButtonData)));
        }
      } else if (command === 'control') {
        message = "To control buttons,\n"
        message += "you can say things like " + firstButtonExampleText
        resolve(conv.ask(message)); // conversation will continue
      } else {
        resolve(conv.ask("I don't understand. Pleaase try again!")); // conversation will continue
      }
    } else {
      message = 'Sorry, you have no connected buttons at this time.  Goodbye!'
      resolve(conv.close(message)); // conversation ended
    }
  });
});

// This intent is triggered either by speaking 'Turn on box button' or by making
// a selection from Button Carousel (which emits an OPTION)
app.intent('control button', (conv, { button_name, state }) => {
  var optionArg = conv.arguments.get('OPTION')

  if (optionArg) {
    // This intent was triggered with OPTION event
    // <state> <button_name> is OPTION format
    var options = optionArg.split(' ');
    state = options[0]
    button_name = options[1]
  }

  if (!state) {
    // tell the user we don't know how to proceed if there's no ledState parameter
    return new Promise((resolve, reject) => {
      resolve(conv.close("I don't understand"));
    });
  }

  if (state !== 'on' && state !== 'off') {
    return new Promise((resolve, reject) => {
      resolve(conv.close(`I don't know how to turn the ${buttonName} ${state}`));
    });
  }

  var connectedButtonData = conv.user.storage.connectedButtonData;

  const sessionParticle = new Particle()
  var controlButtonRequest = []
  // need to find deviceId associated with selected button
  for (var index = 0; index<connectedButtonData.length; index++) {
    if ((connectedButtonData[index].name === button_name) || button_name === 'all_buttons') {
      controlButtonRequest.push(
        sessionParticle.callFunction({
          deviceId: connectedButtonData[index].id,
          name: 'updateState',
          argument: '::' + state + '::spst::',
          auth: conv.user.storage.particleSessionToken})
      );
    }
  }

  return new Promise((resolve, reject) => {
    resolve(Promise.all(controlButtonRequest))
  })
  .then(data => conv.close(button_name + ' has been turned ' + state + '. Goodbye!'))
  .catch(err => conv.close('Action failed!' + err));
});

// Handle the Dialogflow intent named 'actions_intent_PERMISSION'. If user
// agreed to PERMISSION prompt, then boolean value 'permissionGranted' is true.
app.intent('actions_intent_PERMISSION', (conv, params, permissionGranted) => {
  if (!permissionGranted) {
    conv.ask(`OK, no worries. What would you like to do?`);
    conv.ask(new Suggestions('control button', 'list button'));
  } else {
    conv.user.storage.userName = conv.user.name.display;
    conv.ask(`Thanks, ${conv.user.storage.userName}. What would you like to do?`);
    conv.ask(new Suggestions('control button', 'list button'));
  }
});

// Handle the Dialogflow NO_INPUT intent.
// Triggered when the user doesn't provide input to the Action
app.intent('actions_intent_NO_INPUT', (conv) => {
  // Use the number of reprompts to vary response
  const repromptCount = parseInt(conv.arguments.get('REPROMPT_COUNT'));
  if (repromptCount === 0) {
    conv.ask('Hellooo? Remember? Buttons');
  } else if (repromptCount === 1) {
    conv.ask(`Hellooo? Please say what you want to do with buttons.`);
  } else if (conv.arguments.get('IS_FINAL_REPROMPT')) {
    conv.close(`Sorry we're having trouble. Let's ` +
      `try this again later. Goodbye.`);
  }
});

app.catch((conv, e) => {
  console.error(e);
  conv.close("Oops. Something went wrong." + e);
});


const generateCarousel = (connectedButtonData) => {

  // must have at least 2
  var optionItems = []

  optionItems.push({title: 'Turn off all buttons',
                    optionInfo: {key: "off all_buttons"},
                    image: new Image({
                        url: 'https://upload.wikimedia.org/wikipedia/commons/thumb/7/70/Redbutton.svg/2000px-Redbutton.svg.png',
                        alt: 'Turn off all buttons',
                        height: 200,
                        width: 200
                      })
                  });

  for (var index = 0; index<connectedButtonData.length; index++) {

    var buttonName = connectedButtonData[index].name
    var prettyButtonName = buttonName.replace('_', ' ');
    var buttonState = connectedButtonData[index].state;
    var buttonOppositeState = (buttonState === 'off') ? 'on' : 'off'

    optionItems.push({title: 'Turn ' + buttonOppositeState + ' ' + prettyButtonName,
                      optionInfo: {key: buttonOppositeState + ' ' + buttonName},
                      image: new Image({
                        url: 'https://upload.wikimedia.org/wikipedia/commons/thumb/7/70/' + 
                          ((buttonState === 'off') ? 'Redbutton.svg/2000px-Redbutton.svg.png' : 'Greenbutton.svg/2000px-Greenbutton.svg.png'),
                        alt: 'Turn ' + buttonOppositeState + ' ' + prettyButtonName,
                        height: 200,
                        width: 200
                        })
                  });      
  }

  const carousel = new Carousel({items: optionItems});

  console.log('liveCarousel: ', carousel.items);

  return carousel;
}
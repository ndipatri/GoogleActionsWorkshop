'use strict';

const functions = require('firebase-functions');

const {
  dialogflow,
  Permission,
} = require('actions-on-google');

const https = require('https');
const querystring = require('querystring');

const app = dialogflow({debug: true});

// This will be the name of your function as it appears in the Firebase Console 
exports.myCatapult = functions.https.onRequest(app);

// The following are intents that help with basic conversational
// issues (welcome, user persistence, no-response, follow-up, etc.)

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

    // Here we would do any setup for this conversation (e.g. API login)

    return new Promise((resolve, reject) => {
        resolve(conv.ask(`Hello ${name}! What can I do for you today?`));
    })
}
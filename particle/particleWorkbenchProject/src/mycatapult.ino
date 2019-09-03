Servo fireServo;
Servo loadServo;

// Output PINS
int LED = D7;
int MOTOR_IN1 = D5;
int MOTOR_IN2 = D6;
int FIRE_SERVO = D2;
int LOAD_SERVO = D1;

// Input PINS
int MOTOR_LIMIT_SWITCH=D4;
int STATE_SWITCH=D3;

// State
int READY = 1;
int LOADING = 2;
int UNWINDING = 3;
int LOADED = 4;

int catapultState = READY;

int previousState = -1;

// When the motor began to spin
unsigned long spinStartTimeMillis;

// May be set if we only want the motor to spin for a fixed
// duration.
unsigned long targetSpinStopTimeMillis = 0;
    
// This instructs the core to not connect to the
// Particle cloud until explicitly instructed to
// do so...
SYSTEM_MODE(MANUAL);
bool first = true;
bool shouldConnectToParticleCloud = true;

void setup() {
    Serial.begin(9600);

    Particle.function("load", load);  // create a function called "load" 
                                      // that can be called from the cloud
                                      // connect it to the load function
    Particle.function("fire", fire); 
                                      
    fireServo.attach(FIRE_SERVO);  
    loadServo.attach(LOAD_SERVO); 

    pinMode(LED, OUTPUT);    // set D7 as an output so we can flash the onboard LED
    
    pinMode(MOTOR_IN1, OUTPUT);  // Pin 2 on L293d HBridge motor controller
    pinMode(MOTOR_IN2, OUTPUT);  // Pin 7 on L293d HBridge motor controller
    
    pinMode(MOTOR_LIMIT_SWITCH, INPUT_PULLUP); // internally tied to Vcc
    pinMode(STATE_SWITCH, INPUT_PULLUP); // internally tied to Vcc
}

void loop() {

    if (catapultState == UNWINDING && checkIfWeHaveUnwoundEnoughString()) {
        // firing arm is all the way down and string is unwound.
        log("state = LOADED");
        catapultState = LOADED;
    }

    int currentState = digitalRead(STATE_SWITCH); 

    if (first && (currentState == LOW)) {
        shouldConnectToParticleCloud = false;
        log("DISCONNECTED MODE");
    }
    first = false;

    if (shouldConnectToParticleCloud) {
        checkParticleCloudState();
    }

    if (currentState != previousState) {
        previousState = currentState;
        
        if (currentState == LOW) {
            if (catapultState == READY) {
                log("LOCAL MODE SELECT: LOAD");
                load("");
                return;
            }        
        
            if (catapultState == LOADED) {
                log("LOCAL MODE SELECT: FIRE");
                fire("");
                return;
            }
        }
    }

    int limitSwitch = digitalRead(MOTOR_LIMIT_SWITCH); 
    if (catapultState == LOADING && limitSwitch == LOW) {
        
        // hold in place and note how long we spun...
        unsigned long spinTimeMillis = motorStop();
        log("limit reached..  stopping motor. spinTime was %lu", spinTimeMillis);

        closeFireServo();
        
        delay(1000);
        
        grabAnotherPingPongBall();

        // wait for ball to settle
        delay(1000);

        dropPingPongBallIntoEggCup();

        // now that arm is locked, we can unwind string (this is so silly)
        // Note we don't leave 'LOADING' state here. we need to wait
        // for string to unwind ...

        log("state = UNWINDING");
        catapultState = UNWINDING;
        unwindString(spinTimeMillis);

        digitalWrite(LED, LOW);   
    }
    
    delay(50);
}

void singleFlash() {
    digitalWrite(LED, HIGH);   // flash the LED (as an indicator)
    delay(500);
    digitalWrite(LED, LOW);   // flash the LED (as an indicator)
}
void doubleFlash() {
    singleFlash();
    delay(500);
    singleFlash();
}
void dropPingPongBallIntoEggCup() {
    loadServo.write(100);  
}
void grabAnotherPingPongBall() {
    loadServo.write(0); 
}
void openFireServo() {
    fireServo.write(90);  
}
void closeFireServo() {
    fireServo.write(180); 
}
void windUpString() {
    spinStartTimeMillis = millis();

    digitalWrite(MOTOR_IN1, HIGH);
    digitalWrite(MOTOR_IN2, LOW);
}
void unwindString(unsigned long spinTimeMillis) {
    targetSpinStopTimeMillis = millis() + spinTimeMillis;    
    log("current time is %lu", millis());
    log("unwinding string.. setting targetSpinStopTimeMillis to %lu", targetSpinStopTimeMillis);

    digitalWrite(MOTOR_IN1, LOW);
    digitalWrite(MOTOR_IN2, HIGH);
}
boolean checkIfWeHaveUnwoundEnoughString() {
    if (targetSpinStopTimeMillis > 0) {
        if (millis() > targetSpinStopTimeMillis) {
            log("stop time expired.  Done unwinding.");

            // stop time expired
            motorStop();
            targetSpinStopTimeMillis = 0;
            
            return true;
        }
    }

    return false;
}
unsigned long motorStop() {
    digitalWrite(MOTOR_IN1, LOW);
    digitalWrite(MOTOR_IN2, LOW); 

    return millis() - spinStartTimeMillis;
}

int load(String command) {      
    if (catapultState == READY) {
        catapultState = LOADING;
        log("state = LOADING");

        singleFlash();
        
        openFireServo();
    
        // Wind up string so we pull arm down.. This will
        // continue until limit switch is reached...
        log("state = LOADING..  beginning to wind string...");

        windUpString();
    
        // The MOTOR_LIMIIT_SWITCH will eventually
        // cause this to stop (we hope)
    } else {
        doubleFlash();
    }
        
    return 1;                 
}

int fire(String command) {   
    
    if (catapultState == LOADED) {  
        singleFlash();
        
        openFireServo();
        
        delay(1000);
        
        closeFireServo();
        
        catapultState = READY;
        log("state = READY");

    } else {
        doubleFlash();
    }
    
    return 1;                 // return a status of "1", success
}

// @return false if program loop should exit
void checkParticleCloudState() {
    if (!WiFi.hasCredentials()) {
        delay(200);
        return;
    } else if (WiFi.connecting()) {
        delay(200);
        return;
    }

    if (!Particle.connected()) {

        // notice here we block all operation of the button
        // until we get a connection to Particle cloud...
        Particle.connect(); // blocking call
        delay(200);
    }

    // check-in with the Particle cloud 
    Particle.process();
}


void log(String msg, unsigned long value) {
    Serial.printlnf(msg, value);
}
void log(String msg) {
    Serial.printlnf(msg);
}
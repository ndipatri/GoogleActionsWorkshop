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
int LOADED = 3;

int catapultState = READY;

int previousState = -1;

// This instructs the core to not connect to the
// Particle cloud until explicitly instructed to
// do so...
SYSTEM_MODE(MANUAL);
bool first = true;
bool shouldConnectToParticleCloud = true;

void setup() {
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
    int currentState = digitalRead(STATE_SWITCH); 

    if (first && (currentState == LOW)) {
        shouldConnectToParticleCloud = false;
    }
    first = false;

    if (shouldConnectToParticleCloud) {
        checkParticleCloudState();
    }

    if (currentState != previousState) {
        previousState = currentState;
        
        if (currentState == LOW) {
            if (catapultState == READY) {
                load("");
                return;
            }        
        
            if (catapultState == LOADED) {
                fire("");
                return;
            }
        }
    }

    int limitSwitch = digitalRead(MOTOR_LIMIT_SWITCH); 
    if (catapultState == LOADING && limitSwitch == LOW) {

        // firing arm is all the way down.

        catapultState = LOADED;
        
        digitalWrite(LED, HIGH);   
        
        // hold in place and lock
        motorStop();
        closeFireServo();
        
        delay(3000);
        
        // once it's locked, we can unwind string (this is so silly)
        motorReverse();
        delay(3000);
        motorStop();

        grabAnotherPingPongBall();

        // Wait for it to settle 
        delay(3000);

        dropPingPongBallIntoEggCup();

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
void motorForward() {
    digitalWrite(MOTOR_IN1, HIGH);
    digitalWrite(MOTOR_IN2, LOW);
}
void motorReverse() {
    digitalWrite(MOTOR_IN1, LOW);
    digitalWrite(MOTOR_IN2, HIGH);
}
void motorStop() {
    digitalWrite(MOTOR_IN1, LOW);
    digitalWrite(MOTOR_IN2, LOW);   
}

int load(String command) {      
    if (catapultState == READY) {
        catapultState = LOADING;
        singleFlash();
        
        openFireServo();
    
        // Begin by spinning the MOTOR so we pull
        // down the firing arm
        motorForward();
    
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

Servo myservo;

int READY = 1;
int LOADING = 2;
int LOADED = 3;

int catapultState = READY;

int previousState = -1;

int LED = D7;
int MOTOR_IN1 = D5;
int MOTOR_IN2 = D6;
int SERVO = D2;

int MOTOR_LIMIT_SWITCH=D4;
int STATE_SWITCH=D3;

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
void unlockServo() {
    myservo.write(90);  
}
void lockServo() {
    myservo.write(180); 
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
        
        unlockServo();
    
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
        
        unlockServo();
        
        delay(1000);
        
        lockServo();
        
        catapultState = READY;
    } else {
        doubleFlash();
    }
    
    return 1;                 // return a status of "1", success
}

void setup() {
    Particle.function("load", load);  // create a function called "load" 
                                      // that can be called from the cloud
                                      // connect it to the load function
    Particle.function("fire", fire); 
                                      
    myservo.attach(SERVO);   // attach the servo on the D2 pin to the servo object
    pinMode(LED, OUTPUT);    // set D7 as an output so we can flash the onboard LED
    
    pinMode(MOTOR_IN1, OUTPUT);  // Pin 2 on L293d HBridge motor controller
    pinMode(MOTOR_IN2, OUTPUT);  // Pin 7 on L293d HBridge motor controller
    
    pinMode(MOTOR_LIMIT_SWITCH, INPUT_PULLUP); // internally tied to Vcc
    pinMode(STATE_SWITCH, INPUT_PULLUP); // internally tied to Vcc
    
    //motorStop();
}

void loop() {
    
    int currentState = digitalRead(STATE_SWITCH); 
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
        catapultState = LOADED;
        
        digitalWrite(LED, HIGH);   

        // firing arm is all the way down.
        
        // hold in place and lock
        motorStop();
        lockServo();
        
        delay(3000);
        
        // once it's locked, we can unwind string (this is so silly)
        motorReverse();
        delay(3000);
        motorStop();
        
        digitalWrite(LED, LOW);   
    }
    
    delay(100);
}





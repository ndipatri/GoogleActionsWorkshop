use <Threaded/Thread_Library.scad>

$fn=75;

module motorMountCatapult() {
   
        // ring
        cylinder(h=2, r=20);
    
        // lock posts
        translate([12,0,0]) {
            cylinder(h=8, r=1.5);
        }
        translate([-12,0,0]) {
            cylinder(h=8, r=1.5);
        }
        translate([0,12,0]) {
            cylinder(h=8, r=1.5);
        }
        translate([0,-12,0]) {
            cylinder(h=8, r=1.5);
        }

        difference() {
            // center post
            translate([0, 0, -24]) {
                cylinder(h=24, r=6);
            }            
           
            // string hole
            rotate([90,0,0]) {
                translate([0, -10, -8]) {
                    cylinder(h=20, r=1.5);
                }  
            }
        }
        // outer ring
        translate([0,0,-24]) {
            cylinder(h=2, r=20);
        }
}

module mountForLimitSwitch() {
    
    difference() {
        translate([0, 0, 0]) {
            cube([28, 15, 30]);
        }
        
        translate([2.5, 0, 5]) {
            cube([23, 10, 30]);
        }
        
        // wire cut-out
        translate([0, 0, 5]) {
            cube([5, 5, 5]);
        }
    } 
    
    // switch posts
    translate([9,10,25]) {
        rotate([90,0,0]) {
            cylinder(h=10, r=1);
        }
    }    
    translate([19,10,25]) {
        rotate([90,0,0]) {
            cylinder(h=10, r=1);
        }
    }
}


module ballHopperSphere() {
    
    difference() {
        
        // main body
        sphere (r = 40);
        
        // ball box
        translate([-22, 0, 0]) {
            cube([44, 40, 60]);
        }

        // reference ball
        translate([0, 20, 20]) {
            sphere (r = 22);
        }
        
        // side cutout 1
        translate([26, -32, -30]) {
            cube([60, 70, 70]);
        }
        // side cutout 1
        translate([-86, -32, -30]) {
            cube([60, 70, 70]);
        }
        
        //  servo mount recess
        translate([24, -1, -16]) {
            rotate([10, 0, 0]) {
                cube([3.5,8, 32.5]);
            }
        } 
        
        // center axle cutout
        translate([-40,0,0]) {
            rotate([0,90,0]) {
                cylinder(h=90, r=1);
            }
        }          
    }
    
    // non-drive side axle
    translate([-39,0,0]) {
        rotate([0,90,0]) {
            cylinder(h=14, r=4);
        }
    }   
   
    
//    use this to  see the ping pong ball!    
//    translate([0, 15, 15]) {
//        sphere (r = 20);
//    }    
}


module ballHopperBaseDriveSide() {
    
    // servo mount (copied from somewhere else)
    mirror([1, 0, 0]) {
        translate([-60,-20,0]) {
            difference() {
                // outer body
                translate([0,5,0]) {
                    cube([30, 30, 15]);
                } 
                // inner cutout
                translate([4,7,0]) {
                    cube([30, 25, 15]);
                } 
        
                // blade cutout 1
                translate([22,4,0]) {
                    cube([4, 5, 15]);
                } 
                //blade cutout 2
                translate([22,31,0]) {
                    cube([4, 5, 15]);
                }  
            }  
        }
    }
    // top of servo
    translate([50,-15,15]) {
        cube([10, 30, 2]);
    }      
    
    // servo base
    translate([30, -15, -2]) {
        cube([30, 30, 2]);
    }     
 
     // servo leg
    translate([42, 0, -82]) {
        cylinder(h=80, r=5);
    }  
  
    // bottom
    translate([3.5, -20, -82]) {
        cube([45.5, 40, 2]);
    }      
}
    
module ballHopperBaseNonDriveSide() {   
    
    // non-drive side axle rest
    difference() {
        translate([-40,-21,-2]) {
            cube([10, 30, 19]);
        } 
     
         // non-drive side axle cut-out  
        translate([-40, -6, 6]) {
            rotate([0,90,0]) {
                cylinder(h=15, r=5);
            }
        }    
        
        // non-drive side axle drop-down
        translate([-40, -11, 20]) {
            rotate([0,90,0]) {
                cube([15, 10, 20]);
            }
        } 
    }  
  
    // non-drive leg
    translate([-35, -6, -82]) {
        cylinder(h=80, r=5);
    }   
  
    // bottom
    translate([-42, -20, -82]) {
        cube([45.5, 40, 2]);
    }      
}

module ballHopperFeeder() {
    
    // paper-towel tube receiver
    difference() {
        cylinder(h=30, r=20.5);
        cylinder(h=30, r=19);
    } 
    difference() {
        cylinder(h=30, r=24);           
        cylinder(h=30, r=22.5);
    }   
    
    // drive-side receiver bracket
    //
    translate([18.5, 0, 5]) {
        rotate([0,90,0]) {
            cylinder(h=41, r=4);
        }
    }
    translate([55, 0, -31]) {
        cylinder(h=35, r=4);
    }
    
    // non-drive side receiver bracket
    // left
    translate([-39, -16, 5]) {
        rotate([0,90,0]) {
            cylinder(h=25.5, r=4);
        }
    }
    translate([-35, -16, -31]) {
        cylinder(h=35, r=4);
    } 
    // right
    translate([-39, 4, 5]) {
        rotate([0,90,0]) {
            cylinder(h=20.5, r=4);
        }
    }
    translate([-35, 4, -31]) {
        cylinder(h=35, r=4);
    }  
}



color("Yellow") {
        //motorMountCatapult();
        //mountForLimitSwitch();
    
        translate([2, -6, 6]) {
            ballHopperSphere();
        }
        ballHopperBaseDriveSide();
        ballHopperBaseNonDriveSide();
        
        translate([0, 0, 48]) {
            ballHopperFeeder();
        }

}











 






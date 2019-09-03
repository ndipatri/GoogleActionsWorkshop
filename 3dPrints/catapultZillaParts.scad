use <Threaded/Thread_Library.scad>

$fn=75;

module stringSpool() {
   
        // ring
        cylinder(h=2, r=20);
    
        // mount posts
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
            translate([0, 0, -31]) {
                cylinder(h=31, r=6);
            }            
           
            // string hole
            rotate([90,0,0]) {
                translate([0, -15, -8]) {
                    cylinder(h=20, r=2.5);
                }  
            }
        }
        // outer ring
        translate([0,0,-31]) {
            cylinder(h=2, r=20);
        }
        
         // inner ring left
        translate([0, 0, -8]) {
            cylinder(h=2, r=15);
        }       
         // inner ring right
        translate([0, 0, -23]) {
            cylinder(h=2, r=15);
        } 

        // shroud left
        difference() {
            translate([0, 0, 0]) {
                cylinder(h=8, r=20);
            }             
            translate([0, 0, 0]) {
                cylinder(h=12, r=18);
            } 
        }
        // shroud right
        difference() {
            translate([0, 0, -37]) {
                cylinder(h=8, r=20);
            }             
            translate([0, 0, -38]) {
                cylinder(h=14, r=18);
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
    
    // servo base
    translate([30, -15, -2]) {
        cube([30, 30, 2]);
    }     
 
     // servo leg
    translate([42, 0, -122]) {
        cylinder(h=120, r=5);
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
    translate([-35, -6, -122]) {
        cylinder(h=120, r=5);
    }    
}

module ballHopperBase() {
    translate([-40, -27, -122]) {
        cube([87, 55, 2]);
    }
}

module ballHopperFeeder() {
    // paper-towel tube receiver
    difference() {
        translate([-3, 0, 0]) {
            difference() {
                cylinder(h=30, r=26);  
       
                // stop for cardboard  cutout
                cylinder(h=35, r=22);
                
                // upper cutout for cardboard tube
                translate([0, 0, 10]) {
                    cylinder(h=35, r=22.5);
                }
            }  
        } 
    } 

    // non-drive side left leg
    translate([-39, -16, 5]) {
        rotate([0,90,0]) {
            cylinder(h=16, r=4);
        }
    }
    translate([-35, -16, -31]) {
        cylinder(h=35, r=4);
    } 
    // non-drive side right leg
    translate([-39, 4, 5]) {
        rotate([0,90,0]) {
            cylinder(h=12, r=4);
        }
    }
    translate([-35, 4, -31]) {
        cylinder(h=35, r=4);
    }  
    // non-drive side leg base
    translate([-40,-21,-31]) {
        cube([10, 30, 2]);
    }
    
    // drive-side receiver bracket
    //
    translate([20, 0, 5]) {
        rotate([0,90,0]) {
            cylinder(h=39, r=4);
        }
    }
    translate([55, 0, -31]) {
        cylinder(h=35, r=4);
    }  
    // top of servo
    translate([50,-15,-33]) {
        cube([10, 30, 2]);
    }     
}

module motorBracketTop() {
    
    // motor frame
    difference() {
        cube([3, 42, 60]);  
        translate([-1, 10, 10]) { 
            cube([5, 22, 40]);  
        } 
    } 
    
    // motor pegs
    //  bottom left
    translate([0, 7, 10]) {
        rotate([0,90, 0]) {
            cylinder(h=7, r=1.5);
        }
    }   
    //  bottom right
    translate([0, 35, 10]) {
        rotate([0,90, 0]) {
            cylinder(h=7, r=1.5);
        }
    }       
    //  top left
    translate([0, 7, 50]) {
        rotate([0,90, 0]) {
            cylinder(h=7, r=1.5);
        }
    } 
    //  top right
    translate([0, 35, 50]) {
        rotate([0,90, 0]) {
            cylinder(h=7, r=1.5);
        }
    }  

    // base
    translate([0, 0, 0]) { 
        rotate([0,90, 0]) {
            cube([5, 42, 30]);  
        }
    }  
 
    // left arm - main
    translate([33, -3, 25]) { 
        rotate([0, 0, 90]) {
            cube([3, 33, 15]);  
        }
    } 
    // left arm - hook
    translate([33, 1, 25]) { 
        rotate([90, 0, 0]) {
            cube([3, 15, 4]);  
        }
    }     
    // left arm - rubber band hook inner
    translate([15, 0, 32]) {
        rotate([90, 0, 0]) {
            cylinder(h=10, r=2);
        }
    }   
    // left arm - rubber band hook outer
    translate([15, -7, 32]) {
        rotate([90, 0, 0]) {
            cylinder(h=3, r=4);
        }
    }   
    
    // right arm - main
    translate([33, 42, 25]) { 
        rotate([0, 0, 90]) {
            cube([3, 33, 15]);  
        }
    } 
    // right arm - hook
    translate([33, 45, 25]) { 
        rotate([90, 0, 0]) {
            cube([3, 15, 4]);  
        }
    }   
    // right arm - rubber band hook inner
    translate([15, 52, 32]) {
        rotate([90, 0, 0]) {
            cylinder(h=10, r=2);
        }
    }   
    // right arm - rubber band hook outer
    translate([15, 52, 32]) {
        rotate([90, 0, 0]) {
            cylinder(h=3, r=4);
        }
    }  
    
}

module mountForLimitSwitch() {
    
    difference() {
        // main body
        translate([0, 0, -5]) {
            cube([28, 15, 33]);
        }
        
        // servo cut-out
        translate([2.5, -1, 5]) {
            cube([23, 11, 40]);
        }
    } 
    
    // switch posts
    translate([9,10,23]) {
        rotate([90,0,0]) {
            cylinder(h=10, r=1);
        }
    }    
    translate([19,10,23]) {
        rotate([90,0,0]) {
            cylinder(h=10, r=1);
        }
    }
}

module limitSwitchTopPlate() {
    // servo cut-out
    translate([1, 0, 0]) {
        cube([26, 5, 2]);
    }    
}


module winchBase() {
    translate([-5, -40, -23]) {
        difference() {
            union() {
                // left rail guide outer
                translate([-2, 80, -3]) {
                    rotate([90, 0, 0]) {
                        cylinder(h=80, r=10);
                    }
                }
                // right rail guide outer
                translate([85, 80, -3]) {
                    rotate([90, 0, 0]) {
                        cylinder(h=80, r=10);
                    }
                }   
                // base
                translate([-30, 0, 0]) {
                    cube([110, 80, 5]);  
                }
            }
            // left rail guide bottom-cut       
            translate([-12, -13, -20]) {
                cube([20, 100, 20]);  
            }          
            
            // left rail guide inner
            translate([-2, 80, -3]) {
                rotate([90, 0, 0]) {
                    cylinder(h=81, r=7.5);
                }          
            } 
            // right rail guide inner        
            translate([85, 80, -3]) {
                rotate([90, 0, 0]) {
                    cylinder(h=81, r=7.5);
                }          
            }   
            // right rail guide bottom-cut       
            translate([72, -13, -20]) {
                cube([25, 100, 20]);  
            }         
            
            // rubber band cutout       
            translate([-22, 11, -20]) {
                cube([25, 20, 50]);  
            }         
        }   
    }

    translate([-29, -7, -8]) {
        motorBracketBottom();
    }  

    translate([43, -40, -15]) {
        mountForLimitSwitch();
        
        translate([-3, 0, -3]) {
            cube([5, 15, 80]);  
        } 
        
        translate([25, 0, -3]) {
            cube([5, 15, 80]);  
        }         
    }  
    
    
}

module motorBracketBottom() {
   
    // base
    difference() {
        translate([-2.5, -2.5, 0]) {
            rotate([0,90, 0]) {
                cube([10, 49, 37]);  
            }  
        }  
        rotate([0,90, 0]) {
            cube([7, 44, 32]);  
        }

    }
    
    // right arm - rubber band hook inner
    translate([15, 53, -5]) {
        rotate([90, 0, 0]) {
            cylinder(h=7, r=2);
        }
    }   
    // right arm - rubber band hook outer
    translate([15, 53, -5]) {
        rotate([90, 0, 0]) {
            cylinder(h=3, r=4);
        }
    }   
    // left arm - rubber band hook inner
    translate([15, 0, -5]) {
        rotate([90, 0, 0]) {
            cylinder(h=10, r=2);
        }
    }   
    // left arm - rubber band hook outer
    translate([15, -7, -5]) {
        rotate([90, 0, 0]) {
            cylinder(h=3, r=4);
        }
    }     
}

module hopperBase() {
    difference() {
        union() {
            // left rail guide outer
            translate([-2, 10, -3]) {
                rotate([90, 0, 0]) {
                    cylinder(h=60, r=10);
                }
            }
            // right rail guide outer
            translate([85, 10, -3]) {
                rotate([90, 0, 0]) {
                    cylinder(h=60, r=10);
                }
            }   
            // base
            translate([-4, -50, 0]) {
                cube([90, 60, 7]); 
            } 
        }
        // right rail guide inner
        translate([-2, 10, -3]) {
            rotate([90, 0, 0]) {
                cylinder(h=71, r=7.5);
            }          
        } 
        // right rail guide inner        
        translate([85, 10, -3]) {
            rotate([90, 0, 0]) {
                cylinder(h=71, r=7.5);
            }          
        } 
        // left rail guide bottom-cut       
        translate([-12, -73, -20]) {
            cube([80, 100, 20]);  
        }  
        // right rail guide bottom-cut       
        translate([75, -73, -20]) {
            cube([80, 100, 20]);  
        }  

        // zip-tie via 2      
        translate([8, -20, -2]) {
            cylinder(h=14, r=3);
        }     
        // zip-tie via 2       
        translate([8, -30, -2]) {
            cylinder(h=14, r=3);
        }  
        // zip-tie undercut       
        translate([8, -19, -1]) {
            rotate([90, 0, 0]) {
                cylinder(h=13, r=3);
            }
        }         
        
    }
}

module boardMount() {
    
    // main body
    difference() {
        
        // base
        cube([55, 74, 20]);  
        translate([2, 2, 2]) {
            cube([51, 70, 20]);  
        } 
      
        // conduit
        translate([-9, 30, 16]) {
            rotate([0, 90, 0]) {
                cylinder(h=12, r=10);
            }
        } 
        
        // side cut
        translate([3, 69, 6]) {
            cube([49, 7, 17]);  
        }       
    }   
    
    // power supply standoff - left
    translate([12, 20, 21]) {
        cylinder(h=7, r=1.25);
    }
    translate([12, 20, 19]) {
        cylinder(h=5, r=3);
    }
    
    // power supply standoff - right
    translate([42, 5, 21]) {
        cylinder(h=7, r=1.25);
    }   
    translate([42, 5, 19]) {
        cylinder(h=5, r=3);
    }  
    
    // power supply base
     translate([0, 2, 18]) {   
        cube([55, 21, 2]);  
     }       
    
}
color("Yellow") {
        translate([55, 25, 30]) {
            rotate([0, -90, 0]) {
                //stringSpool();
            }
        }

        translate([74, 127, 112]) {
            rotate([0, 0, 180]) {
                //ballHopperSphere();
            }
        }
        
        translate([73, 120, 106]) {
            rotate([0, 0, 180]) {
                //ballHopperBaseDriveSide();
                //ballHopperBaseNonDriveSide();
                //ballHopperBase();
            }
        }
        translate([-5, -65, -23]) {
            //hopperBase();
        }        
        
        translate([73, 120, 154]) {
            rotate([0, 0, 180]) {
                //ballHopperFeeder();
            }
        }
        
        translate([30, 35, 0]) {
            rotate([0, 0, 180]) {
               //motorBracketTop();  
            } 
        } 
        
        translate([0, -240, 0]) {
            //boardMount();
        }
        
        translate([-5, -40, -23]) {
           winchBase();
        }   
        
        translate([48, -40, 20]) {
            //limitSwitchTopPlate();
        }   
         

}











 





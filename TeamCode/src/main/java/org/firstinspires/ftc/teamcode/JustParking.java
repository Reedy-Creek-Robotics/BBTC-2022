package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous (name = "JustParking")
public class JustParking extends BaseOpMode{

    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();
        //have the camera read the signal cone
        int tag = detectAprilTags();
        //forwards 78cm, strafe 63cm

        waitForStart();
        telemetry.addData("Tag Detected", tag);
        telemetry.update();
        //preLoad();
        if (tag == 1){
            strafeRight(-63, 0.5); //no strafe left need that in future ~Aiden
            moveForwards(78, 0.5);
        }
        else if (tag == 2){
            moveForwards(78, 0.5);
        }
        else if (tag == 3){
            strafeRight(63, 0.5);
            moveForwards(78, 0.5);
        }
        else {
            telemetry.addData("No Tag Detected", '0');
            telemetry.update();
        }
    }
}

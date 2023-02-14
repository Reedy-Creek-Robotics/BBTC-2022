package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.BaseOpMode;

@Autonomous (name = "RedTerminalCycle")
public class RedTerminalCycle extends BaseOpMode {

    public static final int SLEEP = 100; //our preset sleep number
    public static final double SPEED1 = 0.5; //our preset speed for driving
    //public static final double SPEED2 = 0.75;

    StackOffsets[] grabbingPositions = StackOffsets.values();
    public int grabCount = 0;

    public enum StackOffsets {
        GRAB1(433),
        GRAB2(318),
        GRAB3(215),
        GRAB4(109),
        GRAB5(0);

        public int offset;

        StackOffsets(int offset) {
            this.offset = offset;
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        initHardware();
        //detect april tags
        int tag = detectAprilTags();
        waitForStart();
        telemetry.addData("Tag Detected", tag);
        telemetry.update();

        //preload
        preLoad(); //opening scissor and bring slides to the ready position
        sleep(10000);
        //moving to medium junction
        moveForwards(82, SPEED1);
        sleep(SLEEP);
        moveForwards(-13, SPEED1);
        sleep(SLEEP);
        strafeRight(35, SPEED1); //strafing to the medium juctions
        sleep(SLEEP);

        //score
        moveSlides(MID_POS); //going to the medium position
        moveForwards(10, SPEED1);
        sleep(SLEEP);
        //moveSlides(1975);
        //sleep(SLEEP);
        scissor(scissorClosed); //dropping the cone
        sleep(SLEEP);
        moveForwards(-10, SPEED1);
        sleep(SLEEP);
        moveSlides(0); //go back to 0

        // move to stack
        strafeRight(-24, SPEED1);
        sleep(SLEEP);
        moveForwards(71, SPEED1); // pushing signal cone
        sleep(SLEEP);
        moveForwards(-10, SPEED1); // backup after pushing signal cone
        sleep(SLEEP);
        turnLeft(91.5, SPEED1);
        sleep(SLEEP);
        //cycling
        for (int i = 0; i < 1; i++) {
            moveSlides(630);
            sleep(SLEEP);
            moveForwards(70, SPEED1); // Drive to stack
            sleep(SLEEP);
            moveSlides(grabbingPositions[grabCount].offset); //go inside the cone that we are at     (Used for cycling)
            sleep(SLEEP);
            scissor(scissorOpen); // open scissor
            sleep(SLEEP);
            moveSlides(LOW_POS); // lift slides to score on low
            sleep(SLEEP);
            moveForwards(-14, SPEED1); // backup to line up to junction
            sleep(SLEEP);
            turnLeft(90, SPEED1); // turn towards junction
            sleep(SLEEP);
            strafeRight(-20, 0.5);
            sleep(SLEEP);
            moveForwards(9, SPEED1);
            sleep(SLEEP);
            moveSlides(1078);
            sleep(SLEEP);
            scissor(scissorClosed); //drop cone on junction
            sleep(SLEEP);
            moveForwards(-13, SPEED1); //moving from the junction
            sleep(SLEEP);
            //if (i < 1) {
                //turnRight(90, SPEED1);
                //sleep(SLEEP);
            //}
            grabCount++;
        }
        //moveForwards(-13, SPEED); //get ready to park
        //park
        if (tag == 1){
            strafeRight(26, SPEED1); //moving to tag one spot
        }
        else if (tag == 2){
            strafeRight(-35, SPEED1); //moving to tag two spot
        }
        else if (tag == 3){
            strafeRight(-90, SPEED1); //moving to tag three spot
        }
        else {
            telemetry.addData("No Tag Detected", '0'); //let us no if there is no tag found
            telemetry.update();
        }
        moveSlides(0); // bringing the slides down so our teleop doesn't get messed up


    }
}
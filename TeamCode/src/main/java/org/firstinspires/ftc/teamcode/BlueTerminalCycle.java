package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous (name = "BlueTerminalCycle")
public class BlueTerminalCycle extends BaseOpMode {

    public static final int SLEEP = 100; //our preset sleep number
    public static final double SPEED = 0.5; //our preset speed for driving

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
        //moving to medium junction
        moveForwards(82, SPEED);
        sleep(SLEEP);
        moveForwards(-13, SPEED);
        sleep(SLEEP);
        strafeRight(-26, SPEED); //strafing to the medium juctions
        sleep(SLEEP);

        //score
        moveSlides(MID_POS); //going to the medium position
        moveForwards(10, SPEED);
        sleep(SLEEP);
        //moveSlides(1975);
        //sleep(SLEEP);
        scissor(scissorClosed); //dropping the cone
        sleep(SLEEP);
        moveForwards(-10, SPEED);
        sleep(SLEEP);
        moveSlides(0); //go back to 0

        // move to stack
        strafeRight(24, SPEED);
        sleep(SLEEP);
        moveForwards(71, SPEED); // pushing signal cone
        sleep(SLEEP);
        moveForwards(-10, SPEED); // backup after pushing signal cone
        sleep(SLEEP);
        turnRight(90, SPEED);
        sleep(SLEEP);
        //cycling
        for (int i = 0; i < 1; i++) {
            moveSlides(630);
            sleep(SLEEP);
            moveForwards(80, SPEED); // Drive to stack
            sleep(SLEEP);
            moveSlides(grabbingPositions[grabCount].offset); //go inside cone
            sleep(SLEEP);
            scissor(scissorOpen); // open scissor
            sleep(SLEEP);
            moveSlides(LOW_POS); // lift slides to score on low
            sleep(SLEEP);
            moveForwards(-14, SPEED); // backup to line up to junction
            sleep(SLEEP);
            turnRight(90, SPEED); // turn towards junction
            sleep(SLEEP);
            strafeRight(20, 0.5);
            sleep(SLEEP);
            moveForwards(9, SPEED);
            sleep(SLEEP);
            moveSlides(1078);
            sleep(SLEEP);
            scissor(scissorClosed); //drop cone on junction
            sleep(SLEEP);
            moveForwards(-13, SPEED); //moving from the junction
            sleep(SLEEP);
            //if (i < 2) {
                //turnLeft(90, SPEED);
                //sleep(SLEEP);
            //}
            grabCount++;
        }
        //moveForwards(-13, SPEED); //get ready to park
        //park
        if (tag == 1){
            strafeRight(90, SPEED); //moving to tag one spot
            moveForwards(10, SPEED);
        }
        else if (tag == 2){
            strafeRight(35, SPEED); //moving to tag two spot
        }
        else if (tag == 3){
            strafeRight(-26, SPEED); //moving to tag three spot
        }
        else {
            telemetry.addData("No Tag Detected", '0'); //let us no if there is no tag found
            telemetry.update();
        }
        moveSlides(0); // bringing the slides down so our teleop doesn't get messed up
    }
}
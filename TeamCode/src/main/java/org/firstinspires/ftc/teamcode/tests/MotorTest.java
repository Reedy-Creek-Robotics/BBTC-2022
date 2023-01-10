/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

/**
 * This OpMode scans a single servo back and forward until Stop is pressed.
 * The code is structured as a LinearOpMode
 * INCREMENT sets how much to increase/decrease the servo position each cycle
 * CYCLE_MS sets the update period.
 *
 * This code assumes a Servo configured with the name "left_hand" as is found on a Robot.
 *
 * NOTE: When any servo position is set, ALL attached servos are activated, so ensure that any other
 * connected servos are able to move freely before running this test.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name = "Motor Test")
public class MotorTest extends LinearOpMode {
    static final double INCREMENT   = 0.01;     // amount to slew servo each CYCLE_MS cycle

    // Define class members
    DcMotor frontRight;
    DcMotor frontLeft;
    DcMotor backRight;
    DcMotor backLeft;
    double  power = 1;

    @Override
    public void runOpMode() {

        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");

        // Wait for the start button
        telemetry.addData(">", "Press Start." );
        telemetry.update();
        waitForStart();

        int BUTTON_DELAY = 250;
        ElapsedTime timeSinceLastPress = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);
        // Scan servo till stop pressed.
        while(opModeIsActive()){

            if (gamepad1.y && (timeSinceLastPress.milliseconds() >= BUTTON_DELAY)) {
                timeSinceLastPress.reset();
                frontLeft.setPower(power);
            }

            if (gamepad1.b && (timeSinceLastPress.milliseconds() >= BUTTON_DELAY)){
                timeSinceLastPress.reset();
                frontRight.setPower(power);
            }

            if (gamepad1.a && (timeSinceLastPress.milliseconds() >= BUTTON_DELAY)){
                timeSinceLastPress.reset();
                backRight.setPower(power);
            }

            if (gamepad1.x && (timeSinceLastPress.milliseconds() >= BUTTON_DELAY)){
                timeSinceLastPress.reset();
                backLeft.setPower(power);
            }

            if (gamepad1.right_bumper && (timeSinceLastPress.milliseconds() >= BUTTON_DELAY)){
                timeSinceLastPress.reset();
                frontLeft.setPower(0);
                frontRight.setPower(0);
                backLeft.setPower(0);
                backRight.setPower(0);
            }

            if (gamepad1.dpad_up){
                power = power + INCREMENT;
            }

            if (gamepad1.dpad_up){
                power = power - INCREMENT;
            }

            telemetry.addData("Motor Power", power);
            processCurrentStats();
            telemetry.update();
        }

        // Signal done;
        telemetry.addData(">", "Done");
        telemetry.update();
    }

    double minFrontRightCurrent = Double.MAX_VALUE;
    double maxFrontRightCurrent = Double.MIN_VALUE;
    double minFrontLeftCurrent= Double.MAX_VALUE;
    double maxFrontLeftCurrent = Double.MIN_VALUE;
    double minBackRightCurrent = Double.MAX_VALUE;
    double maxBackRightCurrent = Double.MIN_VALUE;
    double minBackLeftCurrent = Double.MAX_VALUE;
    double maxBackLeftCurrent = Double.MIN_VALUE;

    public void processCurrentStats() {
        double frontLeftCurrent = ((DcMotorEx)frontLeft).getCurrent(CurrentUnit.AMPS);
        if( frontLeftCurrent < minFrontLeftCurrent ) {
            minFrontLeftCurrent = frontLeftCurrent;
        }
        if( frontLeftCurrent > maxFrontLeftCurrent ) {
            maxFrontLeftCurrent = frontLeftCurrent;
        }

        double frontRightCurrent = ((DcMotorEx)frontRight).getCurrent(CurrentUnit.AMPS);
        if( frontRightCurrent < minFrontRightCurrent ) {
            minFrontRightCurrent = frontRightCurrent;
        }
        if( frontRightCurrent > maxFrontRightCurrent ) {
            maxFrontRightCurrent = frontRightCurrent;
        }

        double backLeftCurrent = ((DcMotorEx)backLeft).getCurrent(CurrentUnit.AMPS);
        if( backLeftCurrent < minBackLeftCurrent ) {
            minBackLeftCurrent = backLeftCurrent;
        }
        if( backLeftCurrent > maxBackLeftCurrent ) {
            maxBackLeftCurrent = backLeftCurrent;
        }

        double backRightCurrent = ((DcMotorEx)backRight).getCurrent(CurrentUnit.AMPS);
        if( backRightCurrent < minBackRightCurrent ) {
            minBackRightCurrent = backRightCurrent;
        }
        if( backRightCurrent > maxBackRightCurrent ) {
            maxBackRightCurrent = backRightCurrent;
        }

        String flString = "frontLeft: current=" + frontLeftCurrent + ", max=" + maxFrontLeftCurrent + ", min=" + minFrontLeftCurrent;
        String frString = "frontRight: current=" + frontRightCurrent + ", max=" + maxFrontRightCurrent + ", min=" + minFrontRightCurrent;
        String blString = "backLeft: current=" + backLeftCurrent + ", max=" + maxBackLeftCurrent + ", min=" + minBackLeftCurrent;
        String brString = "backRight: current=" + backRightCurrent + ", max=" + maxBackRightCurrent + ", min=" + minBackRightCurrent;

        telemetry.addLine(flString);
        RobotLog.d(flString);
        telemetry.addLine(frString);
        RobotLog.d(frString);
        telemetry.addLine(blString);
        RobotLog.d(blString);
        telemetry.addLine(brString);
        RobotLog.d(brString);
    }
}
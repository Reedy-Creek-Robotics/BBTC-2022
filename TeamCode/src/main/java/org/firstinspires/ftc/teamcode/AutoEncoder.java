package org.firstinspires.ftc.teamcode;

//ticks to go one cm: 17.887
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name = "AutoForwards")
public class AutoEncoder extends LinearOpMode {

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor backLeft;

    public int distance;
    private int frontLeftPos;
    private int frontRightPos;
    private int backLeftPos;
    private int backRightPos;


        @Override
        public void runOpMode() {
            frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
            frontRight = hardwareMap.get(DcMotor.class, "frontRight");
            backLeft = hardwareMap.get(DcMotor.class, "backLeft");
            backRight = hardwareMap.get(DcMotor.class, "backRight");

            backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
            backRight.setDirection(DcMotorSimple.Direction.REVERSE);

            backLeftPos = 0;
            backRightPos = 0;
            frontLeftPos = 0;
            frontRightPos = 0;

            waitForStart();
            //double distance = 17.855;
            moveForwards(100, 0.25);
            moveForwards(-100, 0.25);
        }

        // distance is in cm
        private void moveForwards(double distance, double speed) {
            double target = distance * 17.855;
            backLeftPos += target;
            frontLeftPos += target;
            backRightPos += target;
            frontRightPos += target;

            backLeft.setTargetPosition(backLeftPos);
            backRight.setTargetPosition(backRightPos);
            frontLeft.setTargetPosition(frontLeftPos);
            frontRight.setTargetPosition(frontRightPos);

            backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            backLeft.setPower(speed);
            backRight.setPower(speed);
            frontRight.setPower(speed);
            frontLeft.setPower(speed);
            while(opModeIsActive() && backLeft.isBusy() && backRight.isBusy() && frontLeft.isBusy() && frontRight.isBusy()) {
                idle();
                telemetry.addData("backLeft", backLeft.getCurrentPosition());
                telemetry.addData("backRight",backRight.getCurrentPosition());
                telemetry.addData("frontRight", frontRight.getCurrentPosition());
                telemetry.addData("frontLeft", frontLeft.getCurrentPosition());
                telemetry.update();
            }
            backLeft.setPower(0);
            backRight.setPower(0);
            frontRight.setPower(0);
            frontLeft.setPower(0);
        }

    private void strafeRight(double distance, double speed) {
        double target = distance * 17.855;
        backLeftPos -= target;
        frontLeftPos += target;
        backRightPos += target;
        frontRightPos -= target;

        backLeft.setTargetPosition(backLeftPos);
        backRight.setTargetPosition(backRightPos);
        frontLeft.setTargetPosition(frontLeftPos);
        frontRight.setTargetPosition(frontRightPos);

        backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        backLeft.setPower(speed);
        backRight.setPower(speed);
        frontRight.setPower(speed);
        frontLeft.setPower(speed);
        while(opModeIsActive() && backLeft.isBusy() && backRight.isBusy() && frontLeft.isBusy() && frontRight.isBusy()) {
            idle();
            telemetry.addData("backLeft", backLeft.getCurrentPosition());
            telemetry.addData("backRight",backRight.getCurrentPosition());
            telemetry.addData("frontRight", frontRight.getCurrentPosition());
            telemetry.addData("frontLeft", frontLeft.getCurrentPosition());
            telemetry.update();
        }
        backLeft.setPower(0);
        backRight.setPower(0);
        frontRight.setPower(0);
        frontLeft.setPower(0);
    }

}

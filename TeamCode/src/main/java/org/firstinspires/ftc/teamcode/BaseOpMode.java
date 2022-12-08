package org.firstinspires.ftc.teamcode;

//ticks to go one cm: 17.887
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

public abstract class BaseOpMode extends LinearOpMode {

    Servo scissor;
    DcMotor rightLinearSlide;
    DcMotor leftLinearSlide;
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backRight;
    private DcMotor backLeft;
    final double scissorClosed = 0.7;
    final double scissorOpen = 0;

    public int distance;
    private int frontLeftPos;
    private int frontRightPos;
    private int backLeftPos;
    private int backRightPos;
    private double scissorPosition;


    protected void initHardware(){
        frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        backRight = hardwareMap.get(DcMotor.class, "backRight");
        rightLinearSlide = hardwareMap.get(DcMotor.class, "rightLinearSlide");
        leftLinearSlide = hardwareMap.get(DcMotor.class, "leftLinearSlide");
        scissor = hardwareMap.get(Servo.class, "scissor");


        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightLinearSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftLinearSlide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        leftLinearSlide.setDirection(DcMotorSimple.Direction.REVERSE);

        backLeftPos = 0;
        backRightPos = 0;
        frontLeftPos = 0;
        frontRightPos = 0;

    }

        // distance is in cm
        protected void moveForwards(double distance, double speed) {
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

    protected void strafeRight(double distance, double speed) {
        double target = distance * 17.855 * 1.1;
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

    protected void moveSlides(int position){
        leftLinearSlide.setTargetPosition(position);
        rightLinearSlide.setTargetPosition(position);

        leftLinearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightLinearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftLinearSlide.setPower(0.15);
        rightLinearSlide.setPower(0.15);

        while(opModeIsActive() && leftLinearSlide.isBusy() && rightLinearSlide.isBusy()) {
            idle();
            telemetry.addData("leftLinearSlide", leftLinearSlide.getCurrentPosition());
            telemetry.addData("rightLinearSlide",rightLinearSlide.getCurrentPosition());
            telemetry.update();
        }
        telemetry.addData("leftLinearSlide", leftLinearSlide.getCurrentPosition());
        telemetry.addData("rightLinearSlide",rightLinearSlide.getCurrentPosition());
        telemetry.update();

        leftLinearSlide.setPower(0);
        rightLinearSlide.setPower(0);
    }

    protected void preLoad(){
        scissor.setPosition(scissorOpen);
        moveSlides(100);
    }

    protected void scissor(double scissorPosition){
        scissor.setPosition(scissorPosition);
        sleep(200);
    }
}

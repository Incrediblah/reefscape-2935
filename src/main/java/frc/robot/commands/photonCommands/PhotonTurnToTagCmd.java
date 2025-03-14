package frc.robot.commands.photonCommands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.PhotonSubsystem;
import frc.robot.subsystems.DriveSubsystem;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;

import edu.wpi.first.math.MathUtil;

public class PhotonTurnToTagCmd extends Command {
    private final DriveSubsystem DRIVE_SUBSYSTEM;
    private final PhotonSubsystem PHOTON_SUBSYSTEM;
    private final PIDController turnPID;
    private double targetAngle;
    private boolean validTagDetected = false;
    private String driveMode; 


    private boolean endCommand; 

    public PhotonTurnToTagCmd(DriveSubsystem driveSubsystem, PhotonSubsystem photonSubsystem, boolean end, String mode) {
        this.DRIVE_SUBSYSTEM = driveSubsystem;
        this.PHOTON_SUBSYSTEM = photonSubsystem;
        this.turnPID = new PIDController(0.2, 0, 0.005); // Tune values as needed
        this.driveMode= mode;
        this.endCommand = end; 
        turnPID.enableContinuousInput(-180, 180);
        addRequirements(driveSubsystem, photonSubsystem);
    }

    @Override
    public void initialize() {
        int detectedTag = PHOTON_SUBSYSTEM. getBestAprilTagID(); // Get best detected tag ID

        if (detectedTag != -1) { // Ensure a valid tag was detected
            validTagDetected = true;
            double feederAngle = PHOTON_SUBSYSTEM.getFeederAngleForTag(detectedTag, driveMode); // Get pre-defined feeder angle
            targetAngle = feederAngle; // Set target angle directly to the feeder's expected angle
        } else {
            validTagDetected = false;
        }
     
    }

    @Override
    public void execute() {
        if (!validTagDetected) {
            end(true);
            return;
        }

        double currentAngle = DRIVE_SUBSYSTEM.getHeading();
        double turnSpeed = turnPID.calculate(currentAngle, targetAngle);
        turnSpeed = MathUtil.clamp(turnSpeed, -0.3, 0.3); // Limit turn speed
        DRIVE_SUBSYSTEM.drive(0, 0, turnSpeed, false);
    }


    @Override
    public void end(boolean interrupted) {
        DRIVE_SUBSYSTEM.drive(0, 0, 0, false); // Stop movement
    }

    @Override
    public boolean isFinished() {
        if(validTagDetected && Math.abs(targetAngle - DRIVE_SUBSYSTEM.getHeading()) < 2.5){
          return true; 
        }else if(endCommand){
          return true; 
        }else{
          return false; 
        }
    }

   
}

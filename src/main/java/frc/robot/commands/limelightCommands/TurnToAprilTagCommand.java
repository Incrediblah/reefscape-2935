package frc.robot.commands.limelightCommands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.VisionSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import edu.wpi.first.math.MathUtil;

public class TurnToAprilTagCommand extends Command {
    private final DriveSubsystem DRIVE_SUBSYSTEM;
    private final VisionSubsystem VISION_SUBSYSTEM;
    private final PIDController turnPID;
    private double targetAngle;
    private boolean validTagDetected = false;

    private boolean endCommand; 

    private String driveMode; 

    public TurnToAprilTagCommand(DriveSubsystem driveSubsystem, VisionSubsystem visionSubsystem, boolean end, String mode) {
        this.DRIVE_SUBSYSTEM = driveSubsystem;
        this.VISION_SUBSYSTEM = visionSubsystem;
        this.turnPID = new PIDController(0.2, 0, 0.005); // Tune values as needed
        this.endCommand = end; 
        this.driveMode = mode; 
        turnPID.enableContinuousInput(-180, 180);
        addRequirements(driveSubsystem, visionSubsystem);
    }

    @Override
    public void initialize() {
        int detectedTag = VISION_SUBSYSTEM.getBestAprilTag(); // Get best detected tag ID

        if (detectedTag != -1) { // Ensure a valid tag was detected
            validTagDetected = true;
            double reefAngle = VISION_SUBSYSTEM.getReefAngleForTag(detectedTag, driveMode); // Get pre-defined reef angle
            targetAngle = reefAngle; // Set target angle directly to the reef's expected angle
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

    // Maps AprilTag ID to the expected reef angle
    // private double getReefAngleForTag(int tagID) {
    //     switch (tagID) {
    //         case 6: return 120;
    //         case 7: return 180;
    //         case 8: return -120;
    //         case 9: return -60;
    //         case 10: return 0;
    //         case 11: return 60;
    //         case 17: return -120;
    //         case 18: return 180;
    //         case 19: return 120;
    //         case 20: return 60;
    //         case 21: return 0;
    //         case 22: return -60;
    //         default: return 0; // If no valid tag, assume no turn needed
    //     }
    // }
}

package frc.robot.commands.limelightCommands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.VisionSubsystem;
import frc.robot.Constants.AutoConstants;
import frc.robot.subsystems.DriveSubsystem;
import edu.wpi.first.math.MathUtil;

public class TurnToSpecificAprilTagCommand extends Command {
    private final DriveSubsystem DRIVE_SUBSYSTEM;
    private final VisionSubsystem VISION_SUBSYSTEM;
    private final PIDController turnPID;
    private double targetAngle;
    private boolean validTagDetected = false;

    private boolean endCommand; 

    private String driveMode; 

    private int tagId; 

    public TurnToSpecificAprilTagCommand(DriveSubsystem driveSubsystem, VisionSubsystem visionSubsystem, boolean end, String mode, int id) {
        this.DRIVE_SUBSYSTEM = driveSubsystem;
        this.VISION_SUBSYSTEM = visionSubsystem;
        this.turnPID = new PIDController(0.2, 0, 0.005); // Tune values as needed
        this.endCommand = end; 
        this.driveMode = mode; 

        this.tagId = id; 
        turnPID.enableContinuousInput(-180, 180);
        addRequirements(driveSubsystem, visionSubsystem);
    }

    @Override
    public void initialize() {
        int detectedTag = VISION_SUBSYSTEM.getBestAprilTag(); // Get best detected tag ID

        if (detectedTag != -1) { // Ensure a valid tag was detected
            validTagDetected = true;
            double reefAngle = VISION_SUBSYSTEM.getReefAngleForTag(tagId, AutoConstants.autoMode); // Get pre-defined reef angle
            targetAngle = reefAngle; // Set target angle directly to the reef's expected angle
        } else {
            validTagDetected = false;
        }

        SmartDashboard.putNumber("spceific tag rotation value", targetAngle); 
     
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

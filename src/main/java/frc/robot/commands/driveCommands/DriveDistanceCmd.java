package frc.robot.commands.driveCommands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.VisionConstants;

public class DriveDistanceCmd extends Command {

    private final DriveSubsystem DRIVE_SUBSYSTEM;
    private final double targetDistance; 
    private final double baseSpeed; 
    private double adjustedSpeed; 
    private double startDistance; 
    private double targetAngle; //

    private final PIDController rotationPID; //

    private final boolean endCommand; 

    /** Creates a new DriveDistanceCmd with rotation lock. */
    public DriveDistanceCmd(DriveSubsystem drive, double speed, double distance, boolean end) {
        this.DRIVE_SUBSYSTEM = drive;
        this.baseSpeed = Math.abs(speed);  // Ensure speed is always positive
        this.targetDistance = distance; // Distance can be positive or negative
        this.endCommand = end;

        this.rotationPID = new PIDController(VisionConstants.rotAlignKp,VisionConstants.rotAlignKi, VisionConstants.rotAlignKd); 

        addRequirements(DRIVE_SUBSYSTEM);
    }

    @Override
    public void initialize() {
        startDistance = DRIVE_SUBSYSTEM.getAverageDistance();

        // Lock the robot’s current heading
        targetAngle = DRIVE_SUBSYSTEM.getHeading();

        // If the distance is negative, reverse the speed
        adjustedSpeed = (targetDistance < 0) ? -baseSpeed : baseSpeed;
    }

    @Override
    public void execute() {

        double currentHeading = DRIVE_SUBSYSTEM.getHeading();
        double rotationSpeed = rotationPID.calculate(currentHeading, targetAngle);

        rotationSpeed = MathUtil.clamp(rotationSpeed, -0.2, 0.2);

        // Drive forward while maintaining heading
        DRIVE_SUBSYSTEM.drive(adjustedSpeed, 0, rotationSpeed, false);

        // Debug info for SmartDashboard
        double traveledDistance = DRIVE_SUBSYSTEM.getAverageDistance() - startDistance;
        SmartDashboard.putNumber("Traveled Distance", traveledDistance);
        SmartDashboard.putNumber("Drive Rotation Correction", rotationSpeed);
        SmartDashboard.putNumber("Target Heading", targetAngle);
        SmartDashboard.putNumber("Current Heading", currentHeading);
    }

    @Override
    public void end(boolean interrupted) {
        DRIVE_SUBSYSTEM.drive(0, 0, 0, false);
    }

    @Override
    public boolean isFinished() {
        double traveledDistance = DRIVE_SUBSYSTEM.getAverageDistance() - startDistance;
        
        if (endCommand) {
            return true; 
        } else if (Math.abs(traveledDistance) >= Math.abs(targetDistance)) {
            return true; 
        } else {
            return false; 
        }
    }
}

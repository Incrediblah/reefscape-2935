package frc.robot.commands.driveCommands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.Constants.VisionConstants;

public class DriveDistanceAtRobotAngleCmd extends Command {

    private final DriveSubsystem DRIVE_SUBSYSTEM;
    private final double targetDistance;
    private final double speed;
    private final double angleDegrees;
    private final boolean endCommand;
    private final double runTime;

    private double startDistance;
    private double vx;
    private double vy;

    private double startTime;

    public DriveDistanceAtRobotAngleCmd(DriveSubsystem drive, double speed, double distance, double angleDegrees, boolean end, double time) {
        this.DRIVE_SUBSYSTEM = drive;
        this.speed = Math.abs(speed);
        this.targetDistance = distance;
        this.angleDegrees = angleDegrees;
        this.endCommand = end;
        this.runTime = time;

        addRequirements(drive);
    }

    @Override
    public void initialize() {
        // Encoder-based distance tracking
        startDistance = DRIVE_SUBSYSTEM.getAverageDistance();

        // Convert angle to radians and compute vx, vy
        double radians = Math.toRadians(angleDegrees);
        vx = speed * Math.cos(radians);
        vy = speed * Math.sin(radians);

        // Reverse direction if distance is negative
        if (targetDistance < 0) {
            vx = -vx;
            vy = -vy;
        }

        startTime = System.currentTimeMillis();
    }

    @Override
    public void execute() {
        DRIVE_SUBSYSTEM.drive(vx, vy, 0.0, false); // Robot-relative drive

        SmartDashboard.putNumber("Drive Vx", vx);
        SmartDashboard.putNumber("Drive Vy", vy);
        SmartDashboard.putNumber("Distance Traveled", getTraveledDistance());
    }

    private double getTraveledDistance() {
        return DRIVE_SUBSYSTEM.getAverageDistance() - startDistance;
    }

    @Override
    public boolean isFinished() {
        double traveled = getTraveledDistance();

        if (endCommand) return true;
        if (Math.abs(traveled) >= Math.abs(targetDistance)) return true;
        if (System.currentTimeMillis() - startTime > runTime) return true;

        return false;
    }

    @Override
    public void end(boolean interrupted) {
        DRIVE_SUBSYSTEM.drive(0, 0, 0, false);
    }
}

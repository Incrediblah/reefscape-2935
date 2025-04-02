package frc.robot.commands.driveCommands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

import frc.robot.subsystems.DriveSubsystem;


public class TurnToAngleCommand extends Command {
    private final DriveSubsystem DRIVE_SUBSYSTEM;
    private final double targetAngle;
    private final PIDController turnPID;

    public TurnToAngleCommand(DriveSubsystem driveSubsystem, double targetAngle) {
        this.DRIVE_SUBSYSTEM = driveSubsystem;
        this.targetAngle = targetAngle;
        this.turnPID = new PIDController(0.03, 0, 0.001); // Tune values as needed
        turnPID.enableContinuousInput(-180, 180);
        addRequirements(DRIVE_SUBSYSTEM);
    }

    @Override
    public void execute() {
        double currentAngle = DRIVE_SUBSYSTEM.getHeading(); // Get gyro heading
        double turnSpeed = turnPID.calculate(currentAngle, targetAngle);
        turnSpeed = MathUtil.clamp(turnSpeed, -0.8, 0.8); // Limit rotation speed
        DRIVE_SUBSYSTEM.drive(0, 0, turnSpeed, false); // Apply rotation
    }

    @Override
    public boolean isFinished() {
        return Math.abs(targetAngle - DRIVE_SUBSYSTEM.getHeading()) < 2.0; // Stop within 2 degrees
    }

    @Override
    public void end(boolean interrupted) {
        DRIVE_SUBSYSTEM.drive(0, 0, 0, false); // Stop motion
    }
}

package frc.robot.commands.photonCommands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.VisionConstants;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.PhotonSubsystem;
import edu.wpi.first.math.MathUtil;

public class AlignXandYWithPhoton extends Command {

    private final PhotonSubsystem PHOTON_SUBSYSTEM; 
    private final DriveSubsystem DRIVE_SUBSYSTEM; 

    private final PIDController strafePID; 
    private final PIDController drivePID; 
    private final PIDController rotationPID; // NEW: Keeps robot locked to target angle

    private final boolean endCommand; 

    private double measuredYaw; 
    private double measuredPitch; 

    private double strafeSpeed; 
    private double driveSpeed;
    private double rotationSpeed; // NEW: Stores rotation correction

    private final double toleranceX;
    private final double toleranceY;

    private final double targetYaw; 
    private final double targetPitch;
    private double targetAngle; // NEW: Stores locked heading

    private boolean inRangeX; 
    private boolean inRangeY; 

    /** Creates a new alignmentCommand for PhotonVision. */
    public AlignXandYWithPhoton(DriveSubsystem drive, PhotonSubsystem vision, boolean end, 
                                double targetYaw, double targetPitch, double toleranceX, double toleranceY) { 
        this.DRIVE_SUBSYSTEM = drive; 
        this.PHOTON_SUBSYSTEM = vision; 

        this.drivePID = new PIDController(VisionConstants.driveAlignKp,VisionConstants.driveAlignKi, VisionConstants.driveAlignKd); 
        this.strafePID = new PIDController(VisionConstants.strafeAlignKp,VisionConstants.strafeAlignKi, VisionConstants.strafeAlignKd);
        this.rotationPID = new PIDController(VisionConstants.rotAlignKp,VisionConstants.rotAlignKi, VisionConstants.rotAlignKd); // NEW: Controls unintended turning

        this.endCommand = end; 

        this.targetYaw = targetYaw;
        this.targetPitch = targetPitch;  

        this.toleranceX = toleranceX; 
        this.toleranceY = toleranceY; 

        addRequirements(DRIVE_SUBSYSTEM, PHOTON_SUBSYSTEM);
    }

    @Override
    public void initialize() {
        drivePID.reset();
        strafePID.reset();
        rotationPID.reset(); // NEW: Reset rotation PID

        inRangeX = false; 
        inRangeY = false; 

        targetAngle = DRIVE_SUBSYSTEM.getHeading(); // Lock initial heading
    }

    @Override
    public void execute() {
        if(PHOTON_SUBSYSTEM.hasTargets()){
            measuredYaw = PHOTON_SUBSYSTEM.getYawToBestTarget();
            measuredPitch = PHOTON_SUBSYSTEM.getPitchToBestTarget();
            
            // X-direction (Strafing)
            if (Math.abs(targetYaw - measuredYaw) <= toleranceX) { 
                strafeSpeed = 0; 
                inRangeX = true; 
            } else {
                strafeSpeed = strafePID.calculate(measuredYaw, targetYaw);
            }
            strafeSpeed = MathUtil.clamp(strafeSpeed, -0.25, 0.25); // Keep existing limits
            // Y-direction (Forward movement)
            if (Math.abs(targetPitch - measuredPitch) <= toleranceY) { 
                driveSpeed = 0; 
                inRangeY = true; 
            } else {
                driveSpeed = drivePID.calculate(measuredPitch, targetPitch);
            }
            driveSpeed = MathUtil.clamp(driveSpeed, -0.25, 0.25); // Keep existing limits

            // NEW: Lock robot to the set angle and prevent unnecessary rotation
            double currentHeading = DRIVE_SUBSYSTEM.getHeading();
            rotationSpeed = rotationPID.calculate(currentHeading, targetAngle);
            rotationSpeed = MathUtil.clamp(rotationSpeed, -0.2, 0.2);

            SmartDashboard.putNumber("Photon Align Strafe Speed", strafeSpeed); 
            SmartDashboard.putNumber("Photon Align Drive Speed", driveSpeed);
            SmartDashboard.putNumber("Photon Align Rotation Speed", rotationSpeed);
            SmartDashboard.putBoolean("In Range X", inRangeX); 
            SmartDashboard.putBoolean("In Range Y", inRangeY);
            
            // Drive using corrected values
            // DRIVE_SUBSYSTEM.drive(-driveSpeed, -strafeSpeed, rotationSpeed, false);
            DRIVE_SUBSYSTEM.drive(-driveSpeed,-strafeSpeed, rotationSpeed, false);


        } else {
            driveSpeed = 0;
            strafeSpeed = 0;
            rotationSpeed = 0;
        }
    }

    @Override
    public void end(boolean interrupted) {
        DRIVE_SUBSYSTEM.drive(0, 0, 0, false); // Stop all movement
    }

    @Override
    public boolean isFinished() {
        if(endCommand){
            return true; 
        } else if(inRangeX && inRangeY){
            return true; 
        } else {
            return false; 
        }
    }
}

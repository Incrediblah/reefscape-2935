// package frc.robot.commands.limelightCommands;

// import edu.wpi.first.math.controller.PIDController;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import edu.wpi.first.wpilibj2.command.Command;
// import frc.robot.subsystems.VisionSubsystem;
// import frc.robot.Constants.VisionConstants;
// import frc.robot.subsystems.DriveSubsystem;
// import edu.wpi.first.math.MathUtil;

// public class alignXandYLeftCamera extends Command {

//     private final VisionSubsystem VISION_SUBSYSTEM; 
//     private final DriveSubsystem DRIVE_SUBSYSTEM; 

//     private final PIDController strafePID; 
//     private final PIDController drivePID; 
//     private final PIDController rotationPID; // NEW: Keeps robot locked to target angle

//     private final boolean endCommand; 
//     private final int setPipelineNumber; 

//     private double measuredValueX; 
//     private double measuredValueY; 

//     private double strafeSpeed; 
//     private double driveSpeed;
//     private double rotationSpeed; // NEW: Stores rotation correction

//     private final double toleranceX;
//     private final double toleranceY;

//     private final double targetValueX; 
//     private final double targetValueY;
//     private double targetAngle; // NEW: Stores locked heading

//     private boolean inRangeX; 
//     private boolean inRangeY; 

//     /** Creates a new alignmentCommand. */
//     public alignXandYLeftCamera(DriveSubsystem drive, VisionSubsystem vision, int pipeline, boolean end, 
//                                 double targetOffsetX, double targetOffsetY, double toleranceX, double toleranceY) { // NEW: Accepts a locked heading from `TurnToAprilTagCommand`
//         this.DRIVE_SUBSYSTEM = drive; 
//         this.VISION_SUBSYSTEM = vision; 

//          this.drivePID = new PIDController(VisionConstants.driveAlignKp,VisionConstants.driveAlignKi, VisionConstants.driveAlignKd); 
//         this.strafePID = new PIDController(VisionConstants.strafeAlignKp,VisionConstants.strafeAlignKi, VisionConstants.strafeAlignKd);
//         this.rotationPID = new PIDController(VisionConstants.rotAlignKp,VisionConstants.rotAlignKi, VisionConstants.rotAlignKd); // NEW: Controls unintended turning

//         this.endCommand = end; 
//         this.setPipelineNumber = pipeline; 

//         this.targetValueX = targetOffsetX;
//         this.targetValueY = targetOffsetY;  

//         this.toleranceX = toleranceX; 
//         this.toleranceY = toleranceY; 
      
//         addRequirements(DRIVE_SUBSYSTEM, VISION_SUBSYSTEM);
//     }

//     @Override
//     public void initialize() {
//         drivePID.reset();
//         strafePID.reset();
//         rotationPID.reset(); // NEW: Reset rotation PID

//         VISION_SUBSYSTEM.setLeftPipeline(setPipelineNumber);
//         inRangeX = false; 
//         inRangeY = false; 

//         targetAngle = DRIVE_SUBSYSTEM.getHeading(); 

//         VISION_SUBSYSTEM.setLeftPipeline(0);
//         VISION_SUBSYSTEM.setLeftPipeline(0);

//         VISION_SUBSYSTEM.setLeftLED(1);
//         VISION_SUBSYSTEM.setLeftLED(1);
//     }

//     @Override
//     public void execute() {
//         if(VISION_SUBSYSTEM.limelightLeftTargetSeen()){
//             measuredValueY = VISION_SUBSYSTEM.getLeftTy();
//             measuredValueX = VISION_SUBSYSTEM.getLeftTx();  

//             // X-direction (Strafing)
//             if (Math.abs(targetValueX - measuredValueX) <= toleranceX) { 
//                 strafeSpeed = 0; 
//                 inRangeX = true; 
//             } else {
//                 strafeSpeed = strafePID.calculate(measuredValueX, targetValueX);
//             }
//             strafeSpeed = MathUtil.clamp(strafeSpeed, -0.75, 0.75); // Keep existing limits

//             // Y-direction (Forward movement)
//             if (Math.abs(targetValueY - measuredValueY) <= toleranceY) { 
//                 driveSpeed = 0; 
//                 inRangeY = true; 
//             } else {
//                 driveSpeed = drivePID.calculate(measuredValueY, targetValueY);
//             }
//             driveSpeed = MathUtil.clamp(driveSpeed, -0.75, 0.75); // Keep existing limits

//             // NEW: Lock robot to the set angle and prevent unnecessary rotation
//             double currentHeading = DRIVE_SUBSYSTEM.getHeading();
//             rotationSpeed = rotationPID.calculate(currentHeading, targetAngle);
//             rotationSpeed = MathUtil.clamp(rotationSpeed, -0.2, 0.2); // Small corrections only

//             SmartDashboard.putNumber("Align Strafe Speed", strafeSpeed); 
//             SmartDashboard.putNumber("Align Drive Speed", driveSpeed);
//             SmartDashboard.putNumber("Align Rotation Speed", rotationSpeed); // NEW: Monitor rotation correction
//             SmartDashboard.putBoolean("In Range X", inRangeX); 
//             SmartDashboard.putBoolean("In Range Y", inRangeY);

//             // Drive using corrected values, preventing unnecessary rotation
//             DRIVE_SUBSYSTEM.drive(driveSpeed, strafeSpeed, rotationSpeed, false);
//             // DRIVE_SUBSYSTEM.drive(driveSpeed, 0, rotationSpeed, false);

//         } else {
//             driveSpeed = 0;
//             strafeSpeed = 0;
//             rotationSpeed = 0;
//         }
//     }

//     @Override
//     public void end(boolean interrupted) {
//         DRIVE_SUBSYSTEM.drive(0, 0, 0, false); // Stop all movement


//     }

//     @Override
//     public boolean isFinished() {
//         if(endCommand){
//             return true; 
//         } else if(inRangeX && inRangeY){
//             return true; 
//         } else {
//             return false; 
//         }
//     }
// }



/// 
/// 




//// UPDATE WITH ALIGNMENT + ROTATION, PREVIOUS VERSION IS NO ROTATION, JUST CLAMPS THE START HEADING
package frc.robot.commands.limelightCommands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.VisionSubsystem;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.VisionConstants;
import frc.robot.subsystems.DriveSubsystem;
import edu.wpi.first.math.MathUtil;

public class alignXandYLeftCamera extends Command {

    private final VisionSubsystem VISION_SUBSYSTEM; 
    private final DriveSubsystem DRIVE_SUBSYSTEM; 

    private final PIDController strafePID; 
    private final PIDController drivePID; 
    private final PIDController rotationPID; // NEW: Keeps robot locked to target angle

    private final boolean endCommand; 
    private final int setPipelineNumber; 

    private double measuredValueX; 
    private double measuredValueY; 

    private double strafeSpeed; 
    private double driveSpeed;
    private double rotationSpeed; // NEW: Stores rotation correction

    private final double toleranceX;
    private final double toleranceY;

    private final double targetValueX; 
    private final double targetValueY;
    private double targetAngle; // NEW: Stores locked heading

    private boolean inRangeX; 
    private boolean inRangeY; 

    private boolean validTagDetected = false;

    /** Creates a new alignmentCommand. */
    public alignXandYLeftCamera(DriveSubsystem drive, VisionSubsystem vision, int pipeline, boolean end, 
                                double targetOffsetX, double targetOffsetY, double toleranceX, double toleranceY, double heading) { // NEW: Accepts a locked heading from `TurnToAprilTagCommand`
        this.DRIVE_SUBSYSTEM = drive; 
        this.VISION_SUBSYSTEM = vision; 

         this.drivePID = new PIDController(VisionConstants.driveAlignKp,VisionConstants.driveAlignKi, VisionConstants.driveAlignKd); 
        this.strafePID = new PIDController(VisionConstants.strafeAlignKp,VisionConstants.strafeAlignKi, VisionConstants.strafeAlignKd);
        this.rotationPID = new PIDController(VisionConstants.rotAlignKp,VisionConstants.rotAlignKi, VisionConstants.rotAlignKd); // NEW: Controls unintended turning

        this.endCommand = end; 
        this.setPipelineNumber = pipeline; 

        this.targetValueX = targetOffsetX;
        this.targetValueY = targetOffsetY;  

        this.toleranceX = toleranceX; 
        this.toleranceY = toleranceY; 
      
        this.targetAngle = heading; 
        addRequirements(DRIVE_SUBSYSTEM, VISION_SUBSYSTEM);
    }

    @Override
    public void initialize() {
        drivePID.reset();
        strafePID.reset();
        rotationPID.reset(); // NEW: Reset rotation PID

        VISION_SUBSYSTEM.setLeftPipeline(setPipelineNumber);
        inRangeX = false; 
        inRangeY = false; 

        VISION_SUBSYSTEM.setLeftPipeline(0);
        VISION_SUBSYSTEM.setLeftPipeline(0);

        VISION_SUBSYSTEM.setLeftLED(1);
        VISION_SUBSYSTEM.setLeftLED(1);

        driveSpeed = 0;
        strafeSpeed = 0;
        rotationSpeed = 0;
    }

    @Override
    public void execute() {
        if(VISION_SUBSYSTEM.limelightLeftTargetSeen()){
            measuredValueY = VISION_SUBSYSTEM.getLeftTy();
            measuredValueX = VISION_SUBSYSTEM.getLeftTx();  

            // X-direction (Strafing)
            if (Math.abs(targetValueX - measuredValueX) <= toleranceX) { 
                strafeSpeed = 0; 
                inRangeX = true; 
            } else {
                strafeSpeed = strafePID.calculate(measuredValueX, targetValueX);
            }
            strafeSpeed = MathUtil.clamp(strafeSpeed, -0.5, 0.5); // Keep existing limits

            // Y-direction (Forward movement)
            if (Math.abs(targetValueY - measuredValueY) <= toleranceY) { 
                driveSpeed = 0; 
                inRangeY = true; 
            } else {
                driveSpeed = drivePID.calculate(measuredValueY, targetValueY);
            }
            driveSpeed = MathUtil.clamp(driveSpeed, -0.5, 0.5); // Keep existing limits

            SmartDashboard.putNumber("Align Strafe Speed", strafeSpeed); 
            SmartDashboard.putNumber("Align Drive Speed", driveSpeed);

            SmartDashboard.putBoolean("In Range X", inRangeX); 
            SmartDashboard.putBoolean("In Range Y", inRangeY);

            // Drive using corrected values, preventing unnecessary rotation
            // DRIVE_SUBSYSTEM.drive(driveSpeed, 0, rotationSpeed, false);

        } else {
            driveSpeed *= 0.75;
            strafeSpeed *= 0.75;
        }

        
        // NEW: Lock robot to the set angle and prevent unnecessary rotation

        double currentHeading = DRIVE_SUBSYSTEM.getHeading();
        rotationSpeed = rotationPID.calculate(currentHeading, targetAngle);
        rotationSpeed = MathUtil.clamp(rotationSpeed, -0.4, 0.4); // Small corrections only
        
        SmartDashboard.putNumber("Align Rotation Speed", rotationSpeed); // NEW: Monitor rotation correction


        DRIVE_SUBSYSTEM.drive(driveSpeed, strafeSpeed, rotationSpeed, false);
    }

    @Override
    public void end(boolean interrupted) {
        DRIVE_SUBSYSTEM.drive(0, 0, 0, false); // Stop all movement


    }

    @Override
    public boolean isFinished() {
        if(endCommand){
            return true; 
        } else if(inRangeX && inRangeY &&  Math.abs(targetAngle - DRIVE_SUBSYSTEM.getHeading()) < 2.0){
            return true; 
        } else {
            return false; 
        }
    }
}


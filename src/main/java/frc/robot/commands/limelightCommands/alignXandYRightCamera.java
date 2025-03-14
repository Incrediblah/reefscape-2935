// // Copyright (c) FIRST and other WPILib contributors.
// // Open Source Software; you can modify and/or share it under the terms of
// // the WPILib BSD license file in the root directory of this project.


// package frc.robot.commands.limelightCommands;

// import edu.wpi.first.math.controller.PIDController;
// import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import edu.wpi.first.wpilibj2.command.Command;
// import frc.robot.subsystems.VisionSubsystem;
// import frc.robot.subsystems.DriveSubsystem;

// /* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
// public class alignXandYRightCamera extends Command {

  
//   private VisionSubsystem VISION_SUBSYSTEM; 
//   private DriveSubsystem DRIVE_SUBSYSTEM; 

//   private PIDController strafePID; 
//   private PIDController drivePID; 

//   private boolean endCommand; 
//   private int setPipelineNumber; 

//   private double measuredValueX; 
//   private double measuredValueY; 

//   private double strafeSpeed; 
//   private double driveSpeed;

//   private double toleranceX;
//   private double toleranceY;


//   private double targetValueX; 
//   private double targetValueY;


//   private boolean inRangeX; 
//   private boolean inRangeY; 

//   /** Creates a new alignmentCommand. */
//   public alignXandYRightCamera(DriveSubsystem drive, VisionSubsystem vision, int pipeline, boolean end, double targetOffsetX, double targetOffsetY, double toleranceX, double toleranceY) {
//     // Use addRequirements() here to declare subsystem dependencies.
    
//     this.DRIVE_SUBSYSTEM = drive; 
//     this.VISION_SUBSYSTEM = vision; 

//     this.drivePID = new PIDController(0.04, 0, 0); 
//     this.strafePID = new PIDController(0.015, 0, 0.0015); 

//     this.endCommand = end; 
//     this.setPipelineNumber = pipeline; 

//     this.targetValueX = targetOffsetX;
//     this.targetValueY = targetOffsetY;  

//     this.toleranceX = toleranceX; 
//     this.toleranceY = toleranceY; 
  
//     addRequirements(DRIVE_SUBSYSTEM);
//     addRequirements(VISION_SUBSYSTEM);
//   }

//   // Called when the command is initially scheduled.
//   @Override
//   public void initialize() {
//     drivePID.reset();
//     strafePID.reset();
//     VISION_SUBSYSTEM.setRightPipeline(setPipelineNumber);
//     inRangeX = false; 
//     inRangeY = false; 
//   }

//   // Called every time the scheduler runs while the command is scheduled.
//   @Override
//   public void execute() {
    
//     if(VISION_SUBSYSTEM.limelightRightTargetSeen()){
//       measuredValueY = VISION_SUBSYSTEM.getRightTy();
//       measuredValueX = VISION_SUBSYSTEM.getRightTx();  

//       //x-direction 
//       if (Math.abs(targetValueX - measuredValueX) <= toleranceX) { 
//         strafeSpeed = 0; 
//         inRangeX = true; 
//       } 
//       else{
//         strafeSpeed = strafePID.calculate(measuredValueX, targetValueX);
//       }

//       if(strafeSpeed > 0.15){
//         strafeSpeed = 0.15; 
//       }else if(strafeSpeed < -0.15){
//         strafeSpeed = -0.15; 
//       }

//       // y-direction 
//       if (Math.abs(targetValueY - measuredValueY) <= toleranceY) { 
//         driveSpeed = 0; 
//         inRangeY = true; 
//       } 
//       else{
//         driveSpeed = drivePID.calculate(measuredValueY, targetValueY);
//       }
     

//       if(driveSpeed > 0.25){
//         driveSpeed = 0.25; 
//       }else if(driveSpeed < -0.25){
//         driveSpeed = -0.25; 
//       }
      
//     }else{
//       driveSpeed = 0; 
//     }

//     SmartDashboard.putNumber("align speed", driveSpeed); 
//     SmartDashboard.putBoolean("in range x", inRangeX); 
//     SmartDashboard.putBoolean("in range y", inRangeY); 
//     DRIVE_SUBSYSTEM.drive(-driveSpeed, -strafeSpeed, 0, false);

//   }

//   // Called once the command ends or is interrupted.
//   @Override
//   public void end(boolean interrupted) {
//     driveSpeed = 0;
//     strafeSpeed = 0; 
//   }

//   // Returns true when the command should end.
//   @Override
//   public boolean isFinished() {
//     if(endCommand){
//       return true; 
//     }else if(inRangeX && inRangeY){
//       return true; 
//     }
//     else{
//       return false; 
//     }
//   }
// }


package frc.robot.commands.limelightCommands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.VisionSubsystem;
import frc.robot.Constants.VisionConstants;
import frc.robot.subsystems.DriveSubsystem;
import edu.wpi.first.math.MathUtil;

public class alignXandYRightCamera extends Command {

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

    /** Creates a new alignmentCommand. */
    public alignXandYRightCamera(DriveSubsystem drive, VisionSubsystem vision, int pipeline, boolean end, 
                                double targetOffsetX, double targetOffsetY, double toleranceX, double toleranceY) { // NEW: Accepts a locked heading from `TurnToAprilTagCommand`
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
      
        addRequirements(DRIVE_SUBSYSTEM, VISION_SUBSYSTEM);
    }

    @Override
    public void initialize() {
        drivePID.reset();
        strafePID.reset();
        rotationPID.reset(); // NEW: Reset rotation PID

        VISION_SUBSYSTEM.setRightPipeline(setPipelineNumber);
        inRangeX = false; 
        inRangeY = false; 

        targetAngle = DRIVE_SUBSYSTEM.getHeading(); 

        VISION_SUBSYSTEM.setLeftPipeline(1);
        VISION_SUBSYSTEM.setRightPipeline(1);

        VISION_SUBSYSTEM.setLeftLED(1);
        VISION_SUBSYSTEM.setRightLED(1);
    }

    @Override
    public void execute() {
        if(VISION_SUBSYSTEM.limelightRightTargetSeen()){
            measuredValueY = VISION_SUBSYSTEM.getRightTy();
            measuredValueX = VISION_SUBSYSTEM.getRightTx();  

            // X-direction (Strafing)
            if (Math.abs(targetValueX - measuredValueX) <= toleranceX) { 
                strafeSpeed = 0; 
                inRangeX = true; 
            } else {
                strafeSpeed = strafePID.calculate(measuredValueX, targetValueX);
            }
            strafeSpeed = MathUtil.clamp(strafeSpeed, -0.25, 0.25); // Keep existing limits

            // Y-direction (Forward movement)
            if (Math.abs(targetValueY - measuredValueY) <= toleranceY) { 
                driveSpeed = 0; 
                inRangeY = true; 
            } else {
                driveSpeed = drivePID.calculate(measuredValueY, targetValueY);
            }
            driveSpeed = MathUtil.clamp(driveSpeed, -0.15, 0.15); // Keep existing limits

            // NEW: Lock robot to the set angle and prevent unnecessary rotation
            double currentHeading = DRIVE_SUBSYSTEM.getHeading();
            rotationSpeed = rotationPID.calculate(currentHeading, targetAngle);
            rotationSpeed = MathUtil.clamp(rotationSpeed, -0.2, 0.2); // Small corrections only

            SmartDashboard.putNumber("Align Strafe Speed", strafeSpeed); 
            SmartDashboard.putNumber("Align Drive Speed", driveSpeed);
            SmartDashboard.putNumber("Align Rotation Speed", rotationSpeed); // NEW: Monitor rotation correction
            SmartDashboard.putBoolean("In Range X", inRangeX); 
            SmartDashboard.putBoolean("In Range Y", inRangeY);

            // Drive using corrected values, preventing unnecessary rotation
            DRIVE_SUBSYSTEM.drive(driveSpeed, strafeSpeed, rotationSpeed, false);

        } else {
            driveSpeed = 0;
            strafeSpeed = 0;
            rotationSpeed = 0;
        }
    }

    @Override
    public void end(boolean interrupted) {
        DRIVE_SUBSYSTEM.drive(0, 0, 0, false); // Stop all movement

        VISION_SUBSYSTEM.setLeftPipeline(0);
        VISION_SUBSYSTEM.setRightPipeline(0);

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

